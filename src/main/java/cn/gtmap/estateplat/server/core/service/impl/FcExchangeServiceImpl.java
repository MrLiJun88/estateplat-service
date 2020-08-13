package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.FcExchangeService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-12-2
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class FcExchangeServiceImpl implements FcExchangeService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public boolean gdXmIsPureIn(String proid) {
        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, proid);
        if (gdXm != null)
            return true;
        else
            return false;
    }

    @Override
    public void insertGdXm(Map map) {
        GdXm gdXm = getGdxm(map);
        entityMapper.insertSelective(gdXm);
    }

    @Override
    public void insertGdSyq(Map map) {
        GdFwsyq gdFwsyq = getGdFwsyq(map);
        entityMapper.insertSelective(gdFwsyq);
    }

    @Override
    public void insertGdDy(Map map) {
        GdDy gdDy = getGdDy(map);
        entityMapper.insertSelective(gdDy);
    }

    @Override
    public void insertGdCf(Map map) {
        GdCf gdCf = getGdCf(map);
        entityMapper.insertSelective(gdCf);
    }

    @Override
    public void insertGdYg(Map map) {
        GdYg gdYg = getGdYg(map);
        entityMapper.insertSelective(gdYg);
    }

    @Override
    public void insertGdFw(Map map) {
        GdFw gdFw = getGdFw(map);
        gdFw.setFwid(UUIDGenerator.generate18());
        entityMapper.insertSelective(gdFw);
        insertGdBdcQlRel(gdFw);
    }

    @Override
    public void insertGdQlr(Map map) {
        GdQlr gdQlr = getGdQlr(map);
        entityMapper.insertSelective(gdQlr);
    }

    @Override
    public void insertGdYzh(Map map) {
        GdYzh gdYzh = getGdYzh(map);
        entityMapper.insertSelective(gdYzh);
    }

    @Override
    public HashMap getJtztByZsid(String zsid) {
        HashMap map = new HashMap();
        Example example = new Example(BdcJtzf.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("zsid", zsid);
        List<BdcJtzf> bdcJtzfList = entityMapper.selectByExample(BdcJtzf.class, example);
        if (CollectionUtils.isNotEmpty(bdcJtzfList)) {
            BdcJtzf bdcJtzf = bdcJtzfList.get(0);
            map.put("CZMYY", bdcJtzf.getCzmyy());
            map.put("BZ", bdcJtzf.getBz());
        }
        return map;
    }

    public void insertGdBdcQlRel(GdFw gdFw) {
        GdBdcQlRel gdBdcQlRel = new GdBdcQlRel();
        gdBdcQlRel.setRelid(UUIDGenerator.generate18());
        gdBdcQlRel.setBdcid(gdFw.getFwid());
        gdBdcQlRel.setQlid(gdFw.getQlid());
        entityMapper.insertSelective(gdBdcQlRel);
    }

    /*sc:HashMap 转过渡项目**/
    private GdXm getGdxm(Map map) {
        GdXm gdXm = new GdXm();
        if (map.get("proid") != null)
            gdXm.setProid(map.get("proid").toString());
        if (map.get("csr") != null)
            gdXm.setCsr(map.get("csr").toString());
        if (map.get("shr") != null)
            gdXm.setShr(map.get("shr").toString());
        gdXm.setGxrq(new Date());
        if (map.get("bdclx") != null)
            gdXm.setBdclx(map.get("bdclx").toString());
        if (map.get("zl") != null)
            gdXm.setZl(map.get("zl").toString());
        gdXm.setPpzt("");
        if (map.get("djlx") != null)
            gdXm.setDjlx(map.get("djlx").toString());
        if (map.get("slbh") != null)
            gdXm.setSlbh(map.get("slbh").toString());
        return gdXm;
    }

    /*sc:HashMap 转过渡房屋所有权**/
    private GdFwsyq getGdFwsyq(Map map) {
        GdFwsyq gdFwsyq = new GdFwsyq();
        if (map.get("QLID") != null)
            gdFwsyq.setQlid(map.get("QLID").toString());
        if (map.get("FCZH") != null)
            gdFwsyq.setFczh(map.get("FCZH").toString());
        gdFwsyq.setGxrq(new Date());
        if (map.get("DJLX") != null)
            gdFwsyq.setDjlx(map.get("DJLX").toString());
        if (map.get("DJYY") != null)
            gdFwsyq.setDjyy(map.get("DJYY").toString());
        if (map.get("FJ") != null)
            gdFwsyq.setFj(map.get("FJ").toString());
        return gdFwsyq;
    }

    /*sc:HashMap 转过渡抵押**/
    private GdDy getGdDy(Map map) {
        GdDy gdDy = new GdDy();
        if (map.get("DYID") != null)
            gdDy.setDyid(map.get("DYID").toString());
        if (map.get("DYKSRQ") != null)
            gdDy.setDyksrq(CalendarUtil.formatDate(map.get("DYKSRQ").toString()));
        if (map.get("DYJSRQ") != null)
            gdDy.setDyjsrq(CalendarUtil.formatDate(map.get("DYJSRQ").toString()));
        if (map.get("DYFS") != null)
            gdDy.setDyfs(map.get("DYFS").toString());
        if (map.get("ZJGCZL") != null)
            gdDy.setZjgczl(map.get("ZJGCZL").toString());
        if (map.get("ZJGCDYFW") != null)
            gdDy.setZjgcdyfw(map.get("ZJGCDYFW").toString());
        if (map.get("BDBZZQSE") != null)
            gdDy.setBdbzzqse(Double.parseDouble(map.get("BDBZZQSE").toString()));
        if (map.get("ZGZQQDSE") != null)
            gdDy.setZgzqqdse(Double.parseDouble(map.get("ZGZQQDSE").toString()));
        if (map.get("ZGZQQDSS") != null)
            gdDy.setZgzqqdss(map.get("ZGZQQDSS").toString());
        if (map.get(ParamsConstants.BDCLX_CAPITAL) != null)
            gdDy.setBdclx(map.get(ParamsConstants.BDCLX_CAPITAL).toString());
        if (map.get("DYDJZMH") != null)
            gdDy.setDydjzmh(map.get("DYDJZMH").toString());
        if (map.get("DJLX") != null)
            gdDy.setDjlx(map.get("DJLX").toString());
        if (map.get("DBFW") != null)
            gdDy.setDbfw(map.get("DBFW").toString());
        if (map.get("FJ") != null)
            gdDy.setFj(map.get("FJ").toString());
        gdDy.setGxrq(new Date());
        return gdDy;
    }

    /*sc:HashMap 转过渡查封*/
    private GdCf getGdCf(Map map) {
        GdCf gdCf = new GdCf();
        if (map.get("CFID") != null)
            gdCf.setCfid(map.get("CFID").toString());
        if (map.get("CFJG") != null)
            gdCf.setCfjg(map.get("CFJG").toString());
        if (map.get("CFLX") != null)
            gdCf.setCflx(map.get("CFLX").toString());
        if (map.get("CFWJ") != null)
            gdCf.setCfwj(map.get("CFWJ").toString());
        if (map.get("CFWH") != null)
            gdCf.setCfwh(map.get("CFWH").toString());
        if (map.get("CFKSRQ") != null)
            gdCf.setCfksrq(CalendarUtil.formatDate(map.get("CFKSRQ").toString()));
        if (map.get("CFJSRQ") != null)
            gdCf.setCfjsrq(CalendarUtil.formatDate(map.get("CFJSRQ").toString()));
        if (map.get("CFFW") != null)
            gdCf.setCffw(map.get("CFFW").toString());
        if (map.get(ParamsConstants.BDCLX_CAPITAL) != null)
            gdCf.setBdclx(map.get(ParamsConstants.BDCLX_CAPITAL).toString());
        if (map.get("DJLX") != null)
            gdCf.setDjlx(map.get("DJLX").toString());
        if (map.get("FJ") != null)
            gdCf.setFj(map.get("FJ").toString());
        if (map.get("FYSDR") != null)
            gdCf.setFysdr(map.get("FYSDR").toString());
        if (map.get("FYSDRLXFS") != null)
            gdCf.setFysdrlxfs(map.get("FYSDRLXFS").toString());
        gdCf.setGxrq(new Date());
        return gdCf;
    }

    /*sc:HashMap 转过渡预告**/
    private GdYg getGdYg(Map map) {
        GdYg gdYg = new GdYg();
        if (map.get("YGID") != null)
            gdYg.setYgid(map.get("YGID").toString());
        if (map.get("QDJG") != null)
            gdYg.setQdjg(Double.parseDouble(map.get("QDJG").toString()));
        if (map.get("YGDJZMH") != null)
            gdYg.setYgdjzmh(map.get("YGDJZMH").toString());
        if (map.get(ParamsConstants.BDCLX_CAPITAL) != null)
            gdYg.setBdclx(map.get(ParamsConstants.BDCLX_CAPITAL).toString());
        if (map.get("DJLX") != null)
            gdYg.setDjlx(map.get("DJLX").toString());
        if (map.get("YGDJZL") != null)
            gdYg.setYgdjzl(map.get("YGDJZL").toString());
        if (map.get("FJ") != null)
            gdYg.setFj(map.get("FJ").toString());
        gdYg.setGxrq(new Date());
        return gdYg;
    }

    /*sc:HashMap 转过渡房屋**/
    private GdFw getGdFw(Map map) {
        GdFw gdFw = new GdFw();
        if (map.get("FWID") != null)
            gdFw.setFwid(map.get("FWID").toString());
        if (map.get("DAH") != null)
            gdFw.setDah(map.get("DAH").toString());
        if (map.get("GYQK") != null)
            gdFw.setGyqk(map.get("GYQK").toString());
        if (map.get("GHYT") != null)
            gdFw.setGhyt(map.get("GHYT").toString());
        if (map.get("JYJG") != null)
            gdFw.setJyjg(Double.parseDouble(map.get("JYJG").toString()));
        if (map.get("FWXZ") != null)
            gdFw.setFwxz(map.get("FWXZ").toString());
        if (map.get("FWJG") != null)
            gdFw.setFwjg(map.get("FWJG").toString());
        if (map.get("SZC") != null)
            gdFw.setSzc(map.get("SZC").toString());
        if (map.get("ZCS") != null)
            gdFw.setZcs(Integer.parseInt(map.get("ZCS").toString()));
        if (map.get("JZMJ") != null)
            gdFw.setJzmj(Double.parseDouble(map.get("JZMJ").toString()));
        if (map.get("TNJZMJ") != null)
            gdFw.setTnjzmj(Double.parseDouble(map.get("TNJZMJ").toString()));
        if (map.get("FTJZMJ") != null)
            gdFw.setFtjzmj(Double.parseDouble(map.get("FTJZMJ").toString()));
        if (map.get("JGSJ") != null)
            gdFw.setJgsj(CalendarUtil.formatDate(map.get("JGSJ").toString()));
        if (map.get("FWZL") != null)
            gdFw.setFwzl(map.get("FWZL").toString());
        if (map.get("DW") != null)
            gdFw.setDw(map.get("DW").toString());
        if (map.get("CQLY") != null)
            gdFw.setCqly(map.get("CQLY").toString());
        if (map.get("FWLX") != null)
            gdFw.setFwlx(map.get("FWLX").toString());
        if (map.get("QLID") != null)
            gdFw.setQlid(map.get("QLID").toString());
        gdFw.setSjly("1");
        gdFw.setGxrq(new Date());
        return gdFw;
    }

    /*sc:HashMap 转过渡权利人**/
    private GdQlr getGdQlr(Map map) {
        GdQlr gdQlr = new GdQlr();
        if (map.get("QLRID") != null)
            gdQlr.setQlrid(map.get("QLRID").toString());
        if (map.get("QLR") != null)
            gdQlr.setQlr(map.get("QLR").toString());
        if (map.get("QLRSFZJZL") != null)
            gdQlr.setQlrsfzjzl(map.get("QLRSFZJZL").toString());
        if (map.get("QLRZJH") != null)
            gdQlr.setQlrzjh(map.get("QLRZJH").toString());
        if (map.get("QLID") != null)
            gdQlr.setQlid(map.get("QLID").toString());
        if (map.get("QLRLX") != null)
            gdQlr.setQlrlx(map.get("QLRLX").toString());
        if (map.get(ParamsConstants.PROID_CAPITAL) != null)
            gdQlr.setProid(map.get(ParamsConstants.PROID_CAPITAL).toString());
        if (map.get("QLRXZ") != null)
            gdQlr.setQlrxz(map.get("QLRXZ").toString());
        return gdQlr;
    }

    /*sc:HashMap 转过渡原证号**/
    private GdYzh getGdYzh(Map map) {
        GdYzh gdYzh = new GdYzh();
        if (map.get("YZHID") != null)
            gdYzh.setYzhid(map.get("YZHID").toString());
        if (map.get(ParamsConstants.PROID_CAPITAL) != null)
            gdYzh.setProid(map.get(ParamsConstants.PROID_CAPITAL).toString());
        if (map.get("YZH") != null)
            gdYzh.setYzh(map.get("YZH").toString());
        if (map.get("QLID") != null)
            gdYzh.setQlid(map.get("QLID").toString());
        return gdYzh;
    }
}
