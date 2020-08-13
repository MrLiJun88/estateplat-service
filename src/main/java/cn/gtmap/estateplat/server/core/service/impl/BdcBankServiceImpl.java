package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXtYh;
import cn.gtmap.estateplat.server.core.mapper.BdcXtYhMapper;
import cn.gtmap.estateplat.server.core.service.BdcBankService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 银行配置.
 * @author liujie
 * @version V1.0, 15-9-18
 * @description 银行配置服务，用于系统管理中的银行配置功能
 */
@Repository
public class BdcBankServiceImpl implements BdcBankService {
    /**
     * entity对象Mapper.
     * @author liujie
     * @description entity对象Mapper.
     */
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXtYhMapper bdcXtYhMapper;

    /**
     * 根据主键删除银行配置.
     * @author liujie
     * @description 根据主键删除银行配置.
     * @param ids 主键，多个主键以逗号隔开
     * @return
     */
    @Override
    @Transactional
    public void deleteBankByPrimaryKey(final String ids) {
        if(StringUtils.isBlank(ids))
            return;
        /**
         * 根据主键循环删除数据库记录
         */
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            if(StringUtils.isNotBlank(id[i])) entityMapper.deleteByPrimaryKey(BdcXtYh.class, id[i]);
        }
    }

    /**
     * 根据主键插入或更新银行配置.
     * @param bdcBank 银行配置信息
     */
    @Override
    @Transactional
    public void insertOrUpdateByPrimaryKey(final BdcXtYh bdcBank) {
        if(bdcBank==null)return;
        if (StringUtils.isBlank(bdcBank.getYhid())) {
            bdcBank.setYhid(UUIDGenerator.generate18());
        }
        entityMapper.saveOrUpdate(bdcBank, bdcBank.getYhid());

    }

    @Override
    public List<BdcXtYh> getBankListByPage() {
        return bdcXtYhMapper.getBankListByPage();
    }

    @Override
    public List<BdcXtYh> getBankListByYhmc(String yhmc) {
        return bdcXtYhMapper.getBankListByYhmc(yhmc);
    }
}
