package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcXmRelMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.XmRelModel;
import cn.gtmap.estateplat.utils.ArrayUtils;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-20
 */
@Repository
public class BdcXmRelServiceImpl implements BdcXmRelService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmRelMapper bdcXmRelMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private GdXmService gdXmService;

    @Autowired
    private QllxService qllxService;

    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdCfService gdCfService;

    @Override
    public BdcXmRel creatBdcXmRelFromProject(Project project) {
        BdcXmRel bdcXmRel = new BdcXmRel();

        bdcXmRel.setProid(project.getProid());

        bdcXmRel.setQjid(project.getDjId());

        //过渡数据和原项目的Yproid赋值区分
        if (StringUtils.isNotBlank(project.getYxmid())) {
            bdcXmRel.setYproid(project.getYxmid());
        } else {
            bdcXmRel.setYproid(project.getGdproid());
        }
        bdcXmRel.setYdjxmly(project.getXmly());
        bdcXmRel.setYqlid(project.getYqlid());

        if (StringUtils.isNotBlank(project.getProid())) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, project.getProid());
            entityMapper.deleteByExample(example);
        }
        //zdd 此处代码无意义  前面已经删除了  所以百分百找不到
        bdcXmRel.setRelid(UUIDGenerator.generate18());
        return bdcXmRel;
    }

    @Override
    public List<BdcXmRel> creatMulBdcXmRelFromProject(Project project) {
        List<BdcXmRel> bdcXmRelList = null;
        if (project != null) {
            if (StringUtils.isNotBlank(project.getProid())) {
                Example example = new Example(BdcXmRel.class);
                example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, project.getProid());
                entityMapper.deleteByExample(example);
            }

            if (StringUtils.equals(project.getSqlx(), Constants.SQLX_GYJSYDHB_BGDJ)) {
                if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                    bdcXmRelList = new ArrayList<BdcXmRel>();
                    for (BdcXmRel xmRel : project.getBdcXmRelList()) {
                        BdcXmRel tempBdcXmRel = new BdcXmRel();
                        tempBdcXmRel.setProid(project.getProid());
                        tempBdcXmRel.setYdjxmly(project.getXmly());
                        tempBdcXmRel.setYproid(xmRel.getYproid());
                        tempBdcXmRel.setRelid(UUIDGenerator.generate18());
                        bdcXmRelList.add(tempBdcXmRel);
                    }
                }
            } else {
                if (CollectionUtils.isNotEmpty(project.getDjIds())) {
                    bdcXmRelList = new ArrayList<BdcXmRel>();
                    if (StringUtils.indexOf(project.getDjIds().get(0), Constants.SPLIT_STR) > -1) {
                        List<String> djIds = new ArrayList<String>();
                        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                            List<String> bdcidList = gdXmService.getGdBdcidByProid(project.getGdproid());
                            if(CollectionUtils.isNotEmpty(bdcidList)) {
                                for(String bdcid:bdcidList) {
                                    List<String> djIdList = gdXmService.getDjidByGdid(bdcid);
                                    if(CollectionUtils.isNotEmpty(djIdList)) {
                                        djIds.addAll(djIdList);
                                    }
                                }
                            }
                        } else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                            String[] djIdsTemp = StringUtils.split(project.getDjIds().get(0), Constants.SPLIT_STR);
                            if (djIdsTemp != null && djIdsTemp.length > 0) {
                                for (String djId : djIdsTemp) {
                                    djIds.add(djId);
                                }
                            }
                        }
                        for (String djId : djIds) {
                            BdcXmRel bdcXmRel = new BdcXmRel();
                            bdcXmRel.setProid(project.getProid());
                            bdcXmRel.setQjid(djId);
                            //过渡数据和原项目的Yproid赋值区分
                            if (StringUtils.isNotBlank(project.getYxmid())) {
                                bdcXmRel.setYproid(project.getYxmid());
                            } else {
                                bdcXmRel.setYproid(project.getGdproid());
                            }
                            bdcXmRel.setYdjxmly(project.getXmly());
                            bdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRelList.add(bdcXmRel);
                        }
                    } else {
                        for (String djId : project.getDjIds()) {
                            BdcXmRel bdcXmRel = new BdcXmRel();
                            bdcXmRel.setProid(project.getProid());
                            bdcXmRel.setQjid(djId);
                            bdcXmRel.setYdjxmly(project.getXmly());
                            bdcXmRel.setYproid(project.getYxmid());
                            bdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRelList.add(bdcXmRel);
                        }
                    }
                } else if (StringUtils.isNotBlank(project.getDjId())) {
                    bdcXmRelList = new ArrayList<BdcXmRel>();
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    //过渡数据和原项目的Yproid赋值区分
                    if (StringUtils.isNotBlank(project.getYxmid())) {
                        bdcXmRel.setYproid(project.getYxmid());
                    } else {
                        bdcXmRel.setYproid(project.getGdproid());
                    }
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRelList.add(bdcXmRel);
                }
            }
        }

        return bdcXmRelList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> queryBdcXmRelByProid(final String proid) {
        List<BdcXmRel> bdcXmRelList = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcXmRel bdcXmRel = new BdcXmRel();
            bdcXmRel.setProid(proid);
            bdcXmRelList = bdcXmRelMapper.queryBdcXmRelMapper(bdcXmRel);
        }

        return bdcXmRelList;
    }

    @Override
    @Transactional
    public void delBdcXmRelByProid(final String proid) {
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            entityMapper.deleteByExample(BdcXmRel.class, example);
        }
    }

    @Override
    public void batchDelBdcXmRel(List<BdcXm> bdcXmList) {
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            bdcXmRelMapper.batchDelBdcXmRel(bdcXmList);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> queryBdcXmRelByYproid(final String yproid) {
        List<BdcXmRel> bdcXmRelList = null;
        if (StringUtils.isNotBlank(yproid)) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo("yproid", yproid);
            bdcXmRelList = entityMapper.selectByExample(BdcXmRel.class, example);
        }
        return bdcXmRelList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> queryBdcXmRelByYqlid(final String yqlid) {
        List<BdcXmRel> bdcXmRelList = null;
        if (StringUtils.isNotBlank(yqlid)) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo(ParamsConstants.YQLID_LOWERCASE, yqlid);
            bdcXmRelList = entityMapper.selectByExample(BdcXmRel.class, example);
        }
        return bdcXmRelList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> andEqualQueryBdcXmRel(final Map<String, Object> map) {
        List<BdcXmRel> list = null;
        Example qllxTemp = new Example(BdcXmRel.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Example.Criteria criteria = qllxTemp.createCriteria();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andEqualTo(key.toString(), val);
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(BdcXmRel.class, qllxTemp);
        return list;
    }

    /**
     * @param proid 不动产登记项目ID
     * @return 上一手不动产登记项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据不动产登记项目ID获取上一手不动产登记项目ID
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getYproid(final String proid) {
        return bdcXmRelMapper.getYproid(proid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> getHisBdcXmRelByProid(final String proid) {
        return bdcXmRelMapper.getHisXmRelList(proid);
    }

    @Override
    @Transactional(readOnly = true)
    public String getHisXmRelXml(final String proid, final String bdcdyh, final String portalUrl) {
        String xml = "";
        List<String> proidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(proid)) {
            List<BdcXmRel> bdcXmRelList = getHisBdcXmRelByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                List<BdcXmRel> queryBdcXmRelList = new ArrayList<BdcXmRel>();
                List<QllxVo> qllxVoList = new ArrayList<QllxVo>();
                String djid = "";
                if (StringUtils.isNotBlank(bdcdyh)) {
                    HashMap map = new HashMap();
                    map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                    List<Map> bdcdyhMapList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyhMapList) && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(bdcdyhMapList.get(0).get("ID"))))
                        djid = CommonUtil.formatEmptyValue(bdcdyhMapList.get(0).get("ID"));
                }
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (!proidList.contains(bdcXmRel.getProid()))
                        proidList.add(bdcXmRel.getProid());
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                    QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                    if (qllxVo != null)
                        qllxVoList.add(qllxVo);
                    if (StringUtils.isNotBlank(djid)) {
                        if (StringUtils.equals(bdcXmRel.getQjid(), djid) || StringUtils.isBlank(bdcXmRel.getQjid()))
                            queryBdcXmRelList.add(bdcXmRel);
                    } else
                        queryBdcXmRelList.add(bdcXmRel);
                }

                HashMap map = new HashMap();
                map.put("proids", proidList);
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                List<HashMap> bdcZsXmRelList = null;
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListForXmRel(map);
                bdcXmList = changeBdcXmSqlx(bdcXmList);
                List<BdcZdQlzt> bdcZdQlztList = bdcZdGlService.getBdcZdQlztList();
                List<HashMap> hashMapList = bdcQlrService.getQlrByXmList(bdcXmList);
                Document document = XmRelModel.getXmRel(queryBdcXmRelList, bdcZsXmRelList, bdcXmList, qllxVoList, bdcZdQlztList, bdcdyh, hashMapList, portalUrl);
                if (document != null)
                    xml = document.asXML();
            }
        }

        return xml;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> getBdcXmRelByYproidAndBdcdyh(final Map map) {
        return bdcXmRelMapper.getBdcXmRelByYproidAndBdcdyh(map);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> getSameWFXmRelByproid(String proid) {
        return bdcXmRelMapper.getSameWFXmRelByproid(proid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> getAllHisRelList(final Map map) {
        return bdcXmRelMapper.getAllHisRelList(map);
    }

    @Transactional(readOnly = true)
    public List<BdcXmRel> getAllHisRelListByYproid(final Map map) {
        return bdcXmRelMapper.getAllHisRelListByYproid(map);
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllXmRelXml(final String proid, final String bdcdyh, final String portalUrl) {
        String xml = "";
        if (StringUtils.isNotBlank(proid)) {
            String proidtemp = bdcXmService.getProidsByProid(proid);
            List<BdcXmRel> bdcXmRelList = getHisBdcXmRelByProid(proid);
            List<BdcXmRel> bdcXmRelLists = new LinkedList<BdcXmRel>();
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                String ydjxmly = bdcXmRelList.get(0).getYdjxmly();
                HashMap map1 = new HashMap();
                map1.put("xmly",ydjxmly);
                if(StringUtils.split(proidtemp,Constants.SPLIT_STR).length > 0) {
                    String []aa = StringUtils.split(proidtemp,Constants.SPLIT_STR);
                    //根据项目来源分别用proid和yproid查询项目关系
                    if(StringUtils.equals(ydjxmly,"1") || ydjxmly == null) {
                        for (String proideve : aa) {
                            map1.put(ParamsConstants.PROID_LOWERCASE,proideve);
                            List<BdcXmRel> bdcXmRelList1 = getAllHisRelList(map1);
                            bdcXmRelLists.addAll(bdcXmRelList1);
                        }
                    }else{
                        for (String proideve : aa) {
                            List<String> yproideveList = getYproid(proideve);
                            if(CollectionUtils.isNotEmpty(yproideveList)) {
                                for(String yproideve:yproideveList) {
                                    //有的yproid为空，还是要用proid查项目关系
                                    if(StringUtils.isBlank(yproideve)){
                                        map1.put(ParamsConstants.PROID_LOWERCASE,proideve);
                                        List<BdcXmRel> bdcXmRelList1 = getAllHisRelList(map1);
                                        bdcXmRelLists.addAll(bdcXmRelList1);
                                    }else {
                                        map1.put("yproid",yproideve);
                                        List<BdcXmRel> bdcXmRelList1 = getAllHisRelListByYproid(map1);
                                        bdcXmRelLists.addAll(bdcXmRelList1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //去掉重复的项目关系
            if(CollectionUtils.isNotEmpty(bdcXmRelLists)) {
                for (int a = 0; a < bdcXmRelLists.size(); a++) {
                    for (int b = a + 1; b < bdcXmRelLists.size(); b++) {
                        if (bdcXmRelLists.get(a).getRelid().equals(bdcXmRelLists.get(b).getRelid())) {
                            bdcXmRelLists.remove(bdcXmRelLists.get(b));
                        }
                    }
                }
            }
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(bdcXmRelLists)) {
                String[] proids = new String[bdcXmRelLists.size()];
                List<BdcXmRel> queryBdcXmRelList = new ArrayList<BdcXmRel>();
                List<QllxVo> qllxVoList = new ArrayList<QllxVo>();
                String djid = "";
                if (StringUtils.isNotBlank(bdcdyh)) {
                    HashMap map = new HashMap();
                    map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                    List<Map> bdcdyhMapList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyhMapList) && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(bdcdyhMapList.get(0).get("ID"))))
                        djid = CommonUtil.formatEmptyValue(bdcdyhMapList.get(0).get("ID"));
                }
                int i = 0;
                for (BdcXmRel bdcXmRel : bdcXmRelLists) {
                    if (!ArrayUtils.contains(proids, bdcXmRel.getProid()))
                        proids[i] = bdcXmRel.getProid();
                    if (StringUtils.isNotBlank(djid)) {
                        if (StringUtils.equals(bdcXmRel.getQjid(), djid) || (StringUtils.isBlank(bdcXmRel.getQjid()) && bdcdyid != null))
                            queryBdcXmRelList.add(bdcXmRel);
                    } else
                        queryBdcXmRelList.add(bdcXmRel);
                    i++;
                }
                if (CollectionUtils.isNotEmpty(queryBdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : queryBdcXmRelList) {
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                        QllxVo qllxVoTemp = qllxService.queryQllxVo(bdcXm);
                        if (qllxVoTemp != null) {
                            if (qllxVoTemp instanceof BdcFdcq) {
                                if (StringUtils.isNotBlank(bdcdyid) && bdcdyid.equals(((BdcFdcq) qllxVoTemp).getBdcdyid())) {
                                    qllxVoList.add(qllxVoTemp);
                                }
                            } else {
                                qllxVoList.add(qllxVoTemp);
                            }

                        }
                    }
                }
                HashMap map = new HashMap();
                map.put("proids", proids);
                List<HashMap> bdcZsXmRelList = null;
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListForXmRel(map);
                bdcXmList = changeBdcXmSqlx(bdcXmList);
                List<BdcZdQlzt> bdcZdQlztList = bdcZdGlService.getBdcZdQlztList();
                List<HashMap> hashMapList = bdcQlrService.getQlrByXmList(bdcXmList);
                Document document = XmRelModel.getXmRel(queryBdcXmRelList, bdcZsXmRelList, bdcXmList, qllxVoList, bdcZdQlztList, bdcdyh, hashMapList, portalUrl);
                if (document != null)
                    xml = document.asXML();
            }
        }
        return xml;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXmRel> getAfterHisXmRelList(final String proid) {
        return bdcXmRelMapper.getAfterHisXmRelList(proid);
    }

    @Override
    public String getDyAllXmRelXml(final String bdcdyh, final String portalUrl) {
        String xml = "";
        if (StringUtils.isNotBlank(bdcdyh)) {

            BdcBdcdy bdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
            String proid = "";

            HashMap<String, String> querymap = new HashMap<String, String>();
            querymap.put("bdcdyid", bdcdy.getBdcdyid());
            List<BdcXm> bdcXmList = bdcXmService.andEqualQueryBdcXm(querymap);
            if (CollectionUtils.isNotEmpty(bdcXmList))
                proid = bdcXmList.get(0).getProid();
            //zdd 现有逻辑为一个不动产单元对应多个项目
            if (StringUtils.isNotBlank(proid))
                xml = getAllXmRelXml(proid, bdcdyh, portalUrl);
        }
        return xml;
    }

    @Override
    public BdcXmRel creatBdcXmRelFromProjectMul(Project project) {
        BdcXmRel bdcXmRel = new BdcXmRel();
        bdcXmRel.setProid(project.getProid());
        bdcXmRel.setQjid(project.getDjId());
        //过渡数据和原项目的Yproid赋值区分
        if (StringUtils.isNotBlank(project.getYxmid())) {
            bdcXmRel.setYproid(project.getYxmid());
        } else {
            bdcXmRel.setYproid(project.getGdproid());
        }
        bdcXmRel.setYdjxmly(project.getXmly());
        if (StringUtils.isNotBlank(project.getProid())) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, project.getProid()).andEqualTo("ybdcdyid", project.getYbdcdyid());
            entityMapper.deleteByExample(example);

        }
        List<BdcXmRel> bdcXmRelList = bdcXmRelMapper.queryBdcXmRelMapper(bdcXmRel);
        if (CollectionUtils.isEmpty(bdcXmRelList)) {
            bdcXmRel.setRelid(UUIDGenerator.generate18());
        } else {
            bdcXmRel = bdcXmRelList.get(0);
        }
        return bdcXmRel;
    }

    //zhouwanqing 这个显示证书关系中的申请类型
    public List<BdcXm> changeBdcXmSqlx(List<BdcXm> bdcXmList) {
        List<BdcXm> bdcXmLists = new ArrayList<BdcXm>();
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                if (bdcXm != null) {
                    if (StringUtils.isNotBlank(bdcXm.getSqlx())) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("dm", bdcXm.getSqlx());
                        List<BdcZdSqlx> bdcZdSqlxList = bdcZdGlService.getBdcSqlxByMap(hashMap);
                        if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
                            bdcXm.setSqlx(bdcZdSqlxList.get(0).getMc());
                        }
                        bdcXmLists.add(bdcXm);
                    } else {
                        bdcXmLists.add(bdcXm);
                    }
                }
            }
        }
        return bdcXmLists;
    }

    @Override
    public List<BdcXmRel> getBdcXmRelByName(final String name, final String value) {
        List<BdcXmRel> bdcXmRelList = null;
        Example example = new Example(BdcXmRel.class);
        example.createCriteria().andEqualTo(name, value);
        if (StringUtils.isNotBlank(name))
            bdcXmRelList = entityMapper.selectByExample(example);
        return bdcXmRelList;
    }

    @Override
    public List<BdcXmRel> creatBdcXmRelListFromProject(Project project) {
        //分割合并变更流程是否根据权籍不动产单元历史关系表自动关联分割或合并前证书
        String autoRelateZs = AppConfig.getProperty("fghb.autoRelateZs");
        List<BdcXmRel> bdcXmRelList = null;
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, project.getProid());
            entityMapper.deleteByExample(example);
        }
        if (project != null && StringUtils.isNotBlank(project.getYxmid()) && StringUtils.isNotBlank(project.getYqlid()) && StringUtils.indexOf(project.getYqlid(), ",") > -1) {
            //处理原项目id权利id都有有逗号问题
            if (StringUtils.split(project.getYxmid(), ",").length == StringUtils.split(project.getYqlid(), ",").length) {
                bdcXmRelList = new ArrayList<BdcXmRel>();
                String[] yproids = project.getYxmid().split(",");
                String[] yqlids = project.getYqlid().split(",");
                int i = 0;
                for (String proid : yproids) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYproid(proid);
                    bdcXmRel.setYqlid(yqlids[i]);
                    bdcXmRel.setYdjxmly(project.getXmly());
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRelList.add(bdcXmRel);
                    i++;
                }
            } else {
                bdcXmRelList = new ArrayList<BdcXmRel>();
                String[] yqlids = project.getYqlid().split(",");
                for (String yqlid : yqlids) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYproid(project.getYxmid());
                    bdcXmRel.setYqlid(yqlid);
                    bdcXmRel.setYdjxmly(project.getXmly());
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRelList.add(bdcXmRel);
                }
            }
        } else if (project != null && StringUtils.isNotBlank(project.getGdproid()) && StringUtils.isNotBlank(project.getYqlid()) && StringUtils.indexOf(project.getYqlid(), ",") > -1) {
            //处理原过渡项目id权利id都有有逗号问题
            if (StringUtils.split(project.getGdproid(), ",").length == StringUtils.split(project.getYqlid(), ",").length) {
                bdcXmRelList = new ArrayList<BdcXmRel>();
                String[] yproids = project.getGdproid().split(",");
                String[] yqlids = project.getYqlid().split(",");
                int i = 0;
                for (String proid : yproids) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYproid(proid);
                    bdcXmRel.setYqlid(yqlids[i]);
                    bdcXmRel.setYdjxmly(project.getXmly());
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRelList.add(bdcXmRel);
                    i++;
                }
            } else {
                bdcXmRelList = new ArrayList<BdcXmRel>();
                String[] yqlids = project.getYqlid().split(",");
                for (String yqlid : yqlids) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYproid(project.getGdproid());
                    bdcXmRel.setYqlid(yqlid);
                    bdcXmRel.setYdjxmly(project.getXmly());
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRelList.add(bdcXmRel);
                }
            }
        }else if (project!=null&&(StringUtils.equals(project.getSqlx(),Constants.SQLX_FWJF_DM) || StringUtils.equals(project.getSqlx(),Constants.SQLX_TDJF_DM))){
            bdcXmRelList = new ArrayList<BdcXmRel>();
            BdcXmRel bdcXmRel = new BdcXmRel();
            bdcXmRel.setProid(project.getProid());
            bdcXmRel.setQjid(project.getDjId());
            bdcXmRel.setYdjxmly(project.getXmly());
            bdcXmRel.setRelid(UUIDGenerator.generate18());
            if(StringUtils.isNotBlank(project.getGdproid())){
                HashMap map = new HashMap();
                map.put(ParamsConstants.PROID_LOWERCASE,project.getGdproid());
                List<GdCf> gdCfList = gdXmService.getGdCfList(map);
                if(CollectionUtils.isNotEmpty(gdCfList)){
                    bdcXmRel.setYproid(gdCfList.get(0).getProid());
                    bdcXmRel.setYqlid(gdCfList.get(0).getCfid());
                }
            }
            bdcXmRelList.add(bdcXmRel);
        } else if(project!=null&&StringUtils.equals(autoRelateZs,"true") && (StringUtils.equals(project.getSqlx(),Constants.SQLX_FWFGHBBG_DM)||StringUtils.equals(project.getSqlx(),Constants.SQLX_FWFGHBZY_DM)||StringUtils.equals(project.getSqlx(),Constants.SQLX_TDFGHBBG_DM)||StringUtils.equals(project.getSqlx(),Constants.SQLX_TDFGHBZY_DM))){
            if(CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                bdcXmRelList = new ArrayList<BdcXmRel>();
                for(BdcXmRel xmRel : project.getBdcXmRelList()) {
                    BdcXmRel tempBdcXmRel = new BdcXmRel();
                    tempBdcXmRel.setProid(project.getProid());
                    tempBdcXmRel.setQjid(xmRel.getQjid());
                    tempBdcXmRel.setYdjxmly(xmRel.getYdjxmly());
                    tempBdcXmRel.setYproid(xmRel.getYproid());
                    tempBdcXmRel.setYqlid(xmRel.getYqlid());
                    tempBdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRelList.add(tempBdcXmRel);
                }
            }
        }else if(project!=null) {
            bdcXmRelList = new ArrayList<BdcXmRel>();
            BdcXmRel bdcXmRel = new BdcXmRel();
            bdcXmRel.setProid(project.getProid());
            bdcXmRel.setQjid(project.getDjId());
            //过渡数据和原项目的Yproid赋值区分
            if (StringUtils.isNotBlank(project.getYxmid())) {
                bdcXmRel.setYproid(project.getYxmid());
            } else {
                bdcXmRel.setYproid(project.getGdproid());
            }
            bdcXmRel.setYqlid(project.getYqlid());
            bdcXmRel.setYdjxmly(project.getXmly());
            bdcXmRel.setRelid(UUIDGenerator.generate18());
            bdcXmRelList.add(bdcXmRel);
        }
        return bdcXmRelList;
    }

    @Override
    public List<BdcXmRel> getBdcXmRelListByYqlid(String yqlid) {
        List<BdcXmRel> bdcXmRelList = null;
        if (StringUtils.isNotBlank(yqlid)) {
            Example example = new Example(BdcXmRel.class);
            example.createCriteria().andEqualTo(ParamsConstants.YQLID_LOWERCASE,yqlid);
            bdcXmRelList = entityMapper.selectByExample(example);
        }
        return bdcXmRelList;
    }

    @Override
    public List<BdcXmzsRel> getProidByBdcqzh(String bdcqzh) {
        return bdcXmRelMapper.getProidByBdcqzh(bdcqzh);
    }

    @Override
    public void saveBdcXmRel(BdcXmRel bdcXmRel){
        entityMapper.saveOrUpdate(bdcXmRel,bdcXmRel.getRelid());
    }

    @Override
    public List<BdcXmRel> createZjgcYzdDyBdcXmRel(Project project){
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        if(StringUtils.isNotBlank(project.getSqlx()) && StringUtils.isNotBlank(project.getZdzhh()) && (StringUtils.equals(project.getSqlx(),Constants.SQLX_ZJJZWDY_DDD_FW_DM) || StringUtils.equals(project.getSqlx(),Constants.SQLX_ZJJZWDY_FW_DM))){
            String zdBdcdyh = project.getZdzhh() + "W00000000";
            String zdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(zdBdcdyh);
            if(StringUtils.isNotBlank(zdBdcdyid)){
                HashMap map = new HashMap();
                map.put("qszt","1");
                map.put("bdcdyid",zdBdcdyid);
                List<BdcDyaq> zdBdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                if(CollectionUtils.isNotEmpty(zdBdcDyaqList)){
                    for(BdcDyaq zdBdcDyaq : zdBdcDyaqList){
                        BdcXmRel zdDyBdcXmRel = new BdcXmRel();
                        zdDyBdcXmRel.setProid(project.getProid());
                        zdDyBdcXmRel.setQjid(project.getDjId());
                        zdDyBdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                        zdDyBdcXmRel.setRelid(UUIDGenerator.generate18());
                        zdDyBdcXmRel.setYqlid(zdBdcDyaq.getQlid());
                        zdDyBdcXmRel.setYproid(zdBdcDyaq.getProid());
                        bdcXmRelList.add(zdDyBdcXmRel);
                    }
                }
            }
            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(zdBdcdyh);
            if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)){
                BdcGdDyhRel bdcGdDyhRel = bdcGdDyhRelList.get(0);
                String tdid = StringUtils.isNotBlank(bdcGdDyhRel.getTdid())?bdcGdDyhRel.getTdid():bdcGdDyhRel.getGdid();
                List<GdDy> gdTdDyList = gdTdService.queryTddyqByTdid(tdid,Constants.GDQL_ISZX_WZX);
                if(CollectionUtils.isNotEmpty(gdTdDyList)){
                    for(GdDy gdTdDy : gdTdDyList){
                        BdcXmRel zdDyBdcXmRel = new BdcXmRel();
                        zdDyBdcXmRel.setProid(project.getProid());
                        zdDyBdcXmRel.setQjid(project.getDjId());
                        zdDyBdcXmRel.setYdjxmly(Constants.XMLY_TDSP);
                        zdDyBdcXmRel.setRelid(UUIDGenerator.generate18());
                        zdDyBdcXmRel.setYqlid(gdTdDy.getDyid());
                        zdDyBdcXmRel.setYproid(gdTdDy.getProid());
                        bdcXmRelList.add(zdDyBdcXmRel);
                    }
                }
            }
        }
        return  bdcXmRelList;
    }

    @Override
    public List<BdcXmRel> creatZjgcDyZxBdcXmRel(Project project, BdcXmRel bdcXmRel) {
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        if(StringUtils.isNotBlank(project.getSqlx()) && StringUtils.equals(project.getSqlx(),Constants.SQLX_ZJJZW_ZX_DM) && bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if(CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.size() > 1){
                    for(BdcXm zjgcDyBdcXm : bdcXmList){
                        if(StringUtils.isNotBlank(zjgcDyBdcXm.getProid()) && !StringUtils.equals(zjgcDyBdcXm.getProid(),bdcXmRel.getYproid())){
                            //生成注销项目
                            BdcXm zjgcZxBdcXm = new BdcXm();
                            String zxProid = UUIDGenerator.generate();
                            zjgcZxBdcXm.setProid(zxProid);
                            String zxYwh = bdcXmService.creatXmbh(bdcXm);
                            if(StringUtils.isNotBlank(zxYwh)){
                                zjgcZxBdcXm.setBh(zxYwh);
                                zjgcZxBdcXm.setYbh(zjgcDyBdcXm.getBh());
                                zjgcZxBdcXm.setCjsj(new Date());
                                zjgcZxBdcXm.setBjsj(new Date());
                                zjgcZxBdcXm.setWiid(project.getWiid());
                                zjgcZxBdcXm.setQllx(Constants.QLLX_DYAQ);
                                zjgcZxBdcXm.setDjlx(Constants.DJLX_DYDJ_DM);
                                zjgcZxBdcXm.setSqlx(project.getSqlx());
                                zjgcZxBdcXm.setBdclx(zjgcDyBdcXm.getBdclx());
                                zjgcZxBdcXm.setBdcdyid(zjgcDyBdcXm.getBdcdyid());
                                entityMapper.saveOrUpdate(zjgcZxBdcXm,zjgcZxBdcXm.getProid());
                            }
                            //生成新的权利人
                            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(zjgcDyBdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                for (int j = 0; j < bdcQlrList.size(); j++) {
                                    BdcQlr bdcQlr = bdcQlrList.get(j);
                                    bdcQlr.setProid(zxProid);
                                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                                    entityMapper.insertSelective(bdcQlr);
                                }
                            }
                            List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(zjgcDyBdcXm.getProid());
                            if(CollectionUtils.isNotEmpty(bdcYwrList)){
                                for (int j = 0; j < bdcYwrList.size(); j++) {
                                    BdcQlr bdcYwr = bdcYwrList.get(j);
                                    bdcYwr.setProid(zxProid);
                                    bdcYwr.setQlrid(UUIDGenerator.generate18());
                                    entityMapper.insertSelective(bdcYwr);
                                }
                            }
                            //生成新的审批信息
                            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(zjgcDyBdcXm.getProid());
                            if (bdcSpxx != null) {
                                BdcSpxx newBdcSpxx = bdcSpxx;
                                newBdcSpxx.setSpxxid(UUIDGenerator.generate18());
                                newBdcSpxx.setProid(zxProid);
                                entityMapper.saveOrUpdate(newBdcSpxx, newBdcSpxx.getSpxxid());
                            }
                            //生成bdcxmrel
                            BdcXmRel zjgcDybdcXmRel = new BdcXmRel();
                            zjgcDybdcXmRel.setProid(zxProid);
                            zjgcDybdcXmRel.setQjid(project.getDjId());
                            //过渡数据和原项目的Yproid赋值区分
                            if (StringUtils.isNotBlank(project.getYxmid())) {
                                zjgcDybdcXmRel.setYproid(zjgcDyBdcXm.getProid());
                            }
                            BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(zjgcDyBdcXm.getProid());
                            if(bdcDyaq != null && StringUtils.isNotBlank(bdcDyaq.getQlid())){
                                zjgcDybdcXmRel.setYqlid(bdcDyaq.getQlid());
                            }
                            zjgcDybdcXmRel.setYdjxmly(project.getXmly());
                            zjgcDybdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRelList.add(zjgcDybdcXmRel);
                        }
                    }
                }
            }
        }
        return bdcXmRelList;
    }

    @Override
    public List<String> getAllGdCfQlidListByBdcXmid(String proid) {
        List<String> yqlidList = new ArrayList<String>();
        List<BdcXmRel> bdcXmRelList = queryBdcXmRelByProid(proid);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
            //拿到当前项目的bdcxmrel
            BdcXmRel bdcXmRel = bdcXmRelList.get(0);
            if(StringUtils.isNotBlank(bdcXmRel.getYqlid())){
                //根据yqlid查询gdbdcqlrel
                List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRel.getYqlid());
                if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                    for(GdBdcQlRel bdcidGdBdcQlRel : gdBdcQlRelList){
                        //获取bdcid
                        if(StringUtils.isNotBlank(bdcidGdBdcQlRel.getBdcid())){
                            //查询该bdcid下所有gdbdcqlrel
                            List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcidGdBdcQlRel.getBdcid());
                            if(CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)){
                                //循环该bdcid下所有gdbdcqlrel 查出现势所有权
                                for(GdBdcQlRel gdBdcQlRelTemp : gdBdcQlRelListTemp){
                                    GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRelTemp.getQlid());
                                    GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRelTemp.getQlid());
                                    if((gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getQlid()) && gdFwsyq.getIszx() != null && gdFwsyq.getIszx() == 0)|| (gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getQlid()) && gdTdsyq.getIszx() != null && gdTdsyq.getIszx() == 0)){
                                        //找出现势所有权 放入gdcf的cfid 跳出该bdcid循环
                                        List<GdCf> gdCfList = gdCfService.getGdCfListByQlid(gdBdcQlRelTemp.getQlid(),0);
                                        if(CollectionUtils.isNotEmpty(gdCfList)){
                                            for(GdCf gdCf : gdCfList){
                                                yqlidList.add(gdCf.getCfid());
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return yqlidList;
    }

    @Override
    public List<String> getXsSyqQlidListByBdcXmid(String proid) {
        List<String> yqlidList = new ArrayList<String>();
        List<BdcXmRel> bdcXmRelList = queryBdcXmRelByProid(proid);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
            //拿到当前项目的bdcxmrel
            BdcXmRel bdcXmRel = bdcXmRelList.get(0);
            if(StringUtils.isNotBlank(bdcXmRel.getYqlid())){
                //根据yqlid查询gdbdcqlrel
                List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRel.getYqlid());
                if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                    for(GdBdcQlRel bdcidGdBdcQlRel : gdBdcQlRelList){
                        //获取bdcid
                        if(StringUtils.isNotBlank(bdcidGdBdcQlRel.getBdcid())){
                            //查询该bdcid下所有gdbdcqlrel
                            List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcidGdBdcQlRel.getBdcid());
                            if(CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)){
                                //循环该bdcid下所有gdbdcqlrel 查出现势所有权
                                for(GdBdcQlRel gdBdcQlRelTemp : gdBdcQlRelListTemp){
                                    GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRelTemp.getQlid());
                                    GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRelTemp.getQlid());
                                    if(gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getQlid())){
                                        yqlidList.add(gdFwsyq.getQlid());
                                    }
                                    if(gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getQlid())){
                                        yqlidList.add(gdTdsyq.getQlid());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return yqlidList;
    }

    @Override
    public void creatBdcXmRelForFsssGdFwsyq(BdcXm bdcXm) {
        if(bdcXm != null) {
            QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
            if(qllxVo != null&&(qllxVo instanceof BdcFdcq || qllxVo instanceof BdcFdcqDz || qllxVo instanceof BdcYg)&&StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if(bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())){
                    List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(bdcBdcdy.getBdcdyh(),"","");
                    if(CollectionUtils.isNotEmpty(gdQlDyhRelList)){
                        for(GdQlDyhRel gdQlDyhRel : gdQlDyhRelList){
                            if(StringUtils.isNotBlank(gdQlDyhRel.getQlid())){
                                Boolean existRel = true;
                                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdQlDyhRel.getQlid());
                                List<GdFw> gdFwList = gdFwService.getGdFwByQlid(gdQlDyhRel.getQlid());
                                List<BdcXmRel> bdcXmRelList = queryBdcXmRelByProid(bdcXm.getProid());
                                if(CollectionUtils.isNotEmpty(gdFwList)){
                                    GdFw gdFw = gdFwList.get(0);
                                    if(StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(),Constants.XZZT_SD)){
                                        HashMap map = new HashMap();
                                        map.put(ParamsConstants.YQLID_LOWERCASE,gdQlDyhRel.getQlid());
                                        map.put(ParamsConstants.PROID_LOWERCASE,bdcXm.getProid());
                                        List<BdcXmRel> bdcXmRelListTemp = andEqualQueryBdcXmRel(map);
                                        if(CollectionUtils.isEmpty(bdcXmRelListTemp)){
                                            existRel = false;
                                        }
                                    }
                                }
                                if(!existRel && gdFwsyq != null){
                                    BdcXmRel bdcXmRel = new BdcXmRel();
                                    bdcXmRel.setProid(bdcXm.getProid());
                                    if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                                        BdcXmRel getQjidBdcXmRel = bdcXmRelList.get(0);
                                        bdcXmRel.setQjid(getQjidBdcXmRel.getQjid());
                                    }
                                    bdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                                    if(StringUtils.isNotBlank(gdFwsyq.getQlid())){
                                        bdcXmRel.setYqlid(gdFwsyq.getQlid());
                                    }
                                    if(StringUtils.isNotBlank(gdFwsyq.getProid())){
                                        bdcXmRel.setYproid(gdFwsyq.getProid());
                                    }
                                    entityMapper.insert(bdcXmRel);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public List<BdcXmRel> getAllHisXmRelByProid(String proid) {
        return  bdcXmRelMapper.getAllHisXmRelByProid(proid);
    }

    @Override
    public List<GdLsCfBh> getAllGdLsCfBhByCfid(String cfid) {
        return bdcXmRelMapper.getAllGdLsCfBhByCfid(cfid);
    }

    @Override
    public void completeDybgBdcXmRelByWiid(String wiid) {
        /**分两种情况
         * 合并登记中，和抵押变更登记有关的，关联抵押权和产权关系(取这一手产权项目的proid)
         * 更正登记流程，抵押权更正登记时，增加当前项目和上一手抵押关联的产权的项目关系（取上一手对应的产权proid）
         * **/
        HashMap<String,String> map = new HashMap();
        map.put(ParamsConstants.WIID_LOWERCASE,wiid);
        //查出产权项目
        map.put(ParamsConstants.NODY_HUMP, ParamsConstants.TRUE_LOWERCASE);
        List<BdcXm> cqBdcXmList = bdcXmService.getBdcXmList(map);
        //查出抵押权项目
        map.remove(ParamsConstants.NODY_HUMP);
        map.put(ParamsConstants.QLLX_LOWERCASE,Constants.QLLX_DYAQ);
        List<BdcXm> dyaqBdcXmList = bdcXmService.getBdcXmList(map);

        completeDybgBdcXmRelByBdcXmList(cqBdcXmList,dyaqBdcXmList);
    }

    @Override
    public void completeDybgBdcXmRelByBdcXmList(List<BdcXm> cqBdcXmList,List<BdcXm> dyaqBdcXmList) {
        String cqProid = null;
        List<BdcXmRel> bdcXmRelList = null;
        List<BdcXmRel> addBdcXmRelList = null;
        String dyaqProid = null;
        if(CollectionUtils.isNotEmpty(cqBdcXmList)&&StringUtils.isNotBlank(cqBdcXmList.get(0).getProid())){
            //含抵押变更的合并登记，取当前流程的产权项目
            cqProid = cqBdcXmList.get(0).getProid();
        }
        if(CollectionUtils.isNotEmpty(dyaqBdcXmList)){
            addBdcXmRelList = new ArrayList<BdcXmRel>();
            if(StringUtils.isNotBlank(dyaqBdcXmList.get(0).getProid())){
                dyaqProid = dyaqBdcXmList.get(0).getProid();
            }
            if(StringUtils.isNotBlank(dyaqProid)){
                bdcXmRelList = queryBdcXmRelByProid(dyaqProid);
            }
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                BdcXmRel bdcXmRelTemp = bdcXmRelList.get(0);
                String ydjxmly = bdcXmRelTemp.getYdjxmly();
                String qjid = bdcXmRelTemp.getQjid();
                if(StringUtils.isBlank(cqProid)){
                    //选择抵押权做更正登记，bdcXmRel根据上一手bdcXmRel数据补全,否则用当前抵押项目bdcXmRel数据补全
                    String yproid = bdcXmRelList.get(0).getYproid();
                    bdcXmRelList.clear();
                    bdcXmRelList = queryBdcXmRelByProid(yproid);
                }
                for(BdcXm bdcXm : dyaqBdcXmList){
                    Boolean existBdcXmRel = false;
                    BdcXmRel addBdcXmRel = new BdcXmRel();
                    if(StringUtils.isNotBlank(cqProid)){
                        //合并登记循环抵押项目补全抵押项目与当前流程中产权的bdcXmRel
                        addBdcXmRel.setRelid(UUIDGenerator.generate18());
                        addBdcXmRel.setYproid(cqProid);
                        addBdcXmRel.setProid(bdcXm.getProid());
                        addBdcXmRel.setQjid(qjid);
                        addBdcXmRel.setYdjxmly(ydjxmly);
                        //避免重复插入
                        existBdcXmRel = existBdcXmRelByProidAndYproid(bdcXm.getProid(),cqProid);
                    }else{
                        //选择抵押权的更正登记，循环原抵押项目的bdcXmRel补全关系
                        addBdcXmRel.setRelid(UUIDGenerator.generate18());
                        addBdcXmRel.setProid(bdcXm.getProid());
                        //获取现势产权proid
                        List<String> xsCqProidList = bdcXmService.getXsCqProid(bdcXm.getBdcdyid());
                        if(CollectionUtils.isNotEmpty(xsCqProidList)){
                            String xsCqProid = xsCqProidList.get(0);
                            addBdcXmRel.setYproid(xsCqProid);
                            addBdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                            //避免重复插入
                            existBdcXmRel = existBdcXmRelByProidAndYproid(bdcXm.getProid(),xsCqProid);
                        }else{
                            List<GdFwsyq> gdFwsyqList = gdXmService.getGdFwsyqListByBdcdyid(bdcXm.getBdcdyid());
                            List<GdTdsyq> gdTdsyqList = gdXmService.getGdTdsyqListByBdcdyid(bdcXm.getBdcdyid());
                            if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                                GdFwsyq gdFwsyq = gdFwsyqList.get(0);
                                addBdcXmRel.setYproid(gdFwsyq.getProid());
                                addBdcXmRel.setYqlid(gdFwsyq.getQlid());
                                addBdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                                //避免重复插入
                                existBdcXmRel = existBdcXmRelByProidAndYproid(bdcXm.getProid(),gdFwsyq.getProid());
                            }else if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                                GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                                addBdcXmRel.setYproid(gdTdsyq.getProid());
                                addBdcXmRel.setYqlid(gdTdsyq.getQlid());
                                addBdcXmRel.setYdjxmly(Constants.XMLY_TDSP);
                                //避免重复插入
                                existBdcXmRel = existBdcXmRelByProidAndYproid(bdcXm.getProid(),gdTdsyq.getProid());
                            }
                        }
                    }
                    if(!existBdcXmRel&&(StringUtils.isNotBlank(addBdcXmRel.getYproid())||StringUtils.isNotBlank(addBdcXmRel.getYqlid()))){
                        addBdcXmRelList.add(addBdcXmRel);
                    }
                }
                if(CollectionUtils.isNotEmpty(addBdcXmRelList)){
                    entityMapper.insertBatchSelective(addBdcXmRelList);
                }
            }
        }
    }

    @Override
    public Boolean existBdcXmRelByProidAndYproid(String proid, String yproid) {
        Boolean existBdcXmRel = false;
        if(StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(yproid)){
            HashMap queryMap = new HashMap();
            queryMap.put("yproid",yproid);
            queryMap.put("proid",proid);
            List<BdcXmRel> bdcXmRelList = andEqualQueryBdcXmRel(queryMap);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                existBdcXmRel = true;
            }
        }
        return existBdcXmRel;
    }
}
