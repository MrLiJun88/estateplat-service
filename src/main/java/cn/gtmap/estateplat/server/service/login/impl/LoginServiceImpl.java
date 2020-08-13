package cn.gtmap.estateplat.server.service.login.impl;

import cn.gtmap.estateplat.server.model.login.Result;
import cn.gtmap.estateplat.server.model.login.Status;
import cn.gtmap.estateplat.server.model.login.UserComparison;
import cn.gtmap.estateplat.server.service.login.LoginService;
import cn.gtmap.estateplat.server.utils.HttpRequestUtils;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadJsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/4/22
 * @description 系统登录接口
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public String getLoginNameByToken(String token) {
        String loginName = "";
        if(StringUtils.isNotBlank(token)) {
            String oaUserInfoUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("oa.user.info.url"));
            if(StringUtils.isNotBlank(oaUserInfoUrl) && StringUtils.isNotBlank(token)){
                oaUserInfoUrl += token;
                String resultJson = HttpRequestUtils.sendGet(oaUserInfoUrl, null, "utf-8");
                Result result = JSONObject.parseObject(resultJson, Result.class);
                String personId = "";
                if(result != null){
                    List<Status> statusList = result.getStatus();
                    if(CollectionUtils.isNotEmpty(statusList)){
                        Status status = statusList.get(0);
                        if(status != null && StringUtils.isNotBlank(status.getErrorCode())
                                && StringUtils.equals(status.getErrorCode(), ParamsConstants.OA_USER_SUCCESS)){
                            personId = status.getPersonID();
                        }
                    }
                }
                if(StringUtils.isNotBlank(personId)){
                    List<UserComparison> userComparisonList = ReadJsonUtil.getUserComparison();
                    if(CollectionUtils.isNotEmpty(userComparisonList) && StringUtils.isNotBlank(personId)) {
                        for(UserComparison userComparison:userComparisonList) {
                            if(StringUtils.equals(userComparison.getPersonId(),personId)) {
                                loginName = userComparison.getLoginName();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return loginName;
    }
}
