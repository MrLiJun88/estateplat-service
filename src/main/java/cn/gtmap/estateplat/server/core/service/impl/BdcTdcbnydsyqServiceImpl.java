package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:hzj@gtmap.cn">hzj</a>
 * @version 1.0, 2017/2/16
 * @description 
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcTdcbnydsyqService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class BdcTdcbnydsyqServiceImpl implements BdcTdcbnydsyqService {
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存土地承包经营权、农用地的其他使用权登记信息（非林地）
    *@Date 14:53 2017/2/16
    */
    @Autowired
    private EntityMapper entityMapper;
    @Override
    public void saveBdcTdcbnydsyq(BdcTdcbnydsyq bdcTdcbnydsyq) {
        entityMapper.saveOrUpdate(bdcTdcbnydsyq,bdcTdcbnydsyq.getQlid());
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产土地承包经营权、农用地的其他使用权登记信息（非林地）
     */
    @Override
    public Model initBdcTdcbnydsyqForPl(Model model, String qlid, BdcXm bdcXm){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BdcTdcbnydsyq bdcTdcbnydsyq = entityMapper.selectByPrimaryKey(BdcTdcbnydsyq.class, qlid);
        List<BdcZdDjlx> djlxList=bdcZdGlService.getBdcDjlx();
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        String syksqx="";
        String syjsqx="";
        String djsj="";
        String bdcqzh="";
        if(bdcTdcbnydsyq!=null&&bdcTdcbnydsyq.getDjsj()!=null){
            djsj=sdf.format(bdcTdcbnydsyq.getDjsj());
        }
        if(bdcTdcbnydsyq!=null&&bdcTdcbnydsyq.getSyksqx()!=null){
            syksqx=sdf.format(bdcTdcbnydsyq.getSyksqx());
        }
        if(bdcTdcbnydsyq!=null&&bdcTdcbnydsyq.getSyjsqx()!=null){
            syjsqx=sdf.format(bdcTdcbnydsyq.getSyjsqx());
        }
        model.addAttribute("syksqx",syksqx);
        model.addAttribute("syjsqx",syjsqx);
        model.addAttribute("djsj",djsj);
        model.addAttribute("bdcqzh",bdcqzh);
        model.addAttribute("djlxList",djlxList);
        model.addAttribute("bdcTdcbnydsyq", bdcTdcbnydsyq);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("zjlxList",zjlxList);
        return model;
    }
}
