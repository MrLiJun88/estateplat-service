package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcZj;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 质检
 * Created by liaoxiang on 2017/8/2.
 */
@Controller
@RequestMapping(value = "bdcZj")
public class BdcZjController extends BaseController{
    @Autowired
    private BdcZjService bdcZjService;
    @Autowired
    private BdcXmService bdcXmService;



    @ResponseBody
    @RequestMapping(value = "initBdcZj")
    public String initBdcZj(String proid ,String yxmid,String[]yxmids, String wiid) {
        String msg = "失败";
        if (StringUtils.isNoneBlank(yxmids)) {
            //如果已经存在质检项目，则生成新的质检项目
            BdcZj bdcZj = bdcZjService.getBdcZjByProid(proid);
            if (bdcZj != null)
                proid = UUIDGenerator.generate18();
            for (String yxmidArr : yxmids) {
                bdcZjService.initBdcZjxx(proid, yxmidArr,wiid);
                //生成新的proid循环
                proid = UUIDGenerator.generate18();
            }
            msg = "成功";
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "delBdcZj")
    public String delBdcZj(String proid) {
        String msg = "";
        try {
            bdcXmService.delBdcXmByProid(proid);
            bdcZjService.delBdcZjmxByProid(proid);
            bdcZjService.delBdcZjByProid(proid);
            msg = "成功";
        } catch (Exception e) {
            logger.error("BdcZjController.delBdcZj",e);
        }
        return msg;
    }
}
