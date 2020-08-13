package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcFdcqDzMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcXtLimitfieldMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.util.*;

/**
 * 表单必填项验证
 */
@Service
public class BdcXtLimitfieldServiceImpl implements BdcXtLimitfieldService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXtLimitfieldMapper bdcXtLimitfieldMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcFdcqDzMapper bdcFdcqDzMapper;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;


    /**
     * @param wdid          工作流定义ID
     * @param activityDefId 工作流活动定义ID
     * @param cptName       帆软报表名称
     * @return
     * @description 获取表单在工作流及节点上的必填字段配置
     */
    @Override
    public List<BdcXtLimitfield> getLimitfield(final String wdid, String activityDefId, String cptName) {
        List<BdcXtLimitfield> bdcXtLimitfieldList = null;
        Example example = new Example(BdcXtLimitfield.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(wdid)) {
            criteria.andEqualTo("workflowId", wdid);
        }
        if (StringUtils.isNotBlank(activityDefId)) {
            criteria.andEqualTo("workflowNodeid", activityDefId);
        }
        if (StringUtils.isNotBlank(cptName)) {
            criteria.andEqualTo("cptName", cptName);
        }
        if (CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria()))
            bdcXtLimitfieldList = entityMapper.selectByExampleNotNull(example);
        return bdcXtLimitfieldList;
    }

    /**
     * 获取转发的所有验证信息
     *
     * @param map
     * @return
     */
    @Override
    public List<Map> getWorkflowTransmitValidates(final Map map) {
        return bdcXtLimitfieldMapper.getZyValidates(map);
    }

    /**
     * @param sql 验证sql语句
     * @return
     * @description 运行验证sql
     */
    @Override
    public List<Map> runSql(final String sql) {
        return bdcXtLimitfieldMapper.runSql(sql);
    }

    /**
     * @param proid  工作流项目ID
     * @param taskid 任务ID
     * @return
     * @description 根据验证信息去查看是否有错误信息
     */
    @Override
    public List<Map> validateMsg(final String taskid, final String proid) {
        //结果集合
        Set<Map> resultList = new HashSet<Map>();
        //结果map
        HashMap resultMap;
        List<String> wfDfids = new ArrayList<String>();
        List<String> actyIds = new ArrayList<String>();
        List<String> proids = new ArrayList<String>();
        String hbDzDyaq = "false";
        String qllx = "";
        BdcXm bdcXm = null;
        //暂时不考虑商品房共有业主流程，因为选择户太多影响效率
        if (StringUtils.isNotBlank(taskid) && StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 读取配置商品房共有业主是否验证 如果是true则验证
             */
            String spfisyz = AppConfig.getProperty("spfisyz");
            if (bdcXm != null && ((!StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) || StringUtils.equals(spfisyz, "true"))) {

                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {

                    String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (CommonUtil.indexOfStrs(Constants.SQLX_hblc_zlc, sqlxdm) && CollectionUtils.isNotEmpty(bdcXmList)) {
                        //合并流程
                        for (BdcXm bdcXm1 : bdcXmList) {
                            String childNodeWdid = bdcZdGlService.getWdidBySqlxdm(bdcXm1.getSqlx());
                            //当前结点名称
                            String actyId = PlatformUtil.getPfActivityIdByTaskId(taskid);
                            String nodeName = bdcZdGlService.getWorkflowNodeName(actyId, pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                            String childNodeId = null;
                            if (StringUtils.isNotBlank(nodeName))
                                childNodeId = bdcZdGlService.getWorkflowNodeId(nodeName, childNodeWdid);
                            if (StringUtils.isNotBlank(childNodeWdid) && StringUtils.isNotBlank(childNodeId)) {
                                wfDfids.add(childNodeWdid);
                                actyIds.add(childNodeId);
                                proids.add(bdcXm1.getProid());
                            }
                        }

                    } else if (CommonUtil.indexOfStrs(Constants.SPYZ_DQPROID_SQLX, sqlxdm)) {
                        wfDfids.add(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                        actyIds.add(PlatformUtil.getPfActivityIdByTaskId(taskid));
                        proids.add(bdcXm.getProid());
                    } else {
                        //其他流程
                        for (BdcXm bdcXm1 : bdcXmList) {
                            List<BdcFwfsss> bdcFwfsssList = null;
                            if(StringUtils.isNotBlank(bdcXm1.getBdcdyid())){
                                Example example = new Example(BdcFwfsss.class);
                                Example.Criteria criteria = example.createCriteria();
                                criteria.andEqualTo(ParamsConstants.BDCDYID_LOWERCASE,bdcXm1.getBdcdyid());
                                bdcFwfsssList = entityMapper.selectByExample(BdcFwfsss.class, example);
                            }
                            if(CollectionUtils.isEmpty(bdcFwfsssList)&&StringUtils.isNotBlank(bdcXm1.getSqlx()) && !CommonUtil.indexOfStrs(Constants.SQLX_DRBG, bdcXm1.getSqlx())){
                                String wdid = pfWorkFlowInstanceVo.getWorkflowDefinitionId();
                                //当前结点名称
                                String actyId = PlatformUtil.getPfActivityIdByTaskId(taskid);
                                if (StringUtils.isNotBlank(wdid) && StringUtils.isNotBlank(actyId)) {
                                    wfDfids.add(wdid);
                                    actyIds.add(actyId);
                                    proids.add(bdcXm1.getProid());
                                }
                            }
                        }
                    }
                }
            }

        } else {
            return Lists.newArrayList(resultList);
        }

        if (CollectionUtils.isNotEmpty(proids) && CollectionUtils.size(wfDfids) == CollectionUtils.size(actyIds) && CollectionUtils.size(actyIds) == CollectionUtils.size(proids)) {
            QllxVo qllxVO = null;
            int k = 0;
            for (String wfDfid : wfDfids) {
                String actyId = actyIds.get(k);
                String tmpProid = proids.get(k);
                //参数map
                HashMap paraMap = new HashMap();
                paraMap.put("workflowId", wfDfid);
                paraMap.put("workflowNodeId", actyId);

                //获取项目中的qllx
                String xmCptName = "";
                if (StringUtils.isNotBlank(tmpProid)) {
                    BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(tmpProid);
                    if (bdcXmTemp != null) {
                        qllx = bdcXmTemp.getQllx();
                        /**
                         * @date: 2016年3月17日 18:30
                         * @aothor ：xiahui
                         * @description ：由于独幢和多幢权利类型都为4，故需要另外加上cptName作为查询调价过滤掉多幢的
                         * 1.通过bdcXm权利类型QllxVo，并判断是否为4。
                         * 2.判断QllxVo是否属于多幢（BdcFdcqDz），多幢则cptName为bdc_fdcq_dz即BdcFdcqDz的table注解的name值
                         *   否则为bdc_fdcq即BdcFdcq的table注解的name值，此处用Annotation获取
                         */
                        qllxVO = qllxService.makeSureQllx(bdcXmTemp);//通过bdcXm权利类型QllxVo
                        //判断QllxVo是否属于多幢（BdcFdcqDz）
                        if (qllxVO instanceof BdcFdcqDz) {
                            xmCptName = BdcFdcq.class.getAnnotation(Table.class).name();
                        } else if (qllxVO instanceof BdcFdcq) {
                            xmCptName = BdcFdcqDz.class.getAnnotation(Table.class).name();
                        }
                        //zhouwanqing 这边区分只能用排除，不然收件单和审批表的验证就不验证了
                        paraMap.put("cptName", xmCptName);
                    }
                }


                //房地产权利分多幢查询验证条件
                //获取验证的信息
                List<Map> validateList = getWorkflowTransmitValidates(paraMap);
                //存储运行sql后的数据
                List<Map> validateDataList = new ArrayList<Map>();
                //是否已经存在错误
                boolean hasError = false;
                //第一个有问题的表单名称
                String cptName = "";
                String tableId = "";
                if (CollectionUtils.isNotEmpty(validateList)) {
                    for (int i = 0; i < validateList.size(); i++) {
                        Map validateMap = validateList.get(i);
                        //判斷是否為独幢或多幢 独幢去除多幢的验证条件
                        if (StringUtils.isBlank(xmCptName) && validateMap.get("CPT_NAME") != null&&
                                StringUtils.equals("BDC_FDCQ_DZ", ((String) validateMap.get("CPT_NAME")).toUpperCase())) {
                            continue;
                        }
                        //对申请审批表中的多幢和独幢信息进行相互区分，多幢不验证多幢的信息，独幢不验证多幢的信息
                        //多幢的抵押也走多幢的逻辑
                        if(StringUtils.equals("false",hbDzDyaq)){
                            Boolean needCheck = true;
                            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_GJPTSCDJ_DM))){
                                needCheck = false;
                            }
                            String proidsStr = bdcXmService.getProidsByProid(proid);
                            if(needCheck){
                                if(StringUtils.contains(proidsStr,"$")){
                                    String[] proidss = proidsStr.split("\\$");
                                    for(String proidex:proidss){
                                        BdcXm bdcXm2 = bdcXmService.getBdcXmByProid(proidex);
                                        QllxVo qllxVo2 = qllxService.makeSureQllx(bdcXm2);
                                        if(qllxVo2 instanceof BdcFdcqDz){
                                            hbDzDyaq = "true";
                                            break;
                                        }
                                    }
                                }else{
                                    List<BdcFwfzxx> fwfzxxList = bdcFdcqDzMapper.queryBdcFwfzxxlstByProid(proid);
                                    if(qllxVO instanceof BdcDyaq){
                                        Map<String, Object> dzMap = new HashMap<String,Object>();
                                        BdcXm bdcXm3 = bdcXmService.getBdcXmByProid(proid);
                                        dzMap.put(ParamsConstants.BDCDYID_LOWERCASE, bdcXm3.getBdcdyid());
                                        dzMap.put("qszt", Constants.QLLX_QSZT_XS);
                                        List<BdcFdcqDz> bdcFdcqDzList = bdcFdcqDzService.getBdcFdcqDzList(dzMap);
                                        if(CollectionUtils.isNotEmpty(bdcFdcqDzList)){
                                            fwfzxxList = bdcFdcqDzMapper.queryBdcFwfzxxlstByQlid(bdcFdcqDzList.get(0).getQlid());
                                        }
                                    }
                                    if(CollectionUtils.isNotEmpty(fwfzxxList)){
                                        hbDzDyaq = "true";
                                    }
                                }
                            }
                        }
                        if(qllxVO instanceof BdcFdcqDz||StringUtils.equals(hbDzDyaq,"true")){
                            if(StringUtils.equals("YT",validateMap.get("TABLE_FIELD_ID").toString().toUpperCase())){
                                continue;
                            }
                            if(StringUtils.equals("MJ",validateMap.get("TABLE_FIELD_ID").toString().toUpperCase())){
                                continue;
                            }
                            if(StringUtils.equals("MYZCS",validateMap.get("TABLE_FIELD_ID").toString().toUpperCase())){
                                continue;
                            }
                            if(StringUtils.equals("FWJG",validateMap.get("CPT_FIELD_NAME").toString().toUpperCase())){
                                continue;
                            }
                            if(StringUtils.equals("FWXZ",validateMap.get("TABLE_FIELD_ID").toString().toUpperCase())){
                                continue;
                            }
                        }else if(qllxVO instanceof BdcFdcq){
                            if(StringUtils.equals(validateMap.get("CPT_FIELD_NAME").toString().toUpperCase(),"FZFWJG")){
                                continue;
                            }
                            if(StringUtils.equals(validateMap.get("CPT_FIELD_NAME").toString().toUpperCase(),"FZGHYT")){
                                continue;
                            }
                            if(StringUtils.equals(validateMap.get("CPT_FIELD_NAME").toString().toUpperCase(),"FZJZMJ")){
                                continue;
                            }
                        }
                        if (bdcXm != null && StringUtils.equals(bdcXm.getBdclx(),Constants.BDCLX_TD)) {
                            if(StringUtils.equals(validateMap.get("CPT_FIELD_NAME").toString().toUpperCase(),"YT")){
                                continue;
                            }
                        }
                        //判断是否是同一个表单
                        if (cptName != "" && !StringUtils.equals(cptName, (String) validateMap.get("CPT_NAME"))) {
                            //判断是否已经有错误了，若有没有
                            if (hasError) {
                                continue;
                            } else {
                                cptName = (String) validateMap.get("CPT_NAME");
                            }
                        }
                        //不相等 重新取数据
                        if (!StringUtils.equals(tableId, (String) validateMap.get("TABLE_ID"))) {
                            if (cptName == "") {
                                cptName = (String) validateMap.get("CPT_NAME");
                            }
                            //替换tableId
                            tableId = (String) validateMap.get("TABLE_ID");
                            //执行sql
                            String sql = StringUtils.replace((String) validateMap.get("TABLE_XMREL_SQL"), "@proid", "'" + tmpProid + "'");
                            if(StringUtils.isNotBlank(sql) && sql.indexOf(';') > 0){
                                String[] sqlArr = sql.split(";");
                                for(String sql1 : sqlArr){
                                    validateDataList = runSql(PlatformUtil.initOptProperties(sql1));
                                    if(CollectionUtils.isNotEmpty(validateDataList)){
                                        break;
                                    }
                                }
                            }else{
                                validateDataList = runSql(PlatformUtil.initOptProperties(sql));
                            }
                        }

                        //用qllx过滤 qllx为0或空是通用验证
                        if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(validateMap.get("QLLX"))) && !"0".equals(validateMap.get("QLLX")) && !CommonUtil.formatEmptyValue(validateMap.get("QLLX")).contains(qllx)) {
                                continue;
                        } else {
                            //判断是否有数据  没有的话直接存入错误信息
                            if (CollectionUtils.isEmpty(validateDataList)) {
                                //已产生错误，只提示一个表单的错误
                                hasError = true;
                                resultMap = new HashMap();
                                //存储错误
                                resultMap.put("error", validateMap.get("CPT_DESC") + "中" + validateMap.get("TABLE_FIELD_NAME") + "不能为空");
                                resultList.add(resultMap);
                            } else {
                                //没有存入错误
                                boolean notHasError = true;
                                //循环验证必填字段是否有值
                                for (int j = 0; j < validateDataList.size(); j++) {
                                    Map validateDataMap = validateDataList.get(j);
                                    //如果存入错误 跳出
                                    if (!notHasError) {
                                        break;
                                    }
                                    //如果没有值

                                    //对义务人信息中的ywrmc,ywrzjh,ywrsfzjzl进行处理使这些能够正确匹配从bdc_qlr中选出的字段数据
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"YWRZJH")){
                                        validateMap.put("TABLE_FIELD_ID","QLRZJH");
                                    }
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"YWRMC")){
                                        validateMap.put("TABLE_FIELD_ID","QLRMC");
                                    }
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"YWRSFZJZL")){
                                        validateMap.put("TABLE_FIELD_ID","QLRSFZJZL");
                                    }
                                    //处理房地产权字段名称使其能够正确匹配从bdc_fdcq中选出的字段数据
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"MYZCS")){
                                        validateMap.put("TABLE_FIELD_ID","MYZCS");
                                    }
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"COMBO_ZDZHYT")){
                                        validateMap.put("TABLE_FIELD_ID","ZDZHYT");
                                    }
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"COMBO_YT")){
                                        validateMap.put("TABLE_FIELD_ID","YT");
                                    }
                                    //处理权利人,义务人共有方式转发验证字段
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"QLRGYFS")){
                                        validateMap.put("TABLE_FIELD_ID","GYFS");
                                    }
                                    if(StringUtils.equals(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase(),"YWRGYFS")){
                                        validateMap.put("TABLE_FIELD_ID","GYFS");
                                    }
                                    if (validateDataMap.get(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase()) == null || StringUtils.isBlank(validateDataMap.get(validateMap.get("TABLE_FIELD_ID").toString().toUpperCase()).toString())) {
                                        //已产生错误，只提示一个表单的错误
                                        hasError = true;
                                        resultMap = new HashMap();
                                        //存储错误
                                        notHasError = false;
                                        resultMap.put("error", validateMap.get("CPT_DESC") + "中" + validateMap.get("TABLE_FIELD_NAME") + "不能为空");
                                        resultList.add(resultMap);
                                    }
                                }

                            }
                        }
                    }
                }
                k++;
            }
        }
        return Lists.newArrayList(resultList);
    }

    /**
     * @param cptName 帆软报表名称
     * @description 获取过渡页面的必填项
     */
    @Override
    public List<BdcXtLimitfieldofGd> getLimitfieldOfGd(final String cptName) {
        List<BdcXtLimitfieldofGd> bdcXtLimitfieldofGdList = null;
        Example example = new Example(BdcXtLimitfieldofGd.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(cptName)) {
            criteria.andEqualTo("cptName", cptName);
        }

        if (CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria()))
            bdcXtLimitfieldofGdList = entityMapper.selectByExampleNotNull(example);
        return bdcXtLimitfieldofGdList;
    }
}
