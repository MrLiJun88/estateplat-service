package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.examine.BdcExamineParam;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.mapper.ExamineCheckInfoMapper;
import cn.gtmap.estateplat.server.core.service.ExamineCheckInfoService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.service.examine.BdcExamineService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2016/12/2
 * @description
 */
@Service
public class ExamineCheckInfoServiceImpl implements ExamineCheckInfoService {

    @Autowired
    private ExamineCheckInfoMapper examineCheckInfoMapper;
    @Autowired
    private BdcExamineService bdcExamineService;

    @Override
    public List<BdcExamineParam> getBdcExamineParam(String wiid, Project project) {
        List<BdcExamineParam> bdcExamineParamList = new ArrayList<BdcExamineParam>();
        getBdcExamineParamFromProject(project, bdcExamineParamList);
        getBdcExamineParamByWiid(wiid, bdcExamineParamList);
        return bdcExamineParamList;
    }

    @Override
    public String getXzwhByWiid(String wiid) {
        String examineInfo = "";
        String lwsqUrl="";
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcExamineParam> bdcExamineParamList = getBdcExamineParam(wiid, null);
            if (CollectionUtils.isNotEmpty(bdcExamineParamList)) {
                Map<String, Object> checkMap = bdcExamineService.performExamine(bdcExamineParamList, wiid);
                if (checkMap != null&&checkMap.get("examineInfo") != null) {
                    examineInfo = checkMap.get("examineInfo").toString();
                }
            }
        }
        if (StringUtils.isNotBlank(examineInfo)) {
            lwsqUrl = "/bdcXzyzLw/addXzyzLw?wiid=" + wiid +"&examineInfo="+examineInfo;
        }
        return lwsqUrl;
    }

    /**
     * @param project,bdcExamineParamList
     * @return List<BdcExamineParam>
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 创建项目参数组织
     */
    private List<BdcExamineParam> getBdcExamineParamFromProject(Project project, List<BdcExamineParam> bdcExamineParamList) {
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 如果有proid则是新建
         */
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            List<String> bdcdyhList = getBdcdyhFromProject(project);
            if (CollectionUtils.isNotEmpty(bdcdyhList)) {
                for (String bdcdyh : bdcdyhList) {
                    BdcExamineParam bdcExamineParam = new BdcExamineParam();
                    bdcExamineParam.setBdcdyh(bdcdyh);
                    bdcExamineParamList.add(bdcExamineParam);
                }
            }
        } else {
            if (project != null && StringUtils.isNotBlank(project.getGdproid())) {
                List<String> paramList = new ArrayList<String>();
                for (String gdProid : StringUtils.split(project.getGdproid(), Constants.SPLIT_STR)) {
                    if (StringUtils.isNotBlank(gdProid) && !paramList.contains(gdProid)) {
                        paramList.add(gdProid);
                    }
                }
                if (CollectionUtils.isNotEmpty(paramList)) {
                    Map<String, Object> args = Maps.newHashMap();
                    args.put("gdproids", paramList);
                    List<Map<String, String>> gdQlidList = examineCheckInfoMapper.queryGdproidByProject(args);
                    if (CollectionUtils.isNotEmpty(gdQlidList)) {
                        BdcExamineParam bdcExamineParam = new BdcExamineParam();
                        List<String> yQlidList = new ArrayList<String>();
                        for (Map<String, String> gdQlid : gdQlidList) {
                            if (gdQlid.get("QLID") != null) {
                                yQlidList.add(gdQlid.get("QLID"));
                            }
                        }
                        bdcExamineParam.setyQlidList(yQlidList);
                        bdcExamineParamList.add(bdcExamineParam);
                    }
                }
            }
        }
        return bdcExamineParamList;
    }

    /**
     * @param wiid，bdcExamineParamList
     * @return List<BdcExamineParam>
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 转发验证参数组织
     */
    private List<BdcExamineParam> getBdcExamineParamByWiid(String wiid, List<BdcExamineParam> bdcExamineParamList) {
        if (StringUtils.isNotBlank(wiid)) {
            Map<String, Object> args = Maps.newHashMap();
            args.put("wiid", wiid);
            List<Map<String, String>> proidMapList = examineCheckInfoMapper.queryYproidByWiid(args);
            if (CollectionUtils.isNotEmpty(proidMapList)) {
                bdcExamineParamList.clear();
                for (Map<String, String> proidMap : proidMapList) {
                    BdcExamineParam bdcExamineParam = new BdcExamineParam();
                    String proid = "";
                    String bdcdyh = "";
                    List<String> yQlidList = new ArrayList<String>();
                    if (proidMap.get("PROID") != null) {
                        proid = proidMap.get("PROID");
                        bdcExamineParam.setProid(proid);
                        args.clear();
                        args.put("proid", proid);
                        List<Map<String, String>> bdcdyhMapList = examineCheckInfoMapper.queryBdcdyhByWiid(args);
                        if (CollectionUtils.isNotEmpty(bdcdyhMapList)) {
                            Map<String, String> bdcdyhMap = bdcdyhMapList.get(0);
                            if (bdcdyhMap.get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                                bdcdyh = bdcdyhMap.get(ParamsConstants.BDCDYH_CAPITAL);
                                bdcExamineParam.setBdcdyh(bdcdyh);
                            }
                        }
                        List<Map<String, String>> yQlidMapList = examineCheckInfoMapper.queryYproidByWiid(args);
                        if (CollectionUtils.isNotEmpty(yQlidMapList)) {
                            for (Map<String, String> yQlidMap : yQlidMapList) {
                                if (yQlidMap.get("YQLID") != null) {
                                    yQlidList.add(yQlidMap.get("YQLID"));
                                }
                            }
                        }
                    }
                    bdcExamineParamList.add(bdcExamineParam);
                }
            }
        }
        return bdcExamineParamList;
    }

    /**
     * @param project
     * @return List<String>
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据project中的与不动产单元号相关的参数获取当前项目的不动产单元号
     */
    private List<String> getBdcdyhFromProject(Project project) {
        List<String> paramList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        //根据djids获取不动产单元号
        if (CollectionUtils.isNotEmpty(project.getDjIds())) {
            for (String djId : project.getDjIds()) {
                for (String tempDjId : StringUtils.split(djId, Constants.SPLIT_STR)) {
                    if (StringUtils.isNotBlank(tempDjId)) {
                        paramList.add(tempDjId);
                    }
                }
            }
            bdcdyhList.addAll(queryBdcdyhListByParam("djids", paramList));
        }
        paramList.clear();
        //根据fwhsindexs获取不动产单元号
        if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
            for (String dcbIndex : project.getDcbIndexs()) {
                for (String tempDcbIndex : StringUtils.split(dcbIndex, Constants.SPLIT_STR)) {
                    if (StringUtils.isNotBlank(tempDcbIndex)) {
                        paramList.add(tempDcbIndex);
                    }
                }
            }
            bdcdyhList.addAll(queryBdcdyhListByParam("fwdcbindexs", paramList));
        }
        paramList.clear();
        if (StringUtils.isNotBlank(project.getDjId())) {
            for (String djId : StringUtils.split(project.getDjId(), Constants.SPLIT_STR)) {
                if (StringUtils.isNotBlank(djId)) {
                    paramList.add(djId);
                }
            }
            bdcdyhList.addAll(queryBdcdyhListByParam("djids", paramList));
        }
        paramList.clear();
        if (StringUtils.isNotBlank(project.getDcbIndex())) {
            for (String dcbIndex : StringUtils.split(project.getDcbIndex(), Constants.SPLIT_STR)) {
                if (StringUtils.isNotBlank(dcbIndex)) {
                    paramList.add(dcbIndex);
                }
            }
            bdcdyhList.addAll(queryBdcdyhListByParam("fwdcbindexs", paramList));
        }
        paramList.clear();
        if (CollectionUtils.isNotEmpty(project.getBdcdyhs())) {
            for (String bdcdyh : project.getBdcdyhs()) {
                for (String tempBdcdyh : StringUtils.split(bdcdyh, Constants.SPLIT_STR)) {
                    if (StringUtils.isNotBlank(tempBdcdyh) && !bdcdyhList.contains(tempBdcdyh)) {
                        bdcdyhList.add(tempBdcdyh);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(project.getBdcdyh())) {
            for (String tempBdcdyh : StringUtils.split(project.getBdcdyh(), Constants.SPLIT_STR)) {
                if (StringUtils.isNotBlank(tempBdcdyh) && !bdcdyhList.contains(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
        }
        return bdcdyhList;
    }

    private List<String> queryBdcdyhListByParam(String param, List<String> paramList) {
        List<String> bdcdyhList = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(paramList)) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put(param, paramList.toArray());
            List<Map<String, String>> tempBdcdyhList = examineCheckInfoMapper.queryFwHsInfoByBdcdyh(paramMap);
            if (CollectionUtils.isNotEmpty(tempBdcdyhList)) {
                for (Map<String, String> resultMap : tempBdcdyhList) {
                    if (!resultMap.isEmpty() && StringUtils.isNotBlank(resultMap.get(ParamsConstants.BDCDYH_CAPITAL)) && !bdcdyhList.contains(resultMap.get(ParamsConstants.BDCDYH_CAPITAL))) {
                        bdcdyhList.add(resultMap.get(ParamsConstants.BDCDYH_CAPITAL));
                    }
                }
            }
        }
        return bdcdyhList;
    }
}
