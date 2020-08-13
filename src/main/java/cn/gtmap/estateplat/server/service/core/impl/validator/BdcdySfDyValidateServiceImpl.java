package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.GdBdcQlRelService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 不动产单元是否存在抵押
 */
public class BdcdySfDyValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        String proid = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            HashMap<String, Object> querymap = new HashMap<String, Object>();
            querymap.put("bdcdyh", project.getBdcdyh());
            List<QllxParent> list = qllxParentService.queryZtzcQllxVo(new BdcDyaq(), querymap);
            if (CollectionUtils.isNotEmpty(list)) {
                String wiid = project.getWiid();
                if(StringUtils.isBlank(project.getWiid()) && StringUtils.isNotBlank(project.getProid())) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
                    if(bdcXm != null) {
                        wiid = bdcXm.getWiid();
                    }
                }
                if(StringUtils.isNotBlank(wiid)) {
                    for(QllxVo qllxVo:list){
                        String qllxVoProid = qllxVo.getProid();
                        if(StringUtils.isNotBlank(qllxVoProid)){
                            BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(qllxVoProid);
                            if(bdcXmTemp != null && !StringUtils.equals(bdcXmTemp.getWiid(),wiid))
                                proid = qllxVoProid;
                        }
                    }
                }else{
                   proid = list.get(0).getProid();
                }
            } else {
                List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.getGdBdcQlRelByBdcdyh(project.getBdcdyh());
                if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                    for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                        GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRel.getQlid(), 0);
                        if (gdDy != null) {
                            proid = gdDy.getProid();
                        }
                    }
                }
            }
        }
        map.put("info", proid);
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
        return "105";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "不动产单元是否存在抵押";
    }
}
