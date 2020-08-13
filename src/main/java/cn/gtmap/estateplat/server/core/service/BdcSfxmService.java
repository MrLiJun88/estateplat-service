package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSfxm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记收费项目
 */
public interface BdcSfxmService {
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取收费项目
    *@Date 17:15 2017/4/10
    */
    List<BdcSfxm> getSfXm(String qlrlx, String Sfxxid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param sfxmid
     * @return
     * @description 根据sfxmid获取收费项目
     */
    BdcSfxm getBdcSfxmBySfxmid(String sfxmid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcSfxm
     * @return
     * @description 保存收费项目
     */
    void saveBdcSfxmBySfxmid(BdcSfxm bdcSfxm);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据sfxxid查询bdcsfxmlist
     */
    List<BdcSfxm> getBdcSfxmListBySfxxid(String sfxxid);

    /**
     *@auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     *@description 或收费项目
     */
    List<BdcSfxm> queryBdcSfXm(HashMap map);
}
