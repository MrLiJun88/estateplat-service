package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcDyaqService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/9
 * @description 验证被担保主债权数额必须小于10亿
 */
public class BdbzzqseValueValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcDyaqService bdcDyaqService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != project && StringUtils.isNotBlank(project.getProid())){
            BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(project.getProid());
            map.clear();
            if (bdcDyaq != null&&null != bdcDyaq.getBdbzzqse()){
                //被债权数额（最高债权数额）数据以“万元”为标准
                Double bdbzzqse = bdcDyaq.getBdbzzqse();
                // 被担保主债权数额必须小于10亿
                if(bdbzzqse >= 100000){
                    map.put("info",project.getProid());
                }
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "913";
    }

    @Override
    public String getDescription() {
        return "验证被担保主债权数额必须小于10亿";
    }
}
