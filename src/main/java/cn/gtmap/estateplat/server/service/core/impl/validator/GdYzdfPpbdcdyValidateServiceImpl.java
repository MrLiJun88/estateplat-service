package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.GdBdcQlRel;
import cn.gtmap.estateplat.model.server.core.GdDyhRel;
import cn.gtmap.estateplat.model.server.core.GdFwsyq;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.GdBdcQlRelService;
import cn.gtmap.estateplat.server.core.service.GdDyhRelService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/3
 * @description 验证一证多房的房产证是否匹配不同的不动产单元 (若是则走分割流程)
 */
public class GdYzdfPpbdcdyValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private GdDyhRelService gdDyhRelService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(project.getYqlid())) {
            GdFwsyq gdFwsyq =  gdFwService.getGdFwsyqByQlid(project.getYqlid());
            if(gdFwsyq != null) {
                List<GdDyhRel> gdDyhRelList = new ArrayList<GdDyhRel>();
                List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdFwsyq.getQlid());
                if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                    for (GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                        List<GdDyhRel> gdDyhRelListTemp = gdDyhRelService.queryGdDyhRelListByBdcid(gdBdcQlRel.getBdcid());
                        if(CollectionUtils.isNotEmpty(gdDyhRelListTemp)) {
                            gdDyhRelList.addAll(gdDyhRelListTemp);
                        }
                    }
                }
                if(gdBdcQlRelList.size() > 1) {
                    List<String> bdcdyhList = new ArrayList<String>();
                    for(GdDyhRel gdDyhRel:gdDyhRelList) {
                        if(StringUtils.isNotBlank(gdDyhRel.getBdcdyh()) && !bdcdyhList.contains(gdDyhRel.getBdcdyh())) {
                            bdcdyhList.add(gdDyhRel.getBdcdyh());
                        }
                    }
                    if(bdcdyhList.size() > 1) {
                        map.put("info",gdFwsyq.getProid());
                    }
                }
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "302";
    }

    @Override
    public String getDescription() {
        return "验证一证多房的房产证是否匹配不同的不动产单元";
    }
}
