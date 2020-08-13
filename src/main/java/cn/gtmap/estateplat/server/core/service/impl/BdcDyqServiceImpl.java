package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:hzj@gtmap.cn">hzj</a>
 * @version 1.0, 2017/2/16
 * @description 
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDyqMapper;
import cn.gtmap.estateplat.server.core.service.BdcDyqService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class BdcDyqServiceImpl implements BdcDyqService {

    @Autowired
    private  EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcDyqMapper bdcDyqMapper;
    @Override
    public void saveBdcDyq(BdcDyq bdcDyq) {
        entityMapper.saveOrUpdate(bdcDyq,bdcDyq.getQlid());
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产建设用地使用权、宅基地使用权信息页面
     */
    @Override
    public Model initBdcDyqForPl(Model model, String qlid, BdcXm bdcXm){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BdcDyq bdcDyq = entityMapper.selectByPrimaryKey(BdcDyq.class, qlid);
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        String dyqksqx="";
        String dyqjsqx="";
        String djsj="";
        if(bdcDyq!=null&&bdcDyq.getDyqksqx()!=null){
            dyqksqx=sdf.format(bdcDyq.getDyqksqx());
        }
        if(bdcDyq!=null&&bdcDyq.getDyqjsqx()!=null){
            dyqjsqx=sdf.format(bdcDyq.getDyqjsqx());
        }
        if(bdcDyq!=null&&bdcDyq.getDjsj()!=null){
            djsj=sdf.format(bdcDyq.getDjsj());
        }
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("bdcYwrList",bdcYwrList);
        model.addAttribute("djlxList",djlxList);
        model.addAttribute("dyqksqx",dyqksqx);
        model.addAttribute("dyqjsqx",dyqjsqx);
        model.addAttribute("djsj",djsj);
        model.addAttribute("bdcDyq", bdcDyq);
        model.addAttribute("zjlxList", zjlxList);
        return  model;
    }

    @Override
    public BdcDyq queryBdcDyqByProid(String proid) {
        if(StringUtils.isNotBlank(proid)){
            return bdcDyqMapper.getBdcDyqByProid(proid);
        }
        return null;
    }
}
