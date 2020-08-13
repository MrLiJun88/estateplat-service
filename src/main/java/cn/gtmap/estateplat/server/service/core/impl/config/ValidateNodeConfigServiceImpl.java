package cn.gtmap.estateplat.server.service.core.impl.config;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.model.config.ValidateNodeConfig;
import cn.gtmap.estateplat.server.service.core.config.ValidateNodeConfigService;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadJsonFileUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/11/25
 * @description
 */
@Service
public class ValidateNodeConfigServiceImpl implements ValidateNodeConfigService {
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Override
    public Boolean nodeValidateEnable(Project project, String checkCode) {
        String validateNodeConfigJson = ReadJsonFileUtil.readJsonFile("/conf/server/validateNodeConfig.json");
        List<ValidateNodeConfig> validateNodeConfigList = JSON.parseArray(validateNodeConfigJson, ValidateNodeConfig.class);
        if(project != null&&CollectionUtils.isNotEmpty(validateNodeConfigList)){
            String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(project.getWorkFlowDefId());
            for(ValidateNodeConfig validateNodeConfig:validateNodeConfigList) {
                if(StringUtils.equals(validateNodeConfig.getCheckCode(),checkCode)){
                    if(StringUtils.indexOf(validateNodeConfig.getSqlxdm(),project.getSqlx())>-1
                            &&StringUtils.indexOf(validateNodeConfig.getNodeName(),PlatformUtil.getPfActivityNameByTaskId(project.getTaskid()))>-1){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
