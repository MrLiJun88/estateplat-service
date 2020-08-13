package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcFdcqService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public class BdcdyZjDyZmdValidateServiceImpl implements ProjectValidateService {
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
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
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
            HashMap<String, String> querymap = new HashMap<String, String>();
            List<BdcZjjzwxx> bdcZjjzwxxLst = null;
            if (StringUtils.isNotBlank(xmly) && xmly.equals("1")) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
                if (CollectionUtils.isNotEmpty(xmList)) {
                    for (BdcXm xm : xmList) {
                        if (StringUtils.isNotBlank(xm.getBdcdyid())) {
                            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(xm.getBdcdyid());
                            querymap.put("bdcdyh", bdcBdcdy.getBdcdyh());
                            querymap.put("dyzt", "0");
                            bdcZjjzwxxLst = bdcZjjzwxxService.getZjjzwxx(querymap);
                            if (CollectionUtils.isNotEmpty(bdcZjjzwxxLst)) {
                                //验证在建工程是否发证明单
                                HashMap<String,String> parameter=new HashMap<String,String>();
                                parameter.put("proid",xm.getProid());
                                List<BdcFdcq> fdcqList = bdcFdcqService.getBdcFdcq(parameter);
                                if (CollectionUtils.isNotEmpty(fdcqList)) {
                                    BdcFdcq bdcFdcq = fdcqList.get(0);
                                    if (StringUtils.equals(bdcFdcq.getFzlx(), Constants.FZLX_FZM)) {
                                        break;
                                    } else {
                                        bdcZjjzwxxLst.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(bdcZjjzwxxLst)) {
                proidList = new LinkedList<String>();
                for (BdcZjjzwxx bdcZjjzwxx : bdcZjjzwxxLst) {
                    proidList.add(bdcZjjzwxx.getProid());
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
        return "137";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证存在在建工程抵押的不动产单元办理首次登记信息表";
    }
}
