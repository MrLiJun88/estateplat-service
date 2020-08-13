package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.model.server.print.XmlData;
import cn.gtmap.estateplat.server.core.mapper.BdcZsPrintMapper;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;
import cn.gtmap.estateplat.server.core.model.PageXml;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.zs.BdcZsTyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by sunchao on 2016-5-28.
 */
@Repository
public class BdcZsPrintServiceImpl implements BdcZsPrintService {
    @Autowired
    private BdcZsPrintMapper bdcZsPrintMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcZsTyService bdcZsTyService;
    @Autowired
    private BdcXmService bdcXmService;


    @Override
    public MulDataToPrintXml getMulZsPrintXml(String zslx, String serverUrl, Map<String, String> proidAndzsid) throws UnsupportedEncodingException {
        List<HashMap> hashMapList;
        MulDataToPrintXml muldataToPrintXml = new MulDataToPrintXml();
        XmlData dataSourceData = null;
        List<PageXml> pageXmlList = new LinkedList<PageXml>();
        String qfdw = AppConfig.getProperty("qfdw");
        for (Map.Entry proidAndZsidEntry : proidAndzsid.entrySet()) {
            String proid = String.valueOf(proidAndZsidEntry.getKey());
            String zsid = String.valueOf(proidAndZsidEntry.getValue());
            PageXml pageXml = new PageXml();
            List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
            String bz = "";
            String cqzh = "";
            String bdcdyh = "";
            //证书和证明所有数据的字段，在数据为空的时候赋空防止有控件默认数据出现
            String[] zsname = {"SQSJC", "NF", "SZSXQC", "ZHLSH", "GYQK", "ZL", ParamsConstants.BDCDYH_CAPITAL, "QLLX", "QLXZ", "YT", "MJ", "SYQX", "QLQTZK", "FJ", "QFDW", "BDCQZH", "ZSBH"};
            HashMap map = new HashMap();
            map.put("proid", proid);
            map.put("zsid", zsid);
            if (StringUtils.equals(zslx, Constants.BDCQSCDJXX_BH_DM)) {
                hashMapList = bdcZsPrintMapper.getZmdPrint(proid);
            } else {
                hashMapList = bdcZsPrintMapper.getZsPrint(map);
            }
            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                cqzh = bdcFdcq.getZxzh();
            }
            //组织数据
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        //tanyue 首次登记证发证日期取CZRQ字段的值
                        if (StringUtils.equals(zslx, Constants.BDCQSCDJXX_BH_DM) && StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "CZRQ")) {
                            Date date = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(entry.getValue()));
                            String dateStr = CalendarUtil.sdf_China.format(date);
                            dataSourceDataList.add(zzData("YEAR", "String", String.valueOf(CalendarUtil.getDateYear(date))));
                            dataSourceDataList.add(zzData("MONTH", "String", String.valueOf(CalendarUtil.getDateMonth(date))));
                            dataSourceDataList.add(zzData("DAY", "String", String.valueOf(CalendarUtil.getDateDay(date))));
                            dataSourceData = zzData("FZRQ", "String", dateStr);
                            dataSourceDataList.add(dataSourceData);
                        }
                        if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "FZRQ") && !StringUtils.equals(zslx, Constants.BDCQSCDJXX_BH_DM)) {
                            Date date = null;
                            if (StringUtils.isBlank(CommonUtil.formatEmptyValue(entry.getValue())))
                                date = new Date();
                            else
                                date = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(entry.getValue()));
                            String dateStr = CalendarUtil.sdf_China.format(date);
                            dataSourceDataList.add(zzData("YEAR", "String", String.valueOf(CalendarUtil.getDateYear(date))));
                            dataSourceDataList.add(zzData("MONTH", "String", String.valueOf(CalendarUtil.getDateMonth(date))));
                            dataSourceDataList.add(zzData("DAY", "String", String.valueOf(CalendarUtil.getDateDay(date))));
                            dataSourceData = zzData("FZRQ", "String", dateStr);
                            dataSourceDataList.add(dataSourceData);
                        } else {
                            dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), "String", CommonUtil.formatEmptyValue(entry.getValue()));
                            dataSourceDataList.add(dataSourceData);
                        }
                    }
                }
                dataSourceDataList.add(zzData("QFDW", "String", qfdw));
            } else {

                if (StringUtils.equals(zslx, Constants.BDCQZS_BH_DM)) {
                    for (int i = 0; i != zsname.length; i++) {
                        dataSourceDataList.add(zzData(zsname[i], "String", ""));
                    }
                } else {
                    dataSourceDataList.add(getData("", "年"));
                    dataSourceDataList.add(getData("", "月"));
                    dataSourceDataList.add(getData("", "日"));
                }
            }

            if (StringUtils.isNotBlank(zsid)) {
                BdcZs bdcZsTemp = bdcZsService.queryBdcZsByZsid(zsid);
                if (bdcZsTemp != null) {
                    bz = bdcZsTemp.getBdcqzh();
                    cqzh = cqzh + bdcZsTemp.getZhlsh();
                }
            } else {
                List<BdcZs> bdcZsListbdcZsTemp = bdcZsService.queryBdcZsByProid(proid);
                if (CollectionUtils.isNotEmpty(bdcZsListbdcZsTemp)) {
                    bz = bdcZsListbdcZsTemp.get(0).getBdcqzh();
                    cqzh = cqzh + bdcZsListbdcZsTemp.get(0).getZhlsh();
                }
            }
            BdcBdcdy bdcdy = bdcdyService.getBdcdyByProid(proid);
            if (bdcdy != null) {
                bdcdyh = bdcdy.getBdcdyh();
            }

            dataSourceDataList.add(getEwm("cqzhEwm", cqzh, serverUrl));
            dataSourceDataList.add(getEwm("EWM", bz, serverUrl));
            dataSourceDataList.add(getEwm("bdcdyhEWM", bdcdyh, serverUrl));
            pageXml.setData(dataSourceDataList);
            pageXmlList.add(pageXml);
        }
        muldataToPrintXml.setPage(pageXmlList);
        return muldataToPrintXml;
    }

    @Override
    public DataToPrintXml getZsPrintXml(final String proid, final String zslx, final String serverUrl, final String zsid) throws UnsupportedEncodingException {
        List<HashMap> hashMapList;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;
        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        String bz = "";
        String zl = "";
        String cqzh = "";
        //证书和证明所有数据的字段，在数据为空的时候赋空防止有控件默认数据出现
        String[] zsname = {"SQSJC", "NF", "SZSXQC", "ZHLSH", "GYQK", "ZL", ParamsConstants.BDCDYH_CAPITAL, "QLLX", "QLXZ", "YT", "MJ", "SYQX", "QLQTZK", "FJ", "QFDW", "BDCQZH", "ZSBH"};
        String qfdw = AppConfig.getProperty("qfdw");
        HashMap map = new HashMap();
        map.put("proid", proid);
        map.put("zsid", zsid);
        if (StringUtils.equals(zslx, Constants.BDCQSCDJXX_BH_DM)) {
            hashMapList = bdcZsPrintMapper.getZmdPrint(proid);
        } else {
            hashMapList = bdcZsPrintMapper.getZsPrint(map);
        }
        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
            BdcFdcq bdcFdcq = bdcFdcqList.get(0);
            cqzh = bdcFdcq.getZxzh();
        }
        BdcZs bdcZs = bdcZsService.queryBdcZsByZsid(zsid);
        Integer bdcdyCount = 0;
        if (bdcZs != null && StringUtils.isNotBlank(bdcZs.getZsid())) {
            bdcdyCount = bdcZsService.getBdcdyCountByZsid(bdcZs.getZsid());
        }
        if (bdcdyCount > 1) {
            zl = bdcZs.getZl() + "等";
        }
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        //组织数据
        if (CollectionUtils.isNotEmpty(hashMapList) && bdcXm != null) {
            for (HashMap hashMap : hashMapList) {
                hashMap.put("QLLX", bdcZsTyService.getZsqllx(CommonUtil.formatEmptyValue(hashMap.get("QLLX")), bdcXm));
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    //tanyue 首次登记证发证日期取CZRQ字段的值
                    if (StringUtils.equals(zslx, Constants.BDCQSCDJXX_BH_DM) && StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "CZRQ")) {
                        Date date = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(entry.getValue()));
                        String dateStr = CalendarUtil.sdf_China.format(date);
                        dataSourceDataList.add(zzData("YEAR", "String", String.valueOf(CalendarUtil.getDateYear(date))));
                        dataSourceDataList.add(zzData("MONTH", "String", String.valueOf(CalendarUtil.getDateMonth(date))));
                        dataSourceDataList.add(zzData("DAY", "String", String.valueOf(CalendarUtil.getDateDay(date))));
                        dataSourceData = zzData("FZRQ", "String", dateStr);
                        dataSourceDataList.add(dataSourceData);
                    }
                    if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "FZRQ") && !StringUtils.equals(zslx, Constants.BDCQSCDJXX_BH_DM)) {
                        Date date = null;
                        if (StringUtils.isBlank(CommonUtil.formatEmptyValue(entry.getValue())))
                            date = new Date();
                        else
                            date = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(entry.getValue()));
                        String dateStr = CalendarUtil.sdf_China.format(date);
                        dataSourceDataList.add(zzData("YEAR", "String", String.valueOf(CalendarUtil.getDateYear(date))));
                        dataSourceDataList.add(zzData("MONTH", "String", String.valueOf(CalendarUtil.getDateMonth(date))));
                        dataSourceDataList.add(zzData("DAY", "String", String.valueOf(CalendarUtil.getDateDay(date))));
                        dataSourceData = zzData("FZRQ", "String", dateStr);
                        dataSourceDataList.add(dataSourceData);
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "ZL") && bdcdyCount > 1) {
                        dataSourceData = zzData("ZL", "String", CommonUtil.formatEmptyValue(entry.getValue()) + "等");
                        dataSourceDataList.add(dataSourceData);
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), ParamsConstants.BDCDYH_CAPITAL) && bdcdyCount > 1) {
                        dataSourceData = zzData(ParamsConstants.BDCDYH_CAPITAL, "String", CommonUtil.formatEmptyValue(entry.getValue()) + "等");
                        dataSourceDataList.add(dataSourceData);
                    } else {
                        dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), "String", CommonUtil.formatEmptyValue(entry.getValue()));
                        dataSourceDataList.add(dataSourceData);
                    }
                }
                if (hashMap.containsKey("BDCDYH") && hashMap.get("BDCDYH") != null && StringUtils.isNotBlank((CharSequence) hashMap.get("BDCDYH"))) {
                    String bdcdyh = StringUtils.replace((String) hashMap.get("BDCDYH"), " ", "");
                    String content = serverUrl + "/dcxx/selectHst?bdcdyh=" + bdcdyh;
                    dataSourceData = zzData("HST", "Image", content + "&random=" + Math.random());
                    dataSourceDataList.add(dataSourceData);
                }
            }
            dataSourceDataList.add(zzData("QFDW", "String", qfdw));
        } else {
            if (StringUtils.equals(zslx, Constants.BDCQZS_BH_DM)) {
                for (int i = 0; i != zsname.length; i++) {
                    dataSourceDataList.add(zzData(zsname[i], "String", ""));
                }
            } else {
                dataSourceDataList.add(getData("", "年"));
                dataSourceDataList.add(getData("", "月"));
                dataSourceDataList.add(getData("", "日"));
            }
        }

        if (bdcZs != null) {
            bz = bdcZs.getBdcqzh();
            cqzh = cqzh + bdcZs.getZhlsh();
        } else {
            List<BdcZs> bdcZsListbdcZsTemp = bdcZsService.queryBdcZsByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcZsListbdcZsTemp)) {
                bz = bdcZsListbdcZsTemp.get(0).getBdcqzh();
                cqzh = cqzh + bdcZsListbdcZsTemp.get(0).getZhlsh();
            }
        }

        dataSourceDataList.add(getEwm("cqzhEwm", cqzh, serverUrl));
        dataSourceDataList.add(getEwm("EWM", bz, serverUrl));
        String bdcdyh = bdcdyService.getBdcdyhByProid(proid);
        dataSourceDataList.add(getEwm("DYHEWM", bdcdyh, serverUrl));
        dataToPrintXml.setData(dataSourceDataList);
        return dataToPrintXml;
    }


    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    //生成二维码内容
    public XmlData getEwm(String name, String bz, String serverUrl) throws UnsupportedEncodingException {
        XmlData xmlData = null;
        if (StringUtils.isNotBlank(bz)) {
            String s = URLEncoder.encode(bz, "utf-8");
            xmlData = zzData(name, "Image", serverUrl + "/bdcPrint/getEwm?bz=" + s + "&random=" + Math.random());
        }
        return xmlData;
    }

    //将时间分隔出年月日
    public XmlData getData(String fzrq, String sjgs) {
        String date = "";
        if (StringUtils.isBlank(fzrq)) {
            date = CalendarUtil.sdf.format(CalendarUtil.getCurDate());
        } else {
            date = CalendarUtil.sdf.format(CalendarUtil.formatDate(fzrq));
        }
        String[] dates = date.split("-");


        XmlData xmlData = null;
        //防止数组越界
        if (dates.length > 2) {
            if (StringUtils.equals(sjgs, "年")) {
                xmlData = zzData("YEAR", "String", dates[0]);
            } else if (StringUtils.equals(sjgs, "月")) {
                xmlData = zzData("MONTH", "String", dates[1]);
            } else {
                xmlData = zzData("DAY", "String", dates[2]);
            }
        }

        return xmlData;
    }

    //创建data类并塞入数据
    public XmlData zzData(String name, String type, String value) {
        XmlData xmlData = new XmlData();
        xmlData.setName(name);
        xmlData.setType(type);
        xmlData.setValue(value);
        return xmlData;
    }

}
