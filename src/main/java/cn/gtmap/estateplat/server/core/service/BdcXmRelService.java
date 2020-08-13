package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 * 不动产登记项目关系服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-20
 */
public interface BdcXmRelService {
    /**
     * zdd 根据新建项目信息创建BdcXmrel  未插入数据库
     *
     * @param project
     * @return
     */
    BdcXmRel creatBdcXmRelFromProject(Project project);

    /**
     * zx 根据新建项目信息创建多个BdcXmrel  未插入数据库
     *
     * @param project
     * @return
     */
    List<BdcXmRel> creatMulBdcXmRelFromProject(Project project);

    /**
     * zdd 根据项目id查找关系表
     *
     * @param proid
     * @return
     */
    List<BdcXmRel> queryBdcXmRelByProid(final String proid);

    /**
     * zdd 根据项目ID删除项目关系表
     *
     * @param proid
     */
    void delBdcXmRelByProid(final String proid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量删除不动产项目关系表
     */
    void batchDelBdcXmRel(List<BdcXm> bdcXmList);

    /**
     * zdd 根据原项目ID 查找关系表
     *
     * @param yproid
     * @return
     */
    List<BdcXmRel> queryBdcXmRelByYproid(final String yproid);


    /**
     * zdd 根据原qlid 查找关系表
     *
     * @param yqlid
     * @return
     */
    List<BdcXmRel> queryBdcXmRelByYqlid(final String yqlid);

    /**
     * zdd 根据map参数条件查询BdcXmRel
     *
     * @param map
     * @return
     */
    List<BdcXmRel> andEqualQueryBdcXmRel(final Map<String, Object> map);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 不动产登记项目ID
     * @return 上一手不动产登记项目ID
     * @description 根据不动产登记项目ID获取上一手不动产登记项目ID
     */
    List<String> getYproid(final String proid);

    /**
     * zx根据项目id获取该项目之前的历史关系
     *
     * @param proid
     * @return
     */
    List<BdcXmRel> getHisBdcXmRelByProid(final String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过yqlid获取项目关系
     */
    List<BdcXmRel> getBdcXmRelListByYqlid(String yqlid);

    /**
     * zx根据项目id获取xml
     *
     * @param proid
     * @return
     */
    String getHisXmRelXml(final String proid, final String bdcdyh,final String portalUrl);

    /**
     * sc 根据原项目ID和不动产单元号获取项目关系表
     *
     * @param map
     * @return
     */
    List<BdcXmRel> getBdcXmRelByYproidAndBdcdyh(final Map map);

    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param
    * @return
    * @description 获得与proid同流程的所有xmrel
    */
    List<BdcXmRel> getSameWFXmRelByproid(final String proid);

    /**
     * 2015-12-12 zdd &需要优化&   根据不动产单元ID直接就能找到所有的项目
     * zx 获取所有项目关系
     *
     * @param proid
     * @return
     */
    String getAllXmRelXml(final String proid,final String bdcdyh,final String portalUrl);

    /**
     * zx 获取所有项目关系列表
     *
     * @param map
     * @return
     */
    List<BdcXmRel> getAllHisRelList(final Map map);

    /**
     * zx根据项目id获取该项目之后的历史关系
     *
     * @param proid
     * @return
     */
    List<BdcXmRel> getAfterHisXmRelList(final String proid);

    /**
     * zx 获取单元所有项目关系
     *
     * @param bdcdyh
     * @return
     */
    String getDyAllXmRelXml(final String bdcdyh,final String portalUrl);

    /**
     * sc 根据新建项目信息创建BdcXmrel  未插入数据库
     *
     * @param project
     * @return
     */
    BdcXmRel creatBdcXmRelFromProjectMul(Project project);

    /*zwq 根据bdc_xm_rel中的成员名查询*/
    List<BdcXmRel> getBdcXmRelByName(final String name, final String value);

    /**
     * zx 根据新建项目信息创建BdcXmrel  未插入数据库
     * 处理yproid,和yqlid逗号相隔问题
     * @param project
     * @return
     */
    List<BdcXmRel> creatBdcXmRelListFromProject(Project project);

    /**
     *根据不动产权证号获取bdc_xmzs_rel数据
     * @param bdcqzh
     * @return
     */
    List<BdcXmzsRel> getProidByBdcqzh(String bdcqzh);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">jyl</a>
     * @version
     * @param bdcXmRel
     * @return
     * @description 保存不动产项目关系表
     */
    void saveBdcXmRel(BdcXmRel bdcXmRel);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 在建工程首次 在建工程多抵多 宗地抵押关系带入
     */
    List<BdcXmRel> createZjgcYzdDyBdcXmRel(Project project);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 在建工程抵押注销 选择证明后一次性注销所有对应项目
     */
    List<BdcXmRel> creatZjgcDyZxBdcXmRel(Project project,BdcXmRel bdcXmRel);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 通过bdc的xmid查询原权利对应所有查封
     */
    List<String> getAllGdCfQlidListByBdcXmid(String proid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 通过bdc的xmid查询原权利对应现势产权
     */
    List<String> getXsSyqQlidListByBdcXmid(String proid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 附属设施单独发过房产证，创建关系带入项目
     */
    void creatBdcXmRelForFsssGdFwsyq(BdcXm bdcXm);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 根据proid获取不动产上下手全部项目关系
     */
    List<BdcXmRel> getAllHisXmRelByProid(String proid);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 根据proid获取不动产上下手全部项目关系
     */
    List<GdLsCfBh> getAllGdLsCfBhByCfid(String cfid);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 涉及抵押变更登记流程时，根据wiid补全现抵押和现产权的关系
     */
    void completeDybgBdcXmRelByWiid(String wiid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 涉及抵押变更登记流程时，根据产权项目和抵押权项目补全现抵押和现产权的关系
     */
    void completeDybgBdcXmRelByBdcXmList(List<BdcXm> cqBdcXmList,List<BdcXm> dyaqBdcXmList);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description  根据proid和yproid判断bdcXmRel是否存在
     */
    Boolean existBdcXmRelByProidAndYproid(String proid,String yproid);
}
