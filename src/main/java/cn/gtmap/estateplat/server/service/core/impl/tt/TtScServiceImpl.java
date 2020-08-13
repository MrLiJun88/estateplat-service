package cn.gtmap.estateplat.server.service.core.impl.tt;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcTdcbnydsyqMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * 国有水产养殖权首次登记
 * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
 * @version V1.0, 17-02-10
 */
@Service
public class TtScServiceImpl extends TtdjService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcTdcbnydsyqMapper bdcTdcbnydsyqMapper;
    @Autowired
    private BdcdyService bdcdyService;

    /**
     * @param xmxx 项目信息
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 初始化不动产登记项目
     */
    @Override
    public void initializeProject(Xmxx xmxx) {
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project = null;
        if (xmxx instanceof Project)
            project = (Project) xmxx;
        else
            throw new AppException(4005);
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        //初始化bdcdjb
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(project);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }
        }
        if(bdcdjb!=null&&StringUtils.isNotBlank(bdcdjb.getZl())){
            bdcxm.setZl(bdcdjb.getZl());
        }
        // 初始化bdcTd
        BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
        if (bdcTd == null) {
            list.add(intBdcTd(getDjsjNydDcbByBdcdyh(project.getBdcdyh()), project, bdcxm.getQllx()));
        }
        //初始化 bdcSpxx
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(project, bdcSpxx);
        if (bdcSpxx != null)
            list.add(bdcSpxx);
        //初始化 bdcdy
        BdcBdcdy bdcdy = initBdcdy(project, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
        }
        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);
        //考虑多个权利人情况
        BdcQlr bdcQlr = null;
        List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
        if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
            bdcQlrService.delBdcQlrByProid(project.getProid());
        }
        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcQlrList = initBdcQlrFromDjsj(project, getDjsjNyd(project.getDjId()), Constants.QLRLX_QLR);
        //zdd 初始登记选择老数据（房产证，土地证，林权证，林权）
        List<BdcQlr> ybdcQlrList = null;
        project = (Project) xmxx;
        ybdcQlrList = getYbdcQlrList(project);
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> oldbdcQlrs = new ArrayList<BdcQlr>();
            for (BdcQlr ybdcQlr : ybdcQlrList) {
                //zdd 即使选择老数据  权利人依然为老证的权利人
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(ybdcQlr, null, project.getProid());
                oldbdcQlrs.add(bdcQlr);
            }
            bdcQlrList = oldbdcQlrs;
        }
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            list.addAll(bdcQlrList);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            saveOrUpdateProjectDate(list);
        }
        //初始化权利信息
        if (bdcxm != null){
            saveQllx(bdcxm);
        }
    }

    /**
     * @param bdcXm 不动产项目信息
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 水产养殖权
     */
    private void saveQllx(BdcXm bdcXm) {
        BdcTdcbnydsyq tdcbnydsyq = new BdcTdcbnydsyq();
        BdcTdcbnydsyq bdcTtTemp = bdcTdcbnydsyqMapper .getBdcTdcbnydsyq(bdcXm.getProid());
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        BdcXmRel bdcXmRel = null;
        if (CollectionUtils.isNotEmpty(bdcXmRelList))
            bdcXmRel = bdcXmRelList.get(0);
        if (StringUtils.isBlank(tdcbnydsyq.getQlid()))
            tdcbnydsyq.setQlid(UUIDGenerator.generate18());
        qllxService.getQllxParentFrom(tdcbnydsyq, bdcXm);
        if (bdcXmRel != null&&StringUtils.isNotBlank(bdcXmRel.getQjid())) {
            DjsjNydDcb djsjNydDcb = bdcDjsjService.getDjsjTtxx(bdcXmRel.getQjid());
            tdcbnydsyq = qllxService.getBdcTtFromTtxx(tdcbnydsyq, djsjNydDcb);
        }
        if (tdcbnydsyq != null) {
            if (bdcTtTemp == null) {
                entityMapper.insertSelective(tdcbnydsyq);
            } else {
                tdcbnydsyq.setQlid(bdcTtTemp.getQlid());
                entityMapper.updateByPrimaryKeySelective(tdcbnydsyq);
            }
        }
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 删除不动产登记项目权利信息，各权利删除对应的信息有所不同
     */
    @Override
    public void deleteProjectQlxx(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                String proid = bdcXm.getProid();
                //删除权利类型信息(水产养殖)
                Example example = new Example(BdcLq.class);
                example.createCriteria().andEqualTo("proid", proid);
                entityMapper.deleteByExample(BdcTdcbnydsyq.class, example);
                //为了删除项目的时候清楚唯一的不动产单元数据
                bdcdyService.delDjbAndTd(bdcXm);
            }
        }
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 还原上一手权利信息（包括上一手过渡数据），包括权利状态等，主要用于删除项目，撤销或退回项目
     */
    @Override
    public void revertYqlxx(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                //还原过渡权属状态
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        revertGdsjQszt(bdcXmRel, 0);
                    }
                }
            }
        }
    }

    /**
     * @param wiid
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 更新项目权属状态，包括本次项目权利状态、上一手权属状态（包括过渡数据权属状态）
     */
    @Override
    public void updateProjectQszt(String wiid) throws Exception {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                changeYqszt(bdcXm);
            }
        }
    }

    /**
     * @param bdcXm 工作流项目ID
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 更新不动产项目上一手登记项目的权属状态（包括过渡数据权属状态）
     */
    public void changeYqszt(BdcXm bdcXm) throws Exception {
        if (bdcXm == null)
            throw new NullPointerException();
        //zdd 如果存在过渡数据  需要注销过渡权属状态
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx()))
                    changeGdsjQszt(bdcXmRel, 1);
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    changeYgQszt(bdcXm);
                }
            }
            bdcTdService.changeGySjydZt(bdcXm,new ArrayList<String>());
        }
        //预告查封转查封
        String ycfTurnToCfEnable = StringUtils.deleteWhitespace(AppConfig.getProperty("ycfTurnToCf.enable"));
        if(StringUtils.equals(ycfTurnToCfEnable,ParamsConstants.TRUE_LOWERCASE)) {
            List<BdcBdcdy> bdcdyList = null;
            if (StringUtils.isNotBlank(bdcXm.getWiid()))
                //参数修改为wiid @gtsy
                bdcdyList = bdcdyService.queryBdcBdcdy(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcdyList)) {
                //防止商品房首次登记只查封一个bdcdyh的情况
                for (BdcBdcdy bdcdy : bdcdyList) {
                    if (StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
                        List<BdcCf> bdcCfList = bdcCfService.queryYcfByBdcdyh(bdcdy.getBdcdyh());
                        if (CollectionUtils.isNotEmpty(bdcCfList)) {
                            bdcCfService.ycfChangeCf(bdcXm, bdcCfList.get(0));
                        }
                    }
                }
            }
        }
    }
}
