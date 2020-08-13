package cn.gtmap.estateplat.server.model.trade;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/3/29
 * @description 昆山交易状态登记子项验证
 */
public class DjzxValidate {
    private String sqlx; //申请类型
    private String djzx; //登记子项
    private String jyzt; //交易状态
    private String checkModel; //验证模式
    private String checkMsg; //验证信息

    public String getCheckMsg() {
        return checkMsg;
    }

    public void setCheckMsg(String checkMsg) {
        this.checkMsg = checkMsg;
    }

    public String getSqlx() {
        return sqlx;
    }

    public void setSqlx(String sqlx) {
        this.sqlx = sqlx;
    }

    public String getDjzx() {
        return djzx;
    }

    public void setDjzx(String djzx) {
        this.djzx = djzx;
    }

    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    public String getCheckModel() {
        return checkModel;
    }

    public void setCheckModel(String checkModel) {
        this.checkModel = checkModel;
    }

}
