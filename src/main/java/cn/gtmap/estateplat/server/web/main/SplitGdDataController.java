package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.core.service.SplitGdDataServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2016-11-9
 * @description 拆分过度数据
 */
@Controller
@RequestMapping("/splitGdData")
public class SplitGdDataController extends BaseController{

    @Autowired
    private SplitGdDataServer splitGdDataServer;

    /**
     * @param qlid
     * @return HashMap
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 检查是否可以拆分
     */
    @ResponseBody
    @RequestMapping(value = "checkSplit")
    public HashMap checkSplit(String qlid) {
        HashMap resultMap = new HashMap();
        if (StringUtils.isNotBlank(qlid)) {
            resultMap = splitGdDataServer.checkSplit(qlid);
        }
        return resultMap;
    }

    /**
     * @param qlid
     * @return String
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据
     */
    @ResponseBody
    @RequestMapping(value = "splitGdData")
    public String splitGdData(String qlid, String bdclx) {
        String msg = "失败";
        if (StringUtils.isNotBlank(qlid) && StringUtils.isNotBlank(bdclx)) {
            try {
                splitGdDataServer.splitGdData(qlid, bdclx);
                msg = "拆分成功";
            } catch (Exception e) {
                logger.error("SplitGdDataController.splitGdData",e);
            }
        }
        return msg;
    }
}
