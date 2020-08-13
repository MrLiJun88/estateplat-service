package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/20
 * @description 不动产异议登记权利读取
 */
@Service
public class BdcYyDqServiceImpl implements BdcQlDqService {

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcYy bdcYy = null;
        if(CollectionUtils.isNotEmpty(qllxVoList)){
            if (qllxVoList.get(0) instanceof BdcYy) {
                bdcYy = (BdcYy) qllxVoList.get(0);
                isAdd = false;
            }
        }else {
            qllxVoList = Lists.newArrayList();
            bdcYy = new BdcYy();
        }
        if(bdcYy != null && projectPar != null){
            if(StringUtils.isBlank(bdcYy.getQlid())){
                bdcYy.setQlid(UUIDGenerator.generate18());
            }
            if (projectPar.getBdcXm() != null) {
                bdcYy.setBdcdyid(projectPar.getBdcXm().getBdcdyid());
                bdcYy.setProid(projectPar.getBdcXm().getProid());
                bdcYy.setYwh(projectPar.getBdcXm().getBh());
                bdcYy.setQllx(projectPar.getBdcXm().getQllx());
            }
            bdcYy.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcYy);
            }
        }
        return qllxVoList;
    }

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取权利信息从权籍中
     */
    @Override
    public List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        //jiangganzhi 异议权利信息无需从权籍获取
        return null;
    }

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取权利信息从预告中
     */
    @Override
    public List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        //jiangganzhi 异议权利信息无需从预告获取
        return null;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_yy";
    }
}
