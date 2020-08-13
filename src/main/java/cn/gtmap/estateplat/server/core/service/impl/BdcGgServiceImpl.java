package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcGg;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcGgService;
import cn.gtmap.estateplat.server.core.service.BdcLshService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/3/6
 * @description
 */
@Service
public class BdcGgServiceImpl implements BdcGgService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcLshService bdcLshService;

    @Override
    public String getBdcGgBh(BdcXm bdcXm, String userid) {
        String ggbh = "";
        List<BdcGg> bdcGgList = null;
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            Example example = new Example(BdcGg.class);
            example.createCriteria().andEqualTo("wiid", bdcXm.getWiid());
            bdcGgList = entityMapper.selectByExample(example);
        }
        if (CollectionUtils.isEmpty(bdcGgList)) {
            Example example = new Example(BdcGg.class);
            example.createCriteria().andEqualTo("proid", bdcXm.getProid());
            bdcGgList = entityMapper.selectByExample(example);
        }

        String nf = CalendarUtil.getCurrYear();
        String qh = bdcLshService.getQh(userid);
        if (CollectionUtils.isNotEmpty(bdcGgList)) {
            BdcGg bdcGg = bdcGgList.get(0);
            if (StringUtils.isBlank(bdcGg.getGgbh())) {
                String lsh = bdcLshService.getLsh(Constants.BHLX_BDCGG_MC, nf, qh);
                ggbh = bdcLshService.getBh(Constants.BHLX_BDCGG_MC, nf, qh, lsh);
            }
        } else {
            String lsh = bdcLshService.getLsh(Constants.BHLX_BDCGG_MC, nf, qh);
            ggbh = bdcLshService.getBh(Constants.BHLX_BDCGG_MC, nf, qh, lsh);
        }

        return ggbh;
    }

    @Override
    public void deleteBdcGg(BdcXm bdcXm) {
        if (bdcXm != null) {
            Example bdcGgExample = new Example(BdcGg.class);
            bdcGgExample.createCriteria().andEqualTo("proid", bdcXm.getProid());
            entityMapper.deleteByExample(bdcGgExample);
        }
    }

    @Override
    public void saveGgxx(BdcGg bdcGg) {
        entityMapper.saveOrUpdate(bdcGg, bdcGg.getGgid());
    }

    @Override
    public void deleteGgxx(String ggid) {
        entityMapper.deleteByPrimaryKey(BdcGg.class, ggid);
    }

    @Override
    public BdcGg getBdcGgByGgid(String ggid) {
        return entityMapper.selectByPrimaryKey(BdcGg.class, ggid);
    }

    @Override
    public  List<BdcGg> getBdcGg(Map map) {
        List<BdcGg> bdcGgList=null;
        if (map != null) {
            Example bdcGgExample = new Example(BdcGg.class);
            Example.Criteria criteria = bdcGgExample.createCriteria();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null) {
                    criteria.andEqualTo(key.toString(), val);
                }
                bdcGgList = entityMapper.selectByExample(BdcGg.class, bdcGgExample);
            }
        }
        return bdcGgList;
    }
}
