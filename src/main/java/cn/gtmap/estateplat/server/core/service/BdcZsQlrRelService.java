package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.BdcZsQlrRel;

import java.util.List;

/**
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-23
 * @description 不动产登记证书权利人关系服务
 */
public interface BdcZsQlrRelService {
    /**
     * zdd 根据权利人ID  查找关系表
     *
     * @param qlrid
     * @return
     */
    List<BdcZsQlrRel> queryBdcZsQlrRelByQlrid(final String qlrid);

    /**
     * zdd 删除权利人证书关系表根据权利人ID
     *
     * @param qlrid
     */
    void delBdcZsQlrRelByQlrid(final String qlrid);

    /**
     * zx 删除证书和权利人证书关系表根据权利人ID
     *
     * @param qlrid
     */
    void delBdcZsAndZsQlrRelByQlrid(final String qlrid);

    /**
     * zx 删除证书和权利人证书关系表根据证书ID
     * @param zsid
     */
    void delZsQlrRelByZsid(final String zsid);

    /**
     * zdd 根据项目信息、证书信息、权利人信息生成权利人证书关系表
     * @param bdcXm 是否分别持证信息判读
     * @param bdcZsList
     * @param bdcQlrList
     * @return
     */
    public List<BdcZsQlrRel> creatBdcZsQlrRel(BdcXm bdcXm,List<BdcZs> bdcZsList,List<BdcQlr> bdcQlrList);

    /**
     * 通过proid查询当前项目证书权利人关系
     * @param proid
     * @return
     */
    public List<BdcZsQlrRel> queryBdcZsQlrRelByProid(final String proid);


    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description  根据证书信息、权利人信息、共有人信息生成权利人证书关系表(任意流程)
     */
    public List<BdcZsQlrRel> creatBdcZsQlrRelArbitrary(BdcZs bdcZs,BdcQlr bdcQlr,List<BdcQlr> gyrList);

    /**
     * 交叉共有，多个权利人中只有一个持证人，为其他权利人生成权利人证书关系表
     * @param bdcZs
     * @param czrList
     * @param qlrList
     * @return
     */
    public List<BdcZsQlrRel> creatBdcZsQlrRelForOtherQlrExceptCzr(BdcZs bdcZs,List<BdcQlr> czrList,List<BdcQlr> qlrList,BdcXm bdcXm);

    /**
     * @param bdcZsList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description  根据bdcZsList批量删除证书权利人关系表
     */
    void batchDelBdcZsQlrRelByBdcZsList(List<BdcZs> bdcZsList);

    /**
     * @param bdcQlrList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description  根据bdcQlrList批量删除证书权利人关系表
     */
    void batchDelBdcZsQlrRelByBdcQlrList(List<BdcQlr> bdcQlrList);
}
