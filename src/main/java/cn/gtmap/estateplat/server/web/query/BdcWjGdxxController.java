package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcGdxx;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcGdxxService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.service.ArchivePostService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @version V1.0, 2018/6/7
 * @description
 */
@Controller
@RequestMapping("/BdcWjGdxx")
public class BdcWjGdxxController extends BaseController {
    @Autowired
    BdcGdxxService bdcGdxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private ArchivePostService archivePostService;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    /**
     *  访问项目归档信息列表
     */
    @RequestMapping("/list")
    public String index(Model model) {
        String gdxxGjss = AppConfig.getProperty("gdxxGjss.order");
        String showUpFileButton = AppConfig.getProperty("gdxx.showButton.upFile");
        List<String> gdxxGjssOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(gdxxGjss)) {
            String[] gdxxGjssArray = gdxxGjss.split(",");
            for (String gdxxGjssZd : gdxxGjssArray) {
                if(StringUtils.isNotBlank(gdxxGjssZd)) {
                    gdxxGjssOrderList.add(gdxxGjssZd);
                }
            }
        }
        model.addAttribute("gdxxGjss", gdxxGjss);
        model.addAttribute("gdxxGjssOrderList", gdxxGjssOrderList);
        model.addAttribute("showUpFileButton", showUpFileButton);
        return "query/bdcWjGdxxList";
    }

    /**
     *  zdd 吴江归档信息列表数据获取
     */
    @ResponseBody
    @RequestMapping("/getWjGdxxPagesJson")
    public Object getGdxxPagesJson(Pageable pageable, String xmmc, String bdcdyh, String bdcqzh, String zl, String isgd, String searchText) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(searchText)) {
            map.put("searchText", searchText);
        } else {
            map.put("xmmc", xmmc);
            map.put("bdcdyh", bdcdyh);
            map.put("bdcqzh", bdcqzh);
            map.put("zl", zl);
            map.put("isgd", isgd);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getWjGdxxByPage", map, pageable);
        //编号搜不出数据，默认进行统编号查询
        if (dataPaging.getItemSize() == 0) {
            HashMap<String, Object> tmpMap = new HashMap<String, Object>();
            tmpMap.put("tbh", searchText);
            dataPaging = repository.selectPaging("getWjGdxxByPage", tmpMap, pageable);
        }
        return dataPaging;
    }

    /**
     *   zdd 不动产项目归档  支持批量归档
     */
    @ResponseBody
    @RequestMapping("/bdcXmGd")
    public String bdcXmGd(Model model, String proids) {
        String msg = "归档成功";
        if (StringUtils.isNotBlank(proids)) {
            String[] proid = proids.split(",");
            for (int i = 0; i < proid.length; i++) {
                //判断是否已经归档
                if (!bdcGdxxService.checkIsGd(proid[i])) {
                    msg = "已经归档，不能重复归档！";
                    continue;
                }

                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid[i]);
                if (bdcXm != null) {
                    BdcGdxx bdcGdxx = archivePostService.postBdcXmInfo(bdcXm);
                    if (StringUtils.isBlank(bdcGdxx.getDaid())) {
                        msg = "";
                    }
                }
            }
        }
        return msg;
    }


    /**
     * @param proid
     * @return String
     * @author wangtao
     * @description 对单个进行归档
     */
    @ResponseBody
    @RequestMapping("/bdcXmGdOne")
    public String bdcXmGdOne(Model model, String proid) {
        String msg = "归档成功";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                BdcGdxx bdcGdxx = archivePostService.postBdcXmInfo(bdcXm);
                if (bdcGdxx!=null&&StringUtils.isBlank(bdcGdxx.getDaid())) {
                    msg = "";
                }
            }
        }

        return msg;
    }

    /**
     * @param proid
     * @return String
     * @author wangtao
     * @description 判断是否已经归档
     */
    @ResponseBody
    @RequestMapping("/checkBdcXmIsGd")
    public String checkBdcXmIsGd(Model model, String proid) {
        String msg = "";
        if (StringUtils.isNotBlank(proid) && !bdcGdxxService.checkIsGd(proid)) {
            // 判断是否已经归档
            msg = "已经归档，不能重复归档！";
        }
        return msg;
    }

    /**
     * @param proid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 异步获取bdcqzh,zl和bdcdyh
    */
    @ResponseBody
    @RequestMapping("/asycAccessBdcXxByProid")
    public Map asycAccessBdcXxByProid(String proid){
        Map returnMap = Maps.newHashMap();
        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
        StringBuilder zsBuilder = new StringBuilder();
        if(CollectionUtils.isNotEmpty(bdcZsList)){
            for(BdcZs bdcZs : bdcZsList){
                if(StringUtils.isBlank(zsBuilder)){
                    zsBuilder.append(bdcZs.getBdcqzh());
                }else{
                    zsBuilder.append("/").append(bdcZs.getBdcqzh());
                }
            }
        }
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        if(null != bdcSpxx){
            returnMap.put("bdcdyh", CommonUtil.formatEmptyValue(bdcSpxx.getBdcdyh()));
            returnMap.put("zl", CommonUtil.formatEmptyValue(bdcSpxx.getZl()));
        }
        returnMap.put("bdcqzh", CommonUtil.formatEmptyValue(zsBuilder));
        return returnMap;
    }

}
