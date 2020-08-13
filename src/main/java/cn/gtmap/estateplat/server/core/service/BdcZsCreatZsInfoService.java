package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcDyZs;
import cn.gtmap.estateplat.model.server.core.BdcXm;

/**
 * Created by IntelliJ IDEA.
 * User: sc 生成证书时自动读取配置生成一些信息
 * Date: 15-4-19
 * Time: 上午9:51
 * To change this template use File | Settings | File Templates.
 */
public interface BdcZsCreatZsInfoService {

    /**
     * 申请分别持证,生成的多本证书的”权利其他状况”栏需要记载其余共有人
     */
    BdcDyZs setQygyr(BdcXm bdcXm, String czr, BdcDyZs bdcDyZs);

    /**
     * 不动产类型是户室的读取持证人
     */
    BdcDyZs setCzr(BdcXm bdcXm, String czr, BdcDyZs bdcDyZs);
}
