package cn.gtmap.estateplat.server.web.analysis;

import cn.gtmap.estateplat.server.core.service.BdcGdxxService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
 * @version 1.0, 2018/10/11
 * @description 查询移交清单生成统编号
 */

@RequestMapping("/analysis")
public class AnalysisController extends BaseController{
    @Autowired
    BdcGdxxService bdcGdxxService;
    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param bh
     *@return
     *@description 生成虚拟编号
     */
    @ResponseBody
    @RequestMapping(value = "/creatJjdbh",method = RequestMethod.GET)
    public Map<String,String> creatJjdbh(String bh){
        HashMap<String,String> map = new HashMap<String, String>();
        if(StringUtils.isNotBlank(bh)){
            List<String> bhList = new ArrayList<String>();
            String[] bhArr = bh.split(",");
            Collections.addAll(bhList, bhArr);
            map = bdcGdxxService.updateJjdbh(bhList);
        }else{
            map.put("success","false");
            map.put("message","受理编号为空，生成统编号失败！");
        }
        return map;
    }
}
