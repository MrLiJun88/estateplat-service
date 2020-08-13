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
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2018/10/25
 * @description
 */
@Controller
@RequestMapping("/BdcGdxxMul")
public class BdcGdxxMulController extends BaseController {

    @Autowired
    BdcGdxxService bdcGdxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZsService bdcZsService;
    /**
     * a表示未归档，b表示已归档
     */

    private static final String GDXX_NOT = "a";
    private static final String GDXX_YES = "b";

    //zdd 访问项目归档信息列表
    @RequestMapping("/list")
    public String index(Model model) {

        String gdxxGjss = AppConfig.getProperty("gdxxGjss.order");
        String showUpFileButton = "true";
        List<String> gdxxGjssOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(gdxxGjss)) {
            String[] gdxxGjssArray = gdxxGjss.split(",");
            for (String gdxxGjssZd : gdxxGjssArray) {
                if (StringUtils.isNotBlank(gdxxGjssZd)) {
                    gdxxGjssOrderList.add(gdxxGjssZd);
                }
            }
        }
        model.addAttribute("gdxxGjss", gdxxGjss);
        model.addAttribute("gdxxGjssOrderList", gdxxGjssOrderList);
        model.addAttribute("showUpFileButton", showUpFileButton);
        return "query/bdcGdxxMulList";
    }



    //zdd 列表数据获取
    @ResponseBody
    @RequestMapping("/getGdxxPagesJson")
    public Object getGdxxPagesJson(Pageable pageable, String gdr, String gdrq, String mlh, String isgd, String searchText) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(searchText)) {
            map.put("searchText", searchText);
        }
        //添加配置展示下列sqlx下的数据
        String[] proidList = null;
        String sqlxArray = AppConfig.getProperty("bdcGdxx.mul.sqlxdm");
        if (StringUtils.isNotBlank(sqlxArray) && StringUtils.indexOf(sqlxArray,",") > -1) {
            proidList = StringUtils.split(sqlxArray,",");
            map.put("proidList",proidList);
        }
        //高级搜索
        if (StringUtils.isNotBlank(gdr) || StringUtils.isNotBlank(gdrq) || StringUtils.isNotBlank(isgd) || StringUtils.isNotBlank(mlh)) {
            // 已归档与未归档,归档人，归档时间和目录号进行查询则一定是查已经归档的记录
            if (StringUtils.isNotBlank(gdr) || StringUtils.isNotBlank(gdrq) || (StringUtils.isNotBlank(isgd) && StringUtils.equals(GDXX_YES, isgd) || StringUtils.isNotBlank(mlh))){
                map.put("gdr", CommonUtil.formatEmptyValue(StringUtils.deleteWhitespace(gdr)));
                map.put("gdrq", CommonUtil.formatEmptyValue(StringUtils.deleteWhitespace(gdrq)));
                map.put("mlh", CommonUtil.formatEmptyValue(StringUtils.deleteWhitespace(mlh)));
                map.put("Gd", "true");
            }
            if (StringUtils.isNotBlank(isgd) && StringUtils.equals(GDXX_NOT, isgd)) {
                map.put("notGd", true);
            }
        } if (map.size() == 0) {
            map.put("searchText", " ");
        }
        Page<HashMap> dataPaging = repository.selectPaging("getGdxxMulByPage", map, pageable);
        //编号搜不出数据，默认进行统编号查询
        if (dataPaging.getItemSize() == 0) {
            HashMap<String, Object> tmpMap = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(searchText)) {
                tmpMap.put("tbh", searchText);
            } else {
                tmpMap.put("tbh", " ");
            }
            dataPaging = repository.selectPaging("getGdxxMulByPage", tmpMap, pageable);
        }
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping("/getCqWiidByProid")
    public Map getCqWiidByProid(String proid) {
        Map map = new HashMap();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
            map.put(ParamsConstants.WIID_LOWERCASE,bdcXm.getWiid());
        }
        return map;
    }


    @ResponseBody
    @RequestMapping("/saveGdxxInfo")
    public Map saveGdxxInfo(BdcGdxx bdcGdxx) {
        String msg = "保存成功";
        //表单序列化的xmid其实是wiid的值,且项目proid与gdxx的关系是一对一的
        Map<String, String> map = Maps.newHashMap();
        bdcGdxxService.saveGdxxMulForPl(bdcGdxx);
        map.put("msg", msg);
        return map;
    }

    /**
     * @param proid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 获取归档信息填充弹出框
     */
    @ResponseBody
    @RequestMapping("/getCurrentGdxxInfo")
    public Map getCurrentGdxxInfo(String proid) {
        return bdcGdxxService.getCurrentGdxxInfo(proid);
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 批量修改按钮点击修改案卷号和目录号
     */
    @ResponseBody
    @RequestMapping("/saveMulGdxxInfo")
    public Map saveMulGdxxInfo(String ids, BdcGdxx bdcGdxx) {
        return bdcGdxxService.saveMulGdxxInfo(ids,bdcGdxx,getUserName(),true);
    }

    /**
     * @param proid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 异步获取产权信息
     */
    @ResponseBody
    @RequestMapping("/getBdcqxxByProid")
    public JSONObject getBdcqxxByProid(String proid) {
        JSONObject resultMap = new JSONObject();
        String zl = "";
        String bdcqzh = "";
        String bdcdyh = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (null != bdcSpxx) {
                zl = CommonUtil.formatEmptyValue(bdcSpxx.getZl());
                bdcdyh = CommonUtil.formatEmptyValue(bdcSpxx.getBdcdyh());
            }
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                BdcZs bdcZs = bdcZsList.get(0);
                if (null != bdcZs && StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                    bdcqzh = bdcZs.getBdcqzh();
                }
            }
        }
        resultMap.put("zl", zl);
        resultMap.put("bdcdyh", bdcdyh);
        resultMap.put("bdcqzh", bdcqzh);
        return resultMap;
    }

    /**
     * @param proid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 异步获取归档信息
     */
    @ResponseBody
    @RequestMapping("/getBdcGdxxByProid")
    public JSONObject getBdcGdxxByProid(String proid) {
        return  bdcGdxxService.getBdcGdxxByProid(proid);
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 页面直接编辑批量修改
     */
    @ResponseBody
    @RequestMapping("/saveGdxxInfoForPl")
    public JSONObject saveGdxxInfoForPl(String postData) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(postData)) {
            List<BdcGdxx> bdcGdxxList = JSON.parseArray(postData, BdcGdxx.class);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                for (BdcGdxx bdcGdxx : bdcGdxxList) {
                    bdcGdxx.setGdrq(new Date());
                    bdcGdxx.setGdr(getUserName());
                    bdcGdxxService.saveGdxxMulForPl(bdcGdxx);
                }
            }
            jsonObject.put("msg", "success");
        }
        return jsonObject;
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据目录号获取当前最大的案卷号
     */
    @ResponseBody
    @RequestMapping("/getCurrentMaxAjhByMlh")
    public JSONObject getCurrentMaxAjhByMlh(String mlh) {
        JSONObject returnJson = new JSONObject();
        returnJson.put("ajh", bdcGdxxService.getMaxAjhByMlh(mlh, false));
        return returnJson;
    }


}

