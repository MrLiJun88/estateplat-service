package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcQrd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.vo.Xgsj;
import cn.gtmap.estateplat.server.core.service.BdcQrdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.sj.InterfaceCodeBeanFactory;
import cn.gtmap.estateplat.server.sj.qr.BdcQrdXgsjService;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-30
 * @description 不动产确认单服务
 */
@Service
public class BdcQrdServiceImpl implements BdcQrdService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Set<BdcQrdXgsjService> bdcQrdXgsjServiceSet;

    /**
     * @param wiid 工作流实例ID
     * @return 不动产确认单
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 查询不动产确认单
     */
    @Override
    public BdcQrd queryBdcQrd(String wiid) {
        BdcQrd bdcQrd = getBdcQrd(wiid);
        if (bdcQrd != null) {
            Example example = new Example(BdcQrd.class);
            example.createCriteria().andEqualTo("wiid", wiid).andEqualTo("qrlx", bdcQrd.getQrlx());
            List<BdcQrd> bdcQrdList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcQrdList)) {
                bdcQrd = bdcQrdList.get(0);
            }
        }
        return bdcQrd;
    }

    /**
     * @param bdcQrd
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 保存不动产确认单
     */
    @Override
    public void saveBdcQrd(BdcQrd bdcQrd) {
        if (bdcQrd != null) {
            if (StringUtils.isNotBlank(bdcQrd.getWiid()) && StringUtils.isNotBlank(bdcQrd.getQrlx()) && StringUtils.isNotBlank(bdcQrd.getQrr())) {
                Example example = new Example(BdcQrd.class);
                example.createCriteria().andEqualTo("wiid", bdcQrd.getWiid()).andEqualTo("qrlx", bdcQrd.getQrlx()).andEqualTo("qrr", bdcQrd.getQrr());
                List<BdcQrd> bdcQrdList = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(bdcQrdList)) {
                    entityMapper.updateByPrimaryKeySelective(bdcQrd);
                } else {
                    bdcQrd.setId(UUIDGenerator.generate18());
                    entityMapper.insertSelective(bdcQrd);
                }
            } else {
                entityMapper.saveOrUpdate(bdcQrd, bdcQrd.getId());
            }
        }
    }

    /**
     * @param wiid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化不动产确认单
     */
    @Override
    public BdcQrd initBdcQrd(String wiid) {
        BdcQrd bdcQrd = queryBdcQrd(wiid);
        return bdcQrd;
    }

    @Override
    public Map initXgsj(String wiid) {
        List<Xgsj> xgsjList = queryXgsjByQrlx(wiid);
        Map map = initXgsjMap(xgsjList);
        return map;
    }

    private Map initXgsjMap(List<Xgsj> xgsjList) {
        Map<String, List<Xgsj>> map = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(xgsjList)) {
            for (int i = 0; i < xgsjList.size(); i++) {
                Xgsj xgsj = xgsjList.get(i);
                if (map.containsKey(xgsj.getSjms())) {
                    List<Xgsj> xgsjs = map.get(xgsj.getSjms());
                    xgsjs.add(xgsj);
                    map.put(xgsj.getSjms(), xgsjs);
                } else {
                    List<Xgsj> xgsjs = Lists.newArrayList();
                    xgsjs.add(xgsj);
                    map.put(xgsj.getSjms(), xgsjs);
                }
            }
        }
        return map;
    }

    private BdcQrd getBdcQrd(String wiid) {
        BdcQrd bdcQrd = null;
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            String sqlx = bdcXmList.get(0).getSqlx();
            List<Map> qrConfigList = ReadXmlProps.getJsPzByType("/sqlx-qrlx.json", "qr");
            if (CollectionUtils.isNotEmpty(qrConfigList)) {
                for (Map map : qrConfigList) {
                    if (map.containsKey("sqlx") && StringUtils.indexOf(map.get("sqlx").toString(), sqlx) > -1) {
                        bdcQrd = new BdcQrd();
                        bdcQrd.setQrlx(CommonUtil.formatEmptyValue(map.get("qrlx")));
                        bdcQrd.setQrms(CommonUtil.formatEmptyValue(map.get("qrms")));
                        bdcQrd.setWiid(wiid);
                        bdcQrd.setId(UUIDGenerator.generate18());
                        break;
                    }
                }
            }
        }
        return bdcQrd;
    }

    private List<Xgsj> queryXgsjByQrlx(String wiid) {
        List<Xgsj> xgsjList = Lists.newArrayList();
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            String sqlx = bdcXmList.get(0).getSqlx();
            List<Map> qrConfigList = ReadXmlProps.getJsPzByType("/sqlx-qrlx.json", "qr");
            if (CollectionUtils.isNotEmpty(qrConfigList)) {
                for (Map map : qrConfigList) {
                    if (map.containsKey("sqlx") && StringUtils.indexOf(map.get("sqlx").toString(), sqlx) > -1) {
                        BdcQrdXgsjService bdcQrdXgsjService = InterfaceCodeBeanFactory.getBean(bdcQrdXgsjServiceSet, CommonUtil.formatEmptyValue(map.get("qrlx")));
                        if (bdcQrdXgsjService != null) {
                            xgsjList = bdcQrdXgsjService.getXgsjList(map, bdcXmList.get(0));
                        }
                    }
                }
            }
        }
        return xgsjList;
    }
}
