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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * 查询更正登记通知书
 * User: wangchangzhou
 * Date: 16-04-18
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryGzdjTzs")
public class QueryGzdjTzsContorller extends BaseController {

    @Autowired
    private Repo repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "query/gzdjTzsList";
    }

    /**
     * 查询更正登记通知书
     * @param pageable
     * @param startTime
     * @param endTime
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGzdjTzsPagesJson")
    public Object getTdsyqPagesJson(Pageable pageable, String startTime, String endTime) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotBlank(startTime)) {
                map.put("startTime", simpleDateFormat.parse(startTime));
            }
            if (StringUtils.isNotBlank(endTime)) {
                map.put("endTime", simpleDateFormat.parse(endTime));
            }
        } catch (ParseException e) {
            logger.error("QueryGzdjTzsContorller.getGzdjTzsPagesJson",e);
        }
        return repository.selectPaging("getGzdjTzsByPage", map, pageable);
    }

}
