package cn.gtmap.estateplat.server.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/31
 * @description 不动产消息日志表
 */
@Table(name = "BDC_MESSAGELOG")
public class BdcMessageLog {
    @Id
    private String id;//消息唯一键
    private String proid;//项目id
    private String xxdlm;//消息队列名
    private String xxt;//消息体
    private String fszt;//发送状态（0：未发送；1：已发送）
    private String xfzt;//消费状态（0：未消费；1：已消费）
    private Date cjsj;//创建时间
    private Date gxsj;//更新时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getXxdlm() {
        return xxdlm;
    }

    public void setXxdlm(String xxdlm) {
        this.xxdlm = xxdlm;
    }

    public String getXxt() {
        return xxt;
    }

    public void setXxt(String xxt) {
        this.xxt = xxt;
    }

    public String getFszt() {
        return fszt;
    }

    public void setFszt(String fszt) {
        this.fszt = fszt;
    }

    public String getXfzt() {
        return xfzt;
    }

    public void setXfzt(String xfzt) {
        this.xfzt = xfzt;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    public Date getGxsj() {
        return gxsj;
    }

    public void setGxsj(Date gxsj) {
        this.gxsj = gxsj;
    }
}
