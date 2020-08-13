package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdBdcQlRel;
import cn.gtmap.estateplat.model.server.core.GdQlDyhRel;

import java.util.List;

/**
 * @version 1.0, 2017/9/4.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description
 */
public interface GdQlDyhRelService {

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 根据参数生成相应的gdQlDyhRel数据，并返回权利数据，用于匹配
     **/
    List<String> saveGdQlDyhRels(String fwid, String bdclx, String qlid, String tdqlid, String bdcdyh, String djid);


    /**
     * @param
     * @return
     * @auto <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据参数生成相应的gdQlDyhRel数据，并返回权利数据，用于匹配(用于多本房产证匹配多本土地证)
     **/
    List<String> saveGdQlDyhRelsForMul(String fwid, String bdclx, String qlid, String tdqlid, String bdcdyh, String djid);

}
