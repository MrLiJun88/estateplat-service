package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/29
 * @description 判断选择产权证的权利状态 是否为现势状态 即正常状态
 */
public class QlZtCheckValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;


    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        String proid = null;
        QllxVo xQllxVo = StringUtils.isNotBlank(project.getYxmid()) ? qllxService.getQllxVoByProid(project.getYxmid()) : null;
        if (null == xQllxVo && StringUtils.isNotBlank(project.getYxmid())) {
            BdcXm ybdcxm = bdcXmService.getBdcXmByProid(project.getYxmid());
            QllxVo qllxVo = qllxService.makeSureQllx(ybdcxm);
            HashMap mapQuery = new HashMap();
            mapQuery.put("proid", project.getYxmid());
            mapQuery.put("bdcdyh", project.getBdcdyh());
            mapQuery.put("qszt", Constants.QLLX_QSZT_LS);
            mapQuery.put("xmzt", Constants.XMZT_BJ);
            List<QllxParent> list = qllxParentService.queryQllxVo(qllxVo, mapQuery);
            if (CollectionUtils.isNotEmpty(list)) {
                proid = list.get(0).getProid();
            }
        }else{
            if(StringUtils.isNotBlank(project.getGdproid())) {
                HashMap paramMap = Maps.newHashMap();
                paramMap.put("proid",project.getGdproid());
                paramMap.put("iszx",Constants.GDQL_ISZX_YZX);
                List<HashMap> gdFwQlResult = gdFwService.getGdFwQl(paramMap);
                if(CollectionUtils.isNotEmpty(gdFwQlResult)){
                    proid = project.getGdproid();
                }else{
                    List<HashMap> gdTdQlResult = gdTdService.getGdTdQl(paramMap);
                    if(CollectionUtils.isNotEmpty(gdTdQlResult)){
                        proid = project.getGdproid();
                    }
                }
            }
        }
        map.put("info", proid);
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
        return "203";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "判断选择产权证的权利状态 是否为现势状态 即正常状态";
    }
}
