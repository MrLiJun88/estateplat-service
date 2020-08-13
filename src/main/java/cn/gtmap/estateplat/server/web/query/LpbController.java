package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
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

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zzhw
 * Date: 15-3-20
 * Time: 下午4:43
 * doc:楼盘表查询
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/lpb")
public class LpbController extends BaseController {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DjxxMapper djxxMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcSqlxQllxRelService bdcSqlxQllxRelService;
    @Autowired
    private BdcHsService bdcHsService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;


    public static final String PARAMETER_MULSELECT = "mulSelect";
    public static final String PARAMETER_FWDCBINDEX = "fw_dcb_index";
    public static final String PARAMETER_OPENQLWAY = "openQlWay";
    public static final String PARAMETER_DCBID = "dcbId";
    public static final String PARAMETER_ISNOTWF = "isNotWf";

    /*楼盘表查询**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid, @RequestParam(value = "openQlWay", required = false) String openQlWay, @RequestParam(value = "dcxc", required = false) String dcxc) {
        List<Map> bdcList = bdcZdGlService.getZdBdclx();

        String lpbGjss = AppConfig.getProperty("lpbGjss.order");
        List<String> lpbGjssOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(lpbGjss) && lpbGjss.split(",").length > 0) {
            for (String lpbGjssZd : lpbGjss.split(",")) {
                lpbGjssOrderList.add(lpbGjssZd);
            }
        }
        model.addAttribute("bdcList", bdcList);
        model.addAttribute("lpbGjss", lpbGjss);
        model.addAttribute("lpbGjssOrderList", lpbGjssOrderList);


        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(PARAMETER_OPENQLWAY, openQlWay);
        model.addAttribute("dcxc", dcxc);
        return "query/lpbList";
    }

    @ResponseBody
    @RequestMapping("/getLpbPagesJson")
    public Object getLpbPagesJson(Pageable pageable, String fwmc, String zdh, String zl, String dcxc, String zdtzm, String isFilter, String xmjc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(fwmc)) {
                map.put("fwmc", StringUtils.deleteWhitespace(fwmc));
            } else if (StringUtils.isNotBlank(xmjc)) {
                map.put("fwmc", StringUtils.deleteWhitespace(xmjc));
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", StringUtils.deleteWhitespace(zl));
            }
            if (StringUtils.isNotBlank(zdh)) {
                map.put("zdh", StringUtils.deleteWhitespace(zdh));
            }
        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm))
            map.put("xzqdm", userDwdm);
        //无论是简单查询和高级查询都要过来特征码
        if (org.apache.commons.lang3.StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        //是否通过发证过滤
        if (StringUtils.isNotBlank(isFilter) && StringUtils.equals(isFilter, "true")) {
            map.put("isfilter", isFilter);
        }
        return repository.selectPaging("getLpbListByPage", map, pageable);
    }

    @RequestMapping(value = "/queryZdList", method = RequestMethod.GET)
    public String queryLpbList(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "mulSelect", required = false) String mulSelect) {
        BdcXm xmxx = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String yqllxdm = "";
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        if (xmxx != null && (org.apache.commons.lang3.StringUtils.isNotBlank(xmxx.getSqlx()) && org.apache.commons.lang3.StringUtils.isNotBlank(xmxx.getQllx()))) {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put(ParamsConstants.SQLXDM_LOWERCASE, xmxx.getSqlx());
            queryMap.put(ParamsConstants.QLLXDM_LOWERCASE, xmxx.getQllx());
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }

        if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcSqlxQllxRel.getYqllxdm()))
                yqllxdm = bdcSqlxQllxRel.getYqllxdm();
            if (bdcSqlxQllxRel.getBdcdyly() != null)
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm()))
                zdtzm = bdcSqlxQllxRel.getZdtzm();
        }
        model.addAttribute(ParamsConstants.YQLLXDM_LOWERCASE, yqllxdm);
        model.addAttribute(ParamsConstants.BDCDYLY_LOWERCASE, bdcdyly);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        model.addAttribute(PARAMETER_MULSELECT, mulSelect);
        model.addAttribute("type", "lpb");
        return "query/zdList";
    }

    @RequestMapping(value = "/selectLjz", method = RequestMethod.GET)
    public String selectLjz(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "mulSelect", required = false) String mulSelect, @RequestParam(value = "isNotWf", required = false) String isNotWf, @RequestParam(value = "isFilter", required = false) String isFilter) {
        BdcXm xmxx = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String yqllxdm = "";
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        String wiid = "";
        if (xmxx != null && (org.apache.commons.lang3.StringUtils.isNotBlank(xmxx.getSqlx()) && org.apache.commons.lang3.StringUtils.isNotBlank(xmxx.getQllx()))) {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put(ParamsConstants.SQLXDM_LOWERCASE, xmxx.getSqlx());
            queryMap.put(ParamsConstants.QLLXDM_LOWERCASE, xmxx.getQllx());
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }

        if (xmxx != null && StringUtils.isNotBlank(xmxx.getWiid())) {
            wiid = xmxx.getWiid();
        }

        if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcSqlxQllxRel.getYqllxdm()))
                yqllxdm = bdcSqlxQllxRel.getYqllxdm();
            if (bdcSqlxQllxRel.getBdcdyly() != null)
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm()))
                zdtzm = bdcSqlxQllxRel.getZdtzm();
        }
        model.addAttribute(ParamsConstants.YQLLXDM_LOWERCASE, yqllxdm);
        model.addAttribute(ParamsConstants.BDCDYLY_LOWERCASE, bdcdyly);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("wiid", wiid);
        model.addAttribute(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        model.addAttribute(PARAMETER_MULSELECT, mulSelect);
        model.addAttribute("type", "ljz");
        model.addAttribute(PARAMETER_ISNOTWF, isNotWf);
        model.addAttribute("isFilter", isFilter);
        return "query/selectLjz";
    }

    @RequestMapping(value = "/lpb", method = {RequestMethod.GET,RequestMethod.POST})
    public String lpb(Model model, @RequestParam(value = "dcbId", required = false) String dcbId,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "fwmc", required = false) String fwmc,
                                   @RequestParam(value = "proid", required = false) String proid,
                                   @RequestParam(value = "showQl", required = false) String showQl,
                                   @RequestParam(value = "isNotBack", required = false) String isNotBack,
                                   @RequestParam(value = "isNotWf", required = false) String isNotWf,
                                   @RequestParam(value = "mulSelect", required = false) String mulSelect,
                                   @RequestParam(value = "openQlWay", required = false) String openQlWay,
                                   @RequestParam(value = "showFcfht", required = false) String showFcfht,
                                   @RequestParam(value = "dcxc", required = false) String dcxc,
                                   @RequestParam(value = "djbid", required = false) String djbid,
                                   @RequestParam(value = "bdcdyhStr", required = false) String bdcdyhStr,
                                   @RequestParam(value = "isLjz", required = false) String isLjz) {

        Set<String> bdcdyhSet = null;
        if (StringUtils.isNotBlank(bdcdyhStr)) {
            String[] bdcdyhArr = StringUtils.split(bdcdyhStr, ",");
            bdcdyhSet = PublicUtil.arrayToSet(bdcdyhArr);
        }

        List<BdcZdQlzt> bdcZdQlztList = ReadXmlProps.getZdQlztList();
        List<Map> fwmcList = null;
        if (StringUtils.isNotBlank(dcbId))
            fwmcList = djxxMapper.getFwmcListByDcbId(dcbId);
        if (fwmcList == null)
            fwmcList = new ArrayList<Map>();
        if (StringUtils.isBlank(fwmc) && CollectionUtils.isNotEmpty(fwmcList) && fwmcList.get(0).get("FWMC") != null)
            fwmc = fwmcList.get(0).get("FWMC").toString();
        //多选时默认样式修改成active

        String djIds = "";
        String bdcdyhs = "";
        String wiid = "";
        String selectRow = "";
        if (StringUtils.equals(mulSelect, "true")) {
            selectRow = "onclick=\"selectRow('" + proid + "','" + Constants.BDCLX_TDFW + "','','',this,'')\" style=\"cursor:pointer;\" ";
        }
        HashMap map = new HashMap();
        map.put(PARAMETER_FWDCBINDEX, dcbId);
        map.put("notWlcs", "true");
        map.put("orderStr", "  order by wlcs desc,dyh ,sxh");
        List<DjsjFwHs> bdcFwHsList = djsjFwService.getDjsjFwHs(map);
        Map houseBaztMap = new HashMap();
        try {
            if (CollectionUtils.isNotEmpty(bdcFwHsList)) {
                houseBaztMap = djsjFwService.getFwBazt(bdcFwHsList);
            }
        }catch (Exception e){
            logger.info("获取房屋备案状态失败");
        }

        HashMap map2 = new HashMap();
        map2.put(PARAMETER_DCBID, dcbId);
        List<BdcFwHs> bdcFwhsQlztList = bdcHsService.getBdcFwhsQlztList(map2);
        //不动产过度数据权利
        List<Map> gdFwhsList = bdcHsService.getGdFwhsList(map2);

        List<Map> fwDyh = djxxMapper.getDjFwDyh(map2);

        List<String> dyhList = new ArrayList<String>();
        HashMap dyhSxhMap = new HashMap();
        if (CollectionUtils.isNotEmpty(fwDyh)) {
            HashMap sxhMap = new HashMap();
            sxhMap.put(PARAMETER_DCBID, dcbId);
            for (Map map1 : fwDyh) {
                String dyh = null;
                if (map1 != null && map1.get("DYH") != null) {
                    dyh = map1.get("DYH").toString();
                    if (StringUtils.isNotBlank(dyh)) {
                        sxhMap.put("dyh", dyh);
                    }
                }
                Integer sxh = 0;
                if (StringUtils.isBlank(dyh)) {
                    dyh = Constants.DJFW_EMPTY_DYH;
                    sxh = djxxMapper.getDjFwMaxSxhByDyhIsNull(sxhMap);
                } else {
                    sxh = djxxMapper.getDjFwMaxSxh(sxhMap);
                }
                dyhList.add(dyh);
                dyhSxhMap.put(dyh, sxh);
            }
        }

        StringBuilder tableTr = new StringBuilder();
        if (CollectionUtils.isNotEmpty(bdcFwHsList)) {
            String wlcs = CommonUtil.formatEmptyValue(bdcFwHsList.get(0).getWlcs());
            String minwlcs = CommonUtil.formatEmptyValue(bdcFwHsList.get(bdcFwHsList.size() - 1).getWlcs());
            if (StringUtils.isNotBlank(wlcs)) {
                int wlcsInt = Integer.parseInt(wlcs);
                int wlcsIntMIn = Integer.parseInt(minwlcs);
                tableTr.append("<tr>");
                tableTr.append(" <th> 物理层 </th>");
                tableTr.append(" <th> 定义层 </th>");
                for (int i = dyhList.size() - 1; i > -1; i--) {
                    String dyh = dyhList.get(i);
                    if (dyhSxhMap != null && dyhSxhMap.containsKey(dyh) && dyhSxhMap.get(dyh) != null && StringUtils.isNotBlank(dyhSxhMap.get(dyh).toString()))
                        tableTr.append("<th colspan=\"" + dyhSxhMap.get(dyh) + "\"> " + dyh + " </th>");
                    else
                        tableTr.append("<th colspan=\"1\"> " + dyh + " </th>");
                }
                tableTr.append("</tr>");
                for (int i = wlcsInt; i >= wlcsIntMIn; i--) {
                    tableTr.append("<tr>");
                    tableTr.append(" <td   class=\"houseLabel\">" + i + "</td>");

                    List<DjsjFwHs> bdcFwHsList1 = getDjsjFwhsByWlcs(bdcFwHsList, i);
                    if (CollectionUtils.isEmpty(bdcFwHsList1)) {
                        tableTr.append(" <td class=\"houseLabel\"></td>");
                    } else {
                        boolean makeDyc = false;
                        if (!makeDyc) {
                            tableTr.append(" <td " + selectRow + " class=\"houseLabel\">" + bdcFwHsList1.get(0).getDycs() + "</td>");
                            makeDyc = true;
                        }
                    }
                    for (int j = dyhList.size() - 1; j > -1; j--) {
                        if (dyhSxhMap != null && dyhSxhMap.get(dyhList.get(j)) != null) {
                            int sxh = Integer.parseInt(dyhSxhMap.get(dyhList.get(j)).toString());
                            List<DjsjFwHs> bdcFwHsList2 = getDjsjFwhsByDyh(bdcFwHsList1, dyhList.get(j));
                            if (bdcFwHsList2 != null) {
                                //合并户室
                                int hbhsNum = 0;
                                for (DjsjFwHs bdcFwHs : bdcFwHsList2) {
                                    String classActive = "";
                                    if (StringUtils.equals(isLjz, "true")) {
                                        classActive = "active";
                                    }

                                    //先生成定义层
                                    if (StringUtils.isBlank(bdcFwHs.getBdcdyh())) {
                                        bdcFwHs.setBdcdyh("");
                                    }
                                    if (StringUtils.isBlank(bdcFwHs.getDycs())) {
                                        bdcFwHs.setDycs("");
                                    }
                                    if (StringUtils.isBlank(bdcFwHs.getFwHsIndex())){
                                        bdcFwHs.setFwHsIndex("");
                                    }
                                    if (StringUtils.isBlank(bdcFwHs.getFjh())){
                                        bdcFwHs.setFjh("");
                                    }

                                    if (StringUtils.isNotBlank(bdcFwHs.getBdcdyh())
                                            && CollectionUtils.isNotEmpty(bdcdyhSet) && bdcdyhSet.contains(bdcFwHs.getBdcdyh())) {
                                        classActive = "active";
                                    }

                                    String qlztClass = "";
                                    String qlztStyle = "";
                                    String activeStr = "";
                                    if (org.apache.commons.lang.StringUtils.equals(classActive, "active"))
                                        activeStr = "<i class=\"ace-icon fa fa-check\" style=\"margin-right:10%\"></i>";
                                    String qlzt = bdcHsService.getHsQsztByHsId(bdcFwhsQlztList, gdFwhsList, bdcFwHs.getFwHsIndex(), bdcFwHs.getQlzt());

                                    if (StringUtils.equals(dwdm, "320500")) {
                                        //若qlzt是未设权利、已设权利、权利预告、权利抵押（在建工程抵押），则查看户室的发证类型
                                        qlzt = bdcHsService.getFzlxQlzt(bdcFwHs.getBdcdyh(), qlzt);
                                    }

                                    qlztClass = " class=\" " + classActive + "\" ";
                                    if (houseBaztMap.containsKey(bdcFwHs.getFwbm()) && houseBaztMap.get(bdcFwHs.getFwbm()) != null) {
                                        String fwzt = "";
                                        if (StringUtils.equals(String.valueOf(houseBaztMap.get(bdcFwHs.getFwbm())), "1")) {
                                            fwzt = "11";
                                        } else if (StringUtils.equals(String.valueOf(houseBaztMap.get(bdcFwHs.getFwbm())), "2")) {
                                            fwzt = "12";
                                        }
                                        qlztStyle = "style=\" cursor:pointer;background:  " + bdcZdGlService.getQlColorByQlzt(fwzt, bdcZdQlztList) + ";\"";
                                    }
                                    if (org.apache.commons.lang.StringUtils.isNotBlank(qlzt)) {
                                        qlztStyle = "style=\" cursor:pointer;background:  " + bdcZdGlService.getQlColorByQlzt(qlzt, bdcZdQlztList) + ";\"";
                                    }

                                    if (StringUtils.equals(bdcFwHs.getHbfx(), "2") && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(bdcFwHs.getHbhs()))) {
                                        tableTr.append(" <td id=\"" + bdcFwHs.getBdcdyh() + "\" djId=\"" + bdcFwHs.getFwHsIndex() + "\" colspan=\"" + (Integer.parseInt(CommonUtil.formatEmptyValue(bdcFwHs.getHbhs())) + 1) + "\" " + qlztClass + qlztStyle + "onclick=\"chooseBdcdy('" + proid + "','" + Constants.BDCLX_TDFW + "','" + bdcFwHs.getBdcdyh() + "','" + bdcFwHs.getFjh() + "',this,'" + bdcFwHs.getFwHsIndex() + "')\" >" + activeStr + bdcFwHs.getFjh() + "</td>");
                                        hbhsNum += bdcFwHs.getHbhs();
                                    } else {
                                        tableTr.append(" <td id=\"" + bdcFwHs.getBdcdyh() + "\" djId=\"" + bdcFwHs.getFwHsIndex() + "\" " + qlztClass + qlztStyle + "onclick=\"chooseBdcdy('" + proid + "','" + Constants.BDCLX_TDFW + "','" + bdcFwHs.getBdcdyh() + "','" + bdcFwHs.getFjh() + "',this,'" + bdcFwHs.getFwHsIndex() + "')\" >" + activeStr + bdcFwHs.getFjh() + "</td>");
                                    }
                                    if (StringUtils.equals(mulSelect, "true") && StringUtils.isNotBlank(bdcFwHs.getFwHsIndex()) && StringUtils.indexOf(djIds, bdcFwHs.getFwHsIndex()) < 0) {
                                        if (StringUtils.isNotBlank(djIds)) {
                                            djIds = djIds + Constants.SPLIT_STR + bdcFwHs.getFwHsIndex();
                                            bdcdyhs = bdcdyhs + Constants.SPLIT_STR + bdcFwHs.getBdcdyh();
                                        } else {
                                            djIds = bdcFwHs.getFwHsIndex();
                                            bdcdyhs = bdcFwHs.getBdcdyh();
                                        }
                                    }
                                }
                                if (bdcFwHsList2.size() + hbhsNum < sxh) {
                                    for (int m = 0; m < sxh - bdcFwHsList2.size() - hbhsNum; m++) {
                                        tableTr.append(" <td  style=\"cursor:pointer;min-width: 16px;\" onclick=\"chooseBdcdy('" + proid + "','" + Constants.BDCLX_TDFW + "','','')\"></td>");
                                    }
                                }
                            }
                        }
                    }
                    tableTr.append("</tr>");
                }
            }

        }

        //如果多幢和独幢时要取下不动产单元号
        String dzBdcdyh = "";
        HashMap map1 = new HashMap();
        map1.put(PARAMETER_FWDCBINDEX, dcbId);
        List<DjsjFwLjz> bdcFwLjzList = djsjFwService.getDjsjFwLjz(map1);
        if (CollectionUtils.isNotEmpty(bdcFwLjzList)
                && (StringUtils.equals(bdcFwLjzList.get(0).getBdcdyfwlx(), Constants.DJSJ_FWDZ_DM) || StringUtils.equals(bdcFwLjzList.get(0).getBdcdyfwlx(), Constants.DJSJ_FW_DM))) {
            dzBdcdyh = bdcFwLjzList.get(0).getBdcdyh();
        }
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFSCKFSZC_DM)
                || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM))) {
            model.addAttribute("chcekZmd", true);
        } else {
            model.addAttribute("chcekZmd", false);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            wiid = bdcXm.getWiid();
        }
        if (StringUtils.isBlank(showFcfht))
            showFcfht = "";
        model.addAttribute("bdcZdQlztList", bdcZdQlztList);
        model.addAttribute("fwmcList", fwmcList);
        model.addAttribute("tableTr", tableTr);
        model.addAttribute(PARAMETER_DCBID, dcbId);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("wiid", wiid);
        model.addAttribute("fwmc", fwmc);
        model.addAttribute(PARAMETER_ISNOTWF, CommonUtil.formatEmptyValue(isNotWf));
        model.addAttribute("isNotBack", CommonUtil.formatEmptyValue(isNotBack));
        model.addAttribute("showQl", CommonUtil.formatEmptyValue(showQl));
        model.addAttribute(PARAMETER_OPENQLWAY, CommonUtil.formatEmptyValue(openQlWay));
        model.addAttribute(PARAMETER_MULSELECT, mulSelect);
        model.addAttribute("type", type);
        model.addAttribute("bdcdyhs", bdcdyhs);
        model.addAttribute("djIds", djIds);
        model.addAttribute("djbid", djbid);
        model.addAttribute("splitStr", Constants.SPLIT_STR);
        model.addAttribute("dzBdcdyh", dzBdcdyh);
        model.addAttribute("showFcfht", showFcfht);
        model.addAttribute("dcxc", dcxc);
        return "query/lpb";
    }

    @ResponseBody
    @RequestMapping("/getBdcdyFwlx")
    public String getBdcXmPagesJson(@RequestParam(value = "dcbId", required = false) String dcbId) {
        String bdcdyFwlx = Constants.DJSJ_FW_DM;
        if (StringUtils.isNotBlank(dcbId)) {
            HashMap map = new HashMap();
            map.put(PARAMETER_FWDCBINDEX, dcbId);
            List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(map);
            if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
                if (StringUtils.equals(CommonUtil.formatEmptyValue(djsjFwLjzList.get(0).getBdcdyfwlx()), Constants.DJSJ_FWDZ_DM)) {
                    bdcdyFwlx = CommonUtil.formatEmptyValue(djsjFwLjzList.get(0).getFwXmxxIndex());
                } else if (StringUtils.equals(CommonUtil.formatEmptyValue(djsjFwLjzList.get(0).getBdcdyfwlx()), Constants.DJSJ_FW_DM)) {
                    bdcdyFwlx = dcbId;
                }
            }

        }
        return bdcdyFwlx;
    }

    /**
     * zdd 通过 隶属宗地与自然幢号  定位楼盘表
     *
     * @param lszd
     * @param zrzh
     * @return
     */
    @RequestMapping(value = "/redirectLpb", method = RequestMethod.GET)
    public String redirectLpb(@RequestParam(value = "lszd", required = false) String lszd, @RequestParam(value = "zrzh", required = false) String zrzh) {
        String dcbId = "2301";
        HashMap map = new HashMap();
        map.put("lszd", lszd);
        map.put("zrzh", zrzh);
        List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(map);
        if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
            dcbId = djsjFwLjzList.get(0).getFwDcbIndex();
        }
        return "redirect:/lpb/lpb?isNotWf=true&showQl=true&openQlWay=&dcbId=" + dcbId;
    }

    private List<DjsjFwHs> getDjsjFwhsByWlcs(List<DjsjFwHs> djsjFwHsList, Integer wlcs) {
        List<DjsjFwHs> djsjFwHsListTemp = new ArrayList<DjsjFwHs>();
        if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                if (djsjFwHs.getWlcs() == wlcs) {
                    djsjFwHsListTemp.add(djsjFwHs);
                }
            }
        }
        return djsjFwHsListTemp;
    }

    private List<DjsjFwHs> getDjsjFwhsByDyh(List<DjsjFwHs> djsjFwHsList, String dyh) {
        List<DjsjFwHs> djsjFwHsListTemp = new ArrayList<DjsjFwHs>();
        if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                if (dyh.equals(djsjFwHs.getDyh()) || (Constants.DJFW_EMPTY_DYH.equals(dyh) && StringUtils.isBlank(djsjFwHs.getDyh()))) {
                    djsjFwHsListTemp.add(djsjFwHs);
                }
            }
        }
        return djsjFwHsListTemp;
    }

    @RequestMapping(value = "/selectYcLjz", method = RequestMethod.GET)
    public String selectYcLjz(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "mulSelect", required = false) String mulSelect, @RequestParam(value = "isNotWf", required = false) String isNotWf) {
        String zjgcdyFw = "";
        BdcXm xmxx = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, xmxx.getSqlx())) {
            zjgcdyFw = Constants.ZJGCDY_FW;
        } else if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, xmxx.getSqlx())) {
            zjgcdyFw = Constants.ZJGCDY_TD;
        } else {
            zjgcdyFw = Constants.NOT_ZJGCDY;
        }
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String yqllxdm = "";
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        if (StringUtils.isNotBlank(xmxx.getSqlx()) && StringUtils.isNotBlank(xmxx.getQllx())) {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put(ParamsConstants.SQLXDM_LOWERCASE, xmxx.getSqlx());
            queryMap.put(ParamsConstants.QLLXDM_LOWERCASE, xmxx.getQllx());
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }

        if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcSqlxQllxRel.getYqllxdm()))
                yqllxdm = bdcSqlxQllxRel.getYqllxdm();
            if (bdcSqlxQllxRel.getBdcdyly() != null)
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm()))
                zdtzm = bdcSqlxQllxRel.getZdtzm();
        }
        if (StringUtils.isNotBlank(xmxx.getSqlx())) {
            model.addAttribute(ParamsConstants.SQLXDM_LOWERCASE, xmxx.getSqlx());
        } else {
            model.addAttribute(ParamsConstants.SQLXDM_LOWERCASE, "");
        }
        model.addAttribute(ParamsConstants.YQLLXDM_LOWERCASE, yqllxdm);
        model.addAttribute(ParamsConstants.BDCDYLY_LOWERCASE, bdcdyly);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        model.addAttribute(PARAMETER_MULSELECT, mulSelect);
        model.addAttribute("type", "ljz");
        model.addAttribute(PARAMETER_ISNOTWF, isNotWf);
        model.addAttribute("zjgcdyFw", zjgcdyFw);
        return "query/selectYcLjz";
    }

    @RequestMapping(value = "/ycLpb", method = RequestMethod.POST)
    public String ycLpb(Model model, @RequestParam(value = "bdcdyhStr", required = false) String bdcdyhStr,
                                     @RequestParam(value = "isLjz", required = false) String isLjz,
                                     @RequestParam(value = "lszd", required = false) String lszd,
                                     @RequestParam(value = "dcbId", required = false) String dcbId,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "fwmc", required = false) String fwmc,
                                     @RequestParam(value = "proid", required = false) String proid,
                                     @RequestParam(value = "showQl", required = false) String showQl,
                                     @RequestParam(value = "isNotBack", required = false) String isNotBack,
                                     @RequestParam(value = "isNotWf", required = false) String isNotWf,
                                     @RequestParam(value = "mulSelect", required = false) String mulSelect,
                                     @RequestParam(value = "openQlWay", required = false) String openQlWay,
                                     @RequestParam(value = "showFcfht", required = false) String showFcfht,
                                     @RequestParam(value = "dcxc", required = false) String dcxc,
                                     @RequestParam(value = "djbid", required = false) String djbid,
                                     @RequestParam(value = "sqlxdm", required = false) String sqlxdm) {

        Set<String> bdcdyhSet = null;
        if (StringUtils.isNotBlank(bdcdyhStr)) {
            String[] bdcdyhArr = StringUtils.split(bdcdyhStr, ",");
            bdcdyhSet = PublicUtil.arrayToSet(bdcdyhArr);
        }
        List<BdcZdQlzt> bdcZdQlztList = ReadXmlProps.getZdQlztList();
        List<Map> fwmcList = null;
        if (StringUtils.isNotBlank(dcbId))
            fwmcList = djxxMapper.getFwmcListByDcbId(dcbId);
        if (CollectionUtils.isEmpty(fwmcList))
            fwmcList = new ArrayList<Map>();
        if (StringUtils.isBlank(fwmc) && CollectionUtils.isNotEmpty(fwmcList) && fwmcList.get(0).get("FWMC") != null)
            fwmc = fwmcList.get(0).get("FWMC").toString();

        String djIds = "";
        String bdcdyhs = "";
        String selectRow = "";
        if (StringUtils.equals(mulSelect, "true")) {
            selectRow = "onclick=\"selectRow('" + proid + "','" + Constants.BDCLX_TDFW + "','','',this,'')\" style=\"cursor:pointer;\" ";
        }
        HashMap map = new HashMap();
        map.put(PARAMETER_FWDCBINDEX, dcbId);
        map.put("orderStr", "  order by wlcs desc,dyh ,fjh");
        List<DjsjFwHs> bdcFwHsList = djsjFwService.getDjsjFwYcHs(map);
        Map houseBaztMap = new HashMap();
        if (CollectionUtils.isNotEmpty(bdcFwHsList)) {
            houseBaztMap = djsjFwService.getFwBazt(bdcFwHsList);
        }

        HashMap map2 = new HashMap();
        map2.put(PARAMETER_DCBID, dcbId);
        /**
         * bianwen
         * 取预测户室权利
         */
        List<BdcFwHs> bdcFwhsQlztList = bdcHsService.getBdcYcFwhsQlztList(map2);
        //不动产过度数据权利
        List<Map> gdFwhsList = bdcHsService.getGdYcFwhsList(map2);

        List<Map> fwDyh = djxxMapper.getDjYcFwDyh(map2);

        List<String> dyhList = new ArrayList<String>();
        HashMap dyhSxhMap = new HashMap();
        if (CollectionUtils.isNotEmpty(fwDyh)) {
            HashMap sxhMap = new HashMap();
            sxhMap.put(PARAMETER_DCBID, dcbId);
            for (Map map1 : fwDyh) {
                String dyh = null;
                if (map1 != null && map1.get("DYH") != null) {
                    dyh = map1.get("DYH").toString();
                    if (StringUtils.isNotBlank(dyh)) {
                        sxhMap.put("dyh", dyh);
                    }
                }
                if (StringUtils.isBlank(dyh))
                    dyh = Constants.DJFW_EMPTY_DYH;
                Integer sxh = djxxMapper.getYcDjFwMaxSxh(sxhMap);
                dyhList.add(dyh);
                dyhSxhMap.put(dyh, sxh);
            }
        }

        StringBuilder tableTr = new StringBuilder();
        if (CollectionUtils.isNotEmpty(bdcFwHsList)) {
            String wlcs = CommonUtil.formatEmptyValue(bdcFwHsList.get(0).getWlcs());
            String minwlcs = CommonUtil.formatEmptyValue(bdcFwHsList.get(bdcFwHsList.size() - 1).getWlcs());
            if (StringUtils.isNotBlank(wlcs)) {
                int wlcsInt = Integer.parseInt(wlcs);
                int wlcsIntMin = Integer.parseInt(minwlcs);
                tableTr.append("<tr>");
                tableTr.append(" <th> 物理层 </th>");
                tableTr.append(" <th> 定义层 </th>");
                for (int i = dyhList.size() - 1; i > -1; i--) {
                    String dyh = dyhList.get(i);
                    if (dyhSxhMap != null && dyhSxhMap.containsKey(dyh) && dyhSxhMap.get(dyh) != null && StringUtils.isNotBlank(dyhSxhMap.get(dyh).toString()))
                        tableTr.append("<th colspan=\"" + dyhSxhMap.get(dyh) + "\"> " + dyh + " </th>");
                    else
                        tableTr.append("<th colspan=\"1\"> " + dyh + " </th>");
                }
                tableTr.append("</tr>");
                for (int i = wlcsInt; i >= wlcsIntMin; i--) {
                    tableTr.append("<tr>");
                    tableTr.append(" <td   class=\"houseLabel\">" + i + "</td>");
                    List<DjsjFwHs> bdcFwHsList1 = getDjsjFwhsByWlcs(bdcFwHsList, i);
                    if (CollectionUtils.isEmpty(bdcFwHsList1)) {
                        tableTr.append(" <td class=\"houseLabel\"></td>");
                    } else {
                        boolean makeDyc = false;
                        if (!makeDyc) {
                            tableTr.append(" <td " + selectRow + " class=\"houseLabel\">" + bdcFwHsList1.get(0).getDycs() + "</td>");
                            makeDyc = true;
                        }
                    }
                    for (int j = dyhList.size() - 1; j > -1; j--) {
                        if (dyhSxhMap != null && dyhSxhMap.get(dyhList.get(j)) != null) {
                            int sxh = Integer.parseInt(dyhSxhMap.get(dyhList.get(j)).toString());
                            List<DjsjFwHs> bdcFwHsList2 = getDjsjFwhsByDyh(bdcFwHsList1, dyhList.get(j));
                            if (bdcFwHsList2 != null) {
                                //合并户室
                                int hbhsNum = 0;
                                for (DjsjFwHs bdcFwHs : bdcFwHsList2) {

                                    String classActive = "";
                                    if (StringUtils.equals(isLjz, "true")) {
                                        classActive = "active";
                                    }

                                    //先生成定义层
                                    if (StringUtils.isBlank(bdcFwHs.getBdcdyh()))
                                        bdcFwHs.setBdcdyh("");
                                    if (StringUtils.isBlank(bdcFwHs.getDycs()))
                                        bdcFwHs.setDycs("");
                                    if (StringUtils.isBlank(bdcFwHs.getFwHsIndex()))
                                        bdcFwHs.setFwHsIndex("");
                                    if (StringUtils.isBlank(bdcFwHs.getFjh()))
                                        bdcFwHs.setFjh("");

                                    if (StringUtils.isNotBlank(bdcFwHs.getBdcdyh())
                                            && CollectionUtils.isNotEmpty(bdcdyhSet) && bdcdyhSet.contains(bdcFwHs.getBdcdyh())) {
                                        classActive = "active";
                                    }
                                    String qlztClass = "";
                                    String qlztStyle = "";
                                    String activeStr = "";
                                    if (org.apache.commons.lang.StringUtils.equals(classActive, "active"))
                                        activeStr = "<i class=\"ace-icon fa fa-check\" style=\"margin-right:10%\"></i>";
                                    String qlzt = bdcHsService.getHsQsztByHsId(bdcFwhsQlztList, gdFwhsList, bdcFwHs.getFwHsIndex(), bdcFwHs.getQlzt());

                                    qlztClass = " class=\" " + classActive + "\" ";
                                    if (houseBaztMap.containsKey(bdcFwHs.getFwbm()) && houseBaztMap.get(bdcFwHs.getFwbm()) != null) {
                                        String fwzt = "";
                                        if (StringUtils.equals(String.valueOf(houseBaztMap.get(bdcFwHs.getFwbm())), "1")) {
                                            fwzt = "11";
                                        } else if (StringUtils.equals(String.valueOf(houseBaztMap.get(bdcFwHs.getFwbm())), "2")) {
                                            fwzt = "12";
                                        }
                                        qlztStyle = "style=\" cursor:pointer;background:  " + bdcZdGlService.getQlColorByQlzt(fwzt, bdcZdQlztList) + ";\"";
                                    }
                                    if (org.apache.commons.lang.StringUtils.isNotBlank(qlzt)) {
                                        qlztStyle = "style=\" cursor:pointer;background:  " + bdcZdGlService.getQlColorByQlzt(qlzt, bdcZdQlztList) + ";\"";
                                    }

                                    if (StringUtils.equals(bdcFwHs.getHbfx(), "2") && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(bdcFwHs.getHbhs()))) {
                                        tableTr.append(" <td id=\"" + bdcFwHs.getBdcdyh() + "\" djId=\"" + bdcFwHs.getFwHsIndex() + "\" colspan=\"" + (Integer.parseInt(CommonUtil.formatEmptyValue(bdcFwHs.getHbhs())) + 1) + "\" " + qlztClass + qlztStyle + "onclick=\"chooseBdcdy('" + proid + "','" + Constants.BDCLX_TDFW + "','" + bdcFwHs.getBdcdyh() + "','" + bdcFwHs.getFjh() + "',this,'" + bdcFwHs.getFwHsIndex() + "')\" >" + activeStr + bdcFwHs.getFjh() + "</td>");
                                        hbhsNum += bdcFwHs.getHbhs();
                                    } else {
                                        tableTr.append(" <td id=\"" + bdcFwHs.getBdcdyh() + "\" djId=\"" + bdcFwHs.getFwHsIndex() + "\" " + qlztClass + qlztStyle + "onclick=\"chooseBdcdy('" + proid + "','" + Constants.BDCLX_TDFW + "','" + bdcFwHs.getBdcdyh() + "','" + bdcFwHs.getFjh() + "',this,'" + bdcFwHs.getFwHsIndex() + "')\" >" + activeStr + bdcFwHs.getFjh() + "</td>");
                                    }
                                    if (StringUtils.equals(mulSelect, "true") && StringUtils.isNotBlank(bdcFwHs.getFwHsIndex()) && StringUtils.indexOf(djIds, bdcFwHs.getFwHsIndex()) < 0) {
                                        if (StringUtils.isNotBlank(djIds)) {
                                            djIds = djIds + Constants.SPLIT_STR + bdcFwHs.getFwHsIndex();
                                            bdcdyhs = bdcdyhs + Constants.SPLIT_STR + bdcFwHs.getBdcdyh();
                                        } else {
                                            djIds = bdcFwHs.getFwHsIndex();
                                            bdcdyhs = bdcFwHs.getBdcdyh();
                                        }
                                    }
                                }
                                if (bdcFwHsList2.size() + hbhsNum < sxh) {
                                    for (int m = 0; m < sxh - bdcFwHsList2.size() - hbhsNum; m++) {
                                        tableTr.append(" <td  style=\"cursor:pointer;min-width: 16px;\" onclick=\"chooseBdcdy('" + proid + "','" + Constants.BDCLX_TDFW + "','','')\"></td>");
                                    }
                                }
                            }
                        }
                    }
                    tableTr.append("</tr>");
                }
            }

        }

        //如果多幢和独幢时要取下不动产单元号
        String dzBdcdyh = "";
        HashMap map1 = new HashMap();
        map1.put(PARAMETER_FWDCBINDEX, dcbId);
        List<DjsjFwLjz> bdcFwLjzList = djsjFwService.getDjsjFwLjz(map1);
        if (CollectionUtils.isNotEmpty(bdcFwLjzList)
                && (StringUtils.equals(bdcFwLjzList.get(0).getBdcdyfwlx(), Constants.DJSJ_FWDZ_DM) || StringUtils.equals(bdcFwLjzList.get(0).getBdcdyfwlx(), Constants.DJSJ_FW_DM))) {
            dzBdcdyh = bdcFwLjzList.get(0).getBdcdyh();
        }
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM))) {
            model.addAttribute("chcekIfRegistered", true);
        } else {
            model.addAttribute("chcekIfRegistered", false);
        }
        if (StringUtils.isBlank(showFcfht))
            showFcfht = "";
        model.addAttribute("bdcZdQlztList", bdcZdQlztList);
        model.addAttribute("fwmcList", fwmcList);
        model.addAttribute("tableTr", tableTr);
        model.addAttribute(PARAMETER_DCBID, dcbId);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("fwmc", fwmc);
        model.addAttribute(PARAMETER_ISNOTWF, CommonUtil.formatEmptyValue(isNotWf));
        model.addAttribute("isNotBack", CommonUtil.formatEmptyValue(isNotBack));
        model.addAttribute("showQl", CommonUtil.formatEmptyValue(showQl));
        model.addAttribute(PARAMETER_OPENQLWAY, CommonUtil.formatEmptyValue(openQlWay));
        model.addAttribute(PARAMETER_MULSELECT, mulSelect);
        model.addAttribute("type", "yclpb");
        model.addAttribute("djbid", djbid);
        model.addAttribute("splitStr", Constants.SPLIT_STR);
        model.addAttribute("dzBdcdyh", dzBdcdyh);
        model.addAttribute("showFcfht", showFcfht);
        model.addAttribute("dcxc", dcxc);
        model.addAttribute(ParamsConstants.SQLXDM_LOWERCASE, sqlxdm);
        model.addAttribute("zdzhh", lszd);
        return "query/ycLpb";
    }

    @ResponseBody
    @RequestMapping("/getYcLpbPagesJson")
    public Object getYcLpbPagesJson(Pageable pageable, String fwmc, String zdh, String zl, String dcxc, String zdtzm) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(fwmc)) {
                map.put("fwmc", StringUtils.deleteWhitespace(fwmc));
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", StringUtils.deleteWhitespace(zl));
            }
            if (StringUtils.isNotBlank(zdh)) {
                map.put("zdh", StringUtils.deleteWhitespace(zdh));
            }
        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm) && StringUtils.isNotBlank(zdtzm)) {
            //无论是简单查询和高级查询都要过来特征码
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        //过滤只查出yclpb的数据
        map.put("lpbtype", "yclpb");
        return repository.selectPaging("getLpbListByPage", map, pageable);
    }

    /**
     * @param lszd
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据隶属宗地获取不动产单元
     */
    @ResponseBody
    @RequestMapping("/getBdcdyhByLszd")
    public List<String> getBdcdyhByLszd(String lszd) {
        List<String> list = new ArrayList<String>();
        Example example = new Example(DjsjFwHs.class);
        example.createCriteria().andEqualTo("lszd", lszd);
        List<DjsjFwHs> djsjFwHsList = entityMapper.selectByExample(DjsjFwHs.class, example);
        if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                if (StringUtils.isNotBlank(djsjFwHs.getBdcdyh()))
                    list.add(djsjFwHs.getBdcdyh());
            }
        }
        return list;
    }

    /**
     * 验证是否设权利的、只办理过在建工程或预告
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkIfRegistered")
    public boolean checkIfRegistered(String bdcdyh) {
        if (StringUtils.isBlank(bdcdyh)) {
            return false;
        }
        try {
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
            Map<String, Object> parameter = new HashMap<String, Object>();
            List<BdcYg> bdcYgList = null;
            int numOfZjjzwxx = 0;
            List<BdcFdcq> bdcFdcqList = null;
            if (StringUtils.isNotBlank(bdcdyid)) {
                parameter.put("bdcdyid", bdcdyid);
                bdcFdcqList = bdcFdcqService.getBdcFdcq(parameter);
            }
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                bdcYgList = bdcYgService.getBdcYgList(bdcdyh, "");
                if (CollectionUtils.isEmpty(bdcYgList)) {
                    numOfZjjzwxx = bdcZjjzwxxService.getDyBdcZjjzwxxByBdcdyh(bdcdyh);
                }
            }
            return ((numOfZjjzwxx > 0)
                    || CollectionUtils.isEmpty(bdcFdcqList)
                    || CollectionUtils.isNotEmpty(bdcYgList)) ? true : false;

        } catch (Exception e) {
            logger.error("lpb/ycLpb/checkIfRegistered", e);
        }
        return false;
    }

    /**
     * 验证是否 发了信息表
     *
     * @param bdcdyh
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkIfZmd")
    public boolean checkIfZmd(String bdcdyh) {
        if (StringUtils.isBlank(bdcdyh)) {
            return false;
        }
        try {
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
            Map<String, Object> parameter = new HashMap<String, Object>();
            String fzlx = null;
            List<BdcFdcq> bdcFdcqList = null;
            if (StringUtils.isNotBlank(bdcdyid)) {
                parameter.put("bdcdyid", bdcdyid);
                parameter.put("qszt", Constants.QLLX_QSZT_XS);
                bdcFdcqList = bdcFdcqService.getBdcFdcq(parameter);
            }
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                fzlx = bdcFdcq.getFzlx();
            }
            return StringUtils.equals(fzlx, Constants.FZLX_FZM) ? true : false;
        } catch (Exception e) {
            logger.error("lpb/ycLpb/checkIfRegistered", e);
        }
        return false;
    }


    @RequestMapping("/selectMulLjzGwc")
    public String selectMulLjzGwc() {
        return "query/selectLjzGwc";
    }
}
