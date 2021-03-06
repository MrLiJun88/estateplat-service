package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yinyao
 * Date: 15-11-28
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/bdcdjbxx")
public class BdcdjbxxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcQlrService bdcQlrService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("reporturl", reportUrl);
        return "query/bdcdjbxx";
    }

    @ResponseBody
    @RequestMapping("/getQlrJsonace")
    public Object getbdcCfPagesJsonace(Pageable pageable, String xm, String zs, String zl) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("xm", StringUtils.deleteWhitespace(xm));
        map.put("zs", StringUtils.deleteWhitespace(zs));
        map.put("zl", StringUtils.deleteWhitespace(zl));
        return repository.selectPaging("getQlrByPage", map,pageable);

    }

    @ResponseBody
    @RequestMapping("/getBdcdyidByProid")
    public HashMap getBdcdyidByProid(String qlr, String qlrzjh) throws Exception {
        qlr = URLDecoder.decode(qlr, Constants.DEFAULT_CHARSET);
        HashMap resultmap = new HashMap();
        HashMap map = new HashMap();
        map.put("qlr", qlr);
        map.put("qlrzjh", qlrzjh);
        List<Map> bdcdyidlist = bdcQlrService.getBdcdyidByProid(map);
        StringBuilder bdcdyid = new StringBuilder();
        StringBuilder qlrid =  new StringBuilder();
        if (CollectionUtils.isNotEmpty(bdcdyidlist)) {
            for (int i = 0; i < bdcdyidlist.size(); i++) {
                bdcdyid.append(bdcdyidlist.get(i).get("BDCDYID")).append(",");
                qlrid.append(bdcdyidlist.get(i).get("QLRID")).append(",");
            }
        }
        resultmap.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid.substring(0, bdcdyid.length() - 1));
        resultmap.put("qlrid", qlrid.substring(0, qlrid.length() - 1));
        return resultmap;
    }
    @ResponseBody
    @RequestMapping("/getBdcdyhxx")
    public Object getBdcdyhxx(String bdcdyid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ParamsConstants.BDCDYID_LOWERCASE, StringUtils.deleteWhitespace(bdcdyid));
        return repository.selectOne("getBdcBdcdyXxByBdcdyid", map);
    }
}

