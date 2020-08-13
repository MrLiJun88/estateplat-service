package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSqlxQllxRel;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;
import cn.gtmap.estateplat.server.core.mapper.BdcXtConfigMapper;
import cn.gtmap.estateplat.server.core.service.BdcXtConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-19
 */
@Repository
public class BdcXtConfigServiceImpl implements BdcXtConfigService {
    @Autowired
    public BdcXtConfigMapper bdcXtConfigMapper;
    @Autowired
    public EntityMapper entityMapper;

    @Override
    public BdcXtConfig queryBdczsBhConfig(final BdcXm bdcXm) {
        BdcXtConfig bdcXtConfig = null;
        HashMap<String, String> map = new HashMap<String, String>();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDwdm())) {
            map.put("dwdm", bdcXm.getDwdm());
			Calendar calendar = Calendar.getInstance();
			map.put("nf", String.valueOf(calendar.get(Calendar.YEAR)))  ;
            bdcXtConfig = bdcXtConfigMapper.selectBdcXtConfig(map);
        }
        return bdcXtConfig;
    }

    @Override
    public List<BdcSqlxQllxRel> getOthersBySqlx(final String sqlx) {
        Example example = new Example(BdcSqlxQllxRel.class);
        example.createCriteria().andEqualTo("sqlxdm", sqlx);
        return entityMapper.selectByExampleNotNull(example);
    }

    @Override
    public BdcXtConfig queryBdcXtConfigByDwdm(String dwdm) {
        BdcXtConfig bdcXtConfig = null;
        if(StringUtils.isNotBlank(dwdm)){
            Map map=new HashMap();
            map.put("dwdm",dwdm);
            bdcXtConfig = bdcXtConfigMapper.selectBdcXtConfig(map);
        }
        return bdcXtConfig;
    }
}
