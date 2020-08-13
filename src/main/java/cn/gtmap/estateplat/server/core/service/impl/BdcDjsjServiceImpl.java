package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.mapper.BdcDjsjMapper;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 宗地/林权/房屋等权籍信息
 * Created by lst on 2015/3/17.
 */
@Repository
public class BdcDjsjServiceImpl implements BdcDjsjService {
    @Autowired
    private BdcDjsjMapper bdcDjsjMapper;
    @Autowired
    private DjSjMapper djSjMapper;
    @Autowired
    private DjxxMapper djxxMapper;
    @Autowired
    private DjsjFwService djsjFwService;

    @Override
    @Transactional(readOnly = true)
    public DjsjLqxx getDjsjLqxx(final String id) {
        return bdcDjsjMapper.getDjsjLqxx(id);
    }
    @Override
    @Transactional(readOnly = true)
    public DjsjNydDcb getDjsjTtxx(final String id) {
        return bdcDjsjMapper.getDjsjTtxx(id);
    }

    public String getDjsjQsxz(final String bdcdyh) {
        return bdcDjsjMapper.getDjsjQsxz(bdcdyh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjZdxx> getDjsjZdxx(final String id) {
        return bdcDjsjMapper.getDjsjZdxx(id);
    }

    @Override
    public List<DjsjZdxx> getDjsjZdxxWithZd(String id) {
        return bdcDjsjMapper.getDjsjZdxxWithZd(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjZdxx> getDjsjNydxx(final String id) {
        return bdcDjsjMapper.getDjsjNydxx(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjZdxx> getDjsjNydxxByDjh(final String djh) {
        return bdcDjsjMapper.getDjsjNydxxByDjh(djh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjQszdDcb> getDjsjQszdDcb(final String djh) {
        return djSjMapper.getDjsjQszdDcb(djh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjFwxx> getDjsjFwQlr(final String id) {
        return bdcDjsjMapper.getDjsjFwQlr(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjQszdDcb> getDjsjQszdDcbByQszdDjdcbIndex(final String qszdDjdcbIndex) {
        return djSjMapper.getDjsjQszdDcbByQszdDjdcbIndex(qszdDjdcbIndex);
    }

    @Override
    @Transactional(readOnly = true)
    public String getBdcdyfwlxByBdcdyh(final String bdcdyh) {
        if (StringUtils.isNotBlank(bdcdyh)) {
            return djSjMapper.getBdcdyfwlxByBdcdyh(bdcdyh);
        } else {
            return "";
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<DjsjZdxx> getDjsjZdxxForDjh(final String djh) {
        if (StringUtils.isNotBlank(djh)) {
            return bdcDjsjMapper.getDjsjZdxxForDjh(djh);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjZhxx> getDjsjZhxxForDjh(final String djh) {
        return bdcDjsjMapper.getDjsjZhxxForDjh(djh);
    }

    @Override
    @Transactional(readOnly = true)
    public DjsjZhxx getDjsjZhxx(final String id) {
        return bdcDjsjMapper.getDjsjZhxx(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjLqxx> getDjsjLqxxByDjh(@Param("djh") final String djh) {
        return bdcDjsjMapper.getDjsjLqxxByDjh(djh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DJsjZhjnbdyjlb> getDJsjZhjnbdyjlb(final String zhdm) {
        return bdcDjsjMapper.getDJsjZhjnbdyjlb(zhdm);
    }

    @Override
    public List<String> getOldDjh(final String djid) {
        return bdcDjsjMapper.getOldDjh(djid);
    }

    @Override
    public Integer queryBdcdyCountByDjh(final String djh) {
        return bdcDjsjMapper.queryBdcdyCountByDjh(djh);
    }

    @Override
    public String getDjidByBdcdyh(String bdcdyh, String bdclx) {
        Map map = Maps.newHashMap();
        map.put("hhSearch", bdcdyh);
        map.put("bdclx", bdclx);
        return djxxMapper.getDjid(map);
    }

    /**
     * @author bianwen
     * @description 获取构筑物信息
     */
    @Override
    public DjsjGzwxx getDjsjGzwxx(String id) {
        return bdcDjsjMapper.getDjsjGzwxx(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDjsjZdIsSyzdByDjid(String djid) {
        return bdcDjsjMapper.getDjsjZdIsSyzdByDjid(djid);
    }

    @Override
    public List<DjsjZdxx> getDjsjZdxxByProid(String proid) {
        return djSjMapper.getDjsjZdxx(proid);
    }

    @Override
    public DjsjCbzdDcb getDjsjCbzdDcbByDjid(String djid) {
        return djSjMapper.getDjsjCbzdDcbByDjid(djid);
    }

    @Override
    public List<DjsjNydDcb> getDjsjNydDcbByDjh(String djh) {
        return djSjMapper.getDjsjNydDcbByDjh(djh);
    }

    @Override
    public BdcQszdZdmj getBdcQszdZdmj(String djh) {
        return djSjMapper.getBdcQszdZdmj(djh);
    }

    @Override
    public List<DjsjCbzdCbf> getDjsjCbzdCbfByDcbid(String dcbid) {
        return djSjMapper.getDjsjCbzdCbfByDcbid(dcbid);
    }

    @Override
    public List<DjsjCbzdFbf> getDjsjCbzdFbfByDcbid(String dcbid) {
        return djSjMapper.getDjsjCbzdFbfByDcbid(dcbid);
    }

    @Override
    public InitVoFromParm getDjxx(String bdcdyh, String djid, String bdclx) {
        DjsjFwxx djsjFwxx = null;
        DjsjLqxx djsjLqxx = null;
        DjsjZdxx djsjZdxx = null;
        DjsjZhxx djsjZhxx = null;
        DjsjQszdDcb djsjQszdDcb = null;
        DjsjCbzdDcb cbzdDcb = null;
        DjsjNydDcb djsjNydDcb = null;
        List<DjsjZdxx> djsjZdxxList = null;
        List<DjsjZhxx> djsjZhxxList = null;
        List<DjsjFwxx> djsjFwQlrList = null;

        List<DjsjQszdDcb> djsjQszdDcbList = null;
        List<DjsjNydDcb> djsjNydDcbList = null;
        InitVoFromParm initVoFromParm = new InitVoFromParm();

        if (StringUtils.isBlank(djid) && StringUtils.isNotBlank(bdcdyh)) {
            djid = getDjidByBdcdyh(bdcdyh, bdclx);
        }
        if (StringUtils.isNotBlank(djid) && Constants.BDCLX_TDFW.equals(bdclx)) {
            djsjFwxx = djsjFwService.getDjsjFwxx(djid);
            if (djsjFwxx != null) {
                if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                    djsjFwQlrList = getDjsjFwQlr(djsjFwxx.getId());
                }
            }
            /**
             * zzhw
             * 房屋信息中需要查询土地信息继承过来（tdzl、fzmj）
             */
            if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && StringUtils.length(djsjFwxx.getBdcdyh()) > 19) {
                djsjZdxxList = getDjsjZdxxForDjh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
            }
            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                djsjZdxx = djsjZdxxList.get(0);
            }
        }

        if (StringUtils.isNotBlank(djid) && Constants.BDCLX_TDSL.equals(bdclx)) {
            djsjLqxx = getDjsjLqxx(djid);
        }
        if (Constants.BDCLX_HY.equals(bdclx)) {
            if (StringUtils.isNotBlank(bdcdyh) && StringUtils.length(bdcdyh) > 19) {
                djsjZhxxList = getDjsjZhxxForDjh(StringUtils.substring(bdcdyh, 0, 19));
            }
            if (CollectionUtils.isNotEmpty(djsjZhxxList)) {
                djsjZhxx = djsjZhxxList.get(0);
            }
            if (djsjZhxx == null && StringUtils.isNotBlank(djid)) {
                djsjZhxx = getDjsjZhxx(djid);
            }
        }

        if (StringUtils.isNotBlank(djid) && (Constants.BDCLX_TD.equals(bdclx) || Constants.BDCLX_TDQT.equals(bdclx))) {
            cbzdDcb = djSjMapper.getDjsjCbzdDcbByDjid(djid);
            if (cbzdDcb != null && StringUtils.isNotBlank(cbzdDcb.getDjh())) {
                djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(cbzdDcb.getDjh());
            }
            if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                djsjNydDcb = djsjNydDcbList.get(0);
            }
        }

        if (StringUtils.isNotBlank(djid) && StringUtils.isNotBlank(bdclx) && bdclx.indexOf(Constants.BDCLX_TD) > -1) {
            djsjQszdDcbList = getDjsjQszdDcb(djid);
            if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
                djsjQszdDcb = djsjQszdDcbList.get(0);
            }
            djsjZdxxList = getDjsjZdxx(djid);
            //zwq 取农用地调查表信息
            if (CollectionUtils.isEmpty(djsjZdxxList)) {
                djsjZdxxList = getDjsjNydxx(djid);
                if (StringUtils.equals(bdclx, Constants.BDCLX_LQ) && (CollectionUtils.isEmpty(djsjZdxxList)) && StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() > 19) {
                    String djh = StringUtils.substring(bdcdyh, 0, 19);
                    djsjZdxxList = getDjsjNydxxByDjh(djh);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            djsjZdxx = djsjZdxxList.get(0);
        }

        initVoFromParm.setDjsjFwxx(djsjFwxx);
        initVoFromParm.setDjsjLqxx(djsjLqxx);
        initVoFromParm.setDjsjZdxx(djsjZdxx);
        initVoFromParm.setDjsjZhxx(djsjZhxx);
        initVoFromParm.setDjsjQszdDcb(djsjQszdDcb);
        initVoFromParm.setCbzdDcb(cbzdDcb);
        initVoFromParm.setDjsjNydDcb(djsjNydDcb);
        initVoFromParm.setDjsjFwQlrList(djsjFwQlrList);
        initVoFromParm.setDjsjQszdDcbList(djsjQszdDcbList);
        initVoFromParm.setDjsjNydDcbList(djsjNydDcbList);
        initVoFromParm.setDjsjZdxxList(djsjZdxxList);
        initVoFromParm.setDjsjZhxxList(djsjZhxxList);
        return initVoFromParm;
    }

    @Override
    public List<DjsjFwHs> getDjsjFwHsByBdcdyh(String bdcdyh) {
        List<DjsjFwHs> djsjFwHsList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            Map paramMap = Maps.newHashMap();
            paramMap.put("bdcdyh", bdcdyh);
            djsjFwHsList = djsjFwService.getDjsjFwHs(paramMap);
        }
        return djsjFwHsList;
    }
}