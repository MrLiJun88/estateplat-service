package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.GxYhValidateConfig;
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
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @description 互联网+接口相关功能
 */
@Service
public class GxYhServiceImpl implements GxYhService {
    @Autowired
    private EtlGxYhManageService etlGxYhManageService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcZsbhService bdcZsbhService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Map<String, Object> validateGxYhInterface(String proid, String userid) {
        HashMap map = new HashMap();
        String wiid = null;
        BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
        if(bdcXmTemp != null){
            wiid = bdcXmTemp.getWiid();
        }
        String returnMsg = ParamsConstants.TRUE_LOWERCASE;
        String sqlxdm = null;
        List<BdcXm> bdcXmList = null;
        Boolean yhsq = false;
        if(StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(userid)){
            bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm : bdcXmList){
                    if(StringUtils.isNotBlank(bdcXm.getYhsqywh())){
                        yhsq = true;
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
                                                    logger.error("GxYhInterfaceValidateServiceImpl.dbYhxxError",e);
                                                    returnMsg = ParamsConstants.FALSE_LOWERCASE;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                logger.error("GxYhInterfaceValidateServiceImpl.jsonError",e);
                                returnMsg = ParamsConstants.FALSE_LOWERCASE;
                            }
                        }
                    }
                }
            }
        }
        if(StringUtils.isBlank(returnMsg) && yhsq){
            returnMsg = ParamsConstants.FALSE_LOWERCASE;
        }
        if(StringUtils.equals(returnMsg,ParamsConstants.FALSE_LOWERCASE)){
            map.put("info","互联网+接口调用失败！");
            //接口调用失败情况下 节点未被转发 因为将完善证书编号功能配置在转发前执行 需要将相关信息还原
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm : bdcXmList){
                    bdcZsbhService.workFlowBackZsBh(bdcXm.getProid());
                    //提前生成证书 删除证号
                    bdcZsService.delBdcZsBdcqzhAndZsbhByProid(bdcXm.getProid());
                }
                bdcXmService.delXzzrnxInfoFj(bdcXmTemp);
            }
        }
        return map;
    }
}
