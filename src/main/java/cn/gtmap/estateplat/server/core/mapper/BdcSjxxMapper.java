package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 收件信息
 */
@Repository
public interface BdcSjxxMapper {

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
     * @description 根据sjxxidList批量删除收件材料
     */
    void batchDelSjclListBySjxxidList(List<String> sjxxidlist);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param sjxxidlist
     * @return
     * @description 根据sjxxidList批量删除收件信息
     */
    void batchDelSjxxListBySjxxidList(List<String> sjxxidlist);
}
