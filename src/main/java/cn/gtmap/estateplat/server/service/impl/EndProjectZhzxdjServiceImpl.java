package cn.gtmap.estateplat.server.service.impl;/* 
 * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
 * @version 1.0, 16-12-16
 * @description      不动产土地逐户注销登记
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class EndProjectZhzxdjServiceImpl extends EndProjectDefaultServiceImpl implements EndProjectService {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcSpfZdHjgxService bdcSpfZdHjgxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            BdcXmRel bdcXmRel = bdcXmRelList.get(0);
            List<BdcSpfZdHjgx> bdcSpfZdHjgxList = bdcSpfZdHjgxService.getBdcZdFwRelList(bdcXm.getProid());
            Double fwhjtjmj = 0.0;
            Date hjsj = null;

            if (CollectionUtils.isNotEmpty(bdcSpfZdHjgxList)) {
                for (BdcSpfZdHjgx bdcSpfZdHjgx : bdcSpfZdHjgxList) {
                    if (bdcSpfZdHjgx != null ){
                        if (bdcSpfZdHjgx.getFwhjtdmj() != null) {
                            fwhjtjmj = fwhjtjmj + bdcSpfZdHjgx.getFwhjtdmj();
                        }
                        hjsj = bdcSpfZdHjgx.getHjsj();
                    }
                }
            }
            StringBuilder builder = new StringBuilder();
            builder.append(CalendarUtil.formateToStrChinaYMDDate(hjsj) + "," + bdcSpfZdHjgxList.size() + "个不动产单元进行解押，分摊面积为："
                    + fwhjtjmj + "㎡");
            if (StringUtils.equals(bdcXmRel.getYdjxmly(), Constants.XMLY_BDC)) {
                String yproid = bdcXmRel.getYproid();
                if (StringUtils.isNotBlank(yproid)) {
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(yproid);
                    QllxVo yqllxVo = qllxService.makeSureQllx(ybdcXm);
                    yqllxVo = qllxService.queryQllxVo(yqllxVo, ybdcXm.getProid());
                    if (yqllxVo != null && StringUtils.isNotBlank(yqllxVo.getQlid())&&yqllxVo instanceof BdcDyaq) {
                        BdcDyaq bdcDyaq = (BdcDyaq) yqllxVo;
                        if (bdcDyaq.getSydymj() != null && fwhjtjmj != null)
                            bdcDyaq.setSydymj(bdcDyaq.getSydymj() - fwhjtjmj);
                        else if (bdcSpxx != null && bdcSpxx.getZdzhmj() != null && fwhjtjmj != null)
                            bdcDyaq.setSydymj(bdcSpxx.getZdzhmj() - fwhjtjmj);
                        if (StringUtils.isNotBlank(bdcDyaq.getFj())) {
                            bdcDyaq.setFj(bdcDyaq.getFj() + "\n" + builder.toString());
                        } else {
                            bdcDyaq.setFj(builder.toString());
                        }
                        yqllxVo = bdcDyaq;
                        entityMapper.updateByPrimaryKeySelective(yqllxVo);
                        //证书附记更新
                        List<BdcZs> bdcZsList=bdcZsService.queryBdcZsByProid(ybdcXm.getProid());
                        if(CollectionUtils.isNotEmpty(bdcZsList)){
                            for(BdcZs bdcZs:bdcZsList){
                                if (StringUtils.isNotBlank(bdcZs.getFj())) {
                                    bdcZs.setFj(bdcZs.getFj() + "\n" + builder.toString());
                                } else {
                                    bdcZs.setFj(builder.toString());
                                }
                                entityMapper.updateByPrimaryKeySelective(bdcZs);
                            }
                        }
                    }
                }
            } else if (StringUtils.equals(bdcXmRel.getYdjxmly(), Constants.XMLY_TDSP) && StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                GdDy gdDy = gdTdService.getGddyqByQlid(bdcXmRel.getYqlid(),Constants.GDQL_ISZX_WZX);
                GdTd gdTd=null;
                if (gdDy != null) {
                    List<GdBdcQlRel> gdBdcQlRelList=gdBdcQlRelService.queryGdBdcQlListByQlid(gdDy.getDyid());
                    if(CollectionUtils.isNotEmpty(gdBdcQlRelList)&&StringUtils.isNotBlank(gdBdcQlRelList.get(0).getBdcid())){
                        gdTd=gdTdService.queryGdTd(gdBdcQlRelList.get(0).getBdcid());
                    }
                    if (StringUtils.isNotBlank(gdDy.getFj())) {
                        gdDy.setFj(gdDy.getFj() + "\n" + builder.toString());
                    } else {
                        gdDy.setFj(builder.toString());
                    }
                    if (gdDy.getSydymj() != null && fwhjtjmj != null) {
                        gdDy.setSydymj(gdDy.getSydymj() - fwhjtjmj);
                    }
                    else if (gdTd != null && gdTd.getZdmj() != null && fwhjtjmj != null) {
                        gdDy.setSydymj(gdTd.getZdmj() - fwhjtjmj);
                    }
                    entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
                }
            }

            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }

    }
    @Override
    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);
    }
}
