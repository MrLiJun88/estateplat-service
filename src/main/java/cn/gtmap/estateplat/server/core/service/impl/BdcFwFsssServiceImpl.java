package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcFwfsssMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcSpxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.utils.CalculationUtils;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2017-04-13
 * @description
 */
@Service
public class BdcFwFsssServiceImpl implements BdcFwFsssService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    ProjectService projectService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    BdcFwfsssMapper bdcFwfsssMapper;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcFdcqService bdcFdcqService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcZdGlService bdcZdGlService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private  BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcDyaqService bdcDyaqService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public BdcFwfsss intiFwfsssByFwid(String fwid) {
        return bdcFwfsssMapper.intiFwfsssByFwid(fwid);
    }

    /**
     * @param bdcFwfsss
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存房屋附属设施
     */
    @Override
    public void saveBdcFwfsss(BdcFwfsss bdcFwfsss) {
        entityMapper.saveOrUpdate(bdcFwfsss, bdcFwfsss.getFwfsssid());
    }

    /**
     * @param bdcFwfsss
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存房屋附属设施(Null字段也会更新)
     */
    @Override
    public void saveBdcFwfsssNull(BdcFwfsss bdcFwfsss) {
        entityMapper.updateByPrimaryKeyNull(bdcFwfsss);
    }

    @Override
    public void initBdcFwfsssByProject(Project project) {
        List<InsertVo> insertVoList = null;
        //获取哪个登记类型service
        CreatProjectService creatProjectService = projectService.getCreatProjectService(project,"true");
        //获取哪个登记类型service
        TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
        insertVoList = creatProjectService.initVoFromOldData(project);
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            creatProjectService.saveOrUpdateProjectData(insertVoList);
        }

        if(StringUtils.isBlank(project.getBdcdyid()) && StringUtils.isNotBlank(project.getBdcdyh())){
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
            if(bdcBdcdy != null)
                project.setBdcdyid(bdcBdcdy.getBdcdyid());
        }
        if(StringUtils.isBlank(project.getWiid()) && StringUtils.isNotBlank(project.getProid())){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
            if(bdcXm != null)
                project.setWiid(bdcXm.getWiid());
        }

        if(StringUtils.isNotBlank(project.getWiid()) && StringUtils.isNotBlank(project.getBdcdyid())){
            HashMap map = Maps.newHashMap();
            map.put("wiid",project.getWiid());
            map.put("bdcdyid",project.getBdcdyid());
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(map);
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm:bdcXmList)
                    turnProjectDefaultService.saveQllxVo(bdcXm);
            }
        }

    }

    @Override
    public BdcFwfsss getFwfsssByQjid(String qjid) {
        BdcFwfsss bdcFwfsss = new BdcFwfsss();
        DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(qjid);
        if (djsjFwxx != null) {
            bdcFwfsss.setBdcdyh(djsjFwxx.getBdcdyh());
            bdcFwfsss.setGhyt(djsjFwxx.getGhyt());
            if (djsjFwxx.getJyjg() != null && djsjFwxx.getJyjg() > 0) {
                bdcFwfsss.setJyjg(djsjFwxx.getJyjg());
            }
            if (djsjFwxx.getJzmj() != null && djsjFwxx.getJzmj() > 0) {
                bdcFwfsss.setJzmj(djsjFwxx.getJzmj());
            }
            if (djsjFwxx.getTnjzmj() != null && djsjFwxx.getTnjzmj() > 0) {
                bdcFwfsss.setTnjzmj(djsjFwxx.getTnjzmj());
            }
            if (djsjFwxx.getCg() != null && djsjFwxx.getCg() > 0) {
                bdcFwfsss.setCg(djsjFwxx.getCg());
            }
            bdcFwfsss.setFwzl(djsjFwxx.getZl());
            if (djsjFwxx.getFtjzmj() != null && djsjFwxx.getFtjzmj() > 0) {
                bdcFwfsss.setFtjzmj(djsjFwxx.getFtjzmj());
            }
            if (djsjFwxx.getFttdmj() != null && djsjFwxx.getFttdmj() > 0) {
                bdcFwfsss.setFttdmj(djsjFwxx.getFttdmj());
            }
            bdcFwfsss.setFwbm(djsjFwxx.getFcdah());
        }
        return bdcFwfsss;
    }

    @Override
    public BdcFwfsss getFwfsssByfwfssid(String fwfssid) {
        BdcFwfsss bdcFwfsss = null;
        if (StringUtils.isNotBlank(fwfssid)) {
            bdcFwfsss = entityMapper.selectByPrimaryKey(BdcFwfsss.class, fwfssid);
        }
        return bdcFwfsss;
    }

    @Override
    public void delBdcFwfsssByFwfsssid(final String fwfsssid) {
        if (StringUtils.isNotBlank(fwfsssid)) {
            Example example = new Example(BdcFwfsss.class);
            example.createCriteria().andEqualTo("fwfsssid", fwfsssid);
            entityMapper.deleteByExample(BdcFwfsss.class, example);
        }
    }

    @Override
    public List<BdcFwfsss> intiBdcFwfsssForZhs(final String zfbdcdyh, final BdcXm bdcXm) {
        List<BdcFwfsss> bdcFwfsssListTemp = new ArrayList<BdcFwfsss>();
        String fsssTbQjZhs = AppConfig.getProperty("fsss.same.qj.zhs");
        if (StringUtils.isBlank(fsssTbQjZhs) || StringUtils.equals(fsssTbQjZhs,"false")) {
            if (StringUtils.isNotBlank(zfbdcdyh)&&bdcXm != null) {
                List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
                BdcXmRel bdcXmRel = null;
                if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())){
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                        bdcXmRel = bdcXmRelList.get(0);
                    }
                }
                //jyl  没有原项目的就要子户室面积的就要加到主房上去。有原项目的，理论上原项目就加了，所以不用加
                // jyl flag 0：需要加；1：不需要加
                String flag = "0";
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRelTemp : bdcXmRelList) {
                        if (StringUtils.isNotBlank(bdcXmRelTemp.getYproid())&&!StringUtils.equals(bdcXmRel.getYdjxmly(), Constants.XMLY_TDSP)
                                &&!StringUtils.equals(bdcXmRel.getYdjxmly(), Constants.XMLY_FWSP)) {
                            flag = "1";
                            break;
                        }
                    }
                }
                if (StringUtils.equals(flag, "0")) {
                    List<BdcFwfsss> bdcFwfsssList = bdcFwfsssMapper.initBdcFwfsssFromZhsWithZhsFwbm(zfbdcdyh);
                    if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                        for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                            bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                            bdcFwfsss.setZfbdcdyh(zfbdcdyh);
                            bdcFwfsss.setWiid(bdcXm.getWiid());
                            bdcFwfsss.setProid(bdcXm.getProid());
                            saveBdcFwfsss(bdcFwfsss);
                        }
                        bdcFwfsssListTemp.addAll(bdcFwfsssList);
                        /**
                         *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                         *@description 子户室单独发过证的，将该证书添加到项目的原不动产权证号中去
                         */
                        if(lastSyq(bdcXm)){
                            addBdcqzhByZhs(bdcXm);
                        }
                    }
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                    if (bdcSpxx != null ) {
                        for(BdcFwfsss bdcFwfsss:bdcFwfsssList) {
                            Boolean needAddBdcFsssMj = needAddBdcFsssMj(bdcXmRel,bdcFwfsss,zfbdcdyh);
                            if(needAddBdcFsssMj && bdcFwfsss != null && bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() != 0){
                                if (bdcSpxx.getMj() != null) {
                                    bdcSpxx.setMj(bdcSpxx.getMj()+bdcFwfsss.getJzmj());
                                } else {
                                    bdcSpxx.setMj(bdcFwfsss.getJzmj());
                                }
                            }
                        }
                        bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    }
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        for (BdcFdcq bdcFdcq : bdcFdcqList) {
                            if (bdcFdcq != null) {
                                for(BdcFwfsss bdcFwfsss:bdcFwfsssList) {
                                    Boolean needAddBdcFsssMj = needAddBdcFsssMj(bdcXmRel,bdcFwfsss,zfbdcdyh);
                                    if(needAddBdcFsssMj){
                                        addBdcFwfsssMj(bdcXm.getWiid(),bdcFwfsss,bdcFdcq);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } else {
            //qijiadong 吴江需要附属设施和权籍库中是否关联子户室保持同步
            if (StringUtils.isNotBlank(zfbdcdyh) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                //qijiadong 在权籍库中不在项目内的子户室需要添加为房屋附属设施
                List<BdcFwfsss> bdcFwfsssList = bdcFwfsssMapper.initBdcFwfsssFromZhsWithZhsIndex(zfbdcdyh);
                List<BdcFwfsss> bdcFwfsssAddList = new ArrayList<BdcFwfsss>();
                if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                    for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                        if (StringUtils.isNotBlank(bdcFwfsss.getFwbm())) {
                            Map map = new HashMap();
                            map.put("fwbm",bdcFwfsss.getFwbm());
                            map.put("proid",bdcXm.getProid());
                            List<BdcFwfsss> bdcFwfsssTempList = bdcFwfsssMapper.getBdcFwfsssList(map);
                            if (CollectionUtils.isEmpty(bdcFwfsssTempList)) {
                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                bdcFwfsss.setZfbdcdyh(zfbdcdyh);
                                bdcFwfsss.setWiid(bdcXm.getWiid());
                                bdcFwfsss.setProid(bdcXm.getProid());
                                bdcFwfsssAddList.add(bdcFwfsss);
                                saveBdcFwfsss(bdcFwfsss);
                            }
                        }
                    }
                }
                bdcFwfsssListTemp.addAll(bdcFwfsssList);
                /**
                 *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                 *@description 子户室单独发过证的，将该证书添加到项目的原不动产权证号中去
                 */
                if(lastSyq(bdcXm)){
                    addBdcqzhByZhs(bdcXm);
                }
                if (CollectionUtils.isNotEmpty(bdcFwfsssAddList)) {
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        for (BdcFdcq bdcFdcq : bdcFdcqList) {
                            if (bdcFdcq != null) {
                                for (BdcFwfsss bdcFwfsss : bdcFwfsssAddList) {
                                    addBdcFwfsssMj(bdcXm.getWiid(),bdcFwfsss, bdcFdcq);
                                }
                            }
                            if(bdcSpxx != null&&bdcFdcq.getJzmj() != null&&bdcFdcq.getJzmj() > 0) {
                                bdcSpxx.setMj(bdcFdcq.getJzmj());
                                bdcSpxxService.saveBdcSpxx(bdcSpxx);
                            }

                        }
                    }

                    if(StringUtils.isNotBlank(bdcXm.getQllx())&&StringUtils.equals(bdcXm.getQllx(),Constants.QLLX_DYAQ)) {
                        BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(bdcXm.getProid());
                        if(bdcDyaq != null&&bdcSpxx != null&&bdcSpxx.getMj()>0){
                            bdcDyaq.setFwdymj(bdcSpxx.getMj());
                            bdcDyaqService.saveBdcDyaq(bdcDyaq);
                        }
                    }
                }
            }
        }
        return bdcFwfsssListTemp;
    }

    @Override
    public void initializeBdcFwfsss(String zfbdcdyh, BdcXm bdcXm) {
        if(StringUtils.isNotBlank(zfbdcdyh) && bdcXm != null){
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if(StringUtils.equals(bdcXm.getXmly(),Constants.XMLY_BDC)) {
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                    if(bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYdjxmly()) && StringUtils.equals(bdcXmRel.getYdjxmly(), Constants.XMLY_BDC)){
                        String yproid = bdcXmRel.getYproid();
                        if(StringUtils.isNotBlank(yproid)){
                            List<BdcFwfsss> bdcFwfsssList = getBdcFwfsssListByProidOnly(yproid);
                            if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                                for(BdcFwfsss bdcFwfsss:bdcFwfsssList){
                                    bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                    bdcFwfsss.setWiid(bdcXm.getWiid());
                                    bdcFwfsss.setProid(bdcXm.getProid());
                                    bdcFwfsss.setYproid(yproid);
                                    saveBdcFwfsss(bdcFwfsss);
                                }
                            }
                        }
                    }
                }
            }else{
                //jgz 一证多房情况下 自动带入附属设施
                autoCreatBdcFsss(bdcXm.getProid());
                //jgz 附属设施单独发过房产证，创建关系带入项目
                bdcXmRelService.creatBdcXmRelForFsssGdFwsyq(bdcXm);
            }
            intiBdcFwfsssForZhs(zfbdcdyh, bdcXm);
        }
    }

    @Override
    public List<BdcFwfsss> getBdcFwfsssListByProidOnly(String proid){
        List<BdcFwfsss> bdcFwFsssList = new ArrayList<BdcFwfsss>();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid()) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    HashMap fsssMap = new HashMap();
                    fsssMap.put("proid", proid);
                    fsssMap.put(ParamsConstants.ZFBDCDYH_LOWERCASE, bdcBdcdy.getBdcdyh());
                    bdcFwFsssList = getBdcFwfsssList(fsssMap);
                }
            }
        }
        return bdcFwFsssList;
    }



    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param bdcXm 当前项目
    * @return
    * @Description: 判断当前项目上一手是不是所有权/使用权
    */
    private boolean lastSyq(BdcXm bdcXm){
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())){
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                for (BdcXmRel bdcXmRel : bdcXmRelList){
                    if(StringUtils.isNotBlank(bdcXmRel.getYqlid())){
                        GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(bdcXmRel.getYqlid());
                        GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(bdcXmRel.getYqlid());
                        if(gdFwsyq != null || gdTdsyq != null){
                            return true;
                        }
                    }
                    if(StringUtils.isNotBlank(bdcXmRel.getYproid())){
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(ParamsConstants.PROID_LOWERCASE, bdcXmRel.getYproid());
                        map.put("qszt", String.valueOf(Constants.QLLX_QSZT_XS));
                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcq(map);
                        List<BdcFdcqDz> bdcFdcqDzList = bdcFdcqDzService.getBdcFdcqDzList(map);
                        if(CollectionUtils.isNotEmpty(bdcFdcqList) || CollectionUtils.isNotEmpty(bdcFdcqDzList)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void addBdcqzhByZhs(BdcXm bdcXm){
        if(bdcXm!=null){
            String zhsBdcqzh = getZhsBdcqzh(bdcXm);
            if(StringUtils.isNotBlank(zhsBdcqzh)){
                zhsBdcqzh = zhsBdcqzh.substring(0,zhsBdcqzh.length()-1);
                if(StringUtils.isNotBlank(bdcXm.getYbdcqzh())){
                    String[] yBdcqzhArr = bdcXm.getYbdcqzh().split(";");
                    if(yBdcqzhArr!=null&&yBdcqzhArr.length==1){
                        if(StringUtils.isNotBlank(yBdcqzhArr[0])&&yBdcqzhArr[0].contains("房产证号")){
                            bdcXm.setYbdcqzh(bdcXm.getYbdcqzh()+","+zhsBdcqzh);
                        }else{
                            bdcXm.setYbdcqzh("房产证号:"+zhsBdcqzh+";"+bdcXm.getYbdcqzh());
                        }
                    }else{
                        StringBuilder ybdcqzh = new StringBuilder();
                        if(yBdcqzhArr!=null){
                            for(int i=0;i<yBdcqzhArr.length;i++){
                                if(yBdcqzhArr[i].contains("房产证号")){
                                    ybdcqzh.append(yBdcqzhArr[i]).append(",").append(zhsBdcqzh).append(";");
                                }else{
                                    ybdcqzh.append(yBdcqzhArr[i]).append(";");
                                }
                            }
                        }
                        if(!ybdcqzh.toString().contains("房产证号")){
                            ybdcqzh.append(";房产证号:").append(zhsBdcqzh);
                        }
                        bdcXm.setYbdcqzh(String.valueOf(ybdcqzh));
                    }
                }else{
                    bdcXm.setYbdcqzh(zhsBdcqzh);
                }
                bdcXmService.saveBdcXm(bdcXm);
            }
        }
    }

    public String getZhsBdcqzh(BdcXm bdcXm){
        StringBuilder zhsBdcqzh = new StringBuilder();
        if(StringUtils.isNotBlank(bdcXm.getBdcdyid())&&StringUtils.isNotBlank(bdcXm.getProid())){
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if(bdcBdcdy!=null&&StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())&&CollectionUtils.isNotEmpty(bdcXmRelList)){
                /**
                 *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                 *@description 通过主房不动产单元号查询匹配关系，并排除主房的
                 */
                List<GdQlDyhRel> zhsGdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(bdcBdcdy.getBdcdyh(),"","");
                if(CollectionUtils.isNotEmpty(zhsGdQlDyhRelList)){
                    for(GdQlDyhRel gdQlDyhRel : zhsGdQlDyhRelList){
                        if(StringUtils.isNotBlank(gdQlDyhRel.getQlid())){
                            Boolean existRel = true;
                            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdQlDyhRel.getQlid());
                            List<GdFw> gdFwList = gdFwService.getGdFwByQlid(gdQlDyhRel.getQlid());
                            if(CollectionUtils.isNotEmpty(gdFwList)){
                                GdFw gdFw = gdFwList.get(0);
                                if(StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(),Constants.XZZT_SD)){
                                    HashMap map = new HashMap();
                                    map.put("yqlid",gdQlDyhRel.getQlid());
                                    map.put(ParamsConstants.PROID_LOWERCASE,bdcXm.getProid());
                                    List<BdcXmRel> bdcXmRelListTemp = andEqualQueryBdcXmRel(map);
                                    if(CollectionUtils.isEmpty(bdcXmRelListTemp)){
                                        existRel = false;
                                    }
                                }
                            }
                            if(!existRel && gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getFczh()) && gdFwsyq.getIszx() != null && Constants.GDQL_ISZX_WZX.equals(gdFwsyq.getIszx())){
                                zhsBdcqzh.append(gdFwsyq.getFczh()).append(",");
                            }
                        }
                    }
                }
            }
        }
        return zhsBdcqzh.toString();
    }

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

    @Override
    public List<BdcFwfsss> getBdcFwfsssList(Map map) {
        return bdcFwfsssMapper.getBdcFwfsssList(map);
    }

    @Override
    public List<BdcXm> getFsssBdcXmList(List<BdcFwfsss> bdcFwFsssList) {
        List<BdcXm> bdcXmList = new ArrayList<BdcXm>();
        if (CollectionUtils.isNotEmpty(bdcFwFsssList)) {
            for (BdcFwfsss bdcFwfsss : bdcFwFsssList) {
                if (bdcFwfsss != null && StringUtils.isNotBlank(bdcFwfsss.getBdcdyid()) && StringUtils.isNotBlank(bdcFwfsss.getWiid())) {
                    List<BdcXm> bdcXms = bdcXmService.getBdcXmListByWiidAndBdcdyid(bdcFwfsss.getWiid(), bdcFwfsss.getBdcdyid());
                    if (CollectionUtils.isNotEmpty(bdcXms)) {
                        bdcXmList.addAll(bdcXms);
                    }
                }
            }
        }
        return bdcXmList;
    }

    @Override
    public String checkExistFsssForGd(String proid){
        String msg = "notExist";
        if(StringUtils.isNotBlank(proid)){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null) {
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    for(BdcXmRel bdcXmRel : bdcXmRelList){
                        if(StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                            HashMap<String, Object> queryMap = Maps.newHashMap();
                            queryMap.put("qlid", bdcXmRel.getYqlid());
                            List<GdFwQl> gdFwQlList = gdFwService.getGdFwQlByMap(queryMap);
                            if (CollectionUtils.isNotEmpty(gdFwQlList)){
                                GdFwQl gdFwQl = gdFwQlList.get(0);
                                if (gdFwQl != null && StringUtils.isNotBlank(gdFwQl.getQlid())) {
                                    List<GdFw> gdFwList = gdFwService.getGdFwByQlid(gdFwQl.getQlid());
                                    if (CollectionUtils.isNotEmpty(gdFwList)) {
                                        for (GdFw gdFw : gdFwList) {
                                            if (StringUtils.isNotBlank(gdFw.getFwid()) && StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(), "1")) {
                                                msg = "exist";
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
        }
        return msg;
    }

    @Override
    public String checkExistFsss(String proid) {
        String msg = "notExist";
        if (StringUtils.isNotBlank(proid)) {
            List<BdcFwfsss> bdcFwFsssList = getBdcFwfsssListByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcFwFsssList)) {
                msg = "exist";
            }
        }
        return msg;
    }

    @Override
    public Boolean checkAddAllMjIntoFzmj(String wiid){
        Boolean isAddAll = false;
        if(StringUtils.isNotBlank(wiid)){
            String sqlxdm = "";
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
            if(pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
            String fsssFwmjTdmjIntoFzmjSqlx = AppConfig.getProperty("fsss.fwmj.tdmj.intofzmj.sqlx");
            if(StringUtils.isNotBlank(fsssFwmjTdmjIntoFzmjSqlx)){
                String[] sqlxArray = fsssFwmjTdmjIntoFzmjSqlx.split(",");
                if(sqlxArray!=null && Arrays.asList(sqlxArray).contains(sqlxdm)){
                    isAddAll = true;
                }
            }
        }
        return isAddAll;
    }

    @Override
    public List<BdcFwfsss> getBdcFwfsssListByProid(String proid) {
        List<BdcFwfsss> bdcFwFsssList = new ArrayList<BdcFwfsss>();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid()) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    HashMap fsssMap = new HashMap();
                    fsssMap.put("wiid", bdcXm.getWiid());
                    fsssMap.put(ParamsConstants.ZFBDCDYH_LOWERCASE, bdcBdcdy.getBdcdyh());
                    bdcFwFsssList = getBdcFwfsssList(fsssMap);
                }
            }
        }
        return bdcFwFsssList;
    }

    @Override
    public void delBdcFwfsss(final String proid) {
        if (StringUtils.isNotBlank(proid)) {
            List<BdcFwfsss> bdcFwfsssList = getBdcFwfsssListByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                    delBdcFwfsssByFwfsssid(bdcFwfsss.getFwfsssid());
                }
            }
        }
    }

    @Override
    public void syncFsssVoByZfVo(BdcXm fsssBdcXm, BdcXm zfBdcXm) {
        if (fsssBdcXm != null && zfBdcXm != null && StringUtils.isNoneBlank(zfBdcXm.getProid()) && StringUtils.isNotBlank(fsssBdcXm.getProid())) {
            //同步BdcXm信息
            zfBdcXm.setProid(fsssBdcXm.getProid());
            zfBdcXm.setBdcdyid(fsssBdcXm.getBdcdyid());
            zfBdcXm.setZl(fsssBdcXm.getZl());
            bdcXmService.saveBdcXm(zfBdcXm);
            //同步审批信息
            BdcSpxx zfBdcSpxx = bdcSpxxService.queryBdcSpxxByProid(zfBdcXm.getProid());
            BdcSpxx fsssBdcSpxx = bdcSpxxService.queryBdcSpxxByProid(fsssBdcXm.getProid());
            if (zfBdcSpxx != null && fsssBdcSpxx != null) {
                zfBdcSpxx.setProid(fsssBdcSpxx.getProid());
                zfBdcSpxx.setSpxxid(fsssBdcSpxx.getSpxxid());
                zfBdcSpxx.setBdcdyh(fsssBdcSpxx.getBdcdyh());
                zfBdcSpxx.setMj(fsssBdcSpxx.getMj());
                zfBdcSpxx.setYt(fsssBdcSpxx.getYt());
                bdcSpxxService.saveBdcSpxx(zfBdcSpxx);
            }
            //同步权利人信息
            List<BdcQlr> zfBdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(zfBdcXm.getProid());
            if (CollectionUtils.isNotEmpty(zfBdcQlrList)) {
                //删除次项目的权利人，防止重复同步
                bdcQlrService.delBdcQlrByProid(fsssBdcXm.getProid(), "");
                for (BdcQlr bdcQlr : zfBdcQlrList) {
                    bdcQlr.setProid(fsssBdcXm.getProid());
                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                    bdcQlrService.saveBdcQlr(bdcQlr);
                }
            }
        }
    }

    @Override
    public void batchDelBdcFwfsss(List<BdcXm> bdcXmList) {
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            BdcXm bdcXm = bdcXmList.get(0);
            List<String> zfbdcdyhList = bdcdyService.getBdcdyhByBdcXmList(bdcXmList);
            batchDelBdcFwfsssByWiidAndZfbdcdyhList(bdcXm.getWiid(),zfbdcdyhList);
        }
    }

    @Override
    public Boolean needAddBdcFsssMj(BdcXmRel bdcXmRel, BdcFwfsss bdcFwfsss,String zfbdcdyh) {
        Boolean needAddBdcFsssMj = true;
        String wiid = "";
        if(bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())){
            List<BdcXm> ybdcXmList = bdcXmService.getBdcXmListByProid(bdcXmRel.getYproid());
            if(CollectionUtils.isNotEmpty(ybdcXmList)){
                BdcXm ybdcXm = ybdcXmList.get(0);
                if(StringUtils.isNotBlank(ybdcXm.getWiid())){
                    wiid = ybdcXm.getWiid();
                    HashMap existMap = new HashMap();
                    existMap.put("wiid",wiid);
                    existMap.put(ParamsConstants.ZFBDCDYH_LOWERCASE,zfbdcdyh);
                    existMap.put("fwbm",bdcFwfsss.getFwbm());
                    List<BdcFwfsss> existBdcFwfsssList = getBdcFwfsssList(existMap);
                    if(CollectionUtils.isNotEmpty(existBdcFwfsssList)){
                        needAddBdcFsssMj = false;
                    }
                }
            }
        }
        return needAddBdcFsssMj;
    }

    @Override
    public void autoCreatBdcFsss(String proid){
        if(StringUtils.isNotBlank(proid)){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null) {
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                String wiid = "";
                if(StringUtils.isNotBlank(bdcXm.getWiid())){
                    wiid = bdcXm.getWiid();
                }

                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    for(BdcXmRel bdcXmRel : bdcXmRelList){
                        if(StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                            HashMap<String, Object> queryMap = Maps.newHashMap();
                            queryMap.put("qlid", bdcXmRel.getYqlid());
                            List<GdFwQl> gdFwQlList = gdFwService.getGdFwQlByMap(queryMap);
                            if (CollectionUtils.isNotEmpty(gdFwQlList)){
                                GdFwQl gdFwQl = gdFwQlList.get(0);
                                if (gdFwQl != null && StringUtils.isNotBlank(gdFwQl.getQlid())) {
                                    List<GdFw> gdFwList = gdFwService.getGdFwByQlid(gdFwQl.getQlid());
                                    if (CollectionUtils.isNotEmpty(gdFwList)) {
                                        for (GdFw gdFw : gdFwList) {
                                            if (StringUtils.isNotBlank(gdFw.getFwid()) && StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(), "1")) {
                                                String fwid = gdFw.getFwid();
                                                Boolean saveFsss = false;
                                                if (StringUtils.isNotBlank(gdFw.getDah())) {
                                                    HashMap map = new HashMap();
                                                    map.put("fwbm", gdFw.getDah());
                                                    List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(map);
                                                    if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                                                        saveFsss = true;
                                                    }
                                                }
                                                if (saveFsss) {
                                                    List<BdcFwfsss> bdcFwfsssList = null;
                                                    if (gdFw != null && StringUtils.isNotBlank(gdFw.getDah())) {
                                                        HashMap map = new HashMap();
                                                        map.put("fwbm", gdFw.getDah());
                                                        map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
                                                        bdcFwfsssList = getBdcFwfsssList(map);
                                                    }
                                                    if (CollectionUtils.isEmpty(bdcFwfsssList)) {
                                                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                                        if (bdcBdcdy!= null&&StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                                                            //匹配不动产单元号的需要创建项目
                                                            //标记过渡房屋为附属设施
                                                            gdFwService.setFsss(fwid, "1");
                                                            //增加附属设施信息
                                                            BdcFwfsss bdcFwfsss = intiFwfsssByFwid(fwid);

                                                            addBdcFwfsssMj(wiid,bdcFwfsss, bdcSpxx, bdcFdcqList);
                                                            //判断项目的bdcdyh与传进来的是否一致，一致的情况下允许为该项目插入附属设施信息
                                                            if (StringUtils.equals(bdcSpxx.getBdcdyh(), bdcBdcdy.getBdcdyh()) && bdcFwfsss != null) {
                                                                List<BdcZdFwyt> bdcZdFwytList = bdcZdGlService.getBdcZdFwyt();
                                                                //gd_fw用途为名称，保存bdcfwfsss时保存代码
                                                                for (BdcZdFwyt bdcZdFwyt : bdcZdFwytList) {
                                                                    if (bdcZdFwyt!=null&&StringUtils.isNotBlank(bdcZdFwyt.getMc())&&bdcFwfsss!=null&&StringUtils.isNotBlank(bdcFwfsss.getGhyt())&&StringUtils.equals(bdcZdFwyt.getMc().trim(), bdcFwfsss.getGhyt().trim())) {
                                                                        bdcFwfsss.setGhyt(bdcZdFwyt.getDm());
                                                                    }
                                                                }
                                                                bdcFwfsss.setBdcdyh(bdcFwfsss.getBdcdyh());
                                                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                                                bdcFwfsss.setZfbdcdyh(bdcSpxx.getBdcdyh());
                                                                bdcFwfsss.setWiid(wiid);
                                                                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                                                                    bdcFwfsss.setProid(bdcXm.getProid());
                                                                }
                                                                saveBdcFwfsss(bdcFwfsss);
                                                            }
                                                        } else {
                                                            //未匹配不动产单元号
                                                            //标记过渡房屋为附属设施
                                                            gdFwService.setFsss(fwid, "1");
                                                            BdcFwfsss bdcFwfsss = intiFwfsssByFwid(fwid);
                                                            addBdcFwfsssMj(wiid,bdcFwfsss, bdcSpxx, bdcFdcqList);
                                                            if (bdcFwfsss != null) {
                                                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                                                bdcFwfsss.setWiid(wiid);
                                                                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                                                                    bdcFwfsss.setProid(bdcXm.getProid());
                                                                }
                                                                saveBdcFwfsss(bdcFwfsss);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addBdcFwfsssMj(String wiid,BdcFwfsss bdcFwfsss, BdcSpxx bdcSpxx, List<BdcFdcq> bdcFdcqList) {
        addBdcFwfsssMj(bdcFwfsss,bdcSpxx);
        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
            for (BdcFdcq bdcFdcq : bdcFdcqList) {
                addBdcFwfsssMj(wiid,bdcFwfsss,bdcFdcq);
            }
        }
    }

    @Override
    public void addBdcFwfsssMj(String wiid,BdcFwfsss bdcFwfsss, BdcFdcq bdcFdcq) {
        if (bdcFwfsss != null&&bdcFdcq != null) {
            if (bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() > 0) {
                if (bdcFdcq.getJzmj() != null) {
                    bdcFdcq.setJzmj(bdcFdcq.getJzmj() + bdcFwfsss.getJzmj());
                } else {
                    bdcFdcq.setJzmj(bdcFwfsss.getJzmj());
                }
            }

            if (bdcFwfsss.getTnjzmj() != null && bdcFwfsss.getTnjzmj() > 0) {
                if (bdcFdcq.getTnjzmj() != null) {
                    bdcFdcq.setTnjzmj(bdcFdcq.getTnjzmj() + bdcFwfsss.getTnjzmj());
                } else {
                    bdcFdcq.setTnjzmj(bdcFwfsss.getTnjzmj());
                }
            }

            if (bdcFwfsss.getFtjzmj() != null && bdcFwfsss.getFtjzmj() > 0) {
                if (bdcFdcq.getFtjzmj() != null) {
                    bdcFdcq.setFtjzmj(bdcFdcq.getFtjzmj() + bdcFwfsss.getFtjzmj());
                } else {
                    bdcFdcq.setFtjzmj(bdcFwfsss.getFtjzmj());
                }
            }

            if(checkAddAllMjIntoFzmj(wiid)) {
                if (bdcFwfsss.getFttdmj()!= null && bdcFwfsss.getFttdmj() > 0) {
                    if (bdcFdcq.getFttdmj() != null) {
                        bdcFdcq.setFttdmj(bdcFdcq.getFttdmj() + bdcFwfsss.getFttdmj());
                    } else {
                        bdcFdcq.setFttdmj(bdcFwfsss.getFttdmj());
                    }
                }
            }
            bdcFdcqService.saveBdcFdcq(bdcFdcq);
        }
    }

    @Override
    public void addBdcFwfsssTdmj(BdcFwfsss bdcFwfsss,BdcSpxx bdcSpxx){
        if(bdcFwfsss != null && bdcSpxx != null && bdcFwfsss.getFttdmj() != null && bdcFwfsss.getFttdmj() != 0){
            if (bdcSpxx.getZdzhmj() != null) {
                bdcSpxx.setZdzhmj(bdcSpxx.getZdzhmj() + bdcFwfsss.getFttdmj());
            } else {
                bdcSpxx.setZdzhmj(bdcFwfsss.getFttdmj());
            }
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
        }
    }

    @Override
    public void addBdcFwfsssMj(BdcFwfsss bdcFwfsss, BdcSpxx bdcSpxx) {
        if (bdcFwfsss != null&&bdcSpxx != null&&bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() != 0) {
            if (bdcSpxx.getMj() != null) {
                bdcSpxx.setMj(bdcSpxx.getMj() + bdcFwfsss.getJzmj());
            } else {
                bdcSpxx.setMj(bdcFwfsss.getJzmj());
            }
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
        }
    }

    @Override
    public void subtractBdcFwfsssTdmj(BdcFwfsss bdcFwfsss,BdcSpxx bdcSpxx) {
        if(bdcFwfsss != null && bdcSpxx != null && bdcFwfsss.getFttdmj() != null && bdcFwfsss.getFttdmj() != 0 && bdcSpxx.getZdzhmj() != null && bdcSpxx.getZdzhmj()>bdcFwfsss.getFttdmj()){
            bdcSpxx.setZdzhmj(bdcSpxx.getZdzhmj() - bdcFwfsss.getFttdmj());
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
        }
    }

    @Override
    public void subtractBdcFwfsssMj(BdcFwfsss bdcFwfsss, BdcSpxx bdcSpxx, List<BdcFdcq> bdcFdcqList) {
        if(bdcFwfsss != null)  {
            if (bdcSpxx != null && bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() != 0) {
                if (bdcSpxx.getMj() != null&& bdcSpxx.getMj()> bdcFwfsss.getJzmj()) {
                    bdcSpxx.setMj(bdcSpxx.getMj() - bdcFwfsss.getJzmj());
                }
                bdcSpxxService.saveBdcSpxx(bdcSpxx);
            }
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                for (BdcFdcq bdcFdcq : bdcFdcqList) {
                    if(bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() != 0 && bdcFdcq.getJzmj() != null&&bdcFdcq.getJzmj()>bdcFwfsss.getJzmj()) {
                        bdcFdcq.setJzmj(bdcFdcq.getJzmj() - bdcFwfsss.getJzmj());
                    }
                    if(bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() != 0 && bdcFdcq.getScmj() != null&&bdcFdcq.getScmj()>bdcFwfsss.getJzmj()) {
                        bdcFdcq.setScmj(bdcFdcq.getScmj() - bdcFwfsss.getJzmj());
                    }
                    if(bdcFwfsss.getTnjzmj() != null && bdcFwfsss.getTnjzmj() != 0 && bdcFdcq.getTnjzmj() != null&&bdcFdcq.getTnjzmj()>bdcFwfsss.getTnjzmj()) {
                        bdcFdcq.setTnjzmj(bdcFdcq.getTnjzmj() - bdcFwfsss.getTnjzmj());
                    }
                    if(bdcFwfsss.getFtjzmj() != null && bdcFwfsss.getFtjzmj() != 0 && bdcFdcq.getFtjzmj() != null&&bdcFdcq.getFtjzmj()>bdcFwfsss.getFtjzmj()) {
                        bdcFdcq.setFtjzmj(bdcFdcq.getFtjzmj() - bdcFwfsss.getFtjzmj());
                    }
                    bdcFdcqService.saveBdcFdcq(bdcFdcq);
                }
            }
        }
    }


    public void batchDelBdcFwfsssByWiidAndZfbdcdyhList(String wiid,List<String> zfbdcdyhList) {
        if(StringUtils.isNotBlank(wiid) && CollectionUtils.isNotEmpty(zfbdcdyhList)){
            HashMap map = Maps.newHashMap();
            map.put("wiid",wiid);
            map.put("zfbdcdyhList",zfbdcdyhList);
            bdcFwfsssMapper.batchDelBdcFwfsss(map);
        }
    }

    @Override
    public void delBdcFwfsssMj(BdcFwfsss bdcFwfsss, BdcFdcq bdcFdcq) {
        if (bdcFwfsss != null&&bdcFdcq != null) {
            if (bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() != 0) {
                if (bdcFdcq.getJzmj() != null) {
                    bdcFdcq.setJzmj(bdcFdcq.getJzmj() - bdcFwfsss.getJzmj());
                } else {
                    bdcFdcq.setJzmj(bdcFwfsss.getJzmj());
                }
            }

            if (bdcFwfsss.getTnjzmj() != null && bdcFwfsss.getTnjzmj() != 0) {
                if (bdcFdcq.getTnjzmj() != null) {
                    bdcFdcq.setTnjzmj(bdcFdcq.getTnjzmj() - bdcFwfsss.getTnjzmj());
                } else {
                    bdcFdcq.setTnjzmj(bdcFwfsss.getTnjzmj());
                }
            }

            if (bdcFwfsss.getFtjzmj() != null && bdcFwfsss.getFtjzmj() != 0) {
                if (bdcFdcq.getFtjzmj() != null) {
                    bdcFdcq.setFtjzmj(bdcFdcq.getFtjzmj() - bdcFwfsss.getFtjzmj());
                } else {
                    bdcFdcq.setFtjzmj(bdcFwfsss.getFtjzmj());
                }
            }

            bdcFdcqService.saveBdcFdcq(bdcFdcq);
        }
    }


    @Override
    public List<BdcFwfsss> inheritCqFwfsss(String proid, String qllx) {
        List<BdcFwfsss> bdcFwfsssList = null;
        if(StringUtils.isNotBlank(proid)&&StringUtils.isNotBlank(qllx)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.size() > 1) {
                    String cqProid = "";
                    List<String> notCqProidList = new ArrayList<String>();
                    for (BdcXm xm : bdcXmList) {
                        if (StringUtils.equals(xm.getQllx(), qllx)) {
                            notCqProidList.add(xm.getProid());
                        } else {
                            cqProid = xm.getProid();
                        }
                    }
                    if (StringUtils.isNotBlank(cqProid) && CollectionUtils.isNotEmpty(notCqProidList)) {
                        bdcFwfsssList = getBdcFwfsssByProid(cqProid);
                        for (String notCqProid : notCqProidList) {
                            for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                                BdcFwfsss bdcFwfsssCopy = null;
                                try {
                                    bdcFwfsssCopy = (BdcFwfsss) BeanUtils.cloneBean(bdcFwfsss);
                                } catch (Exception e) {
                                    logger.error("BdcFwFsssServiceImpl.inheritCqFwfsss",e);
                                }
                                if (bdcFwfsssCopy != null) {
                                    bdcFwfsssCopy.setProid(notCqProid);
                                    bdcFwfsssCopy.setFwfsssid(UUIDGenerator.generate18());
                                    entityMapper.saveOrUpdate(bdcFwfsssCopy, bdcFwfsssCopy.getFwfsssid());
                                }
                            }
                        }
                    }
                }
            }
        }
        return bdcFwfsssList;
    }

    @Override
    public List<BdcFwfsss> getBdcFwfsssByProid(String proid) {
        List<BdcFwfsss> bdcFwfsssList = null;
        if(StringUtils.isNotBlank(proid)){
            Map param = Maps.newHashMap();
            param.put("proid", proid);
            bdcFwfsssList = bdcFwfsssMapper.getBdcFwfsssList(param);
        }
        return bdcFwfsssList;
    }

    @Override
    public void saveMjFromBdcSpxxToBdcDyaq(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(proid);
            if (bdcSpxx != null && bdcDyaq != null && bdcSpxx.getMj() != null) {
                bdcDyaq.setFwdymj(bdcSpxx.getMj());
                entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
            }
        }
    }

    @Override
    public void subtractBdcFwfsssFwDymj(BdcFwfsss bdcFwfsss, String wiid,String proid) {
        if(bdcFwfsss != null){
            List<BdcXm> bdcXmList = new ArrayList<BdcXm>();
            if(StringUtils.isNotBlank(wiid)){
                bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            }else if(StringUtils.isNotBlank(proid)){
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if(bdcXm != null){
                    bdcXmList.add(bdcXm);
                }
            }
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for(BdcXm bdcXm : bdcXmList){
                    BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(bdcXm.getProid());
                    if(bdcDyaq != null && bdcDyaq.getFwdymj() != null
                            && bdcDyaq.getFwdymj() > bdcFwfsss.getJzmj()){
                        bdcDyaq.setFwdymj(CalculationUtils.sub(bdcDyaq.getFwdymj(), bdcFwfsss.getJzmj()));
                        bdcDyaqService.saveBdcDyaq(bdcDyaq);
                        if(StringUtils.isNotBlank(bdcDyaq.getProid())){
                            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcDyaq.getProid());
                            if(bdcSpxx != null && bdcSpxx.getMj() != null
                            && bdcSpxx.getMj() > bdcFwfsss.getJzmj()){
                                bdcSpxx.setMj(CalculationUtils.sub(bdcSpxx.getMj(),bdcFwfsss.getJzmj()));
                                bdcSpxxService.saveBdcSpxx(bdcSpxx);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void synchronizeDyaSpxxMj(String wiid) {
        if(StringUtils.isNotBlank(wiid)) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                if (CommonUtil.indexOfStrs(Constants.SQLX_HBDY, sqlxdm)) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                    BdcSpxx cqBdcSpxx = null;
                    BdcSpxx dyaBdcSpxx = null;
                    for (BdcXm bdcXm : bdcXmList) {
                        if (bdcXm != null && StringUtils.equals(Constants.QLLX_DYAQ, bdcXm.getQllx())) {
                            dyaBdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                        } else if (bdcXm != null && StringUtils.equals(Constants.QLLX_GYTD_FWSUQ, bdcXm.getQllx())) {
                            cqBdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                        }
                    }
                    dyaBdcSpxx.setMj(cqBdcSpxx.getMj());
                    bdcSpxxService.saveBdcSpxx(dyaBdcSpxx);
                }
            }
        }
    }

}
