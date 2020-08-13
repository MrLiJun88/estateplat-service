package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcJjdXx;

import java.util.List;
import java.util.Map;

/**
 * 不动产登记项目交接单
 * User: apple
 * Date: 15-12-10
 * Time: 下午7:59
 * To change this template use File | Settings | File Templates.
 */
public interface BdcJjdService {
    BdcJjdXx getBdcJjdXxBySlh(final String slh);

    List<BdcJjdXx> getJjdxxJsonByPage(final Map map);

    String creatBdcJjd(final String slhs, final String user);

    String getJjdXh(final String nf);

    String deleteJjd(final String jjdid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 14:12
      * @description 根据proid查询bdcjjd
      */
    BdcJjdXx getBdcJjdXxByProid(String proid);
}
