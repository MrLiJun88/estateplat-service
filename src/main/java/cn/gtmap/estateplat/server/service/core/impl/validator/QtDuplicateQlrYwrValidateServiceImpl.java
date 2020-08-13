package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证权利人义务人是否重复
 */
public class QtDuplicateQlrYwrValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        String proid = project.getProid();
        Map<String, Object> map = new HashMap<String, Object>();
        List<BdcQlr> qlrList = bdcQlrService.queryBdcQlrByProid(proid);
        isDuplicate(map, qlrList, Constants.QLRLX_QLR);
        if (map.containsKey("info") && map.get("info") != null)
            return map;
        List<BdcQlr> ywrList = bdcQlrService.queryBdcYwrByProid(proid);
        isDuplicate(map, ywrList, Constants.QLRLX_YWR);
        return map;
    }

    /**
     * 验证list中权利人、义务人是否存在重复
     */
    private Map isDuplicate(Map map, List<BdcQlr> list, String qlrlx) {
        if (CollectionUtils.isNotEmpty(list) && list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                String name = list.get(i).getQlrmc();
                String zjh = list.get(i).getQlrzjh();
                for (int j = i + 1; j < list.size(); j++) {
                    String compareName = list.get(j).getQlrmc();
                    String compareZjh = list.get(j).getQlrzjh();
                    if ((StringUtils.equals(name, compareName) && StringUtils.equals(zjh, compareZjh))) {
                        if (StringUtils.equals(qlrlx, Constants.QLRLX_QLR))
                            map.put("info", "权利人存在重复！");
                        else if (StringUtils.equals(qlrlx, Constants.QLRLX_YWR))
                            map.put("info", "义务人存在重复！");
                        return map;
                    }
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
        return "901";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证权利人义务人是否重复";
    }
}
