package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.GdCf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 过渡查封
 */
public interface GdCfMapper {

    List<HashMap> queryGdFcCfList(final HashMap paraMap);
    List<HashMap> queryGdTdCfList(final HashMap paraMap);
    List<HashMap> getGdCfListByMap(final HashMap paraMap);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param  map
     * @description 根据cfid集合和查封状态获取过度查封
    */
    List<GdCf> queryGdCfListByQueryMap(final Map map);

    /**
     * @author jiangganzhi
     * @description  通过不动产单元号和查封文号获取过渡查封权利
     */
    List<GdCf> getGdCfByCfwhAndBdcdyh(Map map);

    /**
     * @author jiangganzhi
     * @description  通过房产证号和查封文号获取过渡查封权利
     */
    List<GdCf> getGdCfAndCfwhByFczh(Map map);

    /**
     * @author jiangganzhi
     * @description  通过土地证号和查封文号获取过渡查封权利
     */
    List<GdCf> getGdCfAndCfwhByTdzh(Map map);
}
