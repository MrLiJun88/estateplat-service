package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcSfxm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 收费信息
 */
@Repository
public interface BdcSfxxMapper {
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取收费信息的标准
    *@Date 15:51 2017/4/6
    */
    List<HashMap>  getxtsfbzBySqlx(HashMap map);
    List<HashMap>  getxtsfdwBySqlx(HashMap map);


    List<HashMap> getSfxxMapBySfxxid(HashMap map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param sfzt
     * @param sjrq
     * @rerutn
     * @description 更新收费状态和收缴日期
     */
    public void updateBdcSfxx(@Param("proid") String proid, @Param("sfzt") String sfzt, @Param("sjrq") Date sjrq);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @rerutn
     * @description 获取收费信息
     */
    List<Map> getBdcSfxxListJsonByPage(HashMap<String, Object> map);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @rerutn
     * @description 保存打印临时表
     */
    void insertIdToTemp(Map map);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @rerutn
     * @description 删除打印临时表
     */
    void delPrintBdcSfxxTemp(String uuid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/7/3 10:23
      * @description 根据wiid查询收费项目
      */
    List<BdcSfxm> queryBdcSfxmByWiid(String wiid);
}
