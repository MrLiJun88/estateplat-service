package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBydjdjd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.mapper.BdcBydjdjdMapper;
import cn.gtmap.estateplat.server.core.service.*;
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
 * @version 1.0, 2016/11/12
 * @description
 */
@Service
public class BdcBydjServiceImpl implements BdcBydjService {
    @Autowired
    SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    BdcZdGlService bdcZdGlService;
    @Autowired
    BdcBydjdjdMapper bdcBydjdjdMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcLshService bdcLshService;


    @Override
    public synchronized void creatBdcBydjdjd(BdcXm bdcXm,String userid) {
        HashMap map = new HashMap();
        map.put("proid",bdcXm.getProid());
        List<BdcBydjdjd> bdcBydjdjdList = bdcBydjdjdMapper.getBdcBydjdjd(map);
        if(CollectionUtils.isNotEmpty(bdcBydjdjdList)){
            BdcBydjdjd bdcBydjdjd = bdcBydjdjdList.get(0);
            String nf = CalendarUtil.getCurrYear();
            String qh = bdcLshService.getQh(userid);
            String lsh = bdcLshService.getLsh(Constants.BHLX_BYDJ_MC,nf,qh);
            bdcBydjdjd.setNf(nf);
            bdcBydjdjd.setLsh(lsh);
            String bh =  bdcLshService.getBh(Constants.BHLX_BYDJ_MC,nf, qh, lsh);
            bdcBydjdjd.setBh(bh);
            bdcBydjdjd.setSqlxdm(getBdcBydjdjdSqlxdm(bdcXm));
            entityMapper.saveOrUpdate(bdcBydjdjd,bdcBydjdjd.getJdsid());

        }else{
            BdcBydjdjd bdcBydjdjd = new BdcBydjdjd();
            bdcBydjdjd.setJdsid(UUIDGenerator.generate18());
            bdcBydjdjd.setProid(bdcXm.getProid());
            String nf = CalendarUtil.getCurrYear();
            String qh = bdcLshService.getQh(userid);
            String lsh = bdcLshService.getLsh(Constants.BHLX_BYDJ_MC,nf,qh);
            bdcBydjdjd.setNf(nf);
            bdcBydjdjd.setLsh(lsh);
            String bh =  bdcLshService.getBh(Constants.BHLX_BYDJ_MC,nf, qh, lsh);
            bdcBydjdjd.setBh(bh);
            bdcBydjdjd.setSqlxdm(getBdcBydjdjdSqlxdm(bdcXm));
            bdcBydjdjd.setFjdssj(new Date());
            entityMapper.saveOrUpdate(bdcBydjdjd,bdcBydjdjd.getJdsid());
        }

    }

    @Override
    public void deleteBdcBydjdjd(BdcXm bdcXm) {
        if(bdcXm != null){
            Example bdcBydjdjdExample = new Example(BdcBydjdjd.class);
            bdcBydjdjdExample.createCriteria().andEqualTo("proid", bdcXm.getProid());
            entityMapper.deleteByExample(bdcBydjdjdExample);
        }
    }



    @Override
    public Integer getMaxLsh(HashMap map) {
        return bdcBydjdjdMapper.getMaxLsh(map);
    }


    @Override
    public List<BdcBydjdjd> getBdcBydjdjd(HashMap map) {
        List<BdcBydjdjd> bdcBydjdjdList = null;
        bdcBydjdjdList = bdcBydjdjdMapper.getBdcBydjdjd(map);
        return bdcBydjdjdList;
    }

    private String getBdcBydjdjdSqlxdm(BdcXm bdcXm){
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
