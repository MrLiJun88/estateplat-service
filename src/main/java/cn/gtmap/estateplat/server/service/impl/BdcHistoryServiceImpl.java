package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.BdcHistoryService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/8/18
 * @description 不动产历史关系服务
 */
@Service
public class BdcHistoryServiceImpl implements BdcHistoryService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private GdYgService gdYgService;
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 不动产项目ID
     * @return
     * @description 根据当前项目的项目历史关系生成产权历史关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateQlHistoryByProid(final String proid) {
        if(StringUtils.isBlank(proid)) return;
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class,proid);
        final String bdclx = bdcXm.getBdclx();
        final String bdcdyid = bdcXm.getBdcdyid();
        final String qllx = bdcXm.getQllx();
        final boolean sfcq = sfcq(proid,qllx);
        final BdcBdcdy bdcBdcdy = entityMapper.selectByPrimaryKey(BdcBdcdy.class,bdcdyid);
        final String bdcdyh = bdcBdcdy.getBdcdyh();


        //当前项目所产生的产权权利
        Example example = new Example(BdcXmRel.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE,proid);
        List<BdcXmRel> bdcXmRelList = entityMapper.selectByExampleNotNull(example);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
            for(BdcXmRel bdcXmRel:bdcXmRelList){
                String[] yproids;
                if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    yproids = StringUtils.split(bdcXmRel.getYproid(), ",");
                } else{
                    yproids = new String[1];
                }
                String[] yqlids;
                if(StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                    yqlids = StringUtils.split(bdcXmRel.getYqlid(), ",");
                } else {
                    yqlids = new String[1];
                }

                if("1".equals(bdcXmRel.getYdjxmly())){
                    //上一手项目来源于不动产登记业务
                    if(sfcq){
                        //如果当前不动产项目是产权
                        for(int i=0;i<yproids.length;i++){
                            BdcLsQlBh bdcLsQlBh = getBdcLsQlBhByBdc(proid, qllx, yproids[i]);
                            entityMapper.insertSelective(bdcLsQlBh);
                        }
                    }else{
                        //如果当前不动产项目是非产权
                        //当前项目所产生的产权与抵押关系
                        if(Constants.QLLX_DYAQ.equals(qllx)) {
                            handleProjectDy(bdcXm, bdcXmRel, bdcdyh);
                        } else if(Constants.QLLX_CFDJ.equals(qllx)) {
                            handleProjectCf(bdcXm, bdcXmRel, bdcdyh);
                        } else if(Constants.QLLX_YGDJ.equals(qllx)) {
                            handleProjectYgdy(bdcXm, bdcXmRel, bdcdyh);
                        }
                    }
                }else{
                    //上一手项目来源于过渡库数据业务
                    if (sfcq){
                        for(int i=0;i<yqlids.length;i++){
                            BdcLsQlBh bdcLsQlBh = getBdcLsQlBhByGd(proid, qllx, yqlids[i]);
                            entityMapper.insertSelective(bdcLsQlBh);
                        }
                    }else{
                        //当前项目所产生的产权与抵押关系
                        if(Constants.QLLX_DYAQ.equals(qllx)) {
                            handleProjectDy(bdcXm, bdcXmRel, bdcdyh);
                        } else if(Constants.QLLX_CFDJ.equals(qllx)) {
                            handleProjectCf(bdcXm, bdcXmRel, bdcdyh);
                        } else if(Constants.QLLX_YGDJ.equals(qllx)) {
                            handleProjectYgdy(bdcXm, bdcXmRel, bdcdyh);
                        }
                    }
                }
            }
        }
        if(Constants.BDCLX_TD.equals(bdclx)){
            //当前项目所关联的净地不动产单元信息
            final String djh = bdcdyh.substring(0,19);
            BdcLsBdcdyTdBh bdcLsBdcdyTdBh = new BdcLsBdcdyTdBh();
            bdcLsBdcdyTdBh.setBdcdyid(bdcdyid);
            bdcLsBdcdyTdBh.setBdcdyh(bdcdyh);
            bdcLsBdcdyTdBh.setDjh(djh);
            entityMapper.saveOrUpdate(bdcLsBdcdyTdBh,bdcdyid);
        }else if(Constants.BDCLX_TDFW.equals(bdclx)){
            //当前项目所管理的房地一体不动产单元信息
            BdcLsBdcdyTdfwBh bdcLsBdcdyTdFwBh = new BdcLsBdcdyTdfwBh();
            bdcLsBdcdyTdFwBh.setBdcdyid(bdcdyid);
            bdcLsBdcdyTdFwBh.setBdcdyh(bdcdyh);
            Map fwbhMap = getFwzlAndFwbh(bdcdyh);
            bdcLsBdcdyTdFwBh.setFwbh(String.valueOf(fwbhMap.get("fwbh")));
            bdcLsBdcdyTdFwBh.setZl(String.valueOf(fwbhMap.get("zl")));
            entityMapper.saveOrUpdate(bdcLsBdcdyTdFwBh,bdcdyid);
        }else {
            throw new AppException("4004");
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 不动产项目ID
     * @return
     * @description 删除当前项目产生的产权历史关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQlHistoryByProid(final String proid) {
        if(StringUtils.isBlank(proid)) return;
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class,proid);
        final String qllx = bdcXm.getQllx();
        final boolean sfcq = sfcq(proid,qllx);
        Example example = new Example(BdcXmRel.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE,proid);
        List<BdcXmRel> bdcXmRelList = entityMapper.selectByExampleNotNull(example);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
            for(BdcXmRel bdcXmRel:bdcXmRelList){
                String[] yproids;
                if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    yproids = StringUtils.split(bdcXmRel.getYproid(), ",");
                }else {
                    yproids = new String[1];
                }
                String[] yqlids;
                if(StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                    yqlids = StringUtils.split(bdcXmRel.getYqlid(), ",");
                }else {
                    yqlids = new String[1];
                }

                if("1".equals(bdcXmRel.getYdjxmly())){
                    //上一手项目来源于不动产登记业务
                    if(sfcq){
                        //如果当前不动产项目是产权
                        for(int i=0;i<yproids.length;i++){
                            BdcLsQlBh bdcLsQlBh = getBdcLsQlBhByBdc(proid, qllx, yproids[i]);
                            example = new Example(BdcLsQlBh.class);
                            example.createCriteria().andEqualTo("qlid",bdcLsQlBh.getQlid());
                            entityMapper.deleteByExampleNotNull(example);
                        }
                    }else{
                        //如果当前不动产项目是非产权
                        if(Constants.QLLX_DYAQ.equals(qllx)) {
                            deleteProjectDy(bdcXm);
                        }else if(Constants.QLLX_CFDJ.equals(qllx)){
                            deleteProjectCf(bdcXm);
                        }else if(Constants.QLLX_YGDJ.equals(qllx)) {
                            deleteProjectYgdy(bdcXm);
                        }
                    }
                }else{
                    //上一手项目来源于过渡库数据业务
                    if (sfcq){
                        for(int i=0;i<yqlids.length;i++){
                            BdcLsQlBh bdcLsQlBh = getBdcLsQlBhByGd(proid, qllx, yqlids[i]);
                            example = new Example(BdcLsQlBh.class);
                            example.createCriteria().andEqualTo("qlid",bdcLsQlBh.getQlid());
                            entityMapper.deleteByExampleNotNull(example);
                        }
                    }else{
                        if(Constants.QLLX_DYAQ.equals(qllx)) {
                            deleteProjectDy(bdcXm);
                        }else if(Constants.QLLX_CFDJ.equals(qllx)) {
                            deleteProjectCf(bdcXm);
                        }else if(Constants.QLLX_YGDJ.equals(qllx)) {
                            deleteProjectYgdy(bdcXm);
                        }
                    }
                }
            }
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcXm
     * @param bdcdyh
     * @param bdcXmRel
     * @return
     * @description 对当前项目是生成的抵押权进行生成相关历史信息
     */
    private void handleProjectDy(BdcXm bdcXm,BdcXmRel bdcXmRel,String bdcdyh){
        String bdcdyid = bdcXm.getBdcdyid();
        String bdclx = bdcXm.getBdclx();
        Example example = new Example(BdcDyaq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
        List<BdcDyaq> bdcDyaqList = entityMapper.selectByExampleNotNull(example);
        if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
            //当前项目产生的权利是抵押
            for (BdcDyaq bdcDyaq : bdcDyaqList) {
                if (StringUtils.isNotBlank(bdcDyaq.getZjgcdyfw())) {
                    //如果是在建工程抵押，则不动产单元与抵押关系
                    BdcLsBdcdyDyRel bdcLsBdcdyDyRel = new BdcLsBdcdyDyRel();
                    bdcLsBdcdyDyRel.setBdcdyid(bdcdyid);
                    bdcLsBdcdyDyRel.setDyid(bdcDyaq.getQlid());
                    bdcLsBdcdyDyRel.setBdclx(bdclx);
                    entityMapper.insertSelective(bdcLsBdcdyDyRel);
                } else {
                    //当前项目所产生的抵押信息
                    //产权与抵押的关系
                    Qllx xsql = getBdcXsql(bdcdyid);
                    if(xsql!=null){
                        BdcLsQlDyRel bdcLsQlDyRel = new BdcLsQlDyRel();
                        bdcLsQlDyRel.setQlid(xsql.getQlid());
                        bdcLsQlDyRel.setBdclx(bdclx);
                        bdcLsQlDyRel.setDyid(bdcDyaq.getQlid());
                        entityMapper.insertSelective(bdcLsQlDyRel);
                    }else{
                        List<String> gdQlidList = getGdXsql(bdcdyh);
                        if(CollectionUtils.isNotEmpty(gdQlidList)){
                            for(String gdQlid:gdQlidList){
                                BdcLsQlDyRel bdcLsQlDyRel = new BdcLsQlDyRel();
                                bdcLsQlDyRel.setQlid(gdQlid);
                                bdcLsQlDyRel.setBdclx(bdclx);
                                bdcLsQlDyRel.setDyid(bdcDyaq.getQlid());
                                entityMapper.insertSelective(bdcLsQlDyRel);
                            }
                        }
                    }
                    //抵押变化关系
                    List<BdcLsDyBh> bdcLsDyBhList = getBdcLsDyBh(bdcXm,bdcXmRel,bdcDyaq);
                    if(CollectionUtils.isNotEmpty(bdcLsDyBhList)) {
                        entityMapper.insertBatchSelective(bdcLsDyBhList);
                    }
                }
            }
        }
    }

    private void handleProjectYgdy(BdcXm bdcXm,BdcXmRel bdcXmRel,String bdcdyh){
        String bdcdyid = bdcXm.getBdcdyid();
        String bdclx = bdcXm.getBdclx();
        BdcYg bdcYg = bdcYgService.getBdcYgByProid(bdcXm.getProid());
        if (bdcYg!=null) {
            //当前项目产生的权利是抵押
            //当前项目所产生的抵押信息
            //产权与抵押的关系
            Qllx xsql = getBdcXsql(bdcdyid);
            if(xsql!=null){
                BdcLsQlDyRel bdcLsQlDyRel = new BdcLsQlDyRel();
                bdcLsQlDyRel.setQlid(xsql.getQlid());
                bdcLsQlDyRel.setBdclx(bdclx);
                bdcLsQlDyRel.setDyid(bdcYg.getQlid());
                entityMapper.insertSelective(bdcLsQlDyRel);
            }else{
                List<String> gdQlidList = getGdXsql(bdcdyh);
                if(CollectionUtils.isNotEmpty(gdQlidList)){
                    for(String gdQlid:gdQlidList){
                        BdcLsQlDyRel bdcLsQlDyRel = new BdcLsQlDyRel();
                        bdcLsQlDyRel.setQlid(gdQlid);
                        bdcLsQlDyRel.setBdclx(bdclx);
                        bdcLsQlDyRel.setDyid(bdcYg.getQlid());
                        entityMapper.insertSelective(bdcLsQlDyRel);
                    }
                }
            }
            //预告抵押变化关系
            List<BdcLsDyBh> bdcLsDyBhList = getBdcLsDyBhByYg(bdcXm,bdcXmRel,bdcYg);
            if(CollectionUtils.isNotEmpty(bdcLsDyBhList)) {
                entityMapper.insertBatchSelective(bdcLsDyBhList);
            }
        }
    }

    private void deleteProjectYgdy(BdcXm bdcXm){
        BdcYg bdcYg = bdcYgService.getBdcYgByProid(bdcXm.getProid());
        if (bdcYg!=null) {
            //当前项目产生的权利是抵押
            //当前项目所产生的抵押信息
            //产权与抵押的关系
            Example example = new Example(BdcLsQlDyRel.class);
            example.createCriteria().andEqualTo("dyid",bdcYg.getQlid());
            entityMapper.deleteByExampleNotNull(example);
            //抵押变化关系
            example = new Example(BdcLsDyBh.class);
            example.createCriteria().andEqualTo("dyid",bdcYg.getQlid());
            entityMapper.deleteByExampleNotNull(example);
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcXm
     * @return
     * @description 对当前项目是生成的抵押权进行删除相关历史信息
     */
    private void deleteProjectDy(BdcXm bdcXm){
        String bdcdyid = bdcXm.getBdcdyid();
        Example example = new Example(BdcDyaq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
        List<BdcDyaq> bdcDyaqList = entityMapper.selectByExampleNotNull(example);
        if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
            //当前项目产生的权利是抵押
            for (BdcDyaq bdcDyaq : bdcDyaqList) {
                if (StringUtils.isNotBlank(bdcDyaq.getZjgcdyfw())) {
                    //如果是在建工程抵押，则不动产单元与抵押关系
                    example = new Example(BdcLsBdcdyDyRel.class);
                    example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE,bdcdyid).andEqualTo("dyid",bdcDyaq.getQlid());
                    entityMapper.deleteByExampleNotNull(example);
                } else {
                    //当前项目所产生的抵押信息
                    //产权与抵押的关系
                    example = new Example(BdcLsQlDyRel.class);
                    example.createCriteria().andEqualTo("dyid",bdcDyaq.getQlid());
                    entityMapper.deleteByExampleNotNull(example);
                    //抵押变化关系
                    example = new Example(BdcLsDyBh.class);
                    example.createCriteria().andEqualTo("dyid",bdcDyaq.getQlid());
                    entityMapper.deleteByExampleNotNull(example);
                }
            }
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcXm
     * @param bdcdyh
     * @param bdcXmRel
     * @return
     * @description 对当前项目是生成的查封进行处理
     */
    private void handleProjectCf(BdcXm bdcXm,BdcXmRel bdcXmRel,String bdcdyh){
        String bdcdyid = bdcXm.getBdcdyid();
        String bdclx = bdcXm.getBdclx();
        Example example = new Example(BdcCf.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
        List<BdcCf> bdcCfList = entityMapper.selectByExampleNotNull(example);
        if (CollectionUtils.isNotEmpty(bdcCfList)) {
            //当前项目产生的权利是查封
            for (BdcCf bdcCf : bdcCfList) {
                if (Constants.CFLX_ZD_YCF.equals(bdcCf.getCflx())) {
                    //如果是预查封，则不动产单元与查封关系
                    BdcLsBdcdyCfRel bdcLsBdcdyCfRel = new BdcLsBdcdyCfRel();
                    bdcLsBdcdyCfRel.setBdcdyid(bdcdyid);
                    bdcLsBdcdyCfRel.setCfid(bdcCf.getQlid());
                    bdcLsBdcdyCfRel.setBdclx(bdclx);
                    entityMapper.insertSelective(bdcLsBdcdyCfRel);
                } else {
                    //当前项目所产生的查封或异议信息
                    //产权与查封的关系
                    Qllx xsql = getBdcXsql(bdcdyid);
                    if(xsql!=null){
                        BdcLsQlCfRel bdcLsQlCfRel = new BdcLsQlCfRel();
                        bdcLsQlCfRel.setQlid(xsql.getQlid());
                        bdcLsQlCfRel.setBdclx(bdclx);
                        bdcLsQlCfRel.setCfid(bdcCf.getQlid());
                        entityMapper.insertSelective(bdcLsQlCfRel);
                    }else{
                        List<String> gdQlidList = getGdXsql(bdcdyh);
                        if(CollectionUtils.isNotEmpty(gdQlidList)){
                            for(String gdQlid:gdQlidList){
                                BdcLsQlCfRel bdcLsQlCfRel = new BdcLsQlCfRel();
                                bdcLsQlCfRel.setQlid(gdQlid);
                                bdcLsQlCfRel.setBdclx(bdclx);
                                bdcLsQlCfRel.setCfid(bdcCf.getQlid());
                                entityMapper.insertSelective(bdcLsQlCfRel);
                            }
                        }
                    }
                    //查封变化关系
                    List<BdcLsCfBh> bdcLsCfBhList = getBdcLsCfBh(bdcXm,bdcXmRel,bdcCf);
                    if(CollectionUtils.isNotEmpty(bdcLsCfBhList)) {
                        entityMapper.insertBatchSelective(bdcLsCfBhList);
                    }
                }
            }
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcXm
     * @return
     * @description 对当前项目是生成的查封进行删除相关历史信息
     */
    private void deleteProjectCf(BdcXm bdcXm){
        String bdcdyid = bdcXm.getBdcdyid();
        Example example = new Example(BdcCf.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
        List<BdcCf> bdcCfList = entityMapper.selectByExampleNotNull(example);
        if (CollectionUtils.isNotEmpty(bdcCfList)) {
            //当前项目产生的权利是查封
            for (BdcCf bdcCf : bdcCfList) {
                if (Constants.CFLX_ZD_YCF.equals(bdcCf.getCflx())) {
                    //如果是预查封，则不动产单元与查封关系
                    example = new Example(BdcLsBdcdyCfRel.class);
                    example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE,bdcdyid).andEqualTo("cfid",bdcCf.getQlid());
                    entityMapper.deleteByExampleNotNull(example);
                } else {
                    //当前项目所产生的查封或异议信息
                    //产权与查封的关系
                    example = new Example(BdcLsQlCfRel.class);
                    example.createCriteria().andEqualTo("cfid",bdcCf.getQlid());
                    entityMapper.deleteByExampleNotNull(example);
                    //查封变化关系
                    example = new Example(BdcLsCfBh.class);
                    example.createCriteria().andEqualTo("cfid",bdcCf.getQlid());
                    entityMapper.deleteByExampleNotNull(example);
                }
            }
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcdyh 不动产单元号
     * @return
     * @description 根据不动产单元号获取房屋坐落和房屋编号
     */
    private Map getFwzlAndFwbh(String bdcdyh){
        Map map = Maps.newHashMap();
        map.put("fwbh","");
        map.put("zl","");
        //实测户室
        DjsjFwHs djsjFwHs = getDjsjFwhsByBdcdyh(bdcdyh);
        if(djsjFwHs!=null) {
            map.put("fwbh",djsjFwHs.getFwbm());
            map.put("zl",djsjFwHs.getZl());
        }
        //预测户室
        DjsjFwYcHs djsjFwYcHs = getDjsjFwYchsByBdcdyh(bdcdyh);
        if(djsjFwYcHs!=null) {
            map.put("fwbh",djsjFwYcHs.getFwbm());
            map.put("zl",djsjFwYcHs.getZl());
        }

        //项目内多幢
        DjsjFwXmxx djsjFwXmxx = getDjsjFwXmxxByBdcdyh(bdcdyh);
        if(djsjFwXmxx!=null) {
            map.put("zl",djsjFwXmxx.getZl());
        }
        return map;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcdyh 不动产单元号
     * @return DjsjFwHs 房屋户室
     * @description
     */
    private DjsjFwHs getDjsjFwhsByBdcdyh(String bdcdyh){
        Example example = new Example(DjsjFwHs.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
        List<DjsjFwHs> djsjFwHsList = entityMapper.selectByExampleNotNull(example);
        return CollectionUtils.isNotEmpty(djsjFwHsList)?djsjFwHsList.get(0):null;
    }
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcdyh 不动产单元号
     * @return DjsjFwHs 房屋预测户室
     * @description
     */
    private DjsjFwYcHs getDjsjFwYchsByBdcdyh(String bdcdyh){
        Example example = new Example(DjsjFwYcHs.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
        List<DjsjFwYcHs> djsjFwYcHsList = entityMapper.selectByExampleNotNull(example);
        return CollectionUtils.isNotEmpty(djsjFwYcHsList)?djsjFwYcHsList.get(0):null;
    }
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcdyh 不动产单元号
     * @return DjsjFwHs 项目内多幢信息
     * @description
     */
    private DjsjFwXmxx getDjsjFwXmxxByBdcdyh(String bdcdyh){
        Example example = new Example(DjsjFwXmxx.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
        List<DjsjFwXmxx> djsjFwXmxxList = entityMapper.selectByExampleNotNull(example);
        return CollectionUtils.isNotEmpty(djsjFwXmxxList)?djsjFwXmxxList.get(0):null;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param qllx 权利类型
     * @return
     * @description 根据权利类型判断是否为产权
     */
    private boolean sfcq(String proid,String qllx){
        if(Constants.QLLX_DYAQ.equals(qllx)||Constants.QLLX_YYDJ.equals(qllx)||Constants.QLLX_CFDJ.equals(qllx)) {
            return false;
        }else if(Constants.QLLX_YGDJ.equals(qllx)){
            BdcYg bdcYg = bdcYgService.getBdcYgByProid(proid);
            if(Constants.YGDJZL_YGSPF.equals(bdcYg.getYgdjzl())||Constants.YGDJZL_QTBDCMMYG.equals(bdcYg.getYgdjzl())) {
                return true;
            } else {
                return false;
            }
        }else {
            return true;
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcProid 当前不动产项目ID
     * @param bdcQllx 当前不动产权利类型
     * @param yProid 上一手不动产项目ID
     * @return
     * @description 当前不动产项目为产权项目，其上一手产权来源于不动产数据，返回产权变化对象
     */
    private BdcLsQlBh getBdcLsQlBhByBdc(String bdcProid,String bdcQllx,String yProid){
        BdcLsQlBh bdcLsQlBh = getBdcLsQlBhByBdc(bdcProid,bdcQllx);
        if(StringUtils.isNotBlank(yProid)) {
            BdcXm yBdcXm = bdcXmService.getBdcXmByProid(yProid);
            final String yBdcQllx = yBdcXm.getQllx();
            BdcLsQlBh yBdcLsQlbh = getBdcLsQlBhByBdc(yProid, yBdcQllx);
            bdcLsQlBh.setYcqzh(yBdcLsQlbh.getCqzh());
            bdcLsQlBh.setYqlid(yBdcLsQlbh.getQlid());
            bdcLsQlBh.setYqlr(yBdcLsQlbh.getQlr());
            bdcLsQlBh.setYqlrzjh(yBdcLsQlbh.getQlrzjh());
            bdcLsQlBh.setYzl(yBdcLsQlbh.getZl());
        }
        return  bdcLsQlBh;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcProid 当前不动产项目ID
     * @param bdcQllx 当前不动产权利类型
     * @param yQlid 上一手过渡权利id
     * @return
     * @description 当前不动产项目为产权项目，其上一手产权来源于过渡数据，返回产权变化对象
     */
    private BdcLsQlBh getBdcLsQlBhByGd(String bdcProid,String bdcQllx,String yQlid){
        BdcLsQlBh bdcLsQlBh = getBdcLsQlBhByBdc(bdcProid,bdcQllx);
        if(StringUtils.isNotBlank(yQlid)) {
            bdcLsQlBh.setYqlid(yQlid);
            Map<String,String> gdQlr = getGdQlr(yQlid);
            bdcLsQlBh.setYqlr(gdQlr.get("qlr"));
            bdcLsQlBh.setYqlrzjh(gdQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
            GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, yQlid);
            if (gdTdsyq != null) {
                bdcLsQlBh.setYcqzh(gdTdsyq.getTdzh());
                List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(yQlid);
                List tdzlList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(gdTdList)) {
                    for (GdTd gdTd : gdTdList) {
                        if (StringUtils.isNotBlank(gdTd.getZl())) {
                            tdzlList.add(gdTd.getZl());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(tdzlList)) {
                    bdcLsQlBh.setYzl(StringUtils.join(tdzlList, ","));
                }

                return bdcLsQlBh;
            }
            GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, yQlid);
            if (gdFwsyq != null) {
                bdcLsQlBh.setYcqzh(gdFwsyq.getFczh());
                List<GdFw> gdFwList = gdFwService.getGdFwByQlid(yQlid);
                List fwzlList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(gdFwList)) {
                    for (GdFw gdFw : gdFwList) {
                        if (StringUtils.isNotBlank(gdFw.getFwzl())) {
                            fwzlList.add(gdFw.getFwzl());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(fwzlList)) {
                    bdcLsQlBh.setYzl(StringUtils.join(fwzlList, ","));
                }
                return bdcLsQlBh;
            }
        }
        return bdcLsQlBh;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcProid 不动产项目ID
     * @param bdcQllx 不动产项目权利类型
     * @return
     * @description 根据当前不动产项目生产BdcLsQlBh对象
     */

    private BdcLsQlBh getBdcLsQlBhByBdc(String bdcProid,String bdcQllx){
        Example example;
        BdcLsQlBh bdcLsQlBh = new BdcLsQlBh();
        if(Constants.QLLX_JTTD_SUQ.equals(bdcQllx)||Constants.QLLX_GYTD_SUQ.equals(bdcQllx)){
            //集体土地所有权和国有土地所有权
            example = new Example(BdcTdsyq.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE,bdcProid);
            List<BdcTdsyq> bdcTdsyqList = entityMapper.selectByExampleNotNull(example);
            if(CollectionUtils.isNotEmpty(bdcTdsyqList)) {
                bdcLsQlBh.setQlid(bdcTdsyqList.get(0).getQlid());
                bdcLsQlBh.setDbsj(bdcTdsyqList.get(0).getDjsj());
            }
        }else if(Constants.QLLX_GYTD_JSYDSYQ.equals(bdcQllx)||Constants.QLLX_ZJD_SYQ.equals(bdcQllx)){
            //国有建设用地使用权、宅基地使用权
            example = new Example(BdcJsydzjdsyq.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE,bdcProid);
            List<BdcJsydzjdsyq> bdcJsydzjdsyqList = entityMapper.selectByExampleNotNull(example);
            if(CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                bdcLsQlBh.setQlid(bdcJsydzjdsyqList.get(0).getQlid());
                bdcLsQlBh.setDbsj(bdcJsydzjdsyqList.get(0).getDjsj());
            }
        }else if(Constants.QLLX_GYTD_FWSUQ.equals(bdcQllx)){
            //国有建设用地使用权/房屋所有权
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcProid);
            if("1".equals(bdcBdcdy.getBdcdyfwlx())){
                //项目内多幢
                BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcProid);
                if(bdcFdcqDz!=null) {
                    bdcLsQlBh.setQlid(bdcFdcqDz.getQlid());
                    bdcLsQlBh.setDbsj(bdcFdcqDz.getDjsj());
                }
            }else{
                //独幢、层、套或间
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcProid);
                if(CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    bdcLsQlBh.setQlid(bdcFdcqList.get(0).getQlid());
                    bdcLsQlBh.setDbsj(bdcFdcqList.get(0).getDjsj());
                }
            }
        }else if(Constants.QLLX_YGDJ.equals(bdcQllx)){
            BdcYg bdcYg = bdcYgService.getBdcYgByProid(bdcProid);
            if(bdcYg!=null) {
                bdcLsQlBh.setQlid(bdcYg.getQlid());
                bdcLsQlBh.setDbsj(bdcYg.getDjsj());
            }
        }
        bdcLsQlBh.setCqzh(getBdcqzh(bdcProid));
        Map<String,String> bdcQlr = getBdcQlr(bdcProid);
        bdcLsQlBh.setYqlr(bdcQlr.get("qlr"));
        bdcLsQlBh.setYqlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcProid);
        bdcLsQlBh.setZl(bdcSpxx.getZl());
        bdcLsQlBh.setQllx(bdcQllx);
        bdcLsQlBh.setBdcdyh(bdcSpxx.getBdcdyh());
        bdcLsQlBh.setBdclx(bdcSpxx.getBdclx());
        bdcLsQlBh.setQszt("1");
        return bdcLsQlBh;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcdyId 不动产单元ID
     * @return
     * @description 根据不动产单元ID获取不动产现势产权信息
     */
    private Qllx getBdcXsql(String bdcdyId){
        Qllx qllx = null;
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyId);
        List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(bdcBdcdy.getBdcdyh(),"1");
        if(CollectionUtils.isNotEmpty(bdcYgList)) {
            for(BdcYg bdcYg:bdcYgList) {
                if("1".equals(bdcYg.getYgdjzl())||"2".equals(bdcYg.getYgdjzl())) {
                    return bdcYgList.get(0);
                }
            }
        }
        Example example;
        if(Constants.BDCLX_TD.equals(bdcBdcdy.getBdclx())){
            example = new Example(BdcJsydzjdsyq.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE,bdcdyId);
            example.createCriteria().andEqualTo("qszt","1");
            List<BdcJsydzjdsyq> bdcJsydzjdsyqList = entityMapper.selectByExampleNotNull(example);
            if(CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                return bdcJsydzjdsyqList.get(0);
            }
            example = new Example(BdcTdsyq.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE,bdcdyId);
            example.createCriteria().andEqualTo("qszt","1");
            List<BdcTdsyq> bdcTdsyqList = entityMapper.selectByExampleNotNull(example);
            if(CollectionUtils.isNotEmpty(bdcTdsyqList)) {
                return bdcTdsyqList.get(0);
            }
        }else if(Constants.BDCLX_TDFW.equals(bdcBdcdy.getBdclx())){
            if(bdcBdcdy.getBdcdyfwlx().equals("1")){
                //项目内多幢
                example = new Example(BdcFdcqDz.class);
                example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE,bdcdyId);
                example.createCriteria().andEqualTo("qszt","1");
                List<BdcFdcqDz> bdcFdcqDzList = entityMapper.selectByExampleNotNull(example);
                if(CollectionUtils.isNotEmpty(bdcFdcqDzList)) {
                    return bdcFdcqDzList.get(0);
                }
            }else{
                example = new Example(BdcFdcq.class);
                example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE,bdcdyId);
                example.createCriteria().andEqualTo("qszt","1");
                List<BdcFdcq> bdcFdcqList = entityMapper.selectByExampleNotNull(example);
                if(CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    return bdcFdcqList.get(0);
                }
            }
        }
        return qllx;
    }
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcdyh 不动产单元号
     * @return
     * @description 根据不动产单元号获取过渡库中的现势产权信息
     */
    private List<String> getGdXsql(String bdcdyh){
        List<String> result = Lists.newArrayList();
        Example example;
        example = new Example(GdDyhRel.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
        List<GdDyhRel> gdDyhRelList = entityMapper.selectByExampleNotNull(example);
        if(CollectionUtils.isNotEmpty(gdDyhRelList)){
            for(GdDyhRel gdDyhRel:gdDyhRelList){
                String gdid = gdDyhRel.getGdid();
                String tdid = gdDyhRel.getTdid();
                //查找是否存在预告（包括土地预告和房屋预告）
                List<GdYg> gdYgList = gdYgService.queryGdYgByBdcId(gdid);
                if(CollectionUtils.isNotEmpty(gdYgList)) {
                    GdYg gdYg = gdYgList.get(0);
                    if (gdYg.getIszx() == null || gdYg.getIszx().equals(0)) {
                        result.add(gdYg.getYgid());
                        return result;
                    }
                }
                if(StringUtils.isBlank(tdid)) {
                    //纯土地业务
                    List<GdTdsyq> gdTdsyqList =  gdTdService.queryTdsyqByTdid(gdid);
                    if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                        for(GdTdsyq gdTdsyq:gdTdsyqList){
                            if(gdTdsyq.getIszx()==null||gdTdsyq.getIszx().equals(0)) {
                                result.add(gdTdsyq.getQlid());
                            }
                        }
                    }
                }else{
                    List<GdTdsyq> gdTdsyqList =  gdTdService.queryTdsyqByTdid(tdid);
                    if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                        for(GdTdsyq gdTdsyq:gdTdsyqList){
                            if(gdTdsyq.getIszx()==null||gdTdsyq.getIszx().equals(0)) {
                                result.add(gdTdsyq.getQlid());
                            }
                        }
                    }

                    List<GdFwsyq> gdFwsyqList =  gdFwService.queryFwsyqByFwid(gdid);
                    if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                        for(GdFwsyq gdFwsyq:gdFwsyqList){
                            if(gdFwsyq.getIszx()==null||gdFwsyq.getIszx().equals(0)) {
                                result.add(gdFwsyq.getQlid());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 获取不动产抵押权利变化情况
     */
    private List<BdcLsDyBh> getBdcLsDyBh(BdcXm bdcXm,BdcXmRel bdcXmRel,BdcDyaq bdcDyaq){
        List<BdcLsDyBh> bdcLsDyBhList = Lists.newArrayList();
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        BdcLsDyBh bdcLsDyBh;
        if(Constants.DJLX_CSDJ_DM.equals(bdcXm.getDydjlx())){
            bdcLsDyBh = new BdcLsDyBh();
            bdcLsDyBh.setDyid(bdcDyaq.getQlid());
            Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
            bdcLsDyBh.setQlr(bdcQlr.get("qlr"));
            bdcLsDyBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
            bdcLsDyBh.setCqzh(getBdcqzh(bdcXm.getProid()));
            bdcLsDyBh.setDbsj(bdcDyaq.getDjsj());
            bdcLsDyBh.setBdclx(bdcXm.getBdclx());
            bdcLsDyBh.setZl(bdcSpxx.getZl());
            bdcLsDyBhList.add(bdcLsDyBh);
        } else if(Constants.DJLX_BGDJ_DM.equals(bdcXm.getDydjlx())||Constants.DJLX_ZYDJ_DM.equals(bdcXm.getDydjlx())){
            String[] yproids;
            if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                yproids = StringUtils.split(bdcXmRel.getYproid(), ",");
            } else {
                yproids = new String[1];
            }
            String[] yqlids;
            if(StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                yqlids = StringUtils.split(bdcXmRel.getYqlid(), ",");
            }else {
                yqlids = new String[1];
            }

            if("1".equals(bdcXmRel.getYdjxmly())) {
                for(int i=0;i<yproids.length;i++){
                    bdcLsDyBh = getBdcLsDyBhByYProid(yproids[i]);
                    bdcLsDyBh.setDyid(bdcDyaq.getQlid());
                    Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
                    bdcLsDyBh.setQlr(bdcQlr.get("qlr"));
                    bdcLsDyBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
                    bdcLsDyBh.setCqzh(getBdcqzh(bdcXm.getProid()));
                    bdcLsDyBh.setDbsj(bdcDyaq.getDjsj());
                    bdcLsDyBh.setBdclx(bdcXm.getBdclx());
                    bdcLsDyBh.setZl(bdcSpxx.getZl());
                    bdcLsDyBhList.add(bdcLsDyBh);
                }
            }
            else {
                for(int i=0;i<yqlids.length;i++){
                    bdcLsDyBh = getBdcLsDyBhByGdYQlid(yqlids[i]);
                    bdcLsDyBh.setDyid(bdcDyaq.getQlid());
                    Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
                    bdcLsDyBh.setQlr(bdcQlr.get("qlr"));
                    bdcLsDyBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
                    bdcLsDyBh.setCqzh(getBdcqzh(bdcXm.getProid()));
                    bdcLsDyBh.setDbsj(bdcDyaq.getDjsj());
                    bdcLsDyBh.setBdclx(bdcXm.getBdclx());
                    bdcLsDyBh.setZl(bdcSpxx.getZl());
                    bdcLsDyBhList.add(bdcLsDyBh);
                }
            }
        }else if(Constants.DJLX_ZXDJ_DM.equals(bdcXm.getDydjlx())){
            return bdcLsDyBhList;
        }

        return bdcLsDyBhList;
    }

    private List<BdcLsDyBh> getBdcLsDyBhByYg(BdcXm bdcXm,BdcXmRel bdcXmRel,BdcYg bdcYg){
        List<BdcLsDyBh> bdcLsDyBhList = Lists.newArrayList();
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        BdcLsDyBh bdcLsDyBh;
        if(Constants.DJLX_CSDJ_DM.equals(bdcXm.getDjlx())){
            bdcLsDyBh = new BdcLsDyBh();
            bdcLsDyBh.setDyid(bdcYg.getQlid());
            Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
            bdcLsDyBh.setQlr(bdcQlr.get("qlr"));
            bdcLsDyBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
            bdcLsDyBh.setCqzh(getBdcqzh(bdcXm.getProid()));
            bdcLsDyBh.setDbsj(bdcYg.getDjsj());
            bdcLsDyBh.setBdclx(bdcXm.getBdclx());
            bdcLsDyBh.setZl(bdcSpxx.getZl());
            bdcLsDyBhList.add(bdcLsDyBh);
        } else if(Constants.DJLX_BGDJ_DM.equals(bdcXm.getDjlx())||Constants.DJLX_ZYDJ_DM.equals(bdcXm.getDjlx())){
            String[] yproids;
            if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                yproids = StringUtils.split(bdcXmRel.getYproid(), ",");
            }else {
                yproids = new String[1];
            }
            String[] yqlids;
            if(StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                yqlids = StringUtils.split(bdcXmRel.getYqlid(), ",");
            }else {
                yqlids = new String[1];
            }

            if("1".equals(bdcXmRel.getYdjxmly())) {
                for(int i=0;i<yproids.length;i++){
                    bdcLsDyBh = getBdcLsDyBhYgByYProid(yproids[i]);
                    bdcLsDyBh.setDyid(bdcYg.getQlid());
                    Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
                    bdcLsDyBh.setQlr(bdcQlr.get("qlr"));
                    bdcLsDyBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
                    bdcLsDyBh.setCqzh(getBdcqzh(bdcXm.getProid()));
                    bdcLsDyBh.setDbsj(bdcYg.getDjsj());
                    bdcLsDyBh.setBdclx(bdcXm.getBdclx());
                    bdcLsDyBh.setZl(bdcSpxx.getZl());
                    bdcLsDyBhList.add(bdcLsDyBh);
                }
            }
            else {
                for(int i=0;i<yqlids.length;i++){
                    bdcLsDyBh = getBdcLsDyBhYgByGdYQlid(yqlids[i]);
                    bdcLsDyBh.setDyid(bdcYg.getQlid());
                    Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
                    bdcLsDyBh.setQlr(bdcQlr.get("qlr"));
                    bdcLsDyBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
                    bdcLsDyBh.setCqzh(getBdcqzh(bdcXm.getProid()));
                    bdcLsDyBh.setDbsj(bdcYg.getDjsj());
                    bdcLsDyBh.setBdclx(bdcXm.getBdclx());
                    bdcLsDyBh.setZl(bdcSpxx.getZl());
                    bdcLsDyBhList.add(bdcLsDyBh);
                }
            }
        }else if(Constants.DJLX_ZXDJ_DM.equals(bdcXm.getDjlx())){
            return bdcLsDyBhList;
        }
        return bdcLsDyBhList;
    }


    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 获取不动产项目权利人，以逗号相隔
     */
    private Map<String,String> getBdcQlr(String proid){
        Map result = Maps.newHashMap();
        result.put("qlr","");
        result.put(ParamsConstants.QLRZJH_LOWERCASE,"");
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
        if(CollectionUtils.isNotEmpty(bdcQlrList)){
            List qlrList = Lists.newArrayList();
            List qlrzjhList = Lists.newArrayList();
            for (BdcQlr bdcQlr : bdcQlrList) {
                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                    qlrList.add(bdcQlr.getQlrmc());
                }
                if (StringUtils.isNotBlank(bdcQlr.getQlrzjh())) {
                    qlrzjhList.add(bdcQlr.getQlrzjh());
                }
            }
            if (CollectionUtils.isNotEmpty(qlrList)) {
                result.put("qlr", StringUtils.join(qlrList, ","));
            }
            if (CollectionUtils.isNotEmpty(qlrzjhList)) {
                result.put(ParamsConstants.QLRZJH_LOWERCASE, StringUtils.join(qlrzjhList, ","));
            }
        }
        return result;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 获取不动产查封历史变化关系
     */
    private List<BdcLsCfBh> getBdcLsCfBh(BdcXm bdcXm,BdcXmRel bdcXmRel,BdcCf bdcCf){
        List<BdcLsCfBh> bdcLsCfBhList = Lists.newArrayList();
        BdcLsCfBh bdcLsCfBh;
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        final String djzx = bdcXm.getDjzx();
        if(Constants.DJZX_CF.equals(djzx)||Constants.DJZX_LH.equals(djzx)){
            //查封
            bdcLsCfBh = new BdcLsCfBh();
            bdcLsCfBh.setCfid(bdcCf.getQlid());
            bdcLsCfBh.setCfwh(bdcCf.getCfwh());
            Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
            bdcLsCfBh.setQlr(bdcQlr.get("qlr"));
            bdcLsCfBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
            bdcLsCfBh.setZl(bdcSpxx.getZl());
            bdcLsCfBh.setDbsj(bdcCf.getDjsj());
            bdcLsCfBh.setBdclx(bdcXm.getBdclx());
            bdcLsCfBhList.add(bdcLsCfBh);
        }else if(Constants.DJZX_XF.equals(djzx)){
            String[] yproids;
            if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                yproids = StringUtils.split(bdcXmRel.getYproid(), ",");
            }else {
                yproids = new String[1];
            }
            String[] yqlids;
            if(StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                yqlids = StringUtils.split(bdcXmRel.getYqlid(), ",");
            }else {
                yqlids = new String[1];
            }

            if("1".equals(bdcXmRel.getYdjxmly())) {
                for(int i=0;i<yproids.length;i++) {
                    bdcLsCfBh = getBdcLsCfBhByYProid(yproids[i]);
                    bdcLsCfBh.setCfid(bdcCf.getQlid());
                    bdcLsCfBh.setCfwh(bdcCf.getCfwh());
                    Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
                    bdcLsCfBh.setQlr(bdcQlr.get("qlr"));
                    bdcLsCfBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
                    bdcLsCfBh.setZl(bdcSpxx.getZl());
                    bdcLsCfBh.setDbsj(bdcCf.getDjsj());
                    bdcLsCfBh.setBdclx(bdcXm.getBdclx());
                    bdcLsCfBhList.add(bdcLsCfBh);
                }
            } else {
                for(int i=0;i<yqlids.length;i++) {
                    bdcLsCfBh = getBdcLsCfBhByGdYQlid(yqlids[i]);
                    bdcLsCfBh.setCfid(bdcCf.getQlid());
                    bdcLsCfBh.setCfwh(bdcCf.getCfwh());
                    Map<String,String> bdcQlr = getBdcQlr(bdcXm.getProid());
                    bdcLsCfBh.setQlr(bdcQlr.get("qlr"));
                    bdcLsCfBh.setQlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
                    bdcLsCfBh.setZl(bdcSpxx.getZl());
                    bdcLsCfBh.setDbsj(bdcCf.getDjsj());
                    bdcLsCfBh.setBdclx(bdcXm.getBdclx());
                    bdcLsCfBhList.add(bdcLsCfBh);
                }
            }
            //续封
        }else if(Constants.DJZX_JF.equals(djzx)){
            //解封
        }

        return bdcLsCfBhList;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据原项目ID获取不动产查封历史变化
     */
    private BdcLsCfBh getBdcLsCfBhByYProid(String yProid){
        BdcLsCfBh bdcLsCfBh = new BdcLsCfBh();
        if(StringUtils.isBlank(yProid)) return bdcLsCfBh;
        BdcCf bdcCf = bdcCfService.selectCfByProid(yProid);
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(yProid);
        if(bdcCf!=null){
            bdcLsCfBh.setYcfid(bdcCf.getQlid());
            bdcLsCfBh.setYcfwh(bdcCf.getCfwh());
        }
        if(bdcSpxx!=null) {
            bdcLsCfBh.setYzl(bdcSpxx.getZl());
        }
        Map<String,String> bdcQlr = getBdcQlr(yProid);
        bdcLsCfBh.setYqlr(bdcQlr.get("qlr"));
        bdcLsCfBh.setYqlrzjh(bdcQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
        bdcLsCfBh.setYzl(getBdcZl(yProid));
        return bdcLsCfBh;
    }
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据原权利ID获取过渡查封历史变化
     */
    private BdcLsCfBh getBdcLsCfBhByGdYQlid(String yQlid){
        BdcLsCfBh bdcLsCfBh = new BdcLsCfBh();
        if(StringUtils.isBlank(yQlid)) return bdcLsCfBh;
        GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class,yQlid);
        if(gdCf!=null){
            bdcLsCfBh.setYcfid(gdCf.getCfid());
            bdcLsCfBh.setYcfwh(gdCf.getCfwh());
            bdcLsCfBh.setYzl(getGdZl(yQlid,gdCf.getBdclx()));
        }
        Map<String,String> gdQlr = getGdQlr(yQlid);
        bdcLsCfBh.setYqlr(gdQlr.get("qlr"));
        bdcLsCfBh.setYqlrzjh(gdQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
        return bdcLsCfBh;
    }
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据原项目ID获取不动产抵押历史变化
     */
    private BdcLsDyBh getBdcLsDyBhByYProid(String yProid){
        BdcLsDyBh bdcLsDyBh = new BdcLsDyBh();
        if(StringUtils.isBlank(yProid)) return bdcLsDyBh;
        Example example = new Example(BdcDyaq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE,yProid);
        List<BdcDyaq> bdcDyaqList = entityMapper.selectByExampleNotNull(example);
        if(CollectionUtils.isNotEmpty(bdcDyaqList)){
            bdcLsDyBh.setYdyid(bdcDyaqList.get(0).getQlid());
        }
        Map<String,String> gdQlr = getBdcQlr(yProid);
        bdcLsDyBh.setYqlr(gdQlr.get("qlr"));
        bdcLsDyBh.setYqlrzjh(gdQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
        bdcLsDyBh.setYcqzh(getBdcqzh(yProid));
        bdcLsDyBh.setYzl(getBdcZl(yProid));
        return bdcLsDyBh;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据原权利ID获取不动产预告抵押历史变化
     */
    private BdcLsDyBh getBdcLsDyBhYgByYProid(String yProid){
        BdcLsDyBh bdcLsDyBh = new BdcLsDyBh();
        if(StringUtils.isBlank(yProid)) return bdcLsDyBh;
        Example example = new Example(BdcYg.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE,yProid);
        List<BdcYg> bdcYgList = entityMapper.selectByExampleNotNull(example);
        if(CollectionUtils.isNotEmpty(bdcYgList)){
            bdcLsDyBh.setYdyid(bdcYgList.get(0).getQlid());
        }
        Map<String,String> gdQlr = getBdcQlr(yProid);
        bdcLsDyBh.setYqlr(gdQlr.get("qlr"));
        bdcLsDyBh.setYqlrzjh(gdQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
        bdcLsDyBh.setYcqzh(getBdcqzh(yProid));
        bdcLsDyBh.setYzl(getBdcZl(yProid));
        return bdcLsDyBh;
    }
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据原权利ID获取过渡抵押历史变化
     */
    private BdcLsDyBh getBdcLsDyBhByGdYQlid(String yQlid){
        BdcLsDyBh bdcLsDyBh = new BdcLsDyBh();
        if(StringUtils.isBlank(yQlid)) return bdcLsDyBh;
        GdDy gdDy = entityMapper.selectByPrimaryKey(GdDy.class,yQlid);
        if(gdDy!=null){
            bdcLsDyBh.setYdyid(gdDy.getDyid());
            bdcLsDyBh.setYcqzh(gdDy.getDydjzmh());
            bdcLsDyBh.setYzl(getGdZl(yQlid,gdDy.getBdclx()));
        }
        Map<String,String> gdQlr = getGdQlr(yQlid);
        bdcLsDyBh.setYqlr(gdQlr.get("qlr"));
        bdcLsDyBh.setYqlrzjh(gdQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
        return bdcLsDyBh;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据原权利ID获取过渡预告抵押历史变化
     */
    private BdcLsDyBh getBdcLsDyBhYgByGdYQlid(String yQlid){
        BdcLsDyBh bdcLsDyBh = new BdcLsDyBh();
        if(StringUtils.isBlank(yQlid)) return bdcLsDyBh;
        GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class,yQlid);
        if(gdYg!=null){
            bdcLsDyBh.setYdyid(gdYg.getYgid());
            bdcLsDyBh.setYcqzh(gdYg.getYgdjzmh());
            bdcLsDyBh.setYzl(getGdZl(yQlid,gdYg.getBdclx()));
        }
        Map<String,String> gdQlr = getGdQlr(yQlid);
        bdcLsDyBh.setYqlr(gdQlr.get("qlr"));
        bdcLsDyBh.setYqlrzjh(gdQlr.get(ParamsConstants.QLRZJH_LOWERCASE));
        return bdcLsDyBh;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据过渡权利人信息，以逗号隔开
     */
    private Map<String,String> getGdQlr(String qlid){
        Map result = Maps.newHashMap();
        result.put("qlr","");
        result.put(ParamsConstants.QLRZJH_LOWERCASE,"");
        Example example = new Example(GdQlr.class);
        example.createCriteria().andEqualTo("qlid",qlid);
        example.createCriteria().andEqualTo("qlrlx","qlr");
        List<GdQlr> gdQlrList = entityMapper.selectByExampleNotNull(example);
        if(CollectionUtils.isNotEmpty(gdQlrList)) {
            List yqlrList = Lists.newArrayList();
            List yqlrzjhList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(gdQlrList)) {
                for (GdQlr gdQlr : gdQlrList) {
                    if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        yqlrList.add(gdQlr.getQlr());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrzjh())) {
                        yqlrzjhList.add(gdQlr.getQlrzjh());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(yqlrList)) {
                result.put("qlr", StringUtils.join(yqlrList, ","));
            }
            if (CollectionUtils.isNotEmpty(yqlrzjhList)) {
                result.put(ParamsConstants.QLRZJH_LOWERCASE, StringUtils.join(yqlrzjhList, ","));
            }
        }
        return result;
    }
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param gdQlid 过渡权利ID
     * @return
     * @description 根据过渡权利ID，获取过渡房屋或土地的坐落
     */

    private String getGdZl(String gdQlid,String bdclx){
        List<String> zlList = Lists.newArrayList();
        if(Constants.BDCLX_TD.equals(bdclx)){
            List<GdTd> gdTdList = gdTdService.getGdTdByQlid(gdQlid);
            if(CollectionUtils.isNotEmpty(gdTdList)){
                for(GdTd gdTd:gdTdList){
                    zlList.add(gdTd.getZl());
                }
            }
        }else if(Constants.BDCLX_TDFW.equals(bdclx)){
            List<GdFw> gdFwList = gdFwService.getGdFwByQlid(gdQlid);
            if(CollectionUtils.isNotEmpty(gdFwList)){
                for(GdFw gdFw:gdFwList){
                    zlList.add(gdFw.getFwzl());
                }
            }
        }
        return StringUtils.join(zlList,",");
    }


    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param
     * @return
     * @description 根据不动产项目ID生成产权证号，以逗号相隔
     */
    private String getBdcqzh(String proid){
        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
        if(CollectionUtils.isNotEmpty(bdcZsList)){
            List bdcqzhList = Lists.newArrayList();
            for(BdcZs bdcZs:bdcZsList){
                if(StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                    bdcqzhList.add(bdcZs.getBdcqzh());
                }
            }
            return StringUtils.join(bdcqzhList,",");
        }
        return null;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid
     * @return
     * @description 获取不动产项目坐落
     */
    private String getBdcZl(String proid){
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        return bdcSpxx!=null?bdcSpxx.getZl():"";
    }
}
