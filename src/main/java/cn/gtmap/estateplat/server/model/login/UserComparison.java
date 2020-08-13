package cn.gtmap.estateplat.server.model.login;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/4/22
 * @description 用户对照
 */
public class UserComparison {
    //第三方用户id
    private String personId;
    //系统登录用户名
    private String loginName;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

}
