package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcFwFsssService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/10/8
 * @description 针对商品房首次登记采用多线程初始化项目及权利信息
 */
public class BdcInitInfoThread implements Runnable{
    private Project project;
    private CreatProjectService creatProjectService;
    private TurnProjectService turnProjectService;
    private EntityMapper entityMapper;
    private BdcdyService bdcdyService;
    private BdcFwFsssService bdcFwFsssService;
    private BdcSpxxService bdcSpxxService;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param project
     * @return
     * @description 构造函数
     */
    public BdcInitInfoThread(Project project,EntityMapper entityMapper){
        this.project = project;
        this.entityMapper = entityMapper;
        this.creatProjectService = (CreatProjectService) Container.getBean("creatProjectScdjService");
        this.turnProjectService = (TurnProjectService) Container.getBean("turnComplexScdjProjectServiceImpl");
        this.bdcdyService = (BdcdyService) Container.getBean("bdcdyServiceImpl");
        this.bdcFwFsssService = (BdcFwFsssService) Container.getBean("bdcFwFsssServiceImpl");
        this.bdcSpxxService = (BdcSpxxService) Container.getBean("bdcSpxxServiceImpl");
    }

    @Override
    public void run() {
        List<InsertVo> insertVoList = creatProjectService.initVoFromOldData(project);
        if(CollectionUtils.isNotEmpty(insertVoList)){
            entityMapper.batchSaveSelective(insertVoList);
            for(InsertVo insertVo:insertVoList) {
                if(insertVo instanceof BdcXm){
                    BdcXm bdcXm = (BdcXm) insertVo;
                    turnProjectService.saveQllxVo(bdcXm);
                    //jyl 初始化主房的子户室做附属设施
                    if (StringUtils.isNotBlank(bdcXm.getBdcdyid()) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                        BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                        if (bdcdy != null) {
                            bdcFwFsssService.intiBdcFwfsssForZhs(bdcdy.getBdcdyh(), bdcXm);
                        }
                    }
                    bdcSpxxService.dealWithSpxxZdzhmj(bdcXm);
                    break;
                }
            }
        }
    }
}
