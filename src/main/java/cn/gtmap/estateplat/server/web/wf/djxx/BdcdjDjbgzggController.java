package cn.gtmap.estateplat.server.web.wf.djxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcdjGg")
public class BdcdjDjbgzggController extends BaseController {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcGgService bdcGgService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcZdQllxService bdcZdQllxService;

    private static final FastDateFormat SLBH_DATE_FORMAT = FastDateFormat.getInstance("yyyy年MM月dd日");

    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, String fileName,String ggzdDm, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid,String ggmc) {
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isEmpty(bdcXmList)) {
                throw new NullPointerException("未创建项目");
            }
            List<HashMap<String, Object>> returnValueList = new LinkedList<HashMap<String, Object>>();
            List<BdcGg> bdcGgList = null;
            Date date = null;
            for (BdcXm bdcXm : bdcXmList) {
                Map<String, Object> param = new HashMap<String, Object>();
                HashMap<String, Object> returnValue = new HashMap<String, Object>();
                param.put("proid", bdcXm.getProid());
                bdcGgList = bdcGgService.getBdcGg(param);
                param.put("qlrlx", Constants.QLRLX_QLR);
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrList(param);
                Example bdcspxxExample = new Example(BdcSpxx.class);
                bdcspxxExample.createCriteria().andEqualTo("proid", bdcXm.getProid());
                List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(bdcspxxExample);
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                returnValue.put("bdcZs", CollectionUtils.isNotEmpty(bdcZsList) ? bdcZsList.get(0) : new BdcZs());
                returnValue.put("bdcSpxx", CollectionUtils.isNotEmpty(bdcSpxxList) ? bdcSpxxList.get(0) : new BdcSpxx());
                returnValue.put("bdcQlrList", CollectionUtils.isNotEmpty(bdcQlrList) ? bdcQlrList : "");
                returnValue.put("bdcXm", bdcXm);
                returnValueList.add(returnValue);
                if (date == null) {
                    date = CollectionUtils.isNotEmpty(bdcGgList) ? bdcGgList.get(0).getKssj() : bdcXm.getCjsj();
                }
            }
            List<BdcZdQllx> qllxList = bdcZdQllxService.getAllBdcZdQllx();
            List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
            BdcGg bdcGg = null;
            if (CollectionUtils.isNotEmpty(bdcGgList)) {
                bdcGg = bdcGgList.get(0);
            } else {
                String ggdw=AppConfig.getProperty("ggdw");
                bdcGg= createBdcGg(ggzdDm,ggmc,ggdw,bdcXmList.get(0),PlatformUtil.getCurrentUserId());
            }
            model.addAttribute("bdcGg", bdcGg);
            model.addAttribute("fwytList", fwytList);
            model.addAttribute("qllxList", qllxList);
            model.addAttribute("resultList", returnValueList);
            model.addAttribute("proid", proid);
            model.addAttribute("wiid", wiid);
            model.addAttribute("userid", PlatformUtil.getCurrentUserId());
            model.addAttribute("date", DateUtils.formatTime(date, SLBH_DATE_FORMAT));
        }
        String path = "wf/core/" + dwdm + "/djxx/bdcdjDjbgzgg";
        if (StringUtils.isNotBlank(fileName)) {
            path = "wf/core/" + dwdm + "/djxx/" + fileName;
        }
        return path;
    }

    public BdcGg createBdcGg(String ggzdDm,String ggmc,String ggdw,BdcXm bdcXm,String userid) {
        BdcGg bdcGg = new BdcGg();
        bdcGg.setGgid(UUIDGenerator.generate18());
        bdcGg.setGglx(ggzdDm);
        bdcGg.setGgmc(ggmc);
        bdcGg.setGgdw(ggdw);
        bdcGg.setKssj(bdcXm.getCjsj());
        bdcGg.setJssj(DateUtils.addDays(bdcXm.getCjsj(),15));
        bdcGg.setGgqx(15);
        bdcGg.setWiid(bdcXm.getWiid());
        bdcGg.setProid(bdcXm.getProid());
        String bdcGgBh = bdcGgService.getBdcGgBh(bdcXm,userid);
        bdcGg.setGgbh(bdcGgBh);
        return bdcGg;
    }


    @ResponseBody
    @RequestMapping(value = "/saveGg", method = RequestMethod.POST)
    public Map saveSlxx(Model model, BdcGg bdcGg,BdcXm bdcXm) {
        HashMap<String, Object> map = Maps.newHashMap();
        if (bdcGg==null) {
            map.put("msg", "fail");
            return map;
        }
        entityMapper.saveOrUpdate(bdcGg,bdcGg.getGgid());
        map.put("msg", "success");
        return map;
    }
}
