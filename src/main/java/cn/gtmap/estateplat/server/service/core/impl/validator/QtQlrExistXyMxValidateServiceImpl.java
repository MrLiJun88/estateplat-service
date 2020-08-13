package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXymxService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liaoxiang@gtmap.cn">liaoxiang</a>
 * @version
 * @description 转发验证权利人是否存在信用记录
 */
public class QtQlrExistXyMxValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXymxService bdcXymxService;



    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        List<String> xymxidList = null;
        if(project != null && StringUtils.isNotBlank(project.getWiid())){
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm:bdcXmList){
                    List<BdcQlr> bdcQlrList1 = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                    if(CollectionUtils.isNotEmpty(bdcQlrList1)){
                        bdcQlrList.addAll(bdcQlrList1);
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(bdcQlrList)){
            for(BdcQlr bdcQlr:bdcQlrList){
                if(StringUtils.isNotBlank(bdcQlr.getQlrmc()) &&StringUtils.isNotBlank(bdcQlr.getQlrzjh())){
                    HashMap hashMap = bdcXymxService.getXsBdcXyxxByZjh(bdcQlr.getQlrzjh(),bdcQlr.getQlrmc());
                    if(hashMap != null &&hashMap.get("XYMXID") != null){
                        xymxidList=new ArrayList<String>();
                        xymxidList.add(hashMap.get("XYMXID").toString());
                        break;
                    }

                }
            }
        }
        map.put("info",CollectionUtils.isNotEmpty(xymxidList)?xymxidList:null);

        return map;
    }

    @Override
    public String getCode() {
        return "910";
    }

    @Override
    public String getDescription() {
        return "验证权利人是否存在信用记录";
    }
}
