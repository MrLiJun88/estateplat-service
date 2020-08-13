package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2019/12/26
 * @description 验证不动产单元是否为虚拟宗单元号
 */
public class BdcdyhIsXnzdyhValidateServiceImpl implements ProjectValidateService {
    private static final String xnzdjh = "320281030005GB00187";//江阴虚拟宗地地籍号

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project= (Project)param.get("project");
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh()) && StringUtils.equals(xnzdjh, project.getBdcdyh().substring(0,19))) {
            map.put("info", project.getProid());
        }
        return map;
    }

    @Override
    public String getCode() {
        return "151";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元是否为虚拟宗单元号";
    }
}
