package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
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
 * 选择不动产单元批量发证（商品房批量发证）
 *
 * @author lst
 * @version V1.0, 15-12-24
 */
public class CreatComplexProjectServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {

    @Autowired
    ProjectService projectService;

    /**
     * zdd 此处后续优化 可以通过参数确定调用哪一个服务
     */
    @Resource(name = "creatProjectScdjService")
    CreatProjectService creatProjectScdjService;
    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectService creatProjectDydjService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private EntityMapper entityMapper;


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project = null;
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();

        if (xmxx instanceof Project)
            project = (Project) xmxx;
        if (project != null && StringUtils.isNotBlank(project.getProid()) &&CollectionUtils.isNotEmpty(project.getDjIds())) {
            String wiid = project.getWiid();
            String workflowProid = "";
            if (StringUtils.isNotBlank(project.getWorkflowProid())) {
                workflowProid = project.getWorkflowProid();
            } else if (StringUtils.isNotBlank(project.getProid())) {
                workflowProid = project.getProid();
            }

            creatProjectNode(workflowProid);
            String proid = project.getProid();
            //lst获取收件信息  防止删除后找不到
            if (StringUtils.isNotBlank(wiid)) {
                List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(wiid);
                if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                    insertVoList.addAll(bdcSjxxList);
                }
            }

            //zdd 商品房批量登记选择同一个逻辑撞 下不动产单元 所以登记薄ID 是一致的
            //如果登记簿id等于空给默认值
            if(StringUtils.isBlank(project.getDjbid())){
                project.setDjbid(UUIDGenerator.generate18());
            }

            for (String djId : project.getDjIds()) {
                project.setDjId(djId);
                //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                project.setProid(proid);

                //zdd 不动产单元号获取  暂时只考虑房屋的
                DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(project.getDjId());
                if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                    project.setBdcdyh(djsjFwxx.getBdcdyh());
                }

                List<InsertVo> list = null;
                //判断是否是抵押
                if (StringUtils.isNotBlank(project.getQllx()) && (project.getQllx().equals(Constants.QLLX_DYAQ) || project.getQllx().equals(Constants.QLLX_DYQ))) {
                    list = creatProjectDydjService.initVoFromOldData(project);
                } else {
                    list = creatProjectScdjService.initVoFromOldData(project);
                }

                insertVoList.addAll(list);
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
        return insertVoList;
    }

    @Override
    @Transactional
    public void saveOrUpdateProjectData(final List<InsertVo> dataList) {
        //存储分组后的List数据
        List<List<InsertVo>> newList = new ArrayList<List<InsertVo>>();
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        List<String> zdzhhList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        List<String> bdcTdList = new ArrayList<String>();
        for (int i = 0; i < dataList.size(); i++) {
            InsertVo vo = dataList.get(i);
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                logger.error("CreatComplexProjectServiceImpl.saveOrUpdateProjectData",e);
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
