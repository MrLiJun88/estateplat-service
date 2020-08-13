package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.GdCfService;
import cn.gtmap.estateplat.server.core.service.GdDyhRelService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
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
 * @version 1.0, 2016/10/19
 * @description 不动产单元预查封验证服务
 */
public class BdcdyYcfValidateServiceImpl implements ProjectValidateService{

    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private GdDyhRelService gdDyhRelService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            List<QllxParent> list = qllxParentService.queryLogcfQllxVo(new BdcCf(), project.getBdcdyh(), "", "true");
            if (CollectionUtils.isNotEmpty(list)) {
                proidList = new ArrayList<String>();
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
                                if(StringUtils.equals(StringUtils.deleteWhitespace(gdCf.getCflx()),Constants.CFLX_GD_YCF)) {
                                    proidList.add(gdCf.getProid());
                                }
                            }
                        }
                        if(StringUtils.isNotBlank(gdDyhRel.getTdid())) {
                            List<GdCf> gdCfListTemp = gdCfService.getGdCfListByBdcid(gdDyhRel.getTdid(),Constants.GDQL_ISZX_WZX);
                            if(CollectionUtils.isNotEmpty(gdCfListTemp)) {
                                for(GdCf gdCf:gdCfListTemp) {
                                    if(StringUtils.equals(StringUtils.deleteWhitespace(gdCf.getCflx()),Constants.CFLX_GD_YCF)) {
                                        proidList.add(gdCf.getProid());
                                    }
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
                                    if(StringUtils.equals(StringUtils.deleteWhitespace(gdCf.getCflx()),Constants.CFLX_GD_YCF)) {
                                        proidList.add(gdCf.getProid());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }else {
            map.put("info", null);
        }
        return map;
    }

    @Override
    public String getCode() {
        return "128";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元是否预查封";
    }
}
