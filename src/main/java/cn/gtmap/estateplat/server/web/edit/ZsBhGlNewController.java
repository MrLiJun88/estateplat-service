package cn.gtmap.estateplat.server.web.edit;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repository;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfUserVo;
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
 * 证书编号管理
 * Created by liaoxiang on 2017/6/14.
 */
@Controller
@RequestMapping("/zsBhGlNew")
public class ZsBhGlNewController extends BaseController {
    @Autowired
    Repository repository;
    @Autowired
    private DwxxService dwxxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsbhService bdcZsbhService;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;

    protected static final String ZSBH_TIP_BZQTJ = "证书编号不足请添加";
    protected static final String CONFIGURATION_PARAMETER_FILTERZSBH_XZDM = "bdczsbh.filterZsbh.xzdm";
    protected static final String PARAMETER_HHSEARCH = "hhSearch";
    protected static final String PARAMETER_GETBDCZSBHLISTBYPAGE = "getBdcZsBhListByPage";
    protected static final String PARAMETER_ZSBHID = "zsbhid";
    protected static final String PARAMETER_LQRID = "lqrid";
    protected static final String ZSBHNOTLIKELIST = "zsbhNotLikeList";
    protected static final String ZSBHNOTEQUALLIST = "zsbhNotEqualList";

    @RequestMapping(value = "")
    public String zsBhGlNew(Model model) {
        //加载部门list
        List<PfOrganVo> organVoList = PlatformUtil.getOrganList();
        model.addAttribute("organVoList", organVoList);
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        List<Dwxx> dwxxList = dwxxService.getDwxxList(userDwdm, 6);
        //证书编号分配到各乡镇所,需要放开位数，以取到乡镇的行政代码
        String bdczsbhFilterZsbhXzdm = AppConfig.getProperty(CONFIGURATION_PARAMETER_FILTERZSBH_XZDM);
        if (StringUtils.equals(bdczsbhFilterZsbhXzdm, "true")) {
            dwxxList = dwxxService.getDwxxList(userDwdm, 9);
        }
        if (dwxxList == null) {
            dwxxList = new ArrayList<Dwxx>();
        }
        model.addAttribute("dwxxList", dwxxList);
        return "/sjgl/zsBhGlNew";
    }

    @ResponseBody
    @RequestMapping("/getBdcZsBhListByPage")
    public Object getBdcZsBhListByPage(Pageable pageable, String hhSearch, String qsbh, String jsbh, String jsrq, String qsrq, String dwdm, BdcZsbh bdcZsbh, String organId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(hhSearch)) {
            if (hhSearch.contains("证书")) {
                map.put(PARAMETER_HHSEARCH, "zs");
            }
            else if (hhSearch.contains("证明书")) {
                map.put(PARAMETER_HHSEARCH, "zms");
            }
            else {
                map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
            }
        } else {
            if (StringUtils.isNotBlank(jsrq)) {
                map.put("jsrq", CalendarUtil.formatDate(jsrq));
            }
            if (StringUtils.isNotBlank(qsrq)) {
                map.put("qsrq", CalendarUtil.formatDate(qsrq));
            }
            map.put("qsbh", qsbh);
            if (StringUtils.isNotBlank(dwdm)) {
                while (dwdm.endsWith("0")) {
                    dwdm = dwdm.substring(0, dwdm.length() - 1);
                }
            }
            map.put("dwdm", dwdm);
            map.put("jsbh", jsbh);
            if (bdcZsbh != null) {
                map.put("cjr", bdcZsbh.getCjr());
                map.put("lqr", bdcZsbh.getLqr());
                map.put("zslx", bdcZsbh.getZslx());
                if (StringUtils.isNotBlank(bdcZsbh.getSyqk())) {
                    map.put("syqk", bdcZsbh.getSyqk().split(","));
                }
            }
        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm)) {
            map.put("xzqdm", userDwdm);
        }

        //根据页面选择的部门id，查询平台库中该部门下的所有用户
        List<PfUserVo> userList = PlatformUtil.getUsersByOrganId(organId);
        StringBuilder userNameStr = new StringBuilder();
        if (CollectionUtils.isNotEmpty(userList)) {
            for (int i = 0; i < userList.size(); i++) {
                PfUserVo userVo = userList.get(i);
                userNameStr.append(userVo.getUserName()).append(",");
            }
            if (StringUtils.isNotBlank(userNameStr) && userNameStr.length() > 0) {
                String tmpUserNameStr = userNameStr.toString().substring(0, userNameStr.length() - 1);
                String[] userNameArr = tmpUserNameStr.split(",");
                map.put("cjrList", userNameArr);
            }
        }
        List<String> zsbhNotEqualList = Arrays.asList(ParamsConstants.ZSBH_NOTEQUAL_LIST);
        List<String> zsbhNotLikeList = Arrays.asList(ParamsConstants.ZSBH_NOTLIKE_LIST);
        map.put(ZSBHNOTEQUALLIST,zsbhNotEqualList);
        map.put(ZSBHNOTLIKELIST,zsbhNotLikeList);
        return repository.selectPaging(PARAMETER_GETBDCZSBHLISTBYPAGE, map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcZsBhZfListByPage")
    public Object getBdcZsBhZfListByPage(Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm)) {
            map.put("xzqdm", userDwdm);
        }
        //作废状态证书编号
        map.put("syqk", new String[]{Constants.BDCZSBH_SYQK_ZF});
        List<String> zsbhNotEqualList = Arrays.asList(ParamsConstants.ZSBH_NOTEQUAL_LIST);
        List<String> zsbhNotLikeList = Arrays.asList(ParamsConstants.ZSBH_NOTLIKE_LIST);
        map.put(ZSBHNOTEQUALLIST,zsbhNotEqualList);
        map.put(ZSBHNOTLIKELIST,zsbhNotLikeList);
        return repository.selectPaging(PARAMETER_GETBDCZSBHLISTBYPAGE, map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcZsBhLqqjListByPage")
    public Object getBdcZsBhLqqjListByPage(Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }
        return repository.selectPaging("getBdcZsBhLqqjListByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/saveZsBh")
    public String saveZsBh(Model model, String zslx, String qsbh, String jsbh, String dwdm, String sccs, String xh) {
        if (StringUtils.isBlank(dwdm)) {
            dwdm = super.getWhereXzqdm();
        } else {
            //证书编号分配到各乡镇所,需要放开位数，以取到乡镇的行政代码
            String bdczsbhFilterZsbhXzdm = AppConfig.getProperty(CONFIGURATION_PARAMETER_FILTERZSBH_XZDM);
            if (!StringUtils.equals(bdczsbhFilterZsbhXzdm, "true")) {
                while (dwdm.endsWith("0")) {
                    dwdm = dwdm.substring(0, dwdm.length() - 1);
                }
            }
        }
        HashMap map = new HashMap();
        map.put("qsbh", qsbh);
        map.put("jsbh", jsbh);
        map.put("zslx", zslx);
        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
        BdcZsbh bdcZsbh = new BdcZsbh();
        bdcZsbh.setCjr(super.getUserName());
        bdcZsbh.setDwdm(dwdm);
        bdcZsbh.setZslx(zslx);
        bdcZsbh.setCjsj(new Date());
        bdcZsbh.setSccs(sccs);
        Calendar a = Calendar.getInstance();
        bdcZsbh.setNf(a.get(Calendar.YEAR) + "");
        bdcZsbh.setXh(xh);
        bdcZsbh.setXhksbh(qsbh);
        bdcZsbh.setXhjsbh(jsbh);
        bdcZsbh.setSyqk("6");//未领用
        return bdcZsbhService.checkAndSaveZsh(bdcZsbhList, bdcZsbh, qsbh, jsbh);
    }

    @ResponseBody
    @RequestMapping("/lqZsBh")
    public String lqZsBh(Model model, String zslx, String qsxh, String jsxh, String dwdm, String lqdw, String lqrid) {
        String result = "";
        //验证是否存在对应箱号
        int count = 0;
        int xh = 0;
        if (StringUtils.isNotBlank(qsxh) && StringUtils.isNotBlank(jsxh)) {
            for (int i = 0; i < (Integer.parseInt(jsxh) - Integer.parseInt(qsxh) + 1); i++) {
                xh = (Integer.parseInt(qsxh) + i);
                count = bdcZsbhService.getCountBdcZsBhByXh(xh);
                if (count == 0) {
                    return "选择的箱号不存在";
                }
            }
        }

        if (StringUtils.isBlank(dwdm)) {
            dwdm = super.getWhereXzqdm();
        } else {
            //证书编号分配到各乡镇所,需要放开位数，以取到乡镇的行政代码
            String bdczsbhFilterZsbhXzdm = AppConfig.getProperty(CONFIGURATION_PARAMETER_FILTERZSBH_XZDM);
            if (!StringUtils.equals(bdczsbhFilterZsbhXzdm, "true")) {
                while (dwdm.endsWith("0")) {
                    dwdm = dwdm.substring(0, dwdm.length() - 1);
                }
            }
        }
        HashMap map = new HashMap();
        map.put("qsxh", qsxh);
        map.put("jsxh", jsxh);
        map.put("zslx", zslx);
        map.put("dwdm", dwdm);
        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByQzBh(map);
        map.put("nosyqk", "6");//查看是否存在已领取
        List<BdcZsbh> bdcZsbhList1 = bdcZsbhService.getBdcZsBhListByQzBh(map);
        if (CollectionUtils.isNotEmpty(bdcZsbhList1)) {
            return "选择的证书编号存在已被领用";
        } else if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
            for (BdcZsbh bdcZsbh : bdcZsbhList) {
                bdcZsbh.setLqrid(lqrid);
                if (StringUtils.isNotBlank(lqrid)) {
                    bdcZsbh.setLqr(getUserNameById(lqrid));
                }
                bdcZsbh.setLqsj(new Date());
                bdcZsbh.setLqdw(lqdw);
                bdcZsbh.setSyqk("0");
                entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
            }
            result = ParamsConstants.SUCCESS_LOWERCASE;
        } else {
            return "选择的箱号区间找不到证书编号";
        }

        if(StringUtils.isBlank(result)) {
            result = "失败";
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/zfZsBh")
    public String zfZsBh(Model model, String zsbhid, String bfyy, String zszt) {
        String result = "";
        HashMap map = new HashMap();
        map.put(PARAMETER_ZSBHID, zsbhid);
        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
        if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
            BdcZsbh bdcZsbh = bdcZsbhList.get(0);
            bdcZsbh.setSyqk(zszt);
            if (StringUtils.equals(zszt, Constants.BDCZSBH_SYQK_ZF)) {
                bdcZsbh.setBfyy(bfyy);
                bdcZsbh.setZfr(super.getUserName());
                bdcZsbh.setZfrid(super.getUserId());
                bdcZsbh.setZfsj(new Date());
                entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
            } else if (StringUtils.equals(zszt, Constants.BDCZSBH_SYQK_YS)) {
                bdcZsbh.setYsyy(bfyy);
                bdcZsbh.setYsczr(super.getUserName());
                bdcZsbh.setYsczrid(super.getUserId());
                bdcZsbh.setYssj(new Date());
                entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
            }
            result = "操作成功";
        }
        if(StringUtils.isBlank(result)) {
            result = "失败";
        }
        return result;
    }

    //销毁作废的证书编号
    @ResponseBody
    @RequestMapping("/xhZsBh")
    public String xhZsBh(Model model, String zsbhid, String xhjzr, String zszt) {
        String result = "失败";
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(zsbhid)) {
            map.put(PARAMETER_ZSBHID, zsbhid);
            List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
            if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
                BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                if (bdcZsbh.getSyqk().equals(Constants.BDCZSBH_SYQK_ZF)) {
                    bdcZsbh.setSyqk(zszt);
                    bdcZsbh.setXhr(super.getUserName());
                    bdcZsbh.setXhrid(super.getUserId());
                    bdcZsbh.setXhsj(new Date());
                    bdcZsbh.setXhjzr(xhjzr);
                    entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());

                    result = "操作成功";
                } else {
                    result = "请选择作废的证书编号进行销毁";
                }
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/getZsbH")
    public String getZsbH(Model model, String zslx, String zsid, String dwdm) {
        String result = "失败";
        String zsbh = "";
        //证书编号分配到各乡镇所,需要通过dwdm区分
        String bdczsbhFilterZsbhXzdm = AppConfig.getProperty(CONFIGURATION_PARAMETER_FILTERZSBH_XZDM);
        //获取当前登录人所在部门
        List<PfOrganVo> pfOrganVoList = super.getOrganName();
        String organName = pfOrganVoList.get(0).getOrganName();
        //按人员分配
        String isry = AppConfig.getProperty("getZsbhByRy");
        if (StringUtils.isNotBlank(bdczsbhFilterZsbhXzdm) && Boolean.parseBoolean(bdczsbhFilterZsbhXzdm)) {
            HashMap hashMap = new HashMap();
            hashMap.put("zslx", zslx);
            hashMap.put("dwdm", dwdm);
            //按人员分配
            if (StringUtils.equals(isry, "true"))
                hashMap.put(PARAMETER_LQRID, super.getUserId());
            hashMap.put("lqdw", organName);
            zsbh = bdcZsbhService.getZsbhByDwdm(hashMap);
        } else if (StringUtils.equals(isry, "true")) {
            HashMap hashMap = new HashMap();
            hashMap.put("zslx", zslx);
            hashMap.put(PARAMETER_LQRID, super.getUserId());
            hashMap.put("lqdw", organName);
            zsbh = bdcZsbhService.getZsbhByDwdm(hashMap);
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("zslx", zslx);
            hashMap.put("lqdw", organName);
            zsbh = bdcZsbhService.getZsbhByDwdm(hashMap);
        }
        if (StringUtils.isNotBlank(zslx)) {
            List<BdcZsbh> bdcZsbhList = new ArrayList<BdcZsbh>();
            if (StringUtils.isNotBlank(zsbh)) {
                HashMap map = new HashMap();
                map.put("zsbh", zsbh);
                map.put("zslx", zslx);
                if (StringUtils.equals(isry, "true")) {
                    map.put("dwdm", dwdm);
                }
                bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
            }
            BdcZs bdcZs = bdcZsService.queryBdcZsByZsid(zsid);
            if (CollectionUtils.isNotEmpty(bdcZsbhList) && bdcZs != null) {
                BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                bdcZsbh.setSyqk("3");
                bdcZsbh.setSyr(super.getUserName());
                bdcZsbh.setSyrid(super.getUserId());
                bdcZsbh.setSysj(new Date());
                bdcZsbh.setZsid(zsid);
                entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                String proid = bdcXmZsRelService.getProidByZsid(zsid);
                if (StringUtils.isNotBlank(proid)) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                    if (qllxVo != null) {
                        bdcZs.setFzrq(qllxVo.getDjsj());
                    }
                }
                bdcZs.setBh(bdcZsbh.getZsbh());
                entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                HashMap maphy = new HashMap();
                maphy.put("zsid", zsid);
                maphy.put("zslx", zslx);
                List<BdcZsbh> bdcZsbhListhy = bdcZsbhService.getBdcZsBhListByBhfw(maphy);
                if (CollectionUtils.isNotEmpty(bdcZsbhListhy)) {
                    for (BdcZsbh bdcZsbhhy : bdcZsbhListhy) {
                        if ((!StringUtils.equals(bdcZsbhhy.getZsbh(), bdcZsbh.getZsbh())) && (StringUtils.equals(bdcZsbhhy.getSyqk(), Constants.BDCZSBH_SYQK_LS))) {
                            bdcZsbhhy.setZsid("");
                            bdcZsbhhy.setSyqk(Constants.BDCZSBH_SYQK_WSY);
                            bdcZsbhhy.setSyr("");
                            bdcZsbhhy.setSyrid("");
                            bdcZsbhhy.setSysj(null);
                            bdcZsbhhy.setBfyy("");
                            entityMapper.saveOrUpdate(bdcZsbhhy, bdcZsbhhy.getZsbhid());
                        }
                    }
                }
            }
            result = zsbh;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/organizeZsbh")
    public String organizeZsbh(Model model, String zsbhid) {
        String json = "{";
        HashMap map = new HashMap();
        map.put(PARAMETER_ZSBHID, zsbhid);
        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
        if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
            BdcZsbh bdcZsbh = bdcZsbhList.get(0);
            json += "\"zsbhid\":" + "\"" + bdcZsbh.getZsbhid() + "\",\"zsbh\":" + "\"" + bdcZsbh.getZsbh() + "\",\"bfyy\":" + "\"" + bdcZsbh.getBfyy() + "\",\"ysyy\":" + "\"" + bdcZsbh.getYsyy() + "\",\"xhjzr\":" + "\"" + bdcZsbh.getXhjzr() + "\",\"zszt\":" + "\"" + bdcZsbh.getSyqk() + "\"";
            if (StringUtils.isNotBlank(bdcZsbh.getZsid())) {
                BdcZs bdcZs = entityMapper.selectByPrimaryKey(BdcZs.class, bdcZsbh.getZsid());
                bdcZs.setFzrq(new Date());
                entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                json += ",\"bdcqzh\":" + "\"" + bdcZs.getBdcqzh() + "\"";
            }
            json += "}";
            return json;
        }
        return null;
    }

    //批量导入证书编号
    @ResponseBody
    @RequestMapping("/getZsbhByPl")
    public String getZsbhByPl(Model model, String wiid) {
        String zslx = "";
        String result = "失败";
        List<BdcZs> bdcZsList = bdcZsService.getPlZsByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            for (int i = 0; i < bdcZsList.size(); i++) {
                BdcZs bdcZs = bdcZsList.get(i);
                if (!StringUtils.equals(Constants.BDCQSCDJZ_BH_FONT, bdcZs.getZstype())) {
                    if (StringUtils.equals(Constants.BDCQZM_BH_FONT, bdcZs.getZstype())) {
                        zslx = Constants.BDCQZM_BH_DM;
                    } else {
                        zslx = Constants.BDCQZS_BH_DM;
                    }
                    Map wsyMap = bdcZsbhService.getZsYjByZslx(zslx);
                    if (StringUtils.isNotBlank(wsyMap.get("WSY").toString())) {
                        int wsy = Integer.parseInt(wsyMap.get("WSY").toString());
                        if (wsy < bdcZsList.size()) {
                            return ZSBH_TIP_BZQTJ;
                        }
                    } else {
                        return ZSBH_TIP_BZQTJ;
                    }

                    //获取当前登录人所在部门
                    List<PfOrganVo> pfOrganVoList = super.getOrganName();
                    String organName = pfOrganVoList.get(0).getOrganName();
                    //证书编号分配到各乡镇所,需要通过dwdm区分
                    String bdczsbhFilterZsbhXzdm = AppConfig.getProperty(CONFIGURATION_PARAMETER_FILTERZSBH_XZDM);
                    //按人员分配
                    String isry = AppConfig.getProperty("getZsbhByRy");
                    HashMap hashMap = new HashMap();
                    hashMap.put("zslx", zslx);
                    hashMap.put("lqdw", organName);
                    if (Boolean.parseBoolean(bdczsbhFilterZsbhXzdm))
                        hashMap.put("dwdm", super.getCurrentUserDwdm());
                    if (Boolean.parseBoolean(isry))
                        hashMap.put(PARAMETER_LQRID, super.getUserId());
                    String zsbh = bdcZsbhService.getZsbhByDwdm(hashMap);

                    if (StringUtils.isNotBlank(zslx)) {
                        HashMap map = new HashMap();
                        map.put("zsbh", zsbh);
                        map.put("zslx", zslx);
                        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
                        if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
                            BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                            bdcZsbh.setSyqk("3");
                            bdcZsbh.setLqr(super.getUserName());
                            bdcZsbh.setLqrid(super.getUserId());
                            bdcZsbh.setZsid(bdcZs.getZsid());
                            entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                            bdcZs.setBh(zsbh);
                            entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                            HashMap maphy = new HashMap();
                            maphy.put("zsid", bdcZs.getZsid());
                            maphy.put("zslx", zslx);
                            List<BdcZsbh> bdcZsbhListhy = bdcZsbhService.getBdcZsBhListByBhfw(maphy);
                            if (CollectionUtils.isNotEmpty(bdcZsbhListhy)) {
                                for (BdcZsbh bdcZsbhhy : bdcZsbhListhy) {
                                    if ((!StringUtils.equals(bdcZsbhhy.getZsbh(), bdcZsbh.getZsbh())) && (StringUtils.equals(bdcZsbhhy.getSyqk(), Constants.BDCZSBH_SYQK_LS))) {
                                        bdcZsbhhy.setZsid("");
                                        bdcZsbhhy.setSyqk(Constants.BDCZSBH_SYQK_WSY);
                                        bdcZsbhhy.setLqr("");
                                        bdcZsbhhy.setLqrid("");
                                        bdcZsbhhy.setBfyy("");
                                        entityMapper.saveOrUpdate(bdcZsbhhy, bdcZsbhhy.getZsbhid());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            result = "批量添加证书编号成功";
        }
        return result;
    }

    @RequestMapping(value = "/zsxhTz")
    public String zsxhTz(Model model) {
        return "/sjgl/zsxhTz";
    }

    @RequestMapping(value = "/zslqqjtz")
    public String zslqqjtz(Model model) {
        return "/sjgl/zslqqjtz";
    }

    //销毁台账
    @RequestMapping(value = "/bdczsbhXhList")
    public String bdczsbhXhList(Model model) {
        return "/query/bdczsbhXhList";
    }

    //销毁台账
    @RequestMapping(value = "/bdczsbhLyList")
    public String bdczsbhLyList(Model model) {
        return "/query/bdczsbhLyList";
    }

    //销毁编号查询
    @ResponseBody
    @RequestMapping("/getBdcZsBhXhListByPage")
    public Object getBdcZsBhXhListByPage(Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }
        return repository.selectPaging("getBdcZsBhXhListByPage", map, pageable);
    }

    //领用查询
    @ResponseBody
    @RequestMapping("/getBdcZsBhLqListByPage")
    public Object getBdcZsBhLqListByPage(Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }
        return repository.selectPaging("getBdcZsBhLqListByPage", map, pageable);
    }

    //根据领取部门获取领取人列表
    @ResponseBody
    @RequestMapping(value = "getLqrByLqdw")
    public Object getLqrByLqdw(Model model, String lqdw) {
        return getUserListByOrgan(lqdw);
    }

    //填报验证
    @ResponseBody
    @RequestMapping(value = "/checkZsBh", method = RequestMethod.GET)
    public String checkZsBh(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "zsid", required = false) String zsid, @RequestParam(value = "zsbh", required = false) String zsbh
            , @RequestParam(value = "zslx", required = false) String zslx) {

        List<PfOrganVo> pfOrganVoList = super.getOrganName();
        String organName = pfOrganVoList.get(0).getOrganName();
        String syr = super.getUserName();
        if (StringUtils.isNotBlank(proid)) {
            String xx = bdcZsbhService.checkZsbhNew(proid, zslx, zsbh, organName, zsid, syr);
            if (StringUtils.equals(xx, ParamsConstants.SUCCESS_LOWERCASE) && StringUtils.isNotBlank(zsid)) {
                BdcZs bdcZs = entityMapper.selectByPrimaryKey(BdcZs.class, zsid);
                if (bdcZs != null) {
                    bdcZs.setBh(zsbh);
                    entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                }
            }
            if (StringUtils.isBlank(xx)) {
                return ParamsConstants.SUCCESS_LOWERCASE;
            } else {
                return xx;
            }
        } else {
            return ParamsConstants.ERROR_LOWERCASE;
        }

    }


}

