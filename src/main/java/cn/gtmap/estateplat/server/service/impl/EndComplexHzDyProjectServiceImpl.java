package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @version 1.0, 2016/6/1
 * @author<a href="mailto:zhoudefu@gtmap.cn>zhoudefu</a>
 * @discription 换证抵押流程的办结服务
 */

public class EndComplexHzDyProjectServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private QllxService qllxService;

    /**
     * 修改办结后项目状态
     * @param bdcXm
     */
    public void changeXmzt(BdcXm bdcXm){
        /**
         * 修改过度数据权属状态
         */
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            super.changeXmzt(bdcXm);
            setYbdcqzForDY(bdcXm);

            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList){
                super.changeGdsjQszt(bdcXmRel,1);
            }
        }
    }

    /**
     * 换证抵押办结后，将新产生的不动产权证号作为抵押流程的原不动产权证号
     * @param bdcXm
     */
    public void setYbdcqzForDY(BdcXm bdcXm){
        List<BdcXm> bdcXmList;
        List<BdcZs> bdcZsList;
        String ybdcqzh = "";
        HashMap map = new HashMap();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
            /**
             * 根据wiid获取项目列表
             */
            map.put("wiid",bdcXm.getWiid());
            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcZsList)){
                for (BdcZs bdcZs : bdcZsList){
                    if (StringUtils.isNotBlank(bdcZs.getZstype()) && !(StringUtils.equals(bdcZs.getZstype(),Constants.BDCQZM_BH_FONT))){
                        ybdcqzh = bdcZs.getBdcqzh();
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(bdcXmList)){
                for (BdcXm bdcXm1 : bdcXmList){
                    if (StringUtils.isNotBlank(bdcXm1.getQllx()) && StringUtils.equals(bdcXm1.getQllx(),Constants.QLLX_DYAQ)
                            &&StringUtils.isNotBlank(ybdcqzh)){
                        bdcXm1.setYbdcqzh(ybdcqzh);
                        entityMapper.saveOrUpdate(bdcXm1,bdcXm1.getProid());
                    }
                }
            }
        }
    }
}
