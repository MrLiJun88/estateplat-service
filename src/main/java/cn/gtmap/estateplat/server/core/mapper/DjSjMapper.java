package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sc
 * Date: 15-4-21
 * Time: 下午3:35
 * @description 获取权籍数据的Mapper
 */
@Repository
public interface DjSjMapper {
    BdcQszdZdmj getBdcQszdZdmj(final String djh);

    String getFwlxByProid(final String proid);

    List<DjsjQszdDcb> getDjsjQszdDcb(final String djh);

    List<DjsjZdxx> getDjsjZdxx(final String proid);

    List<DjsjZdxx> getDjsjZdxxByDjh(final String djh);

    List<DjsjQszdDcb> getDjsjQszdDcbByQszdDjdcbIndex(final String qszdDjdcbIndex);

    String getFbfmcByDjh(final String djh);

    List<DjsjNydDcb> getDjsjNydDcbByDjh(final String djh);

    DjsjCbzdDcb getDjsjCbzdDcbByDjid(final String djid);

    List<DjsjCbzdDcb> getDjsjCbzdDcbByDjh(final String djh);

    List<DjsjCbzdJtcy> getDjsjCbzdTtcyByProid(final String poid);

    List<DjsjCbzdCbf> getDjsjCbzdCbfByDcbid(final String dcbid);

    List<DjsjCbzdFbf> getDjsjCbzdFbfByDcbid(final String dcbid);

    List<DjsjZdxx> getDjsjZdxxByDjhxx(final String djh);

    String getBdcdyfwlxByBdcdyh(final String bdcdyh);
}
