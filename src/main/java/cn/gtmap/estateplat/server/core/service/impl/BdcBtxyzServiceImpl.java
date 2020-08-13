package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcXtLimitfieldMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.thread.BtxyzThread;
import cn.gtmap.estateplat.server.thread.ThreadEngine;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2019/4/12
 * @description
 */
@Service
public class BdcBtxyzServiceImpl implements BdcBtxyzService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXtLimitfieldMapper bdcXtLimitfieldMapper;
    @Autowired
    private BdcXtLimitfieldService bdcXtLimitfieldService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private ThreadEngine threadEngine;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private QllxService qllxService;
    private Integer btxyzKjdx = 10;

    @Override
    public List<Map> btxyz(String proid, String workflowId, String workflowNodeId) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        List<Map> resultList = null;
        if (null != bdcXm && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            List<List<String>> proidcsList = getYzcs(bdcXmList);
            Map<String, List<Map>> validateTableMap = getValidateTableMap(workflowId, workflowNodeId);
            Map<String, String> tableSqlMap = getTableSql();
            if (CollectionUtils.isNotEmpty(proidcsList) && MapUtils.isNotEmpty(validateTableMap) && MapUtils.isNotEmpty(tableSqlMap)) {
                if (proidcsList.size() > 1) {
                    List<BtxyzThread> btxyzThreadList = new ArrayList<BtxyzThread>();
                    BdcBtxyzService bdcBtxyzService = (BdcBtxyzService) Container.getBean("bdcBtxyzServiceImpl");
                    for (List<String> proidList : proidcsList) {
                        BtxyzThread btxyzThread = new BtxyzThread(bdcBtxyzService, proidList, validateTableMap, tableSqlMap);
                        btxyzThreadList.add(btxyzThread);
                    }
                    threadEngine.excuteThread(btxyzThreadList, true);
                    Set<Map> resultSet = new HashSet<Map>();
                    for (BtxyzThread btxyzThread : btxyzThreadList) {
                        resultSet.addAll(new HashSet<Map>(btxyzThread.getResultList()));
                    }
                    resultList = new ArrayList<Map>(resultSet);
                } else {
                    resultList = btxyzThread(proidcsList.get(0), validateTableMap, tableSqlMap);
                }
            }
        }
        return resultList;
    }

    /**
     * @param proids           项目ID集合
     * @param validateTableMap 验证项
     * @param tableSqlMap      sqlMap集合
     * @return 验证结果
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 多线程验证
     */
    @Override
    public List<Map> btxyzThread(List<String> proids, Map<String, List<Map>> validateTableMap, Map<String, String> tableSqlMap) {
        List<Map> resultList = null;
        if (validateTableMap != null && CollectionUtils.isNotEmpty(proids)) {
            //保存必填项验证信息
            Set<Map> resultSet = new HashSet<Map>();
            //分割合并在审核签名节点需要验证表的tableid（读配置）
            String fghbShQmTableId = AppConfig.getProperty("btxyz.fghbShQm.tableid");
            BdcXm bdcXmTemp = null;
            HashMap resultMap = null;
            //房地产权多幢和独幢分开验证(一般不存在批量流程既有多幢又有独幢的情况，这里取第一个产权进行判断)
            Map queryBdcXmMap = new HashMap();
            queryBdcXmMap.put("proids", proids);
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(queryBdcXmMap);
            //bdclx土地，房屋分开，批量流程不动产类型一致
            boolean bdclxTdFlag = false;
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                if (bdcXmList.get(0) != null && StringUtils.equals(bdcXmList.get(0).getBdclx(), Constants.BDCLX_TD)) {
                    bdclxTdFlag = true;
                }
                for (BdcXm bdcXm : bdcXmList) {
                    if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_GYTD_FWSUQ)) {
                        bdcXmTemp = bdcXm;
                        break;
                    }
                }
            }
            QllxVo qllxVO = qllxService.makeSureQllx(bdcXmTemp);
            boolean bdcFdcqDzFlag = false;
            boolean bdcFdcqFlag = false;
            if (qllxVO instanceof BdcFdcqDz) {
                bdcFdcqDzFlag = true;
            } else if (qllxVO instanceof BdcFdcq) {
                bdcFdcqFlag = true;
            }
            for (String key : validateTableMap.keySet()) {
                if (StringUtils.isBlank(key)){
                    continue;
                }
                //独幢，多幢分开验证
                if (bdcFdcqFlag && (StringUtils.equals(key, Constants.BTXYZ_TABLEID_FDCQDZ) || StringUtils.equals(key, Constants.BTXYZ_TABLEID_FDCQDZ_ZXX))) {
                    continue;
                }
                if (bdcFdcqDzFlag && StringUtils.equals(key, Constants.BTXYZ_TABLEID_FDCQ)) {
                    continue;
                }
                List<Map> validateTableList = validateTableMap.get(key);
                String tableSql = "";
                StringBuilder proidInSql = new StringBuilder();
                StringBuilder proidStr = new StringBuilder();
                if (tableSqlMap.containsKey(key)) {
                    tableSql = tableSqlMap.get(key);
                }
                List<String> qllxList=new ArrayList<>();
                //初审，复审，核定sql只需要一个proid去查wiid
                if(CommonUtil.indexOfStrs(Constants.BTXYZ_TABLEID_SHQM,key)){
                    proidInSql.append("='"+proids.get(0)+"'");
                }else{
                    proidInSql.append(" in (");
                    for (String proid : proids) {
                        if (StringUtils.isBlank(proidStr)) {
                            proidStr.append("'" + proid + "'");
                        } else {
                            proidStr.append(",'" + proid + "'");
                        }
                        BdcXm bdcXm=bdcXmService.getBdcXmByProid(proid);
                        if(StringUtils.isNotBlank(bdcXm.getQllx())){
                            qllxList.add(bdcXm.getQllx());
                        }
                    }
                    proidInSql.append(proidStr);
                    proidInSql.append(")");
                }

                if (StringUtils.isNotBlank(tableSql)) {
                    tableSql = StringUtils.replace(tableSql, "=@proid", proidInSql.toString());
                }
                List<Map> validateDataList = new ArrayList<Map>();
                String[] tableSqlArray = tableSql.split(ParamsConstants.SEPARATION_CHARACTER_SEMICOION);
                for (String tableSqlTemp: tableSqlArray){
                    if(StringUtils.isNotBlank(tableSqlTemp)){
                        List<Map> validateDataTempList = bdcXtLimitfieldService.runSql(platformUtil.initOptProperties(tableSqlTemp));
                        validateDataList.addAll(validateDataTempList);
                    }
                }
                if (CollectionUtils.isEmpty(validateDataList)) {
                    for (Map validateTabMap : validateTableList) {
                        if(validateTabMap.containsKey("QLLX")&&StringUtils.isNotBlank((CharSequence) validateTabMap.get("QLLX"))){
                            if(qllxList.contains(validateTabMap.get("QLLX"))){
                                resultMap = new HashMap();
                                //存储错误
                                resultMap.put("error", validateTabMap.get("CPT_DESC") + "中" + validateTabMap.get("TABLE_FIELD_NAME") + "不能为空");
                                resultSet.add(resultMap);
                            }
                        }else{
                            resultMap = new HashMap();
                            //存储错误
                            resultMap.put("error", validateTabMap.get("CPT_DESC") + "中" + validateTabMap.get("TABLE_FIELD_NAME") + "不能为空");
                            resultSet.add(resultMap);
                        }
                    }
                } else if (CollectionUtils.isNotEmpty(validateTableList)) {
                    //分割合并审核意见验证项在pf_usersign表里查的数据条数要和项目数一致才能通过
                    if(StringUtils.isNotBlank(fghbShQmTableId) && (StringUtils.indexOf(fghbShQmTableId,key)> -1)){
                        if (validateDataList.size() < proids.size()){
                            //审核环节只有签名的验证
                            for (Map validateTabMap : validateTableList) {
                                resultMap = new HashMap();
                                //存储错误
                                resultMap.put("error", validateTabMap.get("CPT_DESC") + "中" + validateTabMap.get("TABLE_FIELD_NAME") + "不能为空");
                                resultSet.add(resultMap);
                            }
                        }
                    }
                    String validateField = null;
                    for (Map validateDataMap : validateDataList) {
                        for (Map validateTabMap : validateTableList) {
                            //必填项验证字段统一大写（数据库取出的字段是大写）
                            if (validateTabMap.get("TABLE_FIELD_ID") != null) {
                                validateField = validateTabMap.get("TABLE_FIELD_ID").toString().toUpperCase();
                            }
                            //净地不验证房屋用途字段
                            if (bdclxTdFlag && StringUtils.equals(validateField, "YT")) {
                                continue;
                            }
                            if (validateDataMap.get(validateField) == null || StringUtils.isBlank(validateDataMap.get(validateField).toString())) {
                                resultMap = new HashMap();
                                //存储错误
                                resultMap.put("error", validateTabMap.get("CPT_DESC") + "中" + validateTabMap.get("TABLE_FIELD_NAME") + "不能为空");
                                resultSet.add(resultMap);
                            }
                        }
                    }

                }
            }
            resultList = new ArrayList<Map>(resultSet);
        }
        return resultList;
    }

    /**
     * @param bdcXmList 项目集合
     * @return 验证参数
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据项目集合获取验证参数 用于线程
     */
    private List<List<String>> getYzcs(List<BdcXm> bdcXmList) {
        List<List<String>> proidcsList = new ArrayList<List<String>>();
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            int shcs = (bdcXmList.size() % btxyzKjdx == 0) ? (int) (bdcXmList.size() / btxyzKjdx) : (int) (bdcXmList.size() / btxyzKjdx) + 1;
            if (shcs > 0) {
                for (int i = 0; i < shcs; i++) {
                    int kssz = i * btxyzKjdx;
                    int jssz = i * btxyzKjdx + btxyzKjdx - 1;
                    List<String> proidList = new ArrayList<String>();
                    for (int j = kssz; j < bdcXmList.size(); j++) {
                        BdcXm bdcXm = bdcXmList.get(j);
                        proidList.add(bdcXm.getProid());
                        if (j == jssz) {
                            break;
                        }
                    }
                    proidcsList.add(proidList);
                }
            }
        }
        return proidcsList;
    }


    private Map<String, List<Map>> getValidateTableMap(String workflowId, String workflowNodeId) {
        Map<String, List<Map>> validateTableMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(workflowId) && StringUtils.isNotBlank(workflowNodeId)) {
            HashMap paraMap = Maps.newHashMap();
            paraMap.put("workflowId", workflowId);
            paraMap.put("workflowNodeId", workflowNodeId);
            List<String> tableIdList = bdcXtLimitfieldMapper.getTableIdGroupTable(paraMap);
            if (CollectionUtils.isNotEmpty(tableIdList)) {
                for (String tableId : tableIdList) {
                    paraMap.put("tableId", tableId);
                    List<Map> validateList = bdcXtLimitfieldService.getWorkflowTransmitValidates(paraMap);
                    if (CollectionUtils.isNotEmpty(validateList)) {
                        validateTableMap.put(tableId, validateList);
                    }
                }
            }
        }
        return validateTableMap;
    }

    private Map<String, String> getTableSql() {
        Map<String, String> tableSqlMap = Maps.newHashMap();
        List<BdcZdTables> bdcZdTablesList = bdcZdGlService.getBdcZdTables();
        if (CollectionUtils.isNotEmpty(bdcZdTablesList)) {
            for (BdcZdTables bdcZdTables : bdcZdTablesList) {
                if (StringUtils.isNotBlank(bdcZdTables.getTableId()) && StringUtils.isNotBlank(bdcZdTables.getTableXmrelSql())) {
                    tableSqlMap.put(bdcZdTables.getId(), bdcZdTables.getTableXmrelSql());
                }
            }
        }
        return tableSqlMap;
    }
}
