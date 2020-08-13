package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2017-04-13
 * @description 不动产登记附属设施信息
 */
public interface BdcFwFsssService {

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @rerutn String
     * @description 通过fwid查找房屋附属设施
     */
    BdcFwfsss intiFwfsssByFwid(String fwid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param bdcFwfsss
     * @rerutn
     * @description 保存房屋附属设施(不会更新Null字段)
     */
    void saveBdcFwfsss(BdcFwfsss bdcFwfsss);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param bdcFwfsss
     * @rerutn
     * @description 保存房屋附属设施(Null字段也会更新)
     */
    void saveBdcFwfsssNull(BdcFwfsss bdcFwfsss);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param project
     * @rerutn
     * @description 通过主房不动产项目初始化附属设施
     */
    void initBdcFwfsssByProject(Project project);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn String
     * @description 通过Qjid查找房屋附属设施
     */
    BdcFwfsss getFwfsssByQjid(String qjid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn String
     * @description 通过fwfssid查找房屋附属设施
     */
    BdcFwfsss getFwfsssByfwfssid(String fwfssid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param fwfsssid
     * @rerutn
     * @description 删除房屋附属设施
     */
    void delBdcFwfsssByFwfsssid(final String fwfsssid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param zfbdcdyh 主房的不动产单元号
     * @rerutn
     * @description 初始化子户室作为附属设施
     */
    List<BdcFwfsss> intiBdcFwfsssForZhs(final String zfbdcdyh,final BdcXm bdcXm);

    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param zfbdcdyh 主房的不动产单元号  BdcXm bdcXm  不动产项目：要求关联的权利为BdcFdcq
    * @return
    * @Description: ${todo}
    */
    void initializeBdcFwfsss(final String zfbdcdyh, final BdcXm bdcXm);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param map
     * @rerutn
     * @description 获取附属设施
     */
    List<BdcFwfsss> getBdcFwfsssList(Map map);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param bdcFwFsssList
     * @rerutn
     * @description 获取附属设施的不动产项目
     */
    List<BdcXm> getFsssBdcXmList( List<BdcFwfsss> bdcFwFsssList);

    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param proid
     *@return
     *@description 通过proid判断过渡项目一证多房是否存在附属设施
     */
    String  checkExistFsssForGd(String proid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param proid
     * @rerutn
     * @description 根据主房proid判断是否存在附属设施
     */
    String  checkExistFsss(String proid);

    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param wiid
     *@return Boolean
     *@description 判断附属设施房屋土地面积是否同步累加到发证面积中
     */
    Boolean checkAddAllMjIntoFzmj(String wiid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param proid
     * @rerutn
     * @description 根据主房proid获取附属设施
     */
    List<BdcFwfsss> getBdcFwfsssListByProid(String proid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param proid
     * @rerutn
     * @description 根据proid删除附属设施
     */
    void delBdcFwfsss(final String proid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param zfBdcXm,fsssBdcXm
     * @rerutn
     * @description 附属设施部分信息和主房同步
     */
    void syncFsssVoByZfVo(BdcXm fsssBdcXm,BdcXm zfBdcXm);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @rerutn
     * @description 批量删除附属设施
     */
    void batchDelBdcFwfsss(List<BdcXm> bdcXmList);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @rerutn
     * @description 判断是否要加上附属设施面积
     */
    Boolean needAddBdcFsssMj(BdcXmRel bdcXmRel, BdcFwfsss bdcFwfsss,String zfBdcdyh);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @rerutn
     * @description 一证多房情况下 自动带入附属设施
     */
    void autoCreatBdcFsss(String proid);



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcFwfsss
     * @param bdcSpxx
     * @param bdcFdcqList
     * @rerutn
     * @description 手动、自动关联附属设施增加附属设施面积
     */
    void addBdcFwfsssMj(String wiid,BdcFwfsss bdcFwfsss,BdcSpxx bdcSpxx,List<BdcFdcq> bdcFdcqList);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcFwfsss
     * @param bdcFdcq
     * @rerutn
     * @description 手动、自动关联附属设施增加附属设施面积
     */
    void addBdcFwfsssMj(String wiid,BdcFwfsss bdcFwfsss,BdcFdcq bdcFdcq);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcFwfsss
     * @param bdcSpxx
     * @rerutn
     * @description 手动、自动关联附属设施增加附属设施面积
     */
    void addBdcFwfsssMj(BdcFwfsss bdcFwfsss,BdcSpxx bdcSpxx);

    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param bdcFwfsss,bdcSpxx
     *@return
     *@description 增加附属设施土地面积到发证面积中
     */
    void addBdcFwfsssTdmj(BdcFwfsss bdcFwfsss,BdcSpxx bdcSpxx);

    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param bdcFwfsss,bdcSpxx
     *@return
     *@description 关联附属设施累加了土地面积的，取消关联时减去附属设施的土地面积
     */
    void subtractBdcFwfsssTdmj(BdcFwfsss bdcFwfsss,BdcSpxx bdcSpxx);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcFwfsss
     * @param bdcSpxx
     * @param bdcFdcq
     * @rerutn
     * @description 手动、自动关联附属设施减去附属设施面积
     */
    void subtractBdcFwfsssMj(BdcFwfsss bdcFwfsss,BdcSpxx bdcSpxx,List<BdcFdcq> bdcFdcqList);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 手动、自动取消关联附属设施减少附属设施面积
     */
    void delBdcFwfsssMj(BdcFwfsss bdcFwfsss, BdcFdcq bdcFdcq);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param qllx
     * @return
     * @description 继承产权房屋附属设施
     */
    List<BdcFwfsss> inheritCqFwfsss(String proid, String qllx);


    /**
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @param proid
     * @return
     * @Description 根据proid获取房屋附属设施
     */
    List<BdcFwfsss> getBdcFwfsssByProid(String proid);


    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param
    * @return
    * @Description: 利用proid找到相关的bdcdyh，然后依靠proid 和 zfbdcdyh查询附属设施表
    */
    List<BdcFwfsss> getBdcFwfsssListByProidOnly(String proid);

    /**
     * @author <a href="mailto:zhuwei@gtmap.cn">zhuwei</a>
     * @param
     * @return
     * @Description: 将spxx的mj带入bdcDyaq.fwdymj面积（将附属设施总面积带入bdcDyaq.fwdymj）
     */

    void saveMjFromBdcSpxxToBdcDyaq(String proid);

    /**
     *@author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *@param bdcFwfsss,wiid,proid
     *@return
     *@description 关联附属设施累加了房屋抵押面积的，取消关联时减去附属设施的房屋抵押面积
     */
    void subtractBdcFwfsssFwDymj(BdcFwfsss bdcFwfsss,String wiid,String proid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 合并流程关联附属设施不带入抵押时，同步抵押spxx中的mj
     */
    void synchronizeDyaSpxxMj(String wiid);
}
