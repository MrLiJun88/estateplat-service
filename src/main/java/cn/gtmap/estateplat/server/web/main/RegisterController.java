package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.register.DjModel;
import cn.gtmap.estateplat.service.server.ProjectManageService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @version 1.0, 2017/3/29
 * @author<a href = "mailto；liuxing@gtmap.cn">liuxing</a>
 * @description
 */
@Controller
@RequestMapping("register")
public class RegisterController extends BaseController {

    @Autowired
    private ProjectManageService projectManageService;
    @Autowired
    private Repo repository;
    @ResponseBody
    @RequestMapping(value = "/createProject",method = RequestMethod.POST)
    public String createAndCheckProject(String data,String userid){
        String msg = "ok";
        if (null != data) {
            List<DjModel> djModels = JSONObject.parseArray(data,DjModel.class);
            if(StringUtils.isBlank(userid)){
                userid = "0";
            }
            msg = projectManageService.createProject(djModels,userid);
        }
        return msg;
    }
    @ResponseBody
    @RequestMapping("checkExcel")
    public String validateProject(String data){
        String msg = "";
        if (StringUtils.isNotBlank(data)) {
            List<DjModel> djModels = JSONObject.parseArray(data,DjModel.class);
            msg = projectManageService.validateProject(djModels);
        }
        return msg;
    }
    @ResponseBody
    @RequestMapping("getBackDataPagesJson")
    public Object getBackDataPages(Pageable pageable, @RequestParam(value = "sjh", required = false) String sjh,@RequestParam(value = "proids", required = false) String proids){
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> proidList;
        if (StringUtils.isNotBlank(sjh)){
            map.put("sjh",StringUtils.deleteWhitespace(sjh));
        }
        if (StringUtils.isNotBlank(proids)){
            proidList = Arrays.asList(proids.split("\\$"));
            map.put("proids",proidList);
        }
        return repository.selectPaging("getBackDataByPage", map, pageable);
    }
}
