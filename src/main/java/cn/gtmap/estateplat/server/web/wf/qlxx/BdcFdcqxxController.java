package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 16-12-14
 * @description       不动产登记房地产权信息
 */
@Controller
@RequestMapping("/bdcFdcqxx")
public class BdcFdcqxxController extends BaseController {

    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    DjsjFwService djsjFwService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private QllxService qllxService;

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存房地产权信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcFdcqxx", method = RequestMethod.POST)
    public Map saveBdcFdcqxx(Model model, BdcFdcq bdcFdcq, BdcSpxx bdcSpxx, BdcXm bdcXm) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcFdcq != null && StringUtils.isNotBlank(bdcFdcq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcFdcqService.saveBdcFdcq(bdcFdcq);
            bdcSpxx.setMj(bdcFdcq.getJzmj());
            bdcSpxx.setYt(bdcFdcq.getGhyt());
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @return
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params
     * @description 根据权籍fwhs表更新fdcq和spxx数据
     */
    @ResponseBody
    @RequestMapping(value = "/updateFdcqAndSpxxByDjsjfwhs")
    public Map updateFdcqAndSpxxByDjsjfwhs(@RequestParam(value = "proid", required = false) String proid) {
        Map reusltMap = Maps.newHashMap();
        String returnvalue = ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                for (BdcXm xm : bdcXmList) {
                    String bdcdyh = bdcdyService.getBdcdyhByProid(xm.getProid());
                    String bdclx = bdcdyService.getBdclxByPorid(xm.getProid());
                    if (StringUtils.isNotBlank(bdcdyh) && StringUtils.isNotBlank(bdclx)) {
                        ProjectPar projectPar = new ProjectPar();
                        projectPar.setProid(xm.getProid());
                        projectPar.setDjh(StringUtils.substring(bdcdyh,0,19));
                        String djid = bdcDjsjService.getDjidByBdcdyh(bdcdyh, bdclx);
                        if (StringUtils.isNotBlank(djid)) {
                            projectPar.setQjid(djid);
                        }
                        bdcSpxxService.updateSpxxByDjsj(projectPar);
                        qllxService.updateQlxxByDjsj(projectPar);
                        returnvalue = ParamsConstants.SUCCESS_LOWERCASE;
                    }
                }
            }
        }
        reusltMap.put(ParamsConstants.MESSAGE_LOWERCASE, returnvalue);
        return reusltMap;
    }
}
