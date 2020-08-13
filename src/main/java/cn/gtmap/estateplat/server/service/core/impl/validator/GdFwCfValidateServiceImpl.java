package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/19
 * @description
 */
public class GdFwCfValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcCfService bdcCfService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        if (project != null && StringUtils.isNotBlank(project.getGdproid())) {
            List<BdcXmRel> bdcXmRelList = null;
            Example examplexmlrel = new Example(BdcXmRel.class);
            Example.Criteria criteriaxmlrel = examplexmlrel.createCriteria();
            if (StringUtils.isNotBlank(project.getGdproid())) {
                criteriaxmlrel.andEqualTo("yproid", project.getGdproid());
                bdcXmRelList = entityMapper.selectByExample(BdcXmRel.class, examplexmlrel);
            }
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getProid())) {
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                            BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXm.getProid());
                            /**
                             * @author <a href="mailto:liangqing@gtmap.cn">liangqing</a>
                             * @description 判断改查封权利是否为现世
                             */
                            if (bdcCf != null && bdcCf.getQszt() == 1 && (StringUtils.equals(bdcXm.getSqlx(), "801") || StringUtils.equals(bdcXm.getSqlx(), "806"))) {
                                proidList = new ArrayList<String>();
                                proidList.add(project.getProid());
                                break;
                            }
                        }
                    }
                }
            }
        }
        map.put("info", proidList);
        return map;
    }

    @Override
    public String getCode() {
        return "205";
    }

    @Override
    public String getDescription() {
        return "过渡房屋是否存在查封登记-不匹配不动产单元的验证";
    }
}
