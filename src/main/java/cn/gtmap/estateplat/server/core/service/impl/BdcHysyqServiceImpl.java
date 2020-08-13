package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcHysyqMapper;
import cn.gtmap.estateplat.server.core.service.BdcHysyqService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 海域使用权
 */
@Repository
public class BdcHysyqServiceImpl implements BdcHysyqService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    /**
     *
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 海域使用权Mapper接口
     */
    @Autowired
    BdcHysyqMapper bdcHysyqMapper;

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param map
     * @description 获取海域（含无居民海岛） 使用权登记信息
     */
    @Override
    @Transactional(readOnly = true)
    public BdcHysyq getBdcHysyq(final Map map) {
        return bdcHysyqMapper.getBdcHysyq(map);
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcHysyq 海域使用权对象
     * @description 保存海域（含无居民海岛） 使用权登记信息
     */
    @Override
    @Transactional
    public void saveBdcHysyq(BdcHysyq bdcHysyq) {
        bdcHysyqMapper.saveBdcHysyq(bdcHysyq);
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产海域（含无居民海岛） 使用权登记信息页面
     */
    @Override
    public Model initBdcHysyqForPl(Model model, String qlid, BdcXm bdcXm){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        String syksqx="";
        String syjsqx="";
        String djsj="";
        String bdcqzh="";
        BdcHysyq bdcHysyq = entityMapper.selectByPrimaryKey(BdcHysyq.class,qlid);
        if(bdcHysyq!=null&&bdcHysyq.getSyksqx()!=null)
            syksqx = sdf.format(bdcHysyq.getSyksqx());
        if(bdcHysyq!=null&&bdcHysyq.getSyjsqx()!=null)
            syjsqx = sdf.format(bdcHysyq.getSyjsqx());
        if(bdcHysyq!=null&&bdcHysyq.getDjsj()!=null)
            djsj = sdf.format(bdcHysyq.getDjsj());
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("syksqx", syksqx);
        model.addAttribute("syjsqx", syjsqx);
        model.addAttribute("bdcqzh", bdcqzh);
        model.addAttribute("djsj", djsj);
        model.addAttribute("bdcHysyq", bdcHysyq);
        model.addAttribute("zjlxList",zjlxList);
        return  model;
    }
}
