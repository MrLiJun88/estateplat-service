package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmcqRel;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.mapper.BdcCfMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
 * @Time 2020/7/30 10:25
 * @description  首次登记挂接历史预查封
 */
@Service
public class BdcQtLsYcfDqService implements BdcQtDqService {
    @Autowired
    private BdcCfMapper bdcCfMapper;
    @Autowired
    private BdcXmcqRelService bdcXmcqRelService;


    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (projectPar != null && StringUtils.equals(Constants.SQLX_SPFGYSCDJ_DM, projectPar.getSqlx()) && StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(ParamsConstants.BDCDYH_LOWERCASE, projectPar.getBdcdyh());
            paramMap.put("qszt", Constants.QLLX_QSZT_HR);
            paramMap.put("cflx", Constants.CFLX_ZD_YCF);
            List<BdcCf> bdcCfList = bdcCfMapper.getCfByMap(paramMap);
            if (CollectionUtils.isNotEmpty(bdcCfList)) {
                List<BdcXmcqRel> bdcXmcqRelList = Lists.newArrayList();
                for (BdcCf bdcCf : bdcCfList) {
                    BdcXmcqRel bdcXmcqRel = new BdcXmcqRel();
                    bdcXmcqRel.setRelid(UUIDGenerator.generate18());
                    bdcXmcqRel.setProid(projectPar.getProid());
                    bdcXmcqRel.setCqproid(bdcCf.getProid());
                    bdcXmcqRelList.add(bdcXmcqRel);
                }
                bdcXmcqRelService.saveBdcXmcqRel(bdcXmcqRelList);
            }
        }
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "lsycf";
    }
}
