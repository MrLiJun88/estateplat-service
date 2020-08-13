package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSqr;

import java.util.List;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2017/2/13
 * @description 不动产申请人信息接口
 */
public interface BdcSqrService {
    /**
     * 权利人初始化申请人列表
     * @param wiid
     * @return
     */
    public BdcSqr initBdcSqrListFromBdcQlr(BdcQlr bdcQlr,String wiid);

    /**
     * 根据工作流id获取申请人列表
     * @param wiid
     * @return
     */
    public List<BdcSqr> getBdcSqrListByWiid(String wiid);
    /**
     * 根据申请人id获取申请人列表
     * @param sqrid
     * @return
     */
    public BdcSqr getBdcSqrBySqrid(String sqrid);

    /**
     * 根据申请人id删除不动产申请人
     * @param sqrid
     */
    public void delBdcSqrBySqrid(String sqrid);

    /**
     * 保存申请人
     * @param bdcSqr
     */
    public void saveBdcSqr(BdcSqr bdcSqr);

    /**
     * 关联申请人
     * @param sqrid
     * @param proid
     * @param wiid
     * @param qlrlx
     */
    public void glBdcSqr(String sqrid,String proid,String wiid,String qlrlx);

}
