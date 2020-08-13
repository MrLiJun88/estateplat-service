package cn.gtmap.estateplat.server.service.core.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.core.SelectBdcdyHandleService;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.service.config.QlztService;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.gtmap.estateplat.server.utils.Constants.*;

@Service
public class SelectBdcdyHandleServiceImpl implements SelectBdcdyHandleService {
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlMapper bdcZdGlMapper;
    @Autowired
    private BdcSqlxQllxRelService bdcSqlxQllxRelService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QlztService qlztService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXtConfigService bdcXtConfigService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String PARAMETER_ISNOBDC = "isNoBdc";

    @Override
    public void initSelectBdcdyModel(String wiid, String proid, String glbdcdy, String glzs, Model model) {
        List<BdcXm> bdcXmList = null;
        BdcXm xmxx = null;
        if (StringUtils.isNotBlank(wiid)) {
            HashMap<String, String> querymap = new HashMap<String, String>();
            querymap.put("wiid", wiid);
            bdcXmList = bdcXmService.andEqualQueryBdcXm(querymap);
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            xmxx = bdcXmList.get(0);
        } else {
            xmxx = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        }

        List<Map> bdclxList = new ArrayList<Map>();
        //qjd限制流程不动产类型
        if (xmxx != null && StringUtils.isNotBlank(xmxx.getSqlx())) {
            List<BdcSqlxQllxRel> bdcSqlxQllxRelList = bdcXtConfigService.getOthersBySqlx(xmxx.getSqlx());
            if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList) && StringUtils.equals(bdcSqlxQllxRelList.get(0).getBdclx(), "F")) {
                Map map = new HashMap();
                map.put("DM", Constants.BDCLX_TDFW);
                map.put("MC", "土地、房屋");
                bdclxList.add(map);
            } else if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList) && StringUtils.equals(bdcSqlxQllxRelList.get(0).getBdclx(), "W")) {
                Map map = new HashMap();
                map.put("DM", Constants.BDCLX_TD);
                map.put("MC", "土地");
                bdclxList.add(map);
            }
        }
        //放入不动产类型字典表
        if (CollectionUtils.isEmpty(bdclxList)) {
            bdclxList = bdcZdGlMapper.getZdBdclxOrderBy();
        }
        model.addAttribute("bdclxList", bdclxList);

        //通过申请类型名称获取对应权利类型
        String qllx = "";//权利类型代码
        String workFlowName = PlatformUtil.getWorkFlowNameByProid(proid);
        if (StringUtils.isNotBlank(workFlowName)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(workFlowName);
            if (CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                if (map.get("QLLXDM") != null) {
                    qllx = CommonUtil.formatEmptyValue(map.get("QLLXDM"));
                }
            }
        }
        model.addAttribute("qllx", qllx);

        //根据申请类型和权利类型关系获取相关信息
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String yqllxdm = ""; //原权利类型代码
        Integer bdcdyly = Constants.BDCDYLY_ALL; //不动产单元来源 默认权籍和登记都有
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        String ysqlxdm = ""; //原申请类型代码
        String dyfs = ""; //抵押方式
        String bdclxdm = "";//不动产类型代码
        String qlxzdm = "";//权利性质（使用权类型）
        if (xmxx != null && StringUtils.isNotBlank(xmxx.getSqlx())) {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sqlxdm", xmxx.getSqlx());
            if (StringUtils.isNotBlank(qllx)) {
                queryMap.put("qllxdm", qllx);
            }
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }
        if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYqllxdm())) {
                yqllxdm = bdcSqlxQllxRel.getYqllxdm();
            }
            if (bdcSqlxQllxRel.getBdcdyly() != null) {
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            }
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm())) {
                zdtzm = bdcSqlxQllxRel.getZdtzm();
            }
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getDyfs())) {
                dyfs = bdcSqlxQllxRel.getDyfs();
            }
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getBdclx())) {
                bdclxdm = bdcSqlxQllxRel.getBdclx();
            }
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getQlxzdm())) {
                qlxzdm = bdcSqlxQllxRel.getQlxzdm();
            }
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYsqlxdm())) {
                ysqlxdm = bdcSqlxQllxRel.getYsqlxdm();
            }
        }
        model.addAttribute("yqllxdm", yqllxdm); //放入原权利类型代码
        //放入不动产单元来源
        if (StringUtils.equals(glbdcdy, "true")) {
            //进行关联不动产单元操作时,不动产单元来源为权籍
            model.addAttribute(" glbdcdy", glbdcdy);
            bdcdyly = Constants.BDCDYLY_DJ;
        } else if (StringUtils.equals(glzs, "true")) {
            //进行关联证书操作时,不动产单元来源为不动产库
            model.addAttribute("glzs", glzs);
            bdcdyly = Constants.BDCDYLY_BDC;
        }
        model.addAttribute("bdcdyly", bdcdyly);
        model.addAttribute(ParamsConstants.ZDTZM_LOWERCASE, zdtzm); //放入宗地特征码
        model.addAttribute("dyfs", dyfs); //放入抵押方式
        model.addAttribute(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm);//放入不动产类型代码
        model.addAttribute(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm);//放入权利性质代码
        model.addAttribute(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm);//放入原申请类型代码

        //获取申请类型名称和工作流定义ID
        List<BdcZdSqlx> bdcZdSqlxList = null;
        if (xmxx != null && StringUtils.isNotBlank(xmxx.getSqlx())) {
            Example example = new Example(BdcZdSqlx.class);
            example.createCriteria().andEqualTo("dm", xmxx.getSqlx());
            bdcZdSqlxList = entityMapper.selectByExample(BdcZdSqlx.class, example);
        }
        if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
            model.addAttribute("sqlxmc", bdcZdSqlxList.get(0).getMc());
            model.addAttribute("workFlowDefId", bdcZdSqlxList.get(0).getWdid());
        }
        if (xmxx != null) {
            String sqlxdm = xmxx.getSqlx();
            model.addAttribute("sqlxdm", sqlxdm);
            //解封验证逻辑单独处理
            String sqlx = "";
            if (CommonUtil.indexOfStrs(Constants.SQLX_ZXCFDJ_DM, sqlxdm)) {
                sqlx = "JF";
            }
            //在建工程抵押或批量抵押选一条权利验证所有
            if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, sqlxdm)) {
                model.addAttribute("plChoseOne", Constants.PL_CHOSE_ONE);
            }
            model.addAttribute("sqlx", sqlx);
            model.addAttribute("djlx", xmxx.getDjlx());//放入登记类型
            model.addAttribute("wiid", xmxx.getWiid());
            model.addAttribute("proid", proid);
        }
        model.addAttribute("showOptimize", AppConfig.getProperty("selectBdcdy.showOptimization"));
        model.addAttribute("notShowCk", AppConfig.getProperty("selectBdcdy.notShowCk.checkCode"));
    }

    @Override
    public void initAddHbXmModel(Model model) {
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<Map> sqlxList = bdcZdGlService.getZdSqlxList();
        model.addAttribute("djList", djlxList);

        model.addAttribute("sqList", sqlxList);
        //登记类型和申请类型默认值
        model.addAttribute("djlx", Constants.DJLX_CSDJ_DM);
    }

    @Override
    public Map<String, Object> getDjsjBdcdySearchMap(String djh, String bdcdyh, String dcxc, String qlr, String tdzl, String bdclx, String bdclxdm, String zdtzm, String htbh, String qlxzdm, String bdcdyhs, String exactQuery, String fwbm) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            String[] djids = bdcdyService.getDjQlrIdsByQlr(dcxc, bdclx);
            if (djids != null && djids.length > 0)
                map.put("djids", djids);
            String bdcdycxFwbhZdmc = AppConfig.getProperty("bdcdycx.fwbh.zdmc");
            if (StringUtils.isNotBlank(bdcdycxFwbhZdmc)) {
                map.put("fwbh", "t." + bdcdycxFwbhZdmc + "='" + StringUtils.deleteWhitespace(dcxc) + "'");
            }
        } else {
            if (StringUtils.isNotBlank(htbh)) {
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
                    if (StringUtils.indexOf(bdcdyhsByHt, ",") > -1) {
                        bdcdyhListByHt = Arrays.asList(StringUtils.split(bdcdyhsByHt, ","));
                    } else {
                        bdcdyhListByHt.add(bdcdyhsByHt);
                    }
                    if (CollectionUtils.isNotEmpty(bdcdyhListByHt)) {
                        map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhListByHt);
                    }
                } catch (IOException e) {
                    logger.error("getDjsjBdcdySearchMap", e);
                } finally {
                    if (method != null) {
                        method.releaseConnection();
                    }
                    if (client != null) {
                        ((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
                    }
                }
            }
            if (StringUtils.isNotBlank(djh)) {
                map.put("djh", StringUtils.deleteWhitespace(djh));
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }

            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", StringUtils.deleteWhitespace(qlr));
            }

            if (StringUtils.isNotBlank(bdclx)) {
                map.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
            }
            if (StringUtils.isNotBlank(tdzl)) {
                map.put("tdzl", StringUtils.deleteWhitespace(tdzl));
            }

        }
        if (StringUtils.isNotBlank(bdclxdm)) {
            map.put(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm.split(","));
            String bdclxdmConfig = bdclxdm.split(",")[0];
            if (StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "F")) {
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDFW);
            } else if (StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "W")) {
                if (zdtzm.indexOf("H") > -1 || zdtzm.indexOf("GG") > -1) {
                    map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_HY);
                } else {
                    map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TD);
                }

            } else if (StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "L"))
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDSL);
            else if (StringUtils.isBlank(bdclx) && StringUtils.equals(bdclxdmConfig, "Q"))
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDQT);
        }
        // 根据fwbm定位bdcdyh
        if (StringUtils.isNotBlank(fwbm)) {
            List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHsByFwbm(fwbm);
            if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                List<String> bdcdyhList = new ArrayList<String>();
                for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                    if (djsjFwHs != null && StringUtils.isNotBlank(djsjFwHs.getBdcdyh())) {
                        bdcdyhList.add(djsjFwHs.getBdcdyh());
                    }
                }
                map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
            }
        }
        if (StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for (String tempBdcdyh : bdcdyhs.split(",")) {
                if (StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
        }
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        if (StringUtils.isNotBlank(qlxzdm)) {
            map.put(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm.split(","));
        }
        map.put("bdclxZx", "");
        map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        return map;
    }

    @Override
    public Map<String, Object> getBdczsListSearchMap(String bdcdyh, String qllx, String bdcqzh, String qlr, String dyr, String zl, String bdclx, String bdclxdm, String dcxc, String zdtzm, String qlxzdm, String dyfs, String ysqlxdm, String proid, String bdcdyhs, String fzqssj, String fzjssj, String zstype, String cqzhjc, String exactQuery, String fwbm, String proids) {
        Map<String, Object> map = new HashMap<String, Object>();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String sqlx = bdcXm != null ? bdcXm.getSqlx() : "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(bdcqzh)) {
                map.put("bdcqzh", StringUtils.deleteWhitespace(bdcqzh));
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", StringUtils.deleteWhitespace(qlr));
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", StringUtils.deleteWhitespace(zl));
            }
            if (StringUtils.isNotBlank(cqzhjc)) {
                map.put(ParamsConstants.CQZHJC_LOWERCASE, StringUtils.deleteWhitespace(cqzhjc));
            }
            if (StringUtils.isNotBlank(dyr)) {
                map.put("dyr", StringUtils.deleteWhitespace(dyr));
            }
        }
        if (StringUtils.isNotBlank(bdclxdm)) {
            map.put(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm.split(","));
        }
        if (StringUtils.isNotBlank(bdclx)) {
            map.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
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
                map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
            } else {
                bdcdyhList.add("null");
                map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
            }
        }
        /**
         * @author jiangganzhi
         * @description 异议登记不根据权利类型过滤 否则无法搜出证明
         */
        if (StringUtils.isNotBlank(sqlx) && !StringUtils.equals(sqlx, Constants.SQLX_YYDJ_DM) && StringUtils.isNotBlank(qllx)) {
            map.put("qllx", qllx.split(","));
        }
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        if (StringUtils.isNotBlank(dyfs)) {
            map.put("dyfs", dyfs);
        }
        if (StringUtils.isNotBlank(qlxzdm)) {
            map.put(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm.split(","));
        }
        if (StringUtils.isNotBlank(ysqlxdm)) {
            map.put(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm.split(","));
        } else {
            if (StringUtils.isNotBlank(sqlx) && StringUtils.equals(sqlx, SQLX_YG_BGDJ)) {
                map.put(ParamsConstants.YSQLXDM_LOWERCASE, SQLX_YG_YGSPF.split(","));
            } else if ((StringUtils.isNotBlank(sqlx) && StringUtils.equals(sqlx, Constants.SQLX_YG_YGDYBG))) {
                map.put(ParamsConstants.YSQLXDM_LOWERCASE, SQLX_YG_BDCDY.split(","));
            }

        }
        if (StringUtils.isNotBlank(zstype)) {
            if (StringUtils.equals(zstype, Constants.BDCQZS_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZS_BH_FONT);
            } else if (StringUtils.equals(zstype, Constants.BDCQZM_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZM_BH_FONT);
            } else {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, "不动产权证明单");
            }
        }
        if (StringUtils.isNotBlank(sqlx) && (Constants.SQLX_SPFSCKFSZC_DM.equals(sqlx) || Constants.SQLX_SPFXZBG_DM.equals(sqlx))) {
            zstype = AppConfig.getProperty("spfscdj.zstype");
            map.put(ParamsConstants.ZSTYPE_LOWERCASE, zstype);
        }
        //如果不是商品房买卖转移登记和商品房转移登记，首次登记信息表变更登记，商品房首次开发商自持要求不能搜到首次登记证
        if (StringUtils.isNotBlank(sqlx) && !CommonUtil.indexOfStrs(Constants.SQLX_SCDJZM_NO, bdcXm.getSqlx())) {
            // 商品房转移登记只能选证书和首次登记证，更正登记可以选择证书和证明,抵押首次和批量抵押多抵多选择不动产权证书,
            if (StringUtils.equals(AppConfig.getProperty("dysc.show.scdjz"), ParamsConstants.TRUE_LOWERCASE) && CommonUtil.indexOfStrs(Constants.SQLX_DYSC_YES_KS, bdcXm.getSqlx())) {
                map.put("spfZyZsAndScdjz", ParamsConstants.TRUE_LOWERCASE);
            } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFMMZYDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YFZYDYDJ_DM)) {
                map.put("spfZyZsAndScdjz", ParamsConstants.TRUE_LOWERCASE);
            } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GZ_DM)) {
                map.put("gzdjZsAndZm", ParamsConstants.TRUE_LOWERCASE);
            } else if (CommonUtil.indexOfStrs(Constants.QLLX_ZMS, bdcXm.getQllx()) && !CommonUtil.indexOfStrs(Constants.SQLX_DYSC_NO, bdcXm.getSqlx()) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)) {
                map.put("disSpfscZm", ParamsConstants.TRUE_LOWERCASE);
            } else {
                map.put("disSpfscZs", ParamsConstants.TRUE_LOWERCASE);
            }
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotBlank(fzqssj)) {
                map.put("fzqssj", simpleDateFormat.parse(fzqssj));
            }
            if (StringUtils.isNotBlank(fzjssj)) {
                map.put("fzjssj", simpleDateFormat.parse(fzjssj));
            }
        } catch (ParseException e) {
            logger.error("getBdczsListSearchMap", e);
        }
        if (StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for (String tempBdcdyh : bdcdyhs.split(",")) {
                if (StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
        }
        map.put("filterNullBdcqzh", true);
        if (StringUtils.isNotBlank(proids)) {
            map.put("proidList", StringUtils.split(proids, ","));
        }
        /**
         * @author bianwen
         * @description 将权利已注销的证书过滤掉
         */
        map.put("qszt", Constants.QLLX_QSZT_HR);
        map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        return map;
    }

    @Override
    public Map<String, Object> getGdfczListSearchMapAndPath(String bdcdyh, String fczh, String qlr, String fwzl, String dcxc, String qllx, String zdtzm, String proid, String cqzhjc, String exactQuery) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String dydjlx = null;
        String sqlxdm = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(fczh)) {
                map.put("fczh", StringUtils.deleteWhitespace(fczh));
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", StringUtils.deleteWhitespace(qlr));
            }
            if (StringUtils.isNotBlank(fwzl)) {
                map.put("fwzl", StringUtils.deleteWhitespace(fwzl));
            }
            if (StringUtils.isNotBlank(cqzhjc)) {
                map.put(ParamsConstants.CQZHJC_LOWERCASE, StringUtils.deleteWhitespace(cqzhjc));
            }
            if (StringUtils.isNotBlank(qllx)) {
                map.put("qllx", StringUtils.deleteWhitespace(qllx));
            }
        }
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        }
        map.put("iszx", "0");
        Boolean noBdcdy = false;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if (bdcXm != null) {
                if (CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, bdcXm.getSqlx()) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM)) {
                    map.put(PARAMETER_ISNOBDC, "yes");
                    noBdcdy = true;
                }
                if (StringUtils.isNotBlank(bdcXm.getSqlx())) {
                    sqlxdm = bdcXm.getSqlx();
                }
                if (StringUtils.isNotBlank(bdcXm.getDydjlx())) {
                    dydjlx = bdcXm.getDydjlx();
                }
            }
        }
        map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        //更正登记走getGdfczByPage的方法
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        String path = null;
        if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
            path = "getGdfczUnSearchBdcdyOptimizeByPage";
        } else {
            path = "getGdfczUnSearchBdcdyByPage";
        }
        if (StringUtils.isNotBlank(qllx) && qllx.indexOf(Constants.QLLX_DYAQ) > -1 && !StringUtils.equals(dydjlx, Constants.DJLX_CSDJ_DM) && (StringUtils.isNotEmpty(sqlxdm) && !StringUtils.equals(sqlxdm, Constants.SQLX_GZ_DM))) {
            //此处都为房产证的信息，过滤掉土地的
            map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDFW);
            map.put("cxlx", Constants.SELECTBDCDY_CXLX_DY);
            if (noBdcdy || StringUtils.isNotBlank(bdcdyh)) {
                if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
                    path = "getGdYgGdDySearchAllOptimizeByPage";
                } else {
                    path = "getGdYgGdDySearchAllByPage";
                }
                map.remove(ParamsConstants.BDCLX_LOWERCASE);
            }

        } else if (StringUtils.isNotBlank(qllx) && qllx.indexOf(Constants.QLLX_YGDJ) > -1 && !StringUtils.equals(dydjlx, Constants.DJLX_CSDJ_DM) && (StringUtils.isNotEmpty(sqlxdm) && !StringUtils.equals(sqlxdm, Constants.SQLX_GZ_DM))) {
            map.put("cxlx", Constants.SELECTBDCDY_CXLX_YG);
            if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(SQLX_YG_BGDJ)) {
                map.put("ygdjzl", YGDJZL_YGSPF_MC);
            } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(SQLX_YG_YGDYBG)) {
                map.put("ygdjzl", YGDJZL_YGSPFDYAQ_MC);
            }
            if (noBdcdy || StringUtils.isNotBlank(bdcdyh)) {
                if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
                    path = "getGdYgGdDySearchAllOptimizeByPage";
                } else {
                    path = "getGdYgGdDySearchAllByPage";
                }
            }
        } else {
            if (noBdcdy || StringUtils.isNotBlank(bdcdyh)) {
                if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
                    path = "getGdfczSearchAllOptimizeByPage";
                } else {
                    path = "getGdfczSearchAllByPage";
                }
            } else {
                if (StringUtils.equals(sqlxdm, Constants.SQLX_PLCF)) {
                    map.put("notShowNoBdcdyh", "true");
                }
                if (!StringUtils.equals(sqlxdm, Constants.SQLX_GZ_DM)) {
                    map.put("cxlx", Constants.SELECTBDCDY_CXLX_CQ);
                }
            }
        }
        map.put("path", path);
        map.put("filterFsss", "true");
        return map;
    }

    @Override
    public Map<String, Object> getGdtdzListSearchMapAndPath(String bdcdyh, String tdzh, String qlr, String dcxc, String tdzl, String qllx, String zdtzm, String proid, String cqzhjc, String exactQuery) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //是否不匹配不动产单元默认否
        String isNoBdc = "no";
        String sqlxdm = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(tdzh)) {
                map.put("tdzh", StringUtils.deleteWhitespace(tdzh));
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", StringUtils.deleteWhitespace(qlr));
            }
            if (StringUtils.isNotBlank(tdzl)) {
                map.put("tdzl", StringUtils.deleteWhitespace(tdzl));
            }
            if (StringUtils.isNotBlank(cqzhjc)) {
                map.put(ParamsConstants.CQZHJC_LOWERCASE, StringUtils.deleteWhitespace(cqzhjc));
            }
        }
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        }
        map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        BdcXm bdcXm = null;
        String dydjlx = "";
        Boolean noBdcdy = false;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if (bdcXm != null) {
                if (StringUtils.isNotBlank(bdcXm.getDydjlx())) {
                    dydjlx = bdcXm.getDydjlx();
                }
                if (StringUtils.isNotBlank(bdcXm.getSqlx())) {
                    sqlxdm = bdcXm.getSqlx();
                }
                if (CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, bdcXm.getSqlx()) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)) {
                    isNoBdc = "yes";
                    noBdcdy = true;
                }
                map.put(PARAMETER_ISNOBDC, isNoBdc);
            }
        }
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        String path = null;
        if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
            path = "getGdtdzUnSearchBdcdyOptimizeByPage";
        } else {
            path = "getGdtdzUnSearchBdcdyByPage";
        }
        if (StringUtils.isNotBlank(qllx) && StringUtils.indexOf(qllx, Constants.QLLX_DYAQ) > -1 && !StringUtils.equals(dydjlx, Constants.DJLX_CSDJ_DM) && (bdcXm != null && StringUtils.isNotEmpty(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GZ_DM))) {
            if (noBdcdy || StringUtils.isNotBlank(bdcdyh)) {
                if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
                    path = "getTdGdDySearchAllOptimizeByPage";
                } else {
                    path = "getTdGdDySearchAllByPage";
                }
            } else {
                map.put("cxlx", Constants.SELECTBDCDY_CXLX_DY);
            }
        } else {
            if (noBdcdy || StringUtils.isNotBlank(bdcdyh)) {
                if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
                    path = "getGdtdzSearchAllOptimizeByPage";
                } else {
                    path = "getGdtdzSearchAllByPage";
                }
            } else {
                if (StringUtils.equals(sqlxdm, Constants.SQLX_PLCF)) {
                    map.put("notShowNoBdcdyh", "true");
                }
                if (!StringUtils.equals(sqlxdm, Constants.SQLX_GZ_DM)) {
                    map.put("cxlx", Constants.SELECTBDCDY_CXLX_CQ);
                }
            }
        }
        map.put("iszx", "0");
        map.put("path", path);
        return map;
    }

    @Override
    public Map<String, String> getBdcDateByQlid(String qlid) {
        Map<String, String> map = new HashMap<String, String>();
        List<GdQlDyhRel> gdQlDyhRelList = null;
        List<GdBdcQlRel> gdBdcQlRelList = null;
        if (StringUtils.isNotBlank(qlid)) {
            gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel("", qlid, "");
            gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
        }
        if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            GdQlDyhRel gdQlDyhRel = gdQlDyhRelList.get(0);
            if (StringUtils.isNotBlank(gdQlDyhRel.getBdcdyh())) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, gdQlDyhRel.getBdcdyh());
            }
            if (StringUtils.isNotBlank(gdQlDyhRel.getDjid())) {
                map.put("djid", gdQlDyhRel.getDjid());
            }
            if (StringUtils.isNotBlank(gdQlDyhRel.getBdclx())) {
                map.put(ParamsConstants.BDCLX_LOWERCASE, gdQlDyhRel.getBdclx());
            }
        } else {
            //未匹配
            map.put("ppzt", Constants.GD_PPZT_WPP);
        }
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList) && CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            Boolean exsitNoPp = false;
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                List<BdcGdDyhRel> gdDyhRelList = null;
                if (StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                    gdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", gdBdcQlRel.getBdcid());
                }
                //权利与不动产单元号匹配关系中有记录，但是物与不动产单元号匹配中出现没有记录的情况（1、部分匹配 2、数据错误）
                if (CollectionUtils.isEmpty(gdDyhRelList)) {
                    exsitNoPp = true;
                    break;
                }
            }
            if (exsitNoPp) {
                //部分匹配
                map.put("ppzt", Constants.GD_PPZT_BFPP);
            } else {
                //已匹配
                map.put("ppzt", Constants.GD_PPZT_WCPP);
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getXzyy(String qlid, String cqzh) {
        //正则取产权证号
        String regex = ">(.*)<";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cqzh);
        while (matcher.find()) {
            cqzh = matcher.group(1);
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        List<GdBdcSd> list = gdXmService.getGdBdcSdByCqzhAndQlid(cqzh == null ? "" : cqzh, qlid == null ? "" : qlid, Constants.SDZT_SD);
        //该条过渡数据已被锁定
        if (CollectionUtils.isNotEmpty(list)) {
            GdBdcSd gdBdcSd = list.get(0);
            String xzyy = gdBdcSd.getXzyy();
            resultMap.put("msg", "false");
            resultMap.put("xzyy", xzyy);
        } else {
            List<BdcBdcZsSd> bdcList = bdcXmService.getBdcSd("cqzh", cqzh == null ? "" : cqzh, Constants.SDZT_SD);
            if (CollectionUtils.isNotEmpty(bdcList)) {
                BdcBdcZsSd bdcBdcZsSd = bdcList.get(0);
                String xzyy = bdcBdcZsSd.getXzyy();
                resultMap.put("msg", "false");
                resultMap.put("xzyy", xzyy);
            } else {
                resultMap.put("msg", "true");
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getGdcfListSearchMap(String proid, String bdcdyh, String cfwh, String qlr, String dcxc, String yqzh, String fwzl, String tdzl, String exactQuery) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(cfwh)) {
                map.put("cfwh", StringUtils.deleteWhitespace(cfwh));
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", StringUtils.deleteWhitespace(qlr));
            }
        }
        //是否不匹配不动产单元默认是
        String isNoBdc = "no";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
                if (bppSqlxdmList != null && bppSqlxdmList.contains(bdcXm.getSqlx())) {
                    isNoBdc = "yes";
                }
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD)) {
                    isNoBdc = "";
                }
            }
        }
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
            if (StringUtils.isNotBlank(cfwh)) {
                map.put(ParamsConstants.CFWH_LOWERCASE, StringUtils.deleteWhitespace(cfwh));
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(yqzh)) {
                map.put(ParamsConstants.YQZH_LOWERCASE, StringUtils.deleteWhitespace(yqzh));
            }
            if (StringUtils.isNotBlank(fwzl)) {
                map.put(ParamsConstants.FWZL_LOWERCASE, StringUtils.deleteWhitespace(fwzl));
            }
            if (StringUtils.isNotBlank(tdzl)) {
                map.put(ParamsConstants.TDZL_LOWERCASE, StringUtils.deleteWhitespace(tdzl));
            }
            map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        }
        map.put(PARAMETER_ISNOBDC, isNoBdc);
        return map;
    }

    @Override
    public String getGdCfCqzh(String bdcdyid, String gdproid, String qlid) {
        String cqzh = "";
        if (StringUtils.isBlank(bdcdyid) && StringUtils.isNotBlank(qlid)) {
            bdcdyid = getBdcdyidByGdqlid(qlid);
        }
        if (StringUtils.isNotBlank(bdcdyid) || StringUtils.isNotBlank(gdproid)) {
            HashMap<String, String> zsMap = qllxService.getBdcZsByBdcdyid(bdcdyid);
            if (zsMap != null && StringUtils.isNotBlank(zsMap.get("BDCQZH"))) {
                cqzh = zsMap.get("BDCQZH");
            }
            //分不动产和过渡两种情况获取产权证号
            if (StringUtils.isBlank(cqzh) && StringUtils.isNotBlank(gdproid) && StringUtils.isBlank(bdcdyid)) {
                List<GdBdcQlRel> gdBdcQlRelListTmp = gdBdcQlRelService.queryGdBdcQlListByQlid(gdproid);
                if (CollectionUtils.isNotEmpty(gdBdcQlRelListTmp) && StringUtils.isNotBlank(gdBdcQlRelListTmp.get(0).getBdcid())) {
                    String bdcid = gdBdcQlRelListTmp.get(0).getBdcid();
                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcid);
                    for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                        GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRel.getQlid());
                        if (null != gdFwsyq && StringUtils.isNotBlank(gdFwsyq.getFczh()) && StringUtils.equals("0", gdFwsyq.getIszx().toString())) {
                            cqzh = gdFwsyq.getFczh();
                        }
                        if (StringUtils.isBlank(cqzh)) {
                            GdTdsyq gdTdSyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRel.getQlid());
                            if (null != gdTdSyq && StringUtils.isNotBlank(gdTdSyq.getTdzh()) && StringUtils.equals("0", gdTdSyq.getIszx().toString())) {
                                cqzh = gdTdSyq.getTdzh();
                            }
                        }
                        if (StringUtils.isBlank(cqzh)) {
                            GdYg gdYg = gdFwService.getGdYgByYgid(gdBdcQlRel.getQlid(), 0);
                            if (gdYg != null && StringUtils.isNotBlank(gdYg.getYgdjzmh())) {
                                cqzh = gdYg.getYgdjzmh();
                            }
                        }
                    }
                }
            } else if (StringUtils.isNotBlank(bdcdyid) && StringUtils.isBlank(cqzh)) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(bdcBdcdy.getBdcdyh(), null);
                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                    String bdcid = bdcGdDyhRelList.get(0).getGdid();
                    if (StringUtils.isNotBlank(bdcid)) {
                        List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcid);
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRel.getQlid());
                                if (null != gdFwsyq && StringUtils.isNotBlank(gdFwsyq.getFczh()) && StringUtils.equals("0", gdFwsyq.getIszx().toString())) {
                                    cqzh = gdFwsyq.getFczh();
                                }
                                if (StringUtils.isBlank(cqzh)) {
                                    GdTdsyq gdTdSyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRel.getQlid());
                                    if (null != gdTdSyq && StringUtils.isNotBlank(gdTdSyq.getTdzh()) && StringUtils.equals("0", gdTdSyq.getIszx().toString())) {
                                        cqzh = gdTdSyq.getTdzh();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return cqzh;
    }

    @Override
    public void getShowHhcfModel(Model model, String proid) {
        String sqlxMc = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx()))
                sqlxMc = bdcZdGlService.getSqlxMcByDm(bdcXm.getSqlx());
        }
        model.addAttribute("sqlxMc", sqlxMc);
        model.addAttribute("proid", proid);
    }

    @Override
    public Map<String, Object> getdataMapByProid(String proid) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String zl = "";
        String qlr = "";
        String bdcdyh = "";
        String bdclx = "";
        String bdcdyid = "";
        String fwbm = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            if (bdcBdcdy != null) {
                if (StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    bdcdyh = bdcBdcdy.getBdcdyh();
                }
                if (StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                    bdcdyid = bdcBdcdy.getBdcdyid();
                }
                if (StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                    bdclx = bdcBdcdy.getBdclx();
                }
                if (StringUtils.isNotBlank(bdcBdcdy.getFwbm())) {
                    fwbm = bdcBdcdy.getFwbm();
                }
            }
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null) {
                if (StringUtils.isNotBlank(bdcSpxx.getBdclx())) {
                    bdclx = bdcSpxx.getBdclx();
                }
                if (StringUtils.isNotBlank(bdcSpxx.getZl())) {
                    zl = bdcSpxx.getZl();
                }
            }
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                StringBuilder qlrBuilder = new StringBuilder();
                for (BdcQlr bdcQlr : bdcQlrList) {
                    if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                        qlrBuilder.append(bdcQlr.getQlrmc()).append(",");
                        qlr = qlrBuilder.toString();
                    }
                }
            }
            if (StringUtils.isNotBlank(qlr)) {
                qlr = qlr.substring(0, qlr.length() - 1);
            }
        }
        resultMap.put("zl", zl);
        resultMap.put("qlr", qlr);
        resultMap.put("fwbm", fwbm);
        resultMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        resultMap.put("bdcdyid", bdcdyid);
        resultMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        resultMap.put("bdcqzh", bdcZsService.getCombineBdcqzhByProid(proid));
        return resultMap;
    }

    @Override
    public Map<String, Object> getQlxxListMap(String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm, String qlxzdm, String dyfs, String ysqlxdm, String proid, String bzxr, String cfwh, String cqzhjc, String exactQuery, String fwbm, String proids) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        }
        if (StringUtils.isNotBlank(ysqlxdm)) {
            map.put(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm.split(","));
        }
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        if (StringUtils.equals(ParamsConstants.TRUE_LOWERCASE, showOptimize)) {
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(bdcqzh)) {
                map.put(ParamsConstants.BDCQZH_LOWERCASE, StringUtils.deleteWhitespace(bdcqzh));
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put(ParamsConstants.ZL_LOWERCASE, StringUtils.deleteWhitespace(zl));
            }
            if (StringUtils.isNotBlank(cfwh)) {
                map.put(ParamsConstants.CFWH_LOWERCASE, StringUtils.deleteWhitespace(cfwh));
            }
            if (StringUtils.isNotBlank(bzxr)) {
                map.put(ParamsConstants.BZXR_LOWERCASE, StringUtils.deleteWhitespace(bzxr));
            }
            if (StringUtils.isNotBlank(cqzhjc)) {
                map.put(ParamsConstants.CQZHJC_LOWERCASE, StringUtils.deleteWhitespace(cqzhjc));
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", StringUtils.deleteWhitespace(qlr));
            }
            map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
            // 根据fwbm定位bdcdyh
            if (StringUtils.isNotBlank(fwbm)) {
                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHsByFwbm(fwbm);
                if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                    List<String> bdcdyhList = new ArrayList<String>();
                    for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                        if (djsjFwHs != null && StringUtils.isNotBlank(djsjFwHs.getBdcdyh())) {
                            bdcdyhList.add(djsjFwHs.getBdcdyh());
                        }
                    }
                    map.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
                }
            }
        }
        if (StringUtils.isNotBlank(proids)) {
            map.put("proidList", StringUtils.split(proids, ","));
        }
        // 司法处置解封注销登记只允许搜到裁定状态的数据
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZX_SFCD)) {
            map.put("cdzt", Constants.CDZT_CD);
        }
        //sc 需要展示不生成证书的权力信息
        map.put("filterNullBdcqzh", true);
        return map;
    }

    @Override
    public String getBdcdyidByGdqlid(String gdqlid) {
        String bdcdyid = null;
        List<GdQlDyhRel> gdQlDyhRelList = null;
        if (StringUtils.isNotBlank(gdqlid)) {
            gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel("", gdqlid, "");
        }
        if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            GdQlDyhRel gdQlDyhRel = gdQlDyhRelList.get(0);
            if (StringUtils.isNotBlank(gdQlDyhRel.getBdcdyh())) {
                bdcdyid = bdcdyService.getBdcdyidByBdcdyh(gdQlDyhRel.getBdcdyh());
            }

        }
        return bdcdyid;
    }

    @Override
    public Map<String, Object> getBdcdyZt(String proid, String djid, String bdclx) {
        String bdcdyh = "";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(proid) && !StringUtils.equals(proid, "null")) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                bdcdyh = bdcBdcdy.getBdcdyh();
            }
        } else if (StringUtils.isNotBlank(djid) && !StringUtils.equals(djid, "null") && !StringUtils.equals(djid, "undefined")) {
            HashMap map = new HashMap();
            if (StringUtils.isNotBlank(bdclx)) {
                map.put("bdclx", bdclx);
            }
            map.put("id", djid);
            List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
            if (CollectionUtils.isNotEmpty(bdcdyList) && bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                bdcdyh = bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString();
            }
        }
        if (StringUtils.isNotBlank(bdcdyh)) {
            Map map = new HashedMap();
            map.put("bdcdyh", bdcdyh);
            List<cn.gtmap.estateplat.model.config.BdcCxBdcdyZt> bdcCxBdcdyZtList = qlztService.getBdcCxBdcdyZtList(map);
            if (CollectionUtils.isNotEmpty(bdcCxBdcdyZtList)) {
                resultMap.put("bdcCxBdcdyZt", bdcCxBdcdyZtList.get(0));
            } else {
                resultMap.put("bdcCxBdcdyZt", null);
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getBdcdyQlZt(String proid, String djid, String bdclx) {
        String bdcdyh = "";
        boolean existXscq = false;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(proid) && !StringUtils.equals(proid, "null")) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                bdcdyh = bdcBdcdy.getBdcdyh();
            }
        } else if (StringUtils.isNotBlank(djid) && !StringUtils.equals(djid, "null") && !StringUtils.equals(djid, "undefined")) {
            HashMap map = new HashMap();
            if (StringUtils.isNotBlank(bdclx)) {
                map.put("bdclx", bdclx);
            }
            map.put("id", djid);
            List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
            if (CollectionUtils.isNotEmpty(bdcdyList) && bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                bdcdyh = bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString();
            }
        }

        if (StringUtils.isNotBlank(bdcdyh)) {
            List<String> bdcdyhList = new ArrayList<>();
            bdcdyhList.add(bdcdyh);
            String url = AppConfig.getProperty("currency.url") + "/rest/v1.0/xzxx/bdcdyhxz";
            String result = HttpClientUtil.doPostJson(url, JSONObject.toJSONString(bdcdyhList));
            if (StringUtils.isNotBlank(result)) {
                try {
                    JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
                    if (jsonObject.containsKey("data") && jsonObject.get("data") != null) {
                        List<JSONObject> dataList = (List<JSONObject>) jsonObject.get("data");
                        if (CollectionUtils.isNotEmpty(dataList)) {
                            JSONObject data = dataList.get(0);
                            if (data.containsKey("qlzt") && data.get("qlzt") != null) {
                                JSONObject qlzt = (JSONObject) data.get("qlzt");
                                resultMap = qlzt;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.info("返回数据解析错误=" + e);
                }

            }
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
            if (bdcBdcdy != null) {
                List<String> proidList = bdcXmService.getXsCqProid(bdcBdcdy.getBdcdyid());
                if (CollectionUtils.isNotEmpty(proidList)) {
                    existXscq = true;
                }
            }
        }
        resultMap.put("existXscq", existXscq);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> getMulBdcqzxx(String yxmids) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (StringUtils.isNotBlank(yxmids)) {
            String[] yproids = yxmids.split(",");
            for (String proid : yproids) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        for (BdcXm xm : bdcXmList) {
                            QllxVo qllxVo = qllxService.queryQllxVo(xm);
                            if (qllxVo != null && qllxVo.getQszt() != null && qllxVo.getQszt().equals(QLLX_QSZT_XS)) {
                                Map<String, Object> qlxxMap = new HashMap<>();
                                qlxxMap.put("proid", bdcXm.getProid());
                                String bdcdyh = bdcdyService.getBdcdyhByProid(xm.getProid());
                                if (StringUtils.isNotBlank(bdcdyh)) {
                                    qlxxMap.put("bdcdyh", bdcdyh);
                                }
                                mapList.add(qlxxMap);
                            }
                        }
                    }
                }
            }
        }
        return mapList;
    }
}
