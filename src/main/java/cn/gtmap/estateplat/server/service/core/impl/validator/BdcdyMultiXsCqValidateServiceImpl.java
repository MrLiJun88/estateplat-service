package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2019/1/24
 * @description 验证不动产单元是否存在多条现势权利验证
 */
public class BdcdyMultiXsCqValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            List<String> gdProidList = bdcXmService.getXsGdCqProidByBdcdyh(project.getBdcdyh());
            List<String> bdcProidList = bdcXmService.getXsBdcCqProidByBdcdyh(project.getBdcdyh());
            if(CollectionUtils.isNotEmpty(gdProidList)){
                proidList.addAll(gdProidList);
            }
            if(CollectionUtils.isNotEmpty(bdcProidList)){
                proidList.addAll(bdcProidList);
            }
        }
        if (CollectionUtils.isNotEmpty(proidList) && proidList.size() > 1){
            map.put("info", proidList.get(0));
        }
        else{
            map.put("info", null);
        }
        return map;
    }

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @version 1.0, 2019/1/24
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "143";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @version 1.0, 2019/1/24
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否存在多条现势权利验证";
    }
}
