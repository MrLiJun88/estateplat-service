package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.GdFwsyq;
import cn.gtmap.estateplat.model.server.core.GdTdsyq;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.mapper.GdFwsyqMapper;
import cn.gtmap.estateplat.server.core.mapper.GdTdsyqMapper;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.service.etl.GetIntegrationBusinessDataService;
import cn.gtmap.estateplat.utils.CommonUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/5/21
 * @description 验证在集成平台有预约的产权证号必须通过集成平台接口创件（昆山）
 */
public class IntegrationCqzhValidateServiceImpl implements ProjectValidateService{
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private GdFwsyqMapper gdFwsyqMapper;
    @Autowired
    private GdTdsyqMapper gdTdsyqMapper;
    @Autowired
    private GetIntegrationBusinessDataService getIntegrationBusinessDataService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    Logger logger = LoggerFactory.getLogger(IntegrationCqzhValidateServiceImpl.class);
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project)param.get("project");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            if(project != null && StringUtils.isNotBlank(project.getBdcdyh())){
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
                if(bdcXm != null){
                    String bdcdyh = project.getBdcdyh();
                    String cqzh = null;
                    if(StringUtils.isBlank(project.getGdproid())){
                        List<String> cqProidList = bdcXmService.getXsBdcCqProidByBdcdyh(bdcdyh);
                        if (CollectionUtils.isNotEmpty(cqProidList)){
                            cqzh = bdcZsService.getCombineBdcqzhByProid(cqProidList.get(0));
                        }
                    }else{
                        List<GdFwsyq> gdFwsyqList = gdFwsyqMapper.getGdFwsyqListByBdcdyh(bdcdyh);
                        List<GdTdsyq> gdTdsyqList = gdTdsyqMapper.getTdGdTdsyqListByBdcdyh(bdcdyh);
                        if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                            cqzh = gdFwsyqList.get(0).getFczh();
                        }else if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                            cqzh = gdTdsyqList.get(0).getTdzh();
                        }
                    }
                    //分别持证取其中的一本证书
                    if (StringUtils.isNotBlank(cqzh)){
                        String[] cqzhArray = cqzh.split(",");
                        cqzh = cqzhArray[0];
                    }
                    //对产权证号去括号和文字处理（接口需要）
                    String regex = "[\u4e00-\u9fa5]|[(]|[)]|[\uff08-\uff09]";
                    Pattern pat = Pattern.compile(regex);
                    Matcher mat = pat.matcher(cqzh);
                    cqzh = mat.replaceAll("");
                    Map responseMap = getIntegrationBusinessDataService.validateCqzh(cqzh,null);
                    if(responseMap != null && responseMap.get(ParamsConstants.SUCCESS_LOWERCASE) == Boolean.TRUE ){
                        Map dataMap = JSONObject.fromObject(responseMap.get("data"));
                        if (StringUtils.equals(CommonUtil.formatEmptyValue(dataMap.get("yyFlag")),"0")){
                            resultMap.put("info",project.getProid());
                        }
                    }
                }
            }
        } catch (Exception e) {
           logger.error(e.getMessage());
        }
        return resultMap;
    }

    @Override
    public String getCode() {
        return "1003";
    }

    @Override
    public String getDescription() {
        return "集成平台预约的产证号请通过集成平台接口创件！";
    }
}
