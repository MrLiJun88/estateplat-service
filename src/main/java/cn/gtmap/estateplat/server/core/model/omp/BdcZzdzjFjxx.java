package cn.gtmap.estateplat.server.core.model.omp;



/*
 * @author <a href="mailto:zhangyu@gtmap.cn">zhangyu</a>
 * @version 1.0, zhangyu
 * @description 自助打证机存储附件信息
 */

public class BdcZzdzjFjxx {
    private String zsid;//证书id
    private String dzzp;//打证照片
    private String smj;//扫描件
    private String bh;//登记系统业务流水号
    private String zsh;//证书编号
    private String lzrzjh;//领证人证件号
    private long fzrq;//发证日期

    public String getZsid() {
        return zsid;
    }

    public void setZsid(String zsid) {
        this.zsid = zsid;
    }

    public String getDzzp() {
        return dzzp;
    }

    public void setDzzp(String dzzp) {
        this.dzzp = dzzp;
    }

    public String getSmj() {
        return smj;
    }

    public void setSmj(String smj) {
        this.smj = smj;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getZsh() {
        return zsh;
    }

    public void setZsh(String zsh) {
        this.zsh = zsh;
    }

    public String getLzrzjh() {
        return lzrzjh;
    }

    public void setLzrzjh(String lzrzjh) {
        this.lzrzjh = lzrzjh;
    }

    public long getFzrq() {
        return fzrq;
    }

    public void setFzrq(long fzrq) {
        this.fzrq = fzrq;
    }
}
