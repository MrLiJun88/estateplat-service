package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.BdcLshService;
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
 * Created by IntelliJ hqz.
 * User: lst 不予受理通知书
 */
@Controller
@RequestMapping("/bdcByslTzs")
public class BdcByslTzsController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcLshService bdcLshService;
    /*不予受理通知书列表**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "query/bdcByslList";
    }

    /**
     * 不予受理通知书查询接口
     * @param pageable
     * @param startTime
     * @param endTime
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryBdcByslList")
    public Object queryCfdjList(Pageable pageable, String startTime, String endTime) {
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
            logger.error("BdcByslTzsController.queryCfdjList",e);
        }
        return repository.selectPaging("selectBdcByslTzsByPage", map, pageable);
    }


    /**
     * @param
     * @return
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 生成流水号
     */
    @ResponseBody
    @RequestMapping(value = "/getBh")
    public HashMap<String, String> getBh(String userid,String bhlxmc) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String bh = null;
        if (StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(bhlxmc)) {
            bh = bdcLshService.getBh(userid,bhlxmc);
        }
        hashMap.put("thwsLsh", bh);
        return  hashMap;
    }
}
