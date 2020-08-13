package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.List;

/**
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-17
 * @description 不动产登记工作流项目办结时会调用该接口
 */
public interface EndProjectService {
    /**
     * zdd 改变项目(权利类型)完成状态
     *
     * @param bdcXm
     */
    void changeXmzt(BdcXm bdcXm);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @rerutn
     * @description 批量改变项目状态
     */
    void batchChangeXmzt(List<BdcXm> bdcXmList);

    /**
     * zdd 只有发生权力转移的登记类型 才去实现：改变原权利类型状态
     *
     * @param bdcXm
     */
    void changeYqllxzt(final BdcXm bdcXm) throws Exception;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @rerutn
     * @description 批量改变权利状态
     */
    void batchChangeYqllxzt(List<BdcXm> bdcXmList) throws Exception;

    /**
     * hqz 缮证退回
     *
     * @param bdcXm
     */
    void backXmzt(BdcXm bdcXm);

    /**
     * hqz 缮证退回还原原权利状态
     * @param bdcXm
     */
    void backYqllxzt(BdcXm bdcXm);

    /**
     * zx 只有发生预告登记时 才去实现：改变权利类型状态
     *
     * @param bdcXm
     */
    void changeYgQszt(final BdcXm bdcXm);

    /**
     * zx 只有发生预告登记时 才去实现：改变权利类型状态
     *
     * @param bdcXm
     */
    void backYgQszt(BdcXm bdcXm);

    /*
    * zwq 异议登记时改变权属状态
    * @param bdcXm
    * */
    void changeYyQszt(final BdcXm bdcXm);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 预留接口，用于办结项目时做一些额外的事情(该方法放在循环外面，注意合并和批量流程的处理)
     */
    void doExtraWork(BdcXm bdcXm);
}
