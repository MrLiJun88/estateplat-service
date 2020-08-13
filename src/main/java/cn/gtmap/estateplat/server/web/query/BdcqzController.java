package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-3-20
 * Time: 下午4:43
 * doc:不动产单元目录
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcqz")
public class BdcqzController extends BaseController {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcBdcZsSdService bdcBdcZsSdService;

    public static final String PARAMETER_SQLXLISTJSON = "sqlxListJson";
    public static final String PARAMETER_YTLISTJSON = "ytListJson";
    public static final String PARAMETER_QSZTLISTJSON = "qsztListJson";
    public static final String PARAMETER_FWYTLISTJSON = "fwytListJson";
    public static final String PARAMETER_SQLXLIST = "sqlxList";
    public static final String PARAMETER_QSZTLIST= "qsztList";
    public static final String PARAMETER_FWYTLIST= "fwytList";



    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        List<BdcZdSqlx> sqlxList = bdcZdGlService.getBdcSqlx();
        List<Map> zdytList = bdcZdGlService.getZdYt();
        List<BdcZdQszt> qsztList = bdcZdGlService.getBdcZdQszt();
        List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
        String bdcqzGjss = AppConfig.getProperty("bdcqzGjss.order");
        List<String> bdcqzGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(bdcqzGjss) && bdcqzGjss.split(",").length > 0){
            for(String bdcqzGjssZd : bdcqzGjss.split(",")){
                bdcqzGjssOrderList.add(bdcqzGjssZd);
            }
        }
        model.addAttribute("bdcqzGjss", bdcqzGjss);
        model.addAttribute("bdcqzGjssOrderList", bdcqzGjssOrderList);
        model.addAttribute(PARAMETER_SQLXLISTJSON, JSONObject.toJSONString(sqlxList));
        model.addAttribute(PARAMETER_YTLISTJSON, JSONObject.toJSONString(zdytList));
        model.addAttribute(PARAMETER_QSZTLISTJSON, JSONObject.toJSONString(qsztList));
        model.addAttribute(PARAMETER_FWYTLISTJSON, JSONObject.toJSONString(fwytList));
        model.addAttribute(PARAMETER_SQLXLIST, sqlxList);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(PARAMETER_QSZTLIST, qsztList);
        model.addAttribute(PARAMETER_FWYTLIST, fwytList);
        return "query/bdcqzList";
    }

    @RequestMapping(value = "/getBdcdjzm", method = RequestMethod.GET)
    public String getBdcdjzm(Model model, String proid) {
        List<BdcZdSqlx> sqlxList = bdcZdGlService.getBdcSqlx();
        List<Map> zdytList = bdcZdGlService.getZdYt();
        List<BdcZdQszt> qsztList = bdcZdGlService.getBdcZdQszt();
        List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
        List<Map> zdzhytList = bdcTdService.getZdty();

        String bdcdjzmGjss = AppConfig.getProperty("bdcdjzmGjss.order");
        List<String> bdcdjzmGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(bdcdjzmGjss) && bdcdjzmGjss.split(",").length > 0){
            for(String bdcdjzmGjssZd : bdcdjzmGjss.split(",")){
                bdcdjzmGjssOrderList.add(bdcdjzmGjssZd);
            }
        }
        model.addAttribute("bdcdjzmGjss", bdcdjzmGjss);
        model.addAttribute("bdcdjzmGjssOrderList", bdcdjzmGjssOrderList);
        model.addAttribute(PARAMETER_SQLXLISTJSON, JSONObject.toJSONString(sqlxList));
        model.addAttribute(PARAMETER_YTLISTJSON, JSONObject.toJSONString(zdytList));
        model.addAttribute(PARAMETER_QSZTLISTJSON, JSONObject.toJSONString(qsztList));
        model.addAttribute(PARAMETER_FWYTLISTJSON, JSONObject.toJSONString(fwytList));
        model.addAttribute(PARAMETER_SQLXLIST, sqlxList);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(PARAMETER_QSZTLIST, qsztList);
        model.addAttribute(PARAMETER_FWYTLIST, fwytList);
        model.addAttribute("zdzhytList", zdzhytList);
        return "query/bdcdjzmList";
    }

    @ResponseBody
    @RequestMapping("/getBdcqzPagesJson")
    public Object getBdcqzPagesJson(Pageable pageable, String qlr, String bdcdyh, String sqlx, String bdcqzh, String ghyt, String qszt, String dcxc, String zstype,String zdyt,String szr,String fzqssj,String fzjssj,String ywr) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isNotBlank(dcxc)) {
                map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            } else {
                if (StringUtils.isNotBlank(qlr)) {
                    map.put("qlr", StringUtils.deleteWhitespace(qlr));
                }
                if (StringUtils.isNotBlank(ywr)) {
                    map.put("ywr", StringUtils.deleteWhitespace(ywr));
                }
                if (StringUtils.isNotBlank(bdcdyh)) {
                    map.put("bdcdyh", StringUtils.deleteWhitespace(bdcdyh));
                }
                if (StringUtils.isNotBlank(sqlx)) {
                    map.put("sqlx", StringUtils.deleteWhitespace(sqlx));
                }
                if (StringUtils.isNotBlank(bdcqzh)) {
                    map.put("bdcqzh", StringUtils.deleteWhitespace(bdcqzh));
                }
                if (StringUtils.isNotBlank(ghyt)) {
                    map.put("ghyt", StringUtils.deleteWhitespace(ghyt));
                }
                if (StringUtils.isNotBlank(qszt)) {
                    map.put("qszt", StringUtils.deleteWhitespace(qszt));
                }
                if (StringUtils.isNotBlank(zdyt)) {
                    map.put("zdyt", StringUtils.deleteWhitespace(zdyt));
                }
                if (StringUtils.isNotBlank(szr)) {
                    map.put("szr", StringUtils.deleteWhitespace(szr));
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (StringUtils.isNotBlank(fzqssj)) {
                    map.put("fzqssj", simpleDateFormat.parse(fzqssj));
                }
                if (StringUtils.isNotBlank(fzjssj)) {
                    map.put("fzjssj", simpleDateFormat.parse(fzjssj));
                }
            }
        } catch (ParseException e) {
            logger.error("BdcqzController.getBdcqzPagesJson",e);
        }
		/**
		 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
		 * @description 添加商品房首次的证书类型
		 */
		String zsFont = AppConfig.getProperty("spfscdj.zstype");
		List zstypeList = new ArrayList();

        //过滤证书类型
        if (StringUtils.equals(zstype, Constants.BDCQZS_BH_DM)){
			if(StringUtils.isNotBlank(zsFont)){
				zstypeList.add(zsFont);
			}
			zstypeList.add(Constants.BDCQZS_BH_FONT);
			map.put("zstype", zstypeList);
		} else if (StringUtils.equals(zstype, Constants.BDCQZM_BH_DM)){
			zstypeList.add(Constants.BDCQZM_BH_FONT);
			map.put("zstype", zstypeList);
		}

        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm)){
            map.put("xzqdm", userDwdm);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "getBdcqzDyCfStatus")
    public HashMap getBdcqzDyCfStatus(Model model, String proid, String zsid, String bdcdyh) {
        HashMap resultMap = new HashMap();
        //获取查封数据
        List<Map> cfList = null;
        //获取抵押数据
        List<BdcDyaq> dyList = null;
        //获取异议数据
        List<BdcYy> yyList = null;
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(bdcdyh)) {
            BdcXm bdcxm = bdcXmService.getBdcXmByProid(proid);
            String bdcdyid =  bdcxm.getBdcdyid();
            if(StringUtils.isNotBlank(bdcdyid)){
                cfList = qllxService.getCfxxByBdcdyId(bdcdyid);
                dyList = qllxService.getDyxxByBdcdyId(bdcdyid);
                yyList = qllxService.getYyxxByBdcdyId(bdcdyid);
            }
        }
        // true 正常  false 查封
        if (CollectionUtils.isEmpty(cfList)) {
            resultMap.put("cf", true);
        } else {
            resultMap.put("cf", false);
        }
        // true 正常  false 抵押
        if (CollectionUtils.isEmpty(dyList)) {
            resultMap.put("dy", true);
        } else {
            resultMap.put("dy", false);
        }

        // true 正常  false 抵押
        if (CollectionUtils.isEmpty(yyList)) {
            resultMap.put("yy", true);
        } else {
            resultMap.put("yy", false);
        }

        //zwq 不动产证明则为正常
        BdcZs bdcZs = bdcZsService.queryBdcZsByZsid(zsid);
        if (StringUtils.equals(bdcZs.getZstype(), Constants.BDCQZM_BH_FONT)) {
            resultMap.put("cf", true);
            resultMap.put("dy", true);
            resultMap.put("yy", true);
        }
        return resultMap;
    }
    
    /**
     * @author xiejianan
     * @description 针对张家港的需求1221所写，未知标准版的能否直接改动
     * @param model
     * @param proid
     * @return
     */
    @RequestMapping(value = "/getZhjgBdcdjzm", method = RequestMethod.GET)
    public String getZhjgBdcdjzm(Model model, String proid) {
        List<BdcZdSqlx> sqlxList = bdcZdGlService.getBdcSqlx();
        List<Map> zdytList = bdcZdGlService.getZdYt();
        List<BdcZdQszt> qsztList = bdcZdGlService.getBdcZdQszt();
        List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
        List<Map> zdzhytList = bdcTdService.getZdty();

        String bdcdjzmGjss = AppConfig.getProperty("bdcdjzmGjss.order");
        List<String> bdcdjzmGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(bdcdjzmGjss) && bdcdjzmGjss.split(",").length > 0){
            for(String bdcdjzmGjssZd : bdcdjzmGjss.split(",")){
                bdcdjzmGjssOrderList.add(bdcdjzmGjssZd);
            }
        }
        model.addAttribute("bdcdjzmGjss", bdcdjzmGjss);
        model.addAttribute("bdcdjzmGjssOrderList", bdcdjzmGjssOrderList);

        model.addAttribute(PARAMETER_SQLXLISTJSON, JSONObject.toJSONString(sqlxList));
        model.addAttribute(PARAMETER_YTLISTJSON, JSONObject.toJSONString(zdytList));
        model.addAttribute(PARAMETER_QSZTLISTJSON, JSONObject.toJSONString(qsztList));
        model.addAttribute(PARAMETER_FWYTLISTJSON, JSONObject.toJSONString(fwytList));
        model.addAttribute(PARAMETER_SQLXLIST, sqlxList);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(PARAMETER_QSZTLIST, qsztList);
        model.addAttribute(PARAMETER_FWYTLIST, fwytList);
        model.addAttribute("zdzhytList", zdzhytList);
        return "query/zhjgbdcdjzmList";
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 检查数据是否锁定
     */
    @ResponseBody
    @RequestMapping(value = "checkBdcSjSd", method = RequestMethod.GET)
    public String checkBdcSjSd(@RequestParam(value = "zsid", required = false) String zsid) {
        String msg = "true";
        HashMap map = new HashMap();
        map.put("zsid",zsid);
        map.put("xzzt","0");
        List<BdcBdcZsSd> list = bdcBdcZsSdService.getBdcZsSdList(map);

        //该条过渡数据已被锁定
        if (CollectionUtils.isNotEmpty(list)) {
            msg = ParamsConstants.FALSE_LOWERCASE;
        }
        return msg;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 添加不动产数据锁定
     */
    @ResponseBody
    @RequestMapping(value = "lockBdcZsSj", method = RequestMethod.GET)
    public HashMap<String, String> lockBdcZsSj(@RequestParam(value = "zsid", required = false) String zsid,
                                               @RequestParam(value = "cqzh", required = false) String cqzh,
                                               @RequestParam(value = "bdclx", required = false) String bdclx,
                                               @RequestParam(value = "proid", required = false) String proid,
                                               @RequestParam(value = "xzyy", required = false) String xzyy) {
        HashMap map = new HashMap();
        try {
            BdcBdcZsSd bdcBdcZsSd = new BdcBdcZsSd();
            bdcBdcZsSd.setSdid(UUIDGenerator.generate18());
            bdcBdcZsSd.setCqzh(CommonUtil.formatEmptyValue(cqzh));
            bdcBdcZsSd.setZsid(zsid);
            bdcBdcZsSd.setBdclx(bdclx);
            bdcBdcZsSd.setProid(proid);
            bdcBdcZsSd.setXzzt(0);
            bdcBdcZsSd.setXzyy(xzyy);
            bdcBdcZsSd.setSdr(super.getUserName());
            bdcBdcZsSd.setSdsj(new Date(System.currentTimeMillis()));
            entityMapper.saveOrUpdate(bdcBdcZsSd, bdcBdcZsSd.getSdid());
            map.put("flag", "true");
            map.put("msg", "锁定成功!");
        } catch (Exception e) {
            map.put("flag", ParamsConstants.FALSE_LOWERCASE);
            map.put("msg", "锁定失败!");
        }
        return map;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 解锁不动产证书
     */
    @ResponseBody
    @RequestMapping(value = "UnlockBdcZsSj", method = RequestMethod.GET)
    public HashMap<String, String> unlockBdcZsSj(@RequestParam(value = "zsid", required = false) String zsid) {
        HashMap map = new HashMap();
        HashMap bdcZsSd = new HashMap();
        bdcZsSd.put("zsid",zsid);
        bdcZsSd.put("xzzt","0");
        List<BdcBdcZsSd> bdcBdcZsSdList = bdcBdcZsSdService.getBdcZsSdList(bdcZsSd);
        if (CollectionUtils.isEmpty(bdcBdcZsSdList)) {
            map.put("flag", ParamsConstants.FALSE_LOWERCASE);
            map.put("msg", "该数据没有被锁定，不能解锁！");
        } else {
            BdcBdcZsSd bdcBdcZsSd = bdcBdcZsSdList.get(0);
            bdcBdcZsSd.setXzzt(1);
            bdcBdcZsSd.setJsr(super.getUserName());
            bdcBdcZsSd.setJssj(new Date(System.currentTimeMillis()));
            entityMapper.saveOrUpdate(bdcBdcZsSd, bdcBdcZsSd.getSdid());
            map.put("flag", "true");
            map.put("cqzh",bdcBdcZsSd.getCqzh());
            map.put("msg", "解锁成功!");
        }
        return map;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 获取限制原因
     */
    @ResponseBody
    @RequestMapping(value = "getXzyy", method = RequestMethod.GET)
    public HashMap<String, String> getXzyy(@RequestParam(value = "cqzh", required = false) String cqzh) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        HashMap bdcZsSd = new HashMap();
        bdcZsSd.put("cqzh",cqzh);
        bdcZsSd.put("xzzt","0");
        List<BdcBdcZsSd> bdcBdcZsSdList = bdcBdcZsSdService.getBdcZsSdList(bdcZsSd);
        //该条过渡数据已被锁定
        if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
            BdcBdcZsSd bdcBdcZsSd = bdcBdcZsSdList.get(0);
            String xzyy = bdcBdcZsSd.getXzyy();
            resultMap.put("msg", ParamsConstants.FALSE_LOWERCASE);
            resultMap.put("xzyy", xzyy);
        } else {
            resultMap.put("msg", "true");
        }
        return resultMap;
    }

    /**
     * 证书补录
     */
    @RequestMapping(value = "bdcZsBl", method = RequestMethod.GET)
    public String bdcZsBl(Model model) {
        return "query/bdcZszmList";
    }

    @ResponseBody
    @RequestMapping("/getBdcZsListByPage")
    public Object getBdcZsListByPage (Pageable pageable, String sjbh){
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(sjbh)){
            map.put("bh",sjbh);
        }
        return repository.selectPaging("getBdcZszmByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getFzrq")
    public Map getFzrq(String zsid){
        Map<String,String> map = Maps.newHashMap();
        if(StringUtils.isNotBlank(zsid)) {
            BdcZs bdcZs = bdcZsService.queryBdcZsByZsid(zsid);
            if(bdcZs!=null&&bdcZs.getFzrq()!=null){
                String zslzrq = CalendarUtil.formateToStrChinaYMDDate(bdcZs.getLzrq());
                if(StringUtils.isNotEmpty(zslzrq)){
                    map.put("zslzrq",zslzrq);
                }
                if(StringUtils.isNotEmpty(bdcZs.getLzr())){
                    map.put("zslzr",bdcZs.getLzr());
                }
            }
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/saveFzrqInfo")
    public Map saveFzrqInfo(String zslzrq,String zsid,String zslzr){
        String msg = "";
        Map<String,String> map = Maps.newHashMap();
        if(StringUtils.isNotBlank(zsid)){
            List<BdcZs> bdcZsList = bdcZsService.getAllBdcZsWithSameWiidByZsid(zsid);
            if(CollectionUtils.isNotEmpty(bdcZsList)) {
                for(BdcZs bdcZs : bdcZsList) {
                    if (bdcZs != null) {
                        if (StringUtils.isNotEmpty(zslzrq)) {
                            bdcZs.setLzrq(CalendarUtil.formatDate(zslzrq));
                        } else {
                            bdcZs.setLzrq(new Date());
                        }
                        if (StringUtils.isNotEmpty(zslzr)) {
                            bdcZs.setLzr(zslzr);
                        }
                        entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                    }
                }
                msg = "录入成功";
            }
        }else{
            msg = "录入失败";
        }
        map.put("msg",msg);
        return map;
    }

}
