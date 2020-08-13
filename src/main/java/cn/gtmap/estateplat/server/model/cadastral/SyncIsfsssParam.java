package cn.gtmap.estateplat.server.model.cadastral;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/9/26
 * @description 同步附属设施参数
 */
public class SyncIsfsssParam {

    private List<SyncIsfsssData> syncIsfsssDataList;

    public void setData(List<SyncIsfsssData> syncIsfsssDataList) {
        this.syncIsfsssDataList = syncIsfsssDataList;
    }

    public List<SyncIsfsssData> getData() {
        return syncIsfsssDataList;
    }

}