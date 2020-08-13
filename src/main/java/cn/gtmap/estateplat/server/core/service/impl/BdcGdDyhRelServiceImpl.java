package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdFwsyqMapper;
import cn.gtmap.estateplat.server.core.mapper.GdTdsyqMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 匹配数据关系表 林权/房产  不动产单元
 *
 * @author lst
 * @version V1.0, 15-4-20
 */
@Repository
public class BdcGdDyhRelServiceImpl implements BdcGdDyhRelService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private BdcXmRelServiceImpl bdcXmRelService;
    @Autowired
    private GdXmServiceImpl gdXmService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdSaveLogSecvice gdSaveLogSecvice;
    @Autowired
    private GdTdsyqMapper gdTdsyqMapper;
    @Autowired
    private GdFwsyqMapper gdFwsyqMapper;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional(readOnly = true)
    public void addGdDyhRel(BdcGdDyhRel bdcGdDyhRel) {
        if(bdcGdDyhRel != null) {
            //苏州有跨宗的房屋处理，所以需要tdid也判断下
            List<BdcGdDyhRel> bdcGdDyhRelList = getGdDyhRelByGdidOrTdid(bdcGdDyhRel.getGdid(), bdcGdDyhRel.getTdid());
            if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                for(BdcGdDyhRel bdcGdDyhRel1 : bdcGdDyhRelList) {
                    bdcGdDyhRel1.setBdcdyh(bdcGdDyhRel.getBdcdyh());
                    bdcGdDyhRel1.setGdid(bdcGdDyhRel.getGdid());
                    bdcGdDyhRel1.setTdid(bdcGdDyhRel.getTdid());
                    bdcGdDyhRel1.setDjid(bdcGdDyhRel.getDjid());
                    entityMapper.saveOrUpdate(bdcGdDyhRel1, bdcGdDyhRel1.getRelid());
                    gdSaveLogSecvice.gdDyhRelLog(bdcGdDyhRel1);
                }
            } else {
                //jiangganzhi 库中已有未匹配土地证匹配关系时 重新匹配上土地证更新库中已有数据
                List<BdcGdDyhRel> checkBdcGdDyhRelList = getGdDyhRel(bdcGdDyhRel.getBdcdyh(),bdcGdDyhRel.getGdid());
                if(CollectionUtils.isNotEmpty(checkBdcGdDyhRelList)){
                    for(BdcGdDyhRel existBdcGdDyhRel : checkBdcGdDyhRelList){
                        if(!StringUtils.equals(existBdcGdDyhRel.getTdid(),bdcGdDyhRel.getTdid())) {
                            bdcGdDyhRel.setRelid(UUIDGenerator.generate());
                            entityMapper.saveOrUpdate(bdcGdDyhRel, bdcGdDyhRel.getRelid());
                            gdSaveLogSecvice.gdDyhRelLog(bdcGdDyhRel);
                        }
                    }
                }else{
                    bdcGdDyhRel.setRelid(UUIDGenerator.generate());
                    entityMapper.saveOrUpdate(bdcGdDyhRel, bdcGdDyhRel.getRelid());
                    gdSaveLogSecvice.gdDyhRelLog(bdcGdDyhRel);
                }
            }
        }
    }

    /**
     * @param gdQlDyhRel
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn
     * @description 过渡权利与不动产单元之间的关系，以权利主进行插入
     */
    public void addGdQlDyhRel(GdQlDyhRel gdQlDyhRel) {
        StringBuilder djids = new StringBuilder();
        if(gdQlDyhRel != null) {
            List<GdQlDyhRel> gdQlDyhRelList = getGdQlDyhRel("", gdQlDyhRel.getQlid(), "");
            if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                for(GdQlDyhRel gdQlDyhRel1 : gdQlDyhRelList) {
                    gdQlDyhRel1.setBdcdyh(gdQlDyhRel.getBdcdyh());
                    gdQlDyhRel1.setQlid(gdQlDyhRel.getQlid());
                    gdQlDyhRel1.setTdqlid(gdQlDyhRel.getTdqlid());
                    gdQlDyhRel1.setDjid(gdQlDyhRel.getDjid());
                    List<GdBdcQlRel> gdBdcQlRelListtemp = gdBdcQlRelService.queryGdBdcQlListByQlid(gdQlDyhRel.getQlid());
                    if(CollectionUtils.isNotEmpty(gdBdcQlRelListtemp)) {
                        for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelListtemp) {
                            List<BdcGdDyhRel> gdDyhRelList = getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                            if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                                if(StringUtils.isBlank(djids)) {
                                    djids.append(gdDyhRelList.get(0).getDjid());
                                    //zq 不动产单元号不需要存
                                    gdQlDyhRel1.setDjid(djids.toString());
                                } else {
                                    if(!StringUtils.equals(djids, gdDyhRelList.get(0).getDjid())) {
                                        djids.append(",").append(gdDyhRelList.get(0).getDjid());
                                        gdQlDyhRel1.setDjid(djids.toString());
                                    }
                                }
                            }
                        }
                    }
                    gdQlDyhRel1.setBdclx(gdQlDyhRel1.getBdclx());
                    entityMapper.saveOrUpdate(gdQlDyhRel1, gdQlDyhRel1.getRelid());
                }
            } else {
                gdQlDyhRel.setRelid(UUIDGenerator.generate());
                entityMapper.saveOrUpdate(gdQlDyhRel, gdQlDyhRel.getRelid());
            }
        }
    }

    /**
     * @author bianwen
     * @description 根据bdcid查出匹配关系，如果存在匹配关系，则再进行权利匹配关系判断
     * 若不存在不动产单元匹配关系，则直接插入权利匹配关系
     */
    public void addGdQlDyhRelByGdid(GdQlDyhRel gdQlDyhRel, String gdid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = new ArrayList<BdcGdDyhRel>();
        if(StringUtils.isNotBlank(gdid)) {
            bdcGdDyhRelList = getGdDyhRelByGdId(gdid);
        }
        if(gdQlDyhRel != null) {
            if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                BdcGdDyhRel bdcGdDyhRel = bdcGdDyhRelList.get(0);
                List<GdQlDyhRel> gdQlDyhRelList = getGdQlDyhRel(bdcGdDyhRel.getBdcdyh(), gdQlDyhRel.getQlid(), "");
                if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                    GdQlDyhRel gdQlDyhRel1 = gdQlDyhRelList.get(0);
                    gdQlDyhRel1.setBdcdyh(gdQlDyhRel.getBdcdyh());
                    if(StringUtils.isNotBlank(gdQlDyhRel.getTdqlid())) {
                        gdQlDyhRel1.setTdqlid(gdQlDyhRel.getTdqlid());
                    }
                    entityMapper.saveOrUpdate(gdQlDyhRel1, gdQlDyhRel1.getRelid());
                    gdSaveLogSecvice.gdQlDyhRelLog(gdQlDyhRel1);
                } else {
                    gdQlDyhRel.setDjid(bdcGdDyhRel.getDjid());
                    gdQlDyhRel.setRelid(UUIDGenerator.generate());
                    entityMapper.saveOrUpdate(gdQlDyhRel, gdQlDyhRel.getRelid());
                    gdSaveLogSecvice.gdQlDyhRelLog(gdQlDyhRel);
                }
            } else {
                gdQlDyhRel.setRelid(UUIDGenerator.generate());
                entityMapper.saveOrUpdate(gdQlDyhRel, gdQlDyhRel.getRelid());
                gdSaveLogSecvice.gdQlDyhRelLog(gdQlDyhRel);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcGdDyhRel> getGdDyhRel(final String dyh, final String gdid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = null;
        Example example = new Example(BdcGdDyhRel.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(gdid)) {
            criteria.andEqualTo("gdid", gdid);
        }
        if(StringUtils.isNotBlank(dyh)) {
            criteria.andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dyh);
        }
        if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
            bdcGdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class, example);
        }

        return bdcGdDyhRelList;
    }


    @Transactional(readOnly = true)
    public List<BdcGdDyhRel> getGdDyhRelByGdidOrTdid(final String gdid, final String tdid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = null;
        Example example = new Example(BdcGdDyhRel.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(gdid)) {
            criteria.andEqualTo("gdid", gdid);
        }
        if(StringUtils.isNotBlank(tdid)) {
            criteria.andEqualTo("tdid", tdid);
        } else
            criteria.andIsNull("tdid");
        if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
            bdcGdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class, example);
        }
        return bdcGdDyhRelList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcGdDyhRel> getGdDyhRelByDjid(final String dyh, final String djid, final String gdid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = null;
        Example example = new Example(BdcGdDyhRel.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(gdid)) {
            criteria.andEqualTo("gdid", gdid);
        }
        if(StringUtils.isNotBlank(dyh)) {
            criteria.andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dyh);
        }
        if(StringUtils.isNotBlank(djid)) {
            criteria.andEqualTo("djid", djid);
        }
        if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
            bdcGdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class, example);
        }
        return bdcGdDyhRelList;

    }


    public List<GdQlDyhRel> getGdQlDyhRel(final String dyh, final String qlid, final String tdqlid) {
        List<GdQlDyhRel> gdQlDyhRelList = null;
        Example example = new Example(GdQlDyhRel.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(qlid)) {
            criteria.andEqualTo("qlid", qlid);
        }
        if(StringUtils.isNotBlank(tdqlid)) {
            criteria.andEqualTo("tdqlid", tdqlid);
        }
        if(StringUtils.isNotBlank(dyh)) {
            criteria.andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dyh);
        }
        if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
            gdQlDyhRelList = entityMapper.selectByExample(GdQlDyhRel.class, example);
        }
        return gdQlDyhRelList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcGdDyhRel> getGdDyhRelByGdId(final String gdid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = null;
        Example example = new Example(BdcGdDyhRel.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(gdid)) {
            criteria.andEqualTo("gdid", gdid);
            bdcGdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class, example);
        }
        return bdcGdDyhRelList;
    }

    @Override
    public List<BdcGdDyhRel> getGdDyhRelByDyh(final String bdcydh) {
        List<BdcGdDyhRel> bdcGdDyhRelList = null;
        Example example = new Example(BdcGdDyhRel.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(bdcydh)) {
            criteria.andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcydh);
            bdcGdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class, example);
        }
        return bdcGdDyhRelList;
    }

    @Override
    public List<BdcGdDyhRel> getGdDyhRelList(final String gdproids) {
        List<BdcGdDyhRel> bdcGdDyhRelList = new ArrayList<BdcGdDyhRel>();
        if(StringUtils.isNotBlank(gdproids)) {
            String[] gdproidarr = gdproids.split(",");
            for(String gdproid : gdproidarr) {
                List<BdcGdDyhRel> bdcGdDyhRelListTemp = getGdDyhRelByGdId(gdproid);
                if(CollectionUtils.isNotEmpty(bdcGdDyhRelListTemp)) {
                    bdcGdDyhRelList.addAll(bdcGdDyhRelListTemp);
                }
            }
        }
        return bdcGdDyhRelList;
    }

    /**
     * @param djid
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn String
     * @description 多个房屋匹配一个不动产单元，查出qlid集合
     */
    @Override
    public String getGdDyhRelQlidsByDjids(final String djid, final String qlids) {
        //zq 查询gd_ql_dyh_rel查询所有权利匹配的集合
        List<GdQlDyhRel> gdQlDyhRelList = new ArrayList<GdQlDyhRel>();
        String[] qlidArr = qlids.split(",");
        for(String qlid : qlidArr) {
            Example example = new Example(GdQlDyhRel.class);
            Example.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotBlank(qlid)) {
                criteria.andEqualTo("qlid", qlid);
                List<GdQlDyhRel> gdQlDyhReltempList = entityMapper.selectByExample(GdQlDyhRel.class, example);
                if(CollectionUtils.isNotEmpty(gdQlDyhReltempList)) {
                    gdQlDyhRelList.addAll(gdQlDyhReltempList);
                }
            }
        }
        List<String> qlidlist = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            for(GdQlDyhRel gdQlDyhRel : gdQlDyhRelList) {
                if(StringUtils.isNotBlank(gdQlDyhRel.getDjid())) {
                    String[] djidArr = gdQlDyhRel.getDjid().split(",");
                    List<String> tempList = Arrays.asList(djidArr);
                    if(tempList.contains(djid)) {
                        qlidlist.add(gdQlDyhRel.getQlid());
                    }
                }
            }
        }
        StringBuilder returnqlids = new StringBuilder();
        if(CollectionUtils.isNotEmpty(qlidlist)) {
            for(String qlid : qlidlist) {
                if(StringUtils.isBlank(returnqlids)) {
                    returnqlids.append(qlid);
                } else {
                    if(!StringUtils.contains(returnqlids,qlid)) {
                        returnqlids.append(",").append(qlid);
                    }
                }
            }
        }
        return returnqlids.toString();
    }


    @Override
    public String getProidsByDjids(String qlids) {
        String[] qlidArr = null;
        StringBuilder proids = new StringBuilder();
        if(StringUtils.isNotBlank(qlids)) {
            qlidArr = qlids.split(",");
            for(int i = 0; i < qlidArr.length; i++) {
                String qlid = qlidArr[i];
                String proid = gdXmService.getGdproidByQlid(qlid);
                if(StringUtils.isNotBlank(proid)) {
                    if(StringUtils.isBlank(proids)) {
                        proids.append(proid);
                    }else {
                        proids.append(",").append(proid);
                    }
                }
            }
        }
        return proids.toString();
    }

    @Override
    public List<BdcGdDyhRel> getTdDyhRelBytdids(final String tdids) {
        List<BdcGdDyhRel> bdcGdDyhRelList = new ArrayList<BdcGdDyhRel>();
        if(tdids != null && tdids.length() > 0) {
            String[] tdidTemp = tdids.split(",");
            for(String tdid : tdidTemp) {
                List<BdcGdDyhRel> bdcGdDyhRel = getGdDyhRelByGdId(tdid);
                if(CollectionUtils.isNotEmpty(bdcGdDyhRel)) {
                    bdcGdDyhRel.get(0).setGdid(tdid);
                    bdcGdDyhRelList.add(bdcGdDyhRel.get(0));
                }
            }
        }
        return bdcGdDyhRelList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcGdDyhRel> getGdDyhRel(final String tdid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = null;
        if(StringUtils.isNotBlank(tdid)) {
            Example example = new Example(BdcGdDyhRel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("tdid", tdid);
            bdcGdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class, example);
        }
        return bdcGdDyhRelList;
    }

    /**
     * @param proid,bdcdyh
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @rerutn null
     * @description 根据proid和不动产单元号更新GdDyhRel
     */
    @Override

    public void updateGdDyhRelByProidAndBdcdyh(final String proid, final String bdcdyh) {
        if(StringUtils.isNotBlank(proid)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for(BdcXmRel bdcXmRel : bdcXmRelList) {
                    if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        String bdcid = "";
                        List<String> bdcidList = gdXmService.getGdBdcidByProid(bdcXmRel.getYproid());
                        if(CollectionUtils.isNotEmpty(bdcidList)) {
                            bdcid = bdcidList.get(0);
                        }
                        List<BdcGdDyhRel> gdDyhRelList = getGdDyhRel(null, bdcid);
                        if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                            for(BdcGdDyhRel gdDyhRel : gdDyhRelList) {
                                gdDyhRel.setBdcdyh(bdcdyh);
                                entityMapper.saveOrUpdate(gdDyhRel, gdDyhRel.getRelid());
                            }
                        } else {
                            BdcGdDyhRel bdcGdDyhRel = new BdcGdDyhRel();
                            bdcGdDyhRel.setRelid(UUIDGenerator.generate());
                            bdcGdDyhRel.setGdid(bdcid);
                            if(StringUtils.isNotBlank(bdcdyh)) {
                                bdcGdDyhRel.setBdcdyh(bdcdyh);
                            }
                            entityMapper.saveOrUpdate(bdcGdDyhRel, bdcGdDyhRel.getRelid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<BdcGdDyhRel> getGdDyhRelByGdproid(final String gdproid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = new ArrayList<BdcGdDyhRel>();
        if(StringUtils.isNotBlank(gdproid)) {
            List<GdFw> gdFwList = gdFwService.getGdFwByProid(gdproid, "");
            List<GdTd> gdTdList = gdTdService.getGdTdListByProid(gdproid, "");

            if(CollectionUtils.isNotEmpty(gdFwList)) {
                for(GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> gdDyhRelList = getGdDyhRel("", gdFw.getFwid());
                    if(CollectionUtils.isNotEmpty(gdDyhRelList))
                        bdcGdDyhRelList.addAll(gdDyhRelList);
                }
            }
            if(CollectionUtils.isNotEmpty(gdTdList)) {
                for(GdTd gdTd : gdTdList) {
                    List<BdcGdDyhRel> gdDyhRelList = getGdDyhRel("", gdTd.getTdid());
                    if(CollectionUtils.isNotEmpty(gdDyhRelList))
                        bdcGdDyhRelList.addAll(gdDyhRelList);
                }
            }
        }
        return bdcGdDyhRelList;
    }

    @Override
    public void deleteGdPpgx(final String qlid, final String bdclx) {
        if(StringUtils.isNotBlank(qlid)) {
            Example example = new Example(GdQlDyhRel.class);
            Example.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotBlank(bdclx))
                criteria.andEqualTo("qlid", qlid).andEqualTo("bdclx", bdclx);
            if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())){
                List<GdQlDyhRel> gdQlDyhRelList = entityMapper.selectByExample(GdQlDyhRel.class,example);
                if(CollectionUtils.isNotEmpty(gdQlDyhRelList)){
                    for(GdQlDyhRel gdQlDyhRel:gdQlDyhRelList){
                        gdSaveLogSecvice.gdQlDyhRelQxppLog(gdQlDyhRel);
                    }
                }
                entityMapper.deleteByExample(GdQlDyhRel.class, example);
            }
            if(StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(qlid);
                if(CollectionUtils.isNotEmpty(gdTdList)) {
                    for(GdTd gdTd : gdTdList) {
                        if(StringUtils.isNotBlank(gdTd.getTdid())) {
                            Example tdExample = new Example(BdcGdDyhRel.class);
                            Example.Criteria tdCriteria = tdExample.createCriteria();
                            tdCriteria.andEqualTo("gdid", gdTd.getTdid());
                            List<BdcGdDyhRel> gdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class,tdExample);
                            if(CollectionUtils.isNotEmpty(gdDyhRelList)){
                                for(BdcGdDyhRel bdcGdDyhRel:gdDyhRelList){
                                    gdSaveLogSecvice.gdDyhRelQxppLog(bdcGdDyhRel);
                                }
                            }
                            entityMapper.deleteByExample(BdcGdDyhRel.class, tdExample);
                        }
                    }
                }
            } else {
                List<GdFw> gdFwList = gdFwService.getGdFwByQlid(qlid);
                if(CollectionUtils.isNotEmpty(gdFwList)) {
                    for(GdFw gdFw : gdFwList) {
                        if(StringUtils.isNotBlank(gdFw.getFwid())) {
                            Example fwExample = new Example(BdcGdDyhRel.class);
                            Example.Criteria fwCriteria = fwExample.createCriteria();
                            fwCriteria.andEqualTo("gdid", gdFw.getFwid());
                            List<BdcGdDyhRel> gdDyhRelList = entityMapper.selectByExample(BdcGdDyhRel.class,fwExample);
                            if(CollectionUtils.isNotEmpty(gdDyhRelList)){
                                for(BdcGdDyhRel bdcGdDyhRel:gdDyhRelList){
                                    gdSaveLogSecvice.gdDyhRelQxppLog(bdcGdDyhRel);
                                }
                            }
                            entityMapper.deleteByExample(BdcGdDyhRel.class, fwExample);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void saveGdDyhRels(String fwid, String tdid, String tdqlid, String bdcdyh, String djid) {
        BdcGdDyhRel bdcGdDyhRel = new BdcGdDyhRel();
        bdcGdDyhRel.setBdcdyh(bdcdyh);
        bdcGdDyhRel.setDjid(djid);
        bdcGdDyhRel.setGdid(fwid);

        //创建时 tdid是qlid 要通过 qlid 检索tdid。
        if(StringUtils.isNotBlank(tdid) && StringUtils.isBlank(tdqlid))
            tdqlid = tdid;

        List<BdcGdDyhRel> bdcGdDyhRelList = new ArrayList<BdcGdDyhRel>();
        if(StringUtils.isNotBlank(tdqlid)) {
            //匹配土地
            bdcGdDyhRelList = getBdcGdDyhRelByTdQlids(bdcGdDyhRel, tdqlid);
        } else {
            //没有匹配土地
            bdcGdDyhRelList.add(bdcGdDyhRel);
        }

        //zq 插入过渡房屋或者过渡土地与不动产单元之间的关系，一证多房引起的问题
        if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
            for(BdcGdDyhRel gdDyhRel : bdcGdDyhRelList){
                addGdDyhRel(gdDyhRel);
            }
        }
    }

    public List<BdcGdDyhRel> getBdcGdDyhRelByTdQlids(BdcGdDyhRel bdcGdDyhRel, String tdqlid) {
        List<BdcGdDyhRel> bdcGdDyhRelList = new ArrayList<BdcGdDyhRel>();
        HashSet<String> tdidList = new HashSet<String>();
        //通过tdqlid查询不重复的土地
        try {
            String[] qlidArray = tdqlid.split(",");
            for(String qlid : qlidArray) {
                List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(qlid);
                if(CollectionUtils.isNotEmpty(gdTdList) && StringUtils.isNotBlank(gdTdList.get(0).getTdid()))
                    tdidList.add(gdTdList.get(0).getTdid());

            }
            //给tdid赋值
            if(CollectionUtils.isNotEmpty(tdidList)) {
                for(String tdid : tdidList) {
                    BdcGdDyhRel gdDyhRel = new BdcGdDyhRel();
                    BeanUtils.copyProperties(gdDyhRel, bdcGdDyhRel);
                    gdDyhRel.setTdid(tdid);
                    bdcGdDyhRelList.add(gdDyhRel);
                }
            } else {
                bdcGdDyhRelList.add(bdcGdDyhRel);
            }
        } catch (Exception e) {
           logger.error("BdcGdDyhRelServiceImpl.getBdcGdDyhRelByTdQlids",e);
        }
        return bdcGdDyhRelList;
    }

    @Override
    public Map<String,String> getYqlidsAndYxmidsByBdcdyh(String bdcdyh){
        HashMap<String, String> dataMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(bdcdyh)) {
            List<GdFwsyq> gdFwsyqList = gdFwsyqMapper.getGdFwsyqListByBdcdyh(bdcdyh);
            if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                for(GdFwsyq gdFwsyq : gdFwsyqList){
                    if(StringUtils.isNotBlank(gdFwsyq.getQlid()) && StringUtils.isNotBlank(gdFwsyq.getProid())){
                        dataMap.put(gdFwsyq.getProid(),gdFwsyq.getQlid());
                    }
                }
            }
        }
        return getYqlidsAndYxmids(dataMap);
    }

    @Override
    public Map<String, String> getTdYqlidsAndYxmidsByBdcdyh(String bdcdyh) {
        HashMap<String, String> dataMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(bdcdyh)) {
            List<GdTdsyq> gdTdsyqList = gdTdsyqMapper.getTdGdTdsyqListByBdcdyh(bdcdyh);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                for(GdTdsyq gdTdsyq : gdTdsyqList){
                    if(StringUtils.isNotBlank(gdTdsyq.getQlid()) && StringUtils.isNotBlank(gdTdsyq.getProid())){
                        dataMap.put(gdTdsyq.getProid(),gdTdsyq.getQlid());
                    }
                }
            }
        }
        return getYqlidsAndYxmids(dataMap);
    }

    private Map<String, String> getYqlidsAndYxmids(HashMap<String, String> dataMap){
        Map<String,String> map = new HashMap<String, String>();
        String yxmidsString;
        String yqlidsString;
        if(CollectionUtils.isNotEmpty(dataMap.entrySet())){
            List<String> yqlidList = new ArrayList<String>();
            List<String> yxmidList = new ArrayList<String>();
            for(Map.Entry<String, String> entry : dataMap.entrySet()){
                yxmidList.add(entry.getKey());
                yqlidList.add(entry.getValue());
            }
            if(CollectionUtils.isNotEmpty(yqlidList) && CollectionUtils.isNotEmpty(yxmidList)){
                yqlidsString = PublicUtil.join(",", yqlidList);
                yxmidsString = PublicUtil.join(",", yxmidList);
                map.put("yqlids",yqlidsString);
                map.put("yxmids",yxmidsString);
            }
        }
        return map;
    }
}
