package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 抵押登记
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-26
 */
public class EndProjectDydjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;


    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        //zdd 抵押登记不需要修改原权利状态    如果为老数据   注销过渡数据的权利状态

        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx())) {
                    //在建工程对原先的土地抵押进行注销
                    String isCancelYtdDyForZjgcDy = AppConfig.getProperty("isCancelYtdDyForZjgcDy");
                    Integer qszt = (StringUtils.equals(isCancelYtdDyForZjgcDy, "true") && (StringUtils.equals(Constants.SQLX_ZJJZWDY_FW_DM, bdcXm.getSqlx()) ||StringUtils.equals(Constants.SQLX_ZJJZWDY_DDD_FW_DM, bdcXm.getSqlx()))) ? 1 : 0;
                    super.changeGdsjQszt(bdcXmRel, qszt);
                }
                //修改原权利为历史状态
                if (CommonUtil.indexOfStrs(Constants.SQLX_LQ_DY_BGDJ, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_ZJJZWDY_DDD_FW_DM,bdcXm.getSqlx()) ||StringUtils.equals(Constants.SQLX_ZJJZWDY_FW_DM,bdcXm.getSqlx())&&
                        StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                }
            }
            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }

    }

}
