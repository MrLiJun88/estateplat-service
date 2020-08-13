package cn.gtmap.estateplat.server.service.core.impl.lq;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">jane</a>
 * @version 1.0, 2016/4/19
 */
@Service
public class LqDyadjServiceImpl extends LqdjService {
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
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcCheckCancelService bdcCheckCancelService;

    /**
     * @param xmxx 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始化不动产登记项目
     */
    @Override
    public void initializeProject(Xmxx xmxx) {
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project;
        String pplx = AppConfig.getProperty("sjpp.type");
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        } else
            throw new AppException(4005);
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        //初始化不动产登记簿 bdcdjb
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
        // 初始化不动产土地属性信息 bdcTd
        BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
        if (bdcTd == null) {
            list.add(intBdcTd(getDjsjNydDcbByBdcdyh(project.getBdcdyh()), project, bdcxm.getQllx()));
        }
        //初始化审批表信息 bdcSpxx
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(project, bdcSpxx);
        if (bdcSpxx != null)
            list.add(bdcSpxx);
        //初始化不动产单元信息 bdcdy
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
        //初始化权利人和义务人信息bdcQlr
        DjsjLqxx djsjLqxx = StringUtils.isNotBlank(project.getDjId()) ? getDjsjLqxx(project.getDjId()) : null;
        List<BdcQlr> bdcYwrList = initBdcQlrFromDjsj(project, djsjLqxx, Constants.QLRLX_YWR);
        //过程匹配，则权利人和义务人都不需要改变
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
            List<BdcQlr> qlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(qlrList))
                list.addAll(qlrList);
        } else {
            //不是过渡数据执行
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList)
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
                List<BdcQlr> tempBdcYwrs = new ArrayList<BdcQlr>();
                for (BdcQlr bdcQlr : ybdcQlrList) {
                    bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                    tempBdcYwrs.add(bdcQlr);
                }
                bdcYwrList = tempBdcYwrs;
            }
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                list.addAll(bdcYwrList);
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
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
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
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 更新不动产项目上一手登记项目的权属状态（包括过渡数据权属状态）
     */
    private void changeYqszt(BdcXm bdcXm) {
        //抵押登记不需要修改原权利状态    如果为老数据   注销过渡数据的权利状态
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            changeYgQszt(bdcXm);
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx())) {
                    changeGdsjQszt(bdcXmRel, 1);
                }
            }
        }

    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
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
                //删除权利类型信息(抵押)
                Example example = new Example(BdcDyaq.class);
                example.createCriteria().andEqualTo("proid", proid);
                entityMapper.deleteByExample(BdcDyaq.class, example);
                //为了删除项目的时候清楚唯一的不动产单元数据
                bdcdyService.delDjbAndTd(bdcXm);
            }
        }
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 还原上一手权利信息（包括上一手过渡数据），包括权利状态等，主要用于删除项目，撤销或退回项目
     */
    @Override
    public void revertYqlxx(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                //还原过渡权属状态
                List<BdcXmRel> bdcXmRelList = null;
                if (bdcXm != null) {
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                }
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        revertGdsjQszt(bdcXmRel, 0);
                    }
                }

            }
        }
    }


    /**
     * @param bdcXm 不动产项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始化抵押权利信息
     */
    private void saveQllx(BdcXm bdcXm) {
        BdcDyaq bdcDyaq = new BdcDyaq();
        HashMap map = new HashMap();
        map.put("proid", bdcXm.getProid());
        List<BdcDyaq> dyaqList = bdcDyaqService.queryBdcDyaq(map);
        BdcDyaq bdcDyaqTemp = null;
        if (CollectionUtils.isNotEmpty(dyaqList)) {
            bdcDyaqTemp = dyaqList.get(0);
        }
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        BdcXmRel bdcXmRel = null;
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            bdcXmRel = bdcXmRelList.get(0);
        }
        if (StringUtils.isBlank(bdcDyaq.getQlid())) {
            bdcDyaq.setQlid(UUIDGenerator.generate18());
        }
        qllxService.getQllxParentFrom(bdcDyaq, bdcXm);
        if (bdcXmRel != null) {
            //抵押转移
            if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZGDY_SQLXDM, bdcXm.getSqlx())) {
                bdcDyaq.setDyfs(Constants.DYFS_ZGEDY);
            }else {
                bdcDyaq.setDyfs(Constants.DYFS_YBDY);
            }
            //读取过渡抵押数据
            List<GdDy> gdDyList = gdFwService.getGdDyListByGdproid(bdcXmRel.getYproid(), 0);
            if (CollectionUtils.isNotEmpty(gdDyList)) {
                GdDy gdDy = gdDyList.get(0);
                gdDy = bdcCheckCancelService.getGdDyFilterZdsj(gdDy);
                bdcDyaq = gdFwService.readBdcDyaqFromGdDy(gdDy, bdcDyaq, null);
            }

        }
        if (bdcDyaq != null) {
            if (bdcDyaqTemp == null) {
                entityMapper.insertSelective(bdcDyaq);
            } else {
                bdcDyaq.setQlid(bdcDyaqTemp.getQlid());
                entityMapper.updateByPrimaryKeySelective(bdcDyaq);
            }
        }
    }

    @Override
    public String getProjectCode() {
        return "9991201";
    }
}
