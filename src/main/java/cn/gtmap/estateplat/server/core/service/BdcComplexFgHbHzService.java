package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.QllxParent;

import java.util.List;

/**
 * Created jiangganzhi on 2017/3/2.
 */
public interface BdcComplexFgHbHzService {

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *
     * @param wiid
     * @rerutn
     * @description 通过wiid获取节点名称 判断是否是土地分割合并换证登记
     */
    String isTdFgHbHzDj(final String wiid);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *
     * @param bdcdyid
     * @rerutn
     * @description 获取剩余土地换证后新生成的proid作为剩余土地抵押项目的yproid
     */
    String getSytdProid(final String bdcdyid,final String wiid);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *
     * @param bdcdyid
     * @rerutn
     * @description 通过bdcdyid获取原宗地的proid
     */
     String getYzdProidByBdcdyid(final String bdcdyid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *
     * @param wiid
     * @rerutn
     * @description 土地分割合并换证登记 在缮证后将换证项目的不动产权证号插入抵押项目中的原不动产权证号 同时将原宗地抵押注销
     */
     void saveYbdcqzhToDybdcxm(final String wiid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *
     * @param wiid
     * @rerutn
     * @description    土地分割合并换证退回将注销的原土地的抵押还原
     */
    void changeYzdDyQszt(final String wiid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *
     * @param bdcdyh
     * @rerutn
     * @description    土地分割合并换证 获取原宗地的查封
     */
    List<QllxParent> getYzdCf(String bdcdyh);
}
