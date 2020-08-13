package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcQrd;
import cn.gtmap.estateplat.server.core.service.BdcQrdService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-30
 * @description 不动产确认单控制层
 */
@Controller
@RequestMapping("/bdcQrd")
public class BdcQrdController extends BaseController {
    @Autowired
    private BdcQrdService bdcQrdService;

    @RequestMapping(value = "")
    public String index(Model model, String wiid) {
        BdcQrd bdcQrd = bdcQrdService.initBdcQrd(wiid);
        if (bdcQrd != null) {
            bdcQrd.setQrr(StringUtils.isNotBlank(getUserName()) ? getUserName() : "");
            bdcQrd.setQrsj(new Date());
            bdcQrdService.saveBdcQrd(bdcQrd);
        }
        Map xgsjMap = bdcQrdService.initXgsj(wiid);
//        List<Xgsj> xgsjList = Lists.newArrayList();
//        Xgsj Xgsj1 = new Xgsj("（2017）浙01民初1717号", "权属状态", "历史", "现势");
//        Xgsj Xgsj2 = new Xgsj("（2018）苏0583执1372号", "权属状态", "历史", "现势");
//        Xgsj Xgsj3 = new Xgsj("（2017）苏0591民初8775号", "权属状态", "历史", "现势");
//        xgsjList.add(Xgsj1);
//        xgsjList.add(Xgsj2);
//        xgsjList.add(Xgsj3);
        model.addAttribute("xgsjMap", xgsjMap);
        model.addAttribute("bdcQrd", bdcQrd);
        model.addAttribute("bdcQrdJson", JSON.toJSONString(bdcQrd));
        return "wf/batch/qtdjxx/bdcQrd";
    }

    /**
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取证书裁定信息
     */
    @ResponseBody
    @RequestMapping(value = "/qr")
    public Object qrBdcQrd(String bdcQrdJson) {
        Map map = Maps.newHashMap();
        BdcQrd bdcQrd = JSON.parseObject(bdcQrdJson, BdcQrd.class);
        bdcQrd.setQrr(StringUtils.isNotBlank(getUserName()) ? getUserName() : "");
        bdcQrd.setQrsj(new Date());
        bdcQrdService.saveBdcQrd(bdcQrd);
        map.put("msg", "success");
        return map;
    }
}
