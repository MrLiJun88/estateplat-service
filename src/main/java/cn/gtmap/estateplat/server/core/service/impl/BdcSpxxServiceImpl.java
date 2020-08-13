package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDjsjMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcSpxxMapper;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.InterfaceCodeBeanFactory;
import cn.gtmap.estateplat.server.sj.dq.BdcSpxxDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 不动产审批信息
 * Created by lst on 2015/3/17.
 */
@Service
public class BdcSpxxServiceImpl implements BdcSpxxService {
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private DjSjMapper djSjMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    EntityMapper entityMapper;

    @Autowired
    private BdcDjsjMapper bdcDjsjMapper;
    @Autowired
    private BdcSpxxMapper bdcSpxxMapper;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private GdDyhRelService gdDyhRelService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private Set<BdcSpxxDqService> bdcSpxxDqServiceList;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;

    @Override
    @Transactional
    public void delBdcSpxxByProid(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            Example bdcSpxx = new Example(BdcSpxx.class);
            bdcSpxx.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            entityMapper.deleteByExample(BdcSpxx.class, bdcSpxx);
        }
    }

    @Override
    public BdcSpxx queryBdcSpxxByProid(String proid) {
        BdcSpxx bdcSpxx = null;
        if (StringUtils.isNotBlank(proid)) {
            Example bdcSpxxExample = new Example(BdcSpxx.class);
            bdcSpxxExample.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(BdcSpxx.class, bdcSpxxExample);
            if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
                bdcSpxx = bdcSpxxList.get(0);
            }
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromProject(final Project project, BdcSpxx bdcSpxx) {
        if (project == null) {
            return bdcSpxx;
        }
        //存入不动产单元的实体类中
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        }
        bdcSpxx.setProid(project.getProid());
        if (StringUtils.isNotBlank(project.getBdclx()))
            bdcSpxx.setBdclx(project.getBdclx());
        if (StringUtils.isNotBlank(project.getBdcdyh()))
            bdcSpxx.setBdcdyh(project.getBdcdyh());

        /**@author bianwen
         * @description 在建建筑物抵押流程修改不动产类型
         */
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, project.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, project.getSqlx())) {
            bdcSpxx.setBdclx(Constants.BDCLX_TDFW);
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromZd(final DjsjZdxx zdxx, BdcSpxx bdcSpxx) {
        if (zdxx == null) {
            return bdcSpxx;
        }
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        }
        if (StringUtils.isNotBlank(zdxx.getSyqlx()))
            bdcSpxx.setZdzhqlxz(zdxx.getSyqlx());
        if (zdxx.getFzmj() != null && zdxx.getFzmj() != 0)
            bdcSpxx.setZdzhmj(zdxx.getFzmj());
        else
            bdcSpxx.setZdzhmj(zdxx.getScmj());
        bdcSpxx.setZdzhyt(zdxx.getTdyt());
        bdcSpxx.setZdzhyt2(zdxx.getTdyt2());
        bdcSpxx.setZdzhyt3(zdxx.getTdyt3());
        bdcSpxx.setZl(zdxx.getTdzl());
        if (StringUtils.isNotBlank(zdxx.getMjdw())) {
            bdcSpxx.setMjdw(zdxx.getMjdw());
        } else {
            bdcSpxx.setMjdw(Constants.DW_PFM);
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromFw(DjsjFwxx fwxx, BdcSpxx bdcSpxx, Project project) {
        if (bdcSpxx == null) {
            return bdcSpxx;
        }
        if (fwxx != null) {
            if (StringUtils.isNotBlank(fwxx.getBdcdyh())) {
                bdcSpxx.setBdcdyh(fwxx.getBdcdyh());
            }
            if (fwxx.getJzmj() != null && fwxx.getJzmj() > 0) {
                bdcSpxx.setMj(fwxx.getJzmj());
                bdcSpxx.setScmj(fwxx.getJzmj());
            } else {
                //购商品房预告登记定着物面积取预测建筑面积
                bdcSpxx.setMj(fwxx.getYcjzmj());
                bdcSpxx.setScmj(fwxx.getYcjzmj());
            }
            if (StringUtils.isNotBlank(fwxx.getFwyt())) {
                //将过渡期的用途转化为字典数据
                List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                String fwyt = null;
                for (BdcZdFwyt bdcZdFwyt : fwytList) {
                    if (StringUtils.equals(bdcZdFwyt.getMc(), fwxx.getFwyt())) {
                        fwyt = bdcZdFwyt.getDm();
                        break;
                    } else if (StringUtils.equals(bdcZdFwyt.getDm(), fwxx.getFwyt())) {
                        fwyt = bdcZdFwyt.getDm();
                        break;
                    }
                }
                bdcSpxx.setYt(fwyt);
            }
            if (StringUtils.isNotBlank(fwxx.getZl())) {
                bdcSpxx.setZl(fwxx.getZl());
            }
        } else if (StringUtils.isNotBlank(project.getBdclx()) && project.getBdclx().equals(Constants.BDCLX_TDFW)) {
            List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(project.getYqlid());
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                List<GdFw> gdFwGlList = new ArrayList<GdFw>();
                GdFw gdFw = gdFwService.queryGdFw(gdBdcQlRelList.get(0).getBdcid());
                if (gdFw != null) {
                    gdFwGlList.add(gdFw);
                    gdFwGlList = bdcCheckCancelService.getGdFwFilterZdsj(gdFwGlList);
                    bdcSpxx = readerGdxxToSpxx(gdFwGlList, null, bdcSpxx);
                }
            }
        }
        return bdcSpxx;
    }

    @Override
    @Transactional(readOnly = true)
    public BdcSpxx getBdcSpxxFromLq(DjsjLqxx lqxx, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null) {
            return bdcSpxx;
        }
        String djh = "";
        if (StringUtils.isNotBlank(bdcSpxx.getBdcdyh()))
            djh = StringUtils.substring(bdcSpxx.getBdcdyh(), 0, 19);
        List<DjsjNydDcb> djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(djh);
        if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
            DjsjNydDcb djsjNydDcb = djsjNydDcbList.get(0);
            if (djsjNydDcb.getFzmj() != null && djsjNydDcb.getFzmj() != 0)
                bdcSpxx.setZdzhmj(djsjNydDcb.getFzmj());
            else
                bdcSpxx.setZdzhmj(djsjNydDcb.getScmj());
            bdcSpxx.setZdzhyt(djsjNydDcb.getTdyt());
            bdcSpxx.setZdzhqlxz(djsjNydDcb.getSyqlx());
        }
        if (lqxx != null) {
            if (StringUtils.isNotBlank(lqxx.getZl()))
                bdcSpxx.setZl(lqxx.getZl());
            if (StringUtils.isNotBlank(lqxx.getLz()))
                bdcSpxx.setLz(lqxx.getLz());
            if (lqxx.getMj() != null)
                bdcSpxx.setMj(lqxx.getMj());
            if (lqxx.getMjdw() != null)
                bdcSpxx.setMjdw(lqxx.getMjdw());
        }
        return bdcSpxx;
    }

    @Override
    @Transactional(readOnly = true)
    public BdcSpxx getBdcSpxxFromNyd(DjsjNydDcb nydDcb, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null) {
            return bdcSpxx;
        }
        String djh = "";
        if (StringUtils.isNotBlank(bdcSpxx.getBdcdyh()))
            djh = StringUtils.substring(bdcSpxx.getBdcdyh(), 0, 19);
        List<DjsjNydDcb> djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(djh);
        if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
            DjsjNydDcb djsjNydDcb = djsjNydDcbList.get(0);
            if (djsjNydDcb.getFzmj() != null && djsjNydDcb.getFzmj() != 0)
                bdcSpxx.setZdzhmj(djsjNydDcb.getFzmj());
            else
                bdcSpxx.setZdzhmj(djsjNydDcb.getScmj());
            bdcSpxx.setZdzhyt(djsjNydDcb.getTdyt());
            bdcSpxx.setZdzhqlxz(djsjNydDcb.getSyqlx());
        }
        if (nydDcb != null) {
            if (StringUtils.isNotBlank(nydDcb.getTdzl()))
                bdcSpxx.setZl(nydDcb.getTdzl());
            if (nydDcb.getFzmj() != null)
                bdcSpxx.setMj(nydDcb.getFzmj());
            if (nydDcb.getMjdw() != null)
                bdcSpxx.setMjdw(nydDcb.getMjdw());
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromZh(final DjsjZhxx zhxx, BdcSpxx bdcSpxx) {
        if (zhxx == null) {
            return bdcSpxx;
        }
        //存入不动产单元的实体类中
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        }
        //宗海权利性质在国家下发标准中没有，不知道填写什么暂时用项目性质
        if (zhxx.getZhmj() != null && zhxx.getZhmj() != 0)
            bdcSpxx.setZdzhmj(zhxx.getZhmj());
        else
            bdcSpxx.setZdzhmj(zhxx.getYhzmj());
        bdcSpxx.setZl(zhxx.getYhwzsm());
        bdcSpxx.setGzwlx(zhxx.getGzwlx());
        bdcSpxx.setYhlx(zhxx.getYhlxa());
        if (StringUtils.isNotBlank(zhxx.getMjdw())) {
            bdcSpxx.setMjdw(zhxx.getMjdw());
        } else {
            bdcSpxx.setMjdw(Constants.DW_GQ);
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromTdcb(final DjsjNydDcb djsjNydDcb, BdcSpxx bdcSpxx) {
        if (djsjNydDcb == null)
            return bdcSpxx;
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        }
        if (djsjNydDcb.getFzmj() != null && djsjNydDcb.getFzmj() != 0) {
            bdcSpxx.setZdzhmj(djsjNydDcb.getFzmj());
        } else {
            bdcSpxx.setZdzhmj(djsjNydDcb.getScmj());
        }
        bdcSpxx.setZdzhqlxz(djsjNydDcb.getSyqlx());
        bdcSpxx.setZdzhyt(djsjNydDcb.getTdyt());
        bdcSpxx.setZl(djsjNydDcb.getTdzl());

        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromTdSyq(final DjsjQszdDcb djsjQszdDcb, BdcSpxx bdcSpxx) {
        if (djsjQszdDcb == null)
            return bdcSpxx;
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        }
        bdcSpxx.setZl(djsjQszdDcb.getTdzl());
        if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0)
            bdcSpxx.setZdzhmj(djsjQszdDcb.getFzmj());
        else
            bdcSpxx.setZdzhmj(djsjQszdDcb.getScmj());
        bdcSpxx.setZdzhyt(djsjQszdDcb.getTdyt());
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromYProject(Project project, BdcSpxx bdcSpxx) {
        if (project == null) {
            return bdcSpxx;
        }
        //存入不动产单元的实体类中
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        }
        if (StringUtils.isNotBlank(project.getBdclx()))
            bdcSpxx.setBdclx(project.getBdclx());
        if (StringUtils.isNotBlank(project.getBdcdyh()))
            bdcSpxx.setBdcdyh(project.getBdcdyh());
        if (StringUtils.isNotBlank(project.getYxmid())) {
            BdcSpxx spxx = queryBdcSpxxByProid(project.getYxmid());
            if (spxx != null) {
                if (StringUtils.isNotBlank(spxx.getZl()))
                    bdcSpxx.setZl(spxx.getZl());
                if (StringUtils.isNotBlank(spxx.getBdcdyh()))
                    bdcSpxx.setBdcdyh(spxx.getBdcdyh());
                if (StringUtils.isNotBlank(spxx.getBdclx()))
                    bdcSpxx.setBdclx(spxx.getBdclx());
                if (spxx.getMj() != null && spxx.getMj() > 0) {
                    bdcSpxx.setMj(spxx.getMj());
                }
                if (spxx.getScmj() != null && spxx.getScmj() > 0)
                    bdcSpxx.setScmj(spxx.getScmj());
                if (StringUtils.isNotBlank(spxx.getYt()))
                    bdcSpxx.setYt(spxx.getYt());
                if (StringUtils.isNotBlank(spxx.getYhlx()))
                    bdcSpxx.setYhlx(spxx.getYhlx());
                if (StringUtils.isNotBlank(spxx.getGzwlx()))
                    bdcSpxx.setGzwlx(spxx.getGzwlx());
                if (StringUtils.isNotBlank(spxx.getLz()))
                    bdcSpxx.setLz(spxx.getLz());
                if (StringUtils.isNotBlank(spxx.getMjdw()))
                    bdcSpxx.setMjdw(spxx.getMjdw());
                if (spxx.getZdzhmj() != null && spxx.getZdzhmj() > 0)
                    bdcSpxx.setZdzhmj(spxx.getZdzhmj());
                if (StringUtils.isNotBlank(spxx.getZdzhyt()))
                    bdcSpxx.setZdzhyt(spxx.getZdzhyt());
                if (StringUtils.isNotBlank(spxx.getZdzhqlxz()))
                    bdcSpxx.setZdzhqlxz(spxx.getZdzhqlxz());
            }
        }
        //在建建筑物抵押流程修改不动产类型
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, project.getSqlx())) {
            bdcSpxx.setBdclx(Constants.BDCLX_TDFW);
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromGdxm(final String gdProid, final String yqlid, final String xmly, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null)
            bdcSpxx = new BdcSpxx();
        //zdd 来源土地审批登记业务
        if (StringUtils.isNotBlank(xmly) && xmly.equals(Constants.XMLY_TDSP)) {
            //lzq 通过qlid获取土地信息
            List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(yqlid);
            gdTdList = bdcCheckCancelService.getGdTdFilterZdsj(gdTdList);
            bdcSpxx = readerGdxxToSpxx(null, gdTdList, bdcSpxx);
            if (CollectionUtils.isNotEmpty(gdTdList) && StringUtils.isNotBlank(gdTdList.get(0).getTdid())) {
                List<GdTdsyq> gdTdsyqList = gdTdService.getGdTdsyqByTdid(gdTdList.get(0).getTdid());
                if (CollectionUtils.isNotEmpty(gdTdsyqList) && gdTdsyqList.get(0) != null && gdTdsyqList.get(0).getSyqmj() != null && gdTdsyqList.get(0).getSyqmj() > 0) {
                    bdcSpxx.setZdzhmj(gdTdsyqList.get(0).getSyqmj());
                }
            }
        } else if (StringUtils.isNotBlank(xmly) && xmly.equals(Constants.XMLY_FWSP)) {

            //zdd  通过获取gdDyhRel找到对应土地信息  读取宗地信息到审批表
            List<GdFw> gdFwList = new ArrayList<GdFw>();
            List<GdTd> gdTdList = new ArrayList<GdTd>();
            if (StringUtils.isNotBlank(yqlid)) {
                //因项目内多幢拼接yqlid,所以项目内多幢要分开获取gd_fw和gd_td
                gdFwList = gdFwService.getGdFwBySplitQlid(yqlid, ",");
                //房屋按规划用途过滤附属设施（车库，阁楼等）
                String fwFilterFsssGhyt = AppConfig.getProperty("fw.filterFsss.ghyt");
                //房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
                if (StringUtils.equals(fwFilterFsssGhyt, "true") && CollectionUtils.isNotEmpty(gdFwList) && gdFwList.size() > 1) {
                    HashMap queryGdFwMap = new HashMap();
                    queryGdFwMap.put("isExcfwlx", "true");
                    if (yqlid.contains(","))
                        queryGdFwMap.put("qlids", Arrays.asList(yqlid.split(",")));
                    else
                        queryGdFwMap.put("qlid", yqlid);
                    gdFwList = gdFwService.getGdFw(queryGdFwMap);
                }
                //再根据匹配单元号过滤下,处理老数据一证多房，为其中一个房子发证，房屋数据叠加问题
                List<GdFw> gdFwGlList = new ArrayList<GdFw>();
                if (CollectionUtils.isNotEmpty(gdFwList) && StringUtils.isNotBlank(bdcSpxx.getBdcdyh())) {
                    for (GdFw gdFw : gdFwList) {
                        Example example = new Example(GdDyhRel.class);
                        //根据bdcdyh和fwid确定房屋信息
                        example.createCriteria().andEqualTo("bdcdyh", bdcSpxx.getBdcdyh()).andEqualTo("gdid", gdFw.getFwid());
                        List<GdDyhRel> gdDyhRelList = entityMapper.selectByExample(example);
                        if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                            gdFwGlList.add(gdFw);
                        }
                    }
                    gdFwList = gdFwGlList;
                }
                //获取土地信息
                gdTdList = gdTdService.getGdTdListByFwQlid(yqlid.split(",")[0]);
                //批量获取房屋信息，如果不是字典数据不取值
                gdFwList = bdcCheckCancelService.getGdFwFilterZdsj(gdFwList);
                //批量获取土地信息，如果不是字典数据不取值
                gdTdList = bdcCheckCancelService.getGdTdFilterZdsj(gdTdList);
            }
            bdcSpxx = readerGdxxToSpxx(gdFwList, gdTdList, bdcSpxx);
        }
        return bdcSpxx;
    }

    public BdcSpxx readerGdxxToSpxx(List<GdFw> gdFwList, List<GdTd> gdTdList, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null)
            bdcSpxx = new BdcSpxx();
        if (CollectionUtils.isNotEmpty(gdFwList)) {
            Double jzmj = 0.0;
            Double scmj = 0.0;

            for (GdFw gdFw : gdFwList) {
                if (gdFw.getJzmj() != null && gdFw.getJzmj() > 0) {
                    jzmj = jzmj + gdFw.getJzmj();
                }
                if (gdFw.getScmj() != null && gdFw.getScmj() > 0) {
                    scmj = scmj + gdFw.getScmj();
                }
            }
            //获取主房规划用途、坐落和面积
            Double zJzmj = 0.0;
            Double zScmj = 0.0;
            for (GdFw gdFw : gdFwList) {
                if (StringUtils.isBlank(gdFw.getIsfsss()) || (StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(), Constants.ISSYZD_NO))) {
                    if (StringUtils.isNotBlank(gdFw.getGhyt())) {
                        bdcSpxx.setYt(gdFw.getGhyt());
                    }

                    if (StringUtils.isNotBlank(gdFw.getFwzl())) {
                        bdcSpxx.setZl(gdFw.getFwzl());
                    }

                    if (gdFw.getJzmj() != null && gdFw.getJzmj() > 0) {
                        zJzmj += gdFw.getJzmj();
                        bdcSpxx.setMj(zJzmj);
                    }
                    if (gdFw.getScmj() != null && gdFw.getScmj() > 0) {
                        zScmj += gdFw.getScmj();
                        bdcSpxx.setScmj(zScmj);
                    }
                }
            }

            if ((bdcSpxx.getMj() == null || (bdcSpxx.getMj() != null && bdcSpxx.getMj() == 0)) && jzmj > 0) {
                bdcSpxx.setMj(jzmj);
            }

            if ((bdcSpxx.getScmj() == null || (bdcSpxx.getScmj() != null && bdcSpxx.getScmj() == 0)) && scmj > 0) {
                bdcSpxx.setScmj(scmj);
            }

            if (StringUtils.isNotBlank(gdFwList.get(0).getDw()))
                bdcSpxx.setMjdw(gdFwList.get(0).getDw());
            else
                bdcSpxx.setMjdw(Constants.DW_PFM);

            //针对没有匹配不动产单元的附属设施做解封或者做解封时赋值坐落
            if (StringUtils.isBlank(bdcSpxx.getZl()) && gdFwList.size() == 1 && StringUtils.isNotBlank(gdFwList.get(0).getFwzl()) && StringUtils.equals(gdFwList.get(0).getIsfsss(), Constants.ISSYZD_YES)) {
                bdcSpxx.setZl(gdFwList.get(0).getFwzl());
            }
        }
        if (CollectionUtils.isNotEmpty(gdTdList)) {
            GdTd gdTd = gdTdList.get(0);
            if (StringUtils.isNotBlank(gdTd.getYt()))
                bdcSpxx.setZdzhyt(gdTd.getYt());
            if (StringUtils.isNotBlank(gdTd.getSyqlx()))
                bdcSpxx.setZdzhqlxz(gdTd.getSyqlx());
            //zwq 匹配zl取gd_td  sc如果GD_FW坐落为空再读取土地的
            if (StringUtils.isBlank(bdcSpxx.getZl()) && StringUtils.isNotBlank(gdTd.getZl()))
                bdcSpxx.setZl(gdTd.getZl());
            if (StringUtils.isNotBlank(gdTd.getDw()))
                bdcSpxx.setMjdw(gdTd.getDw());
            else
                bdcSpxx.setMjdw(Constants.DW_PFM);
        }

        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromYg(final String bdcdyh, BdcSpxx bdcSpxx) {
        List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(bdcdyh, "1");
        if (CollectionUtils.isNotEmpty(bdcYgList)) {
            BdcSpxx ygBdcSpxx = queryBdcSpxxByProid(bdcYgList.get(0).getProid());
            if (ygBdcSpxx != null) {
                bdcSpxx.setZl(ygBdcSpxx.getZl());
                bdcSpxx.setBdcdyh(ygBdcSpxx.getBdcdyh());
                bdcSpxx.setBdclx(ygBdcSpxx.getBdclx());
                bdcSpxx.setGzwlx(ygBdcSpxx.getGzwlx());
                bdcSpxx.setYt(ygBdcSpxx.getYt());
                bdcSpxx.setZdzhmj(ygBdcSpxx.getZdzhmj());
                bdcSpxx.setZdzhqlxz(ygBdcSpxx.getZdzhqlxz());
                bdcSpxx.setZdzhyt(ygBdcSpxx.getZdzhyt());
                bdcSpxx.setYhlx(ygBdcSpxx.getYhlx());
                bdcSpxx.setLz(ygBdcSpxx.getLz());
                bdcSpxx.setMj(ygBdcSpxx.getMj());
            }
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxByBdcdyh(final String bdcdyh) {
        BdcSpxx bdcSpxx = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            List<BdcXm> bdcXmList = bdcXmService.queryBdcxmByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(bdcXmList))
                bdcSpxx = queryBdcSpxxByProid(bdcXmList.get(0).getProid());

        }
        return bdcSpxx;
    }

    @Override
    public void saveBdcSpxx(BdcSpxx bdcSpxx) {
        entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
    }

    @Override
    public List<BdcSpxx> getBdcSpxxByWiid(String wiid) {
        List<BdcXm> bdcXmList = null;
        ArrayList list = new ArrayList();
        if (StringUtils.isNotBlank(wiid)) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                    Example example = new Example(BdcSpxx.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
                    List<BdcSpxx> bdcSpxxList1 = entityMapper.selectByExample(BdcSpxx.class, example);
                    if (CollectionUtils.isNotEmpty(bdcSpxxList1)) {
                        list.addAll(bdcSpxxList1);
                    }
                }
            }
        }
        return list;
    }

    public BdcSpxx getBdcSpxxFromGzw(DjsjGzwxx gzwxx, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null) {
            return bdcSpxx;
        }
        String djh = "";
        if (StringUtils.isNotBlank(bdcSpxx.getBdcdyh()))
            djh = StringUtils.substring(bdcSpxx.getBdcdyh(), 0, 19);
        String zdtzm = StringUtils.substring(djh, 13, 14);
        if (StringUtils.equals(zdtzm, Constants.ZDZHTZM_H)) {
            List<DjsjZhxx> zhxxList = bdcDjsjMapper.getDjsjZhxxForDjh(djh);
            if (CollectionUtils.isNotEmpty(zhxxList)) {
                DjsjZhxx djsjZhxx = zhxxList.get(0);
                bdcSpxx = getBdcSpxxFromZh(djsjZhxx, bdcSpxx);
            }
        } else {
            List<DjsjZdxx> zdxxList = djSjMapper.getDjsjZdxxByDjhxx(djh);
            if (CollectionUtils.isNotEmpty(zdxxList)) {
                DjsjZdxx djsjZdxx = zdxxList.get(0);
                bdcSpxx = getBdcSpxxFromZd(djsjZdxx, bdcSpxx);

            }
        }

        if (gzwxx != null) {
            if (StringUtils.isNotBlank(gzwxx.getZl()))
                bdcSpxx.setZl(gzwxx.getZl());
            if (gzwxx.getMj() != null) {
                bdcSpxx.setMj(gzwxx.getMj());
                bdcSpxx.setScmj(gzwxx.getMj());
            }
            if (gzwxx.getMjdw() != null)
                bdcSpxx.setMjdw(gzwxx.getMjdw());
        }
        return bdcSpxx;
    }

    @Override
    public void batchDelBdcSpxx(List<BdcXm> bdcXmList) {
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            bdcSpxxMapper.batchDelBdcSpxx(bdcXmList);
        }
    }

    @Override
    public Double getZdzhmjByQlid(String qlid, String dqzd, String bdclx) {
        Double zdzhmj = 0.0;
        String tdsqyQlid = "";
        if (StringUtils.isNotBlank(bdclx) && StringUtils.equals(Constants.BDCLX_TD, bdclx)) {
            tdsqyQlid = qlid;
        } else {
            Example example = new Example(GdQlDyhRel.class);
            example.createCriteria().andEqualTo("qlid", qlid);
            List<GdQlDyhRel> gdQlDyhRelList = entityMapper.selectByExample(GdQlDyhRel.class, example);
            if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                GdQlDyhRel gdQlDyhRel = gdQlDyhRelList.get(0);
                if (StringUtils.isNotBlank(gdQlDyhRel.getTdqlid())) {
                    tdsqyQlid = gdQlDyhRel.getTdqlid();
                }
            }
        }
        if (StringUtils.isNotBlank(tdsqyQlid)) {
            Example tdSyqExample = new Example(GdTdsyq.class);
            tdSyqExample.createCriteria().andEqualTo("qlid", tdsqyQlid);
            List<GdTdsyq> gdTdsyqList = entityMapper.selectByExample(GdTdsyq.class, tdSyqExample);
            if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                if (StringUtils.isNotBlank(dqzd)) {
                    if (StringUtils.equals(dqzd, Constants.SPXX_ZDZHMJ_SYQMJ)) {
                        zdzhmj = gdTdsyq.getSyqmj();
                    }
                    if (StringUtils.equals(dqzd, Constants.SPXX_ZDZHMJ_TDZMJ)) {
                        zdzhmj = gdTdsyq.getTdzmj();
                    }
                }
            }
        }
        return zdzhmj;
    }

    @Override
    public Double getZdzhmj(String djh, String bdcdyh, String bdclx, List<BdcXmRel> bdcXmRelList) {
        Double zdzhmj = 0.00;
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRel.getYqlid());
                if (CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)) {
                    gdBdcQlRelList.addAll(gdBdcQlRelListTemp);
                }
            }
            List<GdDyhRel> gdDyhRelList = new ArrayList<GdDyhRel>();
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    List<GdDyhRel> gdDyhRelListTemp = gdDyhRelService.queryGdDyhRelListByBdcid(gdBdcQlRel.getBdcid());
                    if (CollectionUtils.isNotEmpty(gdDyhRelListTemp)) {
                        gdDyhRelList.addAll(gdDyhRelListTemp);
                    }
                }
            }
            //获取现势的gd_tdsyq个数
            List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
            if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                for (GdDyhRel gdDyhRel : gdDyhRelList) {
                    if (StringUtils.isNotEmpty(gdDyhRel.getTdid())) {
                        List<GdTdsyq> gdTdsyqTmplist = gdTdService.getGdTdsyqByTdid(gdDyhRel.getTdid());
                        if (CollectionUtils.isNotEmpty(gdTdsyqTmplist)) {
                            for (GdTdsyq gdTdsyq : gdTdsyqTmplist) {
                                if (gdTdsyq.getIszx() == null || gdTdsyq.getIszx() != 1) {
                                    gdTdsyqList.add(gdTdsyq);
                                }
                            }
                        }
                    }
                }
            }
            //只关联一本土地证且gd_tdsyq中syqmj不为空时读取syqmj否则读取该不动产单元在地籍调查表中的发证面积
            if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                if (gdBdcQlRelList.size() == 1) {
                    if (gdTdsyqList.get(0).getSyqmj() != null && gdTdsyqList.get(0).getSyqmj() > 0) {
                        zdzhmj = gdTdsyqList.get(0).getSyqmj();
                    } else {
                        List<DjsjZdxx> djsjZdxxList = djSjMapper.getDjsjZdxxByDjh(djh);
                        if (CollectionUtils.isNotEmpty(djsjZdxxList) && djsjZdxxList.get(0).getFzmj() != null && djsjZdxxList.get(0).getFzmj() > 0) {
                            zdzhmj = djsjZdxxList.get(0).getFzmj();
                        }
                    }
                } else {
                    List<DjsjZdxx> djsjZdxxList = djSjMapper.getDjsjZdxxByDjh(djh);
                    if (CollectionUtils.isNotEmpty(djsjZdxxList) && djsjZdxxList.get(0).getFzmj() != null && djsjZdxxList.get(0).getFzmj() > 0) {
                        zdzhmj = djsjZdxxList.get(0).getFzmj();
                    }
                }
            } else {
                List<DjsjZdxx> djsjZdxxList = djSjMapper.getDjsjZdxxByDjh(djh);
                if (CollectionUtils.isNotEmpty(djsjZdxxList) && djsjZdxxList.get(0).getFzmj() != null && djsjZdxxList.get(0).getFzmj() > 0) {
                    zdzhmj = djsjZdxxList.get(0).getFzmj();
                }
            }
            //吴江直接取权籍数据（2018-05-27）
            List<DjsjZdxx> djsjZdxxList = djSjMapper.getDjsjZdxxByDjh(djh);
            if (CollectionUtils.isNotEmpty(djsjZdxxList) && djsjZdxxList.get(0).getFzmj() != null && djsjZdxxList.get(0).getFzmj() > 0) {
                zdzhmj = djsjZdxxList.get(0).getFzmj();
            }
        }
        return zdzhmj;
    }

    @Override
    public void dealWithSpxxZdzhmj(BdcXm bdcXm) {
        if (bdcXm != null) {
            Double tdsyqmj = 0.0;
            BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(bdcXm.getProid());
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            BdcSpxx bdcSpxx = queryBdcSpxxByProid(bdcXm.getProid());
            String dytdmjAddFttdmjspxx = StringUtils.deleteWhitespace(AppConfig.getProperty("spxx.zdzhyt.dytdmjAddFttdmj"));
            if (StringUtils.isNotBlank(dytdmjAddFttdmjspxx) && StringUtils.equals(dytdmjAddFttdmjspxx, "true")) {
                if (bdcSpxx != null && StringUtils.equals(bdcSpxx.getBdclx(), Constants.BDCLX_TDFW)) {
                    tdsyqmj = getTdsyqmjmatchOneTdz(bdcXmRelList, bdcSpxx);
                    if (tdsyqmj > 0) {
                        bdcSpxx.setZdzhmj(tdsyqmj);
                        saveBdcSpxx(bdcSpxx);
                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                            BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                            if (bdcFdcq != null) {
                                bdcFdcq.setTdsyqmj(tdsyqmj);
                                bdcFdcqService.saveBdcFdcq(bdcFdcq);
                            }
                        } else {
                            BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXm.getProid());
                            if (bdcFdcqDz != null) {
                                bdcFdcqDz.setTdsyqmj(tdsyqmj);
                                bdcFdcqDzService.saveBdcFdcqDz(bdcFdcqDz);
                            }
                        }

                    } else {
                        if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC) ||
                                (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_FWSP) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWFGHBBG_DM))) {
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                                if (bdcXmRel != null) {
                                    if (StringUtils.isBlank(bdcXmRel.getYproid())) {
                                        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                                            String bdcdyFwlx = bdcdyService.getBdcdyfwlxBybdcdyh(bdcBdcdy.getBdcdyh());
                                            if (StringUtils.equals(bdcdyFwlx, "4")) {
                                                //首次登记，户室土地使用权面积取值分摊土地面积加分摊土地面积
                                                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                                                if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                                                    BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                                                    if (bdcFdcq != null) {
                                                        if (bdcFdcq.getFttdmj() != null && bdcFdcq.getFttdmj() > 0) {
                                                            tdsyqmj += bdcFdcq.getFttdmj();
                                                        }
                                                        if (tdsyqmj > 0) {
                                                            bdcSpxx.setZdzhmj(tdsyqmj);
                                                            saveBdcSpxx(bdcSpxx);
                                                            bdcFdcq.setTdsyqmj(tdsyqmj);
                                                            bdcFdcqService.saveBdcFdcq(bdcFdcq);
                                                        }
                                                    }
                                                }
                                            } else {
                                                djsjZdDcbFzmj(bdcXm, bdcBdcdy, bdcSpxx);
                                            }
                                        }
                                    } else {
                                        BdcSpxx ybdcSpxx = queryBdcSpxxByProid(bdcXmRel.getYproid());
                                        if (ybdcSpxx != null) {
                                            bdcSpxx.setZdzhmj(ybdcSpxx.getZdzhmj());
                                            saveBdcSpxx(bdcSpxx);
                                        }
                                    }
                                }
                            }
                        } else {
                            djsjZdDcbFzmj(bdcXm, bdcBdcdy, bdcSpxx);
                        }
                    }
                } else {
                    djsjZdDcbFzmj(bdcXm, bdcBdcdy, bdcSpxx);
                }
            } else {
                //yanzhenkun 吴江土地面积读取逻辑
                if (bdcBdcdy != null && StringUtils.isNotEmpty(bdcBdcdy.getBdcdyh()) && bdcBdcdy.getBdcdyh().length() > 0) {
                    String djh = bdcBdcdy.getBdcdyh().substring(0, 19);
                    if (bdcSpxx != null) {
                        tdsyqmj = getZdzhmj(djh, bdcBdcdy.getBdcdyh(), bdcSpxx.getBdclx(), bdcXmRelList);
                        if (tdsyqmj != null && tdsyqmj > 0) {
                            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                                if (bdcFdcq != null) {
                                    bdcFdcq.setTdsyqmj(tdsyqmj);
                                    bdcFdcqService.saveBdcFdcq(bdcFdcq);
                                }
                            } else {
                                BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXm.getProid());
                                if (bdcFdcqDz != null) {
                                    bdcFdcqDz.setTdsyqmj(tdsyqmj);
                                    bdcFdcqDzService.saveBdcFdcqDz(bdcFdcqDz);
                                }
                            }
                            bdcSpxx.setZdzhmj(tdsyqmj);
                            saveBdcSpxx(bdcSpxx);
                        }
                    }
                }
            }
            //土地抵押面积单独处理：取值spxx表中的zdzhmj（土地使用权面积）
            if (bdcDyaq != null) {
                if (bdcSpxx != null && bdcSpxx.getZdzhmj() != null) {
                    bdcDyaq.setTddymj(bdcSpxx.getZdzhmj());
                }
                if (bdcSpxx != null && bdcSpxx.getMj() != null) {
                    bdcDyaq.setFwdymj(bdcSpxx.getMj());
                }
                bdcDyaqService.saveBdcDyaq(bdcDyaq);
            }
        }
    }

    @Override
    public void updateSpxxByDjsj(ProjectPar projectPar) {
        BdcSpxx bdcSpxx = queryBdcSpxxByProid(projectPar.getProid());
        String spxxid = null;
        if (bdcSpxx != null) {
            spxxid = bdcSpxx.getSpxxid();
        }
        List<BdcSpxxDqService> bdcSpxxDqServices = InterfaceCodeBeanFactory.getBeans(bdcSpxxDqServiceList, "qj");
        if (CollectionUtils.isNotEmpty(bdcSpxxDqServices)) {
            for (int i = 0; i < bdcSpxxDqServices.size(); i++) {
                bdcSpxx = bdcSpxxDqServices.get(i).getCreateBdcSpxx(projectPar, bdcSpxx);
            }
        }
        bdcSpxx.setSpxxid(spxxid);
        saveBdcSpxx(bdcSpxx);
    }

    @Override
    public BdcSpxx getBdcSpxxFromProjectPar(ProjectPar projectPar, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null) {
            return bdcSpxx;
        }
        bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        bdcSpxx.setProid(projectPar.getProid());
        if (StringUtils.isNotBlank(projectPar.getBdclx())) {
            bdcSpxx.setBdclx(projectPar.getBdclx());
        }
        if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            bdcSpxx.setBdcdyh(projectPar.getBdcdyh());
        }
        /**
         *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
         *@description 在建建筑物抵押流程修改不动产类型
         */
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, projectPar.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, projectPar.getSqlx())) {
            bdcSpxx.setBdclx(Constants.BDCLX_TDFW);
        }
        return bdcSpxx;
    }

    @Override
    public BdcSpxx getBdcSpxxFromDjsjFwxx(DjsjFwxx djsjFwxx, BdcSpxx bdcSpxx, ProjectPar projectPar) {
        if (bdcSpxx == null) {
            return bdcSpxx;
        }
        if (djsjFwxx != null) {
            if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                bdcSpxx.setBdcdyh(djsjFwxx.getBdcdyh());
            }

            if (CommonUtil.indexOfStrs(Constants.SQLX_YG_YCMJ_DM, projectPar.getSqlx())) {
                if (djsjFwxx.getYcjzmj() != null && djsjFwxx.getYcjzmj() > 0) {
                    bdcSpxx.setMj(djsjFwxx.getYcjzmj());
                    bdcSpxx.setFzmj(djsjFwxx.getJzmj());
                    bdcSpxx.setScmj(djsjFwxx.getYcjzmj());
                }
            } else {
                if (djsjFwxx.getJzmj() != null && djsjFwxx.getJzmj() > 0) {
                    bdcSpxx.setMj(djsjFwxx.getJzmj());
                    bdcSpxx.setFzmj(djsjFwxx.getJzmj());
                    bdcSpxx.setScmj(djsjFwxx.getJzmj());
                }
            }

            if (StringUtils.isNotBlank(djsjFwxx.getFwyt())) {
                //将过渡期的用途转化为字典数据
                List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                String fwyt = null;
                for (BdcZdFwyt bdcZdFwyt : fwytList) {
                    if (StringUtils.equals(bdcZdFwyt.getMc(), djsjFwxx.getFwyt())) {
                        fwyt = bdcZdFwyt.getDm();
                        break;
                    } else if (StringUtils.equals(bdcZdFwyt.getDm(), djsjFwxx.getFwyt())) {
                        fwyt = bdcZdFwyt.getDm();
                        break;
                    }
                }
                bdcSpxx.setYt(fwyt);
            }
            if (StringUtils.isNotBlank(djsjFwxx.getZl())) {
                bdcSpxx.setZl(djsjFwxx.getZl());
            }
            if (djsjFwxx.getSxh() != null) {
                bdcSpxx.setSxh(djsjFwxx.getSxh());
            }
            if (CollectionUtils.isNotEmpty(djsjFwxx.getFwzbxxList())) {
                DjsjFwzbxx djsjFwzbxx = djsjFwxx.getFwzbxxList().get(0);
                if (StringUtils.isNotBlank(djsjFwzbxx.getDh())) {
                    bdcSpxx.setDh(djsjFwzbxx.getDh());
                }
            }
        }
        return bdcSpxx;
    }

    private Double getTdsyqmjmatchOneTdz(List<BdcXmRel> bdcXmRelList, BdcSpxx bdcSpxx) {
        Double tdsyqmj = 0.0;
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRel.getYqlid());
                if (CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)) {
                    gdBdcQlRelList.addAll(gdBdcQlRelListTemp);
                }
            }
            List<GdDyhRel> gdDyhRelList = new ArrayList<GdDyhRel>();
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    List<GdDyhRel> gdDyhRelListTemp = gdDyhRelService.queryGdDyhRelListByBdcid(gdBdcQlRel.getBdcid());
                    if (CollectionUtils.isNotEmpty(gdDyhRelListTemp)) {
                        gdDyhRelList.addAll(gdDyhRelListTemp);
                    }
                }
            }
            //获取现势的gd_tdsyq个数
            List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
            if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                for (GdDyhRel gdDyhRel : gdDyhRelList) {
                    if (StringUtils.isNotEmpty(gdDyhRel.getTdid()) && StringUtils.equals(bdcSpxx.getBdcdyh(), gdDyhRel.getBdcdyh())) {
                        List<GdTdsyq> gdTdsyqTmplist = gdTdService.getGdTdsyqByTdid(gdDyhRel.getTdid());
                        if (CollectionUtils.isNotEmpty(gdTdsyqTmplist)) {
                            for (GdTdsyq gdTdsyq : gdTdsyqTmplist) {
                                if (gdTdsyq.getIszx() == null || gdTdsyq.getIszx() != 1) {
                                    gdTdsyqList.add(gdTdsyq);
                                }
                            }
                        }
                    }
                }
            }
            if (gdTdsyqList != null && gdTdsyqList.size() == 1) {
                GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                if (gdTdsyq != null && gdTdsyq.getFtmj() != null && gdTdsyq.getFtmj() > 0) {
                    tdsyqmj += gdTdsyq.getFtmj();
                }
                if (gdTdsyq != null && gdTdsyq.getDymj() != null && gdTdsyq.getDymj() > 0) {
                    tdsyqmj += gdTdsyq.getDymj();
                }
            }
        }
        return tdsyqmj;
    }

    private void djsjZdDcbFzmj(BdcXm bdcXm, BdcBdcdy bdcBdcdy, BdcSpxx bdcSpxx) {
        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && bdcBdcdy.getBdcdyh().length() > 19) {
            String djh = bdcBdcdy.getBdcdyh().substring(0, 19);
            List<DjsjZdxx> djsjZdxxList = djSjMapper.getDjsjZdxxByDjh(djh);
            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                DjsjZdxx djsjZdxx = djsjZdxxList.get(0);
                if (djsjZdxx != null && djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() > 0) {
                    double tdsyqmj = djsjZdxx.getFzmj();
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                        if (bdcFdcq != null) {
                            bdcFdcq.setTdsyqmj(tdsyqmj);
                            bdcFdcqService.saveBdcFdcq(bdcFdcq);
                        }
                    } else {
                        BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXm.getProid());
                        if (bdcFdcqDz != null) {
                            bdcFdcqDz.setTdsyqmj(tdsyqmj);
                            bdcFdcqDzService.saveBdcFdcqDz(bdcFdcqDz);
                        }
                    }
                    if (bdcSpxx != null) {
                        bdcSpxx.setZdzhmj(tdsyqmj);
                        saveBdcSpxx(bdcSpxx);
                    }
                }
            }
        }
    }

    @Override
    public HashMap<String, String> getArchiveAdditionalInfo(BdcXm bdcXm) {
        HashMap<String, String> returnMap = Maps.newHashMap();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            BdcSpxx bdcSpxx = queryBdcSpxxByProid(bdcXm.getProid());
            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
            BdcFdcq bdcFdcq = CollectionUtils.isEmpty(bdcFdcqList) ? null : bdcFdcqList.get(0);
            BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXm.getProid());
            String tdOrTdfwMj = getTdFwMj(bdcSpxx, bdcXm, bdcFdcq);
            String tdOrTdfwYt = getTdFwYt(bdcSpxx, bdcXm);
            String tdOrTdfwQlxz = getTdFwxz(bdcSpxx, bdcXm, bdcFdcq, bdcFdcqDz);
            returnMap.put("tdOrTdfwMj", tdOrTdfwMj);
            returnMap.put("tdOrTdfwYt", tdOrTdfwYt);
            returnMap.put("tdOrTdfwQlxz", tdOrTdfwQlxz);
        }
        return returnMap;
    }

    private String getTdFwMj(BdcSpxx bdcSpxx, BdcXm bdcXm, BdcFdcq bdcFdcq) {
        String zdzhmj = null;
        String jzmj = null;
        if (bdcSpxx.getZdzhmj() != null) {
            zdzhmj = bdcSpxx.getZdzhmj().toString();
            if (StringUtils.equals(bdcXm.getBdclx(), Constants.BDCLX_TDFW)) {
                if (bdcFdcq != null && bdcFdcq.getJzmj() != null) {
                    jzmj = bdcFdcq.getJzmj().toString();
                } else {
                    jzmj = String.valueOf(bdcSpxx.getMj());
                }
            }
            zdzhmj = PublicUtil.combineString(zdzhmj, jzmj);
        } else {
            if (bdcSpxx.getMj() != null) {
                zdzhmj = String.valueOf(bdcSpxx.getMj());
            }
        }
        return zdzhmj;
    }

    private String getTdFwYt(BdcSpxx bdcSpxx, BdcXm bdcXm) {
        String zdzhyt = null;
        if (null != bdcSpxx && StringUtils.isNotBlank(bdcSpxx.getZdzhyt())) {
            String tdYtDm = bdcSpxx.getZdzhyt();
            String fwYtDm = null;
            String fwYt = null;
            String tdYt = "";
            Map yt = bdcZdGlService.getZdytByDm(tdYtDm);
            if (null != yt) {
                tdYt = (String) yt.get("MC");
            }
            if (StringUtils.isNotBlank(tdYt))
                zdzhyt = tdYt;
            if (bdcSpxx.getYt() != null && StringUtils.equals(bdcXm.getBdclx(), Constants.BDCLX_TDFW)) {
                fwYtDm = bdcSpxx.getYt();
            }
            if (StringUtils.isNotBlank(fwYtDm)) {
                fwYt = bdcZdGlService.getFwytByDm(fwYtDm);
            }
            zdzhyt = PublicUtil.combineString(zdzhyt, fwYt);
        }
        return zdzhyt;
    }

    private String getTdFwxz(BdcSpxx bdcSpxx, BdcXm bdcXm, BdcFdcq bdcFdcq, BdcFdcqDz bdcFdcqDz) {
        String zdzhqlxz = null;
        String djsjFwxz = null;
        if (StringUtils.isNotBlank(bdcSpxx.getZdzhqlxz())) {
            HashMap queryMap = new HashMap();
            String fwxzDm = null;
            String tdQlxzDm = bdcSpxx.getZdzhqlxz();
            queryMap.put("dm", tdQlxzDm);
            List<HashMap> resultTdMapList = bdcZdGlService.getQlxzZdb(queryMap);
            if (CollectionUtils.isNotEmpty(resultTdMapList)) {
                zdzhqlxz = resultTdMapList.get(0).get("MC").toString();
            }
            if (StringUtils.equals(bdcXm.getBdclx(), Constants.BDCLX_TDFW)) {
                if (bdcFdcq != null && bdcFdcq.getGhyt() != null) {
                    fwxzDm = bdcFdcq.getFwxz();
                } else if (bdcFdcqDz != null) {
                    fwxzDm = bdcFdcqDz.getFwxz();
                } else {
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    DjsjFwxx djsjFwxx = null;
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                        if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                            djsjFwxx = djsjFwService.getDjsjFwxx(bdcXmRel.getQjid());
                        }
                        if (djsjFwxx != null) {
                            fwxzDm = djsjFwxx.getFwxz();
                        }
                    }
                }
            }
            List<HashMap> resultFwMapList = new ArrayList<HashMap>();
            if (StringUtils.isNotBlank(fwxzDm)) {
                queryMap.clear();
                queryMap.put("dm", fwxzDm);
                resultFwMapList = bdcZdGlService.getBdcZdFwxz(queryMap);
            }
            if (CollectionUtils.isNotEmpty(resultFwMapList)) {
                djsjFwxz = resultFwMapList.get(0).get("MC").toString();
            }
            zdzhqlxz = PublicUtil.combineString(zdzhqlxz, djsjFwxz);
        }
        return zdzhqlxz;
    }

    @Override
    public BdcSpxx getBdcSpxxFromYProjectPar(ProjectPar projectPar, BdcSpxx bdcSpxx) {
        if (projectPar == null) {
            return bdcSpxx;
        }
        //存入不动产单元的实体类中
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
        }
        bdcSpxx.setSpxxid(UUIDGenerator.generate18());
        bdcSpxx.setProid(projectPar.getProid());
        if (StringUtils.isNotBlank(projectPar.getBdclx()))
            bdcSpxx.setBdclx(projectPar.getBdclx());
        if (StringUtils.isNotBlank(projectPar.getBdcdyh()))
            bdcSpxx.setBdcdyh(projectPar.getBdcdyh());
        if (StringUtils.isNotBlank(projectPar.getYxmid())) {
            BdcSpxx spxx = queryBdcSpxxByProid(projectPar.getYxmid());
            if (spxx != null) {
                if (StringUtils.isNotBlank(spxx.getZl()))
                    bdcSpxx.setZl(spxx.getZl());
                if (StringUtils.isNotBlank(spxx.getBdcdyh()))
                    bdcSpxx.setBdcdyh(spxx.getBdcdyh());
                if (StringUtils.isNotBlank(spxx.getBdclx()))
                    bdcSpxx.setBdclx(spxx.getBdclx());
                if (spxx.getMj() != null && spxx.getMj() > 0) {
                    bdcSpxx.setMj(spxx.getMj());
                }
                if (spxx.getFzmj() != null && spxx.getFzmj() > 0) {
                    bdcSpxx.setFzmj(spxx.getFzmj());
                }
                if (spxx.getScmj() != null && spxx.getScmj() > 0)
                    bdcSpxx.setScmj(spxx.getScmj());
                if (StringUtils.isNotBlank(spxx.getYt()))
                    bdcSpxx.setYt(spxx.getYt());
                if (StringUtils.isNotBlank(spxx.getYhlx()))
                    bdcSpxx.setYhlx(spxx.getYhlx());
                if (StringUtils.isNotBlank(spxx.getGzwlx()))
                    bdcSpxx.setGzwlx(spxx.getGzwlx());
                if (StringUtils.isNotBlank(spxx.getLz()))
                    bdcSpxx.setLz(spxx.getLz());
                if (StringUtils.isNotBlank(spxx.getMjdw()))
                    bdcSpxx.setMjdw(spxx.getMjdw());
                if (spxx.getZdzhmj() != null && spxx.getZdzhmj() > 0)
                    bdcSpxx.setZdzhmj(spxx.getZdzhmj());
                if (StringUtils.isNotBlank(spxx.getZdzhyt()))
                    bdcSpxx.setZdzhyt(spxx.getZdzhyt());
                if (StringUtils.isNotBlank(spxx.getZdzhqlxz())) {
                    bdcSpxx.setZdzhqlxz(spxx.getZdzhqlxz());
                } else if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                    for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                        if (!StringUtils.equals(bdcXmRel.getYproid(), projectPar.getYxmid())) {
                            BdcSpxx tdSpxx = queryBdcSpxxByProid(bdcXmRel.getYproid());
                            if (StringUtils.isNotBlank(tdSpxx.getZdzhqlxz())) {
                                bdcSpxx.setZdzhqlxz(tdSpxx.getZdzhqlxz());
                            }
                        }
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                    BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqService.getBdcJsydzjdsyq(bdcXmRel.getYproid());
                    if (bdcJsydzjdsyq != null) {
                        BdcSpxx yBdcSpxx = queryBdcSpxxByProid(bdcXmRel.getYproid());
                        if (yBdcSpxx != null) {
                            if ((bdcSpxx.getZdzhmj() == null || bdcSpxx.getZdzhmj() == 0) && (yBdcSpxx.getZdzhmj() != null && yBdcSpxx.getZdzhmj() > 0)) {
                                bdcSpxx.setZdzhmj(yBdcSpxx.getZdzhmj());
                            }
                            if (StringUtils.isBlank(bdcSpxx.getZdzhyt()) && StringUtils.isNotBlank(yBdcSpxx.getZdzhyt())) {
                                bdcSpxx.setZdzhyt(yBdcSpxx.getZdzhyt());
                            }
                            if (StringUtils.isBlank(bdcSpxx.getZdzhqlxz()) && StringUtils.isNotBlank(yBdcSpxx.getZdzhqlxz())) {
                                bdcSpxx.setZdzhqlxz(yBdcSpxx.getZdzhqlxz());
                            }
                        }
                    }
                }
            }
        }
        //在建建筑物抵押流程修改不动产类型
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, projectPar.getSqlx())) {
            bdcSpxx.setBdclx(Constants.BDCLX_TDFW);
        }
        return bdcSpxx;
    }

    @Override
    public void updateSpxxByDjsjFwXmxx(DjsjFwXmxx djsjFwXmxx, BdcSpxx bdcSpxx) {
        HashMap ljzMap = new HashMap();
        ljzMap.put("fw_xmxx_index", djsjFwXmxx.getFwXmxxIndex());
        List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(ljzMap);
        if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
            DjsjFwLjz fwLjz = djsjFwLjzList.get(0);
            double jzmj = 0;
            for (DjsjFwLjz djsjFwLjz : djsjFwLjzList) {
                jzmj = jzmj + djsjFwLjz.getScjzmj();
            }
            bdcSpxx.setScmj(jzmj);
            bdcSpxx.setZl(fwLjz.getZldz());
            bdcSpxx.setYt(fwLjz.getFwyt());
            bdcSpxx.setFcghyt(fwLjz.getFwyt());
            saveBdcSpxx(bdcSpxx);
        }
    }
}
