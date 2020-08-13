package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.1.0, 2017/03/09
 * @description 成套房首次登记与在建抵押转一般抵押合并登记业务
 */
public class EndComplexScDyProjectServiceImpl extends EndProjectZydjServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcdyService bdcdyService;

    @Override
    public void changeYqllxzt(BdcXm bdcXm) {
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 注销BDC_ZJJZWXX
         */
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        if (qllxVo instanceof BdcDyaq && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            if (bdcBdcdy != null) {
                Example example = new Example(BdcZjjzwxx.class);
                example.createCriteria().andEqualTo("bdcdyh", bdcBdcdy.getBdcdyh());
                List<BdcZjjzwxx> bdcZjjzwxxList = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                    for (BdcZjjzwxx bdcZjjzwxx : bdcZjjzwxxList) {
                        bdcZjjzwxx.setDyzt("1");
                        entityMapper.saveOrUpdate(bdcZjjzwxx, bdcZjjzwxx.getZjwid());
                    }
                }
            }
        }
    }

    public void doExtraWork(BdcXm bdcXm) {
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            zxYzjgcdy(bdcXm.getWiid());
        }
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 注销原在建工程抵押(土地证的抵押)
     */
    private void zxYzjgcdy(String wiid) {
        logger.info("注销在建工程土地证抵押");
        List<String> yproidList = new ArrayList<String>();
        List<BdcBdcdy> bdcdyList = bdcdyService.queryBdcBdcdy(wiid);
        if (CollectionUtils.isNotEmpty(bdcdyList)) {
            for (BdcBdcdy bdcBdcdy : bdcdyList) {
                if (StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                    Example example = new Example(BdcZjjzwxx.class);
                    example.createCriteria().andEqualTo("bdcdyh", bdcBdcdy.getBdcdyh());
                    List<BdcZjjzwxx> bdcZjjzwxxList = entityMapper.selectByExample(example);
                    if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                        for (BdcZjjzwxx bdcZjjzwxx : bdcZjjzwxxList) {
                            if (!yproidList.contains(bdcZjjzwxx.getProid()))
                                yproidList.add(bdcZjjzwxx.getProid());
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(yproidList)) {
            for (String proid : yproidList) {
                BdcXm ydyxm = bdcXmService.getBdcXmByProid(proid);
                if (ydyxm != null) {
                    QllxVo qllxVo = qllxService.queryQllxVo(ydyxm);
                    if (qllxVo instanceof BdcDyaq && qllxVo.getQszt().equals(Constants.QLLX_QSZT_XS)) {
                        qllxVo.setQszt(Constants.QLLX_QSZT_HR);
                        StringBuilder fj = new StringBuilder();
                        fj.append(qllxVo.getFj());
                        fj.append(CommonUtil.formatEmptyValue(fj)).append("\n").append(com.gtis.common.util.CommonUtil.formateDate(new Date())).append("办理房屋首次登记与在建抵押转现");
                        qllxVo.setFj(fj.toString());
                        entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
                    }
                }
            }
        }
    }

}
