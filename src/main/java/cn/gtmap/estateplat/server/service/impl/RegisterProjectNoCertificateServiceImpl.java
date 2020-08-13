package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.RegisterProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/5/8
 * @description 不生成证书的流程登簿实现类
 */
public class RegisterProjectNoCertificateServiceImpl implements RegisterProjectService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;

    @Override
    @Transactional
    public void registerProject(String proid, String userid) throws Exception {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    if (bdcXmTemp != null) {
                        //改变权利状态
                        EndProjectService endProjectDefaultServiceImpl = projectService.getEndProjectService(bdcXmTemp);
                        projectService.changeQlztProjectEvent(endProjectDefaultServiceImpl, bdcXmTemp);

                        //改变项目状态
                        bdcXmService.changeXmzt(bdcXmTemp.getProid(), Constants.XMZT_SZ);

                        //赋值的登簿信息（登簿人、登簿时间）
                        QllxVo qllxVo = qllxService.makeSureQllx(bdcXmTemp);
                        qllxVo = qllxService.queryQllxVo(qllxVo, bdcXmTemp.getProid());
                        Date dbsj = CalendarUtil.getCurHMSDate();
                        if (qllxVo != null && StringUtils.isNotBlank(qllxVo.getQlid())) {
                            qllxVo = qllxService.updateDbr(qllxVo, userid, dbsj);
                            entityMapper.updateByPrimaryKeySelective(qllxVo);
                        } else {
                            //zhouwanqing   这边针对注销和解封的登簿人和登记时间
                            List<BdcXm> ybdcXmList = bdcXmService.getYbdcXmListByProid(bdcXmTemp.getProid());
                            if (CollectionUtils.isNotEmpty(ybdcXmList)) {
                                for (BdcXm ybdcXm : ybdcXmList) {
                                    QllxVo yqllxVo = qllxService.makeSureQllx(ybdcXm);
                                    yqllxVo = qllxService.queryQllxVo(yqllxVo, ybdcXm.getProid());
                                    if (yqllxVo != null && StringUtils.isNotBlank(yqllxVo.getQlid())) {
                                        yqllxVo = qllxService.updateZxDbr(yqllxVo, userid, "", "", ybdcXm.getProid());
                                        entityMapper.updateByPrimaryKeySelective(yqllxVo);
                                    }
                                }
                            }
                        }
                        //房屋土地查封登记处理登簿人与登簿时间
                        if(StringUtils.isNotEmpty(bdcXmTemp.getSqlx())&&(StringUtils.equals(Constants.SQLX_FWCF_DM,bdcXmTemp.getSqlx())||StringUtils.equals(Constants.SQLX_TDCF_DM_NEW,bdcXmTemp.getSqlx()))){
                            qllxService.updateGdDbr(bdcXmTemp,userid,"","");
                        }
                    }
                }
            }
        }
    }
}
