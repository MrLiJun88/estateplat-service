package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020/3/21
 * @description 不动产完税信息
 */
public interface BdcWsxxService {

    List<BdcWsxx> getBdcWsxxListByProid(final String proid);

    List<BdcWsmx> getBdcWsmxListByWsxxid(final String wsxxid);
}