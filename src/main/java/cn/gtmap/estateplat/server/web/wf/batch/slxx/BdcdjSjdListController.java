package cn.gtmap.estateplat.server.web.wf.batch.slxx;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcdjSjdList")
public class BdcdjSjdListController extends BaseController {

    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcSjxxService bdcSjxxService;

    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, String wiid,String proid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<Map<String, Object>> returnValueList = new LinkedList<Map<String, Object>>();
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            String sqsreportName = "bdc_sjd.cpt";
            String djlx = null;
            HashMap<String,Object> sqlxMap=new HashMap<String,Object>();
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    djlx = StringUtils.isBlank(djlx) ? bdcXm.getDjlx() : djlx;
                    HashMap<String, Object> returnValue = new HashMap<String, Object>();
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                    List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                    BdcSpxx bdcSpxx =bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                    BdcSjxx bdcSjxx=bdcSjxxService.queryBdcSjxxByProid(bdcXm.getProid());
                    sqlxMap.put("dm",bdcXm.getSqlx());
                    List<HashMap> zdSqlx=bdcZdGlService.getBdcZdSqlx(sqlxMap);
                    List<HashMap> djzx=bdcZdGlService.getDjzxByProid(bdcXm.getProid());
                    returnValue.put("sqlxMc", getMc(zdSqlx));
                    returnValue.put("djzxMc", getMc(djzx));
                    returnValue.put("bdcSjxx",bdcSjxx!=null?bdcSjxx:new BdcSjxx());
                    returnValue.put("bdcQlrList",bdcQlrList);
                    returnValue.put("bdcYwrList",bdcYwrList);
                    returnValue.put("bdcSpxx", bdcSpxx);
                    returnValue.put("bdcXm", bdcXm);
                    returnValue.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    returnValueList.add(returnValue);
                    returnValue.put("index",returnValueList.indexOf(returnValue)+1);
                }
            }
            List<Map> djsyMcList = bdcZdGlService.getComDjsy();
            model.addAttribute("djsyMcList", djsyMcList);
            model.addAttribute("sqsreportName", sqsreportName);
            model.addAttribute("proid", proid);
            model.addAttribute("wiid", wiid);
            model.addAttribute("returnValueList", returnValueList);
        }
        return "wf/core/" + dwdm + "/batch/bdcdjSjdList";
    }

    /**
     *
     * @param zd
     * @return
     */
    public String getMc( List<HashMap>  zd){
        String mc=null;
        if(CollectionUtils.isNotEmpty(zd)){
            mc=(String)zd.get(0).get("MC");
        }
        return mc;
    }

}
