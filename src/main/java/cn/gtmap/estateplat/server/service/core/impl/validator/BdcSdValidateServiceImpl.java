package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-10
 * @description 不动产收档验证
 */
public class BdcSdValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcXmService bdcXmService;

    /**
     * @param param 项目信息
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */
    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = Maps.newHashMap();
        List<String> proidList = Lists.newArrayList();
        Project project = (Project) param.get("project");
        if (project != null && StringUtils.isNotBlank(project.getYxmid())) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getYxmid());
            if (!(bdcXm != null && StringUtils.equals(bdcXm.getSfsd(), "1"))) {
                proidList.add(project.getProid());
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }
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
        return "198";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "不动产数据收档验证";
    }
}
