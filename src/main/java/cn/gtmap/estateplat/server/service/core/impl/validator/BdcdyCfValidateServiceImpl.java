package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
import cn.gtmap.estateplat.server.core.mapper.GdFwsyqMapper;
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
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/29
 * @description 不动产单元查封验证服务
 */
public class BdcdyCfValidateServiceImpl  implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性，使用不动产单元验证不动产登记库
     */

    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private BdcComplexFgHbHzService bdcComplexFgHbHzService;
    @Autowired
    private GdDyhRelService gdDyhRelService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        List<QllxParent> list = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            proidList = new ArrayList<String>();
            //jiangganzhi 土地分割、合并换证登记，分割前的证书已经查封了，选择分割后的不动产单元进行登记需要验证查封
            if(StringUtils.isNotBlank(project.getSqlx()) && StringUtils.equals(project.getSqlx(), Constants.SQLX_TDFGHBHZ_DM)) {
                list = bdcComplexFgHbHzService.getYzdCf(project.getBdcdyh());
            }

            if(CollectionUtils.isEmpty(list)) {
                list = qllxParentService.queryLogcfQllxVo(new BdcCf(), project.getBdcdyh(), "", "false");
            }
            if (CollectionUtils.isNotEmpty(list)) {
                for (QllxParent qllxParent : list) {
                    proidList.add(qllxParent.getProid());
                }
            }else{
                List<GdDyhRel> gdDyhRelList = gdDyhRelService.queryGdDyhRelListByBdcdyh(project.getBdcdyh());
                if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                    for(GdDyhRel gdDyhRel:gdDyhRelList) {
                        List<GdCf> gdCfList = gdCfService.getGdCfListByBdcid(gdDyhRel.getGdid(),Constants.GDQL_ISZX_WZX);
                        if(CollectionUtils.isNotEmpty(gdCfList)) {
                            for(GdCf gdCf:gdCfList) {
                                proidList.add(gdCf.getProid());
                            }
                        }
                        if(StringUtils.isNotBlank(gdDyhRel.getTdid())) {
                            List<GdCf> gdCfListTemp = gdCfService.getGdCfListByBdcid(gdDyhRel.getTdid(),Constants.GDQL_ISZX_WZX);
                            if(CollectionUtils.isNotEmpty(gdCfListTemp)) {
                                for(GdCf gdCf:gdCfListTemp) {
                                    proidList.add(gdCf.getProid());
                                }
                            }
                        }
                    }
                }else{
                    List<String> fwidList = gdFwService.getFwidByBdcdyh(project.getBdcdyh());
                    if(CollectionUtils.isNotEmpty(fwidList)) {
                        for(String fwid:fwidList) {
                            List<GdCf> gdCfList = gdCfService.getGdCfListByBdcid(fwid,Constants.GDQL_ISZX_WZX);
                            if(CollectionUtils.isNotEmpty(gdCfList)) {
                                for(GdCf gdCf:gdCfList) {
                                    proidList.add(gdCf.getProid());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }else{
            map.put("info", null);
        }
        return map;
    }

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "101";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否查封";
    }
}
