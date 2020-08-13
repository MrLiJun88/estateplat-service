package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/20
 * @description
 */
public class BdcdyGdFwDyValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcCheckCancelService bdcCheckCancelService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project= (Project)param.get("project");
        if (project == null) {
            return map;
        }
        List<String> proidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(project.getGdproid())) {
            List<String> bdcidList = gdXmService.getGdBdcidByProid(project.getGdproid());
            if (CollectionUtils.isNotEmpty(bdcidList)) {
                for (String bdcid:bdcidList) {
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 重新写了g根据不动产id查询过度查封、抵押
                     */
                    GdDy gdDy = (GdDy) gdXmService.queryDyAndCfByBdcid(bdcid, new GdDy(), 0);
                    //他项证换证不提示
                    if (null != gdDy && !StringUtils.equals(gdDy.getDyid(),project.getGdproid())) {
                        proidList.add(gdDy.getDyid());
                    }

                    /**
                     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
                     * @description 纯土地证匹配了房屋的不动产单元，验证房屋抵押情况
                     */
                    List<BdcGdDyhRel> fwGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(bdcid);
                    if (CollectionUtils.isNotEmpty(fwGdDyhRelList)) {
                        for (BdcGdDyhRel bdcGdDyhRel : fwGdDyhRelList) {
                            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcGdDyhRel.getBdcdyh());
                            if (StringUtils.isNotBlank(bdcdyid)) {
                                List bdcQlList = bdcCheckCancelService.getQlListByBdcdyid(bdcdyid, Constants.QLLX_QSZT_XS);
                                if (CollectionUtils.isNotEmpty(bdcQlList)) {
                                    for (Object bdcQlxx : bdcQlList) {
                                        if (bdcQlxx instanceof ArrayList) {
                                            for (Object bdcQlxx1 : (ArrayList) bdcQlxx) {
                                                if (bdcQlxx1 instanceof BdcDyaq) {
                                                    proidList.add(((BdcDyaq) bdcQlxx1).getQlid());
                                                }
                                            }
                                        } else {
                                            if (bdcQlxx instanceof BdcDyaq) {
                                                proidList.add(((BdcDyaq) bdcQlxx).getQlid());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 考虑匹配界面查封可能不匹配不动产单元创建，下面代码则是判断没有匹配不动产单元验证是否有抵押
         */
        if (CollectionUtils.isEmpty(proidList) && StringUtils.isNotBlank(project.getGdproid())) {
            HashMap map1 = new HashMap();
            map1.put("yproid", project.getGdproid());
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelByYproidAndBdcdyh(map1);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryYdyaqByProid(bdcXmRel.getProid(), null);
                    if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                        for (BdcDyaq bdcDyaq : bdcDyaqList) {
                            if (bdcDyaq.getQszt() != null && !Constants.QLLX_QSZT_LS.equals(bdcDyaq.getQszt())) {
                                proidList.add(bdcDyaq.getProid());
                            }
                        }
                    }
                }
            }
        }
        String gdProid = project.getGdproid();
        if (StringUtils.isNotBlank(gdProid)) {
            String qlids = gdFwService.getGdFwQlidsByProid(gdProid);
            if (StringUtils.isNotBlank(qlids)) {
                String[] arrayQlid = qlids.split(",");
                for (String qlid : arrayQlid) {
                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                            if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                                for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                                    if(StringUtils.isNotBlank(bdcGdDyhRel.getTdid())) {
                                        //查询gd_dy
                                        GdDy gdDy = (GdDy) gdXmService.queryDyAndCfByBdcid(bdcGdDyhRel.getTdid(), new GdDy(), Constants.GDQL_ISZX_WZX);
                                        if (gdDy != null) {
                                            proidList.add(gdDy.getDyid());
                                        }

                                        //查询bdc_dy
                                        Example exampleXmRel = new Example(BdcXmRel.class);
                                        List<GdTdsyq> gdTdsyqList = gdTdService.queryTdsyqByTdid(bdcGdDyhRel.getTdid());

                                        if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                                            for (GdTdsyq gdTdsyq : gdTdsyqList) {
                                                if (gdTdsyq.getQlid() != null) {
                                                    exampleXmRel.createCriteria().andEqualTo("yqlid", gdTdsyq.getQlid());
                                                    List<BdcXmRel> bdcXmRelList = entityMapper.selectByExample(BdcXmRel.class, exampleXmRel);
                                                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                                        for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                                            BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(bdcXmRel.getProid());
                                                            if (bdcDyaq != null && bdcDyaq.getQszt() != Constants.QLLX_QSZT_HR) {
                                                                proidList.add(bdcDyaq.getQlid());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList.get(0));
        }else {
            map.put("info", null);
        }
        return map;
    }

    @Override
    public String getCode() {
        return "107";
    }

    @Override
    public String getDescription() {
        return "验证过渡房屋 是否处于抵押状态";
    }
}
