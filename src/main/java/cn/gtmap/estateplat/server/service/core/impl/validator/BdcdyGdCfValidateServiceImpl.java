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
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 过渡房屋是否存在查封登记
 */
public class BdcdyGdCfValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdTdService gdTdService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();

        if (project!=null&&StringUtils.isNotBlank(project.getGdproid())) {
            if(project.getGdproid().contains("、")){
                String[] gdProids = project.getGdproid().split("、");
                if(gdProids != null && gdProids.length > 0){
                    for(String s : gdProids){
                        validateFromGdThroughBdcidByGdproid(proidList, s);
                    }
                }
            }else {
                validateFromGdThroughBdcidByGdproid(proidList, project.getGdproid());
            }
        }
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 考虑匹配界面查封可能不匹配不动产单元创建，下面代码则是判断没有匹配不动产单元验证是否有查封
         */
        if (CollectionUtils.isEmpty(proidList) && project != null && StringUtils.isNotBlank(project.getGdproid())) {
            if(project.getGdproid().contains("、")){
                String[] gdProids = project.getGdproid().split("、");
                if(gdProids != null && gdProids.length > 0){
                    for(String s : gdProids){
                        validateFromNoBdcdyhByGdproid(proidList, s);
                    }
                }
            }else {
                validateFromNoBdcdyhByGdproid(proidList, project.getGdproid());
            }
        }
        if(CollectionUtils.isEmpty(proidList)){
            //判断已匹配的房屋土地证是否被查封
            String gdProid = project.getGdproid();
            if (StringUtils.isNotBlank(gdProid)) {
                if(project.getGdproid().contains("、")){
                    String[] gdProids = project.getGdproid().split("、");
                    if(gdProids != null && gdProids.length > 0){
                        for(String s : gdProids){
                            validateFromGdByGdproid(proidList, s);
                        }
                    }
                }else {
                    validateFromGdByGdproid(proidList, project.getGdproid());
                }
            }
        }
        if(CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }else {
            map.put("info", null);
        }
        return map;
    }


    private void validateFromGdThroughBdcidByGdproid(List<String> proidList, String gdproid){
        List<String> bdcidList = gdXmService.getGdBdcidByProid(gdproid);
        if (CollectionUtils.isNotEmpty(bdcidList)) {
            for(String bdcid:bdcidList) {
                if (StringUtils.isNotBlank(bdcid)) {
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 重新写了g根据不动产id查询过度查封、抵押
                     */
                    GdCf gdCf = (GdCf) gdXmService.queryDyAndCfByBdcid(bdcid, new GdCf(), 0);
                    if (gdCf != null && (StringUtils.equals(gdCf.getIssx(), Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(gdCf.getIssx())) && (!StringUtils.equals(gdCf.getIscd(), Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(gdCf.getIscd()))) {
                        proidList.add(gdCf.getCfid());
                    }
                }
            }
        }
    }

    private void validateFromNoBdcdyhByGdproid(List<String> proidList, String gdproid){
        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdproid(gdproid);
        for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
            if (StringUtils.isBlank(bdcGdDyhRel.getBdcdyh())) {
                HashMap map1 = new HashMap();
                map1.put("yproid", gdproid);
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelByYproidAndBdcdyh(map1);
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRel.getProid());
                        if (bdcCf != null && bdcCf.getJfdjsj() == null && (StringUtils.equals(bdcCf.getIssx(),Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(bdcCf.getIssx())) && (!StringUtils.equals(bdcCf.getIscd(),Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(bdcCf.getIscd()))) {
                            proidList.add(bdcCf.getQlid());
                        }
                    }
                }
            }
        }
    }

    private void validateFromGdByGdproid(List<String> proidList, String gdproid){
        String qlids = gdFwService.getGdFwQlidsByProid(gdproid);
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
                                    //查询gd_cf
                                    GdCf gdCf = (GdCf) gdXmService.queryDyAndCfByBdcid(bdcGdDyhRel.getTdid(), new GdCf(), 0);
                                    if (gdCf != null && (StringUtils.equals(gdCf.getIssx(),Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(gdCf.getIssx())) && (!StringUtils.equals(gdCf.getIscd(),Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(gdCf.getIscd()))) {
                                        proidList.add(gdCf.getCfid());
                                    }

                                    //查询bdc_cf
                                    Example exampleXmRel = new Example(BdcXmRel.class);
                                    List<GdTdsyq> gdTdsyqList = gdTdService.queryTdsyqByTdid(bdcGdDyhRel.getTdid());

                                    if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                                        for (GdTdsyq gdTdsyq : gdTdsyqList) {
                                            if (gdTdsyq.getQlid() != null) {
                                                exampleXmRel.createCriteria().andEqualTo("yqlid", gdTdsyq.getQlid());
                                                List<BdcXmRel> bdcXmRel = entityMapper.selectByExample(BdcXmRel.class, exampleXmRel);
                                                if (CollectionUtils.isNotEmpty(bdcXmRel)) {
                                                    for (BdcXmRel bdcXmRel1 : bdcXmRel) {
                                                        BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRel1.getProid());
                                                        if (bdcCf != null && bdcCf.getQszt() != Constants.QLLX_QSZT_HR && (StringUtils.equals(bdcCf.getIssx(),Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(bdcCf.getIssx())) && (!StringUtils.equals(bdcCf.getIscd(),Constants.QLLX_QSZT_XS.toString()) || StringUtils.isBlank(bdcCf.getIscd()))) {
                                                            proidList.add(bdcCf.getQlid());
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

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "102";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "过渡房屋是否存在查封登记";
    }
}
