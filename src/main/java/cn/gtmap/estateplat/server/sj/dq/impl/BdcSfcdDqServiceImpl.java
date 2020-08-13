package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/4/16
 * @description 获取司法裁定义务人信息
 */
@Service
public class BdcSfcdDqServiceImpl implements BdcQlrDqService {
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcXmService bdcXmService;

    @Override
    public List<BdcQlr> getCreateQlr(ProjectPar projectPar) {
        List<BdcQlr> bdcQlrList = new ArrayList<>();
        if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            //获取最后一次的裁定解封项目的proid
            List<String> cdproidList= bdcXmService.queryCdBdcXmByBdcdyh(projectPar.getBdcdyh());
            if (CollectionUtils.isNotEmpty(cdproidList)) {
                //获取最后一次裁定解封的义务人
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(cdproidList.get(0));
                if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                    for (BdcQlr bdcQlr : bdcYwrList) {
                        bdcQlr.setQlrid(UUIDGenerator.generate18());
                        bdcQlr.setProid(projectPar.getProid());
                        bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                        bdcQlr.setQlrdlr(null);
                        bdcQlr.setQlrdljg(null);
                        bdcQlr.setQlrdlrdh(null);
                        bdcQlr.setQlrdlrzjh(null);
                        bdcQlr.setQlrdlrzjzl(null);
                        bdcQlrList.add(bdcQlr);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(bdcQlrList)) {
            bdcQlrList = Collections.EMPTY_LIST;
        }
        return bdcQlrList;
    }


    @Override
    public String getIntetfacaCode() {
        return "sfcd";
    }
}
