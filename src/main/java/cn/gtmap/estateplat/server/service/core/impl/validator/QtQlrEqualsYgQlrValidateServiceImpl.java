package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcYg;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
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
 * @description 验证不动产单元权利人是否是预告权利人
 */
public class QtQlrEqualsYgQlrValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcYgService bdcYgService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 判断是否是抵押，如果是抵押预告登记种类是抵押类的2种
             */
            String ygdjzl = "";
            if (StringUtils.equals(project.getDjlx(), Constants.DJLX_DYDJ_DM))
                ygdjzl = Constants.YGDJZL_DY;
            else
                ygdjzl = Constants.YGDJZL_MM;
            List<BdcYg> ygList = bdcYgService.getBdcYgList(project.getBdcdyh(), Constants.QLLX_QSZT_XS.toString(), ygdjzl);
            if (CollectionUtils.isNotEmpty(ygList)) {
                BdcYg bdcYg = ygList.get(0);
                if (bdcYg != null && StringUtils.isNotBlank(bdcYg.getProid())) {
                    List<BdcQlr> bdcYgQlrList = bdcQlrService.queryBdcQlrByProid(bdcYg.getProid());
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
                    if (CollectionUtils.isNotEmpty(bdcYgQlrList)&&CollectionUtils.isNotEmpty(bdcQlrList)) {
                        boolean equalQlr = true;
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            boolean equalOneQlr = false;
                            for (BdcQlr bdcYgQlr : bdcYgQlrList) {
                                if (StringUtils.equals(bdcQlr.getQlrmc(), bdcYgQlr.getQlrmc()) && StringUtils.equals(bdcQlr.getQlrzjh(), bdcYgQlr.getQlrzjh())) {
                                    equalOneQlr = true;
                                    break;
                                }
                            }
                            if (!equalOneQlr)
                                equalQlr = false;
                        }
                        if (!equalQlr) {
                            proidList = new ArrayList<String>();
                            proidList.add(project.getProid());
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
        return "904";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元权利人是否是预告权利人";
    }
}
