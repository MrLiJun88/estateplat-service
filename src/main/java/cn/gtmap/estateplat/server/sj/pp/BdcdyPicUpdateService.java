package cn.gtmap.estateplat.server.sj.pp;

import cn.gtmap.estateplat.server.sj.InterfaceCode;

import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/26
 * @description 虚拟不动产单元匹配后更新数据服务
 */
public interface BdcdyPicUpdateService extends InterfaceCode {

    /**
     * @param paramMap 更新字段值参数
     * @param fwBdcdyid 房屋虚拟单元号ID
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据房屋虚拟单元号ID和虚拟单元号更新表中不动产单元相关信息
     */
    void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid,String fwBdcdyh);

    /**
     * @param paramMap 匹配更新字段参数
     * @param tdBdcdyid 土地虚拟单元号ID
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据土地虚拟单元号ID和虚拟单元号更新表中不动产单元相关信息
     */
    void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid,String tdBdcdyh);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 11:01
      * @description 根据ppgxXm表中的proid和要撤销的不动产单元  撤销匹配
      */
    void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh);
}
