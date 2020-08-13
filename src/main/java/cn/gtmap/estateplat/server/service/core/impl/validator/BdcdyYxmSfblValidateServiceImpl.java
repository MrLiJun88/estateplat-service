package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
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
 * @description 验证原项目是否正在办理
 */
public class BdcdyYxmSfblValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isNotBlank(project.getYxmid())) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if(!StringUtils.equals(bdcXmRel.getYproid(),project.getYxmid())){
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXmRel.getYproid());
                        if (bdcBdcdy != null) {
                            HashMap mapTemp = new HashMap();
                            mapTemp.put("yproid", bdcXmRel.getYproid());
                            mapTemp.put("bdcdyh", bdcBdcdy.getBdcdyh());
                            List<BdcXmRel> list = bdcXmRelService.getBdcXmRelByYproidAndBdcdyh(mapTemp);
                            if (CollectionUtils.isNotEmpty(list)) {
                                for (BdcXmRel bdcXmRel1 : list) {
                                    //zdd 排除当前项目
                                    if (!bdcXmRel1.getProid().equals(project.getProid())) {
                                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel1.getProid());
                                        if (bdcXm != null && !bdcXm.getWiid().equals(project.getWiid()) && StringUtils.isNotBlank(bdcXm.getXmzt()) && !bdcXm.getXmzt().equals(Constants.XMZT_BJ)) {
                                            proidList.add(bdcXm.getProid());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            HashMap mapTemp = new HashMap();
            mapTemp.put("yproid", project.getYxmid());
            mapTemp.put("bdcdyh", project.getBdcdyh());
            List<BdcXmRel> list = bdcXmRelService.getBdcXmRelByYproidAndBdcdyh(mapTemp);
            if (CollectionUtils.isNotEmpty(list)) {
                for (BdcXmRel bdcXmRel : list) {
                    //zdd 排除当前项目
                    if (!bdcXmRel.getProid().equals(project.getProid())) {
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                        if (bdcXm != null && !bdcXm.getWiid().equals(project.getWiid()) && StringUtils.isNotBlank(bdcXm.getXmzt()) && !bdcXm.getXmzt().equals(Constants.XMZT_BJ)) {
                            proidList.add(bdcXm.getProid());
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList.get(0));
        } else {
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
        return "117";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证原项目是否正在办理";
    }
}
