package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 不动产单元是否存在异议登记
 */
public class BdcdySfYYValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private QllxParentService qllxParentService;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            HashMap<String, Object> querymap = new HashMap<String, Object>();
            querymap.put("bdcdyh", project.getBdcdyh());
            List<QllxParent> list = qllxParentService.queryZtzcQllxVo(new BdcYy(), querymap);
            if (CollectionUtils.isNotEmpty(list)) {
                proidList = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    //过滤当前正在办理项目的proid 避免转发出现验证不通过
                    if(!StringUtils.equals(list.get(i).getProid(), project.getProid())) {
                        proidList.add(list.get(i).getProid());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList))
            map.put("info", proidList.get(0));
        else
            map.put("info", null);
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
        return "104";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否存在异议登记";
    }
}
