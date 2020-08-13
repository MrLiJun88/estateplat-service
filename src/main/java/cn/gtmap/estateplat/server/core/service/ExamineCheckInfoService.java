package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.examine.BdcExamineParam;
import cn.gtmap.estateplat.model.server.core.Project;

import java.util.List;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2016/12/2
 * @description
 */
public interface ExamineCheckInfoService {

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param wiid,project
     * @return BdcExamineParam
     * @description 根据wiid或者project获取BdcExamineParam
     */
    List<BdcExamineParam> getBdcExamineParam(String wiid, Project project);

    /**
     * @param wiid 工作流实例Id
     * @return String限制文号(多个以逗号隔开)
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据工作流实例Id获取限制文号
     */
    public String getXzwhByWiid(String wiid);
}
