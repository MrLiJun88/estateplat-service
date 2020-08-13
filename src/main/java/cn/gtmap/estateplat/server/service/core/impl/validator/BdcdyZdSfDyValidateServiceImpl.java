package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证不动产单元所在宗地是否处于抵押
 */
public class BdcdyZdSfDyValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcSpfZdHjgxService bdcSpfZdHjgxService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        QllxVo qllxVo = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String bdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
            if (StringUtils.isNotBlank(bdcdyh)) {
                HashMap<String, Object> querymap = new HashMap<String, Object>();
                querymap.put("bdcdyh", bdcdyh);
                querymap.put("xmzt", "1");
                qllxVo = new BdcDyaq();
                List<QllxParent> list = qllxParentService.queryZtzcQllxVo(qllxVo, querymap);
                if (CollectionUtils.isNotEmpty(list)) {
                    map.put("info", list.get(0).getProid());
                }
            }
            if ((map.get("info") == null || StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("info")))) && StringUtils.isNotBlank(bdcdyh)) {
                List<String> qlids = gdTdService.getGdTdQlidByDjh(bdcdyh.substring(0, 19));
                if (CollectionUtils.isNotEmpty(qlids)) {
                    for (String qlid : qlids) {
                        GdDy gdDy = gdTdService.getGddyqByQlid(qlid, Constants.GDQL_ISZX_WZX);
                        if (gdDy != null && gdDy.getIsjy() != null && gdDy.getIsjy() != 1) {
                            map.put("info", gdDy.getDyid());
                        }
                    }
                }
            }
            //如果已经做过逐户解押不需要验证
            if (project != null && CollectionUtils.isNotEmpty(project.getBdcdyhs())) {
                boolean existsRel = true;
                for (String fwBdcdyh : project.getBdcdyhs()) {
                    List<BdcSpfZdHjgx> bdcSpfZdHjgxList = bdcSpfZdHjgxService.getBdcZdFwRelListByBdcdyh(fwBdcdyh);
                    if (CollectionUtils.isEmpty(bdcSpfZdHjgxList)) {
                        existsRel = false;
                        break;
                    }

                }
                if (existsRel)
                    map.put("info", null);
            } else if (project != null && StringUtils.isNotEmpty(project.getBdcdyh())) {
                List<BdcSpfZdHjgx> bdcSpfZdHjgxList = bdcSpfZdHjgxService.getBdcZdFwRelListByBdcdyh(project.getBdcdyh());
                if (CollectionUtils.isNotEmpty(bdcSpfZdHjgxList)) {
                    map.put("info", null);
                }
            }
        }
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
        return "120";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元所在宗地是否处于抵押";
    }
}
