package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBzcltzs;
import cn.gtmap.estateplat.model.server.core.BdcSjxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcBqbzService;
import cn.gtmap.estateplat.server.core.service.BdcLshService;
import cn.gtmap.estateplat.server.core.service.BdcSjxxService;
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
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/1/19
 * @description
 */
@Service
public class BdcBqbzServiceImpl implements BdcBqbzService {

    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcSjxxService bdcSjxxService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcLshService bdcLshService;


    @Override
    public synchronized void creatBdcBzcltzs(BdcXm bdcXm, String userid) {
        if(bdcXm != null){
            Example bdcBzcltzsExample = new Example(BdcBzcltzs.class);
            bdcBzcltzsExample.createCriteria().andEqualTo("proid", bdcXm.getProid());
            List<BdcBzcltzs> bdcBzcltzsList= entityMapper.selectByExample(bdcBzcltzsExample);
            if(CollectionUtils.isNotEmpty(bdcBzcltzsList)){
                BdcBzcltzs bdcBzcltzs = bdcBzcltzsList.get(0);
                String nf = CalendarUtil.getCurrYear();
                String qh = bdcLshService.getQh(userid);
                String lsh = bdcLshService.getLsh(Constants.BHLX_BQBZ_MC,nf, qh);
                String bh =  bdcLshService.getBh(Constants.BHLX_BQBZ_MC,nf, qh, lsh);
                bdcBzcltzs.setBh(bh);
                bdcBzcltzs.setProid(bdcXm.getProid());
                bdcBzcltzs.setSlbh(bdcXm.getBh());
                bdcBzcltzs.setSqlxdm(getBdcBzcltzsSqlxdm(bdcXm));
                entityMapper.saveOrUpdate(bdcBzcltzs, bdcBzcltzs.getTzsid());

            }else{
                BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByWiid(bdcXm.getWiid());
                BdcBzcltzs bdcBzcltzs = new BdcBzcltzs();
                bdcBzcltzs.setTzsid(UUIDGenerator.generate18());

                String nf = CalendarUtil.getCurrYear();
                String qh = bdcLshService.getQh(userid);
                String lsh = bdcLshService.getLsh(Constants.BHLX_BQBZ_MC,nf, qh);
                String bh =  bdcLshService.getBh(Constants.BHLX_BQBZ_MC,nf, qh, lsh);
                bdcBzcltzs.setBh(bh);
                bdcBzcltzs.setProid(bdcXm.getProid());
                bdcBzcltzs.setSlbh(bdcXm.getBh());
                bdcBzcltzs.setSqlxdm(getBdcBzcltzsSqlxdm(bdcXm));
                if(bdcSjxx != null && StringUtils.isNotBlank(bdcSjxx.getSjr())) {
                    bdcBzcltzs.setSjr(bdcSjxx.getSjr());
                }
                bdcBzcltzs.setFtzsj(new Date());
                entityMapper.saveOrUpdate(bdcBzcltzs, bdcBzcltzs.getTzsid());
            }
        }
    }

    @Override
    public void deleteBdcBzcltzs(BdcXm bdcXm) {
        if(bdcXm != null){
            Example bdcBzcltzsExample = new Example(BdcBzcltzs.class);
            bdcBzcltzsExample.createCriteria().andEqualTo("proid", bdcXm.getProid());
            entityMapper.deleteByExample(bdcBzcltzsExample);
        }

    }


    private String getBdcBzcltzsSqlxdm(BdcXm bdcXm){
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
