package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXyglService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BdcXyglIsBzxzfValidateServiceImpl implements ProjectValidateService {

    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcXyglService bdcXyglService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        HashMap map = new HashMap();
        boolean isBzfry = false;
        if (project != null&&StringUtils.isNotBlank(project.getProid())) {
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    isBzfry = bdcXyglService.checkBzfry(bdcQlr.getQlrzjh());
                    if (isBzfry) {
                        break;
                    }
                }
            }
        }
        if (isBzfry) {
            map.put("info", project.getProid());
        }
        return map;
    }

    @Override
    public String getCode() {
        return "914";
    }

    @Override
    public String getDescription() {
        return "验证权利人是否是保障性住房者";
    }
}
