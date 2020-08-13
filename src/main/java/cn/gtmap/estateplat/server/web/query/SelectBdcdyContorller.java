package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-3-29
 * Time: 下午3:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/selectBdcdy")
public class SelectBdcdyContorller extends BaseController {
    @Autowired
    BdcZdGlMapper bdcZdGlMapper;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSqlxQllxRelService bdcSqlxQllxRelService;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private GdDyhRelService gdDyhRelService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;


    public static final String PARAMETER_ISNOBDC_HUMP = "isNoBdc";
    public static final String PARAMETER_FILTERNULLBDCQZH_HUMP = "filterNullBdcqzh";
    public static final String PARAMETER_UNDEFINED = "undefined";

    /*审批表选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "multiselect", required = false) boolean multiselect, @RequestParam(value = "joinselect", required = false) boolean joinselect, @RequestParam(value = "glbdcdy", required = false) String glbdcdy, @RequestParam(value = "glzs", required = false) String glzs) {

        List<BdcXm> bdcXmList = null;
        if(StringUtils.isNotBlank(wiid)) {
            HashMap querymap = new HashMap();
            querymap.put("wiid", wiid);
            bdcXmList = bdcXmService.andEqualQueryBdcXm(querymap);
        }
        BdcXm xmxx = null;
        if(CollectionUtils.isNotEmpty(bdcXmList)) {
            xmxx = bdcXmList.get(0);
        } else {
            xmxx = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        }

        model.addAttribute("proid", proid);
        List<Map> bdclxList = bdcZdGlMapper.getZdBdclx();
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String yqllxdm = "";
        String bdclxdm = "";
        String qlxzdm = "";
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        String workFlowName = PlatformUtil.getWorkFlowNameByProid(proid);
        String qllx = "";
        String dyfs = "";
        String ysqlxdm = "";
        if(StringUtils.isNotBlank(workFlowName)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(workFlowName);
            if(CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                if(map.get(ParamsConstants.QLLXDM_CAPITAL) != null) {
                    qllx = CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL));
                }
            }
        }

        if(xmxx != null && StringUtils.isNotBlank(xmxx.getSqlx())) {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put(ParamsConstants.SQLXDM_LOWERCASE, xmxx.getSqlx());
            if(StringUtils.isNotBlank(qllx)) {
                queryMap.put("qllxdm", qllx);
            }
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }

        if(CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if(StringUtils.isNotBlank(bdcSqlxQllxRel.getYqllxdm()))
                yqllxdm = bdcSqlxQllxRel.getYqllxdm();
            if(bdcSqlxQllxRel.getBdcdyly() != null)
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            if(StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm()))
                zdtzm = bdcSqlxQllxRel.getZdtzm();
            if(StringUtils.isNotBlank(bdcSqlxQllxRel.getDyfs()))
                dyfs = bdcSqlxQllxRel.getDyfs();
            if(StringUtils.isNotBlank(bdcSqlxQllxRel.getBdclx()))
                bdclxdm = bdcSqlxQllxRel.getBdclx();
            if(StringUtils.isNotBlank(bdcSqlxQllxRel.getQlxzdm()))
                qlxzdm = bdcSqlxQllxRel.getQlxzdm();
            if(StringUtils.isNotBlank(bdcSqlxQllxRel.getYsqlxdm()))
                ysqlxdm = bdcSqlxQllxRel.getYsqlxdm();
        }
        String sqlx = "";
        if(null != xmxx) {
            String sqlxdm = xmxx.getSqlx();
            model.addAttribute(ParamsConstants.SQLXDM_LOWERCASE, sqlxdm);
            if(StringUtils.equals(sqlxdm, Constants.SQLX_JF)) {
                sqlx = "JF";
            }
        }
        List<BdcZdSqlx> bdcZdSqlxList = null;
        if(xmxx!=null&&StringUtils.isNotBlank(xmxx.getSqlx())) {
            Example example = new Example(BdcZdSqlx.class);
            example.createCriteria().andEqualTo("dm", xmxx.getSqlx());
            bdcZdSqlxList = entityMapper.selectByExample(BdcZdSqlx.class, example);
        }


        model.addAttribute("dyfs", dyfs);
        model.addAttribute("yqllxdm", yqllxdm);
        model.addAttribute(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm);
        if(StringUtils.equals(glbdcdy, "true")) {
            model.addAttribute(" glbdcdy", glbdcdy);
            model.addAttribute(ParamsConstants.BDCDYLY_LOWERCASE, 0);
        } else if(StringUtils.equals(glzs, "true")) {
            model.addAttribute("glzs", glzs);
            model.addAttribute(ParamsConstants.BDCDYLY_LOWERCASE, 1);
        } else {
            model.addAttribute(ParamsConstants.BDCDYLY_LOWERCASE, bdcdyly);
        }
        model.addAttribute(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        model.addAttribute(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm);
        model.addAttribute("bdclxList", bdclxList);
        model.addAttribute(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm);
        model.addAttribute("sqlx", sqlx);
        model.addAttribute("qllx", qllx);
        if(xmxx!=null){
            model.addAttribute("djlx", xmxx.getDjlx());
            model.addAttribute("wiid", xmxx.getWiid());
            model.addAttribute(ParamsConstants.SQLXDM_LOWERCASE, xmxx.getSqlx());

            if(CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, xmxx.getSqlx())) {
                model.addAttribute("plChoseOne", Constants.PL_CHOSE_ONE);
            }
        }

        if(CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
            model.addAttribute("sqlxmc", bdcZdSqlxList.get(0).getMc());
            model.addAttribute("workFlowDefId", bdcZdSqlxList.get(0).getWdid());
        }

        //不动产单元或不动产权证多选功能
        String path = "";
        if (StringUtils.equals(AppConfig.getProperty("dwdm"), Constants.DWDM_SZ)){
            path = "query/" + Constants.DWDM_SZ + "/djsjBdcdyList";
            if (joinselect) {
                path = "query/" + Constants.DWDM_SZ + "/djsjJoinMultiselectList";
            } else if (multiselect) {
                path = "query/" + Constants.DWDM_SZ + "/djsjMultiselectList";
            }
        }else{
            if (joinselect) {
                path = "query/djsjJoinMultiselectList";
            } else if (multiselect) {
                path = "query/djsjMultiselectList";
            }
        }


        if(StringUtils.isBlank(path)) {
            path = "query/djsjBdcdyList";
        }
        return path;
    }

    @RequestMapping("/addHbXm")
    public String toSqlxQllxRelConfig1(Model model) {
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<Map> sqlxList = bdcZdGlService.getZdSqlxList();
        model.addAttribute("djList", djlxList);
        model.addAttribute("sqList", sqlxList);
        //登记类型和申请类型默认值
        model.addAttribute("djlx", Constants.DJLX_CSDJ_DM);
        return "/sjgl/addHb";
    }

    @ResponseBody
    @RequestMapping("/getDjsjBdcdyPagesJson")
    public Object getDjsjBdcdyPagesJson(Pageable pageable, String djh, String bdcdyh, String dcxc, String qlr,
                                    String tdzl, String bdclx, String bdclxdm, String zdtzm, String htbh,
                                    @RequestParam(value = "qlxzdm", required = false) String qlxzdm, String bdcdyhs, String bdclxZx) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            String[] djids = bdcdyService.getDjQlrIdsByQlr(dcxc, bdclx);
            if(djids != null && djids.length > 0)
                map.put("djids", djids);
            String bdcdycxFwbhZdmc = AppConfig.getProperty("bdcdycx.fwbh.zdmc");
            if(StringUtils.isNotBlank(bdcdycxFwbhZdmc)) {
                map.put("fwbh", "t." + bdcdycxFwbhZdmc + "='" + StringUtils.deleteWhitespace(dcxc) + "'");
            }
        } else {
            if(StringUtils.isNotBlank(htbh)) {
                HttpClient client = null;
                PostMethod method = null;
                try {
                    String url = AppConfig.getProperty("building-contract.url") + "/htbaServerClient/getBdcdyhs";
                    client = new HttpClient();
                    client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                    client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                    method = new PostMethod(url);
                    method.setRequestHeader("Connection", "close");
                    method.addParameter("htbh", StringUtils.deleteWhitespace(htbh));
                    client.executeMethod(method);
                    String bdcdyhsByHt = method.getResponseBodyAsString();
                    List<String> bdcdyhListByHt = new ArrayList<String>();
                    if(StringUtils.indexOf(bdcdyhsByHt, ",") > -1) {
                        bdcdyhListByHt = Arrays.asList(StringUtils.split(bdcdyhsByHt, ","));
                    } else {
                        bdcdyhListByHt.add(bdcdyhsByHt);
                    }
                    if(CollectionUtils.isNotEmpty(bdcdyhListByHt)) {
                        map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhListByHt);
                    }
                } catch (IOException e) {
                    logger.error("SelectBdcdyController.getDjsjBdcdyPagesJson",e);
                } finally {
                    if(method != null) {
                        method.releaseConnection();
                    }
                    if(client != null) {
                        ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                    }
                }
            }
            if(StringUtils.isNotBlank(djh)) {
                map.put("djh", djh);
            }
            if(StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }

            if(StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }

            if(StringUtils.isNotBlank(bdclx)) {
                map.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
            }
            if(StringUtils.isNotBlank(tdzl)) {
                map.put("tdzl", tdzl);
            }

        }
        if(StringUtils.isNotBlank(bdclxdm)) {
            map.put(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm.split(","));
            String bdclxdmConfig = bdclxdm.split(",")[0];
            if(StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "F")) {
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDFW);
            } else if(StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "W")) {
                if(zdtzm.indexOf("H") > -1 || zdtzm.indexOf("GG") > -1) {
                    map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_HY);
                } else {
                    map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TD);
                }

            } else if(StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "L"))
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDSL);
            else if(StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "Q"))
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDQT);
        }
        if(StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for(String tempBdcdyh : bdcdyhs.split(",")) {
                if(StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
        }
        if(StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        if(StringUtils.isNotBlank(qlxzdm)) {
            map.put(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm.split(","));
        }

        map.put("bdclxZx", "");
        return repository.selectPaging("getDjsjBdcdyByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdczsListByPage")
    public Object getBdczsListByPage(Pageable pageable, String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm
            , @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs
            , @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm, @RequestParam(value = "proid", required = false) String proid, String bdcdyhs
            , String fzqssj, String fzjssj, String zstype) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String sqlx = bdcXm != null ? bdcXm.getSqlx() : "";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if(StringUtils.isNotBlank(bdcqzh)) {
                map.put("bdcqzh", bdcqzh);
            }
            if(StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if(StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if(StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
        }
        if(StringUtils.isNotBlank(bdclxdm)) {
            map.put(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm.split(","));
        }
        /**
         * @author jiangganzhi
         * @description 异议登记不根据权利类型过滤 否则无法搜出证明
         */
        if(StringUtils.isNotBlank(sqlx)&&!StringUtils.equals(sqlx, Constants.SQLX_YYDJ_DM)
                &&StringUtils.isNotBlank(qllx)) {
            map.put("qllx", qllx.split(","));
        }
        if(StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        if(StringUtils.isNotBlank(dyfs)) {
            map.put("dyfs", dyfs);
        }
        if(StringUtils.isNotBlank(qlxzdm)) {
            map.put(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm.split(","));
        }
        if(StringUtils.isNotBlank(ysqlxdm)) {
            map.put(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm.split(","));
        }

        if(StringUtils.isNotBlank(zstype)) {
            if(StringUtils.equals(zstype, Constants.BDCQZS_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZS_BH_FONT);
            } else if(StringUtils.equals(zstype, Constants.BDCQZM_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZM_BH_FONT);
            } else {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, "不动产权证明单");
            }
        }
        if(StringUtils.isNotBlank(sqlx) && (Constants.SQLX_SPFSCKFSZC_DM.equals(sqlx) || Constants.SQLX_SPFXZBG_DM.equals(sqlx))) {
            zstype = AppConfig.getProperty("spfscdj.zstype");
            map.put(ParamsConstants.ZSTYPE_LOWERCASE, zstype);
        }
        //如果不是商品房买卖转移登记和商品房转移登记，首次登记信息表变更登记，商品房首次开发商自持要求不能搜到首次登记证
        if(StringUtils.isNotBlank(sqlx) && !StringUtils.equals(sqlx,Constants.SQLX_SPFMMZYDJ_DM) && !StringUtils.equals(sqlx,Constants.SQLX_CLF) && !StringUtils.equals(sqlx,Constants.SQLX_SPFSCKFSZC_DM) && !StringUtils.equals(sqlx,Constants.SQLX_SPFXZBG_DM) && !StringUtils.equals(sqlx,Constants.SQLX_PLCF) && !StringUtils.equals(sqlx,Constants.SQLX_CF) && !StringUtils.equals(Constants.SQLX_ZY_SFCD,sqlx)){
            map.put("disSpfsc","true");
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isNotBlank(fzqssj)) {
                map.put("fzqssj", simpleDateFormat.parse(fzqssj));
            }
            if(StringUtils.isNotBlank(fzjssj)) {
                map.put("fzjssj", simpleDateFormat.parse(fzjssj));
            }
        } catch (ParseException e) {
            logger.error("SelectBdcdyController.getBdczsListByPage",e);
        }
        if(StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for(String tempBdcdyh : bdcdyhs.split(",")) {
                if(StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
        }

        //sc 需要展示不生成证书的权力信息
        map.put(PARAMETER_FILTERNULLBDCQZH_HUMP, true);
        /**
         * @author bianwen
         * @description 将权利已注销的证书过滤掉
         */
        map.put("qszt", Constants.QLLX_QSZT_HR);

        /**
         * @author bianwen
         * @description 在建工程选择不动产单元页面显示一条信息
         */
        return repository.selectPaging("getBdcZsByPage", map, pageable);
    }


    /**
     * @author jiangganzhi
     * @description 选择不动产单元 产权房产合并查询方法
     */
    @ResponseBody
    @RequestMapping("/getBdczsListAndGdfczOrTdzListByPage")
    public Object getBdczsListAndGdfczOrTdzListByPage(Pageable pageable, String bdcdyh, String fczh, String qlr, String fwzl, String dcxc, String qllx, String zdtzm, String proid, String bdcqzh, String zl, String bdclxdm
            , @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs
            , @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm,String bdcdyhs
            , String fzqssj, String fzjssj, String zstype,String zsly, String tdzh, String tdzl,String isTd) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String sqlx = bdcXm != null ? bdcXm.getSqlx() : "";
        String[] bdclxdmList = null;
        //是否不匹配不动产单元默认否
        String isNoBdc = "no";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if(StringUtils.isNotBlank(bdcqzh)) {
                map.put("bdcqzh", bdcqzh);
            }
            if(StringUtils.isNotBlank(fczh)) {
                map.put("fczh", fczh);
            }
            if(StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if(StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if(StringUtils.isNotBlank(fwzl)) {
                map.put("fwzl", fwzl);
            }
            if(StringUtils.isNotBlank(qllx)) {
                map.put("qllx", qllx);
            }
            if(StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
            if(StringUtils.isNotBlank(tdzl)) {
                map.put("tdzl", tdzl);
            }
            if(StringUtils.isNotBlank(tdzh)) {
                map.put("tdzh", tdzh);
            }
        }
        if(StringUtils.isNotBlank(bdclxdm)) {
            map.put(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm.split(","));
            bdclxdmList = bdclxdm.split(",");
        }
        if(StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        /**
         * @author jiangganzhi
         * @description 异议登记不根据权利类型过滤 否则无法搜出证明
         */
        if(StringUtils.isNotBlank(sqlx) && !StringUtils.equals(sqlx, Constants.SQLX_YYDJ_DM)
                &&StringUtils.isNotBlank(qllx)) {
            map.put("qllx", qllx.split(","));
        }
        if(StringUtils.isNotBlank(dyfs)) {
            map.put("dyfs", dyfs);
        }
        if(StringUtils.isNotBlank(qlxzdm)) {
            map.put(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm.split(","));
        }
        if(StringUtils.isNotBlank(ysqlxdm)) {
            map.put(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm.split(","));
        }
        if(StringUtils.isNotBlank(zstype) && !StringUtils.equals(zstype,PARAMETER_UNDEFINED)) {
            if(StringUtils.equals(zstype, Constants.BDCQZS_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZS_BH_FONT);
            } else if(StringUtils.equals(zstype, Constants.BDCQZM_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZM_BH_FONT);
            } else {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, "不动产权证明单");
            }
        }
        if(StringUtils.isNotBlank(sqlx) && (Constants.SQLX_SPFSCKFSZC_DM.equals(sqlx) || Constants.SQLX_SPFXZBG_DM.equals(sqlx))) {
            zstype = AppConfig.getProperty("spfscdj.zstype");
            map.put(ParamsConstants.ZSTYPE_LOWERCASE, zstype);
        }
        //如果不是商品房买卖转移登记和商品房转移登记，首次登记信息表变更登记，商品房首次开发商自持要求不能搜到首次登记证
        if(StringUtils.isNotBlank(sqlx) && !StringUtils.equals(sqlx,Constants.SQLX_SPFMMZYDJ_DM) && !StringUtils.equals(sqlx,Constants.SQLX_CLF) && !StringUtils.equals(sqlx,Constants.SQLX_SPFSCKFSZC_DM) && !StringUtils.equals(sqlx,Constants.SQLX_SPFXZBG_DM)){
            map.put("disSpfsc","true");
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isNotBlank(fzqssj)  && !StringUtils.equals(fzqssj,PARAMETER_UNDEFINED)) {
                map.put("fzqssj", simpleDateFormat.parse(fzqssj));
            }
            if(StringUtils.isNotBlank(fzjssj)  && !StringUtils.equals(fzjssj,PARAMETER_UNDEFINED)) {
                map.put("fzjssj", simpleDateFormat.parse(fzjssj));
            }
        } catch (ParseException e) {
            logger.error("SelectBdcdyController.getBdczsListAndGdfczOrTdzListByPage",e);
        }
        if(StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for(String tempBdcdyh : bdcdyhs.split(",")) {
                if(StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
        }
        //sc 需要展示不生成证书的权力信息
        map.put(PARAMETER_FILTERNULLBDCQZH_HUMP, true);
        /**
         * @author bianwen
         * @description 将权利已注销的证书过滤掉
         */
        map.put("qszt", Constants.QLLX_QSZT_HR);
        map.put("iszx", "0");
        Page<HashMap> dataPaging = null;
        //jiangganzhi 房产证与抵押证明区分
        String dydjlx = "";
        if(StringUtils.isNotBlank(proid)) {
            bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if(bdcXm != null){
                if(StringUtils.isNotBlank(bdcXm.getDydjlx())){
                    dydjlx = bdcXm.getDydjlx();
                }
                if(CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC,bdcXm.getSqlx())||StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_FWCF_DM)){
                    isNoBdc = "yes";
                    map.put(PARAMETER_ISNOBDC_HUMP,isNoBdc);
                }
            }
        }
        map.put("filterFsss","true");
        if(StringUtils.isNotBlank(zsly)){
            //区分土地和房屋
            Boolean queryTd=  false;
            if(bdclxdmList != null && bdclxdmList.length > 0){
                for(String bdclxdmTemp : bdclxdmList){
                    if(StringUtils.isNotBlank(bdclxdmTemp) && StringUtils.equals(bdclxdmTemp,Constants.DZWTZM_W)){
                        queryTd = true;
                        break;
                    }
                }
            }
            if(StringUtils.equals(isTd,"true") && StringUtils.isBlank(bdclxdm)){
                queryTd = true;
                bdclxdm = "W";
                map.put(ParamsConstants.BDCLXDM_LOWERCASE,bdclxdm.split(","));
            }
            //jiangganzhi 根据证书来源决定查询的证书种类
            String queryStr = "getBdczsListAndGdfczOrTdzListByPage";
            if (StringUtils.isNotBlank(qllx) && qllx.indexOf(Constants.QLLX_DYAQ) > -1 && !StringUtils.equals(dydjlx,Constants.DJLX_CSDJ_DM)) {
                //jiangganzhi 查询不动产和过渡的抵押证明
                if(StringUtils.equals(zsly,Constants.SELECT_ZSLY_BDCANDGD)){
                    if(queryTd){
                        map.put("gdtddyzm","true");
                    }else{
                        map.put("gddyzm","true");
                    }
                }
                if(StringUtils.isNotBlank(zsly) && StringUtils.equals(zsly,Constants.SELECT_ZSLY_GD)){
                    if(queryTd){
                        queryStr = "getGdTdDyzmForBdcAndGdByPage";
                    }else{
                        queryStr = "getGdDyzmForBdcAndGdByPage";
                    }
                }
            } else if(StringUtils.isNotBlank(qllx) && qllx.indexOf(Constants.QLLX_YGDJ) > -1 && !StringUtils.equals(dydjlx,Constants.DJLX_CSDJ_DM)){
                //jiangganzhi 查询不动产和过渡的预告证明
                if(StringUtils.equals(zsly,Constants.SELECT_ZSLY_BDCANDGD)){ map.put("gdygzm","true");}
                if(StringUtils.isNotBlank(zsly) && StringUtils.equals(zsly,Constants.SELECT_ZSLY_GD)){ queryStr = "getGdYgzmForBdcAndGdByPage";}
            }else{
                //jiangganzhi 查询不动产产权证和过渡的房产证
                if(StringUtils.equals(zsly,Constants.SELECT_ZSLY_BDCANDGD)){
                    if(queryTd){
                        map.put("gdtdz","true");
                    }else{
                        map.put("gdfcz","true");
                    }
                }
                if(StringUtils.isNotBlank(zsly) && StringUtils.equals(zsly,Constants.SELECT_ZSLY_GD)){
                    if(queryTd){
                        queryStr = "getGdtdzListForBdcAndGdByPage";
                    }else{
                        queryStr = "getGdfczListForBdcAndGdByPage";
                    }
                }
            }
            dataPaging = repository.selectPaging(queryStr, map, pageable);
        }
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping("/getGdfczListByPage")
    public Object getGdfczListByPage(Pageable pageable, String bdcdyh, String fczh, String qlr, String fwzl, String dcxc, String qllx, String zdtzm, String proid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //是否不匹配不动产单元默认否
        String isNoBdc = "no";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if(StringUtils.isNotBlank(fczh)) {
                map.put("fczh", fczh);
            }
            if(StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if(StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if(StringUtils.isNotBlank(fwzl)) {
                map.put("fwzl", fwzl);
            }
            if(StringUtils.isNotBlank(qllx)) {
                map.put("qllx", qllx);
            }
        }
        if(StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        }
        map.put("iszx", "0");
        Page<HashMap> dataPaging = null;
        //jiangganzhi 房产证与抵押证明区分
        BdcXm bdcXm = null;
        String dydjlx = "";
        if(StringUtils.isNotBlank(proid)) {
            bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if(bdcXm != null){
                if(StringUtils.isNotBlank(bdcXm.getDydjlx())){
                    dydjlx = bdcXm.getDydjlx();
                }
                if(CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC,bdcXm.getSqlx())||StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_FWCF_DM)){
                    isNoBdc = "yes";
                    map.put(PARAMETER_ISNOBDC_HUMP,isNoBdc);
                }
            }
        }

        map.put("filterFsss","true");
        //更正登记走getGdfczByPage的方法
        if (StringUtils.isNotBlank(qllx) && qllx.indexOf(Constants.QLLX_DYAQ) > -1 && !StringUtils.equals(dydjlx,Constants.DJLX_CSDJ_DM) && (bdcXm!=null&&StringUtils.isNotEmpty(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_GZ_DM))) {
            //此处都为房产证的信息，过滤掉土地的
            map.put(ParamsConstants.BDCLX_LOWERCASE,"TDFW");
            dataPaging = repository.selectPaging("getGdDyzmByPage", map, pageable);
        } else if(StringUtils.isNotBlank(qllx) && qllx.indexOf(Constants.QLLX_YGDJ) > -1 && !StringUtils.equals(dydjlx,Constants.DJLX_CSDJ_DM) && (bdcXm!=null&&StringUtils.isNotEmpty(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_GZ_DM))){
            dataPaging = repository.selectPaging("getGdYgzmByPage", map, pageable);
        }else{
            dataPaging = repository.selectPaging("getGdfczByPage", map, pageable);
        }
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping("/getGdtdzListByPage")
    public Object getGdtdzListByPage(Pageable pageable, String bdcdyh, String tdzh, String qlr, String dcxc, String tdzl, String qllx, String zdtzm, String proid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //是否不匹配不动产单元默认否
        String isNoBdc = "no";

        if(org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if(StringUtils.isNotBlank(tdzh)) {
                map.put("tdzh", tdzh);
            }
            if(StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if(StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if(StringUtils.isNotBlank(tdzl)) {
                map.put("tdzl", tdzl);
            }
        }
        if(StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        }
        Page<HashMap> dataPaging = null;
        BdcXm bdcXm = null;
        String dydjlx = "";
        if(StringUtils.isNotBlank(proid)) {
            bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if(bdcXm != null){
                if(StringUtils.isNotBlank(bdcXm.getDydjlx())){
                    dydjlx = bdcXm.getDydjlx();
                }

                if(CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC,bdcXm.getSqlx())||StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_TDCF_DM_NEW)){
                    isNoBdc = "yes";
                }
                // 冻结和解冻流程可选择匹配和非匹配的土地证
                if(StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(Constants.SQLX_BDCDYDJ_DM,bdcXm.getSqlx())||StringUtils.equals(Constants.SQLX_BDCDYJD_DM,bdcXm.getSqlx()))){
                    isNoBdc = "";
                }
                map.put(PARAMETER_ISNOBDC_HUMP,isNoBdc);
            }
        }
        if (StringUtils.isNotBlank(qllx) && StringUtils.equals(qllx, Constants.QLLX_DYAQ) && !StringUtils.equals(dydjlx,Constants.DJLX_CSDJ_DM) && (bdcXm!=null&&StringUtils.isNotEmpty(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_GZ_DM))) {
            dataPaging = repository.selectPaging("getGdtdDyzmByPage", map, pageable);
        } else {
            dataPaging = repository.selectPaging("getGdtdzByPage", map, pageable);
        }
        return dataPaging;
    }


    @ResponseBody
    @RequestMapping("/getGdcfListByPage")
    public Object getGdcfListByPage(Pageable pageable, String proid,String bdcdyh, String cfwh, String qlr, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        if(org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if(StringUtils.isNotBlank(cfwh)) {
                map.put("cfwh", cfwh);
            }
            if(StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if(StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
        }
        //是否不匹配不动产单元默认是
        String isNoBdc = "no";
        if(StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null){
                List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
                if(bppSqlxdmList != null && bppSqlxdmList.contains(bdcXm.getSqlx())) {
                    isNoBdc = "yes";
                }
                if(StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_SFCD)){
                    isNoBdc = "";
                }
            }
        }
        map.put(PARAMETER_ISNOBDC_HUMP,isNoBdc);
        return repository.selectPaging("getGdcfListByPage", map, pageable);
    }


    @ResponseBody
    @RequestMapping("/getQlxxListByPage")
    public Object getQlxxListByPage(Pageable pageable, String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm, @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs, @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm, @RequestParam(value = "proid", required = false) String proid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        }
        if(StringUtils.isNotBlank(ysqlxdm)) {
            map.put(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm.split(","));
        }
        map.put(PARAMETER_FILTERNULLBDCQZH_HUMP, true);
        return repository.selectPaging("getQlxxByPage", map, pageable);
    }



    /**
     * 获取数据
     *
     * @param model
     * @param bdcid,qlid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getdateByBdcid")
    public HashMap<String, Object> getdateByBdcid(Model model, String bdcid, String bdclx, String qlid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String zl = "";
        StringBuilder qlr = new StringBuilder();
        String ppzt = "";
        String gdzh = "";
        List<GdFw> gdFwlList = null;
        if(StringUtils.isNotBlank(bdcid) && !StringUtils.equals("TD", bdclx)) {
            Example example = new Example(GdFw.class);
            example.createCriteria().andEqualTo("fwid", bdcid);
            gdFwlList = entityMapper.selectByExample(GdFw.class, example);
        }

        List<GdTd> gdTdlList = null;
        if(StringUtils.isNotBlank(bdcid) && StringUtils.equals("TD", bdclx)) {
            Example example = new Example(GdTd.class);
            example.createCriteria().andEqualTo("tdid", bdcid);
            gdTdlList = entityMapper.selectByExample(GdTd.class, example);
        }

        if(StringUtils.isNotBlank(qlid)) {
            List<GdQlr> gdQlrlList = gdXmService.getGdqlrByQlid(qlid, "qlr");
            if(CollectionUtils.isNotEmpty(gdQlrlList)) {
                for(int i = 0; i < gdQlrlList.size(); i++) {
                    if(StringUtils.isNotBlank(qlr)) {
                        qlr.append("/").append(gdQlrlList.get(i).getQlr());
                    } else if(!StringUtils.isNotBlank(qlr)) {
                        qlr.append(gdQlrlList.get(i).getQlr());
                    }
                }
            }
            Example example = new Example(GdDyhRel.class);
            example.createCriteria().andEqualTo("gdid", bdcid);
            List<GdDyhRel> gdDyhRelList = entityMapper.selectByExample(GdDyhRel.class,example);
            if(CollectionUtils.isNotEmpty(gdDyhRelList)){
                //已匹配
                ppzt = "0";
            }else{
                //未匹配
                ppzt = "1";
            }
            Example gdTdSyqExample = new Example(GdTdsyq.class);
            gdTdSyqExample.createCriteria().andEqualTo("qlid", qlid);
            List<GdTdsyq> gdTdsyqList = entityMapper.selectByExample(GdTdsyq.class,gdTdSyqExample);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                gdzh = gdTdsyq.getTdzh();
            }
            Example gdFwSyqExample = new Example(GdFwsyq.class);
            gdFwSyqExample.createCriteria().andEqualTo("qlid", qlid);
            List<GdFwsyq> gdFwsyqList = entityMapper.selectByExample(GdFwsyq.class,gdFwSyqExample);
            if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                GdFwsyq gdFwsyq = gdFwsyqList.get(0);
                gdzh = gdFwsyq.getFczh();
            }
        }

        if(CollectionUtils.isNotEmpty(gdFwlList)) {
            zl = gdFwlList.get(0).getFwzl();
        }
        if(CollectionUtils.isNotEmpty(gdTdlList)) {
            zl = gdTdlList.get(0).getZl();
        }
        resultMap.put("ppzt", ppzt);
        resultMap.put("zl", zl);
        resultMap.put("qlr", qlr);
        resultMap.put("gdzh", gdzh);
        return resultMap;
    }


    /**
     * 获取数据
     *
     * @param model
     * @param qlid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getdateByQlid")
    public HashMap<String, Object> getdateByQlid(Model model, String qlid, String bdclx) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String ppzt = "";
        StringBuilder bdcdyh = new StringBuilder();
        String gdzh = "";
        if(StringUtils.isNotBlank(qlid)) {
            List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
            List<GdDyhRel> gdDyhRelList = new ArrayList<GdDyhRel>();
            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                    List<GdDyhRel> gdDyhRelListTemp = gdDyhRelService.queryGdDyhRelListByBdcid(gdBdcQlRel.getBdcid());
                    if(CollectionUtils.isNotEmpty(gdDyhRelListTemp)) {
                        gdDyhRelList.addAll(gdDyhRelListTemp);
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                //已匹配
                ppzt = "0";
                List<String> bdcdyhList = new ArrayList<String>();
                for(GdDyhRel gdDyhRel : gdDyhRelList) {
                    if(StringUtils.isNotBlank(gdDyhRel.getBdcdyh()) && !bdcdyhList.contains(gdDyhRel.getBdcdyh())) {
                        bdcdyhList.add(gdDyhRel.getBdcdyh());
                    }
                }

                if(CollectionUtils.isNotEmpty(bdcdyhList)) {
                    for(String bdcdyhTemp:bdcdyhList) {
                        if(StringUtils.isBlank(bdcdyh)) {
                            bdcdyh.append(bdcdyhTemp);
                        }else {
                            bdcdyh.append(",").append(bdcdyhTemp);
                        }
                    }
                }
            } else {
                //未匹配
                ppzt = "1";
            }

            if(StringUtils.equals(bdclx,Constants.BDCLX_TDFW)) {
                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(qlid);
                if(gdFwsyq != null) {
                    gdzh = gdFwsyq.getFczh();
                }
            } else if(StringUtils.equals(bdclx,Constants.BDCLX_TD)) {
                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(qlid);
                if(gdTdsyq != null) {
                    gdzh = gdTdsyq.getTdzh();
                }
            }

        }

        resultMap.put("gdzh", gdzh);
        resultMap.put("ppzt", ppzt);
        resultMap.put(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
        return resultMap;
    }

    /**
     * 获取数据
     *
     * @param
     * @param bdcqzh
     * @return
     */
    @ResponseBody
    @RequestMapping("/getdateByBdcqzh")
    public HashMap<String, Object> getdateByZsid(String bdcqzh) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String zl = "";
        StringBuilder qlr = new StringBuilder();
        String bdcdyh = "";
        String bdclx = "";
        String bdcdyid = "";
        if(StringUtils.isNotBlank(bdcqzh)) {
            String proid = bdcZsService.getProidByBdcqzh(bdcqzh);
            if(StringUtils.isNotEmpty(proid)){
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                if(bdcBdcdy != null) {
                    if(StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                        bdcdyh = bdcBdcdy.getBdcdyh();
                    }
                    if(StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                        bdcdyid = bdcBdcdy.getBdcdyid();
                    }
                    if(StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                        bdclx = bdcBdcdy.getBdclx();
                    }
                }
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                if(bdcSpxx != null) {
                    if(StringUtils.isNotBlank(bdcSpxx.getBdclx())) {
                        bdclx = bdcSpxx.getBdclx();
                    }
                    if(StringUtils.isNotBlank(bdcSpxx.getZl())) {
                        zl = bdcSpxx.getZl();
                    }
                }
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                if(CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for(BdcQlr bdcQlr : bdcQlrList) {
                        if(StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                            if(StringUtils.isBlank(qlr)) {
                                qlr.append(bdcQlr.getQlrmc());
                            }else{
                                qlr.append(" ").append(bdcQlr.getQlrmc());
                            }
                        }
                    }
                }
            }
        }
        resultMap.put("zl", zl);
        resultMap.put("qlr", qlr);
        resultMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        resultMap.put("bdcdyid", bdcdyid);
        resultMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        return resultMap;
    }


    /**
     * 获取数据
     *
     * @param
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getdateByProid")
    public HashMap<String, Object> getdateByProid(String proid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String zl = "";
        StringBuilder qlr = new StringBuilder();
        String bdcdyh = "";
        String bdclx = "";
        String bdcdyid = "";
        if(StringUtils.isNotBlank(proid)) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            if(bdcBdcdy != null) {
                if(StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    bdcdyh = bdcBdcdy.getBdcdyh();
                }
                if(StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                    bdcdyid = bdcBdcdy.getBdcdyid();
                }
                if(StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                    bdclx = bdcBdcdy.getBdclx();
                }
            }
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if(bdcSpxx != null) {
                if(StringUtils.isNotBlank(bdcSpxx.getBdclx())) {
                    bdclx = bdcSpxx.getBdclx();
                }
                if(StringUtils.isNotBlank(bdcSpxx.getZl())) {
                    zl = bdcSpxx.getZl();
                }
            }
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcQlrList)) {
                for(BdcQlr bdcQlr : bdcQlrList) {
                    if(StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                        if(StringUtils.isBlank(qlr)){
                            qlr.append(bdcQlr.getQlrmc());
                        }else{
                            qlr.append(" ").append(bdcQlr.getQlrmc());
                        }
                    }
                }
            }
        }
        resultMap.put("zl", zl);
        resultMap.put("qlr", qlr);
        resultMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        resultMap.put("bdcdyid", bdcdyid);
        resultMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        return resultMap;
    }


    /**
     * 获取过渡查封数据
     *
     * @param gdproid
     * @param qlid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdCfQlrmcAndZl")
    public HashMap<String, Object> getGdCfQlrmcAndZl(String gdproid, String qlid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String zl = "";
        String qlr = "";
        if(StringUtils.isNotBlank(gdproid) && StringUtils.isNotBlank(qlid)) {
            GdXm gdXm = gdXmService.getGdXm(gdproid);
            if(gdXm != null && StringUtils.isNotBlank(gdXm.getZl()))
                zl = gdXm.getZl();
            String gdQlr = gdQlrService.getGdQlrsByQlid(qlid, Constants.QLRLX_QLR);
            if(StringUtils.isNotBlank(gdQlr))
                qlr = gdQlr;
        }

        resultMap.put("qlr", qlr);
        resultMap.put("zl", zl);
        return resultMap;
    }


    /**
     * 转移登记提示收回证书
     *
     * @param
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkZybdcZsbh")
    public String checkZybdcZsbh(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "yxmid", required = false) String yxmid) {
        String msg = "";
        if(StringUtils.isNotBlank(proid) && dwdm.equals("320900")) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())&&StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_ZYDJ_DM)
                    &&StringUtils.isNotBlank(yxmid)) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(yxmid);
                if(CollectionUtils.isNotEmpty(bdcZsList)) {
                    BdcZs bdcZs = bdcZsList.get(0);
                    if(StringUtils.isNotBlank(bdcZs.getBh())) {
                        msg = Constants.TSXX_ZYYZ;
                    }
                }
            }
        }

        return msg;
    }



    @RequestMapping(value = "showHhcf")
    public String showHhcf(Model model, String proid) {
        String sqlxMc = "";
        if(StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx())) {
                sqlxMc = bdcZdGlService.getSqlxMcByDm(bdcXm.getSqlx());
            }
        }
        model.addAttribute("sqlxMc", sqlxMc);
        model.addAttribute("proid", proid);
        String dwdm = AppConfig.getProperty("dwdm");
        return "query/" + dwdm + "/showHhcfsj";
    }


     /**
       * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
       * @param bdcdyid
       * @return
       * @description 获取不动产产权证号
       */
    @ResponseBody
    @RequestMapping("/getGdCfCqzh")
    public HashMap<String, String> getGdCfCqzh(String bdcdyid,String gdproid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        String cqzh = "";
        if(StringUtils.isNotBlank(bdcdyid)||StringUtils.isNotBlank(gdproid)){
            HashMap<String,String> zsMap = qllxService.getBdcZsByBdcdyid(bdcdyid);
            if(zsMap!=null&&StringUtils.isNotBlank(zsMap.get("BDCQZH"))){
                cqzh = zsMap.get("BDCQZH");
            }
            //分不动产和过渡两种情况获取产权证号
            if(StringUtils.isBlank(cqzh)&&StringUtils.isNotBlank(gdproid)&&StringUtils.isBlank(bdcdyid)){
                List<GdBdcQlRel>  gdBdcQlRelListTmp=gdBdcQlRelService.queryGdBdcQlListByQlid(gdproid);
                if(CollectionUtils.isNotEmpty(gdBdcQlRelListTmp)&&StringUtils.isNotBlank(gdBdcQlRelListTmp.get(0).getBdcid())){
                    String bdcid = gdBdcQlRelListTmp.get(0).getBdcid();
                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcid);
                    for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                        GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRel.getQlid());
                        if(null!=gdFwsyq &&StringUtils.isNotBlank(gdFwsyq.getFczh())&&StringUtils.equals("0",gdFwsyq.getIszx().toString())){
                            cqzh = gdFwsyq.getFczh();
                        }
                        if(StringUtils.isBlank(cqzh)){
                            GdTdsyq gdTdSyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRel.getQlid());
                            if(null!=gdTdSyq &&StringUtils.isNotBlank(gdTdSyq.getTdzh())&&StringUtils.equals("0",gdTdSyq.getIszx().toString())){
                                cqzh = gdTdSyq.getTdzh();
                            }
                        }
                        if(StringUtils.isBlank(cqzh)){
                            GdYg gdYg = gdFwService.getGdYgByYgid(gdBdcQlRel.getQlid(),0);
                            if(gdYg != null && StringUtils.isNotBlank(gdYg.getYgdjzmh())){
                                cqzh = gdYg.getYgdjzmh();
                            }
                        }
                    }
                }
            }else if(StringUtils.isNotBlank(bdcdyid)&&StringUtils.isBlank(cqzh)){
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(bdcBdcdy.getBdcdyh(), null);
                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                    String bdcid = bdcGdDyhRelList.get(0).getGdid();
                    if (StringUtils.isNotBlank(bdcid)) {
                        List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcid);
                        if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRel.getQlid());
                                if(null!=gdFwsyq &&StringUtils.isNotBlank(gdFwsyq.getFczh())&&StringUtils.equals("0",gdFwsyq.getIszx().toString())){
                                    cqzh = gdFwsyq.getFczh();
                                }
                                if(StringUtils.isBlank(cqzh)){
                                    GdTdsyq gdTdSyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRel.getQlid());
                                    if(null!=gdTdSyq &&StringUtils.isNotBlank(gdTdSyq.getTdzh())&&StringUtils.equals("0",gdTdSyq.getIszx().toString())){
                                        cqzh = gdTdSyq.getTdzh();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        resultMap.put("cqzh",cqzh);
        return resultMap;
    }

    /**
     * 获得限制原因
     *
     * @param cqzh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getXzyy", method = RequestMethod.GET)
    public HashMap<String, String> getXzyy(@RequestParam(value = "cqzh", required = false) String cqzh) {
        //正则取产权证号
        String regex = ">(.*)<";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cqzh);
        while (matcher.find()) {
            cqzh = matcher.group(1);
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
         List<GdBdcSd> list = gdXmService.getGdBdcSd("cqzh", cqzh == null ? "" : cqzh, Constants.SDZT_SD);
        //该条过渡数据已被锁定
        if(CollectionUtils.isNotEmpty(list)) {
            GdBdcSd gdBdcSd = list.get(0);
            String xzyy = gdBdcSd.getXzyy();
            resultMap.put("msg", "false");
            resultMap.put("xzyy", xzyy);
        } else {
            List<BdcBdcZsSd> bdcList = bdcXmService.getBdcSd("cqzh",cqzh == null ? "" : cqzh,Constants.SDZT_SD);
            if(bdcList != null && bdcList.size() > 0){
                BdcBdcZsSd bdcBdcZsSd = bdcList.get(0);
                String xzyy = bdcBdcZsSd.getXzyy();
                resultMap.put("msg", "false");
                resultMap.put("xzyy", xzyy);
            }else{
                resultMap.put("msg", "true");
            }
        }
        return resultMap;
    }
}
