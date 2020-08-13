package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.BdcGdxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.List;

/**
 * .
 * <p/>
 * 将登记业务数据归档到档案系统
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-25
 */
public interface ArchivePostService {

    /**
     * zdd post不动产项目信息到档案系统
     *
     * @param bdcXm
     */
    BdcGdxx postBdcXmInfo(BdcXm bdcXm);


    /**
     * 将不动产登记项目信息归档到档案系统，主要用于没用用户信息的自动归档
     * @param bdcXm 不动产登记项目信息
     * @param userName 归档人
     * @return
     */
    BdcGdxx postBdcXmInfo(BdcXm bdcXm,String userName);

    /**
     * @author bianwen
     * @description  批量归档
     */
    public List<BdcGdxx> postPlBdcXmInfo(List<BdcXm> bdcXmList, String userName);

    /**
     * @author chenjia
     * @description 归档
     */
    BdcGdxx postBdcXmInfoNew(BdcXm bdcXm);

    /**
     * @author chenjia
     * @description 根据proid归档
     */
    String postBdcXmInfoByProid(String proids,String cxgd);
}
