package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.model.FoldersModel;

import java.util.List;

/**
 *
 * 不动产收件单和收件材料
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-19
 */
public interface BdcSjdService {
    /**
     * zx 获取收件单和收件材料
     *
     * @param wiid
     * @param proid
     * @return
     */
    List<BdcSjcl> getSjclListByWiid(final String wiid,final String proid);

    /**
     * zdd 根据流程ID 获取收件单信息
     *
     * @param wiid
     */
    List<BdcSjxx> queryBdcSjdByWiid(final String wiid);

    /**
     * zdd 根据收件信息ID 获取收件材料
     *
     * @param sjxxid
     */
    List<BdcSjcl> getSjclListBySjxxid(final String sjxxid);

    /**
     * zdd 根据收件信息删除收件材料
     *
     * @param sjxxid
     */
    void delSjclListBySjxxid(final String sjxxid);

    /**
     * zdd 删除收件单
     *
     * @param sjxxid
     */
    void delBdcSjxxBySjxxid(final String sjxxid);

    /**
     * zx 保存收件单
     *
     * @param bdcSjcl
     */
    void saveSjcl(BdcSjcl bdcSjcl);

    /**
     * zx 从老项目继承收件材料（根据收件单名称）
     *
     * @param proid
     * @return
     */
    void updateSjclFromYpro(final String proid,final String yproid);

    /**
     * 根据申请类型代码和原申请类型代码获取收件材料关系
     *
     * @param sqlxdm
     * @param ysqlxdm
     * @return
     */
    List<BdcSjclRel> getSjclRelList(final String sqlxdm,final String ysqlxdm);

    /**
     * 增加收件信息
     *
     * @param bdcSjxx
     */
    void addBdcSjxx(BdcSjxx bdcSjxx);

    /**
     * 增加收件材料
     *
     * @param bdcSjcl
     */
    void addBdcSjcl(BdcSjcl bdcSjcl);

    /**
     * 获取收件材料配置
     *
     * @param sqlxdm
     */
    List<BdcSjclConfig> getSjclConfigList(final String sqlxdm);

    /**
     * 根据不动产相应创建收件信息
     *
     * @param bdcXm
     * @return
     */
    BdcSjxx createSjxxByBdcxm(BdcXm bdcXm);

    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param
     * @rerutn
     * @description 根据项目id生成收件信息
     */
    BdcSjxx createSjxxByBdcxmByProid(BdcXm bdcXm);
    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param
     * @rerutn
     * @description 根据项目ID获取收件信息
     */
    List<BdcSjxx> queryBdcSjdByProid(final String proid);

    /**
     * zdd 根据项目id删除收件单
     *
     * @param wiid
     */
    void delBdcSjxxByWiid(final String wiid);

    /**
     * zx上传收件材料
     * @param foldersModel
     * @return
     */
    boolean updateSjcls(FoldersModel foldersModel);

    /**
     * 只上传收件材料附件
     * @param foldersModel
     * @return
     */
    boolean updateSjclsFiles(FoldersModel foldersModel);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 根据bdcXmList获取sjxxidlist
     */
    List<String> getSjxxidlistByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param sjxxidlist
     * @return
     * @description 根据sjxxidlist批量删除收件材料
     */
    void batchDelSjclListBySjxxidList(List<String> sjxxidlist);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param sjxxidlist
     * @return
     * @description 根据sjxxidlist批量删除收件信息
     */
    void batchDelSjxxListBySjxxidList(List<String> sjxxidlist);
}
