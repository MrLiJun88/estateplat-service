package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcdyTradingStatusService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
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
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 2019/3/27
 * @description 验证不动产单元交易已查封状态
 */
public class BdcdyTradingStatusCloseDownServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcdyTradingStatusService bdcdyTradingStatusService;
    @Autowired
    private DjsjFwService djsjFwService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String fwbm = "";
            HashMap paramMap = new HashMap();
            paramMap.put(ParamsConstants.BDCDYH_LOWERCASE, project.getBdcdyh());
            List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(paramMap);
            if(CollectionUtils.isNotEmpty(djsjFwHsList)) {
                fwbm = djsjFwHsList.get(0).getFwbm();
            }
            String tradingStatus = bdcdyTradingStatusService.getLtTradingStatus(fwbm);
            if(StringUtils.isNotBlank(tradingStatus) && StringUtils.contains(tradingStatus, Constants.JYZT_DJ_MC)) {
                map.put("info", project.getProid());
            }else if(StringUtils.equals(tradingStatus,ParamsConstants.EXCEPTION_LOWERCASE)){
                map.put("info", project.getProid());
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "150";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元交易状态已查封";
    }
}
