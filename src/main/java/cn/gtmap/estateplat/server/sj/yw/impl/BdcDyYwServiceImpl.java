package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
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
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description 不动产登记簿
 */
@Service
public class BdcDyYwServiceImpl implements BdcDataYwService {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private ProjectParService projectParService;

    /**
     * @param projectPar
     * @param insertVoList
     * @return 不动产数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化获取不动产数据
     */
    @Override
    public List<InsertVo> initbdcData(ProjectPar projectPar, List<InsertVo> insertVoList) {
        if (CollectionUtils.isEmpty(insertVoList)) {
            insertVoList = Lists.newArrayList();
        }
        if (StringUtils.isNotBlank(projectPar.getDjh())) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(projectPar.getBdcdyh());
            if (bdcBdcdy == null) {
                bdcBdcdy = initBdcBdcdy(projectPar, projectPar.getDjbid());
                insertVoList.add(bdcBdcdy);
            }
            if (bdcBdcdy != null) {
                projectPar.setBdcdyid(bdcBdcdy.getBdcdyid());
                projectPar.setBdcBdcdy(bdcBdcdy);
            }
        }
        return insertVoList;
    }


    private BdcBdcdy initBdcBdcdy(ProjectPar projectPar, String djbid) {
        BdcBdcdy bdcBdcdy = new BdcBdcdy();
        bdcBdcdy.setDjbid(djbid);
        bdcBdcdy = bdcdyService.getBdcdyFromProjectPar(projectPar, bdcBdcdy);
        if (StringUtils.equals(Constants.BDCLX_TDFW, bdcBdcdy.getBdclx())) {
            if (projectPar.getDjsjFwxx() == null) {
                projectParService.getQjsj(projectPar, "djsjFwxx");
            }
            bdcBdcdy = bdcdyService.getBdcdyFromFw(projectPar.getDjsjFwxx(), bdcBdcdy);
        }
        if (StringUtils.isBlank(bdcBdcdy.getBdcdyid())) {
            bdcBdcdy.setBdcdyid(UUIDGenerator.generate18());
        }
        return bdcBdcdy;
    }


    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_bdcdy";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 1;
    }
}
