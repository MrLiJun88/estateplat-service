package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
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
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证不动产单元是否办理过预告登记,预告抵押权登记
 */
public class BdcdyYzxValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private GdYgService gdYgService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            proidList = new ArrayList<String>();
            List<BdcYg> ygList = bdcYgService.getBdcYgList(project.getBdcdyh(), Constants.QLLX_QSZT_XS.toString());
            if (CollectionUtils.isNotEmpty(ygList)&&ygList.size() == 2) {
                Map<String, BdcYg> mapYgList = new HashMap<String, BdcYg>();
                for (BdcYg bdcYg : ygList) {
                    mapYgList.put(bdcYg.getYgdjzl(), bdcYg);
                }
                if (mapYgList.containsKey(Constants.YGDJZL_YGSPF) && mapYgList.containsKey(Constants.YGDJZL_YGSPFDY)) {
                    proidList.add(project.getProid());
                }
            }else{
                List<GdYg> gdYgList = gdYgService.getGdygListByBdcdyh(project.getBdcdyh());
                if(CollectionUtils.isNotEmpty(gdYgList)&&gdYgList.size() == 2) {
                    Map<String, GdYg> mapYgList = new HashMap<String, GdYg>();
                    for (GdYg gdYg : gdYgList) {
                        mapYgList.put(gdYg.getYgdjzl(), gdYg);
                    }
                    if (mapYgList.containsKey(Constants.YGDJZL_YGSPF_MC) && mapYgList.containsKey(Constants.YGDJZL_YGSPFDYAQ_MC)) {
                        proidList.add(project.getProid());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList.get(0));
        }else {
            map.put("info", null);
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
        return "118";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否办理过预告登记,预告抵押权登记";
    }
}
