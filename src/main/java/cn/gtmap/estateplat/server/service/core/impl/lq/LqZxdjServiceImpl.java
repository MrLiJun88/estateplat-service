package cn.gtmap.estateplat.server.service.core.impl.lq;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/4/19
 */
@Service
public class LqZxdjServiceImpl extends LqdjService {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcDyaqService bdcDyaqService;

    /**
     * 初始化项目，根据不动产单元号或者证书（明）信息初始化BDC_XM,收件信息C_SJXX，收件材料C_SJCl，审批信息C_SPXX，
     * 不动产权利信息，不动产单元BDC_BDCDY、不动产登记簿BDC_、权利人信息
     *
     * @param xmxx 信息信息
     */
    @Override
    public void initializeProject(Xmxx xmxx) {
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project = null;
        List<BdcQlr> ybdcQlrList = null;
        List<BdcQlr> ybdcYwrList = null;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
            ybdcYwrList = getYbdcYwrList(project);
        } else
            throw new AppException(4005);
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(project);
                // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
                BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
                if (bdcTd == null) {
                    list.add(intBdcTd(getDjsjNydDcbByBdcdyh(project.getBdcdyh()), project, bdcxm.getQllx()));
                }
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }
        }
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(project, bdcSpxx);
        if (bdcSpxx != null)
            list.add(bdcSpxx);
        //存入不动产单元
        BdcBdcdy bdcdy = initBdcdy(project, bdcdjb);
        if (bdcdy != null) {
            list.add(bdcdy);
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
        }
        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);
        //初始化bdcQlr
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcQlr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            for (BdcQlr bdcQlr : ybdcYwrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            saveOrUpdateProjectDate(list);
        }
        //初始化权利信息
        if (bdcxm != null) {
            saveQllxVo(bdcxm);
        }
    }

    /**
     * @param wiid
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 更新项目权属状态，包括本次项目权利状态、上一手权属状态（包括过渡数据权属状态）
     */
    @Override
    public void updateProjectQszt(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                changeYqszt(bdcXm);
            }
        }
    }

    /**
     * @param bdcXm 不动产登记项目
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 更新不动产项目上一手登记项目的权属状态（包括过渡数据权属状态）
     */
    private void changeYqszt(BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            String yqllxdm = bdcZdGlService.getYqllxBySqlx(bdcXm.getSqlx());
            bdcXm.setQllx(yqllxdm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                }
                changeGdsjQszt(bdcXmRel, 1);
                changeFj(bdcXm);
            }
        }
    }

    /**
     * @param bdcXm 不动产登记项目
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 更新不动产项目上一手登记项目的权属状态（包括过渡数据权属状态）
     */
    private void changeFj(BdcXm bdcXm) {
        if (bdcXm != null) {
            /*根据现项目编号获得原项目proid*/
            BdcXm ybdcxm;
            Example bdcxmExample = new Example(bdcXm.getClass());
            List<BdcXm> ybdcxmList = new ArrayList<BdcXm>();
            if (StringUtils.isNotBlank(bdcXm.getYbh())) {
                bdcxmExample.createCriteria().andEqualTo("bh", bdcXm.getYbh());
                ybdcxmList = entityMapper.selectByExample(bdcxmExample);
            }

            if (CollectionUtils.isNotEmpty(ybdcxmList)) {
                ybdcxm = ybdcxmList.get(0);
                String yproid = ybdcxm.getProid() == null ? "" : ybdcxm.getProid();
            /*如果原权利类型是抵押、异议、查封，则不做附记处理*/
                if (!StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_DYAQ) && !StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_YYDJ) && !StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_CFDJ)) {
                /*获得原项目权利类型*/
                    QllxVo yQllxVo = qllxService.makeSureQllx(ybdcxm);
                    Example yQllx = new Example(yQllxVo.getClass());
                    yQllx.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, yproid);
                    List<QllxVo> yQllxVoList = (List<QllxVo>) entityMapper.selectByExample(yQllxVo.getClass(), yQllx);
                    if (CollectionUtils.isNotEmpty(yQllxVoList)) {
                        for (QllxVo yqllxVo : yQllxVoList) {
                            if (yqllxVo != null) {
                                yqllxVo.setFj((yqllxVo.getFj() == null ? "" : yqllxVo.getFj()) + "\n" + CalendarUtil.formateToStrChinaYMDDate(CalendarUtil.getCurDate()) + "\n注销权利");
                                entityMapper.updateByPrimaryKeySelective(yqllxVo);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 删除不动产登记项目权利信息，各权利删除对应的信息有所不同
     */
    @Override
    public void deleteProjectQlxx(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                String proid = bdcXm.getProid();
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                String yproid = "";
                if (CollectionUtils.isNotEmpty(bdcXmRelList) && StringUtils.isNotBlank(bdcXmRelList.get(0).getYproid()))
                    yproid = bdcXmRelList.get(0).getYproid();
                //删除权利类型信息
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                //如果为注销抵押  查封  还原注销信息
                if (StringUtils.isNotBlank(yproid) && qllxVo instanceof BdcDyaq) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(ParamsConstants.PROID_LOWERCASE, yproid);
                    List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
                    if (CollectionUtils.isNotEmpty(qllxVos)) {
                        BdcDyaq bdcDyaq = (BdcDyaq) qllxVos.get(0);
                        bdcDyaq.setZxdbr("");
                        bdcDyaq.setZxdyywh("");
                        bdcDyaq.setZxdyyy("");
                        bdcDyaq.setZxsj(null);
                        bdcDyaqService.updateBdcDyaqForZxdj(bdcDyaq);
                    }
                } else if (StringUtils.isNotBlank(yproid) && qllxVo instanceof BdcCf) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(ParamsConstants.PROID_LOWERCASE, yproid);
                    List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
                    if (CollectionUtils.isNotEmpty(qllxVos)) {
                        BdcCf bdcCf = (BdcCf) qllxVos.get(0);
                        bdcCf.setJfdbr("");
                        bdcCf.setJfdjsj(null);
                        bdcCf.setJfjg("");
                        bdcCf.setJfsj(null);
                        bdcCf.setJfwh("");
                        bdcCf.setJfwj("");
                        bdcCf.setJfywh("");
                        bdcCfService.updateBdcCfForJfxx(bdcCf);
                    }
                    {
                        qllxService.delQllxByproid(qllxVo, proid);
                    }
                } else
                    bdcdyService.delDjbAndTd(bdcXm);
            }
        }
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 还原上一手权利信息（包括上一手过渡数据），包括权利状态等，主要用于删除项目，撤销或退回项目
     */
    @Override
    public void revertYqlxx(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                List<BdcXmRel> bdcXmRelList = null;
                if (bdcXm != null)
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    String yqllxdm = bdcZdGlService.getYqllxBySqlx(bdcXm.getSqlx());
                    bdcXm.setQllx(yqllxdm);
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid()))
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), 1);
                        //如果存在老数据  则注销老数据状态
                        revertGdsjQszt(bdcXmRel, 0);
                    }
                }
            }
        }
    }

    /**
     * 初始化权利信息
     *
     * @param bdcXm
     * @return
     */
    public void saveQllxVo(BdcXm bdcXm) {
        //获取项目来源
        String xmly = bdcXm.getXmly();
        //如果项目来源不是过渡数据
        if (!org.apache.commons.lang.StringUtils.equals(xmly, Constants.XMLY_BDC)) {
            saveXQllxVo(bdcXm);
            saveYQllxVo(bdcXm);
        } else {
            //修改原权利信息
            saveYQllxVo(bdcXm);
        }
    }

    /**
     * 初始化当前权利信息
     *
     * @param bdcXm
     * @return
     */
    private void saveXQllxVo(final BdcXm bdcXm) {
        QllxVo qllxVo = null;
        if (bdcXm != null) {
            qllxVo = qllxService.makeSureQllx(bdcXm);
            QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
            qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm);
            if (qllxVo != null) {
                if (qllxVoTemp == null) {
                    entityMapper.insertSelective(qllxVo);
                } else {
                    qllxVo.setQlid(qllxVoTemp.getQlid());
                    entityMapper.updateByPrimaryKeySelective(qllxVo);
                }
            }
        }
    }

    /**
     * 初始化原权利信息
     *
     * @param bdcXm
     * @return
     */
    private void saveYQllxVo(final BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            List<BdcXm> ybdcXmList = bdcXmService.getYbdcXmListByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(ybdcXmList)) {
                for (BdcXm ybdcXm : ybdcXmList) {
                    if (ybdcXm != null && StringUtils.isNotBlank(ybdcXm.getProid())) {
                        QllxVo qllxVo = null;
                        qllxVo = qllxService.makeSureQllx(ybdcXm);
                        qllxVo = qllxService.queryQllxVo(qllxVo, ybdcXm.getProid());
                        if (qllxVo != null) {
                            if (qllxVo instanceof BdcCf) {
                                BdcCf bdcCf = (BdcCf) qllxVo;

                                bdcCf.setJfywh(bdcXm.getBh());
                                entityMapper.updateByPrimaryKeySelective(bdcCf);
                            } else if (qllxVo instanceof BdcDyaq) {
                                BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                                bdcDyaq.setZxdyywh(bdcXm.getBh());
                                bdcDyaq.setZxdyyy(bdcXm.getDjyy());
                                entityMapper.updateByPrimaryKeySelective(bdcDyaq);

                            } else if (qllxVo instanceof BdcYy) {
                                BdcYy bdcYy = (BdcYy) qllxVo;
                                bdcYy.setZxyyh(bdcXm.getBh());
                                bdcYy.setZxyyyy(bdcXm.getDjyy());
                                entityMapper.updateByPrimaryKeySelective(bdcYy);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getProjectCode() {
        return "4001201";
    }
}
