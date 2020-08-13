package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.GdTdsyq;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.GdTdService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/11/23
 * @description
 */
public class GdFwTdZsZtValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private GdTdService gdTdService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(project.getGdproid())) {
            String tdQlid = gdTdService.getGdTdQlidByFwQlid(project.getGdproid());
            GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(tdQlid);
            if(null != gdTdsyq && null !=gdTdsyq.getIszx()&& 1==gdTdsyq.getIszx()) {
                map.put("info",gdTdsyq.getProid());
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "301";
    }

    @Override
    public String getDescription() {
        return "验证房屋土地证是否注销";
    }
}
