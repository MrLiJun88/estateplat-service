package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2016/5/05
 * @description 过度日志
 */
public interface GdSaveLogSecvice {

    /**
     * @param gdXm
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度项目日志
     */
    void gdXmLog(GdXm gdXm);

    /**
     * @param gdFwsyq
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度房屋所有权日志
     */
    void gdFwsyqLog(GdFwsyq gdFwsyq);

    /**
     * @param gdTdsyq
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度土地所有权日志
     */
    void gdTdsyqLog(GdTdsyq gdTdsyq);

    /**
     * @param gdDy
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度抵押日志
     */
    void gdDyLog(GdDy gdDy);

    /**
     * @param gdCf
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度房屋查封日志
     */
    void gdCfLog(GdCf gdCf);

    /**
     * @param gdYg
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度房屋预告日志
     */
    void gdYgLog(GdYg gdYg);

    /**
     * @param gdYy
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度异议权日志
     */
    void gdYyLog(GdYy gdYy);

    /**
     * @param gdFw
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度房屋日志
     */
    void gdFwLog(GdFw gdFw);

    /**
     * @param gdTd
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 过度土地日志
     */
    void gdTdLog(GdTd gdTd);

    /**
     * @param bdcGdDyhRel
     * @author <a href="mailto:sunchao@gtmap.cn">jiangganzhi</a>
     * @description 过度单元号匹配日志
     */
    void gdDyhRelLog(BdcGdDyhRel bdcGdDyhRel);

    /**
     * @param gdQlDyhRel
     * @author <a href="mailto:sunchao@gtmap.cn">jiangganzhi</a>
     * @description 过度权利单元号匹配日志
     */
    void gdQlDyhRelLog(GdQlDyhRel gdQlDyhRel);

    /**
     * @param bdcGdDyhRel
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 过度单元号取消匹配日志
     */
    void gdDyhRelQxppLog(BdcGdDyhRel bdcGdDyhRel);

    /**
     * @param gdQlDyhRel
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 过度权利单元号取消匹配匹配日志
     */
    void gdQlDyhRelQxppLog(GdQlDyhRel gdQlDyhRel);
}
