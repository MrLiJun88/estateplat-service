package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcJjd;

import java.io.IOException;

/**
 * 不动产登记项目归档服务
 * User: apple
 * Date: 15-12-10
 * Time: 下午11:26
 * To change this template use File | Settings | File Templates.
 */
public interface ArchiveExchangeSecivce {
    String gdjjd(final String jjdbhs, final String archiveUrl) throws IOException;

    BdcJjd getBdcjjd(final String jjdbh);
}
