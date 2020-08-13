package cn.gtmap.estateplat.server.sj.zs.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcYg;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.sj.zs.BdcZsGetbhService;
import cn.gtmap.estateplat.server.sj.zs.BdcZsTyService;
import cn.gtmap.estateplat.server.sj.zs.CompleteZsInfoService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
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
 * @description 补全证书信息
 */
@Service
public class CompleteZsInfoServiceImpl implements CompleteZsInfoService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsTyService bdcZsTyService;
    @Autowired
    private BdcZsGetbhService bdcZsGetbhService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcYgService bdcYgService;

    /**
     * @param wiid 工作流实例ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 补全证书信息
     */
    @Override
    public void completeZsInfo(String wiid) {
        List<BdcZs> bdcZss = Lists.newArrayList();
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        String lclx = bdcXmService.getLclx(wiid);
        Map zsfontMap = bdcZsTyService.getZsFont(wiid, lclx);
        if (MapUtils.isNotEmpty(zsfontMap) && CollectionUtils.isNotEmpty(bdcXmList)) {
            if (StringUtils.equals(lclx, Constants.LCLX_ZH)) {
                List<BdcXm> cqXmList = Lists.newArrayList();
                List<BdcXm> dyaqXmList = Lists.newArrayList();
                List<BdcZs> cqBdcZsList = Lists.newArrayList();
                List<BdcZs> dyaqBdcZsList = Lists.newArrayList();
                for (int i = 0; i < bdcXmList.size(); i++) {
                    BdcXm bdcXm = bdcXmList.get(i);
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        bdcZsGetbhService.getbh(bdcZsList, zsfontMap.get(bdcXm.getProid()).toString(), bdcXm);
                        if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_YGDJ) && StringUtils.isNotBlank(bdcXm.getProid())) {
                            BdcYg bdcYg = bdcYgService.getBdcYgByProid(bdcXm.getProid());
                            if (bdcYg != null && StringUtils.isNotBlank(bdcYg.getYgdjzl()) && CollectionUtils.isNotEmpty(bdcZsList)) {
                                if (StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPF)) {
                                    cqXmList.add(bdcXm);
                                    cqBdcZsList.add(bdcZsList.get(0));
                                } else if (StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPFDY)) {
                                    dyaqXmList.add(bdcXm);
                                    dyaqBdcZsList.add(bdcZsList.get(0));
                                }
                            }
                        } else {
                            if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                                dyaqXmList.add(bdcXm);
                                dyaqBdcZsList.add(bdcZsList.get(0));
                            } else {
                                cqXmList.add(bdcXm);
                                cqBdcZsList.add(bdcZsList.get(0));
                            }
                        }
                    }
                    bdcZss.addAll(bdcZsList);
                }
                bdcZsService.dealHbZsInfo(dyaqBdcZsList, cqBdcZsList, dyaqXmList, cqXmList);
            } else {
                List<BdcZs> bdcZsList = bdcZsService.getBdcZsListByWiidOrderByZl(wiid);
                bdcZsGetbhService.getbh(bdcZsList, zsfontMap.get(Constants.DEFULT_STR).toString(), bdcXmList.get(0));
                bdcZss.addAll(bdcZsList);
            }
        }
        if (CollectionUtils.isNotEmpty(bdcZss)) {
            bdcZsService.updateBdcZs(bdcZss);
        }
    }
}
