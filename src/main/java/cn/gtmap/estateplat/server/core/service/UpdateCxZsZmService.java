package cn.gtmap.estateplat.server.core.service;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/3
 * @description 
 */

import java.util.List;
import java.util.Map;

public interface UpdateCxZsZmService {

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:通过proid获取相关联的产权证号
     *@Date 20:48 2018/1/2
     */
    public List<Map<String, Object>> getCqzhListByProid(String proid);

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:cqzhList
     *@Description:通过cqzh更新当前证书证明权利状态
     *@Date 20:52 2018/1/2
     */
    public void updateCxZsZmByCqzh(List<Map<String, Object>> mapList);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:proid
    *@Description:通过proid更新当前证书证明权利状态
    *@Date 14:11 2018/1/5
    */
    public  void updateCxZsZmByProid(String proid);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:proid
    *@Description:通过proid处理删除事件证书证明方法
    *@Date 14:23 2018/1/5
    */
    public  void updateZszmZtDeleteEnvent(String proid);
}
