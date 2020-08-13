package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.model.server.core.QllxParent;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/19
 * @description 验证未办结的项目进行查封、抵押后，原项目被冻结，不可继续办理
 */
public class BdcdyYxmDjValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private QllxParentService qllxParentService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            HashMap<String, Object> cfmap = new HashMap<String, Object>();
            cfmap.put("bdcdyh", project.getBdcdyh());
            map.put("xmzt", Constants.XMZT_BJ);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            map.put("isjf", "1");
            List<QllxParent> cflist = qllxParentService.queryQllxVo(new BdcCf(), cfmap);
            if (CollectionUtils.isNotEmpty(cflist)) {
                proidList = new ArrayList<String>();
                for (int i = 0; i < cflist.size(); i++) {
                    proidList.add(cflist.get(i).getProid());
                }
            }
            HashMap<String, Object> dymap = new HashMap<String, Object>();
            dymap.put("bdcdyh", project.getBdcdyh());
            dymap.put("xmzt", Constants.XMZT_BJ);
            dymap.put("qszt", Constants.QLLX_QSZT_XS);
            dymap.put("iszx", "1");
            List<QllxParent> dylist = qllxParentService.queryQllxVo(new BdcDyaq(), dymap);
            if (CollectionUtils.isNotEmpty(dylist)) {
                proidList = new ArrayList<String>();
                for (int i = 0; i < dylist.size(); i++) {
                    proidList.add(dylist.get(i).getProid());
                }
            }
        }
        map.put("info", proidList);
        return map;
    }

    @Override
    public String getCode() {
        return "131";
    }

    @Override
    public String getDescription() {
        return "验证未办结的项目进行查封、抵押后，原项目被冻结，不可继续办理";
    }
}
