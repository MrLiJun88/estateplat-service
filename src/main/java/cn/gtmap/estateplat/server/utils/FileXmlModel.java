package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.core.service.impl.BdcZdGlServiceImpl;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * 项目关系xml
 *
 * @author <a href="mailto:zx@gtmap.cn">zx</a>
 * @version V1.0, 15-4-25
 */

public class FileXmlModel {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 构造函数
     */
    private FileXmlModel(){

    }

    public static Document getXmRel(List<BdcXmRel> bdcXmRelList, List<HashMap> bdcZsList, List<BdcXm> bdcXmList, List<QllxVo> qllxVoList, List<BdcZdQlzt> bdcZdQlztList, String bdcdyh, List<HashMap> hashMapList) {
        BdcZdGlService bdcZdGlService = new BdcZdGlServiceImpl();
        String qlr = "";
        //新建文档
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        //创建根元素
        Element xmRelElement = document.addElement("WorkFlow");
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            //展现证书关系id必须要整型
            List<String> proidList = new ArrayList<String>();
            HashMap proidMap = new HashMap();
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (!proidList.contains(bdcXmRel.getProid()))
                    proidList.add(bdcXmRel.getProid());
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && !proidList.contains(bdcXmRel.getYproid()))
                    proidList.add(bdcXmRel.getYproid());
            }
            if (CollectionUtils.isNotEmpty(proidList)) {
                int j = 1;
                for (String proid : proidList) {
                    proidMap.put(proid, j);
                    j++;
                }
            }
            if (proidMap != null) {
                StringBuilder proidsx = new StringBuilder();
                HashMap groupProids = new HashMap();
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (bdcXmRel != null) {
                        //添加关系节点
                        List<String> toProidList = getProidListByProid(bdcXmRelList, bdcXmRel.getProid());
                        if (CollectionUtils.isNotEmpty(toProidList)) {
                            for (String toProid : toProidList) {
                                Element routeElement = xmRelElement.addElement("Route");
                                routeElement.addAttribute("ID", UUIDGenerator.generate());
                                routeElement.addAttribute("Name", UUIDGenerator.generate());
                                routeElement.addAttribute("FromElementID", CommonUtil.formatEmptyValue(proidMap.get(bdcXmRel.getProid())));
                                routeElement.addAttribute("ToElementID", CommonUtil.formatEmptyValue(proidMap.get(toProid)));
                            }
                        }
                        if (StringUtils.isNotBlank(bdcXmRel.getProid()) && StringUtils.isBlank(proidsx))
                            proidsx.append(bdcXmRel.getProid());
                        else if (StringUtils.isNotBlank(bdcXmRel.getProid()))
                            proidsx.append(",").append(bdcXmRel.getProid());
                        if (StringUtils.isNotBlank(bdcXmRel.getProid())) {
                            StringBuilder groupOneProid = new StringBuilder();
                            for (BdcXmRel bdcXmRel1 : bdcXmRelList) {
                                if (bdcXmRel1 != null && StringUtils.equals(bdcXmRel1.getYproid(), bdcXmRel.getProid())) {
                                    if (StringUtils.isBlank(groupOneProid)) {
                                        groupOneProid.append(bdcXmRel1.getProid());
                                    }else if (StringUtils.isNotBlank(groupOneProid)) {
                                        groupOneProid.append(",").append(bdcXmRel1.getProid());
                                    }
                                }
                            }
                            if (StringUtils.isNotBlank(groupOneProid)) {
                                groupProids.put(bdcXmRel.getProid(), groupOneProid);
                            }
                        }
                    }
                }
                Element workNodeGroupElement = null;
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (bdcXmRel != null) {
                        //增加项目
                        HashMap zsMap = getZsxxMap(bdcZsList, bdcXmRel.getProid());
                        //zhouwanqing 添加证书权利人
                        for (HashMap hashMap : hashMapList) {
                            if (StringUtils.equals(CommonUtil.formatEmptyValue(hashMap.get("ID")), bdcXmRel.getProid())) {
                                qlr = CommonUtil.formatEmptyValue(hashMap.get("QLR"));
                            }
                        }

                        if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                            String groupOneProid = CommonUtil.formatEmptyValue(groupProids.get(bdcXmRel.getYproid()));
                            if (StringUtils.isNotBlank(groupOneProid) && StringUtils.indexOf(groupOneProid, bdcXmRel.getProid()) > -1 && StringUtils.split(groupOneProid, ",").length > 1) {
                                //  增加项目信息

                                BdcXm bdcXm = getBdcXm(bdcXmList, bdcXmRel.getProid());
                                if (StringUtils.indexOf(groupOneProid, bdcXmRel.getProid()) == 0) {
                                    workNodeGroupElement = xmRelElement.addElement("WorkNodeGroup");
                                    Element nodeElement = workNodeGroupElement.addElement(ParamsConstants.WORKNODE_HUMP);
                                    nodeElement.addAttribute("ID", CommonUtil.formatEmptyValue(proidMap.get(bdcXmRel.getProid())));
                                    QllxVo qllxVo = getQllxVo(qllxVoList, bdcXmRel.getProid());
                                    if (qllxVo == null && StringUtils.isNotBlank(bdcXmRel.getYproid()))
                                        qllxVo = getQllxVo(qllxVoList, bdcXmRel.getYproid());
                                    String zsxx = getZsxx(zsMap, bdcXm, qllxVo, bdcdyh, qlr);
                                    if (StringUtils.isNotBlank(zsxx)) {
                                        String zsxxTitle = zsxx.substring(0, zsxx.indexOf('$'));
                                        //zwq 证书关系显示登记类型,下面注释亦同
                                        nodeElement.addAttribute(ParamsConstants.TITLE_HUMP, zsxxTitle);
                                        String name = zsxx.substring(zsxx.indexOf('$') + 1, zsxx.length());
                                        nodeElement.addAttribute("Name", name);
                                    }
                                    String qlzt = getQlztByQllxVo(qllxVo, bdcXm);
                                    String qlColor = bdcZdGlService.getQlColorByQlzt(qlzt, bdcZdQlztList);
                                    nodeElement.addAttribute(ParamsConstants.COLOR_HUMP, qlColor);
                                    String qlMC = bdcZdGlService.getQlMCByQlzt(qlzt, bdcZdQlztList);
                                    nodeElement.addAttribute(ParamsConstants.LEGENDNAME_HUMP, qlMC);

                                } else if (workNodeGroupElement != null) {
                                    Element nodeElement = workNodeGroupElement.addElement(ParamsConstants.WORKNODE_HUMP);
                                    nodeElement.addAttribute("ID", CommonUtil.formatEmptyValue(proidMap.get(bdcXmRel.getProid())));
                                    QllxVo qllxVo = getQllxVo(qllxVoList, bdcXmRel.getProid());
                                    if (qllxVo == null && StringUtils.isNotBlank(bdcXmRel.getYproid()))
                                        qllxVo = getQllxVo(qllxVoList, bdcXmRel.getYproid());
                                    String zsxx = getZsxx(zsMap, bdcXm, qllxVo, bdcdyh, qlr);
                                    if (StringUtils.isNotBlank(zsxx)) {
                                        String zsxxTitle = zsxx.substring(0, zsxx.indexOf('$'));
                                        nodeElement.addAttribute(ParamsConstants.TITLE_HUMP, zsxxTitle);
                                        String name = zsxx.substring(zsxx.indexOf('$') + 1, zsxx.length());
                                        nodeElement.addAttribute("Name", name);
                                    }
                                    String qlzt = getQlztByQllxVo(qllxVo, bdcXm);
                                    String qlColor = bdcZdGlService.getQlColorByQlzt(qlzt, bdcZdQlztList);
                                    nodeElement.addAttribute(ParamsConstants.COLOR_HUMP, qlColor);
                                    String qlMC = bdcZdGlService.getQlMCByQlzt(qlzt, bdcZdQlztList);
                                    nodeElement.addAttribute(ParamsConstants.LEGENDNAME_HUMP, qlMC);

                                }

                            } else {
                                BdcXm bdcXm = getBdcXm(bdcXmList, bdcXmRel.getProid());
                                if (zsMap != null) {
                                    Element nodeElement = xmRelElement.addElement(ParamsConstants.WORKNODE_HUMP);
                                    nodeElement.addAttribute("ID", CommonUtil.formatEmptyValue(proidMap.get(bdcXmRel.getProid())));
                                    QllxVo qllxVo = getQllxVo(qllxVoList, bdcXmRel.getProid());
                                    if (qllxVo == null && StringUtils.isNotBlank(bdcXmRel.getYproid()))
                                        qllxVo = getQllxVo(qllxVoList, bdcXmRel.getYproid());
                                    String zsxx = getZsxx(zsMap, bdcXm, qllxVo, bdcdyh, qlr);
                                    if (StringUtils.isNotBlank(zsxx)) {
                                        String zsxxTitle = zsxx.substring(0, zsxx.indexOf('$'));
                                        nodeElement.addAttribute(ParamsConstants.TITLE_HUMP, zsxxTitle);
                                        String name = zsxx.substring(zsxx.indexOf('$') + 1, zsxx.length());
                                        nodeElement.addAttribute("Name", name);
                                    }
                                    String qlzt = getQlztByQllxVo(qllxVo, bdcXm);
                                    String qlColor = bdcZdGlService.getQlColorByQlzt(qlzt, bdcZdQlztList);
                                    nodeElement.addAttribute(ParamsConstants.COLOR_HUMP, qlColor);
                                    String qlMC = bdcZdGlService.getQlMCByQlzt(qlzt, bdcZdQlztList);
                                    nodeElement.addAttribute(ParamsConstants.LEGENDNAME_HUMP, qlMC);

                                }
                            }
                        } else {
                            BdcXm bdcXm = getBdcXm(bdcXmList, bdcXmRel.getProid());
                            if (zsMap != null&&bdcXm != null) {
                                Element nodeElement = xmRelElement.addElement(ParamsConstants.WORKNODE_HUMP);
                                nodeElement.addAttribute("ID", CommonUtil.formatEmptyValue(proidMap.get(bdcXmRel.getProid())));
                                QllxVo qllxVo = getQllxVo(qllxVoList, bdcXmRel.getProid());
                                if (qllxVo == null && StringUtils.isNotBlank(bdcXmRel.getYproid()))
                                    qllxVo = getQllxVo(qllxVoList, bdcXmRel.getYproid());
                                String zsxx = getZsxx(zsMap, bdcXm, qllxVo, bdcdyh, qlr);
                                if (StringUtils.isNotBlank(zsxx)) {
                                    String zsxxTitle = zsxx.substring(0, zsxx.indexOf('$'));
                                    nodeElement.addAttribute(ParamsConstants.TITLE_HUMP, zsxxTitle);
                                    String name = zsxx.substring(zsxx.indexOf('$') + 1, zsxx.length());
                                    nodeElement.addAttribute("Name", name);
                                }
                                String qlzt = getQlztByQllxVo(qllxVo, bdcXm);
                                String qlColor = bdcZdGlService.getQlColorByQlzt(qlzt, bdcZdQlztList);
                                nodeElement.addAttribute(ParamsConstants.COLOR_HUMP, qlColor);
                                String qlMC = bdcZdGlService.getQlMCByQlzt(qlzt, bdcZdQlztList);
                                nodeElement.addAttribute(ParamsConstants.LEGENDNAME_HUMP, qlMC);

                            }
                        }
                    }
                }
            }
        }
        return document;
    }

    public static List<String> getProidListByProid(List<BdcXmRel> bdcXmRelList, String proid) {
        List<String> proidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(proid)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (bdcXmRel != null && StringUtils.equals(proid, bdcXmRel.getYproid()))
                    proidList.add(bdcXmRel.getProid());
            }
        }
        return proidList;
    }

    public static QllxVo getQllxVo(List<QllxVo> qllxVoList, String proid) {
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            for (QllxVo qllxVo : qllxVoList) {
                if (StringUtils.equals(proid, qllxVo.getProid())) {
                    return qllxVo;
                }
            }
        }
        return null;
    }

    public static HashMap getZsxxMap(List<HashMap> bdcZsList, String proid) {
        HashMap zsxxMap = new HashMap();
        if (bdcZsList != null && StringUtils.isNotBlank(proid)) {
            List<HashMap> zsMapList = new ArrayList<HashMap>();
            for (HashMap map : bdcZsList) {
                if (map != null && StringUtils.equals(CommonUtil.formatEmptyValue(map.get("ID")), proid)) {
                    zsMapList.add(map);
                }
            }
            if (CollectionUtils.isNotEmpty(zsMapList)) {
                StringBuilder bdcqzh = new StringBuilder();
                StringBuilder zl = new StringBuilder();
                StringBuilder qlr = new StringBuilder();
                List<String> zlList = new ArrayList<String>();
                for (HashMap zsMap : zsMapList) {
                    zsxxMap = zsMap;
                    if (zsMap != null && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(zsMap.get(ParamsConstants.BDCQZH_CAPITAL)))) {
                        if (StringUtils.isNotBlank(bdcqzh)) {
                            bdcqzh.append(",").append(zsMap.get(ParamsConstants.BDCQZH_CAPITAL));
                        }else {
                            bdcqzh.append(CommonUtil.formatEmptyValue(zsMap.get(ParamsConstants.BDCQZH_CAPITAL)));
                        }
                    }
                    if (zsMap != null && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(zsMap.get("ZL")))) {
                        if (!zlList.contains(CommonUtil.formatEmptyValue(zsMap.get("ZL")))) {
                            if (StringUtils.isNotBlank(zl)) {
                                zl.append(",").append(zsMap.get("ZL"));
                            }else {
                                zl.append(CommonUtil.formatEmptyValue(zsMap.get("ZL")));
                            }
                        }
                        zlList.add(CommonUtil.formatEmptyValue(zsMap.get("ZL")));
                    }
                    if (zsMap != null && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(zsMap.get("QLR")))) {
                        if (StringUtils.isNotBlank(qlr)) {
                            qlr.append(",").append(zsMap.get("QLR"));
                        }else {
                            qlr.append(CommonUtil.formatEmptyValue(zsMap.get("QLR")));
                        }
                    }
                }
                if(zsxxMap != null){
                    zsxxMap.put(ParamsConstants.BDCQZH_CAPITAL, bdcqzh);
                    zsxxMap.put("ZL", zl);
                    zsxxMap.put("QLR", qlr);
                }
            }
        }
        return zsxxMap;
    }

    public static BdcXm getBdcXm(List<BdcXm> bdcXmList, String proid) {
        BdcXm bdcXm = null;
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm queryBdcXm : bdcXmList) {
                if (StringUtils.equals(proid, queryBdcXm.getProid())) {
                    bdcXm = queryBdcXm;
                    break;
                }
            }
        }
        return bdcXm;
    }

    public static String getZsxx(HashMap zsMap, BdcXm bdcXm, QllxVo qllxVo, String bdcdyh, String qlr) {
        StringBuilder zsxx = new StringBuilder();
        boolean isjf = false;
        if (zsMap != null&&bdcXm!=null) {
            if (StringUtils.isNotBlank(bdcXm.getDjlx()))
                zsxx.append("登记类型：").append(bdcXm.getDjlx()).append("$");
            if (StringUtils.isNotBlank(bdcXm.getSqlx()))
                zsxx.append("申请类型：").append(bdcXm.getSqlx()).append("$");
            if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(zsMap.get("BDCDYH"))))
                zsxx.append("不动产单元：").append(CommonUtil.formatEmptyValue(zsMap.get("BDCDYH"))).append("$");
            else {
                if (StringUtils.isNotBlank(bdcdyh))
                    zsxx.append("不动产单元：").append(bdcdyh).append("$");
            }

            if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(zsMap.get(ParamsConstants.BDCQZH_CAPITAL))))
                zsxx.append("不动产证号：").append(CommonUtil.formatEmptyValue(zsMap.get(ParamsConstants.BDCQZH_CAPITAL))).append("$");
            if (StringUtils.isNotBlank(qlr)) {
                zsxx.append("权利人：").append(qlr).append("$");
            }


            if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(zsMap.get("ZL"))))
                zsxx.append("坐落：").append(CommonUtil.formatEmptyValue(zsMap.get("ZL"))).append("$");
            else {
                if (StringUtils.isNotBlank(bdcXm.getZl())) {
                    zsxx.append("坐落：").append(bdcXm.getZl()).append("$");
                }
            }

        }
        if (qllxVo instanceof BdcCf&&bdcXm != null) {
            //zhouwanqing 如果qllxVo的proid和bdcxm的不一致，则认为是这个是解封
            if (!StringUtils.equals(qllxVo.getProid(), bdcXm.getProid())) {
                isjf = true;
            }
            return String.valueOf(cfInfo(zsxx, qllxVo, isjf));
        }
        return zsxx.toString();
    }

    private static StringBuilder cfInfo(StringBuilder zsxx, QllxVo qllxVo, boolean isjf) {
        BdcCf bdcCf = (BdcCf) qllxVo;
        if (StringUtils.isNotBlank(bdcCf.getCfjg()))
            zsxx.append("查封机关：").append(bdcCf.getCfjg()).append("$");
        if (StringUtils.isNotBlank(bdcCf.getCfwh()))
            zsxx.append("查封文号：").append(bdcCf.getCfwh()).append("$");
        if (StringUtils.isNotBlank(bdcCf.getCfwj()))
            zsxx.append("查封文件：").append(bdcCf.getCfwj()).append("$");
        if (bdcCf.getQszt() != null&&Constants.QLLX_QSZT_HR.equals(bdcCf.getQszt())&& isjf) {
            //查封不需要显示下面的信息，来区分查封和解封
            if (StringUtils.isNotBlank(bdcCf.getJfjg())) {
                zsxx.append("解封机关：").append(bdcCf.getJfjg()).append("$");
            }
            if (StringUtils.isNotBlank(bdcCf.getJfwh())) {
                zsxx.append("解封文号：").append(bdcCf.getJfwh()).append("$");
            }
        }
        return zsxx;
    }

    //zhouwanqing 根据qllxvo获取相应的颜色
    public static String getQlztByQllxVo(QllxVo qllxVo, BdcXm bdcXm) {
        String qlzt = "0";
        if (qllxVo != null) {
            if (qllxVo instanceof BdcCf) {
                qlzt = "3";
            } else if (qllxVo instanceof BdcDyaq) {
                qlzt = "2";
            } else if (qllxVo instanceof BdcYg) {
                //预告抵押有自己的颜色
                if (bdcXm != null && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFDY) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BDCDY))) {
                    qlzt = "5";
                } else {
                    qlzt = "4";
                }
            } else if (qllxVo instanceof BdcYy) {
                qlzt = "6";
            } else if (qllxVo instanceof BdcDyq) {
                qlzt = "7";
            } else {
                qlzt = "1";
            }
        }
        return qlzt;
    }

}
