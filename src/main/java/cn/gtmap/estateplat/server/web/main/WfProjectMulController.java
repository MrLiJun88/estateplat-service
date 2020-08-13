package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: appleForSuncho`
 * Date: 15-12-9
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/wfProjectMul")
public class WfProjectMulController extends BaseController {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private DjsjFwService djsjFwService;

    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcYgService bdcYgService;


    /**
     * zdd
     *
     * @param project
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/initVoFromOldData", method = RequestMethod.GET)
    public String createWfProject(Project project) {
        String msg = "失败";
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            //zdd 一个流程多个项目处理
            if (StringUtils.isNotBlank(project.getDcbIndex())) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("fw_dcb_index", project.getDcbIndex());
                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(map);
                List<String> djsjIds = new ArrayList<String>();
                List<String> bdcdys = new ArrayList<String>();
                if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                    for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                        djsjIds.add(djsjFwHs.getFwHsIndex());
                        bdcdys.add(djsjFwHs.getBdcdyh());
                    }
                }
                project.setBdcdyhs(bdcdys);
                project.setDjIds(djsjIds);
            } else if (project.getDjIds() != null) {
                //当人工选择逻辑幢处理下djIds
                String djIds = project.getDjIds().get(0);
                if (StringUtils.isNotBlank(djIds)) {
                    String[] djAllIds = StringUtils.split(djIds, Constants.SPLIT_STR);
                    List<String> djsjIds = new ArrayList<String>();
                    for (String djId : djAllIds) {
                        djsjIds.add(djId);
                    }
                    project.setDjIds(djsjIds);
                }
                String bdcdyhs = "";
                if (CollectionUtils.isNotEmpty(project.getBdcdyhs()))
                    bdcdyhs = project.getBdcdyhs().get(0);
                if (StringUtils.isNotBlank(bdcdyhs)) {
                    String[] bdcdyhAllIds = StringUtils.split(bdcdyhs, Constants.SPLIT_STR);
                    List<String> bdcdyhIds = new ArrayList<String>();
                    for (String bdcdyh : bdcdyhAllIds) {
                        bdcdyhIds.add(bdcdyh);
                    }
                    project.setBdcdyhs(bdcdyhIds);
                }
            } else {
                if (StringUtils.isNotBlank(project.getBdcdyh())&&project.getBdcdyh().indexOf("/") > -1) {
                    String[] bdcdyList = project.getBdcdyh().split("/");
                    project.setBdcdyh(bdcdyList[0]);
                }
                if (StringUtils.isNotBlank(project.getYbdcdyid())&&project.getYbdcdyid().indexOf("/") > -1) {
                    String[] ybdcdyidList = project.getYbdcdyid().split("/");
                    project.setYbdcdyid(ybdcdyidList[0]);
                }
            }
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
            if (bdcXm != null)
                project = bdcXmService.getProjectFromBdcXm(bdcXm, project);

            if (project!=null&&StringUtils.isNotBlank(project.getYxmid()) && StringUtils.isBlank(project.getQllx())) {
                BdcXm ybdcxm = bdcXmService.getBdcXmByProid(project.getYxmid());
                if (ybdcxm != null)
                    project.setQllx(ybdcxm.getQllx());
            }
            List<InsertVo> insertVoList;
            if (project != null) {
                //获取哪个登记类型service
                CreatProjectService creatProjectService = projectService.getCreatProjectService(project);
                //获取哪个登记类型service
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
                insertVoList = creatProjectService.initVoFromOldDataMul(project);

                if (CollectionUtils.isNotEmpty(insertVoList)) {
                    creatProjectService.saveOrUpdateProjectData(insertVoList);
                }
                bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());

                turnProjectDefaultService.saveQllxVo(bdcXm);

                //               zx 继承预告登记等的收件单材料有原项目的取项目的收件材料
                //针对预告登记
                List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(project.getBdcdyh(), Constants.QLLX_QSZT_XS.toString());
                if (CollectionUtils.isNotEmpty(bdcYgList)) {
                    bdcSjdService.updateSjclFromYpro(project.getProid(), bdcYgList.get(0).getProid());
                }
                if (StringUtils.isNotBlank(project.getYxmid()))
                    bdcSjdService.updateSjclFromYpro(project.getProid(), project.getYxmid());
            }
            msg = "成功";
        }
        return msg;
    }


}
