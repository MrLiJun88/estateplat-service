package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.GdQlr;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.BdcdyTradingStatusService;
import cn.gtmap.estateplat.server.core.service.GdQlrService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2019/9/16
 * @description 验证产权证号交易已备案状态
 */
public class CqzhTradingStatusForRecordServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcdyTradingStatusService bdcdyTradingStatusService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        String ybdcqzh = "";
        String tradingStatus = "";
        // 通过交易创建时不验证
        if (project != null && !StringUtils.equals(Constants.XTLY_JYSL, project.getXtly())) {
            if (StringUtils.isNotBlank(project.getGdproid())) {
                List<GdQlr> gdQlrList = gdQlrService.queryGdQlrListByProid(project.getGdproid(), Constants.QLRLX_QLR);
                if (CollectionUtils.isNotEmpty(gdQlrList)) {
                    ybdcqzh = gdQlrList.get(0).getCqzh();
                }
            } else if (StringUtils.isNotBlank(project.getYxmid())) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(project.getYxmid());
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    ybdcqzh = bdcZsList.get(0).getBdcqzh();
                }
            }
            String[] ybdcqzhArray = ybdcqzh.split(ParamsConstants.PUNCTUATION_COMMA);
            for (String ybdcqzhTemp : ybdcqzhArray) {
                tradingStatus = bdcdyTradingStatusService.getTradingFilingStatus(ybdcqzhTemp);
                if (StringUtils.equals(tradingStatus, ParamsConstants.Y_CAPITAL)) {
                    break;
                }
            }
            if (StringUtils.equals(tradingStatus, ParamsConstants.Y_CAPITAL)) {
                map.put("info", project.getProid());
            } else if (StringUtils.equals(tradingStatus, ParamsConstants.EXCEPTION_LOWERCASE)) {
                map.put("info", project.getProid());
                map.put("replace", "验证交易备案接口调用异常，请检查接口！");
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "503";
    }

    @Override
    public String getDescription() {
        return "验证产权证号交易状态已备案";
    }
}
