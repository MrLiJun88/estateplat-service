package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created jiangganzhi on 2017/3/2.
 */
@Repository
public class BdcComplexFgHbHzServiceImpl implements BdcComplexFgHbHzService{
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcBdcdyMapper bdcBdcdyMapper;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private QllxParentService qllxParentService;

    private static final String PARAMETER_DEFAULT_BDCDYH = "W00000000";


    @Override
    public String isTdFgHbHzDj(String wiid){
        String sqlxdm = "";
        String isTdFgHbHzDj = "false";
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
        if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
            sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            if(StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm,Constants.SQLX_TDFGHBHZ_DM)){
                    isTdFgHbHzDj = "true";
            }
        }
        return  isTdFgHbHzDj;
    }

    @Override
    public String getSytdProid(String bdcdyid,String wiid){
        String sytdProid = "";
        if(StringUtils.isNotBlank(bdcdyid) && StringUtils.isNotBlank(wiid)){
            HashMap map = new HashMap();
            map.put("wiid", wiid);
            map.put(ParamsConstants.BDCDYID_LOWERCASE,bdcdyid);
            map.put("sqlx",Constants.SQLX_HZ_DM);
            List<BdcXm> bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                sytdProid =bdcXmList.get(0).getProid();
            }
        }
        return  sytdProid;
    }


    @Override
    public String getYzdProidByBdcdyid(String bdcdyid){
        String yZdProid = "";

        Example example = new Example(BdcBdcdy.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
        List<BdcBdcdy> bdcBdcdyList = entityMapper.selectByExample(BdcBdcdy.class, example);

        if(CollectionUtils.isNotEmpty(bdcBdcdyList)){
            String zddDjh = StringUtils.substring(bdcBdcdyList.get(0).getBdcdyh(), 0, 19);
            HashMap hashMap = new HashMap();
            hashMap.put("djh", zddDjh);
            List<String> yZdDjhList = bdcBdcdyMapper.getYdjhByDjh(hashMap);
            List<BdcJsydzjdsyq> bdcJsydzjdsyqList = null;
            String yZdBdcdyid = "";
            for (String yZdDjh : yZdDjhList) {
                String yZdBdcdyh = yZdDjh + PARAMETER_DEFAULT_BDCDYH;
                if (StringUtils.isNotBlank(yZdBdcdyh)) {
                        yZdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(yZdBdcdyh);
                        if (StringUtils.isNotBlank(yZdBdcdyid)) {
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCDYID_LOWERCASE, yZdBdcdyid);
                            map.put("qszt", 1);
                            bdcJsydzjdsyqList = bdcJsydzjdsyqService.getBdcJsydzjdsyqList(map);
                            if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                                yZdProid = bdcJsydzjdsyqList.get(0).getProid();
                                break;
                            }
                        }
                }
            }
        }
        return yZdProid;
    }

    @Override
    public void saveYbdcqzhToDybdcxm(String wiid){
        if(StringUtils.isNotBlank(wiid)){
            String syZdProid ="";
            String isTdFgHbHzDj = isTdFgHbHzDj(wiid);
            if(StringUtils.isNotBlank(isTdFgHbHzDj) && StringUtils.equals(isTdFgHbHzDj, "true")){
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                for(BdcXm syZdbdcXm : bdcXmList){
                    if(StringUtils.isNotBlank(syZdbdcXm.getBdcdyid())){
                        syZdProid = getSytdProid(syZdbdcXm.getBdcdyid(),wiid);
                        if(StringUtils.isNotBlank(syZdProid)){
                            break;
                        }
                    }
                }
                List<BdcZs> syzdBdcZsList = bdcZsService.queryBdcZsByProid(syZdProid);
                if(CollectionUtils.isNotEmpty(syzdBdcZsList)){
                    String ybdcqzh = syzdBdcZsList.get(0).getBdcqzh();
                    if(StringUtils.isNotBlank(ybdcqzh)){
                        for(BdcXm dyBdcxm : bdcXmList){
                            if(StringUtils.isNotBlank(dyBdcxm.getQllx()) && StringUtils.equals(dyBdcxm.getQllx(),Constants.QLLX_DYAQ)){
                                dyBdcxm.setYbdcqzh(ybdcqzh);
                                entityMapper.saveOrUpdate(dyBdcxm,dyBdcxm.getProid());
                            }
                        }
                    }
                }

                //原宗地抵押权利注销
                Example example = new Example(BdcSpxx.class);
                example.createCriteria().andEqualTo("proid", syZdProid);
                List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(BdcSpxx.class, example);
                if(CollectionUtils.isNotEmpty(bdcSpxxList)){
                    String syzdBdcdyh = bdcSpxxList.get(0).getBdcdyh();
                    if(StringUtils.isNotBlank(syzdBdcdyh)){
                        String zddDjh = StringUtils.substring(syzdBdcdyh, 0, 19);
                        HashMap hashMap = new HashMap();
                        hashMap.put("djh", zddDjh);
                        List<String> yZdDjhList = bdcBdcdyMapper.getYdjhByDjh(hashMap);
                        String yZdBdcdyid = "";
                        for (String yZdDjh : yZdDjhList) {
                            String yZdBdcdyh = yZdDjh + PARAMETER_DEFAULT_BDCDYH;
                            if (StringUtils.isNotBlank(yZdBdcdyh)) {
                                yZdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(yZdBdcdyh);
                                if (StringUtils.isNotBlank(yZdBdcdyid)) {
                                    HashMap map = new HashMap();
                                    map.put(ParamsConstants.BDCDYID_LOWERCASE, yZdBdcdyid);
                                    map.put("qszt", 1);
                                    List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                                    if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                        for (BdcDyaq bdcdyaq : bdcDyaqList) {
                                            bdcdyaq.setQszt(Constants.QLLX_QSZT_HR);
                                            entityMapper.saveOrUpdate(bdcdyaq,bdcdyaq.getQlid());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    public void changeYzdDyQszt(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            String syZdProid = "";
            String isTdFgHbHzDj = isTdFgHbHzDj(wiid);
            if (StringUtils.isNotBlank(isTdFgHbHzDj) && StringUtils.equals(isTdFgHbHzDj, "true")) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                if(CollectionUtils.isNotEmpty(bdcXmList)){
                    for (BdcXm syZdbdcXm : bdcXmList) {
                        if (StringUtils.isNotBlank(syZdbdcXm.getBdcdyid())) {
                            syZdProid = getSytdProid(syZdbdcXm.getBdcdyid(), wiid);
                            if (StringUtils.isNotBlank(syZdProid)) {
                                break;
                            }
                        }
                    }
                }

                //原宗地抵押权利还原
                Example example = new Example(BdcSpxx.class);
                example.createCriteria().andEqualTo("proid", syZdProid);
                List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(BdcSpxx.class, example);
                if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
                    String syzdBdcdyh = bdcSpxxList.get(0).getBdcdyh();
                    if (StringUtils.isNotBlank(syzdBdcdyh)) {
                        String zddDjh = StringUtils.substring(syzdBdcdyh, 0, 19);
                        HashMap hashMap = new HashMap();
                        hashMap.put("djh", zddDjh);
                        List<String> yZdDjhList = bdcBdcdyMapper.getYdjhByDjh(hashMap);
                        String yZdBdcdyid = "";
                        for (String yZdDjh : yZdDjhList) {
                            String yZdBdcdyh = yZdDjh + PARAMETER_DEFAULT_BDCDYH;
                            if (StringUtils.isNotBlank(yZdBdcdyh)) {
                                yZdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(yZdBdcdyh);
                                if (StringUtils.isNotBlank(yZdBdcdyid)) {
                                    HashMap map = new HashMap();
                                    map.put(ParamsConstants.BDCDYID_LOWERCASE, yZdBdcdyid);
                                    map.put("qszt", 2);
                                    List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                                    if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                        for (BdcDyaq bdcdyaq : bdcDyaqList) {
                                            bdcdyaq.setQszt(Constants.QLLX_QSZT_XS);
                                            entityMapper.saveOrUpdate(bdcdyaq, bdcdyaq.getQlid());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public List<QllxParent> getYzdCf(String bdcdyh){
        List<QllxParent> list = null;
            if(StringUtils.isNotBlank(bdcdyh)){
                String zddDjh = StringUtils.substring(bdcdyh, 0, 19);
                HashMap hashMap = new HashMap();
                hashMap.put("djh", zddDjh);
                List<String> yZdDjhList = bdcBdcdyMapper.getYdjhByDjh(hashMap);
                if(CollectionUtils.isNotEmpty(yZdDjhList)){
                    String yZdBdcdyh = "";
                    for(String yZdDjh : yZdDjhList){
                        yZdBdcdyh = yZdDjh + PARAMETER_DEFAULT_BDCDYH;
                        list = qllxParentService.queryLogcfQllxVo(new BdcCf(), yZdBdcdyh, "", "false");
                        if(CollectionUtils.isNotEmpty(list))
                            break;
                    }
                }
            }
        return  list;
    }
}
