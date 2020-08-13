package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by lst on 2015/3/18
 * @description 不动产抵押权登记信息
 */

public interface BdcDyaqMapper {
    /**
     * 获取不动产抵押权登记信息
     *
     * @param map
     * @return
     */
    List<BdcDyaq> queryBdcDyaq(final Map map);

    /**
     * zdd 只更新注销登记的抵押信息
     *
     * @param bdcDyaq
     */
    void updateBdcDyaqForZxdj(final BdcDyaq bdcDyaq);

    /**
     * 通过proid获取原项目的对应的某权属状态的抵押权数据
     * @author lisongtao
     * @param proid 项目id
     * @param qszt 权属状态
     * @return
     */
    List<BdcDyaq> queryYdyaqByProid(@Param("proid") final String proid,@Param("qszt") final Integer qszt);
    
    /**
     * @author xiejianan
     * @description 从过度和登记库中读取抵押权信息
     * @param map
     * @return
     */
    List<Map> queryBdcDyaqByPage(final Map map);

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @param map
     * @rerutn
     * @description 通过fwid和wiid确定当前项目下的抵押权
     */
    BdcDyaq queryBdcDyaqByFwid(HashMap map);

    /**
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @param proid
     * @rerutn
     * @description 获取抵押权本地打印信息
     */
    Map<String,Object> getDyaqxxForPrint(String proid);

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @param proid
     * @rerutn
     * @description 通过proid确定当前项目下的抵押权
     */
    BdcDyaq queryBdcDyaqByProid(String proid);
}
