package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.model.dataPic.BdcPicQo;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataFgService;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataHbService;
import cn.gtmap.estateplat.server.utils.CalculationUtils;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/12
 * @description 不动产数据分割合并功能
 */
@Service
public class BdcFgHbServiceImpl implements BdcFgHbService {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdjbService bdcdjbService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private Set<BdcDataHbService> bdcDataHbServiceList;
    @Autowired
    private Set<BdcDataFgService> bdcDataFgServiceList;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcYgService bdcYgService;

    @Override
    public String initBdcFghb(Model model, String proids) {
        String path = "/wf/dataFghb/bdcSplit";
        if (StringUtils.isNotBlank(proids)) {
            String[] proidArray = StringUtils.split(proids, ",");
            if (proidArray != null && proidArray.length > 0) {
                String fzxxids = "";
                StringBuilder fzxxidList = new StringBuilder();
                List<BdcPicQo> bdcPicQoList = Lists.newArrayList();
                String bdclx = "";
                for (String proid : proidArray) {
                    if (proidArray.length == 1) {
                        BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(proid);
                        if (bdcFdcqDz != null && StringUtils.isNotBlank(bdcFdcqDz.getQlid())) {
                            List<BdcFwfzxx> bdcFwfzxxList = bdcFwfzxxService.getBdcFwfzxxListByQlid(bdcFdcqDz.getQlid());
                            if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
                                bdcPicQoList = getBdcPicQoListByFwfzxx(bdcFwfzxxList, proid);
                                for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                                    if (StringUtils.isBlank(fzxxidList)) {
                                        fzxxidList.append(bdcFwfzxx.getFzid());
                                    } else {
                                        fzxxidList.append(",").append(bdcFwfzxx.getFzid());
                                    }
                                }
                                if (StringUtils.isNotBlank(fzxxidList)) {
                                    fzxxids = fzxxidList.toString();
                                }
                            }
                        }
                    } else {
                        BdcPicQo bdcPicQo = new BdcPicQo();
                        bdcPicQo.setProid(proid);
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                        if (bdcBdcdy != null) {
                            if (StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                                bdcPicQo.setBdclx(bdcBdcdy.getBdclx());
                                bdclx = bdcBdcdy.getBdclx();
                            }
                            bdcPicQo.setBdcdyh(bdcBdcdy.getBdcdyh());
                        }
                        Map zsmap = bdcZsService.queryBdcqzhByProid(proid);
                        if (MapUtils.isNotEmpty(zsmap)) {
                            if (zsmap.containsKey("BDCQZH")) {
                                bdcPicQo.setBdcqzh(zsmap.get("BDCQZH").toString());
                            }
                        }
                        bdcPicQoList.add(bdcPicQo);
                    }
                }
                String bdcqzhs = "";
                StringBuilder bdcqzhBuilder = new StringBuilder();
                if (CollectionUtils.isNotEmpty(bdcPicQoList)) {
                    for (BdcPicQo bdcPicQo : bdcPicQoList) {
                        if (StringUtils.isNotBlank(bdcqzhBuilder)) {
                            bdcqzhBuilder.append(",").append(bdcPicQo.getBdcqzh());
                        } else {
                            bdcqzhBuilder.append(bdcPicQo.getBdcqzh());
                        }
                    }
                }
                if (StringUtils.isNotBlank(bdcqzhBuilder)) {
                    bdcqzhs = bdcqzhBuilder.toString();
                }
                model.addAttribute("bdclx", StringUtils.isNoneBlank(bdclx) ? bdclx : "TDFW");
                model.addAttribute("bdcPicQoList", bdcPicQoList);
                model.addAttribute("proids", proids);
                model.addAttribute("fzxxids", fzxxids);
                model.addAttribute("bdcqzhs", bdcqzhs);
                if (proidArray.length > 1) {
                    path = "/wf/dataFghb/bdcCombine";
                }
            }
        }
        return path;
    }

    /**
     * @param model
     * @param wiid  工作流实例ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化查看信息
     */
    @Override
    public String initCkxx(Model model, String wiid) {
        String path = "/wf/dataFghb/ckxx";
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            List<BdcPicQo> bdcPicQoList = Lists.newArrayList();
            for (BdcXm bdcXm : bdcXmList) {
                BdcPicQo bdcPicQo = new BdcPicQo();
                bdcPicQo.setProid(bdcXm.getProid());
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                if (bdcBdcdy != null) {
                    bdcPicQo.setBdcdyh(bdcBdcdy.getBdcdyh());
                }
                Map zsmap = bdcZsService.queryBdcqzhByProid(bdcXm.getProid());
                if (MapUtils.isNotEmpty(zsmap)) {
                    if (zsmap.containsKey("BDCQZH")) {
                        bdcPicQo.setBdcqzh(zsmap.get("BDCQZH").toString());
                    }
                }
                bdcPicQoList.add(bdcPicQo);
            }
            model.addAttribute("bdcPicQoList", bdcPicQoList);
        }
        return path;
    }

    @Override
    public Map checkExistFdcqDz(String proids) {
        Map map = new HashMap();
        String result = ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(proids)) {
            String[] proidArray = proids.split(",");
            if (proidArray != null && proidArray.length > 0) {
                for (String proid : proidArray) {
                    if (StringUtils.isNotBlank(proid)) {
                        BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(proid);
                        if (bdcFdcqDz != null && StringUtils.isNotBlank(bdcFdcqDz.getQlid())) {
                            result = ParamsConstants.TRUE_LOWERCASE;
                            break;
                        }
                    }
                }
            }
        }
        map.put(ParamsConstants.MSG_LOWERCASE, result);
        return map;
    }

    @Override
    @Transactional
    public Map combineBdcData(String proids, String bdcdyh, String qjid, String userid) {
        Map map = new HashMap();
        if (StringUtils.isNotBlank(proids) && StringUtils.isNotBlank(bdcdyh)) {
            String[] proidArray = proids.split(",");
            List<String> proidList = Arrays.asList(proidArray);
            if (CollectionUtils.isNotEmpty(proidList)) {
                Map dataMap = getCombineDateByProids(proidList);
                if (dataMap != null) {
                    //取第一个proid作为合并后的proid
                    String combineCqProid = proidList.get(0);
                    dataMap.put("combineCqProid", combineCqProid);
                    //判断不动产数据中是否存在合并后的不动产单元
                    BdcBdcdy bdcBdcdy = null;
                    BdcBdcdjb bdcdjb = null;
                    if (StringUtils.isNotBlank(bdcdyh)) {
                        bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
                    }
                    if (bdcBdcdy == null) {
                        //不存在要匹配的不动产单元信息 初始化不动产单元信息
                        //初始化登记簿
                        InitVoFromParm initVoFromParm = bdcDjsjService.getDjxx(bdcdyh, qjid, Constants.BDCLX_TDFW);
                        if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() > 19) {
                            //防止造成的垃圾数据
                            String zdzhh = StringUtils.substring(bdcdyh, 0, 19);
                            List<BdcBdcdjb> bdcdjbTemps = bdcdjbService.selectBdcdjb(zdzhh);
                            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                                bdcdjb = bdcdjbService.initBdcdjb(initVoFromParm, combineCqProid, null, zdzhh, userid);
                            } else {
                                bdcdjb = bdcdjbTemps.get(0);
                            }
                        }
                        if (bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid())) {
                            //初始化不动产单元
                            bdcBdcdy = bdcdyService.initBdcdy(initVoFromParm, bdcdjb, bdcdyh, Constants.BDCLX_TDFW);
                            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                                bdcdjbService.saveBdcBdcdjb(bdcdjb);
                                bdcdyService.saveBdcdy(bdcBdcdy);
                                dataMap.put("bdcdjb", bdcdjb);
                            }
                        }
                    }
                    dataMap.put("bdcBdcdy", bdcBdcdy);

                    //相关表合并数据
                    for (BdcDataHbService bdcDataHbService : bdcDataHbServiceList) {
                        bdcDataHbService.combineData(dataMap);
                    }
                    map.put(ParamsConstants.MSG_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
                }
            }
        }
        return map;
    }

    @Override
    public Map getCombineDateByProids(List<String> proidList) {
        Map dataMap = null;
        if (CollectionUtils.isNotEmpty(proidList)) {
            dataMap = new HashMap();

            StringBuilder ybdcqzhBuilder = new StringBuilder();
            StringBuilder ybhBuilder = new StringBuilder();
            Double mj = 0.0;
            Double zdzhmj = 0.0;
            Double jzmj = 0.0;
            Double tnjzmj = 0.0;
            Double ftjzmj = 0.0;
            Double dytdmj = 0.0;
            Double fttdmj = 0.0;
            List<BdcQlr> bdcQlrList = new ArrayList<>();
            List<BdcXmRel> bdcXmRelList = new ArrayList<>();
            List<String> bdcdyidList = new ArrayList<>();
            for (String proid : proidList) {
                if (StringUtils.isNotBlank(proid)) {
                    //bdcxm中可获取组织的数据
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    if (bdcXm != null) {
                        if (StringUtils.isNotBlank(ybdcqzhBuilder)) {
                            ybdcqzhBuilder.append(",").append(bdcXm.getYbdcqzh());
                        } else {
                            ybdcqzhBuilder.append(bdcXm.getYbdcqzh());
                        }
                        if (StringUtils.isNotBlank(ybhBuilder)) {
                            ybhBuilder.append(",").append(bdcXm.getYbh());
                        } else {
                            ybhBuilder.append(bdcXm.getYbh());
                        }
                        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                            bdcdyidList.add(bdcXm.getBdcdyid());
                        }
                    }
                    //bdcspxx中可组织的数据
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    if (bdcSpxx != null) {
                        if (bdcSpxx.getMj() != null && mj != null) {
                            mj = CalculationUtils.doubleAdd(mj, bdcSpxx != null ? bdcSpxx.getMj() : 0.0);
                        }
                        zdzhmj = bdcSpxx.getZdzhmj();
                    }
                    //bdcfdcq中可组织的数据
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
                    if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                        jzmj = CalculationUtils.doubleAdd(jzmj, bdcFdcq.getJzmj() != null ? bdcFdcq.getJzmj() : 0.0);
                        tnjzmj = CalculationUtils.doubleAdd(tnjzmj, bdcFdcq.getTnjzmj() != null ? bdcFdcq.getTnjzmj() : 0.0);
                        ftjzmj = CalculationUtils.doubleAdd(ftjzmj, bdcFdcq.getFtjzmj() != null ? bdcFdcq.getFtjzmj() : 0.0);
                        dytdmj = CalculationUtils.doubleAdd(dytdmj, bdcFdcq.getDytdmj() != null ? bdcFdcq.getDytdmj() : 0.0);
                        fttdmj = CalculationUtils.doubleAdd(fttdmj, bdcFdcq.getFttdmj() != null ? bdcFdcq.getFttdmj() : 0.0);
                    }
                }
            }

            bdcXmRelList = dealCombineBdcXmRel(proidList);

            String ybdcqzh = StringUtils.isNotBlank(ybdcqzhBuilder) ? ybdcqzhBuilder.toString() : null;
            String ybh = StringUtils.isNotBlank(ybhBuilder) ? ybhBuilder.toString() : null;

            dataMap.put("ybdcqzh", ybdcqzh);
            dataMap.put("ybh", ybh);
            dataMap.put("mj", mj);
            dataMap.put("zdzhmj", zdzhmj);
            dataMap.put("jzmj", jzmj);
            dataMap.put("tnjzmj", tnjzmj);
            dataMap.put("ftjzmj", ftjzmj);
            dataMap.put("dytdmj", dytdmj);
            dataMap.put("fttdmj", fttdmj);
            dataMap.put("bdcQlrList", bdcQlrList);
            dataMap.put("bdcXmRelList", bdcXmRelList);
            dataMap.put("bdcdyidList", bdcdyidList);
        }
        return dataMap;
    }

    private List<BdcXmRel> dealCombineBdcXmRel(List<String> proidList) {
        List<BdcXmRel> bdcXmRelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(proidList)) {
            for (String proid : proidList) {
                List<BdcXmRel> bdcXmRelListTemp = bdcXmRelService.queryBdcXmRelByProid(proid);
                if (CollectionUtils.isNotEmpty(bdcXmRelListTemp)) {
                    for (BdcXmRel bdcXmRelTemp : bdcXmRelListTemp) {
                        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                            Boolean addXmRel = true;
                            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                if (StringUtils.equals(bdcXmRel.getYproid(), bdcXmRelTemp.getYproid())) {
                                    addXmRel = false;
                                }
                            }
                            if (addXmRel) {
                                bdcXmRelTemp.setRelid(UUIDGenerator.generate18());
                                bdcXmRelTemp.setProid(proidList.get(0));
                                bdcXmRelList.add(bdcXmRelTemp);
                            }
                        } else {
                            bdcXmRelTemp.setRelid(UUIDGenerator.generate18());
                            bdcXmRelTemp.setProid(proidList.get(0));
                            bdcXmRelList.add(bdcXmRelTemp);
                        }
                    }
                }
            }
        }
        return bdcXmRelList;
    }

    @Override
    @Transactional
    public Map splitBdcData(List<Map> splitMaps, String userid) {
        Map map = new HashMap();
        Map delDataMap = null;
        if (CollectionUtils.isNotEmpty(splitMaps)) {
            for (Map splitMap : splitMaps) {
                //组织更新数据
                Map dataMap = getSplitDate(splitMap, userid);
                if (delDataMap == null) {
                    delDataMap = dataMap;
                }
                //进行拆分
                if (dataMap != null) {
                    for (BdcDataFgService bdcDataFgService : bdcDataFgServiceList) {
                        bdcDataFgService.splitData(dataMap);
                    }
                }
            }
            //对拆分多幢信息进行删除
            for (BdcDataFgService bdcDataFgService : bdcDataFgServiceList) {
                bdcDataFgService.deleteSplitOldData(delDataMap);
            }
            map.put(ParamsConstants.MSG_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
        }
        return map;
    }

    @Override
    public Map getSplitDate(Map splitMap, String userid) {
        Map dataMap = null;
        if (splitMap != null) {
            dataMap = new HashMap();
            String splitCqProid = UUIDGenerator.generate18();
            if (splitMap.get("fwfzxxid") != null && StringUtils.isNotBlank(splitMap.get("fwfzxxid").toString())) {
                BdcFwfzxx bdcFwfzxx = bdcFwfzxxService.getBdcFwfzxxListByfzxxid(splitMap.get("fwfzxxid").toString());
                if (bdcFwfzxx != null) {
                    dataMap.put("bdcFwfzxx", bdcFwfzxx);
                }
            }
            if (splitMap.get("proid") != null && StringUtils.isNotBlank(splitMap.get("proid").toString())) {
                BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(splitMap.get("proid").toString());
                if (bdcFdcqDz != null) {
                    dataMap.put("bdcFdcqDz", bdcFdcqDz);
                    if (StringUtils.isNotBlank(bdcFdcqDz.getBdcdyid())) {
                        BdcBdcdy dzBdcBdcdy = bdcdyService.queryBdcdyById(bdcFdcqDz.getBdcdyid());
                        if (dzBdcBdcdy != null) {
                            dataMap.put("dzBdcBdcdy", dzBdcBdcdy);
                        }
                    }
                }
            }
            if (splitMap.get("qjid") != null && StringUtils.isNotBlank(splitMap.get("qjid").toString())
                    && splitMap.get("bdcdyh") != null && StringUtils.isNotBlank(splitMap.get("bdcdyh").toString())) {
                String bdcdyh = splitMap.get("bdcdyh").toString();
                String qjid = splitMap.get("qjid").toString();
                BdcBdcdy bdcBdcdy = null;
                BdcBdcdjb bdcdjb = null;
                if (StringUtils.isNotBlank(bdcdyh)) {
                    bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
                }
                if (bdcBdcdy == null) {
                    //不存在要匹配的不动产单元信息 初始化不动产单元信息
                    //初始化登记簿
                    InitVoFromParm initVoFromParm = bdcDjsjService.getDjxx(bdcdyh, qjid, Constants.BDCLX_TDFW);
                    if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() > 19) {
                        //防止造成的垃圾数据
                        String zdzhh = StringUtils.substring(bdcdyh, 0, 19);
                        List<BdcBdcdjb> bdcdjbTemps = bdcdjbService.selectBdcdjb(zdzhh);
                        if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                            bdcdjb = bdcdjbService.initBdcdjb(initVoFromParm, splitCqProid, null, zdzhh, userid);
                        } else {
                            bdcdjb = bdcdjbTemps.get(0);
                        }
                    }
                    if (bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid())) {
                        //初始化不动产单元
                        bdcBdcdy = bdcdyService.initBdcdy(initVoFromParm, bdcdjb, bdcdyh, Constants.BDCLX_TDFW);
                        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                            bdcdjbService.saveBdcBdcdjb(bdcdjb);
                            bdcdyService.saveBdcdy(bdcBdcdy);
                        }
                    }
                }
                dataMap.put("bdcBdcdy", bdcBdcdy);
                dataMap.put("splitCqProid", splitCqProid);
            }
        }
        return dataMap;
    }

    @Override
    public Map getHbDetailInfo(String proids) {
        Map detailMap = null;
        if (StringUtils.isNotBlank(proids)) {
            detailMap = new HashMap();
            String[] proidArray = proids.split(",");
            List<String> proidList = Arrays.asList(proidArray);
            List<String> bdcdyidList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(proidList)) {
                Map dataMap = getCombineDateByProids(proidList);
                //产权信息
                String cqProid = proidList.get(0);
                Map cqMap = getHbCqDetailInfo(cqProid, dataMap);
                if (cqMap != null) {
                    detailMap.put("cq", cqMap);
                }

                for (String proid : proidList) {
                    if (StringUtils.isNotBlank(proid)) {
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                            bdcdyidList.add(bdcXm.getBdcdyid());
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(bdcdyidList)) {
                //抵押
                Map dyMap = getHbDyaqDetailInfo(bdcdyidList);
                if (dyMap != null) {
                    detailMap.put("dy", dyMap);
                }
                //查封
                Map cfMap = getHbCfDetailInfo(bdcdyidList);
                if (cfMap != null) {
                    detailMap.put("cf", cfMap);
                }
                //预告
                Map ygMap = getHbYgDetailInfo(bdcdyidList);
                if (ygMap != null) {
                    detailMap.put("yg", ygMap);
                }
            }
        }
        return detailMap;
    }

    @Override
    public Map getHbCqDetailInfo(String proid, Map dataMap) {
        Map cqMap = null;
        if (StringUtils.isNotBlank(proid) && dataMap != null) {
            cqMap = new HashMap();
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getZl())) {
                cqMap.put("zl", bdcSpxx.getZl());
            }
            StringBuilder cqzhBuilder = new StringBuilder();
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    if (StringUtils.isNotBlank(cqzhBuilder)) {
                        cqzhBuilder.append(",").append(bdcZs.getBdcqzh());
                    } else {
                        cqzhBuilder.append(bdcZs.getBdcqzh());
                    }
                }
            }
            if (StringUtils.isNotBlank(cqzhBuilder)) {
                cqMap.put("bdcqzh", cqzhBuilder.toString());
            }
            Double mj = dataMap.get("mj") != null ? (Double) dataMap.get("mj") : null;
            if (mj != null) {
                cqMap.put("mj", mj);
            }
            Double zdzhmj = dataMap.get("zdzhmj") != null ? (Double) dataMap.get("zdzhmj") : null;
            if (zdzhmj != null) {
                cqMap.put("zdzhmj", zdzhmj);
            }
        }
        return cqMap;
    }

    @Override
    public Map getHbDyaqDetailInfo(List<String> bdcdyidList) {
        Map dyMap = null;
        if (CollectionUtils.isNotEmpty(bdcdyidList)) {
            BdcDyaq bdcDyaq = null;
            for (String bdcdyid : bdcdyidList) {
                if (StringUtils.isNotBlank(bdcdyid)) {
                    Map queryMap = new HashMap();
                    queryMap.put("bdcdyid", bdcdyid);
                    List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(queryMap);
                    if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                        bdcDyaq = bdcDyaqList.get(0);
                        break;
                    }
                }
            }
            if (bdcDyaq != null) {
                dyMap = new HashMap();
                StringBuilder cqzhBuilder = new StringBuilder();
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcDyaq.getProid());
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    for (BdcZs bdcZs : bdcZsList) {
                        if (StringUtils.isNotBlank(cqzhBuilder)) {
                            cqzhBuilder.append(",").append(bdcZs.getBdcqzh());
                        } else {
                            cqzhBuilder.append(bdcZs.getBdcqzh());
                        }
                    }
                }
                if (StringUtils.isNotBlank(cqzhBuilder)) {
                    dyMap.put("dyzm", cqzhBuilder.toString());
                }
                if (bdcDyaq.getBdbzzqse() != null) {
                    dyMap.put("bdbzqse", bdcDyaq.getBdbzzqse());
                }

                if (bdcDyaq.getZwlxksqx() != null) {
                    dyMap.put("zwlxksqx", CalendarUtil.sdf_China.format(bdcDyaq.getZwlxksqx()));
                }

                if (bdcDyaq.getZwlxjsqx() != null) {
                    dyMap.put("zwlxjsqx", CalendarUtil.sdf_China.format(bdcDyaq.getZwlxjsqx()));
                }

                if (bdcDyaq.getDyfs() != null) {
                    dyMap.put("dyfs", bdcDyaq.getDyfs());
                }
            }
        }
        return dyMap;
    }

    @Override
    public Map getHbCfDetailInfo(List<String> bdcdyidList) {
        Map cfMap = null;
        if (CollectionUtils.isNotEmpty(bdcdyidList)) {
            BdcCf bdcCf = null;
            for (String bdcdyid : bdcdyidList) {
                if (StringUtils.isNotBlank(bdcdyid)) {
                    List<BdcCf> bdcCfList = bdcCfService.queryCfByBdcdyid(bdcdyid);
                    if (CollectionUtils.isNotEmpty(bdcCfList)) {
                        bdcCf = bdcCfList.get(0);
                        break;
                    }
                }
            }
            if (bdcCf != null) {
                cfMap = new HashMap();
                if (StringUtils.isNotBlank(bdcCf.getCfwh())) {
                    cfMap.put("cfwh", bdcCf.getCfwh());
                }
                if (StringUtils.isNotBlank(bdcCf.getCffw())) {
                    cfMap.put("cffw", bdcCf.getCffw());
                }
                if (bdcCf.getCfksqx() != null) {
                    cfMap.put("cfksqx", CalendarUtil.sdf_China.format(bdcCf.getCfksqx()));
                }
                if (bdcCf.getCfjsqx() != null) {
                    cfMap.put("cfjsqx", CalendarUtil.sdf_China.format(bdcCf.getCfjsqx()));
                }
            }
        }
        return cfMap;
    }

    @Override
    public Map getHbYgDetailInfo(List<String> bdcdyidList) {
        Map ygMap = null;
        if (CollectionUtils.isNotEmpty(bdcdyidList)) {
            BdcYg bdcYg = null;
            BdcYg bdcDyyg = null;
            for (String bdcdyid : bdcdyidList) {
                if (StringUtils.isNotBlank(bdcdyid)) {
                    HashMap queryMap = new HashMap();
                    queryMap.put("bdcdyid", bdcdyid);
                    List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(queryMap);
                    if (CollectionUtils.isNotEmpty(bdcYgList)) {
                        for (BdcYg bdcYgTemp : bdcYgList) {
                            if (CommonUtil.indexOfStrs(Constants.YG_YGDJZL_DY, bdcYg.getYgdjzl())) {
                                if (bdcDyyg == null) {
                                    bdcDyyg = bdcYgTemp;
                                }
                            } else {
                                if (bdcYg == null) {
                                    bdcYg = bdcYgTemp;
                                }
                            }
                        }
                    }
                }
            }
            if (bdcYg != null) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcYg.getProid());
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    ygMap.put("ygzm", bdcZsList.get(0).getBdcqzh());
                }
            }
            if (bdcDyyg != null) {
                List<BdcZs> ygdybdcZsList = bdcZsService.queryBdcZsByProid(bdcDyyg.getProid());
                if (CollectionUtils.isNotEmpty(ygdybdcZsList)) {
                    ygMap.put("dyygzm", ygdybdcZsList.get(0).getBdcqzh());
                }
                if (bdcDyyg.getZwlxksqx() != null) {
                    ygMap.put("zwlxksqx", CalendarUtil.sdf_China.format(bdcDyyg.getZwlxksqx()));
                }
                if (bdcDyyg.getZwlxjsqx() != null) {
                    ygMap.put("zwlxjsqx", CalendarUtil.sdf_China.format(bdcDyyg.getZwlxjsqx()));
                }
            }
        }
        return ygMap;
    }

    @Override
    public Map getFgDetailInfo(List<Map> splitMaps) {
        Map detailMap = null;
        if (CollectionUtils.isNotEmpty(splitMaps)) {
            detailMap = new HashMap();
            String fdcqDzProid = null;
            if (splitMaps.get(0).get("proid") != null) {
                fdcqDzProid = splitMaps.get(0).get("proid").toString();
            }
            Map cqMap = getFgCqDetailInfo(splitMaps);
            if (cqMap != null) {
                detailMap.put("cq", cqMap);
            }
            if (StringUtils.isNotBlank(fdcqDzProid)) {
                BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(fdcqDzProid);
                if (bdcFdcqDz != null && StringUtils.isNotBlank(bdcFdcqDz.getBdcdyid())) {
                    Map dyMap = getFgDyaqDetailInfo(bdcFdcqDz.getBdcdyid());
                    if (dyMap != null) {
                        detailMap.put("dy", dyMap);
                    }

                    Map cfMap = getFgCfDetailInfo(bdcFdcqDz.getBdcdyid());
                    if (cfMap != null) {
                        detailMap.put("cf", cfMap);
                    }

                    Map ygMap = getFgYgDetailInfo(bdcFdcqDz.getBdcdyid());
                    if (ygMap != null) {
                        detailMap.put("yg", ygMap);
                    }
                }
            }
        }
        return detailMap;
    }

    @Override
    public Map getFgCqDetailInfo(List<Map> splitMaps) {
        Map cqMap = null;
        if (CollectionUtils.isNotEmpty(splitMaps)) {
            cqMap = new HashMap();
            String fdcqDzProid = null;
            List<Map> fwfzxxMapList = new ArrayList<>();
            for (Map splitMap : splitMaps) {
                if (splitMap.get("proid") != null && StringUtils.isBlank(fdcqDzProid)) {
                    fdcqDzProid = splitMap.get("proid").toString();
                }
                Map fwfzxxMap = null;
                if (splitMap.get("fwfzxxid") != null) {
                    BdcFwfzxx bdcFwfzxx = bdcFwfzxxService.getBdcFwfzxxListByfzxxid(splitMap.get("fwfzxxid").toString());
                    if (bdcFwfzxx != null) {
                        fwfzxxMap = new HashMap();
                        fwfzxxMap.put("zl", bdcFwfzxx.getXmmc());
                        if (splitMap.get("bdcdyh") != null) {
                            fwfzxxMap.put("bdcdyh", splitMap.get("bdcdyh").toString());
                        }
                        fwfzxxMapList.add(fwfzxxMap);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(fwfzxxMapList)) {
                cqMap.put("fwxxlist", fwfzxxMapList);
            }
            if (StringUtils.isNotBlank(fdcqDzProid)) {
                StringBuilder cqzhBuilder = new StringBuilder();
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(fdcqDzProid);
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    for (BdcZs bdcZs : bdcZsList) {
                        if (StringUtils.isNotBlank(cqzhBuilder)) {
                            cqzhBuilder.append(",").append(bdcZs.getBdcqzh());
                        } else {
                            cqzhBuilder.append(bdcZs.getBdcqzh());
                        }
                    }
                }
                if (StringUtils.isNotBlank(cqzhBuilder)) {
                    cqMap.put("bdcqzh", cqzhBuilder.toString());
                }
            }
        }
        return cqMap;
    }

    @Override
    public Map getFgDyaqDetailInfo(String bdcdyid) {
        Map dyMap = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            List<Map> dyxxList = new ArrayList<>();
            Map queryMap = new HashMap();
            queryMap.put("bdcdyid", bdcdyid);
            queryMap.put("qszt", Constants.QLLX_QSZT_XS);
            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(queryMap);
            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                dyMap = new HashMap();
                for (BdcDyaq bdcDyaq : bdcDyaqList) {
                    Map dyxxMap = new HashMap();
                    if (StringUtils.isNotBlank(bdcDyaq.getProid())) {
                        StringBuilder cqzhBuilder = new StringBuilder();
                        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcDyaq.getProid());
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                if (StringUtils.isNotBlank(cqzhBuilder)) {
                                    cqzhBuilder.append(",").append(bdcZs.getBdcqzh());
                                } else {
                                    cqzhBuilder.append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(cqzhBuilder)) {
                            dyxxMap.put("dyzm", cqzhBuilder.toString());
                        }
                        if (StringUtils.isNotBlank(bdcDyaq.getDyfs())) {
                            dyxxMap.put("dyfs", bdcDyaq.getDyfs());
                        }
                        if (bdcDyaq.getBdbzzqse() != null) {
                            dyxxMap.put("bdbzzqs", bdcDyaq.getBdbzzqse());
                        }
                        if (bdcDyaq.getZwlxksqx() != null) {
                            dyxxMap.put("zwlxksqx", CalendarUtil.sdf_China.format(bdcDyaq.getZwlxksqx()));
                        }
                        if (bdcDyaq.getZwlxjsqx() != null) {
                            dyxxMap.put("zwlxjsqx", CalendarUtil.sdf_China.format(bdcDyaq.getZwlxjsqx()));
                        }
                        dyxxList.add(dyxxMap);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(dyxxList)) {
                dyMap.put("dyxxs", dyxxList);
            }
        }
        return dyMap;
    }

    @Override
    public Map getFgCfDetailInfo(String bdcdyid) {
        Map cfMap = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            List<Map> cfxxList = new ArrayList<>();
            List<BdcCf> bdcCfList = bdcCfService.queryCfByBdcdyid(bdcdyid);
            if (CollectionUtils.isNotEmpty(bdcCfList)) {
                cfMap = new HashMap();
                for (BdcCf bdcCf : bdcCfList) {
                    if (bdcCf.getQszt() == Constants.QLLX_QSZT_XS) {
                        Map cfxxMap = new HashMap();
                        if (StringUtils.isNotBlank(bdcCf.getCfwh())) {
                            cfxxMap.put("cfwh", bdcCf.getCfwh());
                            cfxxMap.put("cffw", bdcCf.getCffw());
                            if (bdcCf.getCfksqx() != null) {
                                cfxxMap.put("cfksqx", CalendarUtil.sdf_China.format(bdcCf.getCfksqx()));
                            }
                            if (bdcCf.getCfjsqx() != null) {
                                cfxxMap.put("cfjsqx", CalendarUtil.sdf_China.format(bdcCf.getCfjsqx()));
                            }
                        }
                        cfxxList.add(cfxxMap);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(cfxxList)) {
                cfMap.put("cfxxs", cfxxList);
            }
        }
        return cfMap;
    }

    @Override
    public Map getFgYgDetailInfo(String bdcdyid) {
        Map ygMap = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            List<Map> ygxxList = new ArrayList<>();
            HashMap queryMap = new HashMap();
            queryMap.put("bdcdyid", bdcdyid);
            queryMap.put("qszt", Constants.QLLX_QSZT_XS);
            List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(queryMap);
            if (CollectionUtils.isNotEmpty(bdcYgList)) {
                ygMap = new HashMap();
                for (BdcYg bdcYg : bdcYgList) {
                    Map ygxxMap = new HashMap();
                    StringBuilder cqzhBuilder = new StringBuilder();
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcYg.getProid());
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        for (BdcZs bdcZs : bdcZsList) {
                            if (StringUtils.isNotBlank(cqzhBuilder)) {
                                cqzhBuilder.append(",").append(bdcZs.getBdcqzh());
                            } else {
                                cqzhBuilder.append(bdcZs.getBdcqzh());
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(cqzhBuilder)) {
                        ygxxMap.put("ygzm", cqzhBuilder.toString());
                    }
                    if (bdcYg.getZwlxksqx() != null) {
                        ygxxMap.put("zwlxksqx", CalendarUtil.sdf_China.format(bdcYg.getZwlxksqx()));
                    }
                    if (bdcYg.getZwlxjsqx() != null) {
                        ygxxMap.put("zwlxjsqx", CalendarUtil.sdf_China.format(bdcYg.getZwlxjsqx()));
                    }
                    if (StringUtils.isNotBlank(bdcYg.getYgdjzl())) {
                        ygxxMap.put("ygdjzl", bdcYg.getYgdjzl());
                    }
                    ygxxList.add(ygxxMap);
                }
            }
            if (CollectionUtils.isNotEmpty(ygxxList)) {
                ygMap.put("ygxxs", ygxxList);
            }
        }
        return ygMap;
    }

    private List<BdcPicQo> getBdcPicQoListByFwfzxx(List<BdcFwfzxx> bdcFwfzxxList, String proid) {
        List<BdcPicQo> bdcPicQoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
            for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                BdcPicQo bdcPicQo = new BdcPicQo();
                bdcPicQo.setProid(bdcFwfzxx.getFzid());
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                if (bdcBdcdy != null) {
                    if (StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                        bdcPicQo.setBdclx(bdcBdcdy.getBdclx());
                    }
                    bdcPicQo.setBdcdyh(bdcBdcdy.getBdcdyh());
                }
                bdcPicQo.setBdcqzh(bdcFwfzxx.getXmmc());
                bdcPicQoList.add(bdcPicQo);
            }
        }
        return bdcPicQoList;
    }
}
