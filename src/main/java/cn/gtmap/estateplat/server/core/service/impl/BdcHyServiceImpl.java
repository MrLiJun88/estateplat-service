package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcHyMapper;
import cn.gtmap.estateplat.server.core.service.BdcHyService;
import cn.gtmap.estateplat.server.core.service.BdcHysyqService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;


/**
 * 海域服务
 *
 * @author zhx
 * @version V1.0, 15-3-18
 */
@Repository
public class BdcHyServiceImpl implements BdcHyService {

    @Autowired
    private BdcHyMapper bdcHyMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcHysyqService bdcHysyqService;

    @Override
    @Transactional(readOnly = true)
    public BdcHy selectBdcHy(final String zdzhh) {
        return bdcHyMapper.selectBdcHy(zdzhh);
    }

    @Override
    public BdcHy getBdcHyFromZhxx(final DjsjZhxx djsjZhxx, final Project project, BdcHy bdcHy) {
        if (bdcHy == null) {
            bdcHy = new BdcHy();
        }
        if (StringUtils.isBlank(bdcHy.getHyid()))
            bdcHy.setHyid(UUIDGenerator.generate18());
        if (djsjZhxx == null)
            return bdcHy;
        bdcHy.setZdzhh(project.getZdzhh());
        bdcHy.setDwdm(project.getDwdm());
        bdcHy.setHddm(djsjZhxx.getHddm());
        bdcHy.setHdmc(djsjZhxx.getHdmc());
        bdcHy.setHdwz(djsjZhxx.getHdwz());
        bdcHy.setXmxz(djsjZhxx.getXmxz());
        if (djsjZhxx.getZhmj() != null && djsjZhxx.getZhmj() != 0)
            bdcHy.setZhmj(djsjZhxx.getZhmj());
        if (djsjZhxx.getDb() != null)
            bdcHy.setHydb(Integer.parseInt(djsjZhxx.getDb()));
        bdcHy.setZyax(djsjZhxx.getZhax().toString());
        bdcHy.setYhzmj(djsjZhxx.getYhzmj());
        bdcHy.setZyaxdw(Constants.DW_PFM);
        bdcHy.setYhlxa(djsjZhxx.getYhlxa());
        bdcHy.setYhlxb(djsjZhxx.getYhlxb());
        bdcHy.setYhwzsm(djsjZhxx.getYhwzsm());
        bdcHy.setHdmc(djsjZhxx.getHdmc());
        bdcHy.setHddm(djsjZhxx.getHddm());
        bdcHy.setYdfw(djsjZhxx.getYdfw());
        bdcHy.setYdmj(djsjZhxx.getYdmj());
        bdcHy.setHdwz(djsjZhxx.getHdwz());
        bdcHy.setYt(djsjZhxx.getHdyt());
        //zdd 地籍没有单位  默认就是平方米
        bdcHy.setMjdw(Constants.DW_PFM);
        return bdcHy;
    }


    @Override
    @Transactional
    public void deleteBdcHyByZdzhh(final String zdzhh) {
        if (StringUtils.isNotBlank(zdzhh)) {
            Example example = new Example(BdcHy.class);
            example.createCriteria().andEqualTo(ParamsConstants.ZDZHH_LOWERCASE, zdzhh);
            entityMapper.deleteByExample(BdcHy.class, example);
        }
    }

    @Override
    @Transactional
    public void changeHysyqZt(BdcXm bdcXm) {
        if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CSDJ_DM)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            if (qllxVo instanceof BdcFdcq) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null) {
                    HashMap map = new HashMap();
                    if (bdcBdcdy.getBdcdyh() != null && bdcBdcdy.getBdcdyh().length() > 19) {
                        map.put(ParamsConstants.ZDZHH_LOWERCASE, StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19));
                        BdcHysyq bdcHysyq = bdcHysyqService.getBdcHysyq(map);
                        if (bdcHysyq != null&&StringUtils.isNotBlank(bdcHysyq.getQlid())) {
                            bdcHysyq.setQszt(Constants.QLLX_QSZT_HR);
                            if (qllxVo.getDjsj() != null) {
                                bdcHysyq.setFj(CalendarUtil.formateToStrChinaYMDDate(qllxVo.getDjsj()) + "办理海域首次登记");
                            }else {
                                bdcHysyq.setFj(CalendarUtil.formateToStrChinaYMDDate(new Date()) + "办理海域首次登记");
                            }
                            entityMapper.saveOrUpdate(bdcHysyq, bdcHysyq.getQlid());
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void changeBackHysyqZt(BdcXm bdcXm) {
        if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CSDJ_DM)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            if (qllxVo instanceof BdcFdcq) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null&&bdcBdcdy.getBdcdyh() != null && bdcBdcdy.getBdcdyh().length() > 19) {
                    HashMap map = new HashMap();
                    map.put(ParamsConstants.ZDZHH_LOWERCASE, StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19));
                    BdcHysyq bdcHysyq = bdcHysyqService.getBdcHysyq(map);
                    if (bdcHysyq != null&&StringUtils.isNotBlank(bdcHysyq.getQlid())) {
                        bdcHysyq.setQszt(Constants.QLLX_QSZT_XS);
                        bdcHysyq.setFj("");
                        entityMapper.saveOrUpdate(bdcHysyq, bdcHysyq.getQlid());
                    }
                }
            }
        }
    }

    @Override
    public BdcHy getBdcHyFromDjxx(DjsjZhxx djsjZhxx, Project project, BdcHy bdcHy, String qllx) {
        if (bdcHy == null) {
            bdcHy = new BdcHy();
        }
        if (StringUtils.isBlank(bdcHy.getHyid()))
            bdcHy.setHyid(UUIDGenerator.generate18());

        if (djsjZhxx == null)
            return bdcHy;
        bdcHy.setZdzhh(project.getZdzhh());
        bdcHy.setDwdm(project.getDwdm());
        bdcHy.setHddm(djsjZhxx.getHddm());
        bdcHy.setHdmc(djsjZhxx.getHdmc());
        bdcHy.setHdwz(djsjZhxx.getHdwz());
        bdcHy.setXmxz(djsjZhxx.getXmxz());
        if (djsjZhxx.getZhmj() != null && djsjZhxx.getZhmj() != 0)
            bdcHy.setZhmj(djsjZhxx.getZhmj());
        if (djsjZhxx.getDb() != null)
            bdcHy.setHydb(Integer.parseInt(djsjZhxx.getDb()));
        if(djsjZhxx.getZhax()!=null) {
            bdcHy.setZyax(djsjZhxx.getZhax().toString());
        }
        bdcHy.setYhzmj(djsjZhxx.getYhzmj());
        bdcHy.setZyaxdw(djsjZhxx.getMjdw());
        bdcHy.setYhlxa(djsjZhxx.getYhlxa());
        bdcHy.setYhlxb(djsjZhxx.getYhlxb());
        bdcHy.setYhwzsm(djsjZhxx.getYhwzsm());
        bdcHy.setHdmc(djsjZhxx.getHdmc());
        bdcHy.setHddm(djsjZhxx.getHddm());
        bdcHy.setYdfw(djsjZhxx.getYdfw());
        bdcHy.setYdmj(djsjZhxx.getYdmj());
        bdcHy.setHdwz(djsjZhxx.getHdwz());
        bdcHy.setYt(djsjZhxx.getHdyt());
        if (djsjZhxx.getMjdw() == null) {
            bdcHy.setMjdw(Constants.DW_PFM);
        } else {
            bdcHy.setMjdw(djsjZhxx.getMjdw());
        }
        return bdcHy;
    }
}
