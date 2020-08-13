package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/1/15
 * @description 变更和转移合并、转移和其他转移合并流程
 */
public class TurnComplexBgZyProjectServiceImpl extends TurnProjectZydjServiceImpl {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    EntityMapper entityMapper;

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm, final String previewZs) {
        List<BdcXm> bdcXmList = null;
        List<BdcZs> list = new ArrayList<BdcZs>();
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            // 变更和转移合并登记、转移和其他转移合并登记需进行排序
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo("wiid", bdcXm.getWiid());
            example.setOrderByClause("cjsj");
            bdcXmList = entityMapper.selectByExample(BdcXm.class, example);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                StringBuilder ybdcqzh = new StringBuilder();
                List<BdcZs> listTemp = null;
                for (int i=0 ; i< bdcXmList.size() ; i++) {
                    //获取当前项目的权利人
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmList.get(i).getProid());
                    //创建证书
                    if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)) {
                        listTemp = createBdcZs(bdcXmList.get(i),previewZs);
                        if (CollectionUtils.isNotEmpty(listTemp))
                            list.addAll(listTemp);
                    } else{
                        //xuchao 创建证书
                        listTemp = bdcZsService.creatBdcqz(bdcXmList.get(i), bdcQlrList, this.userid,boolPreviewZs);
                        if (CollectionUtils.isNotEmpty(listTemp)) {
                            list.addAll(listTemp);
                        }

                        //zdd 生成权利人证书关系表
                        bdcZsQlrRelService.creatBdcZsQlrRel(bdcXmList.get(i), listTemp, bdcQlrList);
                        //zdd 生成项目证书关系表
                        bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcXmList.get(i).getProid());
                    }
                    // 变更新生成的证号为转移的原不动产权证号
                    if (i == 0) {
                        for (BdcZs bdcZs : listTemp) {
                            if (StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                                if (StringUtils.isNotBlank(ybdcqzh)) {
                                    ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                } else {
                                    ybdcqzh.append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                    }
                    if (i == 1 && StringUtils.isNotBlank(ybdcqzh.toString())) {
                        bdcXmList.get(i).setYbdcqzh(ybdcqzh.toString());
                        entityMapper.saveOrUpdate(bdcXmList.get(i), bdcXmList.get(i).getProid());
                    }
                }
            }
        }
        return list;
    }
}
