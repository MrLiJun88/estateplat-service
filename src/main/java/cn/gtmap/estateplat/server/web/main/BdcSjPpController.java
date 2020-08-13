package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcSqlxDjsyRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.mapper.BdcZsMapper;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.service.exchange.QzYwxxPageService;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者 xhc
 * 日期 2015/12/16
 * 时间 16:37
 * 大连数据匹配
 */
@Controller
@RequestMapping("/bdcsjpp")
public class BdcSjPpController extends BaseController {
    @Autowired
    QzYwxxPageService qzYwxxPageService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcZsMapper bdcZsMapper;

    /**
     * 跳转到数据匹配页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "todatapic")
    public String toDataPic(Model model) {
        List<Map> djlxList = bdcZdGlService.getDjlxByBdclx(Constants.BDCLX_TDFW);
        List<Map> sqlxList = bdcZdGlService.getSqlxByBdclxDjlx(Constants.BDCLX_TDFW, Constants.DJLX_CSDJ_DM);
        String gdTabOrder = AppConfig.getProperty("gdTab.order");
        List<String> gdTabOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(gdTabOrder) && gdTabOrder.split(",").length > 0) {
            for (String gdTab : gdTabOrder.split(","))
                gdTabOrderList.add(gdTab);
        }
        List<String> sqlxdmList = ReadXmlProps.getUnPpGdfwtdSqlxDm();
        String wfids = bdcZdGlService.getWdidsBySqlxdm(sqlxdmList);
        String gdTabLoadData = AppConfig.getProperty("gdTab.loadData");
        model.addAttribute("gdTabOrderList", gdTabOrderList);
        model.addAttribute("gdTabOrder", gdTabOrder);
        model.addAttribute("gdTabLoadData", gdTabLoadData);
        model.addAttribute("sqlxList", sqlxList);
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("wfids", wfids);
        return "/sjgl/dataPp";
    }

    /**
     * 返回过渡库业务信息
     *
     * @param pageable
     * @param hhSearch
     * @param bdclx
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQzYwxxFwJsonByPage")
    public Object getGdXmFwJsonByPage(Pageable pageable, String bdclx, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Page<HashMap> pageImpl = null;
        //混合查询
        map.put("hhSearch", StringUtils.deleteWhitespace(hhSearch));
        if (bdclx.equals("TDFW")) {
            pageImpl = qzYwxxPageService.getQzFwYwxxPage(pageable, map);
        } else if (bdclx.equals("TD")) {
            pageImpl = qzYwxxPageService.getQzTdYwxxPage(pageable, map);
        } else if (bdclx.equals("TDSL")) {
            pageImpl = qzYwxxPageService.getQzLdYwxxPage(pageable, map);
        } else if (bdclx.equals("TDQT")) {
            pageImpl = qzYwxxPageService.getQzCdYwxxPage(pageable, map);
        } else if (bdclx.equals("HY")) {
            pageImpl = qzYwxxPageService.getQzHyYwxxPage(pageable, map);
        }
        return pageImpl;
    }

    @ResponseBody
    @RequestMapping(value = "creatCsdj")
    public Project creatCsdj(Model model, Project project, String sqlxMc, String lx) {

        if (StringUtils.isNotBlank(lx)) {
            project.setBdclx(lx);
        } else {
            project.setBdclx(Constants.BDCLX_TDFW);
        }
        project.setUserId(super.getUserId());
        //获取权利类型和登记事由、申请类型
        if (StringUtils.isNotBlank(sqlxMc)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
            if (CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                String djsy = "";
                if (map.get(ParamsConstants.QLLXDM_CAPITAL) != null)
                    project.setQllx(map.get(ParamsConstants.QLLXDM_CAPITAL).toString());
                if (map.get(ParamsConstants.SQLXDM_CAPITAL) != null) {
                    project.setSqlx(map.get(ParamsConstants.SQLXDM_CAPITAL).toString());
                    djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                }
                if (StringUtils.isNotBlank(djsy)) {
                    project.setDjsy(djsy);
                } else {
                    if (map.get(ParamsConstants.DJSYDM_CAPITAL) != null) {
                        project.setDjsy(map.get(ParamsConstants.DJSYDM_CAPITAL).toString());
                    }
                }
            }
        }
        Project returnProject = projectService.creatProjectEvent(projectService.getCreatProjectService((BdcXm) project), project);
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        //赋值原bdcqzh
        if (project.getProid() != null) {
            String ybdcqzh = bdcZsMapper.getYbdcqzhByProid(project.getProid());
            if (ybdcqzh != null) {
                bdcXm.setYbdcqzh(ybdcqzh);
                entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
            }
        }
        bdcSjdService.createSjxxByBdcxm(bdcXm);
        if (bdcXm != null) {
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
            projectService.creatZs(turnProjectDefaultService, bdcXm);
        }
        return returnProject;
    }

}
