package cn.gtmap.estateplat.server.web.archives;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.server.model.BdcCqGdQd;
import cn.gtmap.estateplat.server.model.BdcDyGdQd;
import cn.gtmap.estateplat.server.model.BdcZdGdlx;
import cn.gtmap.estateplat.server.service.archives.BdcArchivesService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/25
 * @description 昆山归档信息
 */
@Controller
@RequestMapping("/bdcarchives")
public class BdcArchivesController extends BaseController{
    @Autowired
    private BdcArchivesService bdcArchivesService;
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 归档信息页面
     */
    @RequestMapping("")
    public String gdxxSearch(Model model,String userid){
        bdcArchivesService.initGdxxModel(model,userid);
        return "archives/archivesSearch";
    }

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @return
     * @description 太仓归档信息页面
     */
    @RequestMapping("tcGdxx")
    public String tcGdxxSearch(Model model,String userid,String slbh){
        bdcArchivesService.initTcGdxxModel(model,userid,slbh);
        return "archives/tcArchivesSearch";
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 归档信息查询功能
     */
    @RequestMapping(value = "getGdxxPagesJson")
    @ResponseBody
    public Page<HashMap> getGdxxPagesJson(String cqzh, String dyzmh, String dah, String slbhs,String gdlx,Pageable pageable){
        boolean gdByProidFlag = Boolean.FALSE;
        return bdcArchivesService.getGdxxJson(cqzh,dyzmh,dah,slbhs,gdlx,null,pageable,gdByProidFlag);
    }


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @return
     * @description 太仓归档信息查询功能
     */
    @RequestMapping(value = "getTcGdxxPagesJson")
    @ResponseBody
    public Page<HashMap> getTcGdxxPagesJson(String slbhs,Pageable pageable){
        return bdcArchivesService.getTcGdxxJson(slbhs,pageable);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 归档功能实现
     */
    @ResponseBody
    @RequestMapping(value = "bdcGd")
    public String bdcGd(String wiids,String gdlx,String userid){
        String wfProids = bdcArchivesService.transferWiidsToProids(wiids);
        return bdcArchivesService.bdcGd(wfProids,gdlx,userid);
    }

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @return
     * @description 太仓归档功能实现
     */
    @ResponseBody
    @RequestMapping(value = "tcBdcGd")
    public String tcBdcGd(String wiids,String gdlx,String userid){
        String wfProids = bdcArchivesService.transferWiidsToProids(wiids);
        return bdcArchivesService.tcBdcGd(wfProids,gdlx,userid);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 异步加载余下的归档信息
     */
    @ResponseBody
    @RequestMapping(value = "asyncGetRestOfGdxx")
    public Map asyncGetRestOfGdxx(String wiid){
        return bdcArchivesService.getRestOfGdxx(wiid);
    }
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 保存页面编辑的盒号和档案号
     */
    @ResponseBody
    @RequestMapping(value = "saveGdxxDahAndHh")
    public String saveGdxxDahAndHh(String wiid, String hh, String dah){
        return  bdcArchivesService.saveGdxxDahAndHh(wiid,dah,hh,"false");
    }
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 不动产产权归档清单导出
     */
    @ResponseBody
    @RequestMapping(value = "exportCqGdQd")
    public void exportCqGdQd(String wiids,HttpServletResponse response){
        List<BdcCqGdQd> bdcCqGdQdList = bdcArchivesService.getBdcCqGdQdListByWiids(wiids);
        bdcArchivesService.exportCqGdQd(bdcCqGdQdList,response);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 不动产产权抵押清单导出
     */
    @ResponseBody
    @RequestMapping(value = "exportDyGdQd")
    public void exportDyGdQd(String wiids,HttpServletResponse response){
        List<BdcDyGdQd> bdcDyGdQdList = bdcArchivesService.getBdcDyGdQdListByWiids(wiids);
        bdcArchivesService.exportDyGdQd(bdcDyGdQdList,response);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 组织打印预览xmlStr
     */
    @ResponseBody
    @RequestMapping(value = "getGdxxQdXmlStr",method = RequestMethod.GET)
    public String getGdxxQdXmlStr(String wiids,String gdlx){
        Boolean gdByProidFlag = Boolean.FALSE;
        return bdcArchivesService.getGdXmlStr(wiids,gdlx,gdByProidFlag,null,null);
    }


    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 获取打印档案最小档案号和最大档案号
     */
    @ResponseBody
    @RequestMapping(value = "getQsDahAndJsDah")
    public Map getQsDahAndJsDah(String gdlx, String wiids){
        return bdcArchivesService.getQsDahAndJsDah(gdlx,bdcArchivesService.transferWiidsToProids(wiids));
    }


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @return
     * @description 太仓归档修改目录号页面
     */
    @RequestMapping("modifyMlh")
    public String modifyMlh(Model model,String slbh){
        List<Map> gdlxList = bdcZdGlService.getBdcZdGdlx();
        model.addAttribute("gdlxlist",gdlxList);
        model.addAttribute("slbh",slbh);
        return "archives/modifyMlh";
    }
    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 保存目录信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveMlhXx", method = RequestMethod.POST)
    public Map saveMlhXx(String s) {
        Map resultMap = new HashMap();
        String msg = "fail";
        if(StringUtils.isNotBlank(s)){
            List<BdcZdGdlx> bdcZdGdlxList = JSON.parseArray(s, BdcZdGdlx.class);
            if(CollectionUtils.isNotEmpty(bdcZdGdlxList)){
                for(BdcZdGdlx bdcZdGdlx : bdcZdGdlxList){
                    Map map = null;
                    if(StringUtils.isNotBlank(bdcZdGdlx.getDm()) && StringUtils.isNotBlank(bdcZdGdlx.getMc())){
                        map = new HashMap();
                        map.put("mc",bdcZdGdlx.getMc());
                        map.put("dm",bdcZdGdlx.getDm());
                        bdcArchivesService.changeMlh(map);
                        msg = ParamsConstants.SUCCESS_LOWERCASE;
                    }
                }
            }
        }
        resultMap.put("msg",msg);
        return resultMap;
    }

}
