package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * zx 用于创建商品房共有业主工作流程处理的服务
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-28
 */
public class CreatProjectSpfGyyzServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    ProjectService projectService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcQlrService bdcQlrService;

    /**
     * zdd 可能是采用一个流程一个收件   也可能是一个项目一个收件
     *
     * @param proid 项目ID
     * @return
     */
    @Override
    public Integer creatProjectNode(final String proid) {
        return null;
    }

    @Override
    public List<InsertVo> initVoFromOldData(Xmxx xmxx) {
        Project project;
        if (xmxx != null) {
            project = (Project) xmxx;
        }else
            throw new AppException(4005);
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        list.add(bdcxm);
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatMulBdcXmRelFromProject(project);
        list.addAll(bdcXmRelList);

        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
        //zx删除不动产单元
        if (project.getDjIds() != null) {
            super.initMulBdcdyAndBdcdjb(list, initVoFromParm);
        } else {
            BdcBdcdjb bdcdjb = null;
            if (StringUtils.isNotBlank(project.getZdzhh())) {
                //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
                List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
                if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                    bdcdjb = initBdcdjb(initVoFromParm);
                    list.add(bdcdjb);
                } else {
                    bdcdjb = bdcdjbTemps.get(0);
                }

                // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
                BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
                if (bdcTd == null) {
                    bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(),initVoFromParm.getProject(), bdcxm.getQllx());
                    list.add(bdcTd);
                }
            }

            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
            bdcSpxx = initBdcSpxx(initVoFromParm,bdcSpxx);
            if (bdcSpxx != null)
                list.add(bdcSpxx);
            BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
            if (bdcdy != null) {
                bdcxm.setBdcdyid(bdcdy.getBdcdyid());
                list.add(bdcdy);
            }
        }
        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcQlrList = initBdcQlrFromDjsj(initVoFromParm.getProject(),initVoFromParm.getDjsjFwQlrList(),initVoFromParm.getDjsjLqxx(),initVoFromParm.getDjsjZdxxList(),initVoFromParm.getDjsjQszdDcbList() ,initVoFromParm.getCbzdDcb(), Constants.QLRLX_QLR);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            List<BdcQlr> tempBdcQlrs = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrs)) {
                for (BdcQlr qlr : tempBdcQlrs) {
                    bdcQlrService.delBdcQlrByQlrid(qlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            list.addAll(bdcQlrList);
        }
        return list;
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
                logger.error("CreatProjectSpfGyyzServiceImpl.saveOrUpdateProjectData",e);
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
