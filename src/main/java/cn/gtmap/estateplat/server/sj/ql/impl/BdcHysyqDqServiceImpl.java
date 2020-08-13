package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.BdcHysyq;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/20
 * @description 海域使用权
 */
@Service
public class BdcHysyqDqServiceImpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    private QllxService qllxService;

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcHysyq bdcHysyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcHysyq) {
                bdcHysyq = (BdcHysyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcHysyq = new BdcHysyq();
        }
        if (bdcHysyq != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcHysyq.setProid(projectPar.getBdcXm().getProid());
                    bdcHysyq.setYwh(projectPar.getBdcXm().getBh());
                    bdcHysyq.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcHysyq.setBdcdyid(projectPar.getBdcdyid());
                }
            }
            if (StringUtils.isBlank(bdcHysyq.getQlid())) {
                bdcHysyq.setQlid(UUIDGenerator.generate18());
            }
            bdcHysyq.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcHysyq);
            }
        }
        return qllxVoList;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取权利信息从权籍
     */
    @Override
    public List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcHysyq bdcHysyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcHysyq) {
                bdcHysyq = (BdcHysyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcHysyq = new BdcHysyq();
        }
        if (bdcHysyq != null) {
            if (projectPar.getDjsjZhxx() == null) {
                projectParService.getQjsj(projectPar, "djsjZhxx");
            }
            if (projectPar.getDjsjZhxx() != null) {
                bdcHysyq = qllxService.getHysyqFromZhxx(bdcHysyq, projectPar.getDjsjZhxx());
            }
            if (isAdd) {
                qllxVoList.add(bdcHysyq);
            }
        }
        return qllxVoList;
    }

    @Override
    public List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        return null;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_hysyq";
    }
}
