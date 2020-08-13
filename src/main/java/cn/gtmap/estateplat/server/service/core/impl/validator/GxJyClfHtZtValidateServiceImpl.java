package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.JiaoYiClfHTZt;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.service.etl.JyxxService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/5/16
 * @description
 */
public class GxJyClfHtZtValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private JyxxService jyxxService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project= (Project)param.get("project");
        String checkProid = null;
        String zjjg = null;
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(project.getBdcdyh());
            if(StringUtils.isNotBlank(bdcdyid)) {
                HashMap hashMap = Maps.newHashMap();
                hashMap.put("bdcdyid",bdcdyid);
                hashMap.put("qszt",Constants.QLLX_QSZT_XS);
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcq(hashMap);
                List<BdcFdcqDz> bdcFdcqDzList = bdcFdcqDzService.getBdcFdcqDzList(hashMap);
                if(CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                    if(StringUtils.isNotBlank(bdcFdcq.getProid())) {
                        List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(bdcFdcq.getProid());
                        if(CollectionUtils.isNotEmpty(bdcZsList)) {
                            for(BdcZs bdcZs:bdcZsList) {
                                String clfHTJson = jyxxService.getJyClfHTZt(bdcZs.getBdcqzhjc());
                                if (StringUtils.equals(ParamsConstants.EXCEPTION_LOWERCASE,clfHTJson)){
                                    // 接口调用异常、超时
                                    checkProid = bdcFdcq.getProid();
                                    map.put("replace", "交易验证接口调用失败，请检查接口！");
                                    break;
                                } else {
                                    if(StringUtils.isNotBlank(clfHTJson)){
                                        JiaoYiClfHTZt jiaoYiClfHTZt = JSON.parseObject(clfHTJson, JiaoYiClfHTZt.class);
                                        if(jiaoYiClfHTZt != null && StringUtils.isNotBlank(jiaoYiClfHTZt.getZt())){
                                            checkProid = bdcFdcq.getProid();
                                            if(StringUtils.isNotBlank(jiaoYiClfHTZt.getZjjg())){
                                                zjjg = jiaoYiClfHTZt.getZjjg();
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if(CollectionUtils.isNotEmpty(bdcFdcqDzList)){
                    BdcFdcqDz bdcFdcqDz = bdcFdcqDzList.get(0);
                    if(StringUtils.isNotBlank(bdcFdcqDz.getProid())){
                        List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(bdcFdcqDz.getProid());
                        if(CollectionUtils.isNotEmpty(bdcZsList)) {
                            for(BdcZs bdcZs:bdcZsList) {
                                String clfHTJson = jyxxService.getJyClfHTZt(bdcZs.getBdcqzhjc());
                                if (StringUtils.equals(ParamsConstants.EXCEPTION_LOWERCASE,clfHTJson)){
                                    // 接口调用异常、超时
                                    checkProid = bdcFdcqDz.getProid();
                                    map.put("replace", "交易验证接口调用失败，请检查接口！");
                                    break;
                                } else {
                                    if(StringUtils.isNotBlank(clfHTJson)){
                                        JiaoYiClfHTZt jiaoYiClfHTZt = JSON.parseObject(clfHTJson, JiaoYiClfHTZt.class);
                                        if (jiaoYiClfHTZt != null && StringUtils.isNotBlank(jiaoYiClfHTZt.getZt())) {
                                            checkProid = bdcFdcqDz.getProid();
                                            if (StringUtils.isNotBlank(jiaoYiClfHTZt.getZjjg())) {
                                                zjjg = jiaoYiClfHTZt.getZjjg();
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else {
                    Map gdMap = validateGdFczh(project.getBdcdyh());
                    if(gdMap.get("zjjg") != null){
                        zjjg = gdMap.get("zjjg").toString();
                    }
                    if(gdMap.get("checkProid") != null){
                        checkProid = gdMap.get("checkProid").toString();
                    }
                }
            }else {
                Map gdMap = validateGdFczh(project.getBdcdyh());
                if(gdMap.get("zjjg") != null){
                    zjjg = gdMap.get("zjjg").toString();
                }
                if(gdMap.get("checkProid") != null){
                    checkProid = gdMap.get("checkProid").toString();
                }
            }
            if(StringUtils.isNotBlank(checkProid)){
                map.put("info", checkProid);
                if (StringUtils.isNotBlank(zjjg)) {
                    map.put("detail","中介机构："+zjjg);
                }else{
                    map.put("detail","已经交易！");
                }
            }else {
                map.put("info", null);
            }
        }
        return map;
    }


    /**
     * @param bdcdyh
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 验证过渡房产证号存量房合同交易状态
     */
    private Map validateGdFczh(String bdcdyh) {
        Map map = new HashMap();
        String checkProid = null;
        String zjjg = null;
        if(StringUtils.isNotBlank(bdcdyh)) {
            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(bdcdyh);
            if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                for(BdcGdDyhRel bdcGdDyhRel:bdcGdDyhRelList) {
                    if(bdcGdDyhRel != null&&StringUtils.isNotBlank(bdcGdDyhRel.getGdid())) {
                        List<GdFwsyq> gdFwsyqList = gdFwService.queryGdFwsyqByFwidAndQszt(bdcGdDyhRel.getGdid(),"0");
                        if(CollectionUtils.isNotEmpty(gdFwsyqList)) {
                            GdFwsyq gdFwsyq = gdFwsyqList.get(0);
                            if(gdFwsyq != null&&StringUtils.isNotBlank(gdFwsyq.getProid())) {
                                String clfHTJson = jyxxService.getJyClfHTZt(gdFwsyq.getFczh());
                                if (StringUtils.equals(ParamsConstants.EXCEPTION_LOWERCASE,clfHTJson)){
                                    // 接口调用异常、超时
                                    checkProid = gdFwsyq.getProid();
                                    map.put("replace", "交易验证接口调用失败，请检查接口！");
                                    break;
                                } else {
                                    if(StringUtils.isNotBlank(clfHTJson)){
                                        JiaoYiClfHTZt jiaoYiClfHTZt = JSON.parseObject(clfHTJson, JiaoYiClfHTZt.class);
                                        if (jiaoYiClfHTZt != null && StringUtils.isNotBlank(jiaoYiClfHTZt.getZt())) {
                                            checkProid = gdFwsyq.getProid();
                                            if (StringUtils.isNotBlank(jiaoYiClfHTZt.getZjjg())) {
                                                zjjg = jiaoYiClfHTZt.getZjjg();
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        map.put("checkProid",checkProid);
        map.put("zjjg",zjjg);
        return map;
    }

    @Override
    public String getCode() {
        return "303";
    }

    @Override
    public String getDescription() {
        return "验证存量房交易状态是否在办";
    }
}
