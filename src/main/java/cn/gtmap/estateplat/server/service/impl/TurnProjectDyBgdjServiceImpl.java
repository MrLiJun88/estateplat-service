package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 变更登记转发服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class TurnProjectDyBgdjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;
        //zx初始化权利信息时删除项目id
        qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, bdcXm.getProid());
        //zdd 转移登记 需要继承原权利信息
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        //参数修改为wiid @gtsy
        List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdy(bdcXm.getWiid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList) && bdcBdcdyList != null && bdcBdcdyList.size() == bdcXmRelList.size()) {
            int i = 0;
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                qllxVo = qllxService.makeSureQllx(bdcXm);
                if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
					if (ybdcxm != null) {
						QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
						if (yqllxVo == null) {
                            yqllxVo = qllxService.makeSureQllx(bdcXm);
                        }
						yqllxVo.setQlid(UUIDGenerator.generate18());
						yqllxVo.setProid(bdcXm.getProid());
						yqllxVo.setYwh(bdcXm.getBh());
						yqllxVo.setDbr(null);
						yqllxVo.setDjsj(null);
						//zdd 不应该继承原来项目的附记
						yqllxVo.setFj("");
						//zdd 前后的权利类型相同
                        yqllxVo.setQszt(0);
						qllxVo = yqllxVo;
					}
                }

                bdcXm.setBdcdyid(bdcBdcdyList.get(i).getBdcdyid());
                qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
                entityMapper.insertSelective(qllxVo);
                i++;
            }
        }
        //根据继承过来的抵押权信息里面的抵押方式，确定bdcxm里面的登记子项的取值
        if(qllxVo instanceof BdcDyaq){
            if(StringUtils.isNotEmpty(((BdcDyaq) qllxVo).getDyfs())&&StringUtils.equals(((BdcDyaq) qllxVo).getDyfs(),"1")){
                bdcXm.setDjzx(Constants.DJZX_YBDYQ);
            }
            if(StringUtils.isNotEmpty(((BdcDyaq) qllxVo).getDyfs())&&StringUtils.equals(((BdcDyaq) qllxVo).getDyfs(),"2")){
                bdcXm.setDjzx(Constants.DJZX_ZGEDY);
            }
            bdcXmService.saveBdcXm(bdcXm);
        }

        return qllxVo;
    }

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcZs> list = null;
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
            list  = createBdcZs(bdcXm,previewZs);
        }else{
            list = bdcZsService.creatBdcqz(bdcXm, bdcQlrList,boolPreviewZs);

            //zdd 生成权利人证书关系表
            bdcZsQlrRelService.creatBdcZsQlrRel(bdcXm,list,bdcQlrList);
            //zdd 生成项目证书关系表
            bdcXmZsRelService.creatBdcXmZsRel(list, bdcXm.getProid());
        }

        return list;
    }
}
