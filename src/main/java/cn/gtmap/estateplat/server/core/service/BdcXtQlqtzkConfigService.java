package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtQlqtzkConfig;

import java.util.List;

/**
 * lst
 * 不动产登记权利其他状况模板
 */
public interface BdcXtQlqtzkConfigService {

    /**
     * 新增或修改权利其它状况配置
     *
     * @param bdcXtQlqtzkConfig
     */
    void saveOrUpdateQlqtzk(BdcXtQlqtzkConfig bdcXtQlqtzkConfig) throws Exception;

    /**
     * 删除权利其它状况配置
     *
     * @param bdcXtQlqtzkConfig
     */
    void deleteQlqtzk(BdcXtQlqtzkConfig bdcXtQlqtzkConfig) throws Exception;

    /**
     * 通过sqlx和zlx获取权利其他状况配置
     *
     * @param bdcXtQlqtzkConfig
     */
    List<BdcXtQlqtzkConfig> getQlqtzk(BdcXtQlqtzkConfig bdcXtQlqtzkConfig);

    /**
     * 根据数据源替换模板内容
     *
     * @param mb
     * @param sql
     * @param bdcXm
     * @param bdcBdcdy
     * @return
     */
    String replaceMb(String mb, String sql, BdcXm bdcXm,BdcBdcdy bdcBdcdy);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param mb
     * @param sql
     * @param bdcXm
     * @param bdcBdcdy
     * @param bdcBdcdy
     * @param bdcBdcdy
     * @return
     * @description 根据数据源替换模板内容空值也显示
     */
    String replaceMbDisplayNull(String mb, String sql, BdcXm bdcXm,BdcBdcdy bdcBdcdy);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param mb
     * @param sql
     * @param bdcXm
     * @param bdcBdcdy
     * @param bdcBdcdy
     * @param bdcBdcdy
     * @return
     * @description 根据数据源替换模板内容空值不显示
     */
    String replaceMbUndisplayNull(String mb, String sql, BdcXm bdcXm,BdcBdcdy bdcBdcdy);

}
