package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcSpfZdHjgxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *不动产商品房与宗地核减关系Mapper
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2016/9/13
 */
@Service
public class BdcSpfZdHjgxServiceImpl implements BdcSpfZdHjgxService{
    @Autowired
    BdcSpfZdHjgxMapper bdcSpfZdHjgxMapper;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    private EntityMapper entityMapper;


    @Override
    public List<BdcSpfZdHjgx> getBdcZdFwRelList(String proid) {
        HashMap map = Maps.newHashMap();
        map.put("proid",proid);
        return bdcSpfZdHjgxMapper.getBdcZdFwRelList(map);
    }

    @Override
    public void updateSydyjeAndSyfttdmj(String proid) {
        if(StringUtils.isNotBlank(proid)){
            List<BdcSpfZdHjgx> bdcSpfZdHjgxList= getBdcZdFwRelList(proid);
            if(CollectionUtils.isNotEmpty(bdcSpfZdHjgxList)){
                Double fwhjtdmj=0.0;
                Double xmhjdyje=bdcSpfZdHjgxList.get(0).getXmhjdyje();
                for(BdcSpfZdHjgx bdcSpfZdHjgx:bdcSpfZdHjgxList){
                    if(bdcSpfZdHjgx!=null && bdcSpfZdHjgx.getFwhjtdmj()!=null)
                        fwhjtdmj=fwhjtdmj+bdcSpfZdHjgx.getFwhjtdmj();
                }
                String zdbdcdyh=bdcSpfZdHjgxList.get(0).getZdbdcdyh();
                String zdDjh=bdcSpfZdHjgxList.get(0).getDjh();
                List<BdcXm> bdcXmList= null;
                if(StringUtils.isNotBlank(zdbdcdyh)){
                    bdcXmList=   bdcXmService.queryBdcxmByBdcdyh(zdbdcdyh);
                }else  if(StringUtils.isNotBlank(zdDjh)){
                    bdcXmList=   bdcXmService.queryBdcxmByBdcdyh(zdDjh+"W00000000");
                }
                BdcDyaq bdcDyaq = null;
                if(CollectionUtils.isNotEmpty(bdcXmList)){
                    for(BdcXm bdcXm:bdcXmList) {
                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                        QllxVo qllxVo = qllxService.queryQllxVo(new BdcDyaq(), bdcXm.getProid());
                        if (qllxVo instanceof BdcDyaq) {
                            bdcDyaq = (BdcDyaq) qllxVo;
                            if(bdcDyaq.getQszt()!= Constants.QLLX_QSZT_HR) {
                                if (bdcDyaq.getSydyje() != null && xmhjdyje != null)
                                    bdcDyaq.setSydyje(bdcDyaq.getSydyje() - xmhjdyje);
                                else if (bdcDyaq.getBdbzzqse() != null && xmhjdyje != null)
                                    bdcDyaq.setSydyje(bdcDyaq.getBdbzzqse() - xmhjdyje);
                                if (bdcDyaq.getSydymj() != null && fwhjtdmj != null)
                                    bdcDyaq.setSydymj(bdcDyaq.getSydymj() - fwhjtdmj);
                                else if (bdcSpxx != null && bdcSpxx.getZdzhmj() != null && fwhjtdmj != null)
                                    bdcDyaq.setSydymj(bdcSpxx.getZdzhmj() - fwhjtdmj);
                                entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                            }
                        }
                    }
                }
                if(bdcDyaq==null && StringUtils.isNotBlank(zdDjh)) {
                    List<String> qlids=gdTdService.getGdTdQlidByDjh(zdDjh);
                    GdTd gdTd=gdTdService.getGdTdByDjh(zdDjh);
                    if(CollectionUtils.isNotEmpty(qlids)){
                        for(String qlid:qlids){
                            GdDy gdDy=gdTdService.getGddyqByQlid(qlid,Constants.GDQL_ISZX_WZX);
                            if(gdDy!=null){
                                if (gdDy.getSydyje() != null && xmhjdyje != null)
                                    gdDy.setSydyje(gdDy.getSydyje() - xmhjdyje);
                                else if (gdDy.getBdbzzqse() != null && xmhjdyje != null)
                                    gdDy.setSydyje(gdDy.getBdbzzqse() - xmhjdyje);
                                if (gdDy.getSydymj() != null && fwhjtdmj != null)
                                    gdDy.setSydymj(gdDy.getSydymj() - fwhjtdmj);
                                else if (gdTd != null && gdTd.getZdmj() != null && fwhjtdmj != null)
                                    gdDy.setSydymj(gdTd.getZdmj() - fwhjtdmj);
                                entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateXmhjdyje(String proid) {
        if(StringUtils.isNotBlank(proid)){
            List<BdcSpfZdHjgx> bdcSpfZdHjgxList= getBdcZdFwRelList(proid);
            if(CollectionUtils.isNotEmpty(bdcSpfZdHjgxList)){
                Double xmhjdyje=0.0;
                for(BdcSpfZdHjgx bdcSpfZdHjgx:bdcSpfZdHjgxList){
                    if(bdcSpfZdHjgx!=null && bdcSpfZdHjgx.getXmhjdyje()!=null) {
                        xmhjdyje = bdcSpfZdHjgx.getXmhjdyje();
                        break;
                    }
                }
                if(xmhjdyje!=0){
                    for(BdcSpfZdHjgx bdcSpfZdHjgx:bdcSpfZdHjgxList){
                        bdcSpfZdHjgx.setXmhjdyje(xmhjdyje);
                        entityMapper.saveOrUpdate(bdcSpfZdHjgx, bdcSpfZdHjgx.getRelid());
                    }
                }

            }
        }
    }

    @Override
    public List<BdcSpfZdHjgx> getBdcZdFwRelListByBdcdyh(String fwbdcdyh) {
        HashMap map = Maps.newHashMap();
        map.put("fwbdcdyh",fwbdcdyh);
        return bdcSpfZdHjgxMapper.getBdcZdFwRelList(map);
    }
}
