package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.service.currency.CurrencyService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2020/4/20
 * @description 江阴验证房屋网签备案状态
 */
public class JybdcdyZtValidateServiceImpl implements ProjectValidateService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private DjsjFwService djsjFwService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project = (Project) param.get("project");
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String fwbm = "";
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getFwbm())) {
                fwbm = bdcBdcdy.getFwbm();
            } else {
                HashMap paramMap = new HashMap();
                paramMap.put(ParamsConstants.BDCDYH_LOWERCASE, project.getBdcdyh());
                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(paramMap);
                if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                    fwbm = djsjFwHsList.get(0).getFwbm();
                }
            }
            List<Map> mapList = new ArrayList<>();
            if (StringUtils.isNotBlank(fwbm)) {
                Map checkMap = new HashMap();
                checkMap.put("houseCode", fwbm);
                mapList.add(checkMap);
            }
            String result = currencyService.checkHouseZt(mapList);
            if (StringUtils.isNotBlank(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.containsKey("statuscode") && StringUtils.equals(String.valueOf(jsonObject.get("statuscode")), "0") && jsonObject.containsKey("housecodelist")) {
                    List<JSONObject> housecodelist = (List<JSONObject>) jsonObject.get("housecodelist");
                    if (CollectionUtils.isNotEmpty(housecodelist)) {
                        for (JSONObject object : housecodelist) {
                            if (object.containsKey("housecode") && StringUtils.isNotBlank((CharSequence) object.get("housecode")) && object.containsKey("housestatus") && StringUtils.isNotBlank((CharSequence) object.get("housestatus")) && (StringUtils.equals(String.valueOf(object.get("housestatus")), "2") || StringUtils.equals(String.valueOf(object.get("housestatus")), "3") || StringUtils.equals(String.valueOf(object.get("housestatus")), "9"))) {
                                map.put("info", project.getProid());
                            }
                        }
                    }
                }
            }
        } else {
            logger.error("江阴验证房屋网签备案状态：bdcdyh为空");
        }
        return map;
    }

    @Override
    public String getCode() {
        return "152";
    }

    @Override
    public String getDescription() {
        return "江阴验证房屋网签备案状态";
    }
}
