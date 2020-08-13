package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcPpgx;
import cn.gtmap.estateplat.model.server.core.BdcPpgxXm;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/26 0026
 * @description 不动产匹配关系表 服务
 */
public interface BdcPpgxService {
    /**
     * @param fwproid 房屋项目ID
     * @return 不动产匹配关系表
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据房屋项目ID获取不动产匹配关系表
     */
    BdcPpgx getBdcPpgxByFwproid(String fwproid);

    /**
     * @param bdcdyh 不动产单元号
     * @return 不动产匹配关系表
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取不动产匹配关系表
     */
    List<BdcPpgx> getBdcPpgxByBdcdyh(String bdcdyh);

    /**
     * @param bdcdyh 不动产单元号
     * @return 不动产匹配关系表
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取不动产匹配关系表
     */
    List<BdcPpgx> getBdcPpgxByBdcdyh(String bdcdyh,String fwproid);


    /**
     * @param tdproid 土地项目ID
     * @return 不动产匹配关系表
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据土地项目ID获取不动产匹配关系表
     */
    BdcPpgx getBdcPpgxByTdproid(String tdproid);


    /**
     * @param bdcPpgx
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 保存不动产匹配关系表
     */
    void saveBdcPpgx(BdcPpgx bdcPpgx);

    /**
     * @param bdcPpgxXm
     * @return
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @description 保存不动产匹配关系项目表
     */
    void saveBdcPpgxXm(BdcPpgxXm bdcPpgxXm);

    /**
     * @param map 关系表参数map
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据传入参数Map插入或更新关系表
     */
    void saveOrUpdateBdcPpgxByMap(Map map);

    /**
     * @param ppParamMap
     * @param cxBdcdyh
     * @param sfhqqj
     * @return
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @description 保存不动产匹配关系表
     */
    void saveBdcPpgxByMap(Map<String, Object> ppParamMap, String fwProid, String tdProid, String fwBdcdyh, String tdBdcdyh, String czlx, String cxBdcdyh, String sfhqqj);

    /**
     * @param ppParamMap
     * @return
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @description 保存不动产匹配关系项目表
     */
    void saveBdcPpgxXmByMap(Map<String, Object> ppParamMap, String proid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/1 21:25
      * @description  获取匹配结果打印xml
      */
    DataToPrintXml getPpjgPrintXml(String proid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/14 17:32
      * @description 根据不动产单元号查询bdcPpgxXm
      */
    List<BdcPpgxXm> getBdcPpgxXmByBdcdyh(String bdcdyh);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 16:43
      * @description 保存不动产匹配关系日志表
      */
    void saveBdcPpgxLogByMap(Map<String,Object> ppParamMap, String fwproid, String tdproid, String fwbdcdyh, String tdbdcdyh, String bdcPpCzlxCx);
}
