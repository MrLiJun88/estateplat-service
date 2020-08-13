package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2018/11/5
 * @description 验证房产证是否匹配土地证
 */
public class GdfczMatchTdzValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        String proid = null;
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        if(project != null && StringUtils.isNotBlank(project.getGdproid()) && StringUtils.isNotBlank(project.getBdcdyh())){
            HashMap hashMap = Maps.newHashMap();
            //抵押权首次登记权利类型是抵押但是选的是房产证所以先考虑这种情况
            if (StringUtils.isNotBlank(project.getSqlx()) && StringUtils.equals(project.getSqlx(), Constants.SQLX_FWDY_DM)){
                String qllxFwsyq = Constants.QLLX_GYTD_FWSUQ;
                hashMap.put("qllx",qllxFwsyq);
                hashMap.put("bdcdyh",project.getBdcdyh());
            }else{
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
                if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getQllx())){
                    String qllx = bdcXm.getQllx();
                    hashMap.put("qllx",qllx);
                }
                hashMap.put("bdcdyh",project.getBdcdyh());
            }
            //判断不动产单元号匹配的和项目权利类型对应的权利是否匹配土地证
            List<String> tdQlidList = gdFwService.getTdQlidByQllx(hashMap);
            if(CollectionUtils.isNotEmpty(tdQlidList)){
                for (String tdQlidTemp: tdQlidList){
                    if (StringUtils.isBlank(tdQlidTemp)){
                        proid = project.getGdproid();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(proid)){
            map.put("info",proid);
        }else{
            map.put("info",null);
        }
        return map;
    }
    @Override
    public String getCode() {
        return "304";
    }

    @Override
    public String getDescription() {
        return "验证房产证是否匹配土地证";
    }
}
