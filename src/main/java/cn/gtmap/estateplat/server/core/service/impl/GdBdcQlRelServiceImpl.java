package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcGdDyhRel;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.model.server.core.GdBdcQlRel;
import cn.gtmap.estateplat.server.core.service.BdcGdDyhRelService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.core.service.GdBdcQlRelService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zq
 * Date: 2016-4-20
 * Time: 16:58
 * Interpretation:
 */
@Service
public class GdBdcQlRelServiceImpl implements GdBdcQlRelService{
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;


    /**
     * @param qlid
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn List<GdBdcQlRel>
     * @description 获取过渡关系表
     */
    @Override
    public List<GdBdcQlRel> queryGdBdcQlListByQlid(final String qlid) {
        List<GdBdcQlRel> gdBdcQlRelList = null;
        if(StringUtils.isNotEmpty(qlid)){
            Example gdBdcQlRel = new Example(GdBdcQlRel.class);
            gdBdcQlRel.createCriteria().andEqualTo("qlid", qlid);
            gdBdcQlRelList = entityMapper.selectByExampleNotNull(GdBdcQlRel.class, gdBdcQlRel);
        }
        return gdBdcQlRelList;
    }

    @Override
    public List<GdBdcQlRel> queryGdBdcQlListByBdcid(String bdcid) {
        List<GdBdcQlRel> gdBdcQlRelList = null;
        if(StringUtils.isNotBlank(bdcid)){
            Example gdBdcQlRel = new Example(GdBdcQlRel.class);
            gdBdcQlRel.createCriteria().andEqualTo("bdcid", bdcid);
            gdBdcQlRelList = entityMapper.selectByExampleNotNull(GdBdcQlRel.class, gdBdcQlRel);
        }
        return gdBdcQlRelList;
    }

    @Override
    public List<GdBdcQlRel> getGdBdcQlRelByBdcdyh(String bdcdyh) {
        List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();

        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(bdcdyh, null);

        if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
            for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                String fwid = bdcGdDyhRel.getGdid();
                List<GdBdcQlRel> tempgdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(fwid, null);
                if (CollectionUtils.isNotEmpty(tempgdBdcQlRelList))
                    gdBdcQlRelList.addAll(tempgdBdcQlRelList);
            }
        } else {
            //没有匹配情况，使用档案号查询
            Map hsmap = new HashMap();
            hsmap.put("bdcdyh", bdcdyh);
            String dah = "";
            List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(hsmap);
            if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                dah = djsjFwHsList.get(0).getFcdah();
            }
            if (StringUtils.isNotBlank(dah)) {
                List<String> fwhsIds = gdFwService.getFwidByDah(dah);
                if (CollectionUtils.isNotEmpty(fwhsIds)) {
                    gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(fwhsIds.get(0), null);
                }
            }
        }
        return gdBdcQlRelList;
    }
}
