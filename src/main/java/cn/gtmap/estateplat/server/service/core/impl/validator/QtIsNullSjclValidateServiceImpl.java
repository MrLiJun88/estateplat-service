package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcSjcl;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.mapper.BdcSjclMapper;
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
 * @description 检查收件材料份数是否为空
 */
public class QtIsNullSjclValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private BdcSjclMapper bdcSjclMapper;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getProid()) && StringUtils.isNotBlank(project.getBdcdyh())) {
            boolean isAdd = true;
            List<BdcSjcl> bdcSjclList = bdcSjclMapper.getSjclByproid(project.getProid());
            if (CollectionUtils.isNotEmpty(bdcSjclList)) {
                for (int i = 0; i < bdcSjclList.size(); i++) {
                    if (bdcSjclList.get(i).getMrfs() == null ||  bdcSjclList.get(i).getMrfs() == 0
                            || bdcSjclList.get(i).getFs() == 0) {
                        isAdd = false;
                        break;
                    }
                }
            }
            if (!isAdd) {
                proidList = new ArrayList<String>();
                proidList.add(project.getProid());
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
        return "903";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "检查收件材料份数是否为空";
    }
}
