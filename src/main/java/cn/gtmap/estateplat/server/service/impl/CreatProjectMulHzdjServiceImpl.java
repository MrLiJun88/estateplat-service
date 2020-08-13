package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: appleForSuncho
 * Date: 15-12-5
 * Time: 下午1:29
 * 匹配批量换证登记
 * To change this template use File | Settings | File Templates.
 */
public class CreatProjectMulHzdjServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmRelService bdcXmRelService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project = null;
        List<BdcQlr> ybdcQlrList = null;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        }
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        list.add(bdcxm);
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatMulBdcXmRelFromProject(project);
        list.addAll(bdcXmRelList);

        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);

        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcYwrList = initBdcQlrFromDjsj(project, initVoFromParm.getDjsjFwQlrList(),initVoFromParm.getDjsjLqxx(),initVoFromParm.getDjsjZdxxList(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getCbzdDcb(), Constants.QLRLX_YWR);

        //zdd 权利人信息
        //不是过渡数据执行
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            List<BdcQlr> tempBdcYwrs = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                tempBdcYwrs.add(bdcQlr);
            }
            bdcYwrList = tempBdcYwrs;
        }
        if (CollectionUtils.isNotEmpty(bdcYwrList)&&project != null) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            list.addAll(bdcYwrList);
        }

        return list;
    }
}
