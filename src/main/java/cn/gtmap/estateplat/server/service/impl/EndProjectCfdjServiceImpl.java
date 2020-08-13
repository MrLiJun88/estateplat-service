package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * 查封登记的办结业务逻辑与默认服务一致
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class EndProjectCfdjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcdyService bdcdyService;

    public void changeXmzt(BdcXm bdcXm) {
        //zdd 处理轮候查封序号
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
        if (qllxVo instanceof BdcCf) {
            BdcCf bdcCf = (BdcCf) qllxVo;
            if (Constants.CFLX_LHCF.equals(bdcCf.getCflx())&&bdcCf.getLhsx() == null) {
                //更新lhsx  如果已经有值  不更新
                HashMap map = new HashMap();
                map.put("bdcdyid", bdcCf.getBdcdyid());
                map.put("cflx", Constants.CFLX_LHCF);
                map.put("qszt", Constants.QLLX_QSZT_XS);
                List<BdcCf> bdcCfList = bdcCfService.andEqualQueryCf(map, "to_number(lhsx) desc");
                List<Integer> lhsxList = new ArrayList<Integer>();
                lhsxList.add(0);
                if(CollectionUtils.isNotEmpty(bdcCfList)){
                    for(BdcCf cf : bdcCfList){
                        if(cf.getLhsx() != null){
                            lhsxList.add(cf.getLhsx());
                        }
                    }

                }
                bdcCf.setLhsx(Collections.max(lhsxList) + 1);
                entityMapper.updateByPrimaryKeySelective(bdcCf);
            }
        }
        bdcXmService.endBdcXm(bdcXm);
        qllxService.endQllxZt(bdcXm);


        /**
         * @author bianwen
         * @description 更新查封顺序
         */
        if (qllxVo instanceof BdcCf) {
            BdcCf bdcCf = (BdcCf) qllxVo;
            if(StringUtils.isNotBlank(bdcCf.getBdcdyid())){
                BdcBdcdy bdcBdcdy=bdcdyService.queryBdcdyById(bdcCf.getBdcdyid());
                if(bdcBdcdy!=null){
                    bdcCfService.updateCfsx(bdcCf.getCflx(),bdcBdcdy.getBdcdyh(),null,null);
                }
            }
        }
        else {
            String bdcid="";
            String cflx="";
            List<BdcXmRel> bdcxmrelList=bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if(CollectionUtils.isNotEmpty(bdcxmrelList)){
                String yqlid=bdcxmrelList.get(0).getYqlid();
                if(StringUtils.isNotBlank(yqlid)){
                    HashMap param=new HashMap();
                    param.put("qlid",yqlid);
                    List<GdBdcQlRel> relList= gdXmService.getGdBdcQlRelByQlidAndBdcId(param);
                    if(CollectionUtils.isNotEmpty(relList)){
                        bdcid=relList.get(0).getBdcid();
                    }
                    List<GdCf> gdCfList=gdXmService.getGdCfList(param);
                    if(CollectionUtils.isNotEmpty(gdCfList)){
                        cflx=gdCfList.get(0).getCflx();
                    }
                }
            }
            if(bdcXm.getXmly().equals(Constants.XMLY_TDSP)){   //土地
                bdcCfService.updateCfsx(cflx,null,null,bdcid);
            }
            if(bdcXm.getXmly().equals(Constants.XMLY_FWSP)) {   //房屋
                bdcCfService.updateCfsx(cflx,null,bdcid,null);
            }
        }

    }

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        String sjppType = AppConfig.getProperty("sjpp.type");
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx())||CommonUtil.indexOfStrs(Constants.SQLX_CFDJ_DM, bdcXm.getSqlx())&&bdcCfService.checkIsXf(bdcXm)) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                }
                if (StringUtils.equals(sjppType, Constants.PPLX_GC) && StringUtils.equals(bdcXm.getXmly(),Constants.XMLY_FWSP)) {
                    super.changeGdsjQszt(bdcXmRel, 1);
                }
            }

            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }
    }

}
