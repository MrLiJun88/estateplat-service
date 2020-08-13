package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.model.server.print.XmlData;
import cn.gtmap.estateplat.server.core.mapper.BdcGdxxMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcXmMapper;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;
import cn.gtmap.estateplat.server.core.model.PageXml;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.etl.EtlGxYhService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zdd on 2016/1/7.
 */
@Service
public class BdcGdxxServiceImpl implements BdcGdxxService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String STRING_CAMEL_CASE = "String";
    private static final String SUCCEED_LOWERCASE = "succeed";
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcGdxxMapper bdcGdxxMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmMapper bdcXmMapper;

    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private EtlGxYhService etlGxYhService;

    @Override
    @Transactional
    public void insertBdcGdxx(BdcGdxx bdcGdxx) {
        if (bdcGdxx != null) {
            if (StringUtils.isBlank(bdcGdxx.getGdxxid())) {
                bdcGdxx.setGdxxid(UUIDGenerator.generate18());
            }
            if (StringUtils.isNotEmpty(bdcGdxx.getGdxxid()) && StringUtils.isNotEmpty(bdcGdxx.getXmid())) {
                entityMapper.insertSelective(bdcGdxx);
            }
        }
    }

    @Override
    @Transactional
    public void updateBdcGdxx(BdcGdxx bdcGdxx) {
        if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getGdxxid())) {
            entityMapper.updateByPrimaryKeySelective(bdcGdxx);
        }
    }

    @Override
    public BdcGdxx initBdcGdxx(BdcGdxx bdcGdxx, final String gdxml) {

        try {
            if (StringUtils.isNotBlank(gdxml)) {
                Document document = DocumentHelper.parseText(gdxml);
                /**
                 * @author bianwen
                 * @description 判断归档成功或失败，解析追溯到list根节点
                 */
                Node listNode = document.selectSingleNode("//list");
                if (listNode != null) {
                    if (bdcGdxx == null) {
                        bdcGdxx = new BdcGdxx();
                        bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                    }

                    Element listElement = (Element) listNode;
                    if (StringUtils.isNotBlank(listElement.attributeValue(ParamsConstants.RESULT_LOWERCASE))) {
                        //如果成功记录档案返回的基本信息
                        if (listElement.attributeValue(ParamsConstants.RESULT_LOWERCASE).equals(SUCCEED_LOWERCASE)) {
                            List<Node> archiveList = listNode.selectNodes("archive");
                            if (CollectionUtils.isNotEmpty(archiveList)) {
                                for (Node archive : archiveList) {
                                    Element archiveElement = (Element) archive;
                                    if (StringUtils.isNotBlank(archiveElement.attributeValue(ParamsConstants.RESULT_LOWERCASE)) && archiveElement.attributeValue(ParamsConstants.RESULT_LOWERCASE).equals(SUCCEED_LOWERCASE)) {
                                        List<Node> fieldList = archiveElement.selectNodes("field");
                                        if (CollectionUtils.isNotEmpty(fieldList)) {
                                            for (Node field : fieldList) {
                                                Element fieldElement1 = (Element) field;
                                                String name = fieldElement1.attributeValue("name");
                                                if (name != null && name.equals("id")) {
                                                    bdcGdxx.setDaid(field.getText());
                                                }
                                                if (name != null && name.equals("ajh")) {
                                                    bdcGdxx.setAjh(field.getText());
                                                }
                                                if (name != null && name.equals("mlh")) {
                                                    bdcGdxx.setMlh(field.getText());
                                                }
                                            }
                                        }
                                        bdcGdxx.setGdxx(null);
                                    } else {
                                        if (StringUtils.isNotBlank(archiveElement.attributeValue("msg"))) {
                                            bdcGdxx.setGdxx(archiveElement.attributeValue("msg"));
                                        }
                                    }
                                }
                            }

                        } else {
                            //zdd 记录失败的基本信息
                            if (StringUtils.isNotBlank(listElement.attributeValue("msg"))) {
                                bdcGdxx.setGdxx(listElement.attributeValue("msg"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("BdcGdxxServcie.initBdcGdxx", e);
        }
        return bdcGdxx;
    }

    /**
     * @param
     * @author bianwen
     * @rerutn list
     * @description 批量归档（多个archive）
     */
    public List<BdcGdxx> initPlBdcGdxx(String gdxml) {
        List<BdcGdxx> gdxxList = new ArrayList<BdcGdxx>();

        try {
            if (StringUtils.isNotBlank(gdxml)) {
                Document document = DocumentHelper.parseText(gdxml);
                Node listNode = document.selectSingleNode("//list");
                if (listNode != null) {

                    Element listElement = (Element) listNode;
                    if (StringUtils.isNotBlank(listElement.attributeValue(ParamsConstants.RESULT_LOWERCASE)) && StringUtils.equals(SUCCEED_LOWERCASE, listElement.attributeValue(ParamsConstants.RESULT_LOWERCASE))) {
                        //如果成功记录档案返回的基本信息
                        List<Node> archiveList = listNode.selectNodes("archive");
                        if (CollectionUtils.isNotEmpty(archiveList)) {
                            for (Node archive : archiveList) {
                                BdcGdxx bdcGdxx = new BdcGdxx();
                                bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                                bdcGdxx.setGdrq(new Date());
                                Element archiveElement = (Element) archive;
                                if (StringUtils.isNotBlank(archiveElement.attributeValue("result")) && archiveElement.attributeValue("result").equals(SUCCEED_LOWERCASE)) {
                                    List<Node> fieldList = archiveElement.selectNodes("field");
                                    if (CollectionUtils.isNotEmpty(fieldList)) {
                                        for (Node field : fieldList) {
                                            Element fieldElement1 = (Element) field;
                                            String name = fieldElement1.attributeValue("name");
                                            if (name != null && name.equals("id")) {
                                                bdcGdxx.setDaid(field.getText());
                                            }
                                            if (name != null && name.equals("ajh")) {
                                                bdcGdxx.setAjh(field.getText());
                                            }
                                            if (name != null && name.equals("mlh")) {
                                                bdcGdxx.setMlh(field.getText());
                                            }
                                            if (name != null && name.equals("proId")) {
                                                bdcGdxx.setXmid(field.getText());
                                            }
                                        }
                                    }
                                    bdcGdxx.setGdxx(null);
                                } else {
                                    if (StringUtils.isNotBlank(archiveElement.attributeValue("msg")))
                                        bdcGdxx.setGdxx(archiveElement.attributeValue("msg"));
                                }
                                gdxxList.add(bdcGdxx);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            logger.error("BdcGdxxService.initPlBdcGdxx", e);
        }
        return gdxxList;
    }

    /**
     * @param
     * @return
     * @author wangtao
     * @description
     */
    @Override
    public boolean checkIsGd(final String proid) {
        int count = 0;
        count = bdcGdxxMapper.countXmid(proid);
        return count > 0 ? false : true;
    }

    @Override
    public boolean checkAIsGd(final String wiid) {
        int count = 0;
        count = bdcGdxxMapper.countXmidByWiid(wiid);
        return count > 0 ? false : true;
    }

    /**
     * @author bianwen
     * @description 根据proid获取归档信息
     */
    @Override
    public BdcGdxx selectBdcGdxx(BdcXm bdcXm) {
        BdcGdxx bdcGdxx = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            Example example = new Example(BdcGdxx.class);
            if (StringUtils.isNotBlank(bdcXm.getProid())) {

                example.createCriteria().andEqualTo("xmid", bdcXm.getProid());
                List<BdcGdxx> bdcGdxxList = entityMapper.selectByExample(BdcGdxx.class, example);
                if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                    bdcGdxx = bdcGdxxList.get(0);
                }
            }
        }
        return bdcGdxx;
    }

    @Override
    public DataToPrintXml getDafpxxPrintXml(String proid) {
        Boolean cfdj = false;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx()) && StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CFDJ_DM)) {
                cfdj = true;
            }
        }
        List<HashMap> hashMapList = null;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;
        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        //在数据为空的时候赋空防止有控件默认数据出现
        String[] fpname = {"BDCDYH", "ZL", "QLRMC", "YWRMC", "MLH", "AJH", "PROID", "SJRQ", "NF", "YF", "TXMH", "AJJS", "AJYS"};
        if (cfdj) {
            hashMapList = getCfDafpxxMapByproid(proid);
        } else {
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            hashMapList = getDafpxxMapByproid(map);
        }
        if (CollectionUtils.isNotEmpty(hashMapList)) {
            for (HashMap daFpxxHashmap : hashMapList) {
                String sjDate = CommonUtil.formatEmptyValue(daFpxxHashmap.get("SJRQ"));
                if (StringUtils.isNotBlank(sjDate) && StringUtils.indexOf(sjDate, "-") >= 0) {
                    String[] dateArray = StringUtils.split(sjDate, "-");
                    if (dateArray.length >= 2) {
                        daFpxxHashmap.put("NF", dateArray[0]);
                        daFpxxHashmap.put("YF", dateArray[1]);
                    }
                } else {
                    daFpxxHashmap.put("NF", "");
                    daFpxxHashmap.put("YF", "");
                }
            }
        }
        //组织数据
        if (CollectionUtils.isNotEmpty(hashMapList)) {
            for (HashMap hashMap : hashMapList) {
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING_CAMEL_CASE, CommonUtil.formatEmptyValue(entry.getValue()));
                    dataSourceDataList.add(dataSourceData);
                }
            }
        } else {
            for (int i = 0; i != fpname.length; i++) {
                dataSourceDataList.add(zzData(fpname[i], STRING_CAMEL_CASE, ""));
            }
        }
        dataToPrintXml.setData(dataSourceDataList);
        return dataToPrintXml;
    }

    @Override
    public MulDataToPrintXml getAllDafpxxPrintXml(List<String> proidList) {
        List<HashMap> hashMapList;
        MulDataToPrintXml muldataToPrintXml = new MulDataToPrintXml();
        XmlData dataSourceData = null;
        List<PageXml> pageXmlList = new LinkedList<PageXml>();
        for (String proid : proidList) {
            PageXml pageXml = new PageXml();
            List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
            //在数据为空的时候赋空防止有控件默认数据出现
            String[] fpname = {"BDCDYH", "ZL", "QLRMC", "YWRMC", "MLH", "AJH", "PROID", "SJRQ", "NF", "YF", "TXMH", "AJJS", "AJYS"};
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            hashMapList = getDafpxxMapByproid(map);
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap daFpxxHashmap : hashMapList) {
                    String sjDate = CommonUtil.formatEmptyValue(daFpxxHashmap.get("SJRQ"));
                    if (StringUtils.isNotBlank(sjDate) && StringUtils.indexOf(sjDate, "-") >= 0) {
                        String[] dateArray = StringUtils.split(sjDate, "-");
                        if (dateArray.length >= 2) {
                            daFpxxHashmap.put("NF", dateArray[0]);
                            daFpxxHashmap.put("YF", dateArray[1]);
                        }
                    } else {
                        daFpxxHashmap.put("NF", "");
                        daFpxxHashmap.put("YF", "");
                    }
                }
            }
            //组织数据
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING_CAMEL_CASE, CommonUtil.formatEmptyValue(entry.getValue()));
                        dataSourceDataList.add(dataSourceData);
                    }
                }
            } else {
                for (int i = 0; i != fpname.length; i++) {
                    dataSourceDataList.add(zzData(fpname[i], STRING_CAMEL_CASE, ""));
                }
            }
            pageXml.setData(dataSourceDataList);
            pageXmlList.add(pageXml);
        }
        muldataToPrintXml.setPage(pageXmlList);
        return muldataToPrintXml;
    }

    @Override
    public List<HashMap> getDafpxxMapByproid(HashMap map) {
        return bdcGdxxMapper.getDafpxxMapByProid(map);
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
    public List<BdcGdxx> getGdxxByDdid(String daid) {
        List<BdcGdxx> bdcGdxxeList = new ArrayList<BdcGdxx>();
        if (StringUtils.isNotBlank(daid)) {
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("daid", daid);
            bdcGdxxeList = entityMapper.selectByExample(example);
        }
        return bdcGdxxeList;
    }

    @Override
    public HashMap<String, String> updateJjdbh(List<String> bhList) {
        return creatJjdbh(bhList);
    }

    public String checkDjlx(List<String> bhList) {
        /**
         *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
         *@description 验证登记类型是否一致
         */
        String msg = "";
        if (CollectionUtils.isNotEmpty(bhList)) {
            List<BdcXm> bdcXmList = bdcXmMapper.getBdcXmListByBh(bhList.get(0));
            if (CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.get(0) != null && StringUtils.isNotBlank(bdcXmList.get(0).getDjlx())) {
                String djlx = bdcXmList.get(0).getDjlx();
                if (StringUtils.isNotBlank(djlx)) {
                    for (String bh : bhList) {
                        if (StringUtils.isNotBlank(bh)) {
                            List<BdcXm> bdcXmListTmp = bdcXmMapper.getBdcXmListByBh(bh);
                            if (CollectionUtils.isNotEmpty(bdcXmListTmp) && bdcXmListTmp.get(0) != null && StringUtils.isNotBlank(bdcXmListTmp.get(0).getDjlx()) && !StringUtils.equals(bdcXmListTmp.get(0).getDjlx(), djlx)) {
                                msg = "登记类型不一致";
                            }
                        }
                    }
                }
            }
        }
        return msg;
    }

    public HashMap<String, String> creatJjdbh(List<String> bhList) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        String sqlxString = AppConfig.getProperty("tbh.sqlx");
        if (StringUtils.isNotBlank(sqlxString)) {
            String[] sqlxKindArr = sqlxString.split(";");
            List<String> sqlxKindList = new ArrayList<String>();
            CollectionUtils.addAll(sqlxKindList, sqlxKindArr);
            if (CollectionUtils.isNotEmpty(sqlxKindList)) {
                for (String sqlxs : sqlxKindList) {
                    String jjdbh = getJjdBh();
                    if (StringUtils.isNotBlank(sqlxs) && StringUtils.isNotBlank(jjdbh)) {
                        String[] sqlxArr = sqlxs.split(",");
                        List<String> sqlxList = new ArrayList<String>();
                        CollectionUtils.addAll(sqlxList, sqlxArr);
                        if (CollectionUtils.isNotEmpty(sqlxList)) {
                            for (String bh : bhList) {
                                if (StringUtils.isNotBlank(bh)) {
                                    String platfromSqlx = bdcXmService.getPlatfromSqlxByBh(bh);
                                    if (StringUtils.isBlank(platfromSqlx)) {
                                        resultMap.put("success", "false");
                                        resultMap.put("message", "未获取到流程申请类型，生成统编号失败！");
                                    } else {
                                        if (sqlxList.contains(platfromSqlx)) {
                                            List<BdcXm> bdcXmList = bdcXmMapper.getBdcXmListByBh(bh);
                                            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                                                for (BdcXm bdcXm : bdcXmList) {
                                                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                                                        bdcXm.setJjdbh(jjdbh);
                                                        bdcXmService.saveBdcXm(bdcXm);
                                                    }
                                                }
                                                resultMap.put("success", "true");
                                                resultMap.put("message", "生成统编号成功！");
                                            }
                                        }
                                    }
                                }
                            }
                            if (CollectionUtils.isEmpty(resultMap.entrySet())) {
                                resultMap.put("success", "false");
                                resultMap.put("message", "该项目申请类型未配置，生成统编号失败！");
                            }
                        } else {
                            resultMap.put("success", "false");
                            resultMap.put("message", "统编号申请类型配置为空，生成统编号失败！");
                        }
                    }
                }
            } else {
                resultMap.put("success", "false");
                resultMap.put("message", "统编号申请类型配置为空，生成统编号失败！");
            }
        } else {
            resultMap.put("success", "false");
            resultMap.put("message", "统编号申请类型配置为空，生成统编号失败！");
        }
        return resultMap;
    }

    public String getJjdBh() {
        String jjdbh = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String temp = "%0".concat(String.valueOf(3)).concat("d");
        String list = bdcGdxxMapper.getSzyjdbh();
        int lsh = 0;
        if (StringUtils.isNotEmpty(list)) {
            lsh = Integer.parseInt(list);
        }
        jjdbh += String.format(temp, lsh);
        return jjdbh;
    }

    @Override
    public List<BdcGdxx> getGdxxByXmid(String proid) {
        List<BdcGdxx> bdcGdxxeList = new ArrayList<BdcGdxx>();
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("xmid", proid);
            bdcGdxxeList = entityMapper.selectByExample(example);
        }
        return bdcGdxxeList;
    }

    @Override
    public void updateGdxxByNew(BdcGdxx bdcGdxx, BdcGdxx newbdcGdxx) {
        if (null != bdcGdxx && null != newbdcGdxx) {
            if (StringUtils.isNoneBlank(newbdcGdxx.getAjh())) {
                bdcGdxx.setAjh(newbdcGdxx.getAjh());
            }
            if (newbdcGdxx.getAjjs() != null) {
                bdcGdxx.setAjjs(newbdcGdxx.getAjjs());
            }
            if (newbdcGdxx.getAjys() != null) {
                bdcGdxx.setAjys(newbdcGdxx.getAjys());
            }
            if (StringUtils.isNotBlank(newbdcGdxx.getMlh())) {
                bdcGdxx.setMlh(newbdcGdxx.getMlh());
            }
            if (StringUtils.isBlank(bdcGdxx.getGdxxid())) {
                bdcGdxx.setGdxxid(UUIDGenerator.generate18());
            }
            if (StringUtils.isNotBlank(bdcGdxx.getMlh()) && StringUtils.isNotBlank(bdcGdxx.getAjh())) {
                reOrganizeDaidByAjhAndMlh(bdcGdxx, false);
                if (null != newbdcGdxx.getGdrq()) {
                    bdcGdxx.setGdrq(newbdcGdxx.getGdrq());
                }
                if (StringUtils.isNotBlank(newbdcGdxx.getGdr())) {
                    bdcGdxx.setGdr(newbdcGdxx.getGdr());
                }
            }
            entityMapper.saveOrUpdate(bdcGdxx, bdcGdxx.getGdxxid());
        }
    }

    @Override
    public String getGdxxProidByWiid(String wiid) {
        String proid = "";
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                if (bdcXmList.size() > 1) {
                    for (BdcXm bdcXm : bdcXmList) {
                        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getQllx()) && !StringUtils.equals(Constants.QLLX_DYAQ, bdcXm.getQllx())) {
                            proid = bdcXm.getProid();
                            break;
                        }
                    }
                    BdcXm bdcXm = bdcXmList.get(0);
                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDY_DDD_SQLXDM)) {
                        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                        proid = pfWorkFlowInstanceVo.getProId();
                    }
                } else {
                    BdcXm bdcXmTemp = bdcXmList.get(0);
                    if (bdcXmTemp != null && StringUtils.isNotBlank(bdcXmTemp.getProid())) {
                        proid = bdcXmTemp.getProid();
                    }
                }
            }
        }
        return proid;
    }

    @Override
    public void saveGdxxForPl(BdcGdxx bdcGdxx, String userid) {
        if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getXmid())) {
            String proids = bdcXmService.getProidsByQllxAndWiid(bdcGdxx.getXmid(), "");
            String[] proidArray = StringUtils.split(proids, Constants.SPLIT_STR);
            for (String proid : proidArray) {
                List<BdcGdxx> bdcGdxxList = getGdxxByXmid(proid);
                if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                    BdcGdxx bdcGdxxTemp = bdcGdxxList.get(0);
                    if (null != bdcGdxxTemp) {
                        updateGdxxByNew(bdcGdxxTemp, bdcGdxx);
                    }
                } else {
                    bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                    bdcGdxx.setXmid(proid);
                    if (StringUtils.isNotBlank(bdcGdxx.getMlh()) && StringUtils.isNotBlank(bdcGdxx.getAjh())) {
                        reOrganizeDaidByAjhAndMlh(bdcGdxx, false);
                    } else {
                        bdcGdxx.setGdrq(null);
                        bdcGdxx.setGdr("");
                    }
                    entityMapper.saveOrUpdate(bdcGdxx, bdcGdxx.getGdxxid());
                }
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                    updateJyZtByWiid(bdcXm.getWiid(), userid);
                }
            }
        }
    }

    @Override
    public void saveGdxxMulForPl(BdcGdxx bdcGdxx) {
        if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getXmid())) {
            String proid = bdcGdxx.getXmid();
            List<BdcGdxx> bdcGdxxList = getGdxxByXmid(proid);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                BdcGdxx bdcGdxxTemp = bdcGdxxList.get(0);
                if (null != bdcGdxxTemp) {
                    updateGdxxByNew(bdcGdxxTemp, bdcGdxx);
                }
            } else {
                bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                bdcGdxx.setXmid(proid);
                if (StringUtils.isNotBlank(bdcGdxx.getMlh()) && StringUtils.isNotBlank(bdcGdxx.getAjh())) {
                    reOrganizeDaidByAjhAndMlh(bdcGdxx, false);
                } else {
                    bdcGdxx.setGdrq(null);
                    bdcGdxx.setGdr("");
                }
                entityMapper.saveOrUpdate(bdcGdxx, bdcGdxx.getGdxxid());
            }
        }
    }

    @Override
    public int getMaxAjhByMlh(String mlh, boolean sfGd) {
        int ajh = 0;
        if (StringUtils.isNotBlank(mlh)) {
            try {
                if (!sfGd) {
                    ajh = bdcGdxxMapper.getCurrentMaxAjhByMlh(mlh) + 1;
                } else {
                    ajh = bdcGdxxMapper.getCurrentMaxAjhByMlhGd(mlh) + 1;
                }
            } catch (Exception e) {
                ajh++;
                logger.error("未搜到对应的目录号的归档记录");
            }
        }
        return ajh;
    }

    @Override
    public List<HashMap> getCfDafpxxMapByproid(String proid) {
        List<HashMap> oldHashMapList = new ArrayList<HashMap>();
        List<HashMap> newHashMapList = new ArrayList<HashMap>();
        if (StringUtils.isNotBlank(proid)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            oldHashMapList = getDafpxxMapByproid(map);
        }
        if (CollectionUtils.isNotEmpty(oldHashMapList)) {
            String qlrmc = "";
            HashMap fpxxMap = oldHashMapList.get(0);
            fpxxMap.remove(ParamsConstants.QLRMC_LOWERCASE);
            fpxxMap.remove(ParamsConstants.YWRMC_LOWERCASE);
            //qlrmc为现势产权权利人 ywrmc为查封机关
            if (StringUtils.isNotBlank(proid)) {
                BdcCf bdcCf = bdcCfService.selectCfByProid(proid);
                if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getCfjg())) {
                    fpxxMap.put(ParamsConstants.YWRMC_LOWERCASE, bdcCf.getCfjg());
                    qlrmc = bdcQlrService.getXsQlQlrByProid(proid, false);
                }
                List<BdcXmRel> bdcXmRelList = null;
                if (bdcCf == null) {
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                }
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                    if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                        Example example = new Example(GdCf.class);
                        example.createCriteria().andEqualTo("cfid", bdcXmRel.getYqlid());
                        List<GdCf> gdCfList = entityMapper.selectByExample(example);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            if (StringUtils.isNotBlank(gdCf.getCfjg())) {
                                fpxxMap.put(ParamsConstants.YWRMC_LOWERCASE, gdCf.getCfjg());
                            }
                            qlrmc = bdcQlrService.getXsQlQlrByProid(proid, true);
                        }
                    }
                }
                if (StringUtils.isNotBlank(qlrmc)) {
                    fpxxMap.put(ParamsConstants.QLRMC_LOWERCASE, qlrmc);
                }
            }
            newHashMapList.add(fpxxMap);
        }
        return newHashMapList;
    }

    @Override
    public void saveOrUpdateBdcGdxx(List<BdcXm> bdcXmlist, String userName) {
        // 只在点击归档按钮的时候赋值归档人和归档日期，生成归档daid，所以在归档前首先保存案卷号和目录号
        if (CollectionUtils.isNotEmpty(bdcXmlist)) {
            for (BdcXm bdcXm : bdcXmlist) {
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                    List<BdcGdxx> bdcGdxxlist = getGdxxByXmid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcGdxxlist)) {
                        for (BdcGdxx bdcGdxx : bdcGdxxlist) {
                            bdcGdxx.setGdr(userName);
                            bdcGdxx.setGdrq(new Date());
                            reOrganizeDaidByAjhAndMlh(bdcGdxx, false);
                            entityMapper.saveOrUpdate(bdcGdxx, bdcGdxx.getGdxxid());
                        }
                    } else {
                        BdcGdxx bdcGdxx = new BdcGdxx();
                        bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                        bdcGdxx.setXmid(bdcXm.getProid());
                        bdcGdxx.setGdr(userName);
                        bdcGdxx.setGdrq(new Date());
                        entityMapper.saveOrUpdate(bdcGdxx, bdcGdxx.getGdxxid());
                    }
                }
            }
        }
    }

    @Override
    public void reOrganizeDaidByAjhAndMlh(BdcGdxx bdcGdxx, boolean ishandledAjh) {
        // 档案号配置后置
        String dahSuffix = AppConfig.getProperty("bdcGdxx.daid.suffix");
        if (null != bdcGdxx && StringUtils.isNotBlank(bdcGdxx.getAjh()) && StringUtils.isNotBlank(bdcGdxx.getMlh())) {
            // 案卷号判断是否已经处理过了，这边只按三位补足目录号
            String formatAjh = bdcGdxx.getAjh();
            if (ishandledAjh == Boolean.FALSE) {
                formatAjh = formatString(formatAjh, 4);
            }
            String formatMlh = formatString(bdcGdxx.getMlh(), 3);
            // 档案号生成规则：目录号 + C4 + 案卷号 + 后缀，要求后缀可配置
            String daidNew = formatMlh + "C4" + formatAjh + dahSuffix;
            bdcGdxx.setDaid(daidNew);
            bdcGdxx.setAjh(formatAjh);
            bdcGdxx.setMlh(formatMlh);
        }
    }

    // 格式化补足字符串
    private String formatString(String stringTemp, int length) {
        String returnString = "";
        if (StringUtils.isNotBlank(stringTemp) && length > 0) {
            int stringLength = stringTemp.length();
            int zeroNumber = length - stringLength;
            StringBuilder stringBuilder = new StringBuilder();
            if (zeroNumber > 0) {
                for (int i = 0; i < zeroNumber; i++) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(stringTemp);
                returnString = stringBuilder.toString();
            } else {
                returnString = stringTemp;
            }
        }
        return returnString;
    }

    public Map getCqProidAndCqWiidByBh(String bh) {
        //吴江暂未有需要归档的批量流程 暂不考虑批量
        Map map = Maps.newHashMap();
        String wiid = "";
        String proid = "";
        if (StringUtils.isNotBlank(bh)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(bh);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    if (StringUtils.isNotBlank(bdcXm.getQllx())) {
                        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                            wiid = bdcXm.getWiid();
                        }
                        if (StringUtils.isNotBlank(bdcXm.getProid())) {
                            proid = bdcXm.getProid();
                        }
                        break;
                    }
                }
            }
        }
        map.put(ParamsConstants.WIID_LOWERCASE, wiid);
        map.put(ParamsConstants.PROID_LOWERCASE, proid);
        return map;
    }

    //获取归档信息填充弹出框
    public Map getCurrentGdxxInfo(String proid) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(proid)) {
            List<BdcGdxx> bdcGdxxList = getGdxxByXmid(proid);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                BdcGdxx bdcGdxx = bdcGdxxList.get(0);
                if (null != bdcGdxx) {
                    if (bdcGdxx.getAjjs() != null && bdcGdxx.getAjjs() >= 0) {
                        map.put("ajjs", CommonUtil.formatEmptyValue(bdcGdxx.getAjjs()));
                    }
                    if (bdcGdxx.getAjys() != null && bdcGdxx.getAjys() >= 0) {
                        map.put("ajys", CommonUtil.formatEmptyValue(bdcGdxx.getAjys()));
                    }
                    if (StringUtils.isNotBlank(bdcGdxx.getAjh())) {
                        map.put("ajh", CommonUtil.formatEmptyValue(bdcGdxx.getAjh()));
                    }
                    if (StringUtils.isNotBlank(bdcGdxx.getMlh())) {
                        map.put("mlh", CommonUtil.formatEmptyValue(bdcGdxx.getMlh()));
                    }
                }
            }
        }
        return map;
    }

    //异步获取归档信息
    public JSONObject getBdcGdxxByProid(String proid) {
        JSONObject resultMap = new JSONObject();
        String ajh = "";
        String mlh = "";
        String gdr = "";
        String gdrq = "";
        String daid = "";
        String ajjs = "";
        String ajys = "";
        String gdxxid = "";
        if (StringUtils.isNotBlank(proid)) {
            List<BdcGdxx> bdcGdxxList = getGdxxByXmid(proid);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                BdcGdxx bdcGdxx = bdcGdxxList.get(0);
                if (null != bdcGdxx) {
                    ajh = CommonUtil.formatEmptyValue(bdcGdxx.getAjh());
                    ajjs = CommonUtil.formatEmptyValue(bdcGdxx.getAjjs());
                    ajys = CommonUtil.formatEmptyValue(bdcGdxx.getAjys());
                    mlh = CommonUtil.formatEmptyValue(bdcGdxx.getMlh());
                    gdr = CommonUtil.formatEmptyValue(bdcGdxx.getGdr());
                    gdxxid = CommonUtil.formatEmptyValue(bdcGdxx.getGdxxid());
                    if (bdcGdxx.getGdrq() != null) {
                        gdrq = CommonUtil.formatEmptyValue(CalendarUtil.sdf.format(bdcGdxx.getGdrq()));
                    }
                    daid = CommonUtil.formatEmptyValue(bdcGdxx.getDaid());
                }
            }
        }
        resultMap.put("ajh", ajh);
        resultMap.put("ajys", ajys);
        resultMap.put("ajjs", ajjs);
        resultMap.put("mlh", mlh);
        resultMap.put("gdr", gdr);
        resultMap.put("gdrq", gdrq);
        resultMap.put("daid", daid);
        resultMap.put("gdxxid", gdxxid);
        return resultMap;
    }

    @Override
    public void updateJyZtByWiid(String wiid, String userid) {
        if (StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(userid)) {
            String wfProid = PlatformUtil.getPfProidByWiid(wiid);
            if (StringUtils.isNotBlank(wfProid)) {
                etlGxYhService.dbYhxx(wfProid, wiid, "7", userid);
            }
        }
    }

    @Override
    public List<HashMap> getGdDafpxxMapByGdxxid(HashMap map) {
        return bdcGdxxMapper.getGdDafpxxMapByGdxxid(map);
    }

    @Override
    public DataToPrintXml getGdDafpxxPrintXml(String gdxxid) {
        List<HashMap> hashMapList = null;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;
        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        //在数据为空的时候赋空防止有控件默认数据出现
        String[] fpname = {"BDCDYH", "ZL", "QLRMC", "YWRMC", "MLH", "AJH", "PROID", "SJRQ", "NF", "YF", "TXMH", "AJJS", "AJYS"};

        HashMap map = new HashMap();
        map.put("gdxxid", gdxxid);
        hashMapList = getGdDafpxxMapByGdxxid(map);

        if (CollectionUtils.isNotEmpty(hashMapList)) {
            for (HashMap daFpxxHashmap : hashMapList) {
                String sjDate = CommonUtil.formatEmptyValue(daFpxxHashmap.get("SJRQ"));
                if (StringUtils.isNotBlank(sjDate) && StringUtils.indexOf(sjDate, "-") >= 0) {
                    String[] dateArray = StringUtils.split(sjDate, "-");
                    if (dateArray.length >= 2) {
                        daFpxxHashmap.put("NF", dateArray[0]);
                        daFpxxHashmap.put("YF", dateArray[1]);
                    }
                } else {
                    daFpxxHashmap.put("NF", "");
                    daFpxxHashmap.put("YF", "");
                }
            }
        }
        //组织数据
        if (CollectionUtils.isNotEmpty(hashMapList)) {
            for (HashMap hashMap : hashMapList) {
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING_CAMEL_CASE, CommonUtil.formatEmptyValue(entry.getValue()));
                    dataSourceDataList.add(dataSourceData);
                }
            }
        } else {
            for (int i = 0; i != fpname.length; i++) {
                dataSourceDataList.add(zzData(fpname[i], STRING_CAMEL_CASE, ""));
            }
        }
        dataToPrintXml.setData(dataSourceDataList);
        return dataToPrintXml;
    }

    @Override
    public MulDataToPrintXml getAllGdDafpxxPrintXml(List<String> gdxxidList) {
        List<HashMap> hashMapList;
        MulDataToPrintXml muldataToPrintXml = new MulDataToPrintXml();
        XmlData dataSourceData = null;
        List<PageXml> pageXmlList = new LinkedList<PageXml>();
        for (String gdxxid : gdxxidList) {
            PageXml pageXml = new PageXml();
            List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
            //在数据为空的时候赋空防止有控件默认数据出现
            String[] fpname = {"BDCDYH", "ZL", "QLRMC", "YWRMC", "MLH", "AJH", "PROID", "SJRQ", "NF", "YF", "TXMH", "AJJS", "AJYS"};
            HashMap map = new HashMap();
            map.put("gdxxid", gdxxid);
            hashMapList = getGdDafpxxMapByGdxxid(map);
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap daFpxxHashmap : hashMapList) {
                    String sjDate = CommonUtil.formatEmptyValue(daFpxxHashmap.get("SJRQ"));
                    if (StringUtils.isNotBlank(sjDate) && StringUtils.indexOf(sjDate, "-") >= 0) {
                        String[] dateArray = StringUtils.split(sjDate, "-");
                        if (dateArray.length >= 2) {
                            daFpxxHashmap.put("NF", dateArray[0]);
                            daFpxxHashmap.put("YF", dateArray[1]);
                        }
                    } else {
                        daFpxxHashmap.put("NF", "");
                        daFpxxHashmap.put("YF", "");
                    }
                }
            }
            //组织数据
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING_CAMEL_CASE, CommonUtil.formatEmptyValue(entry.getValue()));
                        dataSourceDataList.add(dataSourceData);
                    }
                }
            } else {
                for (int i = 0; i != fpname.length; i++) {
                    dataSourceDataList.add(zzData(fpname[i], STRING_CAMEL_CASE, ""));
                }
            }
            pageXml.setData(dataSourceDataList);
            pageXmlList.add(pageXml);
        }
        muldataToPrintXml.setPage(pageXmlList);
        return muldataToPrintXml;
    }

    @Override
    public JSONObject getBdcGdxxByGdxxid(String gdxxid) {
        JSONObject resultMap = new JSONObject();
        String ajh = "";
        String mlh = "";
        String gdr = "";
        String gdrq = "";
        String daid = "";
        String ajjs = "";
        String ajys = "";
        if (StringUtils.isNotBlank(gdxxid)) {
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("gdxxid", gdxxid);
            List<BdcGdxx> bdcGdxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                BdcGdxx bdcGdxx = bdcGdxxList.get(0);
                if (null != bdcGdxx) {
                    ajh = CommonUtil.formatEmptyValue(bdcGdxx.getAjh());
                    ajjs = CommonUtil.formatEmptyValue(bdcGdxx.getAjjs());
                    ajys = CommonUtil.formatEmptyValue(bdcGdxx.getAjys());
                    mlh = CommonUtil.formatEmptyValue(bdcGdxx.getMlh());
                    gdr = CommonUtil.formatEmptyValue(bdcGdxx.getGdr());
                    gdxxid = CommonUtil.formatEmptyValue(bdcGdxx.getGdxxid());
                    if (bdcGdxx.getGdrq() != null) {
                        gdrq = CommonUtil.formatEmptyValue(CalendarUtil.sdf.format(bdcGdxx.getGdrq()));
                    }
                    daid = CommonUtil.formatEmptyValue(bdcGdxx.getDaid());
                }
            }
        }
        resultMap.put("ajh", ajh);
        resultMap.put("ajys", ajys);
        resultMap.put("ajjs", ajjs);
        resultMap.put("mlh", mlh);
        resultMap.put("gdr", gdr);
        resultMap.put("gdrq", gdrq);
        resultMap.put("daid", daid);
        resultMap.put("gdxxid", gdxxid);
        return resultMap;
    }

    @Override
    public BdcGdxx queryBdcGdxxByProid(String proid) {
        BdcGdxx bdcGdxx = null;
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcGdxx.class);
            example.createCriteria().andEqualTo("xmid", proid);
            List<BdcGdxx> bdcGdxxList = entityMapper.selectByExample(BdcGdxx.class, example);
            if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                bdcGdxx = bdcGdxxList.get(0);
            }
        }
        return bdcGdxx;
    }

    @Override
    public String checkGdxxByProid(String proids) {
        String msg = "";
        if (StringUtils.isNotBlank(proids)) {
            for(String proid:proids.split(",")){
                BdcGdxx bdcGdxx = queryBdcGdxxByProid(proid);
                if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getDaid())) {
                    msg="已存在部分归档，是否重新归档！";
                    break;
                }
            }
        }
        return msg;
    }

    //批量修改按钮点击修改案卷号和目录号
    @Override
    public Map saveMulGdxxInfo(String ids, BdcGdxx bdcGdxx, String username, Boolean isMul) {
        String message = "";
        String proidTemp = "";
        String ajhFormat = "";
        Map<String, String> map = Maps.newHashMap();
        if (null != bdcGdxx && StringUtils.isNotBlank(bdcGdxx.getMlh()) && StringUtils.isNotBlank(bdcGdxx.getAjh())) {
            int ajh = Integer.parseInt(bdcGdxx.getAjh());
            int maxCurrentAjh = 1;
            //读取配置同一目录下最大的案卷号限制，如果未配置则默认为3000
            String maxAjh = StringUtils.isNotBlank(AppConfig.getProperty("gdxx.ajhMaxLimit")) ? AppConfig.getProperty("gdxx.ajhMaxLimit") : "3000";
            try {
                maxCurrentAjh = bdcGdxxMapper.getCurrentMaxAjhByMlh(bdcGdxx.getMlh());
            } catch (Exception e) {
                logger.error("未搜到对应的目录号的归档记录");
            }
            int leftAjh = Integer.parseInt(maxAjh) - maxCurrentAjh;
            if (StringUtils.isNotBlank(ids)) {
                String[] proid = ids.split(",");
                if (leftAjh >= proid.length) {
                    for (int i = 0; i < proid.length; i++) {
                        //格式化案卷号，四位续编，不足补0
                        if (ajh < 10 && ajh > 0) {
                            ajhFormat = "000" + ajh;
                        } else if (ajh >= 10 && ajh < 100) {
                            ajhFormat = "00" + ajh;
                        } else if (ajh >= 100 && ajh < 1000) {
                            ajhFormat = "0" + ajh;
                        } else {
                            ajhFormat = String.valueOf(ajh);
                        }
                        if (isMul) {
                            proidTemp = proid[i];
                        } else {
                            proidTemp = getGdxxProidByWiid(proid[i]);
                        }
                        List<BdcGdxx> bdcGdxxList = getGdxxByXmid(proidTemp);
                        if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                            for (BdcGdxx bdcGdxxTemp : bdcGdxxList) {
                                bdcGdxxTemp.setAjh(ajhFormat);
                                bdcGdxxTemp.setMlh(bdcGdxx.getMlh());
                                reOrganizeDaidByAjhAndMlh(bdcGdxx, true);
                                bdcGdxxTemp.setGdr(username);
                                bdcGdxxTemp.setGdrq(new Date());
                                entityMapper.saveOrUpdate(bdcGdxxTemp, bdcGdxxTemp.getGdxxid());
                                message = "修改成功";
                            }
                        } else {
                            BdcGdxx bdcGdxxNew = new BdcGdxx();
                            bdcGdxxNew.setGdxxid(UUIDGenerator.generate18());
                            bdcGdxxNew.setXmid(proidTemp);
                            bdcGdxxNew.setAjh(ajhFormat);
                            bdcGdxxNew.setMlh(bdcGdxx.getMlh());
                            reOrganizeDaidByAjhAndMlh(bdcGdxxNew, true);
                            bdcGdxxNew.setGdrq(new Date());
                            bdcGdxxNew.setGdr(username);
                            entityMapper.insertSelective(bdcGdxxNew);
                            message = "保存成功";
                        }
                        ajh++;
                    }
                } else {
                    message = "该目录号所剩案卷数" + leftAjh + "，案卷号不足，请重新输入新的目录号！";
                }
            }
            map.put("msg", message);
        }
        return map;
    }

    //批量修改按钮点击修改案卷号和目录号
    public Map<String, String> saveMulGdxxInfoGd(String gdids, String mlh, String ajh) {
        HashMap resultMap = new HashMap();
        String msg = "修改失败";
        if (StringUtils.isNoneBlank(gdids, mlh, ajh)) {
            //案卷号四位格式化
            if (ajh.length() == 1) {
                ajh = "000" + ajh;
            } else if (ajh.length() == 2) {
                ajh = "00" + ajh;
            } else if (ajh.length() == 3) {
                ajh = "0" + ajh;
            }

            String[] gdxxids = gdids.split(",");
            if (gdxxids.length > 0) {
                for (String gdxxid : gdxxids) {
                    BdcGdxx bdcGdxx = entityMapper.selectByPrimaryKey(BdcGdxx.class, gdxxid);
                    bdcGdxx.setMlh(mlh);
                    bdcGdxx.setAjh(ajh);
                    entityMapper.saveOrUpdate(bdcGdxx, bdcGdxx.getGdxxid());
                }
            }
            msg = "修改成功";
        }
        resultMap.put("msg", msg);
        return resultMap;
    }
}
