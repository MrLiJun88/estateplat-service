package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 更正登记办结服务  与转移登记逻辑一致
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-8
 */
public class EndProjectGzdjServiceImpl extends EndProjectZydjServiceImpl {

    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private GdFwService gdFwService;

    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
//            changeYgQszt(bdcXm);
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(bdcXmRel.getYqlid());
                List<GdTdsyq> gdTdsyqList=gdFwService.queryTdsyqByQlid(bdcXmRel.getYqlid());
                if(gdFwsyq!=null){
                    if(qllxVo instanceof BdcFdcq ||qllxVo instanceof BdcFdcqDz){
                        super.changeGdsjQszt(bdcXmRel, 1);
                    }
                }else if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                    if(qllxVo instanceof BdcJsydzjdsyq){
                        super.changeGdsjQszt(bdcXmRel, 1);
                    }
                }else {
                    super.changeGdsjQszt(bdcXmRel, 1);
                }
            }

            if(qllxVo instanceof BdcDyaq) {
                bdcXmRelService.completeDybgBdcXmRelByWiid(bdcXm.getWiid());
            }
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">qijiadong</a>
             *@description 只有合并登记才会走下面这块，其他流程执行下面的代码后会将xmrel中原抵押项目proid置空。
             */
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                String sqlxdm = "";
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if(pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
                if (StringUtils.isNotBlank(sqlxdm) && CommonUtil.indexOfStrs(Constants.SQLX_hblc_zlc, sqlxdm) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                    //lx 变更登记办结时走转移登记流程，合并流程中抵押权需要将其xmrel表中yproid换成新的所有权的proid
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    List<BdcXm> bdcDyaXmList = new ArrayList<BdcXm>();
                    List<BdcXm> bdcCqXmList = new ArrayList<BdcXm>();
                    for (BdcXm bdcXmTemp :bdcXmList) {
                        if (StringUtils.isNotBlank(bdcXmTemp.getQllx()) && StringUtils.equals(bdcXmTemp.getQllx(),"18")) {
                            bdcDyaXmList.add(bdcXmTemp);
                        } else {
                            bdcCqXmList.add(bdcXmTemp);
                        }
                    }
                    String yproid = bdcCqXmList.get(0).getProid();
                    for (BdcXm bdcDyaXm : bdcDyaXmList) {
                        List<BdcXmRel> bdcDyaXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcDyaXm.getProid());
                        BdcXmRel bdcXmRel = bdcDyaXmRelList.get(0);
                        bdcXmRel.setYproid(yproid);
                        entityMapper.saveOrUpdate(bdcXmRel,bdcXmRel.getRelid());
                    }
                }
            }

            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }
    }
}
