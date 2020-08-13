package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZddbService;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/3/8
 * @description 字段对比
 */
@Controller
@RequestMapping("/bdcZddb")
public class BdcZddbController extends BaseController {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZddbService bdcZddbService;


    @ResponseBody
    @RequestMapping(value = "/getBdcZddbJg")
    public String getBdcZddbJg(String proid, String zdmc) {
        String result = "true";
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(zdmc)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                HashMap<String, List<String>> listHashMap = ReadXmlProps.getSqlxDbzdMap();
                if (listHashMap != null && StringUtils.isNotBlank(bdcXm.getSqlx())) {
                    List<String> zdmcList = listHashMap.get(bdcXm.getSqlx());
                    if (CollectionUtils.isNotEmpty(zdmcList)&&zdmcList.contains(zdmc)) {
                        if (StringUtils.equals(zdmc, "dzwyt"))
                            result = bdcZddbService.getBdcZddbJgByDzwyt(proid);
                        else if (StringUtils.equals(zdmc, "zdzhyt")) {
                            result = bdcZddbService.getBdcZddbJgByZdzhyt(proid);
                        } else if (StringUtils.equals(zdmc, "djzwmj")) {
                            result = bdcZddbService.getBdcZddbJgByDjzwmj(proid);
                        } else if (StringUtils.equals(zdmc, "zdzhmj")) {
                            result = bdcZddbService.getBdcZddbJgByZdzhmj(proid);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param proid
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getHistoryZd")
    public Map<String, Object> getHistoryZd(String proid,String zdbInfo) {
        Map<String, Object> result = null;
        Map<String, Object> zdvalue = null;
        Set<String> mcSet = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                HashMap<String, HashMap<String, String>> listHashMap = ReadXmlProps.getSqlxZdMap();
                if (listHashMap != null && StringUtils.isNotBlank(bdcXm.getSqlx())) {
                    HashMap<String, String> zdmcMap = listHashMap.get(bdcXm.getSqlx());
                    if (null != zdmcMap) {
                        zdvalue = new HashMap<String, Object>();
                        result = new HashMap<String, Object>();
                        mcSet = new HashSet<String>();
                        Map<String, Object> zdxx = bdcZddbService.getZdxx(proid,Boolean.valueOf(zdbInfo));
                        Map<String, Object> fwxx = bdcZddbService.getFwxx(proid);
                        if (null == zdxx && null == fwxx) {
                            throw new NullPointerException("权籍信息为空！");
                        }
                        for (Map.Entry zdmc : zdmcMap.entrySet()) {
                            String qjmc = (String) zdmc.getKey();
                            String mc = (String) zdmc.getValue();
                            if(zdxx != null){
                                Object value = zdxx.get(mc);
                                if (null == value) {
                                    value = fwxx.get(mc);
                                }
                                zdvalue.put(qjmc, value);
                            }
                            mcSet.add(qjmc);
                        }
                        result.put("zdvalue", zdvalue);
                        result.put("zdmc", mcSet);
                    }
                }
            }
        }
        return result;
    }
}


