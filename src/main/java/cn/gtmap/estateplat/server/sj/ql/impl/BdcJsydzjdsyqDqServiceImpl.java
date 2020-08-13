package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.BdcJsydzjdsyq;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/20
 * @description 建设用地、宅基地使用权
 */
@Service
public class BdcJsydzjdsyqDqServiceImpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    @Resource(name = "dozerQlMapper")
    private DozerBeanMapper dozerMapper;

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcJsydzjdsyq bdcJsydzjdsyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcJsydzjdsyq) {
                bdcJsydzjdsyq = (BdcJsydzjdsyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcJsydzjdsyq = new BdcJsydzjdsyq();
        }
        if (bdcJsydzjdsyq != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcJsydzjdsyq.setProid(projectPar.getBdcXm().getProid());
                    bdcJsydzjdsyq.setYwh(projectPar.getBdcXm().getBh());
                    bdcJsydzjdsyq.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcJsydzjdsyq.setBdcdyid(projectPar.getBdcdyid());
                }
            }
            if (StringUtils.isBlank(bdcJsydzjdsyq.getQlid())) {
                bdcJsydzjdsyq.setQlid(UUIDGenerator.generate18());
            }
            bdcJsydzjdsyq.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcJsydzjdsyq);
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
        BdcJsydzjdsyq bdcJsydzjdsyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcJsydzjdsyq) {
                bdcJsydzjdsyq = (BdcJsydzjdsyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcJsydzjdsyq = new BdcJsydzjdsyq();
        }
        if (bdcJsydzjdsyq != null) {
            if (projectPar.getDjsjZdxx() == null) {
                projectParService.getQjsj(projectPar, "djsjZdxx");
            }
            if (projectPar.getQjid() != null) {
                projectParService.getQjsj(projectPar, "djsjFwxx");
            }
            if (projectPar.getDjsjZdxx() != null) {
                if (projectPar.getDjsjZdxx().getFzmj() != null && projectPar.getDjsjZdxx().getFzmj() != 0) {
                    bdcJsydzjdsyq.setSyqmj(projectPar.getDjsjZdxx().getFzmj());
                } else {
                    bdcJsydzjdsyq.setSyqmj(projectPar.getDjsjZdxx().getScmj());
                }
                dozerMapper.map(projectPar.getDjsjZdxx(), bdcJsydzjdsyq);
            }
            if (StringUtils.equals(Constants.SQLX_TDSCDJXXBBDJ_DM, projectPar.getSqlx()) && projectPar.getDjsjFwxx() != null) {
                if (projectPar.getDjsjFwxx().getFttdmj() != null && projectPar.getDjsjFwxx().getFttdmj() != 0) {
                    bdcJsydzjdsyq.setSyqmj(projectPar.getDjsjFwxx().getFttdmj());
                }
            }
            if (StringUtils.isBlank(bdcJsydzjdsyq.getQlid())) {
                bdcJsydzjdsyq.setQlid(UUIDGenerator.generate18());
            }
            if (isAdd) {
                qllxVoList.add(bdcJsydzjdsyq);
            }
        }
        return qllxVoList;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取权利信息从预告中
     */
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
        return "bdc_jsydzjdsyq";
    }
}
