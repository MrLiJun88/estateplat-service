package cn.gtmap.estateplat.server.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/9/1
 * @description 合并过渡数据
 */
public interface GdDataMergeService {
    /**
     * @param qlids
     * @param bdclx
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 检查是否可以合并
     */
    public HashMap checkGdDataMerge(String qlids,String bdclx);

    /**
     * @param qlids
     * @@param bdclx
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 合并数据
     */
    public void mergeGdData(String qlids, String bdclx) throws InvocationTargetException, IllegalAccessException;

}
