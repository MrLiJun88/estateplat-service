package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2020/4/21
 * @description 在建抵押转现房抵押
 */
@Service
public class BdcZjdyZXfdyDqServiceImpl implements BdcQtDqService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (projectPar != null) {
            if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                List<String> yxscqproidList = bdcXmService.getXsCqProidBybdcdyid(projectPar.getBdcdyid());
                if (CollectionUtils.isNotEmpty(yxscqproidList)) {
                    String xsCqProid = yxscqproidList.get(0);
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(xsCqProid);
                    //预转现坐落取首次登记坐落
                    if (bdcSpxx != null && projectPar.getBdcSpxx() != null) {
                        if (StringUtils.isNotBlank(bdcSpxx.getZl())) {
                            projectPar.getBdcSpxx().setZl(bdcSpxx.getZl());
                            entityMapper.saveOrUpdate(projectPar.getBdcSpxx(), projectPar.getBdcSpxx().getSpxxid());
                        }
                    }
                    //添加预告与首次登记项目关系，抵押和产权项目关系
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRel.setProid(projectPar.getProid());
                    bdcXmRel.setQjid(projectPar.getQjid());
                    bdcXmRel.setYproid(xsCqProid);
                    bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                    entityMapper.insertSelective(bdcXmRel);
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(xsCqProid);
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        String bdcqzh = "";
                        for (BdcZs bdcZs : bdcZsList) {
                            if (StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                                if (StringUtils.isNotBlank(bdcqzh)) {
                                    bdcqzh += "/" + bdcZs.getBdcqzh();
                                } else {
                                    bdcqzh = bdcZs.getBdcqzh();
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(bdcqzh)) {
                            projectPar.getBdcXm().setYbdcqzh(bdcqzh);
                            entityMapper.saveOrUpdate(projectPar.getBdcXm(), projectPar.getBdcXm().getProid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "zjdyzxfdy";
    }
}
