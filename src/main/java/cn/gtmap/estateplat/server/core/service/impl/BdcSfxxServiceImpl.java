package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2017/4/6
 * @description
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.model.server.print.XmlData;
import cn.gtmap.estateplat.server.core.mapper.BdcSfxxMapper;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;
import cn.gtmap.estateplat.server.core.model.PageXml;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSfxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcYhService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class BdcSfxxServiceImpl implements BdcSfxxService {
    @Autowired
    BdcSfxxMapper bdcSfxxMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcYhService bdcYhService;
    @Autowired
    BdcXmService bdcXmService;


    private static final String PARAMETER_STRING = "String";

    @Override
    public List<HashMap> getxtsfbzBySqlx(String sqlx, String qlrlx) {
        HashMap map = new HashMap();
        map.put("sqlx", sqlx);
        map.put("qlrlx", qlrlx);
        return bdcSfxxMapper.getxtsfbzBySqlx(map);
    }

    @Override
    public List<HashMap> getxtsfdwBySqlx(String sqlx, String qlrlx) {
        HashMap map = new HashMap();
        map.put("sqlx", sqlx);
        map.put("qlrlx", qlrlx);
        return bdcSfxxMapper.getxtsfdwBySqlx(map);
    }

    @Override
    public List<HashMap> getSfxxMapBySfxxid(HashMap map) {
        return bdcSfxxMapper.getSfxxMapBySfxxid(map);
    }

    @Override
    public DataToPrintXml getFpxxPrintXml(String sfxxid) throws UnsupportedEncodingException {
        List<HashMap> hashMapList;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;
        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        //在数据为空的时候赋空防止有控件默认数据出现
        String[] fpname = {"FKRQC", "FKRZH", "FKRKHYH", "SKRQC", "SKRZH", "SKRKHYH", "FPBH", "TZRQ", "FPH", "CPR", "ZJE", "FKFS", "ZSDWMC", "ZSFWDM", "FPYH", "SFXMBH", "SFXMMC", "DW", "SL", "SFBZ", "JE"};
        HashMap map = new HashMap();
        map.put("sfxxid", sfxxid);
        hashMapList = getSfxxMapBySfxxid(map);
        //组织数据
        if (CollectionUtils.isNotEmpty(hashMapList)) {
            String gspos = AppConfig.getProperty("finance.pos.gs");
            for (HashMap hashMap : hashMapList) {
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "TZRQ")) {
                        Date date = null;
                        if (StringUtils.isBlank(CommonUtil.formatEmptyValue(entry.getValue())))
                            date = new Date();
                        else
                            date = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(entry.getValue()));
                        String dateStr = CalendarUtil.sdf_China.format(date);
                        dataSourceDataList.add(zzData("YEAR", PARAMETER_STRING, String.valueOf(CalendarUtil.getDateYear(date))));
                        dataSourceDataList.add(zzData("MONTH", PARAMETER_STRING, String.valueOf(CalendarUtil.getDateMonth(date))));
                        dataSourceDataList.add(zzData("DAY", PARAMETER_STRING, String.valueOf(CalendarUtil.getDateDay(date))));
                        dataSourceData = zzData("TZRQ", PARAMETER_STRING, dateStr);
                        dataSourceDataList.add(dataSourceData);
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "SFBZ")) {
                        String sfbz = "";
                        if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getValue()))) {
                            sfbz = CommonUtil.formatEmptyValue(entry.getValue()) + "元/件";
                        }
                        dataSourceData = zzData("SFBZ", PARAMETER_STRING, sfbz);
                        dataSourceDataList.add(dataSourceData);
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "PJWYBSM") && StringUtils.isNotBlank(gspos) && StringUtils.equals(gspos, "true")) {
                        String pjywbsm = "";
                        if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getValue()))) {
                            pjywbsm = "标识码：" + CommonUtil.formatEmptyValue(entry.getValue());
                        }
                        dataSourceData = zzData("PJWYBSM", PARAMETER_STRING, pjywbsm);
                        dataSourceDataList.add(dataSourceData);
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "FPH") && StringUtils.isNotBlank(gspos) && StringUtils.equals(gspos, "true")) {
                        String fph = "";
                        if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getValue()))) {
                            fph = "票据号码：" + CommonUtil.formatEmptyValue(entry.getValue());
                        }
                        dataSourceData = zzData("FPH", PARAMETER_STRING, fph);
                        dataSourceDataList.add(dataSourceData);
                    } else {
                        dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), PARAMETER_STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                        dataSourceDataList.add(dataSourceData);
                    }
                }
            }

            if (StringUtils.isNotBlank(gspos) && StringUtils.equals(gspos, "true")) {
                String pjzlbm = AppConfig.getProperty("finance.pos.gs.pjlxbh");
                if (StringUtils.isNotBlank(pjzlbm)) {
                    pjzlbm = "种类编码：" + pjzlbm;
                } else {
                    pjzlbm = "";
                }
                dataSourceData = zzData("ZLBM", PARAMETER_STRING, pjzlbm);
                dataSourceDataList.add(dataSourceData);
            }
        } else {
            for (int i = 0; i != fpname.length; i++) {
                dataSourceDataList.add(zzData(fpname[i], PARAMETER_STRING, ""));
            }
        }
        dataToPrintXml.setData(dataSourceDataList);
        return dataToPrintXml;
    }

    @Override
    public MulDataToPrintXml getMulFpxxPrintXml(Map<String, String> sfxxidMap) throws UnsupportedEncodingException {
        List<HashMap> hashMapList;
        MulDataToPrintXml muldataToPrintXml = new MulDataToPrintXml();
        XmlData dataSourceData = null;
        List<PageXml> pageXmlList = new LinkedList<PageXml>();
        for (Map.Entry sfxxidEntry : sfxxidMap.entrySet()) {
            String sfxxid = String.valueOf(sfxxidEntry.getValue());
            PageXml pageXml = new PageXml();
            List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
            //在数据为空的时候赋空防止有控件默认数据出现
            String[] fpname = {"FKRQC", "FKRZH", "FKRKHYH", "SKRQC", "SKRZH", "SKRKHYH", "FPBH", "TZRQ", "FPH", "CPR", "ZJE", "FKFS", "ZSDWMC", "ZSFWDM", "FPYH", "SFXMBH", "SFXMMC", "DW", "SL", "SFBZ", "JE"};
            HashMap map = new HashMap();
            map.put("sfxxid", sfxxid);
            hashMapList = getSfxxMapBySfxxid(map);
            //组织数据
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                String dw = "";
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entryTemp = (Map.Entry) iterator.next();
                        if (StringUtils.equals(CommonUtil.formatEmptyValue(entryTemp.getKey()), "DW")) {
                            dw = CommonUtil.formatEmptyValue(entryTemp.getValue());
                        }
                    }
                }
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "TZRQ")) {
                            Date date = null;
                            if (StringUtils.isBlank(CommonUtil.formatEmptyValue(entry.getValue())))
                                date = new Date();
                            else
                                date = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(entry.getValue()));
                            String dateStr = CalendarUtil.sdf_China.format(date);
                            dataSourceDataList.add(zzData("YEAR", PARAMETER_STRING, String.valueOf(CalendarUtil.getDateYear(date))));
                            dataSourceDataList.add(zzData("MONTH", PARAMETER_STRING, String.valueOf(CalendarUtil.getDateMonth(date))));
                            dataSourceDataList.add(zzData("DAY", PARAMETER_STRING, String.valueOf(CalendarUtil.getDateDay(date))));
                            dataSourceData = zzData("TZRQ", PARAMETER_STRING, dateStr);
                            dataSourceDataList.add(dataSourceData);
                        } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "SFBZ")) {
                            String sfbz = "";
                            if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getValue()))) {
                                sfbz = CommonUtil.formatEmptyValue(entry.getValue()) + "元/" + dw;
                            }
                            dataSourceData = zzData("SFBZ", PARAMETER_STRING, sfbz);
                            dataSourceDataList.add(dataSourceData);
                        } else {
                            dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), PARAMETER_STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                            dataSourceDataList.add(dataSourceData);
                        }
                    }
                }
            } else {
                for (int i = 0; i != fpname.length; i++) {
                    dataSourceDataList.add(zzData(fpname[i], PARAMETER_STRING, ""));
                }
            }
            pageXml.setData(dataSourceDataList);
            pageXmlList.add(pageXml);
        }
        muldataToPrintXml.setPage(pageXmlList);
        return muldataToPrintXml;
    }

    @Override
    public void updateBdcSfxx(String proid, String sfzt, Date sjrq) {
        bdcSfxxMapper.updateBdcSfxx(proid, sfzt, sjrq);
    }

    //创建data类并塞入数据
    public XmlData zzData(String name, String type, String value) {
        XmlData xmlData = new XmlData();
        xmlData.setName(name);
        xmlData.setType(type);
        xmlData.setValue(value);
        return xmlData;
    }

    @Override
    public BdcSfxx getbdcSfxxByProid(String proid) {
        BdcSfxx bdcSfxx = null;
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcSfxx.class);
            example.createCriteria().andEqualTo("proid", proid);
            List<BdcSfxx> bdcSfxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcSfxxList)) {
                bdcSfxx = bdcSfxxList.get(0);
            }
        }
        return bdcSfxx;
    }

    @Override
    public void changesfzt(String proid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
        for (BdcXm bdcXmTemp : bdcXmList) {
            if (StringUtils.equals(bdcXmTemp.getDjlx(), Constants.DJLX_DYDJ_DM)) {
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmTemp.getProid());
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for (BdcQlr bdcQlr : bdcQlrList) {
                        if (bdcQlr != null && StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                            List<BdcXtYh> bdcXtYhList = bdcYhService.getBankListByYhmc(bdcQlr.getQlrmc());
                            if (CollectionUtils.isNotEmpty(bdcXtYhList) && StringUtils.equals(bdcXtYhList.get(0).getSftysf(), Constants.SFTYSF_S_DM)) {
                                BdcSfxx bdcSfxx = getbdcSfxxByProid(bdcXmTemp.getProid());
                                if (bdcSfxx != null) {
                                    bdcSfxx.setSfzt(Constants.SFXX_SFZT_YJF);
                                    entityMapper.saveOrUpdate(bdcSfxx, bdcSfxx.getSfxxid());
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Map> getBdcSfxxList(HashMap<String, Object> map) {
        return bdcSfxxMapper.getBdcSfxxListJsonByPage(map);
    }

    @Override
    public void insertIdToTemp(Map map) {
        if (MapUtils.isNotEmpty(map)) {
            bdcSfxxMapper.insertIdToTemp(map);
        }
    }

    @Override
    public void delPrintBdcSfxxTemp(String uuid) {
        if (StringUtils.isNotBlank(uuid)) {
            bdcSfxxMapper.delPrintBdcSfxxTemp(uuid);
        }
    }

    @Override
    public List<BdcSfxm> queryBdcSfxmByWiid(String wiid) {
        if(StringUtils.isNotBlank(wiid)){
            return bdcSfxxMapper.queryBdcSfxmByWiid(wiid);
        }
        return null;
    }
}

