package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import cn.gtmap.estateplat.model.server.core.GdBdcSd;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcSdService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
 * @Time 2020/4/29 17:00
 * @description 遗失补证流程证书是否存在非遗失补证的验证
 */
public class BdczsExistFysbzValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcSdService bdcSdService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> queryMap = Maps.newHashMap();
        List<String> proidList = new ArrayList<String>();

        if (StringUtils.isNotBlank(project.getYxmid())) {
            queryMap.put("proid", project.getYxmid());
            List<BdcBdcZsSd> bdcBdcZsSdList = bdcSdService.queryBdcZsSdByMap(queryMap);
            if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                for (BdcBdcZsSd bdcBdcZsSd : bdcBdcZsSdList) {
                    if (StringUtils.isNotBlank(bdcBdcZsSd.getXztype()) && !StringUtils.equals(Constants.XZZT_YS, bdcBdcZsSd.getXztype())) {
                        proidList.add(project.getProid());
                        break;
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }else{
            map.put("info", null);
        }
        return map;
    }

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @version 1.0, 2018-08-09
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "207";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @version 1.0, 2018-08-09
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证证书是否存在非遗失补证的限制";
    }

}
