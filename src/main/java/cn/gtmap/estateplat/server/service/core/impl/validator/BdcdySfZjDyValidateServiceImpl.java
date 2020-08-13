package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证不动产单元是否存在在建筑物抵押
 */
public class BdcdySfZjDyValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcXmService bdcXmService;


    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
            String xmly = bdcXm.getXmly();
            if (StringUtils.isNotBlank(xmly) && xmly.equals("1")&&StringUtils.isNotBlank(project.getBdcdyh())) {
                HashMap<String, String> querymap = new HashMap<String, String>();
                querymap.put("bdcdyh", project.getBdcdyh());
                querymap.put("dyzt", "0");
                List<BdcZjjzwxx> bdcZjjzwxxLst = bdcZjjzwxxService.getZjjzwxx(querymap);
                if (CollectionUtils.isNotEmpty(bdcZjjzwxxLst)) {
                    proidList = new ArrayList<String>();
                    for (BdcZjjzwxx bdcZjjzwxx : bdcZjjzwxxLst) {
                        proidList.add(bdcZjjzwxx.getProid());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList))
            map.put("info", proidList);
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
        return "108";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否存在在建筑物抵押";
    }
}
