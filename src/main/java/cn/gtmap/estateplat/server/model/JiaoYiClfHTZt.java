package cn.gtmap.estateplat.server.model;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/5/16
 * @description  存量房交易状态
 */
public class JiaoYiClfHTZt {
    //区域
    private String qy;
    //交易编号
    private String jybh;
    //状态
    private String zt;
    //合同编号
    private String htbh;
    //交易确认编号
    private String jyqrbh;
    //中介机构
    private String zjjg;

    public void setQy(String qy) {
        this.qy = qy;
    }
    public String getQy() {
        return qy;
    }

    public void setJybh(String jybh) {
        this.jybh = jybh;
    }
    public String getJybh() {
        return jybh;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
    public String getZt() {
        return zt;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getJyqrbh() {
        return jyqrbh;
    }

    public void setJyqrbh(String jyqrbh) {
        this.jyqrbh = jyqrbh;
    }

    public String getZjjg() {
        return zjjg;
    }

    public void setZjjg(String zjjg) {
        this.zjjg = zjjg;
    }
}
