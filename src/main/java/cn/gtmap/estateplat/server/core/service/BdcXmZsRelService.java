package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmzsRel;
import cn.gtmap.estateplat.model.server.core.BdcZs;

import java.util.List;

/**
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-24
 * @description 项目证书关系表
 */
public interface BdcXmZsRelService {
    /**
     * zdd 根据项目Id 查找项目证书关系表
     *
     * @param proid 不动产登记项目ID
     * @return
     */
    List<BdcXmzsRel> queryBdcXmZsRelByProid(final String proid);

    /**
     * zx 根据证书Id 查找项目证书关系表
     *
     * @param zsid 证书ID
     * @return
     */
    String getProidByZsid(final String zsid);

    /**
     * zdd 根据证书列表  生成项目证书关系表
     *
     * @param list 不动产登记证书对象类别
     * @param proid 不动产登记项目ID
     * @return
     */
    List<BdcXmzsRel> creatBdcXmZsRel(List<BdcZs> list, final String proid);

    /**
     * zdd 根据项目ID  删除项目证书关系表
     *
     * @param proid 不动产登记项目ID
     */
    void delBdcXmZsRelByProid(final String proid);

    /**
     * zdd 根据主键删除
     *
     * @param xmzsgxid 不动产登记项目证书关系主键
     */
    void delBdcXmZsRelByXmzsgxid(final String xmzsgxid);

    /**
     * zdd 根据 证书ID  项目ID 创建
     *
     * @param zsid 证书ID
     * @param proid 不动产登记项目ID
     * @return
     */
    BdcXmzsRel creatBdcXmZsRel(final String zsid,final  String proid);

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description  生成项目证书关系(任意流程)
     */
    List<BdcXmzsRel> creatBdcXmZsRelArbitrary(final String zsid, List<BdcXm> bdcXmList);

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description  根据证书ID  删除项目证书关系表
     */
    void delBdcXmZsRelByZsid(final String zsid);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description  根据bdcXmList批量删除项目证书关系表
     */
    void batchDelBdcXmZsRelByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description  根据bdcXmList批量查询项目证书关系表
     */
    List<String> getZsidListByBdcXmList(List<BdcXm> bdcXmList);


}
