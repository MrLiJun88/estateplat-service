package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.RegisterProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/5/8
 * @description 注销登记登簿实现类
 */
public class RegisterProjectZxdjServiceImpl implements RegisterProjectService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcTdService bdcTdService;

    @Override
    @Transactional
    public void registerProject(String proid, String userid) throws Exception {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<String> djhList = new ArrayList<>();
                for (BdcXm bdcXmTemp : bdcXmList) {
                    //改变原权利登记状态
                    EndProjectService endProjectDefaultServiceImpl = projectService.getEndProjectService(bdcXm);
                    projectService.changeQlztProjectEvent(endProjectDefaultServiceImpl, bdcXm);

                    //改变项目状态
                    bdcXmService.changeXmzt(bdcXmTemp.getProid(), Constants.XMZT_SZ);
                    bdcTdService.changeGySjydZt(bdcXmTemp, djhList);

                    //赋值注销登簿人和登簿时间
                    List<String> yProidList = bdcXmRelService.getYproid(bdcXmTemp.getProid());
                    if (CollectionUtils.isNotEmpty(yProidList)) {
                        for (String yProid : yProidList) {
                            if (StringUtils.isNotBlank(yProid)) {
                                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yProid);
                                QllxVo qllxVo = qllxService.makeSureQllx(ybdcXm);
                                QllxVo yQllxVo = null;
                                if (qllxVo != null) {
                                    yQllxVo = qllxService.queryQllxVo(qllxVo, yProid);
                                    if (yQllxVo == null && bdcXmTemp != null) {
                                        qllxVo = qllxService.queryQllxVo(bdcXmTemp);
                                    }
                                } else {
                                    qllxVo = qllxService.getQllxVoByProid(yProid);
                                    if (null == qllxVo && (StringUtils.equals(Constants.SQLX_PLDYZX, bdcXmTemp.getSqlx()) ||
                                            StringUtils.equals(Constants.SQLX_DYZX, bdcXmTemp.getSqlx()) ||
                                            StringUtils.equals(Constants.SQLX_JSYDSYQDYAQ_ZX, bdcXmTemp.getSqlx()) ||
                                            StringUtils.equals(Constants.SQLX_JSYDSYQ_ZX, bdcXmTemp.getSqlx()) ||
                                            StringUtils.equals(Constants.SQLX_JSYDGZWSYQ_ZX, bdcXmTemp.getSqlx())) ||
                                            StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_PLJF) ||
                                            StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_JF)) {
                                        qllxVo = qllxService.queryQllxVo(bdcXmTemp);
                                    }
                                }
                                if (yQllxVo != null) {
                                    yQllxVo = qllxService.updateZxDbr(yQllxVo, userid, "", "", proid);
                                    entityMapper.updateByPrimaryKeySelective(yQllxVo);

                                } else if (qllxVo instanceof BdcDyaq || qllxVo instanceof BdcCf) {
                                    qllxVo = qllxService.updateZxDbr(qllxVo, userid, "", "", proid);
                                    entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
                                }
                            }
                        }
                    }
                    //批量抵押部分注销换证的，当前流程被注销的权利
                    if (StringUtils.equals(Constants.DJLX_PLDYBG_YBZS_SQLXDM, bdcXmTemp.getSqlx())) {
                        QllxVo qllxVo = qllxService.queryQllxVo(bdcXmTemp);
                        if (null != qllxVo && Constants.QLLX_QSZT_HR == qllxVo.getQszt()) {
                            qllxVo = qllxService.updateZxDbr(qllxVo, userid, "", "", proid);
                            entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
                        }
                    }
                    //抵押（原他项证）注销登记，过渡查封注销登记处理注销登簿人等信息
                    if (StringUtils.equals(Constants.SQLX_GDDYZX_DM, bdcXmTemp.getSqlx()) || StringUtils.equals(Constants.SQLX_SFCD, bdcXmTemp.getSqlx())
                            || StringUtils.equals(Constants.SQLX_SFCD_PL, bdcXmTemp.getSqlx())
                            || StringUtils.equals(Constants.SQLX_FWJF_DM, bdcXmTemp.getSqlx())
                            || StringUtils.equals(Constants.SQLX_TDJF_DM, bdcXmTemp.getSqlx())
                            || StringUtils.equals(Constants.SQLX_PLGDDYZX_DM, bdcXmTemp.getSqlx())) {
                        qllxService.updateGdZxDbr(bdcXmTemp, userid, "", "", proid);
                    }
                }

            }
        }
    }
}
