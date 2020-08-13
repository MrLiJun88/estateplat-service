package cn.gtmap.estateplat.server.core.service;

import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/12
 * @description 不动产数据分割合并功能
 */
public interface BdcFgHbService {

    /**
     * @param model
     * @param proids
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 初始化分割合并功能页面
     */
    String initBdcFghb(Model model, String proids);

    /**
     * @param wiid 工作流实例ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化查看信息
     */
    String initCkxx(Model model, String wiid);

    /**
     * @param proids
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 验证是否存在项目内多幢
     */
    Map checkExistFdcqDz(String proids);

    /**
     * @param proids
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 合并不动产数据
     */
    Map combineBdcData(String proids, String bdcdyh, String qjid, String userid);

    /**
     * @param proidList
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 通过proids获取合并的字段信息
     */
    Map getCombineDateByProids(List<String> proidList);

    /**
     * @param splitMaps
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 拆分不动产数据
     */
    Map splitBdcData(List<Map> splitMaps, String userid);

    /**
     * @param splitMap
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取拆分数据字段信息
     */
    Map getSplitDate(Map splitMap, String userid);

    /**
     * @param proids
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取合并详细信息
     */
    Map getHbDetailInfo(String proids);

    /**
     * @param proid
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据proid获取产权合并详细信息
     */
    Map getHbCqDetailInfo(String proid, Map dataMap);

    /**
     * @param bdcdyidList
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据bdcdyidlist获取抵押合并详细信息
     */
    Map getHbDyaqDetailInfo(List<String> bdcdyidList);

    /**
     * @param bdcdyidList
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据bdcdyidlist获取查封合并详细信息
     */
    Map getHbCfDetailInfo(List<String> bdcdyidList);

    /**
     * @param bdcdyidList
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据bdcdyidlist获取预告合并详细信息
     */
    Map getHbYgDetailInfo(List<String> bdcdyidList);

    /**
     * @param splitMaps
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取拆分详细信息
     */
    Map getFgDetailInfo(List<Map> splitMaps);

    /**
     * @param splitMaps
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取产权拆分详细信息
     */
    Map getFgCqDetailInfo(List<Map> splitMaps);

    /**
     * @param bdcdyid
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取抵押拆分详细信息
     */
    Map getFgDyaqDetailInfo(String bdcdyid);

    /**
     * @param bdcdyid
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取查封拆分详细信息
     */
    Map getFgCfDetailInfo(String bdcdyid);

    /**
     * @param bdcdyid
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取预告拆分详细信息
     */
    Map getFgYgDetailInfo(String bdcdyid);
}
