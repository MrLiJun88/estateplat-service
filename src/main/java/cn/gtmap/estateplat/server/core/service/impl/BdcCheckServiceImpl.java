package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdysdMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.google.common.collect.Lists;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/11/13
 * @description
 */
@Service
public class BdcCheckServiceImpl implements BdcCheckService {
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcBdcdysdMapper bdcBdcdysdMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public List<Map<String, Object>> organizeExamineData(List<String> cfQlidList) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(cfQlidList)){
            for(String cfQlid:cfQlidList){
                Map<String, Object> returnMap = organizeExamineCfData(cfQlid);
                if(returnMap != null){
                    resultList.add(returnMap);
                }else{
                    returnMap = organizeExamineSdData(cfQlid);
                    if(returnMap != null){
                        resultList.add(returnMap);
                    }
                }
            }
        }
        return resultList;
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 组织examine查封验证数据
     */
    private Map<String, Object> organizeExamineCfData(String cfQlid){
        Map<String, Object> returnMap = null;
        if(StringUtils.isNotBlank(cfQlid)){
            BdcCf bdcCf = entityMapper.selectByPrimaryKey(BdcCf.class, cfQlid);
            if(bdcCf == null){
                GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class, cfQlid);
                if(gdCf != null){
                    returnMap = new HashMap<String, Object>();
                    String bdcdyh = "";
                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(cfQlid);
                    if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                        for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList){
                            if(gdBdcQlRel != null&&StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                                if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)){
                                    for(BdcGdDyhRel gdDyhRel:bdcGdDyhRelList){
                                        if(gdDyhRel != null&&StringUtils.isNotBlank(gdDyhRel.getBdcdyh())){
                                            bdcdyh = gdDyhRel.getBdcdyh();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    returnMap.put("checkModel", ParamsConstants.ALERT_LOWERCASE);
                    returnMap.put(ParamsConstants.CHECKMSG_HUMP, AppConfig.getProperty("msg.gdCf"));
                    returnMap.put("info", gdCf.getProid());
                    returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                }
            }else{
                returnMap = new HashMap<String, Object>();
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcCf.getBdcdyid());
                returnMap.put(ParamsConstants.CHECKMODEL_HUMP, ParamsConstants.ALERT_LOWERCASE);
                returnMap.put(ParamsConstants.CHECKMSG_HUMP, AppConfig.getProperty("msg.bdcCf"));
                returnMap.put("info", bdcCf.getProid());
                if(bdcBdcdy != null){
                    returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcBdcdy.getBdcdyh());
                }
            }
        }
        return returnMap;
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 组织examine锁定验证数据
     */
    private Map<String, Object> organizeExamineSdData(String sdid){
        Map<String, Object> returnMap = null;
        if(StringUtils.isNotBlank(sdid)){
            BdcBdcdySd bdcBdcdySd = bdcBdcdysdMapper.findBdcBdcdySd(sdid);
            if(bdcBdcdySd != null){
                returnMap = new HashMap<String, Object>();
                returnMap.put(ParamsConstants.CHECKMODEL_HUMP, ParamsConstants.ALERT_LOWERCASE);
                returnMap.put(ParamsConstants.CHECKMSG_HUMP, AppConfig.getProperty("msg.bdcSd"));
                returnMap.put("info", bdcBdcdySd.getBdcdyid());
                returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcBdcdySd.getBdcdyh());
            } else {
                BdcBdcZsSd bdcBdcZsSd = entityMapper.selectByPrimaryKey(BdcBdcZsSd.class, sdid);
                if (bdcBdcZsSd != null) {
                    String proid = bdcZsService.getProidByBdcqzh(bdcBdcZsSd.getCqzh());
                    returnMap = new HashMap<String, Object>();
                    returnMap.put(ParamsConstants.CHECKMODEL_HUMP, ParamsConstants.ALERT_LOWERCASE);
                    returnMap.put(ParamsConstants.CHECKMSG_HUMP, AppConfig.getProperty("msg.bdcZsSd"));
                    returnMap.put("info", bdcBdcZsSd.getSdid());
                    returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyService.getBdcdyhByProid(proid));
                } else {
                    GdBdcSd gdBdcSd = entityMapper.selectByPrimaryKey(GdBdcSd.class, sdid);
                    if(gdBdcSd != null){
                        returnMap = new HashMap<String, Object>();
                        List gdQlList = gdFwService.getGdQlByCqzh(gdBdcSd.getCqzh(),gdBdcSd.getBdclx());
                        if(CollectionUtils.isNotEmpty(gdQlList)) {
                            if (StringUtils.equals(gdBdcSd.getBdclx(), Constants.BDCLX_TDFW)) {
                                GdFwsyq gdFwsyq = (GdFwsyq) gdQlList.get(0);
                                if (gdFwsyq != null) {
                                    String bdcdyh = "";
                                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdFwsyq.getQlid());
                                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                            if (gdBdcQlRel != null && StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                                                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                                                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                                                    for (BdcGdDyhRel gdDyhRel : bdcGdDyhRelList) {
                                                        if (gdDyhRel != null && StringUtils.isNotBlank(gdDyhRel.getBdcdyh())) {
                                                            bdcdyh = gdDyhRel.getBdcdyh();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    returnMap.put(ParamsConstants.CHECKMODEL_HUMP, ParamsConstants.ALERT_LOWERCASE);
                                    returnMap.put(ParamsConstants.CHECKMSG_HUMP, AppConfig.getProperty("msg.gdSd"));
                                    returnMap.put("info", gdBdcSd.getSdid());
                                    returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                                }
                            } else {
                                GdTdsyq gdTdsyq = (GdTdsyq) gdQlList.get(0);
                                if (gdTdsyq != null) {
                                    String bdcdyh = "";
                                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdTdsyq.getQlid());
                                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                            if (gdBdcQlRel != null && StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                                                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                                                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                                                    for (BdcGdDyhRel gdDyhRel : bdcGdDyhRelList) {
                                                        if (gdDyhRel != null && StringUtils.isNotBlank(gdDyhRel.getBdcdyh())) {
                                                            bdcdyh = gdDyhRel.getBdcdyh();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    returnMap.put(ParamsConstants.CHECKMODEL_HUMP, ParamsConstants.ALERT_LOWERCASE);
                                    returnMap.put(ParamsConstants.CHECKMSG_HUMP, AppConfig.getProperty("msg.gdSd"));
                                    returnMap.put("info", gdBdcSd.getSdid());
                                    returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnMap;
    }


}
