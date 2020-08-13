package cn.gtmap.estateplat.server.web.query;


import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcZdQszt;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询不动产单元
 * User: zx
 * Date: 15-3-29
 * Time: 下午3:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryBdcdy")
public class QueryBdcdyContorller extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private QllxService qllxService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcQlrService bdcQlrService;

    /*审批表选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        List<Map> bdcList = bdcZdGlService.getZdBdclx();

        List<BdcZdQszt> qsztList = bdcZdGlService.getBdcZdQszt();

        String bdcdyGjss = AppConfig.getProperty("bdcdyGjss.order");
        List<String> bdcdyGjssOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(bdcdyGjss) && bdcdyGjss.split(",").length > 0) {
            for (String bdcdyGjssZd : bdcdyGjss.split(",")) {
                bdcdyGjssOrderList.add(bdcdyGjssZd);
            }
        }
        model.addAttribute("bdcdyGjss", bdcdyGjss);
        model.addAttribute("bdcdyGjssOrderList", bdcdyGjssOrderList);

        model.addAttribute("bdcList", bdcList);
        model.addAttribute("qsztListJson", JSONObject.toJSONString(qsztList));

        return "query/bdcdyList";
    }

    @ResponseBody
    @RequestMapping("/getBdcdyPagesJson")
    public Object getBdcdyPagesJson(Pageable pageable, String zl, String bdcdyh, String bdclx, String dcxc, String qlr) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
        } else {
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl.trim());
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(bdclx)) {
                map.put("bdclx", bdclx.trim());
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr.trim());
            }
        }

        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm)) {
            map.put("xzqdm", userDwdm);
        }
        return repository.selectPaging("getBdcdyByPage", map, pageable);
    }

    /**
     * @param bdcdyh
     * @return 返回获取的信息
     * @author liujie
     * @description 获取不动产单元的不动产类型，权利人，坐落
     */
    @ResponseBody
    @RequestMapping("/getLsxxBdcdyPagesJson")
    public Object getLsxxBdcdyPagesJson(Pageable pageable, String wiid, String zl, String bdcdyh, String bdclx, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
        } else {
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl.trim());
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
        }

        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }

        return repository.selectPaging("getLsxxBdcdyByPage", map, pageable);
    }


    /**
     * @param bdcdyh
     * @return 返回获取的信息
     * @author xiejianan
     * @description 获取不动产单元的不动产类型，权利人，坐落，权利状态信息
     */
    @ResponseBody
    @RequestMapping("/getBdcdyhxx")
    public HashMap getBdcdyhxx(String bdcdyh) {
        return qllxService.getBdcdyhxx(bdcdyh);
    }

    @ResponseBody
    @RequestMapping("/getBdcdyhQlxx")
    public HashMap getBdcdyhQlxx(String bdcdyh) {
        return qllxService.getBdcdyhQlxx(bdcdyh);
    }

    @ResponseBody
    @RequestMapping("/getBdcdyhByFwtdid")
    /**
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @param   tdid
     * @rerutn
     * @description 通过房屋土地id查询不动产单元号
     */
    public HashMap<String, String> getBdcdyhByFwtdid(String tdid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        String bdcdyh = "";
        bdcdyh = gdFwService.getBdcdyhByFwtdid(tdid);
        resultMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        return resultMap;
    }

    /*审批表选择不动产单元**/
    @RequestMapping(value = "bdcdyFw", method = RequestMethod.GET)
    public String bdcdyFw(Model model) {
        return "query/bdcdyFw";
    }

    @ResponseBody
    @RequestMapping("/bdcdyFwPagesJson")
    public Object bdcdyFwPagesJson(Pageable pageable, String zl, String bdcdyh, String bdclx, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            HashMap mapQlr = new HashMap();
            mapQlr.put("qlr", StringUtils.deleteWhitespace(dcxc.trim()));
            mapQlr.put("qlrlx", "qlr");
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrList(mapQlr);
            List<String> idList = bdcdyService.getDjhByQlr(mapQlr);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                List<String> bdcdyList = new ArrayList<String>();
                for (BdcQlr bdcQlr : bdcQlrList) {
                    String bdcdy = bdcdyService.getBdcdyhByProid(bdcQlr.getProid());
                    if (StringUtils.isNotBlank(bdcdy)) {
                        bdcdyList.add(bdcdy);
                    }
                }
                if (CollectionUtils.isNotEmpty(bdcdyList)) {
                    map.put("bdcdyhs", bdcdyList);
                }
                if (CollectionUtils.isNotEmpty(idList)) {
                    map.put("ids", idList);
                }
            }

            if (map.size() == 0) {
                map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
                String bdcdycxFwbhZdmc = AppConfig.getProperty("bdcdycx.fwbh.zdmc");
                if (StringUtils.isNotBlank(bdcdycxFwbhZdmc)) {
                    map.put("fwbh", "c." + bdcdycxFwbhZdmc + "='" + StringUtils.deleteWhitespace(dcxc) + "'");
                }
            }
        }
        return repository.selectPaging("queryBdcdyFwByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcdyxx")
    public HashMap getBdcdyxx(String id) {
        return bdcdyService.getBdcdyxxById(id);
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据zl和bdclx获取权利状态
    */
    @ResponseBody
    @RequestMapping(value = "/getBdcdyhQlxxByBdclxAndZl")
    public HashMap getBdcdyhQlxxByBdclxAndZl(String bdclx,String zl){
        HashMap returnMap = null;
        if(StringUtils.isNoneBlank(bdclx,zl)) {
            HashMap<String, String> queryMap = Maps.newHashMap();
            queryMap.put("zl", zl);
            queryMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
            List<BdcBdcdy> bdcBdcdyList = bdcdyService.getBdcdyInfoByQueryMap(queryMap);
            if(CollectionUtils.isNotEmpty(bdcBdcdyList)){
                BdcBdcdy tempBdcbdcdy = bdcBdcdyList.get(0);
                if(null != tempBdcbdcdy) {
                    returnMap = qllxService.getBdcdyhQlxx(CommonUtil.formatEmptyValue(tempBdcbdcdy.getBdcdyh()));
                }
            }
        }
        return returnMap;
    }
}
