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
 * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @version 1.0, 2018-08-09
 * @description 解锁流程选择数据是否存在锁定
 */
public class JsWorkflowExistSdValidateServiceImpl implements ProjectValidateService{
    @Autowired
    private BdcSdService bdcSdService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        HashMap<String,Object> queryMap = Maps.newHashMap();
        List<String> proidList = new ArrayList<String>();
        List<BdcBdcZsSd> bdcBdcZsSdList = null;
        List<GdBdcSd> gdBdcSdList = null;
        queryMap.put("xzzt", Constants.XZZT_SD);
        if(StringUtils.isNotBlank(project.getYxmid())){
            queryMap.put("proid",project.getYxmid());
            bdcBdcZsSdList = bdcSdService.queryBdcZsSdByMap(queryMap);
        }else if(StringUtils.isNotBlank(project.getGdproid())){
            queryMap.put("proid",project.getGdproid());
            gdBdcSdList = bdcSdService.queryGdBdcSdByMap(queryMap);
        }
        if(CollectionUtils.isEmpty(bdcBdcZsSdList) && CollectionUtils.isEmpty(gdBdcSdList)){
            proidList.add(project.getProid());
            map.put("info", proidList);
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
        return "206";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @version 1.0, 2018-08-09
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证解锁流程证书是否存在锁定";
    }

}
