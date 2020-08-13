package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2016/9/19
 * @description 不动产证书锁定接口
 */
public interface BdcBdcZsSdService {
    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 获取证书锁定数据
     */
    public List<BdcBdcZsSd> getBdcZsSdList(HashMap map);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @param bdcXm
     * @return
     * @description 删除证书锁定数据
     */
    void delBdcBdcZsSd(BdcXm bdcXm);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @param proid
     * @return
     * @description 修改限制状态
     */
    void changeXzzt(String proid, String xzzt,String userid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 修改原证书限制状态（上一手为产权数据并非限制数据）
     */
    void changeYzsXzzt(String proid, String xzzt, String userid);
}
