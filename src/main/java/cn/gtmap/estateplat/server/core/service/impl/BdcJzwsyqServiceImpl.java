package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcJzwsyqService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:Will@gtmap.cn">Will</a>
 * @version 1.0, 2017-02-16
 * @description 构（建）筑物所有权登记信息
 */
@Repository
public class BdcJzwsyqServiceImpl implements BdcJzwsyqService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    /**
     * @param bdcJzwsyq
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 保存构（建）筑物所有权登记信息
     */
    @Override
    public void saveBdcJzwsyq(BdcJzwsyq bdcJzwsyq) {
        entityMapper.saveOrUpdate(bdcJzwsyq, bdcJzwsyq.getQlid());
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产构（建）筑物所有权登记信息页面
     */
    @Override
    public  Model initBdcJzwsyqForPl(Model model, String qlid, BdcXm bdcXm){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<HashMap> gjzwLxList = bdcZdGlService.getGjzwLxZdb(new HashMap());
        List<BdcZdFwyt> fwytList=bdcZdGlService.getBdcZdFwyt();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        String syksqx="";
        String syjsqx="";
        String jgsj="";
        String djsj="";
        BdcJzwsyq bdcJzwsyq = entityMapper.selectByPrimaryKey(BdcJzwsyq.class, qlid);
        if(bdcJzwsyq!=null&&bdcJzwsyq.getSyksqx()!=null)
            syksqx = sdf.format(bdcJzwsyq.getSyksqx());
        if(bdcJzwsyq!=null&&bdcJzwsyq.getSyjsqx()!=null)
            syjsqx = sdf.format(bdcJzwsyq.getSyjsqx());
        if(bdcJzwsyq!=null&&bdcJzwsyq.getJgsj()!=null)
            jgsj = sdf.format(bdcJzwsyq.getJgsj());
        if(bdcJzwsyq!=null&&bdcJzwsyq.getDjsj()!=null)
            djsj = sdf.format(bdcJzwsyq.getDjsj());
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("gjzwLxList", gjzwLxList);
        model.addAttribute("fwytList", fwytList);
        model.addAttribute("bdcJzwsyq", bdcJzwsyq);
        model.addAttribute("syksqx", syksqx);
        model.addAttribute("syjsqx", syjsqx);
        model.addAttribute("jgsj", jgsj);
        model.addAttribute("djsj", djsj);
        model.addAttribute("zjlxList",zjlxList);
        return  model;
    }
}
