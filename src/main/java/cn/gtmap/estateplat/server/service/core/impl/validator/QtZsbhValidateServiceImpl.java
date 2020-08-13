package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.BdcZsbh;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.BdcZsbhService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
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
 * @description 验证证书编号填写是否正确
 */
public class QtZsbhValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcZsbhService bdcZsbhService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        boolean isadd = true;
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getWiid())) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 先根据wiid获取所有的项目，获取所有的证书
             */
            List<BdcXm> bdcXmList = new ArrayList<BdcXm>();
            // 变更和转移合并登记、转移和其他转移合并登记需进行排序
            String sqlxdm = "";
            if (StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            // 变更和转移合并登记、转移和其他转移合并登记只验证后一个项目
            if (StringUtils.equals(sqlxdm, Constants.SQLX_BGZY_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_ZYQTZY_DM)) {
                Example example = new Example(BdcXm.class);
                example.createCriteria().andEqualTo("wiid", project.getWiid());
                example.setOrderByClause("cjsj");
                List<BdcXm> bdcXmListTemp = entityMapper.selectByExample(BdcXm.class, example);
                if (CollectionUtils.isNotEmpty(bdcXmListTemp) && bdcXmListTemp.size() > 1) {
                    bdcXmList.add(bdcXmListTemp.get(1));
                }
            } else {
                bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            }
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
                for (BdcXm bdcXm : bdcXmList) {
                    List<BdcZs> bdcZsListTemp = bdcZsService.getPlZsByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcZsListTemp)) {
                        bdcZsList.addAll(bdcZsListTemp);
                    }
                }
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    for (BdcZs bdcZs : bdcZsList) {
                        /**
                         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                         * @description 证书编号为空直接弹出验证失败
                         */
                        if (StringUtils.isBlank(bdcZs.getBh())) {
                            isadd = false;
                            break;
                        }
                        /**
                         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                         * @description 根据证书id获取所有的证书编号（可能是点击按钮获取、可能是自己手填的）
                         */
                        HashMap zsBhMap = new HashMap();
                        zsBhMap.put("zsid", bdcZs.getZsid());
                        if (StringUtils.equals(bdcZs.getZstype(), Constants.BDCQZS_BH_FONT))
                            zsBhMap.put("zslx", Constants.BDCQZS_BH_DM);
                        else if (StringUtils.equals(bdcZs.getZstype(), Constants.BDCQZM_BH_FONT))
                            zsBhMap.put("zslx", Constants.BDCQZM_BH_DM);
                        List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(zsBhMap);
                        if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
                            for (BdcZsbh bdcZsbh : bdcZsbhList) {
                                if (StringUtils.equals(bdcZsbh.getZsbh(), bdcZs.getBh())&&StringUtils.equals(bdcZsbh.getSyqk(), Constants.BDCZSBH_SYQK_ZF)) {
                                    isadd = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (isadd == false) {
            proidList = new ArrayList<String>();
            proidList.add(project.getProid());
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
        return "906";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证证书编号填写是否正确";
    }
}
