package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
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
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/29
 * @description 验证查封是否存在没有解封的续封
 */
public class BdcdyXfValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            List<BdcXm> bdcXmList = bdcCfService.getCfXmByBdcdyh(project.getBdcdyh());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcxm : bdcXmList) {
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcxm.getProid());
                    if (StringUtils.equals(bdcxm.getDjzx(), Constants.DJZX_XF) && CollectionUtils.isNotEmpty(bdcXmRelList)&&
                            !StringUtils.equals(bdcxm.getProid(), project.getYxmid())&&
                            !StringUtils.equals(bdcxm.getWiid(),project.getWiid())&&
                            StringUtils.equals(bdcXmRelList.get(0).getYproid(),project.getYxmid())) {
                        BdcCf bdcCf = bdcCfService.selectCfByProid(bdcxm.getProid());
                        if(Constants.QLLX_QSZT_XS == bdcCf.getQszt()) {
                            proidList.add(bdcxm.getProid());
                        }
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
        return "201";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证查封是否存在没有解封的续封";
    }
}
