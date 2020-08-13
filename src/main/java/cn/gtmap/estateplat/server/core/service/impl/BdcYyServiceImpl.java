package cn.gtmap.estateplat.server.core.service.impl;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.model.server.core.BdcZdZjlx;
import cn.gtmap.estateplat.server.core.mapper.BdcYyMapper;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcYyService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-11
 */
@Service
public class BdcYyServiceImpl implements BdcYyService {
    @Autowired
    BdcYyMapper bdcYyMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcZdGlService bdcZdGlService;

    @Override
    @Transactional(readOnly = true)
    public List<BdcYy> queryBdcYy(final Map map) {
        return bdcYyMapper.queryBdcYy(map);
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param bdcYy
     * @return
     * @description 保存不动产异议信息
     */
    @Override
    public void saveBdcYy(BdcYy bdcYy){
        entityMapper.saveOrUpdate(bdcYy,bdcYy.getQlid());
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产异议信息页面
     */
    @Override
    public Model initBdcYyForPl(Model model, String qlid, BdcXm bdcXm){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        String djsj="";
        String zxsj="";
        BdcYy bdcYy = entityMapper.selectByPrimaryKey(BdcYy.class,qlid);
        if(bdcYy!=null&&bdcYy.getDjsj()!=null){
            djsj=sdf.format(bdcYy.getDjsj());
        }
        if(bdcYy!=null&&bdcYy.getZxsj()!=null){
            zxsj=sdf.format(bdcYy.getZxsj());
        }
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("bdcYy", bdcYy);
        model.addAttribute("djsj", djsj);
        model.addAttribute("zxsj", zxsj);
        model.addAttribute("zjlxList",zjlxList);
        return model;
    }

    @Override
    public BdcYy getBdcYyByProid(String proid) {
        if(StringUtils.isNotBlank(proid)){
            return  bdcYyMapper.getBdcYyByProid(proid);
        }
        return null;
    }
}
