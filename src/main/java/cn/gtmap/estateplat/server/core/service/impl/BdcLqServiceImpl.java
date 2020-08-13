package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcLqService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2017/2/15.
 */
@Repository
public class BdcLqServiceImpl implements BdcLqService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    BdcSpxxService bdcSpxxService;


    @Override
    public void saveBdcLqxx(BdcLq bdcLq) {
        entityMapper.saveOrUpdate(bdcLq, bdcLq.getQlid());
    }

    /**
     * @param model,qlid,bdcXm
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 初始化不动产构（建）筑物所有权登记信息页面
     */
    @Override
    public Model initBdcLqForPl(Model model, String qlid, BdcXm bdcXm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BdcZdDjlx> djlxlist = bdcZdGlService.getBdcDjlx();
        List<BdcZdSqlx> sqlxList = bdcZdGlService.getBdcSqlxList();
        List<HashMap> djzxList = bdcZdGlService.getDjzx(new HashMap());
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        BdcLq bdcLq = entityMapper.selectByPrimaryKey(BdcLq.class, qlid);
        String ldsyksqx = "";
        String ldsyjsqx = "";
        String djsj = "";
        if (bdcLq != null && bdcLq.getLdsyksqx() != null)
            ldsyksqx = sdf.format(bdcLq.getLdsyksqx());
        if (bdcLq != null && bdcLq.getLdsyjsqx() != null)
            ldsyjsqx = sdf.format(bdcLq.getLdsyjsqx());
        if (bdcLq != null && bdcLq.getDjsj() != null)
            djsj = sdf.format(bdcLq.getDjsj());
        String djlxMc="";
        if(StringUtils.isNotBlank(bdcXm.getDjlx())){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dm", bdcXm.getDjlx());
            List<HashMap> djlxList = bdcZdGlService.getBdcZdDjlx(map);
            if (CollectionUtils.isNotEmpty(djlxList) && djlxList.get(0).get("MC") != null)
                djlxMc=djlxList.get(0).get("MC").toString();
        }
        model.addAttribute("ldsyksqx", ldsyksqx);
        model.addAttribute("ldsyjsqx", ldsyjsqx);
        model.addAttribute("djsj", djsj);
        model.addAttribute("bdcLq", bdcLq);
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getMjdw()))
            model.addAttribute("mjdw", bdcSpxx.getMjdw());
        model.addAttribute("djlxlist", djlxlist);
        model.addAttribute("sqlxList", sqlxList);
        model.addAttribute("djzxList", djzxList);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("zjlxList",zjlxList);
        model.addAttribute("djlxMc", djlxMc);
        return model;
    }
}
