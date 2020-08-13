package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.GdTdService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 16-12-14
 * @description       不动产登记查封信息
 */
@Controller
@RequestMapping("/bdcCfxx")
public class BdcCfxxController extends BaseController {
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcBdcdyMapper bdcBdcdyMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存查封信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcCfxx", method = RequestMethod.POST)
    public Map saveBdcCfxx(Model model, BdcCf bdcCf) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
            bdcCfService.saveBdcCf(bdcCf);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    public String checkCfsj(){
        HashMap param=new HashMap();
        param.put("qszt",1);
        param.put("cflx", Constants.CFLX_XF);
        param.put("cfkssj","1");
        List<Map> bdcCfList=bdcCfService.queryBdcCfByPage(param);
        HashMap<String, Integer> cfMap=new HashMap();
        HashMap<String, Date> cfComMap=new HashMap();
        if(CollectionUtils.isNotEmpty(bdcCfList)){
            for(int i=0;i<bdcCfList.size();i++){
                Map tempMap=bdcCfList.get(i);
                String bdcdyid= (String) tempMap.get("BDCDYID");
                Date cfkssj= (Date) tempMap.get("CFKSQX");
                if(!cfComMap.containsKey(bdcdyid)){
                    cfComMap.put(bdcdyid,cfkssj);
                    cfMap.put(bdcdyid,0);
                }
                else {
                    if(cfkssj.compareTo((Date) cfComMap.get(bdcdyid))==0){
                        cfMap.put(bdcdyid,(Integer)cfMap.get(bdcdyid)+1);
                    }
                }
            }
        }
        StringBuffer bdcdyBuf=new StringBuffer();
        if(MapUtils.isNotEmpty(cfMap)){
            for (Map.Entry<String, Integer> entry : cfMap.entrySet()) {
                if(entry.getValue()>0){
                    bdcdyBuf.append(entry.getKey()+"$$$$$");
                }
            }
        }
        System.out.println(bdcdyBuf.toString());
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/updateAllCfsx", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateAllCfsx(){
        List<String> bdcBdcdyList=bdcBdcdyMapper.selectAllBdcdyh();
        if(CollectionUtils.isNotEmpty(bdcBdcdyList)){
            for(int i=0;i<bdcBdcdyList.size();i++){
                String bdcdyh=bdcBdcdyList.get(i);
                bdcCfService.updateCfsx("1",bdcdyh,null,null);
            }
        }
        List<Map> gdFwList=gdFwService.selectGdfwNopp();
        if(CollectionUtils.isNotEmpty(gdFwList)){
            for(int j=0;j<gdFwList.size();j++){
                String fwid= (String) gdFwList.get(j).get("FWID");
                bdcCfService.updateCfsx("1",null,fwid,null);
            }
        }

        List<Map> gdTdList= gdTdService.selectGdtdNopp();
        if(CollectionUtils.isNotEmpty(gdTdList)){
            for(int k=0;k<gdTdList.size();k++){
                String tdid= (String) gdTdList.get(k).get("TDID");
                bdcCfService.updateCfsx("1",null,null,tdid);
            }
        }
        return "success";
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 轮候查封转查封
     */
    @ResponseBody
    @RequestMapping(value = "/changeLhcfToCf")
    public Map changeLhcfToCf(String proid, String userid){
        return bdcCfService.changeLhcfToCf(proid, userid);
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 查询修改查封信息
     */
    @ResponseBody
    @RequestMapping(value = "/changeCfxx")
    public Map changeCfxx(String param){
        Map map = PublicUtil.turnStrToMap(param);
        Map returnMap = Maps.newHashMap();
        if (map != null && map.containsKey("bdccf") && map.containsKey("ip")) {
            BdcCf bdcCf = JSON.parseObject(JSON.toJSONString(map.get("bdccf")), BdcCf.class);
            return bdcCfService.changeCfxx(map.get("ip").toString(), bdcCf);
        } else {
            returnMap.put(ParamsConstants.MSG_LOWERCASE, "修改失败！");
        }
        return returnMap;
    }
}
