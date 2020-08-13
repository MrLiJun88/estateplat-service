package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zdd on 2016/1/26.
 * 解封登记的办结服务
 */
public class EndProjectJfdjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcdyService bdcdyService;

    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);
        qllxService.endQllxZt(bdcXm);
    }

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRel.getYproid());
                    //zdd 一定要先注销历史状态  后面处理轮候查封的时候  查找没有查封才会处理轮候查封

                    /**
                     * @author bianwen
                     * @description  若不动产解封，则要通过项目关系找到原查封注销
                     *                 若过渡解封，则直接带入到不动产的查封权利
                     */
                    if(StringUtils.equals(bdcXm.getXmly(),Constants.XMLY_BDC)){
                        qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                        bdcCfService.turnLhcfToCf(bdcCf, bdcXm.getBdcdyid(), bdcXm);
                    }
                    else {
                        qllxService.changeQllxZt(bdcXm, Constants.QLLX_QSZT_HR,true);
                        changeGdsjQszt(bdcXmRel, 1);
                    }

                    //liujie 解封续封时把上一首查封解封掉
                    bdcCfService.jfBdcCfByXf(bdcCf);
                }
            }

            if(!StringUtils.equals(bdcXm.getXmly(),Constants.XMLY_BDC)) {
                bdcCfService.turnLhcfToCf(null, bdcXm.getBdcdyid(), bdcXm);
            }
        }
    }


}
