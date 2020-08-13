package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.server.core.service.BdcSpfZdHjgxService;
import cn.gtmap.estateplat.server.service.ProjectService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *
 *不动产商品房与宗地核减关系
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2016/9/13
 */
@Controller
@RequestMapping("/bdcSpfZdHjgx")
public class BdcSpfZdHjgxController extends BaseController  {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcSpfZdHjgxService bdcSpfZdHjgxService;

    @ResponseBody
    @RequestMapping(value = "/updateSydyje", method = RequestMethod.GET)
    public Map updateSydyje(@RequestParam(value = "proid", required = false) String proid,
                                    @RequestParam(value = "xmhjdyje", required = false) Double xmhjdyje,
                                    @RequestParam(value = "fwhjtdmj", required = false) Double fwhjtdmj){
        Map resultMsg= Maps.newHashMap();
        String msg="ok";
        try {
            bdcSpfZdHjgxService.updateSydyjeAndSyfttdmj(proid);
        }catch (Exception e){
            msg="error";
            logger.info("核减金额错误:"+e.getMessage());
        }
        resultMsg.put("msg",msg);
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "/updateXmhjdyje", method = RequestMethod.GET)
    public Map updateXmhjdyje(@RequestParam(value = "proid", required = false) String proid){
        Map resultMsg= Maps.newHashMap();
        String msg="ok";
        try {
            bdcSpfZdHjgxService.updateXmhjdyje(proid);
        }catch (Exception e){
            msg="error";
            logger.info("保存项目核减抵押金额错误:"+e.getMessage());
        }
        resultMsg.put("msg",msg);
        return resultMsg;
    }
}
