package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import cn.gtmap.estateplat.model.server.core.BdcZdSqlx;
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
 * Created by IntelliJ IDEA.
 * User: sc 权利历史查询
 * Date: 15-4-27
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryQlls")
public class QueryQllsController extends BaseController {
    @Autowired
    private Repo repository;


    /*权利历史查询**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        List<BdcZdQllx> qllxList = bdcZdGlService.getBdcQllx();
        List<BdcZdSqlx> sqlxList = bdcZdGlService.getSqlxOrderbyDm();
        model.addAttribute("qllxList", qllxList);
        model.addAttribute("sqlxList", sqlxList);
        return "query/queryQlls";
    }

    @ResponseBody
    @RequestMapping("/queryQllxByPageJsonace")
    public Object queryQllxByPageJsonace(Pageable pageable, String bdcdyh, String zl, String sqlx, String dcxc, String bdcqzh, String qllx, String qlr) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put("bdcdyh", bdcdyh);
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
            if (StringUtils.isNotBlank(sqlx)) {
                map.put("sqlx", sqlx);
            }
            if (StringUtils.isNotBlank(bdcqzh)) {
                map.put("bdcqzh", bdcqzh);
            }
            if (StringUtils.isNotBlank(qllx)) {
                map.put("qllx", qllx);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
        }
        return repository.selectPaging("queryQllxByPage", map, pageable);
    }
}
