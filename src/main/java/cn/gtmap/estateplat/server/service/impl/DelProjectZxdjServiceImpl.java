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
 * 注销登记删除服务  删除表单  还原原权利状态
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class DelProjectZxdjServiceImpl extends DelProjectDefaultServiceImpl {
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
    private BdcCfService bdcCfService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Override
    public void delBdcBdxx(BdcXm bdcXm) {
        String proid = bdcXm.getProid();
        //zdd 删除项目关系表
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
        String yproid = "";
        if (CollectionUtils.isNotEmpty(bdcXmRelList) && StringUtils.isNotBlank(bdcXmRelList.get(0).getYproid()))
            yproid = bdcXmRelList.get(0).getYproid();
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
        /**
         * @author bianwen
         * @description  若新建走注销或者解封，则修改原权利信息，
         * 若过渡匹配不动产单元，则删除带入不动产库的权利
         */
        if(StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)){
            if (StringUtils.isNotBlank(yproid) && qllxVo instanceof BdcDyaq) {
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
            } else if (StringUtils.isNotBlank(yproid) && qllxVo instanceof BdcCf) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("proid", yproid);
                List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
                if (CollectionUtils.isNotEmpty(qllxVos)) {
                    BdcCf bdcCf = (BdcCf) qllxVos.get(0);
                    bdcCf.setJfdbr("");
                    bdcCf.setJfdjsj(null);
                    bdcCf.setJfjg("");
                    bdcCf.setJfsj(null);
                    bdcCf.setJfwh("");
                    bdcCf.setJfwj("");
                    bdcCf.setJfywh("");
                    bdcCf.setIssx("");
                    bdcCf.setIscd("");
                    bdcCfService.updateBdcCfForJfxx(bdcCf);
                }
            }
        }
         else {
            qllxService.delQllxByproid(qllxVo, proid);
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
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            String yqllxdm = bdcZdGlService.getYqllxBySqlx(bdcXm.getSqlx());
            /**
             * @author bianwen
             * @description bdc_sqlx_qllx_rel表中如果不配置原权利类型，就默认上一手和这一手权利一样
             */
            if(StringUtils.isNotBlank(yqllxdm)){
                bdcXm.setQllx(yqllxdm);
            }
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()))
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), 1);
                //zdd 如果存在老数据  则注销老数据状态  后续需要完善
                super.changeGdsjQszt(bdcXmRel, qllxVo, 0);
            }
        }
    }
}
