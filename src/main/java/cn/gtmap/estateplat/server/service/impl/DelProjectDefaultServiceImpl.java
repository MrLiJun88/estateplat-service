package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * **********************在不清楚业务逻辑时  请不要修改默认方法************************
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-24
 */
public class DelProjectDefaultServiceImpl implements DelProjectService {
    @Autowired
    GdXmService gdXmService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private BdcZsbhService bdcZsbhService;

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSpfZdHjgxService bdcSpfZdHjgxService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcByslService bdcByslService;
    @Autowired
    private BdcBydjService bdcBydjService;
    @Autowired
    private BdcBqbzService bdcBqbzService;
    @Autowired
    private BdcGgService bdcGgService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private BdcZjService bdcZjService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcJyxxService bdcJyxxService;
    @Autowired
    private BdcBdcZsSdService bdcBdcZsSdService;

    @Override
    public void delWorkFlow(final String proid) {
    }

    @Override
    public void delProjectNode(final String proid) {
        if(StringUtils.isNotBlank(proid)){
            Space space = fileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF");
            com.gtis.fileCenter.model.Node tempNode = fileCenterNodeServiceImpl.getNode(space.getId(), proid, true);
            fileCenterNodeServiceImpl.remove(tempNode.getId());
        }
    }


    @Override
    public void delBdcBdxx(BdcXm bdcXm) {
        String proid = bdcXm.getProid();
        //zdd 删除项目关系表
        bdcXmRelService.delBdcXmRelByProid(proid);
        //zdd 删除收件单信息表
        List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(bdcXm.getWiid());
        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
            for (BdcSjxx bdcSjxx : bdcSjxxList) {
                bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
            }
        }
        //zdd 删除审批信息
        bdcSpxxService.delBdcSpxxByProid(proid);
        //zdd 删除权利人证书关系信息表以及权利人信息
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
                bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
            }
        }
        //zdd 删除义务人证书关系信息表以及义务人信息
        List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcYwrList)) {
            for (BdcQlr bdcYwr : bdcYwrList) {
                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcYwr.getQlrid());
                bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid());
            }
        }
        //zdd 删除权利类型信息
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, proid);
        //zdd 删除项目证书关系表 删除证书信息
        List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
            for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                bdcXmZsRelService.delBdcXmZsRelByXmzsgxid(bdcXmzsRel.getXmzsgxid());
                bdcZsService.delBdcZsByZsid(bdcXmzsRel.getZsid());
            }
        }
        bdcdyService.delDjbAndTd(bdcXm);
        //zdd 删除不动产单元
        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            List<BdcXm> bdcXmList = null;
            HashMap map = new HashMap();
            map.put("bdcdyid", bdcXm.getBdcdyid());
            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            if (bdcXmList != null && bdcXmList.size() == 1) {
                bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
            }
        }
        //zx 删除商品房面积核减
        List<BdcSpfZdHjgx> bdcSpfZdHjgxList = bdcSpfZdHjgxService.getBdcZdFwRelList(proid);
        if (CollectionUtils.isNotEmpty(bdcSpfZdHjgxList)) {
            for (BdcSpfZdHjgx bdcSpfZdHjgx : bdcSpfZdHjgxList) {
                entityMapper.delete(bdcSpfZdHjgx);
            }
        }
        /**
         * @author bianwen
         * @description  删除抵押物清单
         */
        bdcZjjzwxxService.deleteZjjzwxx(bdcXm);
        /**
         * @author liujie
         * @description  删除不动产不予受理通知书
         */
        bdcByslService.deleteBdcBysltzs(bdcXm);

        /**
         * @author liujie
         * @description  删除不动产不予登记登记单
         */
        bdcBydjService.deleteBdcBydjdjd(bdcXm);

        /**
         * @author liujie
         * @description  删除不动产补正材料通知书
         */
        bdcBqbzService.deleteBdcBzcltzs(bdcXm);

        /**
         * @author liujie
         * @description  删除不动产公告
         */
        bdcGgService.deleteBdcGg(bdcXm);

        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 删除质检信息
         */
        bdcZjService.delBdcZjmxByProid(proid);
        bdcZjService.delBdcZjByProid(proid);

        /**
         * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
         * @description 删除交易现势信息要和历史信息
         */
        bdcJyxxService.delAllBdcJyxx(proid);

    }

    @Override
    public void batchDelBdcBdxx(List<BdcXm> bdcXmList) {
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            //删除项目关系表
            bdcXmRelService.batchDelBdcXmRel(bdcXmList);

            //删除收件单信息表
            List<String> sjxxidList = bdcSjdService.getSjxxidlistByBdcXmList(bdcXmList);
            bdcSjdService.batchDelSjclListBySjxxidList(sjxxidList);
            bdcSjdService.batchDelSjxxListBySjxxidList(sjxxidList);

            //删除审批信息
            bdcSpxxService.batchDelBdcSpxx(bdcXmList);

            //删除权利人证书关系信息表以及权利人信息
            List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrListByBdcXmList(bdcXmList);
            bdcZsQlrRelService.batchDelBdcZsQlrRelByBdcQlrList(bdcQlrList);
            bdcQlrService.batchDelBdcQlrByBdcXmList(bdcXmList);

            //删除权利类型信息
            BdcXm bdcXm = bdcXmList.get(0);
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            if(qllxVo instanceof BdcFdcq){
                bdcFdcqService.batchDelBdcFdcqByBdcXmList(bdcXmList);
            }

            //删除项目证书关系表 删除证书信息
            bdcXmZsRelService.batchDelBdcXmZsRelByBdcXmList(bdcXmList);
            List<String> zsidList = bdcXmZsRelService.getZsidListByBdcXmList(bdcXmList);
            bdcZsService.batchDelBdcZsByZsidList(zsidList);
            //删除不动产单元
            bdcdyService.batchDelBdcBdcdyBdcXmList(bdcXmList);
        }
    }

    @Override
    public void delBdcXm(final String proid) {
        bdcXmService.delBdcXmByProid(proid);
    }

    @Override
    public void batchDelBdcXm(List<BdcXm> bdcXmList) {
        bdcXmService.batchDelBdcXm(bdcXmList);
    }

    @Override
    public void delZsbh(final String proid) {
        List<BdcZs> zsList = bdcZsService.queryBdcZsByProid(proid);
        if (CollectionUtils.isNotEmpty(zsList)) {
            for (BdcZs bdcZs : zsList) {
                HashMap map = new HashMap();
                map.put("zsid", bdcZs.getZsid());
                List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
                if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
                    for (BdcZsbh bdcZsbh : bdcZsbhList) {
                        bdcZsbh.setLqr("");
                        bdcZsbh.setLqrid("");
                        bdcZsbh.setZsid("");
                        bdcZsbh.setSyqk("0");
                        entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                    }
                }
            }
        }
    }

    @Override
    public void batchDelBdcZsbh(List<BdcXm> bdcXmList) {
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            BdcXm bdcXm = bdcXmList.get(0);
            if(bdcXm != null&&StringUtils.isNotBlank(bdcXm.getWiid())){
                List<BdcZs> bdcZsList = bdcZsService.getPlZsByWiid(bdcXm.getWiid());
                if(CollectionUtils.isNotEmpty(bdcZsList)){
                    List<BdcZsbh> bdcZsbhList = bdcZsbhService.batchSelectBdcZsbh(bdcZsList);
                    if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
                        for (BdcZsbh bdcZsbh : bdcZsbhList) {
                            bdcZsbh.setLqr("");
                            bdcZsbh.setLqrid("");
                            bdcZsbh.setZsid("");
                            bdcZsbh.setSyqk("0");
                        }
                        bdcZsbhService.batchUpdateBdcZsbh(bdcZsbhList);
                    }
                }
            }
        }

    }

    @Override
    public void delBdcBdcZsSd(BdcXm bdcXm) {
        bdcBdcZsSdService.delBdcBdcZsSd(bdcXm);
    }

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        //zdd 如果存在过渡数据  需要还原过渡权属状态
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                changeGdsjQszt(bdcXmRel, qllxVo, 0);
                changeYgQszt(bdcXm);
            }
        }
    }

    @Override
    public void batchChangeYqllxzt(List<BdcXm> bdcXmList, String mainProid) {
        if(CollectionUtils.isNotEmpty(bdcXmList) && StringUtils.isNotBlank(mainProid)){
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(mainProid);
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(mainProid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList) && bdcXm != null) {
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    changeGdsjQszt(bdcXmRel, qllxVo, 0);
                    changeYgQszt(bdcXm);

                }
            }
        }
    }

    /**
     * zdd 修改过渡数据的权属状态
     *
     * @param bdcXmRel
     * @param qllxVo
     * @param qszt
     */
    protected void changeGdsjQszt(BdcXmRel bdcXmRel, QllxVo qllxVo, Integer qszt) {
        if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && StringUtils.isNotBlank(bdcXmRel.getYdjxmly()) && !bdcXmRel.getYdjxmly().equals("1")
                &&StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
            gdXmService.updateGdQszt(bdcXmRel.getYqlid(), qszt);
        }
    }

    /**
     * jiangganzhi 在做商品房买卖转移登记和抵押登记时 删除同时修改预告的权属状态
     *
     * @param bdcXm
     */
    public void changeYgQszt(BdcXm bdcXm){
        if(null != bdcXm){
            String sqlxdm = "";
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            if(StringUtils.equals(sqlxdm, Constants.SQLX_CLF)){
                String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
                if(StringUtils.isNotBlank(bdcdyh)){
                    List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(bdcdyh, "2");
                    if(CollectionUtils.isNotEmpty(bdcYgList)){
                        for(BdcYg bdcYg : bdcYgList){
                            bdcYg.setQszt(Constants.QLLX_QSZT_XS);
                            entityMapper.saveOrUpdate(bdcYg,bdcYg.getQlid());
                        }
                    }
                }
            }
        }
    }
}
