package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.GdCf;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 过渡查封服务
 */
public interface GdCfService {
    /**
    * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
    * @data:2016/4/6
    * @param:
    * @return:null
    * @description:通过gdfwsyq或者gdtdsyq获得过度查封
    * */
    List<GdCf> getGdCfListByQlid(final String qlid,final Integer iszx);

    /**
     * @author:<a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param:
     * @description:通过任意过渡权利的gdproid获取对应现势的过渡查封
     * */
    List<GdCf> getGdCfListByAnyGdProid(String gdProid);

    /**
     * @author:<a href="mailto:xusong@gtmap.cn">xusong</a>
     * @param:
     * @description:通过任意不动产id（gdid或者tdid）去获取对应的过度查封
     * */
    List<GdCf> getGdCfListByBdcid(final String bdcid, final Integer iszx);

    /**
     * @author:<a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param: map
     * @description: 根据cfid和查封状态获取对应的过度查封
    */
    List<GdCf> queryGdCfListByQueryMap(final Map map);

    /**
     * @param cfid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据cfid获取过度查封
    */
    GdCf getGdCfByCfid(final String cfid);

    /**
     * @param gdCf
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 保存gdCf
    */
    void saveGdCf(GdCf gdCf);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 过渡查封解封 办结时同步解封文号等信息到过渡查封中
     */
    void updateGdCfInfo(BdcXm bdcXm, BdcCf bdcCf);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 删除房屋、土地解封相关的记录
     */
    String deleteGdJf(String proid);

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
