package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcGg;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public class BdcGgValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private EntityMapper entityMapper;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = null;
        List<BdcGg> bdcGgList=null;
        if (project != null && StringUtils.isNotBlank(project.getWiid())) {
            Example example =new Example(BdcGg.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("wiid",project.getWiid());
            criteria.andGreaterThanOrEqualTo("jssj",new Date());
            bdcGgList=entityMapper.selectByExample(example);
        }
        if(CollectionUtils.isNotEmpty(bdcGgList)){
            proidList=new ArrayList<String>();
            for(BdcGg gg:bdcGgList){
                proidList.add(gg.getProid());
            }
        }
        map.put("info",CollectionUtils.isNotEmpty(proidList)?proidList:null);
        return map;
    }

    @Override
    public String getCode() {
        return "909";
    }

    @Override
    public String getDescription() {
        return "验证公告是否过期";
    }
}
