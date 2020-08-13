package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 多个不动产单元做抵押权（地役权）变更登记创建项目服务  同普通变更登记最大的区别在于人方信息全部读取过来
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-31
 */
public class CreatProjectMulBdcdyDyBgdjServiceImpl extends CreatProjectDefaultServiceImpl {

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
        List<BdcQlr> ybdcYwrList = null;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
            ybdcYwrList = getYbdcYwrList(project);
        }
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        list.add(bdcxm);
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatMulBdcXmRelFromProject(project);
        list.addAll(bdcXmRelList);

        //zdd 获取地籍数据
        super.getDjxx(xmxx);
        super.initGdMulBdcdyAndBdcdjb(list, project);

        //zdd 人方信息
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            for (BdcQlr bdcYwr : ybdcYwrList) {
                bdcYwr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcYwr, tempBdcYwrList, project.getProid());
                list.add(bdcYwr);
            }
        }
        return list;
    }
}
