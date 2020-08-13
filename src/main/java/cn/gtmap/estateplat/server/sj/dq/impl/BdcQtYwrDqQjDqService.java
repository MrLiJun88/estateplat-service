package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-07
 * @description 义务人读取权籍
 */
@Service
public class BdcQtYwrDqQjDqService implements BdcQtDqService {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Resource(name = "bdcDataDqQjServiceImpl")
    private BdcQlrDqService bdcDataDqQjServiceImpl;
    @Resource(name = "creatProjectDefaultService")
    private CreatProjectService creatProjectService;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (projectPar != null && projectPar.getProid() != null) {
            List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrByProid(projectPar.getProid(), Constants.QLRLX_YWR);
            if (CollectionUtils.isEmpty(bdcQlrList)) {
                List<BdcQlr> bdccYwrList = bdcDataDqQjServiceImpl.getCreateQlr(projectPar);
                List<InsertVo> list = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(bdccYwrList)) {
                    list.addAll(bdccYwrList);
                    creatProjectService.insertProjectData(list);
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
        return "ywrdqqj";
    }
}
