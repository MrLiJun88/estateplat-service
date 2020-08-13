package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
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
 * @description 带抵押转移，必须存在抵押情况
 */
public class BdcdyCreatDDyzyValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            //查找正式库的抵押数据
            List<QllxParent> bdcDyaqList = null;
            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                HashMap queryMap = new HashMap();
                queryMap.put("bdcdyh", project.getBdcdyh());
                queryMap.put("qszt", Constants.QLLX_QSZT_XS);
                bdcDyaqList = qllxParentService.queryQllxVo(new BdcDyaq(), queryMap);
            }
            //查过过渡库的抵押数据
            List<GdDy> gdDyList = null;
            if (StringUtils.isNotBlank(project.getGdproid()) || StringUtils.isNotBlank(project.getYxmid())) {
                List<GdFwsyq> gdFwsyqList = gdFwService.queryGdFwsyqByGdproid(project.getGdproid());
                if (CollectionUtils.isEmpty(gdFwsyqList)) {
                    gdFwsyqList = gdFwService.queryGdFwsyqByGdproid(project.getYxmid());
                }
                if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                    gdDyList = new ArrayList<GdDy>();
                    if (StringUtils.isNotBlank(gdFwsyqList.get(0).getQlid())) {
                        List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid("", gdFwsyqList.get(0).getQlid());
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                            for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                                List<GdBdcQlRel> gdBdcQlRelListTemp = gdFwService.getGdBdcQlRelByBdcidOrQlid(gdBdcQlRel.getBdcid(), "");
                                if (CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)) {
                                    for (GdBdcQlRel gdBdcQlRelTemp : gdBdcQlRelListTemp) {
                                        GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRelTemp.getQlid(), Constants.GDQL_ISZX_WZX);
                                        if (gdDy != null) {
                                            gdDyList.add(gdDy);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isEmpty(bdcDyaqList) && CollectionUtils.isEmpty(gdDyList)) {
                map.put("info", "false");
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "129";
    }

    @Override
    public String getDescription() {
        return "带抵押转移，必须存在抵押情况";
    }
}
