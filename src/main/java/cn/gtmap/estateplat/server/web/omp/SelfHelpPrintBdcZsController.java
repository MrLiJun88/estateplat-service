package cn.gtmap.estateplat.server.web.omp;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.model.omp.BdcZzdzjFjxx;
import cn.gtmap.estateplat.server.core.service.SelfHelpPrintBdcZsService;
import cn.gtmap.estateplat.server.service.AutoWorkFlowService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:zhangwentao@gtmap.cn">zhangwentao</a>
 * @version 1.0, 2018/9/12
 * @description 自助打证机打印证书接口
 */
@Controller
@RequestMapping("/selfHelpPrint")
public class SelfHelpPrintBdcZsController extends BaseController {
    @Autowired
    private SelfHelpPrintBdcZsService selfHelpPrintBdcZsService;
    @Autowired
    private AutoWorkFlowService autoWorkFlowService;

    @ResponseBody
    @RequestMapping("/checkData")
    public Map checkData(BdcXm bdcXm, BdcZs bdcZs, String slbh, long fzrq) {
        Map map = new HashMap();
        if (null != bdcXm && null != bdcZs) {
            Date fzrqTime = new Date(fzrq);
            bdcXm.setBh(slbh);
            bdcZs.setFzrq(fzrqTime);
            String userid = super.getUserId();
            map = selfHelpPrintBdcZsService.checkData(bdcXm, bdcZs);
            //缮证自动转发
            //  autoWorkFlowService.autoTurnProject(bdcZs,Constants.WORKFLOW_SZ,userid);
        } else {
            map.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
            map.put(ParamsConstants.CHECKMSG_HUMP, "验证数据为空！");
        }
        return map;
    }


    @ResponseBody
    @RequestMapping(value = "/savePrintZsFj", method = RequestMethod.POST)
    public Map savePrintZsFj(@RequestBody String jsonString) {
        Map map = new HashMap();
        BdcZzdzjFjxx bdczzdzjFjxx = JSON.parseObject(jsonString, BdcZzdzjFjxx.class);
        if (bdczzdzjFjxx != null) {
            BdcXm bdcXm = new BdcXm();
            BdcZs bdcZs = new BdcZs();
            bdcXm.setBh(bdczzdzjFjxx.getBh());
            bdcXm.setLzrzjh(bdczzdzjFjxx.getLzrzjh());
            bdcZs.setZsid(bdczzdzjFjxx.getZsid());
            bdcZs.setBh(bdczzdzjFjxx.getZsh());
            bdcZs.setFzrq(new Date(bdczzdzjFjxx.getFzrq()));
            map = selfHelpPrintBdcZsService.checkData(bdcXm, bdcZs);
            if (StringUtils.equals(ParamsConstants.SUCCESS_LOWERCASE, CommonUtil.formatEmptyValue(map.get("info")))) {
                map = selfHelpPrintBdcZsService.savePrintZsFj(bdczzdzjFjxx);
            }
        } else {
            map.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
            map.put(ParamsConstants.CHECKMSG_HUMP, "验证数据为空！");
        }
        return map;
    }

}
