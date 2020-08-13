package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.Dwxx;

import java.util.List;
import java.util.Map;

/**
 * @description 单位信息
 * Created by lst on 2015/3/17
 */
public interface DwxxMapper {
    /**
     * 获取单位信息
     *
     * @param map
     * @return
     */
    List<Dwxx> getDwxxList(Map map);

    /**
     * 获取乡镇单位信息
     *
     * @param map
     * @return
     */
    List<Dwxx> getXzDwxxList(Map map);

    /**
     * 根据单位代码获取单位信息
     *
     * @param dwdm
     * @return
     */
    Dwxx getDwxxByDwdm(String dwdm);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 吴江根据bdcdyh前9位取dwmc
     */
    String getDwmcByDwdm(String dwdm);
}
