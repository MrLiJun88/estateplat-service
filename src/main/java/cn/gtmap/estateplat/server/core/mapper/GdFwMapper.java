package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过渡房产证
 * Created by lst on 2015/3/23
 */
@Repository
public interface GdFwMapper {
    /**
     * yl 获取房屋数据信息
     *
     * @param map
     * @return
     */
    List<GdFw> getGdFwJsonByPage(Map map);

    /**
     * 获取房屋信息
     *
     * @param map
     * @return
     */
    List<GdFw> getGdFw(Map map);

    GdLq getGdLqBylqid(String lqzh);

    GdCq getGdCqByCqid(String cqzh);

    String getGdFczhByDah(String dah);

    List<String> getFwidByDah(String dah);

    List<Map> getGdXmFwJsonByPage(Map map);

    List<GdFwsyq> getGdFwsyq(String fwid);

    /**
     * 获取过渡权利列表
     *
     * @param map
     * @return
     */
    List<Map> getGdQlList(Map map);

    /**
     * sc根据gdProid获取过度房屋信息
     *
     * @param proid
     * @return
     */
//    List<GdFw> getGdFwByProid(String proid);


    /**
     * sc 根据fwid获取房产证号
     *
     * @param fwid
     * @return
     */
    String getFczhByFwid(String fwid);


    /**
     * sc 根据fwid获取房产证号
     *
     * @param fwid
     * @return
     */
    String getFczhByGdproid(String fwid);

    /**
     * 根据过渡proid获取过渡fwzl
     *
     * @param proid
     * @return
     */
    List<String> getGdfwZlByproid(String proid);

    /**
     * zdd 根据不动产单元号  获取权利信息
     *
     * @param map
     * @return
     */
    List<DjbQlPro> getGdqlByBdcdyh(Map<String, String> map);


    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/3/16
     * @param:map
     * @return:List<GdFw>
     * @description:根据gd_xm的proid获取相应的gd_fw
     */
    List<GdFw> getGdFwByGdProid(HashMap hashMap);

    /**
     * hqz 根据预告权利找到现实的首次登记证书
     *
     * @param ygid
     * @return
     */
    GdFwsyq getGdFwsyqByYgQlid(String ygid);

    /**
     * @param tdid
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @rerutn
     * @description 通过房屋土地id查询不动产单元号
     */
    String getBdcdyhByFwtdid(String tdid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 查询过渡房屋权利
     */
    List<HashMap> getGdFwQl(HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 查询过渡房屋权利
     */
    List<GdFwQl> getGdFwQlByHashMap(HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据qlid获取权利人
     */
    HashMap<String, String> getGdqlr(@Param(value = "qlid") String qlid);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据GdProid获取CQQID
     */
    List<String> getCqqidByGdProid(HashMap map);

    /**
     * @param qlid 权利ID
     * @return 权利相关的房屋信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据权利ID查找返回的房屋信息
     */
    List<GdFw> getGdFwByQlid(String qlid);

    /**
     * @param bdcdyh
     * @param fwid
     * @return
     * @description 根据不动产单元号或者房屋id查找gd_fw与djsj_bdcdy_tdfw的关系
     */
    HashMap<String, String> getBdcdyhAndFwid(@Param(value = "bdcdyh") String bdcdyh, @Param(value = "fwid") String fwid);

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 更具权利ID更新补录数据的审核状态
     */
    void updateSfsh(String qlid);

    HashMap<String, String> getGdZsJgJsonByPage(Map map);

    /**
     * 通过bdcdyid 获得 gdfw
     *
     * @param
     * @return
     */
    List<GdFw> getGdFwByBdcdyid(String bdcdyid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn String
     * @description 获取需要排除的mc
     */
    String getGdFwExclx(HashMap map);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @rerutn String
     * @description 通过fwid查找房屋附属设施
     */
    BdcFwfsss getFwfsssByFwid(String fwid);

    /**
     * @param map
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn List<HashMap>
     * @description 根据档案号或者坐落获取不动产单元号
     */
    List<HashMap> getBdcdyh(HashMap map);

    /**
     * @param zl
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn List<String>
     * @description 根据坐落获取TDQLID
     */
    List<String> getTdQlidByZl(String zl);

    /**
     * @param zl
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn List<String>
     * @description 根据坐落获取tdid
     */
    List<String> getTdidByZl(String zl);

    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn String
     * @description 根据不动产单元号获取房产证号
     */
    List<HashMap> getFczhByBdcdyh(String bdcdyh);

    List<Map> selectGdfwNopp();

    List<Map>  selectGdfwPpTdNoBdcdy();

    List<GdFwsyq> queryFwsyqByCqzh(HashMap map);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param map
     * @return tdqlid
     * @description 通过权利类型和不动产单元号查匹配的权利是否匹配土地证
     */
    List<String> getTdQlidByQllx(Map map);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过权籍bdcdyh查找fwid
     */
    List<String> getFwidByBdcdyh(String bdcdyh);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据fwidList查询显示fwsyq的proid
     */
    List<String> getXsFwsyqProidByfwid(List<String> fwidList);
}
