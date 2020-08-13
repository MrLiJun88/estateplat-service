package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdFw;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2016-11-9
 * @description 拆分过度数据
 */
public interface SplitGdDataServer {
    /**
     * @param qlid
     * @return String
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 检查是否可以拆分
     */
    public HashMap checkSplit(String qlid);

    /**
     * @param qlid
     * @return boolean,bdclx
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据
     */
    public void splitGdData(String qlid, String bdclx) throws InvocationTargetException, IllegalAccessException;


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 已匹配数据进行拆分数据处理原权利的gd_ql_dyh_rel
     */
    public void completeSplitGdData(String yqlid,List<GdFw> gdFwList);

}
