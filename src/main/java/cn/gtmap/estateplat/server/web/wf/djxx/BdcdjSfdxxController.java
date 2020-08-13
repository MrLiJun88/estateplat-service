package cn.gtmap.estateplat.server.web.wf.djxx;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2017/4/5
 * @description 
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/bdcdjSfdxx")
public class BdcdjSfdxxController  extends BaseController{

    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcSfxxService bdcSfxxService;
    @Autowired
    BdcSfxmService bdcSfxmService;

    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid,  @RequestParam(value = "wiid", required = false) String wiid) {
        String comqlr="";
        String comywr="";
        String sqlxMc="";
        String qlrlx="qlr";
        String ywrlx="ywr";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                BdcSpxx  bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                if(bdcSpxx!=null){
                    model.addAttribute("bdcSpxx",bdcSpxx);
                }
                else{
                    model.addAttribute("bdcSpxx",new BdcSpxx());
                }
                //处理权利人
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
                comqlr=bdcQlrService.combinationQlr(bdcQlrList);
                comywr=bdcQlrService.combinationYwr(bdcYwrList);
                if(StringUtils.isNotBlank(bdcXm.getSqlx())){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("dm", bdcXm.getSqlx());
                    List<HashMap> sqlxList = bdcZdGlService.getBdcZdSqlx(map);
                    if (CollectionUtils.isNotEmpty(sqlxList) && sqlxList.get(0).get("MC") != null)
                        sqlxMc=sqlxList.get(0).get("MC").toString();
                }
                BdcFdcq bdcFdcq=new BdcFdcq();
                Example example1 = new Example(BdcSfxx.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("proid", proid);
                List<BdcFdcq> bdcFdcqList = entityMapper.selectByExample(BdcFdcq.class, example1);
                if(bdcFdcqList!=null&&bdcFdcqList.size()>0){
                    bdcFdcq=bdcFdcqList.get(0);
                }
                //获取收件信息
                Example bdcsjxxExample = new Example(BdcSjxx.class);
                bdcsjxxExample.createCriteria().andEqualTo("proid", proid);
                List<BdcSjxx> bdcSjxxList = entityMapper.selectByExample(bdcsjxxExample);
                if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                    BdcSjxx bdcSjxx = bdcSjxxList.get(0);
                    model.addAttribute(bdcSjxx);
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String sjrq = "";
//                    sjrq = sdf.format(bdcSjxx.getSjrq());
                    sjrq= CalendarUtil.formatDateToString(bdcSjxx.getSjrq());
                    model.addAttribute("sjrq", sjrq);
                } else {
                    BdcSjxx bdcSjxx=new BdcSjxx();
                    bdcSjxx.setSjr(getUserName());
                    model.addAttribute(new BdcSjcl());
                    model.addAttribute( "bdcSjxx",bdcSjxx);
                    model.addAttribute("sjrq", CalendarUtil.formatDateToString( new Date()));
                }

                //获取收费信息和收费项目
                List<BdcSfxm> qlrbdcSfxmList=new ArrayList<BdcSfxm>();
                List<BdcSfxm> ywrbdcSfxmList=new ArrayList<BdcSfxm>();
                BdcSfxx bdcSfxx=new BdcSfxx();
                Example example = new Example(BdcSfxx.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("proid", proid);
                List<BdcSfxx> bdcSfxxList = entityMapper.selectByExample(BdcSfxx.class, example);
                if(bdcSfxxList!=null&&bdcSfxxList.size()>0){
                    bdcSfxx=bdcSfxxList.get(0);
                    //获取权利人收费信息
                    qlrbdcSfxmList=bdcSfxmService.getSfXm(qlrlx,bdcSfxx.getSfxxid());
                    ywrbdcSfxmList=bdcSfxmService.getSfXm(ywrlx,bdcSfxx.getSfxxid());
//                    Example qlrbdcSfxmexample= new Example(BdcSfxm.class);
//                    qlrbdcSfxmexample.createCriteria().andEqualTo("qlrlx", "qlr").andEqualTo("sfxxid",bdcSfxx.getSfxxid());
//                    qlrbdcSfxmList = entityMapper.selectByExample(BdcSfxm.class, qlrbdcSfxmexample);
                    //获取义务人收费信息
//                    Example ywrbdcSfxmexample= new Example(BdcSfxm.class);
//                    ywrbdcSfxmexample.createCriteria().andEqualTo("qlrlx", "ywr").andEqualTo("sfxxid",bdcSfxx.getSfxxid());
//                    ywrbdcSfxmList = entityMapper.selectByExample(BdcSfxm.class, ywrbdcSfxmexample);
                }

                //获取权 利人系统配置收费项目
                Example qlrexample = new Example(BdcXtSfxm.class);
                Example.Criteria criteria2= qlrexample.createCriteria();
                criteria2.andEqualTo("qlrlx", "qlr").andEqualTo("sqlxdm",bdcXm.getSqlx());
                List<BdcXtSfxm> qlrXtSfxmList = entityMapper.selectByExample(BdcXtSfxm.class, qlrexample);
                List<HashMap> qlrxtsfxmmcList=new ArrayList<HashMap>();
                if(qlrXtSfxmList!=null&&qlrXtSfxmList.size()>0){
                    for (BdcXtSfxm bdcXtSfxm:qlrXtSfxmList) {
                            HashMap map=new HashMap();
                            map.put("mc",bdcXtSfxm.getSfxmmc());
                            map.put("bz",bdcXtSfxm.getSfxmbz());
                            map.put("sl",bdcXtSfxm.getMrsl());
                            map.put("dw",bdcXtSfxm.getDw());
                            qlrxtsfxmmcList.add(map);
                    }
                }
                HashMap map1=new HashMap();
                map1.put("mc","test");
                qlrxtsfxmmcList.add(map1);
                //获取义务人系统配置收费项目
                Example ywrexample= new Example(BdcXtSfxm.class);
                ywrexample.createCriteria().andEqualTo("qlrlx", "ywr").andEqualTo("sqlxdm",bdcXm.getSqlx());
                List<BdcXtSfxm> ywrXtSfxmList = entityMapper.selectByExample(BdcXtSfxm.class, ywrexample);
                List<HashMap> ywrxtsfxmmcList=new ArrayList<HashMap>();
                if(ywrXtSfxmList!=null&&ywrXtSfxmList.size()>0){
                    for (BdcXtSfxm bdcXtSfxm:ywrXtSfxmList) {
                        HashMap map=new HashMap();
                        map.put("mc",bdcXtSfxm.getSfxmmc());
                        map.put("bz",bdcXtSfxm.getSfxmbz());
                        map.put("sl",bdcXtSfxm.getMrsl());
                        map.put("dw",bdcXtSfxm.getDw());
                        ywrxtsfxmmcList.add(map);
                    }
                }
                //获取权利人系统配置的收费标准list
                List<HashMap> sfbzList=bdcSfxxService.getxtsfbzBySqlx(bdcXm.getSqlx(),qlrlx);
                //获取权利人系统配置的收费单位list
                List<HashMap> sfdwList=bdcSfxxService.getxtsfdwBySqlx(bdcXm.getSqlx(),qlrlx);
                //获取义务人人系统配置的收费标准list
                List<HashMap> ywrsfbzList=bdcSfxxService.getxtsfbzBySqlx(bdcXm.getSqlx(),ywrlx);
                //获取义务人系统配置的收费单位list
                List<HashMap> ywrsfdwList=bdcSfxxService.getxtsfdwBySqlx(bdcXm.getSqlx(),ywrlx);
                model.addAttribute("qlrbdcSfxmList",qlrbdcSfxmList);
                model.addAttribute("ywrbdcSfxmList",ywrbdcSfxmList);
                model.addAttribute("ywrsfbzList",ywrsfbzList);
                model.addAttribute("ywrsfdwList",ywrsfdwList);
                model.addAttribute("sfbzList",sfbzList);
                model.addAttribute("sfdwList",sfdwList);
                model.addAttribute("ywrxtsfxmmcList",ywrxtsfxmmcList);
                model.addAttribute("qlrxtsfxmmcList",qlrxtsfxmmcList);
                model.addAttribute("qlrXtSfxmList",qlrXtSfxmList);
                model.addAttribute("bdcSfxx",bdcSfxx);
                model.addAttribute("bdcFdcq",bdcFdcq);
                model.addAttribute("sqlxMc",sqlxMc);
                model.addAttribute("comqlr",comqlr);
                model.addAttribute("comywr",comywr);
                model.addAttribute("bdcXm",bdcXm);

            }
        }
        return "wf/core/"+dwdm+"/djxx/bdcdjSfdxx";
    }
    @RequestMapping(value = "getBzAndDw", method = RequestMethod.POST)
    public String getBzAndDw(HttpServletResponse response, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "sfxmmc", required = false) String sfxmmc) {
       JSONObject jsonObject=new JSONObject();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            Example qlrexample = new Example(BdcXtSfxm.class);
            qlrexample.createCriteria().andEqualTo("qlrlx", "qlr").andEqualTo("sqlxdm", bdcXm.getSqlx()).andEqualTo("sfxmmc", sfxmmc);
            List<BdcXtSfxm> qlrXtSfxmList = entityMapper.selectByExample(BdcXtSfxm.class, qlrexample);
            if (qlrXtSfxmList != null && qlrXtSfxmList.size() > 0) {
                HashMap map = new HashMap();
                map.put("qlrbz", qlrXtSfxmList.get(0).getSfxmbz());
                map.put("qlrdw", qlrXtSfxmList.get(0).getDw());
                jsonObject= JSONObject.fromObject(map);
                try {
                 /*设置编码格式，返回结果
                 * ***/
                    response.setContentType( "text/html;charset=UTF-8");
                    response.getWriter().write( jsonObject.toString());
                    response.getWriter().flush();
                    response.getWriter().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return  null;
    }
    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description: 删除收费单项目
     * @Date 18:36 2017/4/10
     */
    @ResponseBody
    @RequestMapping(value = "delSfdXm", method = RequestMethod.POST)
    public Map delBdcQlr(Model model, String s) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(s)) {
            Example example = new Example(BdcSfxm.class);
            example.createCriteria().andEqualTo("sfxmid", s);
            entityMapper.deleteByExample(BdcSfxm.class,example);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:保存收费单信息
     * @Date 21:58 2017/4/10
     */
    @ResponseBody
    @RequestMapping(value = "saveSfxx", method = RequestMethod.POST)
    public Map saveBdcSpbxx(Model model,  String s) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
            //处理权利人收费项目及义务人收费项目的保存
            if (StringUtils.isNotBlank(s)) {
                List<BdcSfxm> bdcSfxmList = JSON.parseArray(s, BdcSfxm.class);
                model.addAttribute("bdcSfxmList", bdcSfxmList);
                if (CollectionUtils.isNotEmpty(bdcSfxmList) && bdcSfxmList.size() > 0) {
                    for (BdcSfxm bdcSfxm : bdcSfxmList) {
                        if (StringUtils.isNotBlank(bdcSfxm.getSfxmid())) {
                            //判断前台组织的权利人对象是否是新添加的
                            if (bdcSfxm.getSfxmid().length() < 10) {
                                if (StringUtils.isNotBlank(bdcSfxm.getSfxmmc())) {
                                    bdcSfxm.setSfxmid(UUIDGenerator.generate18());
                                    entityMapper.saveOrUpdate(bdcSfxm,bdcSfxm.getSfxmid());
                                }
                            } else {
                                entityMapper.saveOrUpdate(bdcSfxm,bdcSfxm.getSfxmid());
                            }
                        }
                    }
                    returnvalue = "success";
                }
            }
        map.put("msg", returnvalue);
        return map;
    }
}
