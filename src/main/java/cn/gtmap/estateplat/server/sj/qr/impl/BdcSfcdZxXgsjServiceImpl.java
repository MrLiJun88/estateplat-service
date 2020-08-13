package cn.gtmap.estateplat.server.sj.qr.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.vo.Xgsj;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.qr.BdcQrdXgsjService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020-04-1
 * @description 司法处置注销数据修改模型组织
 */
@Service
public class BdcSfcdZxXgsjServiceImpl implements BdcQrdXgsjService {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcCfService bdcCfService;

    @Override
    public List<Xgsj> getXgsjList(Map map, BdcXm bdcXm) {
        String xgzdStr = CommonUtil.formatEmptyValue(map.get("xgzd"));
        List<Xgsj> xgsjList = Lists.newArrayList();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid()) && StringUtils.isNotBlank(xgzdStr)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    List<Xgsj> xgsjListTemp = getXgsjListByYXm(bdcXmRel.getYproid(), xgzdStr);
                    if (CollectionUtils.isNotEmpty(xgsjListTemp)) {
                        xgsjList.addAll(xgsjListTemp);
                    }
                }
            }
        }
        return xgsjList;
    }

    @Override
    public String getIntetfacaCode() {
        return "2";
    }

    private List<Xgsj> getXgsjListByYXm(String yproid, String xgzdStr) {
        List<Xgsj> xgsjList = Lists.newArrayList();
        if (StringUtils.isNotBlank(yproid) && StringUtils.isNotBlank(xgzdStr)) {
            BdcCf bdcCf = bdcCfService.selectCfByProid(yproid);
            String[] xgzdArray = xgzdStr.split(",");
            if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
                StringBuilder sjms = new StringBuilder();
                if (StringUtils.isNotBlank(bdcCf.getCfwh())) {
                    sjms.append("【查封文号】").append(bdcCf.getCfwh()).append(";");
                }
                if (StringUtils.isNotBlank(bdcCf.getCfjg())) {
                    sjms.append("【查封机关】").append(bdcCf.getCfjg()).append(";");
                }
                for (String xgzd : xgzdArray) {
                    if (StringUtils.isNotBlank(xgzd)) {
                        Xgsj xgsj = new Xgsj();
                        xgsj.setSjms(sjms.toString());
                        switch (xgzd) {
                            case ParamsConstants.QSZT_LOWERCASE:
                                xgsj = getQsztXgsj(bdcCf, xgsj);
                                break;
                            case ParamsConstants.ISSX_LOWERCASE:
                                xgsj = getIssxXgsj(bdcCf, xgsj);
                                break;
                            case ParamsConstants.ISCD_LOWERCASE:
                                xgsj = getIscdXgsj(bdcCf, xgsj);
                                break;
                            default:
                                xgsj = null;
                        }
                        if (xgsj != null) {
                            xgsjList.add(xgsj);
                        }
                    }
                }
            }
        }
        return xgsjList;
    }

    private Xgsj getQsztXgsj(BdcCf bdcCf, Xgsj xgsj) {
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
            xgsj.setXgzd("权属状态");
            if (Constants.QLLX_QSZT_XS.equals(bdcCf.getQszt())) {
                xgsj.setYz(Constants.QLLX_QSZT_XS_CHS);
            } else if (Constants.QLLX_QSZT_HR.equals(bdcCf.getQszt())) {
                xgsj.setYz(Constants.QLLX_QSZT_HR_CHS);
            }
            xgsj.setXz(Constants.QLLX_QSZT_XS_CHS);
        }
        return xgsj;
    }

    private Xgsj getIssxXgsj(BdcCf bdcCf, Xgsj xgsj) {
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
            xgsj.setXgzd("失效状态");
            if (Constants.SXZT_ISSX.equals(bdcCf.getIssx())) {
                xgsj.setYz(Constants.YES_CHS);
                xgsj.setXz(Constants.NO_CHS);
            } else if (Constants.SXZT_ISSX_WSX.equals(bdcCf.getIssx())) {
                xgsj = null;
            }
        }
        return xgsj;
    }

    private Xgsj getIscdXgsj(BdcCf bdcCf, Xgsj xgsj) {
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
            xgsj.setXgzd("裁定状态");
            if (Constants.ISCD_POSITIVE.equals(bdcCf.getIscd())) {
                xgsj.setYz(Constants.YES_CHS);
                xgsj.setXz(Constants.NO_CHS);
            } else if (Constants.ISCD_NEGETIVE.equals(bdcCf.getIscd())) {
                xgsj = null;
            }
        }
        return xgsj;
    }
}
