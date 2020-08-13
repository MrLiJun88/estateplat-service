package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 预告登记办结服务  与默认服务逻辑一致
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class EndProjectYgdjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private BdcXmService bdcXmService;

    @Autowired
    private QllxService qllxService;

    @Autowired
    private BdcXmRelService bdcXmRelService;

    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;

    @Autowired
    private GdYgService gdYgService;

    @Autowired
    private GdFwService gdFwService;
    @Override
    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);
        //zdd 修改权利状态
        qllxService.endQllxZt(bdcXm);
    }

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        List<GdBdcQlRel> bdcQlRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                //  预告登记原证书状态还是现势
                if (CommonUtil.indexOfStrs(Constants.SQLX_HSYGDJ_DM, bdcXm.getSqlx())|| bdcXm.getSqlx().equals("704")){
                 //如果申请类型为衡水新增的预告登记类型，则办结后权属状态置为历史
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                }else{
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                }
                //zhouwanqing 老数据做完流程要注销
                if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx())){
                    super.changeGdsjQszt(bdcXmRel, 1);
                }
            }
            //预告登记转移时奖过渡预告的产权证注销掉
            if(bdcXm.getSqlx().equals("704")){
                bdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRelList.get(0).getYqlid());
                if(CollectionUtils.isNotEmpty(bdcQlRelList) && StringUtils.isNotBlank(bdcQlRelList.get(0).getBdcid())){
                    bdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcQlRelList.get(0).getBdcid());
                    for (int i = 0;i < bdcQlRelList.size();i++){
                        List<GdYg> gdYgList = gdYgService.queryGdYgByBdcId(bdcQlRelList.get(i).getBdcid());
                        if(CollectionUtils.isNotEmpty(gdYgList)){
                            GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class,gdYgList.get(0).getYgid());
                            if (null != gdYg) {
                                gdYg.setIszx(1);
                                entityMapper.saveOrUpdate(gdYg,gdYgList.get(0).getYgid());
                                gdFwService.changeGdqlztByQlid(gdYgList.get(0).getYgid(),Constants.QLLX_QSZT_XS.toString());
                            }
                        }
                    }
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
