package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.BdcZj;
import cn.gtmap.estateplat.model.server.core.BdcZjmx;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/7/20
 * @description
 */
@Service
public class BdcZjServieImpl implements BdcZjService{
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public BdcZj getBdcZjByProid(String proid) {
        BdcZj bdcZj = null;
        Example example = new Example(BdcZj.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcZj> bdcZjList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcZjList))
            bdcZj = bdcZjList.get(0);
        return bdcZj;
    }

    @Override
    public List<BdcZjmx> getBdcZjmxListByProid(String proid) {
        List<BdcZjmx> bdcZjmxList = null;
        BdcZj bdcZj = getBdcZjByProid(proid);
        if (bdcZj != null && StringUtils.isNotBlank(bdcZj.getZjid())) {
            Example example = new Example(BdcZjmx.class);
            example.createCriteria().andEqualTo("zjid", bdcZj.getZjid());
            bdcZjmxList = entityMapper.selectByExample(example);
        }
        return bdcZjmxList;
    }

    @Override
    public void delBdcZjByProid(String proid) {
        Example example = new Example(BdcZj.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        entityMapper.deleteByExample(example);
    }

    @Override
    public void delBdcZjmxByProid(String proid) {
        List<BdcZjmx> bdcZjmxList = getBdcZjmxListByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcZjmxList)) {
            for (BdcZjmx bdcZjmx : bdcZjmxList)
                delBdcZjmxByid(bdcZjmx.getZjmxid());
        }
    }

    @Override
    public void delBdcZjmxByid(String id) {
        entityMapper.deleteByPrimaryKey(BdcZjmx.class, id);
    }

    @Override
    public void saveBdcZjmx(List<BdcZjmx> bdcZjmxList) {
        for (BdcZjmx bdcZjmx : bdcZjmxList) {
            if (bdcZjmx.getZjmxid().length() < 10) {
                bdcZjmx.setZjmxid(UUIDGenerator.generate18());
            }
            entityMapper.saveOrUpdate(bdcZjmx, bdcZjmx.getZjmxid());
        }
    }

    @Override
    public void saveBdcZjzt(String proid, String zjzt) {
        BdcZj bdcZj = getBdcZjByProid(proid);
        if (bdcZj != null) {
            bdcZj.setZjzt(zjzt);
            entityMapper.saveOrUpdate(bdcZj, bdcZj.getZjid());
        }
    }

    @Override
    public void initBdcXmRelForBdcZj(String proid, String yxmid) {
        Example example = new Example(BdcXmRel.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        entityMapper.deleteByExample(example);
        BdcXmRel bdcXmRel = new BdcXmRel();
        bdcXmRel.setRelid(UUIDGenerator.generate18());
        bdcXmRel.setProid(proid);
        bdcXmRel.setYproid(yxmid);
        bdcXmRelService.saveBdcXmRel(bdcXmRel);
    }

    public List<BdcZjmx> intBdcZjmx(BdcZj bdcZj) {
        List<BdcZjmx> bdcZjmxList = new ArrayList<BdcZjmx>();
        String zjmxStr = AppConfig.getProperty("bdczjmx.default");
        if (StringUtils.isNotBlank(zjmxStr)) {
            String[] zjmxArr = zjmxStr.split(",");
            for (String zjmx : zjmxArr) {
                BdcZjmx bdcZjmx = new BdcZjmx();
                bdcZjmx.setZjmxid(UUIDGenerator.generate18());
                bdcZjmx.setZjid(bdcZj.getZjid());
                bdcZjmx.setZjnr(zjmx);
                bdcZjmx.setSftg("1");
                bdcZjmxList.add(bdcZjmx);
            }
            entityMapper.batchSaveSelective(bdcZjmxList);
        }
        return bdcZjmxList;
    }

    @Override
    public void initBdcZjxx(String proid,String yxmid,String wiid){
        if(StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm == null){
                //生成新的bdc_xm
                bdcXm = new BdcXm();
                bdcXm.setProid(proid);
                bdcXm.setWiid(wiid);
                bdcXm.setBh(bdcXmService.creatXmbh(bdcXm));
                bdcXm.setSqlx("199");
                entityMapper.saveOrUpdate(bdcXm,bdcXm.getProid());
            }
            //bdcZj
            BdcZj bdcZj = new BdcZj();
            bdcZj.setZjid(UUIDGenerator.generate18());
            bdcZj.setProid(proid);
            bdcZj.setZjr(SessionUtil.getCurrentUser().getUsername());
            bdcZj.setZjrq(new Date());
            bdcZj.setZjbh(CalendarUtil.getTimeMs());
            bdcZj.setZjzt("0");
            entityMapper.saveOrUpdate(bdcZj, bdcZj.getZjid());
            //BdcZjmx
            intBdcZjmx(bdcZj);
            //BdcXmRel
            initBdcXmRelForBdcZj(proid, yxmid);
        }

    }
}
