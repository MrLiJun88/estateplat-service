package cn.gtmap.estateplat.server.web.query;
/*
 * @author <a href="mailto:wangwenjun@gtmap.cn">wangwenjun</a>
 * @version 1.0, 2016/4/23
 * @description
 */

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;

@Controller
@RequestMapping("/bdcLZdj")
public class BdcLZdjContorller extends BaseController {
    @Autowired
    private Repo repository;
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        model.addAttribute("reporturl", reportUrl);
        return "query/bdcLzdj";
    }
    @ResponseBody
    @RequestMapping(value = "/getbdcLzdjMainPagesJsonace")
    public Object getbdcLzdjMainPagesJsonace(Pageable pageable, String dcxc, String bh, String qlr, String zl, String bdcqzh,@RequestParam(value = "lzrq", required = false) String lzrq ) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(bh)) {
                map.put("bh", bh);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
            if (StringUtils.isNotBlank(bdcqzh)) {
                map.put("bdcqzh", bdcqzh);
            }
            if (StringUtils.isNotBlank(lzrq)) {
                map.put("lzrq", lzrq);
            }

        }
        return repository.selectPaging("getbdcwlzdjByPage", map, pageable);
    }
    @ResponseBody
    @RequestMapping(value = "/getbdcLzdjPagesJsonace")
    public Object getbdcLzdjPagesJsonace(Pageable pageable, String dcxc, String bh, String qlr, String zl, String bdcqzh,@RequestParam(value = "lzzt", required = false) String lzzt ) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(bh))
                map.put("bh", bh);
            if (StringUtils.isNotBlank(qlr))
                map.put("qlr", qlr);
            if (StringUtils.isNotBlank(zl))
                map.put("zl", zl);
            if (StringUtils.isNotBlank(bdcqzh))
                map.put("bdcqzh", bdcqzh);
            if (StringUtils.isNotBlank(lzzt))
                map.put("lzzt", lzzt);

        }
        Page<HashMap> dataPaging=null;
        if(StringUtils.equals(lzzt,"wlz")) {
            dataPaging = repository.selectPaging("getbdcwlzdjByPage", map, pageable);
        }else if(StringUtils.equals(lzzt,"ylz")) {
             dataPaging = repository.selectPaging("getbdcylzdjByPage", map, pageable);
        }else{
           dataPaging = repository.selectPaging("getbdcLzdjByPage", map, pageable);
        }
        return dataPaging;
    }

}
