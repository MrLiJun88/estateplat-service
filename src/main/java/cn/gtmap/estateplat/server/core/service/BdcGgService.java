package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcGg;
import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/3/6
 * @description 公告接口
 */
public interface BdcGgService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @param userid 用户ID
     * @return
     * @description 获取不动产公告编号
     */
    String getBdcGgBh(BdcXm bdcXm,String userid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @return
     * @description 删除不动产公告
     */
    void deleteBdcGg(BdcXm bdcXm);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存公告信息
    *@Date 10:46 2017/3/8
    */
    void saveGgxx(BdcGg bdcGg);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:删除选中的公告信息
    *@Date 14:01 2017/3/8
    */
    void deleteGgxx(String ggid);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:根据ggid获取公告信息
    *@Date 15:23 2017/3/8
    */
    BdcGg getBdcGgByGgid(String ggid);

    /**
     *获取公告
     * @param map
     * @return
     */
    List<BdcGg> getBdcGg(Map map);

}
