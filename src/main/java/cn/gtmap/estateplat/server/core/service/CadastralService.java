package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.server.model.cadastral.SyncIsfsssParam;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/12/1
 * @description 权籍库接口
 */
public interface CadastralService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param wiid
     * @return
     * @description 同步权藉权利人,面积,用途,坐落信息
     */
    void syncQlrForCadastral(String wiid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganhzi</a>
     * @param syncIsfsssParam
     * @return
     * @description 同步权籍库中isfsss字段
     */
    void syncIsfsssForCadastral(SyncIsfsssParam syncIsfsssParam);
}
