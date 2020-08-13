package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcPpgxLog;
import cn.gtmap.estateplat.server.core.model.vo.BdcPicData;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/25 0025
 * @description 数据匹配服务
 */
public interface BdcPicService {
    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据匹配
     */
    void initBdcPic(Model model, String proids);

    /**
     * @param bdcPpgxLog
     * @param userId
     * @param bdclx
     * @param sfhqqj
     * @return 匹配结果
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 虚拟不动产单元匹配不动产单元
     */
    Map<String,String> bdcdyMatch(BdcPpgxLog bdcPpgxLog, String userId, String bdclx, String sfhqqj);

    /**
     * @param ppParamMap 匹配更新字段参数
     * @param fwproid 房屋项目ID
     * @return fwBdcdyh 房屋虚拟不动产单元号
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 匹配房屋对应不动产单元信息
     */
    String matchFwBdcdy(Map ppParamMap,String fwproid);

    /**
     * @param ppParamMap 匹配更新字段参数
     * @param tdproid 土地项目ID
     * @return tdBdcdyh 土地虚拟不动产单元号
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 匹配土地对应不动产单元信息
     */
    String matchTdBdcdy(Map ppParamMap,String tdproid);

    /**
     * @param bdcPpgxLog 匹配更新字段参数
     * @param bdclx 土地项目ID
     * @return tdBdcdyh 土地虚拟不动产单元号
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 匹配土地对应不动产单元信息
     */
    Map checkPic(BdcPpgxLog bdcPpgxLog,String bdclx);

    /**
     * @param bdcPicData
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 匹配单元号
     */
    Map bdcdyMatch(BdcPicData bdcPicData);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/14 17:11
      * @description 撤销不动产匹配
      */
    Map cxBdcPic(String proid, String userid);

    Map checkCxPic(String proid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/6/3 13:57
      * @description 获取分摊和独用土地面积
      */
    Object getTdmj(String id, String bdclx);
}
