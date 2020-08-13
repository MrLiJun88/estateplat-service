package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description
 */
@Service
public class BdcXmYwServiceImpl implements BdcDataYwService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcdyService bdcdyService;

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
        BdcXm bdcXm = initBdcXm(projectPar);
        if (bdcXm != null) {
            if (bdcXm.getQszt() == null) {
                bdcXm.setQszt(0);
            }
            projectPar.setBdcXm(bdcXm);
            insertVoList.add(bdcXm);
        }
        return insertVoList;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 初始化bdcXm表
     */
    private BdcXm initBdcXm(ProjectPar projectPar) {
        BdcXm bdcXm = bdcXmService.initBdcXmFromProjectPar(projectPar);
        if (StringUtils.isBlank(bdcXm.getBh())) {
            bdcXm.setBh(bdcXmService.creatXmbh(bdcXm));
        }
        if (StringUtils.isBlank(bdcXm.getProid())) {
            bdcXm.setProid(UUIDGenerator.generate18());
        }
        BdcXm yBdcXm = null;
        //获取原项目相关信息
        if (StringUtils.isNotBlank(projectPar.getYxmid())) {
            yBdcXm = bdcXmService.getBdcXmByProid(projectPar.getYxmid());
        }
        bdcXmService.initBdcXmFromYbdcXm(bdcXm, yBdcXm, projectPar);
        //正对没有配置qllx的权籍数据根据不动产单元号获取权利类型
        if (StringUtils.isBlank(projectPar.getQllx()) && StringUtils.isBlank(bdcXm.getQllx()) && yBdcXm == null && StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            String qllxdm = bdcdyService.getQllxFormBdcdy(projectPar.getBdcdyh());
            if (StringUtils.isNotBlank(qllxdm)) {
                bdcXm.setQllx(qllxdm);
                projectPar.setQllx(qllxdm);
            }
        }
        //处理bdcXm的djsy
        bdcXmService.dealBdcXmDjsyByProjectPar(projectPar, bdcXm);
        //处理查封登记子项
        if (StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_CFDJ_DM) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZX_SFCD)) {
            String djzx = bdcCfService.getCfDjzxByProjectPar(projectPar, yBdcXm);
            if (StringUtils.equals(djzx, Constants.DJZX_XF)) {
                projectPar.setSfdyYzh(false);
                bdcXm.setYbdcqzh(yBdcXm.getYbdcqzh());
            }
            bdcXm.setDjzx(djzx);
        }
        bdcXm = bdcXmService.getDydjlx(bdcXm);
        bdcXm = bdcXmService.initBdcXmFromOntBdcXm(bdcXm, projectPar.getProid());
        bdcXm.setCjsj(new Date());
        //处理合并流程其他转移创建时间慢1,用于缮证改变qlzt排序
        if (StringUtils.equals(Constants.SQLX_FWMM_DM, projectPar.getSqlx()) && projectPar.getSx() == 2) {
            bdcXm.setCjsj(DateUtils.addSeconds(new Date(), 1));
        }
        Date lzrq = bdcXmService.getLzrqByWiid(projectPar.getWiid(), bdcXm);
        if (lzrq != null) {
            bdcXm.setLzrq(lzrq);
        }
        //完善处理bdcXm的ybdcqzmh
        String ybdcqzmh = bdcXmService.completeBdcXmYbdcqzmh(projectPar, bdcXm);
        bdcXm.setYbdcqzmh(ybdcqzmh);
        if (projectPar.isSfdyYzh()) {
            //完善处理bdcXm的ybdcqzh
            String ybdcqzh = bdcXmService.completeBdcXmYbdcqzh(projectPar, bdcXm);
            bdcXm.setYbdcqzh(ybdcqzh);
        }
        bdcXm.setDcsjzs(projectPar.getDcsjzs());
        if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
            List<String> yxscqproidList = bdcXmService.getXsCqProidBybdcdyid(projectPar.getBdcdyid());
            if (CollectionUtils.isNotEmpty(yxscqproidList)) {
                bdcXm.setYcqproid(yxscqproidList.get(0));
            }
        }
        return bdcXm;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_xm";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 2;
    }
}
