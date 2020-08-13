package cn.gtmap.estateplat.server.core.service.impl;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmzsRel;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.mapper.BdcZsMapper;
import cn.gtmap.estateplat.server.core.service.BdcXmZsRelService;
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
 * @version V1.0, 15-3-24
 */
@Service
public class BdcXmZsRelServiceImpl implements BdcXmZsRelService {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcZsMapper bdcZsMapper;

    @Override
    public List<BdcXmzsRel> queryBdcXmZsRelByProid(final String proid) {
        List<BdcXmzsRel> list = null;
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcXmzsRel.class);
            example.createCriteria().andEqualTo("proid", proid);
            list = entityMapper.selectByExample(BdcXmzsRel.class, example);
        }
        return list;

    }

    @Override
    public String getProidByZsid(final String zsid) {
        String proid = null;
        if (StringUtils.isNotBlank(zsid)) {
            Example example = new Example(BdcXmzsRel.class);
            example.createCriteria().andEqualTo("zsid", zsid);
            List<BdcXmzsRel> list = entityMapper.selectByExample(BdcXmzsRel.class, example);
            if (CollectionUtils.isNotEmpty(list))
                proid = list.get(0).getProid();
        }
        return proid;
    }

    @Override
    public void delBdcXmZsRelByProid(final String proid) {
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcXmzsRel.class);
            example.createCriteria().andEqualTo("proid", proid);
            entityMapper.deleteByExample(BdcXmzsRel.class, example);

        }
    }

    @Override
    public void delBdcXmZsRelByXmzsgxid(final String xmzsgxid) {
        if (StringUtils.isNotBlank(xmzsgxid)) {
            Example example = new Example(BdcXmzsRel.class);
            example.createCriteria().andEqualTo("xmzsgxid", xmzsgxid);
            entityMapper.deleteByExample(BdcXmzsRel.class, example);

        }
    }

    @Override
    public List<BdcXmzsRel> creatBdcXmZsRel(List<BdcZs> list,final String proid) {
        //zx先删除证书和项目关系然后再创建，主要是改变了逻辑，改变退回再创建时，先删除证书然后创建
        delBdcXmZsRelByProid(proid);
        List<BdcXmzsRel> bdcXmzsRelList = queryBdcXmZsRelByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
            for (BdcZs bdcZs : list) {
                String zsid = bdcZs.getZsid();
                Boolean isInsert = true;
                for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                    if (bdcXmzsRel.getZsid().equals(zsid)) {
                        isInsert = false;
                        break;
                    }
                }
                if (isInsert) {
                    BdcXmzsRel bdcXmzsRel = creatBdcXmZsRel(bdcZs.getZsid(), proid);
                    bdcXmzsRelList.add(bdcXmzsRel);
                }
            }
        } else {
            bdcXmzsRelList = new ArrayList<BdcXmzsRel>();
            for (BdcZs bdcZs : list) {
                BdcXmzsRel bdcXmzsRel = creatBdcXmZsRel(bdcZs.getZsid(), proid);
                bdcXmzsRelList.add(bdcXmzsRel);
            }
        }
        return bdcXmzsRelList;
    }

    public BdcXmzsRel creatBdcXmZsRel(final String zsid, final String proid) {
        BdcXmzsRel bdcXmzsRel = new BdcXmzsRel();
        bdcXmzsRel.setProid(proid);
        bdcXmzsRel.setXmzsgxid(UUIDGenerator.generate18());
        bdcXmzsRel.setZsid(zsid);
        entityMapper.insertSelective(bdcXmzsRel);
        return bdcXmzsRel;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 生成项目证书关系(任意流程)
     */
    @Override
    public List<BdcXmzsRel> creatBdcXmZsRelArbitrary(final String zsid, List<BdcXm> bdcXmList) {
        List<BdcXmzsRel> bdcXmzsRelList = null;
        if (StringUtils.isNotBlank(zsid)) {
            //zx先删除证书和项目关系然后再创建，主要是改变了逻辑，改变退回再创建时，先删除证书然后创建
            delBdcXmZsRelByZsid(zsid);
            if(CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    String proid=bdcXm.getProid();
                    bdcXmzsRelList = queryBdcXmZsRelByProid(proid);
                    if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
                        Boolean isInsert = true;
                        for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                            if (bdcXmzsRel.getZsid().equals(zsid)) {
                                isInsert = false;
                                break;
                            }
                        }
                        if (isInsert) {
                            BdcXmzsRel bdcXmzsRel = creatBdcXmZsRel(zsid, proid);
                            bdcXmzsRelList.add(bdcXmzsRel);
                        }
                    } else {
                        bdcXmzsRelList = new ArrayList<BdcXmzsRel>();
                        BdcXmzsRel bdcXmzsRel = creatBdcXmZsRel(zsid, proid);
                        bdcXmzsRelList.add(bdcXmzsRel);
                    }
                }
            }
        }
        return bdcXmzsRelList;
    }

    @Override
    public void delBdcXmZsRelByZsid(final String zsid) {
        if (StringUtils.isNotBlank(zsid)) {
            Example example = new Example(BdcXmzsRel.class);
            example.createCriteria().andEqualTo("zsid", zsid);
            entityMapper.deleteByExample(BdcXmzsRel.class, example);

        }
    }

    @Transactional
    @Override
    public void batchDelBdcXmZsRelByBdcXmList(List<BdcXm> bdcXmList) {
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            bdcZsMapper.batchDelBdcXmZsRelByBdcXmList(bdcXmList);
        }
    }

    @Override
    public List<String> getZsidListByBdcXmList(List<BdcXm> bdcXmList) {
        List<String> zsidList = null;
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            zsidList = bdcZsMapper.getZsidListByBdcXmList(bdcXmList);
        }
        return zsidList;
    }
}
