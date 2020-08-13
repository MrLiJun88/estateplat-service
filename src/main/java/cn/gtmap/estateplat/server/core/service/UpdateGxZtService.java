package cn.gtmap.estateplat.server.core.service;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/5
 * @description 
 */

import cn.gtmap.estateplat.model.exchange.national.QlfQlZxdj;

import java.util.List;
import java.util.Map;

public interface UpdateGxZtService {

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:proid，isCompara，reason
    *@Description:根据wiid获取共享的yproid
    *@Date 16:03 2018/1/5
    */
    public Map getGxZtByProid(String proid, String isCompara,String reason);
    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @param:proid
     * @Description:共享获取proidList和Yproidlist并更新
     * @Date 14:37 2018/1/5
     */
    public void updateGxztByProid(String proid);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @param:proid
     * @Description:退回时共享获取proidList和Yproidlist并更新
     * @Date 14:37 2018/1/5
     */
    public void updateGxztForBack(String proid);


    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:map
    *@Description:共享的删除更新操作
    *@Date 16:14 2018/1/5
    */
    public void updateGxztByMap(Map map);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@param:proid
    *@Description:共享获取对象List为了注销使用
    *@Date 14:44 2018/1/5
    */
    public List<QlfQlZxdj> getGxListByProid(String proid);
}
