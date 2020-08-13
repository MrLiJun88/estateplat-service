package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.model.server.core.QllxParent;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.core.service.QllxService;
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
 * @description 验证异议是否是同一个异议事项和申请人
 */
public class QtSqrAndYysxValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private QllxParentService qllxParentService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        Map hashMap = new HashMap<String, String>();
        if (project!=null&&StringUtils.isNotBlank(project.getProid())) {
            hashMap.put("proid", project.getProid());
            BdcYy bdcYy = null;
            List<QllxVo> bdcYyList = qllxService.andEqualQueryQllx(new BdcYy(), hashMap);
            if (CollectionUtils.isNotEmpty(bdcYyList))
                bdcYy = (BdcYy) bdcYyList.get(0);
            List<String> qlrList = bdcQlrService.getQlrmcByProid(project.getProid());
            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                HashMap<String, Object> querymap = new HashMap<String, Object>();
                querymap.put("bdcdyh", project.getBdcdyh());
                List<QllxParent> list = qllxParentService.queryZtzcQllxVo(new BdcYy(), querymap);
                if (CollectionUtils.isNotEmpty(list)&&!project.getProid().equals(list.get(0).getProid())) {
                    List<BdcYy> ybdcYyList = new ArrayList<BdcYy>();
                    for (QllxParent qllxParent : list) {
                        BdcYy bdcYyTemp = (BdcYy) qllxService.queryQllxVo(new BdcYy(), qllxParent.getProid());
                        if(null != bdcYyTemp) {
                            ybdcYyList.add(bdcYyTemp);
                        }
                    }
                    for (BdcYy ybdcYy : ybdcYyList) {
                        if (bdcYy!=null&&StringUtils.equals(ybdcYy.getYysx(),bdcYy.getYysx()))
                            continue;
                        String proid = ybdcYy.getProid();
                        List<String> yqlrList = bdcQlrService.getQlrmcByProid(proid);
                        if (CollectionUtils.isNotEmpty(qlrList) && CollectionUtils.isNotEmpty(yqlrList) && qlrList.size() == ybdcYyList.size()) {
                            int i;
                            for (i = 0; i < qlrList.size(); i++) {
                                if (!yqlrList.contains(qlrList.get(i)))
                                    break;
                            }
                            if (i == yqlrList.size())
                                map.put("info", proid);
                        }
                    }
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
        return "905";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证异议是否是同一个异议事项和申请人";
    }
}
