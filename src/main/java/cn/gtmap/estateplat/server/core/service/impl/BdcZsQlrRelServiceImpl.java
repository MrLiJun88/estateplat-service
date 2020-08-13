package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.BdcZsQlrRel;
import cn.gtmap.estateplat.server.core.mapper.BdcZsQlrRelMapper;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZsQlrRelService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-23
 */
@Service
public class BdcZsQlrRelServiceImpl implements BdcZsQlrRelService {
    @Autowired
    BdcZsQlrRelMapper bdcZsQlrRelMapper;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    EntityMapper entityMapper;

    @Override
    public List<BdcZsQlrRel> queryBdcZsQlrRelByQlrid(final String qlrid) {
        List<BdcZsQlrRel> bdcZsQlrRels = null;
        if (StringUtils.isNotBlank(qlrid))
            bdcZsQlrRels = bdcZsQlrRelMapper.queryBdcZsQlrRelByQlrid(qlrid);
        return bdcZsQlrRels;
    }

    @Override
    public void delBdcZsQlrRelByQlrid(final String qlrid) {
        if (StringUtils.isNotBlank(qlrid)) {
            Example example = new Example(BdcZsQlrRel.class);
            example.createCriteria().andEqualTo("qlrid", qlrid);
            entityMapper.deleteByExample(BdcZsQlrRel.class, example);
        }
    }

    @Override
    public void delBdcZsAndZsQlrRelByQlrid(final String qlrid) {
        if (StringUtils.isNotBlank(qlrid)) {
            List<BdcZsQlrRel> zsQlrRelList = queryBdcZsQlrRelByQlrid(qlrid);
            if (CollectionUtils.isNotEmpty(zsQlrRelList)) {
                for (BdcZsQlrRel bdcZsQlrRel : zsQlrRelList) {
                    if (StringUtils.isNotBlank(bdcZsQlrRel.getZsid())) {
                        bdcZsService.delBdcZsByZsid(bdcZsQlrRel.getZsid());
                    }
                }
            }
            delBdcZsQlrRelByQlrid(qlrid);
        }
    }

    @Override
    public void delZsQlrRelByZsid(final String zsid) {
        if (StringUtils.isNotBlank(zsid)) {
            Example example = new Example(BdcZsQlrRel.class);
            example.createCriteria().andEqualTo("zsid", zsid);
            entityMapper.deleteByExample(BdcZsQlrRel.class, example);
        }
    }

    @Override
    public List<BdcZsQlrRel> creatBdcZsQlrRel(BdcXm bdcXm, List<BdcZs> bdcZsList, List<BdcQlr> bdcQlrList) {
        List<BdcZsQlrRel> bdcZsQlrRelList = new ArrayList<BdcZsQlrRel>();
        if (CollectionUtils.isNotEmpty(bdcZsList)&&CollectionUtils.isNotEmpty(bdcQlrList)) {
            for(BdcQlr bdcQlr:bdcQlrList) {
                delBdcZsAndZsQlrRelByQlrid(bdcQlr.getQlrid());
            }

            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqfbcz()) && bdcXm.getSqfbcz().equals(Constants.BDCXM_FBCZ)) {
                for (int i = 0; i < bdcQlrList.size(); i++) {
                    BdcQlr bdcQlr = bdcQlrList.get(i);
                    if (bdcZsList.size() > i) {
                        BdcZsQlrRel bdcZsQlrRel = new BdcZsQlrRel();
                        bdcZsQlrRel.setGxid(UUIDGenerator.generate18());
                        bdcZsQlrRel.setQlrid(bdcQlr.getQlrid());
                        //zdd 此处防止证书与权利人对照关系与持证人不符
                        if (bdcZsList.get(i).getCzr().equals(bdcQlr.getQlrmc())) {
                            bdcZsQlrRel.setZsid(bdcZsList.get(i).getZsid());
                        } else {
                            String zsid = "";
                            for (BdcZs bdcZs : bdcZsList) {
                                if (bdcZs.getCzr().equals(bdcQlr.getQlrmc())) {
                                    zsid = bdcZs.getZsid();
                                    break;
                                }
                            }
                            if (zsid != null) {
                                bdcZsQlrRel.setZsid(zsid);
                            } else {
                                bdcZsQlrRel.setZsid(bdcZsList.get(i).getZsid());
                            }
                        }
                        entityMapper.insertSelective(bdcZsQlrRel);
                        bdcZsQlrRelList.add(bdcZsQlrRel);
                    }
                }
            } else {
                //如果不是分别持证，认为项目默认是一本证书
                for (BdcQlr bdcQlr : bdcQlrList) {
                    BdcZsQlrRel bdcZsQlrRel = new BdcZsQlrRel();
                    bdcZsQlrRel.setGxid(UUIDGenerator.generate18());
                    bdcZsQlrRel.setQlrid(bdcQlr.getQlrid());
                    bdcZsQlrRel.setZsid(bdcZsList.get(0).getZsid());
                    entityMapper.insertSelective(bdcZsQlrRel);
                    bdcZsQlrRelList.add(bdcZsQlrRel);
                }
            }
        }
        return bdcZsQlrRelList;
    }

    @Override
    public List<BdcZsQlrRel> queryBdcZsQlrRelByProid(final String proid) {
        return bdcZsQlrRelMapper.queryBdcZsQlrRelByProid(proid);
    }

    @Override
    public List<BdcZsQlrRel> creatBdcZsQlrRelArbitrary(BdcZs bdcZs, BdcQlr bdcQlr, List<BdcQlr> gyrList) {
        List<BdcZsQlrRel> bdcZsQlrRelList = new ArrayList<BdcZsQlrRel>();
        if (bdcZs != null && bdcQlr != null) {
            BdcZsQlrRel bdcZsQlrRel = new BdcZsQlrRel();
            bdcZsQlrRel.setGxid(UUIDGenerator.generate18());
            bdcZsQlrRel.setQlrid(bdcQlr.getQlrid());
            bdcZsQlrRel.setZsid(bdcZs.getZsid());
            entityMapper.insertSelective(bdcZsQlrRel);
            bdcZsQlrRelList.add(bdcZsQlrRel);
            if (CollectionUtils.isNotEmpty(gyrList)) {
                for (BdcQlr gyr : gyrList) {
                    BdcZsQlrRel bdcZsQlrRelTemp = new BdcZsQlrRel();
                    bdcZsQlrRelTemp.setGxid(UUIDGenerator.generate18());
                    bdcZsQlrRelTemp.setQlrid(gyr.getQlrid());
                    bdcZsQlrRelTemp.setZsid(bdcZs.getZsid());
                    entityMapper.insertSelective(bdcZsQlrRelTemp);
                    bdcZsQlrRelList.add(bdcZsQlrRelTemp);
                }
            }
        }
        return bdcZsQlrRelList;
    }

    @Override
    public List<BdcZsQlrRel> creatBdcZsQlrRelForOtherQlrExceptCzr(BdcZs bdcZs, List<BdcQlr> czrList, List<BdcQlr> qlrList, BdcXm bdcXm) {
        List<BdcZsQlrRel> bdcZsQlrRels = null;
        int czrSize = 0;
        if (CollectionUtils.isNotEmpty(czrList)) {
            czrSize = czrList.size();
        }
        if (CollectionUtils.isNotEmpty(qlrList) && czrSize == 1) {
            int qlrSize = qlrList.size();
            if (qlrSize > czrSize) {
                List<BdcZsQlrRel> zsQlrRels = bdcZsQlrRelMapper.queryBdcZsQlrRelByProid(bdcXm.getProid());
                for (BdcQlr qlr : qlrList) {
                    boolean breakOuter = false;
                    if (StringUtils.equals(qlr.getSfczr(), Constants.CZR)) {
                        continue;
                    }
                    if (CollectionUtils.isNotEmpty(zsQlrRels)) {
                        for (BdcZsQlrRel zsQlrRel : zsQlrRels) {
                            if (StringUtils.equals(zsQlrRel.getZsid(), bdcZs.getZsid()) && StringUtils.equals(zsQlrRel.getQlrid(), qlr.getQlrid())) {
                                breakOuter=true;
                                break;
                            }
                        }
                    }
                    if(breakOuter){
                        continue;
                    }
                    bdcZsQlrRels = creatBdcZsQlrRelArbitrary(bdcZs, qlr, null);
                }
            }
        }
        return bdcZsQlrRels;
    }

    @Transactional
    @Override
    public void batchDelBdcZsQlrRelByBdcZsList(List<BdcZs> bdcZsList) {
        if(CollectionUtils.isNotEmpty(bdcZsList)){
            bdcZsQlrRelMapper.batchDelBdcZsQlrRelByBdcZsList(bdcZsList);
        }
    }

    @Override
    public void batchDelBdcZsQlrRelByBdcQlrList(List<BdcQlr> bdcQlrList) {
        if(CollectionUtils.isNotEmpty(bdcQlrList)){
            bdcZsQlrRelMapper.batchDelBdcZsQlrRelByBdcQlrList(bdcQlrList);
        }
    }
}
