package cn.gtmap.estateplat.server.service.etl;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/7/11
 * @description 互联网+银行申请接口
 */
public interface EtlGxYhService {

    /**
     * @param proid
     * @param wiid
     * @param bjblzt
     * @return
     * @auto <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 更新互联网+银行登簿状态
     **/
    void dbYhxx(String proid, String wiid,String bjblzt,String userid);
}
