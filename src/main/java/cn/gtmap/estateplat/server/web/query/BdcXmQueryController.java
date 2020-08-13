package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 15-12-25
 * Time: 上午1:18
 * sc:项目查询页面
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcXmQuery")
public class BdcXmQueryController extends BaseController {
    @Autowired
    private Repo repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {

        String xmcxGjss = AppConfig.getProperty("xmcxGjss.order");
        List<String> xmcxGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(xmcxGjss) && xmcxGjss.split(",").length > 0){
            for(String bdcCfGjssZd : xmcxGjss.split(",")){
                xmcxGjssOrderList.add(bdcCfGjssZd);
            }
        }
        model.addAttribute("xmcxGjss", xmcxGjss);
        model.addAttribute("xmcxGjssOrderList", xmcxGjssOrderList);

        return "query/bdcxmList";
    }

    @ResponseBody
    @RequestMapping("/getBdcXmListJsonByPage")
    public Object getBdcdyPagesJson(int page, int rows, String zl, String bdcdyh, String qlr, String bh, String dcxc, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(dcxc)) {
                map.put("hhSearch", StringUtils.deleteWhitespace(dcxc.trim()));
            } else {
                if (StringUtils.isNotBlank(bh)) {
                    map.put("bh", bh.trim());
                }
                if (StringUtils.isNotBlank(qlr)) {
                    map.put("qlr", qlr.trim());
                }
                if (StringUtils.isNotBlank(zl)) {
                    map.put("zl", zl.trim());
                }
                if (StringUtils.isNotBlank(bdcdyh)) {
                    map.put("bdcdyh", bdcdyh.trim());
                }
            }

        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (org.apache.commons.lang.StringUtils.isNotBlank(userDwdm)) {
            map.put("xzqdm", userDwdm);
        }
        return repository.selectPaging("getBdcXmListJsonByPage", map, page - 1, rows);
    }

	@ResponseBody
	@RequestMapping(value = "getTaskid")
	public String getTaskid(String proid){
		String taskid = "";
		PfWorkFlowInstanceVo pf = super.getWorkFlowInstance(proid);
		SysTaskService sysTaskService = PlatformUtil.getTaskService();
        if (pf != null){
		    List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pf.getWorkflowIntanceId());
		    if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
			    taskid = pfTaskVoList.get(0).getTaskId();
		    }
        }
		return taskid;
	}

    @ResponseBody
    @RequestMapping(value = "getTaskidOrWiid")
    public HashMap<String,String> getTaskidOrWiid(String proid) {
        HashMap<String, String> map = new HashMap<String, String>();
        PfWorkFlowInstanceVo pf = super.getWorkFlowInstance(proid);
        SysTaskService sysTaskService = PlatformUtil.getTaskService();
        if (pf != null) {
            List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pf.getWorkflowIntanceId());
            if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
                map.put("TASKID", pfTaskVoList.get(0).getTaskId());
            }else{
                map.put("WIID", pf.getWorkflowIntanceId());
            }
        }
        return map;
    }

	@ResponseBody
	@RequestMapping(value = "getActivityName")
	public String getActivityName(String proid){
		String taskid = "";
		String activityName = "";
		PfWorkFlowInstanceVo pf = super.getWorkFlowInstance(proid);

		SysTaskService sysTaskService = PlatformUtil.getTaskService();
        if (pf != null){
		    List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pf.getWorkflowIntanceId());
		    if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
			    taskid = pfTaskVoList.get(0).getTaskId();
			    activityName= PlatformUtil.getPfActivityNameByTaskId(taskid);
		    }
        }
		return activityName;
	}
}
