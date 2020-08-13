package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/12/26
 * @description
 */
public class DelProjectScdjAndPldyProjectServiceImpl extends DelProjectDefaultServiceImpl {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private QllxService qllxService;

    @Override
    public void changeYqllxzt(BdcXm bdcXm) {
        //zdd 如果存在过渡数据  需要还原过渡权属状态
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                changeGdsjQszt(bdcXmRel, qllxVo, 0);
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())){
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);//当删除项目时，将原项目权属状态变为现势
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
}
