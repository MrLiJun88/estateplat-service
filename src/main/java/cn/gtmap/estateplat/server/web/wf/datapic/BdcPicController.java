package cn.gtmap.estateplat.server.web.wf.datapic;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.core.support.xml.XmlUtils;
import cn.gtmap.estateplat.model.exchange.share.GxWwSqxx;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.HttpClientUtil;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/24 0024
 * @description
 */
@Controller
@RequestMapping("/bdcpic")
public class BdcPicController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcPicService bdcPicService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcPpgxService bdcPpgxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcPpgxLogService bdcPpgxLogService;
    @Autowired
    private BdcdyService bdcdyService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proids, String wwslbh) {
        bdcPicService.initBdcPic(model, proids);
        model.addAttribute("wwslbh", wwslbh);
        return "/wf/dataPic/bdcPic";
    }

    @ResponseBody
    @RequestMapping(value = "/getDjsjBdcdyByPage")
    public Object getDjsjBdcdyByPage(Pageable pageable, String zl, String bdcdyh, String fwdm, String bdclx) {
        Map param = Maps.newHashMap();
        zl = StringUtils.deleteWhitespace(zl);
        bdcdyh = StringUtils.deleteWhitespace(bdcdyh);
        fwdm = StringUtils.deleteWhitespace(fwdm);
        bdclx = StringUtils.deleteWhitespace(bdclx);
        if (StringUtils.isNoneBlank(zl)) {
            param.put("tdzl", zl);
        }
        if (StringUtils.isNoneBlank(bdcdyh)) {
            param.put("bdcdyh", bdcdyh);
        }
        if (StringUtils.isNoneBlank(fwdm)) {
            param.put("fwdm", fwdm);
        }
        if (StringUtils.isBlank(bdclx)) {
            bdclx = "TDFW";
        }
        param.put("bdclx", bdclx);
        return repository.selectPaging("getDjsjBdcdyByPage", param, pageable);
    }


    @ResponseBody
    @RequestMapping(value = "/getBdcZsByPage", method = RequestMethod.GET)
    public Object getBdcZsByPage(Pageable pageable, String zl, String bdcdyh, String qlr, String bdcqzh, String tdProid) {
        Map param = Maps.newHashMap();
        zl = StringUtils.deleteWhitespace(zl);
        bdcdyh = StringUtils.deleteWhitespace(bdcdyh);
        qlr = StringUtils.deleteWhitespace(qlr);
        bdcqzh = StringUtils.deleteWhitespace(bdcqzh);
        if (StringUtils.isNotBlank(bdcqzh) || StringUtils.isNotBlank(qlr)) {
            List<String> proidList = bdcXmService.getProid(bdcqzh, qlr);
            param.put("proidList", CollectionUtils.isNotEmpty(proidList) ? proidList : Lists.newArrayList());
        }
        if (StringUtils.isNoneBlank(zl)) {
            param.put("zl", zl);
        }
        if (StringUtils.isNoneBlank(bdcdyh)) {
            param.put("bdcdyh", bdcdyh);
        }
        if (StringUtils.isNoneBlank(tdProid)) {
            param.put("proid", tdProid);
        }
        return repository.selectPaging("getBdcTdZsByPage", param, pageable);
    }

    @ResponseBody
    @RequestMapping(value = "/getYwsjxx")
    public Object getYwsjxx(@RequestParam(value = "proid", required = false) String proid) {
        Map map = Maps.newHashMap();
        String qlrmc = bdcQlrService.getQlrmcByProid(proid, Constants.QLRLX_QLR);
        Map zsmap = bdcZsService.queryBdcqzhByProid(proid);
        if (MapUtils.isNotEmpty(zsmap)) {
            if (zsmap.containsKey("BDCQZH")) {
                map.put("bdcqzh", zsmap.get("BDCQZH"));
            }
            if (zsmap.containsKey("ZSTYPE")) {
                map.put("zstype", zsmap.get("ZSTYPE"));
            }
        }
        map.put("qlrmc", qlrmc);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/qrPic")
    public Object qrPic(String bdcPpgxLogJson, String bdclx,String sfhqqj) {
        BdcPpgxLog bdcPpgxLog = JSON.parseObject(bdcPpgxLogJson, BdcPpgxLog.class);
        Map map = bdcPicService.bdcdyMatch(bdcPpgxLog, PlatformUtil.getCurrentUserId(), bdclx,sfhqqj);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/checkPic")
    public Object checkPic(String bdcPpgxLogJson, String bdclx) {
        BdcPpgxLog bdcPpgxLog = JSON.parseObject(bdcPpgxLogJson, BdcPpgxLog.class);
        Map map = bdcPicService.checkPic(bdcPpgxLog, bdclx);
        return map;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 匹配关系查询台账
     */
    @RequestMapping(value = "/showPpgx", method = RequestMethod.GET)
    public String showPpgx(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, String wwslbh) {
        List<Map> bdclxList = bdcZdGlService.getZdBdclx();
        model.addAttribute("bdclxList", bdclxList);
        model.addAttribute("bdcdyh", bdcdyh);
        model.addAttribute("wwslbh", wwslbh);
        return "/wf/dataPic/showPpgx";
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 查询
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcZsListByPage", method = RequestMethod.GET)
    public Object getBdcZsListByPage(Pageable pageable, String zl, String bdcdyh, String qlr, String bdcqzh, String fwbm, String bdclx, String cqzhjc, String exactQuery) {
        Map param = Maps.newHashMap();
        if (StringUtils.isNotBlank(bdcqzh)) {
            param.put(ParamsConstants.BDCQZH_LOWERCASE, StringUtils.deleteWhitespace(bdcqzh));
        }
        if (StringUtils.isNotBlank(bdcdyh)) {
            param.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
        }
        if (StringUtils.isNotBlank(qlr)) {
            param.put(Constants.QLRLX_QLR, StringUtils.deleteWhitespace(qlr));
        }
        if (StringUtils.isNotBlank(zl)) {
            param.put(ParamsConstants.ZL_LOWERCASE, StringUtils.deleteWhitespace(zl));
        }
        if (StringUtils.isNotBlank(cqzhjc)) {
            param.put(ParamsConstants.CQZHJC_LOWERCASE, StringUtils.deleteWhitespace(cqzhjc));
        }
        if (StringUtils.isNotBlank(bdclx)) {
            param.put(ParamsConstants.BDCLX_LOWERCASE, StringUtils.deleteWhitespace(bdclx));
        }
        // 根据fwbm定位bdcdyh
        if (StringUtils.isNotBlank(fwbm)) {
            List<String> bdcdyhList = new ArrayList<String>();
            List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdyByFwbm(fwbm);
            if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {
                for (BdcBdcdy bdcBdcdy : bdcBdcdyList) {
                    if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                        bdcdyhList.add(bdcBdcdy.getBdcdyh());
                    }
                }
            } else {
                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHsByFwbm(fwbm);
                if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                    for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                        if (djsjFwHs != null && StringUtils.isNotBlank(djsjFwHs.getBdcdyh())) {
                            bdcdyhList.add(djsjFwHs.getBdcdyh());
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(bdcdyhList)) {
                param.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
            }
        }
        // 过滤已注销
        param.put("qszt", Constants.QLLX_QSZT_HR);
        param.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        param.put("spfZyZsAndScdjz", ParamsConstants.TRUE_LOWERCASE);
        return repository.selectPaging("getBdcZsOptimizeByPage", param, pageable);
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取匹配状态
     */
    @ResponseBody
    @RequestMapping(value = "/getPpzt")
    public String getPpzt(String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        String result = Constants.PPZT_WPP;
        BdcPpgx bdcPpgx = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null) {
                if (StringUtils.equals(bdcSpxx.getBdclx(), Constants.BDCLX_TDFW)) {
                    bdcPpgx = bdcPpgxService.getBdcPpgxByFwproid(proid);
                } else {
                    bdcPpgx = bdcPpgxService.getBdcPpgxByTdproid(proid);
                }
                if (bdcPpgx == null) {
                    BdcBdcdy bdcBdcdy = bdcdyService.getBdcdyByProid(proid);
                    if (bdcBdcdy != null) {
                        List<BdcPpgx> bdcPpgxList = bdcPpgxService.getBdcPpgxByBdcdyh(bdcBdcdy.getBdcdyh());
                        if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                            bdcPpgx = bdcPpgxList.get(0);
                        }
                    }
                }

            }
        } else if (StringUtils.isNotBlank(bdcdyh)) {
            List<BdcPpgx> bdcPpgxList = bdcPpgxService.getBdcPpgxByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                bdcPpgx = bdcPpgxList.get(0);
            }
        }

        if (bdcPpgx != null) {
            if (StringUtils.isNotBlank(bdcPpgx.getFwproid()) && StringUtils.isBlank(bdcPpgx.getTdproid())) {
                result = Constants.PPZT_WPPTDZ;
            } else if (StringUtils.isNotBlank(bdcPpgx.getFwproid()) && StringUtils.isNotBlank(bdcPpgx.getTdproid())) {
                result = Constants.PPZT_YPP;
            }

        }
        return result;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 查看已匹配结果
     */
    @RequestMapping(value = "/showPpjg", method = RequestMethod.GET)
    public String showPpjg(Model model, String proid, String order) {
        Map params = Maps.newHashMap();
        List<Map> mapList = new ArrayList<>();
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null) {
                List<BdcPpgxLog> bdcPpgxLogList = null;
                if (StringUtils.equals(bdcSpxx.getBdclx(), Constants.BDCLX_TDFW)) {
                    params.put(ParamsConstants.FWPROID_LOWERCASE, proid);
                    bdcPpgxLogList = bdcPpgxLogService.getBdcPpgxLogByMap(params);
                } else {
                    params.put(ParamsConstants.TDPROID_LOWERCASE, proid);
                    bdcPpgxLogList = bdcPpgxLogService.getBdcPpgxLogByMap(params);
                }
                if (StringUtils.equals(order, "true") && CollectionUtils.isNotEmpty(bdcPpgxLogList)) {
                    BdcPpgxLog bdcPpgxLog = bdcPpgxLogList.get(bdcPpgxLogList.size() - 1);
                    bdcPpgxLogList = new ArrayList<>();
                    bdcPpgxLogList.add(bdcPpgxLog);
                }
                if (CollectionUtils.isNotEmpty(bdcPpgxLogList)) {
                    for (BdcPpgxLog bdcPpgxLog : bdcPpgxLogList) {
                        Map<String, Object> map = JSONObject.parseObject(toJSONString(bdcPpgxLog));
                        Map fwZsMap = bdcZsService.queryBdcqzhByProid(bdcPpgxLog.getFwproid());
                        if (MapUtils.isNotEmpty(fwZsMap) && fwZsMap.containsKey("BDCQZH")) {
                            map.put("fwbdcqzh", fwZsMap.get("BDCQZH"));
                        }
                        Map tdZsMap = bdcZsService.queryBdcqzhByProid(bdcPpgxLog.getTdproid());
                        if (MapUtils.isNotEmpty(tdZsMap) && tdZsMap.containsKey("BDCQZH")) {
                            map.put("tdbdcqzh", tdZsMap.get("BDCQZH"));
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        map.put("ppsj", sdf.format(bdcPpgxLog.getPpsj()));
                        mapList.add(map);
                    }
                }
            }
        }
        model.addAttribute("bdcPpgxLogList", mapList);
        return "/wf/dataPic/showPpjg";
    }

    @ResponseBody
    @RequestMapping(value = "/getGxWwSqxx")
    public Object getGxWwSqxx(@RequestParam(value = "wwslbh", required = false) String wwslbh, @RequestParam(value = "fczh", required = false) String fczh) {
        if (StringUtils.isNotBlank(fczh) && StringUtils.isNotBlank(wwslbh)) {
            Map rquestMap = new HashMap();
            rquestMap.put("wwslbh", wwslbh);
            rquestMap.put("fczh", fczh);
            String url = AppConfig.getProperty("currency.url") + "/rest/v1.0/wwsq/getGxWwSqxx";
            String result = HttpClientUtil.doPostJson(url, JSONObject.toJSONString(rquestMap));
            if (StringUtils.isNotBlank(result)) {
                List<GxWwSqxx> gxWwSqxxList = (List<GxWwSqxx>) JSONObject.parse(result);
                if (CollectionUtils.isNotEmpty(gxWwSqxxList)) {
                    return gxWwSqxxList.get(0);
                }
            }
        }
        return null;
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/6/5 13:42
     * @description 查看不动产单元的匹配结果
     */
    @RequestMapping(value = "/showBdcdyhPpjg", method = RequestMethod.GET)
    public String showBdcdyhPpjg(Model model, String bdcdyh) {
        Map params = Maps.newHashMap();
        List<Map> mapList = new ArrayList<>();
        if (StringUtils.isNotBlank(bdcdyh)) {
            params.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            List<BdcPpgxLog> bdcPpgxLogList = bdcPpgxLogService.getBdcPpgxLogByMap(params);
            if (CollectionUtils.isNotEmpty(bdcPpgxLogList)) {
                for (BdcPpgxLog bdcPpgxLog : bdcPpgxLogList) {
                    Map<String, Object> map = JSONObject.parseObject(toJSONString(bdcPpgxLog));
                    Map fwZsMap = bdcZsService.queryBdcqzhByProid(bdcPpgxLog.getFwproid());
                    if (MapUtils.isNotEmpty(fwZsMap) && fwZsMap.containsKey("BDCQZH")) {
                        map.put("fwbdcqzh", fwZsMap.get("BDCQZH"));
                    }
                    Map tdZsMap = bdcZsService.queryBdcqzhByProid(bdcPpgxLog.getTdproid());
                    if (MapUtils.isNotEmpty(tdZsMap) && tdZsMap.containsKey("BDCQZH")) {
                        map.put("tdbdcqzh", tdZsMap.get("BDCQZH"));
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    map.put("ppsj", sdf.format(bdcPpgxLog.getPpsj()));
                    mapList.add(map);
                }
            }
        }
        model.addAttribute("bdcPpgxLogList", mapList);
        return "/wf/dataPic/showPpjg";
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/5/14 17:10
     * @description 撤销匹配
     */
    @ResponseBody
    @RequestMapping(value = "/cxBdcPic")
    public Object cxBdcPic(String proid) {
        return bdcPicService.cxBdcPic(proid, PlatformUtil.getCurrentUserId());
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/5/15 17:14
     * @description 撤销匹配验证
     */
    @ResponseBody
    @RequestMapping(value = "/checkCxPic")
    public Object checkCxPic(String proid) {
        return bdcPicService.checkCxPic(proid);
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/5/15 17:14
     * @description 获取分摊和独用土地面积
     */
    @ResponseBody
    @RequestMapping(value = "/getTdmj")
    public Object getTdmj(String id, String bdclx) {
        return bdcPicService.getTdmj(id, bdclx);
    }

}
