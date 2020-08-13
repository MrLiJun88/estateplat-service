package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.common.util.UUIDGenerator;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sc
 * Date: 15-8-27
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/gdXxLr")
public class GdXxLrController extends BaseController {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdQlrService gdQlrService;
    @Autowired
    private Repo repository;
    @Autowired
    private GdXmService gdXmService;

    @Autowired
    private GdqlService gdqlService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    private String msgInfo;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcXmService bdcXmService;

    public static final String PARAMETER_HHSEARCH = "hhSearch";
    public static final String PARAMETER_GETGDFWLRJSONBYPAGE = "getGdFwLrJsonByPage";
    public static final String PARAMETER_GETGDQLJSONBYPAGE = "getGdQlJsonByPage";

    @ResponseBody
    @RequestMapping(value = "getUUid", method = RequestMethod.GET)
    public String getUUid() throws IOException {
        return UUIDGenerator.generate18();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdclx", required = false) String bdclx,
                        @RequestParam(value = "editFlag", required = false) String editFlag, @RequestParam(value = "iscp", required = false) String iscp,
                        @RequestParam(value = "tdid", required = false) String tdid,@RequestParam(value = "qrFlag", required = false) String qrFlag) {
        if (StringUtils.isBlank(proid))
            proid = UUIDGenerator.generate18();
        List<String> gdTabOrderList = new ArrayList<String>();
        gdTabOrderList.add("xm");
        gdTabOrderList.add("ql");
        if (StringUtils.equals(bdclx, "fw"))
            gdTabOrderList.add("fw");
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("gdTabOrderList", gdTabOrderList);
        HashMap qlSelect = new HashMap();
        if (StringUtils.equals(bdclx, "fw"))
            qlSelect.put(Constants.GDQL_FWSYQ_CPT, Constants.GDQL_FWSYQ);
        else if (StringUtils.equals(bdclx, "td")) {
            qlSelect.put(Constants.GDQL_TDSYQ_CPT, Constants.GDQL_TDSYQ);
            qlSelect.put(Constants.GDQL_TDSYNQ_CPT, Constants.GDQL_TDSYNQ);
        }
        qlSelect.put(Constants.GDQL_CF_CPT, Constants.GDQL_CF);
        qlSelect.put(Constants.GDQL_YG_CPT, Constants.GDQL_YG);
        qlSelect.put(Constants.GDQL_YY_CPT, Constants.GDQL_YY);
        qlSelect.put(Constants.GDQL_DY_CPT, Constants.GDQL_DY);
        model.addAttribute("qlSelect", qlSelect);
        if (StringUtils.isNotBlank(editFlag)) {
            //sc 运行编辑操作
            model.addAttribute("editFlag", editFlag);
            //获取土地权力状态，新增的置0，修改保持不变
            List<GdTdsyq> gdTdsyqList = gdTdService.getGdTdsyqListByGdproid(proid,null);
            Integer qlzt = 0;
            if(CollectionUtils.isNotEmpty(gdTdsyqList) && (gdTdsyqList.get(0).getIszx() != null)){
                qlzt = gdTdsyqList.get(0).getIszx();
            }
            model.addAttribute("qlzt", qlzt);
        }
        model.addAttribute("tdid", tdid);
        model.addAttribute("bdclx", bdclx);
        String gdTabOrder = "fw,xm,lq,cq";
        model.addAttribute("gdTabOrder", gdTabOrder);
        String gdTabLoadData = AppConfig.getProperty("gdTab.loadData");
        model.addAttribute("gdTabLoadData", gdTabLoadData);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(ParamsConstants.PROID_LOWERCASE, proid);
		String fwid = "";
        Page<HashMap> dataPaging = repository.selectPaging(PARAMETER_GETGDFWLRJSONBYPAGE, map, 0, 1);
        if (CollectionUtils.isNotEmpty(dataPaging.getRows())
                &&dataPaging.getRows().get(0).get("FWID") != null) {
            fwid = dataPaging.getRows().get(0).get("FWID").toString();
            model.addAttribute("fwid", fwid);
        }
		if (StringUtils.isNotBlank(fwid)) {
			model.addAttribute(ParamsConstants.BDCID_LOWERCASE,fwid);
		} else if (StringUtils.isNotBlank(tdid)) {
			model.addAttribute(ParamsConstants.BDCID_LOWERCASE,tdid);
		}
        if(CollectionUtils.isEmpty(dataPaging.getRows())&&StringUtils.equals(bdclx,"td") ){
            model.addAttribute("tdqlid",  UUIDGenerator.generate18());
        }
        Page<HashMap> dataPagingql = repository.selectPaging(PARAMETER_GETGDQLJSONBYPAGE, map, 0, 1);
        String cpt = "";
        if (StringUtils.equals(bdclx, "fw"))
            cpt = Constants.GDQL_FWSYQ_CPT;
        else if (StringUtils.equals(bdclx, "td"))
            cpt = Constants.GDQL_TDSYQ_CPT;
        if (CollectionUtils.isNotEmpty(dataPagingql.getRows())
                &&dataPagingql.getRows().get(0).get("QLID") != null) {
            String qlid = dataPagingql.getRows().get(0).get("QLID").toString();
            String qllx = dataPagingql.getRows().get(0).get("QLLX").toString();
            if (StringUtils.equals(qllx, Constants.GDQL_FWSYQ))
                cpt = Constants.GDQL_FWSYQ_CPT;
            else if (StringUtils.equals(qllx, Constants.GDQL_CF))
                cpt = Constants.GDQL_CF_CPT;
            else if (StringUtils.equals(qllx, Constants.GDQL_DY))
                cpt = Constants.GDQL_DY_CPT;
            else if (StringUtils.equals(qllx, Constants.GDQL_YG))
                cpt = Constants.GDQL_YG_CPT;
            else if (StringUtils.equals(qllx, Constants.GDQL_YY))
                cpt = Constants.GDQL_YY_CPT;
            else if (StringUtils.equals(qllx, Constants.GDQL_TDSYQ))
                cpt = Constants.GDQL_TDSYQ_CPT;
            else if (StringUtils.equals(qllx, Constants.GDQL_TDSYNQ))
                cpt = Constants.GDQL_TDSYNQ_CPT;
            model.addAttribute("qlid", qlid);
            if(StringUtils.equals(bdclx,"td")){
                List<GdBdcQlRel> bdcQlRelList=gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                if(CollectionUtils.isNotEmpty(bdcQlRelList)) {
                    model.addAttribute("tdid", bdcQlRelList.get(0).getBdcid());
                }
            }
        }
        model.addAttribute("cpt", cpt);
        if (StringUtils.isBlank(msgInfo)) {
            model.addAttribute("msgInfo", "null");
            msgInfo = "";
        } else {
            model.addAttribute("msgInfo", msgInfo);
            msgInfo = "";
        }
        if(StringUtils.isNotBlank(qrFlag)) {
            model.addAttribute("qrFlag",qrFlag);
        }
        return "/sjgl/gdxxlr";
    }

    @RequestMapping(value = "indexql", method = RequestMethod.GET)
    public String indexql(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdclx", required = false) String bdclx, @RequestParam(value = "editFlag", required = false) String editFlag,@RequestParam(value = "wiid", required = false) String wiid) {
        model.addAttribute("bdclx", bdclx);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("wiid", wiid);
        return "/query/ygdxx";
    }

    @RequestMapping("/gdQlFwTdInfo")
    public String gdQlFwTdInfo(Model model, @RequestParam(value = "infoId", required = false)String infoId, @RequestParam(value = "info", required = false)String info) {
        String cptName = StringUtils.EMPTY;
        if (StringUtils.equals(info, "QL")) {
            GdFwsyq gdFwsyq=entityMapper.selectByPrimaryKey(GdFwsyq.class,infoId);
            GdTdsyq gdTdsyq=entityMapper.selectByPrimaryKey(GdTdsyq.class,infoId);
            GdDy gddy = entityMapper.selectByPrimaryKey(GdDy.class, infoId);
            GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class,infoId);
            GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class,infoId);
            GdYy gdYy = entityMapper.selectByPrimaryKey(GdYy.class,infoId);
            if(gdFwsyq!=null){
                cptName = Constants.GDQL_FWSYQ_CPT;
                model.addAttribute("qllx",Constants.GDQL_FWSYQ);
            }
            if(gdTdsyq!=null){
                cptName = Constants.GDQL_TDSYNQ_CPT;
                model.addAttribute("qllx", Constants.GDQL_TDSYNQ);
            }
            if(gddy!=null){
                cptName = Constants.GDQL_DY_CPT;
                model.addAttribute("qllx", Constants.GDQL_DY);
            }
            if(gdCf!=null){
                cptName = Constants.GDQL_CF_CPT;
                model.addAttribute("qllx", Constants.GDQL_CF);
            }
            if(gdYg!=null){
                cptName = Constants.GDQL_YG_CPT;
                model.addAttribute("qllx", Constants.GDQL_YG);
            }
            if (gdYy!=null) {
                cptName = Constants.GDQL_YY_CPT;
                model.addAttribute("qllx", Constants.GDQL_YY);
            }
        }else if (StringUtils.equals(info, "FW")){
            cptName = Constants.GD_FW;
        } else if (StringUtils.equals(info, "TD")){
            cptName = Constants.GD_TD;
        }
        model.addAttribute("cptName", cptName);
        model.addAttribute("infoId", infoId);
        model.addAttribute("info", info);
        return "/query/gdQlFwTdInfo";
    }

    @ResponseBody
    @RequestMapping("/getGdFwJson")
    public Object getGdFwJson(Model model, Pageable pageable, Integer iszx, String hhSearch, String proid, String qlid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //混合查询
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        map.put(ParamsConstants.PROID_LOWERCASE, proid);
        map.put("qlid", qlid);
        return repository.selectPaging(PARAMETER_GETGDFWLRJSONBYPAGE, map, pageable);
    }

    /**
     * 获得过渡土地信息
     * @param model
     * @param pageable
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdTdJson")
    public Object getGdTdJson(Model model, Pageable pageable, String proid,String hhSearch,String qlid,String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(wiid)){
            HashMap param=new HashMap();
            param.put("wiid",wiid);
            List<BdcXmRel> list = bdcXmRelService.getBdcXmRelByYproidAndBdcdyh(param);
            if(CollectionUtils.isNotEmpty(list)){
                List<String> yproidList = new ArrayList<String>();
                for(int i=0;i<list.size();i++){
                    yproidList.add(list.get(i).getYproid());
                }
                map.put(ParamsConstants.PROIDS_LOWERCASE,yproidList);

            }
        }
        //混合查询
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        map.put("qlid", qlid);
        return repository.selectPaging("getGdTdLrJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/delFw")
    public String delFw(String fwid) {
        String msg = "失败";
        if (StringUtils.isNotBlank(fwid)) {
            entityMapper.deleteByPrimaryKey(GdFw.class, fwid);
            msg = "删除成功";
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/delTd")
    public String delTd(String tdid) {
        String msg = "失败";
        if (StringUtils.isNotBlank(tdid)) {
            entityMapper.deleteByPrimaryKey(GdTd.class, tdid);
            msg = "删除成功";
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/delQl")
    public String delQl(String qlid) {
        String msg = "失败";
        Object vo = new Object();
        if (StringUtils.isNotBlank(qlid)) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description  删除权利时删除查询表
             */
            entityMapper.deleteByPrimaryKey(GdFwQl.class,qlid);
            entityMapper.deleteByPrimaryKey(GdTdQl.class,qlid);
            String qllx = gdqlService.getQllxNameByQlid(qlid);
            if (StringUtils.isNotBlank(qllx)) {
                if (StringUtils.equals(qllx, Constants.GDQL_FWSYQ)) {
                    vo = new GdFwsyq();
                }else if (StringUtils.equals(qllx, Constants.GDQL_CF)) {
                    vo = new GdCf();
                }else if (StringUtils.equals(qllx, Constants.GDQL_DY)) {
                    vo = new GdDy();
                }else if (StringUtils.equals(qllx, Constants.GDQL_YG)) {
                    vo = new GdYg();
                }else if (StringUtils.equals(qllx, Constants.GDQL_YY)) {
                    vo = new GdYy();
                }else if (StringUtils.equals(qllx, Constants.GDQL_TDSYQ) || StringUtils.equals(qllx, Constants.GDQL_TDSYNQ)) {
                    vo = new GdTdsyq();
                }
                entityMapper.deleteByPrimaryKey(vo.getClass(), qlid);
                gdQlrService.delGdQlrByQlid(qlid);
                msg = "删除成功";
            }
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/getGdQlJson")
    public Object getGdQlJson(Model model, Pageable pageable, Integer iszx, String hhSearch, String proid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //混合查询
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        map.put(ParamsConstants.PROID_LOWERCASE, proid);
        return repository.selectPaging(PARAMETER_GETGDQLJSONBYPAGE, map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getGdqlCptUrl")
    public String getGdqlCptUrl(String qlid, String bdcid) {
        String cpt = "";
        String qllx = gdqlService.getQllxNameByQlid(qlid);
        if (StringUtils.isNotBlank(qllx)) {
            if (StringUtils.equals(qllx, Constants.GDQL_FWSYQ)) {
                cpt = Constants.GDQL_FWSYQ_CPT;
            }else if (StringUtils.equals(qllx, Constants.GDQL_CF)) {
                cpt = Constants.GDQL_CF_CPT;
            }else if (StringUtils.equals(qllx, Constants.GDQL_DY)) {
                cpt = Constants.GDQL_DY_CPT;
            }else if (StringUtils.equals(qllx, Constants.GDQL_YG)) {
                cpt = Constants.GDQL_YG_CPT;
            }else if (StringUtils.equals(qllx, Constants.GDQL_YY)) {
                cpt = Constants.GDQL_YY_CPT;
            }else if (StringUtils.equals(qllx, Constants.GDQL_TDSYQ)) {
                cpt = Constants.GDQL_TDSYQ_CPT;
            }else if (StringUtils.equals(qllx, Constants.GDQL_TDSYNQ)) {
                cpt = Constants.GDQL_TDSYNQ_CPT;
            }
        } else {
            cpt = Constants.GDQL_FWSYQ_CPT;
        }
        return reportUrl + "/ReportServer?reportlet=edit%2F" + cpt + ".cpt&op=write&bdcid=" + bdcid + "&qlid=" + qlid + "&ywType=server";
    }

    @ResponseBody
    @RequestMapping("/getGdqllx")
    public String getGdqllx(String qlid) {
        return gdqlService.getQllxNameByQlid(qlid);
    }

    @ResponseBody
    @RequestMapping("/getGdQlr")
    public String getGdQlr(String qlid) {
        String qlr = StringUtils.EMPTY;
        HashMap<String,String> map = gdFwService.getGdqlr(qlid);
        if(map != null){
            qlr = map.get("QLR");
        }
        return qlr;
    }

    @ResponseBody
    @RequestMapping("/glfw")
    public String glfw(String qlid, String fwids, String bdclx) {
        String msg = "失败";
        if (StringUtils.isNotBlank(qlid)&&StringUtils.isNotBlank(fwids)) {
            String[] strArray = fwids.split(",");
            if (strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    String fwid = strArray[i];
                    GdBdcQlRel gdBdcQlRel = new GdBdcQlRel();
                    HashMap map = new HashMap();
                    map.put("qlid", qlid);
                    map.put(ParamsConstants.BDCID_LOWERCASE, fwid);
                    List<GdBdcQlRel> gdBdcQlRelList = gdXmService.getGdBdcQlRelByQlidAndBdcId(map);
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (int j = 0; j < gdBdcQlRelList.size(); j++) {
                            entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRelList.get(j).getRelid());
                        }
                    }
                    gdBdcQlRel.setQlid(qlid);
                    gdBdcQlRel.setBdcid(fwid);
                    gdBdcQlRel.setRelid(UUIDGenerator.generate18());
                    gdBdcQlRel.setBdclx(bdclx);
                    entityMapper.saveOrUpdate(gdBdcQlRel, gdBdcQlRel.getRelid());
                }
                gdqlService.saveFwzlByQlid(qlid);
                msg = "关联成功";
            }
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "gltd")
    public String gltd(String qlid,String tdids,String bdclx){
        String msg = "失败";
        if (StringUtils.isNotBlank(qlid)&&StringUtils.isNotBlank(tdids)) {
            String[] strArray = tdids.split(",");
            if (strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    String tdid = strArray[i];
                    GdBdcQlRel gdBdcQlRel = new GdBdcQlRel();
                    HashMap map = new HashMap();
                    map.put("qlid", qlid);
                    map.put(ParamsConstants.BDCID_LOWERCASE, tdid);
                    List<GdBdcQlRel> gdBdcQlRelList = gdXmService.getGdBdcQlRelByQlidAndBdcId(map);
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (int j = 0; j < gdBdcQlRelList.size(); j++) {
                            entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRelList.get(j).getRelid());
                        }
                    }
                    gdBdcQlRel.setQlid(qlid);
                    gdBdcQlRel.setBdcid(tdid);
                    gdBdcQlRel.setRelid(UUIDGenerator.generate18());
                    gdBdcQlRel.setBdclx(bdclx);
                    entityMapper.saveOrUpdate(gdBdcQlRel, gdBdcQlRel.getRelid());

                    //关联土地时，将tdid,gd_td中的DJH写入gd_td_ql表；lcl
                    GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class,qlid);
                    if (null != gdTdQl){
                        gdTdQl.setTdid(tdid);
                        gdTdQl.setDjh(gdTdService.queryGdTd(tdid).getDjh());
                        entityMapper.saveOrUpdate(gdTdQl,gdTdQl.getQlid());
                    }
                }
                gdqlService.saveTdzlByQlid(qlid);
                msg = "关联成功";
            }
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/delglfw")
    public String delglfw(String qlid, String fwids) {
        String msg = "失败";
        if (StringUtils.isNotBlank(qlid)&&StringUtils.isNotBlank(fwids)) {
            String[] strArray = fwids.split(",");
            if (strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    String fwid = strArray[i];
                    HashMap map = new HashMap();
                    map.put("qlid", qlid);
                    map.put(ParamsConstants.BDCID_LOWERCASE, fwid);
                    List<GdBdcQlRel> gdBdcQlRelList = gdXmService.getGdBdcQlRelByQlidAndBdcId(map);
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (int j = 0; j < gdBdcQlRelList.size(); j++) {
                            entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRelList.get(j).getRelid());
                        }
                    }
                }
                gdqlService.saveFwzlByQlid(qlid);
                msg = "删除成功";
            }
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/getGdFwAllJson")
    public Object getGdFwAllJson(Model model, Pageable pageable, Integer iszx, String hhSearch, String proid, String qlid,String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(wiid)){
            HashMap param=new HashMap();
            param.put("wiid",wiid);
            List<BdcXmRel> list = bdcXmRelService.getBdcXmRelByYproidAndBdcdyh(param);
            if(CollectionUtils.isNotEmpty(list)){
                List<String> yproidList = new ArrayList<String>();
                for(int i=0;i<list.size();i++){
                    yproidList.add(list.get(i).getYproid());
                }
                map.put(ParamsConstants.PROIDS_LOWERCASE,yproidList);

            }
        }
        //混合查询
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        map.put("qlid", qlid);
        return repository.selectPaging(PARAMETER_GETGDFWLRJSONBYPAGE, map, pageable);
    }


    /**
     * 查询出匹配同一个不动产单元下的所有房屋信息（一个或多个）
     * @param model
     * @param pageable
     * @param iszx
     * @param hhSearch
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdQlJsonAll")
    public Object getGdQlJsonAll(Model model, Pageable pageable, Integer iszx, String hhSearch, String proid,String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(wiid)){
            List<String> yproidList = new ArrayList<String>();
            List<String> yGdCfQlidList = null;
            Boolean isUnPpSfcd = false;
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                BdcXm bdcXm = bdcXmList.get(0);
                if(StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_SFCD) && StringUtils.isBlank(bdcXm.getBdcdyid())){
                    isUnPpSfcd = true;
                    yGdCfQlidList = bdcXmRelService.getAllGdCfQlidListByBdcXmid(bdcXm.getProid());
                    if(CollectionUtils.isNotEmpty(yGdCfQlidList)){
                        map.put("qlids",yGdCfQlidList);
                    }
                }
            }
            if(!isUnPpSfcd){
                HashMap param=new HashMap();
                param.put("wiid",wiid);
                List<BdcXmRel> list = bdcXmRelService.getBdcXmRelByYproidAndBdcdyh(param);
                if(CollectionUtils.isNotEmpty(list)){
                    for(int i=0;i<list.size();i++){
                        yproidList.add(list.get(i).getYproid());
                    }
                    map.put(ParamsConstants.PROIDS_LOWERCASE,yproidList);
                }
            }
        }
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        return repository.selectPaging(PARAMETER_GETGDQLJSONBYPAGE, map, pageable);
    }

    @ResponseBody
    @RequestMapping(value = "delgltd")
    public String delgltd(String qlid, String tdids){
        String msg = "失败";
        if (StringUtils.isNotBlank(qlid)&&StringUtils.isNotBlank(tdids)) {
            String[] strArray = tdids.split(",");
            if (strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    String tdid = strArray[i];
                    HashMap map = new HashMap();
                    map.put("qlid", qlid);
                    map.put(ParamsConstants.BDCID_LOWERCASE, tdid);
                    List<GdBdcQlRel> gdBdcQlRelList = gdXmService.getGdBdcQlRelByQlidAndBdcId(map);
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (int j = 0; j < gdBdcQlRelList.size(); j++) {
                            entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRelList.get(j).getRelid());
                        }
                    }
                }
                gdqlService.saveTdzlByQlid(qlid);
                msg = "删除成功";
            }
        }
        return msg;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description      获得过渡房屋的土地信息
     */
    @ResponseBody
    @RequestMapping("/getGdFwTdJson")
    public Object getGdFwTdJson(Model model, Pageable pageable, String hhSearch, String fwids) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //混合查询
        List<String> tdidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(fwids)) {
            for (String fwid : fwids.split(",")){
                List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, fwid);
                if (CollectionUtils.isNotEmpty(gdDyhRelList)){
                    for (BdcGdDyhRel gdDyhRel : gdDyhRelList){
                        if (StringUtils .isNotBlank(gdDyhRel.getTdid())&&!tdidList.contains(gdDyhRel.getTdid())){
                            tdidList.add(gdDyhRel.getTdid());
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(tdidList)){
            map.put("tdids", tdidList);
        }else{
            tdidList.add("$$$");
            map.put("tdids", tdidList);
        }
        return repository.selectPaging("getGdTdLrJsonByPage", map, pageable);
    }

    /** * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @param
     * @rerutn
     * @description     获得过渡房屋土地的权利信息
     */
    @ResponseBody
    @RequestMapping("/getGdFwTdQlJson")
    public Object getGdFwTdQlJson(Model model, Pageable pageable, String hhSearch, String fwids) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> tdidList = new ArrayList<String>();
        List<String> proidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(fwids)) {
            for (String fwid : fwids.split(",")){
                List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null,fwid);
                if (CollectionUtils.isNotEmpty(gdDyhRelList)){
                    for (BdcGdDyhRel gdDyhRel : gdDyhRelList){
                        if (StringUtils .isNotBlank(gdDyhRel.getTdid())&&!tdidList.contains(gdDyhRel.getTdid())){
                            tdidList.add(gdDyhRel.getTdid());
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(tdidList)){
                for (String tdid :tdidList){
                    if(StringUtils.isNotBlank(tdid)){
                        List<GdTdsyq> gdTdsyqList = gdTdService.getGdTdsyqByTdid(tdid);
                        if (CollectionUtils.isNotEmpty(gdTdsyqList)){
                            for (GdTdsyq gdTdsyq : gdTdsyqList){
                                if (StringUtils.isNotBlank(gdTdsyq.getProid())&&!proidList.contains(gdTdsyq.getProid())){
                                    proidList.add(gdTdsyq.getProid());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(proidList)){
            map.put(ParamsConstants.PROIDS_LOWERCASE, proidList);
        }else{
            proidList.add("$$$");
            map.put(ParamsConstants.PROIDS_LOWERCASE, proidList);
        }
        return repository.selectPaging(PARAMETER_GETGDQLJSONBYPAGE, map, pageable);
    }
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description验证新增土地证号是否重复
     */
    @RequestMapping(value = "validateTdzh")
    @ResponseBody
    public Map validateTdzh(String tdzh){
        return gdTdService.validateTdzh(tdzh);
    }
}
