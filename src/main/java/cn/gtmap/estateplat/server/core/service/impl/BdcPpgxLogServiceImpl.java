package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcPpgxLog;
import cn.gtmap.estateplat.model.server.core.BdcPpgxXm;
import cn.gtmap.estateplat.server.core.service.BdcPpgxLogService;
import cn.gtmap.estateplat.server.core.service.BdcPpgxService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/26
 * @description 不动产匹配关系日志表 服务
 */
@Service
public class BdcPpgxLogServiceImpl implements BdcPpgxLogService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcPpgxService bdcPpgxService;


    @Override
    public void insertBdcPpgxLogByMap(Map map) {
        if(map != null){
            String userid = map.get(ParamsConstants.USERID_HUMP) != null ? map.get(ParamsConstants.USERID_HUMP).toString() : null;
            String czr = PlatformUtil.getCurrentUserName(userid);
            BdcPpgxLog bdcPpgxLog = new BdcPpgxLog();
            bdcPpgxLog.setLogid(UUIDGenerator.generate18());
            bdcPpgxLog.setQjid(map.get(ParamsConstants.QJID_LOWERCASE) != null ? map.get(ParamsConstants.QJID_LOWERCASE).toString() : null);
            bdcPpgxLog.setBdcdyh(map.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? map.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null);
            if(StringUtils.isNotBlank(czr)){
                bdcPpgxLog.setCzr(czr);
            }
            bdcPpgxLog.setFwbdcdyh(map.get(ParamsConstants.FWBDCDYH_LOWERCASE) != null ? map.get(ParamsConstants.FWBDCDYH_LOWERCASE).toString() : null);
            bdcPpgxLog.setFwproid(map.get(ParamsConstants.FWPROID_LOWERCASE) != null ? map.get(ParamsConstants.FWPROID_LOWERCASE).toString() : null);
            bdcPpgxLog.setPpsj(new Date());
            bdcPpgxLog.setTdproid(map.get(ParamsConstants.TDPROID_LOWERCASE) != null ? map.get(ParamsConstants.TDPROID_LOWERCASE).toString() : null);
            bdcPpgxLog.setTdbdcdyh(map.get(ParamsConstants.TDBDCDYH_LOWERCASE) != null ? map.get(ParamsConstants.TDBDCDYH_LOWERCASE).toString() : null);
            bdcPpgxLog.setCzlx(map.get(ParamsConstants.CZLX_LOWERCASE) != null ? map.get(ParamsConstants.CZLX_LOWERCASE).toString() : null);
            saveBdcPpgxLog(bdcPpgxLog);
            updateBdcPpgxXm(bdcPpgxLog.getBdcdyh(),bdcPpgxLog.getLogid());
        }
    }

    private void updateBdcPpgxXm(String bdcdyh, String logid) {
        List<BdcPpgxXm> bdcPpgxXmList=bdcPpgxService.getBdcPpgxXmByBdcdyh(bdcdyh);
        if(CollectionUtils.isNotEmpty(bdcPpgxXmList)){
            for (BdcPpgxXm bdcPpgxXm : bdcPpgxXmList) {
                if(StringUtils.isBlank(bdcPpgxXm.getPplogid())){
                    bdcPpgxXm.setPplogid(logid);
                    entityMapper.saveOrUpdate(bdcPpgxXm,bdcPpgxXm.getPpxmid());
                }
            }
        }
    }

    @Override
    public void saveBdcPpgxLog(BdcPpgxLog bdcPpgxLog) {
        if(bdcPpgxLog != null && StringUtils.isNotBlank(bdcPpgxLog.getLogid())){
            entityMapper.saveOrUpdate(bdcPpgxLog,bdcPpgxLog.getLogid());
        }
    }

    @Override
    public List<BdcPpgxLog> getBdcPpgxLogByMap(Map map) {
        List<BdcPpgxLog> bdcPpgxLogList = null;
        if(map != null){
            Example example = new Example(BdcPpgxLog.class);
            Example.Criteria criteria = example.createCriteria();
            example.setOrderByClause("ppsj asc");
            if(map.get(ParamsConstants.FWPROID_LOWERCASE) != null && StringUtils.isNotBlank(map.get(ParamsConstants.FWPROID_LOWERCASE).toString())){
                criteria.andEqualTo(ParamsConstants.FWPROID_LOWERCASE, map.get(ParamsConstants.FWPROID_LOWERCASE).toString());
            }
            if(map.get(ParamsConstants.TDPROID_LOWERCASE) != null && StringUtils.isNotBlank(map.get(ParamsConstants.TDPROID_LOWERCASE).toString())){
                criteria.andEqualTo(ParamsConstants.TDPROID_LOWERCASE, map.get(ParamsConstants.TDPROID_LOWERCASE).toString());
            }
            if(map.get(ParamsConstants.BDCDYH_LOWERCASE) != null && StringUtils.isNotBlank(map.get(ParamsConstants.BDCDYH_LOWERCASE).toString())){
                criteria.andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, map.get(ParamsConstants.BDCDYH_LOWERCASE).toString());
            }
            if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
                bdcPpgxLogList = entityMapper.selectByExample(BdcPpgxLog.class, example);
            }
        }
        return bdcPpgxLogList;
    }

    @Override
    public List<BdcPpgxLog> getBdcPpgxLogOrderByPpsj(BdcPpgxLog bdcPpgxLog) {
        List<BdcPpgxLog> bdcPpgxLogList = null;
        if(bdcPpgxLog!= null){
            Example example = new Example(BdcPpgxLog.class);
            Example.Criteria criteria = example.createCriteria();
            example.setOrderByClause("ppsj desc");
            if(StringUtils.isNotBlank(bdcPpgxLog.getFwproid())){
                criteria.andEqualTo(ParamsConstants.FWPROID_LOWERCASE,bdcPpgxLog.getFwproid());
            }
            if(StringUtils.isNotBlank(bdcPpgxLog.getTdproid())){
                criteria.andEqualTo(ParamsConstants.TDPROID_LOWERCASE,bdcPpgxLog.getTdproid());
            }
            if( CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
                bdcPpgxLogList = entityMapper.selectByExample(BdcPpgxLog.class, example);
            }
        }
        return bdcPpgxLogList;
    }
}
