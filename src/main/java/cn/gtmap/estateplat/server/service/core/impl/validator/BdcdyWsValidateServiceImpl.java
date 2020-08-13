package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcWsxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcWsxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.service.core.config.ValidateNodeConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020/3/23
 * @description 江阴验证是否完税
 */
public class BdcdyWsValidateServiceImpl implements ProjectValidateService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BdcWsxxService bdcWsxxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ValidateNodeConfigService validateNodeConfigService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project = (Project) param.get("project");
        List<BdcWsxx> bdcWsxxList = new ArrayList<>();
        Boolean validateEnable = validateNodeConfigService.nodeValidateEnable(project, this.getCode());
        if (project != null && StringUtils.isNotBlank(project.getWiid()) && validateEnable) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm:bdcXmList){
                    bdcWsxxList = bdcWsxxService.getBdcWsxxListByProid(bdcXm.getProid());
                    if(CollectionUtils.isNotEmpty(bdcWsxxList)){
                        break;
                    }
                }
            }
            if (CollectionUtils.isEmpty(bdcWsxxList)) {
                List<String> proidList = new ArrayList<>();
                proidList.add("false");
                map.put("info", proidList);
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "309";
    }

    @Override
    public String getDescription() {
        return "江阴验证是否完税";
    }
}
