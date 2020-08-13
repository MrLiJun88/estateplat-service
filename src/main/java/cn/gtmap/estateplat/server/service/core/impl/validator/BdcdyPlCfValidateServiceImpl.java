package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
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
 * @version 1.0, 2016/10/19
 * @description 验证是否可以批量查封
 */
public class BdcdyPlCfValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcCfService bdcCfService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map map = new HashMap();
        Project project= (Project)param.get("project");
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            List<BdcCf> bdcCfList = new ArrayList<BdcCf>();
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                List<BdcCf> queryBdcCfList = bdcCfService.getCfByBdcdyid(bdcBdcdy.getBdcdyid());
                if (CollectionUtils.isNotEmpty(queryBdcCfList))
                    bdcCfList.addAll(queryBdcCfList);
            }
            //批量续封，批量轮候查封验证暂未考虑
            if (CollectionUtils.isNotEmpty(bdcCfList)) {
                for (BdcCf bdcCf : bdcCfList) {
                    if (StringUtils.isNotBlank(bdcCf.getCflx())) {
                        if (CollectionUtils.isEmpty(proidList)) {
                            proidList = new ArrayList<String>();
                            proidList.add(bdcCf.getProid());
                        } else {
                            proidList.add(bdcCf.getProid());
                        }
                    }
                }
            }
            map.put("info", proidList);
        }
        return map;
    }

    @Override
    public String getCode() {
        return "130";
    }

    @Override
    public String getDescription() {
        return "验证是否可以批量查封";
    }
}
