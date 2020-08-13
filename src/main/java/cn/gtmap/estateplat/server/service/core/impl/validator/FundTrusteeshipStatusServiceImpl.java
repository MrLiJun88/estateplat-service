package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.service.etl.JyxxService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import cn.gtmap.estateplat.server.web.main.BaseController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2019/4/10
 * @description
 */
public class FundTrusteeshipStatusServiceImpl extends BaseController implements ProjectValidateService {
    @Autowired
    private JyxxService jyxxService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (project != null) {
            String status = "";
            if (StringUtils.equals(AppConfig.getProperty("qxdm"),ParamsConstants.QXDM_CS)) {
                if (StringUtils.equals(Constants.XTLY_JYSL, project.getXtly())) {
                    // 常熟使用jybh去验证资金监管
                    status = jyxxService.getFundTrusteeshipStatus(null,project.getJybh());
                }
            } else {
                status = jyxxService.getFundTrusteeshipStatus(project.getBdcdyh(),null);
            }
            if (StringUtils.equals(status, Constants.FUND_SUPERVISION_STATUS_WDZ)) {
                map.put("info", project.getProid());
            } else if (StringUtils.equals(status,ParamsConstants.EXCEPTION_LOWERCASE)){
                map.put("info", project.getProid());
                map.put("replace", "资金监管接口调用异常，请检查接口！");
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "306";
    }

    @Override
    public String getDescription() {
        return "验证资金托管是否未到账";
    }
}
