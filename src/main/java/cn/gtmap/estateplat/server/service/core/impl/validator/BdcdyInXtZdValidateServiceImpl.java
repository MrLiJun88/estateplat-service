package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2016/7/29
 * @description 验证不动产单元号是否处于同一宗地
 */
public class BdcdyInXtZdValidateServiceImpl implements ProjectValidateService {
    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        QllxVo qllxVo = null;
//        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
//            String bdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
//            if (StringUtils.isNotBlank(bdcdyh)) {
//                HashMap<String, Object> querymap = new HashMap<String, Object>();
//                querymap.put("bdcdyh", bdcdyh);
//                querymap.put("xmzt", "1");
//                qllxVo = new BdcDyaq();
//                List<QllxParent> list = qllxParentService.queryZtzcQllxVo(qllxVo, querymap);
//                if (list != null && list.size() > 0) {
//                    map.put("info", list.get(0).getProid());
//                }
//            }
//        }
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
        return "125";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元号是否处于同一宗地";
    }
}
