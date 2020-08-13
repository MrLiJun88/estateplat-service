package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/19
 * @description
 */
public class BdcdySzzdDyValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project= (Project)param.get("project");
        QllxVo qllxVo = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String bdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
            if (StringUtils.isNotBlank(bdcdyh)) {
                HashMap<String, Object> querymap = new HashMap<String, Object>();
                querymap.put("bdcdyh", bdcdyh);
                querymap.put("xmzt", "1");
                qllxVo = new BdcDyaq();
                List<QllxParent> list = qllxParentService.queryZtzcQllxVo(qllxVo, querymap);
                if (CollectionUtils.isNotEmpty(list)) {
                    map.put("info", list.get(0).getProid());
                }
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "133";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元所在宗地是否已抵押";
    }
}
