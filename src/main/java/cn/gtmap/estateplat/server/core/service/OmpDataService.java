package cn.gtmap.estateplat.server.core.service;


import cn.gtmap.estateplat.model.server.core.BdcGdRel;
import cn.gtmap.estateplat.server.core.model.omp.ContractInfo;

import java.util.List;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2017/6/28
 * @description
 */
public interface OmpDataService {

    String getContractInfoByHth(String hth);

    String impContractInfo(ContractInfo contractInfo, String bdcdyh, String proid);


    /**
     *@Author:<a href="mailto:liujie@gtmap.cn">liujie</a>
     *@Description: 根据djh获取土地是否抵押信息（0代表无抵押，1代表有抵押）
     *@Date 15:52 2017/11/22
     */
    String getTdSfDyInfoByDjh(String djh);
    /**
     * zx 根据bdcdyh获取不动产与供地关系
     *
     * @param bdcdyh
     * @param proid
     * @return
     */
    List<BdcGdRel> getBdcGdRelList(final String bdcdyh, final String proid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param contractInfo
     * @param proid
     * @return
     * @description 抽取政务一张图的合同附件
     */
    String extractAttachment(ContractInfo contractInfo, String proid);

}
