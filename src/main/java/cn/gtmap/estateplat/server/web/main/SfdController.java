package cn.gtmap.estateplat.server.web.main;


import cn.gtmap.estateplat.model.server.core.BdcXtSfxm;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
/**
 *@author <a herf="mailto:lichaolong@gtmap.cn">lichaolong</a>
 *@version V1.0,
 *@param
 *@return
 *@description
*/
@Controller
@RequestMapping("/sfd")
public class SfdController extends BaseController{

    @ResponseBody
    @RequestMapping("/getSfdMrz")
    public Object getSfdMrz(@RequestParam(value = "sfxmmc", required = false) String sfxmmc, @RequestParam(value = "sfxmbz", required = false) String sfxmbz) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try{
            if (StringUtils.isNotBlank(sfxmmc))
                sfxmmc = URLDecoder.decode(sfxmmc, Constants.DEFAULT_CHARSET);
            if (StringUtils.isNotBlank(sfxmbz))
                sfxmbz = URLDecoder.decode(sfxmbz, Constants.DEFAULT_CHARSET);
            List<BdcXtSfxm> bdcSfdList = bdcZdGlService.getBdcSfdMrz(sfxmmc, sfxmbz);
            if (CollectionUtils.isNotEmpty(bdcSfdList)) {
                BdcXtSfxm bdcSfd = bdcSfdList.get(0);
                map.put("mrsl", bdcSfd.getMrsl());
                map.put("dw", bdcSfd.getDw());
            }
        } catch (Exception e) {
            logger.error("SfdController.getSfdMrz",e);
        }
        return map;
    }
}
