package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcJjdXx;
import cn.gtmap.estateplat.server.core.service.ArchiveExchangeSecivce;
import cn.gtmap.estateplat.server.core.service.BdcJjdService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-12-10
 * Time: 下午3:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/archiveExchange")
public class ArchiveExchangeConntroller extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcJjdService bdcJjdService;
    @Autowired
    private ArchiveExchangeSecivce archiveExchangeSecivce;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "main/archiveExchange";
    }

    @ResponseBody
    @RequestMapping("/getJjdxx")
    public Object getJjdxx(Pageable pageable, String hhSearch, String bhs) {
        HashMap map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(bhs)) {
            String[] strArrays = bhs.split(",");
            if (strArrays != null && strArrays.length > 0) {
                map.put("bhs", strArrays);
            }
        }
        return repository.selectPaging("getJjdxxJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/creatJjd")
    public String creatJjd(String slh) {
        return bdcJjdService.creatBdcJjd(slh, super.getUserName());
    }

    @ResponseBody
    @RequestMapping("/getjjdid")
    public String getjjdid(String slh) {
        String jjdid = "";
        BdcJjdXx bdcJjdXx = bdcJjdService.getBdcJjdXxBySlh(slh);
        if (bdcJjdXx != null) {
            jjdid = bdcJjdXx.getJjdid();
        }
        return jjdid;
    }

    @RequestMapping(value = "jjdQuery", method = RequestMethod.GET)
    public String jjdQuery() {
        return "main/jjdQuery";
    }

    @ResponseBody
    @RequestMapping("/getJjd")
    public Object getJjd(Pageable pageable, String hhSearch, String jjdbhs) {
        HashMap map = new HashMap<String, String>();

        if (StringUtils.isNotBlank(jjdbhs)) {
            String[] strArrays = jjdbhs.split(",");
            if (strArrays != null && strArrays.length > 0) {
                map.put("jjdbhs", strArrays);
            }
        }
        return repository.selectPaging("getJjdJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/gdjjd")
    public String gdjjd(String jjdbhs) {
        String msg = null;
        try {
            msg = archiveExchangeSecivce.gdjjd(jjdbhs, archiveUrl);
        } catch (IOException e) {
            logger.error("ArchiveExchangeConntroller.gdjjd",e);
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/deletejjd")
    public String deletejjd(String jjdid) {
        return bdcJjdService.deleteJjd(jjdid);
    }

    @RequestMapping(value = "queryGd", method = RequestMethod.GET)
    public String queryGd() {
        return "main/queryGd";
    }

}
