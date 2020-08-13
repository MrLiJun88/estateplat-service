package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDyMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-3-25
 * Time: 上午8:47
 * To change this template use File | Settings | File Templates. 、
 * doc:不动产登记簿查询
 */
@Controller
@RequestMapping("/bdcDjb")
public class BdcDjbController extends BaseController {

    @Autowired
    private Repo repository;
    @Autowired
    private BdcdjbService bdcdjbService;
    @Autowired
    private BdcDyMapper bdcDyMapper;
    @Autowired
    private CreatBdcDjbService creatBdcDjbService;
    @Autowired
    private BdcHsService bdcHsService;
    @Autowired
    private DjsjLpbService djsjLpbService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private BdcdyService bdcdyService;

    private static final String PARAMETER_VIEW_FCFHT_URL = "/dcxx/viewHst?bdcdyh=";
    private static final String PARAMETER_FCFHT_MC = "分层分户图";

    /*不动产登记簿查询**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        List<Map> bdcList = bdcZdGlService.getZdBdclx();
        String bdcdjbGjss = AppConfig.getProperty("bdcdjbGjss.order");
        List<String> bdcdjbGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(bdcdjbGjss) && bdcdjbGjss.split(",").length > 0){
            for(String bdcdjbGjssZd : bdcdjbGjss.split(",")){
                bdcdjbGjssOrderList.add(bdcdjbGjssZd);
            }
        }
        model.addAttribute("bdcList", bdcList);
        model.addAttribute("bdcdjbGjss", bdcdjbGjss);
        model.addAttribute("bdcdjbGjssOrderList", bdcdjbGjssOrderList);
        return "query/bdcDjbList";
    }

    @ResponseBody
    @RequestMapping(value = "/getbdcDjbPagesJson", method = RequestMethod.GET)
    public String getbdcDjbPagesJson(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "limit", required = false) String limit, @RequestParam(value = "pageIndex", required = false) String pageIndex) {
        HashMap<String, String> map = new HashMap<String, String>();
        return bdcdjbService.getJosnByDjbList(map);
    }

    /*不动产登记簿查询ACE**/
    @RequestMapping(value = "/djblistace", method = RequestMethod.GET)
    public String djblistace(Model model, String proid) {
        return "query/bdcdjb";
    }

    @ResponseBody
    @RequestMapping("/getbdcDjbPagesJsonace")
    public Object getbdcDjbPagesJsonace(Pageable pageable, String zdzhh, String zl, String dbr, String dcxc, String qlr) {
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(zdzhh)) {
                map.put("zdzhh", zdzhh);
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
            if (StringUtils.isNotBlank(dbr)) {
                map.put("dbr", dbr);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm)) {
            map.put("xzqdm", userDwdm);
        }
        String xmzts = "1,2";
        map.put("xmzts", xmzts.split(","));
        return repository.selectPaging("getBdcdjbByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getbdcdy")
    public JSONObject getBdcXmPagesJson(String djbid) {
        List<BdcDyForQuery> bdcBdcdyItems = bdcDyMapper.queryBdcdyByZdzhh(djbid);
        JSONObject jsonObject = new JSONObject();
        if (CollectionUtils.isNotEmpty(bdcBdcdyItems)) {
            jsonObject.put("rows", bdcBdcdyItems);
            return jsonObject;
        }
        return null;
    }

    /**
     * 不动产单元号分页数据
     *
     * @param pageable
     * @param sidx
     * @param sord
     * @param djh
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcDyhPagesJson")
    public Object getBdcDyhPagesJson(Pageable pageable, String sidx, String sord, String djh, String bdcdyh) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("djh", djh);
        map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        return repository.selectPaging("getBdcdyListByPage", map, pageable);
    }


    /**
     * 登记簿树
     * lst
     */
    @RequestMapping(value = "djb", method = RequestMethod.GET)
    public String djb(Model model, String djbid) {
        List<HashMap> list = new ArrayList<HashMap>();
        String zl = "";
        String zdtzm = "";
        if (StringUtils.isNotBlank(djbid)) {
            BdcBdcdjb bdcBdcdjb = bdcdjbService.getBdcBdcdjbByDjbid(djbid);
            if (bdcBdcdjb != null) {
                zl = bdcBdcdjb.getZl();
                if (StringUtils.isNotBlank(bdcBdcdjb.getZdzhh())) {
                    String json = djsjLpbService.getLpbMenu(bdcBdcdjb.getZdzhh());
                    if (StringUtils.isNotBlank(json))
                        list = bdcHsService.xmlToJson(json, djbid);
                    zdtzm = StringUtils.substring(bdcBdcdjb.getZdzhh(), 12, 14);
                }
            }
        }
        model.addAttribute("zdtzm", zdtzm);
        model.addAttribute("djbid", djbid);
        model.addAttribute("title", zl);
        model.addAttribute("list", list);
        return "query/djbquery";
    }

    /**
     * zx根据不动产单元号查看权利
     *
     * @param model
     * @param bdcdyh
     * @return
     */
    @RequestMapping(value = "/showQL")
    public String showQL(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "djbid", required = false) String djbid, @RequestParam(value = "fjh", required = false) String fjh, @RequestParam(value = "isql", required = false) String isql, @RequestParam(value = "showFcfht", required = false) String showFcfht, @RequestParam(value = "djid", required = false) String djid) {
        List<DjbQlPro> qllist = creatBdcDjbService.getQlByBdcdy(bdcdyh);
        List<DjbQlPro> newQllist = new ArrayList<DjbQlPro>();
        if (qllist != null) {
            //权利类型记录
            HashMap qlMap = new HashMap();
            for (DjbQlPro djbQlPro : qllist) {
                if (StringUtils.isNotBlank(djbQlPro.getTableName()) && (djbQlPro.getTableName().startsWith("gd_"))) {
                    String url = AppConfig.getProperty("qllxcpt.filepath");
                    url = url.replace("${tableName}", djbQlPro.getTableName().toLowerCase());
                    url = PlatformUtil.initOptProperties(url);
                    djbQlPro.setTableName(url + "&bdcid=" + djbQlPro.getQlid() + "&__showtoolbar__=false");
                } else if (StringUtils.isNotBlank(djbQlPro.getTableName())) {
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 如果是房地产权需要判断是否是多幢
                     */
                    if (StringUtils.equalsIgnoreCase(djbQlPro.getTableName(), Constants.BDC_FDCQ)) {
                        String fwlx = bdcDjsjService.getBdcdyfwlxByBdcdyh(bdcdyh);
                        if (StringUtils.equals(Constants.DJSJ_FWDZ_DM, fwlx)) {
                            djbQlPro.setTableName(StringUtils.upperCase(Constants.BDC_FDCQ_DZ));
                        }
                    }
                    //如果已存在该权利就不再该权利的记录了
                    if (qlMap.containsKey(djbQlPro.getQllx())) {
                        continue;
                    }
                    String startPage = creatBdcDjbService.getStartPageBy(bdcdyh, djbQlPro.getQllx());
                    String url = AppConfig.getProperty("qllxDjbCpt.filepath");
                    url = url.replace("${tableName}", djbQlPro.getTableName().toLowerCase());
                    url = PlatformUtil.initOptProperties(url);
                    djbQlPro.setTableName(url + "&bdcdyh=" + bdcdyh + "&djbid=" + djbid + "&startpage=" + startPage);
                    qlMap.put(djbQlPro.getQllx(), djbQlPro.getMc());
                }
                newQllist.add(djbQlPro);
            }
        }
        HashMap map = new HashMap();
        map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        if (StringUtils.isNotBlank(bdcdyh) && StringUtils.equals(showFcfht, "true")) {

            List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(map);
            if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                DjbQlPro djbQlPro = new DjbQlPro();
                djbQlPro.setMc(PARAMETER_FCFHT_MC);
                djbQlPro.setBdcdyh(bdcdyh);
                djbQlPro.setTableName(bdcdjUrl + PARAMETER_VIEW_FCFHT_URL + bdcdyh);
                newQllist.add(djbQlPro);
            } else {
                List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(map);
                if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
                    DjbQlPro djbQlPro = new DjbQlPro();
                    djbQlPro.setMc(PARAMETER_FCFHT_MC);
                    djbQlPro.setBdcdyh(bdcdyh);
                    djbQlPro.setTableName(bdcdjUrl + PARAMETER_VIEW_FCFHT_URL + bdcdyh);
                    newQllist.add(djbQlPro);
                } else {
                    List<DjsjFwXmxx> djsjFwXmxxList = djsjFwService.getDjsjFwXmxx(map);
                    if (CollectionUtils.isNotEmpty(djsjFwXmxxList)) {
                        DjbQlPro djbQlPro = new DjbQlPro();
                        djbQlPro.setMc(PARAMETER_FCFHT_MC);
                        djbQlPro.setBdcdyh(bdcdyh);
                        djbQlPro.setTableName(bdcdjUrl + PARAMETER_VIEW_FCFHT_URL + bdcdyh);
                        newQllist.add(djbQlPro);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(isql)) {
            model.addAttribute("isql", isql);
        }
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        model.addAttribute("qllist", newQllist);
        model.addAttribute("fjh", fjh);
        model.addAttribute("djid", djid);
        return "query/showQl";
    }

    /**
     * zx根据不动产单元号获取权利参数
     *
     * @param model
     * @param bdcdyh
     * @return
     */
    @RequestMapping(value = "/getQlJson")
    @ResponseBody
    public List<DjbQlPro> showQL(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        List<DjbQlPro> qllist = creatBdcDjbService.getQlByBdcdy(bdcdyh);
        List<DjbQlPro> qlFrlist = new ArrayList<DjbQlPro>();

        if (CollectionUtils.isNotEmpty(qllist)) {
            for (DjbQlPro qlPro : qllist) {
                //是bdc_fdcq需判断是否多幢
                if (StringUtils.isNotBlank(qlPro.getTableName())&&qlPro.getTableName().equals(StringUtils.upperCase(Constants.BDC_FDCQ))) {
                    String fwlx = bdcdyService.getBdcdyfwlxBybdcdyh(bdcdyh);
                    if (StringUtils.equals(Constants.DJSJ_FWDZ_DM, fwlx)) {
                        qlPro.setTableName(StringUtils.lowerCase(Constants.BDC_FDCQ_DZ));
                    }
                }
                qlFrlist.add(qlPro);
            }
        }
        return qlFrlist;
    }

    /**
     * zx根据不动产单元号获取权利页数
     *
     * @param model
     * @param bdcdyh
     * @return
     */
    @RequestMapping(value = "/getQlPageJson")
    @ResponseBody
    public HashMap getQlPageJson(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        return creatBdcDjbService.getQlPageByBdcdyh(bdcdyh);
    }

    /**
     * zx根据不动产单元号获取权利参数
     *
     * @param model
     * @param djbid
     * @return
     */
    @RequestMapping(value = "/getAllPrintQlJson")
    @ResponseBody
    public List<DjbQlPro> getAllPrintQlJson(Model model, @RequestParam(value = "djbid", required = false) String djbid, @RequestParam(value = "isPrintAll", required = false) String isPrintAll) {
        List<DjbQlPro> qlFrlist = new ArrayList<DjbQlPro>();
        int zys = 0;
        int i = 0;
        List<String> qzys = new ArrayList<String>();
        if (StringUtils.isNotBlank(djbid)) {
            HashMap map = new HashMap();
            map.put("djbid", djbid);
            List<Map> bdcdyMlList = bdcdjbService.getQldjByPage(map);
            //zwq 批量打印所有的时候，在头部会有不动产登记簿和宗地宗海信息2页
            if (StringUtils.equals(isPrintAll, "true")) {
                zys = 2;
            }
            qzys.add(String.valueOf(zys));
            if (CollectionUtils.isNotEmpty(bdcdyMlList)) {
                for (Map bdcdyMap : bdcdyMlList) {
                    if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(bdcdyMap.get(ParamsConstants.BDCDYH_CAPITAL)))) {
                        List<DjbQlPro> qllist = creatBdcDjbService.getQlByBdcdy(CommonUtil.formatEmptyValue(bdcdyMap.get(ParamsConstants.BDCDYH_CAPITAL)));
                        if (CollectionUtils.isNotEmpty(qllist)) {
                            DjbQlPro djbQlProFm = new DjbQlPro();
                            djbQlProFm.setTableName(Constants.FR_BDCDYXX_FM);
                            djbQlProFm.setBdcdyh(CommonUtil.formatEmptyValue(bdcdyMap.get(ParamsConstants.BDCDYH_CAPITAL)));
                            HashMap qlPage = creatBdcDjbService.getQlPageByBdcdyh(CommonUtil.formatEmptyValue(bdcdyMap.get(ParamsConstants.BDCDYH_CAPITAL)));
                            djbQlProFm.setQt(CommonUtil.formatEmptyValue(qlPage.get("qt")));
                            djbQlProFm.setDy(CommonUtil.formatEmptyValue(qlPage.get("dy")));
                            djbQlProFm.setDya(CommonUtil.formatEmptyValue(qlPage.get("dya")));
                            djbQlProFm.setYg(CommonUtil.formatEmptyValue(qlPage.get("yg")));
                            djbQlProFm.setYy(CommonUtil.formatEmptyValue(qlPage.get("yy")));
                            djbQlProFm.setCf(CommonUtil.formatEmptyValue(qlPage.get("cf")));
                            qlFrlist.add(djbQlProFm);
                            zys += Integer.parseInt(CommonUtil.formatEmptyValue(qlPage.get("zys")));
                            qzys.add(String.valueOf(zys));
                            for (DjbQlPro qlPro : qllist) {
                                qlPro.setBdcdyh(CommonUtil.formatEmptyValue(bdcdyMap.get(ParamsConstants.BDCDYH_CAPITAL)));
                                //zwq 获取前一本的总页数
                                qlPro.setZys(qzys.get(i));
                                if (StringUtils.isNotBlank(qlPro.getTableName())) {
                                    qlPro.setTableName(StringUtils.lowerCase(qlPro.getTableName()));
                                }
                                qlFrlist.add(qlPro);
                            }
                            i++;
                        }
                    }
                }
            }
        }
        return qlFrlist;
    }
}
