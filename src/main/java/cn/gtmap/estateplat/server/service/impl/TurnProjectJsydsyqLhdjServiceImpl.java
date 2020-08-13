package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcJsydsyqLhxxService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/5/3
 * @description 建设用地使用权量化登记服务
 */
public class TurnProjectJsydsyqLhdjServiceImpl extends TurnComplexZyDyProjectServiceImpl {
    @Autowired
    private BdcJsydsyqLhxxService bdcJsydsyqLhxxService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = super.saveQllxVo(bdcXm);
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 读取权籍建设用地量化表，获取除了首次登记的楼幢到登记权籍建设用地量化表中
         */
        //防止重复生成
        if (!(qllxVo instanceof BdcDyaq)) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            if (bdcBdcdy != null) {
                String djh = bdcBdcdy.getBdcdyh().substring(0, 19);
                HashMap tdMap = new HashMap();
                tdMap.put("djh", djh);
                List<DjsjZdJsydsyb> djsjZdJsydsybList = bdcJsydsyqLhxxService.getZdJsydsybList(tdMap);
                HashMap fwMap = new HashMap();
                fwMap.put("lszd", djh);
                fwMap.put("gcjd", new String[]{"1", "2", "3"});
                List<DjsjFwJsydzrzxx> djsjFwJsydzrzxxList = bdcJsydsyqLhxxService.getFwJsydzrzxxList(fwMap);
                List<BdcJsydsyqLhxx> bdcJsydsyqLhxxList = bdcJsydsyqLhxxService.getBdcJsydsyqLhxxFromDjsj(bdcXm, djsjZdJsydsybList, djsjFwJsydzrzxxList);
                if (CollectionUtils.isNotEmpty(bdcJsydsyqLhxxList)) {
                    entityMapper.insertBatchSelective(bdcJsydsyqLhxxList);
                }
            }
        }
        return qllxVo;
    }
}
