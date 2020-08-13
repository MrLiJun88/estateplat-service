package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.DjsjFwxx;
import cn.gtmap.estateplat.model.server.core.DjsjZdxx;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcSpxxDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcYwrDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/13 0013
 * @description 不动产数据获取---->权籍
 */
@Service
public class BdcDataDqQjServiceImpl implements BdcQlrDqService, BdcYwrDqService, BdcSpxxDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private DjsjFwMapper djsjFwMapper;

    /**
     * @param projectPar
     * @return 权利人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时权利人
     */
    @Override
    public List<BdcQlr> getCreateQlr(ProjectPar projectPar) {
        List<BdcQlr> bdcQlrList = new ArrayList<>();
        if (CollectionUtils.isEmpty(projectPar.getDjsjFwQlrList())) {
            projectParService.getQjsj(projectPar, "djsjFwQlrList");
        }
        if (CollectionUtils.isNotEmpty(projectPar.getDjsjFwQlrList())) {
            for (DjsjFwxx fwxx : projectPar.getDjsjFwQlrList()) {
                BdcQlr bdcQlr = new BdcQlr();
                bdcQlr.setQlrid(UUIDGenerator.generate());
                bdcQlr.setProid(projectPar.getProid());
                bdcQlr = bdcQlrService.getBdcQlrFromFw(fwxx, bdcQlr);
                bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                    bdcQlrList.add(bdcQlr);
                }
            }
        } else {
            if (StringUtils.isNotBlank(projectPar.getDjh())) {
                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(projectPar.getDjh());
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    for (DjsjZdxx zdxx : djsjZdxxList) {
                        BdcQlr bdcQlr = new BdcQlr();
                        bdcQlr.setQlrid(UUIDGenerator.generate());
                        bdcQlr.setProid(projectPar.getProid());
                        bdcQlr = bdcQlrService.getBdcQlrFromZd(zdxx, bdcQlr);
                        bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                        if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                            bdcQlrList.add(bdcQlr);
                        }
                    }
                }
            }
        }
        return bdcQlrList;
    }

    /**
     * @param projectPar
     * @return 义务人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时义务人
     */
    @Override
    public List<BdcQlr> getCreateYwr(ProjectPar projectPar) {
        List<BdcQlr> bdcQlrList = new ArrayList<>();
        if (CollectionUtils.isEmpty(projectPar.getDjsjFwQlrList())) {
            projectParService.getQjsj(projectPar, "djsjFwQlrList");
        }
        if (CollectionUtils.isNotEmpty(projectPar.getDjsjFwQlrList())) {
            for (DjsjFwxx fwxx : projectPar.getDjsjFwQlrList()) {
                BdcQlr bdcQlr = new BdcQlr();
                bdcQlr.setQlrid(UUIDGenerator.generate());
                bdcQlr.setProid(projectPar.getProid());
                bdcQlr = bdcQlrService.getBdcQlrFromFw(fwxx, bdcQlr);
                bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                    bdcQlrList.add(bdcQlr);
                }
            }
        } else {
            if (StringUtils.isNotBlank(projectPar.getDjh())) {
                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(projectPar.getDjh());
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    for (DjsjZdxx zdxx : djsjZdxxList) {
                        BdcQlr bdcQlr = new BdcQlr();
                        bdcQlr.setQlrid(UUIDGenerator.generate());
                        bdcQlr.setProid(projectPar.getProid());
                        bdcQlr = bdcQlrService.getBdcQlrFromZd(zdxx, bdcQlr);
                        bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                        if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                            bdcQlrList.add(bdcQlr);
                        }
                    }
                }
            }
        }
        return bdcQlrList;
    }

    /**
     * @param projectPar 流程参数类
     * @return 不动产登记项目审批信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据过去项目信息
     */
    @Override
    public BdcSpxx getCreateBdcSpxx(ProjectPar projectPar, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
        }
        //通过projectPar初始化bdcSpxx
        bdcSpxx = bdcSpxxService.getBdcSpxxFromProjectPar(projectPar, bdcSpxx);
        if (projectPar.getDjsjFwxx() == null) {
            projectParService.getQjsj(projectPar, "djsjFwxx");
        }
        //通过权籍宗地信息初始化bdcSpxx
        if (CollectionUtils.isEmpty(projectPar.getDjsjZdxxList())) {
            projectParService.getQjsj(projectPar, "djsjZdxxList");
        }
        if (CollectionUtils.isNotEmpty(projectPar.getDjsjZdxxList())) {
            bdcSpxx = bdcSpxxService.getBdcSpxxFromZd(projectPar.getDjsjZdxxList().get(0), bdcSpxx);
        }
        //通过权籍房屋信息初始化bdcSpxx
        if (projectPar.getDjsjFwxx() != null) {
            bdcSpxx = bdcSpxxService.getBdcSpxxFromDjsjFwxx(projectPar.getDjsjFwxx(), bdcSpxx, projectPar);
        }
        //通过农用地初始化bdcSpxx
        if (CollectionUtils.isEmpty(projectPar.getDjsjNydDcbList())) {
            projectParService.getQjsj(projectPar, "djsjNydDcbList");
        }
        if (CollectionUtils.isNotEmpty(projectPar.getDjsjNydDcbList())) {
            bdcSpxx = bdcSpxxService.getBdcSpxxFromTdcb(projectPar.getDjsjNydDcbList().get(0), bdcSpxx);
        }
        //通过权属宗地初始化bdcSpxx
        if (CollectionUtils.isEmpty(projectPar.getDjsjQszdDcbList())) {
            projectParService.getQjsj(projectPar, "djsjQszdDbcList");
        }
        if (CollectionUtils.isNotEmpty(projectPar.getDjsjQszdDcbList())) {
            bdcSpxx = bdcSpxxService.getBdcSpxxFromTdSyq(projectPar.getDjsjQszdDcbList().get(0), bdcSpxx);
        }
        //通过林权初始化bdcSpxx
        if (projectPar.getDjsjLqxx() == null) {
            projectParService.getQjsj(projectPar, "djsjLqxx");
        }
        if (projectPar.getDjsjLqxx() != null) {
            bdcSpxx = bdcSpxxService.getBdcSpxxFromLq(projectPar.getDjsjLqxx(), bdcSpxx);
        }
        //通过宗海初始化bdcSpxx
        if (projectPar.getDjsjZhxx() == null) {
            projectParService.getQjsj(projectPar, "djsjZhxx");
        }
        if (projectPar.getDjsjZhxx() != null) {
            bdcSpxx = bdcSpxxService.getBdcSpxxFromZh(projectPar.getDjsjZhxx(), bdcSpxx);
        }
        if (CommonUtil.indexOfStrs(Constants.SQLX_DQFTTDMJ_DM, projectPar.getSqlx()) && projectPar.getDjsjFwxx() != null) {
            if (projectPar.getDjsjFwxx().getFttdmj() != null && projectPar.getDjsjFwxx().getFttdmj() != 0) {
                bdcSpxx.setZdzhmj(projectPar.getDjsjFwxx().getFttdmj());
            }
        }

        if (StringUtils.isNotBlank(projectPar.getQjid())) {
            List<String> fwlxList = djsjFwMapper.getBdcfwlxById(projectPar.getQjid());
            String bdcdyFwlx = "";
            if (CollectionUtils.isNotEmpty(fwlxList)) {
                bdcdyFwlx = fwlxList.get(0);
                if (StringUtils.equals(bdcdyFwlx, Constants.DJSJ_FWHS_DM)) {
                    if (projectPar.getDjsjFwxx() != null) {
                        bdcSpxx.setZdzhyt(projectPar.getDjsjFwxx().getTdyt());
                        bdcSpxx.setZdzhqlxz(projectPar.getDjsjFwxx().getTdsyqlx());
                    }
                }
            }

        }
        //转化为代码
        bdcSpxx = bdcZdGlService.changeToDm(bdcSpxx);
        return bdcSpxx;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "qj";
    }
}
