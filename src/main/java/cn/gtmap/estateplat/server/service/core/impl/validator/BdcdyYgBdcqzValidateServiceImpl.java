package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
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
public class BdcdyYgBdcqzValidateServiceImpl implements ProjectValidateService {
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
    private BdcXmService bdcXmService;
    @Autowired
    private BdcFdcqService bdcFdcqService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
            String xmly = bdcXm.getXmly();
            HashMap<String, Object> querymap = new HashMap<String, Object>();
            List<BdcYg> bdcYgList = null;
            if (StringUtils.isNotBlank(xmly) && xmly.equals("1")) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
                if (CollectionUtils.isNotEmpty(xmList)) {
                    for (BdcXm xm : xmList) {
                        if (StringUtils.isNotBlank(xm.getBdcdyid())) {
                            querymap.put("bdcdyid", xm.getBdcdyid());
                            querymap.put("qszt", Constants.QLLX_QSZT_XS);
                            bdcYgList = bdcYgService.getBdcYgList(querymap);
                            if (CollectionUtils.isNotEmpty(bdcYgList)) {
                                //验证预告是否发不动产权证
                                HashMap<String, String> parameter = new HashMap<String, String>();
                                parameter.put("proid", xm.getProid());
                                List<BdcFdcq> fdcqList = bdcFdcqService.getBdcFdcq(parameter);
                                if (CollectionUtils.isNotEmpty(fdcqList)) {
                                    BdcFdcq bdcFdcq = fdcqList.get(0);
                                    if (StringUtils.equals(bdcFdcq.getFzlx(), Constants.FZLX_FZS)) {
                                        break;
                                    } else {
                                        bdcYgList.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(bdcYgList)) {
                proidList = new LinkedList<String>();
                for (BdcYg bdcYg : bdcYgList) {
                    proidList.add(bdcYg.getProid());
                }
            }
        }
        map.put("info", CollectionUtils.isNotEmpty(proidList) ? proidList : null);
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
        return "138";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证存在预告登记的不动产单元办理不动产权证书";
    }
}
