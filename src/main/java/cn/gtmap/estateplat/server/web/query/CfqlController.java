package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 15-9-30
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcCfql")
public class CfqlController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcCfService bdcCfService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        List<String> list = bdcCfService.getBdcCflxMc();

        String bdccfqlGjss = AppConfig.getProperty("bdccfqlGjss.order");
        List<String> bdccfqlGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(bdccfqlGjss) && bdccfqlGjss.split(",").length > 0){
            for(String bdcCfGjssZd : bdccfqlGjss.split(",")){
                bdccfqlGjssOrderList.add(bdcCfGjssZd);
            }
        }
        model.addAttribute("bdccfqlGjss", bdccfqlGjss);
        model.addAttribute("bdccfqlGjssOrderList", bdccfqlGjssOrderList);

        model.addAttribute("bdcCflxlist", list);
        model.addAttribute("reporturl", reportUrl);
        return "query/cfQl";
    }

    @ResponseBody
    @RequestMapping(value = "/getbdcCfPagesJsonace")
    public Object getbdcCfPagesJsonace(Pageable pageable, String dcxc, String bdcdyh, String cfwh, String cflx, String cfjg) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(bdcdyh))
                map.put("bdcdyh", bdcdyh);
            if (StringUtils.isNotBlank(cfwh))
                map.put("cfwh", cfwh);
            if (StringUtils.isNotBlank(cflx))
                map.put("cflx", cflx);
            if (StringUtils.isNotBlank(cfjg))
                map.put("cfjg", cfjg);
        }
        return repository.selectPaging("getBdcCfByPage", map, pageable);
    }

}
