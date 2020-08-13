package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/4/30
 * @description
 */
public class BdcQlrZjhLengthCheckServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map map = Maps.newHashMap();
        Project project = (Project) param.get("project");
        List<BdcXm> bdcXmList = null;
        List<BdcQlr> bdcQlrList = null;
        if (project != null && StringUtils.isNotBlank(project.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)){
            for(BdcXm bdcXm: bdcXmList){
                bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcQlrList)){
                    for(BdcQlr bdcQlr: bdcQlrList){
                        if(StringUtils.isNotBlank(bdcQlr.getQlrzjh()) && (StringUtils.length(bdcQlr.getQlrzjh())<=5)){
                            map.put("info", ParamsConstants.FALSE_LOWERCASE);
                            break;
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "916";
    }

    @Override
    public String getDescription() {
        return "权利人或义务人证件号码小于5位，请检查！";
    }
}
