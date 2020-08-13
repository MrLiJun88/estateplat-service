package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wangfeike
 * @version 1.0, 2016/6/2
 * @description 张家港公开查询
 */
@Repository
public interface ZjgGkcxMapper {
	List<Map> getCfxxByZsid(Map map);

	List<Map> getDyaxxByZsid(Map map);
}
