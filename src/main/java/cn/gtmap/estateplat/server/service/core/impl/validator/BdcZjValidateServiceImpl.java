package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.BdcZj;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/8/20
 * @description
 */
public class BdcZjValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZjService bdcZjService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(project.getProid())) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo("yproid",project.getProid());
            List<BdcXmRel> bdcXmRelList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                    if (bdcXm != null && StringUtils.equals(bdcXm.getSqlx(), "199")) {
                        BdcZj bdcZj = bdcZjService.getBdcZjByProid(bdcXm.getProid());
                        if (bdcZj != null && StringUtils.isNotBlank(bdcZj.getZjzt()) && bdcZj.getZjzt().equals("0")) {
                            map.put("info", bdcXm.getProid());
                            break;
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "401";
    }

    @Override
    public String getDescription() {
        return "验证流程是否质检通过";
    }
}
