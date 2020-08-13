package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.GdFwsyq;
import cn.gtmap.estateplat.model.server.core.GdQlr;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.GdQlrService;
import cn.gtmap.estateplat.server.core.service.GdqlService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.service.etl.JyxxService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2019/8/21
 * @description 验证抵押首次时产权证是否存在有效的合同状态
 */
public class BdcJyDyValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private JyxxService jyxxService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        String ybdcqzh = "";
        if (project != null) {
            if(StringUtils.isNotBlank(project.getGdproid())) {
                List<GdQlr> gdQlrList = gdQlrService.queryGdQlrListByProid(project.getGdproid(),Constants.QLRLX_QLR);
                if(CollectionUtils.isNotEmpty(gdQlrList)) {
                    ybdcqzh = gdQlrList.get(0).getCqzh();
                }
            }else if(StringUtils.isNotBlank(project.getYxmid())) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(project.getYxmid());
                if(CollectionUtils.isNotEmpty(bdcZsList)) {
                    ybdcqzh = bdcZsList.get(0).getBdcqzh();
                }
            }
        }
        String status = jyxxService.getHtZtByCqzh(ybdcqzh);
        if (StringUtils.isNotBlank(status) && !StringUtils.equals(status, ParamsConstants.HTZT_WBA)) {
            map.put("info", project.getProid());
        }
        return map;
    }

    @Override
    public String getCode() {
        return "502";
    }

    @Override
    public String getDescription() {
        return "验证抵押首次时产权证是否存在有效的合同状态";
    }
}
