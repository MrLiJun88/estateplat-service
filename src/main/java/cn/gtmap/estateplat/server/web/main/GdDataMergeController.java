package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.core.service.GdDataMergeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/9/1
 * @description 合并过渡数据（针对产权分别持证）
 */
@Controller
@RequestMapping("/gdDataMerge")
public class GdDataMergeController extends BaseController {

    @Autowired
    private GdDataMergeService gdDataMergeService;
    /**
     * @param qlids
     * @param bdclx
     * @return HashMap
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 检查是否可以合并
     */
    @ResponseBody
    @RequestMapping(value = "/checkGdDataMerge")
    public HashMap checkGdDataMerge(String qlids,String bdclx) {
        HashMap resultMap = new HashMap();
        if (StringUtils.isNotBlank(qlids)) {
            resultMap = gdDataMergeService.checkGdDataMerge(qlids,bdclx);
        }
        return resultMap;
    }

    /**
     * @param qlids
     * @param bdclx
     * @return String
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 合并数据
     */
    @ResponseBody
    @RequestMapping(value = "/mergeGdData")
    public String mergeGdData(String qlids, String bdclx) {
        String msg = "合并失败";
        if (StringUtils.isNotBlank(qlids) && StringUtils.isNotBlank(bdclx)) {
            try {
                gdDataMergeService.mergeGdData(qlids, bdclx);
                msg = "合并成功";
            } catch (Exception e) {
                logger.error("GdDataMergeController.mergeGdData",e);
            }
        }
        return msg;
    }
}
