package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdBdcQlRel;

import java.util.List;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/3/5
 * @description 过渡关系表
 */
public interface GdBdcQlRelService {
    /**
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @param qlid
     * @rerutn List<GdBdcQlRel>
     * @description 获取过渡关系表
     */
    List<GdBdcQlRel> queryGdBdcQlListByQlid(final String qlid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcid
     * @rerutn List<GdBdcQlRel>
     * @description 获取过渡关系表
     */
    List<GdBdcQlRel> queryGdBdcQlListByBdcid(final String bdcid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过不动产单元号获取过渡权利关系表，除了会根据匹配关系查找以外，还会根据档案号查找
     */
    public List<GdBdcQlRel> getGdBdcQlRelByBdcdyh(String bdcdyh);
}
