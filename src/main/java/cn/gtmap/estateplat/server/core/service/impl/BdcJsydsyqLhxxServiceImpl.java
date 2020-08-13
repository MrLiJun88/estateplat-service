package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcJsydsyqLhxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcJsydsyqLhxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/5/4
 * @description
 */
@Service
public class BdcJsydsyqLhxxServiceImpl implements BdcJsydsyqLhxxService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcJsydsyqLhxxMapper bdcJsydsyqLhxxMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public List<DjsjZdJsydsyb> getZdJsydsybList(Map map) {
        return bdcJsydsyqLhxxMapper.getZdJsydsybList(map);
    }

    @Override
    public List<DjsjFwJsydzrzxx> getFwJsydzrzxxList(Map map) {
        return bdcJsydsyqLhxxMapper.getFwJsydzrzxxList(map);
    }

    @Override
    public List<BdcJsydsyqLhxx> getBdcJsydsyqLhxxFromDjsj(BdcXm bdcXm, List<DjsjZdJsydsyb> djsjZdJsydsybList, List<DjsjFwJsydzrzxx> djsjFwJsydzrzxxList) {
        List<BdcJsydsyqLhxx> bdcJsydsyqLhxxList = null;
        if (CollectionUtils.isNotEmpty(djsjZdJsydsybList) && CollectionUtils.isNotEmpty(djsjFwJsydzrzxxList)) {
            bdcJsydsyqLhxxList = new ArrayList<BdcJsydsyqLhxx>();
            DjsjZdJsydsyb djsjZdJsydsyb  = djsjZdJsydsybList.get(0);
            for (DjsjFwJsydzrzxx djsjFwJsydzrzxx : djsjFwJsydzrzxxList) {
                BdcJsydsyqLhxx bdcJsydsyqLhxx = new BdcJsydsyqLhxx();
                bdcJsydsyqLhxx.setLhid(UUIDGenerator.generate18());
                bdcJsydsyqLhxx.setProid(bdcXm.getProid());
                bdcJsydsyqLhxx.setWiid(bdcXm.getWiid());
                //这两个参数确定唯一一条记录
                bdcJsydsyqLhxx.setDjh(djsjZdJsydsyb.getDjh());
                bdcJsydsyqLhxx.setZrzh(djsjFwJsydzrzxx.getZrzh());

                bdcJsydsyqLhxx.setXmmc(djsjZdJsydsyb.getXmmc());
                bdcJsydsyqLhxx.setKfqymc(djsjZdJsydsyb.getKfqymc());
                bdcJsydsyqLhxx.setTdzl(djsjZdJsydsyb.getXmzl());
                bdcJsydsyqLhxx.setTdmj(djsjZdJsydsyb.getTdmj());
                bdcJsydsyqLhxx.setGcjd(djsjFwJsydzrzxx.getGcjd());
                bdcJsydsyqLhxx.setGhjzmj(djsjFwJsydzrzxx.getGhjzmj());
                bdcJsydsyqLhxx.setBzghmjzbl(djsjFwJsydzrzxx.getBzghmjbl());
                bdcJsydsyqLhxx.setScjzmj(djsjFwJsydzrzxx.getScjzmj());
                bdcJsydsyqLhxx.setBz(djsjFwJsydzrzxx.getBz());
                bdcJsydsyqLhxxList.add(bdcJsydsyqLhxx);
            }
        }
        return bdcJsydsyqLhxxList;
    }

    @Override
    public void updateFwljzGcjd(String proid, String gcjd) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                HashMap queryMap = new HashMap();
                queryMap.put("bdcdyh", bdcBdcdy.getBdcdyh());
                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(queryMap);
                if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                    DjsjFwHs djsjFwHs = djsjFwHsList.get(0);
                    HashMap map = new HashMap();
                    map.put("id", djsjFwHs.getFwDcbIndex());
                    map.put("gcjd", gcjd);
                    bdcJsydsyqLhxxMapper.updateFwljzGcjd(map);
                }
            }
        }
    }

    @Override
    public void updateDjsjFwLhxx(String proid) {
        Example example = new Example(BdcJsydsyqLhxx.class);
        example.createCriteria().andEqualTo("proid", proid);
        List<BdcJsydsyqLhxx> bdcJsydsyqLhxxList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcJsydsyqLhxxList)) {
            for (BdcJsydsyqLhxx bdcJsydsyqLhxx: bdcJsydsyqLhxxList) {
                DjsjFwJsydzrzxx djsjFwJsydzrzxx = new DjsjFwJsydzrzxx();
                djsjFwJsydzrzxx.setLszd(bdcJsydsyqLhxx.getDjh());
                djsjFwJsydzrzxx.setZrzh(bdcJsydsyqLhxx.getZrzh());
                djsjFwJsydzrzxx.setGcjd(bdcJsydsyqLhxx.getGcjd());
                djsjFwJsydzrzxx.setGhjzmj(bdcJsydsyqLhxx.getGhjzmj());
                djsjFwJsydzrzxx.setBzghmjbl(bdcJsydsyqLhxx.getBzghmjzbl());
                djsjFwJsydzrzxx.setScjzmj(bdcJsydsyqLhxx.getScjzmj());
                djsjFwJsydzrzxx.setBz(bdcJsydsyqLhxx.getBz());
                bdcJsydsyqLhxxMapper.updateDjsjFwJsydzrzxx(djsjFwJsydzrzxx);
            }
        }
    }

    @Override
    public void delBdcJsydLhxx(String proid) {
        Example example = new Example(BdcJsydsyqLhxx.class);
        example.createCriteria().andEqualTo("proid", proid);
        entityMapper.deleteByExample(example);
    }

    @Override
    public void batchDelBdcJsydLhxx(List<BdcXm> bdcXmList) {
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            bdcJsydsyqLhxxMapper.batchDelBdcJsydLhxx(bdcXmList);
        }
    }
}
