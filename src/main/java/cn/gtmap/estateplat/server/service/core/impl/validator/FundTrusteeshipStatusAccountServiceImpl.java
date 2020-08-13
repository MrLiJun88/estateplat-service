package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.service.etl.JyxxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2019/9/25
 * @description
 */
public class FundTrusteeshipStatusAccountServiceImpl implements ProjectValidateService {
    @Autowired
    private JyxxService jyxxService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (project != null) {
            String status = jyxxService.getFundTrusteeshipStatus(project.getBdcdyh(),null);
            if (StringUtils.equals(status, Constants.FUND_SUPERVISION_STATUS_DZ)) {
                map.put("info", project.getProid());
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "307";
    }

    @Override
    public String getDescription() {
        return "验证资金托管是否已到账";
    }
}
