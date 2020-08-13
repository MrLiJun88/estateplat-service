package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/19
 * @description 房地产权多幢
 */
@Service
public class BdcFdcqDzDqServiceimpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    @Resource(name = "dozerQlMapper")
    private DozerBeanMapper dozerMapper;
    @Autowired
    private EntityMapper entityMapper;

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcFdcqDz bdcFdcqDz = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcFdcqDz) {
                bdcFdcqDz = (BdcFdcqDz) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcFdcqDz = new BdcFdcqDz();
        }
        if (bdcFdcqDz != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcFdcqDz.setProid(projectPar.getBdcXm().getProid());
                    bdcFdcqDz.setYwh(projectPar.getBdcXm().getBh());
                    bdcFdcqDz.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcFdcqDz.setBdcdyid(projectPar.getBdcdyid());
                }
            }
            if (StringUtils.isBlank(bdcFdcqDz.getQlid())) {
                bdcFdcqDz.setQlid(UUIDGenerator.generate18());
            }
            bdcFdcqDz.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcFdcqDz);
            }
        }
        return qllxVoList;
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取权利信息从权籍
     */
    @Override
    public List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcFdcqDz bdcFdcqDz = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcFdcqDz) {
                bdcFdcqDz = (BdcFdcqDz) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcFdcqDz = new BdcFdcqDz();
        }
        if (bdcFdcqDz != null) {
            if (projectPar.getDjsjFwxx() == null) {
                projectParService.getQjsj(projectPar, "djsjFwxx");
            }
            if (projectPar.getDjsjZdxx() == null) {
                projectParService.getQjsj(projectPar, "djsjZdxx");
            }
            if (projectPar.getDjsjFwxx() != null) {
                dozerMapper.map(projectPar.getDjsjFwxx(), bdcFdcqDz);
                //处理房屋分幢信息
                List<DjsjFwzbxx> djsjFwzbxxList = projectPar.getDjsjFwxx().getFwzbxxList();
                List<BdcFwfzxx> bdcFwfzxxList = new ArrayList<BdcFwfzxx>();
                if (CollectionUtils.isNotEmpty(djsjFwzbxxList)) {
                    for (int i = 0; i < djsjFwzbxxList.size(); i++) {
                        DjsjFwzbxx djsjFwzbxx = djsjFwzbxxList.get(i);
                        BdcFwfzxx bdcFwfzxx = new BdcFwfzxx();
                        bdcFwfzxx.setFzid(UUIDGenerator.generate18());
                        bdcFwfzxx.setQlid(bdcFdcqDz.getQlid());
                        dozerMapper.map(djsjFwzbxx, bdcFwfzxx);
                        entityMapper.saveOrUpdate(bdcFwfzxx, bdcFwfzxx.getFzid());
                        bdcFwfzxxList.add(bdcFwfzxx);
                    }
                }
                bdcFdcqDz.setFwfzxxList(bdcFwfzxxList);
            }
            if (projectPar.getDjsjZdxx() != null) {
                dozerMapper.map(projectPar.getDjsjZdxx(), bdcFdcqDz);
            }
            if (isAdd) {
                qllxVoList.add(bdcFdcqDz);
            }
        }
        return qllxVoList;
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取权利信息从预告中
     */
    @Override
    public List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        return qllxVoList;
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_fdcq_dz";
    }
}
