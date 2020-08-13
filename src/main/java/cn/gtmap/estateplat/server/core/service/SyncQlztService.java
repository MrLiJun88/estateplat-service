package cn.gtmap.estateplat.server.core.service;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/3
 * @description 
 */

import java.util.List;
import java.util.Map;

public interface SyncQlztService {

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:maplist
     *@Description:根据产权证号更新证书证明、不动产单元、共享库的权利状态
     *@Date 10:20 2018/1/3
     */
    public boolean updateQlztAfterDelet(List<Map<String, Object>> mapList);
    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:通过proid获取相关联的产权证号
     *@Date 10:26 2018/1/3
     */
    public  List<Map<String, Object>> queryQlztBeforDelet(String proid);
    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:通过proid获取共享所需要的proidList和YproidList
     *@Date 16:00 2018/1/5
     */
    public  Map queryGxztBeforDelet(String proid);

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:
     *@Date 16:01 2018/1/5
     */
    public void updateGxztAfterDelet(Map map);

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:通过proid获取不动产单元状态管理需要的bdcdyList和bdclx
     *@Date 11:06 2018/1/19
     */
    public  Map queryBdcdyZtMapBeforDelet(String proid);

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:map
     *@Description:通过map更新对应的不动产单元号
     *@Date 11:06 2018/1/19
     */
    public  void updateBdcdyZtMapAfterDelet(Map<String, Object> map);

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:转发和办结时更新证书证明、推送共享数据
     *@Date 14:03 2018/1/5
     */
    public  void updateQlztByProid(String proid);

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:退回时更新证书证明、推送共享数据
     *@Date 18:37 2018/1/6
     */
    public  void updateQlztByProidForBack(String proid);
    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@param:proid
     *@Description:工作流删除事件时，需要同步处理的证书证明、及共享管理
     *@Date 14:21 2018/1/5
     */
    public void updateQlztForDelete(String proid);
}
