package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.mapper.GdFwMapper;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.impl.BdcZjjzwServiceImpl;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/12/19
 * @description
 */
public class CreatComplexScdjAndPldyProjectServiceImpl extends CreatProjectDefaultServiceImpl {

    @Resource(name = "creatProjectScdjService")
    CreatProjectService creatProjectScdjService;
    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectService creatProjectDydjService;
    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;


    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    GdFwMapper gdFwMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcZjjzwServiceImpl bdcZjjzwServiceImpl;
    @Autowired
    DjxxMapper djxxMapper;

    @Override
    public List<InsertVo> initVoFromOldData(Xmxx xmxx) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        Project project = xmxx instanceof Project ? (Project) xmxx : null;
        if (project!=null&&StringUtils.isBlank(project.getDjbid())) {
            project.setDjbid(UUIDGenerator.generate18());
        }

        String workflowProid = project!=null&&StringUtils.isNotBlank(project.getWorkflowProid()) ? project.getWorkflowProid() :
                project!=null&&StringUtils.isNotBlank(project.getProid()) ? project.getProid() : "";
        creatProjectNode(workflowProid);

        //商品房首次登记
        Project projectDto = null;
        try {
            projectDto = (Project) BeanUtils.cloneBean(project);
        } catch (Exception e) {
            logger.error("CreatComplexScdjAndPldyProjectServiceImpl.initVoFromOldData",e);
        }
        String scProid = UUIDGenerator.generate18();
        if(projectDto != null){
            projectDto.setQllx(Constants.QLLX_GYTD_FWSUQ);
            projectDto.setSqlx(Constants.SQLX_SPFGYSCDJ_DM);
            projectDto.setDjlx(Constants.DJLX_CSDJ_DM);
            projectDto.setDjsy(Constants.DJSY_GYJSYDSHQ);
            projectDto.setBdclx(Constants.BDCLX_TDFW);
            projectDto.setProid(scProid);
            List<BdcXmRel> csdjBdcXmRelList = getBdcXmRelListFromSysyw(projectDto, projectDto.getYxmid());
            if(CollectionUtils.isNotEmpty(csdjBdcXmRelList)) {
                List<InsertVo> list = initScdj(csdjBdcXmRelList, projectDto);
                if(CollectionUtils.isNotEmpty(list)){
                    saveOrUpdateProjectData(list);
                }
            }
        }


        //批量抵押
        try {
            projectDto = (Project) BeanUtils.cloneBean(project);
        } catch (Exception e) {
            logger.error("CreatComplexScdjAndPldyProjectServiceImpl.initVoFromOldData",e);
        }
        if(projectDto != null){
            projectDto.setQllx(Constants.QLLX_DYAQ);
            projectDto.setDjlx(Constants.DJLX_DYDJ_DM);
            projectDto.setDjsy(Constants.DJSY_DYAQ);
            projectDto.setYbdcqzh("");
            List<BdcXmRel> pldyBdcXmRelList = getBdcXmRelListFromSysyw(projectDto, scProid);
            if(CollectionUtils.isNotEmpty(pldyBdcXmRelList)){
                insertVoList = initPldy(pldyBdcXmRelList,projectDto);
            }
        }

        return insertVoList;
    }

    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param
    * @return
    * @description 商品房首次登记部分
    */
    private List<InsertVo> initScdj(List<BdcXmRel> bdcXmRelList, Project projectDto) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        for (BdcXmRel bdcXmRel : bdcXmRelList) {
            projectDto.setProid(bdcXmRel.getProid());
            projectDto.setYxmid(bdcXmRel.getYproid());
            projectDto.setDjId(bdcXmRel.getQjid());
            String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXmRel.getYproid());
            projectDto.setBdcdyh(bdcdyh);
            List<InsertVo> list = creatProjectScdjService.initVoFromOldData(projectDto);
            if(CollectionUtils.isNotEmpty(list)) {
                insertVoList.addAll(list);
            }
        }
        return insertVoList;
    }

    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param
    * @return
    * @description 批量抵押部分
    */
    private List<InsertVo> initPldy(List<BdcXmRel> bdcXmRelList,Project projectDto) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        projectDto.setXmly(Constants.XMLY_BDC);
        for (BdcXmRel bdcXmRel : bdcXmRelList) {
            projectDto.setProid(bdcXmRel.getProid());
            projectDto.setYxmid(bdcXmRel.getYproid());
            projectDto.setDjId(bdcXmRel.getQjid());
            String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXmRel.getYproid());
            projectDto.setBdcdyh(bdcdyh);
            projectDto = initYbdcdyid(projectDto,bdcXmRel.getYproid());
            List<InsertVo> list = creatProjectDydjService.initVoFromOldData(projectDto);
            if(CollectionUtils.isNotEmpty(list)) {
                insertVoList.addAll(list);
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @param
     * @return
     * @description 用上一手业务初始化xmrellist
     */
    private List<BdcXmRel> getBdcXmRelListFromSysyw(Project project, String sysProid) {
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        List<BdcXmRel> yBdcXmRelList = bdcXmRelService.getSameWFXmRelByproid(sysProid);
        if (CollectionUtils.isNotEmpty(yBdcXmRelList)) {
            String proid = project.getProid();
            for(BdcXmRel bdcXmRel : yBdcXmRelList) {
                bdcXmRel.setYproid(bdcXmRel.getProid());
                bdcXmRel.setProid(proid);
                bdcXmRel.setRelid(UUIDGenerator.generate18());
                bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                proid = UUIDGenerator.generate18();
                bdcXmRelList.add(bdcXmRel);
            }
            if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                project.setBdcXmRelList(bdcXmRelList);
            }
        }
        return bdcXmRelList;
    }

    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param
    * @return
    * @description 设置projec的ybdcdyid
    */
    private Project initYbdcdyid(Project project,String yProid) {
        if(StringUtils.isNotBlank(yProid)) {
            BdcXm yBdcxm = entityMapper.selectByPrimaryKey(BdcXm.class,yProid);
            if(StringUtils.isNotBlank(yBdcxm.getBdcdyid())) {
                project.setYbdcdyid(yBdcxm.getBdcdyid());
            }
        }
        return project;
    }
}
