package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 更正登记转发服务  继承变更登记
 * 此种登记类型在创建项目时不能确定权利类型  所以在两次选择不动产权证（明）的情况下
 * 生成的权利类型不一致   应该先删除当前项目不同种类的权利
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-8
 */
public class TurnProjectGzdjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    private GdFwService gdFwService;
    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;
        qllxVo = qllxService.makeSureQllx(bdcXm);
        String hzFj = "";
        //zdd 此处代码解决一个项目  多次选择不同种类的不动产信息时  上一次生成的权利信息未删除问题
        List<QllxParent> qllxParentList = qllxService.queryQllxByProid(bdcXm.getProid());
        if(CollectionUtils.isNotEmpty(qllxParentList)) {
            for(int i = 0; i < qllxParentList.size(); i++) {
                QllxParent qllxVotemp = qllxParentList.get(i);
                if(StringUtils.isNotBlank(qllxVotemp.getQllx())) {
                    QllxVo hisQllx = qllxService.makeSureQllx(qllxVotemp.getQllx());
                    if(!hisQllx.getClass().equals(qllxVo.getClass()))
                        qllxService.delQllxByproid(hisQllx, bdcXm.getProid());
                }
            }
        }

        QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
        //zdd 转移登记 需要继承原权利信息
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        for(BdcXmRel bdcXmRel : bdcXmRelList) {
            qllxVo = qllxService.makeSureQllx(bdcXm);
            if(bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                List<QllxVo> yqllxList = qllxService.queryQllx(bdcXm);
                QllxVo yqllxVo = null;
                if(CollectionUtils.isNotEmpty(yqllxList)) {
                    yqllxVo = yqllxList.get(0);
                }
                if(yqllxVo == null) {
                    yqllxVo = qllxService.makeSureQllx(bdcXm);
                }
                yqllxVo.setQlid(UUIDGenerator.generate18());
                yqllxVo.setProid(bdcXm.getProid());
                yqllxVo.setYwh(bdcXm.getBh());
                yqllxVo.setDbr(null);
                yqllxVo.setDjsj(null);
                //zdd 不应该继承原来项目的附记
                //对于换证流程，要把原业务中的附记内容带入到新建业务的附记中
                if(StringUtils.isNotBlank(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_HZ_DM)){
                    yqllxVo.setFj("");
                }
                if(StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.isNotBlank(bdcXmRel.getYqlid()) && StringUtils.isNotBlank(bdcXmRel.getProid()) && StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_HZ_DM)) {
                    String yqlid = bdcXmRel.getYqlid();
                    String yproid = bdcXmRel.getYproid();
                    List<GdTdsyq> gdTdsyqList = gdFwService.queryTdsyqByQlid(yqlid);
                    List<GdFwsyq> gdFwsyqList = gdFwService.getGdFwsyqListByGdproid(yproid, 0);
                    if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                        GdFwsyq gdFwsyq = gdFwsyqList.get(0);
                        hzFj = gdFwsyq.getFj();
                    }else if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                        GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                        hzFj = gdTdsyq.getFj();
                    }
                }
                yqllxVo.setQszt(0);
                //zdd 前后的权利类型相同
                yqllxVo.setQszt(0);
                qllxVo = yqllxVo;
            }

            qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
            if(StringUtils.isNotBlank(hzFj)){
                qllxVo.setFj(hzFj);
            }
            if(qllxVo != null) {
                if(qllxVoTemp == null) {
                    entityMapper.insertSelective(qllxVo);
                } else {
                    qllxVo.setQlid(qllxVoTemp.getQlid());
                    entityMapper.updateByPrimaryKeySelective(qllxVo);
                }

                //hqz（房地产权登记信息（项目内多幢房屋））获取原权利登记信息里的项目列表并继承到当前权利登记信息里
                if(qllxVo instanceof BdcFdcqDz) {
                    BdcFdcqDz fdcq = (BdcFdcqDz) qllxVo;
                    List<BdcFwfzxx> fwfzxxs = fdcq.getFwfzxxList();
                    if(CollectionUtils.isNotEmpty(fwfzxxs) &&
                            !StringUtils.equals(fwfzxxs.get(0).getQlid(), qllxVo.getQlid())) {
                        for(BdcFwfzxx fwfzxx : fwfzxxs) {
                            fwfzxx.setQlid(qllxVo.getQlid());
                            if(qllxVoTemp == null) {
                                fwfzxx.setFzid(UUIDGenerator.generate());
                            }
                            entityMapper.saveOrUpdate(fwfzxx, fwfzxx.getFzid());
                        }
                    }
                }
            }
            break;
        }
        //转移登记、变更登记、更正登记、换证登记不继承交易价格
        qllxService.noInheritJyjgByQllxVo(qllxVo);
        return qllxVo;
    }

    @Override
    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcZs> list = new ArrayList<BdcZs>();
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        List<BdcZs> bdcDyZsList = new ArrayList<BdcZs>();
        List<BdcXm> bdcXmList;
        if(bdcXm != null && org.apache.commons.lang.StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if(CollectionUtils.isNotEmpty(bdcXmList)) {
                for(BdcXm bdcxmTemp : bdcXmList) {
                    if(bdcxmTemp.getSqlx().equals(Constants.SQLX_DY_GDDY)) {
                        bdcDyZsList = super.saveBdcZs(bdcxmTemp,previewZs);
                        //若是带抵押的过渡项目，生成证书时bdcqzh保留为原过渡的他项证号
                        for(BdcZs bdcZs : bdcDyZsList) {
                            List<BdcXmRel> xmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcxmTemp.getProid());
                            if(CollectionUtils.isNotEmpty(xmRelList)) {
                                String qlid = xmRelList.get(0).getYqlid();
                                if(StringUtils.isNotBlank(qlid)) {
                                    GdDy gdDy = gdTdService.getGddyqByQlid(qlid,null);
                                    if(null != gdDy) {
                                        bdcZs.setBdcqzh(gdDy.getDydjzmh());
                                        bdcZs.setZhlsh("");
                                        entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                                    }
                                }
                            }
                        }
                    } else {
                        bdcZsList = super.saveBdcZs(bdcxmTemp,previewZs);
                    }
                }
                list.addAll(bdcDyZsList);
                list.addAll(bdcZsList);
            }
        }
        return list;
    }

}
