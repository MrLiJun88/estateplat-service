package cn.gtmap.estateplat.server.core.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 房屋登记查询结果
 *
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2016/5/8
 */
@Table(name = "bdc_fwdjcx_cxjg")
public class BdcFwdjcxCxjg {
    @Id
    //房屋登记查询---查询结果id
    private String cxjgid;
    //房屋坐落
    private String zl;
    //建筑面积
    private String jzmj;
    //权利人
    private String qlr;
    //权利人证件号
    private String qlrZjh;
    //原产权人
    private String ycqr;
    //原产权人证件号
    private String ycqrZjh;
    //登记时间
    private Date djsj;
    //房屋登记信息查询ID
    private String fwdjcxid;
    //备注
    private String bz;
    //产权证号
    private String cqzh;

    public String getCxjgid() {
        return cxjgid;
    }

    public void setCxjgid(String cxjgid) {
        this.cxjgid = cxjgid;
    }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getJzmj() {
        return jzmj;
    }

    public void setJzmj(String jzmj) {
        this.jzmj = jzmj;
    }

    public String getQlr() {
        return qlr;
    }

    public void setQlr(String qlr) {
        this.qlr = qlr;
    }

    public String getQlrZjh() {
        return qlrZjh;
    }

    public void setQlrZjh(String qlrZjh) {
        this.qlrZjh = qlrZjh;
    }

    public String getYcqr() {
        return ycqr;
    }

    public void setYcqr(String ycqr) {
        this.ycqr = ycqr;
    }

    public String getYcqrZjh() {
        return ycqrZjh;
    }

    public void setYcqrZjh(String ycqrZjh) {
        this.ycqrZjh = ycqrZjh;
    }

    public Date getDjsj() {
        return djsj;
    }

    public void setDjsj(Date djsj) {
        this.djsj = djsj;
    }

    public String getFwdjcxid() {
        return fwdjcxid;
    }

    public void setFwdjcxid(String fwdjcxid) {
        this.fwdjcxid = fwdjcxid;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getCqzh() {
        return cqzh;
    }

    public void setCqzh(String cqzh) {
        this.cqzh = cqzh;
    }
}
