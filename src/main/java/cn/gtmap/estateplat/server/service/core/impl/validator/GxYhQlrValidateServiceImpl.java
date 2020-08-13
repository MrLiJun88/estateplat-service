package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.register.QlrModel;
import cn.gtmap.estateplat.model.register.SqxxModel;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @version 1.0, 2017/7/3.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description
 */
public class GxYhQlrValidateServiceImpl implements ProjectValidateService {
    @Autowired
    BdcQlrService bdcQlrService;

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 验证注销抵押权利人和抵押首次义务人
     **/
    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<QlrModel> qlrModelList = (List<QlrModel>) param.get("QlrModel");
        SqxxModel sqxxModel = (SqxxModel) param.get("SqxxModel");
        List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrListByBdcqzh(sqxxModel.getYbdcqzh(), Constants.QLRLX_QLR);
        if(!assertSameQlr(qlrModelList, bdcQlrList))
            map.put("info", sqxxModel.getBdcdyh());
        return map;
    }


    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 比较俩个权利人
     **/
    private boolean assertSameQlr(List<QlrModel> qlrModelList, List<BdcQlr> bdcQlrList) {
        boolean same = false;
        if(qlrModelList == null && bdcQlrList == null)
            same = true;
        else if(CollectionUtils.isNotEmpty(qlrModelList) && CollectionUtils.isNotEmpty(bdcQlrList) && qlrModelList.size() == bdcQlrList.size()) {
            for(QlrModel qlrModel : qlrModelList) {
                same = false;
                for(BdcQlr bdcQlr : bdcQlrList) {
                    if(StringUtils.equals(qlrModel.getQlrzjh(), bdcQlr.getQlrzjh()) && StringUtils.equals(bdcQlr.getQlrmc(), qlrModel.getQlrmc())) {
                        same = true;
                        break;
                    }
                }
                //未找到不用继续循环
                if(!same)
                    break;
            }
        }
        return same;
    }


    @Override
    public String getCode() {
        return "1001";
    }

    @Override
    public String getDescription() {
        return "验证抵押银行权利人活人义务人是否一致";
    }
}
