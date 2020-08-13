package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.core.service.BdcCheckService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/11/13
 * @description
 */
@Controller
@RequestMapping("/promptView")
public class BdcPromptViewController extends BaseController {

    @Autowired
    BdcCheckService bdcCheckService;

    /**
     * 组织examine验证数据
     *
     * @param model
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/organizeData")
    public List<Map<String, Object>> organizeData(Model model,String info) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if(StringUtils.isNotEmpty(info)){
            List<String> cfQlidList = JSON.parseArray(info, String.class);
            resultList = bdcCheckService.organizeExamineData(cfQlidList);
        }
        return  resultList;
    }

}
