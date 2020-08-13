package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.BdcXtCheckinfo;
import cn.gtmap.estateplat.model.server.core.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证不动产项目服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-31
 * @description
 */
public interface ProjectCheckInfoService {

    /**
     * @param checkList 验证项
     * @param project   不动产登记项目
     * @return 验证结果
     * @description 根据验证项，验证当前项目是否符合要求
     */
    List<Map<String, Object>> checkXm(List<BdcXtCheckinfo> checkList, Project project);

    /**
     * zdd 根据项目ID  验证项目信息
     *
     * @param proid
     * @return
     */
    List<Map<String, Object>> checkXm(String proid, boolean isEndPorject, String checkModel,String userid,String taskid);

    /**
     * @param project 不动产登记项目
     * @return 验证结果
     * @description 根据不动产登记项目，验证项目是否符合要求
     */
    List<Map<String, Object>> checkXmByProject(Project project);


    /**
     * @param sybl 剩余比例
     * @param bfbl 报废比例
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 验证证书数量是否充足，作废比例过高预警
     */
    Map<String, Object> checkBdcdyZsbhsl(Double sybl, Double bfbl);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 不传project的验证，主要是用于银行网外创建验证抵押权利人和不动产库是否一致
     **/
    List<Map<String, Object>> checkXmByMap(HashMap hashMap);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param project
     * @return
     * @description 根据project获取验证信息
     */
    List<BdcXtCheckinfo> getBdcXtCheckinfoByProject(Project project);

    /**
     * @param  proid
     * @rerutn
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 转发验证comfirm项
    */
    List<Map<String, Object>> checkXmComfirmItems(String proid,String taskid);

}
