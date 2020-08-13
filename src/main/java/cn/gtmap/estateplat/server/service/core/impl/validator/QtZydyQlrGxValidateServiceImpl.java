package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/19
 * @description 转移抵押转发验证，验证转移的权利人是抵押的义务人
 */
public class QtZydyQlrGxValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map map = new HashMap();
        Project project= (Project)param.get("project");
        //转移的权利人
        List<BdcQlr> zyQlrList = null;
        //抵押的义务人
        List<BdcQlr> dyYwrList = null;
        if (project != null && StringUtils.isNotBlank(project.getWiid())) {
            //过滤掉附属设施
            HashMap<String,String> filterMap = new HashMap<String, String>();
            filterMap.put("wiid",project.getWiid());
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(filterMap);
            if (CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.size() > 1) {
                for (BdcXm bdcXm : bdcXmList) {
                    if (!bdcXm.getQllx().equals(Constants.QLLX_DYAQ)) {
                        zyQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                    } else {
                        dyYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(zyQlrList) && CollectionUtils.isNotEmpty(dyYwrList) && zyQlrList.size() == dyYwrList.size()) {
            int i = 0;
            for (BdcQlr bdcQlr : zyQlrList) {
                String name = bdcQlr.getQlrmc();
                String zjh = bdcQlr.getQlrzjh();
                int j = 0;
                for (BdcQlr bdcYwr : dyYwrList) {
                    String compareName = bdcYwr.getQlrmc();
                    String compareZjh = bdcYwr.getQlrzjh();
                    if (StringUtils.equals(name, compareName) && StringUtils.equals(zjh, compareZjh))
                        break;
                    j++;
                }
                if (j == dyYwrList.size())
                    break;
                i++;
            }
            if (i < zyQlrList.size())
                map.put("info", "false");
        } else {
            map.put("info", "false");
        }
        return map;
    }

    @Override
    public String getCode() {
        return "907";
    }

    @Override
    public String getDescription() {
        return "转移抵押转发验证，验证转移的权利人是抵押的义务人";
    }
}
