package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSjcl;
import cn.gtmap.estateplat.model.server.core.BdcSjxx;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;

import java.util.List;
import java.util.Map;

/**
 * 不动产登记收件信息配置.
 *
 * @author juyulin
 * @version V1.0, 16-12-8
 * @since
 */
public interface BdcSjxxService {

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param wiid
     * @return
     * @description  根据wiid获取不动产收件信息
     */
    BdcSjxx queryBdcSjxxByWiid(String wiid);
    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param proid
     * @return
     * @description  根据wiid获取不动产收件信息
     */
    BdcSjxx queryBdcSjxxByProid(String proid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param bdcSjxx
     * @return
     * @description 保存收件信息
     */
    void saveBdcSjxx(BdcSjxx bdcSjxx);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取收件材料
    *@Date 15:17 2017/3/29
    */
    List<Map> getSjclWithProidByPage(Map map);
/**
*@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
*@Description:获取系统配置的收件材料
*@Date 15:26 2017/3/29
*/
List<Map> getSjclWithProidAndDjzxByPage(Map map);
/**
*@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
*@Description:获取收件材料最大序号+1
*@Date 18:04 2017/4/19
*/
String getSjclXhBySjxxid(String sjxxid);

/**
  * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
  * @Time 2020/6/11 15:28
  * @description 获取保存收件信息
  */
    BdcSjxx saveBdcSjxx(String wfProid);
}
