package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcZdCheck;

import java.util.List;

/**
 * lst
 * 不动产登记验证信息字典表
 * Created by lst on 2015/3/31
 */
public interface BdcZdCheckService {

    /**
     * 增加验证信息
     *
     * @param bdcZdCheck
     */
    void saveZdCheck(BdcZdCheck bdcZdCheck);

    /**
     * 删除验证信息
     *
     * @param id
     */
    void delZdCheck(final String id);

    /**
     * 修改验证信息
     *
     * @param bdcZdCheck
     */
    void updateZdCheck(BdcZdCheck bdcZdCheck);

    /**
     * 获取所有验证信息
     */
    List<BdcZdCheck> getZdCheck(final Example example);
}
