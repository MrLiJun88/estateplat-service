package cn.gtmap.estateplat.server.sj.sc.Impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.sc.DeleteBdcDataService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description
 */
@Service
public class DeleteBdcDataServiceImpl implements DeleteBdcDataService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
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
    private BdcZjService bdcZjService;
    @Autowired
    private BdcJyxxService bdcJyxxService;
    @Autowired
    private BdcBdcZsSdService bdcBdcZsSdService;
    @Autowired
    private BdcXmcqRelService bdcXmcqRelService;

    @Override
    @Transactional
    public void deleteBdcData(ProjectPar projectPar) {
        if (StringUtils.isNotBlank(projectPar.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(projectPar.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    delBdcBdxx(bdcXm);
                    //最后删除项目表
                    bdcXmService.delBdcXmByProid(bdcXm.getProid());
                }
            }
        }
    }

    private void delBdcBdxx(BdcXm bdcXm) {
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 根据wiid删除不动产系统业务模型
         */
        bdcXmcqRelService.deleteBdcXmcqRelByWiid(bdcXm.getWiid());

        //删除项目关系表
        bdcXmRelService.delBdcXmRelByProid(bdcXm.getProid());
//        List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(bdcXm.getWiid());
//        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
//            for (BdcSjxx bdcSjxx : bdcSjxxList) {
//                //删除收件材料
//                bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
//                //删除收件信息
//                bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
//            }
//        }
        //删除审批信息
        bdcSpxxService.delBdcSpxxByProid(bdcXm.getProid());
        //删除权利人证书关系信息表以及权利人信息
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
                bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
            }
        }
        //删除义务人证书关系信息表以及义务人信息
        List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcQlrYwrByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcYwrList)) {
            for (BdcQlr bdcYwr : bdcYwrList) {
                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcYwr.getQlrid());
                bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid());
            }
        }
        //删除权利类型信息
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, bdcXm.getProid());
        //删除项目证书关系表 删除证书信息
        List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
            for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                bdcXmZsRelService.delBdcXmZsRelByXmzsgxid(bdcXmzsRel.getXmzsgxid());
                bdcZsService.delBdcZsByZsid(bdcXmzsRel.getZsid());
            }
        }
        bdcdyService.delDjbAndTd(bdcXm);
        //删除不动产单元
        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            HashMap map = new HashMap();
            map.put("bdcdyid", bdcXm.getBdcdyid());
            List<BdcXm> bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            if (bdcXmList != null && bdcXmList.size() == 1) {
                bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
            }
        }
        //删除商品房面积核减
        List<BdcSpfZdHjgx> bdcSpfZdHjgxList = bdcSpfZdHjgxService.getBdcZdFwRelList(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcSpfZdHjgxList)) {
            for (BdcSpfZdHjgx bdcSpfZdHjgx : bdcSpfZdHjgxList) {
                entityMapper.delete(bdcSpfZdHjgx);
            }
        }
        //删除抵押物清单
        bdcZjjzwxxService.deleteZjjzwxx(bdcXm);
        //删除不动产不予受理通知书
        bdcByslService.deleteBdcBysltzs(bdcXm);
        //删除不动产不予登记登记单
        bdcBydjService.deleteBdcBydjdjd(bdcXm);
        //删除不动产补正材料通知书
        bdcBqbzService.deleteBdcBzcltzs(bdcXm);
        //删除不动产公告
        bdcGgService.deleteBdcGg(bdcXm);
        //删除质检信息
        bdcZjService.delBdcZjmxByProid(bdcXm.getProid());
        bdcZjService.delBdcZjByProid(bdcXm.getProid());
        //删除交易现势信息要和历史信息
        bdcJyxxService.delAllBdcJyxx(bdcXm.getProid());
        //删除证书锁定信息
        bdcBdcZsSdService.delBdcBdcZsSd(bdcXm);
    }
}
