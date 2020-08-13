package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
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
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.Constants;
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
 * Created by zdd on 2016/1/7.
 * 展现项目归档信息
 */
@Controller
@RequestMapping("/BdcGdxx")
public class BdcGdxxController extends BaseController {

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
    @Autowired
    private EntityMapper entityMapper;
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
        return "query/bdcGdxxList";
    }

    /**
     * @version 1.0
     * @author<a href="mailto:zhuwei@gtmap.cn>zhuwei</a>
     * @discription 过渡数据归档查询页面
     */
    @RequestMapping("/list/gd")
    public String indexGdList(Model model) {
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
        return "query/bdcGdxxGdList";
    }

    //zdd 列表数据获取
    @ResponseBody
    @RequestMapping("/getGdxxPagesJson")
    public Object getGdxxPagesJson(Pageable pageable, String gdr, String gdrq, String mlh, String isgd, String searchText) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(searchText)) {
            map.put("searchText", searchText);
        }
        //添加配置不展示下列sqlx下的数据
        String[] proidList = null;
        String sqlxArray = AppConfig.getProperty("bdcGdxx.mul.sqlxdm");
        if (StringUtils.isNotBlank(sqlxArray) &&  StringUtils.indexOf(sqlxArray,",") > -1) {
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
        Page<HashMap> dataPaging = repository.selectPaging("getGdxxByPage", map, pageable);
        //编号搜不出数据，默认进行统编号查询
        if (dataPaging.getItemSize() == 0) {
            HashMap<String, Object> tmpMap = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(searchText)) {
                tmpMap.put("tbh", searchText);
            } else {
                tmpMap.put("tbh", " ");
            }
            dataPaging = repository.selectPaging("getGdxxByPage", tmpMap, pageable);
        }
        return dataPaging;
    }

    //zdd 列表数据获取
    @ResponseBody
    @RequestMapping("/getGdxxGdPagesJson")
    public Object getGdxxGdPagesJson(Pageable pageable, String mlh, String ajhKs, String ajhJs) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(mlh)) {
            map.put("mlh", mlh);
        }
        if (StringUtils.isNotBlank(ajhKs)) {
            map.put("ajhKs", ajhKs);
        }
        if (StringUtils.isNotBlank(ajhJs)) {
            map.put("ajhJs", ajhJs);
        }

        Page<HashMap> dataPaging = repository.selectPaging("getGdxxGdByPage", map, pageable);
        return dataPaging;
    }

    //zdd 不动产项目归档  支持批量归档
    @ResponseBody
    @RequestMapping("/bdcXmGd")
    public String bdcXmGd(Model model, String wiids) {
        String msg = "归档成功";
        if (StringUtils.isNotBlank(wiids)) {
            String[] wiidArray = wiids.split(",");
            for (int i = 0; i < wiidArray.length; i++) {
                //判断是否已经归档
                if (!bdcGdxxService.checkAIsGd(wiidArray[i])) {
                    msg = "已经归档，不能重复归档！";
                    continue;
                }
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiidArray[i]);
                bdcGdxxService.saveOrUpdateBdcGdxx(bdcXmList, getUserName());
                bdcGdxxService.updateJyZtByWiid(wiidArray[i],getUserId());
            }
        }
        return msg;
    }


    /**
     * @param wiid
     * @return String
     * @author wangtao
     * @description 对单个进行归档
     */
    @ResponseBody
    @RequestMapping("/bdcXmGdOne")
    public String bdcXmGdOne(Model model, String wiid) {
        String msg = "归档成功";
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            bdcGdxxService.saveOrUpdateBdcGdxx(bdcXmList, getUserName());
            bdcGdxxService.updateJyZtByWiid(wiid,getUserId());
        }
        return msg;
    }

    /**
     * @param wiid
     * @return String
     * @author wangtao
     * @description 判断是否已经归档
     */
    @ResponseBody
    @RequestMapping("/checkBdcXmIsGd")
    public String checkBdcXmIsGd(Model model, String wiid) {
        String msg = "";
        if (StringUtils.isNotBlank(wiid) && !bdcGdxxService.checkAIsGd(wiid)) {
            // 判断是否已经归档
            msg = "已经归档，不能重复归档！";
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping("/getCqProidAndCqWiidByBh")
    public Map getCqProidAndCqWiidByBh(String bh) {
        return bdcGdxxService.getCqProidAndCqWiidByBh(bh);
    }


    @ResponseBody
    @RequestMapping("/saveGdxxInfo")
    public Map saveGdxxInfo(BdcGdxx bdcGdxx) {
        String msg = "保存成功";
        //表单序列化的xmid其实是wiid的值,且项目proid与gdxx的关系是一对一的
        Map<String, String> map = Maps.newHashMap();
        bdcGdxxService.saveGdxxForPl(bdcGdxx,getUserId());
        map.put("msg", msg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/saveGdxxInfoGd")
    public Map saveGdxxInfoGd(String gdxxid, int ajjs, int ajys, String ajh, String mlh) {
        HashMap map = new HashMap();
        String msg = "保存成功";
        if (StringUtils.isNotBlank(gdxxid)) {
            BdcGdxx bdcGdxx = entityMapper.selectByPrimaryKey(BdcGdxx.class, gdxxid);
            if (bdcGdxx != null) {
                bdcGdxx.setAjjs(ajjs);
                bdcGdxx.setAjys(ajys);
                bdcGdxx.setAjh(ajh);
                bdcGdxx.setMlh(mlh);
                entityMapper.saveOrUpdate(bdcGdxx, bdcGdxx.getGdxxid());
            } else {
                msg = "保存失败";
            }
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * @param wiid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 获取归档信息填充弹出框
     */
    @ResponseBody
    @RequestMapping("/getCurrentGdxxInfo")
    public Map getCurrentGdxxInfo(String wiid) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(wiid)) {
            //只获取流程的一个proid
            List<BdcXm> bdcXms = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXms)) {
                BdcXm bdcXm = bdcXms.get(0);
                map = bdcGdxxService.getCurrentGdxxInfo(bdcXm.getProid());
            }
        }
        return map;
    }

    /**
     * @param
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 获取归档信息填充弹出框
     */
    @ResponseBody
    @RequestMapping("/getCurrentGdxxInfoGd")
    public Object getCurrentGdxxInfogD(String gdxxid) {
        BdcGdxx bdcGdxx = new BdcGdxx();
        if (StringUtils.isNotBlank(gdxxid)) {
            bdcGdxx = entityMapper.selectByPrimaryKey(BdcGdxx.class, gdxxid);
        }
        return bdcGdxx;
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 批量修改按钮点击修改案卷号和目录号
     */
    @ResponseBody
    @RequestMapping("/saveMulGdxxInfo")
    public Map saveMulGdxxInfo(String ids, BdcGdxx bdcGdxx) {
        return bdcGdxxService.saveMulGdxxInfo(ids,bdcGdxx,getUserName(),false);
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 批量修改按钮点击修改案卷号和目录号
     */
    @ResponseBody
    @RequestMapping("/saveMulGdxxInfoGd")
    public Map saveMulGdxxInfoGd(String gdids, String mlh, String ajh) {
        return bdcGdxxService.saveMulGdxxInfoGd(gdids, mlh, ajh);
    }

    /**
     * @param wiid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 异步获取产权信息
     */
    @ResponseBody
    @RequestMapping("/getBdcqxxByWiid")
    public JSONObject getBdcqxxByWiid(String wiid) {
        JSONObject resultMap = new JSONObject();
        String zl = "";
        String bdcqzh = "";
        String bdcdyh = "";
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                BdcXm bdcXm = bdcXmList.get(0);
                if (null != bdcXm && StringUtils.isNotBlank(bdcXm.getProid())) {
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                    if (null != bdcSpxx) {
                        zl = CommonUtil.formatEmptyValue(bdcSpxx.getZl());
                        bdcdyh = CommonUtil.formatEmptyValue(bdcSpxx.getBdcdyh());
                    }
                    bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                }
                if (bdcXmList.size() > 1) {
                    if (StringUtils.isNotBlank(zl)) {
                        zl = zl + "等";
                    }
                    if (StringUtils.isNotBlank(bdcdyh)) {
                        bdcdyh = bdcdyh + "等";
                    }
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        BdcZs bdcZs = bdcZsList.get(0);
                        if (null != bdcZs && StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                            bdcqzh = bdcZs.getBdcqzh();
                        }
                        if (StringUtils.isNotBlank(bdcqzh)) {
                            bdcqzh = bdcqzh + "等";
                        }
                    }
                } else {
                    BdcXm bdcXm1 = bdcXmList.get(0);
                    if (bdcXm1 != null && StringUtils.isNotBlank(bdcXm1.getProid())) {
                        List<BdcZs> bdcZses = bdcZsService.queryBdcZsByProid(bdcXm1.getProid());
                        if (CollectionUtils.isNotEmpty(bdcZses)) {
                            BdcZs bdcZsTemp = bdcZses.get(0);
                            if (null != bdcZsTemp && StringUtils.isNotBlank(bdcZsTemp.getBdcqzh())) {
                                bdcqzh = bdcZsTemp.getBdcqzh();
                            }
                        }
                    }
                }
            }
        }
        resultMap.put("zl", zl);
        resultMap.put("bdcdyh", bdcdyh);
        resultMap.put("bdcqzh", bdcqzh);
        return resultMap;
    }

    /**
     * @param wiid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 异步获取归档信息
     */
    @ResponseBody
    @RequestMapping("/getBdcGdxxByWiid")
    public JSONObject getBdcGdxxByWiid(String wiid) {
        JSONObject resultMap = new JSONObject();
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                BdcXm bdcXm = bdcXmList.get(0);
                resultMap = bdcGdxxService.getBdcGdxxByProid(bdcXm.getProid());
            }
        }
        return resultMap;
    }

    /**
     * @param wiid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 获取打印的proid
     */
    @ResponseBody
    @RequestMapping("/getbdcXmProidByWiid")
    public Map<String, String> getbdcXmProidByWiid(String wiid) {
        HashMap<String, String> map = new HashMap<String, String>();
        String proid = bdcGdxxService.getGdxxProidByWiid(wiid);
        map.put("proid", proid);
        return map;
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
                    bdcGdxxService.saveGdxxForPl(bdcGdxx,getUserId());
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
    public JSONObject getCurrentMaxAjhByMlh(String mlh, String sfGd) {
        JSONObject returnJson = new JSONObject();
        if (!StringUtils.equals("true", sfGd)) {
            returnJson.put("ajh", bdcGdxxService.getMaxAjhByMlh(mlh, false));
        } else {
            returnJson.put("ajh", bdcGdxxService.getMaxAjhByMlh(mlh, true));
        }
        return returnJson;
    }

    /**
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 异步获取归档信息
     */
    @ResponseBody
    @RequestMapping("/getBdcGdxxByGdxxid")
    public JSONObject getBdcGdxxByGdxxid(String gdxxid) {
        JSONObject resultMap = new JSONObject();
        if (StringUtils.isNotBlank(gdxxid)) {
            resultMap = bdcGdxxService.getBdcGdxxByGdxxid(gdxxid);
        }
        return resultMap;
    }


    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 页面直接编辑批量修改
     */
    @ResponseBody
    @RequestMapping("/saveGdGdxxInfoForPl")
    public JSONObject saveGdGdxxInfoForPl(String postData) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(postData)) {
            List<BdcGdxx> bdcGdxxList = JSON.parseArray(postData, BdcGdxx.class);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                for (BdcGdxx bdcGdxx : bdcGdxxList) {
                    bdcGdxx.setGdrq(new Date());
                    bdcGdxx.setGdr(getUserName());
                    bdcGdxxService.saveGdxxForPl(bdcGdxx,getUserId());
                }
            }
            jsonObject.put("msg", "success");
        }
        return jsonObject;
    }

}
