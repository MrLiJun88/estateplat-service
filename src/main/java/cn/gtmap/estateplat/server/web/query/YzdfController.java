package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-12
 * @description 一证多房控制层
 */
@Controller
@RequestMapping("/yzdf")
public class YzdfController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXmService bdcXmService;

    @ResponseBody
    @RequestMapping("/yzdfYz")
    public Object yzdfYz(String proids) {
        Map map = Maps.newHashMap();
        List<String> yzdfProids = bdcXmService.getYzdfProids(Arrays.asList(StringUtils.split(proids, ",")));
        if (CollectionUtils.isNotEmpty(yzdfProids)) {
            map.put("wdrProids", yzdfProids);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/getFdcqxxByPage")
    public Object getBdcdyPagesJson(Pageable pageable, String proids) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("proidList", StringUtils.split(proids, ","));
        return repository.selectPaging("getFdcqxxByPage", map, pageable);
    }
}
