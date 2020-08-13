package cn.gtmap.estateplat.server.service.login;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/4/22
 * @description 系统登录接口
 */
public interface LoginService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param token
     * @return String
     * @description 根据接口中的token获取系统登录用户名
     */
    String getLoginNameByToken(String token);
}
