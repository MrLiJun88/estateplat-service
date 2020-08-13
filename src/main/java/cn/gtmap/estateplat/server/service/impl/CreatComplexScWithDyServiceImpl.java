package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
 * @version 1.1.0, 2016/12/19
 * @description 房屋（构筑物）首次，抵押权首次登记
 */
public class CreatComplexScWithDyServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcDyaqService bdcDyaqService;

    @Resource(name = "creatProjectScdjService")
    CreatProjectScdjServiceImpl  creatProjectScdjService;

    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectDydjServiceImpl creatProjectDydjService;

    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;





    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else
            throw new AppException(4005);
        String workflowProid = "";
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }
        creatProjectNode(workflowProid);
        /**
         * 根据工作流wiid，查找是否存在记录，若存在则删除对应记录
         */
        if (StringUtils.isNotBlank(project.getWiid())) {
            //删除收件信息记录
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }

        //创建登记
        List<GdFwsyq> fwsyqList = null;
        if (StringUtils.isNotBlank(project.getGdproid())) {
            fwsyqList = gdFwService.getGdFwsyqListByGdproid(project.getGdproid(), 0);
        }
        if (StringUtils.isNotBlank(project.getBdclx())) {
            project.setDjlx(Constants.DJLX_HBDJ_DM);
            project.setSqlx("111");
            project.setQllx(Constants.QLLX_GYTD_FWSUQ);
            project.setDjsy(Constants.DJSY_FWSYQ);
            project.setDjbid(project.getBdcdyh().substring(0, 19));
            List<InsertVo> list = creatProjectScdjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;
                    // 创建收件信息
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    if(CollectionUtils.isNotEmpty(fwsyqList)){
                        project.setYbdcqzh(fwsyqList.get(0).getFczh());
                    }
                }
                if( vo instanceof  BdcBdcdy){
                    project.setBdcdyid(((BdcBdcdy) vo).getBdcdyid());
                }
                insertVoList.add(vo);
            }
            //创建抵押流程
            if (project != null && StringUtils.isNotBlank(project.getProid())) {
                project.setProid(UUIDGenerator.generate18());
                project.setSqlx(Constants.SQLX_FWDY_DM);
                project.setQllx(Constants.QLLX_DYAQ);
                project.setDjlx(Constants.DJLX_HBDJ_DM);
                project.setDjsy(Constants.DJSY_DYAQ);
                List<InsertVo> list1 = creatProjectDydjService.initVoFromOldData(project);
                for (InsertVo vo : list1) {
                    if (vo instanceof BdcXm) {
                        BdcXm bdcXm = (BdcXm) vo;
                        // 创建收件信息
                        BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                        if (bdcSjxx != null) {
                            bdcSjxx.setSjxxid(UUIDGenerator.generate());
                            bdcSjxx.setProid(project.getProid());
                            bdcSjxx.setWiid(project.getWiid());
                            insertVoList.add(bdcSjxx);
                        }
                        // 产生新证前抵押流程中原不动产权证号置空
                        bdcXm.setYbdcqzh(null);
                    }
                    insertVoList.add(vo);
                }
            }
        }
        return insertVoList;
    }


    @Override
    @Transactional
    public void saveOrUpdateProjectData(final List<InsertVo> list) {
        //存储分组后的List数据
        List<List<InsertVo>> newList = new ArrayList<List<InsertVo>>();
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        List<String> zdzhhList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        List<String> bdcTdList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            InsertVo vo = list.get(i);
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                logger.error("CreatComplexScWithDyServiceImpl.saveOrUpdateProjectData",e);
            }
            if (StringUtils.isNotBlank(id)&&entityMapper.selectByPrimaryKey(vo.getClass(), id) != null) {
                entityMapper.updateByPrimaryKeySelective(vo);
                continue;
            }
            //zdd 特殊处理BdcBdcdy  不动产单元号不能重复
            if (vo instanceof BdcBdcdy) {
                BdcBdcdy bdcdy = (BdcBdcdy) vo;
                if (bdcdyhList.contains(bdcdy.getBdcdyh())) {
                    continue;
                } else {
                    bdcdyhList.add(bdcdy.getBdcdyh());
                }

                //zdd 特殊处理BdcBdcdjb
            } else if (vo instanceof BdcBdcdjb) {
                BdcBdcdjb bdcdjb = (BdcBdcdjb) vo;
                if (zdzhhList.contains(bdcdjb.getZdzhh())) {
                    continue;
                } else {
                    zdzhhList.add(bdcdjb.getZdzhh());
                }
            } else if (vo instanceof BdcTd) {
                BdcTd bdcTd = (BdcTd) vo;
                if (bdcTdList.contains(bdcTd.getZdzhh())) {
                    continue;
                } else {
                    bdcTdList.add(bdcTd.getZdzhh());
                }
            }
            //如果没有值
            if (map.get(vo.getClass().getSimpleName()) == null) {
                voList = new ArrayList<InsertVo>();
                map.put(vo.getClass().getSimpleName(), voList);
                newList.add(voList);
            } else {
                voList = map.get(vo.getClass().getSimpleName());
            }
            voList.add(vo);
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            for (int j = 0; j < newList.size(); j++) {
                entityMapper.insertBatchSelective(newList.get(j));
            }
        }
    }
}
