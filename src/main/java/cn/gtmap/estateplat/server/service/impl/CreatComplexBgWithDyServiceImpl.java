package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.model.server.core.Xmxx;
import java.util.List;

/**
 * @version 1.0, 2016/12/30
 * @author<a href = "mailto；liuxing@gtmap.cn">lx</a>
 * @description
 */
public class CreatComplexBgWithDyServiceImpl extends CreatComplexHzWithDyProjectServiceImpl{
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx){
        return super.initVoFromOldData(xmxx);
    }
}
