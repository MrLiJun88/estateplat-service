package cn.gtmap.estateplat.server.core.service;


/**
* @Description: 对bdc_xygl表的操作
* @author xusong
* @date 2018/8/8 17:46
*/
public interface BdcXyglService {
    /**
    * @Description: 通过权利人证件号查看是否是保障性住房者。
    * @param qlrzjh 权利人证件号
    * @return 是否是保障性住房者
    * @author xusong
    * @date 2018/8/8 17:47
    */
    boolean checkBzfry(final String qlrzjh);
}
