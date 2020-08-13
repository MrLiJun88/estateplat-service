package cn.gtmap.estateplat.server.web.main;

import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.vo.PfBusinessVo;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:Will@gtmap.cn">Will</a>
 * @version 1.0, 2017-07-17
 * @description
 */
@Controller
@RequestMapping("/bdcSjglManageBySqlx")
public class BdcSjglForManageBySqlxConntroller extends BaseController  {

    @Autowired
    SysWorkFlowDefineService sysWorkFlowDefineService;

    /**
     * 土地/林权/不动产单元匹配页面
     * 根据登记类型和不动产类型获取申请类型
     *
     * @param businessId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getSqlxByDjlx")
    public List<Map> getSqlxByBdclxDjlx(String businessId) {
        List<Map> pfWorkFlowDefineVoMap = new ArrayList<Map>();
        if (StringUtils.isEmpty(businessId)) {
            List<PfBusinessVo> pfBusinessVoList = sysWorkFlowDefineService.getBusinessList();
            if (CollectionUtils.isNotEmpty(pfBusinessVoList))
                businessId = pfBusinessVoList.get(0).getBusinessId();
        }
        List<PfWorkFlowDefineVo> pfWorkFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineByBusiness(businessId);
        if (CollectionUtils.isNotEmpty(pfWorkFlowDefineVoList)) {
            for (int i = 0; i < pfWorkFlowDefineVoList.size(); i++) {
                HashMap map = new HashMap();
                map.put("wfId", pfWorkFlowDefineVoList.get(i).getWorkflowDefinitionId());
                map.put("wfName", pfWorkFlowDefineVoList.get(i).getWorkflowName());
                pfWorkFlowDefineVoMap.add(map);
            }
        }
        return pfWorkFlowDefineVoMap;
    }

    @ResponseBody
    @RequestMapping(value = "getDjzxByWfid")
    public List<HashMap> getDjzx(String wfid) {
        List<HashMap> djzx = new ArrayList<HashMap>();
        if (StringUtils.isNoneBlank(wfid)) {
            HashMap map = new HashMap();
            map.put("wdid", wfid);
            djzx = bdcZdGlService.getDjzx(map);
        }
        return djzx;
    }

    /**
     * 获取过渡房产登记对应的不动产申请类型
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getGdFcDjlxToSqlxWfid")
    public HashMap getGdFcDjlxToSqlxWfid(Model model, @RequestParam(value = "djzx", required = false) String djzx) {
        HashMap map = new HashMap();
        if (StringUtils.isNoneBlank(djzx)) {
            HashMap map1 = new HashMap();
            map1.put("djzx", djzx);
            List<HashMap> djzxMap = bdcZdGlService.getDjzx(map1);
            if (CollectionUtils.isNotEmpty(djzxMap)) {
                map = djzxMap.get(0);
                if (djzxMap.get(0).get("WFID") != null) {
                    PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(djzxMap.get(0).get("WFID").toString());
                    if (pfWorkFlowDefineVo != null) {
                        map.put("businessId", pfWorkFlowDefineVo.getBusinessId());
                    }
                }
            }

        }
        return map;
    }


}
