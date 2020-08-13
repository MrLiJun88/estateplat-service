package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0, 2016/6/23
 * @author<a href="mailto:jhj@gtmap.cn>jhj</a>
 * @discription 批量换证流程
 */

public class CreatComplexYsbzProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Resource(name = "creatProjectYsbzServiceImpl")
    CreatProjectService creatProjectYsbzServiceImpl;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private BdcdyService bdcdyService;


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project = null;
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (xmxx instanceof Project)
            project = (Project) xmxx;
        if (project != null && StringUtils.isNotBlank(project.getProid()) &&CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
            List<BdcXmRel> bdcXmRelList = project.getBdcXmRelList();
            String workflowProid = "";
            if (StringUtils.isNotBlank(project.getWorkflowProid())) {
                workflowProid = project.getWorkflowProid();
            } else if (StringUtils.isNotBlank(project.getProid())) {
                workflowProid = project.getProid();
            }
            creatProjectNode(workflowProid);
            String proid = project.getProid();
            //zdd 将不动产单元编号前19位作为键   登记薄ID作为值
            HashMap<String,String> djbidMap = new HashMap<String, String>();
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    //zdd 将后台传过来的项目关系信息  初始化到project对象中   便于后面创建单个项目
                    project.setDjId(bdcXmRel.getQjid());
                    project.setYxmid(bdcXmRel.getYproid());
                    project.setGdproid(bdcXmRel.getYproid());
                    project.setXmly(bdcXmRel.getYdjxmly());
                    project.setYqlid(bdcXmRel.getYqlid());
                    //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                    project.setProid(proid);

                    //zdd 不动产单元号获取
                    if (StringUtils.isNotBlank(project.getDjId())){
                        HashMap map = new HashMap();
                        if (StringUtils.isNotBlank(project.getBdclx()))
                            map.put("bdclx", project.getBdclx());
                        map.put("id", project.getDjId());
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if (CollectionUtils.isNotEmpty(bdcdyList)&&bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)!=null){
                            project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                        }
                    }else if (StringUtils.isNotBlank(project.getYxmid())){
                        //否则来源不动产自身
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                        if (bdcBdcdy!=null){
                            project.setBdcdyh(bdcBdcdy.getBdcdyh());
                            djbidMap.put(bdcBdcdy.getBdcdyh().substring(0,19),bdcBdcdy.getDjbid());
                        }
                    }
                    //zdd 批量抵押  登记薄可能不一致
                    if(StringUtils.isNotBlank(project.getBdcdyh())){
                        project.setZdzhh(project.getBdcdyh().substring(0,19));
                        if (djbidMap.containsKey(project.getBdcdyh().substring(0,19))) {
                            project.setDjbid(djbidMap.get(project.getBdcdyh().substring(0, 19)));
                        }else{
                            String djbid = UUIDGenerator.generate18();
                            djbidMap.put(project.getBdcdyh().substring(0,19),djbid);
                            project.setDjbid(djbid);
                        }
                    }
                    List<InsertVo> list = null;
                    list = creatProjectYsbzServiceImpl.initVoFromOldData(project);
                    insertVoList.addAll(list);
                    //zdd 从新赋值proid
                    proid = UUIDGenerator.generate18();
                }
            }
        }
        return insertVoList;
    }
}
