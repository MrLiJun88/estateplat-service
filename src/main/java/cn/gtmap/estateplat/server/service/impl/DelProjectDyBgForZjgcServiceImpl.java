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
 * @version 1.0 16-10-27
 * @author: bianwen
 * @description  在建工程抵押变更
 */
public class DelProjectDyBgForZjgcServiceImpl extends DelProjectDefaultServiceImpl {
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
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;

    public void changeYqllxzt(BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 原抵押物清单变成现势
         */
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                HashMap zxmap = new HashMap();
                zxmap.put("proid", bdcXmRel.getYproid());
                zxmap.put("dyzt", "0");
                bdcZjjzwxxService.updateZjjzwDyzt(zxmap);
            }
        }

        /**
         * @author bianwen
         * @description  过渡数据抵押变更转移默认不支持部分
         */
        if (bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getWiid())) {
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            List<BdcXm> bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm1:bdcXmList){
                    List<BdcXmRel> bdcXmRelList1 = bdcXmRelService.queryBdcXmRelByProid(bdcXm1.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList1)) {
                        String yqllxdm = bdcZdGlService.getYqllxBySqlx(bdcXm1.getSqlx());
                        bdcXm1.setQllx(yqllxdm);
                        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm1);
                        for (BdcXmRel bdcXmRel : bdcXmRelList1) {
                            super.changeGdsjQszt(bdcXmRel, qllxVo, 0);
                        }
                    }
                }
            }

        }

    }

    public void delBdcBdxx(BdcXm bdcXm) {
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
        }
        if(CollectionUtils.isNotEmpty(bdcXmList)){
           for(BdcXm bdcXmTemp:bdcXmList){
               String proid = bdcXmTemp.getProid();
               // 删除项目关系表
               bdcXmRelService.delBdcXmRelByProid(proid);

               //删除收件单信息表
               List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(bdcXmTemp.getWiid());
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
               //删除权利类型信息
               QllxVo qllxVo = qllxService.makeSureQllx(bdcXmTemp);
               qllxService.delQllxByproid(qllxVo, proid);

               bdcdyService.delDjbAndTd(bdcXmTemp);

               if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                   List<BdcXm> bdcXmList1 = null;
                   HashMap map = new HashMap();
                   map.put("bdcdyid", bdcXm.getBdcdyid());
                   bdcXmList1 = bdcXmService.andEqualQueryBdcXm(map);
                   if (bdcXmList1 != null && bdcXmList1.size() == 1) {
                       bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
                   }
               }

               /**
                * @author bianwen
                * @description  删除抵押物清单
                */
               bdcZjjzwxxService.deleteZjjzwxx(bdcXmTemp);
           }
        }
    }
    public void delBdcXm(final String proid) {
        BdcXm bdcXm=bdcXmService.getBdcXmByProid(proid);
        List<BdcXm> bdcXmList = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
        }
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            for(BdcXm bdcXmTemp:bdcXmList){
                String proidTemp = bdcXmTemp.getProid();
                bdcXmService.delBdcXmByProid(proidTemp);
            }
        }
    }
}
