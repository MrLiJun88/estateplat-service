package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcYgMapper;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
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
 * Created by lst on 2015/3/17.
 */
@Repository
public class BdcYgServiceImpl implements BdcYgService {
    @Autowired
    private BdcYgMapper bdcYgMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public List<BdcYg> getBdcYgList(final String bdcdyh,final String qszt) {
        List<BdcYg> bdcYgList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            HashMap map = new HashMap();
            map.put("bdcdyh", bdcdyh);
            map.put("qszt", qszt);
            bdcYgList = bdcYgMapper.getBdcYgList(map);
        }
        return bdcYgList;
    }

    @Override
    public List<BdcYg> getBdcYgList(HashMap map) {
        return bdcYgMapper.getBdcYgList(map);
    }

    @Override
    public void updateBdcYg(BdcYg bdcYg) {
        entityMapper.updateByPrimaryKeySelective(bdcYg);
    }

    @Override
    public BdcYg getBdcYgByProid(final String proid) {
        if (StringUtils.isNotBlank(proid)) {
            HashMap map = new HashMap();
            map.put("proid", proid);
            List<BdcYg> bdcYgList = bdcYgMapper.getBdcYgList(map);
            if (CollectionUtils.isNotEmpty(bdcYgList))
                return bdcYgList.get(0);
        }
        return null;
    }

    @Override
    public List<BdcYg> getBdcYgList(final String bdcdyh, final String qszt, final String ygdjzl) {
        List<BdcYg> bdcYgList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            HashMap map = new HashMap();
            map.put("bdcdyh", bdcdyh);
            map.put("qszt", qszt);
            if (StringUtils.indexOf(ygdjzl, ",") > -1) {
                map.put("ygdjzls", StringUtils.split(ygdjzl, ","));
            } else{
                map.put("ygdjzl", ygdjzl);
            }
            bdcYgList = bdcYgMapper.getBdcYgList(map);
        }
        return bdcYgList;
    }
    @Override
    public  void saveYgxx(BdcYg bdcYg){
        entityMapper.saveOrUpdate(bdcYg,bdcYg.getQlid());
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产预告信息页面
     */
    @Override
    public Model initBdcYgForPl(Model model, String qlid, BdcXm bdcXm){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String bdcqzh="";
        String zwlxksqx="";
        String zwlxjsqx="";
        String djsj="";
        BdcYg bdcYg = entityMapper.selectByPrimaryKey(BdcYg.class, qlid);
        List<BdcZdDjlx> djlxList=bdcZdGlService.getBdcDjlx();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        if(bdcYg!=null&&bdcYg.getZwlxksqx()!=null){
            zwlxksqx=sdf.format(bdcYg.getZwlxksqx());
        }
        if(bdcYg!=null&&bdcYg.getZwlxjsqx()!=null){
            zwlxjsqx=sdf.format(bdcYg.getZwlxjsqx());
        }
        if(bdcYg!=null&&bdcYg.getDjsj()!=null){
            djsj=sdf.format(bdcYg.getDjsj());
        }
        model.addAttribute("bdcqzh",bdcqzh);
        model.addAttribute("zwlxksqx",zwlxksqx);
        model.addAttribute("zwlxjsqx",zwlxjsqx);
        model.addAttribute("djsj",djsj);
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("bdcYg", bdcYg);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("bdcYwrList", bdcYwrList);
        model.addAttribute("zjlxList",zjlxList);
        return model;
    }
}
