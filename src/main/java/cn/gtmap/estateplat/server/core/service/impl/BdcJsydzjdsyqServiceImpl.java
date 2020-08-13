package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcJsydzjdsyqMapper;
import cn.gtmap.estateplat.server.core.service.BdcJsydzjdsyqService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 15-10-27
 * Time: 上午10:05
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcJsydzjdsyqServiceImpl implements BdcJsydzjdsyqService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    BdcSpxxService bdcSpxxService;

    /**
     * mapper接口
     *
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description
     */
    @Autowired
    BdcJsydzjdsyqMapper bdcJsydzjdsyqMapper;

    /**
     * @param map
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 获取建设用地使用权、宅基地使用权登记信息
     */
    @Override
    @Transactional(readOnly = true)
    public BdcJsydzjdsyq getBdcJsydzjdsyq(final Map map) {
        return bdcJsydzjdsyqMapper.getBdcJsydzjdsyq(map);
    }

    @Override
    public BdcJsydzjdsyq getBdcJsydzjdsyq(String proid) {
        BdcJsydzjdsyq bdcJsydzjdsyq = null;
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcJsydzjdsyq.class);
            example.createCriteria().andEqualTo("proid", proid);
            List<BdcJsydzjdsyq> bdcJsydzjdsyqList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                bdcJsydzjdsyq = bdcJsydzjdsyqList.get(0);
            }
        }
        return bdcJsydzjdsyq;
    }

    @Override
    public List<BdcJsydzjdsyq> getBdcJsydzjdsyqList(Map map) {
        return bdcJsydzjdsyqMapper.getBdcJsydzjdsyqList(map);
    }

    /**
     * @param djh 地籍号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据地籍号，获取建设用地
     */
    @Override
    @Transactional(readOnly = true)
    public List<BdcJsydzjdsyq> getJsyByDjh(final String djh) {
        return bdcJsydzjdsyqMapper.getJsyByDjh(djh);
    }

    /**
     * @param bdcJsydzjdsyq
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 保存建设用地使用权、宅基地使用权登记信息
     */
    @Override
    public void saveBdcJsydzjdsyq(BdcJsydzjdsyq bdcJsydzjdsyq) {
        entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
    }

    /**
     * @param model,qlid,bdcXm
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 初始化不动产建设用地使用权、宅基地使用权信息页面
     */
    @Override
    public Model initBdcJsydzjdsyqForPl(Model model, String qlid, BdcXm bdcXm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        String syksqx = "";
        String syjsqx = "";
        String djsj = "";
        BdcJsydzjdsyq bdcJsydzjdsyq = entityMapper.selectByPrimaryKey(BdcJsydzjdsyq.class, qlid);
        if (bdcJsydzjdsyq != null && bdcJsydzjdsyq.getSyksqx() != null)
            syksqx = sdf.format(bdcJsydzjdsyq.getSyksqx());
        if (bdcJsydzjdsyq != null && bdcJsydzjdsyq.getSyjsqx() != null)
            syjsqx = sdf.format(bdcJsydzjdsyq.getSyjsqx());
        if (bdcJsydzjdsyq != null && bdcJsydzjdsyq.getDjsj() != null)
            djsj = sdf.format(bdcJsydzjdsyq.getDjsj());
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("bdcJsydzjdsyq", bdcJsydzjdsyq);
        model.addAttribute("syksqx", syksqx);
        model.addAttribute("syjsqx", syjsqx);
        model.addAttribute("djsj", djsj);
        model.addAttribute("zjlxList", zjlxList);
        return model;
    }
}
