package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcYg;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-12
 * @description
 */
@Service
public class BdcQtYgTbJeService implements BdcQtDqService {

    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(projectPar.getWiid());
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            Map<String, BdcYg> bdcYgMap = Maps.newHashMap();
            for (BdcXm bdcXm : bdcXmList) {
                BdcYg bdcYg = bdcYgService.getBdcYgByProid(bdcXm.getProid());
                if (bdcYg != null) {
                    bdcYgMap.put(bdcYg.getYgdjzl(), bdcYg);
                }
            }
            if (MapUtils.isNotEmpty(bdcYgMap)) {
                Double je = 0.0;
                if (bdcYgMap.containsKey(Constants.YGDJZL_YGSPF)) {
                    BdcYg bdcYg = bdcYgMap.get(Constants.YGDJZL_YGSPF);
                    if (bdcYg.getQdjg() != null && bdcYg.getQdjg() > 0) {
                        je = bdcYg.getQdjg();
                    }
                }
                if (bdcYgMap.containsKey(Constants.YGDJZL_QTBDCMMYG)) {
                    BdcYg bdcYg = bdcYgMap.get(Constants.YGDJZL_YGSPF);
                    if (bdcYg.getQdjg() != null && bdcYg.getQdjg() > 0) {
                        je = bdcYg.getQdjg();
                    }
                }
                if (je > 0) {
                    if (bdcYgMap.containsKey(Constants.YGDJZL_YGSPFDY)) {
                        BdcYg bdcYg = bdcYgMap.get(Constants.YGDJZL_YGSPFDY);
                        bdcYg.setQdjg(null);
                        bdcYg.setFwpgjg(je);
                        entityMapper.updateByPrimaryKeyNull(bdcYg);
                    }
                    if (bdcYgMap.containsKey(Constants.YGDJZL_QTYGSPFDY)) {
                        BdcYg bdcYg = bdcYgMap.get(Constants.YGDJZL_QTYGSPFDY);
                        bdcYg.setQdjg(null);
                        bdcYg.setFwpgjg(je);
                        entityMapper.updateByPrimaryKeyNull(bdcYg);
                    }
                }
            }
        }
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "ygtbje";
    }
}
