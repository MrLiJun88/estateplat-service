package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lst on 2015/3/17.
 *
 * @description 不动产登记证书服务
 */
public interface BdcZsService {

    /**
     * zdd 生成不动产权证书
     *
     * @param bdcXm
     * @param czr
     * @param bdcqzh 不动产权证号 如果为空则从新生成
     * @return
     */
    BdcDyZs creatBdcZs(BdcXm bdcXm, String czr, String bdcqzh, Boolean previewZs);

    /**
     * @param bdcXm
     * @param czr
     * @param bdcqzh
     * @param userId
     * @return
     */
    BdcDyZs creatBdcZs(BdcXm bdcXm, String czr, String bdcqzh, String userId, Boolean previewZs);

    /**
     * zdd 获取最大流水号
     *
     * @param map
     * @return
     */
    Integer getMaxLsh(final Map map);

    /**
     * zdd 根据权利人id 查找权利证书
     *
     * @param zsid
     * @return
     */
    BdcZs queryBdcZsByZsid(final String zsid);

    /**
     * zdd 根据项目ID删除不动产证书
     *
     * @param zsid
     */
    void delBdcZsByZsid(final String zsid);

    /**
     * zdd 创建不动产权证（证明 证书）
     *
     * @param bdcXm      项目信息
     * @param bdcQlrList 项目权利人信息
     * @return 已经生成的证书
     */
    List<BdcZs> creatBdcqz(BdcXm bdcXm, List<BdcQlr> bdcQlrList, String userId, Boolean previewZs);

    /**
     * @param bdcXm
     * @param bdcQlrList
     * @return
     */
    List<BdcZs> creatBdcqz(BdcXm bdcXm, List<BdcQlr> bdcQlrList, Boolean previewZs);

    /**
     * @param bdcXm 不动产项目
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 生成的证书
     */
    List<BdcZs> creatBdcqz(BdcXm bdcXm, BdcQlr bdcQlr, String userId);


    /**
     * sc 获取权利其他状况
     *
     * @param bdcZs
     * @return
     */
    BdcDyZs getZsQlqtzk(BdcXm bdcXm, BdcDyZs bdcZs);

    /**
     * zdd 根据项目ID查找证书（证明）
     *
     * @param proid
     * @return
     */
    List<BdcZs> queryBdcZsByProid(final String proid);

    /**
     * sc
     *
     * @param proid
     * @return
     */
    String getYbdcqzhByProid(final String proid);

    /**
     * 生成其他状况时获取过渡证书号
     */
    List<Map> getGdzsByProid(final String proid);

    /**
     * zx 更新缮证人
     *
     * @param proid
     * @param userid
     * @param defaultUserId
     * @param signKey
     * @return
     */
    void updateSzrByProid(final String proid, final String userid, final String defaultUserId, final String signKey);

    /**
     * zx 根据项目ID删除不动产证书
     *
     * @param proid
     */
    void delBdcZsByProid(final String proid);

    List<BdcZs> getPlZsByProid(final String proid);


    /**
     * lst 批量获取证书
     *
     * @param wiid
     * @return
     */
    List<BdcZs> getPlZsByWiid(final String wiid);

    /**
     * lst 批量获取证书
     *
     * @param
     * @param
     * @return
     */
    List<Map> getPlZs(Map map);

    /**
     * @param bdcZsbh
     * @return
     * @author <a href="mailto:tanyue@gtmap.cn">qijiadong</a>
     * @description 保存证书编号
     */
    void saveBdcZsBh(BdcZsbh bdcZsbh);

    /**
     * 根据proid和bdcid获取原证号
     *
     * @param proid
     * @param bdcdyid
     * @return
     */
    String getYzhByProidAndBdcid(final String proid, final String bdcdyid);

    /**
     * 获取权证号，包含过渡的和新建项目的
     *
     * @param bdcXm
     * @param bdcdyid
     * @return
     */
    String getYzh(BdcXm bdcXm, String bdcdyid);

    /**
     * 创建不动产权证（证明 证书）已经入库  用于抵押
     *
     * @param bdcXm
     * @param bdcQlrList
     * @return
     */
    List<BdcZs> creatDyBdcqz(BdcXm bdcXm, List<BdcQlr> bdcQlrList, Boolean previewZs);


    /*
     * zwq 通过bdcxm查找原不动产权证
     *
     * */

    List<BdcZs> getYbdcqz(BdcXm bdcXm);

    /**
     * 获取其他情况
     *
     * @param proid
     * @return
     */
    BdcDyZs getQtqkforView(final String proid);

    /**
     * 获取附记
     *
     * @param proid
     * @return
     */
    String getZsFjView(final String proid);

    /**
     * @param proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 合并流程删除原证号
     */
    void deleteHblcYbdcqzh(final String proid);

    /**
     * 通过qlid获取土地证号
     *
     * @param qlid
     * @return
     */
    String getTdzhByQlid(final String qlid);

    /**
     * @param qlid
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 获取房产不动产权证号
     */
    String getFczhByQlid(final String qlid);

    /**
     * zx 根据项目ID删除证书不动产证号和证书编号
     *
     * @param proid
     */
    void delBdcZsBdcqzhAndZsbhByProid(final String proid);

    /**
     * 更新打印状态
     *
     * @param
     */
    void updateDyzt(Map map);

    /**
     * liujie  创建不动产权证（证明 证书）（交叉共有方式）
     *
     * @param bdcXm
     * @param bdcQlr
     * @return
     */
    BdcZs creatBdcqzCrossSharingMode(BdcXm bdcXm, BdcQlr bdcQlr, String userId, Boolean previewZs);

    /**
     * jyl  创建不动产权证（证明 证书）（任意流程）
     *
     * @param bdcXm
     * @param bdcQlr
     * @return
     */
    BdcZs creatBdcqzArbitrary(BdcXm bdcXm, BdcQlr bdcQlr, String userId);

    /**
     * jyl  创建不动产权证（证明 证书）（任意流程）
     *
     * @param bdcXm
     * @param czr
     * @param bdcqzh
     * @return
     */
    BdcDyZs creatBdcZsArbitrary(BdcXm bdcXm, String czr, String bdcqzh, Boolean previewZs);

    /**
     * jyl  创建不动产权证（证明 证书）（任意流程）
     *
     * @param bdcXm
     * @param czr
     * @param bdcqzh
     * @param userId
     * @return
     */
    BdcDyZs creatBdcZsArbitrary(BdcXm bdcXm, String czr, String bdcqzh, String userId, Boolean previewZs);

    /**
     * jyl 根据wiid删除不动产证书
     *
     * @param wiid
     */
    void delBdcZsByWiid(final String wiid);


    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据宗地不动产单元号获取不动产权证号
     */
    String getYtdbdcqzhByZdbdcdyh(String bdcdyh);

    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据宗地不动产单元号获取土地证号
     */
    String getYtdzhByZdbdcdyh(String bdcdyh);

    /**
     * @param bdcqzh
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @description 根据不动产权证号获取项目id
     */
    String getProidByBdcqzh(String bdcqzh);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过proid获取项目组织证书号
     */
    String getCombineBdcqzhByProid(String proid);


    /**
     * @author
     * @description 通过zsid获取bdcdy数量
     */
    Integer getBdcdyCountByZsid(String zsid);

    BdcZs queryBdcZsByBdcqzh(String bdcqzh);

    String getProidByBdczqh(String bdcqzh);


    /**
     * @param wiid
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据proid删除证书相关表信息
     */
    void batchDelBdcZs(String wiid);


    /**
     * @param bdcZsList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcZsList删除证书表信息
     */
    void batchDelBdcZsByBdcZsList(List<BdcZs> bdcZsList);


    /**
     * @param zsidList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据zsidList删除证书表信息
     */
    void batchDelBdcZsByZsidList(List<String> zsidList);


    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcXmList删除项目证书关系表信息
     */
    void batchDelBdcXmZsRelByBdcXmList(List<BdcXm> bdcXmList);


    /**
     * @param bdcDelZszhList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcDelZszhList批量插入不动产删除证书证号表记录
     */
    void batchInsertBdcDelZszhByBdcDelZszhList(List<BdcDelZszh> bdcDelZszhList);


    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcXmList批量查询证书证号表记录
     */
    List<String> getZsidListByBdcXmList(List<BdcXm> bdcXmList);


    /**
     * @param bdcXmList
     * @param lzrq
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据zsidList更新证书领证日期
     */
    void batchUpateBdcZsLzrqByzsidList(List<BdcXm> bdcXmList, Date lzrq);


    /**
     * @param bdcXm
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据proid获取默认配置的权利其他状况
     */
    HashMap getBdcXtConfigQlqtzkAndFj(BdcXm bdcXm);

    /**
     * 生成附记和权利其他状况
     */
    void saveBdcXtConfigQlqtzkAndFj(BdcXm bdcXm);


    /**
     * @param qlqtzk
     * @param bdcqzh
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据qlqtzk和bdcqzh替换不动产权证号
     */
    String getQlqtzkByReplaceBdcqzh(String qlqtzk, String bdcqzh);

    /**
     * 补全证书信息
     *
     * @param bdcZs,bdcXm
     */
    void completeZsInfo(BdcXm bdcXm, BdcZs bdcZs, String userId, String zsType);

    void dealHbZsInfo(List<BdcZs> dyaqBdcZsList, List<BdcZs> cqBdcZsList, List<BdcXm> dyaqXmList, List<BdcXm> cqXmList);

    /**
     * @param
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description
     */
    List<BdcZs> getPlZsByMap(Map map);

    /**
     * @return
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params
     * @description 保存zs
     */
    void saveZs(BdcZs bdcZs);

    /**
     * @param zsid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据zsid获取同一编号下的bdczs
     */
    List<BdcZs> getAllBdcZsWithSameWiidByZsid(String zsid);

    /**
     * @param zsid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据证书id获取xm的wiid
     */
    String getXmWiidByZsid(String zsid);


    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">jiangganzhi</a>
     * @description 创建证书
     */
    List<BdcZs> createBdcZs(BdcXm bdcXm, String previewZs, String userid);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 保存附记
     */
    void saveBdcXtConfigFj(BdcXm bdcXm);

    /**
     * @param bdcXm
     * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 根据proid获取默认配置的附记
     */
    HashMap getBdcXtConfigFj(BdcXm bdcXm);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 根据zxzh获取当年首次登记证最大流水号
     */
    Integer getMaxScdjzLsh(Map map);


    /**
     * @param proid 项目ID
     * @return 产权证号
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据项目ID获取产权证号
     */
    Map queryBdcqzhByProid(String proid);

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 根据bdcdyh查询bdczs
     */
    List<BdcZs> queryBdcZsByBdcdyh(String bdcdyh);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 获取用于生成证书号的信息
     */
    Map getInfoForCreateBdcqzh(BdcXm bdcXm);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 批量更新证书
     */
    void updateBdcZs(List<BdcZs> bdcZsList);

    /**
     * @param bdcqzhjc
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 根据不动产权证号简称获取项目id
     */
    String getProidByBdcqzhjc(String bdcqzhjc,String zstype);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据wiid获取不动产证书通过坐落排序
     */
    List<BdcZs> getBdcZsListByWiidOrderByZl(final String wiid);
}
