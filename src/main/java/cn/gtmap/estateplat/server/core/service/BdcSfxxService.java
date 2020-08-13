package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSfxm;
import cn.gtmap.estateplat.model.server.core.BdcSfxx;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记收费信息
 */
public interface BdcSfxxService {
/**
*@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
*@Description:获取收费信息的标准(去掉重复)
*@Date 15:54 2017/4/6
*/
    List<HashMap> getxtsfbzBySqlx(String sqlx,String qlrlx);
    List<HashMap> getxtsfdwBySqlx(String sqlx,String qlrlx);


    /**
     * @param map
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn List<HashMap>
     * @description 根据受理编号获取收费信息
     */
    public List<HashMap> getSfxxMapBySfxxid(HashMap map);

    DataToPrintXml getFpxxPrintXml(final String sfxxid) throws UnsupportedEncodingException;

    MulDataToPrintXml getMulFpxxPrintXml(Map<String, String> proidAndzsid) throws UnsupportedEncodingException;


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param sfzt
     * @rerutn
     * @description 更新收费状态和收缴日期
     */
    public void updateBdcSfxx(String proid,String sfzt,Date sjrq);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 通过proid获取sfxx
     */
    BdcSfxx getbdcSfxxByProid(String proid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 根据proid改变sfxx为已收费
     */
    void changesfzt(String proid);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @rerutn
     * @description 获取收费信息
     */
    List<Map> getBdcSfxxList(HashMap<String, Object> map);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @rerutn
     * @description 保存打印临时表
     */
    void insertIdToTemp(Map map);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @rerutn
     * @description 删除打印临时表
     */
    void delPrintBdcSfxxTemp(String uuid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/7/3 10:22
      * @description  根据Wiid查询收费项目
      */
    List<BdcSfxm> queryBdcSfxmByWiid(String wiid);
}
