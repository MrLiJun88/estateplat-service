package cn.gtmap.estateplat.server.model.dataPic;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/25 0025
 * @description
 */
public class BdcPicQo {
    private String proid;
    private String fwbm;
    private String bdcqzh;
    private String bdcdyh;
    private String bdclx;
    private String tdid;
    private String tdbdcqzh;
    private String ppbdcdyh;
    private String ppbdcdyhid;

    public BdcPicQo(String proid, String fwbm, String bdcqzh, String bdcdyh, String bdclx, String tdid, String tdbdcqzh) {
        this.proid = proid;
        this.fwbm = fwbm;
        this.bdcqzh = bdcqzh;
        this.bdcdyh = bdcdyh;
        this.bdclx = bdclx;
        this.tdid = tdid;
        this.tdbdcqzh = tdbdcqzh;
    }

    public BdcPicQo() {
        super();
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getFwbm() {
        return fwbm;
    }

    public void setFwbm(String fwbm) {
        this.fwbm = fwbm;
    }

    public String getBdcqzh() {
        return bdcqzh;
    }

    public void setBdcqzh(String bdcqzh) {
        this.bdcqzh = bdcqzh;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getBdclx() {
        return bdclx;
    }

    public void setBdclx(String bdclx) {
        this.bdclx = bdclx;
    }

    public String getTdid() {
        return tdid;
    }

    public void setTdid(String tdid) {
        this.tdid = tdid;
    }

    public String getTdbdcqzh() {
        return tdbdcqzh;
    }

    public void setTdbdcqzh(String tdbdcqzh) {
        this.tdbdcqzh = tdbdcqzh;
    }

    public String getPpbdcdyh() {
        return ppbdcdyh;
    }

    public void setPpbdcdyh(String ppbdcdyh) {
        this.ppbdcdyh = ppbdcdyh;
    }

    public String getPpbdcdyhid() {
        return ppbdcdyhid;
    }

    public void setPpbdcdyhid(String ppbdcdyhid) {
        this.ppbdcdyhid = ppbdcdyhid;
    }
}
