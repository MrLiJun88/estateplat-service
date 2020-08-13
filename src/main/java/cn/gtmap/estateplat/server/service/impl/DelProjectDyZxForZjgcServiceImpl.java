package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;

/**
 * .
 *
 * @version 1.0 16-10-14
 * @author: bianwen
 * @description 在建工程注销删除事件
 */
public class DelProjectDyZxForZjgcServiceImpl extends DelProjectDefaultServiceImpl {
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;

    public void changeYqllxzt(BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            String yqllxdm = bdcZdGlService.getYqllxBySqlx(bdcXm.getSqlx());
            bdcXm.setQllx(yqllxdm);
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), 1);
                }
                super.changeGdsjQszt(bdcXmRel, qllxVo, 0);
            }
        }

        /**
         * @author bianwen
         * @description 删除时讲抵押物清单中的dyzt还原为抵押状态
         */
        bdcZjjzwxxService.changeZjjzwxxDyzt(bdcXm,"2","0");
    }

    public void delBdcBdxx(BdcXm bdcXm) {
        String proid = bdcXm.getProid();
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
        String yproid = "";
        if (CollectionUtils.isNotEmpty(bdcXmRelList) && StringUtils.isNotBlank(bdcXmRelList.get(0).getYproid())) {
            yproid = bdcXmRelList.get(0).getYproid();
        }

        // 删除项目关系表
        bdcXmRelService.delBdcXmRelByProid(proid);

        //删除收件单信息表
        List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(bdcXm.getWiid());
        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
            for (BdcSjxx bdcSjxx : bdcSjxxList) {
                bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
            }
        }
        //删除审批信息
        bdcSpxxService.delBdcSpxxByProid(proid);

        //删除权利人证书关系信息表以及权利人信息
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
                bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
            }
        }
        //删除权利类型信息 ,注销抵押 还原注销信息
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        if (StringUtils.isNotBlank(yproid) && qllxVo instanceof BdcDyaq){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("proid", yproid);
            List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
            if (CollectionUtils.isNotEmpty(qllxVos)) {
                BdcDyaq bdcDyaq = (BdcDyaq) qllxVos.get(0);
                bdcDyaq.setZxdbr("");
                bdcDyaq.setZxdyywh("");
                bdcDyaq.setZxdyyy("");
                bdcDyaq.setZxsj(null);
                bdcDyaqService.updateBdcDyaqForZxdj(bdcDyaq);
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
}
