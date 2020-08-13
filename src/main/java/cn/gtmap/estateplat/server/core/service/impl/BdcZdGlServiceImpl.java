package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lst on 2015/3/17.
 */
@Repository
public class BdcZdGlServiceImpl implements BdcZdGlService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlMapper bdcZdGlMapper;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    private List<Map> djlxQllxRelList = new ArrayList<Map>();

    private List<Map> bdclxQllxRelList = new ArrayList<Map>();


    private HashMap zdLogMap = new HashMap();

    @Override
    public List<BdcZdDjlx> getBdcDjlx() {
        return entityMapper.select(new BdcZdDjlx());
    }

    @Override
    public List<BdcZdSqlx> getBdcSqlx() {
        return entityMapper.select(new BdcZdSqlx());
    }

    @Override
    public List<BdcZdSqlx> getSqlxOrderbyDm() {
        return bdcZdGlMapper.getSqlxOrderbyDm();
    }


    @Override
    public List<BdcZdQszt> getBdcZdQszt() {
        return entityMapper.select(new BdcZdQszt());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcZdFwyt> getBdcZdFwyt() {
        return entityMapper.select(new BdcZdFwyt());
    }


    @Override
    public List<BdcZdQllx> getBdcQllx() {
        return entityMapper.select(new BdcZdQllx());
    }

    @Override
    public List<Map> getZdBdclx() {
        return bdcZdGlMapper.getZdBdclx();
    }

    @Override
    public String getBdclxMcByDm(String dm, List<Map> bdclxList) {
        String bdclxMc = "";
        if (StringUtils.isNotEmpty(dm) && CollectionUtils.isNotEmpty(bdclxList)) {
            for (Map maptmp : bdclxList) {
                if (maptmp.get("DM") != null && StringUtils.isNotEmpty(maptmp.get("DM").toString()) &&
                        StringUtils.equals(dm, maptmp.get("DM").toString()) &&
                        maptmp.get("MC") != null && StringUtils.isNotBlank(maptmp.get("MC").toString())) {
                    bdclxMc = maptmp.get("MC").toString();
                    break;
                }
            }
        }
        return bdclxMc;
    }

    @Override
    public Double getZdmj(final String djh) {
        return bdcZdGlMapper.getZdmj(djh);
    }

    @Override
    public List<BdcFdcq> getFdcqs(final String djh) {
        return bdcZdGlMapper.getFdcqs(djh);
    }

    @PostConstruct
    public List<HashMap> initZdInfo() {

        bdclxQllxRelList = bdcZdGlMapper.getbdclxQllxRel();
        djlxQllxRelList = bdcZdGlMapper.getdjlxQllxRel();
        return null;
    }

    @Override
    public List<?> getqllxByDjlxOrBdclx(final String djlxdm, final String bdclxdm) {
        List<Map> qllxList = new ArrayList<Map>();

        List<Map> djlxQllxList = new ArrayList<Map>();
        if (StringUtils.isNotBlank(djlxdm) && CollectionUtils.isNotEmpty(djlxQllxRelList)) {
            for (Map map : djlxQllxRelList) {
                if (map.containsKey(ParamsConstants.DJLXDM_CAPITAL) && map.get(ParamsConstants.DJLXDM_CAPITAL).equals(djlxdm)) {
                    djlxQllxList.add(map);
                }
            }
        }

        List<Map> bdclxQllxList = new ArrayList<Map>();
        if (StringUtils.isNotBlank(bdclxdm) && CollectionUtils.isNotEmpty(bdclxQllxRelList)) {
            for (Map map : bdclxQllxRelList) {
                if (map.containsKey("BDCLXDM") && map.get("BDCLXDM").equals(bdclxdm)) {
                    bdclxQllxList.add(map);
                }
            }
        }
        //zdd 如果登记类型权利类型关系表为空  则返回不动产类型权利类型关系表
        if (CollectionUtils.isEmpty(djlxQllxList)) {
            qllxList = bdclxQllxList;
        } else if (CollectionUtils.isEmpty(bdclxQllxList)) {
            //zdd 如果不动产类型权利类型关系表为空  则返回登记类型权利类型关系表
            qllxList = djlxQllxList;
        } else {
            //zdd 如果两者都不为空  那么取共同部分
            for (Map map : bdclxQllxList) {
                for (Map tempMap : djlxQllxList) {
                    if (map.get(ParamsConstants.QLLXDM_CAPITAL).equals(tempMap.get(ParamsConstants.QLLXDM_CAPITAL))) {
                        qllxList.add(map);
                        break;
                    }
                }
            }
        }

        return qllxList;
    }


    @Override
    public List<Map> getZdYt() {
        return bdcZdGlMapper.getZdYt();
    }

    @Override
    public Map getZdytByDm(final String dm) {
        return bdcZdGlMapper.getZdytByDm(dm);
    }

    @Override
    public String getZdytMcByDm(String dm) {
        return bdcZdGlMapper.getZdytMcByDm(dm);
    }

    @Override
    public String getFwytByDm(String dm) {
        return bdcZdGlMapper.getFwytByDm(dm);
    }

    @Override
    public List<String> getBdcQllxDmByDjlx(final String djlxdm) {
        List<String> qllxList = null;
        if (StringUtils.isNotBlank(djlxdm) && CollectionUtils.isNotEmpty(djlxQllxRelList)) {
            qllxList = new ArrayList<String>();
            for (Map map : djlxQllxRelList) {
                if (map.get(ParamsConstants.DJLXDM_CAPITAL) != null && StringUtils.equals(map.get(ParamsConstants.DJLXDM_CAPITAL).toString(), djlxdm) && map.get(ParamsConstants.QLLXDM_CAPITAL) != null) {
                    qllxList.add(CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL)));
                }
            }

        }
        return qllxList;
    }

    /**
     * 获取申请字典数据
     *
     * @return
     */
    public List<Map> getZdSqlxList() {
        return bdcZdGlMapper.getZdSqlxList();
    }


    @Override
    public BdcZdLogController getZdLogControllerByPath(final String controllerPath) {
        return entityMapper.selectByPrimaryKey(BdcZdLogController.class, controllerPath);
    }

    @Override
    @PostConstruct
    public List<BdcZdLogController> getZdLogController() {
        List<BdcZdLogController> zdLogControllerList = entityMapper.select(new BdcZdLogController());
        if (zdLogControllerList != null) {
            for (int i = 0; i < zdLogControllerList.size(); i++) {
                zdLogMap.put(zdLogControllerList.get(i).getControllerPath(), zdLogControllerList.get(i).getControllerMsg());
            }
        }
        return zdLogControllerList;
    }

    @Override
    public HashMap getZdLogControllerMap() {
        if (!(zdLogMap != null && zdLogMap.size() > 0)) {
            getZdLogController();
        }
        return zdLogMap;
    }

    @Override
    public List<Map> getDjlxSqlxRel() {

        return bdcZdGlMapper.getDjlxSqlxRel();
    }

    @Override
    public List<BdcZdSqlx> getBdcSqlxList() {
        return entityMapper.select(new BdcZdSqlx());
    }


    @Override
    public String getYqllxBySqlx(String sqlxdm) {
        return bdcZdGlMapper.getYqllxBySqlx(sqlxdm);
    }

    @Override
    public List<Map> getZdDyfs() {
        return bdcZdGlMapper.getZdDyfs();
    }

    @Override
    public List<Map> getSqlxByBdclxDjlx(String bdclxdm, String djlxdm) {
        return bdcZdGlMapper.getSqlxByBdclxDjlx(bdclxdm, djlxdm);
    }

    public List<Map> getDjlxByBdclx(String bdclxdm) {
        return bdcZdGlMapper.getDjlxByBdclx(bdclxdm);
    }


    @Override
    public List<BdcZdSqlx> getSqlxBydjlx(String djlxdm) {
        return bdcZdGlMapper.getSqlxBydjlx(djlxdm);
    }

    @Override
    public String getWdidBySqlxdm(final String sqlxdm) {
        String wdid = "";
        if (StringUtils.isNotBlank(sqlxdm)) {
            List<BdcZdSqlx> bdcZdSqlxList = getBdcSqlxList();
            if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
                for (BdcZdSqlx bdcZdSqlx : bdcZdSqlxList) {
                    if (bdcZdSqlx != null && StringUtils.equals(bdcZdSqlx.getDm(), sqlxdm)) {
                        wdid = bdcZdSqlx.getWdid();
                        break;
                    }
                }
            }
        }
        return wdid;
    }

    /**
     * lst获取房屋结构字典表
     *
     * @return
     */
    public List<Map> getZdFwjg() {
        return bdcZdGlMapper.getZdFwjg();
    }

    @Override
    public List<Map> getZdtzm() {
        return bdcZdGlMapper.getZdtzm();
    }

    @Override
    public List<Map> getZdZdtzm() {
        List<Map> zdtzmList = new ArrayList<Map>();
        List<Map> list = bdcZdGlMapper.getZdQslx();
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                //循环拼接字典数据
                Map map = list.get(i);
                String dm = map.get("DM").toString();
                String mc = map.get("MC").toString();
                map = new HashMap();
                map.put("DM", "G" + dm);
                map.put("MC", "国有_" + mc);
                zdtzmList.add(map);
                map = new HashMap();
                map.put("DM", "J" + dm);
                map.put("MC", "集体_" + mc);
                zdtzmList.add(map);
                map = new HashMap();
                map.put("DM", "Z" + dm);
                map.put("MC", "争议_" + mc);
                zdtzmList.add(map);
            }
            //单独插入G J Z数据
            HashMap map = new HashMap();
            map.put("DM", "G");
            map.put("MC", "国有");
            zdtzmList.add(map);
            map = new HashMap();
            map.put("DM", "J");
            map.put("MC", "集体");
            zdtzmList.add(map);
            map = new HashMap();
            map.put("DM", "Z");
            map.put("MC", "争议");
            zdtzmList.add(map);
        }
        return zdtzmList;
    }

    @Override
    public List<Map> getZdDzwtzm() {
        return bdcZdGlMapper.getZdDzwtzm();
    }

    @Override
    public List<HashMap> getWorkFlowNodes(final String xml) {
        List<HashMap> listResult = new ArrayList<HashMap>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); //
            List nodeList = rootElt.selectNodes("//Package/WorkflowProcesses/WorkflowProcess/Activities/Activity");
            if (CollectionUtils.isNotEmpty(nodeList)) {
                for (int i = 0; i < nodeList.size(); i++) {
                    HashMap map = new HashMap();
                    Element element = (Element) nodeList.get(i);
                    String id = element.attributeValue("Id");
                    String name = element.attributeValue("Name");
                    map.put("dm", id);
                    map.put("mc", name);
                    listResult.add(map);
                }
            }
        } catch (Exception e) {
            logger.error("BdcZdGlServiceImpl.getWorkFlowNodes", e);
        }
        return listResult;
    }

    @Override
    public List<BdcZdTables> getBdcZdTables() {
        return bdcZdGlMapper.getBdcLimitTableConfigByPage();
    }

    @Override
    public List<HashMap> getFields(final String workFlowId, final String workFlowNodeid, final String tableId, final String cptName) {
        List<HashMap> resultList = new ArrayList<HashMap>();
        //存储数据库表字段名
        List<String> validateDataList = new ArrayList<String>();
        HashMap paraMap = new HashMap();
        paraMap.put("workflowId", workFlowId);
        paraMap.put("workflowNodeId", workFlowNodeid);
        paraMap.put("tableId", tableId);
        paraMap.put("cptName", cptName);
        //获取验证的信息,即已添加的必填项

        //zwq 通过数据库表名获取表的所有字段名,将未被添加的字段名放入map中返回
        BdcZdTables bdcZdTables = entityMapper.selectByPrimaryKey(BdcZdTables.class, tableId);
        if (bdcZdTables != null) {
            if (StringUtils.isNotBlank(bdcZdTables.getTableId())) {
                if (StringUtils.equals("pf_usersign", bdcZdTables.getTableId())) {
                    validateDataList.add("SIGN_OPINION");
                } else {
                    validateDataList = bdcZdGlMapper.getFiledName(bdcZdTables.getTableId().toUpperCase());
                }
            }
            if (CollectionUtils.isNotEmpty(validateDataList)) {
                for (int i = 0; i != validateDataList.size(); i++) {
                    HashMap map = new HashMap();
                    map.put("dm", validateDataList.get(i));
                    map.put("mc", validateDataList.get(i));
                    resultList.add(map);
                }

            }

        }


        return resultList;
    }


    @Override
    public List<Map> getZdQlxz() {
        return bdcZdGlMapper.getZdQlxz();
    }

    @Override
    public String getWdidsBySqlxdm(List<String> sqlxdmList) {
        StringBuilder wfids = new StringBuilder();
        List<BdcZdSqlx> bdcZdSqlxList = getBdcSqlxList();
        if (CollectionUtils.isNotEmpty(bdcZdSqlxList) && sqlxdmList != null) {
            for (BdcZdSqlx bdcZdSqlx : bdcZdSqlxList) {
                if (sqlxdmList.contains(bdcZdSqlx.getDm())) {
                    if (StringUtils.isBlank(wfids)) {
                        wfids.append(bdcZdSqlx.getWdid());
                    } else {
                        wfids.append(",").append(bdcZdSqlx.getWdid());
                    }
                }
            }
        }
        return wfids.toString();
    }

    @Override
    public List<BdcZdZjlx> getBdcZdZjlx() {
        return entityMapper.select(new BdcZdZjlx());
    }

    @Override
    public String getZjlxMcByDm(String zjlxdm, List<BdcZdZjlx> zjlxList) {
        String zjlxmc = "";
        if (StringUtils.isNotEmpty(zjlxdm) && CollectionUtils.isNotEmpty(zjlxList)) {
            for (BdcZdZjlx bdcZdZjlx : zjlxList) {
                if (StringUtils.equals(zjlxdm, bdcZdZjlx.getDm())) {
                    zjlxmc = bdcZdZjlx.getMc();
                    break;
                }
            }
        }
        return zjlxmc;
    }

    @Override
    public String getDjlxDmBySqlxdm(final String sqlxdm) {
        String djlx = "";
        if (StringUtils.isNotBlank(sqlxdm)) {
            Example example = new Example(BdcDjlxSqlxRel.class);
            example.createCriteria().andEqualTo("sqlxdm", sqlxdm);
            List<BdcDjlxSqlxRel> list = entityMapper.selectByExample(BdcDjlxSqlxRel.class, example);
            if (CollectionUtils.isNotEmpty(list))
                djlx = list.get(0).getDjlxdm();
        }
        return djlx;
    }

    @Override
    public String getTdsyqxByDm(String dldm) {
        return bdcZdGlMapper.getTdsyqxByDm(dldm);
    }

    @Override
    public String getSqlxMcByDm(String dm) {
        return bdcZdGlMapper.getSqlxMcByDm(dm);
    }

    @Override
    public List<BdcZdQlzt> getBdcZdQlztList() {
        Example example = new Example(BdcZdQlzt.class);
        example.setOrderByClause("sxh desc");
        return entityMapper.selectByExample(BdcZdQlzt.class, example);
    }

    @Override
    public String getQlColorByQlzt(final String qlzt, List<BdcZdQlzt> bdcZdQlztList) {
        String color = "";
        if (CollectionUtils.isNotEmpty(bdcZdQlztList) && StringUtils.isNotBlank(qlzt)) {
            for (BdcZdQlzt bdcZdQlzt : bdcZdQlztList) {
                if (StringUtils.equals(qlzt, bdcZdQlzt.getDm())) {
                    color = bdcZdQlzt.getColor();
                    break;
                }
            }
        }
        return color;
    }

    @Override
    public String getQlMCByQlzt(final String qlzt, List<BdcZdQlzt> bdcZdQlztList) {
        String mc = "";
        if (CollectionUtils.isNotEmpty(bdcZdQlztList) && StringUtils.isNotBlank(qlzt)) {
            for (BdcZdQlzt bdcZdQlzt : bdcZdQlztList) {
                if (StringUtils.equals(qlzt, bdcZdQlzt.getDm())) {
                    mc = bdcZdQlzt.getMc();
                    break;
                }
            }
        }
        return mc;
    }

    @Override
    public String getFwjgMc(final String dm) {
        List<Map> list = bdcZdGlMapper.getZdFwjg();
        String value = "";
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map hashMap : list) {
                if (StringUtils.equals(hashMap.get("DM").toString(), dm)) {
                    value = hashMap.get("MC").toString();
                    break;
                }
            }
        }
        return value;
    }

    @Override
    public List<BdcZdFwlx> getBdcZdFwlx() {
        return entityMapper.select(new BdcZdFwlx());
    }

    @Override
    public String getGdFcDjlxToSqlxWdid(final String djlxdm) {
        String sqlxWdid = "";
        if (StringUtils.isNotBlank(djlxdm)) {
            List<GdFcDjlxSqlxRel> gdFcDjlxSqlxRelList = bdcZdGlMapper.getGdFcDjlxSqlxRel(djlxdm);
            if (CollectionUtils.isNotEmpty(gdFcDjlxSqlxRelList)) {
                String sqlxDm = gdFcDjlxSqlxRelList.get(0).getSqlxDm();
                sqlxWdid = getWdidBySqlxdm(sqlxDm);
            }
        }

        return sqlxWdid;
    }

    @Override
    public BdcSpxx changeToDm(BdcSpxx bdcSpxx) {
        HashMap hashMap = new HashMap();
        //宗地宗海用途
        if (StringUtils.isNotBlank(bdcSpxx.getZdzhyt()) && !NumberUtils.isNumber(bdcSpxx.getZdzhyt())) {
            hashMap.put("mc", bdcSpxx.getZdzhyt());
            List<HashMap> hashMapList = getZdzhytZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("DM") != null)
                bdcSpxx.setZdzhyt(hashMapList.get(0).get("DM").toString());
        }
        //定着物用途
        if (StringUtils.isNotBlank(bdcSpxx.getYt()) && !NumberUtils.isNumber(bdcSpxx.getYt())) {
            hashMap.put("mc", bdcSpxx.getYt());
            List<HashMap> hashMapList = getDzwytZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("DM") != null)
                bdcSpxx.setYt(hashMapList.get(0).get("DM").toString());
        }
        //宗地宗海权利性质
        if (StringUtils.isNotBlank(bdcSpxx.getZdzhqlxz()) && !NumberUtils.isNumber(bdcSpxx.getZdzhqlxz())) {
            hashMap.put("mc", bdcSpxx.getZdzhqlxz());
            List<HashMap> hashMapList = getQlxzZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("DM") != null)
                bdcSpxx.setZdzhqlxz(hashMapList.get(0).get("DM").toString());
        }
        //构筑物类型
        if (StringUtils.isNotBlank(bdcSpxx.getGzwlx()) && !NumberUtils.isNumber(bdcSpxx.getGzwlx())) {
            hashMap.put("mc", bdcSpxx.getGzwlx());
            List<HashMap> hashMapList = getGjzwLxZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("DM") != null)
                bdcSpxx.setGzwlx(hashMapList.get(0).get("DM").toString());
        }
        //林种
        if (StringUtils.isNotBlank(bdcSpxx.getLz()) && !NumberUtils.isNumber(bdcSpxx.getLz())) {
            hashMap.put("mc", bdcSpxx.getZdzhyt());
            List<HashMap> hashMapList = getZdzhytZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("DM") != null)
                bdcSpxx.setZdzhyt(hashMapList.get(0).get("DM").toString());
        }
        return bdcSpxx;
    }

    /**
     * @author bianwen
     * @description
     */
    public BdcSpxx changeDmToMc(BdcSpxx bdcSpxx) {
        HashMap hashMap = new HashMap();
        //宗地宗海用途
        if (StringUtils.isNotBlank(bdcSpxx.getZdzhyt()) && !NumberUtils.isNumber(bdcSpxx.getZdzhyt())) {
            hashMap.put("dm", bdcSpxx.getZdzhyt());
            List<HashMap> hashMapList = getZdzhytZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("MC") != null)
                bdcSpxx.setZdzhyt(hashMapList.get(0).get("MC").toString());
        }
        //定着物用途
        if (StringUtils.isNotBlank(bdcSpxx.getYt()) && !NumberUtils.isNumber(bdcSpxx.getYt())) {
            hashMap.put("dm", bdcSpxx.getYt());
            List<HashMap> hashMapList = getDzwytZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("MC") != null)
                bdcSpxx.setYt(hashMapList.get(0).get("MC").toString());
        }
        //宗地宗海权利性质
        if (StringUtils.isNotBlank(bdcSpxx.getZdzhqlxz()) && !NumberUtils.isNumber(bdcSpxx.getZdzhqlxz())) {
            hashMap.put("dm", bdcSpxx.getZdzhqlxz());
            List<HashMap> hashMapList = getQlxzZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("MC") != null)
                bdcSpxx.setZdzhqlxz(hashMapList.get(0).get("MC").toString());
        }
        //构筑物类型
        if (StringUtils.isNotBlank(bdcSpxx.getGzwlx()) && !NumberUtils.isNumber(bdcSpxx.getGzwlx())) {
            hashMap.put("dm", bdcSpxx.getGzwlx());
            List<HashMap> hashMapList = getGjzwLxZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("MC") != null)
                bdcSpxx.setGzwlx(hashMapList.get(0).get("MC").toString());
        }
        //林种
        if (StringUtils.isNotBlank(bdcSpxx.getLz()) && !NumberUtils.isNumber(bdcSpxx.getLz())) {
            hashMap.put("dm", bdcSpxx.getZdzhyt());
            List<HashMap> hashMapList = getZdzhytZdb(hashMap);
            if (CollectionUtils.isNotEmpty(hashMapList) && hashMapList.get(0).get("MC") != null)
                bdcSpxx.setZdzhyt(hashMapList.get(0).get("MC").toString());
        }
        return bdcSpxx;
    }

    @Override
    public List<HashMap> getGjzwLxZdb(HashMap hashMap) {
        return bdcZdGlMapper.getGjzwLxZdb(hashMap);
    }

    @Override
    public List<HashMap> getQlxzZdb(HashMap hashMap) {
        return bdcZdGlMapper.getQlxzZdb(hashMap);
    }

    @Override
    public List<HashMap> getDzwytZdb(HashMap hashMap) {
        return bdcZdGlMapper.getDzwytZdb(hashMap);
    }

    @Override
    public List<HashMap> getZdzhytZdb(HashMap hashMap) {
        return bdcZdGlMapper.getZdzhytZdb(hashMap);
    }

    @Override
    public List<BdcZdSqlx> getBdcSqlxByMap(HashMap hashMap) {
        return bdcZdGlMapper.getBdcSqlxByMap(hashMap);
    }

    @Override
    public String getBdcSqlxdmByWdid(final String wdid) {
        String sqlxdm = "";
        if (StringUtils.isNotBlank(wdid)) {
            HashMap map = Maps.newHashMap();
            map.put("wdid", wdid);
            List<BdcZdSqlx> bdcZdSqlxList = getBdcSqlxByMap(map);
            if (CollectionUtils.isNotEmpty(bdcZdSqlxList))
                sqlxdm = bdcZdSqlxList.get(0).getDm();
        }

        return sqlxdm;
    }

    @Override
    public String getWorkflowNodeId(final String nodeName, final String wdid) {
        String nodeId = "";
        if (StringUtils.isNotBlank(nodeName) && StringUtils.isNotBlank(wdid)) {
            String xml = PlatformUtil.getWorkFlowDefineService().getWorkFlowDefineXml(wdid);
            List<HashMap> nodeMapList = getWorkFlowNodes(xml);
            if (CollectionUtils.isNotEmpty(nodeMapList)) {
                for (HashMap map : nodeMapList) {
                    if (StringUtils.equals(CommonUtil.formatEmptyValue(map.get("mc")), nodeName)) {
                        nodeId = CommonUtil.formatEmptyValue(map.get("dm"));
                        break;
                    }
                }
            }

        }
        return nodeId;
    }

    @Override
    public String getWorkflowNodeName(final String nodeId, final String wdid) {
        String nodeName = "";
        if (StringUtils.isNotBlank(nodeId) && StringUtils.isNotBlank(wdid)) {
            String xml = PlatformUtil.getWorkFlowDefineService().getWorkFlowDefineXml(wdid);
            List<HashMap> nodeMapList = getWorkFlowNodes(xml);
            if (CollectionUtils.isNotEmpty(nodeMapList)) {
                for (HashMap map : nodeMapList) {
                    if (StringUtils.equals(CommonUtil.formatEmptyValue(map.get("dm")), nodeId)) {
                        nodeName = CommonUtil.formatEmptyValue(map.get("mc"));
                        break;
                    }
                }
            }

        }
        return nodeName;
    }

    /**
     * @param
     * @author <a herf="mailto:lichaolong@gtmap.cn">lichaolong</a>
     * @version V1.0,
     * @description获取收费配置项
     */
    @Override
    public List<BdcXtSfxm> getBdcSfdMrz(final String sfxmmc, final String sfxmbz) {
        Example example = new Example(BdcXtSfxm.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(sfxmmc))
            criteria.andEqualTo("sfxmmc", sfxmmc);
        if (StringUtils.isNotBlank(sfxmbz))
            criteria.andEqualTo("sfxmbz", sfxmbz);
        List<BdcXtSfxm> sfdList = null;
        if (CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria()))
            sfdList = entityMapper.selectByExample(example);

        return sfdList;
    }


    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 查询登记子项
     */
    @Override
    public List<HashMap> getDjzx(HashMap hashMap) {
        return bdcZdGlMapper.getDjzx(hashMap);
    }

    @Override
    public List<HashMap> getDyfs(HashMap hashMap) {
        return bdcZdGlMapper.getDyfs(hashMap);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @rerutn
     * @description wdid查询登记子项
     */
    @Override
    public List<HashMap> getDjzxBywdid(HashMap hashMap) {
        return bdcZdGlMapper.getDjzxBywdid(hashMap);
    }

    /**
     * @return
     * @author xiejianan
     * @description
     * @see cn.gtmap.estateplat.server.core.service.BdcZdGlService()
     * 2016年5月20日
     */
    @Override
    public List<HashMap<String, String>> getTDFWSqlxGddjlx() {
        return bdcZdGlMapper.getTDFWSqlxGddjlx();
    }

    /**
     * @param
     * @return 查封类型字典表 mc
     * @author<a href="mailto:zhoudefu@gtmap.cn">zhoudefu</a>
     * @discription.根据查封类型名称 获取 对应代码
     */
    @Override
    public String getCflxDmByMc(String mc) {
        String dm = null;
        if (StringUtils.isNotBlank(mc)) {
            dm = bdcZdGlMapper.getCflxDmByMc(mc);
        }
        return dm;
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn BdcZdQllx
     * @description 获取权利类型字典
     */
    @Override
    public List<BdcZdQllx> getBdcZdQllx(HashMap hashMap) {
        return bdcZdGlMapper.getbdcZdQllx(hashMap);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取用海类型字典
     */
    @Override
    public List<HashMap> getBdcZdYhlx(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdYhlx(hashMap);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取林种类型字典
     */
    @Override
    public List<HashMap> getBdcZdLz(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdLz(hashMap);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取房屋性质类型字典
     */
    @Override
    public List<HashMap> getBdcZdFwxz(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdFwxz(hashMap);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取查封类型字典
     */
    @Override
    public List<HashMap> getBdcZdCflx(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdCflx(hashMap);
    }

    /**
     * @param dm 登记事由名称
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 根据代码 查  登记事由名称
     */
    @Override
    public String getDjsyByDm(String dm) {
        return bdcZdGlMapper.getDjsyByDm(dm);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取登记类型字典
     */
    @Override
    public List<HashMap> getBdcZdDjlx(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdDjlx(hashMap);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取申请类型字典
     */
    @Override
    public List<HashMap> getBdcZdSqlx(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdSqlx(hashMap);
    }

    @Override
    public List<HashMap> getDjzxByProid(String proid) {
        return bdcZdGlMapper.getDjzxByProid(proid);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取房屋类型字典
     */
    @Override
    public List<HashMap> getBdcZdFwlxList(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdFwlxList(hashMap);
    }

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取登记事由类型字典
     */
    @Override
    public List<HashMap> getBdcZdDjsy(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdDjsy(hashMap);
    }

    @Override
    public List<Map> getZdGglxlist(HashMap hashMap) {
        return bdcZdGlMapper.getZdGglxlist(hashMap);
    }


    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取抵押不动产类型类型字典
     */
    @Override
    public List<HashMap> getBdcZdDybdclx(HashMap hashMap) {
        return bdcZdGlMapper.getBdcZdDybdclx(hashMap);
    }

    @Override
    public List<Map> getComDjsy() {
        return bdcZdGlMapper.getComDjsy();
    }

    @Override
    public List<BdcZdCflx> getBdcCflx() {
        return entityMapper.select(new BdcZdCflx());
    }

    @Override
    public String getWorkFlowSqlxdm(String proid) {
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        String sqlxdm = "";
        if (pfWorkFlowInstanceVo != null) {
            sqlxdm = getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
        }
        return sqlxdm;
    }


    @Override
    public String getZjzlByMc(String mc) {
        return bdcZdGlMapper.getZjzlByMc(mc);
    }

    @Override
    public String getDjlxDmByMc(String mc) {
        return bdcZdGlMapper.getDjlxDmByMc(mc);
    }

    @Override
    public String getDjlxMcByDm(String dm, List<HashMap> bdcdjlxList) {
        String djlxMc = "";
        if (StringUtils.isNotEmpty(dm) && CollectionUtils.isNotEmpty(bdcdjlxList)) {
            for (HashMap hashMap : bdcdjlxList) {
                if (hashMap.get("DM") != null && StringUtils.isNotEmpty(hashMap.get("DM").toString()) &&
                        StringUtils.equals(dm, hashMap.get("DM").toString())) {
                    if (hashMap.get("MC") != null && StringUtils.isNotEmpty(hashMap.get("MC").toString())) {
                        djlxMc = hashMap.get("MC").toString();
                    }
                    break;
                }
            }
        }
        return djlxMc;
    }

    @Override
    public String getSqlxDmByMc(String mc) {
        return bdcZdGlMapper.getSqlxDmByMc(mc);
    }

    @Override
    public String getQllxDmByMC(String mc) {
        return bdcZdGlMapper.getQllxDmByMC(mc);
    }

    @Override
    public String getQllxMcByDm(String dm) {
        return bdcZdGlMapper.getQllxMcByDm(dm);
    }

    @Override
    public String getGyfsDmByMc(String mc) {
        return bdcZdGlMapper.getGyfsDmByMc(mc);
    }

    @Override
    public String getGyfsMcByDm(String dm) {
        return bdcZdGlMapper.getGyfsMcByDm(dm);
    }

    @Override
    public String getDkfsDmByMc(String mc) {
        String dm = "";
        if (StringUtils.equals(mc, "公积金"))
            dm = "1";
        else if (StringUtils.equals(mc, "商业"))
            dm = "2";
        else if (StringUtils.equals(mc, "组合"))
            dm = "3";
        else if (StringUtils.equals(mc, "其他"))
            dm = "4";
        return dm;
    }

    @Override
    public String getFwytMcByDm(String dm, List<BdcZdFwyt> fwytList) {
        String fwytMc = "";
        if (StringUtils.isNotEmpty(dm) && CollectionUtils.isNotEmpty(fwytList)) {
            for (BdcZdFwyt bdcZdFwyt : fwytList) {
                if (StringUtils.equals(bdcZdFwyt.getDm(), dm)) {
                    fwytMc = bdcZdFwyt.getMc();
                    break;
                }
            }
        }
        return fwytMc;
    }

    @Override
    public String getZdzhytMcByDm(String dm, List<HashMap> bdcZdzhytList) {
        String zdzhyt = "";
        if (StringUtils.isNotEmpty(dm) && CollectionUtils.isNotEmpty(bdcZdzhytList)) {
            for (HashMap hashMap : bdcZdzhytList) {
                if (hashMap.get("DM") != null && StringUtils.isNotEmpty(hashMap.get("DM").toString()) &&
                        StringUtils.equals(dm, hashMap.get("DM").toString()) &&
                        hashMap.get("MC") != null && StringUtils.isNotEmpty(hashMap.get("MC").toString())) {
                    zdzhyt = hashMap.get("MC").toString();
                    break;
                }
            }
        }
        return zdzhyt;
    }

    @Override
    public String getZdzhqlxzMcByDm(String dm, List<HashMap> bdcZdzhQlxzList) {
        String zdzhqlxzMc = "";
        if (StringUtils.isNotEmpty(dm) && CollectionUtils.isNotEmpty(bdcZdzhQlxzList)) {
            for (HashMap hashMap : bdcZdzhQlxzList) {
                if (hashMap.get("DM") != null && StringUtils.isNotEmpty(hashMap.get("DM").toString()) &&
                        StringUtils.equals(dm, hashMap.get("DM").toString()) &&
                        hashMap.get("MC") != null && StringUtils.isNotEmpty(hashMap.get("MC").toString())) {
                    zdzhqlxzMc = hashMap.get("MC").toString();
                    break;
                }
            }
        }
        return zdzhqlxzMc;
    }

    @Override
    public String getDjlxBySqlx(String sqlxdm) {
        return bdcZdGlMapper.getDjlxBySqlx(sqlxdm);
    }

    @Override
    public List<Map> getBdcZdGdlx() {
        return bdcZdGlMapper.getBdcZdGdlx();
    }

    /**
     * @param sqlx 申请类型
     * @return 权利类型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据申请类型获取权利类型
     */
    @Override
    public String getQllxBySqlx(String sqlx) {
        String qllx = "";
        if (StringUtils.isNotBlank(sqlx)) {
            List<String> qllxList = bdcZdGlMapper.getQllxBySqlx(sqlx);
            if (CollectionUtils.isNotEmpty(qllxList)) {
                qllx = qllxList.get(0);
            }
        }
        return qllx;
    }

    @Override
    public String getArchiveNameBySqlx(String sqlx) {
        if (StringUtils.isNotBlank(sqlx)) {
            return bdcZdGlMapper.getArchiveNameBySqlx(sqlx);
        }
        return null;
    }
}