package cn.gtmap.estateplat.server.core.service;

import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记收费项目
 */

public interface ZjgGkcxService {
	
	List<Map> getCfxxByZsid(final Map map);
	
	
	List<Map> getDyaxxByZsid(final Map map);
}
