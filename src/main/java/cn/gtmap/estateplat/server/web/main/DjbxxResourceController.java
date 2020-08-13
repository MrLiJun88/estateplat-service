package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
 * @description A包登记簿信息资源地址url
 */
@Controller
@RequestMapping("/djbxxResource")
public class DjbxxResourceController {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void index(@RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(proid)) {
            String djbxxUrl = StringUtils.EMPTY;
            String bdcdyh = bdcdyService.getBdcdyhByProid(proid);
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            List<BdcXmRel> bdcXmRels = bdcXmRelService.queryBdcXmRelByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcXmRels)) {
                String yproid = bdcXmRels.get(0).getYproid();
                List<Map> djbxxUrls = ReadXmlProps.getJsPzByType("/urlConfig.json", "djbxxUrl");
                if (CollectionUtils.isNotEmpty(djbxxUrls)) {
                    for (Map map : djbxxUrls) {
                        djbxxUrl = map.get("value") != null ? map.get("value").toString() : "";
                    }
                }
                if (StringUtils.isNotBlank(djbxxUrl)) {
                    //登簿后查看现手的登记簿信息
                    if(bdcXm!=null&&StringUtils.equals(bdcXm.getXmzt(),Constants.XMZT_SZ)){
                        yproid=proid;
                    }
                    String url = djbxxUrl + "?proid=" + yproid + "&bdcdyh=" + bdcdyh;
                    response.sendRedirect(url);
                }
            }
        }

    }
}
