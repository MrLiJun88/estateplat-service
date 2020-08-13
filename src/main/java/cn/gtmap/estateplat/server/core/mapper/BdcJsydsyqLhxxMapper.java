package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjFwJsydzrzxx;
import cn.gtmap.estateplat.model.server.core.DjsjZdJsydsyb;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/5/4
 * @description 建设用地量化信息查询接口
 */
@Repository
public interface BdcJsydsyqLhxxMapper {
    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 获取权籍宗地建设用地使用表
     */
    List<DjsjZdJsydsyb> getZdJsydsybList(Map map);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 获取权籍建设用地自然幢信息
     */
    List<DjsjFwJsydzrzxx> getFwJsydzrzxxList(Map map);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 首次登记更新工程进度
     */
    void updateFwljzGcjd(HashMap map);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 更新量化信息
     */
    void updateDjsjFwJsydzrzxx(DjsjFwJsydzrzxx djsjFwJsydzrzxx);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @return
     * @description 批量删除量化信息
     */
    void batchDelBdcJsydLhxx(List<BdcXm> bdcXmList);
}
