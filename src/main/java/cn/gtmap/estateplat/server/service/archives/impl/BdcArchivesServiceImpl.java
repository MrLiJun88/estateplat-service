package cn.gtmap.estateplat.server.service.archives.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.BdcCqGdQd;
import cn.gtmap.estateplat.server.model.BdcDyGdQd;
import cn.gtmap.estateplat.server.model.BdcZdGdlx;
import cn.gtmap.estateplat.server.service.archives.BdcArchivesService;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fr.third.org.apache.poi.hssf.record.formula.functions.Int;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/26
 * @description 昆山归档信息Service实现类
 */
@Service
public class BdcArchivesServiceImpl implements BdcArchivesService {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcGdxxMapper bdcGdxxMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdTdsyqMapper gdTdsyqMapper;
    @Autowired
    private GdFwsyqMapper gdFwsyqMapper;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcGdxxService bdcGdxxService;
    @Override
    public void initGdxxModel(Model model,String userid) {
        String jsonPath = "conf/server/gdlx.json";
        String jsonStr = ReadJsonFileUtil.readJsonFile(jsonPath);
        List<BdcZdGdlx> gdlxList = JSONArray.parseArray(jsonStr,BdcZdGdlx.class);
        model.addAttribute("userid",userid);
        model.addAttribute("gdlxList",gdlxList);
    }

    @Override
    public void initTcGdxxModel(Model model, String userid,String slbh) {
        List<Map> gdlxList = bdcZdGlService.getBdcZdGdlx();
        model.addAttribute("userid",userid);
        model.addAttribute("gdlxList",gdlxList);
        model.addAttribute("slbh",slbh);
    }

    @Override
    public Page getGdxxJson(String cqzh, String dyzmh, String dah, String slbhs,String gdlx,String cxlx,Pageable pageable, Boolean gdByProidFlag) {
        HashMap map = new HashMap();
        Page<LinkedHashMap> dataPaging = null;
        //区分按流程归档和按项目归档的流程
        String sqlxArrayStr = AppConfig.getProperty("bdcGdxx.mul.sqlxdm");
        String pfUserName = AppConfig.getProperty("egov.db.username");
        if (StringUtils.isNotBlank(sqlxArrayStr)){
            String[] sqlxArray = sqlxArrayStr.split(",");
            map.put("sqlxArray",sqlxArray);
        }
        if (StringUtils.isNotBlank(cqzh)){
            map.put("cqzh",cqzh);
        }
        if (StringUtils.isNotBlank(dyzmh)){
            map.put("dyzmh",dyzmh);
        }
        if(StringUtils.isNotBlank(dah)){
            map.put("dah",dah);
        }
        if(StringUtils.isNotBlank(gdlx)){
            map.put("gdlx",gdlx);
        }
        if (StringUtils.isNotBlank(cxlx)) {
            if (StringUtils.equals(cxlx,"产权")) {
                map.put("cxlx",Constants.SQLX_CQ);
            } else if (StringUtils.equals(cxlx,"抵押权")) {
                map.put("cxlx",Constants.SQLX_DYAQ);
            }
        }
        if (StringUtils.isNotBlank(slbhs)){
            String[] slbhArray = slbhs.split(",");
            map.put("slbhArray",slbhArray);
            if (gdByProidFlag){
                List<String> proidList = new ArrayList<String>();
                for (String slbhTemp:slbhArray){
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(slbhTemp);
                    if (CollectionUtils.isNotEmpty(bdcXmList)){
                        for (BdcXm bdcXmTemp:bdcXmList){
                            proidList.add(bdcXmTemp.getProid());
                        }
                    }
                }
                map.put("slbhDecodeSortArray",proidList);
            }else{
                List<String> wiidList = new ArrayList<String>();
                for (String slbhTemp:slbhArray){
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(slbhTemp);
                    if (CollectionUtils.isNotEmpty(bdcXmList)){
                        wiidList.add(bdcXmList.get(0).getWiid());
                    }
                }
                if(CollectionUtils.isNotEmpty(wiidList)){
                    map.put("slbhDecodeSortArray",wiidList);
                }
            }
        }
        if(StringUtils.isNotBlank(pfUserName)){
            map.put("pfUserName",pfUserName);
        }
        if (map.size() > 0){
            //昆山和高新区查询分开,高新区受理编号和证号支持模糊搜索
            String flag = AppConfig.getProperty("slbhAndZh.search.fuzzy");
            if (StringUtils.equals(flag,ParamsConstants.TRUE_LOWERCASE)){
                //按流程归档和按项目归档区分开
                if (gdByProidFlag){
                    dataPaging = repository.selectPaging("getBdcGdxxProidByPage",map, pageable);
                }else{
                    dataPaging = repository.selectPaging("getBdcGdxxWiidByPage",map, pageable);
                }
            }else{
                //按流程归档和按项目归档区分开
                if (gdByProidFlag){
                    dataPaging = repository.selectPaging("getBdcGdxxMulByPage",map, pageable);
                }else{
                    dataPaging = repository.selectPaging("getBdcGdxxByPage",map, pageable);
                }
            }

        }
        return dataPaging;
    }

    @Override
    public Page getTcGdxxJson(String slbhs, Pageable pageable) {
        HashMap map = new HashMap();
        Page<LinkedHashMap> dataPaging = null;
        String pfUserName = AppConfig.getProperty("egov.db.username");
        if (StringUtils.isNotBlank(slbhs)){
            String[] slbhArray = StringUtils.deleteWhitespace(slbhs).split(",");
            map.put("slbhArray",slbhArray);
            List<String> wiidList = new ArrayList<String>();
            for (String slbhTemp:slbhArray){
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(slbhTemp);
                if (CollectionUtils.isNotEmpty(bdcXmList)){
                    wiidList.add(bdcXmList.get(0).getWiid());
                }
            }
            map.put("slbhDecodeSortArray",wiidList);
        }
        if(StringUtils.isNotBlank(pfUserName)){
            map.put("pfUserName",pfUserName);
        }
        if (map.size() > 0){
            dataPaging = repository.selectPaging("getBdcGdxxByPage",map, pageable);
        }
        return dataPaging;
    }

    @Override
    public String getGdxxQlrMc(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        String qlrMc = null;
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            String proid = bdcXmList.get(0).getProid();
            List<BdcQlr> bdcQlrist = null;
            if (CollectionUtils.size(bdcXmList) > 1){
                bdcQlrist = bdcQlrService.queryBdcQlrByProid(proid);
                qlrMc = bdcQlrService.combinationQlr(bdcQlrist);
                StringBuilder sb = new StringBuilder("");
                qlrMc = sb.append(qlrMc).append("等").toString();
            }else{
                bdcQlrist = bdcQlrService.queryBdcQlrByProid(proid);
                qlrMc = bdcQlrService.combinationQlr(bdcQlrist);
            }
        }
        return qlrMc;
    }

    @Override
    public String bdcGd(String xmids,String gdlx, String userid) {
        String result = null;
        //盒号和档案号生成采用同步锁避免并发生成错误档案号
        if (StringUtils.isNotBlank(xmids)){
            //产权和抵押需要获取盒号，同一批次使用同一个盒号
            String hh = null;
            synchronized (this){
                hh = getHhByGdlx(gdlx);
            }
            String[] xmidsArray = xmids.split(",");
            HashSet<String> wiidSet = new HashSet<String>();
            for (String xmid: xmidsArray){
                synchronized (this){
                    String resultTemp= dealGdxx(xmid,gdlx,hh,userid,null);
                    if (StringUtils.isNotBlank(resultTemp)) {
                        if (StringUtils.isNotBlank(result)) {
                            result += "," + resultTemp;
                        } else {
                            result = "所选流程已归档，请检查，编号为：" + resultTemp;
                        }
                    }
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(xmid);
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                        wiidSet.add(bdcXm.getWiid());
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(wiidSet)){
                for(String wiid : wiidSet){
                    if(StringUtils.isNotBlank(wiid)){
                        bdcGdxxService.updateJyZtByWiid(wiid,userid);
                    }
                }
            }
        }
        if (StringUtils.isBlank(result)) {
            result = "归档成功";
        }
        return result;
    }

    @Override
    public String tcBdcGd(String xmids, String gdlx, String userid) {
        String result = null;
        if(StringUtils.isNotBlank(xmids)){
            String[] xmidsArray = xmids.split(",");
            HashSet<String> wiidSet = new HashSet<String>();
            for (String xmid: xmidsArray){
                synchronized (this){
                    result= dealTcGdxx(xmid,gdlx,userid);
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(xmid);
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                        wiidSet.add(bdcXm.getWiid());
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(wiidSet)){
                for(String wiid : wiidSet){
                    if(StringUtils.isNotBlank(wiid)){
                        bdcGdxxService.updateJyZtByWiid(wiid,userid);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String bdcArchives(String xmids, String gdlx, String userid) {
        String result = null;
        synchronized (this){
            if (StringUtils.isNotBlank(xmids) && StringUtils.isNotBlank(gdlx)){
                //获取配置的分类字母
                String jsonPath = "conf/server/gdlx.json";
                String jsonStr = ReadJsonFileUtil.readJsonFile(jsonPath);
                List<BdcZdGdlx> gdlxList = JSONArray.parseArray(jsonStr,BdcZdGdlx.class);
                String classiFieldLetters = null;
                if (CollectionUtils.isNotEmpty(gdlxList)){
                   for (BdcZdGdlx bdcZdGdlx: gdlxList){
                       if (StringUtils.equals(bdcZdGdlx.getDm(),gdlx)){
                           classiFieldLetters = bdcZdGdlx.getBzf();
                           break;
                       }
                   }
                }
                String[] xmidArray = xmids.split(",");
                HashSet<String> wiidSet = new HashSet<String>();
                for (String xmid: xmidArray){
                    result = dealBdcArchives(xmid,gdlx,userid,classiFieldLetters);
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(xmid);
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                        wiidSet.add(bdcXm.getWiid());
                    }
                }
                if(CollectionUtils.isNotEmpty(wiidSet)){
                    for(String wiid : wiidSet){
                        if(StringUtils.isNotBlank(wiid)){
                            bdcGdxxService.updateJyZtByWiid(wiid,userid);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<BdcGdxx> getBdcGdxxByDah(String dah,String gdlx) {
        List<BdcGdxx> bdcGdxxList = null;
        if (StringUtils.isNotBlank(dah)){
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("dah",dah).andEqualTo("gdlx",gdlx);
            bdcGdxxList = entityMapper.selectByExample(example);
        }
        return bdcGdxxList;
    }

    @Override
    public List<BdcGdxx> getBdcGdxxByHh(String hh, String gdlx) {
        List<BdcGdxx> bdcGdxxList = null;
        if (StringUtils.isNotBlank(hh)){
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("hh",hh).andEqualTo("gdlx",gdlx);
            bdcGdxxList = entityMapper.selectByExample(example);
        }
        return bdcGdxxList;
    }

    @Override
    public String getBdcGdxxLcMc(String wiid) {
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
        String lcMc = null;
        if (pfWorkFlowInstanceVo != null){
            HashMap map = new HashMap();
            map.put("wdid",pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            List<HashMap> sqlxList = bdcZdGlService.getBdcZdSqlx(map);
            if(CollectionUtils.isNotEmpty(sqlxList)){
                lcMc = sqlxList.get(0).get("MC").toString();
            }
        }
        return lcMc;
    }

    @Override
    public List<BdcGdxx> getBdcGdxxByXmid(String xmid) {
        List<BdcGdxx> bdcGdxxList = null;
        if(StringUtils.isNotBlank(xmid)){
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("xmid",xmid);
            bdcGdxxList = entityMapper.selectByExample(example);
        }
        return bdcGdxxList;
    }

    @Override
    public String delGdxxByXmids(String xmids) {
        String result = null;
        if (StringUtils.isNotBlank(xmids)){
            String[] xmidArray = xmids.split(",");
            bdcGdxxMapper.delGdxxByXmid(xmidArray);
            result = ParamsConstants.SUCCESS_LOWERCASE;
        }
        return result;
    }

    @Override
    public Map getDahAndHhByXmid(String xmid) {
        HashMap map = new HashMap();
        map.put("dah","");
        map.put("hh","");
        List<BdcGdxx> bdcGdxxList = getBdcGdxxByXmid(xmid);
        if (CollectionUtils.isNotEmpty(bdcGdxxList)){
            if(StringUtils.isNotBlank(bdcGdxxList.get(0).getDah())){
                map.put("dah",bdcGdxxList.get(0).getDah());
            }
            if (StringUtils.isNotBlank(bdcGdxxList.get(0).getHh())){
                map.put("hh",bdcGdxxList.get(0).getHh());
            }
            if(StringUtils.isNotBlank(bdcGdxxList.get(0).getGdr())){
                map.put("gdr",bdcGdxxList.get(0).getGdr());
            }
            if(bdcGdxxList.get(0).getGdrq() != null){
                map.put("gdrq",CalendarUtil.formatDateTime(bdcGdxxList.get(0).getGdrq()));
            }
            if(StringUtils.isNotBlank(bdcGdxxList.get(0).getMlh())){
                map.put("mlh",bdcGdxxList.get(0).getMlh());
            }
            if(StringUtils.isNotBlank(bdcGdxxList.get(0).getAjh())){
                map.put("ajh",bdcGdxxList.get(0).getAjh());
            }
        }
        return map;
    }

    @Override
    public String saveGdxxDahAndHh(String xmid,String dah, String hh, String yzDah) {
        String result = null;
        String exitDahFlag = ParamsConstants.FALSE_LOWERCASE;
        //库里如果有该xmid的归档信息，先删除该条记录，再验证dah是否重复，然后插入保存后的数据
        List<BdcGdxx> bdcGdxxList = getBdcGdxxByXmid(xmid);
        if (CollectionUtils.isNotEmpty(bdcGdxxList)){
            deleteBdcGdxxByXmid(xmid);
            if (StringUtils.equals(yzDah, ParamsConstants.TRUE_LOWERCASE)) {
                exitDahFlag = validateExitDah(dah);
            }
            if (StringUtils.equals(exitDahFlag,ParamsConstants.TRUE_LOWERCASE)){
                result = "所编辑档案号存在重复，请检查！";
            }
            if (StringUtils.equals(exitDahFlag,ParamsConstants.FALSE_LOWERCASE)){
                BdcGdxx bdcGdxxTemp = bdcGdxxList.get(0);
                bdcGdxxTemp.setDah(dah);
                bdcGdxxTemp.setDaid(dah);
                bdcGdxxTemp.setHh(hh);
                entityMapper.saveOrUpdate(bdcGdxxTemp,bdcGdxxTemp.getGdxxid());
                result = ParamsConstants.SUCCESS_LOWERCASE;
            }
        }else{
            result = "该项目未归档，请归档后修改";
        }
        return result;
    }

    @Override
    public void exportCqGdQd(List<BdcCqGdQd> list, HttpServletResponse response) {
        //excel标题
        String[] title = {"序号","档案号","盒号","产权证号","产权人","坐落"};
        //excel文件名
        String fileName = CalendarUtil.getTimeMs()+".xls";
        //sheet名
        String sheetName = "不动产产权归档清单";
        String[][] content = new String[list.size()][title.length];

        for (int i = 0; i < list.size(); i++) {
            content[i][0] = list.get(i).getXh();
            content[i][1] = list.get(i).getDah();
            content[i][2] = list.get(i).getHh();
            content[i][3] = list.get(i).getCqzh();
            content[i][4] = list.get(i).getCqr();
            content[i][5] = list.get(i).getZl();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        wb.getSheet(sheetName).setColumnWidth(0,10*256);
        wb.getSheet(sheetName).setColumnWidth(1,18*256);
        wb.getSheet(sheetName).setColumnWidth(2,18*256);
        wb.getSheet(sheetName).setColumnWidth(3,40*256);
        wb.getSheet(sheetName).setColumnWidth(4,20*256);
        wb.getSheet(sheetName).setColumnWidth(5,40*256);
        String outputPath = AppConfig.getProperty("excel.bdcCqGdQd.path");
        ExcelUtil.downloadFileToClient(outputPath,fileName,wb,response);
    }

    @Override
    public List<BdcCqGdQd> getBdcCqGdQdListByWiids(String wiids) {
        List<BdcCqGdQd> bdcCqGdQdList =  new ArrayList<BdcCqGdQd>();
        if(StringUtils.isNotBlank(wiids)){
            String[] wiidArray = wiids.split(",");
            String wiid = null;
            for (int i = 0;i<wiidArray.length;i++){
                wiid = wiidArray[i];
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                if (CollectionUtils.isNotEmpty(bdcXmList)){
                    BdcCqGdQd bdcCqGdQd = new BdcCqGdQd();
                    String proid = bdcXmList.get(0).getProid();
                    String zl = null;
                    String qlrMc = null;
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                    qlrMc = bdcQlrService.combinationQlr(bdcQlrList);
                    if(bdcSpxx != null){
                        zl = bdcSpxx.getZl();
                    }
                    String bdccqzh = bdcZsService.getCombineBdcqzhByProid(proid);
                    if (CollectionUtils.size(bdcXmList) >1 ){
                        StringBuilder zlBuilder = new StringBuilder("");
                        zl = zlBuilder.append(zl).append("等").toString();
                        StringBuilder bdcqzhBuilder = new StringBuilder("");
                        bdccqzh = bdcqzhBuilder.append(bdccqzh).append("等").toString();
                        StringBuilder qlrMcBuilder = new StringBuilder("");
                        qlrMc = qlrMcBuilder.append(qlrMc).append("等").toString();
                    }
                    //根据主proid去查bdc_gdxx
                    String wfProid = PlatformUtil.getPfProidByWiid(wiid);
                    List<BdcGdxx> bdcGdxxList = getBdcGdxxByXmid(wfProid);
                    if (CollectionUtils.isNotEmpty(bdcGdxxList)){
                        bdcCqGdQd.setDah(bdcGdxxList.get(0).getDah());
                        bdcCqGdQd.setHh(bdcGdxxList.get(0).getHh());
                    }
                    bdcCqGdQd.setCqr(qlrMc);
                    bdcCqGdQd.setCqzh(bdccqzh);
                    bdcCqGdQd.setWiid(wiid);
                    bdcCqGdQd.setZl(zl);
                    bdcCqGdQd.setXh(String.valueOf(i+1));
                    bdcCqGdQdList.add(bdcCqGdQd);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(bdcCqGdQdList)){
            for (int j =0;j<bdcCqGdQdList.size();j++){
                bdcCqGdQdList.get(j).setXh(String.valueOf(j+1));
            }
        }
        return bdcCqGdQdList;
    }

    @Override
    public List<BdcDyGdQd> getBdcDyGdQdListByWiids(String wiids) {
        List<BdcDyGdQd> bdcDyGdQdList = new ArrayList<BdcDyGdQd>();
        if (StringUtils.isNotBlank(wiids)){
            String[] wiidArray = wiids.split(",");
            String wiid = null;
            for(int i =0;i<wiidArray.length;i++){
                wiid = wiidArray[i];
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                if (CollectionUtils.isNotEmpty(bdcXmList)){
                    BdcDyGdQd bdcDyGdQd = new BdcDyGdQd();
                    String proid = bdcXmList.get(0).getProid();
                    BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
                    String zl = null;
                    String dyqr = null;
                    String cqr = null;
                    String cqzh = null;
                    String dyzmh = bdcZsService.getCombineBdcqzhByProid(proid);
                    dealDyGdQdCqrAndCqzh(bdcDyGdQd,bdcXmTemp);
                    cqzh = bdcDyGdQd.getCqzh();
                    cqr = bdcDyGdQd.getCqr();
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getZl())){
                        zl = bdcSpxx.getZl();
                    }
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                    dyqr = bdcQlrService.combinationQlr(bdcQlrList);
                    if (CollectionUtils.size(bdcXmList) > 1){
                        StringBuilder zlBuilder = new StringBuilder("");
                        zl = zlBuilder.append(zl).append("等").toString();
                        StringBuilder cqzhBuilder = new StringBuilder("");
                        cqzh = cqzhBuilder.append(cqzh).append("等").toString();
                        StringBuilder dyzmhBuilder = new StringBuilder("");
                        dyzmh = dyzmhBuilder.append(dyzmh).append("等").toString();
                        StringBuilder cqrBuilder = new StringBuilder("");
                        cqr = cqrBuilder.append(cqr).append("等").toString();
                        StringBuilder dyqrBuilder = new StringBuilder("");
                        dyqr = dyqrBuilder.append(dyqr).append("等").toString();

                    }
                    bdcDyGdQd.setZl(zl);
                    bdcDyGdQd.setCqzh(cqzh);
                    bdcDyGdQd.setTxqzh(dyzmh);
                    bdcDyGdQd.setCqr(cqr);
                    bdcDyGdQd.setDyqr(dyqr);
                    bdcDyGdQd.setWiid(wiid);
                    //根据主proid获取归档信息
                    String wfProid = PlatformUtil.getPfProidByWiid(wiid);
                    List<BdcGdxx> bdcGdxxList = getBdcGdxxByXmid(wfProid);
                    if(CollectionUtils.isNotEmpty(bdcGdxxList)){
                        bdcDyGdQd.setDah(bdcGdxxList.get(0).getDah());
                        bdcDyGdQd.setHh(bdcGdxxList.get(0).getHh());
                    }
                    bdcDyGdQdList.add(bdcDyGdQd);
                }
            }
            if (CollectionUtils.isNotEmpty(bdcDyGdQdList)){
                for (int j =0;j<bdcDyGdQdList.size();j++){
                    bdcDyGdQdList.get(j).setXh(String.valueOf(j+1));
                }
            }
        }
        return bdcDyGdQdList;
    }

    @Override
    public List<BdcCqGdQd> getBdcCqGdQdListByProids(String proids,String qsNumber,String jsNumber) {
        List<BdcCqGdQd> bdcCqGdQdList = null;
        if (StringUtils.isNotBlank(proids)){
            String[] proidArray = proids.split(",");
            bdcCqGdQdList = new ArrayList<BdcCqGdQd>();
            String proid = null;
            for (int i = 0;i< proidArray.length;i++){
                proid = proidArray[i];
                BdcCqGdQd bdcCqGdQd = new BdcCqGdQd();
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                if (bdcSpxx != null){
                    bdcCqGdQd.setZl(bdcSpxx.getZl());
                }
                bdcCqGdQd.setCqr(bdcQlrService.combinationQlr(bdcQlrService.queryBdcQlrByProid(proid)));
                bdcCqGdQd.setCqzh(bdcZsService.getCombineBdcqzhByProid(proid));
                bdcCqGdQd.setWiid(proid);
                bdcCqGdQd.setXh(String.valueOf(i+1));
                List<BdcGdxx> bdcGdxxList = getBdcGdxxByXmid(proid);
                if (CollectionUtils.isNotEmpty(bdcGdxxList)){
                    bdcCqGdQd.setDah(bdcGdxxList.get(0).getDah());
                    bdcCqGdQd.setHh(bdcGdxxList.get(0).getHh());
                }
                bdcCqGdQdList.add(bdcCqGdQd);
            }
        }
        if(CollectionUtils.isNotEmpty(bdcCqGdQdList)){
            for (int j =0;j<bdcCqGdQdList.size();j++){
                bdcCqGdQdList.get(j).setXh(String.valueOf(j+1));
            } 
        }
        return bdcCqGdQdList;
    }

    @Override
    public List<BdcDyGdQd> getBdcDyGdQdListByProids(String proids,String qsNumber,String jsNumber) {
        List<BdcDyGdQd> bdcDyGdQdList = null;
        Integer qs = null;
        Integer js = null;
        if (StringUtils.isNotBlank(proids)){
            String[] proidArray = proids.split(",");
            bdcDyGdQdList = new ArrayList<BdcDyGdQd>();
            String proid = null;

            List<String> proidList = new ArrayList<String>();
            if(StringUtils.isNotBlank(qsNumber) && StringUtils.isNotBlank(jsNumber)){
                qs = Integer.parseInt(qsNumber) -1;
                js = Integer.parseInt(jsNumber) -1;
                for(int i = qs;i <= js; i++){
                    if((i + 1) <= proidArray.length){
                        proidList.add(proidArray[i]);
                    }
                }
            }else{
                proidList = Arrays.asList(proidArray);
            }
            if(CollectionUtils.isNotEmpty(proidList)){
                for (int i = 0;i < proidList.size(); i++){
                    BdcDyGdQd bdcDyGdQd = new BdcDyGdQd();
                    proid = proidList.get(i);
                    BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
                    dealDyGdQdCqrAndCqzh(bdcDyGdQd,bdcXmTemp);
                    bdcDyGdQd.setWiid(proid);
                    bdcDyGdQd.setTxqzh(bdcZsService.getCombineBdcqzhByProid(proid));
                    bdcDyGdQd.setDyqr(bdcQlrService.combinationQlr(bdcQlrService.queryBdcQlrByProid(proid)));
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    if(bdcSpxx != null){
                        bdcDyGdQd.setZl(bdcSpxx.getZl());
                    }
                    List<BdcGdxx> bdcGdxxList = getBdcGdxxByXmid(proid);
                    if(CollectionUtils.isNotEmpty(bdcGdxxList)){
                        bdcDyGdQd.setDah(bdcGdxxList.get(0).getDah());
                        bdcDyGdQd.setHh(bdcGdxxList.get(0).getHh());
                    }
                    bdcDyGdQdList.add(bdcDyGdQd);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(bdcDyGdQdList)){
            if(qs != null && js != null){
                for (int j =0;j<bdcDyGdQdList.size();j++){
                    qs++;
                    bdcDyGdQdList.get(j).setXh(String.valueOf(qs));
                }
            }else{
                for (int j =0;j<bdcDyGdQdList.size();j++){
                    bdcDyGdQdList.get(j).setXh(String.valueOf(j+1));
                }
            }
        }
        return bdcDyGdQdList;
    }

    @Override
    public void exportDyGdQd(List<BdcDyGdQd> list, HttpServletResponse response) {
        //excel标题
        String[] title = {"序号","档案号","盒号","他项权证号","抵押权人","坐落","产权证号","产权人"};
        //excel文件名
        String fileName = CalendarUtil.getTimeMs()+".xls";
        //sheet名
        String sheetName = "不动产抵押归档清单";
        String[][] content = new String[list.size()][title.length];

        for (int i = 0; i < list.size(); i++) {
            content[i][0] = list.get(i).getXh();
            content[i][1] = list.get(i).getDah();
            content[i][2] = list.get(i).getHh();
            content[i][3] = list.get(i).getTxqzh();
            content[i][4] = list.get(i).getDyqr();
            content[i][5] = list.get(i).getZl();
            content[i][6] = list.get(i).getCqzh();
            content[i][7] = list.get(i).getCqr();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        wb.getSheet(sheetName).setColumnWidth(0,10*256);
        wb.getSheet(sheetName).setColumnWidth(1,18*256);
        wb.getSheet(sheetName).setColumnWidth(2,18*256);
        wb.getSheet(sheetName).setColumnWidth(3,40*256);
        wb.getSheet(sheetName).setColumnWidth(4,40*256);
        wb.getSheet(sheetName).setColumnWidth(5,40*256);
        wb.getSheet(sheetName).setColumnWidth(6,40*256);
        wb.getSheet(sheetName).setColumnWidth(7,40*256);
        String outputPath = AppConfig.getProperty("excel.bdcDyGdQd.path");
        ExcelUtil.downloadFileToClient(outputPath,fileName,wb,response);
    }

    @Override
    public String getGdXmlStr(String xmids, String gdlx, Boolean gdByProid,String qsNumber,String jsNumber) {
        List<Map> mapList = new ArrayList<Map>();
        Map<String,Object> map = null;
        //组织产权归档清单和抵押归档清单xml
        if (StringUtils.equals(gdlx, ParamsConstants.GDLX_CQ_DM)){
            List<BdcCqGdQd> bdcCqGdQdList = null;
            //通过gdByProid标识按项目组织数据还是按流程组织数据
            if(gdByProid){
                bdcCqGdQdList = getBdcCqGdQdListByProids(xmids,qsNumber,jsNumber);
            }else{
                bdcCqGdQdList = getBdcCqGdQdListByWiids(xmids);
            }
            String bdcCqGdQdJson = null;
            if (CollectionUtils.isNotEmpty(bdcCqGdQdList)){
                for (BdcCqGdQd bdcCqGdQd: bdcCqGdQdList){
                    bdcCqGdQdJson = JSONObject.toJSONString(bdcCqGdQd, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty);
                    map = (Map<String, Object>) JSON.parse(bdcCqGdQdJson);
                    mapList.add(map);
                }
            }
        }else if(StringUtils.equals(gdlx,ParamsConstants.GDLX_DY_DM)){
            List<BdcDyGdQd> bdcDyGdQdList = null;
            if(gdByProid){
                bdcDyGdQdList = getBdcDyGdQdListByProids(xmids,qsNumber,jsNumber);
            }else{
                bdcDyGdQdList = getBdcDyGdQdListByWiids(xmids);
            }
            String bdcDyGdQdJson = null;
            if (CollectionUtils.isNotEmpty(bdcDyGdQdList)){
                for (BdcDyGdQd bdcDyGdQd:bdcDyGdQdList){
                    bdcDyGdQdJson = JSONObject.toJSONString(bdcDyGdQd, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty);
                    map = (Map<String, Object>) JSON.parse(bdcDyGdQdJson);
                    mapList.add(map);
                }
            }
        }
        return CxmlUtils.listToXmlstr(mapList);
    }

    @Override
    public String validateExitDah(String dah) {
        String msg = null;
        if(StringUtils.isNotBlank(dah)) {
            msg = ParamsConstants.FALSE_LOWERCASE;
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("dah",dah);
            List<BdcGdxx> bdcGdxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)){
                msg = ParamsConstants.TRUE_LOWERCASE;
            }
        }
        return msg;
    }

    @Override
    public Map asyncGetBhAndSpxx(String wiid) {
        Map map = new HashMap();
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)){
                String proid = bdcXmList.get(0).getProid();
                StringBuilder bdcdyhBuilder =new StringBuilder("");
                StringBuilder zlBuilder = new StringBuilder("");
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                if (bdcSpxx != null){
                    bdcdyhBuilder = bdcdyhBuilder.append(bdcSpxx.getBdcdyh());
                    zlBuilder = zlBuilder.append(bdcSpxx.getZl());
                }
                if (CollectionUtils.size(bdcXmList) > 1){
                    map.put("bh",bdcXmList.get(0).getBh());
                    map.put(ParamsConstants.BDCDYH_LOWERCASE,bdcdyhBuilder.append("等").toString());
                    map.put("zl",zlBuilder.append("等").toString());
                }
                else{
                    map.put("bh",bdcXmList.get(0).getBh());
                    map.put(ParamsConstants.BDCDYH_LOWERCASE,bdcdyhBuilder.toString());
                    map.put("zl",zlBuilder.toString());
                }
            }
        return map;
    }

    @Override
    public Map getQsDahAndJsDah(String gdlx, String xmids) {
        HashMap map = new HashMap();
        map.put("gdlx",gdlx);
        if( StringUtils.isNotBlank(xmids)){
            String[] xmidArray = xmids.split(",");
            map.put("xmidArray",xmidArray);
        }
        List<String> dahList = bdcGdxxMapper.getBdcGdxxDahList(map);
        Map resultMap = new HashMap();
        if(CollectionUtils.isNotEmpty(dahList)){
            resultMap.put("qsdah",dahList.get(0));
            resultMap.put("jsdah",dahList.get(dahList.size()-1));
        }
        return resultMap;
    }

    @Override
    public Map getRestOfGdxx(String wiid) {
        Map map = new HashMap();
        map.put("qlrMc",getGdxxQlrMc(wiid));
        map.put("lcmc",getBdcGdxxLcMc(wiid));
        map.put("cqzh",getBdcCqzhByWiid(wiid));
        map.putAll(asyncGetBhAndSpxx(wiid));
        map.putAll(getDahAndHhByXmid(transferWiidsToProids(wiid)));
        return map;
    }

    @Override
    public Map getRestOfGdxxMul(String proid) {
        Map map = new HashMap();
        if (StringUtils.isNotBlank(proid)){
            List<BdcQlr> bdcQlrist = bdcQlrService.queryBdcQlrByProid(proid);
            String qlrMc = bdcQlrService.combinationQlr(bdcQlrist);
            map.put("qlrMc",qlrMc);
            map.put("cqzh",bdcZsService.getCombineBdcqzhByProid(proid));
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh())){
                map.put("bh",bdcXm.getBh());
                if (StringUtils.isNotBlank(bdcXm.getWiid())){
                    map.put("lcmc",getBdcGdxxLcMc(bdcXm.getWiid()));
                }
            }
            if (bdcSpxx !=null){
                map.put("zl",bdcSpxx.getZl());
                map.put(ParamsConstants.BDCDYH_LOWERCASE,bdcSpxx.getBdcdyh());
            }
            map.putAll(getDahAndHhByXmid(proid));
        }
        return map;
    }

    @Override
    public String getBdcCqzhByWiid(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        String cqzh = null;
        if (CollectionUtils.isNotEmpty(bdcXmList)){
            cqzh = bdcZsService.getCombineBdcqzhByProid(bdcXmList.get(0).getProid());
            if (CollectionUtils.size(bdcXmList) > 1){
                StringBuilder cqzhBuilder = new StringBuilder("");
                cqzh = cqzhBuilder.append(cqzh).append("等").toString();
            }
        }
        return cqzh;
    }

    @Override
    public void deleteBdcGdxxByXmid(String xmid) {
        Example example = new Example(BdcGdxx.class);
        example.createCriteria().andEqualTo("xmid",xmid);
        entityMapper.deleteByExample(BdcGdxx.class,example);
    }

    @Override
    public String validateDahExist(String xmids) {
        String result = null;
        if (StringUtils.isNotBlank(xmids)){
            result = ParamsConstants.FALSE_LOWERCASE;
            String[] xmidArray = xmids.split(",");
            for (String xmid :xmidArray){
                List<BdcGdxx> bdcGdxxList = getBdcGdxxByXmid(xmid);
                if (CollectionUtils.isNotEmpty(bdcGdxxList)){
                    result = ParamsConstants.TRUE_LOWERCASE;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public String reBdcArchive(String xmids, String gdlx, String userid) {
        String result = null;
        if (StringUtils.isNotBlank(xmids) && StringUtils.isNotBlank(gdlx)) {
            String[] xmidArray = xmids.split(",");
            //删除归档相关信息
            bdcGdxxMapper.delGdxxByXmid(xmidArray);
            result = bdcArchives(xmids,gdlx,userid);
        }
        return result;
    }

    @Override
    public String transferWiidsToProids(String wiids) {
        String wfProids = null;
        if (StringUtils.isNotBlank(wiids)){
            String[] wiidArray = wiids.split(",");
            StringBuilder wfProidBuilder = new StringBuilder("");
            String wfProidTemp =null;
            for (String wiid: wiidArray){
                wfProidTemp = PlatformUtil.getPfProidByWiid(wiid);
                if (wfProidBuilder.length() == 0){
                    wfProidBuilder.append(wfProidTemp);
                }else{
                    wfProidBuilder.append(",").append(wfProidTemp);
                }
            }
            wfProids = wfProidBuilder.toString();
        }
        return wfProids;
    }

    @Override
    public String bdcGdMulFlow(String xmids, String gdlx, String userid) {
        String result = null;
        //盒号和档案号生成采用同步锁避免并发生成错误档案号
        if (StringUtils.isNotBlank(xmids)){
            //产权和抵押需要获取盒号，同一批次使用同一个盒号
            String hh = null;
            synchronized (this){
                hh = getHhByGdlx(gdlx);
            }
            String[] xmidsArray = xmids.split(",");
            //只需要一个档案号
            String dahTemp = null;
            synchronized (this){
                dahTemp = getDahByGdlx(gdlx);
            }
            for (String xmid: xmidsArray){
                result = dealGdxx(xmid,gdlx,hh,userid,dahTemp);
            }
            if(StringUtils.isBlank(result)){
                result = "归档成功";
            }
        }
        return result;
    }

    @Override
    public String dealTcGdxx(String xmid, String gdlx, String userid) {
        String result = null;
        List<BdcGdxx> bdcGdxxListTemp = getBdcGdxxByXmid(xmid);
        Integer maxAjh = getMaxAjhByGdlx(gdlx);
        if (CollectionUtils.isNotEmpty(bdcGdxxListTemp)){
            result = "所选流程已归档，请检查";
        }else if(maxAjh != null && maxAjh == 9999){
            result = "该目录号已满，请重新录入目录";
        }else{
            //根据归档类型获得配置的档案号
            if (StringUtils.isNotBlank(gdlx)) {
                String ajh = null;
                if(maxAjh == null){
                    ajh = "0001";
                }else{
                    ajh = String.format("%04d", maxAjh + 1);
                }
                //目录号+K+案卷号
                if(StringUtils.isNotBlank(ajh)){
                    String dah = gdlx + "K" + ajh;
                    BdcGdxx bdcGdxx = new BdcGdxx();
                    bdcGdxx.setDaid(dah);
                    bdcGdxx.setDah(dah);
                    bdcGdxx.setMlh(gdlx);
                    bdcGdxx.setAjh(ajh);
                    bdcGdxx.setXmid(xmid);
                    bdcGdxx.setGdrq(new Date());
                    if (StringUtils.isNotBlank(userid)){
                        bdcGdxx.setGdr(PlatformUtil.getCurrentUserName(userid));
                    }
                    bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                    entityMapper.insertSelective(bdcGdxx);
                    result = "归档成功";
                }
            }
        }
        return result;
    }

    @Override
    public Integer getMaxAjhByGdlx(String gdlx) {
        Integer maxAjh = null;
        if(StringUtils.isNotBlank(gdlx)){
            maxAjh = bdcGdxxMapper.getMaxAjhByGdlx(gdlx);
        }
        return maxAjh;
    }

    @Override
    public void changeMlh(Map map) {
        if(map != null){
            bdcGdxxMapper.changeMlh(map);
        }
    }

    public String dealGdxx(String xmid,String gdlx,String hh,String userid,String dah){
        String result = null;
        List<BdcGdxx> bdcGdxxListTemp = getBdcGdxxByXmid(xmid);
        if (CollectionUtils.isNotEmpty(bdcGdxxListTemp)){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(xmid);
            if ( bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh())) {
                if (StringUtils.isBlank(result)) {
                    result = bdcXm.getBh();
                }
            }
        }else{
            //根据归档类型获得配置的档案号
            if (StringUtils.isNotBlank(gdlx)) {
                if(StringUtils.isBlank(dah)){
                    dah = getDahByGdlx(gdlx);
                }
                BdcGdxx bdcGdxx = new BdcGdxx();
                bdcGdxx.setDaid(dah);
                bdcGdxx.setDah(dah);
                bdcGdxx.setXmid(xmid);
                bdcGdxx.setHh(hh);
                bdcGdxx.setGdrq(new Date());
                if (StringUtils.isNotBlank(userid)){
                    bdcGdxx.setGdr(PlatformUtil.getCurrentUserName(userid));
                }
                bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                bdcGdxx.setGdlx(gdlx);
                entityMapper.insertSelective(bdcGdxx);
            }
        }
        return result;
    }

    public String dealBdcArchives(String xmid, String gdlx, String userid,String classifieldLetters){
        String result = null;
        String defaultDah = null;
        if (StringUtils.isNotBlank(gdlx) && StringUtils.isNotBlank(classifieldLetters)){
            //流水号不满8位，左侧补0
            DecimalFormat df = new DecimalFormat("00000000");
            StringBuilder defaultDahBuilder = new StringBuilder(classifieldLetters);
            switch (Integer.parseInt(gdlx)) {
                case 0:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultCqDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultCqDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                case 1:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultDyDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultDyDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                case 2:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultCqzxDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultCqzxDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                case 3:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultDyzxDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultDyzxDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                case 4:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultCfJfDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultCfJfDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                case 5:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultQtDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultQtDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                case 6:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultYgdjDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultYgdjDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                case 7:
                    if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultZlDah"))){
                        defaultDah = df.format(Integer.parseInt(AppConfig.getProperty("bdcGdxx.defaultZlDah")));
                        defaultDah = defaultDahBuilder.append(defaultDah).toString();
                    }
                    break;
                default:
                    break;
            }
            List<BdcGdxx> bdcGdxxList = getBdcGdxxByDah(defaultDah, gdlx);
            BdcGdxx bdcGdxx = new BdcGdxx();
            if (CollectionUtils.isEmpty(bdcGdxxList)) {
                bdcGdxx.setDah(defaultDah);
                bdcGdxx.setDaid(defaultDah);
            }else{
                Long maxLsh = bdcGdxxMapper.getMaxLsh(gdlx);
                StringBuilder dahBuilder = new StringBuilder(classifieldLetters);
                if (maxLsh != null) {
                    String dah = dahBuilder.append(df.format(maxLsh+1)).toString();
                    bdcGdxx.setDaid(dah);
                    bdcGdxx.setDah(dah);
                }
            }
            bdcGdxx.setXmid(xmid);
            bdcGdxx.setGdrq(new Date());
            if (StringUtils.isNotBlank(userid)){
                bdcGdxx.setGdr(PlatformUtil.getCurrentUserName(userid));
            }
            bdcGdxx.setGdxxid(UUIDGenerator.generate18());
            bdcGdxx.setGdlx(gdlx);
            entityMapper.insertSelective(bdcGdxx);
            result = "归档成功";
        }
        return result;
    }

    private void dealDyGdQdCqrAndCqzh(BdcDyGdQd bdcDyGdQd,BdcXm bdcXm){
        //产权可能来自过度，通过xmly区分
        if (bdcDyGdQd != null && bdcXm != null){
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
            String sqlx = null;
            bdcDyGdQd.setCqzh(bdcXm.getYbdcqzh());
            if(pfWorkFlowInstanceVo != null ){
                HashMap map = new HashMap();
                map.put("wdid",pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                List<HashMap> sqlxList = bdcZdGlService.getBdcZdSqlx(map);
                if(CollectionUtils.isNotEmpty(sqlxList) && sqlxList.get(0).get("DM") != null){
                    sqlx = sqlxList.get(0).get("DM").toString();
                }
            }
            if(StringUtils.equals(bdcXm.getXmly(),Constants.XMLY_BDC )|| CommonUtil.indexOfStrs(Constants.SQLX_HBZYDY,sqlx)){
                String cqProid = null;
                String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
                List<String> cqProidList = bdcXmService.getXsBdcCqProidByBdcdyh(bdcdyh);
                if (CollectionUtils.isNotEmpty(cqProidList)){
                    cqProid = cqProidList.get(0);
                }
                bdcDyGdQd.setCqr(bdcQlrService.combinationQlr(bdcQlrService.queryBdcQlrByProid(cqProid)));
            }else{
                String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
                List<GdFwsyq> gdFwsyqList = gdFwsyqMapper.getGdFwsyqListByBdcdyh(bdcdyh);
                List<GdTdsyq> gdTdsyqList = gdTdsyqMapper.getTdGdTdsyqListByBdcdyh(bdcdyh);
                String cqr = null;
                if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                    if(gdFwsyqList.size()>1){
                        cqr = gdQlrService.getGdQlrsByQlid(gdFwsyqList.get(0).getQlid(),Constants.QLRLX_QLR)+"等";
                    }else{
                        cqr = gdQlrService.getGdQlrsByQlid(gdFwsyqList.get(0).getQlid(),Constants.QLRLX_QLR);
                    }
                }else if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                    if(gdTdsyqList.size()>1){
                        cqr = gdQlrService.getGdQlrsByQlid(gdTdsyqList.get(0).getQlid(),Constants.QLRLX_QLR)+"等";
                    }else{
                        cqr = gdQlrService.getGdQlrsByQlid(gdTdsyqList.get(0).getQlid(),Constants.QLRLX_QLR);
                    }
                }
                bdcDyGdQd.setCqr(cqr);
            }
        }
    }
    //代码重构
    public String getDahByGdlx(String gdlx){
        String dah = null;
        switch (Integer.parseInt(gdlx)) {
            case 0:
                dah = AppConfig.getProperty("bdcGdxx.defaultCqDah");
                break;
            case 1:
                dah = AppConfig.getProperty("bdcGdxx.defaultDyDah");
                break;
            case 2:
                dah = AppConfig.getProperty("bdcGdxx.defaultScdjDah");
                break;
            case 3:
                dah = AppConfig.getProperty("bdcGdxx.defaultCqZxDah");
                break;
            case 4:
                dah = AppConfig.getProperty("bdcGdxx.defaultDyZxDah");
                break;
            case 5:
                dah = AppConfig.getProperty("bdcGdxx.defaultDyBgDah");
                break;
            case 6:
                dah = AppConfig.getProperty("bdcGdxx.defaultYgdjDah");
                break;
            case 7:
                dah = AppConfig.getProperty("bdcGdxx.defaultYzGyDah");
                break;
            default:
                break;
        }
        List<BdcGdxx> bdcGdxxList = getBdcGdxxByDah(dah, gdlx);
        if (CollectionUtils.isEmpty(bdcGdxxList)) {
            return dah;
        } else {
            Long maxDah = bdcGdxxMapper.getMaxDah(gdlx);
            dah = String.valueOf(maxDah + 1);
            //产权，抵押，抵押注销每年需要重新编码
            if (CommonUtil.indexOfStrs(ParamsConstants.GDXX_DAH_RECODE, gdlx)) {
                String curYear = CalendarUtil.getCurStrYear();
                if (Integer.parseInt(curYear) > Integer.parseInt(dah.substring(0, 4))) {
                    if (StringUtils.equals(gdlx, ParamsConstants.GDLX_CQ_DM) || StringUtils.equals(gdlx, ParamsConstants.GDLX_DY_DM)) {
                        dah = curYear + "000000";
                    } else if (StringUtils.equals(gdlx, ParamsConstants.GDLX_DYZX_DM)) {
                        dah = curYear + "00000";
                    }
                }
            }
            return dah;
        }
    }
    //代码重构
    public String getHhByGdlx(String gdlx){
        String hh = null;
        if (StringUtils.equals(gdlx,ParamsConstants.GDLX_CQ_DM) || StringUtils.equals(gdlx,ParamsConstants.GDLX_DY_DM)){
            String defaultHh = null;
            if (StringUtils.equals(gdlx,ParamsConstants.GDLX_CQ_DM) && StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultCqHh"))){
                defaultHh = AppConfig.getProperty("bdcGdxx.defaultCqHh");
            }else if (StringUtils.isNotBlank(AppConfig.getProperty("bdcGdxx.defaultDyHh"))){
                defaultHh = AppConfig.getProperty("bdcGdxx.defaultDyHh");
            }
            List<BdcGdxx> bdcGdxxList = getBdcGdxxByHh(defaultHh, gdlx);
            if (CollectionUtils.isEmpty(bdcGdxxList)){
                hh = defaultHh;
            } else {
                Integer maxHh = bdcGdxxMapper.getMaxHh(gdlx);
                hh = String.valueOf(maxHh + 1);
            }
        }
        return hh;
    }
}
