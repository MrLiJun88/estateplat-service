package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/12/16
 * @description  预告变更登记转发服务
 */
public class TurnProjectYgBgdjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        //zx初始化权利信息时删除项目id
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, bdcXm.getProid());
        //zdd 转移登记 需要继承原权利信息
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                qllxVo = qllxService.makeSureQllx(bdcXm);
                if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    if (ybdcxm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
                        if (yqllxVo == null) {
                            yqllxVo = qllxService.makeSureQllx(bdcXm);
                        }
                        yqllxVo.setQlid(UUIDGenerator.generate18());
                        yqllxVo.setProid(bdcXm.getProid());
                        yqllxVo.setYwh(bdcXm.getBh());
                        yqllxVo.setDbr(null);
                        yqllxVo.setDjsj(null);
                        //zdd 不应该继承原来项目的附记
                        yqllxVo.setFj("");
                        yqllxVo.setQszt(0);
                        qllxVo = yqllxVo;
                    }
                }
                qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
                entityMapper.insertSelective(qllxVo);
            }
        }

        return qllxVo;
    }

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcZs> list = null;
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
            list = createBdcZs(bdcXm,previewZs);
        } else{
            list = bdcZsService.creatBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
            //zdd 生成权利人证书关系表
            bdcZsQlrRelService.creatBdcZsQlrRel(bdcXm,list,bdcQlrList);
            //zdd 生成项目证书关系表
            bdcXmZsRelService.creatBdcXmZsRel(list, bdcXm.getProid());
        }
        return list;
    }
}
