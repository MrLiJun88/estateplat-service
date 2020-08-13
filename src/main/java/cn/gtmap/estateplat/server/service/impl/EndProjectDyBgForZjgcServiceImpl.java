package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * .
 *
 * @version 1.0 16-10-28
 * @author: bianwen
 * @description  在建工程抵押变更
 */
public class EndProjectDyBgForZjgcServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;

    public void changeYqllxzt(final BdcXm bdcXm) throws Exception {
        String zjjzwFw= "";
        if(StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_ZJJZW_BG_FW_DM)){
            zjjzwFw="true";
        }
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        }
        String yproid="";
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        if(StringUtils.equals(zjjzwFw, "true")){
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        yproid = bdcXmRel.getYproid();
                        break;
                    }
                    super.changeGdsjQszt(bdcXmRel, 1);
                }
                /**
                 * @author bianwen
                 * @description  如果是部分变更，则需要通过yproid取到上一个所有的权利
                 */
                if(StringUtils.isNotBlank(yproid)){
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
                    if(ybdcXm!=null){
                        String ywiid=ybdcXm.getWiid();
                        List<BdcXm> ybdcxmList=bdcXmService.getBdcXmListByWiid(ywiid);
                        if(CollectionUtils.isNotEmpty(ybdcxmList)){
                            for(BdcXm bdcXm1:ybdcxmList){
                                if (StringUtils.isNotBlank(bdcXm1.getProid())) {
                                    qllxService.changeQllxZt(bdcXm1.getProid(), Constants.QLLX_QSZT_HR);
                                }
                            }
                        }
                    }
                }

            }
        }
        else {
            /**
             * @author bianwen
             * @description  修改原权利状态
             */
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                    }
                    changeGdsjQszt(bdcXmRel, 1);
                }
            }
        }

        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 在建建筑物dyzt现势
         */
        HashMap map = new HashMap();
        map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
        map.put("dyzt", "0");
        bdcZjjzwxxService.updateZjjzwDyzt(map);
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 原抵押物清单变成历史
         */
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                HashMap zxmap = new HashMap();
                zxmap.put(ParamsConstants.PROID_LOWERCASE, bdcXmRel.getYproid());
                zxmap.put("dyzt", "1");
                bdcZjjzwxxService.updateZjjzwDyzt(zxmap);
            }
        }
        /**
         * @author bianwen
         * @description 修改当前权利状态
         */
        qllxService.endQllxZt(bdcXm);
    }
    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);
    }

    public void backYqllxzt(BdcXm bdcXm) {
        String zjjzwFw= "";
        if(StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_ZJJZW_BG_FW_DM)){
            zjjzwFw="true";
        }
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        String yproid="";
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        if(StringUtils.equals(zjjzwFw, "true")){
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        yproid = bdcXmRel.getYproid();
                        break;
                    }
                    super.changeGdsjQszt(bdcXmRel, 0);
                }
                if(StringUtils.isNotBlank(yproid)){
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
                    if(ybdcXm!=null){
                        String ywiid=ybdcXm.getWiid();
                        List<BdcXm> ybdcxmList=bdcXmService.getBdcXmListByWiid(ywiid);
                        if(CollectionUtils.isNotEmpty(ybdcxmList)){
                            for(BdcXm bdcXm1:ybdcxmList){
                                if (StringUtils.isNotBlank(bdcXm1.getProid())) {
                                    qllxService.changeQllxZt(bdcXm1.getProid(), Constants.QLLX_QSZT_XS);
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                    }
                    super.changeGdsjQszt(bdcXmRel, 0);
                }
            }
        }
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 原抵押物清单变成现势
         */
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                HashMap zxmap = new HashMap();
                zxmap.put(ParamsConstants.PROID_LOWERCASE, bdcXmRel.getYproid());
                zxmap.put("dyzt", "0");
                bdcZjjzwxxService.updateZjjzwDyzt(zxmap);
            }
        }

    }
}
