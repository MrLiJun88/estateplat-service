package cn.gtmap.estateplat.server.model;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/27
 * @description 昆山抵押归档清单
 */
public class BdcDyGdQd {
    //序号
    private String xh;
    //档案号
    private String dah;
    //盒号
    private String hh;
    //他项权证号
    private String txqzh;
    //抵押权人
    private String dyqr;
    //坐落
    private String zl;
    //产权证号
    private String cqzh;
    //产权人
    private String cqr;
    //流程主键
    private String wiid;

    public String getWiid() {
        return wiid;
    }

    public void setWiid(String wiid) {
        this.wiid = wiid;
    }

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

    public String getTxqzh() {
        return txqzh;
    }

    public void setTxqzh(String txqzh) {
        this.txqzh = txqzh;
    }

    public String getDyqr() {
        return dyqr;
    }

    public void setDyqr(String dyqr) {
        this.dyqr = dyqr;
    }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
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
}
