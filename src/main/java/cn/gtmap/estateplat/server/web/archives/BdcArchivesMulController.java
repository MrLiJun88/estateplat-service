package cn.gtmap.estateplat.server.web.archives;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.server.model.BdcCqGdQd;
import cn.gtmap.estateplat.server.model.BdcDyGdQd;
import cn.gtmap.estateplat.server.service.archives.BdcArchivesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aliyun.mns.sample.HttpEndpoint.logger;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/3/18
 * @description 昆山多业务按项目归档
 */
@Controller
@RequestMapping("/bdcArchivesMul")
public class BdcArchivesMulController {
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
        return "archives/archivesSearchMul";
    }
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 归档信息查询功能
     */
    @RequestMapping(value = "getGdxxMulPagesJson")
    @ResponseBody
    public Page<HashMap> getGdxxMulPagesJson(String cqzh, String dyzmh, String dah, String slbhs, String gdlx, String cxlx, Pageable pageable){
        boolean gdByProidFlag = Boolean.TRUE;
        return bdcArchivesService.getGdxxJson(cqzh,dyzmh,dah,slbhs,gdlx,cxlx,pageable,gdByProidFlag);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 归档功能实现
     */
    @ResponseBody
    @RequestMapping(value = "bdcGdMul")
    String bdcGdMul(String proids,String gdlx,String userid){
        return bdcArchivesService.bdcGd(proids,gdlx,userid);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 异步加载余下的归档信息
     */
    @ResponseBody
    @RequestMapping(value = "asyncGetRestOfGdxxMul")
    Map asyncGetRestOfGdxxMul(String proid){
        return bdcArchivesService.getRestOfGdxxMul(proid);
    }
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 保存页面编辑的盒号和档案号
     */
    @ResponseBody
    @RequestMapping(value = "saveGdxxDahAndHh")
    String saveGdxxDahAndHh(String proid, String hh, String dah, String yzDah){
        return  bdcArchivesService.saveGdxxDahAndHh(proid,dah,hh,yzDah);
    }
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 不动产产权归档清单导出
     */
    @ResponseBody
    @RequestMapping(value = "exportCqGdQd")
    public void exportCqGdQd(String proids,HttpServletResponse response){
        List<BdcCqGdQd> bdcCqGdQdList = bdcArchivesService.getBdcCqGdQdListByProids(proids,null,null);
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
    public void exportDyGdQd(String proids,HttpServletResponse response){
        List<BdcDyGdQd> bdcDyGdQdList = bdcArchivesService.getBdcDyGdQdListByProids(proids,null,null);
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
    public String getGdxxQdXmlStr(String proids,String gdlx,String qsNumber,String jsNumber){
        Boolean gdByProidFlag = Boolean.TRUE;
        return bdcArchivesService.getGdXmlStr(proids,gdlx,gdByProidFlag,qsNumber,jsNumber);
    }


    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 获取打印档案最小档案号和最大档案号
     */
    @ResponseBody
    @RequestMapping(value = "getQsDahAndJsDah")
    public Map getQsDahAndJsDah(String gdlx, String proids){
        return bdcArchivesService.getQsDahAndJsDah(gdlx,proids);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 多个流程归一个档案号功能页面(昆山)
     */
    @RequestMapping(value = "bdcArchivesMulFlow")
    public String mulFlowSearch(Model model,String userid){
        bdcArchivesService.initGdxxModel(model,userid);
        return "archives/archivesSearchMulFlow";
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 多个流程归一个档案号归档功能实现(昆山)
     */
    @ResponseBody
    @RequestMapping(value = "bdcGdMulFlow")
    String bdcGdMulFlow(String proids,String gdlx,String userid){
        return bdcArchivesService.bdcGdMulFlow(proids,gdlx,userid);
    }

}
