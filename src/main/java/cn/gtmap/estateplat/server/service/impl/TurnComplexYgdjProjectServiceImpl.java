package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version 1.1.0, 2016/3/11.
 * @description 预告、预告抵押登记合并办理登记  权利人不同   不动产单元相同
 */
public class TurnComplexYgdjProjectServiceImpl extends TurnProjectYgdjServiceImpl {

    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcXm> bdcXmList;
        List<BdcZs> list = new ArrayList<BdcZs>();
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcZs> listTemp = null;
                StringBuilder ybdcqzh = new StringBuilder();
                for (BdcXm bdcxmTemp : bdcXmList) {
                    /*
                     *zdd 业务中 权利人不同，所以将此方法放在项目表循环 内
                     */
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                    //创建证书
                    //共有方式按份共有中存在共同共有
                    if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)) {
                        listTemp  = createBdcZs(bdcxmTemp,previewZs);
                        if(CollectionUtils.isNotEmpty(listTemp))
                            list.addAll(listTemp);
                    }else{
                        listTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList,boolPreviewZs);
                        if (CollectionUtils.isNotEmpty(listTemp)) {
                            list.addAll(listTemp);
                        }
                        //zdd 生成权利人证书关系表
                        bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp,listTemp,bdcQlrList);
                        //zdd 生成项目证书关系表
                        bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcxmTemp.getProid());
                    }

                    if(StringUtils.equals(bdcxmTemp.getSqlx(), Constants.DJLX_YGDJ_DM)){
                        for(BdcZs bdcZs:listTemp){
                            if (StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                                if (StringUtils.isNotBlank(ybdcqzh)) {
                                    ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                } else {
                                    ybdcqzh.append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                    }else{
                        bdcxmTemp.setYbdcqzh(ybdcqzh.toString());
                        entityMapper.saveOrUpdate(bdcxmTemp,bdcxmTemp.getProid());
                    }
                }
            }
        }
        return list;
    }
}
