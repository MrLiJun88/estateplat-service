package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJsxx;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public class BdcdyJsxxValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private EntityMapper entityMapper;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> bdcdyhList = null;
        Example example =new Example(BdcJsxx.class);
        Example.Criteria criteria=example.createCriteria();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            criteria.andEqualTo("bdcdyh",project.getBdcdyh());
        }
        if(project != null && StringUtils.isNotBlank(project.getYbdcqzh())){
            criteria.andEqualTo("cqzh",project.getYbdcqzh());
        }
        criteria.andEqualTo("jszt", Constants.JSZT_JS);
        List<BdcJsxx> bdcJsxxList = null;
        if((project != null && StringUtils.isNotBlank(project.getBdcdyh())) || (project != null && StringUtils.isNotBlank(project.getYbdcqzh())))
            bdcJsxxList = entityMapper.selectByExample(example);

        if(CollectionUtils.isNotEmpty(bdcJsxxList)){
            bdcdyhList=new ArrayList<String>();
            for(BdcJsxx jsxx:bdcJsxxList){
                bdcdyhList.add(jsxx.getBdcdyh());
            }
        }
        map.put("info",CollectionUtils.isNotEmpty(bdcdyhList)?bdcdyhList:null);
        return map;
    }

    @Override
    public String getCode() {
        return "908";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元或产权证是否警示";
    }
}
