package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/20
 * @description 验证在建工程抵押，权籍数据是否处于抵押状态
 */
public class BdcdyFwYcHsIsDyValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private DjsjFwMapper djsjFwMapper;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project= (Project)param.get("project");
        List<String> proidList = new ArrayList<String>();
        param.put("bdcdyh",project.getBdcdyh());
        List<Map> ycFwHsList=djsjFwMapper.getDjsjYcFwHsForZjgcdy(param);
        if(CollectionUtils.isNotEmpty(ycFwHsList)){
            String zjgcdy= (String) ycFwHsList.get(0).get("ZJGCDY");
            if(StringUtils.equals(zjgcdy, "0")){
                proidList.add((String) ycFwHsList.get(0).get("FW_HS_INDEX"));
            }
            if (CollectionUtils.isNotEmpty(proidList))
                map.put("info", proidList.get(0));
            else
                map.put("info", null);
        }
        return map;
    }

    @Override
    public String getCode() {
        return "134";
    }

    @Override
    public String getDescription() {
        return "验证在建工程抵押，权籍数据是否处于抵押状态";
    }
}
