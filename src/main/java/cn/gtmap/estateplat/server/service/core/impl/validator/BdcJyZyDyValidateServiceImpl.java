package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.service.etl.JyxxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2018/12/25
 * @description
 */
public class BdcJyZyDyValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private JyxxService jyxxService;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (project != null && StringUtils.isNotBlank(project.getJybh())){
            boolean sfdk = jyxxService.getSfdkByJybh(project.getJybh());
            if (sfdk == Boolean.TRUE){
                map.put("info",project.getProid());
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "501";
    }

    @Override
    public String getDescription() {
        return "根据住建接口中的shifoudk字段验证不动产做转移登记时是否有抵押";
    }
}
