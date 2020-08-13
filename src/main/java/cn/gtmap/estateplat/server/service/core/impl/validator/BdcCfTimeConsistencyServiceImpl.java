package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
 * @version 1.0, 2019-08-09
 * @description: 验证查封开始时间和项目创建时间是同一天
 */
public class BdcCfTimeConsistencyServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcCfService bdcCfService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if(project != null && StringUtils.isNotBlank(project.getWiid())){
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm : bdcXmList){
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())){
                        BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXm.getProid());
                        if(bdcCf != null){
                            String cflx = bdcCf.getCflx();
                            if(StringUtils.isNotBlank(cflx)
                                    && (StringUtils.equals(cflx, Constants.CFLX_ZD_CF)
                                        || StringUtils.equals(cflx, Constants.CFLX_ZD_YCF))){
                                Date xmCjsj = bdcXm.getCjsj();
                                Date cfksqx = bdcCf.getCfksqx();
                                if(xmCjsj == null || cfksqx == null){
                                    proidList.add(bdcXm.getProid());
                                }else {
                                    if(!DateUtils.isSameDay(xmCjsj, cfksqx)){
                                        proidList.add(bdcXm.getProid());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList))
            map.put("info", proidList);
        else
            map.put("info", null);
        return map;
    }

    @Override
    public String getCode() {
        return "917";
    }

    @Override
    public String getDescription() {
        return "验证查封时间是否和项目创建时间一致";
    }
}
