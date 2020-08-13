package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcSjcl;

import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * @description 收件材料
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-9-18
 * Time: 上午8:57
 */
@Repository
public interface BdcSjclMapper {
    /**
     * 根据proid获取收件材料
     *
     * @param proid
     * @return
     */
    List<BdcSjcl> getSjclByproid(String proid);

    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param oldCode
    * @return
    * @description 根据老的checkcode获得新的checkcode
    */
    Integer getNewCheckCode(Integer oldCode);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:根据sjxxid获取最大xh
    *@Date 11:39 2017/3/7
    */
    Integer getSjclMaxXh(String sjxxid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param map
     * @return
     * @description  根据t条件获取不动产系统配置收件材料信息
     */
    List<BdcSjcl> getbdcXtSjcl(Map map);
}
