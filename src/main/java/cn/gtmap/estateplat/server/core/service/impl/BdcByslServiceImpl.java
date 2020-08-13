package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBysltzs;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.mapper.BdcByslTzsMapper;
import cn.gtmap.estateplat.server.core.service.BdcByslService;
import cn.gtmap.estateplat.server.core.service.BdcLshService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/11/11
 * @description
 */
@Service
public class BdcByslServiceImpl implements BdcByslService {
    @Autowired
    SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    BdcZdGlService bdcZdGlService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcByslTzsMapper bdcByslTzsMapper;
    @Autowired
    BdcLshService bdcLshService;


    @Override
    public synchronized void creatBdcBysltzs(BdcXm bdcXm,String userid) {
        if(bdcXm != null){
            HashMap map = new HashMap();
            map.put("proid",bdcXm.getProid());
            List<BdcBysltzs> bdcBysltzsList = bdcByslTzsMapper.getBdcBysltzs(map);
            if(CollectionUtils.isNotEmpty(bdcBysltzsList)){
                BdcBysltzs bdcBysltzs = bdcBysltzsList.get(0);
                String nf = CalendarUtil.getCurrYear();
                String qh = bdcLshService.getQh(userid);
                String lsh = bdcLshService.getLsh(Constants.BHLX_BYSL_MC,nf, qh);
                bdcBysltzs.setNf(nf);
                bdcBysltzs.setLsh(lsh);
                String bh =  bdcLshService.getBh(Constants.BHLX_BYSL_MC,nf, qh, lsh);
                bdcBysltzs.setBh(bh);
                bdcBysltzs.setSqlxdm(getBdcBysltzsSqlxdm(bdcXm));
                entityMapper.saveOrUpdate(bdcBysltzs, bdcBysltzs.getTzsid());

            }else{
                BdcBysltzs bdcBysltzs = new BdcBysltzs();
                bdcBysltzs.setTzsid(UUIDGenerator.generate18());

                String nf = CalendarUtil.getCurrYear();
                String qh = bdcLshService.getQh(userid);
                String lsh = bdcLshService.getLsh(Constants.BHLX_BYSL_MC,nf, qh);
                bdcBysltzs.setNf(nf);
                bdcBysltzs.setLsh(lsh);
                String bh =  bdcLshService.getBh(Constants.BHLX_BYSL_MC,nf, qh, lsh);
                bdcBysltzs.setBh(bh);
                bdcBysltzs.setProid(bdcXm.getProid());
                bdcBysltzs.setWiid(bdcXm.getWiid());
                bdcBysltzs.setSqlxdm(getBdcBysltzsSqlxdm(bdcXm));
                bdcBysltzs.setFtzsj(new Date());
                entityMapper.saveOrUpdate(bdcBysltzs, bdcBysltzs.getTzsid());
            }

        }
    }

    @Override
    public void deleteBdcBysltzs(BdcXm bdcXm) {
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
            Example bdcBysltzsExample = new Example(BdcBysltzs.class);
            bdcBysltzsExample.createCriteria().andEqualTo("wiid", bdcXm.getWiid());
            entityMapper.deleteByExample(bdcBysltzsExample);
        }

    }


    @Override
    public Integer getMaxLsh(HashMap map) {
        return bdcByslTzsMapper.getMaxLsh(map);
    }


    @Override
    public List<BdcBysltzs> getBdcBysltzs(HashMap map) {
        List<BdcBysltzs> bdcBysltzsList = null;
        bdcBysltzsList = bdcByslTzsMapper.getBdcBysltzs(map);
        return bdcBysltzsList;
    }

    private String getBdcBysltzsSqlxdm(BdcXm bdcXm){
        String sqlxdm = "";
        //获取平台的申请类型代码,主要为了合并
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        return sqlxdm;
    }
}


