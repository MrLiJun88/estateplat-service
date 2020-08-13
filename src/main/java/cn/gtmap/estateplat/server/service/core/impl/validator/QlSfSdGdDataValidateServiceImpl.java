package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.GdBdcSd;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.GdXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/29
 * @description 验证过渡数据是否处于锁定状态
 */
public class QlSfSdGdDataValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private GdXmService gdXmService;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> sdList = new ArrayList<String>();
        //只验证过度匹配创建的项目
        if (project != null && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
            String gdproid = project.getGdproid();
            if (StringUtils.isNotBlank(gdproid)) {
                List<GdBdcSd> list = gdXmService.getGdBdcSd("proid", gdproid, Constants.SDZT_SD);
                if (CollectionUtils.isNotEmpty(list)) {
                    sdList.add(gdproid);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(sdList))
            map.put("info", sdList.get(0));
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
        return "202";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证过渡数据是否处于锁定状态";
    }
}
