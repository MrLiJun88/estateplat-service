package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 继承默认服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-26
 */
public class TurnProjectDydjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcZs> list = null;
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
            list = createBdcZs(bdcXm,previewZs);
        }else{
            list = bdcZsService.creatDyBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
            //zdd 生成权利人证书关系表
            bdcZsQlrRelService.creatBdcZsQlrRel(bdcXm,list,bdcQlrList);
            //zdd 生成项目证书关系表
            bdcXmZsRelService.creatBdcXmZsRel(list, bdcXm.getProid());
        }
        return list;
    }

}
