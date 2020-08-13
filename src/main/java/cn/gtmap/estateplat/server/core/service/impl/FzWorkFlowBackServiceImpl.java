package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.FzWorkFlowBackService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/3/7
 */
@Service
public class FzWorkFlowBackServiceImpl implements FzWorkFlowBackService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    SysTaskService sysTaskService;
    @Autowired
    BdcZsService bdcZsService;

    @Override
    public void fzWorkFlowBack(final String proid,final String userid,final String activityid,final String targetActivityDefids) {
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        PfActivityVo backActivitys =null;
        if(pfWorkFlowInstanceVo!=null){
            backActivitys = sysTaskService.getActivityBywIdandadId(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
        }
        if(backActivitys!=null && StringUtils.isNotBlank(backActivitys.getActivityName())&&!"缮证".equals(backActivitys.getActivityName())) {
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                if(CommonUtil.indexOfStrs(Constants.BATCH_OPERATION_SQLX_DM,bdcXm.getSqlx())){
                    bdcZsService.batchDelBdcZs(bdcXm.getWiid());
                }else{
                    Example example = new Example(BdcXm.class);
                    example.createCriteria().andEqualTo("wiid", bdcXm.getWiid());
                    List<BdcXm> xmList = entityMapper.selectByExample(example);
                    for (BdcXm xm : xmList) {
                        bdcZsService.delBdcZsByProid(xm.getProid());
                    }
                }

            } else {
                bdcZsService.delBdcZsByProid(proid);
            }
        }
    }
}
