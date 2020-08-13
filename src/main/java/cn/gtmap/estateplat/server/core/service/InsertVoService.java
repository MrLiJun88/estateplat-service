package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.InsertVo;

import java.util.List;

/**
 * Created by zdd on 2015/12/13.
 * 处理新建项目插入对象服务
 */
public interface InsertVoService {


    /**
     * zdd 将业务审批信息覆盖到不动产登记的业务信息中
     *
     * @param insertVoList
     * @param bdcspxxList
     * @return
     */
    List<InsertVo> reReadBdcspxx(List<InsertVo> insertVoList, List<InsertVo> bdcspxxList);

    /**
     * zdd 从list中将bdcxmrel抽取出来   只考虑BdcXmRel存在一个的情况  批量的需要在外面循环处理
     *
     * @param insertVoList
     * @return
     */
    BdcXmRel getBdcXmRel(List<InsertVo> insertVoList);
}
