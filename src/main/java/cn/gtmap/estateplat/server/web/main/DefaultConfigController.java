package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXtOpinion;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zx on 2015/4/19.
 * 默认配置
 */
@Controller
@RequestMapping("/defaultConfig")
public class DefaultConfigController extends BaseController {
    @Autowired
    EntityMapper entityMapper;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Map defaultConfig(Model model, HttpServletRequest request) throws Exception {
        String taskid = request.getParameter("taskid");
        String proid = request.getParameter("proid");

        String activityName = "";
        String wfName = "";
        if (StringUtils.isNotBlank(taskid))
            activityName = PlatformUtil.getPfActivityNameByTaskId(taskid);
        if (StringUtils.isNotBlank(proid))
            wfName = PlatformUtil.getWorkFlowNameByProid(proid);
        Example example = new Example(BdcXtOpinion.class);
        if (StringUtils.isNotBlank(wfName) && StringUtils.isNotBlank(activityName))
            example.createCriteria().andEqualTo("workflowname", wfName).andEqualTo("activitytype", activityName);
        else if (StringUtils.isNotBlank(wfName))
            example.createCriteria().andEqualTo("workflowname", wfName);
        String option = "";
        if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
            List<BdcXtOpinion> bdcXtOpinionList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcXtOpinionList)) {
                option = bdcXtOpinionList.get(0).getContent();
            }
        }
        Map map = new HashMap();
        map.put("opinion", option);
        return map;
    }
}
