package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/3/23
 * @description 不动产字段对比
 */
@Service
public class BdcZddbServiceImpl implements BdcZddbService {
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    BdcZdGlService bdcZdGlService;

    @Override
    public String getBdcZddbJgByDzwyt(String proid) {
        String result = ParamsConstants.FALSE_LOWERCASE;
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        if (bdcSpxx != null && bdcBdcdy != null) {
            DjsjFwxx djsjFwxx = null;
            String djId = bdcDjsjService.getDjidByBdcdyh(bdcBdcdy.getBdcdyh(), bdcBdcdy.getBdclx());
            if (StringUtils.isNotBlank(djId) && Constants.BDCLX_TDFW.equals(bdcBdcdy.getBdclx())) {
                djsjFwxx = djsjFwService.getDjsjFwxx(djId);
            }
            if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getFwyt())) {
                //将过渡期的用途转化为字典数据
                List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                String fwyt = "";
                for (BdcZdFwyt bdcZdFwyt : fwytList) {
                    if (StringUtils.equals(bdcZdFwyt.getMc(), djsjFwxx.getFwyt())) {
                        fwyt = bdcZdFwyt.getDm();
                        break;
                    } else if (StringUtils.equals(bdcZdFwyt.getDm(), djsjFwxx.getFwyt())) {
                        fwyt = bdcZdFwyt.getDm();
                        break;
                    }
                }
                if (StringUtils.equals(fwyt, bdcSpxx.getYt()))
                    result = "true";
            }
        }
        return result;
    }

    @Override
    public String getBdcZddbJgByDjzwmj(String proid) {
        String result = ParamsConstants.FALSE_LOWERCASE;
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        if (bdcSpxx != null && bdcBdcdy != null) {
            DjsjFwxx djsjFwxx = null;
            String djId = bdcDjsjService.getDjidByBdcdyh(bdcBdcdy.getBdcdyh(), bdcBdcdy.getBdclx());
            if (StringUtils.isNotBlank(djId) && Constants.BDCLX_TDFW.equals(bdcBdcdy.getBdclx())) {
                djsjFwxx = djsjFwService.getDjsjFwxx(djId);
            }
            if (djsjFwxx != null&&djsjFwxx.getJzmj() != null && bdcSpxx.getMj() != null && djsjFwxx.getJzmj() == bdcSpxx.getMj()) {
                result = "true";
            }
        }
        return result;
    }

    @Override
    public String getBdcZddbJgByZdzhyt(String proid) {
        String result = ParamsConstants.FALSE_LOWERCASE;
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        DjsjZdxx djsjZdxx = null;
        List<DjsjZdxx> djsjZdxxList = null;
        String djId = bdcDjsjService.getDjidByBdcdyh(bdcBdcdy.getBdcdyh(), bdcBdcdy.getBdclx());

        if (StringUtils.isNotBlank(djId) && StringUtils.isNotBlank(bdcBdcdy.getBdclx()) && bdcBdcdy.getBdclx().indexOf(Constants.BDCLX_TD) > -1) {
            djsjZdxxList = bdcDjsjService.getDjsjZdxx(djId);
            //zwq 取农用地调查表信息
            if (CollectionUtils.isEmpty(djsjZdxxList)) {
                djsjZdxxList = bdcDjsjService.getDjsjNydxx(djId);
                if (bdcBdcdy.getBdclx().equals(Constants.BDCLX_LQ) &&CollectionUtils.isEmpty(djsjZdxxList)&&StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && bdcBdcdy.getBdcdyh().length() > 19) {
                    String djh = StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19);
                    djsjZdxxList = bdcDjsjService.getDjsjNydxxByDjh(djh);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            djsjZdxx = djsjZdxxList.get(0);
            if (StringUtils.equals(bdcSpxx.getZdzhyt(), djsjZdxx.getTdyt()))
                result = "true";
        }
        return result;
    }

    @Override
    public String getBdcZddbJgByZdzhmj(String proid) {
        String result = ParamsConstants.FALSE_LOWERCASE;
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        DjsjZdxx djsjZdxx = null;
        List<DjsjZdxx> djsjZdxxList = null;
        String djId = bdcDjsjService.getDjidByBdcdyh(bdcBdcdy.getBdcdyh(), bdcBdcdy.getBdclx());

        if (StringUtils.isNotBlank(djId) && StringUtils.isNotBlank(bdcBdcdy.getBdclx()) && bdcBdcdy.getBdclx().indexOf(Constants.BDCLX_TD) > -1) {
            djsjZdxxList = bdcDjsjService.getDjsjZdxx(djId);
            //zwq 取农用地调查表信息
            if (CollectionUtils.isEmpty(djsjZdxxList)) {
                djsjZdxxList = bdcDjsjService.getDjsjNydxx(djId);
                if (bdcBdcdy.getBdclx().equals(Constants.BDCLX_LQ) &&CollectionUtils.isEmpty(djsjZdxxList)&&StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && bdcBdcdy.getBdcdyh().length() > 19) {
                    String djh = org.apache.commons.lang3.StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19);
                    djsjZdxxList = bdcDjsjService.getDjsjNydxxByDjh(djh);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            djsjZdxx = djsjZdxxList.get(0);
            if (djsjZdxx.getScmj() != null && bdcSpxx.getZdzhmj() != null && djsjZdxx.getScmj() == bdcSpxx.getZdzhmj())
                result = "true";
        }
        return result;
    }

    @Override
    public Map<String, Object> getZdxx(String proid,boolean withZd) {
        DjsjZdxx djsjZdxx = null;
        List<DjsjZdxx> djsjZdxxList = null;
        Map<String, Object> result = null;
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        if (bdcBdcdy == null) {
            return result;
        }
        String bdcdyh = bdcBdcdy.getBdcdyh();
        if (!StringUtils.equals(bdcBdcdy.getBdclx(), Constants.BDCLX_TD)) {
            bdcdyh = bdcdyService.getZdBdcdyh(bdcBdcdy.getBdcdyh());
        }
        String djId = bdcDjsjService.getDjidByBdcdyh(bdcdyh, Constants.BDCLX_TD);
        if (StringUtils.isNotBlank(djId)) {
            djsjZdxxList = withZd?bdcDjsjService.getDjsjZdxxWithZd(djId):bdcDjsjService.getDjsjZdxx(djId);
            //zwq 取农用地调查表信息
            if (CollectionUtils.isEmpty(djsjZdxxList)) {
                djsjZdxxList = bdcDjsjService.getDjsjNydxx(djId);
                if (bdcBdcdy.getBdclx().equals(Constants.BDCLX_LQ) && (CollectionUtils.isEmpty(djsjZdxxList))&&StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && bdcBdcdy.getBdcdyh().length() > 19) {
                    String djh = org.apache.commons.lang3.StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19);
                    djsjZdxxList = bdcDjsjService.getDjsjNydxxByDjh(djh);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            djsjZdxx = djsjZdxxList.get(0);
            Object object = JSON.toJSON(djsjZdxx);
            result = (Map<String, Object>) object;
        }
        return result;
    }

    public Map<String, Object> getFwxx(String proid) {
        Map<String, Object> result = null;
        DjsjFwxx djsjFwxx = null;
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        if (bdcBdcdy != null) {
            String djId = bdcDjsjService.getDjidByBdcdyh(bdcBdcdy.getBdcdyh(), bdcBdcdy.getBdclx());
            if (StringUtils.isNotBlank(djId) && Constants.BDCLX_TDFW.equals(bdcBdcdy.getBdclx())) {
                djsjFwxx = djsjFwService.getDjsjFwxx(djId);
            }
        }
        if (djsjFwxx != null) {
            Object object = JSON.toJSON(djsjFwxx);
            result = (Map<String, Object>) object;
        }
        return result;
    }
}
