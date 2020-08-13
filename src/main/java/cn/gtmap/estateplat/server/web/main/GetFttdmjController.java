package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.GetFttdmjService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lj
 * Date: 15-10-19
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/getFttdmj")
public class GetFttdmjController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private GetFttdmjService getFttdmjService;


    @RequestMapping("/toGetFttdmj")
    public String toGdsjgl(Model model) {
        return "sjgl/fttdmj";
    }

    @ResponseBody
    @RequestMapping("/getFdcqZdJsonByPage")
    public Object getFdcqZdJsonByPage(Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //混合查询
        map.put("hhSearch", StringUtils.deleteWhitespace(hhSearch));
        return repository.selectPaging("getFdcqZdJosnByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getFdcqJsonByPage")
    public Object getFdcqJsonByPage(Pageable pageable, String djh) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("djh", djh);
        return repository.selectPaging("getFdcqJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/jsFttdmj")
    public String jsFttdmj(String djh) {
        return getFttdmjService.calculateFttdmj(djh);
    }
}
