package cn.gtmap.estateplat.server.core.service;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/18
 * @description 
 */

import java.util.List;
import java.util.Map;

public interface UpdateCxBdcdyZtService {

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param: bdcdyhList、bdclx
    *@Description:更新不动产单元表权利状态
    *@Date 9:41 2018/1/18
    */
    String updateBdcdyZtByBdcdyh(List<String> bdcdyhList, String bdclx);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:proid
    *@Description:根据proid找到对应的不动产单元号并更新
    *@Date 9:42 2018/1/19
    */
    Boolean updateBdcdyZtByProid(String proid);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:proid
    *@Description:根据proid获取bdcdyhList 和bdclx
    *@Date 10:53 2018/1/19
    */
    Map<String,Object> getMapByProid(String proid);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:map
    *@Description:根据map更新
    *@Date 10:58 2018/1/19
    */
    Boolean updateBdcdyZtByMap(Map<String, Object> map);
}
