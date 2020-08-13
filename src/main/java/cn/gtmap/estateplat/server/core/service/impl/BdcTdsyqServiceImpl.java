package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:hzj@gtmap.cn">hzj</a>
 * @version 1.0, 2017/2/16
 * @description 
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.utils.CalendarUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BdcTdsyqServiceImpl implements BdcTdsyqService{
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    private GdTdService gdTdService;
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存土地所有权登记信息
    *@Date 15:34 2017/2/16
    */
    @Autowired
   private EntityMapper entityMapper;
    @Override
    public  void saveBdcTdsyq(BdcTdsyq bdcTdsyq){
        entityMapper.saveOrUpdate(bdcTdsyq,bdcTdsyq.getQlid());
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产土地所有权登记信息页面
     */
    @Override
    public Model initBdcTdsyqForPl(Model model, String qlid, BdcXm bdcXm){
        String ywh="";
        String bdcqzsh="";
        String djsj="";
        BdcTdsyq bdcTdsyq = entityMapper.selectByPrimaryKey(BdcTdsyq.class, qlid);
        model.addAttribute("bdcTdsyq", bdcTdsyq);
        //处理面积单位
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getMjdw()))
            model.addAttribute("mjdw", bdcSpxx.getMjdw());
        if(bdcTdsyq!=null&&bdcTdsyq.getDjsj()!=null){
            djsj= CalendarUtil.formateDatetoStr(bdcTdsyq.getDjsj());

        }
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        String djlxMc="";
        if(StringUtils.isNotBlank(bdcXm.getDjlx())){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dm", bdcXm.getDjlx());
            List<HashMap>  djlxList1 = bdcZdGlService.getBdcZdDjlx(map);
            if (CollectionUtils.isNotEmpty(djlxList1) && djlxList1.get(0).get("MC") != null)
                djlxMc=djlxList1.get(0).get("MC").toString();
        }
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("ywh", ywh);
        model.addAttribute("bdcqzsh", bdcqzsh);
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("djsj", djsj);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("zjlxList", zjlxList);
        model.addAttribute("djlxMc", djlxMc);
        return model;
    }

    @Override
    public void updateCqzhjcByQlid(String qlid) {
        GdTdsyq gdTdsyq = queryGdtdsyqByQlid(qlid);
        if(gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getTdzh())){
            //将土地证号去掉中英文括号存到cqzhjc字段
            String regex = "[(]|[)]|[\uff08-\uff09]";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(gdTdsyq.getTdzh());
            gdTdsyq.setCqzhjc(mat.replaceAll(""));
            entityMapper.saveOrUpdate(gdTdsyq,gdTdsyq.getQlid());
        }
    }

    @Override
    public GdTdsyq queryGdtdsyqByQlid(String qlid) {
        return StringUtils.isNotBlank(qlid)?entityMapper.selectByPrimaryKey(GdTdsyq.class,qlid):null;
    }
}
