package cn.gtmap.estateplat.server.core.model;


import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 房屋登记查询
 *
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2016/5/8
 */
@Table(name = "bdc_fwdjcx")
public class BdcFwdjcx {
    @Id
    //房屋登记查询id
    private String fwdjcxid;
    //查询人
    private String cxr;
    //项目id
    private String proid;
    //查询类型（0;个人；1:家庭）
    private String cxlx;
    //查询时间
    private Date cxsj;

    public String getFwdjcxid() {
        return fwdjcxid;
    }

    public void setFwdjcxid(String fwdjcxid) {
        this.fwdjcxid = fwdjcxid;
    }

    public String getCxr() {
        return cxr;
    }

    public void setCxr(String cxr) {
        this.cxr = cxr;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getCxlx() {
        return cxlx;
    }

    public void setCxlx(String cxlx) {
        this.cxlx = cxlx;
    }

    public Date getCxsj() {
        return cxsj;
    }

    public void setCxsj(Date cxsj) {
        this.cxsj = cxsj;
    }
}
