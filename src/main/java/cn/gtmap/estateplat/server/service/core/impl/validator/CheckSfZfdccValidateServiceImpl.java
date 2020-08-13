package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcDyaqService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.service.core.config.ValidateNodeConfigService;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadJsonUtil;
import com.gtis.plat.vo.PfActivityVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/9
 * @description 验证被担保主债权数额必须小于10亿
 */
public class CheckSfZfdccValidateServiceImpl implements ProjectValidateService {
    private Logger logger=LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ValidateNodeConfigService validateNodeConfigService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        String targetActivityDefids="";
        try {
            HttpServletRequest request= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            targetActivityDefids= request.getParameter("targetActivityDefids");
        }catch (Exception e){
            logger.info("获取数据错误="+e);
        }
        Project project = (Project) param.get("project");
        String targetActivityName = PlatformUtil.getTargetActivityName(project.getWiid(), targetActivityDefids);
        Map<String, Object> map = new HashMap<String, Object>();
        Boolean validateEnable = validateNodeConfigService.nodeValidateEnable(project, this.getCode());
        if (null != project && StringUtils.isNotBlank(project.getProid())&&validateEnable){
            BdcXm bdcXm=bdcXmService.getBdcXmByProid(project.getProid());
            Integer dcsjzs= ReadJsonUtil.getDcsjzs(bdcXm.getSqlx(),bdcXm.getDjlx());
            if(dcsjzs!=null&&bdcXm.getDcsjzs()!=null&&dcsjzs>=bdcXm.getDcsjzs()){
                if(!StringUtils.equals("督查",targetActivityName)){
                    map.put("info",project.getProid());
                }
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "404";
    }

    @Override
    public String getDescription() {
        return "验证是否需要转发至督查节点";
    }
}
