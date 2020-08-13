package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 批量抵押变更登记   附件复制到每一个项目
 *
 * @author lst
 * @version V1.0, 15-12-24
 */
public class EndComplexDybgdjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcYgTurnZyService bdcYgTurnZyService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void changeXmzt(BdcXm bdcXm) {
        try {
            if (bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXmRel> bdcXmRelList = null;
                bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                bdcXmService.endBdcXm(bdcXm);
                //zdd 修改权利状态
                qllxService.endQllxZt(bdcXm);
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx())) {
                            super.changeGdsjQszt(bdcXmRel, 1);
                        }
                    }
                }
                //copy附件
                if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    List<Integer> list = null;
                    String fileProid = "";
                    for (BdcXm bdcXmTemp  : bdcXmList){
                        fileProid = bdcXmTemp.getProid();
                        list = PlatformUtil.getFileIds(fileProid);
                        if (CollectionUtils.isNotEmpty(list))
                            break;
                    }
                    if (!bdcXm.getProid().equals(fileProid) &&CollectionUtils.isNotEmpty(list)){
                        Integer nodeId = PlatformUtil.creatNode(bdcXm.getProid());
                        PlatformUtil.copyFileImplToNode(list, nodeId);
                    }
                }
            }
        }catch (Exception e){
            logger.error("EndComplexDybgdjServiceImpl.changeXmzt",e);
        }
    }

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        if(bdcXm!=null && StringUtils.isNotBlank(bdcXm.getProid())){
           List<BdcDyaq> list= bdcDyaqService.queryYdyaqByProid(bdcXm.getProid(), Constants.QLLX_QSZT_XS);
            if(CollectionUtils.isNotEmpty(list)){
                UserInfo user= SessionUtil.getCurrentUser();
                String username="";
                if(user!=null){
                    username=user.getUsername();
                }
                for(int i=0;i<list.size();i++){
                    BdcDyaq bdcDyaq=list.get(i);
                    bdcDyaq.setQszt(Constants.QLLX_QSZT_HR);
                    bdcDyaq.setZxdbr(username);
                    bdcDyaq.setZxsj(new Date());
                    bdcDyaq.setZxdyywh(bdcXm.getBh());
                    entityMapper.updateByPrimaryKeySelective(bdcDyaq);
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
