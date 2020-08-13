package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.1.0, 2016/04/14.
 * @description 带抵押的转移流程
 */
public class TurnComplexZyDyProjectServiceImpl extends TurnProjectZydjServiceImpl {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcDyaqService bdcDyaqService;


    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcXm> bdcXmList = null;
        List<BdcZs> list = new ArrayList<BdcZs>();
        List<BdcXm> bdcZsXmList = null;
        List<BdcXm> bdcZmXmList = null;
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                StringBuilder ybdcqzh = new StringBuilder();
                //xuchao 将bdcxm区分为两个部分，第一个部分是生成证书的项目，第二个部分是生成证明的项目
                bdcZsXmList = getZsZmXmList(bdcXmList, "");
                bdcZmXmList = getZsZmXmList(bdcXmList, Constants.QLLX_DYAQ);
                HashMap bdcdyZsMap = new HashMap();
                //xuchao  生成证书的项目只需要循环list
                List<BdcZs> listTemp = null;
                for (BdcXm bdcxmTemp : bdcZsXmList) {
                    //获取当前项目的权利人
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                    //创建证书
                    if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)) {
                        listTemp = createBdcZs(bdcxmTemp,previewZs);
                        if (CollectionUtils.isNotEmpty(listTemp))
                            list.addAll(listTemp);
                    } else{
                        //xuchao 创建证书
                        listTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList, this.userid,boolPreviewZs);
                        if (CollectionUtils.isNotEmpty(listTemp)) {
                            list.addAll(listTemp);
                        }

                        //zdd 生成权利人证书关系表
                        bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, listTemp, bdcQlrList);
                        //zdd 生成项目证书关系表
                        bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcxmTemp.getProid());
                    }


                    for (BdcZs bdcZs : listTemp) {
                        if(StringUtils.isNotBlank(bdcZs.getBdcqzh())){
                            if (StringUtils.isNotBlank(ybdcqzh)) {
                                ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                            }else {
                                ybdcqzh.append(bdcZs.getBdcqzh());
                            }
                        }
                    }
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcxmTemp.getProid());
                    if (StringUtils.isNoneBlank(bdcSpxx.getBdcdyh())) {
                        bdcdyZsMap.put(bdcSpxx.getBdcdyh(), ybdcqzh);
                    }
                }
                //xuchao 生成证明
                /*xuchao 因为生成证明需要区分为两个部分，第一个部分是公积金，第二个部分是银行
                    目前只是根据权利人名称来区分，后期如果有问题，可根据gd权利寻找项目
                */
                //xuchao key:权利人，val:List<BdcXm>
                HashMap<String, List<BdcXm>> qlrZsXmMap = new HashMap<String, List<BdcXm>>();
                for (BdcXm tempBdcXm : bdcZmXmList) {
                    //抵押证明的原证号是新产生的不动产权证
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(tempBdcXm.getProid());
                    String ybdcqzhTemp = "";
                    if (StringUtils.isNoneBlank(bdcSpxx.getBdcdyh())) {
                        ybdcqzhTemp = CommonUtil.formatEmptyValue(bdcdyZsMap.get(bdcSpxx.getBdcdyh()));
                    }
                    tempBdcXm.setYbdcqzh(ybdcqzhTemp);
                    entityMapper.saveOrUpdate(tempBdcXm, tempBdcXm.getProid());

                    BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(tempBdcXm.getProid());
                    if(bdcDyaq != null) {
                        String qlqtzk = bdcZsService.getQlqtzkByReplaceBdcqzh(bdcDyaq.getQlqtzk(), ybdcqzhTemp);
                        if(StringUtils.isNotBlank(qlqtzk)) {
                            bdcDyaq.setQlqtzk(qlqtzk);
                            bdcDyaqService.saveBdcDyaq(bdcDyaq);
                        }
                    }

                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(tempBdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                        if (qlrZsXmMap.containsKey(bdcQlrList.get(0).getQlrmc())) {
                            qlrZsXmMap.get(bdcQlrList.get(0).getQlrmc()).add(tempBdcXm);
                        } else {
                            List<BdcXm> tempBdcXmList = new ArrayList<BdcXm>();
                            tempBdcXmList.add(tempBdcXm);
                            qlrZsXmMap.put(bdcQlrList.get(0).getQlrmc(), tempBdcXmList);
                        }
                    }
                }
                //循环qlrZsXmMap.values,生成多个证明
                Set entrySet = qlrZsXmMap.entrySet();
                for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                    Map.Entry entry = (Map.Entry) it.next();
                    List<BdcXm> bdcXmZmList = (List<BdcXm>) entry.getValue();
                    //在循环外 只生成一本证书 循环内生成多个项目证书关系  权利人证书关系
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmZmList.get(0).getProid());
                    list = bdcZsService.creatDyBdcqz(bdcXmZmList.get(0), bdcQlrList,boolPreviewZs);
                    for (BdcXm bdcxmTemp : bdcXmZmList) {
                        //zdd 生成权利人证书关系表
                        bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, list, bdcQlrList);
                        //zdd 生成项目证书关系表
                        bdcXmZsRelService.creatBdcXmZsRel(list, bdcxmTemp.getProid());
                    }
                }

            }
        }
        return list;
    }

    private List<BdcXm> getZsZmXmList(List<BdcXm> bdcXmList, String type) {
        List<BdcXm> bdcZsXmList = new ArrayList<BdcXm>();
        List<BdcXm> bdcZmXmList = new ArrayList<BdcXm>();
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                if (bdcXm != null && !StringUtils.equals(Constants.QLLX_DYAQ, bdcXm.getQllx())) {
                    bdcZsXmList.add(bdcXm);
                } else {
                    bdcZmXmList.add(bdcXm);
                }
            }
        }
        if (StringUtils.equals(type, Constants.QLLX_DYAQ)) {
            return bdcZmXmList;
        }
        return bdcZsXmList;
    }
}
