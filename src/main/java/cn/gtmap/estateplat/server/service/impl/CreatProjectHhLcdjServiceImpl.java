package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

/**
 * @version 1.0, 2017/9/25.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description 混合流程，目前用于苏州批量查封和批量抵押
 */
public class CreatProjectHhLcdjServiceImpl extends CreatProjectDefaultServiceImpl {

    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    DjsjFwService djsjFwService;

    @Resource(name = "creatProjectCfdjServiceImpl")
    CreatProjectService creatProjectCfdjServiceImpl;

    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectService creatProjectDydjService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project = null;
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if(xmxx instanceof Project) {
            project = (Project) xmxx;
        }

        if(project != null && CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
            HashMap hashMap = new HashMap();
            //相同不动产单元号的归为一类
            for(BdcXmRel bdcXmRel : project.getBdcXmRelList()) {
                if(StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                    if(hashMap.containsKey(bdcXmRel.getQjid())) {
                        List<BdcXmRel> bdcXmRelList = (List<BdcXmRel>) hashMap.get(bdcXmRel.getQjid());
                        bdcXmRelList.add(bdcXmRel);
                        hashMap.put(bdcXmRel.getQjid(), bdcXmRelList);
                    } else {
                        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
                        bdcXmRelList.add(bdcXmRel);
                        hashMap.put(bdcXmRel.getQjid(), bdcXmRelList);
                    }
                } else {
                    List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
                    bdcXmRelList.add(bdcXmRel);
                    hashMap.put(bdcXmRel, bdcXmRelList);
                }
            }
            String proid = project.getProid();
            if(hashMap != null && hashMap.size() > 0) {
                Iterator iterator = hashMap.entrySet().iterator();
                while(iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    List<BdcXmRel> bdcXmRelList = (List<BdcXmRel>) entry.getValue();
                    StringBuilder yproids = new StringBuilder();
                    StringBuilder yqlids = new StringBuilder();
                    if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        project.setXmly(bdcXmRelList.get(0).getYdjxmly());
                        project.setDjId(bdcXmRelList.get(0).getQjid());
                        for(BdcXmRel xmRel : bdcXmRelList) {
                            if(StringUtils.isNotBlank(xmRel.getYproid())){
                                if(StringUtils.isBlank(yproids)) {
                                    yproids.append(xmRel.getYproid());
                                } else {
                                    yproids.append(",").append(xmRel.getYproid());
                                }
                            }
                            if(StringUtils.isNotBlank(xmRel.getYqlid())){
                                if(StringUtils.isBlank(yqlids)) {
                                    yqlids.append(xmRel.getYqlid());
                                } else {
                                    yqlids.append(",").append(xmRel.getYqlid());
                                }
                            }
                        }
                    }
                    project.setBdcXmRelList(bdcXmRelList);
                    project.setYxmid(yproids.toString());
                    project.setYqlid(yqlids.toString());
                    project.setProid(proid);
                    if(StringUtils.isNotBlank(project.getDjId())) {
                        HashMap map = new HashMap();
                        if(StringUtils.isNotBlank(project.getBdclx())) {
                            map.put("bdclx", project.getBdclx());
                        }
                        map.put("id", project.getDjId());
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if(CollectionUtils.isNotEmpty(bdcdyList)) {
                            if(bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                                project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                            }
                            if(bdcdyList.get(0).containsKey(ParamsConstants.BDCLX_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL) != null) {
                                project.setBdclx(bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL).toString());
                            }
                        } else {
                            if(StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                                DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(project.getDjId());
                                if(djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                                    project.setBdcdyh(djsjFwxx.getBdcdyh());
                                }
                            }
                        }
                    } else if(StringUtils.isNotBlank(project.getYxmid()) && StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
                        //否则来源不动产自身
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                        if(bdcBdcdy != null) {
                            project.setBdclx(bdcBdcdy.getBdclx());
                            project.setBdcdyh(bdcBdcdy.getBdcdyh());
                            project.setZdzhh(bdcBdcdy.getBdcdyh().substring(0, 19));
                            project.setYbdcdyid(bdcBdcdy.getBdcdyid());
                        }
                    }

                    //登记薄可能不一致
                    if(StringUtils.isNotBlank(project.getBdcdyh())) {
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
                        project.setZdzhh(project.getBdcdyh().substring(0, 19));
                        if(bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getDjbid())) {
                            project.setDjbid(bdcBdcdy.getDjbid());
                        } else {
                            project.setDjbid(UUIDGenerator.generate18());
                        }
                    }
                    CreatProjectService createProjectService=null;
                    if(StringUtils.equals(project.getDjlx(),Constants.DJLX_CFDJ_DM)) {
                        createProjectService = creatProjectCfdjServiceImpl;
                    }
                    else if(StringUtils.equals(project.getDjlx(),Constants.DJLX_DYDJ_DM)) {
                        createProjectService = creatProjectDydjService;
                    }

                    if(createProjectService != null){
                        List<InsertVo> list = createProjectService.initVoFromOldData(project);
                        if(CollectionUtils.isNotEmpty(list)) {
                            insertVoList.addAll(list);
                        }
                    }
                    proid = UUIDGenerator.generate18();
                }
            }

        }
        return insertVoList;
    }
}
