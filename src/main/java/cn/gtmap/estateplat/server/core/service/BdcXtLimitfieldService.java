package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXtLimitfield;
import cn.gtmap.estateplat.model.server.core.BdcXtLimitfieldofGd;

import java.util.List;
import java.util.Map;

/**
 * 表单必填项验证
 */
public interface BdcXtLimitfieldService {

    /**
     * @description 获取表单在工作流及节点上的必填字段配置
     * @param wdid 工作流定义ID
     * @param activityDefId 工作流活动定义ID
     * @param cptName 帆软报表名称
     * @return
     */
    List<BdcXtLimitfield> getLimitfield(final String wdid, final String activityDefId, final String cptName);

    /**
     * 获取工作流转发的所有验证信息
     * @param map
     * @return
     */
    List<Map> getWorkflowTransmitValidates(final Map map);

    /**
     * @description 运行验证sql
     * @param sql 验证sql语句
     * @return
     */
    List<Map> runSql(final String sql);

    /**
     * @description 根据验证信息去查看是否有错误信息
     * @param proid 工作流项目ID
     * @param taskid 任务ID
     * @return
     */
    List<Map> validateMsg(final String taskid,final String proid);

    /**
     * @description 获取过渡页面的必填项
     * @param cptName 帆软报表名称
     */
    List<BdcXtLimitfieldofGd> getLimitfieldOfGd(final String cptName);
}
