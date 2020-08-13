package cn.gtmap.estateplat.server.service;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/8/18
 * @description 不动产历史关系服务
 */
public interface BdcHistoryService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 不动产项目ID
     * @return
     * @description 根据当前项目的项目历史关系生成产权历史关系
     */
    void generateQlHistoryByProid(final String proid);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 不动产项目ID
     * @return
     * @description 删除当前项目产生的产权历史关系
     */
    void deleteQlHistoryByProid(final String proid);
}
