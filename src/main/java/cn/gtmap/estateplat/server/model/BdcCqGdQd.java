package cn.gtmap.estateplat.server.model;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/27
 * @description 昆山产权归档清单
 */
public class BdcCqGdQd {
    //序号
    private String xh;
    //档案号
    private String dah;
    //盒号
    private String hh;
    //产权证号
    private String cqzh;
    //产权人
    private String cqr;
    //坐落
    private String zl;
    //流程id
    private String wiid;

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getDah() {
        return dah;
    }

    public void setDah(String dah) {
        this.dah = dah;
    }

    public String getHh() {
        return hh;
    }

    public void setHh(String hh) {
        this.hh = hh;
    }

    public String getCqzh() {
        return cqzh;
    }

    public void setCqzh(String cqzh) {
        this.cqzh = cqzh;
    }

    public String getCqr() {
        return cqr;
    }

    public void setCqr(String cqr) {
        this.cqr = cqr;
    }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getWiid() {
        return wiid;
    }

    public void setWiid(String wiid) {
        this.wiid = wiid;
    }
}
