package cn.gtmap.estateplat.server.sj.qr;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.vo.Xgsj;
import cn.gtmap.estateplat.server.sj.InterfaceCode;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020-04-1
 * @description 确认单修改数据模型
 */
public interface BdcQrdXgsjService extends InterfaceCode {
    List<Xgsj> getXgsjList(Map map, BdcXm bdcXm);
}
