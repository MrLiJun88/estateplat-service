package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0  2018/6/14.
 * @description 修改登记簿人多线程
 */
public class TurnProjectEventDbrThread implements Runnable {

    private BdcXm bdcXm;

    private QllxService qllxService;

    private BdcXmService bdcXmService;

    private EntityMapper entityMapper;

    private String userid;

    private Date dbsj;

    private String defaultUserId;

    private String dbrRead;

    /**
     * 构造函数
     */
    public TurnProjectEventDbrThread(QllxService qllxService, BdcXmService bdcXmService, EntityMapper entityMapper, BdcXm bdcXm, String userid, Date dbsj, String defaultUserId, String dbrRead) {
        this.qllxService = qllxService;
        this.bdcXmService = bdcXmService;
        this.bdcXm = bdcXm;
        this.entityMapper = entityMapper;
        this.userid = userid;
        this.dbsj = dbsj;
        this.defaultUserId = defaultUserId;
        this.dbrRead = dbrRead;
    }

    @Override
    public void run() {
        if (bdcXm != null) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
            if (qllxVo != null && StringUtils.isNotBlank(qllxVo.getQlid())) {
                qllxVo = qllxService.updateDbr(qllxVo, userid, dbsj);
                entityMapper.updateByPrimaryKeySelective(qllxVo);
            } else {
                //zhouwanqing   这边针对注销和解封的登簿人和登记时间
                List<BdcXm> ybdcXmList = bdcXmService.getYbdcXmListByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(ybdcXmList)) {
                    for (BdcXm ybdcXm : ybdcXmList) {
                        QllxVo yqllxVo = qllxService.makeSureQllx(ybdcXm);
                        yqllxVo = qllxService.queryQllxVo(yqllxVo, ybdcXm.getProid());
                        if (yqllxVo != null && StringUtils.isNotBlank(yqllxVo.getQlid())) {
                            yqllxVo = qllxService.updateZxDbr(yqllxVo, userid, defaultUserId, dbrRead, ybdcXm.getProid());
                            entityMapper.updateByPrimaryKeySelective(yqllxVo);
                        }
                    }
                }
            }
        }
    }
}
