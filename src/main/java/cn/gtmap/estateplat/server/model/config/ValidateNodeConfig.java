package cn.gtmap.estateplat.server.model.config;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/11/25
 * @description 新建项目验证验证节点配置
 */
public class ValidateNodeConfig {
    private String checkCode; //验证代码
    private String sqlxdm; //申请类型代码
    private String nodeName; //节点名称

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getSqlxdm() {
        return sqlxdm;
    }

    public void setSqlxdm(String sqlxdm) {
        this.sqlxdm = sqlxdm;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

}
