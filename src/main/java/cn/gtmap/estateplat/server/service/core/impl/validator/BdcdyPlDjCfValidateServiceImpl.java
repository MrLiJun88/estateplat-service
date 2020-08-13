package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.model.server.core.QllxParent;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 批量验证是否查封
 */
public class BdcdyPlDjCfValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private QllxParentService qllxParentService;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isBlank(project.getZdzhh())&&CollectionUtils.isNotEmpty(project.getBdcdyhs())) {
            String bdcdyh = project.getBdcdyhs().get(0);
            if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() > 19) {
                project.setZdzhh(bdcdyh.substring(0, 19));
            }
        }
        if (project != null && StringUtils.isNotBlank(project.getZdzhh())) {
            HashMap<String, Object> querymap = new HashMap<String, Object>();
            querymap.put("zdzhh", project.getZdzhh());
            querymap.put("cflx", "'"+Constants.CFLX_ZD_CF+"','"+Constants.CFLX_LHCF+"'");
            if (StringUtils.isNotBlank(project.getDcbIndex())) {
                List<QllxParent> qllxParentList = qllxParentService.queryZtzcQllxVo(new BdcCf(), querymap);
                if (CollectionUtils.isNotEmpty(qllxParentList)) {
                    for (QllxParent qllxParent : qllxParentList) {
                        proidList.add(qllxParent.getProid());
                    }
                }
            } else if (CollectionUtils.isNotEmpty(project.getBdcdyhs())) {
                List<String> bdcdyhs = project.getBdcdyhs();
                querymap = new HashMap<String, Object>();
                String zdzhh = project.getZdzhh();
                querymap.put("zdzhh", zdzhh);
                querymap.put("filterycf","true");
                List<Map> qllxMapParentList = qllxParentService.queryZtzcQllxMap(new BdcCf(), querymap);
                if (CollectionUtils.isNotEmpty(qllxMapParentList)) {
                    for (Map qllxMap : qllxMapParentList) {
                        String tempBdcdyh = "";
                        if (qllxMap.containsKey(ParamsConstants.BDCDYH_CAPITAL) && qllxMap.get(ParamsConstants.BDCDYH_CAPITAL) != null)
                            tempBdcdyh = qllxMap.get(ParamsConstants.BDCDYH_CAPITAL).toString();
                        if (bdcdyhs.contains(tempBdcdyh) && qllxMap.containsKey(ParamsConstants.PROID_CAPITAL) && qllxMap.get(ParamsConstants.PROID_CAPITAL) != null)
                            proidList.add(qllxMap.get(ParamsConstants.PROID_CAPITAL).toString());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList))
            map.put("info", proidList.get(0));
        else
            map.put("info", null);
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
        return "110";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证批量发证的不动产单元是否查封（只针对批量选择楼盘表或者选择逻辑幢）";
    }
}
