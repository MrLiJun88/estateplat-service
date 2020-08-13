package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证创建抵押转移合并(原证书不能是抵押状态)
 */
public class BdcdyCreatZydyValidateServiceImpl implements ProjectValidateService {
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
    private GdFwService gdFwService;
    @Autowired
    private BdcXmService bdcXmService;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            //查找正式库的抵押数据
            List<QllxParent> bdcDyaqList = null;
            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                HashMap queryMap = new HashMap();
                queryMap.put("bdcdyh", project.getBdcdyh());
                queryMap.put("qszt", "1");
                bdcDyaqList = qllxParentService.queryQllxVo(new BdcDyaq(), queryMap);
                removeBdcDyaq(bdcDyaqList, project);
            }
            //查过过渡库的抵押数据
            List<GdDy> gdDyList = null;
            if (StringUtils.isNotBlank(project.getGdproid()) || StringUtils.isNotBlank(project.getYxmid())) {
                List<GdFwsyq> gdFwsyqList = gdFwService.queryGdFwsyqByGdproid(project.getGdproid());
                if (CollectionUtils.isEmpty(gdFwsyqList)) {
                    gdFwsyqList = gdFwService.queryGdFwsyqByGdproid(project.getYxmid());
                }
                if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                    gdDyList = new ArrayList<GdDy>();
                    if (StringUtils.isNotBlank(gdFwsyqList.get(0).getQlid())) {
                        List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid("", gdFwsyqList.get(0).getQlid());
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                            for(GdBdcQlRel gdBdcQlRel: gdBdcQlRelList) {
                                List<GdBdcQlRel> gdBdcQlRelListTemp = gdFwService.getGdBdcQlRelByBdcidOrQlid(gdBdcQlRel.getBdcid(), "");
                                if (CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)) {
                                    for (GdBdcQlRel gdBdcQlRelTemp : gdBdcQlRelListTemp) {
                                        GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRelTemp.getQlid(), Constants.GDQL_ISZX_WZX);
                                        if (gdDy != null) {
                                            gdDyList.add(gdDy);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(bdcDyaqList) || CollectionUtils.isNotEmpty(gdDyList)) {
                proidList = new ArrayList<String>();
                if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                    proidList.add(bdcDyaqList.get(0).getProid());
                } else {
                    proidList.add("false");
                }
                map.put("info", proidList);
            }
        }
        return map;
    }

    /**
     * 去除合并流程中创建的抵押
     */
    public void removeBdcDyaq(List<QllxParent> bdcDyaqList,Project project){
        if(CollectionUtils.isNotEmpty(bdcDyaqList)){
            Iterator iterator=bdcDyaqList.iterator();
            while(iterator.hasNext()){
                QllxParent qllxParent=(QllxParent)iterator.next();
                BdcXm bdcXm=bdcXmService.getBdcXmByProid(qllxParent.getProid());
                if(null!=bdcXm&&StringUtils.equals(bdcXm.getWiid(),project.getWiid())){
                    iterator.remove();
                }
            }
        }
    }
    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "113";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证创建抵押转移合并(原证书不能是抵押状态)";
    }
}
