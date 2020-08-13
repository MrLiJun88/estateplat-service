package cn.gtmap.estateplat.server.model;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/4/2
 * @description 登记子项和交易状态验证entity
 */
public class TradingStatusAndDjzx {
    //项目登记子项
    private String djzx;
    //项目登记子项代码
    private String djzxdm;
    //alert的交易状态（逗号隔开）
    private String alertStatus;
    //confirm的交易状态(逗号隔开)
    private String confirmStatus;

    public String getDjzx() {
        return djzx;
    }

    public void setDjzx(String djzx) {
        this.djzx = djzx;
    }

    public String getDjzxdm() {
        return djzxdm;
    }

    public void setDjzxdm(String djzxdm) {
        this.djzxdm = djzxdm;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }
}
