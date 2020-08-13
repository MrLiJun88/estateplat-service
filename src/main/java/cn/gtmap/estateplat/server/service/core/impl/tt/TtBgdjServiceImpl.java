package cn.gtmap.estateplat.server.service.core.impl.tt;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcTdcbnydsyqMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * 国有水产养殖权变更登记
 * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
 * @version V1.0, 17-02-21
 */
@Service
public class TtBgdjServiceImpl extends TtdjService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private BdcTdcbnydsyqMapper bdcTdcbnydsyqMapper;

    /**
     * @param xmxx 项目信息
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 初始化不动产登记项目
     */
    @Override
    public void initializeProject(Xmxx xmxx) {
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project = null;
        List<BdcQlr> ybdcQlrList = null;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        } else
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
            } else
                bdcdjb = bdcdjbTemps.get(0);
        }
        if(bdcdjb != null&&StringUtils.isNotBlank(bdcdjb.getZl())){
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
        //初始化bdcQlr
        List<BdcQlr> tempBdcQlrList = null;
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList)
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            saveOrUpdateProjectDate(list);
        }
        //初始化权利信息
        if (bdcxm != null) {
            saveQllx(bdcxm);
        }
    }

    /**
     * @param wiid
     * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
     * @description 更新项目权属状态，包括本次项目权利状态、上一手权属状态（包括过渡数据权属状态）
     */
    @Override
    public void updateProjectQszt(String wiid) {
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
    private void changeYqszt(BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            changeYgQszt(bdcXm);
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                changeGdsjQszt(bdcXmRel, 1);
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
                //删除义务人证书关系信息表以及义务人信息
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
                if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                    for (BdcQlr bdcYwr : bdcYwrList) {
                        bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcYwr.getQlrid());
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid());
                    }
                }
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
                List<BdcXmRel> bdcXmRelList = null;
                if (bdcXm != null)
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        //还原对应的过渡数据权属状态
                        revertGdsjQszt(bdcXmRel, 0);
                        //还原上一手权属整体
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid()))
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                    }
                }
            }
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
}
