package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.BdcZsQlrRel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-23
 * @description 不动产登记证书权利人关系Mapper
 */
@Repository
public interface BdcZsQlrRelMapper {
    /**
     * zdd 根据权利人ID  查找关系表
     *
     * @param qlrid
     * @return
     */
    List<BdcZsQlrRel> queryBdcZsQlrRelByQlrid(String qlrid);

    /**
     * 通过proid查询当前项目证书权利人关系
     * @param proid
     * @return
     */
    public List<BdcZsQlrRel> queryBdcZsQlrRelByProid(String proid);


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
