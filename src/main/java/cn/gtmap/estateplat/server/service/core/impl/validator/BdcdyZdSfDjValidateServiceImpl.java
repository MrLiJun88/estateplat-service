package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证不动产单元所在宗地是否正在登记
 */
public class BdcdyZdSfDjValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private QllxService qllxService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String bdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
            if (StringUtils.isNotBlank(bdcdyh)) {
                hashMap.put("bdcdyh", bdcdyh);
                hashMap.put("xmzt", "1");
                List<Map> qllxList = qllxService.getQllxListByPage(hashMap);
                //去掉当前项目本身
                if (qllxList != null) {
                    for (int i = 0; i < qllxList.size(); i++) {
                        if (StringUtils.equals(project.getProid(), (String) qllxList.get(i).get("PROID"))) {
                            qllxList.remove(i);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(qllxList)) {
                    map.put("info", qllxList.get(0).get("PROID"));
                }
            }
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
        return "119";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元所在宗地是否正在登记";
    }
}
