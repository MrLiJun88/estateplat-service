package cn.gtmap.estateplat.server.core.service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/20
 * @description 不动产交易信息
 */
public interface BdcJyxxService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @return
     * @description 删除所有交易信息(交易信息、交易合同和交易申请人)
     */
    void delAllBdcJyxx(String proid);

}
