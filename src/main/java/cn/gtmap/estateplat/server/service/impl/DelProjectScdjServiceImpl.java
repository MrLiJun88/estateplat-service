package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-24
 */
public class DelProjectScdjServiceImpl extends DelProjectDefaultServiceImpl {

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
    private BdcXmService bdcXmService;

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
        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            List<BdcXm> bdcXmList = null;
            HashMap map = new HashMap();
            map.put("bdcdyid", bdcXm.getBdcdyid());
            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            if (bdcXmList != null && bdcXmList.size() == 1) {
                bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
            }
        }

    }


    @Override
    public void changeYqllxzt(BdcXm bdcXm) {
        if(bdcXm != null&&StringUtils.isNotBlank(bdcXm.getSqlx())
                && (StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_SPFSCKFSZC_DM)||StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_SPFXZBG_DM)
                ||StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM))){
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                    }
                }
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
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                    }
                    changeGdsjQszt(bdcXmRel, qllxVo, 0);
                    changeYgQszt(bdcXm);
                }
            }
        }
    }

}
