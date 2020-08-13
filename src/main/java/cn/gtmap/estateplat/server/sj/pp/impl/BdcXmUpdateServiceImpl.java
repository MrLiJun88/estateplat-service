package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcTdsyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcPpgxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.sj.pp.BdcdyPicUpdateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/26
 * @description 匹配不动产单元 更新bdc_xm表数据
 */
@Service
public class BdcXmUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcDjsjService djsjService;
    @Autowired
    private BdcPpgxService bdcPpgxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, fwBdcdyid);
            List<BdcXm> bdcXmList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcXm> updateBdcXmList = new ArrayList<>();
                for (BdcXm bdcXm : bdcXmList) {
                    bdcXm.setBdcdyid(bdcdyid);
                    bdcXm.setBdcdyh(bdcdyh);
                    updateBdcXmList.add(bdcXm);
                    bdcPpgxService.saveBdcPpgxXmByMap(paramMap, bdcXm.getProid());

                    //更新remark字段
                    if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                        if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getRemark())
                                && pfWorkFlowInstanceVo.getRemark().contains(fwBdcdyh)) {
                            String newRemark = pfWorkFlowInstanceVo.getRemark().replaceAll(fwBdcdyh, bdcdyh);
                            pfWorkFlowInstanceVo.setRemark(newRemark);
                            sysWorkFlowInstanceService.updateWorkFlowInstanceRemark(pfWorkFlowInstanceVo);
                        }
                    }
                }
                entityMapper.batchSaveSelective(updateBdcXmList);
            }
            boolean sfYdhDfw = bdcdyService.sfYdhDfw(bdcdyh);
            if (sfYdhDfw) {
                Example bdcXmExample = new Example(BdcXm.class);
                bdcXmExample.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                List<BdcXm> bdcXms = entityMapper.selectByExample(bdcXmExample);
                if (CollectionUtils.isNotEmpty(bdcXms)) {
                    List<BdcXm> updateBdcXmList = new ArrayList<>();
                    for (BdcXm bdcXm : bdcXms) {
                        bdcXm.setSfydydf("1");
                        updateBdcXmList.add(bdcXm);
                    }
                    entityMapper.batchSaveSelective(updateBdcXmList);
                }
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, tdBdcdyid);
            List<BdcXm> bdcXmList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcXm> updateBdcXmList = new ArrayList<>();
                for (BdcXm bdcXm : bdcXmList) {
                    String bdcdybh = null;
                    bdcXm.setBdcdyid(bdcdyid);
                    bdcXm.setBdcdyh(bdcdyh);
                    if (StringUtils.contains(bdcdyh, Constants.DZWTZM_W)) {
                        if (bdcdyh.length() > 19) {
                            if (StringUtils.isBlank(bdcdybh)) {
                                bdcdybh = bdcdyh.substring(0, 19);
                            } else {
                                bdcdybh = bdcdybh + "," + bdcdyh.substring(0, 19);
                            }
                            bdcXm.setBdcdybh(bdcdybh);
                        }
                    }
                    updateBdcXmList.add(bdcXm);
                    bdcPpgxService.saveBdcPpgxXmByMap(paramMap, bdcXm.getProid());

                    //更新remark字段
                    if (StringUtils.isNotBlank(bdcXm.getWiid()) && StringUtils.isBlank(bdcXm.getSfcf())) {
                        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                        if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getRemark())
                                && pfWorkFlowInstanceVo.getRemark().contains(tdBdcdyh)) {
                            String newRemark = pfWorkFlowInstanceVo.getRemark().replaceAll(tdBdcdyh, bdcdyh);
                            pfWorkFlowInstanceVo.setRemark(newRemark);
                            sysWorkFlowInstanceService.updateWorkFlowInstanceRemark(pfWorkFlowInstanceVo);
                        }
                    }
                }
                entityMapper.batchSaveSelective(updateBdcXmList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            bdcXm.setBdcdyid(bdcdyid);
            bdcXm.setBdcdyh(bdcdyh);
            bdcXm.setBdcdybh(StringUtils.substring(bdcdyh, 0, 19));
            entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());

            String xbdcdyh = bdcdyService.getBdcdyhByProid(proid);
            //更新remark字段
            if (StringUtils.isNotBlank(bdcXm.getWiid()) && StringUtils.isBlank(bdcXm.getSfcf())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getRemark())
                        && pfWorkFlowInstanceVo.getRemark().contains(xbdcdyh)) {
                    String newRemark = pfWorkFlowInstanceVo.getRemark().replaceAll(xbdcdyh, bdcdyh);
                    pfWorkFlowInstanceVo.setRemark(newRemark);
                    sysWorkFlowInstanceService.updateWorkFlowInstanceRemark(pfWorkFlowInstanceVo);
                }
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_xm";
    }
}
