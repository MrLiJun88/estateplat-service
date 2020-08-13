package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: zx 查看房产交易业务
 */
@Controller
@RequestMapping("/fcjyFcYw")
public class FcjyFcYwController extends BaseController {
    @Autowired
    private Repo repository;


    /*查封列表**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "query/fcjyFcYwList";
    }

    @ResponseBody
    @RequestMapping("/queryFcjyFcYwList")
    public Object queryFcjyFcYwList(Pageable pageable,  String gzdh) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("gzdh", gzdh);
        return repository.selectPaging("getFcjyFcYwByPage", map, pageable);
    }
}
