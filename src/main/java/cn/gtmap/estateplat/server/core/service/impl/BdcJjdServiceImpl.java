package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJjd;
import cn.gtmap.estateplat.model.server.core.BdcJjdXx;
import cn.gtmap.estateplat.server.core.mapper.BdcJjdMapper;
import cn.gtmap.estateplat.server.core.service.BdcJjdService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 15-12-10
 * Time: 下午8:14
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcJjdServiceImpl implements BdcJjdService {
    @Autowired
    private BdcJjdMapper bdcJjdMapper;
    @Autowired
    private EntityMapper entityMapper;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional(readOnly = true)
    public BdcJjdXx getBdcJjdXxBySlh(final String slh) {
        Example example = new Example(BdcJjdXx.class);
        example.createCriteria().andEqualTo("slh", slh);
        List<BdcJjdXx> bdcJjdXxList = entityMapper.selectByExample(BdcJjdXx.class, example);
        if (CollectionUtils.isNotEmpty(bdcJjdXxList)) {
            return bdcJjdXxList.get(0);
        }
        return null;
    }

    @Override
    public List<BdcJjdXx> getJjdxxJsonByPage(final Map map) {
        return bdcJjdMapper.getJjdxxJsonByPage(map);
    }

    @Override
    @Transactional
    public String creatBdcJjd(String slhs, String user) {
        String msg = "创建失败";
        StringBuilder error = new StringBuilder();
        boolean isSave = true;
        try {
            HashMap map = new HashMap();
            if (slhs.split(",").length > 0) {
                map.put("bhs", slhs.split(","));
            }
            List<BdcJjdXx> bdcJjdXxLists = getJjdxxJsonByPage(map);
            if (CollectionUtils.isNotEmpty(bdcJjdXxLists)) {
                Calendar a = Calendar.getInstance();
                String nf = a.get(Calendar.YEAR) + "";
                BdcJjd bdcJjd = new BdcJjd();
                bdcJjd.setJjdid(UUIDGenerator.generate18());
                bdcJjd.setJjrq(new Date());
                bdcJjd.setJjr(user);
                bdcJjd.setJjdbh(getJjdXh(nf));
                List<BdcJjdXx> bdcJjdXxList = new ArrayList<BdcJjdXx>();
                int i = 1;
                for (BdcJjdXx bdcJjdXx : bdcJjdXxLists) {
                    if (StringUtils.isNotBlank(bdcJjdXx.getSlh())) {
                        BdcJjdXx bdcJjdXxTemp = getBdcJjdXxBySlh(bdcJjdXx.getSlh());
                        if (bdcJjdXxTemp != null) {
                            error.append(bdcJjdXxTemp.getSlh()).append(",");
                            isSave = false;
                        } else {
                            bdcJjdXx.setJjdxxid(UUIDGenerator.generate18());
                            bdcJjdXx.setJjdid(bdcJjd.getJjdid());
                            bdcJjdXx.setXl(i + "");
                        }
                        bdcJjdXxList.add(bdcJjdXx);
                    }
                    i++;
                }
                if (isSave) {
                    entityMapper.saveOrUpdate(bdcJjd, bdcJjd.getJjdid());
                    for (BdcJjdXx bdcJjdXx : bdcJjdXxList) {
                        entityMapper.saveOrUpdate(bdcJjdXx, bdcJjdXx.getJjdxxid());
                    }
                    msg = bdcJjd.getJjdid();
                } else {
                    msg = "创建失败,受理号为：" + error + "已生成交接单";
                }
            } else {
                msg = "创建失败,没有找到对应的项目信息";
            }
        } catch (Exception e) {
            logger.error("BdcJjdServiceImpl.creatBdcJjd", e);
        }
        return msg;
    }

    @Override
    public String getJjdXh(final String nf) {
        String jjdbh = "";
        String xh = bdcJjdMapper.getJjdXh(nf);
        if (StringUtils.isNotBlank(xh)) {
            jjdbh = nf + xh;
        } else {
            jjdbh = nf + "000001";
        }
        return jjdbh;
    }

    @Override
    @Transactional(readOnly = true)
    public String deleteJjd(final String jjdid) {
        String msg = "删除成功";
        try {
            Example example = new Example(BdcJjd.class);
            example.createCriteria().andEqualTo("jjdid", jjdid);
            entityMapper.deleteByPrimaryKey(BdcJjd.class, example);
            msg = "删除成功";
        } catch (Exception e) {
            logger.error("BdcJjdServiceImpl.deleteJjd", e);
        }
        return msg;
    }

    @Override
    public BdcJjdXx getBdcJjdXxByProid(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcJjdXx.class);
            example.createCriteria().andEqualTo("proid", proid);
            List<BdcJjdXx> bdcJjdXxList = entityMapper.selectByExample(BdcJjdXx.class, example);
            if (CollectionUtils.isNotEmpty(bdcJjdXxList)) {
                return bdcJjdXxList.get(0);
            }
        }
        return null;
    }
}
