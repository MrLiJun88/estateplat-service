package cn.gtmap.estateplat.server.core.aop;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/5/12 0012
 * @description
 */
public class ChangeXmzt {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;

    public void changeXmzt(final JoinPoint jp) {
        String proid = (String) jp.getArgs()[0];
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm xm : bdcXmList) {
                    xm.setXmzt("1");
                    xm.setBjsj(new Date());
                    entityMapper.updateByPrimaryKeySelective(bdcXm);
                }
            }
        }
    }


}
