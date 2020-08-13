package cn.gtmap.estateplat.server.sj.qr.impl;

import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.server.core.model.vo.Xgsj;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.qr.BdcQrdXgsjService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020-04-1
 * @description 预查封转现数据修改模型组织
 */
@Service
public class BdcYcfZXsXgsjServiceImpl implements BdcQrdXgsjService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public List<Xgsj> getXgsjList(Map map, BdcXm bdcXm) {
        String xgzdStr = CommonUtil.formatEmptyValue(map.get("xgzd"));
        List<Xgsj> xgsjList = Lists.newArrayList();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid()) && StringUtils.isNotBlank(xgzdStr)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    xgsjList = getXgsjListByXm(bdcXmTemp, xgzdStr);
                }
            }
        }
        return xgsjList;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "1";
    }

    private List<Xgsj> getXgsjListByXm(BdcXm bdcXm, String xgzdStr) {
        List<Xgsj> xgsjList = Lists.newArrayList();
        if (StringUtils.isNotBlank(bdcXm.getProid()) && StringUtils.isNotBlank(xgzdStr) && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());

            // 获取预查封
            HashMap map = new HashMap();
            map.put("bdcdyid", bdcXm.getBdcdyid());
            map.put("cflx", Constants.CFLX_ZD_YCF);
            List<BdcCf> bdcCfList = bdcCfService.andEqualQueryCf(map, null);
            if (CollectionUtils.isNotEmpty(bdcCfList)) {
                for (BdcCf bdcCf : bdcCfList) {
                    List<BdcQlr> cfBdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcCf.getProid());
                    Boolean sameQlr = bdcQlrService.checkSameQlr(bdcQlrList, cfBdcQlrList);
                    if (sameQlr) {
                        List<Xgsj> xgsjs = getXgsjListByYcf(bdcCf, xgzdStr);
                        if (CollectionUtils.isNotEmpty(xgsjs)) {
                            xgsjList.addAll(xgsjs);
                        }
                    }
                }

            }
        }
        return xgsjList;
    }

    private List<Xgsj> getXgsjListByYcf(BdcCf bdcCf, String xgzdStr) {
        List<Xgsj> xgsjList = Lists.newArrayList();
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
                        case ParamsConstants.CFLX_LOWERCASE:
                            xgsj = getCflxXgsj(bdcCf, xgsj);
                            break;
                        case ParamsConstants.QSZT_LOWERCASE:
                            xgsj = getQsztXgsj(bdcCf, xgsj);
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

    private Xgsj getCflxXgsj(BdcCf bdcCf, Xgsj xgsj) {
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
            xgsj.setXgzd("查封类型");
            if (Constants.CFLX_ZD_YCF.equals(bdcCf.getCflx())) {
                xgsj.setYz(Constants.CFLX_GD_YCF);
            } else if (Constants.CFLX_ZD_CF.equals(bdcCf.getCflx())) {
                xgsj.setYz(Constants.CFLX_GD_CF);
            }
            xgsj.setXz(Constants.CFLX_GD_CF);
        }
        return xgsj;
    }


}
