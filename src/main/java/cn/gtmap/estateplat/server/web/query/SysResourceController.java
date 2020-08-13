package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcGdxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import cn.gtmap.estateplat.server.core.service.BdcGdxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zx on 2015/9/18.
 * des:资源url访问过滤
 */
@Controller
@RequestMapping("/sysResource")
public class SysResourceController extends BaseController {
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private BdcGdxxService bdcGdxxService;

    @RequestMapping(value = "/filterCpt", method = RequestMethod.GET)
    public ModelAndView filterDay(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "cptName", required = false) String cptName, @RequestParam(value = "path", required = false) String path, @RequestParam(value = "mulPath", required = false) String mulPath, @RequestParam(value = "wiid", required = false) String wiid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuffer urlBuf = request.getRequestURL();
        String queryString = request.getQueryString();
        String url = bdcdjUrl + "/index/toError";
        if (urlBuf != null) {
            url = urlBuf.toString();
            //参数修改为wiid @gtsy
            //取消注释，if判断
            List<BdcBdcdy> bdcBdcdyList = null;
            if (StringUtils.isNotBlank(wiid)) {
                //jyl 过滤不动产房屋附属设施
                bdcBdcdyList = bdcdyService.queryBdcBdcdyFilterBdcFwfsss(wiid);
            } else if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid()))
                    //jyl 过滤不动产房屋附属设施
                    bdcBdcdyList = bdcdyService.queryBdcBdcdyFilterBdcFwfsss(bdcXm.getWiid());
            }
            if ((bdcBdcdyList != null && bdcBdcdyList.size() > 1) || "bdc_bdcdy_list".equals(cptName)) {
                if (StringUtils.isNotBlank(queryString) && StringUtils.indexOf(queryString, "&op=write") > -1)
                    url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\" + path + "\\" + mulPath + "\\" + cptName + "&op=write&cptName=" + cptName + StringUtils.substring(queryString, StringUtils.indexOf(queryString, "&op=write") + 9, queryString.length());
            } else {
                if (StringUtils.isNotBlank(queryString) && StringUtils.indexOf(queryString, "&op=write") > -1)
                    url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\" + path + "\\" + cptName + "&op=write&cptName=" + cptName + StringUtils.substring(queryString, StringUtils.indexOf(queryString, "&op=write") + 9, queryString.length());
            }
            url = url + "&ywType=server";
        }
        response.sendRedirect(url);
        return null;
    }

    @RequestMapping(value = "/filterCptByCqgs", method = RequestMethod.GET)
    public ModelAndView filterCptByCqgs(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "cptName", required = false) String cptName, @RequestParam(value = "path", required = false) String path, @RequestParam(value = "mulPath", required = false) String mulPath, @RequestParam(value = "wiid", required = false) String wiid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuffer urlBuf = request.getRequestURL();
        String queryString = request.getQueryString();
        String url = bdcdjUrl + "/index/toError";
        String cqgs = "";
        String qllx = "";
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (bdcXmList != null && bdcXmList.size() > 0) {
            cqgs = bdcXmList.get(0).getCqgs();
            qllx = bdcXmList.get(0).getQllx();
        }
        if (urlBuf != null) {
            url = urlBuf.toString();
            //参数修改为wiid @gtsy
            //取消注释，if判断
            List<BdcBdcdy> bdcBdcdyList = null;
            if (StringUtils.isNotBlank(wiid))
                bdcBdcdyList = bdcdyService.queryBdcBdcdy(wiid);
            else if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid()))
                    bdcBdcdyList = bdcdyService.queryBdcBdcdy(bdcXm.getWiid());
            }
            if (StringUtils.isNotBlank(queryString) && StringUtils.indexOf(queryString, "&op=write") > -1) {
                if (StringUtils.equals(cptName, "bdc_sqs") && (StringUtils.equals(qllx, Constants.QLLX_DYAQ) || StringUtils.equals(qllx, Constants.QLLX_DYQ)))
                    url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\" + path + "\\" + cptName + "_dy" + "&op=write&cptName=" + cptName + StringUtils.substring(queryString, StringUtils.indexOf(queryString, "&op=write") + 9, queryString.length());
                else if (StringUtils.equals(cqgs, Constants.CQGS_DW))
                    url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\" + path + "\\" + cptName + "_dw" + "&op=write&cptName=" + cptName + StringUtils.substring(queryString, StringUtils.indexOf(queryString, "&op=write") + 9, queryString.length());
                else
                    url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\" + path + "\\" + cptName + "_gr" + "&op=write&cptName=" + cptName + StringUtils.substring(queryString, StringUtils.indexOf(queryString, "&op=write") + 9, queryString.length());
            }
            url = url + "&ywType=server";
        }
        response.sendRedirect(url);
        return null;
    }

    @RequestMapping(value = "/sendUrl", method = RequestMethod.GET)
    public void sendUrl(@RequestParam(value = "cptName", required = false) String cptName, @RequestParam(value = "path", required = false) String path, @RequestParam(value = "mulPath", required = false) String mulPath, @RequestParam(value = "op", required = false) String op, @RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "updateQllx", required = false) String updateQllx, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, HttpServletResponse response) throws Exception {
        String url = "";
        if (StringUtils.isNoneBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                if (bdcXmList.size() == 1) {
                    url = bdcdjUrl + "/sysResource/filterCptByCqgs";
                    if (StringUtils.equals(cptName, "bdc_sjd"))
                        url = bdcdjUrl + "/sysResource/filterCpt";
                } else {
                    url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=%2Fedit%2F" + cptName + "_list&op=write&cptName=" + cptName + "_list";
                    //jyl批量流程收件单用mul下的。
                    if (StringUtils.equals(cptName, "bdc_sjd")) {
                        List<BdcBdcdy> bdcBdcdyList = null;
                        if (StringUtils.isNotBlank(wiid))
                            bdcBdcdyList = bdcdyService.queryBdcBdcdy(wiid);
                        if (bdcBdcdyList != null && bdcBdcdyList.size() > 1)
                            url = bdcdjUrl + "/sysResource/filterCpt";
                    }
                }
            } else {
                url = bdcdjUrl + "/index/toError";
            }
        }
        if (StringUtils.isNotBlank(taskid)) {
            if (StringUtils.indexOf(url, "?") > -1)
                url = url + "&cptName=" + cptName + "&path=" + path + "&mulPath=" + mulPath + "&op=" + op + "&proid=" + proid + "&ywType=server&rid=" + rid + "&from=" + from + "&wiid=" + wiid + "&__showtoolbar__=" + __showtoolbar__ + "&taskid=" + taskid;
            else
                url = url + "?cptName=" + cptName + "&path=" + path + "&mulPath=" + mulPath + "&op=" + op + "&proid=" + proid + "&ywType=server&rid=" + rid + "&from=" + from + "&wiid=" + wiid + "&__showtoolbar__=" + __showtoolbar__ + "&taskid=" + taskid;
        } else {
            if (StringUtils.indexOf(url, "?") > -1)
                url = url + "&cptName=" + cptName + "&path=" + path + "&mulPath=" + mulPath + "&op=" + op + "&proid=" + proid + "&ywType=server&rid=" + rid + "&from=" + from + "&wiid=" + wiid + "&__showtoolbar__=" + __showtoolbar__;
            else
                url = url + "?cptName=" + cptName + "&path=" + path + "&mulPath=" + mulPath + "&op=" + op + "&proid=" + proid + "&ywType=server&rid=" + rid + "&from=" + from + "&wiid=" + wiid + "&__showtoolbar__=" + __showtoolbar__;
        }
        response.sendRedirect(url);
    }

    @RequestMapping(value = "/cqzxArchive", method = RequestMethod.GET)
    public void cqzxArchive(@RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) throws Exception {
        String url = archiveUrl + "/package.action?modelName=";
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null && StringUtils.isNoneBlank(bdcXm.getBh())) {
            String modelName = "";
            BdcGdxx bdcGdxx = bdcGdxxService.queryBdcGdxxByProid(proid);
            if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getDaid())) {
                modelName = bdcGdxx.getModelname();
            }
            if (StringUtils.isBlank(modelName) && StringUtils.isNotBlank(bdcXm.getSqlx())) {
                HashMap map = new HashMap();
                map.put("dm", bdcXm.getSqlx());
                List<HashMap> mapList = bdcZdGlService.getBdcZdSqlx(map);
                if (CollectionUtils.isNotEmpty(mapList) && mapList.get(0).containsKey("ARCHIVE_NAME") && StringUtils.isNotBlank((CharSequence) mapList.get(0).get("ARCHIVE_NAME"))) {
                    modelName = String.valueOf(mapList.get(0).get("ARCHIVE_NAME"));
                }
            }
            if (StringUtils.isBlank(modelName) && StringUtils.isNotBlank(bdcXm.getQllx())) {
                HashMap map = new HashMap();
                map.put("dm", bdcXm.getQllx());
                List<BdcZdQllx> bdcZdQllxList = bdcZdGlService.getBdcZdQllx(map);
                if (CollectionUtils.isNotEmpty(bdcZdQllxList) && StringUtils.isNotBlank(bdcZdQllxList.get(0).getArchiveName())) {
                    modelName = bdcZdQllxList.get(0).getArchiveName();
                }
            }
            if (StringUtils.isNotBlank(modelName)) {
                url += modelName;
            }
            url += "&ywh=" + bdcXm.getBh();
        }
        response.sendRedirect(url);
    }

}
