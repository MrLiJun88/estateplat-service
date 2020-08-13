package cn.gtmap.estateplat.server.web.query;


import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import cn.gtmap.estateplat.server.core.service.BdcZdQllxService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fq
 * Date: 15-4-26
 * Time: 下午3:43
 * doc:不动产权利登记目录查询
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcqldj")
public class BdcqldjController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcZdQllxService bdcZdQllxService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String djbid) {
        List<BdcZdQllx> qllxList = bdcZdQllxService.getAllBdcZdQllx();
        model.addAttribute("qllxList", qllxList);
        model.addAttribute("djbid", djbid);
        return "query/bdcQldjList";
    }

    @ResponseBody
    @RequestMapping("/getQldjPagesJson")
    public Object getQldjPagesJson(Pageable pageable, String djbid, String bdcdyh) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(djbid)) {
            map.put("djbid", djbid);
        }
        if (StringUtils.isNotBlank(bdcdyh)) {
            map.put("bdcdyh", bdcdyh);
        }
        return repository.selectPaging("getQldjByPage", map, pageable);
    }

}
