package cn.gtmap.estateplat.server.web.archives;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/19
 * @description  调用张家港激扬档案
 */
@Controller
@RequestMapping("/archives")
public class ArchivesController extends BaseController {
    @Autowired
    private BdcXmService bdcXmService;

    private static  final String QUERYNAME_PARAM_URL = "&queryname=";
    private static final String ZH_PARAM_URL= "&zh=";
    private static final String COMMA_SEPARATOR= ",";

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param wiid
     * @return
     * @description 查看上一手产权证的激扬档案数据
     */
    @RequestMapping(value = "")
    public void archives(@RequestParam(value = "proid", required = false) String proid,
                           @RequestParam(value = "wiid", required = false) String wiid,
                            HttpServletResponse response) {
        String archivesUrl = AppConfig.getProperty("archives.url");
        if(StringUtils.isNotBlank(archivesUrl) && StringUtils.isNotBlank(wiid)){
            archivesUrl = StringUtils.deleteWhitespace(archivesUrl) + QUERYNAME_PARAM_URL + this.getUserName();
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            StringBuilder zhStringBuilder = new StringBuilder();
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm:bdcXmList) {
                    if(StringUtils.isNotBlank(bdcXm.getYbdcqzh())) {
                        if(StringUtils.isBlank(zhStringBuilder)) {
                            zhStringBuilder.append(StringUtils.deleteWhitespace(bdcXm.getYbdcqzh()));
                        }else{
                            zhStringBuilder.append(COMMA_SEPARATOR + StringUtils.deleteWhitespace(bdcXm.getYbdcqzh()));
                        }
                    }
                }
            }
            archivesUrl += ZH_PARAM_URL + zhStringBuilder.toString();
        }
        try {
            response.sendRedirect(archivesUrl);
        } catch (IOException e) {
            logger.error("ArchivesController.archives",e);
        }
    }
}
