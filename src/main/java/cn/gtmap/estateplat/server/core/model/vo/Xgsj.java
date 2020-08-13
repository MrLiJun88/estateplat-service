package cn.gtmap.estateplat.server.core.model.vo;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-30
 * @description
 */
public class Xgsj {
    private String sjms;//数据描述
    private String xgzd;//修改字段
    private String yz;//原值
    private String xz;//现值


    public Xgsj() {
        super();
    }

    public String getSjms() {
        return sjms;
    }

    public void setSjms(String sjms) {
        this.sjms = sjms;
    }

    public String getXgzd() {
        return xgzd;
    }

    public void setXgzd(String xgzd) {
        this.xgzd = xgzd;
    }

    public String getYz() {
        return yz;
    }

    public void setYz(String yz) {
        this.yz = yz;
    }

    public String getXz() {
        return xz;
    }

    public void setXz(String xz) {
        this.xz = xz;
    }


}
