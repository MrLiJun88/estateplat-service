package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcGg;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcGgService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version V1.0,  不动产公告
 */
@Controller
@RequestMapping("/bdcgg")
public class BdcGgController extends BaseController {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcGgService bdcGgService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    Repo repository;

    @RequestMapping(value = "gg", method = RequestMethod.GET)
    public String list(Model model, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        List<HashMap> ggList = ReadXmlProps.getGgList();
        model.addAttribute("workflowProid", proid);
        model.addAttribute("rid", rid);
        model.addAttribute("taskid", taskid);
        model.addAttribute("from", from);
        model.addAttribute("wiid", wiid);

        List<BdcGg> bdcGgList = null;
        String src = "";

        if (StringUtils.isNotBlank(wiid)) {
            Example example = new Example(BdcGg.class);
            example.createCriteria().andEqualTo("wiid", wiid);
            bdcGgList = entityMapper.selectByExample(example);
        }
        if (CollectionUtils.isEmpty(bdcGgList)) {
            Example example = new Example(BdcGg.class);
            example.createCriteria().andEqualTo("proid", proid);
            bdcGgList = entityMapper.selectByExample(example);
        }

        if (CollectionUtils.isNotEmpty(bdcGgList)) {
            BdcGg bdcGg = bdcGgList.get(0);
            String ggzdDm = "";
            String ggzdMc = "";
            if (CollectionUtils.isNotEmpty(ggList)) {
                for (HashMap ggMap : ggList) {
                    String ggmc = ggMap.get("gglx").toString();
                    ggzdDm = getGgzddm(ggmc);
                    if (StringUtils.equals(ggzdDm, bdcGg.getGglx())) {
                        src = getSrc( wiid, proid, ggMap);
                    }
                    if (StringUtils.isNotBlank(src)) {
                        break;
                    }
                }
                bdcGg.setGglx(ggzdMc);
                model.addAttribute("gglist", ggList);
            }
        } else {
            model.addAttribute("gglist", ggList);
            String scdjgg=AppConfig.getProperty("scdjgg.filepath");
            if (StringUtils.isNotBlank(scdjgg)) {
                String[] scdjggs=scdjgg.split(":");
                src = this.bdcdjUrl + "/bdcdjGg?wiid=" + wiid + "&proid=" + proid + "&fileName=" + scdjggs[0] + "&ggmc=" + scdjggs[1];
            } else {
                if (CollectionUtils.isNotEmpty(ggList)) {
                    HashMap ggMap = ggList.get(0);
                    src = getSrc( wiid, proid, ggMap);
                }
            }
        }
        model.addAttribute("src", src);
        return "main/gg";
    }

    public String getGgzddm(String ggmc) {
        String ggzdDm = null;
        if (StringUtils.isNotBlank(ggmc)) {
            HashMap ggzdMap = new HashMap();
            ggzdMap.put("mc", ggmc);
            HashMap ggzd = repository.selectOne("getGglxdmByGgmc", ggzdMap);
            ggzdDm = ggzd.get("DM").toString();
        }
        return ggzdDm;
    }

    public String getSrc(String wiid, String proid, HashMap ggMap) {
        String ggzdMc = null;
        String src = null;
        ggzdMc = ggMap.get("gglx").toString();
        String fileName = ggMap.get("ftl").toString();
        if(StringUtils.isNotBlank(fileName)){
            src = this.bdcdjUrl + "/bdcdjGg?wiid=" + wiid + "&proid=" + proid + "&fileName=" + fileName + "&ggmc=" + ggzdMc;
        }
        return src;
    }

    @ResponseBody
    @RequestMapping(value = "getBdcGgBh", method = RequestMethod.GET)
    public String getBdcGgBh(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "userid", required = false) String userid) {
        String bdcGgBh = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(userid)) {
                bdcGgBh = bdcGgService.getBdcGgBh(bdcXm, userid);
            }
        }
        return bdcGgBh;
    }

    @ResponseBody
    @RequestMapping(value = "/getGglxdmByGgmc")
    public String saveGglx(String ggFtl) {
        List<HashMap> ggList = ReadXmlProps.getGgList();
        String ggzdDm = "";
        if (CollectionUtils.isNotEmpty(ggList)) {
            for (HashMap ggMap : ggList) {
                if (ggMap.get("ftl") != null) {
                    String ggCptFromXml = ggMap.get("ftl").toString();
                    if (StringUtils.isNotBlank(ggCptFromXml) && StringUtils.equals(ggCptFromXml, ggFtl)&&ggMap.get("gglx") != null) {
                        String ggmc = CommonUtil.formatEmptyValue(ggMap.get("gglx"));
                        if (StringUtils.isNotBlank(ggmc)) {
                            HashMap ggzdMap = new HashMap();
                            ggzdMap.put("mc", ggmc);
                            HashMap ggzd = repository.selectOne("getGglxdmByGgmc", ggzdMap);
                            if (ggzd != null && StringUtils.isNotBlank(ggzd.get("DM").toString())) {
                                ggzdDm = ggzd.get("DM").toString();
                            }
                        }
                    }
                }
            }
        }

        return ggzdDm;
    }
}
