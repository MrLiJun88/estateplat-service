package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.model.GxYhValidateConfig;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.service.etl.EtlGxYhManageService;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @description 验证互联网+接口调用是否成功
 */

public class GxYhInterfaceValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private EtlGxYhManageService etlGxYhManageService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        HashMap map = new HashMap();
        String proid = project.getProid();
        String wiid = project.getWiid();
        String userid = project.getUserId();
        String returnMsg = ParamsConstants.TRUE_LOWERCASE;
        String sqlxdm = null;
        if(StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(userid)){
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm : bdcXmList){
                    if(StringUtils.isNotBlank(bdcXm.getYhsqywh())){
                        proid = bdcXm.getProid();
                        //先判断节点
                        PfTaskVo pfTaskVo = PlatformUtil.getPfTaskVoByWiid(wiid);
                        String targetActivityName = null;
                        if (pfTaskVo != null) {
                            targetActivityName = PlatformUtil.getActivityNameByPfTaskVo(pfTaskVo);
                        }
                        if (StringUtils.isNotBlank(wiid)) {
                            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
                            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                            }
                        }
                        if (StringUtils.isNotBlank(targetActivityName) && StringUtils.isNotBlank(sqlxdm)) {
                            String gxYhJson = "";
                            try {
                                FileInputStream fileInputStream = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("/conf/").getPath().substring(1) + "json/gxYhInterfaceValidateConfig.json");
                                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                                BufferedReader reader = new BufferedReader(inputStreamReader);
                                String s;
                                while ((s = reader.readLine()) != null) {
                                    gxYhJson += s;
                                }
                                reader.close();
                                JSONObject jsonObject = JSON.parseObject(gxYhJson);
                                String gxYhvalidateConfig = CommonUtil.formatEmptyValue(jsonObject.get("gxYhValidateConfig"));
                                if(StringUtils.isNotBlank(gxYhvalidateConfig)){
                                    List<GxYhValidateConfig> gxYhValidateConfigList = JSON.parseArray(gxYhvalidateConfig,GxYhValidateConfig.class);
                                    if(CollectionUtils.isNotEmpty(gxYhValidateConfigList)){
                                        for(GxYhValidateConfig gxYhValidateConfig : gxYhValidateConfigList){
                                            if(StringUtils.equals(sqlxdm,gxYhValidateConfig.getSqlxdm()) &&
                                                    StringUtils.equals(gxYhValidateConfig.getTargetActivityName(),targetActivityName)){
                                                try {
                                                    returnMsg = etlGxYhManageService.dbYhxx(proid,wiid,"",gxYhValidateConfig.getBjzt(),userid);
                                                } catch (IOException e) {
                                                    logger.error("GxYhInterfaceValidateServiceImpl.dbYhxxError");
                                                    returnMsg = ParamsConstants.FALSE_LOWERCASE;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                logger.error("GxYhInterfaceValidateServiceImpl.jsonError");
                                returnMsg = ParamsConstants.FALSE_LOWERCASE;
                            }
                        }
                    }
                }
            }
        }
        if(StringUtils.isBlank(returnMsg)){
            returnMsg = ParamsConstants.FALSE_LOWERCASE;
        }
        if(StringUtils.equals(returnMsg,ParamsConstants.FALSE_LOWERCASE)){
            map.put("info",returnMsg);
        }
        return map;
    }

    @Override
    public String getCode() {
        return "915";
    }

    @Override
    public String getDescription() {
        return "验证互联网+接口调用是否成功";
    }
}
