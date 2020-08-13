package cn.gtmap.estateplat.server.service.core.impl.validator;
/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2017/3/15
 * @description 验证当依据法律文书转移时的权利人是否是裁定的被转移人
 */

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BdcdyCdZyrValidateServiceImpl implements ProjectValidateService {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcQlrService bdcQlrService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(project.getBdcdyh())) {
            String bdcdyh = project.getBdcdyh();
            // 获取申请裁定转移登记项目的proid
            String cdzyproid = project.getProid();
            //获取最后一次的裁定解封项目的proid
            List<String> cdproidList= bdcXmService.queryCdBdcXmByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(cdproidList)) {
                //获取最后一次裁定解封的义务人
                List<Map> bdcYwrList = bdcQlrService.ywrMcAndZjhByProid(cdproidList.get(0));
                if (CollectionUtils.isNotEmpty(bdcYwrList)&&StringUtils.isNotBlank(cdzyproid)) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(cdzyproid);
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getQllx()) && StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                        return map;
                    }
                    //判断裁定转移的权利人是否存在裁定解封的义务人
                    List<Map> bdcQlrList = bdcQlrService.qlrMcAndZjhByProid(cdzyproid);
                    if(CollectionUtils.isNotEmpty(bdcQlrList)){
                        boolean contains = bdcQlrList.containsAll(bdcYwrList);
                        if (!contains) {
                            proidList.add(cdproidList.get(0));
                        }
                    }else {
                        proidList.add(cdproidList.get(0));
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList))
            map.put("info", proidList.get(0));
        else
            map.put("info", null);
        return map;
    }

    @Override
    public String getCode() {
        return "140";
    }

    @Override
    public String getDescription() {
        return "验证当依据法律文书转移时的权利人是否是裁定的被转移人";
    }
}
