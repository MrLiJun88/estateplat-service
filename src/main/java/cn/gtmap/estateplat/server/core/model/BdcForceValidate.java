package cn.gtmap.estateplat.server.core.model;



/**
 * 不动产强制验证bean
 *
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/5/8
 */
public class BdcForceValidate {
    //排除强制验证申请类型代码
    private String esqlxdm;
    //强制验证提示信息
    private String checkmsg;

    public String getEsqlxdm() {
        return esqlxdm;
    }

    public void setEsqlxdm(String esqlxdm) {
        this.esqlxdm = esqlxdm;
    }

    public String getCheckmsg() {
        return checkmsg;
    }

    public void setCheckmsg(String checkmsg) {
        this.checkmsg = checkmsg;
    }
}
