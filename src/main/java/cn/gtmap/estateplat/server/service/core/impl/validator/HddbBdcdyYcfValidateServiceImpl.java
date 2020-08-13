package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.GdCfService;
import cn.gtmap.estateplat.server.core.service.GdDyhRelService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
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
 * @description 不动产单元预查封验证服务
 */
public class HddbBdcdyYcfValidateServiceImpl implements ProjectValidateService{

    @Autowired
    private QllxParentService qllxParentService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String activityName = PlatformUtil.getActivityName(project.getProid());
            if (StringUtils.equals(Constants.WORKFLOW_HD, activityName)) {
                List<QllxParent> list = qllxParentService.queryLogcfQllxVo(new BdcCf(), project.getBdcdyh(), "", "true");
                if (CollectionUtils.isNotEmpty(list)) {
                    proidList = new ArrayList<String>();
                    for (QllxParent qllxParent : list) {
                        proidList.add(qllxParent.getProid());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }else {
            map.put("info", null);
        }
        return map;
    }

    @Override
    public String getCode() {
        return "194";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元存在预查封信息转为查封信息";
    }
}
