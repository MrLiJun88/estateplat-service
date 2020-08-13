package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public class BdcFwOfTdHasZjgcdyValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String zdzhh = project.getBdcdyh().substring(0, 19);
            List<BdcBdcdy> bdcBdcdyList = bdcdyService.getBdcdyByZdzhh(zdzhh, Constants.BDCLX_TDFW);
            if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {
                proidList = new LinkedList<String>();
                Map<String, Object> queryParam = new HashMap<String, Object>();
                for (BdcBdcdy bdcBdcdy : bdcBdcdyList) {
                    queryParam.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    queryParam.put("dyzt", Constants.ZJJZWXX_DYZT_XS);
                    List<BdcZjjzwxx> bdcZjjzwxxLst = bdcZjjzwxxService.getZjjzwxx(queryParam);
                    if (CollectionUtils.isNotEmpty(bdcZjjzwxxLst)) {
                        for (BdcZjjzwxx bdcZjjzwxx : bdcZjjzwxxLst) {
                            proidList.add(bdcZjjzwxx.getProid());
                        }
                        break;
                    }
                }
            }

        }
        map.put("info", CollectionUtils.isNotEmpty(proidList) ? proidList : null);
        return map;
    }

    @Override
    public String getCode() {
        return "139";
    }

    @Override
    public String getDescription() {
        return "验证土地对应的房屋是否有在建工程抵押";
    }
}
