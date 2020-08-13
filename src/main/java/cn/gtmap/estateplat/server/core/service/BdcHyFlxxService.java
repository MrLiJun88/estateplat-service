package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcHyFlxx;

import java.util.List;


/**
 * 不动产登记海域分类信息
 *
 * @author zhx
 * @version V1.0, 2016-1-7
 */
public interface BdcHyFlxxService {
    List<BdcHyFlxx> getBdcHyFlxxFromZHJNBDYJLB(final String hyid,final String zhdm);
}
