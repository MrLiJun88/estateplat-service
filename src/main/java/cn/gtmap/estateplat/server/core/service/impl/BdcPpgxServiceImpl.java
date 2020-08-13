package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcPpgx;
import cn.gtmap.estateplat.model.server.core.BdcPpgxXm;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.model.server.print.FdcqDzxx;
import cn.gtmap.estateplat.model.server.print.FdcqDzxxPage;
import cn.gtmap.estateplat.model.server.print.XmlData;
import cn.gtmap.estateplat.server.core.mapper.BdcPpgxXmMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/26 0026
 * @description 不动产匹配关系表
 */
@Service
public class BdcPpgxServiceImpl implements BdcPpgxService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcPpgxLogService bdcPpgxLogService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcPpgxXmMapper bdcPpgxXmMapper;
    @Autowired
    private BdcPicService bdcPicService;
    @Autowired
    private DjsjFwService djsjFwService;

    private static final String PARAMETER_STRING = "String";

    /**
     * @param fwproid 房屋项目ID
     * @return 不动产匹配关系表
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据房屋项目ID获取不动产匹配关系表
     */
    @Override
    public BdcPpgx getBdcPpgxByFwproid(String fwproid) {
        BdcPpgx bdcPpgx = null;
        if (StringUtils.isNotBlank(fwproid)) {
            Example example = new Example(BdcPpgx.class);
            example.createCriteria().andEqualTo("fwproid", fwproid);
            List<BdcPpgx> bdcPpgxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                bdcPpgx = bdcPpgxList.get(0);
            }
        }
        return bdcPpgx;
    }


    /**
     * @param bdcdyh 不动产单元号
     * @return 不动产匹配关系表
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取不动产匹配关系表
     */
    @Override
    public List<BdcPpgx> getBdcPpgxByBdcdyh(String bdcdyh) {
        List<BdcPpgx> bdcPpgxList = Lists.newArrayList();
        if (StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(BdcPpgx.class);
            example.createCriteria().andEqualTo("bdcdyh", bdcdyh);
            bdcPpgxList = entityMapper.selectByExample(example);
        }
        if (CollectionUtils.isEmpty(bdcPpgxList)) {
            bdcPpgxList = Collections.EMPTY_LIST;
        }
        return bdcPpgxList;
    }

    /**
     * @param bdcdyh  不动产单元号
     * @param fwproid
     * @return 不动产匹配关系表
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取不动产匹配关系表
     */
    @Override
    public List<BdcPpgx> getBdcPpgxByBdcdyh(String bdcdyh, String fwproid) {
        List<BdcPpgx> bdcPpgxList = Lists.newArrayList();
        if (StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(BdcPpgx.class);
            example.createCriteria().andEqualTo("bdcdyh", bdcdyh);
            if (StringUtils.isNotBlank(fwproid)) {
                example.createCriteria().andEqualTo("fwproid", fwproid);
            }
            bdcPpgxList = entityMapper.selectByExample(example);
        }
        if (CollectionUtils.isEmpty(bdcPpgxList)) {
            bdcPpgxList = Collections.EMPTY_LIST;
        }
        return bdcPpgxList;
    }

    @Override
    public BdcPpgx getBdcPpgxByTdproid(String tdproid) {
        BdcPpgx bdcPpgx = null;
        if (StringUtils.isNotBlank(tdproid)) {
            Example example = new Example(BdcPpgx.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(tdproid)) {
                criteria.andEqualTo(ParamsConstants.TDPROID_LOWERCASE, tdproid);
            }
            List<BdcPpgx> bdcPpgxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                bdcPpgx = bdcPpgxList.get(0);
            }
        }
        return bdcPpgx;
    }

    @Override
    public void saveBdcPpgx(BdcPpgx bdcPpgx) {
        if (bdcPpgx != null && StringUtils.isNotBlank(bdcPpgx.getGxid())) {
            entityMapper.saveOrUpdate(bdcPpgx, bdcPpgx.getGxid());
        }
    }

    @Override
    public void saveBdcPpgxXm(BdcPpgxXm bdcPpgxXm) {
        if (bdcPpgxXm != null && StringUtils.isNotBlank(bdcPpgxXm.getPpxmid())) {
            entityMapper.saveOrUpdate(bdcPpgxXm, bdcPpgxXm.getPpxmid());
        }
    }

    @Override
    public void saveOrUpdateBdcPpgxByMap(Map map) {
        if (map != null) {
            String fwProid = map.get(ParamsConstants.FWPROID_LOWERCASE) != null ? map.get(ParamsConstants.FWPROID_LOWERCASE).toString() : null;
            String tdProid = map.get(ParamsConstants.TDPROID_LOWERCASE) != null ? map.get(ParamsConstants.TDPROID_LOWERCASE).toString() : null;
            String bdclx = map.get(ParamsConstants.BDCLX_LOWERCASE) != null ? map.get(ParamsConstants.BDCLX_LOWERCASE).toString() : null;
            BdcPpgx bdcPpgx = null;
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                bdcPpgx = getBdcPpgxByFwproid(fwProid);
            } else {
                bdcPpgx = getBdcPpgxByTdproid(tdProid);
            }
            if (bdcPpgx == null) {
                bdcPpgx = new BdcPpgx();
                bdcPpgx.setGxid(UUIDGenerator.generate18());
            } else {
                bdcPpgx.setGxid(bdcPpgx.getGxid());
            }
            bdcPpgx.setBdcdyh(map.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? map.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null);
            bdcPpgx.setFwproid(fwProid);
            bdcPpgx.setTdproid(tdProid);
            bdcPpgx.setSfhqqj(map.get(ParamsConstants.SFHQQJ_LOWERCASE) != null ? map.get(ParamsConstants.SFHQQJ_LOWERCASE).toString() : null);
            saveBdcPpgx(bdcPpgx);
        }
    }

    @Override
    public void saveBdcPpgxByMap(Map<String, Object> ppParamMap, String fwProid, String tdProid, String fwBdcdyh, String tdBdcdyh, String czlx, String cxBdcdyh, String sfhqqj) {

        String bdcdyh = ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
        String qjid = ppParamMap.get(ParamsConstants.QJID_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.QJID_LOWERCASE).toString() : null;
        String bdclx = ppParamMap.get(ParamsConstants.BDCLX_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.BDCLX_LOWERCASE).toString() : null;
        String userid = ppParamMap.get(ParamsConstants.USERID_HUMP) != null ? ppParamMap.get(ParamsConstants.USERID_HUMP).toString() : null;


        //组织匹配关系数据
        Map<String, Object> ppgxMap = new HashMap<>();
        ppgxMap.put(ParamsConstants.FWPROID_LOWERCASE, fwProid);
        ppgxMap.put(ParamsConstants.TDPROID_LOWERCASE, tdProid);
        ppgxMap.put(ParamsConstants.FWBDCDYH_LOWERCASE, fwBdcdyh);
        ppgxMap.put(ParamsConstants.TDBDCDYH_LOWERCASE, tdBdcdyh);
        ppgxMap.put(ParamsConstants.QJID_LOWERCASE, qjid);
        if (StringUtils.equals(Constants.BDC_PP_CZLX_CX, czlx)) {
            ppgxMap.put(ParamsConstants.BDCDYH_LOWERCASE, cxBdcdyh);
        } else {
            ppgxMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        }
        ppgxMap.put(ParamsConstants.USERID_HUMP, userid);
        ppgxMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        ppgxMap.put(ParamsConstants.CZLX_LOWERCASE, czlx);
        ppgxMap.put(ParamsConstants.SFHQQJ_LOWERCASE, sfhqqj);
        //更新或插入bdcPpgx
        saveOrUpdateBdcPpgxByMap(ppgxMap);
        //插入bdcPpgxLog
        bdcPpgxLogService.insertBdcPpgxLogByMap(ppgxMap);
    }

    @Override
    public void saveBdcPpgxXmByMap(Map<String, Object> ppParamMap, String proid) {
        String bdcdyh = ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
        if (StringUtils.isNotBlank(proid) || StringUtils.isNotBlank(bdcdyh)) {
            BdcPpgxXm bdcPpgxXm = new BdcPpgxXm();
            bdcPpgxXm.setPpxmid(UUIDGenerator.generate18());
            bdcPpgxXm.setBdcdyh(bdcdyh);
            bdcPpgxXm.setProid(proid);
            //更新或插入bdcPpgxXm
            saveBdcPpgxXm(bdcPpgxXm);
        }
    }

    @Override
    public DataToPrintXml getPpjgPrintXml(String proid) {
        String bdcdyh = StringUtils.EMPTY;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;

        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        List<FdcqDzxxPage> fdcqDzxxPageList = new ArrayList<FdcqDzxxPage>();
        List<FdcqDzxx> fdcqDzxxList = new ArrayList<FdcqDzxx>();
        XmlData detailSourceData = null;

        FdcqDzxxPage fdcqDzxxPage = new FdcqDzxxPage();
        //在数据为空的时候赋空防止有控件默认数据出现
        String[] fpname = {"BDCDYH", "ZL", "FCZH", "TDZH", "DYSJ", "EWM", "FTTDMJ", "DYTDMJ", "SFHQQJ"};

        List<Map> resultList = getPpjgByProid(proid);
        int id = 1;
        try {
            //组织数据
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map map : resultList) {
                    FdcqDzxx fdcqDzxx = new FdcqDzxx();
                    List<XmlData> detaildataSourceDataList = new ArrayList<XmlData>();
                    Iterator iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "BDCDYH")) {
                            bdcdyh = CommonUtil.formatEmptyValue(entry.getValue());
                            dataSourceData = zzData("BDCDYH", PARAMETER_STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                            dataSourceDataList.add(dataSourceData);
                        } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "ZL")) {
                            dataSourceData = zzData("ZL", PARAMETER_STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                            dataSourceDataList.add(dataSourceData);
                        } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "FCZH")) {
                            detailSourceData = zzData("FCZH", PARAMETER_STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                            detaildataSourceDataList.add(detailSourceData);
                        } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "TDZH")) {
                            detailSourceData = zzData("TDZH", PARAMETER_STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                            detaildataSourceDataList.add(detailSourceData);
                        } else if (StringUtils.equals(CommonUtil.formatEmptyValue(entry.getKey()), "SFHQQJ")) {
                            dataSourceData = zzData("SFHQQJ", PARAMETER_STRING, StringUtils.equalsIgnoreCase(CommonUtil.formatEmptyValue(entry.getValue()), "1") ? Constants.YES_CHS : Constants.NO_CHS);
                            dataSourceDataList.add(dataSourceData);
                        } else {
                            dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), PARAMETER_STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                            dataSourceDataList.add(dataSourceData);
                        }
                    }
                    fdcqDzxx.setXmlDataList(detaildataSourceDataList);
                    fdcqDzxx.setId(String.valueOf(id));
                    fdcqDzxxList.add(fdcqDzxx);
                    id++;
                }
                if (StringUtils.isNotBlank(bdcdyh)) {
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
                    if (bdcBdcdy != null) {
                        String djid = djsjFwService.getDjidByBdcdyh(bdcBdcdy.getBdcdyh());
                        HashMap<String, Object> resultMap = (HashMap<String, Object>) bdcPicService.getTdmj(djid, bdcBdcdy.getBdclx());
                        if (MapUtils.isNotEmpty(resultMap)) {
                            detailSourceData = zzData("FTTDMJ", PARAMETER_STRING, CommonUtil.formatEmptyValue(resultMap.get("fttdmj")));
                            dataSourceDataList.add(detailSourceData);
                            detailSourceData = zzData("DYTDMJ", PARAMETER_STRING, CommonUtil.formatEmptyValue(resultMap.get("dytdmj")));
                            dataSourceDataList.add(detailSourceData);
                        }
                    }
                }

                dataSourceData = zzData("DYSJ", PARAMETER_STRING, CalendarUtil.getCurChinaYMDStrDate());
                dataSourceDataList.add(dataSourceData);

                dataSourceData = getEwm("EWM", bdcdyh, AppConfig.getProperty("server.url"));
                dataSourceDataList.add(dataSourceData);


                fdcqDzxxPage.setRow(fdcqDzxxList);
                fdcqDzxxPage.setId("bdccqxxList");
                fdcqDzxxPageList.add(fdcqDzxxPage);


            } else {
                for (int i = 0; i != fpname.length; i++) {
                    dataSourceDataList.add(zzData(fpname[i], PARAMETER_STRING, ""));
                }
            }
            dataToPrintXml.setData(dataSourceDataList);
            dataToPrintXml.setDetail(fdcqDzxxPageList);
        } catch (Exception e) {
            logger.error("打印匹配关系错误：", e);
        }
        return dataToPrintXml;
    }

    @Override
    public List<BdcPpgxXm> getBdcPpgxXmByBdcdyh(String bdcdyh) {
        if (StringUtils.isNotBlank(bdcdyh)) {
            return bdcPpgxXmMapper.getBdcPpgxXmByBdcdyh(bdcdyh);
        }
        return null;
    }

    @Override
    public void saveBdcPpgxLogByMap(Map<String, Object> ppParamMap, String fwproid, String tdproid, String fwbdcdyh, String tdbdcdyh, String czlx) {
        String bdcdyh = ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
        String qjid = ppParamMap.get(ParamsConstants.QJID_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.QJID_LOWERCASE).toString() : null;
        String bdclx = ppParamMap.get(ParamsConstants.BDCLX_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.BDCLX_LOWERCASE).toString() : null;
        String userid = ppParamMap.get(ParamsConstants.USERID_HUMP) != null ? ppParamMap.get(ParamsConstants.USERID_HUMP).toString() : null;

        //组织匹配关系数据
        Map<String, Object> ppgxMap = new HashMap<>();
        ppgxMap.put(ParamsConstants.FWPROID_LOWERCASE, fwproid);
        ppgxMap.put(ParamsConstants.TDPROID_LOWERCASE, tdproid);
        ppgxMap.put(ParamsConstants.FWBDCDYH_LOWERCASE, fwbdcdyh);
        ppgxMap.put(ParamsConstants.TDBDCDYH_LOWERCASE, tdbdcdyh);
        ppgxMap.put(ParamsConstants.QJID_LOWERCASE, qjid);
        ppgxMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        ppgxMap.put(ParamsConstants.USERID_HUMP, userid);
        ppgxMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        ppgxMap.put(ParamsConstants.CZLX_LOWERCASE, czlx);

        //插入bdcPpgxLog
        bdcPpgxLogService.insertBdcPpgxLogByMap(ppgxMap);
    }

    private List<Map> getPpjgByProid(String proid) {

        List<Map> resultList = new ArrayList<Map>();
        Map params = Maps.newHashMap();
        String fwBdcqzh = "";
        String tdBdcqzh = "";
        String ppsj = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null) {
                List<BdcPpgx> bdcPpgxList = getBdcPpgxByBdcdyh(bdcSpxx.getBdcdyh());
                if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                    for (BdcPpgx bdcPpgx : bdcPpgxList) {
                        Map<String, Object> resultMap = Maps.newHashMap();
                        Map fwZsMap = bdcZsService.queryBdcqzhByProid(bdcPpgx.getFwproid());
                        if (MapUtils.isNotEmpty(fwZsMap) && fwZsMap.containsKey("BDCQZH")) {
                            fwBdcqzh = fwZsMap.get("BDCQZH").toString();
                        }
                        Map tdZsMap = bdcZsService.queryBdcqzhByProid(bdcPpgx.getTdproid());
                        if (MapUtils.isNotEmpty(tdZsMap) && tdZsMap.containsKey("BDCQZH")) {
                            tdBdcqzh = tdZsMap.get("BDCQZH").toString();
                        }
                        resultMap.put("BDCDYH", bdcPpgx.getBdcdyh());
                        resultMap.put("ZL", bdcSpxx.getZl());
                        resultMap.put("FCZH", fwBdcqzh);
                        resultMap.put("TDZH", tdBdcqzh);
                        resultMap.put("SFHQQJ", bdcPpgx.getSfhqqj());
                        resultList.add(resultMap);
                    }
                }
            }
            return resultList;
        }
        return null;
    }

    //创建data类并塞入数据
    public XmlData zzData(String name, String type, String value) {
        value = value.equals("0.0") ? "" : value;
        XmlData xmlData = new XmlData();
        xmlData.setName(name);
        xmlData.setType(type);
        xmlData.setValue(value);
        return xmlData;
    }

    //生成二维码内容
    public XmlData getEwm(String name, String bz, String serverUrl) throws UnsupportedEncodingException {
        XmlData xmlData = null;
        if (StringUtils.isNotBlank(bz)) {
            xmlData = zzData(name, "Image", serverUrl + "/bdcPrint/getEwm?bz=" + bz + "&random=" + Math.random());
        }
        return xmlData;
    }
}
