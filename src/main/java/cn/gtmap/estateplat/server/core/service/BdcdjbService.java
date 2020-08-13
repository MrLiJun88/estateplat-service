package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记簿
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-18
 */
public interface BdcdjbService {
    /**
     * zdd 根据宗地宗海查找bdcdjb
     *
     * @param zdzhh
     * @return
     */
    List<BdcBdcdjb> selectBdcdjb(final String zdzhh);

    /**
     * zdd 将宗地信息读取到不动产登记薄
     *
     * @param djsjZdxx
     * @param bdcdjb
     * @return
     */
    BdcBdcdjb getBdcdjbFromZdxx(DjsjZdxx djsjZdxx, BdcBdcdjb bdcdjb);

    /*
    * zwq
    * 将权属宗地的信息放入登记簿中
    * */

    BdcBdcdjb getBdcdjbFromQsdcb(DjsjQszdDcb djsjQszdDcb, BdcBdcdjb bdcdjb);

    /**
     * zdd 将宗地信息读取到不动产登记薄
     *
     * @param djsjNydDcbList
     * @param bdcdjb
     * @return
     */
    BdcBdcdjb getBdcdjbFromNydZdxx(List<DjsjNydDcb> djsjNydDcbList, BdcBdcdjb bdcdjb);

    /**
     * zdd 将林权信息读取到不动产登记薄
     *
     * @param djsjLqxx
     * @param bdcdjb
     * @return
     */
    BdcBdcdjb getBdcdjbFromLqxx(DjsjLqxx djsjLqxx, BdcBdcdjb bdcdjb);

    /**
     * zdd 将房屋信息读取到不动产登记薄
     *
     * @param djsjFwxx
     * @param bdcdjb
     * @return
     */
    BdcBdcdjb getBdcdjbFromFwxx(DjsjFwxx djsjFwxx, BdcBdcdjb bdcdjb);

    /**
     * zdd 将项目信息读取到不动产登记薄
     *
     * @param project
     * @param bdcdjb
     * @return
     */
    BdcBdcdjb getBdcdjbFromProject(Project project, BdcBdcdjb bdcdjb);


    /**
     * zdd 将项目信息读取到不动产登记薄
     *
     * @param projectPar
     * @param bdcdjb
     * @return
     */
    BdcBdcdjb getBdcdjbFromProjectPar(ProjectPar projectPar, BdcBdcdjb bdcdjb);


    /**
     * sc 多条件查询bdcdjb
     *
     * @param map
     * @return
     */
    List<BdcBdcdjb> getBdcdjbByPage(final Map map);

    /**
     * sc 根据不动产登记簿拼出Json
     *
     * @param map
     * @return
     */
    String getJosnByDjbList(final Map map);

    /**
     * lst 根据djbid获取宗地宗海号
     *
     * @param djbid
     * @return
     */
    BdcBdcdjb getBdcBdcdjbByDjbid(final String djbid);

    /**
     * zx 根据登记薄获取不动产单元目录
     *
     * @param map
     * @return
     */
    List<Map> getQldjByPage(final Map map);

    /**
     * 跟新登记簿登簿人登记时间
     *
     * @param proid
     * @param userName
     */
    void updateDjb(final String proid,final String userName,final QllxVo qllxVo);

    /**
     * zhx 将宗海信息读取到不动产登记薄
     *
     * @param djsjZhxx
     * @param bdcdjb
     * @return
     */
    BdcBdcdjb getBdcdjbFromZhxx(DjsjZhxx djsjZhxx, BdcBdcdjb bdcdjb);

    //zhouwanqing 根据zdzhh删除登记簿
    void delBdcdjbByZdzhh(final String zdzhh);


    /**
     * @author bianwen
     * @description 读取构筑物信息到登记簿
     */
    BdcBdcdjb getBdcdjbFromGzwxx(DjsjGzwxx djsjGzwxx, BdcBdcdjb bdcdjb);


    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param xmxx 创建项目参数集
    * @return
    * @Description: 在开启多线程创建不动产首次登记之前，先创建不动产登记簿，避免以后的线程不安全。
    */
    void initBdcBdcdjb(Xmxx xmxx);

    /**
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @param xmxx 创建项目参数集
     * @param zdzhhList 项目宗地宗海号列表
     * @return
     * @Description: 在开启多线程创建不动产首次登记之前，先创建不动产登记簿，避免以后的线程不安全。
     */
    void initBdcBdcdjb(Xmxx xmxx, List<String> zdzhhList);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param initVoFromParm
     * @return
     * @Description: 初始化登记簿
     */
    BdcBdcdjb initBdcdjb(final InitVoFromParm initVoFromParm,final String fwProid,final String tdProid,String zdzhh,final String userid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @return
     * @Description: 保存登记簿
     */
    void saveBdcBdcdjb(BdcBdcdjb bdcBdcdjb);
}
