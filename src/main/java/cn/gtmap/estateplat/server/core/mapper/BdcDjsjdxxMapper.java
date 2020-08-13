package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcSjcl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizhi
 * @version V1.0, 17-02-21
 * @description 不动产登记收件单信息
 */
@Repository
public interface BdcDjsjdxxMapper {
    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description  根据proid查询sjcl
     */
    Integer getBdcSjclByProid(String proid);

    /** * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @param
     * @rerutn
     * @description    根据proid查询djzx
     */
    String  getDjzxByProid(String proid);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:根据proid获取收件材料信息
    *@Date 15:24 2017/3/29
    */
    List<Map> getSjclWithProidByPage(Map map);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取系统配置的收件材料
    *@Date 15:27 2017/3/29
    */
    List<Map> getSjclWithProidAndDjzxByPage(Map map);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取收件材料最大号+1
    *@Date 18:07 2017/4/19
    */
    String getSjclXhBySjxxid(String sjxxid);
}
