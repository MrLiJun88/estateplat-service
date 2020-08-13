package cn.gtmap.estateplat.server.core.service;

/**
 * 不动产登记项目归档反馈服务
 * User: apple
 * Date: 15-12-11
 * Time: 上午10:22
 * To change this template use File | Settings | File Templates.
 */
public interface ArchiveReceiveSecivce {
    /**
     * 接受归档反馈信息
     *
     * @param gdxx
     */
    void receive(final String gdxx);
}
