package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.thread.BdcDyInitInfoThread;
import cn.gtmap.estateplat.server.thread.BdcInitInfoThread;
import cn.gtmap.estateplat.server.thread.BdcThreadEngine;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
 * @version 1.0, ${date}
 * @description: 多线程在建工程多抵多创建项目
 */
public class CreatProjectDydjThreadServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    ProjectService projectService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcThreadEngine bdcThreadEngine;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;


    @Override
    public List<InsertVo> initVoFromOldData(Xmxx xmxx) {
        Project project = null;
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        List<BdcDyInitInfoThread> bdcDyInitInfoThreadList = new ArrayList<BdcDyInitInfoThread>();
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else
            throw new AppException(4005);
        List<BdcXmRel> bdcXmRelList = project.getBdcXmRelList();
        String proid = project.getProid();
        //zdd 将不动产单元编号前19位作为键   登记薄ID作为值
        HashMap<String, String> djbidMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                //jyl组织单个不动产单元信息的project
                List<BdcXmRel> tempLdcXmRelList = new ArrayList<BdcXmRel>();
                bdcXmRel.setProid(proid);
                tempLdcXmRelList.add(bdcXmRel);
                project.setBdcXmRelList(tempLdcXmRelList);
                //zdd 将后台传过来的项目关系信息  初始化到project对象中   便于后面创建单个项目
                project.setDjId(bdcXmRel.getQjid());
                project.setYxmid(bdcXmRel.getYproid());
                project.setYqlid(bdcXmRel.getYqlid());
                project.setXmly(bdcXmRel.getYdjxmly());
                //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                project.setProid(proid);
                //zdd 不动产单元号获取
                if (StringUtils.isNotBlank(project.getDjId())) {
                    HashMap map = new HashMap();
                    if (StringUtils.isNotBlank(project.getBdclx())) {
                        map.put("bdclx", project.getBdclx());
                    }
                    map.put("id", project.getDjId());
                    List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyList)) {
                        if (bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                            project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                        }
                        if (bdcdyList.get(0).containsKey(ParamsConstants.BDCLX_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL) != null) {
                            project.setBdclx(bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL).toString());
                        }
                    } else {
                        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                            DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(project.getDjId());
                            if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                                project.setBdcdyh(djsjFwxx.getBdcdyh());
                            }
                        }
                    }
                } else if (StringUtils.isNotBlank(project.getYxmid())) {
                    //否则来源不动产自身
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                    if (bdcBdcdy != null) {
                        project.setBdclx(bdcBdcdy.getBdclx());
                        project.setBdcdyh(bdcBdcdy.getBdcdyh());
                        project.setZdzhh(bdcBdcdy.getBdcdyh().substring(0, 19));
                        project.setYbdcdyid(bdcBdcdy.getBdcdyid());
                        if (StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                            djbidMap.put(bdcBdcdy.getBdcdyh().substring(0, 19), bdcBdcdy.getDjbid());
                        }
                    }
                }
                //登记薄可能不一致
                if (StringUtils.isNotBlank(project.getBdcdyh())) {
                    project.setZdzhh(project.getBdcdyh().substring(0, 19));
                    if (djbidMap.containsKey(project.getBdcdyh().substring(0, 19))) {
                        project.setDjbid(djbidMap.get(project.getBdcdyh().substring(0, 19)));
                    } else {
                        String djbid = UUIDGenerator.generate18();
                        djbidMap.put(project.getBdcdyh().substring(0, 19), djbid);
                        project.setDjbid(djbid);
                    }
                }
                Project newProject = null;
                try {
                    newProject = (Project) BeanUtils.cloneBean(project);
                } catch (Exception e) {
                    logger.error("CreatProjectDydjThreadServiceImpl.initVoFromOldData",e);
                }
                if(newProject != null){
                    BdcDyInitInfoThread bdcDyInitInfoThread = new BdcDyInitInfoThread(newProject,entityMapper);
                    bdcDyInitInfoThreadList.add(bdcDyInitInfoThread);
                }
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
        bdcThreadEngine.excuteThread(bdcDyInitInfoThreadList);
        return insertVoList;
    }
}
