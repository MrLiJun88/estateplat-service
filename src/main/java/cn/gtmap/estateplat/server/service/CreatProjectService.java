package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;

import java.util.List;

/**
 * .
 * <p/>
 * 创建项目需要完成的事情：创建工作流  读取老数据  生成新数据
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-16
 */
public interface CreatProjectService {
    /**
     * zdd  如果存在工作流业务ID  则创建工作流
     *
     * @param xmxx
     * @return 有值说明工作流创建成功
     */
    Project creatWorkFlow(final Xmxx xmxx);

    /**
     * zdd 创建项目对应的文件中心目录
     *
     * @param proid 项目ID
     * @return 节点ID
     */
    Integer creatProjectNode(final String proid);

    /**
     * zdd 返回新建项目需要初始化的vo, InsertVo必须实现插入本对象的方法，以便后面执行插入
     *
     * @param xmxx
     * @return
     */
    List<InsertVo> initVoFromOldData(final Xmxx xmxx);

    /**
     * zdd 返回新建项目需要初始化的vo, InsertVo必须实现插入本对象的方法，以便后面执行插入
     *
     * @param xmxx
     * @return
     */
    List<InsertVo> initVoFromOldDataMul(final Xmxx xmxx);

    /**
     * zdd 执行InsertVo的插入操作
     *
     * @param list
     * @return
     */
    void insertProjectData(final List<InsertVo> list);


    /**
     * zx 执行InsertVo的插入和保存操作
     *
     * @param dataList
     * @return
     */
    void saveOrUpdateProjectData(final List<InsertVo> dataList);

    /**
     * zx  更新工作流任务
     *
     * @param xmxx
     * @return 更新工作流任务
     */
    void updateWorkFlow(final Xmxx xmxx);

    /**
     * zx 根据过渡期原项目ID或者房产证ID 如果是房产证上的义务人获取原土地证的权利人
     * 如果有原项目信息  则不再读取房产证义务人
     *
     * @param project
     * @return
     */
    List<BdcQlr> getGdYbdcYwrList(final Project project);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @param project
     * @rerutn
     * @description 流程中关联不动产单元
     */
    void glBdcdyh(final Project project);

	/**
	 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
	 * @param bdcXm 不动产项目
	 * @rerutn
	 * @description 复制不动产项目，生成新的项目
	 */
	List<InsertVo> copyBdcxxListFromBdcxm(BdcXm bdcXm);

    /**
     * juyulin 关联项目初始化的vo+ybdxcm
     *
     * @param bdcXm
     * @return
     */
    List<InsertVo> copyBdcxxListFromBdcxm(BdcXm bdcXm,String yproid);
    /**
     * @param xmxx
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 初始化地籍信息
    */
    InitVoFromParm getDjxx(final Xmxx xmxx);

}
