package cn.gtmap.estateplat.server.core.service;

import java.util.List;

/**
 * zwq
 * @description 证明书附件的其他情况获取
 */
public interface ZmsFjService {
    //证明书附记的修改和不让其在退回时叠加
    // public void saveFj(String proid,String qllx);
    //验证字符串是否在字符串数组中，以及在数组中的位置
    List<String> changeQkSz(List<String> list, String xx, String scfj, String qz);
}
