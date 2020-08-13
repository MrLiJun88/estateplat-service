package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcSjxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.mapper.BdcZsMapper;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
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

/**
 * 收件模式数据匹配
 * 作者 xhc
 * 日期 2015/12/18
 * 时间 19:34
 */
@Controller
@RequestMapping("/todatasjpp")
public class BdcDataSjPpController extends BaseController {
    @Autowired
    QzYwxxPageService qzYwxxPageService;
    @Autowired
    DjxxMapper djxxMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcZsMapper bdcZsMapper;

    private static final String PARAMETER_HHSEARCH = "hhSearch";

    /**
     * 跳转到数据匹配页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "todatapic")
    public String toDataPic(Model model) {
        String gdTabOrder = AppConfig.getProperty("gdTab.order");
        List<String> gdTabOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(gdTabOrder) && gdTabOrder.split(",").length > 0) {
            for (String gdTab : gdTabOrder.split(","))
                gdTabOrderList.add(gdTab);
        }
        String gdTabLoadData = AppConfig.getProperty("gdTab.loadData");
        model.addAttribute("gdTabOrderList", gdTabOrderList);
        model.addAttribute("gdTabOrder", gdTabOrder);
        model.addAttribute("gdTabLoadData", gdTabLoadData);
        return "/sjgl/dataSjPp";
    }

    /**
     * 返回过渡库业务信息
     *
     * @param page
     * @param rows
     * @param hhSearch
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQzYwxxFwJsonByPage")
    public Object getGdXmFwJsonByPage(Pageable pageable, String hhSearch, String bdclx) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Page<HashMap> pageImpl = null;
        //混合查询
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
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

    /**
     * 不动产项目分页数据
     *
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcXmPagesJson")
    public Object getBdcDyhPagesJson(Pageable pageable, String hhSearch) {
        HashMap map = new HashMap();
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        return repository.selectPaging("queryBdcXmJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcDyhPagesJson")
    public String getBdcDyhPagesJson(String hhSearch, String bdclx) {
        HashMap map = new HashMap();
        map.put("bdclx", bdclx);
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        return djxxMapper.getDjid(map);
    }

    @ResponseBody
    @RequestMapping(value = "creatCsdj")
    public String creatCsdj(Project project, String lx) {
        if (project == null)
            throw new NullPointerException();
        String msg = "创建成功";
        if (StringUtils.isNotBlank(lx)) {
            project.setBdclx(lx);
        } else {
            project.setBdclx(Constants.BDCLX_TDFW);
        }
        project.setUserId(super.getUserId());
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        if (bdcXm == null)
            throw new NullPointerException();

        project = bdcXmService.getProjectFromBdcXm(bdcXm, project);
        project.setWiid(bdcXm.getWiid());
        List<InsertVo> insertVoList;

        //获取哪个登记类型service
        CreatProjectService creatProjectService = projectService.getCreatProjectService((BdcXm) project);
        insertVoList = creatProjectService.initVoFromOldData(project);
        if (bdcXm.getWiid() != null && !bdcXm.getWiid().equals("")) {
            List<BdcSjxx> sjxxList = bdcSjdService.queryBdcSjdByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(sjxxList))
                insertVoList.addAll(sjxxList);
        }
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            creatProjectService.insertProjectData(insertVoList);
        }
        bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        //赋值原bdcqzh
        if (project.getProid() != null) {
            String ybdcqzh = bdcZsMapper.getYbdcqzhByProid(project.getProid());
            if (ybdcqzh != null) {
                bdcXm.setYbdcqzh(ybdcqzh);
                entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
            }
        }
        if (bdcXm != null) {
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
            projectService.creatZs(turnProjectDefaultService, bdcXm);
        } else {
            msg = "创建失败";
        }
        return msg;
    }
}
