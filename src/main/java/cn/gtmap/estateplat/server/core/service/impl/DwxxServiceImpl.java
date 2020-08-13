package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.Dwxx;
import cn.gtmap.estateplat.server.core.mapper.DwxxMapper;
import cn.gtmap.estateplat.server.core.service.DwxxService;
import com.fr.web.core.A.S;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p/>
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version V1.0
 */
@Service
public class DwxxServiceImpl implements DwxxService {
    @Autowired
    DwxxMapper dwxxMapper;

    @Override
    public List<Dwxx> getDwxxList(final String xzqdm,final  int level) {
        HashMap map = new HashMap();
        map.put("dwdm", xzqdm);
        map.put("xzLevel", level);
        return dwxxMapper.getDwxxList(map);
    }

    @Override
    public Dwxx getDwxxByDwdm(String dwdm) {
        return dwxxMapper.getDwxxByDwdm(dwdm);
    }

    @Override
    public String getDwmcByDwdm(String dwdm) {
        String dwmc = null;
        if (StringUtils.isNotBlank(dwdm)) {
            dwmc = dwxxMapper.getDwmcByDwdm(dwdm);
        }
        return dwmc ;
    }
}
