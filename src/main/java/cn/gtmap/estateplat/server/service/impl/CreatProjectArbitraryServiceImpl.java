package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2017-03-20
 * @description 任意单个组合流程创建项目服务
 */
public class CreatProjectArbitraryServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else {
            throw new AppException(4005);
        }
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        list.add(bdcxm);
        //zdd 需要完善   一个项目有可能有多个项目关系表
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            list.addAll(bdcXmRelList);
        }
        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm = super.getDjxx(xmxx);
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
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx()
                        , initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList()
                        , initVoFromParm.getProject(), bdcxm.getQllx());
                list.add(bdcTd);
            }
        }
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null) {
            list.add(bdcSpxx);
        }
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
        }
        return list;
    }

}
