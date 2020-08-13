package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcJsydsyqLhxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjFwJsydzrzxx;
import cn.gtmap.estateplat.model.server.core.DjsjZdJsydsyb;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/5/4
 * @description 建设用地量化信息服务
 */
public interface BdcJsydsyqLhxxService {
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
     * @description 继承权籍量化信息
     */
    List<BdcJsydsyqLhxx> getBdcJsydsyqLhxxFromDjsj(BdcXm bdcXm, List<DjsjZdJsydsyb> djsjZdJsydsybList, List<DjsjFwJsydzrzxx> djsjFwJsydzrzxxList);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 更新权籍量化关系表
     */
    void updateFwljzGcjd(String proid, String gcjd);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 登记数据更新权籍数据
     */
    void updateDjsjFwLhxx(String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 删除量化信息
     */
    void delBdcJsydLhxx(String proid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param  bdcXmList
     * @return
     * @description 批量删除量化信息
     */
    void batchDelBdcJsydLhxx(List<BdcXm> bdcXmList);
}
