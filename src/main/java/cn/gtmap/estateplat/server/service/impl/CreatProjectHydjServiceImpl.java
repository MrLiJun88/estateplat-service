package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 海域首次登记创建项目实现类
 *
 * @author zhx
 * @version V1.0, 2016-01-06
 */

public class CreatProjectHydjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcHyFlxxService bdcHyFlxxService;
    @Autowired
    private BdcHyService bdcHyService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcSpxxService bdcSpxxService;


    @Override
    @Transactional
    public List<InsertVo> initVoFromOldData(Xmxx xmxx) {
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project = (Project) xmxx;
        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);

        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            list.addAll(bdcXmRelList);
        }

        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            // 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }

            bdcHyService.deleteBdcHyByZdzhh(project.getZdzhh());
            // 在选取不动产单元的时候顺便继承海域信息到bdc_hy表中
            BdcHy bdcHy = bdcHyService.selectBdcHy(project.getZdzhh());
            if (bdcHy == null) {
                bdcHy = bdcHyService.getBdcHyFromDjxx(initVoFromParm.getDjsjZhxx(), project, bdcHy, bdcxm.getQllx());
                list.add(bdcHy);
            }
            if (bdcHy.getHyid() != null) {
                List<BdcHyFlxx> bdcHyFlxxList = bdcHyFlxxService.getBdcHyFlxxFromZHJNBDYJLB(bdcHy.getHyid(), project.getZdzhh());
                if (CollectionUtils.isNotEmpty(bdcHyFlxxList)) {
                    list.addAll(bdcHyFlxxList);
                }
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

        //考虑多个权利人情况
        List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
        if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
            bdcQlrService.delBdcQlrByProid(project.getProid());
        }

        //zhx 根据宗海信息初始化qlr
        List<BdcQlr> bdcQlrList = initBdcQlrFromZh(project, initVoFromParm.getDjsjZhxxList(), Constants.QLRLX_QLR);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            list.addAll(bdcQlrList);
        }

        return list;
    }

}
