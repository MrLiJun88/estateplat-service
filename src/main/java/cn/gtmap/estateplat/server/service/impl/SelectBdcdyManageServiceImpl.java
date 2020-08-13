package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.DjsjZdxx;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.SelectBdcdyManageService;
import cn.gtmap.estateplat.server.service.core.SelectBdcdyHandleService;
import cn.gtmap.estateplat.server.service.currency.CurrencyService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SelectBdcdyManageServiceImpl implements SelectBdcdyManageService {
    @Autowired
    private SelectBdcdyHandleService selectBdcdyHandleService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public String getSelectBdcdyPath(String dwdm, Boolean multiselect, Boolean joinselect) {
        String path = "query/" + Constants.DWDM_SZ + "/djsjBdcdyQlShow/djsjBdcdyQlShowList";
        if (multiselect) {
            path = "query/" + Constants.DWDM_SZ + "/djsjBdcdyMulQlShow/djsjBdcdyMulQlShowList";
        }
        return path;
    }

    @Override
    public void getSelectBdcdyModel(String wiid, String proid, String glbdcdy, String glzs, Model model) {
        selectBdcdyHandleService.initSelectBdcdyModel(wiid, proid, glbdcdy, glzs, model);
    }

    @Override
    public String getAddHbXmModel(Model model) {
        selectBdcdyHandleService.initAddHbXmModel(model);
        return "/sjgl/addHb";
    }

    @Override
    public Map<String, Object> getDjsjBdcdySearchMap(String djh, String bdcdyh, String dcxc, String qlr, String tdzl, String bdclx, String bdclxdm, String zdtzm, String htbh, String qlxzdm, String bdcdyhs, String exactQuery, String fwbm) {
        return selectBdcdyHandleService.getDjsjBdcdySearchMap(djh, bdcdyh, dcxc, qlr, tdzl, bdclx, bdclxdm, zdtzm, htbh, qlxzdm, bdcdyhs, exactQuery, fwbm);
    }

    @Override
    public Map<String, Object> getBdczsListSearchMap(String bdcdyh, String qllx, String bdcqzh, String qlr, String dyr, String zl, String bdclx, String bdclxdm, String dcxc, String zdtzm, String qlxzdm, String dyfs, String ysqlxdm, String proid, String bdcdyhs, String fzqssj, String fzjssj, String zstype, String cqzhjc, String exactQuery, String fwbm, String proids) {
        return selectBdcdyHandleService.getBdczsListSearchMap(bdcdyh, qllx, bdcqzh, qlr, dyr, zl, bdclx, bdclxdm, dcxc, zdtzm, qlxzdm, dyfs, ysqlxdm, proid, bdcdyhs, fzqssj, fzjssj, zstype, cqzhjc, exactQuery, fwbm, proids);
    }

    @Override
    public Map<String, Object> getGdfczListSearchMapAndPath(String bdcdyh, String fczh, String qlr, String fwzl, String dcxc, String qllx, String zdtzm, String proid, String cqzhjc, String exactQuery) {
        return selectBdcdyHandleService.getGdfczListSearchMapAndPath(bdcdyh, fczh, qlr, fwzl, dcxc, qllx, zdtzm, proid, cqzhjc, exactQuery);
    }

    @Override
    public Map<String, Object> getGdtdzListSearchMapAndPath(String bdcdyh, String tdzh, String qlr, String dcxc, String tdzl, String qllx, String zdtzm, String proid, String cqzhjc, String exactQuery) {
        return selectBdcdyHandleService.getGdtdzListSearchMapAndPath(bdcdyh, tdzh, qlr, dcxc, tdzl, qllx, zdtzm, proid, cqzhjc, exactQuery);
    }

    @Override
    public Map<String, String> getBdcDateByQlid(String qlid) {
        return selectBdcdyHandleService.getBdcDateByQlid(qlid);
    }

    @Override
    public Map<String, String> getXzyy(String qlid, String cqzh) {
        return selectBdcdyHandleService.getXzyy(qlid, cqzh);
    }

    @Override
    public Map<String, Object> getGdcfListSearchMap(String proid, String bdcdyh, String cfwh, String qlr, String dcxc, String yqzh, String fwzl, String tdzl, String exactQuery) {
        return selectBdcdyHandleService.getGdcfListSearchMap(proid, bdcdyh, cfwh, qlr, dcxc, yqzh, fwzl, tdzl, exactQuery);
    }

    @Override
    public String getGdCfCqzh(String bdcdyid, String gdproid, String qlid) {
        return selectBdcdyHandleService.getGdCfCqzh(bdcdyid, gdproid, qlid);
    }

    @Override
    public void getShowHhcfModel(Model model, String proid) {
        selectBdcdyHandleService.getShowHhcfModel(model, proid);
    }

    @Override
    public Map<String, Object> getdataMapByProid(String proid) {
        return selectBdcdyHandleService.getdataMapByProid(proid);
    }

    @Override
    public Map<String, Object> getQlxxListMap(String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm, String qlxzdm, String dyfs, String ysqlxdm, String proid, String bzxr, String cfwh, String cqzhjc, String exactQuery, String fwbm, String proids) {
        return selectBdcdyHandleService.getQlxxListMap(bdcdyh, qllx, bdcqzh, qlr, zl, bdclxdm, dcxc, zdtzm, qlxzdm, dyfs, ysqlxdm, proid, bzxr, cfwh, cqzhjc, exactQuery, fwbm, proids);
    }

    @Override
    public Map<String, Object> getBdcdyZt(String proid, String djid, String bdclx) {
        return selectBdcdyHandleService.getBdcdyZt(proid, djid, bdclx);
    }

    @Override
    public Map<String, Object> getBdcdyQlZt(String proid, String djid, String bdclx) {
        return selectBdcdyHandleService.getBdcdyQlZt(proid, djid, bdclx);
    }

    @Override
    public Map<String, Object> getBdcdyJyZt(String fwbm,String proid) {
        if(StringUtils.isBlank(fwbm)&&StringUtils.isNotBlank(proid)){
            BdcBdcdy bdcdy = bdcdyService.getBdcdyByProid(proid);
            if(bdcdy!=null){
                fwbm=bdcdy.getFwbm();
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotBlank(fwbm)) {
            List<Map> mapList = new ArrayList<>();
            Map map = new HashMap();
            map.put("houseCode", fwbm);
            mapList.add(map);
            String result = currencyService.checkHouseZt(mapList);
            if (StringUtils.isNotBlank(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.containsKey("statuscode") && StringUtils.equals(String.valueOf(jsonObject.get("statuscode")), "0") && jsonObject.containsKey("housecodelist")) {
                    List<JSONObject> housecodelist = (List<JSONObject>) jsonObject.get("housecodelist");
                    if (CollectionUtils.isNotEmpty(housecodelist)) {
                        for (JSONObject object : housecodelist) {
                            if (object.containsKey("housecode") && StringUtils.isNotBlank((CharSequence) object.get("housecode")) && object.containsKey("housestatus") && StringUtils.isNotBlank((CharSequence) object.get("housestatus"))) {
                                resultMap.put("jyzt", object.get("housestatus"));
                            }
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> getMulBdcqzxx(String yxmids) {
        return selectBdcdyHandleService.getMulBdcqzxx(yxmids);
    }

    @Override
    public Map<String, Object> getZdQlrByDjh(String djh) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotBlank(djh)) {
            List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(djh);
            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                StringBuilder qlrStringBuilder = new StringBuilder();
                for (DjsjZdxx djsjZdxx : djsjZdxxList) {
                    if (StringUtils.isNotBlank(djsjZdxx.getQlrmc())) {
                        if (StringUtils.isNotBlank(qlrStringBuilder)) {
                            qlrStringBuilder.append(",").append(djsjZdxx.getQlrmc());
                        } else {
                            qlrStringBuilder.append(djsjZdxx.getQlrmc());
                        }
                    }
                }
                resultMap.put("qlr", qlrStringBuilder.toString());
            }
        }
        return resultMap;
    }
}
