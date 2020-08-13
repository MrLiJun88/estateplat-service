package cn.gtmap.estateplat.server.core.service;

import org.springframework.ui.Model;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/9/26
 * @description 数据匹配界面Model服务
 */
public interface BdcDataPicModel {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param model
     * @param model
     * @return
     * @description 初始化不动产数据匹配模型数据
     */
    void initBdcDataPicModel(Model model);
}
