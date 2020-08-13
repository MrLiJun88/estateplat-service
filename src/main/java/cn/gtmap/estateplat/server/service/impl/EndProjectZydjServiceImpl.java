package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 * 转移登记办结服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-26
 */
public class EndProjectZydjServiceImpl extends EndProjectDefaultServiceImpl implements EndProjectService {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            changeYgQszt(bdcXm);
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                Boolean needChange = true;
                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                if (ybdcXm != null && StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ) && !StringUtils.equals(ybdcXm.getQllx(), bdcXm.getQllx())) {
                    needChange = false;
                }
                if (needChange) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                }
            }
            // 司法裁定办结后需要将查封全注销
            if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZY_SFCD)) {
                bdcCfService.updateAdjudicationState(bdcXm, bdcXmRelList);
            }
            //预告查封转查封
            String ycfTurnToCfEnable = StringUtils.deleteWhitespace(AppConfig.getProperty("ycfTurnToCf.enable"));
            if (StringUtils.equals(ycfTurnToCfEnable, ParamsConstants.TRUE_LOWERCASE)
                    && StringUtils.isNotBlank(bdcXm.getProid()) && Constants.SQLX_SPFMMZYDJ_DM.equals(bdcXm.getSqlx())) {
                BdcBdcdy bdcbdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                if (bdcbdcdy != null && StringUtils.isNotBlank(bdcbdcdy.getBdcdyh())) {
                    // 预查封
                    List<BdcCf> bdcCfList = bdcCfService.queryYcfByBdcdyh(bdcXm.getBdcdyh());
                    Boolean ycfchangecf = false;
                    if (CollectionUtils.isNotEmpty(bdcCfList) && StringUtils.isNotBlank(bdcCfList.get(0).getBzxr())) {
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                            for (BdcQlr bdcQlr : bdcQlrList) {
                                if (bdcQlr != null && StringUtils.isNotBlank(bdcQlr.getQlrmc()) && bdcCfList.get(0).getBzxr().contains(bdcQlr.getQlrmc())) {
                                    ycfchangecf = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (ycfchangecf) {
                        bdcCfService.ycfChangeCf(bdcXm, bdcCfList.get(0));
                    }
                    // 轮候预查封
                    Example example = new Example(BdcCf.class);
                    example.createCriteria().andEqualTo("bdcdyid", bdcXm.getBdcdyid()).andEqualTo("cflx", Constants.CFLX_ZD_LHYCF);
                    List<BdcCf> bdcLhycfList = entityMapper.selectByExample(example);
                    if (CollectionUtils.isNotEmpty(bdcLhycfList)) {
                        for (BdcCf bdcCf : bdcLhycfList) {
                            Boolean lhycfchangelhcf = false;
                            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                for (BdcQlr bdcQlr : bdcQlrList) {
                                    if (bdcQlr != null && StringUtils.isNotBlank(bdcQlr.getQlrmc()) && StringUtils.isNotBlank(bdcCf.getBzxr()) && bdcCf.getBzxr().contains(bdcQlr.getQlrmc())) {
                                        lhycfchangelhcf = true;
                                        break;
                                    }
                                }
                            }
                            if (lhycfchangelhcf) {
                                StringBuilder fj = new StringBuilder();
                                if (StringUtils.isNotBlank(bdcCf.getFj())){
                                    fj.append(bdcCf.getFj());
                                }
                                fj.append(CalendarUtil.formatDateToString(new Date())).append("轮候预查封转轮候查封");
                                bdcCf.setCflx(Constants.CFLX_LHCF);
                                bdcCf.setFj(fj.toString());
                                entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                            }
                        }
                    }
                }
            }
            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }
    }
}
