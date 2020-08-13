package cn.gtmap.estateplat.server.web.login;

import cn.gtmap.estateplat.server.service.login.LoginService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.fr.base.Base64;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/4/22
 * @description 外界第三方登录系统
 */
@Controller
@RequestMapping("/externalLogin")
public class ExternalLoginController extends BaseController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        token = URLEncoder.encode(token);
        String singleLoginUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("single.login.url"));
        if(StringUtils.isNotBlank(singleLoginUrl)) {
            StringBuilder redirectrUrl = new StringBuilder();
            String loginName = loginService.getLoginNameByToken(token);
            redirectrUrl.append(this.casUrl).append(singleLoginUrl).append(loginName).append("&password=&url=").append(this.portalUrl);
            response.sendRedirect(redirectrUrl.toString());
        }
    }

}
