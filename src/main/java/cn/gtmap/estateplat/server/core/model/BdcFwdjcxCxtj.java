package cn.gtmap.estateplat.server.core.model;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 房屋登记查询条件
 *
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2016/5/8
 */
@Table(name = "bdc_fwdjcx_cxtj")
public class BdcFwdjcxCxtj {
    @Id
    //房屋登记查询---查询条件id
    private String cxtjid;
    //查询人
    private String cxr;
    //证件号
    private String zjh;
    //房屋登记查询id
    private String fwdjcxid;
    //房屋坐落
    private String fwzl;
    //产权证号
    private String cqzh;

    public String getCxtjid() {
        return cxtjid;
    }

    public void setCxtjid(String cxtjid) {
        this.cxtjid = cxtjid;
    }

    public String getCxr() {
        return cxr;
    }

    public void setCxr(String cxr) {
        this.cxr = cxr;
    }

    public String getZjh() {
        return zjh;
    }

    public void setZjh(String zjh) {
        this.zjh = zjh;
    }

    public String getFwdjcxid() {
        return fwdjcxid;
    }

    public void setFwdjcxid(String fwdjcxid) {
        this.fwdjcxid = fwdjcxid;
    }

    public String getFwzl() {
        return fwzl;
    }

    public void setFwzl(String fwzl) {
        this.fwzl = fwzl;
    }

    public String getCqzh() {
        return cqzh;
    }

    public void setCqzh(String cqzh) {
        this.cqzh = cqzh;
    }
}
