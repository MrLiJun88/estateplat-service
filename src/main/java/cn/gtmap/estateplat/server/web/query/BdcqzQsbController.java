package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
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

/**
 * Created with IntelliJ IDEA.
 * User: trr
 * Date: 15-12-4
 * Time: 下午3:56
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcqzqsb")
public class BdcqzQsbController extends BaseController {

    @Autowired
    private Repo repository;

    /*不动产权证签收簿查询**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        return "query/bdcqzQsbList";
    }
}