package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
import cn.gtmap.estateplat.server.core.service.GdYgService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
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
 * @description 不动产单元是否存在预告登记
 */
public class BdcdySfYgValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

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
            if (CollectionUtils.isNotEmpty(ygList)) {
                for (BdcYg bdcYg : ygList) {
                    if (bdcYg != null &&!StringUtils.equals(bdcYg.getProid(),project.getProid())&&CommonUtil.indexOfStrs(Constants.YG_ZHUANXIAN, bdcYg.getYgdjzl())) {
                        proidList.add(bdcYg.getProid());
                    }
                }
            }else{
                List<GdYg> gdYgList = gdYgService.getGdygListByBdcdyh(project.getBdcdyh());
                if(CollectionUtils.isNotEmpty(gdYgList)){
                    for (GdYg gdYg : gdYgList) {
                        if (gdYg != null&&StringUtils.equals(StringUtils.deleteWhitespace(gdYg.getYgdjzl()),Constants.YGDJZL_YGSPF_MC)) {
                            proidList.add(gdYg.getProid());
                        }
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
        return "103";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否存在预告登记";
    }
}
