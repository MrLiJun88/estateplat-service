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
 * 首次登记创建项目实现类
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-16
 */

public class CreatProjectScdjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcTdService bdcTdService;
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
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                list.add(bdcTd);
            }
        }

        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm,bdcSpxx);
        if (bdcSpxx!=null) {
            list.add(bdcSpxx);
        }


        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
        }


        //考虑多个权利人情况
        BdcQlr bdcQlr = null;
        List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
        if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
            bdcQlrService.delBdcQlrByProid(project.getProid());
        }

        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcQlrList = initBdcQlrFromDjsj(initVoFromParm.getProject(), initVoFromParm.getDjsjFwQlrList(),initVoFromParm.getDjsjLqxx(),initVoFromParm.getDjsjZdxxList(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getCbzdDcb(), Constants.QLRLX_QLR);
        //zdd 初始登记选择老数据（房产证，土地证，林权证，林权）
        List<BdcQlr> ybdcQlrList = null;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> oldbdcQlrs = new ArrayList<BdcQlr>();
                for (BdcQlr ybdcQlr : ybdcQlrList) {
                    //zdd 即使选择老数据  权利人依然为老证的权利人
                    bdcQlr = bdcQlrService.qlrTurnProjectQlr(ybdcQlr, null, project.getProid());
                    oldbdcQlrs.add(bdcQlr);
                }
                bdcQlrList = oldbdcQlrs;
            }
        }
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            list.addAll(bdcQlrList);
        }
        return list;
    }

}
