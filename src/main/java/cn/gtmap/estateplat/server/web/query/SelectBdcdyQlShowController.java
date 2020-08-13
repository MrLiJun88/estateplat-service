package cn.gtmap.estateplat.server.web.query;

/*
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2018/7/30
 * @description  选择不动产单元
 */

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.service.SelectBdcdyManageService;
import cn.gtmap.estateplat.server.service.etl.IntegrationPlatformService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadJsonFileUtil;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSONObject;
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

import java.util.*;

@Controller
@RequestMapping("/selectBdcdyQlShow")
public class SelectBdcdyQlShowController extends BaseController {
    @Autowired
    private SelectBdcdyManageService selectBdcdyManageService;
    @Autowired
    private Repo repository;
    @Autowired
    private IntegrationPlatformService integrationPlatformService;

    /*选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "multiselect", required = false) boolean multiselect, @RequestParam(value = "joinselect", required = false) boolean joinselect, @RequestParam(value = "glbdcdy", required = false) String glbdcdy, @RequestParam(value = "glzs", required = false) String glzs) {
        //获取选择不动产单元model
        selectBdcdyManageService.getSelectBdcdyModel(wiid, proid, glbdcdy, glzs, model);
        return selectBdcdyManageService.getSelectBdcdyPath(dwdm, multiselect, joinselect);
    }

    @RequestMapping("/addHbXm")
    public String toSqlxQllxRelConfig1(Model model) {
        return selectBdcdyManageService.getAddHbXmModel(model);
    }

    /*查询权籍数据不动产单元信息**/
    @ResponseBody
    @RequestMapping("/getDjsjBdcdyPagesJson")
    public Object getBdcXmPagesJson(Pageable pageable, String djh, String bdcdyh, String dcxc, String qlr,
                                    String tdzl, String bdclx, String bdclxdm, String zdtzm, String htbh, String fwbm,
                                    @RequestParam(value = "qlxzdm", required = false) String qlxzdm, String bdcdyhs, String exactQuery) {
        Map<String, Object> map = selectBdcdyManageService.getDjsjBdcdySearchMap(djh, bdcdyh, dcxc, qlr, tdzl, bdclx, bdclxdm, zdtzm, htbh, qlxzdm, bdcdyhs, exactQuery, fwbm);
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        if (StringUtils.equals(ParamsConstants.TRUE_LOWERCASE, showOptimize)) {
            return repository.selectPaging("getDjsjBdcdyOptimizeByPage", map, pageable);
        } else {
            return repository.selectPaging("getDjsjBdcdyByPage", map, pageable);
        }
    }

    /*查询不动产证书信息**/
    @ResponseBody
    @RequestMapping("/getBdczsListByPage")
    public Object getBdczsListByPage(Pageable pageable, String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclx, String bdclxdm, String dcxc, String zdtzm, String fwbm
            , @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs
            , @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm, @RequestParam(value = "proid", required = false) String proid, String bdcdyhs
            , String fzqssj, String fzjssj, String zstype, String cqzhjc, String proids, String dyr, String exactQuery) {
        Map<String, Object> map = selectBdcdyManageService.getBdczsListSearchMap(bdcdyh, qllx, bdcqzh, qlr, dyr, zl, bdclx, bdclxdm, dcxc, zdtzm, qlxzdm, dyfs, ysqlxdm, proid, bdcdyhs, fzqssj, fzjssj, zstype, cqzhjc, exactQuery, fwbm, proids);
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        if (StringUtils.equals(ParamsConstants.TRUE_LOWERCASE, showOptimize)) {
            return repository.selectPaging("getBdcZsOptimizeByPage", map, pageable);
        } else {
            return repository.selectPaging("getBdcZsByPage", map, pageable);
        }
    }


    @ResponseBody
    @RequestMapping("/getGdfczListByPage")
    public Object getGdfczListByPage(Pageable pageable, String bdcdyh, String fczh, String qlr, String fwzl, String dcxc, String qllx, String zdtzm, String proid, String searchBdcdyh, String cqzhjc, String exactQuery) {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(bdcdyh)) {
            bdcdyh = StringUtils.deleteWhitespace(searchBdcdyh);
        } else {
            bdcdyh = StringUtils.deleteWhitespace(bdcdyh);
        }
        Map<String, Object> map = selectBdcdyManageService.getGdfczListSearchMapAndPath(bdcdyh, fczh, qlr, fwzl, dcxc, qllx, zdtzm, proid, cqzhjc, exactQuery);
        logger.info("过渡房产信息获取查询条件结束：" + (System.currentTimeMillis() - startTime) + "ms");
        String path = "getGdfczUnSearchBdcdyByPage";
        if (map.get("path") != null) {
            path = map.get("path").toString();
        }
        map.remove("path");
        long operateTime = System.currentTimeMillis();
        Page<HashMap> dataPaging = repository.selectPaging(path, map, pageable);
        logger.info("过渡房产执行查询语句时间结束：" + (System.currentTimeMillis() - operateTime) + "ms");
        logger.info("过渡房产信息查询结束：" + (System.currentTimeMillis() - startTime) + "ms");
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping("/getGdtdzListByPage")
    public Object getGdtdzListByPage(Pageable pageable, String bdcdyh, String tdzh, String qlr, String dcxc, String tdzl, String qllx, String zdtzm, String proid, String searchBdcdyh, String cqzhjc, String exactQuery) {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(bdcdyh)) {
            bdcdyh = StringUtils.deleteWhitespace(searchBdcdyh);
        } else {
            bdcdyh = StringUtils.deleteWhitespace(bdcdyh);
        }
        Map<String, Object> map = selectBdcdyManageService.getGdtdzListSearchMapAndPath(bdcdyh, tdzh, qlr, dcxc, tdzl, qllx, zdtzm, proid, cqzhjc, exactQuery);
        logger.info("过渡土地信息获取查询条件结束：" + (System.currentTimeMillis() - startTime) + "ms");
        String path = "";
        if (map.get("path") != null) {
            path = map.get("path").toString();
        }
        map.remove("path");
        long operateTime = System.currentTimeMillis();
        Page<HashMap> dataPaging = repository.selectPaging(path, map, pageable);
        logger.info("过渡土地执行查询语句时间结束：" + (System.currentTimeMillis() - operateTime) + "ms");
        logger.info("过渡土地信息查询结束：" + (System.currentTimeMillis() - startTime) + "ms");
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping("/getQlxxListByPage")
    public Object getQlxxListByPage(Pageable pageable, String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm, @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs, @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm, @RequestParam(value = "proid", required = false) String proid, String bzxr, String cfwh, String fwbm, String cqzhjc, String exactQuery, String proids) {
        Map<String, Object> map = selectBdcdyManageService.getQlxxListMap(bdcdyh, qllx, bdcqzh, qlr, zl, bdclxdm, dcxc, zdtzm, qlxzdm, dyfs, ysqlxdm, proid, bzxr, cfwh, cqzhjc, exactQuery, fwbm, proids);
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
            return repository.selectPaging("getQlxxOptimizeByPage", map, pageable);
        } else {
            return repository.selectPaging("getQlxxByPage", map, pageable);
        }
    }

    @ResponseBody
    @RequestMapping("/getXzqlxxListByPage")
    public Object getXzqlxxListByPage(Pageable pageable, String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm, @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs, @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm, @RequestParam(value = "proid", required = false) String proid, String bzxr, String cfwh, String fwbm, String cqzhjc, String exactQuery, String proids) {
        Map<String, Object> map = selectBdcdyManageService.getQlxxListMap(bdcdyh, qllx, bdcqzh, qlr, zl, bdclxdm, dcxc, zdtzm, qlxzdm, dyfs, ysqlxdm, proid, bzxr, cfwh, cqzhjc, exactQuery, fwbm, proids);
        return repository.selectPaging("getXzqlxxOptimizeByPage", map, pageable);
    }

    /**
     * 获取数据
     *
     * @param
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getdateByProid")
    public Map<String, Object> getdateByProid(String proid) {
        return selectBdcdyManageService.getdataMapByProid(proid);
    }

    @ResponseBody
    @RequestMapping("/getBdcdyZt")
    public Map<String, Object> getBdcdyZt(String proid, String djid, String bdclx) {
        return null;
    }


    @RequestMapping(value = "showHhcf")
    public String showHhcf(Model model, String proid) {
        selectBdcdyManageService.getShowHhcfModel(model, proid);
        String dwdm = AppConfig.getProperty("dwdm");
        return "query/" + dwdm + "/showHhcfsj";
    }

    /**
     * 获得限制原因
     *
     * @param cqzh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getXzyy", method = RequestMethod.GET)
    public Map<String, String> getXzyy(String cqzh, String qlid) {
        return selectBdcdyManageService.getXzyy(qlid, cqzh);
    }

    /**
     * 根据qlid获取不动产信息
     *
     * @param qlid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getBdcDateByQlid", method = RequestMethod.GET)
    public Map<String, String> getBdcDateByQlid(String qlid) {
        return selectBdcdyManageService.getBdcDateByQlid(qlid);
    }

    @ResponseBody
    @RequestMapping("/getGdcfListByPage")
    public Object getGdcfListByPage(Pageable pageable, String proid, String bdcdyh, String cfwh, String qlr, String dcxc, String searchBdcdyh, String yqzh, String fwzl, String tdzl, String exactQuery) {
        if (StringUtils.isEmpty(bdcdyh)) {
            bdcdyh = StringUtils.deleteWhitespace(searchBdcdyh);
        } else {
            bdcdyh = StringUtils.deleteWhitespace(bdcdyh);
        }
        Map<String, Object> map = selectBdcdyManageService.getGdcfListSearchMap(proid, bdcdyh, cfwh, qlr, dcxc, yqzh, fwzl, tdzl, exactQuery);
        String showOptimize = AppConfig.getProperty("selectBdcdy.showOptimization");
        if (StringUtils.equals(showOptimize, ParamsConstants.TRUE_LOWERCASE)) {
            return repository.selectPaging("getGdcfListSearchAllOptimizeByPage", map, pageable);
        } else {
            return repository.selectPaging("getGdcfListSearchAllByPage", map, pageable);
        }
    }

    /**
     * @param bdcdyid
     * @return
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 获取不动产产权证号
     */
    @ResponseBody
    @RequestMapping("/getGdCfCqzh")
    public HashMap<String, String> getGdCfCqzh(String bdcdyid, String gdproid, String qlid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        String cqzh = selectBdcdyManageService.getGdCfCqzh(bdcdyid, gdproid, qlid);
        resultMap.put("cqzh", cqzh);
        return resultMap;
    }

    // 获取数据
    @ResponseBody
    @RequestMapping(value = "/getBusinessData")
    public Object getBusinessData(String businessNo, String proid) {
        Object businessDataJson = "";
        if (StringUtils.isNotBlank(businessNo)) {
            businessDataJson = integrationPlatformService.getBusinessData(businessNo, proid);
        }
        return businessDataJson;
    }

    // 导入wwslbh
    @ResponseBody
    @RequestMapping(value = "/importYwbh")
    public JSONObject importYwbh(String businessNo, String proid) {
        JSONObject returnJson = new JSONObject();
        if (StringUtils.isNotBlank(businessNo)) {
            returnJson = integrationPlatformService.importYwbh(businessNo, proid);
        }
        return returnJson;
    }

    @ResponseBody
    @RequestMapping("/getBdcdyQlZt")
    public Map<String, Object> getBdcdyQlZt(String proid, String djid, String bdclx) {
        return selectBdcdyManageService.getBdcdyQlZt(proid, djid, bdclx);
    }

    @ResponseBody
    @RequestMapping("/getBdcdyJyZt")
    public Map<String, Object> getBdcdyJyZt(String fwbm, @RequestParam(value = "proid", required = false) String proid) {
        return selectBdcdyManageService.getBdcdyJyZt(fwbm, proid);
    }

    @ResponseBody
    @RequestMapping("/getZdQlrByDjh")
    public Map<String, Object> getZdQlrByDjh(String djh) {
        return selectBdcdyManageService.getZdQlrByDjh(djh);
    }

    @ResponseBody
    @RequestMapping("/getPzUrl")
    public Map<String, Object> getPzUrl() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map> dayxUrls = ReadXmlProps.getJsPzByType("/urlConfig.json", "dayxUrl");
        if (CollectionUtils.isNotEmpty(dayxUrls)) {
            for (Map map : dayxUrls) {
                String dayxUrl = map.get("value") != null ? map.get("value").toString() : "";
                resultMap.put("dayxUrl", dayxUrl);
            }
        }
        List<Map> djbxxUrls = ReadXmlProps.getJsPzByType("/urlConfig.json", "djbxxUrl");
        if (CollectionUtils.isNotEmpty(djbxxUrls)) {
            for (Map map : djbxxUrls) {
                String djbxxUrl = map.get("value") != null ? map.get("value").toString() : "";
                resultMap.put("djbxxUrl", djbxxUrl);
            }
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/getMulBdcqzxx")
    public List<Map<String, Object>> getMulBdcqzxx(String yxmids) {
        return selectBdcdyManageService.getMulBdcqzxx(yxmids);
    }
}
