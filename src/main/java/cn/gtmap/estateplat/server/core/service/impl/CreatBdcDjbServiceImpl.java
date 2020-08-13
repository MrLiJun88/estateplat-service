package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import cn.gtmap.estateplat.model.server.core.DjbQlPro;
import cn.gtmap.estateplat.server.core.mapper.BdcXmMapper;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcZdQllxService;
import cn.gtmap.estateplat.server.core.service.CreatBdcDjbService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * lst 根据不动产单元 获取权利信息数据
 */
@Repository
public class CreatBdcDjbServiceImpl implements CreatBdcDjbService {
    @Autowired
    private BdcZdQllxService bdcZdQllxService;
    @Autowired
    private BdcXmMapper bdcXmMapper;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public List<DjbQlPro> getQlByBdcdy(final String bdcdyh) {
        List<DjbQlPro> djbQlProList;
        List<DjbQlPro> list = new ArrayList<DjbQlPro>();
        if (StringUtils.isNotBlank(bdcdyh)) {
            HashMap map = new HashMap();
            map.put("bdcdyh", bdcdyh);
            djbQlProList = bdcXmMapper.getBdcdyByDyh(map);
            if (CollectionUtils.isNotEmpty(djbQlProList)) {
                List<String> qllxList = new ArrayList<String>();
                for (int j = 0; j < djbQlProList.size(); j++) {
                    if (!qllxList.contains(djbQlProList.get(j).getQllx()))
                        qllxList.add(djbQlProList.get(j).getQllx());

                }
                if (CollectionUtils.isNotEmpty(qllxList)) {
                    for (String qllx : qllxList) {
                        BdcZdQllx bdcZdQllx = bdcZdQllxService.queryBdcZdQllxByDm(qllx);
                        DjbQlPro djbQlPro = new DjbQlPro();
                        djbQlPro.setBdcdyh(bdcdyh);
                        djbQlPro.setQllx(qllx);
                        djbQlPro.setMc(bdcZdQllx.getMc());
                        String fwlx = null;
                        if (StringUtils.equals(StringUtils.substring(bdcdyh, 19, 20), Constants.DZWTZM_F))
                            fwlx = bdcDjsjService.getBdcdyfwlxByBdcdyh(bdcdyh);
                        //多幢要加上权利类型的判断，否则查封等权利也会显示多幢的信息
                        if (StringUtils.isNotBlank(fwlx) && StringUtils.equals(Constants.DJSJ_FWDZ_DM, fwlx) && StringUtils.equals(bdcZdQllx.getTableName(), "BDC_FDCQ")) {
                            djbQlPro.setTableName("BDC_FDCQ_DZ");
                        } else {
                            djbQlPro.setTableName(bdcZdQllx.getTableName());
                        }

                        list.add(djbQlPro);
                    }
                }
            }
        }


        return list;
    }

    @Override
    public HashMap getQlPageByBdcdyh(final String bdcdyh) {
        HashMap retQlMap = new HashMap();
        if (StringUtils.isNotBlank(bdcdyh)) {
            List<Map> qlMapList = bdcXmMapper.getQlByBdcdyh(bdcdyh);

            if (CollectionUtils.isNotEmpty(qlMapList)) {
                //抵押权
                int dyaCount = 0;
                //地役权
                int dyCount = 0;
                //预告
                int ygCount = 0;
                //异议
                int yyCount = 0;
                //查封
                int cfCount = 0;
                //其他权利
                int qtCount = 0;
                //总页数
                int zys = 0;
                //权利顺序
                List<String> qlsxList = new ArrayList<String>();
                for (Map qlMap : qlMapList) {
                    if (StringUtils.equals(CommonUtil.formatEmptyValue(qlMap.get("QLLX")), Constants.QLLX_DYAQ)) {
                        if (!qlsxList.contains(Constants.QLLX_DYAQ))
                            qlsxList.add(Constants.QLLX_DYAQ);
                        dyaCount++;
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(qlMap.get("QLLX")), Constants.QLLX_DYQ)) {
                        if (!qlsxList.contains(Constants.QLLX_DYQ))
                            qlsxList.add(Constants.QLLX_DYQ);
                        dyCount++;
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(qlMap.get("QLLX")), Constants.QLLX_YGDJ)) {
                        if (!qlsxList.contains(Constants.QLLX_YGDJ))
                            qlsxList.add(Constants.QLLX_YGDJ);
                        ygCount++;
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(qlMap.get("QLLX")), Constants.QLLX_YYDJ)) {
                        if (!qlsxList.contains(Constants.QLLX_YYDJ))
                            qlsxList.add(Constants.QLLX_YYDJ);
                        yyCount++;
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(qlMap.get("QLLX")), Constants.QLLX_CFDJ)) {
                        if (!qlsxList.contains(Constants.QLLX_CFDJ))
                            qlsxList.add(Constants.QLLX_CFDJ);
                        cfCount++;
                    } else if (StringUtils.equals(CommonUtil.formatEmptyValue(qlMap.get("QLLX")), Constants.QLLX_GYTD_FWSUQ)) {
                        if (!qlsxList.contains(Constants.QLLX_GYTD_FWSUQ))
                            qlsxList.add(Constants.QLLX_GYTD_FWSUQ);
                        qtCount++;
                    } else {
                        if (!qlsxList.contains(Constants.QLLX_QT))
                            qlsxList.add(Constants.QLLX_QT);
                        qtCount++;
                    }
                }
                if (CollectionUtils.isNotEmpty(qlsxList)) {
                    int i = 1;
                    int lastPage = 1;
                    for (String ql : qlsxList) {
                        if (StringUtils.equals(ql, Constants.QLLX_DYAQ)) {
                            //登记薄一页只能显示3列
                            int page = 0;
                            if (dyaCount > 3 && dyaCount % 3 > 0)
                                page = dyaCount / 3 + 1;
                            else if (dyaCount < 3)
                                page = dyaCount / 3 + 1;
                            else
                                page = dyaCount / 3;

                            //页数显示以1、2、3形式显示
                            StringBuilder pageStr = new StringBuilder();
                            for (int j = 1; j < page + 1; j++) {
                                if (StringUtils.isBlank(pageStr)) {
                                    pageStr.append(j + lastPage).append("");
                                }else {
                                    pageStr.append(",").append(j + lastPage).append("");
                                }
                            }
                            lastPage = page + lastPage;
                            retQlMap.put("dya", pageStr);
                        } else if (StringUtils.equals(ql, Constants.QLLX_DYQ)) {
                            //登记薄一页只能显示3列
                            int page = 0;
                            if (dyCount > 3 && dyCount % 3 > 0)
                                page = dyCount / 3 + 1;
                            else if (dyCount < 3)
                                page = dyCount / 3 + 1;
                            else
                                page = dyCount / 3;

                            //页数显示以1、2、3形式显示
                            StringBuilder pageStr = new StringBuilder();
                            for (int j = 1; j < page + 1; j++) {
                                if (StringUtils.isBlank(pageStr)) {
                                    pageStr.append(j + lastPage).append("");
                                }else {
                                    pageStr.append(",").append(j + lastPage).append("");
                                }
                            }
                            lastPage = page + lastPage;
                            retQlMap.put("dy", dyCount + "");
                        } else if (StringUtils.equals(ql, Constants.QLLX_YGDJ)) {
                            //登记薄一页只能显示3列
                            int page = 0;
                            if (ygCount > 3 && ygCount % 3 > 0)
                                page = ygCount / 3 + 1;
                            else if (ygCount < 3)
                                page = ygCount / 3 + 1;
                            else
                                page = ygCount / 3;

                            //页数显示以1、2、3形式显示
                            StringBuilder pageStr = new StringBuilder();
                            for (int j = 1; j < page + 1; j++) {
                                if (StringUtils.isBlank(pageStr)) {
                                    pageStr.append(j + lastPage).append("");
                                }else {
                                    pageStr.append(",").append(j + lastPage).append("");
                                }
                            }
                            lastPage = page + lastPage;
                            retQlMap.put("yg", pageStr);
                        } else if (StringUtils.equals(ql, Constants.QLLX_YYDJ)) {
                            //登记薄一页只能显示3列
                            int page = 0;
                            if (yyCount > 3 && yyCount % 3 > 0)
                                page = yyCount / 3 + 1;
                            else if (yyCount < 3)
                                page = yyCount / 3 + 1;
                            else
                                page = yyCount / 3;

                            //页数显示以1、2、3形式显示
                            StringBuilder pageStr = new StringBuilder();
                            for (int j = 1; j < page + 1; j++) {
                                if (StringUtils.isBlank(pageStr)) {
                                    pageStr.append(j + lastPage).append("");
                                }else {
                                    pageStr.append(",").append(j + lastPage).append("");
                                }
                            }
                            lastPage = page + lastPage;
                            retQlMap.put("yy", pageStr);
                        } else if (StringUtils.equals(ql, Constants.QLLX_CFDJ)) {
                            //登记薄一页只能显示3列
                            int page = 0;
                            if (cfCount > 3 && cfCount % 3 > 0)
                                page = cfCount / 3 + 1;
                            else if (cfCount < 3)
                                page = cfCount / 3 + 1;
                            else
                                page = cfCount / 3;

                            //页数显示以1、2、3形式显示
                            StringBuilder pageStr = new StringBuilder();
                            for (int j = 1; j < page + 1; j++) {
                                if (StringUtils.isBlank(pageStr)) {
                                    pageStr.append(j + lastPage).append("");
                                }else {
                                    pageStr.append(",").append(j + lastPage).append("");
                                }
                            }
                            lastPage = page + lastPage;
                            retQlMap.put("cf", pageStr);
                        } else if (StringUtils.equals(ql, Constants.QLLX_GYTD_FWSUQ)) {
                            //登记薄一页只能显示3列
                            int page = 0;
                            if (qtCount > 3 && qtCount % 3 > 0)
                                page = qtCount / 3 + 1;
                            else if (qtCount < 3)
                                page = qtCount / 3 + 1;
                            else
                                page = qtCount / 3;
                            //页数显示以1、2、3形式显示
                            StringBuilder pageStr = new StringBuilder();
                            for (int j = 1; j < page + 1; j++) {
                                if (StringUtils.isBlank(pageStr)) {
                                    pageStr.append(j + lastPage).append("");
                                }else {
                                    pageStr.append(",").append(j + lastPage).append("");
                                }
                            }
                            lastPage = page + lastPage + 1;
                            retQlMap.put("qt", pageStr + "," + String.valueOf(page + i + 1));
                        } else {
                            //登记薄一页只能显示3列
                            int page = 0;
                            if (qtCount > 3 && qtCount % 3 > 0)
                                page = qtCount / 3 + 1;
                            else if (qtCount < 3)
                                page = qtCount / 3 + 1;
                            else
                                page = qtCount / 3;

                            //页数显示以1、2、3形式显示
                            StringBuilder pageStr = new StringBuilder();
                            for (int j = 1; j < page + 1; j++) {
                                if (StringUtils.isBlank(pageStr)) {
                                    pageStr.append(j + lastPage).append("");
                                }else {
                                    pageStr.append(",").append(j + lastPage).append("");
                                }
                            }
                            lastPage = page + lastPage;
                            retQlMap.put("qt", pageStr);
                        }
                        zys = lastPage;
                        i++;
                    }
                }
                retQlMap.put("zys", zys + "");
            }
        }

        return retQlMap;
    }

    @Override
    public List<DjbQlPro> getQlByzdzhh(final String zdzhh) {
        List<DjbQlPro> djbQlProList;
        List<DjbQlPro> list = new ArrayList<DjbQlPro>();
        if (StringUtils.isNotBlank(zdzhh)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("zdzhh", zdzhh);
            map.put("qszt", "1");
            djbQlProList = bdcXmMapper.getBdcdyByDyh(map);
            list = initBdcdjDjbQlPros(djbQlProList);
            //zdd 如果从新系统数据中找不到对应的权利信息  则从过度数据中读取
            List<DjbQlPro> gdqlList = gdFwService.getGdqlByBdcdyh(map);
            list = initGdSjDjbQlPros(gdqlList, djbQlProList, list);
        }
        return list;
    }

    private List<DjbQlPro> initBdcdjDjbQlPros(List<DjbQlPro> djbQlProList) {
        List<DjbQlPro> list = new ArrayList<DjbQlPro>();
        if (CollectionUtils.isNotEmpty(djbQlProList)) {
            List<BdcZdQllx> bdcZdQllxList = bdcZdQllxService.getAllBdcZdQllx();
            for (int j = 0; j < djbQlProList.size(); j++) {
                for (BdcZdQllx bdcZdQllx : bdcZdQllxList) {
                    if (bdcZdQllx.getDm().equals(djbQlProList.get(j).getQllx())) {
                        djbQlProList.get(j).setMc(bdcZdQllx.getMc());
                        djbQlProList.get(j).setTableName(bdcZdQllx.getTableName());
                        list.add(djbQlProList.get(j));
                        break;
                    }
                }
            }
        }
        return list;
    }

    private List<DjbQlPro> initGdSjDjbQlPros(List<DjbQlPro> gdqlList, List<DjbQlPro> djbQlProList, List<DjbQlPro> list) {
        if (CollectionUtils.isNotEmpty(gdqlList)) {
            if (CollectionUtils.isNotEmpty(djbQlProList)) {
                for (DjbQlPro gddjbQlPro : gdqlList) {
                    Boolean existBol = true;
                    for (DjbQlPro djbQlPro : djbQlProList) {
                        if (gddjbQlPro.getBdcdyh().equals(djbQlPro.getBdcdyh()) && gddjbQlPro.getQllx().equals(djbQlPro.getQllx())) {
                            existBol = false;
                            break;
                        }
                    }
                    if (existBol)
                        list.add(gddjbQlPro);
                }
            } else {
                list.addAll(gdqlList);
            }
        }
        return list;
    }

    @Override
    public String getStartPageBy(final String bdcdyh,final  String qllx) {
        String page = "";
        if (StringUtils.isNotBlank(bdcdyh)) {
            HashMap qlPage = getQlPageByBdcdyh(bdcdyh);
            if (qlPage != null && qlPage.size() > 0) {
                if (StringUtils.equals(qllx, Constants.QLLX_CFDJ)) {
                    page = CommonUtil.formatEmptyValue(qlPage.get("cf"));
                } else if (StringUtils.equals(qllx, Constants.QLLX_DYAQ)) {
                    page = CommonUtil.formatEmptyValue(qlPage.get("dya"));
                } else if (StringUtils.equals(qllx, Constants.QLLX_YGDJ)) {
                    page = CommonUtil.formatEmptyValue(qlPage.get("yg"));
                } else if (StringUtils.equals(qllx, Constants.QLLX_YYDJ)) {
                    page = CommonUtil.formatEmptyValue(qlPage.get("yy"));
                } else if (StringUtils.equals(qllx, Constants.QLLX_DYQ)) {
                    page = CommonUtil.formatEmptyValue(qlPage.get("dy"));
                } else {
                    page = CommonUtil.formatEmptyValue(qlPage.get("qt"));
                }
            }
        }
        if (StringUtils.isNotBlank(page) && page.indexOf(',') != -1) {
            page = page.split(",")[0];
        }
        return page;
    }
}
