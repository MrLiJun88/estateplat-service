package cn.gtmap.estateplat.server.model.trade;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/3/27
 * @description
 */
@XmlRootElement(name = "Data")
public class Data {
    private String fwid; //房屋编码
    private String zt;   //状态（网签备案中，已备案，租赁备案中，已租赁和无交易状态）

    @XmlElement(name = "FWID")
    public String getFwid() {
        return fwid;
    }

    public void setFwid(String fwid) {
        this.fwid = fwid;
    }

    @XmlElement(name = "ZT")
    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
}
