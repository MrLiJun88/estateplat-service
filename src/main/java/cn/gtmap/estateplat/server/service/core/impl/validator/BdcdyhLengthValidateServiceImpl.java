package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/9
 * @description 验证不动产单元号是否12-28位
 */
public class BdcdyhLengthValidateServiceImpl implements ProjectValidateService {
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (null!=project&& StringUtils.isNotBlank(project.getBdcdyh())){
            int len = StringUtils.length(StringUtils.deleteWhitespace(project.getBdcdyh()));
            //不动产单元号验证是否12-28位
            if (len < 12)
                map.put("info",project.getProid());
            if (len > 28)
                map.put("info",project.getProid());
        }
        return map;
    }

    @Override
    public String getCode() {
        return "912";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元号是否12-28位";
    }
}
