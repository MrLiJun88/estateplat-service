package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 异议登记办结服务  与默认服务一致
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-2
 */
public class EndProjectYydjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private QllxService qllxService;


    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()))
                    super.changeYyQszt(bdcXm);
                //zhouwanqing 老数据做完流程要注销,
                //异议登记办结后，过渡房屋或是土地的iszx并不需要改变为注销状态
                super.changeGdsjQszt(bdcXmRel, 0);
            }

            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }
    }


}