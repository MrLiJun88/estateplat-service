package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.core.mapper.BdcFdcqDzMapper;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房地产权（多幢）
 * User: sc
 * Date: 15-4-17
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcFdcqDzServiceImpl implements BdcFdcqDzService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcFdcqDzMapper bdcFdcqDzMapper;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    @Transactional
    public void saveBdcFdcqDz(BdcFdcqDz bdcFdcqDz, QllxVo qllxParent) {
        if(StringUtils.isBlank(bdcFdcqDz.getQlid()))return;
        List<BdcFwfzxx> bdcFwfzxxList = bdcFdcqDz.getFwfzxxList();
        bdcFdcqDz.setFwfzxxList(null);
        if (qllxParent == null)
            entityMapper.insertSelective(bdcFdcqDz);
        else {
            bdcFdcqDz.setQlid(qllxParent.getQlid());
            entityMapper.updateByPrimaryKeySelective(bdcFdcqDz);
        }
        if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
            bdcFdcqDzMapper.delBdcFwfzxxByQlid(bdcFdcqDz.getQlid());
            for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                entityMapper.insertSelective(bdcFwfzxx);
            }
        }
    }

    @Override
    public void saveBdcFdcqDz(BdcFdcqDz bdcFdcqDz) {
            entityMapper.saveOrUpdate(bdcFdcqDz,bdcFdcqDz.getQlid());
    }

    @Override
    @Transactional
    public void delBdcFdcqDzByProid(final String proid) {
        if(StringUtils.isBlank(proid)) return;
        BdcFdcqDz bdcFdcqDz = bdcFdcqDzMapper.getBdcFdcqDz(proid);
        if (bdcFdcqDz != null) {
            entityMapper.deleteByPrimaryKey(BdcFdcqDz.class, bdcFdcqDz.getQlid());
            bdcFdcqDzMapper.delBdcFwfzxxByQlid(bdcFdcqDz.getQlid());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public BdcFdcqDz getBdcFdcqDz(final String proid) {
        return StringUtils.isNotBlank(proid)?bdcFdcqDzMapper.getBdcFdcqDz(proid):null;
    }

    public BdcFdcqDz getBdcFdcqDzByBdcdyid(final String  bdcdyid){
       return bdcFdcqDzMapper.getBdcFdcqDzByBdcdyid(bdcdyid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcFdcqDz> getBdcFdcqDzList(final Map map){
        return bdcFdcqDzMapper.getBdcFdcqDzList(map);
    }

    @Override
    public BdcFdcqDz reGenerateBdcFdcqDzFromQj(BdcXm bdcXm,BdcFdcqDz bdcFdcqDz){
        if(bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getBdcdyid())){
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 获取qjid
             */
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            if(bdcBdcdy!=null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())){
                String djid = djsjFwService.getDjidByBdcdyh(bdcBdcdy.getBdcdyh());
                if(StringUtils.isNotBlank(djid)){
                    bdcFdcqDz = reGenerateBdcFdcqDz(djid,bdcXm,bdcFdcqDz);
                }
            }
        }
        return bdcFdcqDz;
    }

    public BdcFdcqDz reGenerateBdcFdcqDz(String djid,BdcXm bdcXm,BdcFdcqDz bdcFdcqDz){
        if(StringUtils.isNotBlank(djid)&&bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getProid())) {
            DjsjZdxx djsjZdxx = new DjsjZdxx();
            DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(djid);
            if (djsjFwxx != null) {
                if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && djsjFwxx.getBdcdyh().length() > 19) {
                    List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(djsjFwxx.getBdcdyh().substring(0, 19));
                    if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                        djsjZdxx = djsjZdxxList.get(0);
                    }
                }
            } else if (StringUtils.isNotBlank(bdcXm.getBdcdyh()) && bdcXm.getBdcdyh().length() > 19) {
                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(bdcXm.getBdcdyh().substring(0, 19));
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    djsjZdxx = djsjZdxxList.get(0);
                }
            }
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 删除当前项目已有的bdc_fwfzxx
             */
            bdcFwfzxxService.deleteProjectBdcFwfzxx(bdcXm.getProid());
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 通过权籍数据重新获取bdc_fdcq_dz和bdc_fwfzxx
             */
            if(bdcFdcqDz!=null&&StringUtils.isNotBlank(bdcFdcqDz.getQlid())){
                bdcFdcqDz = qllxService.getBdcFdcqDzFromFwxx(bdcFdcqDz, djsjFwxx, djsjZdxx);
            }
        }
        return bdcFdcqDz;
    }

    @Override
    public Double getBdcFdcqDzFwzmj(String xmly,String bdcdyid,String bdcdyh,String djlx){
        Double fwzmj = 0.00;
        if(StringUtils.isNotEmpty(xmly)&&StringUtils.equals(xmly, Constants.XMLY_BDC)){
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 禅道16600 多幢重新读取权籍信息生成bdc_fdcq_dz和bdc_fwfzxx
             */
            if(StringUtils.isNotBlank(djlx)&&(StringUtils.equals(djlx,Constants.DJLX_ZYDJ_DM)||StringUtils.equals(djlx,Constants.DJLX_CFDJ_DM)||StringUtils.equals(djlx,Constants.DJLX_DYDJ_DM))&&StringUtils.isNotEmpty(bdcdyid)){
                Map<String, Object> dzMap = new HashMap<String,Object>();
                dzMap.put("bdcdyid",bdcdyid);
                dzMap.put("qszt", Constants.QLLX_QSZT_XS);
                List<BdcFdcqDz> bdcFdcqDzList = getBdcFdcqDzList(dzMap);
                if(CollectionUtils.isNotEmpty(bdcFdcqDzList)){
                    List<BdcFwfzxx> fwfzxxList = bdcFdcqDzMapper.queryBdcFwfzxxlstByQlid(bdcFdcqDzList.get(0).getQlid());
                    if(CollectionUtils.isNotEmpty(fwfzxxList)){
                        for(BdcFwfzxx bdcFwfzxx:fwfzxxList){
                            if(bdcFwfzxx.getJzmj()!=null){
                                fwzmj+=bdcFwfzxx.getJzmj();
                            }
                        }
                    }
                }
            }else{
                Map map = new HashMap();
                map.put("bdcdyh",bdcdyh);
                List<DjsjFwXmxx> djsjFwXmxxList = djsjFwService.getDjsjFwXmxx(map);
                if(CollectionUtils.isNotEmpty(djsjFwXmxxList)&&djsjFwXmxxList.get(0)!=null&&StringUtils.isNotBlank(djsjFwXmxxList.get(0).getFwXmxxIndex())){
                    Map mapTmp = new HashMap();
                    mapTmp.put("fw_xmxx_index",djsjFwXmxxList.get(0).getFwXmxxIndex());
                    List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(mapTmp);
                    if(CollectionUtils.isNotEmpty(djsjFwLjzList)){
                        for(DjsjFwLjz djsjFwLjz:djsjFwLjzList){
                            if(djsjFwLjz.getScjzmj()>0){
                                fwzmj+=djsjFwLjz.getScjzmj();
                            }
                        }
                    }
                }
            }
        }else if(StringUtils.isNotEmpty(xmly)&&StringUtils.equals(xmly,Constants.XMLY_FWSP)&&StringUtils.isNotBlank(bdcdyh)){
            //过渡多幢的情况下，直接查询权籍里面的信息
            Map map = new HashMap();
            map.put("bdcdyh",bdcdyh);
            List<DjsjFwXmxx> djsjFwXmxxList = djsjFwService.getDjsjFwXmxx(map);
            if(CollectionUtils.isNotEmpty(djsjFwXmxxList)&&djsjFwXmxxList.get(0)!=null&&StringUtils.isNotBlank(djsjFwXmxxList.get(0).getFwXmxxIndex())){
                Map mapTmp = new HashMap();
                mapTmp.put("fw_xmxx_index",djsjFwXmxxList.get(0).getFwXmxxIndex());
                List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(mapTmp);
                if(CollectionUtils.isNotEmpty(djsjFwLjzList)){
                    for(DjsjFwLjz djsjFwLjz:djsjFwLjzList){
                        if(djsjFwLjz.getScjzmj()>0){
                            fwzmj+=djsjFwLjz.getScjzmj();
                        }
                    }
                }
            }
        }
        return fwzmj;
    }

    @Override
    public void updateFdcqdfzxxByDjsjfwXmxx(DjsjFwXmxx djsjFwXmxx, BdcFdcqDz bdcFdcqDz) {
        HashMap ljzMap = new HashMap();
        ljzMap.put("fw_xmxx_index", djsjFwXmxx.getFwXmxxIndex());
        List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(ljzMap);
        if(CollectionUtils.isNotEmpty(djsjFwLjzList)){
            List<BdcFwfzxx> bdcFwfzxxList = bdcFwfzxxService.getBdcFwfzxxListByQlid(bdcFdcqDz.getQlid());
            for (int i = 0; i <bdcFwfzxxList.size() ; i++) {
                BdcFwfzxx bdcFwfzxx = bdcFwfzxxList.get(i);
                DjsjFwLjz djsjFwLjz = djsjFwLjzList.get(i);
                bdcFwfzxx.setGhyt(djsjFwLjz.getFwyt());
                bdcFwfzxx.setXmmc(djsjFwLjz.getXmmc());
                bdcFwfzxx.setZcs(String.valueOf(djsjFwLjz.getFwcs()));
                bdcFwfzxx.setZh(djsjFwLjz.getZrzh());
                bdcFwfzxx.setFwjg(djsjFwLjz.getFwjg());
                bdcFwfzxx.setFwxz(djsjFwXmxx.getFwxz());
                bdcFwfzxx.setJzmj(djsjFwLjz.getScjzmj());
                entityMapper.saveOrUpdate(bdcFwfzxx,bdcFwfzxx.getFzid());
            }
        }
    }
}
