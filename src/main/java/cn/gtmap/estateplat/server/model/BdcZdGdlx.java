package cn.gtmap.estateplat.server.model;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/25
 * @description 归档类型字典表
 */
public class BdcZdGdlx {
    //代码
    private String dm;
    //名称
    private String mc;
    //高新区档案号组成为分类字母+流水号
    private String bzf;

    public String getBzf() {
        return bzf;
    }

    public void setBzf(String bzf) {
        this.bzf = bzf;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }
}
