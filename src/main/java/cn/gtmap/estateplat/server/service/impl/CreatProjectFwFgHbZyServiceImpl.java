package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zx
 * @version 1.0, 2016/7/28
 * @description 房屋分割、合并转移登记
 */
public class CreatProjectFwFgHbZyServiceImpl extends CreatProjectDefaultServiceImpl{
    @Autowired
    ProjectService projectService;

    /**
     * zdd 此处后续优化 可以通过参数确定调用哪一个服务
     */
    @Resource(name = "creatProjectZydjService")
    CreatProjectService creatProjectZydjService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;




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

            if (StringUtils.isNotBlank(project.getWiid())) {
                List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
                if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                    bdcSjdService.delBdcSjxxByWiid(project.getWiid());
                }
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
                        if ((StringUtils.isNotBlank(project.getSqlx())&&!StringUtils.equals(project.getSqlx(), Constants.SQLX_FWFGHBZY_DM))&&StringUtils.isNotBlank(project.getBdclx()))
                            map.put("bdclx", project.getBdclx());
                        map.put("id", project.getDjId());
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if (CollectionUtils.isNotEmpty(bdcdyList)){
                            if (bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)!=null) {
                                project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                            }
                            if (bdcdyList.get(0).containsKey(ParamsConstants.BDCLX_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL)!=null) {
                                project.setBdclx(bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL).toString());
                            }
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
                    project.setBdcdyid(null);
                    if(StringUtils.equals(project.getBdclx(),Constants.BDCLX_TDFW)) {
                        project.setXmly(Constants.XMLY_FWSP);
                    }
                    else {
                        project.setXmly(Constants.BDCLX_TDFW);
                    }
                    list = creatProjectZydjService.initVoFromOldData(project);
                    //为bdc_xm 附权利类型
                    if(CollectionUtils.isNotEmpty(list)){
                        String bdclx = project.getBdclx();
                        if(StringUtils.isNotBlank(bdclx)){
                            List<InsertVo> qlrVo = new ArrayList<InsertVo>();
                            for(InsertVo insertVo : list){
                                if(insertVo instanceof BdcXm){
                                    BdcXm bdcXm = (BdcXm) insertVo;
                                    for(InsertVo vo : list){
                                        if(vo instanceof BdcSpxx){
                                            BdcSpxx bdcSpxx = (BdcSpxx) vo;
                                            if(StringUtils.equals(bdcXm.getProid(),bdcSpxx.getProid()) && StringUtils.isNotBlank(bdcSpxx.getBdcdyh())){
                                                String qllx=bdcdyService.getQllxFormBdcdy(bdcSpxx.getBdcdyh());
                                                bdcXm.setQllx(qllx);
                                                break;
                                            }
                                        }
                                    }
                                    if(StringUtils.isBlank(bdcXm.getQllx())) {
                                        if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                                            bdcXm.setQllx(Constants.QLLX_GYTD_FWSUQ);
                                        } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                                            bdcXm.setQllx(Constants.QLLX_GYTD_JSYDSYQ);
                                        }
                                    }
                                    if (StringUtils.isNotBlank(bdcXm.getQllx())) {
                                        String djsy = bdcXmService.getBdcQllxDjsyRel(bdcXm.getQllx());
                                        if (StringUtils.isNotBlank(djsy)) {
                                            bdcXm.setDjsy(djsy);
                                        }
                                    }
                                    //将原不动产权证号制空，通过7关联项目生成
                                    String autoRelateZs = AppConfig.getProperty("fghb.autoRelateZs");
                                    if(!StringUtils.equals(autoRelateZs,"true")){
                                        bdcXm.setYbdcqzh("");
                                    }
                                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                                    if (bdcSjxx != null) {
                                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                        bdcSjxx.setProid(project.getProid());
                                        bdcSjxx.setWiid(project.getWiid());
                                        insertVoList.add(bdcSjxx);
                                    }
                                }
                                //删除权利人，权利人从关联的证书上读取
                                if(insertVo instanceof BdcQlr){
                                    qlrVo.add(insertVo);
                                }
                            }
                            list.removeAll(qlrVo);
                        }
                    }
                    //取权籍的权利人
                    List<BdcQlr> bdcQlrList = null;
                    //zdd 获取地籍数据
                    InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
                    bdcQlrList = initBdcQlrFromDjsj(initVoFromParm.getProject(), initVoFromParm.getDjsjFwQlrList(),initVoFromParm.getDjsjLqxx(),initVoFromParm.getDjsjZdxxList(),initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getCbzdDcb(), Constants.QLRLX_QLR);
                    if(CollectionUtils.isNotEmpty(bdcQlrList)) {
                        insertVoList.addAll(bdcQlrList);
                    }
                    insertVoList.addAll(list);
                    //zdd 从新赋值proid
                    proid = UUIDGenerator.generate18();
                }
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
                logger.error("CreatProjectFwFgHbZyServiceImpl.saveOrUpdateProjectData",e);
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
