package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdDhDzhMapper;
import cn.gtmap.estateplat.server.core.mapper.GdFwMapper;
import cn.gtmap.estateplat.server.core.mapper.GdTdMapper;
import cn.gtmap.estateplat.server.core.mapper.GdXmMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 过渡房屋
 * Created by lst on 2015/3/17.
 */
@Repository
public class GdFwServiceImpl implements GdFwService {
    @Autowired
    GdFwMapper gdFwMapper;

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    GdQlrService gdQlrService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    GdLqService gdLqService;
    @Autowired
    GdCqService gdCqService;
    @Autowired
    GdXmMapper gdXmMapper;
    @Autowired
    GdTdMapper gdTdMapper;

    @Autowired
    BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    GdDhDzhMapper gdDhDzhMapper;
    @Autowired
    BdcCfService bdcCfService;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    GdDyhRelService gdDyhRelService;

    @Override
    public List<GdFw> getGdFw(Map map) {
        return gdFwMapper.getGdFw(map);
    }


    @Override
    public GdFw queryGdFw(String fwId) {
        GdFw gdFw = null;
        if (StringUtils.isNotBlank(fwId)) {
            gdFw = entityMapper.selectByPrimaryKey(GdFw.class, fwId);
        }
        return gdFw;
    }

    @Override
    public BdcFdcq readBdcFdcqFromGdxx(List<GdFw> gdFwList, List<GdTd> gdTdList, BdcFdcq bdcFdcq, GdFwsyq gdFwsyq, GdTdsyq gdTdsyq, BdcXm bdcXm, GdYg gdYg) {
        Date date = null;
        if (bdcFdcq == null) {
            bdcFdcq = new BdcFdcq();
        }
        String bdcdyh = "";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                bdcdyh = bdcBdcdy.getBdcdyh();
            }
        }
        if (CollectionUtils.isNotEmpty(gdFwList)) {
            Double jzmj = 0.0;
            Double tnjzmj = 0.0;
            Double ftjzmj = 0.0;
            Double scmj = 0.0;
            for (GdFw gdFw : gdFwList) {
                Boolean needSetFwlx = false;
                List<GdDyhRel> gdDyhRelList = new ArrayList<GdDyhRel>();
                if (StringUtils.isNotBlank(gdFw.getFwid())) {
                    gdDyhRelList = gdDyhRelService.queryGdDyhRelListByBdcid(gdFw.getFwid());
                }
                if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                    for (GdDyhRel gdDyhRel : gdDyhRelList) {
                        if (StringUtils.isNotBlank(gdDyhRel.getBdcdyh()) && StringUtils.equals(gdDyhRel.getBdcdyh(), bdcdyh)) {
                            needSetFwlx = true;
                        }
                    }
                }
                if (StringUtils.isNotBlank(gdFw.getGyqk()))
                    bdcFdcq.setGyqk(gdFw.getGyqk());
                if (StringUtils.isNotBlank(gdFw.getCqly()))
                    bdcFdcq.setCqly(gdFw.getCqly());
                if (StringUtils.isNotBlank(gdFw.getFwlx()) && NumberUtils.isNumber(gdFw.getFwlx()) && needSetFwlx)
                    bdcFdcq.setFwlx(Integer.valueOf(gdFw.getFwlx()));

                //zdd 房产交易价格应该是新填写的  不应该继承
                if (gdFw.getJyjg() != null)
                    bdcFdcq.setJyjg(gdFw.getJyjg());
                if (StringUtils.isNotBlank(gdFw.getFwxz()))
                    bdcFdcq.setFwxz(gdFw.getFwxz());
                if (StringUtils.isNotBlank(gdFw.getFwjg()))
                    bdcFdcq.setFwjg(gdFw.getFwjg());
                /**
                 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                 * @description 整数存szc，非整数存szmyc
                 */
                if (StringUtils.isNotBlank(gdFw.getSzc())) {
                    if (PublicUtil.isInteger(gdFw.getSzc()))
                        bdcFdcq.setSzc(Integer.valueOf(gdFw.getSzc()));
                    else
                        bdcFdcq.setSzmyc(gdFw.getSzc());
                }

                if (gdFw.getZcs() != null)
                    bdcFdcq.setZcs(gdFw.getZcs());

                if (gdFw.getJgsj() != null)
                    bdcFdcq.setJgsj(gdFw.getJgsj());
                if (gdFw.getJzmj() != null)
                    jzmj = jzmj + gdFw.getJzmj();
                if (gdFw.getTnjzmj() != null)
                    tnjzmj = tnjzmj + gdFw.getTnjzmj();
                if (gdFw.getFtjzmj() != null)
                    ftjzmj = ftjzmj + gdFw.getFtjzmj();

                if (gdFw.getScmj() != null) {
                    scmj = scmj + gdFw.getScmj();
                }
            }

            if (jzmj > 0) {
                bdcFdcq.setJzmj(jzmj);
            }

            if ((bdcFdcq.getTnjzmj() == null || bdcFdcq.getTnjzmj() == 0) && tnjzmj > 0) {
                bdcFdcq.setTnjzmj(tnjzmj);
            }
            if ((bdcFdcq.getFtjzmj() == null || bdcFdcq.getFtjzmj() == 0) && ftjzmj > 0) {
                bdcFdcq.setFtjzmj(ftjzmj);
            }
            if (scmj > 0) {
                bdcFdcq.setScmj(scmj);
            }

            //zwq 获取gd_fwsyq的fj字段
            String pplx = AppConfig.getProperty("sjpp.type");
            if (gdFwsyq != null && StringUtils.equals(pplx, Constants.PPLX_GC))
                bdcFdcq.setFj(gdFwsyq.getFj());
        }
        if (CollectionUtils.isNotEmpty(gdTdList)) {
            GdTd gdTd = gdTdList.get(0);
            if (gdTd != null) {

                if (StringUtils.isNotBlank(gdTd.getQsrq()) && StringUtils.isNotBlank(gdTd.getZzrq())) {
                    bdcFdcq.setTdsyksqx(CalendarUtil.formatDate(gdTd.getQsrq()));
                    bdcFdcq.setTdsyjsqx(CalendarUtil.formatDate(gdTd.getZzrq()));
                }
                if (StringUtils.isBlank(gdTd.getQsrq()) && StringUtils.isNotBlank(gdTd.getZzrq())) {
                    date = gdXmService.getRqFromYt(null, gdTd.getZzrq(), gdTd.getYt(), gdTd.getSyqlx());
                    bdcFdcq.setTdsyksqx(date);
                    bdcFdcq.setTdsyjsqx(CalendarUtil.formatDate(gdTd.getZzrq()));
                }
                if (StringUtils.isNotBlank(gdTd.getQsrq()) && StringUtils.isBlank(gdTd.getZzrq())) {
                    date = gdXmService.getRqFromYt(gdTd.getQsrq(), null, gdTd.getYt(), gdTd.getSyqlx());
                    bdcFdcq.setTdsyksqx(CalendarUtil.formatDate(gdTd.getQsrq()));
                    bdcFdcq.setTdsyjsqx(date);
                }

            }
        }

        if (gdTdsyq != null) {
            bdcFdcq.setDytdmj(gdTdsyq.getDymj());
            bdcFdcq.setFttdmj(gdTdsyq.getFtmj());
        }

        //预告登记
        if (gdYg != null) {
            bdcFdcq.setJyjg(gdYg.getQdjg());
            bdcFdcq.setFj(gdYg.getFj());
        }
        return bdcFdcq;
    }


    public List<GdFwsyq> queryGdFwsyqByGdproid(String gdproid) {
        List<GdFwsyq> list = null;
        if (StringUtils.isNotBlank(gdproid)) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(ParamsConstants.PROID_LOWERCASE, gdproid);
            list = andEqualQueryGdFwsyq(map);
        }
        return list;
    }


    @Override
    public List<BdcQlr> readQlrFromGdFw(String fwid, String gdqlid) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(gdqlid)) {
            //zdd 过度抵押权转移注销登记时   直接通过权利ID读取权利人
            List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(gdqlid, Constants.QLRLX_QLR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        } else if (StringUtils.isNotBlank(fwid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCID_LOWERCASE, fwid);
            List<GdFwsyq> gdFwsyqList = andEqualQueryGdFwsyq(map);
            if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                for (GdFwsyq gdFwsyq : gdFwsyqList) {
                    List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(gdFwsyq.getQlid(), Constants.QLRLX_QLR);
                    bdcQlrList.addAll(gdQlrService.readGdQlrs(gdQlrs));
                }
            }
        }
        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> readQlrFromGdFwByGdqlid(String qlid, String proid) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(qlid, Constants.QLRLX_QLR);

        if (CollectionUtils.isNotEmpty(gdQlrList)) {
            List<GdFw> gdFwList1 = getGdFwByQlid(CommonUtil.formatEmptyValue(qlid));
            GdFw gdFw = null;
            if (CollectionUtils.isNotEmpty(gdFwList1)) {
                gdFw = bdcCheckCancelService.getGdFwFilterZdsj(gdFwList1.get(0));
            }
            if (CollectionUtils.isNotEmpty(gdQlrList)) {
                bdcQlrList.addAll(gdQlrService.readGdFwQlrs(gdQlrList, gdFw, proid));
            }
        }

        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> readYwrFromGdFwByGdqlid(String qlid, String proid) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(qlid, Constants.QLRLX_YWR);

        if (CollectionUtils.isNotEmpty(gdQlrList)) {
            List<GdFw> gdFwList1 = getGdFwByQlid(CommonUtil.formatEmptyValue(qlid));
            GdFw gdFw = null;
            if (CollectionUtils.isNotEmpty(gdFwList1))
                gdFw = bdcCheckCancelService.getGdFwFilterZdsj(gdFwList1.get(0));
            if (CollectionUtils.isNotEmpty(gdQlrList))
                bdcQlrList.addAll(gdQlrService.readGdFwQlrs(gdQlrList, gdFw, proid));
        }

        return bdcQlrList;
    }

    public List<BdcQlr> readYwrFromGdFw(String fwid, String gdqlid) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(gdqlid)) {
            //zdd 过度抵押权转移注销登记时   直接通过权利ID读取权利人
            List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(gdqlid, Constants.QLRLX_YWR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        } else if (StringUtils.isNotBlank(fwid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, fwid);
            List<GdFwsyq> gdFwsyqList = andEqualQueryGdFwsyq(map);
            if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                for (GdFwsyq gdFwsyq : gdFwsyqList) {
                    List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(gdFwsyq.getQlid(), Constants.QLRLX_YWR);
                    bdcQlrList.addAll(gdQlrService.readGdQlrs(gdQlrs));
                }
            }
        }

        return bdcQlrList;
    }

    @Override
    public List<GdCf> andEqualQueryGdCf(Map<String, Object> map) {
        List<GdCf> list = null;

        Example qllxTemp = new Example(GdCf.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                //zhouwanqing 通过这个判断现势或历史
                if (StringUtils.equals(CommonUtil.formatEmptyValue(key), "iszx")) {
                    if (val != null && NumberUtils.isNumber(CommonUtil.formatEmptyValue(val))) {
                        criteria.andEqualNvlTo("isjf", val, 0);
                    }
                } else if (val != null) {
                    criteria.andEqualTo(key.toString(), val);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdCf.class, qllxTemp);

        return list;
    }


    @Override
    public List<GdDy> andEqualQueryGdDy(Map<String, Object> map) {
        List<GdDy> list = null;
        Example qllxTemp = new Example(GdDy.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                //zhouwanqing 通过这个判断现势或历史
                if (StringUtils.equals(CommonUtil.formatEmptyValue(key), "iszx")) {
                    if (val != null && NumberUtils.isNumber(CommonUtil.formatEmptyValue(val))) {
                        criteria.andEqualNvlTo("isjy", val, 0);
                    }
                } else if (val != null) {
                    criteria.andEqualTo(key.toString(), val);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdDy.class, qllxTemp);

        return list;
    }


    @Override
    public List<GdYg> andEqualQueryGdYg(Map<String, Object> map) {
        List<GdYg> list = null;
        Example qllxTemp = new Example(GdYg.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                //zhouwanqing 通过这个判断现势或历史
                if (StringUtils.equals(CommonUtil.formatEmptyValue(key), "iszx")) {
                    if (val != null && NumberUtils.isNumber(CommonUtil.formatEmptyValue(val))) {
                        criteria.andEqualNvlTo("iszx", val, 0);
                    }
                } else if (val != null) {
                    criteria.andEqualTo(key.toString(), val);

                }
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdYg.class, qllxTemp);
        return list;
    }

    @Override
    public List<GdYy> andEqualQueryGdYy(Map<String, Object> map) {
        List<GdYy> list = null;
        Example qllxTemp = new Example(GdYy.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                //zhouwanqing 通过这个判断现势或历史
                if (StringUtils.equals(CommonUtil.formatEmptyValue(key), "iszx")) {
                    if (val != null && NumberUtils.isNumber(CommonUtil.formatEmptyValue(val))) {
                        criteria.andEqualNvlTo("iszx", val, 0);
                    }
                } else if (val != null)
                    criteria.andEqualTo(key.toString(), val);
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdYy.class, qllxTemp);
        return list;
    }

    @Override
    public List<GdFwsyq> andEqualQueryGdFwsyq(Map<String, Object> map) {
        List<GdFwsyq> list = null;
        Example qllxTemp = new Example(GdFwsyq.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                //zhouwanqing 通过这个判断现势或历史
                if (StringUtils.equals(CommonUtil.formatEmptyValue(key), "iszx")) {
                    if (val != null && NumberUtils.isNumber(CommonUtil.formatEmptyValue(val))) {
                        criteria.andEqualNvlTo("iszx", val, 0);
                    }
                } else if (val != null) {
                    criteria.andEqualTo(key.toString(), val);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdFwsyq.class, qllxTemp);
        return list;
    }

    @Override
    public void changeGdCfQszt(String gdid, Integer qszt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(ParamsConstants.BDCID_LOWERCASE, gdid);
        List<GdCf> gdCfList = andEqualQueryGdCf(map);
        if (CollectionUtils.isNotEmpty(gdCfList)) {
            for (GdCf gdCf : gdCfList) {
                gdCf.setIsjf(qszt);
                entityMapper.updateByPrimaryKeySelective(gdCf);
                changeGdqlztByQlid(gdCf.getCfid(), qszt.toString());
            }
        }
    }

    @Override
    public void changeGdDyQszt(String qlid, Integer qszt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(ParamsConstants.BDCID_LOWERCASE, qlid);
        List<GdDy> gdDyList = andEqualQueryGdDy(map);
        if (CollectionUtils.isNotEmpty(gdDyList)) {
            for (GdDy gdDy : gdDyList) {
                gdDy.setIsjy(qszt);
                entityMapper.updateByPrimaryKeySelective(gdDy);
                changeGdqlztByQlid(gdDy.getDyid(), qszt.toString());
            }
        }
    }

    @Override
    public void changeGdYgQszt(String gdid, Integer qszt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(ParamsConstants.BDCID_LOWERCASE, gdid);
        List<GdYg> gdYgList = andEqualQueryGdYg(map);
        if (CollectionUtils.isNotEmpty(gdYgList)) {
            for (GdYg gdYg : gdYgList) {
                gdYg.setIszx(qszt);
                entityMapper.updateByPrimaryKeySelective(gdYg);
                changeGdqlztByQlid(gdYg.getYgid(), qszt.toString());
            }
        }
    }

    @Override
    public List<String> getFwidByDah(String dah) {
        List<String> fwidList = null;
        if(StringUtils.isNotBlank(dah)) {
            fwidList = gdFwMapper.getFwidByDah(dah);
        }
        return fwidList;
    }


    @Override
    public GdDy getGdDyByDyid(String dyid, Integer iszx) {
        GdDy gdDy = null;
        if (StringUtils.isNotBlank(dyid)) {
            HashMap map = new HashMap();
            if (iszx != null) {
                map.put("iszx", iszx);
            }
            map.put("dyid", dyid);
            List<GdDy> gdDyList = andEqualQueryGdDy(map);
            if (CollectionUtils.isNotEmpty(gdDyList)) {
                gdDy = gdDyList.get(0);
            }
        }
        return gdDy;
    }

    @Override
    public BdcDyaq readBdcDyaqFromGdDy(GdDy gdDy, BdcDyaq bdcDyaq, GdYg gdYg) {
        if (gdDy != null) {
            if (bdcDyaq == null)
                bdcDyaq = new BdcDyaq();
            bdcDyaq.setZwlxksqx(gdDy.getDyksrq());
            bdcDyaq.setZwlxjsqx(gdDy.getDyjsrq());
            if (StringUtils.isNotBlank(gdDy.getDyfs()))
                bdcDyaq.setDyfs(gdDy.getDyfs());
            if (StringUtils.isNotBlank(gdDy.getZjgcdyfw()))
                bdcDyaq.setDbfw(gdDy.getZjgcdyfw());
            if (gdDy.getBdbzzqse() != null)
                bdcDyaq.setBdbzzqse(gdDy.getBdbzzqse());
            if (gdDy.getZgzqqdse() != null)
                bdcDyaq.setZgzqqdse(gdDy.getZgzqqdse());
            if (StringUtils.isNotBlank(gdDy.getZgzqqdss()))
                bdcDyaq.setZgzqqdss(gdDy.getZgzqqdss());
            //zwq 将gd_dy的fj字段赋给bdc_dyaq
            if (StringUtils.isNotBlank(gdDy.getFj()))
                bdcDyaq.setFj(gdDy.getFj());
            //liujie 继承bdc_dyaq中的zwr字段值
            if (StringUtils.isNotBlank(gdDy.getZwr()))
                bdcDyaq.setZwr(gdDy.getZwr());
            if (StringUtils.isNotBlank(gdDy.getDbr()))
                bdcDyaq.setDbr((gdDy.getDbr()));
            if (gdDy.getDjsj() != null)
                bdcDyaq.setDjsj(gdDy.getDjsj());
        }
        if (gdYg != null) {
            bdcDyaq.setBdbzzqse(gdYg.getQdjg());
            bdcDyaq.setZwlxksqx(gdYg.getDyksrq());
            bdcDyaq.setZwlxjsqx(gdYg.getDyjsrq());
        }
        return bdcDyaq;
    }


    @Override
    public void saveSpxxFromGdXm(BdcSpxx spxx, String gdProid) {
        if (StringUtils.isNotBlank(gdProid)) {
            GdXm gdXm = getGdXmByGdproid(gdProid);
            if (gdXm != null) {
                spxx.setCsr(gdXm.getCsr());
                spxx.setFsr(gdXm.getShr());
                entityMapper.saveOrUpdate(spxx, spxx.getProid());
            }
        }
    }

    @Override
    public GdYg getGdYgByYgid(String ygid, Integer iszx) {
        GdYg gdYg = null;
        if (StringUtils.isNotBlank(ygid)) {
            HashMap map = new HashMap();
            if (iszx != null) {
                map.put("iszx", iszx);
            }
            map.put("ygid", ygid);
            List<GdYg> gdYgList = andEqualQueryGdYg(map);
            if (CollectionUtils.isNotEmpty(gdYgList)) {
                gdYg = gdYgList.get(0);
            }
        }
        return gdYg;
    }

    @Override
    public GdCf getGdCfByCfid(String cfid, Integer iszx) {
        GdCf gdCf = null;
        if (StringUtils.isNotBlank(cfid)) {
            HashMap map = new HashMap();
            if (iszx != null) {
                map.put("iszx", iszx);
            }
            map.put("cfid", cfid);
            List<GdCf> gdCfList = andEqualQueryGdCf(map);
            if (CollectionUtils.isNotEmpty(gdCfList)) {
                gdCf = gdCfList.get(0);
            }
        }
        return gdCf;
    }


    @Override
    public GdYy getGdYyByYyid(String yyid, Integer iszx) {
        GdYy gdYy = null;
        if (StringUtils.isNotBlank(yyid)) {
            HashMap map = new HashMap();
            if (iszx != null) {
                map.put("iszx", iszx);
            }
            map.put("yyid", yyid);
            List<GdYy> gdYyList = andEqualQueryGdYy(map);
            if (CollectionUtils.isNotEmpty(gdYyList)) {
                gdYy = gdYyList.get(0);
            }
        }
        return gdYy;
    }


    @Override
    public List<GdFw> getGdFwByQlid(String qlid) {
        List<GdFw> gdFwList = null;
        if (StringUtils.isNotBlank(qlid)) {
            gdFwList = gdFwMapper.getGdFwByQlid(qlid);
        }
        return gdFwList;
    }


    @Override
    public List<GdFw> getGdFwBySplitQlid(String qlid, String split) {
        List<GdFw> gdFwList = new ArrayList<GdFw>();
        if (StringUtils.isNotBlank(qlid)) {
            if (qlid.contains(split)) {
                String[] qlidArray = qlid.split(split);
                HashMap hashMap = new HashMap();
                hashMap.put(ParamsConstants.QLIDS_LOWERCASE, Arrays.asList(qlidArray));
                gdFwList.addAll(getGdFw(hashMap));
            } else {
                gdFwList.addAll(getGdFwByQlid(qlid));
            }
        }
        return gdFwList;
    }

    @Override
    public List<GdFw> getGdFwByProid(String proid, String qlids) {
        List<GdFw> gdFwList = null;
        if (StringUtils.isNotBlank(proid) || StringUtils.isNotBlank(qlids)) {
            if (StringUtils.isBlank(qlids)) {
                qlids = getGdFwQlidsByProid(proid);
            }
            if (StringUtils.isNotBlank(qlids)) {
                HashMap queryGdFwMap = new HashMap();
                ////房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
                queryGdFwMap.put("isExcfwlx", "true");
                queryGdFwMap.put(ParamsConstants.QLIDS_LOWERCASE, qlids.split(","));
                gdFwList = getGdFw(queryGdFwMap);
            }
        }
        return gdFwList;
    }

    @Override
    public String getGdFwDahsByProid(String proid) {
        StringBuilder gdFwDahs = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            List<GdFw> gdFwList = getGdFwByProid(proid, null);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                for (GdFw gdFw : gdFwList) {
                    if ((StringUtils.indexOf(gdFwDahs, ",") < 0 && !StringUtils.equals(gdFwDahs, gdFw.getDah())) || (StringUtils.indexOf(gdFwDahs, ",") > -1 && StringUtils.indexOf("," + gdFwDahs, "," + gdFw.getDah() + ",") < 0)) {
                        if (StringUtils.isBlank(gdFwDahs)) {
                            gdFwDahs.append(gdFw.getDah());
                        } else {
                            gdFwDahs.append(",").append(gdFw.getDah());
                        }
                    }
                }
            }
        }
        return gdFwDahs.toString();
    }

    @Override
    public String getGdFwidsByProid(String proid) {
        StringBuilder gdFwids = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            List<GdFw> gdFwList = getGdFwByProid(proid, null);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                for (GdFw gdFw : gdFwList) {
                    if ((StringUtils.indexOf(gdFwids, ",") < 0 && !StringUtils.equals(gdFwids, gdFw.getFwid())) || (StringUtils.indexOf(gdFwids, ",") > -1 && StringUtils.indexOf("," + gdFwids, "," + gdFw.getFwid() + ",") < 0)) {
                        if (StringUtils.isBlank(gdFwids)) {
                            gdFwids.append(gdFw.getFwid());
                        } else {
                            gdFwids.append(",").append(gdFw.getFwid());
                        }
                    }
                }
            }
        }
        return gdFwids.toString();
    }

    @Override
    public String getGdFwQlidsByProid(String proid) {
        StringBuilder qlids = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            HashMap map = new HashMap();
            map.put("bdclx", Constants.BDCLX_TDFW);
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            List<Map> gdQlList = gdFwMapper.getGdQlList(map);
            if (CollectionUtils.isNotEmpty(gdQlList)) {
                for (Map gdQlMap : gdQlList) {
                    if (StringUtils.isBlank(qlids)) {
                        qlids.append(CommonUtil.formatEmptyValue(gdQlMap.get("QLID")));
                    } else {
                        qlids.append(",").append(CommonUtil.formatEmptyValue(gdQlMap.get("QLID")));
                    }
                }
            }
        }
        return qlids.toString();
    }

    @Override
    public List<Map> getGdFwQlListByGdproid(String gdProid, String bdclx) {
        List<Map> gdFwQlList = null;
        if(StringUtils.isNotBlank(gdProid)) {
            HashMap map = new HashMap();
            map.put("bdclx", bdclx);
            map.put(ParamsConstants.PROID_LOWERCASE, gdProid);
            gdFwQlList = gdFwMapper.getGdQlList(map);
        }
        return gdFwQlList;
    }

    @Override
    public void updateGdXmPpzt(String gdProid, String ppzt) {
        if (StringUtils.isNotBlank(gdProid)) {
            GdXm gdXm = getGdXmByGdproid(gdProid);
            if (gdXm != null) {
                gdXm.setPpzt(ppzt);
                entityMapper.updateByPrimaryKeyNull(gdXm);
            }
        }
    }

    @Override
    public GdXm getGdXmByGdproid(String gdProid) {
        GdXm gdXm = null;
        if (StringUtils.isNotBlank(gdProid)) {
            gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdProid);
        }
        return gdXm;
    }

    @Override
    public List<GdTdsyq> queryTdsyqByGdproid(String gdProid) {
        List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
        if (StringUtils.isNotBlank(gdProid)) {
            List<GdFw> gdFwList = getGdFwByProid(gdProid, null);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                for (GdFw gdFw : gdFwList) {
                    if (gdFw != null && StringUtils.isNotBlank(gdFw.getDah())) {
                        List<GdTdsyq> gdTdsyqs = gdTdService.queryTdsyqByFwid(gdFw.getDah());
                        if (gdTdsyqs != null) {
                            gdTdsyqList.addAll(gdTdsyqs);
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(gdTdsyqList)) {
            gdTdsyqList = null;
        }
        return gdTdsyqList;
    }

    @Override
    public String getFczhByFwid(String fwid) {
        String fczh = "";
        if(StringUtils.isNotBlank(fwid)) {
            fczh = gdFwMapper.getFczhByFwid(fwid);
        }
        return fczh;
    }

    @Override
    public String getFczhByGdproid(String gdProid) {
        String fczh = "";
        if(StringUtils.isNotBlank(gdProid)) {
            fczh = gdFwMapper.getFczhByGdproid(gdProid);
        }
        return fczh;
    }

    @Override
    public List<String> getGdfwZlByproid(String proid) {
        List<String> gdfwList = null;
        if(StringUtils.isNotBlank(proid)) {
            gdfwList = gdFwMapper.getGdfwZlByproid(proid);
        }
        return gdfwList;
    }

    @Override
    public List<DjbQlPro> getGdqlByBdcdyh(Map<String, String> map) {
        List<DjbQlPro> list = new ArrayList<DjbQlPro>();
        if (!map.isEmpty()) {
            List<DjbQlPro> djbQlProList = gdFwMapper.getGdqlByBdcdyh(map);
            if (CollectionUtils.isNotEmpty(djbQlProList)) {
                for (int j = 0; j < djbQlProList.size(); j++) {
                    if (djbQlProList.get(j).getQllx().equals("21")) {
                        djbQlProList.get(j).setTableName("gd_cf");
                        djbQlProList.get(j).setMc("查封权利信息");
                    } else if (djbQlProList.get(j).getQllx().equals("18")) {
                        djbQlProList.get(j).setTableName("gd_dy");
                        djbQlProList.get(j).setMc("抵押权利信息");
                    } else if (djbQlProList.get(j).getQllx().equals("4")) {
                        djbQlProList.get(j).setTableName("gd_fw");
                        djbQlProList.get(j).setMc("房屋所有权信息");
                    }
                    list.add(djbQlProList.get(j));
                }
            }
        }
        return list;
    }

    @Override
    public List<GdCf> getGdcfByGdproid(String gdProid, Integer isjf) {
        List<GdCf> gdCfList = null;
        if (StringUtils.isNotBlank(gdProid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, gdProid);
            map.put("isjf", isjf);
            gdCfList = andEqualQueryGdCf(map);
        }
        return gdCfList;
    }

    @Override
    public String getDahByFwid(String fwid) {
        String dah = "";
        GdFw gdFw = entityMapper.selectByPrimaryKey(GdFw.class, fwid);
        if (gdFw != null) {
            dah = gdFw.getDah();
        }
        return dah;
    }


    @Override
    public List<GdDy> getGdDyListByGdproid(String gdProid, Integer isjy) {
        List<GdDy> gdDyList = null;
        if (StringUtils.isNotBlank(gdProid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, gdProid);
            map.put("isjy", isjy);
            gdDyList = andEqualQueryGdDy(map);
        }
        return gdDyList;
    }

    @Override
    public List<GdYy> getGdYyListByGdproid(String gdProid, Integer iszx) {
        List<GdYy> gdYyList = null;
        if (StringUtils.isNotBlank(gdProid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, gdProid);
            map.put("iszx", iszx);
            gdYyList = andEqualQueryGdYy(map);
        }
        return gdYyList;
    }

    @Override
    public List<GdFwsyq> getGdFwsyqListByGdproid(String gdProid, Integer iszx) {
        List<GdFwsyq> gdFwsyqList = null;
        if (StringUtils.isNotBlank(gdProid)) {
            HashMap map = new HashMap();
            if (StringUtils.indexOf(gdProid, ",") > -1) {
                String[] gdproids = StringUtils.split(gdProid, ",");
                if (gdproids != null && gdproids.length > 0) {
                    Object[] gdProidObjs = gdproids;
                    List<Object> gdproidList = Arrays.asList(gdProidObjs);
                    Example example = new Example(GdFwsyq.class);
                    example.createCriteria().andEqualTo("iszx", iszx).andIn(ParamsConstants.PROID_LOWERCASE, gdproidList);
                    gdFwsyqList = entityMapper.selectByExampleNotNull(example);
                }
            } else {
                map.put(ParamsConstants.PROID_LOWERCASE, gdProid);
                map.put("iszx", iszx);
                gdFwsyqList = andEqualQueryGdFwsyq(map);
            }
        }
        return gdFwsyqList;
    }

    @Override
    public List<GdYg> getGdYgListByGdproid(String gdProid, Integer iszx) {
        List<GdYg> gdYgList = null;
        if (StringUtils.isNotBlank(gdProid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, gdProid);
            map.put("iszx", iszx);
            gdYgList = andEqualQueryGdYg(map);
        }
        return gdYgList;
    }

    @Override
    @Transactional(readOnly = true)
    public List getGdQlListByGdproid(String gdProid, Integer iszx, String bdclx) {
        List qlList = new ArrayList();
        if (StringUtils.isNotBlank(gdProid)) {


            List<GdDy> gdDyList = getGdDyListByGdproid(gdProid, iszx);
            List<GdCf> gdCfList = getGdcfByGdproid(gdProid, iszx);
            List<GdYg> gdYgList = getGdYgListByGdproid(gdProid, iszx);
            List<GdYy> gdYyList = getGdYyListByGdproid(gdProid, iszx);

            List<GdFwsyq> gdFwsyqList = getGdFwsyqListByGdproid(gdProid, iszx);
            List<GdTdsyq> gdTdsyqListList = gdTdService.getGdTdsyqListByGdproid(gdProid, iszx);
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                if (CollectionUtils.isNotEmpty(gdFwsyqList))
                    qlList.addAll(gdFwsyqList);
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                if (CollectionUtils.isNotEmpty(gdTdsyqListList))
                    qlList.addAll(gdTdsyqListList);
            } else {
                if (CollectionUtils.isNotEmpty(gdFwsyqList))
                    qlList.addAll(gdFwsyqList);
                if (CollectionUtils.isNotEmpty(gdTdsyqListList))
                    qlList.addAll(gdTdsyqListList);
            }


            if (CollectionUtils.isNotEmpty(gdDyList))
                qlList.addAll(gdDyList);
            if (CollectionUtils.isNotEmpty(gdCfList))
                qlList.addAll(gdCfList);
            if (CollectionUtils.isNotEmpty(gdYgList))
                qlList.addAll(gdYgList);
            if (CollectionUtils.isNotEmpty(gdYyList))
                qlList.addAll(gdYyList);

        }
        return qlList;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGdCqzhsByGdproid(String gdProid, Integer iszx, String bdclx, List gdQlList) {
        StringBuilder cqzhs = new StringBuilder();
        if (CollectionUtils.isEmpty(gdQlList))
            gdQlList = getGdQlListByGdproid(gdProid, iszx, bdclx);
        if (CollectionUtils.isNotEmpty(gdQlList)) {
            if (CollectionUtils.size(gdQlList) > 3) {
                for (Object qlxx : gdQlList) {
                    if (StringUtils.isBlank(cqzhs) || StringUtils.split(cqzhs.toString(), ",").length < 4) {
                        if (qlxx instanceof GdFwsyq)
                            cqzhs.append(((GdFwsyq) qlxx).getFczh()).append(",");
                        else if (qlxx instanceof GdDy)
                            cqzhs.append(((GdDy) qlxx).getDydjzmh()).append(",");
                        else if (qlxx instanceof GdYg)
                            cqzhs.append(((GdYg) qlxx).getYgdjzmh()).append(",");
                        else if (qlxx instanceof GdCf)
                            cqzhs.append(((GdCf) qlxx).getCfwh()).append(",");
                        else if (qlxx instanceof GdTdsyq)
                            cqzhs.append(((GdTdsyq) qlxx).getTdzh()).append(",");
                    }
                }
                if (StringUtils.isNotBlank(cqzhs))
                    cqzhs = new StringBuilder(StringUtils.substring(cqzhs.toString(), 0, cqzhs.length() - 1) + "等");
            } else {
                for (Object qlxx : gdQlList) {
                    if (StringUtils.isBlank(cqzhs)) {
                        if (qlxx instanceof GdFwsyq)
                            cqzhs.append(((GdFwsyq) qlxx).getFczh()).append(",");
                        else if (qlxx instanceof GdDy)
                            cqzhs.append(((GdDy) qlxx).getDydjzmh()).append(",");
                        else if (qlxx instanceof GdYg)
                            cqzhs.append(((GdYg) qlxx).getYgdjzmh()).append(",");
                        else if (qlxx instanceof GdCf)
                            cqzhs.append(((GdCf) qlxx).getCfwh()).append(",");
                        else if (qlxx instanceof GdTdsyq)
                            cqzhs.append(((GdTdsyq) qlxx).getTdzh()).append(",");
                    }
                }
                if (StringUtils.isNotBlank(cqzhs))
                    cqzhs = new StringBuilder(StringUtils.substring(cqzhs.toString(), 0, cqzhs.length() - 1));
            }
        }
        return cqzhs.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public String getQlztByGdproid(String gdProid, Integer iszx, String bdclx, List gdQlList) {
        StringBuilder qlzts = new StringBuilder();
        List<String> qlztList = new ArrayList<String>();
        if (CollectionUtils.isEmpty(gdQlList))
            gdQlList = getGdQlListByGdproid(gdProid, iszx, bdclx);
        if (CollectionUtils.isNotEmpty(gdQlList)) {
            if (gdQlList.size() == 1) {
                //一个权利
                Object qlxx = gdQlList.get(0);
                String qlid = "";
                if (qlxx instanceof GdFwsyq)
                    qlid = ((GdFwsyq) qlxx).getQlid();
                else if (qlxx instanceof GdTdsyq)
                    qlid = ((GdTdsyq) qlxx).getQlid();
                else if (qlxx instanceof GdDy && ((GdDy) qlxx).getIsjy() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdCf && ((GdCf) qlxx).getIsjf() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdYg && ((GdYg) qlxx).getIszx() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdYy && ((GdYy) qlxx).getIszx() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);


                if (StringUtils.isNotBlank(qlid)) {
                    List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcidOrQlid(null, qlid);

                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        String bdcid = gdBdcQlRelList.get(0).getBdcid();
                        if (StringUtils.isNotBlank(bdcid)) {
                            List gdQlByBdcidList = getGdQlByBdcid(bdcid, bdclx);
                            if (CollectionUtils.isNotEmpty(gdQlByBdcidList)) {
                                for (Object gdQlxx : gdQlByBdcidList) {
                                    if (gdQlxx instanceof GdDy)
                                        qlztList.add(Constants.GD_QLZT_DY);
                                    else if (gdQlxx instanceof GdYg)
                                        qlztList.add(Constants.GD_QLZT_YG);
                                    else if (gdQlxx instanceof GdCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                        qlztList.add(Constants.GD_QLZT_CF);
                                    else if (gdQlxx instanceof GdYy)
                                        qlztList.add(Constants.GD_QLZT_YY);
                                }
                            }
                            //获取不动产权利状态
                            List<BdcGdDyhRel> list = bdcGdDyhRelService.getGdDyhRel("", bdcid);
                            String bdcdyid = "";
                            if (CollectionUtils.isNotEmpty(list) && StringUtils.isNotBlank(list.get(0).getBdcdyh())) {
                                bdcdyid = bdcdyService.getBdcdyidByBdcdyh(list.get(0).getBdcdyh());
                            }
                            if (StringUtils.isNotBlank(bdcdyid)) {
                                List bdcQlList = bdcCheckCancelService.getQlListByBdcdyid(bdcdyid, Constants.QLLX_QSZT_XS);
                                if (CollectionUtils.isNotEmpty(bdcQlList)) {
                                    for (Object bdcQlxx : bdcQlList) {
                                        if (bdcQlxx instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                            qlztList.add(Constants.GD_QLZT_DY);
                                        else if (bdcQlxx instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                            qlztList.add(Constants.GD_QLZT_YG);
                                        else if (bdcQlxx instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                            qlztList.add(Constants.GD_QLZT_CF);
                                        else if (bdcQlxx instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                            qlztList.add(Constants.GD_QLZT_YY);
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (gdQlList.size() > 1) {
                //多个权利
                qlztList.add(Constants.GD_QLZT_DGQLZT);
            }
        }
        if (CollectionUtils.isNotEmpty(qlztList)) {
            for (String qlzt : qlztList) {
                if (StringUtils.isNotBlank(qlzts))
                    qlzts.append(",").append(qlzt);
                else
                    qlzts.append(qlzt);
            }
        }
        return qlzts.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdBdcQlRel> getGdBdcQlRelByBdcidOrQlid(String gdBdcid, String qlid) {
        List<GdBdcQlRel> list = null;
        Example bdcQlRelTemp = new Example(GdBdcQlRel.class);
        Example.Criteria criteria = bdcQlRelTemp.createCriteria();
        if (StringUtils.isNotBlank(gdBdcid))
            criteria.andEqualTo(ParamsConstants.BDCID_LOWERCASE, gdBdcid);
        if (StringUtils.isNotBlank(qlid))
            criteria.andEqualTo("qlid", qlid);
        if (CollectionUtils.isNotEmpty(bdcQlRelTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(bdcQlRelTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdBdcQlRel.class, bdcQlRelTemp);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List getGdQlByBdcid(String gdBdcid, String bdclx) {
        List gdQlList = new ArrayList();
        if (StringUtils.isNotBlank(gdBdcid)) {
            List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcidOrQlid(gdBdcid, null);
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    if (StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {

                        HashMap map = new HashMap();
                        if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                            map.put("qlid", gdBdcQlRel.getQlid());
                            List<GdFwsyq> gdFwsyqList = andEqualQueryGdFwsyq(map);
                            if (CollectionUtils.isNotEmpty(gdFwsyqList))
                                gdQlList.addAll(gdFwsyqList);
                        }
                        map = new HashMap();
                        map.put("dyid", gdBdcQlRel.getQlid());
                        List<GdDy> gdDyList = andEqualQueryGdDy(map);
                        if (CollectionUtils.isNotEmpty(gdDyList))
                            gdQlList.addAll(gdDyList);
                        map = new HashMap();
                        map.put("cfid", gdBdcQlRel.getQlid());
                        List<GdCf> gdCfList = andEqualQueryGdCf(map);
                        if (CollectionUtils.isNotEmpty(gdCfList))
                            gdQlList.addAll(gdCfList);
                        map = new HashMap();
                        map.put("ygid", gdBdcQlRel.getQlid());
                        List<GdYg> gdYgList = andEqualQueryGdYg(map);
                        if (CollectionUtils.isNotEmpty(gdYgList))
                            gdQlList.addAll(gdYgList);
                        map = new HashMap();
                        map.put("yyid", gdBdcQlRel.getQlid());
                        List<GdYy> gdYyList = andEqualQueryGdYy(map);
                        if (CollectionUtils.isNotEmpty(gdYyList))
                            gdQlList.addAll(gdYyList);
                        if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                            map = new HashMap();
                            map.put("qlid", gdBdcQlRel.getQlid());
                            List<GdTdsyq> gdTdsyqList = gdTdService.andEqualQueryGdTdsyq(map);
                            if (CollectionUtils.isNotEmpty(gdTdsyqList))
                                gdQlList.addAll(gdTdsyqList);
                        }
                    }
                }
            }
        }

        return gdQlList;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGdZlsByGdproid(String gdProid, Integer iszx, String bdclx, List gdQlList) {
        StringBuilder zls = new StringBuilder();
        List<String> zlList = new ArrayList<String>();
        if (StringUtils.isNotBlank(gdProid)) {
            StringBuilder qlids = new StringBuilder();
            if (CollectionUtils.isNotEmpty(gdQlList)) {
                for (Object gdQl : gdQlList) {
                    if (gdQl instanceof GdFwsyq)
                        qlids.append(((GdFwsyq) gdQl).getQlid()).append(",");
                    else if (gdQl instanceof GdDy)
                        qlids = qlids.append(((GdDy) gdQl).getDyid()).append(",");
                    else if (gdQl instanceof GdYg)
                        qlids = qlids.append(((GdYg) gdQl).getYgid()).append(",");
                    else if (gdQl instanceof GdCf)
                        qlids = qlids.append(((GdCf) gdQl).getCfid()).append(",");
                    else if (gdQl instanceof GdYy)
                        qlids = qlids.append(((GdYy) gdQl).getYyid()).append(",");
                }
                if (StringUtils.isNotBlank(qlids))
                    qlids = new StringBuilder(StringUtils.substring(qlids.toString(), 0, qlids.length() - 1));
            }
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                List<GdFw> gdFwList = getGdFwByProid(gdProid, qlids.toString());
                if (CollectionUtils.isNotEmpty(gdFwList)) {
                    for (GdFw gdFw : gdFwList) {
                        if (StringUtils.isNotBlank(gdFw.getFwzl()) && !zlList.contains(gdFw.getFwzl()))
                            zlList.add(gdFw.getFwzl());
                    }
                }
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                List<GdTd> gdTdList = gdTdService.getGdTdListByProid(gdProid, qlids.toString());
                if (CollectionUtils.isNotEmpty(gdTdList)) {
                    for (GdTd gdTd : gdTdList) {
                        if (StringUtils.isNotBlank(gdTd.getZl()) && !zlList.contains(gdTd.getZl()))
                            zlList.add(gdTd.getZl());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(zlList)) {
            if (CollectionUtils.size(zlList) > 3) {
                int i = 0;
                for (String zl : zlList) {
                    if (i < 3) {
                        if (StringUtils.isNotBlank(zls))
                            zls.append(",").append(zl);
                        else
                            zls = new StringBuilder(zl);
                    }
                    i++;
                }
                if (StringUtils.isNotBlank(zls))
                    zls.append("等");
            } else {
                for (String zl : zlList) {
                    if (StringUtils.isNotBlank(zls))
                        zls.append(",").append(zl);
                    else
                        zls = new StringBuilder(zl);
                }
            }
        }
        return zls.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public List getGdQlByCqzh(String cqzh, String bdclx) {
        List gdQlList = new ArrayList();
        if (StringUtils.isNotBlank(cqzh)) {

            HashMap map = new HashMap();
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                map.put("fczh", cqzh + "%");
                List<GdFwsyq> gdFwsyqList = andLikeQueryGdFwsyq(map);
                if (CollectionUtils.isNotEmpty(gdFwsyqList))
                    gdQlList.addAll(gdFwsyqList);
            }
            map = new HashMap();
            map.put("dydjzmh", cqzh + "%");
            List<GdDy> gdDyList = andLikeQueryGdDy(map);
            if (CollectionUtils.isNotEmpty(gdDyList))
                gdQlList.addAll(gdDyList);
            map = new HashMap();
            map.put("cfwh", cqzh + "%");
            List<GdCf> gdCfList = andLikeQueryGdCf(map);
            if (CollectionUtils.isNotEmpty(gdCfList))
                gdQlList.addAll(gdCfList);
            map = new HashMap();
            map.put("ygdjzmh", cqzh + "%");
            List<GdYg> gdYgList = andLikeQueryGdYg(map);
            if (CollectionUtils.isNotEmpty(gdYgList))
                gdQlList.addAll(gdYgList);

            if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                map = new HashMap();
                map.put("tdzh", cqzh + "%");
                List<GdTdsyq> gdTdsyqList = gdTdService.andLikeQueryGdTdsyq(map);
                if (CollectionUtils.isNotEmpty(gdTdsyqList))
                    gdQlList.addAll(gdTdsyqList);
            }

        }

        return gdQlList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdYg> andLikeQueryGdYg(Map<String, Object> map) {
        List<GdYg> list = null;
        Example qllxTemp = new Example(GdYg.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andLike(key.toString(), val.toString());
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdYg.class, qllxTemp);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdCf> andLikeQueryGdCf(Map<String, Object> map) {
        List<GdCf> list = null;

        Example qllxTemp = new Example(GdCf.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andLike(key.toString(), val.toString());
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdCf.class, qllxTemp);

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdDy> andLikeQueryGdDy(Map<String, Object> map) {
        List<GdDy> list = null;
        Example qllxTemp = new Example(GdDy.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andLike(key.toString(), val.toString());
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdDy.class, qllxTemp);

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdFwsyq> andLikeQueryGdFwsyq(Map<String, Object> map) {
        List<GdFwsyq> list = null;
        Example qllxTemp = new Example(GdFwsyq.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andLike(key.toString(), val.toString());
            }
        }
        if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdFwsyq.class, qllxTemp);
        return list;
    }

    @Override
    public String[] getFwQlidByFwzl(String fwzl) {
        List<String> idList = new ArrayList<String>();
        String[] ids = null;
        if (StringUtils.isNotBlank(fwzl)) {
            HashMap fwmap = new HashMap();
            fwmap.put("fwzl", fwzl);
            List<GdFw> gdFwList = getGdFw(fwmap);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                for (GdFw gdFw : gdFwList) {
                    if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(gdFw.getFwid()))) {
                        List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcidOrQlid(gdFw.getFwid(), null);
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(gdBdcQlRel.getQlid())) && !idList.contains(CommonUtil.formatEmptyValue(gdBdcQlRel.getQlid())))
                                    idList.add(CommonUtil.formatEmptyValue(gdBdcQlRel.getQlid()));
                            }
                        }
                    }

                }
            }
        }
        if (CollectionUtils.isNotEmpty(idList)) {
            ids = new String[idList.size()];
            int i = 0;
            for (String id : idList) {
                ids[i] = id;
                i++;
            }
        }
        return ids;
    }

    @Override
    @Transactional(readOnly = true)
    public String getQlztByQlid(String qlid, Integer iszx, String bdclx, List gdQlList) {
        StringBuilder qlzts = new StringBuilder();
        List<String> qlztList = new ArrayList<String>();
        if (CollectionUtils.isEmpty(gdQlList))
            gdQlList = getGdQlListByGdproid(qlid, iszx, bdclx);
        if (CollectionUtils.isNotEmpty(gdQlList)) {
            if (gdQlList.size() == 1) {
                //一个权利
                Object qlxx = gdQlList.get(0);
                if (qlxx instanceof GdFwsyq && ((GdFwsyq) qlxx).getIszx() != null && ((GdFwsyq) qlxx).getIszx() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdDy && ((GdDy) qlxx).getIsjy() != null && ((GdDy) qlxx).getIsjy() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdCf && ((GdCf) qlxx).getIsjf() != null && ((GdCf) qlxx).getIsjf() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdYg && ((GdYg) qlxx).getIszx() != null && ((GdYg) qlxx).getIszx() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdYy && ((GdYy) qlxx).getIszx() != null && ((GdYy) qlxx).getIszx() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdTdsyq && ((GdTdsyq) qlxx).getIszx() != null && ((GdTdsyq) qlxx).getIszx() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);
                else if (qlxx instanceof GdLq && ((GdLq) qlxx).getIszx() != null && ((GdLq) qlxx).getIszx() == 1)
                    qlztList.add(Constants.GD_QLZT_ZX);

                Boolean isZx = false;
                if (CollectionUtils.isNotEmpty(qlztList)) {
                    isZx = true;
                }
                //zwq 过渡抵押，预告，异议，查封状态只能为 正常或注销，没必要进行下列的判断
                if (StringUtils.isNotBlank(qlid) && (qlxx instanceof GdFwsyq || qlxx instanceof GdTdsyq || qlxx instanceof GdLq || qlxx instanceof GdCq)) {
                    List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcidOrQlid(null, qlid);
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        //可能会有多个房屋，需完善
                        String bdcid = gdBdcQlRelList.get(0).getBdcid();
                        if (StringUtils.isNotBlank(bdcid)) {
                            List gdQlByBdcidList = getGdQlByBdcid(bdcid, bdclx);
                            if (CollectionUtils.isNotEmpty(gdQlByBdcidList)) {
                                for (Object gdQlxx : gdQlByBdcidList) {
                                    if (gdQlxx instanceof GdDy && !StringUtils.equals(((GdDy) gdQlxx).getDyid(), qlid) && !(((GdDy) gdQlxx).getIsjy() != null && ((GdDy) gdQlxx).getIsjy() == 1))
                                        qlztList.add(Constants.GD_QLZT_DY);
                                    else if (gdQlxx instanceof GdYg && !StringUtils.equals(((GdYg) gdQlxx).getYgid(), qlid) && !(((GdYg) gdQlxx).getIszx() != null && ((GdYg) gdQlxx).getIszx() == 1))
                                        qlztList.add(Constants.GD_QLZT_YG);
                                    else if (gdQlxx instanceof GdCf && !StringUtils.equals(((GdCf) gdQlxx).getCfid(), qlid) && !(((GdCf) gdQlxx).getIsjf() != null && ((GdCf) gdQlxx).getIsjf() == 1) && !qlztList.contains(Constants.GD_QLZT_CF))
                                        qlztList.add(Constants.GD_QLZT_CF);
                                    else if (gdQlxx instanceof GdYy && !StringUtils.equals(((GdYy) gdQlxx).getYyid(), qlid) && !(((GdYy) gdQlxx).getIszx() != null && ((GdYy) gdQlxx).getIszx() == 1))
                                        qlztList.add(Constants.GD_QLZT_YY);
                                }
                            }
                            //获取不动产权利状态
                            List<BdcGdDyhRel> list = null;
                            List<BdcGdDyhRel> list1 = new ArrayList<BdcGdDyhRel>();
                            if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                                list = bdcGdDyhRelService.getTdDyhRelBytdids(bdcid);
                                list1 = bdcGdDyhRelService.getGdDyhRel(bdcid);
                            } else {
                                list = bdcGdDyhRelService.getGdDyhRel("", bdcid);
                            }
                            String bdcdyid = "";
                            if (StringUtils.isNotBlank(bdcid)) {
                                List<BdcGdDyhRel> bdcGdDyhRels = bdcGdDyhRelService.getGdDyhRelByGdId(bdcid);
                                if (CollectionUtils.isNotEmpty(bdcGdDyhRels)) {
                                    if (CollectionUtils.isNotEmpty(list) && CollectionUtils.size(list) == 1 && StringUtils.isNotBlank(list.get(0).getBdcdyh())) {
                                        bdcdyid = bdcdyService.getBdcdyidByBdcdyh(list.get(0).getBdcdyh());
                                    }
                                } else {
                                    String bdcdyh;
                                    if (getBdcdyhAndFwid("", bdcid) != null) {
                                        bdcdyh = getBdcdyhAndFwid("", bdcid).get("BDCDYH");
                                        if (StringUtils.isNotBlank(bdcdyh)) {
                                            bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
                                        }
                                    }
                                }
                                if (StringUtils.isNotBlank(bdcdyid)) {
                                    List bdcQlList = bdcCheckCancelService.getQlListByBdcdyid(bdcdyid, Constants.QLLX_QSZT_XS);
                                    if (CollectionUtils.isNotEmpty(bdcQlList)) {
                                        for (Object bdcQlxx : bdcQlList) {
                                            if (bdcQlxx instanceof ArrayList) {
                                                for (Object bdcQlxx1 : (ArrayList) bdcQlxx) {
                                                    if (bdcQlxx1 instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                                        qlztList.add(Constants.GD_QLZT_DY);
                                                    else if (bdcQlxx1 instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                                        qlztList.add(Constants.GD_QLZT_YG);
                                                    else if (bdcQlxx1 instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                                        qlztList.add(Constants.GD_QLZT_DYI);
                                                    else if (bdcQlxx1 instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                                        qlztList.add(Constants.GD_QLZT_CF);
                                                    else if (bdcQlxx1 instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                                        qlztList.add(Constants.GD_QLZT_YY);
                                                }
                                            } else {
                                                if (bdcQlxx instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                                    qlztList.add(Constants.GD_QLZT_DY);
                                                else if (bdcQlxx instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                                    qlztList.add(Constants.GD_QLZT_YG);
                                                else if (bdcQlxx instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                                    qlztList.add(Constants.GD_QLZT_CF);
                                                else if (bdcQlxx instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                                    qlztList.add(Constants.GD_QLZT_DYI);
                                                else if (bdcQlxx instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                                    qlztList.add(Constants.GD_QLZT_YY);
                                            }

                                        }
                                    }
                                }
                            }
                            /**
                             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                             * @description 根据房产证proid查询权利
                             */
                            if (gdQlList.get(0) instanceof GdFwsyq) {
                                GdFwsyq gdFwsyq = (GdFwsyq) gdQlList.get(0);
                                Example exampleXmRel = new Example(BdcXmRel.class);
                                exampleXmRel.createCriteria().andEqualTo("yproid", gdFwsyq.getProid());
                                List<BdcXmRel> bdcXmRel = entityMapper.selectByExample(BdcXmRel.class, exampleXmRel);
                                if (CollectionUtils.isNotEmpty(bdcXmRel)) {
                                    for (BdcXmRel bdcXmRe1l : bdcXmRel) {
                                        BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRe1l.getProid());
                                        if (bdcCf != null && !Constants.QLLX_QSZT_HR.equals(bdcCf.getQszt())) {
                                            qlztList.add(Constants.GD_QLZT_CF);
                                        }
                                        HashMap mapdy = new HashMap();
                                        mapdy.put(ParamsConstants.PROID_LOWERCASE, bdcXmRe1l.getProid());
                                        mapdy.put("qszt", 1);
                                        List<BdcDyaq> bdcDyaq = bdcDyaqService.queryBdcDyaq(mapdy);
                                        if (CollectionUtils.isNotEmpty(bdcDyaq)) {
                                            qlztList.add(Constants.GD_QLZT_DY);
                                        }
                                    }
                                }
                            } else if (gdQlList.get(0) instanceof GdTdsyq) {
                                GdTdsyq gdTdsyq = (GdTdsyq) gdQlList.get(0);
                                Example exampleXmRel = new Example(BdcXmRel.class);
                                exampleXmRel.createCriteria().andEqualTo("yproid", gdTdsyq.getProid());
                                List<BdcXmRel> bdcXmRel = entityMapper.selectByExample(BdcXmRel.class, exampleXmRel);
                                if (CollectionUtils.isNotEmpty(bdcXmRel)) {
                                    for (BdcXmRel bdcXmRe1l : bdcXmRel) {
                                        BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRe1l.getProid());
                                        if (bdcCf != null && bdcCf.getQszt() != Constants.QLLX_QSZT_HR) {
                                            qlztList.add(Constants.GD_QLZT_CF);
                                        }
                                        HashMap mapdy = new HashMap();
                                        mapdy.put(ParamsConstants.PROID_LOWERCASE, bdcXmRe1l.getProid());
                                        mapdy.put("qszt", 1);
                                        List<BdcDyaq> bdcDyaq = bdcDyaqService.queryBdcDyaq(mapdy);
                                        if (CollectionUtils.isNotEmpty(bdcDyaq)) {
                                            qlztList.add(Constants.GD_QLZT_DY);
                                        }
                                    }
                                }
                            }
                            /**
                             * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
                             * @description 土地证状态，不仅要查对应土地不动产权利的状态还要查对应房屋不动产权利的状态
                             */
                            String bdcdyid1 = "";
                            if (CollectionUtils.isNotEmpty(list1) && !isZx) {
                                for (BdcGdDyhRel dyhList : list1) {
                                    bdcdyid1 = bdcdyService.getBdcdyidByBdcdyh(dyhList.getBdcdyh());
                                    if (StringUtils.isNotBlank(bdcdyid1)) {
                                        List bdcQlList = bdcCheckCancelService.getQlListByBdcdyid(bdcdyid1, Constants.QLLX_QSZT_XS);
                                        if (CollectionUtils.isNotEmpty(bdcQlList)) {
                                            for (Object bdcQlxx : bdcQlList) {
                                                if (bdcQlxx instanceof ArrayList) {
                                                    for (Object bdcQlxx1 : (ArrayList) bdcQlxx) {
                                                        if (bdcQlxx1 instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                                            qlztList.add(Constants.GD_QLZT_DY);
                                                        else if (bdcQlxx1 instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                                            qlztList.add(Constants.GD_QLZT_YG);
                                                        else if (bdcQlxx1 instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                                            qlztList.add(Constants.GD_QLZT_DYI);
                                                        else if (bdcQlxx1 instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                                            qlztList.add(Constants.GD_QLZT_CF);
                                                        else if (bdcQlxx1 instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                                            qlztList.add(Constants.GD_QLZT_YY);
                                                    }
                                                } else {
                                                    if (bdcQlxx instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                                        qlztList.add(Constants.GD_QLZT_DY);
                                                    else if (bdcQlxx instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                                        qlztList.add(Constants.GD_QLZT_YG);
                                                    else if (bdcQlxx instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                                        qlztList.add(Constants.GD_QLZT_CF);
                                                    else if (bdcQlxx instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                                        qlztList.add(Constants.GD_QLZT_DYI);
                                                    else if (bdcQlxx instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                                        qlztList.add(Constants.GD_QLZT_YY);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        List<BdcGdDyhRel> bdcGdDyhRels = bdcGdDyhRelService.getGdDyhRelByGdId(qlid);
                        if (CollectionUtils.isNotEmpty(bdcGdDyhRels)) {
                            for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRels) {
                                String bdcdyh = bdcGdDyhRel.getBdcdyh();
                                String bdcdyid = StringUtils.isNotBlank(bdcdyh) ? StringUtils.isNotBlank(bdcdyService.getBdcdyidByBdcdyh(bdcdyh)) ? bdcdyService.getBdcdyidByBdcdyh(bdcdyh) : "" : "";
                                if (StringUtils.isNotBlank(bdcdyid)) {
                                    List bdcQlList = bdcCheckCancelService.getQlListByBdcdyid(bdcdyid, Constants.QLLX_QSZT_XS);
                                    if (CollectionUtils.isNotEmpty(bdcQlList)) {
                                        for (Object bdcQlxx : bdcQlList) {
                                            if (bdcQlxx instanceof ArrayList) {
                                                for (Object bdcQlxx1 : (ArrayList) bdcQlxx) {
                                                    if (bdcQlxx1 instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                                        qlztList.add(Constants.GD_QLZT_DY);
                                                    else if (bdcQlxx1 instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                                        qlztList.add(Constants.GD_QLZT_YG);
                                                    else if (bdcQlxx1 instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                                        qlztList.add(Constants.GD_QLZT_DYI);
                                                    else if (bdcQlxx1 instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                                        qlztList.add(Constants.GD_QLZT_CF);
                                                    else if (bdcQlxx1 instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                                        qlztList.add(Constants.GD_QLZT_YY);
                                                }
                                            } else {
                                                if (bdcQlxx instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                                    qlztList.add(Constants.GD_QLZT_DY);
                                                else if (bdcQlxx instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                                    qlztList.add(Constants.GD_QLZT_YG);
                                                else if (bdcQlxx instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                                    qlztList.add(Constants.GD_QLZT_CF);
                                                else if (bdcQlxx instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                                    qlztList.add(Constants.GD_QLZT_DYI);
                                                else if (bdcQlxx instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                                    qlztList.add(Constants.GD_QLZT_YY);
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            } else if (gdQlList.size() > 1) {
                //多个权利
                qlztList.add(Constants.GD_QLZT_DGQLZT);
            }
        }
        if (CollectionUtils.isNotEmpty(qlztList)) {
            for (String qlzt : qlztList) {
                //zhouwanqing 防止在匹配界面显示多个相同的权利
                if (StringUtils.isNotBlank(qlzts)) {
                    if (qlzts.indexOf(qlzt) == -1) {
                        qlzts.append(",").append(qlzt);
                    }
                } else {
                    qlzts.append(qlzt);
                }
            }
        }
        return qlzts.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public List getGdQlListByQlid(String qlid, Integer iszx, String bdclx) {
        List qlList = new ArrayList();
        if (StringUtils.isNotBlank(qlid)) {
            GdDy gdDy = getGdDyByDyid(qlid, iszx);
            GdCf gdCf = getGdCfByCfid(qlid, iszx);
            GdYg gdYg = getGdYgByYgid(qlid, iszx);
            GdYy gdYy = getGdYyByYyid(qlid, iszx);

            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                GdFwsyq gdFwsyq = getGdFwsyqByQlid(qlid);
                if (gdFwsyq != null)
                    qlList.add(gdFwsyq);
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(qlid);
                if (gdTdsyq != null)
                    qlList.add(gdTdsyq);
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_LQ)) {
                GdLq gdLq = gdLqService.queryGdLqById(qlid);
                if (gdLq != null) {
                    qlList.add(gdLq);
                }
            }


            if (gdDy != null)
                qlList.add(gdDy);
            if (gdCf != null)
                qlList.add(gdCf);
            if (gdYg != null)
                qlList.add(gdYg);
            if (gdYy != null)
                qlList.add(gdYy);
        }
        return qlList;
    }

    @Override
    @Transactional(readOnly = true)
    public GdFwsyq getGdFwsyqByQlid(String qlid) {
        GdFwsyq gdFwsyq = null;
        if (StringUtils.isNotBlank(qlid)) {
            HashMap map = new HashMap();
            map.put("qlid", qlid);
            List<GdFwsyq> gdFwsyqList = andEqualQueryGdFwsyq(map);
            if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                gdFwsyq = gdFwsyqList.get(0);
            }
        }
        return gdFwsyq;
    }

    public GdFwsyq getGdFwsyqByYgQlid(String ygid) {
        GdFwsyq gdFwsyq = null;
        if(StringUtils.isNotBlank(ygid)) {
            gdFwsyq = gdFwMapper.getGdFwsyqByYgQlid(ygid);
        }
        return gdFwsyq;
    }


    @Override
    public List<GdTdsyq> queryTdsyqByQlid(String qlid) {
        List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
        if (StringUtils.isNotBlank(qlid)) {
            List<GdFw> gdFwList = getGdFwByQlid(qlid);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                for (GdFw gdFw : gdFwList) {
                    if (gdFw != null && StringUtils.isNotBlank(gdFw.getDah())) {
                        List<GdTdsyq> gdTdsyqs = gdTdService.queryTdsyqByFwid(gdFw.getDah());
                        if (gdTdsyqs != null)
                            gdTdsyqList.addAll(gdTdsyqs);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(gdTdsyqList)) {
            gdTdsyqList = null;
        }
        return gdTdsyqList;
    }

    @Override
    @Transactional(readOnly = true)
    public String getQlztByBdcid(String bdcid, Integer iszx, String bdclx) {
        StringBuilder qlzts = new StringBuilder();
        List<String> qlztList = new ArrayList<String>();
        if (StringUtils.isNotBlank(bdcid)) {
            List gdQlByBdcidList = getGdQlByBdcid(bdcid, bdclx);
            if (CollectionUtils.isNotEmpty(gdQlByBdcidList)) {
                for (Object gdQlxx : gdQlByBdcidList) {
                    if (gdQlxx instanceof GdDy && !(((GdDy) gdQlxx).getIsjy() != null && ((GdDy) gdQlxx).getIsjy() == 1))
                        qlztList.add(Constants.GD_QLZT_DY);
                    else if (gdQlxx instanceof GdYg && !(((GdYg) gdQlxx).getIszx() != null && ((GdYg) gdQlxx).getIszx() == 1))
                        qlztList.add(Constants.GD_QLZT_YG);
                    else if (gdQlxx instanceof GdCf && !(((GdCf) gdQlxx).getIsjf() != null && ((GdCf) gdQlxx).getIsjf() == 1) && !qlztList.contains(Constants.GD_QLZT_CF))
                        qlztList.add(Constants.GD_QLZT_CF);
                    else if (gdQlxx instanceof GdYy && !(((GdYy) gdQlxx).getIszx() != null && ((GdYy) gdQlxx).getIszx() == 1))
                        qlztList.add(Constants.GD_QLZT_YY);
                    else if (gdQlxx instanceof GdTdsyq && ((GdTdsyq) gdQlxx).getIszx() != null && ((GdTdsyq) gdQlxx).getIszx() == 1)
                        qlztList.add(Constants.GD_QLZT_ZX);
                    else if (gdQlxx instanceof GdFwsyq && ((GdFwsyq) gdQlxx).getIszx() != null && ((GdFwsyq) gdQlxx).getIszx() == 1)
                        qlztList.add(Constants.GD_QLZT_ZX);
                }
            }

            List<BdcGdDyhRel> list = null;
            List<BdcGdDyhRel> list1 = new ArrayList<BdcGdDyhRel>();
            if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                list = bdcGdDyhRelService.getGdDyhRel("", bdcid);
                list1 = bdcGdDyhRelService.getGdDyhRel(bdcid);
            } else {
                list = bdcGdDyhRelService.getGdDyhRel("", bdcid);
            }
            //获取不动产权利状态
            String bdcdyid = "";
            if (CollectionUtils.isNotEmpty(list) && CollectionUtils.size(list) == 1 && StringUtils.isNotBlank(list.get(0).getBdcdyh())) {
                bdcdyid = bdcdyService.getBdcdyidByBdcdyh(list.get(0).getBdcdyh());
            }
            if (StringUtils.isNotBlank(bdcdyid)) {
                List bdcQlList = bdcCheckCancelService.getQlListByBdcdyid(bdcdyid, Constants.QLLX_QSZT_XS);
                if (CollectionUtils.isNotEmpty(bdcQlList)) {
                    for (Object bdcQlxx : bdcQlList) {
                        if (bdcQlxx instanceof ArrayList) {
                            for (Object bdcQlxx1 : (ArrayList) bdcQlxx) {
                                if (bdcQlxx1 instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                    qlztList.add(Constants.GD_QLZT_DY);
                                else if (bdcQlxx1 instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                    qlztList.add(Constants.GD_QLZT_YG);
                                else if (bdcQlxx1 instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                    qlztList.add(Constants.GD_QLZT_DYI);
                                else if (bdcQlxx1 instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                    qlztList.add(Constants.GD_QLZT_CF);
                                else if (bdcQlxx1 instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                    qlztList.add(Constants.GD_QLZT_YY);
                            }
                        } else {
                            if (bdcQlxx instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                qlztList.add(Constants.GD_QLZT_DY);
                            else if (bdcQlxx instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                qlztList.add(Constants.GD_QLZT_YG);
                            else if (bdcQlxx instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                qlztList.add(Constants.GD_QLZT_CF);
                            else if (bdcQlxx instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                qlztList.add(Constants.GD_QLZT_DYI);
                            else if (bdcQlxx instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                qlztList.add(Constants.GD_QLZT_YY);
                        }

                    }
                }
            }
            /**
             * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
             * @description 土地证状态，不仅要查对应土地不动产权利的状态还要查对应房屋不动产权利的状态
             */
            String bdcdyid1 = "";
            if (CollectionUtils.isNotEmpty(list1)) {
                for (BdcGdDyhRel dyhList : list1) {
                    bdcdyid1 = bdcdyService.getBdcdyidByBdcdyh(dyhList.getBdcdyh());
                    if (StringUtils.isNotBlank(bdcdyid1)) {
                        List bdcQlList = bdcCheckCancelService.getQlListByBdcdyid(bdcdyid1, Constants.QLLX_QSZT_XS);
                        if (CollectionUtils.isNotEmpty(bdcQlList)) {
                            for (Object bdcQlxx : bdcQlList) {
                                if (bdcQlxx instanceof ArrayList) {
                                    for (Object bdcQlxx1 : (ArrayList) bdcQlxx) {
                                        if (bdcQlxx1 instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                            qlztList.add(Constants.GD_QLZT_DY);
                                        else if (bdcQlxx1 instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                            qlztList.add(Constants.GD_QLZT_YG);
                                        else if (bdcQlxx1 instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                            qlztList.add(Constants.GD_QLZT_DYI);
                                        else if (bdcQlxx1 instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                            qlztList.add(Constants.GD_QLZT_CF);
                                        else if (bdcQlxx1 instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                            qlztList.add(Constants.GD_QLZT_YY);
                                    }
                                } else {
                                    if (bdcQlxx instanceof BdcDyaq && !qlztList.contains(Constants.GD_QLZT_DY))
                                        qlztList.add(Constants.GD_QLZT_DY);
                                    else if (bdcQlxx instanceof BdcYg && !qlztList.contains(Constants.GD_QLZT_YG))
                                        qlztList.add(Constants.GD_QLZT_YG);
                                    else if (bdcQlxx instanceof BdcCf && !qlztList.contains(Constants.GD_QLZT_CF))
                                        qlztList.add(Constants.GD_QLZT_CF);
                                    else if (bdcQlxx instanceof BdcDyq && !qlztList.contains(Constants.GD_QLZT_DYI))
                                        qlztList.add(Constants.GD_QLZT_DYI);
                                    else if (bdcQlxx instanceof BdcYy && !qlztList.contains(Constants.GD_QLZT_YY))
                                        qlztList.add(Constants.GD_QLZT_YY);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(qlztList)) {
            for (String qlzt : qlztList) {
                if (StringUtils.isNotBlank(qlzts)) {
                    if (qlzts.indexOf(qlzt) == -1) {
                        qlzts.append(",").append(qlzt);
                    }
                } else {
                    qlzts.append(qlzt);
                }
            }
        }
        return qlzts.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public String getPpztByQlid(String qlid, String bdclx, String proid) {
        String ppzt = Constants.GD_PPZT_WPP;
        if (StringUtils.isNotBlank(qlid) && StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            List<GdFw> gdFwList = getGdFwByQlid(qlid);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                int i = 0;
                for (GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> gdBdcQlRelList = bdcGdDyhRelService.getGdDyhRel("", gdFw.getFwid());
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList) && StringUtils.isNotBlank(gdBdcQlRelList.get(0).getBdcdyh()))
                        i++;

                }
                if (i == 0) {
                    GdXm gdXm = gdXmService.getGdXm(proid);
                    if (gdXm != null) {
                        ppzt = gdXm.getPpzt();
                    } else {
                        ppzt = Constants.GD_PPZT_WPP;
                    }
                } else if (i < gdFwList.size())
                    ppzt = Constants.GD_PPZT_BFPP;
                else
                    ppzt = Constants.GD_PPZT_WCPP;
            }
        } else if (StringUtils.isNotBlank(qlid) && StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
            List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(qlid);
            if (CollectionUtils.isNotEmpty(gdTdList)) {
                int i = 0;
                for (GdTd gdTd : gdTdList) {
                    List<BdcGdDyhRel> gdBdcQlRelList = bdcGdDyhRelService.getGdDyhRel("", gdTd.getTdid());
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList))
                        i++;

                }
                if (i == 0)
                    ppzt = Constants.GD_PPZT_WPP;
                else if (i < gdTdList.size())
                    ppzt = Constants.GD_PPZT_BFPP;
                else
                    ppzt = Constants.GD_PPZT_WCPP;
            }
        }
        return ppzt;
    }

    @Override
    public String getPpztByProid(String proid, String bdclx) {
        String ppzt = Constants.GD_PPZT_WPP;
        if (StringUtils.isNotBlank(proid) && StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            List<GdFw> gdFwList = getGdFwByProid(proid, "");
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                int i = 0;
                for (GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> gdBdcQlRelList = bdcGdDyhRelService.getGdDyhRel("", gdFw.getFwid());
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList))
                        i++;

                }
                if (i == 0)
                    ppzt = Constants.GD_PPZT_WPP;
                else if (i < gdFwList.size())
                    ppzt = Constants.GD_PPZT_BFPP;
                else
                    ppzt = Constants.GD_PPZT_WCPP;
            }
        } else if (StringUtils.isNotBlank(proid) && StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
            List<GdTd> gdTdList = gdTdService.getGdTdListByProid(proid, "");
            if (CollectionUtils.isNotEmpty(gdTdList)) {
                int i = 0;
                for (GdTd gdTd : gdTdList) {
                    List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcidOrQlid(gdTd.getTdid(), "");
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList))
                        i++;

                }
                if (i == 0)
                    ppzt = Constants.GD_PPZT_WPP;
                else if (i < gdTdList.size())
                    ppzt = Constants.GD_PPZT_BFPP;
                else
                    ppzt = Constants.GD_PPZT_WCPP;
            }
        }
        return ppzt;
    }

    @Override
    public List<GdFw> getGdFwByProidForCheckFwDz(String proid, String isck) {
        List<GdFw> gdFwList = null;
        if (StringUtils.isNotBlank(proid)) {
            String qlids = getGdFwQlidsByProid(proid);
            if (StringUtils.isNotBlank(qlids)) {
                HashMap queryGdFwMap = new HashMap();
                ////房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
                if (StringUtils.isNotBlank(isck)) {
                    queryGdFwMap.put("isinfwlx", "true");
                } else {
                    queryGdFwMap.put("isExcfwlx", "true");
                }
                queryGdFwMap.put(ParamsConstants.QLIDS_LOWERCASE, qlids.split(","));
                gdFwList = getGdFw(queryGdFwMap);
            }
        }
        return gdFwList;
    }

    @Override
    public Object makeSureQllxByGdproid(String gdproid) {
        Object obj = null;
        if (StringUtils.isNotBlank(gdproid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, gdproid);
            List<GdFwsyq> gdFwsyqList = andEqualQueryGdFwsyq(map);
            List<GdTdsyq> gdTdsyqList = gdTdService.andEqualQueryGdTdsyq(map);
            List<GdDy> gdDyList = andEqualQueryGdDy(map);
            List<GdYg> gdYgList = andEqualQueryGdYg(map);
            List<GdYy> gdYyList = andEqualQueryGdYy(map);
            if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                obj = new GdFwsyq();
            } else if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                obj = new GdTdsyq();
            } else if (CollectionUtils.isNotEmpty(gdDyList)) {
                obj = new GdDy();
            } else if (CollectionUtils.isNotEmpty(gdYgList)) {
                obj = new GdYg();
            } else if (CollectionUtils.isNotEmpty(gdYyList)) {
                obj = new GdYy();
            }
        }
        return obj;
    }


    @Override
    public List<GdFw> getGdFwByGdProid(HashMap hashMap) {
        return gdFwMapper.getGdFwByGdProid(hashMap);
    }

    @Override
    public String getBdcdyhByFwtdid(String tdid) {
        String bdcdyh = "";
        if(StringUtils.isNotBlank(tdid)) {
            bdcdyh = gdFwMapper.getBdcdyhByFwtdid(tdid);
        }
        return bdcdyh;
    }

    @Override
    public List<HashMap> getGdFwQl(HashMap map) {
        return gdFwMapper.getGdFwQl(map);
    }

    @Override
    public List<GdFwQl> getGdFwQlByMap(HashMap map) {
        return gdFwMapper.getGdFwQlByHashMap(map);
    }

    @Override
    public HashMap<String, String> getGdqlr(String qlid) {
        HashMap<String, String> resultMap = null;
        if(StringUtils.isNotBlank(qlid)) {
            resultMap = gdFwMapper.getGdqlr(qlid);
        }
        return resultMap;
    }

    @Override
    public List<String> getCqqidByGdProid(HashMap map) {
        return gdFwMapper.getCqqidByGdProid(map);
    }

    @Override
    public HashMap<String, String> getBdcdyhAndFwid(String bdcdyh, String fwid) {
        return gdFwMapper.getBdcdyhAndFwid(bdcdyh, fwid);
    }

    @Override
    public List<GdFwsyq> queryFwsyqByFwid(String fwid) {
        List<GdFwsyq> list = null;
        if (StringUtils.isNotBlank(fwid)) {
            List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcidOrQlid(fwid, null);
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                list = new ArrayList<GdFwsyq>();
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    GdFwsyq gdFwsyq = getGdFwsyqByQlid(gdBdcQlRel.getQlid());
                    if (gdFwsyq != null) {
                        list.add(gdFwsyq);
                    }
                }
            }
        }

        return list;
    }

    @Override
    public void setFsss(String fwid, String fsssFlag) {
        GdFw gdFw = queryGdFw(fwid);
        gdFw.setIsfsss("1");
        entityMapper.saveOrUpdate(gdFw, gdFw.getFwid());
    }

    @Override
    public List<GdFw> glGdFw(HashMap map) {
        List<GdFw> gdFwList = getGdFw(map);
        if (map.get(ParamsConstants.BDCDYID_LOWERCASE) != null && !StringUtils.isEmpty(map.get(ParamsConstants.BDCDYID_LOWERCASE).toString())) {
            List<GdFw> gdfws = gdFwMapper.getGdFwByBdcdyid(map.get(ParamsConstants.BDCDYID_LOWERCASE).toString());
            gdFwList.clear();
            gdFwList.addAll(gdfws);
        }
        List<GdFw> gdFwList1 = new ArrayList<GdFw>();
        if (CollectionUtils.isNotEmpty(gdFwList)) {
            String exYt = getGdFwExclx(new HashMap());
            String fwFilterFsssGhyt = AppConfig.getProperty("fw.filterFsss.ghyt");
            if (StringUtils.equals(fwFilterFsssGhyt, "true")) {
                //房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
                for (GdFw gdFw : gdFwList) {
                    if ((StringUtils.indexOf(exYt, gdFw.getGhyt()) == -1)) {
                        gdFwList1.add(gdFw);
                    } else if (StringUtils.equals(gdFw.getGhyt(), "车库") && gdFw.getCg() != null && gdFw.getCg() > 2.2) {
                        gdFwList1.add(gdFw);
                    }
                }
            } else {
                gdFwList1.addAll(gdFwList);
            }
        }
        return gdFwList1;
    }

    @Override
    public String getGdFwExclx(HashMap map) {
        return gdFwMapper.getGdFwExclx(map);
    }

    @Override
    public List<GdFw> glGdFwRemoveSameFw(HashMap map) {
        List<GdFw> gdFwList = getGdFw(map);
        List<GdFw> gdFwList1 = new ArrayList<GdFw>();
        if (CollectionUtils.isNotEmpty(gdFwList)) {
            String exYt = getGdFwExclx(new HashMap());
            List<String> dahList = new ArrayList<String>();
            String fwFilterFsssGhyt = AppConfig.getProperty("fw.filterFsss.ghyt");
            if (StringUtils.equals(fwFilterFsssGhyt, "true")) {
                //房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
                for (GdFw gdFw : gdFwList) {
                    if ((StringUtils.indexOf(exYt, gdFw.getGhyt()) == -1)) {
                        if (!dahList.contains(gdFw.getDah())) {
                            gdFwList1.add(gdFw);
                            dahList.add(gdFw.getDah());
                        }
                    } else if (StringUtils.equals(gdFw.getGhyt(), "车库") && gdFw.getCg() != null && gdFw.getCg() > 2.2 && !dahList.contains(gdFw.getDah())) {
                        gdFwList1.add(gdFw);
                        dahList.add(gdFw.getDah());
                    }
                }
            } else {
                gdFwList1.addAll(gdFwList);
            }
        }
        return gdFwList1;
    }

    @Override
    public HashMap getBdcdyh(GdFw gdFw) {
        HashMap bdcdyMap = null;
        HashMap map = new HashMap();
        List<HashMap> mapList = new ArrayList<HashMap>();
        if (StringUtils.isNoneBlank(gdFw.getDah())) {
            map.put("dah", gdFw.getDah());
            mapList = gdFwMapper.getBdcdyh(map);
        }
        if (CollectionUtils.isEmpty(mapList) && StringUtils.isNoneBlank(gdFw.getFwzl())) {
            map.clear();
            map.put("zl", gdFw.getFwzl());
            mapList = gdFwMapper.getBdcdyh(map);
        }
        if (CollectionUtils.isNotEmpty(mapList)){
            bdcdyMap = mapList.get(0);
        }
        return bdcdyMap;
    }

    @Override
    public List<HashMap> getFczhByBdcdyh(String bdcdyh) {
        List<HashMap> fczhList = null;
        if(StringUtils.isNotBlank(bdcdyh)) {
            fczhList = gdFwMapper.getFczhByBdcdyh(bdcdyh);
        }
        return fczhList;
    }

    @Override
    public List<String> getFwidByQlid(String qlid) {
        List<String> fwidList = null;
        if (StringUtils.isNotBlank(qlid)) {
            fwidList = new ArrayList<String>();
            List<GdFw> gdFwList = getGdFwByQlid(qlid);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                for (GdFw gdFw : gdFwList) {
                    if (!fwidList.contains(gdFw.getFwid())) {
                        fwidList.add(gdFw.getFwid());
                    }
                }
            }
        }
        return fwidList;
    }

    @Override
    public List<Map> selectGdfwNopp() {
        return gdFwMapper.selectGdfwNopp();
    }

    @Override
    public List<Map> selectGdfwPpTdNoBdcdy() {
        return gdFwMapper.selectGdfwPpTdNoBdcdy();
    }

    @Override
    public List<GdFwsyq> queryGdFwsyqByFwidAndQszt(String fwid, String qszt) {
        List<GdFwsyq> gdFwsyqList = null;
        List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcidOrQlid(fwid, "");
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            gdFwsyqList = new ArrayList<GdFwsyq>();
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                HashMap map = new HashMap();
                map.put("qlid", gdBdcQlRel.getQlid());
                if (StringUtils.isNotBlank(qszt)){
                    map.put("iszx", qszt);
                }
                List<GdFwsyq> gdFwsyqList1 = andEqualQueryGdFwsyq(map);
                if (CollectionUtils.isNotEmpty(gdFwsyqList1)) {
                    gdFwsyqList.addAll(gdFwsyqList1);
                }
            }
        }
        return gdFwsyqList;
    }

    @Override
    public List<GdFwsyq> queryFwsyqByCqzh(String cqzh) {
        List<GdFwsyq> gdFwsyqList = null;
        if (StringUtils.isNoneBlank(cqzh)) {
            HashMap map = new HashMap();
            map.put("fczh", cqzh);
            gdFwsyqList = gdFwMapper.queryFwsyqByCqzh(map);
        }
        return gdFwsyqList;
    }

    @Override
    public void changeGdqlztByQlid(String qlid, String qszt) {
        if (StringUtils.isNotBlank(qlid) && StringUtils.isNotBlank(qszt)) {
            Example example = new Example(GdFwQl.class);
            example.createCriteria().andEqualTo("qlid", qlid);
            List<GdFwQl> gdFwQlList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(gdFwQlList)) {
                GdFwQl gdFwQl = gdFwQlList.get(0);
                gdFwQl.setIszx(qszt);
                entityMapper.saveOrUpdate(gdFwQl, gdFwQl.getQlid());
            } else {
                Example tdexample = new Example(GdTdQl.class);
                tdexample.createCriteria().andEqualTo("qlid", qlid);
                List<GdTdQl> gdTdQlList = entityMapper.selectByExample(tdexample);
                if (CollectionUtils.isNotEmpty(gdTdQlList)) {
                    GdTdQl gdTdQl = gdTdQlList.get(0);
                    gdTdQl.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdTdQl, gdTdQl.getQlid());
                }
            }
        }
    }

    @Override
    public List<String> getTdQlidByQllx(Map map) {
        return gdFwMapper.getTdQlidByQllx(map);
    }

    @Override
    public List<String> getFwidByBdcdyh(String bdcdyh) {
        if (StringUtils.isNotBlank(bdcdyh)) {
            return gdFwMapper.getFwidByBdcdyh(bdcdyh);
        } else {
            return null;
        }
    }

    @Override
    public List<String> getXsFwsyqProidByfwid(List<String> fwidList) {
        if (CollectionUtils.isNotEmpty(fwidList)) {
            return gdFwMapper.getXsFwsyqProidByfwid(fwidList);
        } else {
            return null;
        }
    }
}
