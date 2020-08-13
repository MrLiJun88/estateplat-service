package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXtCheckinfo;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
 * @Time 2020/5/28 9:16
 * @description 验证不动产单元是否存在权籍
 */
public class BdcdyExistQjValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private DjxxMapper djxxMapper;
    @Autowired
    private PlatformUtil platformUtil;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        BdcXtCheckinfo bdcXtCheckinfo = (BdcXtCheckinfo) param.get("bdcXtCheckinfo");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String activityName = PlatformUtil.getActivityName(project.getProid());
            if (StringUtils.equals(Constants.WORKFLOW_HD, activityName) || StringUtils.equals(Constants.XT_CHECKTYPE_BDCDY, bdcXtCheckinfo.getCheckType())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
                if (bdcBdcdy != null) {
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    paramMap.put("bdclx", bdcBdcdy.getBdclx());
                    String bdcdyh = djxxMapper.getDjsjBdcdyhByBdcdyh(paramMap);
                    if (StringUtils.isBlank(bdcdyh)) {
                        proidList.add(project.getProid());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList.get(0));
        } else {
            map.put("info", null);
        }
        return map;
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/5/28 9:19
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "193";
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/5/28 9:19
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "该不动产单元是否存在权籍";
    }
}
