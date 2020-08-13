package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcPpgxXmMapper;
import cn.gtmap.estateplat.server.core.model.vo.BdcPicDTO;
import cn.gtmap.estateplat.server.core.model.vo.BdcPicData;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.model.dataPic.BdcPicQo;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.sj.pp.BdcdyPicUpdateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/25 0025
 * @description
 */
@Service
public class BdcPicServiceImpl implements BdcPicService {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcDjsjServiceImpl bdcDjsjService;
    @Autowired
    private BdcdjbService bdcdjbService;
    @Autowired
    private BdcPpgxLogService bdcPpgxLogService;
    @Autowired
    private BdcPpgxService bdcPpgxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private Set<BdcdyPicUpdateService> bdcdyMatchUpdateServiceList;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcPpgxXmMapper bdcPpgxXmMapper;
    @Autowired
    private DjsjFwService djsjFwService;


    private Logger logger = LoggerFactory.getLogger(BdcPicServiceImpl.class);

    /**
     * @param model
     * @param proids
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据匹配
     */
    @Override
    public void initBdcPic(Model model, String proids) {
        String[] proidArray = StringUtils.split(proids, ",");
        if (proidArray != null && proidArray.length > 0) {
            List<BdcPicQo> bdcPicQoList = Lists.newArrayList();
            String bdclx = "";
            for (String proid : proidArray) {
                BdcPicQo bdcPicQo = new BdcPicQo();
                bdcPicQo.setProid(proid);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                if (bdcBdcdy != null) {
                    if (StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                        bdcPicQo.setBdclx(bdcBdcdy.getBdclx());
                        bdclx = bdcBdcdy.getBdclx();
                    }
                    bdcPicQo.setBdcdyh(bdcBdcdy.getBdcdyh());
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 回调----房屋
                     */
                    BdcPpgx bdcPpgx = bdcPpgxService.getBdcPpgxByFwproid(proid);
                    if (bdcPpgx == null) {
                        bdcPpgx = bdcPpgxService.getBdcPpgxByTdproid(proid);
                    }
                    if (bdcPpgx != null) {
                        if (StringUtils.isNotBlank(bdcPpgx.getTdproid())) {
                            Map zsmap = bdcZsService.queryBdcqzhByProid(bdcPpgx.getTdproid());
                            if (MapUtils.isNotEmpty(zsmap)) {
                                bdcPicQo.setTdid(bdcPpgx.getTdproid());
                                if (zsmap.containsKey("BDCQZH")) {
                                    bdcPicQo.setTdbdcqzh(zsmap.get("BDCQZH").toString());
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(bdcPpgx.getBdcdyh())) {
                            bdcPicQo.setPpbdcdyh(bdcPpgx.getBdcdyh());
                            if (StringUtils.isNotBlank(bdcPicQo.getBdclx())) {
                                String djid = bdcDjsjService.getDjidByBdcdyh(bdcPpgx.getBdcdyh(), bdcPicQo.getBdclx());
                                if (StringUtils.isNotBlank(djid)) {
                                    bdcPicQo.setPpbdcdyhid(djid);
                                }
                            }
                        }
                    } else {
                        bdcPicQo.setFwbm(bdcBdcdy.getFwbm());
                        String maxProid = bdcXmService.getProidByBdcdyid(bdcBdcdy.getBdcdyid());
                        if (StringUtils.isNotBlank(maxProid)) {
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(maxProid);
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                                    if (bdcXm != null && StringUtils.equals(bdcXm.getQllx(), "3")) {
                                        Map zsmap = bdcZsService.queryBdcqzhByProid(bdcXm.getProid());
                                        if (MapUtils.isNotEmpty(zsmap)) {
                                            bdcPicQo.setTdid(bdcXm.getProid());
                                            if (zsmap.containsKey("BDCQZH")) {
                                                bdcPicQo.setTdbdcqzh(zsmap.get("BDCQZH").toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Map zsmap = bdcZsService.queryBdcqzhByProid(proid);
                if (MapUtils.isNotEmpty(zsmap)) {
                    if (zsmap.containsKey("BDCQZH")) {
                        bdcPicQo.setBdcqzh(zsmap.get("BDCQZH").toString());
                    }
                }
                bdcPicQoList.add(bdcPicQo);
            }
            model.addAttribute("bdclx", StringUtils.isNoneBlank(bdclx) ? bdclx : "TDFW");
            model.addAttribute("bdcPicQoList", bdcPicQoList);
        }
    }

    @Override
    public Map<String, String> bdcdyMatch(BdcPpgxLog bdcPpgxLog, String userid, String bdclx, String sfhqqj) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            BdcBdcdy bdcBdcdy = null;
            Map<String, Object> ppParamMap = null;
            if (bdcPpgxLog != null && StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(bdclx)) {
                String fwProid = bdcPpgxLog.getFwproid();
                String tdProid = bdcPpgxLog.getTdproid();
                String bdcdyh = bdcPpgxLog.getBdcdyh();
                String qjid = bdcPpgxLog.getQjid();
                //判断是否存在要匹配的不动产单元
                if (StringUtils.isNotBlank(bdcdyh)) {
                    bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
                }
                if (bdcBdcdy == null) {
                    //不存在要匹配的不动产单元信息 初始化不动产单元信息
                    //初始化登记簿
                    InitVoFromParm initVoFromParm = bdcDjsjService.getDjxx(bdcdyh, qjid, bdclx);
                    BdcBdcdjb bdcdjb = null;
                    if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() > 19) {
                        //防止造成的垃圾数据
                        String zdzhh = StringUtils.substring(bdcdyh, 0, 19);
                        List<BdcBdcdjb> bdcdjbTemps = bdcdjbService.selectBdcdjb(zdzhh);
                        if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                            bdcdjb = bdcdjbService.initBdcdjb(initVoFromParm, fwProid, tdProid, zdzhh, userid);
                        } else {
                            bdcdjb = bdcdjbTemps.get(0);
                        }
                    }
                    if (bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid())) {
                        //初始化不动产单元
                        bdcBdcdy = bdcdyService.initBdcdy(initVoFromParm, bdcdjb, bdcdyh, bdclx);
                        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                            bdcdjbService.saveBdcBdcdjb(bdcdjb);
                            bdcdyService.saveBdcdy(bdcBdcdy);
                        }
                    }
                }
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                    ppParamMap = new HashMap<>();
                    ppParamMap.put(ParamsConstants.BDCDYID_LOWERCASE, bdcBdcdy.getBdcdyid());
                    ppParamMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcBdcdy.getBdcdyh());
                    ppParamMap.put(ParamsConstants.DJBID_LOWERCASE, bdcBdcdy.getDjbid());
                    ppParamMap.put(ParamsConstants.QJID_LOWERCASE, qjid);
                    ppParamMap.put(ParamsConstants.USERID_HUMP, userid);
                    ppParamMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
                }
                if (ppParamMap != null && (StringUtils.isNotBlank(fwProid) || StringUtils.isNotBlank(tdProid))) {
                    //更新虚拟不动产单元对应表数据
                    String fwBdcdyh = matchFwBdcdy(ppParamMap, fwProid);
                    String tdBdcdyh = matchTdBdcdy(ppParamMap, tdProid);
                    //更新虚拟不动产单元对应权利的权利类型
                    matchQllx(ppParamMap, fwProid, tdProid);
                    //更新匹配关系数据
                    bdcPpgxService.saveBdcPpgxByMap(ppParamMap, fwProid, tdProid, fwBdcdyh, tdBdcdyh, Constants.BDC_PP_CZLX_PP, "",sfhqqj);
                    resultMap.put(ParamsConstants.MSG_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
                }
            }
        } catch (Exception e) {
            logger.error("BdcPicServiceImpl.bdcdyMatch:",e);
        }
        return resultMap;
    }

    private void matchQllx(Map<String, Object> ppParamMap, String fwProid, String tdProid) {
        String qllx = StringUtils.EMPTY;
        String bdcdyh = ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? ppParamMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : "";
        if (StringUtils.isNotBlank(bdcdyh)) {
            qllx = bdcdyService.getQllxFormBdcdy(bdcdyh);
        }
        if (StringUtils.isNotBlank(qllx)) {
            if (StringUtils.isNotBlank(fwProid)) {
                BdcXm fwBdcXm = bdcXmService.getBdcXmByProid(fwProid);
                QllxVo fwQllxVo = qllxService.queryQllxVo(fwBdcXm);
                if (fwQllxVo != null && (fwQllxVo instanceof BdcFdcq || fwQllxVo instanceof BdcFdcqDz)) {
                    fwBdcXm.setQllx(qllx);
                    fwQllxVo.setQllx(qllx);
                    entityMapper.saveOrUpdate(fwBdcXm, fwBdcXm.getProid());
                    entityMapper.saveOrUpdate(fwQllxVo, fwQllxVo.getQlid());
                    xgXsjQllx(fwProid, qllx);
                }
            } else {
                BdcXm tdBdcXm = bdcXmService.getBdcXmByProid(tdProid);
                QllxVo tdQllxVo = qllxService.queryQllxVo(tdBdcXm);
                if (tdQllxVo != null && tdQllxVo instanceof BdcJsydzjdsyq) {
                    tdBdcXm.setQllx(qllx);
                    tdQllxVo.setQllx(qllx);
                    entityMapper.saveOrUpdate(tdBdcXm, tdBdcXm.getProid());
                    entityMapper.saveOrUpdate(tdQllxVo, tdQllxVo.getQlid());
                    xgXsjQllx(tdProid, qllx);
                }
            }
        }
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 修改显示权利类型
     */
    private void xgXsjQllx(String proid, String qllx) {
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByYproid(proid);
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                if (qllxVo != null && (qllxVo instanceof BdcFdcq || qllxVo instanceof BdcFdcqDz || qllxVo instanceof BdcJsydzjdsyq)) {
                    qllxVo.setQllx(qllx);
                    bdcXm.setQllx(qllx);
                    entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                    entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
                }
            }
        }
    }

    @Override
    public String matchFwBdcdy(Map ppParamMap, String fwproid) {
        String fwBdcdyh = null;
        if (StringUtils.isNotBlank(fwproid) && ppParamMap != null) {
            BdcXm fwBdcxm = bdcXmService.getBdcXmByProid(fwproid);
            if (fwBdcxm != null) {
                BdcBdcdy fwBdcdy = bdcdyService.queryBdcdyById(fwBdcxm.getBdcdyid());
                if (fwBdcdy != null) {
                    fwBdcdyh = fwBdcdy.getBdcdyh();
                    //更新虚拟不动产单元对应表数据
                    for (BdcdyPicUpdateService bdcdyMatchUpdateService : bdcdyMatchUpdateServiceList) {
                        bdcdyMatchUpdateService.updateFwMatchBdcdyInfo(ppParamMap, fwBdcdy.getBdcdyid(), fwBdcdyh);
                    }
                    if (StringUtils.isNotBlank((CharSequence) ppParamMap.get(ParamsConstants.BDCDYID_LOWERCASE))) {
                        Map map = new HashMap();
                        map.put(ParamsConstants.BDCDYID_LOWERCASE, ppParamMap.get(ParamsConstants.BDCDYID_LOWERCASE));
                        List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(map);
                        if (CollectionUtils.isNotEmpty(bdcXmList)) {
                            for (BdcXm bdcXm : bdcXmList) {
                                CreatProjectService creatProjectService = projectService.getCreatProjectService(bdcXm);
                                creatProjectService.updateWorkFlow(bdcXm);
                            }
                        }
                    }
                }
            }
        }
        return fwBdcdyh;
    }

    @Override
    public String matchTdBdcdy(Map ppParamMap, String tdproid) {
        String tdBdcdyh = null;
        if (StringUtils.isNotBlank(tdproid) && ppParamMap != null) {
            BdcXm tdBdcxm = bdcXmService.getBdcXmByProid(tdproid);
            if (tdBdcxm != null) {
                BdcBdcdy tdBdcdy = bdcdyService.queryBdcdyById(tdBdcxm.getBdcdyid());
                if (tdBdcdy != null) {
                    tdBdcdyh = tdBdcdy.getBdcdyh();
                    //更新虚拟不动产单元对应表数据
                    for (BdcdyPicUpdateService bdcdyMatchUpdateService : bdcdyMatchUpdateServiceList) {
                        bdcdyMatchUpdateService.updateTdMatchBdcdyInfo(ppParamMap, tdBdcdy.getBdcdyid(), tdBdcdyh);
                    }
                    if (StringUtils.isNotBlank((CharSequence) ppParamMap.get(ParamsConstants.BDCDYID_LOWERCASE))) {
                        Map map = new HashMap();
                        map.put(ParamsConstants.BDCDYID_LOWERCASE, ppParamMap.get(ParamsConstants.BDCDYID_LOWERCASE));
                        List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(map);
                        if (CollectionUtils.isNotEmpty(bdcXmList)) {
                            for (BdcXm bdcXm : bdcXmList) {
                                CreatProjectService creatProjectService = projectService.getCreatProjectService(bdcXm);
                                creatProjectService.updateWorkFlow(bdcXm);
                            }
                        }
                    }
                }
            }
        }
        return tdBdcdyh;
    }

    @Override
    public Map checkPic(BdcPpgxLog bdcPpgxLog, String bdclx) {
        Map resultMap = new HashMap();
        if (bdcPpgxLog != null && StringUtils.isNotBlank(bdclx)) {
            String fwproid = bdcPpgxLog.getFwproid();
            String tdproid = bdcPpgxLog.getTdproid();
            String bdcdyh = bdcPpgxLog.getBdcdyh();
            List<BdcPpgxLog> bdcPpgxLogList = null;
            String bdcfwlx = bdcdyService.getBdcdyfwlxBybdcdyh(bdcdyh);
            if (StringUtils.isNotBlank(bdcdyh)) {
                // 验证不动产单元号是否已经匹配
                List<BdcPpgx> bdcPpgxList = bdcPpgxService.getBdcPpgxByBdcdyh(bdcdyh);
                if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                    resultMap.put(ParamsConstants.CHECKMSG_HUMP, "不动产单元号已匹配！");
                    resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                    return resultMap;
                }
                // 验证不动产单元号是否已经创建流程
                List<BdcXm> bdcXmList = bdcXmService.queryBdcxmByBdcdyh(bdcdyh);
                if (CollectionUtils.isNotEmpty(bdcXmList) && !StringUtils.equals(bdcfwlx, Constants.BDCFWLX_DZ_DM)) {
                    resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该不动产单元号已办理过登记！");
                    resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                    return resultMap;
                }
            }
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                // 房地匹配
                if (StringUtils.isNotBlank(tdproid)) {
                    Map queryMap = new HashMap();
                    queryMap.put(ParamsConstants.TDPROID_LOWERCASE, tdproid);
                    bdcPpgxLogList = bdcPpgxLogService.getBdcPpgxLogByMap(queryMap);
                    if (CollectionUtils.isNotEmpty(bdcPpgxLogList)) {
                        Boolean isPpTd = true;
                        for (BdcPpgxLog bdcPpgxLogTemp : bdcPpgxLogList) {
                            if (bdcPpgxLog != null && StringUtils.isNotBlank(bdcPpgxLogTemp.getTdproid()) && !StringUtils.equals(tdproid, bdcPpgxLogTemp.getTdproid())) {
                                isPpTd = false;
                                break;
                            }
                        }
                        if (isPpTd) {
                            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "当前匹配土地证匹配其他房屋，请检查！");
                            if (StringUtils.equals(bdcfwlx, Constants.BDCFWLX_DZ_DM)) {
                                resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                            } else {
                                resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                            }
                        } else {
                            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该房屋已存在匹配,是否替换不动产单元号？");
                            resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                        }
                        return resultMap;
                    }
                } else {
                    // 房屋
                    if (StringUtils.isNotBlank(fwproid)) {
                        Map queryMap = new HashMap();
                        queryMap.put(ParamsConstants.FWPROID_LOWERCASE, fwproid);
                        bdcPpgxLogList = bdcPpgxLogService.getBdcPpgxLogByMap(queryMap);
                        if (CollectionUtils.isNotEmpty(bdcPpgxLogList)) {
                            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该房屋已匹配,是否替换不动产单元号？");
                            resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                            return resultMap;
                        }
                    }
                }
            } else {
                // 土地
                if (StringUtils.isNotBlank(tdproid)) {
                    Map queryMap = new HashMap();
                    queryMap.put(ParamsConstants.TDPROID_LOWERCASE, tdproid);
                    bdcPpgxLogList = bdcPpgxLogService.getBdcPpgxLogByMap(queryMap);
                    if (CollectionUtils.isNotEmpty(bdcPpgxLogList)) {
                        Boolean isPpFw = false;
                        for (BdcPpgxLog bdcPpgxLogTemp : bdcPpgxLogList) {
                            if (bdcPpgxLog != null && StringUtils.isNotBlank(bdcPpgxLog.getFwproid())) {
                                isPpFw = true;
                                break;
                            }
                        }
                        if (isPpFw) {
                            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该土地证已匹配过房屋！");
                            resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_ALERT);
                        } else {
                            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该土地证已匹配,是否替换不动产单元号？");
                            resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                        }
                        return resultMap;
                    }
                }
            }

            /**
             * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
             * @Time 2020/5/5 22:11
             * @description 验证土地证是否抵押查封
             */
            if (StringUtils.isNotBlank(tdproid)) {
                String checkMsg = StringUtils.EMPTY;
                List<QllxParent> qllxList = new ArrayList<QllxParent>();
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(tdproid);
                if (bdcBdcdy != null) {
                    Map<String, Object> ParamMap = new HashMap<String, Object>();
                    ParamMap.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    ParamMap.put("qszt", "1");
                    //抵押
                    qllxList = qllxParentService.queryZtzcQllxVo(new BdcDyaq(), ParamMap);
                    if (CollectionUtils.isNotEmpty(qllxList)) {
                        checkMsg = "该土地证已抵押";
                    }
                    //查封
                    qllxList = qllxParentService.queryZtzcQllxVo(new BdcCf(), ParamMap);
                    if (CollectionUtils.isNotEmpty(qllxList)) {
                        if (StringUtils.isNotBlank(checkMsg)) {
                            checkMsg += "/" + "该土地证已查封";
                        } else {
                            checkMsg = "该土地证已查封";
                        }
                    }
                    if (StringUtils.isNotBlank(checkMsg)) {
                        resultMap.put(ParamsConstants.CHECKMSG_HUMP, checkMsg + ",是否继续匹配？");
                        resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_COMFIRM);
                        return resultMap;
                    }
                }
            }
            resultMap.put(ParamsConstants.CHECKMODEL_HUMP, ParamsConstants.SUCCESS_LOWERCASE);
        }
        return resultMap;
    }

    /**
     * @param bdcPicData
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 匹配单元号
     */
    @Override
    public Map bdcdyMatch(BdcPicData bdcPicData) {
        Map map = Maps.newHashMap();
        try {
            if (bdcPicData != null && CollectionUtils.isNotEmpty(bdcPicData.getData())) {
                for (BdcPicDTO bdcPicDTO : bdcPicData.getData()) {
                    BdcPpgxLog bdcPpgxLog = new BdcPpgxLog();
                    if (StringUtils.isNotBlank(bdcPicDTO.getBdcdyh()) && StringUtils.isNotBlank(bdcPicDTO.getYbdcdyh())) {
                        String proid = bdcdyService.getCqproidByBdcdyh(bdcPicDTO.getYbdcdyh());
                        bdcPpgxLog.setFwproid(proid);
                        bdcPpgxLog.setFwbdcdyh(bdcPicDTO.getYbdcdyh());
                        bdcPpgxLog.setBdcdyh(bdcPicDTO.getBdcdyh());
                        bdcdyMatch(bdcPpgxLog, "0", "TDFW", "0");
                    }
                }
                map.put(ParamsConstants.MSG_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
            }
        } catch (Exception e) {
            map.put(ParamsConstants.MSG_LOWERCASE, e);
        }
        return map;
    }

    @Override
    public Map cxBdcPic(String proid, String userid) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> ppParamMap = new HashMap<>();
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
            List<BdcPpgxLog> bdcPpgxLogList = getBdcPpgxLog(proid, bdcBdcdy.getBdcdyh());
            if (CollectionUtils.isNotEmpty(bdcPpgxLogList)) {
                BdcPpgxLog bdcPpgxLog = bdcPpgxLogList.get(0);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("pplogid", bdcPpgxLog.getLogid());
                List<BdcPpgxXm> bdcPpgxXmList = bdcPpgxXmMapper.getBdcPpgxXmByMap(paramMap);
                if (CollectionUtils.isNotEmpty(bdcPpgxXmList)) {
                    BdcBdcdy cxBdcdy = getCxBdcdy(bdcPpgxLog);
                    for (BdcPpgxXm bdcPpgxXm : bdcPpgxXmList) {
                        cxBdcdyh(bdcPpgxXm.getProid(), cxBdcdy);
                    }


                    ppParamMap.put(ParamsConstants.USERID_HUMP, userid);


                    List<BdcPpgxLog> bdcPpgxLogs = getBdcPpgxLog(proid, "");
                    if (CollectionUtils.isNotEmpty(bdcPpgxLogs) && bdcPpgxLogs.size() > 1) {
                        //匹配多次的  撤销后获取上手的匹配关系
                        List<BdcPpgxLog> ppgxLogList = bdcPpgxLogService.getBdcPpgxLogOrderByPpsj(bdcPpgxLog);
                        if (CollectionUtils.isNotEmpty(ppgxLogList)) {
                            for (BdcPpgxLog ppgxLog : ppgxLogList) {
                                if (!StringUtils.equals(ppgxLog.getBdcdyh(), bdcBdcdy.getBdcdyh())) {
                                    BdcBdcdy ssBdcdy = bdcdyService.queryBdcdyByBdcdyh(ppgxLog.getBdcdyh());
                                    ppParamMap.put(ParamsConstants.BDCDYID_LOWERCASE, ssBdcdy.getBdcdyid());
                                    ppParamMap.put(ParamsConstants.BDCDYH_LOWERCASE, ssBdcdy.getBdcdyh());
                                    ppParamMap.put(ParamsConstants.DJBID_LOWERCASE, ssBdcdy.getDjbid());
                                    ppParamMap.put(ParamsConstants.QJID_LOWERCASE, bdcDjsjService.getDjidByBdcdyh(ssBdcdy.getBdcdyh(), ssBdcdy.getBdclx()));
                                    ppParamMap.put(ParamsConstants.BDCLX_LOWERCASE, ssBdcdy.getBdclx());
                                    //更新权利类型
                                    matchQllx(ppParamMap, ppgxLog.getFwproid(), ppgxLog.getTdproid());
                                    //更新匹配关系数据
                                    bdcPpgxService.saveBdcPpgxByMap(ppParamMap, ppgxLog.getFwproid(), ppgxLog.getTdproid(), ppgxLog.getFwbdcdyh(), ppgxLog.getTdbdcdyh(), Constants.BDC_PP_CZLX_CX, cxBdcdy.getBdcdyh(), "");
                                    break;
                                }
                            }
                        }
                    } else if (CollectionUtils.isNotEmpty(bdcPpgxLogs) && bdcPpgxLogs.size() == 1) {
                        ppParamMap.put(ParamsConstants.BDCDYID_LOWERCASE, cxBdcdy.getBdcdyid());
                        ppParamMap.put(ParamsConstants.BDCDYH_LOWERCASE, cxBdcdy.getBdcdyh());
                        ppParamMap.put(ParamsConstants.DJBID_LOWERCASE, cxBdcdy.getDjbid());
                        ppParamMap.put(ParamsConstants.QJID_LOWERCASE, bdcDjsjService.getDjidByBdcdyh(cxBdcdy.getBdcdyh(), cxBdcdy.getBdclx()));
                        ppParamMap.put(ParamsConstants.BDCLX_LOWERCASE, cxBdcdy.getBdclx());

                        List<BdcPpgx> bdcPpgxList = bdcPpgxService.getBdcPpgxByBdcdyh(bdcPpgxLogs.get(0).getBdcdyh());
                        if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                            BdcPpgx bdcPpgx = bdcPpgxList.get(0);
                            entityMapper.deleteByPrimaryKey(BdcPpgx.class, bdcPpgx.getGxid());
                        }
                        bdcPpgxService.saveBdcPpgxLogByMap(ppParamMap, bdcPpgxLog.getFwproid(), bdcPpgxLog.getTdproid(), bdcPpgxLog.getFwbdcdyh(), bdcPpgxLog.getTdbdcdyh(), Constants.BDC_PP_CZLX_CX);
                    }
                    resultMap.put(ParamsConstants.MSG_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
                }
            }

        }
        return resultMap;
    }

    @Override
    public Map checkCxPic(String proid) {
        Map<String, Object> resultMap = new HashMap<>();
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("bdcdyh", bdcBdcdy.getBdcdyh());
            List<BdcPpgxXm> bdcPpgxXmList = bdcPpgxXmMapper.getBdcPpgxXmByMap(paramMap);
            if (CollectionUtils.isNotEmpty(bdcPpgxXmList)) {
                for (BdcPpgxXm bdcPpgxXm : bdcPpgxXmList) {
                    if (StringUtils.isBlank(bdcPpgxXm.getPplogid())) {
                        resultMap.put(ParamsConstants.CHECKMSG_HUMP, "撤销匹配存在风险，请联系管理员！");
                        resultMap.put(ParamsConstants.CHECKMODEL_HUMP, Constants.CHECKMODEL_ALERT);
                        return resultMap;
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public Object getTdmj(String id, String bdclx) {
        if (StringUtils.isNotBlank(id)) {
            double fttdmj = 0.0;
            double dytdmj = 0.0;
            HashMap<String, Object> resultMap = Maps.newHashMap();
            HashMap<String, Object> paramMap = Maps.newHashMap();
            if (StringUtils.equals(Constants.BDCLX_TDFW, bdclx)) {
                paramMap.put("fw_hs_index", id);
                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(paramMap);
                if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                    DjsjFwHs djsjFwHs = djsjFwHsList.get(0);
                    fttdmj = djsjFwHs.getFttdmj();
                    dytdmj = djsjFwHs.getDytdmj();
                } else {
                    paramMap.clear();
                    paramMap.put("fw_xmxx_index", id);
                    List<DjsjFwXmxx> djsjFwXmxxList = djsjFwService.getDjsjFwXmxx(paramMap);
                    if (CollectionUtils.isNotEmpty(djsjFwXmxxList)) {
                        DjsjFwXmxx djsjFwXmxx = djsjFwXmxxList.get(0);
                        fttdmj = djsjFwXmxx.getFttdmj();
                        dytdmj = djsjFwXmxx.getDytdmj();
                    }
                }
            } else if (StringUtils.equals(Constants.BDCLX_TD, bdclx)) {
                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxx(id);
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    DjsjZdxx djsjZdxx = djsjZdxxList.get(0);
                    dytdmj = djsjZdxx.getFzmj();
                }
            }
            resultMap.put("fttdmj", fttdmj);
            resultMap.put("dytdmj", dytdmj);
            return resultMap;
        }
        return null;
    }

    private void cxBdcdyh(String proid, BdcBdcdy cxBdcdy) {
        if (StringUtils.isNotBlank(proid) && cxBdcdy != null) {
            BdcXm Bdcxm = bdcXmService.getBdcXmByProid(proid);
            if (Bdcxm != null) {
                //撤销匹配不动产单元对应表数据
                for (BdcdyPicUpdateService bdcdyMatchUpdateService : bdcdyMatchUpdateServiceList) {
                    if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(cxBdcdy.getBdcdyid()) && StringUtils.isNotBlank(cxBdcdy.getBdcdyh())) {
                        bdcdyMatchUpdateService.cxMatchBdcdyInfo(proid, cxBdcdy.getBdcdyid(), cxBdcdy.getBdcdyh());
                    }
                }

                Map map = new HashMap();
                map.put(ParamsConstants.BDCDYID_LOWERCASE, cxBdcdy.getBdcdyid());
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(map);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXm : bdcXmList) {
                        CreatProjectService creatProjectService = projectService.getCreatProjectService(bdcXm);
                        creatProjectService.updateWorkFlow(bdcXm);
                    }
                }
            }
        }
    }

    private BdcBdcdy getCxBdcdy(BdcPpgxLog bdcPpgxLog) {
        String bdcdyh = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(bdcPpgxLog.getFwbdcdyh())) {
            bdcdyh = bdcPpgxLog.getFwbdcdyh();
        } else {
            bdcdyh = bdcPpgxLog.getTdbdcdyh();
        }
        return bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
    }

    private List<BdcPpgxLog> getBdcPpgxLog(String proid, String bdcdyh) {
        BdcPpgxLog bdcPpgxLog = null;
        List<BdcPpgxLog> bdcPpgxLogs = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(proid)) {
            paramMap.put("fwproid", proid);
        }
        if (StringUtils.isNotBlank(bdcdyh)) {
            paramMap.put("bdcdyh", bdcdyh);
        }
        bdcPpgxLogs = bdcPpgxLogService.getBdcPpgxLogByMap(paramMap);
        if (CollectionUtils.isEmpty(bdcPpgxLogs)) {
            paramMap.clear();
            if (StringUtils.isNotBlank(proid)) {
                paramMap.put("tdproid", proid);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                paramMap.put("bdcdyh", bdcdyh);
            }
            bdcPpgxLogs = bdcPpgxLogService.getBdcPpgxLogByMap(paramMap);
        }
        return bdcPpgxLogs;
    }

}
