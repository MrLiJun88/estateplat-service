package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXtCheckinfo;

import java.util.List;

/**
 * lst
 * 系统验证信息配置表
 * Created by lst on 2015/3/31
 */
public interface BdcXtCheckinfoService {
    /**
     * 增加系统验证信息配置表
     *
     * @param bdcXtCheckinfo
     */
    void saveXtCheckinfo(BdcXtCheckinfo bdcXtCheckinfo);

    /**
     * 删除系统验证信息配置表
     *
     * @param id
     */
    void delXtCheckinfo(final String id);

    /**
     * 修改系统验证信息配置表
     *
     * @param bdcXtCheckinfo
     */
    void updateXtCheckinfo(BdcXtCheckinfo bdcXtCheckinfo);

    /**
     * 获取系统验证信息配置表
     */
    List<BdcXtCheckinfo> getXtCheckinfo(final Example example);

}
