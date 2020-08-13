package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.service.currency.CurrencyService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020/3/17
 * @description 江阴验证商品房网签备案状态是否已备案
 */
public class JySpfJyZtValidateServiceImpl implements ProjectValidateService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project = (Project) param.get("project");
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getFwbm())) {
                String status = currencyService.checkJyzt(bdcBdcdy.getFwbm(), Constants.GETSPFXX_LOWERCAS);
                if ("true".equals(status)) {
                    map.put("info", project.getProid());
                }
            } else {
                logger.error("验证商品房网签备案状态失败原因：fwbm为空");
            }
        } else {
            logger.error("验证商品房网签备案状态失败原因：bdcdyh为空");
        }
        return map;
    }

    @Override
    public String getCode() {
        return "308";
    }

    @Override
    public String getDescription() {
        return "江阴验证商品房网签备案状态是否已备案";
    }
}
