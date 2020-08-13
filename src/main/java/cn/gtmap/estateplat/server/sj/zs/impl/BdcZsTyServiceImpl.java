package cn.gtmap.estateplat.server.sj.zs.impl;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.sj.zs.BdcZsTyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description 证书通用方法
 */
@Service
public class BdcZsTyServiceImpl implements BdcZsTyService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    /**
     * @param wiid 工作流实例ID
     * @param lclx 流程类型
     * @return 证书类型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取证书类型
     */
    @Override
    public Map getZsFont(String wiid, String lclx) {
        Map<String, String> map = Maps.newHashMap();
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            if (StringUtils.equals(lclx, Constants.LCLX_ZH)) {
                for (BdcXm bdcXm : bdcXmList) {
                    Map bdczsMap = bdcZsService.getInfoForCreateBdcqzh(bdcXm);
                    if (MapUtils.isNotEmpty(bdczsMap)) {
                        map.putAll(bdczsMap);
                    }
                }
            } else {
                Map bdczsMap = bdcZsService.getInfoForCreateBdcqzh(bdcXmList.get(0));
                if (MapUtils.isNotEmpty(bdczsMap)) {
                    for (Object key : bdczsMap.keySet()) {
                        map.put(Constants.DEFULT_STR, bdczsMap.get(key).toString());
                        break;
                    }
                }
            }
        }
        return map;
    }

    /**
     * @param bdcZs
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化纯房证书
     */
    @Override
    public BdcZs initCfzs(BdcZs bdcZs, BdcXm bdcXm) {
        if (bdcZs != null && bdcXm != null && CommonUtil.indexOfStrs(Constants.QLLX_TDFW, bdcXm.getQllx())) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
            if (bdcSpxx != null) {
                if ((bdcSpxx.getZdzhmj() == null || bdcSpxx.getZdzhmj() == 0)) {
                    bdcZs.setMj("---/房屋建筑面积" + bdcSpxx.getFzmj() + "平方米");
                }
            }
        }
        return bdcZs;
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化纯房证书
     */
    @Override
    public String getZsqllx(String qllx, BdcXm bdcXm) {
        if (bdcXm != null && CommonUtil.indexOfStrs(Constants.QLLX_TDFW, bdcXm.getQllx())) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
            if (bdcSpxx != null) {
                if ((bdcSpxx.getZdzhmj() == null || bdcSpxx.getZdzhmj() == 0)) {
                    qllx = "---/房屋所有权";
                }
            }
        }
        return qllx;
    }
}
