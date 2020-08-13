package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BdcdyExistXsCqValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            List<String> gdProidList = bdcXmService.getXsGdCqProidByBdcdyh(project.getBdcdyh());
            List<String> bdcProidList = bdcXmService.getXsBdcCqProidByBdcdyh(project.getBdcdyh());
            // 权籍单元号找房屋
            List<String> gdfwidList = gdFwService.getFwidByBdcdyh(project.getBdcdyh());
            List<String> unPpGdProidList = gdFwService.getXsFwsyqProidByfwid(gdfwidList);
            if(CollectionUtils.isNotEmpty(unPpGdProidList)){
                proidList.addAll(unPpGdProidList);
            }
            if(CollectionUtils.isNotEmpty(gdProidList)){
                proidList.addAll(gdProidList);
            }
            if(CollectionUtils.isNotEmpty(bdcProidList)){
                proidList.addAll(bdcProidList);
            }
        }
        if (CollectionUtils.isNotEmpty(proidList) && proidList.size() >= 1){
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
        return "144";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @version 1.0, 2019/1/24
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否已存在一条及以上现势产权验证";
    }
}
