package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.model.server.core.BdcBdcdjb;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.DjbQlPro;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-10-31
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcDjbProject")
public class BdcDjbProjectController {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcdjbService bdcdjbService;
    @Autowired
    private DjsjLpbService djsjLpbService;
    @Autowired
    private BdcHsService bdcHsService;
    @Autowired
    private CreatBdcDjbService creatBdcDjbService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, String updateZs, HttpServletResponse response) {
        //参数修改为wiid @gtsy
        List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdy(wiid);
        String djbid = "";
        if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {

            List<HashMap> list = new ArrayList<HashMap>();
            List<HashMap> lists = new ArrayList<HashMap>();
            HashMap<String, String> map = new HashMap<String, String>();
            String bdcdyh = "";
            String zl = "";
            String djh = "";
            List<BdcBdcdjb> bdcBdcdjbList = bdcdjbService.selectBdcdjb(StringUtils.substring(bdcBdcdyList.get(0).getBdcdyh(), 0, 19));
            BdcBdcdjb bdcBdcdjb = null;
            if (CollectionUtils.isNotEmpty(bdcBdcdjbList))
                bdcBdcdjb = bdcBdcdjbList.get(0);
            if (bdcBdcdjb != null) {
                djbid = bdcBdcdjb.getDjbid();
                //zwq 通过djbid查询bdclx和bdcdyh
                map.put("djbid", djbid);
                map.put("xmzt", "1");
                List<Map> hashlist = bdcdjbService.getQldjByPage(map);
                map.clear();
                if (CollectionUtils.isNotEmpty(hashlist)) {
                    for (int i = 0; i != hashlist.size(); i++) {
                        map = (HashMap<String, String>) hashlist.get(i);
                        if (StringUtils.equals(map.get("BDCLX"), "土地")) {
                            bdcdyh = map.get("BDCDYH");
                        }
                    }
                }

                zl = bdcBdcdjb.getZl();
                if (StringUtils.isNotBlank(bdcBdcdjb.getZdzhh())) {
                    djh = bdcBdcdjb.getZdzhh();
                    if (bdcBdcdyList.size() > 1) {
                        String json = djsjLpbService.getLpbMenu(bdcBdcdjb.getZdzhh());
                        if (StringUtils.isNotBlank(json)) {
                            list = bdcHsService.xmlToJson(json, djbid);
                        }
                        if (CollectionUtils.isNotEmpty(list)) {
                            lists.add(list.get(0));
                        }
                        model.addAttribute("list", lists);
                        model.addAttribute("havaBdcdy", "true");
                    } else {
                        list = getlpb(bdcBdcdyList.get(0).getBdcdyh());
                        HashMap qlPageMap = creatBdcDjbService.getQlPageByBdcdyh(bdcBdcdyList.get(0).getBdcdyh());
                        model.addAttribute("qlPageMap", qlPageMap);
                        model.addAttribute("list", list);
                        model.addAttribute("bdcdyhdy", bdcBdcdyList.get(0).getBdcdyh());
                        model.addAttribute("havaBdcdy", "false");
                    }
                }
            }
            model.addAttribute("djbid", djbid);
            model.addAttribute("djh", djh);
            model.addAttribute("title", zl);
            model.addAttribute("bdcdyh", bdcdyh);
        }
        return "query/djbProject";
    }

    public List getlpb(String bdcdyh) {
        List<DjbQlPro> qllist = creatBdcDjbService.getQlByBdcdy(bdcdyh);
        List<DjbQlPro> newQllist = new ArrayList<DjbQlPro>();
        HashMap qlPageMap = creatBdcDjbService.getQlPageByBdcdyh(bdcdyh);
        Iterator iter = qlPageMap.entrySet().iterator();
        String dyStr = "", dyaStr = "", ygStr = "", yyStr = "", cfStr = "", qtStr = "";
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            if (key == "dy") {
                dyStr = String.valueOf(entry.getValue());
            } else if (key == "dya") {
                dyaStr = String.valueOf(entry.getValue());
            } else if (key == "yg") {
                ygStr = String.valueOf(entry.getValue());
            } else if (key == "yy") {
                yyStr = String.valueOf(entry.getValue());
            } else if (key == "cf") {
                cfStr = String.valueOf(entry.getValue());
            } else if (key == "qt") {
                qtStr = String.valueOf(entry.getValue());
            }
        }

        if (qllist != null) {
            for (DjbQlPro djbQlPro : qllist) {
                DjbQlPro qlPro = new DjbQlPro();
                if (StringUtils.isNotBlank(djbQlPro.getTableName())) {
                    String qlStartPage = "";
                    if (djbQlPro.getQllx().equals("17")) {
                        if (dyStr != null) {
                            qlStartPage = dyStr.split(",")[0];
                        }
                    } else if (djbQlPro.getQllx().equals("18")) {
                        if (dyaStr != null) {
                            qlStartPage = dyaStr.split(",")[0];
                        }
                    } else if (djbQlPro.getQllx().equals("19")) {
                        if (ygStr != null) {
                            qlStartPage = ygStr.split(",")[0];
                        }
                    } else if (djbQlPro.getQllx().equals("20")) {
                        if (yyStr != null) {
                            qlStartPage = yyStr.split(",")[0];
                        }
                    } else if (djbQlPro.getQllx().equals("21")) {
                        if (cfStr != null) {
                            qlStartPage = cfStr.split(",")[0];
                        }
                    } else {
                        if (qtStr != null) {
                            qlStartPage = qtStr.split(",")[0];
                        }
                    }

                    String url = AppConfig.getProperty("qllxDjbCpt.filepath");
                    if (djbQlPro.getTableName().toLowerCase().equals("bdc_fdcq")) {
                        String qlLastPage = "";
                        if (StringUtils.isNotBlank(qtStr) && StringUtils.indexOf(qtStr, ",") > -1) {
                            qlLastPage = qtStr.split(",")[qtStr.split(",").length - 1];
                        }
                        qlPro.setMc("建筑物区分所有权业主共有部分登记信息");
                        qlPro.setBdcdyh(bdcdyh);
                        qlPro.setQllx(djbQlPro.getQllx() + "gy");
                        String qlProUrl = url.replace("${tableName}", "bdc_jzwqfsyqgyyz");
                        qlProUrl = PlatformUtil.initOptProperties(qlProUrl);
                        qlPro.setTableName(qlProUrl + "&bdcdyh=" + bdcdyh + "&startpage=" + qlLastPage);
                    }
                    url = url.replace("${tableName}", djbQlPro.getTableName().toLowerCase());
                    url = PlatformUtil.initOptProperties(url);
                    djbQlPro.setTableName(url + "&bdcdyh=" + bdcdyh + "&startpage=" + qlStartPage);

                }
                newQllist.add(djbQlPro);
                if (StringUtils.isNotBlank(qlPro.getTableName())) {
                    newQllist.add(qlPro);
                }
            }
        }
        return newQllist;
    }
}
