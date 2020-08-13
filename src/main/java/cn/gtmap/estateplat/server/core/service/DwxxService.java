package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.Dwxx;

import java.util.List;

/**
 * .
 * 行政区信息
 *
 * @author <a href="mailto:zx@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-12
 */
public interface DwxxService {
    /**
     * 根据行政区代码，和行政级别获取单位信息
     *
     * @param xzqdm
     * @param level
     * @return
     */
    List<Dwxx> getDwxxList(final String xzqdm, final int level);

    /**
     * 根据行政区代码获取单位信息
     *
     * @param xzqdm
     * @return
     */
    Dwxx getDwxxByDwdm(final String xzqdm);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 吴江根据bdcdyh前9位取dwmc
     */
    String getDwmcByDwdm(final String dwdm);
}
