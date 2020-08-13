package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcSfxm;
import cn.gtmap.estateplat.server.core.service.BdcSfxmService;
import cn.gtmap.estateplat.server.core.service.BdcSfxxService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/10/23
 * @description
 */
@Controller
@RequestMapping("/bdcSfxx")
public class BdcSfxxController extends BaseController {
    @Autowired
    private BdcSfxxService bdcSfxxService;
    @Autowired
    private BdcSfxmService bdcSfxmService;

    @ResponseBody
    @RequestMapping("/updateBdcSfxx")
    public HashMap updateBdcSfxx(@RequestParam(value = "json", required = false) String json) {
        HashMap<String,String> resultHashMap = new HashMap<String,String>();
        logger.error(json);
        if(StringUtils.isNotBlank(json)){
            JSONArray jsonArray = JSON.parseArray(json);
            if(jsonArray != null && jsonArray.size() > 0) {
                String proid = "";
                String sfzt = "";
                int wsfNum = 0;
                for(int i=0;i<jsonArray.size();i++) {
                    String ywh = "";
                    String sfxmid = "";
                    String ssje = "";
                    String fffs = "";
                    String ffzd = "";
                    Object object =  jsonArray.get(i);
                    if(object != null) {
                        if (((JSONObject) object).get("ywh") != null) {
                            ywh = CommonUtil.formatEmptyValue(((JSONObject) object).get("ywh"));
                        }

                        if (((JSONObject) object).get("sfxmid") != null) {
                            sfxmid = CommonUtil.formatEmptyValue(((JSONObject) object).get("sfxmid"));
                        }

                        if (((JSONObject) object).get("ssje") != null) {
                            ssje = CommonUtil.formatEmptyValue(((JSONObject) object).get("ssje"));
                            if (((JSONObject) object).get("fffs") != null) {
                                fffs = CommonUtil.formatEmptyValue(((JSONObject) object).get("fffs"));
                            }
                            if (((JSONObject) object).get("ffzd") != null) {
                                ffzd = CommonUtil.formatEmptyValue(((JSONObject) object).get("ffzd"));
                            }
                        }

                        if (StringUtils.isBlank(ssje)) {
                            wsfNum++;
                        }

                        BdcSfxm bdcSfxm = bdcSfxmService.getBdcSfxmBySfxmid(sfxmid);
                        if (bdcSfxm != null) {
                            bdcSfxm.setFffs(fffs);
                            bdcSfxm.setFfzd(ffzd);
                            bdcSfxmService.saveBdcSfxmBySfxmid(bdcSfxm);
                        }

                        if (StringUtils.isBlank(proid) && StringUtils.isNotBlank(ywh)) {
                            proid = ywh;
                        }
                    }
                }

                if(jsonArray.size() > 1){
                    if(wsfNum == 0) {
                        sfzt = Constants.SFXX_SFZT_YJF;
                    }
                    else if(wsfNum == jsonArray.size()){
                        sfzt = Constants.SFXX_SFZT_WJF;
                    } else{
                        sfzt = Constants.SFXX_SFZT_BFJF;
                    }
                }else{
                    if(wsfNum == 0){
                        sfzt = Constants.SFXX_SFZT_YJF;
                    }else {
                        sfzt = Constants.SFXX_SFZT_WJF;
                    }
                }

                if(StringUtils.isNotBlank(proid)){
                    bdcSfxxService.updateBdcSfxx(proid,sfzt,new Date());
                    resultHashMap.put("result","success");
                    return resultHashMap;
                }
            }
        }
        resultHashMap.put("result","fail");
        return resultHashMap;
    }
}
