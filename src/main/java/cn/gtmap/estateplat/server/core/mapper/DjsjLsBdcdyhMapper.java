package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 查询历史不动产单元上的所有不动产单元号
 * Created with IntelliJ IDEA.
 * User: lj
 * Date: 16-5-13
 * Time: 下午11:44
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface DjsjLsBdcdyhMapper {
    List<String> getBdcdyhListByBh(String bh);
}
