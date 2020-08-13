package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
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
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证项目是否匹配不动产单元
 */
public class BdcdyIsMatchValidateServiceImpl implements ProjectValidateService {
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
    private BdcXmService bdcXmService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                        if (bdcBdcdy == null) {
                            proidList = new ArrayList<String>();
                            proidList.add(bdcXm.getBdcdyid());
                        }
                    } else if(bdcXm != null){
                        proidList = new ArrayList<String>();
                        proidList.add(bdcXm.getProid());
                    }
                }
            }
        }
        map.put("info", proidList);
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
        return "115";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证项目是否匹配不动产单元";
    }
}
