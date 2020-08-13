package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcZsSdMapper;
import cn.gtmap.estateplat.server.core.service.BdcBdcZsSdService;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2020/4/1
 * @description
 */
@Service
public class BdcBdcZsSdServiceImpl implements BdcBdcZsSdService {
    @Autowired
    private BdcBdcZsSdMapper bdcBdcZsSdMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;

    @Override
    public List<BdcBdcZsSd> getBdcZsSdList(HashMap map) {
        return bdcBdcZsSdMapper.getBdcZsSdList(map);
    }

    @Override
    public void delBdcBdcZsSd(BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            Example example = new Example(BdcBdcZsSd.class);
            example.createCriteria().andEqualTo("sdlcgzlid", bdcXm.getWiid());
            entityMapper.deleteByExample(example);
        }
    }

    @Override
    public void changeXzzt(String proid, String xzzt,String userid) {
        if(StringUtils.isNotBlank(proid)){
            List<BdcXmRel> bdcXmRelList=bdcXmRelService.queryBdcXmRelByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                for(BdcXmRel bdcXmRel:bdcXmRelList){
                    if(StringUtils.isNotBlank(bdcXmRel.getYproid())){
                        BdcXm bdcXm=bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                        if(bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getWiid())){
                            HashMap hashMap=new HashMap();
                            hashMap.put("sdlcgzlid",bdcXm.getWiid());
                            List<BdcBdcZsSd> bdcBdcZsSdList=getBdcZsSdList(hashMap);
                            if(CollectionUtils.isNotEmpty(bdcBdcZsSdList)){
                                for(BdcBdcZsSd bdcBdcZsSd:bdcBdcZsSdList){
                                    bdcBdcZsSd.setJssj(new Date());
                                    bdcBdcZsSd.setXzzt(Integer.parseInt(xzzt));
                                    if(StringUtils.isNotBlank(userid)){
                                        bdcBdcZsSd.setJsr(PlatformUtil.getCurrentUserName(userid));
                                    }
                                    entityMapper.saveOrUpdate(bdcBdcZsSd,bdcBdcZsSd.getSdid());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void changeYzsXzzt(String proid, String xzzt, String userid) {
        if (StringUtils.isNotBlank(proid)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        HashMap map = Maps.newHashMap();
                        map.put(ParamsConstants.PROID_LOWERCASE, bdcXmRel.getYproid());
                        List<BdcBdcZsSd> bdcBdcZsSdList = getBdcZsSdList(map);
                        if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                            for (BdcBdcZsSd bdcBdcZsSd : bdcBdcZsSdList) {
                                bdcBdcZsSd.setJssj(new Date());
                                bdcBdcZsSd.setXzzt(Integer.parseInt(xzzt));
                                if (StringUtils.isNotBlank(userid)) {
                                    bdcBdcZsSd.setJsr(PlatformUtil.getCurrentUserName(userid));
                                }
                                entityMapper.saveOrUpdate(bdcBdcZsSd, bdcBdcZsSd.getSdid());
                            }
                        }
                    }
                }
            }
        }
    }
}
