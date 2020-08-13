package cn.gtmap.estateplat.server.web.omp;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.omp.ContractInfo;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.OmpDataService;
import cn.gtmap.estateplat.server.utils.UrlConnectionUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.service.FileService;
import com.gtis.fileCenter.service.NodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2017/6/25
 * @description 与一张图的数据交互接口
 */
@Controller
@RequestMapping("/ompData")
public class OmpDataController extends BaseController {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private OmpDataService ompDataService;



    @RequestMapping("/ompDataExchange")
    public String ompDataExchange(Model model, String hthSearchCondition, String proid) {
        String fileCenterUrl = AppConfig.getProperty("onemap.unifiedPlatform.fileCenterUrl");
        String casUrl = AppConfig.getProperty("onemap.unifiedPlatform.casUrl");
        String contractInfo = "";
        ContractInfo contractInfoTemp = null;
        if (StringUtils.isNoneBlank(hthSearchCondition)) {
            String contractInfoJson = ompDataService.getContractInfoByHth(hthSearchCondition);
            contractInfoTemp = JSON.parseObject(contractInfoJson,ContractInfo.class);
            model.addAttribute("contractInfo",JSON.toJSONString(contractInfoTemp));
            model.addAttribute("hthSearchCondition", hthSearchCondition);
        }
        if (StringUtils.isNoneBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNoneBlank(bdcXm.getBdcdyid())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null) {
                    model.addAttribute("bdcdyh", bdcBdcdy.getBdcdyh());
                }
            }
        }
        if (StringUtils.isNoneBlank(fileCenterUrl) && StringUtils.isNotBlank(casUrl) && contractInfoTemp != null && StringUtils.isNoneBlank(proid)) {
            fileCenterUrl += "&fromProid=" + CommonUtil.formatEmptyValue(contractInfoTemp.getProid()) + "&toProid=" + proid;
            fileCenterUrl = casUrl + URLEncoder.encode(fileCenterUrl);
            model.addAttribute("fileCenterUrl", fileCenterUrl);

        }
        return "omp/ompDataExchange";
    }

    @ResponseBody
    @RequestMapping("/impContractInfo")
    public String impContractInfo(String contractInfo, String bdcdyh, String proid) {
        if (StringUtils.isNoneBlank(contractInfo)) {
            return ompDataService.impContractInfo(JSON.parseObject(StringUtils.deleteWhitespace(contractInfo), ContractInfo.class), bdcdyh, proid);
        } else {
            return "FAIL";
        }
    }

    @ResponseBody
    @RequestMapping("/extractAttachment")
    public String extractAttachment(String contractInfo, String bdcdyh, String proid) {
        if (StringUtils.isNoneBlank(contractInfo)) {
            return ompDataService.extractAttachment(JSON.parseObject(StringUtils.deleteWhitespace(contractInfo), ContractInfo.class), proid);
        } else {
            return "FAIL";
        }
    }


    @ResponseBody
    @RequestMapping("/getTdSfDyInfo")
    public String getTdSfDyInfo(String djh) {
        return ompDataService.getTdSfDyInfoByDjh(djh);
    }

}
