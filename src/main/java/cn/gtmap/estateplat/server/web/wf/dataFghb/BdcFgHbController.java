package cn.gtmap.estateplat.server.web.wf.dataFghb;

import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.core.service.BdcFgHbService;
import cn.gtmap.estateplat.server.service.split.BdcSplitService;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/12
 * @description 不动产数据分割合并功能
 */
@Controller
@RequestMapping("/bdcFghb")
public class BdcFgHbController extends BaseController {
    @Autowired
    private BdcFgHbService bdcFgHbService;
    @Autowired
    private BdcSplitService bdcSplitService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String initFghb(Model model, String proids) {
        return bdcFgHbService.initBdcFghb(model, proids);
    }

    @RequestMapping(value = "ckxx", method = RequestMethod.GET)
    public String ckxx(Model model, String wiid) {
        return bdcFgHbService.initCkxx(model, wiid);
    }

    /**
     * @param proids
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 验证是否存在项目内多幢
     */
    @ResponseBody
    @RequestMapping(value = "/checkExistFdcqDz")
    public Map checkExistFdcqDz(String proids){
        return bdcFgHbService.checkExistFdcqDz(proids);
    }

    /**
     * @param proids
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 合并数据
     */
    @ResponseBody
    @RequestMapping(value = "/combine")
    public Map combine(String proids,String bdcdyh,String qjid){
        return bdcFgHbService.combineBdcData(proids,bdcdyh,qjid,PlatformUtil.getCurrentUserId());
    }

    /**
     * @param splitDatas
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 拆分数据
     */
    @ResponseBody
    @RequestMapping(value = "/split")
    public Map split(String splitDatas){
        Map map = null;
        if(StringUtils.isNotBlank(splitDatas)){
            List<Map> splitMaps = JSON.parseArray(splitDatas,Map.class);
            map = bdcFgHbService.splitBdcData(splitMaps,PlatformUtil.getCurrentUserId());
        }
        return map;
    }

    /**
     * @param proids
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取合并详情信息
     */
    @ResponseBody
    @RequestMapping(value = "/getHbDetailInfo")
    public Map getHbDetailInfo(String proids){
        return bdcFgHbService.getHbDetailInfo(proids);
    }

    /**
     * @param splitDatas
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取拆分详情信息
     */
    @ResponseBody
    @RequestMapping(value = "/getFgDetailInfo")
    public Map getFgDetailInfo(String splitDatas){
        Map map = null;
        if(StringUtils.isNotBlank(splitDatas)){
            List<Map> splitMaps = JSON.parseArray(splitDatas,Map.class);
            map = bdcFgHbService.getFgDetailInfo(splitMaps);
        }
        return map;
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 拆分
     */
    @ResponseBody
    @RequestMapping(value = "/bdcSplit")
    public Map bdcSplit(String json, String proid){
        Map map = Maps.newHashMap();
        List<SplitNum> splitNumList = JSON.parseArray(json, SplitNum.class);
        return bdcSplitService.bdcSplit(splitNumList,proid);
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 土地拆分录入信息页面
     */
    @RequestMapping(value = "/tdSplit", method = RequestMethod.GET)
    public String tdSplit(Model model, String proid){
        return bdcSplitService.initTdSplit(model,proid);
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据proid获取分割id
     */
    @ResponseBody
    @RequestMapping("/getFgidByproid")
    public Map<String, Object> getFgidByproid(String proid) {
        return bdcSplitService.getFgidByproid(proid);
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 撤回分割
     */
    @ResponseBody
    @RequestMapping("/withdrawFg")
    public String withdrawFg(String fgid) {
        return bdcSplitService.revokeBdcSplit(fgid);
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 验证是否可以进行撤回
     */
    @ResponseBody
    @RequestMapping("/validateWithdrawFg")
    public Map validateWithdrawFg(String fgid) {
        return bdcSplitService.validateWithdrawFg(fgid);
    }
}
