package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.GdBdcSd;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcBdcZsSdService;
import cn.gtmap.estateplat.server.core.service.BdcFdcqService;
import cn.gtmap.estateplat.server.core.service.BdcSdService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2016/9/21
 * @description 证书证明锁定验证
 */
public class BdcZsSdValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcBdcZsSdService bdcBdcZsSdService;
    @Autowired
    private BdcSdService bdcSdService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        String xzyy = "";
        if (project != null) {
            if(StringUtils.isNotBlank(project.getYxmid())){
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(project.getYxmid());
                List<BdcBdcZsSd> list = null;
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    List<String> zsids = new ArrayList<String>();
                    for (BdcZs bdcZs : bdcZsList) {
                        zsids.add(bdcZs.getZsid());
                    }
                    HashMap queryMap = new HashMap();
                    queryMap.put("zsids",zsids);
                    queryMap.put("xzzt", Constants.XZZT_SD);
                    list = bdcBdcZsSdService.getBdcZsSdList(queryMap);
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    proidList = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {
                        proidList.add(list.get(i).getProid());
                    }
                    xzyy = list.get(0).getXzyy();
                }
            }
            if(CollectionUtils.isEmpty(proidList)){
                HashMap<String,Object> queryMapTemp = Maps.newHashMap();
                List<GdBdcSd> gdBdcSdList = new ArrayList<GdBdcSd>();
                if(StringUtils.isNotBlank(project.getGdproid())) {
                    queryMapTemp.put("proid", project.getGdproid());
                    queryMapTemp.put("xzzt", 1);
                    gdBdcSdList = bdcSdService.queryGdBdcSdByMap(queryMapTemp);
                }
                if(CollectionUtils.isNotEmpty(gdBdcSdList)){
                    proidList = new ArrayList<String>();
                    if(gdBdcSdList.get(0)!=null && StringUtils.isNotBlank(gdBdcSdList.get(0).getXzyy())) {
                        xzyy = gdBdcSdList.get(0).getXzyy();
                    }
                    for(GdBdcSd gdBdcSd:gdBdcSdList){
                        if(gdBdcSd!=null && StringUtils.isNotBlank(gdBdcSd.getProid())){
                            proidList.add(gdBdcSd.getProid());
                        }
                    }
                }
            }
            if(StringUtils.equalsIgnoreCase(project.getSqlx(),Constants.SQLX_JF)&&StringUtils.isNotBlank(project.getBdcdyh())){
              List<BdcZs> bdcZsList= bdcZsService.queryBdcZsByBdcdyh(project.getBdcdyh());
                List<BdcBdcZsSd> list = null;
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    List<String> zsids = new ArrayList<String>();
                    for (BdcZs bdcZs : bdcZsList) {
                        zsids.add(bdcZs.getZsid());
                    }
                    HashMap queryMap = new HashMap();
                    queryMap.put("zsids",zsids);
                    queryMap.put("xzzt", Constants.XZZT_SD);
                    list = bdcBdcZsSdService.getBdcZsSdList(queryMap);
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    proidList = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {
                        proidList.add(list.get(i).getProid());
                    }
                    xzyy = list.get(0).getXzyy();
                }
            }
        }
        map.put("info", proidList);
        map.put("tsinfo", xzyy);
        return map;
    }

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @version 1.0, 2016/9/21
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "204";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @version 1.0, 2016/9/21
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证产权证是否锁定";
    }
}
