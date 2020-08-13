package cn.gtmap.estateplat.server.web.edit;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.BdcZsbh;
import cn.gtmap.estateplat.model.server.core.Dwxx;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.BdcZsbhService;
import cn.gtmap.estateplat.server.core.service.DwxxService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-9-18
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/zsBhGl")
public class ZsBhGlConntroller extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsbhService bdcZsbhService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private DwxxService dwxxService;

    protected static final String ZSBH_TIP_BZQTJ = "证书编号不足请添加";
    protected static final String CONFIGURATION_PARAMETER_FILTERZSBH_XZDM = "bdczsbh.filterZsbh.xzdm";
    protected static final String PARAMETER_HHSEARCH = "hhSearch";
    protected static final String PARAMETER_GETBDCZSBHLISTBYPAGE = "getBdcZsBhListByPage";
    protected static final String ZSBHNOTLIKELIST = "zsbhNotLikeList";
    protected static final String ZSBHNOTEQUALLIST = "zsbhNotEqualList";

    @RequestMapping(value = "")
    public String zsBhGl(Model model, String isedit) {
        //加载部门list
        List<PfOrganVo> organVoList = PlatformUtil.getOrganList();

        model.addAttribute("organVoList", organVoList);
        model.addAttribute("isedit", isedit);
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        List<Dwxx> dwxxList = dwxxService.getDwxxList(userDwdm, 6);
        //证书编号分配到各乡镇所,需要放开位数，以取到乡镇的行政代码
        String bdczsbhFilterZsbhXzdm = AppConfig.getProperty(CONFIGURATION_PARAMETER_FILTERZSBH_XZDM);
        if(StringUtils.equals(bdczsbhFilterZsbhXzdm, "true"))
            dwxxList = dwxxService.getDwxxList(userDwdm, 9);
        if(dwxxList == null)
            dwxxList = new ArrayList<Dwxx>();
        model.addAttribute("dwxxList", dwxxList);
        //zwq 是否根据人员来创建证书编号
        List<PfUserVo> pfUserVoList = null;
        String getZsbhByRy = AppConfig.getProperty("getZsbhByRy");
        String showXzqmc = AppConfig.getProperty("showXzqmc");
        if(StringUtils.isNotBlank(getZsbhByRy) && getZsbhByRy.equals("true")) {
            String zsbhUseRoleName = AppConfig.getProperty("zsbhUseRoleName");
            if(StringUtils.isNotBlank(zsbhUseRoleName)) {
                List<PfRoleVo> pfRoleVoList = sysUserService.getRoleList();
                if(CollectionUtils.isNotEmpty(pfRoleVoList)) {
                    for(PfRoleVo pfRoleVo : pfRoleVoList) {
                        if(StringUtils.equals(pfRoleVo.getRoleName(), zsbhUseRoleName)) {
                            pfUserVoList = sysUserService.getUserListByRole(pfRoleVo.getRoleId());
                            break;
                        }
                    }
                }
            } else {
                pfUserVoList = sysUserService.getAllUsers();
            }
        }
        model.addAttribute("pfusers", pfUserVoList);
        model.addAttribute("isry", getZsbhByRy);
        model.addAttribute("showXzqmc", showXzqmc);
        return "/sjgl/zsBhGl";
    }

    @ResponseBody
    @RequestMapping("/getBdcZsBhListByPage")
    public Object getBdcZsBhListByPage(Pageable pageable, String hhSearch, String qsbh, String jsbh, String jsrq, String qsrq, String dwdm, BdcZsbh bdcZsbh, String organId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(hhSearch)) {
            if(hhSearch.contains("证书"))
                map.put(PARAMETER_HHSEARCH, "zs");
            else if(hhSearch.contains("证明书"))
                map.put(PARAMETER_HHSEARCH, "zms");
            else
                map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        } else {
            try {
                if(StringUtils.isNotBlank(jsrq)) {
                    map.put("jsrq", new SimpleDateFormat("yyyy-MM-dd").parse(jsrq));
                }
                if(StringUtils.isNotBlank(qsrq)) {
                    map.put("qsrq", new SimpleDateFormat("yyyy-MM-dd").parse(qsrq));
                }
            } catch (ParseException e) {
                logger.error("ZsBhGlConntroller.getBdcZsBhListByPage",e);
            }
            map.put("qsbh", qsbh);
            if(StringUtils.isNotBlank(dwdm)) {
                while(dwdm.endsWith("0")) {
                    dwdm = dwdm.substring(0, dwdm.length() - 1);
                }
            }
            map.put("dwdm", dwdm);
            map.put("jsbh", jsbh);
            if(bdcZsbh != null) {
                map.put("cjr", bdcZsbh.getCjr());
                map.put("lqr", bdcZsbh.getLqr());
                if(StringUtils.isNotBlank(bdcZsbh.getZslx())&& StringUtils.equals(bdcZsbh.getZslx(),Constants.BDCDZZZZMS_BH_DM)){
                    map.put("zslx",Constants.BDCQZM_BH_DM);
                    map.put("bz", Constants.HLW_ZSBH_BZ);
                }else{
                    map.put("zslx", bdcZsbh.getZslx());
                    map.put("isHlwDya", "false");
                }
                if(StringUtils.isNotBlank(bdcZsbh.getSyqk())) {
                    map.put("syqk", bdcZsbh.getSyqk().split(","));
                }
            }

        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm))
            map.put(ParamsConstants.XZQDM_LOWERCASE, userDwdm);

        //根据页面选择的部门id，查询平台库中该部门下的所有用户
        List<PfUserVo> userList = PlatformUtil.getUsersByOrganId(organId);
        StringBuilder userNameStr = new StringBuilder();
        if(CollectionUtils.isNotEmpty(userList)) {
            for(int i = 0; i < userList.size(); i++) {
                PfUserVo userVo = userList.get(i);
                userNameStr.append(userVo.getUserName()).append(",");
            }
            if(StringUtils.isNotBlank(userNameStr) && userNameStr.length() > 0) {
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
        if(StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }

        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm))
            map.put(ParamsConstants.XZQDM_LOWERCASE, userDwdm);

        //作废状态证书编号
        map.put("syqk", new String[]{Constants.BDCZSBH_SYQK_ZF});

        List<String> zsbhNotEqualList = Arrays.asList(ParamsConstants.ZSBH_NOTEQUAL_LIST);
        List<String> zsbhNotLikeList = Arrays.asList(ParamsConstants.ZSBH_NOTLIKE_LIST);
        map.put(ZSBHNOTEQUALLIST,zsbhNotEqualList);
        map.put(ZSBHNOTLIKELIST,zsbhNotLikeList);
        return repository.selectPaging(PARAMETER_GETBDCZSBHLISTBYPAGE, map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcZsBhXhListByPage")
    public Object getBdcZsBhXhListByPage(Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }

        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm))
            map.put(ParamsConstants.XZQDM_LOWERCASE, userDwdm);

        //作废状态证书编号
        map.put("syqk", new String[]{Constants.BDCZSBH_SYQK_XH});

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
        if(StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }
        return repository.selectPaging("getBdcZsBhLqqjListByPage", map, pageable);
    }


    @ResponseBody
    @RequestMapping("/saveZsBh")
    public String saveZsBh(Model model, String zslx, String qsbh, String jsbh, String dwdm, String lqrname, String lqrid) {
        if(StringUtils.isBlank(dwdm)) {
            dwdm = super.getWhereXzqdm();
        } else {
            //证书编号分配到各乡镇所,需要放开位数，以取到乡镇的行政代码
            String bdczsbhFilterZsbhXzdm = AppConfig.getProperty(CONFIGURATION_PARAMETER_FILTERZSBH_XZDM);
            if(!StringUtils.equals(bdczsbhFilterZsbhXzdm, "true")) {
                while(dwdm.endsWith("0")) {
                    dwdm = dwdm.substring(0, dwdm.length() - 1);
                }
            }
        }
        HashMap map = new HashMap();
        map.put("qsbh", qsbh);
        map.put("jsbh", jsbh);
        if(StringUtils.isNotBlank(zslx)&&StringUtils.equals(zslx,Constants.BDCDZZZZMS_BH_DM)){
            map.put("zslx",Constants.BDCQZM_BH_DM);
        }else{
            map.put("zslx", zslx);
        }
        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
        BdcZsbh bdcZsbh = new BdcZsbh();
        bdcZsbh.setCjr(super.getUserName());
        bdcZsbh.setDwdm(dwdm);
        bdcZsbh.setZslx(zslx);
        bdcZsbh.setSyqk("0");
        //zwq 增加领取人
        bdcZsbh.setLqrid(lqrid);
        bdcZsbh.setLqr(lqrname);
        if(StringUtils.isNotBlank(zslx)&&StringUtils.equals(zslx,"dzzzzms")){
            bdcZsbh.setBz(Constants.HLW_ZSBH_BZ);
            bdcZsbh.setZslx(Constants.BDCQZM_BH_DM);
        }
        bdcZsbh.setCjsj(new Date());
        Calendar a = Calendar.getInstance();
        bdcZsbh.setNf(a.get(Calendar.YEAR) + "");
        return bdcZsbhService.checkAndSaveZsh(bdcZsbhList, bdcZsbh, qsbh, jsbh);
    }

    @ResponseBody
    @RequestMapping("/zfZsBh")
    public String zfZsBh(Model model, String zsbhid, String bfyy, String zszt) {
        String result = "失败";
        HashMap map = new HashMap();
        map.put("zsbhid", zsbhid);
        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
        if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
            BdcZsbh bdcZsbh = bdcZsbhList.get(0);
            bdcZsbh.setSyqk(zszt);
            bdcZsbh.setBfyy(bfyy);
            bdcZsbh.setZfr(getUserName());
            bdcZsbh.setZfrid(getUserId());
            bdcZsbh.setZfsj(new Date());
            entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
            result = "操作成功";
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
        //按人员分配
        String isry = AppConfig.getProperty("getZsbhByRy");
        if(StringUtils.isNotBlank(bdczsbhFilterZsbhXzdm) && StringUtils.equals(bdczsbhFilterZsbhXzdm.trim(), "true")) {
            HashMap hashMap = new HashMap();
            hashMap.put("zslx", zslx);
            hashMap.put("dwdm", dwdm);
            //按人员分配
            if(StringUtils.equals(isry, "true"))
                hashMap.put("lqrid", super.getUserId());
            zsbh = bdcZsbhService.getZsbhByDwdm(hashMap);
        } else if(StringUtils.equals(isry, "true")) {
            HashMap hashMap = new HashMap();
            hashMap.put("zslx", zslx);
            hashMap.put("lqrid", super.getUserId());
            zsbh = bdcZsbhService.getZsbhByDwdm(hashMap);
        } else
            zsbh = bdcZsbhService.getZsbh(zslx);
        if(StringUtils.isNotBlank(zslx)) {
            List<BdcZsbh> bdcZsbhList = new ArrayList<BdcZsbh>();
            if(StringUtils.isNotBlank(zsbh)) {
                HashMap map = new HashMap();
                map.put("zsbh", zsbh);
                map.put("zslx", zslx);
                map.put("dwdm", dwdm);
                bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
            }
            BdcZs bdcZs = bdcZsService.queryBdcZsByZsid(zsid);
            if(CollectionUtils.isNotEmpty(bdcZsbhList) && bdcZs != null) {
                BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                bdcZsbh.setSyqk("3");
                bdcZsbh.setLqr(super.getUserName());
                bdcZsbh.setLqrid(super.getUserId());
                bdcZsbh.setZsid(zsid);
                entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                bdcZs.setBh(bdcZsbh.getZsbh());
                entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                HashMap maphy = new HashMap();
                maphy.put("zsid", zsid);
                maphy.put("zslx", zslx);
                List<BdcZsbh> bdcZsbhListhy = bdcZsbhService.getBdcZsBhListByBhfw(maphy);
                if(CollectionUtils.isNotEmpty(bdcZsbhListhy)) {
                    for(BdcZsbh bdcZsbhhy : bdcZsbhListhy) {
                        if((!StringUtils.equals(bdcZsbhhy.getZsbh(), bdcZsbh.getZsbh())) && (StringUtils.equals(bdcZsbhhy.getSyqk(), Constants.BDCZSBH_SYQK_LS))) {
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
            result = zsbh;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/organizeZsbh")
    public String organizeZsbh(Model model, String zsbhid) {
        String json = "{";
        HashMap map = new HashMap();
        map.put("zsbhid", zsbhid);
        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
        if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
            BdcZsbh bdcZsbh = bdcZsbhList.get(0);
            json += "\"zsbhid\":" + "\"" + bdcZsbh.getZsbhid() + "\",\"zsbh\":" + "\"" + bdcZsbh.getZsbh() + "\",\"bfyy\":" + "\"" + bdcZsbh.getBfyy() + "\",\"zszt\":" + "\"" + bdcZsbh.getSyqk() + "\"";
            if(StringUtils.isNotBlank(bdcZsbh.getZsid())) {
                BdcZs bdcZs = entityMapper.selectByPrimaryKey(BdcZs.class, bdcZsbh.getZsid());
                if(bdcZs != null) {
                    json += ",\"bdcqzh\":" + "\"" + bdcZs.getBdcqzh() + "\"";
                }
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
        if(CollectionUtils.isNotEmpty(bdcZsList)) {
            for(int i = 0; i < bdcZsList.size(); i++) {
                BdcZs bdcZs = bdcZsList.get(i);
                if(!StringUtils.equals(Constants.BDCQSCDJZ_BH_FONT, bdcZs.getZstype())) {
                    if(StringUtils.equals(Constants.BDCQZM_BH_FONT, bdcZs.getZstype())) {
                        zslx = Constants.BDCQZM_BH_DM;
                    } else {
                        zslx = Constants.BDCQZS_BH_DM;
                    }
                    Map wsyMap = bdcZsbhService.getZsYjByZslx(zslx);
                    if(StringUtils.isNotBlank(wsyMap.get("WSY").toString())) {
                        int wsy = Integer.parseInt(wsyMap.get("WSY").toString());
                        if(wsy < bdcZsList.size()) {
                            return ZSBH_TIP_BZQTJ;
                        }
                    } else {
                        return ZSBH_TIP_BZQTJ;
                    }

                    String zsbh = bdcZsbhService.getZsbh(zslx);
                    if(StringUtils.isNotBlank(zslx)) {
                        HashMap map = new HashMap();
                        map.put("zsbh", zsbh);
                        map.put("zslx", zslx);
                        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
                        if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
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
                            if(CollectionUtils.isNotEmpty(bdcZsbhListhy)) {
                                for(BdcZsbh bdcZsbhhy : bdcZsbhListhy) {
                                    if((!StringUtils.equals(bdcZsbhhy.getZsbh(), bdcZsbh.getZsbh())) && (StringUtils.equals(bdcZsbhhy.getSyqk(), Constants.BDCZSBH_SYQK_LS))) {
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

    /**
     * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
     * @description 标记销毁证书
     */
    @ResponseBody
    @RequestMapping(value = "/zsbhXh")
    public void zsbhXh(@RequestParam(value = "zsbhid", required = false) String zsbhid, @RequestParam(value = "xhr", required = false) String xhr,
                       @RequestParam(value = "xhrid", required = false) String xhrid, @RequestParam(value = "qxxh", required = false) String qxxh,
                       @RequestParam(value = "xhjzr", required = false) String xhjzr) {
        if(StringUtils.isNotBlank(zsbhid)) {
            xhr = StringUtils.isNotBlank(xhr) ? xhr : super.getUserName();
            xhrid = StringUtils.isNoneBlank(xhrid) ? xhrid : super.getUserId();
            if(StringUtils.isNotBlank(qxxh) && StringUtils.equals(qxxh, "true")) {
                bdcZsbhService.qxZsbhXh(zsbhid);
            } else {
                bdcZsbhService.zsbhXh(zsbhid, xhr, xhrid, xhjzr);
            }
        }
    }

    @ResponseBody
    @RequestMapping("/getZsbhByZsid")
    public String getZsbhByZsid(Model model, String zsid) {
        String zslx = "";
        String zsbh = "";
        String result = "";
        BdcZs bdcZs = bdcZsService.queryBdcZsByZsid(zsid);
        if(!StringUtils.equals(Constants.BDCQSCDJZ_BH_FONT, bdcZs.getZstype())) {
            if(StringUtils.equals(Constants.BDCQZM_BH_FONT, bdcZs.getZstype())) {
                zslx = Constants.BDCQZM_BH_DM;
            } else {
                zslx = Constants.BDCQZS_BH_DM;
            }
            Map wsyMap = bdcZsbhService.getZsYjByZslx(zslx);
            if(StringUtils.isNotBlank(wsyMap.get("WSY").toString())) {
                int wsy = Integer.parseInt(wsyMap.get("WSY").toString());
                if(wsy < 1) {
                    return ZSBH_TIP_BZQTJ;
                }
            } else {
                return ZSBH_TIP_BZQTJ;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("zslx", zslx);
            hashMap.put("lqrid", super.getUserId());
            hashMap.put("dwdm", super.getCurrentUserDwdm());
            zsbh = bdcZsbhService.getZsbhByDwdm(hashMap);
            if(StringUtils.isNotBlank(zslx)) {
                HashMap map = new HashMap();
                map.put("zsbh", zsbh);
                map.put("zslx", zslx);
                map.put("dwdm", super.getCurrentUserDwdm());
                List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
                if (CollectionUtils.isNotEmpty(bdcZsbhList) && bdcZs != null) {
                    BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                    bdcZsbh.setSyqk("3");
                    bdcZsbh.setSyr(super.getUserName());
                    bdcZsbh.setSyrid(super.getUserId());
                    bdcZsbh.setSysj(new Date());
                    bdcZsbh.setZsid(zsid);
                    bdcZsService.saveBdcZsBh(bdcZsbh);
                    zsbh = bdcZsbh.getZsbh();
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
                                bdcZsService.saveBdcZsBh(bdcZsbh);
                            }
                        }
                    }
                }
            }
        }
        if (bdcZs != null && StringUtils.isNotBlank(zsbh)) {
            bdcZs.setBh(zsbh);
            bdcZs.setFzrq(CalendarUtil.getCurDate());
            bdcZsService.saveZs(bdcZs);
        }
        result = "添加证书编号成功";
        return result;
    }
}
