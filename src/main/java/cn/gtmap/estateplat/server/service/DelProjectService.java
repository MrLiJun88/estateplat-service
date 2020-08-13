package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.List;

/**
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-17 不动产登记工作流项目删除时会调用该接口
 */
public interface DelProjectService {
    /**
     * zdd 调用平台服务删除工作流
     *
     * @param proid
     */
    void delWorkFlow(final String proid);

    /**
     * zdd 删除文件中心节点
     *
     * @param proid
     */
    void delProjectNode(final String proid);

    /**
     * zdd 只有发生权力转移的登记类型 才去实现接口：改变原权利类型状态
     * 业务不同  调用不同的实现
     *
     * @param bdcXm
     */
    void changeYqllxzt(final BdcXm bdcXm);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @param mainProid
     * @rerutn
     * @description 批量改变原权利状态
     */
    void batchChangeYqllxzt(List<BdcXm> bdcXmList, String mainProid);

    /**
     * 删除表单信息
     *
     * @param bdcXm
     */
    void delBdcBdxx(final BdcXm bdcXm);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @rerutn
     * @description 批量删除表单信息
     */
    void batchDelBdcBdxx(List<BdcXm> bdcXmList);

    /**
     * zdd 不动产项目信息是否直接删除  有待考虑
     *
     * @param proid
     */
    void delBdcXm(final String proid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @rerutn
     * @description 批量删除项目
     */
    void batchDelBdcXm(List<BdcXm> bdcXmList);


    void delZsbh(final String proid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @rerutn
     * @description 批量证书编号
     */
    void batchDelBdcZsbh(List<BdcXm> bdcXmList);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @param bdcXm
     * @return
     * @description 删除证书锁定数据
     */
    void delBdcBdcZsSd(BdcXm bdcXm);
}
