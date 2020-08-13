package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.ba.BaFwxxCxParamModel;
import cn.gtmap.estateplat.server.model.ba.House;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.HttpRequestUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
 * @Time 2020/4/27 14:17
 * @description 验证不动产单元是否未售
 */
public class BdcdyBafwztValidateServiceImpl implements ProjectValidateService {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        String checkMsg = StringUtils.EMPTY;
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
            if (bdcBdcdy != null) {
                BaFwxxCxParamModel baFwxxCxParamModel = new BaFwxxCxParamModel();
                List<House> houseList = new ArrayList<House>();
                String sn = AppConfig.getProperty("ba.fwxxcx.sn");
                House house = new House();
                house.setEstateUnitNo(bdcBdcdy.getBdcdyh());
                house.setHouseCode(bdcBdcdy.getFwbm());
                houseList.add(house);

                baFwxxCxParamModel.setSN(sn);
                baFwxxCxParamModel.setHouseList(houseList);
                String responseStr = HttpRequestUtils.sendPost(AppConfig.getProperty("ba.fwxxcx.url"), JSONObject.toJSONString(baFwxxCxParamModel, SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty));
                if (StringUtils.isNotBlank(responseStr)) {
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    if (StringUtils.equals(jsonObject.getString("statuscode"), Constants.BA_FWCX_SUCCESS_CODE)) {
                        JSONArray housecodelist = jsonObject.getJSONArray("housecodelist");
                        if (CollectionUtils.isEmpty(housecodelist)) {
                            proidList.add(project.getProid());
                        } else {
                            for (int i = 0; i < housecodelist.size(); i++) {
                                String housestatus = housecodelist.getJSONObject(i).get("housestatus").toString();
                                if (!StringUtils.equals(housestatus, Constants.BA_FWCX_FWZT_YS)) {
                                    proidList.add(project.getProid());
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList.get(0));
            if (StringUtils.isNotBlank(checkMsg)) {
                map.put("replace", checkMsg);
            }
        } else {
            map.put("info", null);
        }
        return map;
    }


    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "918";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否未售";
    }
}
