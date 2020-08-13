package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
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
 * @description 抵押权
 */
@Service
public class BdcDyaqDqServieImpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    private BdcYgService bdcYgService;
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
        BdcDyaq bdcDyaq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcDyaq) {
                bdcDyaq = (BdcDyaq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcDyaq = new BdcDyaq();
        }
        if (bdcDyaq != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcDyaq.setProid(projectPar.getBdcXm().getProid());
                    bdcDyaq.setYwh(projectPar.getBdcXm().getBh());
                    bdcDyaq.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcDyaq.setBdcdyid(projectPar.getBdcdyid());
                }
                if (projectPar.getBdcBdcdy() != null) {
                    bdcDyaq.setDybdclx(projectPar.getBdcBdcdy().getBdclx());
                }
            }
            if (StringUtils.isBlank(bdcDyaq.getQlid())) {
                bdcDyaq.setQlid(UUIDGenerator.generate18());
            }
            bdcDyaq.setQszt(Constants.QLLX_QSZT_LS);
            if (projectPar.getBdcSpxx() != null && projectPar.getBdcSpxx().getMj() != null) {
                bdcDyaq.setFwdymj(projectPar.getBdcSpxx().getMj());
            }
            if (isAdd) {
                qllxVoList.add(bdcDyaq);
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
        BdcDyaq bdcDyaq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcDyaq) {
                bdcDyaq = (BdcDyaq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcDyaq = new BdcDyaq();
        }
        if (bdcDyaq != null) {
            if (projectPar.getDjsjFwxx() == null) {
                projectParService.getQjsj(projectPar, "djsjFwxx");
            }
            if (projectPar.getDjsjFwxx() != null) {
                bdcDyaq.setFttdmj(projectPar.getDjsjFwxx().getFttdmj());
            }
//            if (projectPar.getBdcSpxx() != null && projectPar.getBdcSpxx().getZdzhmj() != null) {
//                bdcDyaq.setTddymj(projectPar.getBdcSpxx().getZdzhmj());
//            }
            if (projectPar.getBdcSpxx() != null && projectPar.getBdcSpxx().getMj() != null) {
                bdcDyaq.setFwdymj(projectPar.getBdcSpxx().getMj());
            }
            if (isAdd) {
                qllxVoList.add(bdcDyaq);
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
        boolean isAdd = true;
        BdcDyaq bdcDyaq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcDyaq) {
                bdcDyaq = (BdcDyaq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcDyaq = new BdcDyaq();
        }
        if (bdcDyaq != null) {
            if (projectPar != null && projectPar.getBdcBdcdy() != null && StringUtils.isNotBlank(projectPar.getBdcBdcdy().getBdcdyh())) {
                List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(projectPar.getBdcBdcdy().getBdcdyh(), Constants.QLLX_QSZT_XS.toString());
                if (CollectionUtils.isNotEmpty(bdcYgList)) {
                    for (BdcYg bdcYg : bdcYgList) {
                        if (bdcYg != null && (StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPFDY) || StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_QTYGSPFDY))) {
                            dozerMapper.map(bdcYg, bdcDyaq);
                        }
                    }
                }
            }
            if (isAdd) {
                qllxVoList.add(bdcDyaq);
            }
        }
        return qllxVoList;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_dyaq";
    }
}
