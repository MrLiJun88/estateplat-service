package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.BdcZjjzwxx;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 *
 * @version 1.0 16-10-14
 * @author: bianwen
 * @description  在建工程抵押注销
 */
public class EndProjectDyZxForZjgcServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;

    @Autowired
    private EndProjectService endProjectZxdjServiceImpl;
    public void changeYqllxzt(final BdcXm bdcXm) throws Exception {
        String zjjzwFw= "";
        if(StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_ZJJZW_ZX_FW_DM) || StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_ZJJZW_ZX_DM)){
            zjjzwFw="true";
        }
        if(StringUtils.equals(zjjzwFw, "true")){
            List<BdcXmRel> list = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            String yproid="";
            if (CollectionUtils.isNotEmpty(list)) {
                for (BdcXmRel bdcXmRel : list) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        yproid = bdcXmRel.getYproid();
                        break;
                    }
                }
            }
            Example example=new Example(BdcZjjzwxx.class);
            example.createCriteria().andEqualTo("proid", yproid).andBetween("dyzt","1","2");
            List<BdcZjjzwxx> zjjzwxxList=entityMapper.selectByExample(example);
            String dwdm = AppConfig.getProperty("dwdm");
            if(CollectionUtils.isNotEmpty(zjjzwxxList) || (StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_ZJJZW_ZX_DM)&& StringUtils.equals(dwdm,Constants.DWDM_SZ))){
                endProjectZxdjServiceImpl.changeYqllxzt(bdcXm);
            }
        }
        else{
            List<BdcXmRel> list = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            String yproid="";
            if (CollectionUtils.isNotEmpty(list)) {
                for (BdcXmRel bdcXmRel : list) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        yproid = bdcXmRel.getYproid();
                        break;
                    }
                }
            }
            Example example=new Example(BdcZjjzwxx.class);
            example.createCriteria().andEqualTo("proid", yproid).andEqualTo("dyzt","0");
            List<BdcZjjzwxx> zjjzwxxList=entityMapper.selectByExample(example);
            if(CollectionUtils.isEmpty(zjjzwxxList) ){

                List<BdcXmRel> bdcXmRelList = null;
                if (bdcXm != null) {
                    /**
                     * @author bianwen
                     * @description 过渡数据注销
                     */
                    if (!StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)) {
                        qllxService.changeQllxZt(bdcXm.getProid(), Constants.QLLX_QSZT_HR);
                    }
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                }
                /**
                 * @author bianwen
                 * @description  修改原权利状态
                 */
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    String yqllxdm = bdcZdGlService.getYqllxBySqlx(bdcXm.getSqlx());
                    bdcXm.setQllx(yqllxdm);
                    QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                        }
                        changeGdsjQszt(bdcXmRel, 1);
                    }
                }
            }
        }
    }

    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);

        /**
         * @author bianwen
         * @description 办结时将抵押物清单中的dyzt还原为注销状态
         */
        bdcZjjzwxxService.changeZjjzwxxDyzt(bdcXm,"2","1");
    }
}
