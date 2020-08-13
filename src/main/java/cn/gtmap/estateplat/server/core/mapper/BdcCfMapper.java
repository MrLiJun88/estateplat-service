package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description 查封登记信息
 * Created by lst on 2015/3/18
 */

/**
 * @author  wangyunchen
 * 2016年6月30日
 * @version v1.0 
 * @description
 */
@Repository
public interface BdcCfMapper {

    /**
     * zdd 只更新查封登记的解封信息
     *
     * @param bdcCf 查封登记信息
     */
    void updateBdcCfForJfxx(final BdcCf bdcCf);

    /**
     * 根据登记项目ID查询查封信息
     *
     * @param proid 登记项目ID
     * @return
     */
    BdcCf selectCfByProid(final String proid);


    /**
     * 获取查封类型名称
     *
     * @return
     */
    List<String> getBdcCflxMc();


    /**
     * 根据不动产单元查询续封
     *
     * @param bdcdyh 不动产单元号
     * @return
     */
    List<BdcCf> getXfByBdcdyh(final String bdcdyh);

    /**
     * 根据不动产单元号获取轮候查封
     *
     * @param bdcdyh 不动产单元号
     * @return
     */
    List<BdcCf> getLhcfByBdcdyh(final String bdcdyh);

    /**
     * @param bdcdyid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<BdcCf>
     * @description 根据不动产单元id查询cf
     */
    List<BdcCf> getCfByBdcdyid(final String bdcdyid);

    /**
     * 查询查封信息
     *
     * @param map
     * @return
     */
    List<Map> queryBdcCfByPage(final Map map);


    /**
     * 根据不动产单元号查询预查封
     */
    List<BdcCf> queryYcfByBdcdyh(final String bdcdyh);


    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/4/5
     * @param:
     * @return:null
     * @description:通过各种条件查询查封的数据
     */
    List<BdcCf> getCfByMap(final Map hashMap);


    /**
     * @param bdcdyh
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<BdcCf>
     * @description 根据不动产单元号查询查封项目
     */
    List<BdcXm> getCfXmByBdcdyh(final String bdcdyh);
    
    /**
     * @author xiejianan
     * @description 查询过度和登记库的查封信息
     * @param map
     * @return
     */
    List<Map> queryBdcGdCf(final Map map);
    
    /**
     * @author wangyunchen
     * @description 查询查封信息
     * @param map
     * @return
     */
    List<Map> queryCfxxByPage(final Map map);


    /**
     * @author bianwen
     * @description  查询所有不动产单元上的现势查封（不动产和过度）
     */
    List<Map> selectAllCfxxByBdcdyh(final Map map);

    /**
     * @author jiangganzhi
     * @description  通过不动产单元号和查封文号获取查封权利
     */
    List<BdcCf> getBdcCfByCfwhAndBdcdyh(Map map);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 更新查封开始结束时间为空
     */
    void updatCfsjForNull(BdcCf bdcCf);
}
