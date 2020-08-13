package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2018/11/15
 * @description 验证项目是否已经发起评价
 */
public class BdcZfValidateServiceimpl implements ProjectValidateService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(project.getWiid())) {
            String wfProid=PlatformUtil.getPfProidByWiid(project.getWiid());
            if (StringUtils.isNotBlank(wfProid)) {
                Example example = new Example(BdcTszt.class);
                example.createCriteria().andEqualTo("proid", wfProid).andEqualTo("tsdx", Constants.TSDX_PJXT);
                List<BdcTszt> bdcTsztList = entityMapper.selectByExample(example);
                if (CollectionUtils.isEmpty(bdcTsztList)) {
                    map.put("info", wfProid);
                }
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "402";
    }

    @Override
    public String getDescription() {
        return "验证是否已经发起评价";
    }
}
