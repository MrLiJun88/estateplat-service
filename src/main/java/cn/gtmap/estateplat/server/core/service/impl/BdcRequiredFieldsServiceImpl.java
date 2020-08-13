package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtLimitfield;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXtLimitfieldService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.plat.service.SysRequiredFieldsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zx on 2015/10/8.
 * des:必填项接口
 */
public class BdcRequiredFieldsServiceImpl implements SysRequiredFieldsService {
    @Autowired
    BdcXtLimitfieldService bdcXtLimitfieldService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcZdGlService bdcZdGlService;

    @Override
    public List getResourceRequiredFieldsByResourceName(String wfDfid, String activitDefinitionId, String cptName, String proid) {
        String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(wfDfid);
        List<BdcXtLimitfield> list = null;
        if (StringUtils.isNotBlank(sqlxdm) && (CommonUtil.indexOfStrs(Constants.HB_SQLXDM,sqlxdm))) {
            //zx合并流程按照合并的两个流程获取必填项
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (StringUtils.isNotBlank(bdcXm.getSqlx())) {
                    String childNodeWdid = bdcZdGlService.getWdidBySqlxdm(bdcXm.getSqlx());
                    //当前结点名称
                    String nodeName=bdcZdGlService.getWorkflowNodeName(activitDefinitionId,wfDfid);
                    String childNodeId=null;
                    if(StringUtils.isNotBlank(nodeName))
                        childNodeId=bdcZdGlService.getWorkflowNodeId(nodeName, childNodeWdid);
                    if (StringUtils.isNotBlank(childNodeWdid) && StringUtils.isNotBlank(childNodeId))
                        list = bdcXtLimitfieldService.getLimitfield(childNodeWdid, childNodeId, cptName);
                }
            }
        } else
            list = bdcXtLimitfieldService.getLimitfield(wfDfid, activitDefinitionId, cptName);

        List<String> elementList = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (BdcXtLimitfield bdcXtLimitfield : list) {
                elementList.add(bdcXtLimitfield.getCptFieldName());
            }
        }
        return elementList;
    }

    @Override
    public List getResourceRequiredFieldsByResourceId(String s, String s1, String s2, String proid) {
        return null;
    }
}
