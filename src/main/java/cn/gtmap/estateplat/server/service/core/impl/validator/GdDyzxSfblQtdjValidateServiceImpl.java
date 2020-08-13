package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.GdDy;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.GdDyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2018/11/8
 * @description
 */
public class GdDyzxSfblQtdjValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private GdDyService gdDyService;
    @Override
    public Map<String, Object> validate(HashMap param) {
        String proid = null;
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (project != null && StringUtils.isNotBlank(project.getYbdcqzh())){
            GdDy gdDyTemp = gdDyService.getGdDyByDydjzmh(project.getYbdcqzh());
            if (gdDyTemp != null){
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByYqlid(gdDyTemp.getDyid());
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    proid = bdcXmRelList.get(0).getProid();
                }
            }
        }
        if (StringUtils.isNotBlank(proid)){
            map.put("info",proid);
        }
        return map;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
