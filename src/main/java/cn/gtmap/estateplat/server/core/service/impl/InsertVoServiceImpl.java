package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.core.service.InsertVoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zdd on 2015/12/13.
 * 处理新建项目插入对象服务
 */
@Service
public class InsertVoServiceImpl implements InsertVoService {


    @Override
    public List<InsertVo> reReadBdcspxx(List<InsertVo> insertVoList, List<InsertVo> bdcspxxList) {
        return null;
    }

    @Override
    public BdcXmRel getBdcXmRel(List<InsertVo> insertVoList) {
        BdcXmRel bdcXmRel = null;
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            for (InsertVo insertVo : insertVoList) {
                if (insertVo != null && insertVo.getClass().equals(BdcXmRel.class)) {
                    bdcXmRel = (BdcXmRel) insertVo;
                    break;
                }

            }
        }
        return bdcXmRel;
    }
}
