package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcGdxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zdd on 2016/1/7.
 * 不动产登记项目归档信息服务
 */
public interface BdcGdxxService {
    /**
     * zdd 插入归档信息
     *
     * @param bdcGdxx
     */
    void insertBdcGdxx(BdcGdxx bdcGdxx);

    /**
     * zdd 更新归档信息
     *
     * @param bdcGdxx
     */
    void updateBdcGdxx(BdcGdxx bdcGdxx);

    /**
     * zdd 根据档案系统返回的xml信息   初始化归档信息
     *
     * @param gdxml 档案系统归档xml
     * @return
     */
    BdcGdxx initBdcGdxx(BdcGdxx bdcGdxx, final String gdxml);
    /**
     * 
     * @author wangtao
     * @description 判断是否已经归档
     * @param 
     * @return boolean
     */
    boolean checkIsGd(final String porid);
	
	 /**
     * 
     * @description 根据wiid判断是否已经归档
     * @param 
     * @return boolean
     */

    boolean checkAIsGd(final String wiid);

    /**
     * @author bianwen
     * @description  批量归档
     */
    List<BdcGdxx> initPlBdcGdxx(String gdxml);

    /**
     * @author bianwen
     * @description  获取归档信息
     */
    BdcGdxx selectBdcGdxx(BdcXm bdcXm);

    /**
     * @author yanzhenkun
     * @description  获取归档封面信息
     */
    List<HashMap> getDafpxxMapByproid(HashMap map);

    /**
     * @author yanzhenkun
     * @description  组织档案封皮信息数据
     */
    DataToPrintXml getDafpxxPrintXml(String proid);
    /**
     * @author yanzhenkun
     * @description  组织批量档案封皮信息数据
     */
    MulDataToPrintXml getAllDafpxxPrintXml(List<String> proidList);

    /**
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param daid
     * @description 根据档案id获取Gdxx
     */
     List<BdcGdxx> getGdxxByDdid(String daid);

    /**
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param proid
     * @description 根据Xmid(proid)获取Gdxx
     */
    List<BdcGdxx> getGdxxByXmid(String proid);
    /**
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param bdcGdxx
     * @description
     */
    void updateGdxxByNew(BdcGdxx bdcGdxx,BdcGdxx newBdcGdxx);

    /**
    * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
    * @param wiid
    * @description 根据归档的wiid获取所需要的proid
   */
    String getGdxxProidByWiid(String wiid);

    /**
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param bdcGdxx
     * @description 保存Gdxx
     */
    void saveGdxxForPl(BdcGdxx bdcGdxx, String userid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params bdcGdxx
     *@description 保存批量流程的gdxx
     */
    void saveGdxxMulForPl(BdcGdxx bdcGdxx);

    /**
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param mlh
     * @description 获取当前能使用的最大案卷号
     */
    int getMaxAjhByMlh(String mlh, boolean sfGd);

    /**
     * @author yanzhenkun
     * @description  获取查封归档封面信息
     */
   List<HashMap> getCfDafpxxMapByproid(String proid);

   /**
    *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
    *@param bhList
    *@return
    *@description 生成交接单编号
    */
    HashMap<String,String> updateJjdbh(List<String> bhList);

   /**
    * @param bdcXmlist
    * @param userName
    * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
    * @description 更新和插入归档信息
   */
   void saveOrUpdateBdcGdxx(List<BdcXm> bdcXmlist,String userName);
    /**
     * @param  bdcGdxx
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据目录号和案卷号重新生成daid
    */
   void reOrganizeDaidByAjhAndMlh(BdcGdxx bdcGdxx,boolean ishandledAjh);
    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">xinghuajian</a>
     *@params
     *@return
     *@description 批量修改按钮点击修改案卷号和目录号
     */
   Map<String,String> saveMulGdxxInfo(String ids, BdcGdxx bdcGdxx, String username,Boolean isMul);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">xinghuajian</a>
     *@params
     *@return
     *@description 批量修改按钮点击修改案卷号和目录号
     */
    Map<String,String> saveMulGdxxInfoGd(String gdids, String mlh, String ajh);
   /**
    *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
    *@params
    *@return
    *@description 通过编号获取产权的proid和wiiid
    */
   Map getCqProidAndCqWiidByBh(String bh);
   /**
    *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
    *@params
    *@return
    *@description 获取归档信息填充弹出框
    */
   Map<String,Object>getCurrentGdxxInfo(String proid);
   /**
    *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
    *@params
    *@return
    *@description 异步通过proid获取归档信息
    */
    JSONObject getBdcGdxxByProid(String proid);

    /**
     *@auther <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *@params
     *@return
     *@description 根据wiid推送互联网+报件状态为移交状态
     */
    void updateJyZtByWiid(String wiid,String userid);

    /**
     *@auther <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *@params
     *@return
     *@description 获取过渡档案封皮
     */
    List<HashMap> getGdDafpxxMapByGdxxid(HashMap map);

    /**
     * @author jiangganzhi
     * @description  组织过渡档案封皮信息数据
     */
    DataToPrintXml getGdDafpxxPrintXml(String gdxxid);

    /**
     * @author yanzhenkun
     * @description  组织过渡批量档案封皮信息数据
     */
    MulDataToPrintXml getAllGdDafpxxPrintXml(List<String> gdxxidList);

    /**
     *@auther <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *@params
     *@return
     *@description 异步通过gdxxid获取归档信息
     */
    JSONObject getBdcGdxxByGdxxid(String gdxxid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据proid获取归档信息
     */
    BdcGdxx queryBdcGdxxByProid(String proid);

    /**
     *@auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     *@description 根据proid验证归档信息
     */
    String checkGdxxByProid(String proid);
}
