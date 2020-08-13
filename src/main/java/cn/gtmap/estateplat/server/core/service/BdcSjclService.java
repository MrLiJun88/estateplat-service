package cn.gtmap.estateplat.server.core.service;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2017/3/7
 * @description 
 */

import cn.gtmap.estateplat.model.server.core.BdcSjcl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface BdcSjclService {
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description: 获取收件材料序号最大值
    *@Date 11:32 2017/3/7
    */
    Integer getSjclMaxXh( String sjxxid);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存新增的收件材料
    *@Date 13:43 2017/3/7
    */
    void saveBdcSjcl(BdcSjcl bdcSjcl);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:删除所选的收件材料
    *@Date 15:09 2017/3/7
    */
    void delSjclxx(String sjclid);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:根据sjclid获取收件材料
    *@Date 19:45 2017/3/8
    */
    BdcSjcl getBdcSjclById(String sjclid);


    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param sjxxid
     * @return
     * @description  根据收件信息id获取不动产收件材料信息
     */
    List<BdcSjcl> queryBdcSjclBySjxxid(String sjxxid);
    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param map
     * @return
     * @description  根据t条件获取不动产系统配置收件材料信息
     */
    List<BdcSjcl> getbdcXtSjcl(Map map);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:根据sjxxid和材料名称删除对应的收件材料
    *@Date 10:22 2017/4/1
    */
    void delSjclBySjxxidAndClmc(String sjxxid,String clmc);
}
