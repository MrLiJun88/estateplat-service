package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcFwfzxx;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by lst on 2015/3/18
 * @description 房地产权登记信息（项目内多幢房屋）
 */
@Repository
public interface BdcFdcqDzMapper {
    /**
     * 获取房地产权登记信息（项目内多幢房屋）
     *
     * @param proid
     * @return
     */
    BdcFdcqDz getBdcFdcqDz(final String proid);

    /**
     * 根据权利Id删除房屋发证信息
     * @param qlid
     */
    void delBdcFwfzxxByQlid(final String qlid);

    /**
     * g根据权利Id获取对多幢项目对应的项目集合
     * @param qlid
     * @return
     */
    List<BdcFwfzxx> queryBdcFwfzxxlstByQlid(final String qlid);

    BdcFdcqDz getBdcFdcqDzByBdcdyid(final String  bdcdyid);

    List<BdcFwfzxx> queryBdcFwfzxxlstByProid(final String proid);

    /**
     * 获取房地产权登记信息（项目内多幢房屋）
     *
     * @param map
     * @return
     */
    List<BdcFdcqDz>  getBdcFdcqDzList(Map map);

}
