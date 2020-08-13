package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcXtLimitfieldMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 检查是否可以注销
 * User: lst
 * Date: 15-5-5
 * Time: 下午7:17
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcCheckCancelServiceImpl implements BdcCheckCancelService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdLqService gdLqService;
    @Autowired
    private GdCqService gdCqService;
    @Autowired
    private BdcXtLimitfieldMapper bdcXtLimitfieldMapper;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;


    @Override
    public boolean checkCancel(final String sqlxdm,final String yqllxdm,final String djlxdm,final String bdcid) {
        boolean isCancel = true;
        if (StringUtils.equals(sqlxdm, "799")) {//预告
            List<GdYg> ygList = getGdYgByProid(bdcid, 0);
            if (CollectionUtils.isEmpty(ygList)) {
                isCancel = false;
            }

        } else if (CommonUtil.indexOfStrs(Constants.DJLX_CFDJ_JF_SQLXDM, sqlxdm)) {//解封
            List<GdCf> cfList = getGdCfByProid(bdcid, 0);
            if (CollectionUtils.isEmpty(cfList)) {
                isCancel = false;
            }
        } else if (CommonUtil.indexOfStrs(Constants.DJLX_DYAQ_ZXDJ_SQLXDM, sqlxdm)&&StringUtils.equals(yqllxdm, "18")) {
            //抵押
            List<GdDy> dyList = getGdDyByProid(bdcid, 0);
            if (CollectionUtils.isEmpty(dyList)) {
                isCancel = false;
            }
        }
        return isCancel;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdDy> getGdDyByProid(final String proid,final Integer isJy) {
        List<GdDy> gdDyList = null;
        if(StringUtils.isNotBlank(proid)) {
            Example example = new Example(GdDy.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid).andEqualNvlTo("isjy", 0, isJy);
            gdDyList = entityMapper.selectByExample(example);
        }
        return gdDyList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcDyaq> getBdcDyaqByBdcdyid(final String bdcdyid,final Integer qszt) {
        List<BdcDyaq> bdcDyaqList = null;
        if(StringUtils.isNotBlank(bdcdyid)){
            Example example = new Example(BdcDyaq.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid).andEqualTo("qszt", qszt);
            bdcDyaqList = entityMapper.selectByExample(example);
        }
        return bdcDyaqList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdCf> getGdCfByProid(final String proid,final Integer qszt) {
        List<GdCf> gdCfList = null;
        if(StringUtils.isNotBlank(proid)){
            Example example = new Example(GdCf.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid).andEqualNvlTo("isjf", 0,qszt);
            gdCfList = entityMapper.selectByExample(example);
        }
        return gdCfList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcCf> getBdcCfByBdcdyid(final String bdcdyid,final Integer qszt) {
        List<BdcCf> bdcCfList = null;
        if(StringUtils.isNotBlank(bdcdyid)){
            Example example = new Example(BdcCf.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid).andEqualTo("qszt", qszt);
            bdcCfList = entityMapper.selectByExample(example);
        }
        return bdcCfList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdYg> getGdYgByProid(final String proid,final Integer iszx) {
        List<GdYg> gdYgList = null;
        if(StringUtils.isNotBlank(proid)){
            Example example = new Example(GdYg.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid).andEqualNvlTo("iszx", 0, iszx);
            gdYgList = entityMapper.selectByExample(example);
        }
        return gdYgList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcYg> getBdcYgByBdcdyid(final String bdcdyid,final Integer qszt) {
        List<BdcYg> bdcYgList = null;
        if(StringUtils.isNotBlank(bdcdyid)){
            Example example = new Example(BdcYg.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid).andEqualTo("qszt", qszt);
            bdcYgList = entityMapper.selectByExample(example);
        }
        return bdcYgList;
    }

    @Override
    @Transactional
    public Project cancelInfo(Project project, String yqllxdm, String bdcid, String lx) {
        String proid = UUIDGenerator.generate18();
        String xmbh = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        xmbh = simpleDateFormat.format(date);
        QllxVo vo = qllxService.makeSureQllx((BdcXm) project);
        if (StringUtils.equals(project.getSqlx(), "703")) {//预告
            //原项目Id赋值
            project.setYxmid(proid);
            //将gd_yg信息存入bdcYg
            List<GdYg> ygList = getGdYgByProid(bdcid, 0);
            GdYg gdYg = ygList.get(0);
            BdcYg bdcYg = new BdcYg();
            bdcYg.setProid(proid);
            bdcYg.setQlid(UUIDGenerator.generate18());
            bdcYg.setQdjg(gdYg.getQdjg());
            bdcYg.setYwh(xmbh);
            bdcYg.setQszt(2);
            //插入
            entityMapper.insertSelective(bdcYg);
            project.setQllxVo(bdcYg);
        } else if (StringUtils.equals(project.getSqlx(), "803")) {//查封
            //原项目Id赋值
            project.setYxmid(proid);
            //将gd_cf信息存入bdcCf
            List<GdCf> cfList = getGdCfByProid(bdcid, 0);
            GdCf gdCf = cfList.get(0);
            BdcCf bdcCf = new BdcCf();
            bdcCf.setProid(proid);
            bdcCf.setQlid(UUIDGenerator.generate18());
            bdcCf.setCfjg(gdCf.getCfjg());
            bdcCf.setCflx(gdCf.getCflx());
            bdcCf.setCfwj(gdCf.getCfwj());
            bdcCf.setCfwh(gdCf.getCfwh());
            bdcCf.setCfksqx(gdCf.getCfksrq());
            bdcCf.setCfjsqx(gdCf.getCfjsrq());
            bdcCf.setCffw(gdCf.getCffw());
            bdcCf.setQszt(2);
            bdcCf.setYwh(xmbh);
            //插入
            entityMapper.insertSelective(bdcCf);
            project.setQllxVo(bdcCf);
        } else if (StringUtils.equals(project.getDjlx(), "1000")) {
            //抵押
            if (StringUtils.equals(yqllxdm, "18")) {
                //原项目Id赋值
                project.setYxmid(proid);
                //将gd_dy信息存入bdcDyaq
                List<GdDy> dyList = getGdDyByProid(bdcid, 0);
                GdDy gdDy = dyList.get(0);
                BdcDyaq bdcDyaq = new BdcDyaq();
                bdcDyaq.setBdbzzqse(gdDy.getBdbzzqse());
                bdcDyaq.setDyfs(gdDy.getDyfs());
                bdcDyaq.setZjgczl(gdDy.getZjgczl());
                bdcDyaq.setZjgcdyfw(gdDy.getZjgcdyfw());
                bdcDyaq.setZgzqqdss(gdDy.getZgzqqdss());
                bdcDyaq.setZgzqqdse(gdDy.getZgzqqdse());
                bdcDyaq.setZwlxksqx(gdDy.getDyksrq());
                bdcDyaq.setZwlxjsqx(gdDy.getDyjsrq());
                bdcDyaq.setQlid(UUIDGenerator.generate18());
                bdcDyaq.setProid(proid);
                bdcDyaq.setYwh(xmbh);
                bdcDyaq.setQszt(2);
                //插入
                entityMapper.insertSelective(bdcDyaq);
                project.setQllxVo(bdcDyaq);
            } else if (StringUtils.equals(lx, Constants.BDCLX_TDFW)) {
                project.setYxmid(proid);
                //房地产权
                if (vo instanceof BdcFdcq) {
                    BdcFdcq bdcFdcq = (BdcFdcq) vo;
                    bdcFdcq.setQszt(2);
                    bdcFdcq.setQlid(UUIDGenerator.generate18());
                    bdcFdcq.setProid(proid);
                    bdcFdcq.setYwh(xmbh);
                    entityMapper.insertSelective(bdcFdcq);
                    project.setQllxVo(bdcFdcq);
                }
                //TODO  后续完善其他权利类型
            } else if (StringUtils.equals(lx, Constants.BDCLX_TDSL)) {
                project.setYxmid(proid);
            } else if (StringUtils.equals(lx, Constants.BDCLX_TDQT)) {
                project.setYxmid(proid);
            }
        }
        return project;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> checkGdFwZdsjByFwid(final String fwid) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdFwZdsjByFwid(fwid);
        if (map != null) {
            //判断共有情况字典数据
            if (map.get("GYQK") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GYQKDM_CAPITAL)))) {
                list.add("共有情况字典数据错误");
            }
            //判断规划用途字典数据
            if (map.get("GHYT") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GHYTDM_CAPITAL)))) {
                list.add("规划用途字典数据错误");
            }
            //判断房屋性质字典数据
            if (map.get("FWXZ") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWXZDM_CAPITAL)))) {
                list.add("房屋性质字典数据错误");
            }
            //判断房屋结构字典数据
            if (map.get("FWJG") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWJGDM_CAPITAL)))) {
                list.add("房屋结构字典数据错误");
            }
            //判断房屋类型字典数据
            if (map.get("FWLX") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWLXDM_CAPITAL)))) {
                list.add("房屋类型典数据错误");
            }
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getGdFwErrorZdsjByFwid(final String fwid) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdFwZdsjByFwid(fwid);
        GdFw gdFw = gdFwService.queryGdFw(fwid);
        if (map != null) {
            //判断共有情况字典数据
            if (map.get("GYQK") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GYQKDM_CAPITAL)))&&
                    gdFw != null && StringUtils.isNotBlank(gdFw.getGyqk())) {
                list.add("原房屋共有情况:" + gdFw.getGyqk());
            }
            //判断规划用途字典数据
            if (map.get("GHYT") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GHYTDM_CAPITAL)))&&
                    gdFw != null && StringUtils.isNotBlank(gdFw.getGhyt())) {
                list.add("原房屋规划用途:" + gdFw.getGhyt());
            }
            //判断房屋性质字典数据
            if (map.get("FWXZ") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWXZDM_CAPITAL)))&&
                    gdFw != null && StringUtils.isNotBlank(gdFw.getFwxz())) {
                list.add("原房屋房屋性质:" + gdFw.getFwxz());
            }
            //判断房屋结构字典数据
            if (map.get("FWJG") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWJGDM_CAPITAL)))
                    &&gdFw != null && StringUtils.isNotBlank(gdFw.getFwjg())) {
                list.add("原房屋房屋结构:" + gdFw.getFwjg());
            }
            //判断房屋类型字典数据
            if (map.get("FWLX") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWLXDM_CAPITAL)))&&
                    gdFw != null && StringUtils.isNotBlank(gdFw.getFwlx())) {
                list.add("原房屋房屋类型:" + gdFw.getFwlx());
            }
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getGdTdErrorZdsjByTdid(final String tdid,final String bdclx) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdTdZdsjByTdid(tdid);
        GdTd gdTd = gdTdService.queryGdTd(tdid);
        if (map != null) {
            //判断单位字典数据
            if (map.get("DW") != null && !StringUtils.equals(bdclx, Constants.BDCLX_TDFW)&&
                    StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("MJDM"))) && gdTd != null && StringUtils.isNotBlank(gdTd.getDw())) {
                list.add("原土地面积单位:" + gdTd.getDw() + ";");
            }
            //判断用途字典数据
            if (map.get("YT") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("YTDM")))&&
                    gdTd != null && StringUtils.isNotBlank(gdTd.getYt())) {
                list.add("原土地用途:" + gdTd.getYt() + ";");
            }
            //判断使用权类型字典数据
            if (map.get("SYQLX") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.SYQLXDM_CAPITAL)))&&
                    gdTd != null && StringUtils.isNotBlank(gdTd.getSyqlx())) {
                list.add("原土地使用权类型:" + gdTd.getSyqlx() + ";");
            }
            //判断共有情况字典数据
            if (map.get("GYQK") != null && !StringUtils.equals(bdclx, Constants.BDCLX_TDFW)&&
                    StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GYQKDM_CAPITAL))) && gdTd != null && StringUtils.isNotBlank(gdTd.getGyqk())) {
                list.add("原土地共有情况:" + gdTd.getGyqk() + ";");
            }
        }

        return list;
    }

    @Override
    public List<String> getQlrZjhlx(final String proid) {
        List<String> qlrZjhlxList = null;
        return qlrZjhlxList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getGdLqErrorZdsjByLqid(final String lqid) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdLqZdsjByLqid(lqid);
        GdLq gdLq = gdLqService.queryGdLqById(lqid);
        if (map != null&&map.get("DW") != null&&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("MJDM")))
                && gdLq != null && StringUtils.isNotBlank(gdLq.getDw())) {
            //判断单位字典数据
            list.add("面积单位:" + gdLq.getDw() + ";");
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getGdCqErrorZdsjByCqid(final String cqid) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdCqZdsjByCqid(cqid);
        GdCq gdCq = gdCqService.queryGdCqById(cqid);
        if (map != null&&map.get("DW") != null
                &&StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("MJDM"))) && gdCq != null && StringUtils.isNotBlank(gdCq.getDw())) {
            //判断单位字典数据
            list.add("面积单位:" + gdCq.getDw() + ";");
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public GdFw getGdFwFilterZdsj(GdFw gdFw) {
        if (gdFw != null && StringUtils.isNotBlank(gdFw.getFwid())) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdFwZdsjByFwid(gdFw.getFwid());
            if (map != null) {
                if (map.get("DW") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("MJDM"))) && StringUtils.isNotBlank(gdFw.getDw())) {
                        gdFw.setDw("");
                    } else if (map.get("MJDM") != null)
                        gdFw.setDw(CommonUtil.formatEmptyValue(map.get("MJDM")));
                }

                //判断共有情况字典数据
                if (map.get("GYQK") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GYQKDM_CAPITAL))) && StringUtils.isNotBlank(gdFw.getGyqk())) {
                        gdFw.setGyqk("");
                    } else if (map.get(ParamsConstants.GYQKDM_CAPITAL) != null)
                        gdFw.setGyqk(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GYQKDM_CAPITAL)));
                }
                //判断规划用途字典数据
                if (map.get("GHYT") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GHYTDM_CAPITAL))) && StringUtils.isNotBlank(gdFw.getGhyt())) {
                        gdFw.setGhyt("");
                    } else if (map.get(ParamsConstants.GHYTDM_CAPITAL) != null)
                        gdFw.setGhyt(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GHYTDM_CAPITAL)));
                }
                //判断房屋性质字典数据
                if (map.get("FWXZ") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWXZDM_CAPITAL))) && StringUtils.isNotBlank(gdFw.getFwxz())) {
                        gdFw.setFwxz("");
                    } else if (map.get(ParamsConstants.FWXZDM_CAPITAL) != null)
                        gdFw.setFwxz(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWXZDM_CAPITAL)));
                }
                //判断房屋结构字典数据
                if (map.get("FWJG") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWJGDM_CAPITAL)))&& StringUtils.isNotBlank(gdFw.getFwjg())) {
                        gdFw.setFwjg("");
                    } else if (map.get(ParamsConstants.FWJGDM_CAPITAL) != null)
                        gdFw.setFwjg(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWJGDM_CAPITAL)));
                }
                //判断房屋类型字典数据
                if (map.get("FWLX") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWLXDM_CAPITAL))) && StringUtils.isNotBlank(gdFw.getFwlx())) {
                        gdFw.setFwlx("");
                    } else if (map.get(ParamsConstants.FWLXDM_CAPITAL) != null)
                        gdFw.setFwlx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.FWLXDM_CAPITAL)));
                }
            }
        }
        return gdFw;
    }

    @Override
    @Transactional(readOnly = true)
    public GdTd getGdTdFilterZdsj(GdTd gdTd) {
        if (gdTd != null && StringUtils.isNotBlank(gdTd.getTdid())) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdTdZdsjByTdid(gdTd.getTdid());
            if (map != null) {
                //判断单位字典数据
                if (map.get("DW") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("MJDM"))) && StringUtils.isNotBlank(gdTd.getDw())) {
                        gdTd.setDw("");
                    } else if (map.get("MJDM") != null)
                        gdTd.setDw(CommonUtil.formatEmptyValue(map.get("MJDM")));
                }

                //判断用途字典数据
                if (map.get("YT") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get("YTDM"))) && StringUtils.isNotBlank(gdTd.getYt())) {
                        gdTd.setYt("");
                    } else if (map.get("YTDM") != null)
                        gdTd.setYt(CommonUtil.formatEmptyValue(map.get("YTDM")));

                }
                //判断使用权类型字典数据
                if (map.get("SYQLX") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.SYQLXDM_CAPITAL))) && StringUtils.isNotBlank(gdTd.getSyqlx())) {
                        gdTd.setSyqlx("");
                    } else if (map.get(ParamsConstants.SYQLXDM_CAPITAL) != null)
                        gdTd.setSyqlx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.SYQLXDM_CAPITAL)));


                }
                //判断共有情况字典数据
                if (map.get("GYQK") != null) {
                    if (StringUtils.isBlank(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GYQKDM_CAPITAL))) && StringUtils.isNotBlank(gdTd.getGyqk())) {
                        gdTd.setGyqk("");
                    } else if (map.get(ParamsConstants.GYQKDM_CAPITAL) != null)
                        gdTd.setGyqk(CommonUtil.formatEmptyValue(map.get(ParamsConstants.GYQKDM_CAPITAL)));

                }
            }
        }
        return gdTd;
    }

    @Override
    @Transactional(readOnly = true)
    public GdLq getGdLqFilterZdsj(GdLq gdLq) {
        if (gdLq != null && StringUtils.isNotBlank(gdLq.getLqid())) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdLqZdsjByLqid(gdLq.getLqid());
            if (map != null&&map.get("DW") != null) {
                //判断单位字典数据
                if (map.get("MJDM") == null || StringUtils.isBlank(map.get("MJDM") + "") && StringUtils.isNotBlank(gdLq.getDw())) {
                    gdLq.setDw("");
                } else if (map.get("MJDM") != null) {
                    gdLq.setDw(CommonUtil.formatEmptyValue(map.get("MJDM")));
                }
            }
        }
        return gdLq;
    }

    @Override
    @Transactional(readOnly = true)
    public GdCq getGdCqFilterZdsj(GdCq gdCq) {
        if (gdCq != null && StringUtils.isNotBlank(gdCq.getCqid())) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdCqZdsjByCqid(gdCq.getCqid());
            if (map != null&&map.get("DW") != null) {
                //判断单位字典数据
                if (map.get("MJDM") == null || StringUtils.isBlank(map.get("MJDM") + "") && StringUtils.isNotBlank(gdCq.getDw())) {
                    gdCq.setDw("");
                } else if (map.get("MJDM") != null) {
                    gdCq.setDw(CommonUtil.formatEmptyValue(map.get("MJDM")));
                }
            }
        }
        return gdCq;
    }

    @Override
    @Transactional(readOnly = true)
    public GdYg getGdYgFilterZdsj(GdYg gdYg) {
        if (gdYg != null && StringUtils.isNotBlank(gdYg.getYgid())) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdYgZdsjByYgid(gdYg.getYgid());
            if (map != null&&map.get("YGDJZL") != null) {
                //判断单位字典数据
                if (map.get(ParamsConstants.YGDJZLDM_CAPITAL) == null || StringUtils.isBlank(map.get(ParamsConstants.YGDJZLDM_CAPITAL) + "") && StringUtils.isNotBlank(gdYg.getYgdjzl())) {
                    gdYg.setYgdjzl("");
                } else if (map.get(ParamsConstants.YGDJZLDM_CAPITAL) != null) {
                    gdYg.setYgdjzl(CommonUtil.formatEmptyValue(map.get(ParamsConstants.YGDJZLDM_CAPITAL)));
                }
            }
        }
        return gdYg;
    }

    @Override
    @Transactional(readOnly = true)
    public GdCf getGdCfFilterZdsj(GdCf gdCf) {
        if (gdCf != null && StringUtils.isNotBlank(gdCf.getCfid())) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdCfZdsjByCfid(gdCf.getCfid());
            if (map != null&&map.get("CFLX") != null) {
                //判断单位字典数据
                if (map.get("CFLXDM") == null || StringUtils.isBlank(map.get("CFLXDM") + "") && StringUtils.isNotBlank(gdCf.getCflx())) {
                    gdCf.setCflx("");
                } else {
                    gdCf.setCflx(CommonUtil.formatEmptyValue(map.get("CFLX")));
                }
            }
        }
        return gdCf;
    }

    @Override
    @Transactional(readOnly = true)
    public GdDy getGdDyFilterZdsj(GdDy gdDy) {
        if (gdDy != null && StringUtils.isNotBlank(gdDy.getDyid())) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdDyZdsjByDyid(gdDy.getDyid());
            if (map != null&&map.get("DYFS") != null) {
                //判断单位字典数据
                if (map.get(ParamsConstants.DYFSDM_CAPITAL) == null || StringUtils.isBlank(map.get(ParamsConstants.DYFSDM_CAPITAL) + "")&& StringUtils.isNotBlank(gdDy.getDyfs())) {
                    gdDy.setDyfs("");
                } else if (map.get(ParamsConstants.DYFSDM_CAPITAL) != null) {
                    gdDy.setDyfs(CommonUtil.formatEmptyValue(map.get(ParamsConstants.DYFSDM_CAPITAL)));
                }
            }
        }
        return gdDy;
    }

    @Override
    @Transactional(readOnly = true)
    public GdQlr getGdQlrFilterZdsj(GdQlr gdQlr) {
        if (gdQlr != null && StringUtils.isNotBlank((gdQlr.getQlrid()))) {
            HashMap<String, Object> map = bdcXtLimitfieldMapper.checkGdQlrZdsjByQlrid(gdQlr.getQlrid());
            if (map != null&&map.get("QLRSFZJZL") != null) {
                if (map.get(ParamsConstants.ZJLXDM_CAPITAL) == null || StringUtils.isBlank(map.get(ParamsConstants.ZJLXDM_CAPITAL) + "")&& StringUtils.isNotBlank(gdQlr.getQlrsfzjzl())) {
                    gdQlr.setQlrsfzjzl("");
                } else if (map.get(ParamsConstants.ZJLXDM_CAPITAL) != null) {
                    gdQlr.setQlrsfzjzl(CommonUtil.formatEmptyValue(map.get(ParamsConstants.ZJLXDM_CAPITAL)));
                }
            }
        }
        return gdQlr;
    }

    @Override
    public List<GdYy> getGdYyByProid(final String proid,final Integer iszx) {
        List<GdYy> gdYyList = null;
        if(StringUtils.isNotBlank(proid)){
            Example example = new Example(GdYy.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid).andEqualTo("iszx", iszx);
            gdYyList = entityMapper.selectByExample(example);
        }
        return gdYyList;
    }

    @Override
    public List<BdcYy> getBdcYyByBdcdyid(final String bdcdyid,final Integer qszt) {
        List<BdcYy> bdcYyList = null;
        if(StringUtils.isNotBlank(bdcdyid)) {
            Example example = new Example(BdcYy.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid).andEqualTo("qszt", qszt);
            bdcYyList = entityMapper.selectByExample(example);
        }
        return bdcYyList;
    }

    @Override
    public List<BdcDyq> getBdcDyqByBdcdyid(final String bdcdyid,final Integer qszt) {
        List<BdcDyq> bdcDyqList = null;
        if(StringUtils.isNotBlank(bdcdyid)) {
            Example example = new Example(BdcDyq.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid).andEqualTo("qszt", qszt);
            bdcDyqList = entityMapper.selectByExample(example);
        }
        return bdcDyqList;
    }

    @Override
    public List getQlListByBdcdyid(final String bdcdyid,final Integer iszx) {
        List qlxxList = new ArrayList();
        if (StringUtils.isNotBlank(bdcdyid)) {
            List<BdcCf> bdcCfList = getBdcCfByBdcdyid(bdcdyid, iszx);
            List<BdcDyaq> bdcDyaqList = getBdcDyaqByBdcdyid(bdcdyid, iszx);
            List<BdcYg> bdcYgList = getBdcYgByBdcdyid(bdcdyid, iszx);
            List<BdcYy> bdcYyList = getBdcYyByBdcdyid(bdcdyid, iszx);
            List<BdcDyq> bdcDyqList = getBdcDyqByBdcdyid(bdcdyid, iszx);
            if (CollectionUtils.isNotEmpty(bdcDyaqList))
                qlxxList.add(bdcDyaqList);
            if (CollectionUtils.isNotEmpty(bdcCfList))
                qlxxList.add(bdcCfList);
            if (CollectionUtils.isNotEmpty(bdcYgList))
                qlxxList.add(bdcYgList);
            if (CollectionUtils.isNotEmpty(bdcYyList))
                qlxxList.add(bdcYyList);
            if (CollectionUtils.isNotEmpty(bdcDyqList))
                qlxxList.add(bdcDyqList);
        }
        return qlxxList;
    }

    @Override
    public List<GdFw> getGdFwFilterZdsj(List<GdFw> gdFwList) {
        List<GdFw> gdFws = new ArrayList<GdFw>();
        if (CollectionUtils.isNotEmpty(gdFwList)) {
            for (GdFw gdFw : gdFwList) {
                gdFw = getGdFwFilterZdsj(gdFw);
                gdFws.add(gdFw);
            }
        }
        return gdFws;
    }

    @Override
    public List<GdTd> getGdTdFilterZdsj(List<GdTd> gdTdList) {
        List<GdTd> gdTds = new ArrayList<GdTd>();
        if (CollectionUtils.isNotEmpty(gdTdList)) {
            for (GdTd gdTd : gdTdList) {
                gdTd = getGdTdFilterZdsj(gdTd);
                gdTds.add(gdTd);
            }
        }
        return gdTds;
    }
}
