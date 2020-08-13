package cn.gtmap.estateplat.server.web.archives;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.server.service.archives.BdcArchivesService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/3/20
 * @description 高新区一笔业务多个档案号按proid归档
 */
@Controller
@RequestMapping(value = "bdcGdByProid")
public class BdcGdByProidController extends BaseController{
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
        return "archives/bdcGdByProid";
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 归档信息查询功能
     */
    @RequestMapping(value = "getGdxxPagesJson")
    @ResponseBody
    public Page<HashMap> getGdxxPagesJson(String zh, String slbhs, String gdlx, Pageable pageable){
        boolean gdByProidFlag = Boolean.TRUE;
        return bdcArchivesService.getGdxxJson(zh,null,null,slbhs,gdlx,null,pageable,gdByProidFlag);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 归档功能实现
     */
    @ResponseBody
    @RequestMapping(value = "bdcGd")
    String bdcGd(String proids,String gdlx,String userid){
        return bdcArchivesService.bdcArchives(proids,gdlx,userid);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 异步加载余下的归档信息
     */
    @ResponseBody
    @RequestMapping(value = "asyncGetRestOfGdxx")
    Map asyncGetRestOfGdxx(String proid){
        return bdcArchivesService.getRestOfGdxxMul(proid);
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 验证档案号是否存在
     */
    @ResponseBody
    @RequestMapping(value = "validateDahExist")
    Map validateDahExist(String xmids){
        Map map = new HashMap();
        String result = bdcArchivesService.validateDahExist(xmids);
        map.put("result",result);
        return map;
    }
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 重新生成归档信息
     */
    @ResponseBody
    @RequestMapping(value = "reBdcArchive")
    Map reBdcArchive(String xmids,String gdlx,String userid){
        Map map = new HashMap();
        String result = bdcArchivesService.reBdcArchive(xmids,gdlx,userid);
        map.put("result",result);
        return map;
    }
}
