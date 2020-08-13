package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZjjzwxx;
import cn.gtmap.estateplat.server.web.query.HaBdcxmContorller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产在建建筑物信息服务
 */
public interface BdcZjjzwxxService {

    /**
     * @param proId  项目id
     * @param xh     序号
     * @param bdcdyh 不动产单元号
     * @return 建建筑物信息
     * @author hqz
     * 抵押物清单选择从地籍库中读取信息并导入在建建筑物信息表中
     */
    BdcZjjzwxx createZjjzwxx(final String proId, final String xh, final String bdcdyh);


    /**
     * 获取在建建筑物信息清单
     *
     * @param map 查询条件
     * @return 在建建筑物清单
     */
    List<BdcZjjzwxx> getZjjzwxx(Map map);

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 获得与proid同流程里的再见建筑物清单
     */
    List<BdcZjjzwxx> getSameWFZjjzwxx(String proid);

    /**
     * @param bdcdyh 不动产单元号
     * @return 处于现势抵押状态的不动产单元数量
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据不动产单元号查询是否有现势的在建工程抵押
     */
    int getDyBdcZjjzwxxByBdcdyh(final String bdcdyh);

    /**
     * @author bianwen
     * @description 改变抵押物清单的抵押状态
     */
    public void changeZjjzwxxDyzt(BdcXm bdcXm, String ydyzt, String dyzt);

    /**
     * @author bianwen
     * @description 删除抵押物清单
     */
    public void deleteZjjzwxx(BdcXm bdcXm);

    /**
     * 原土地抵押存在，申请在建工程抵押，并对原先的土地抵押进行注销。
     *
     * @param
     */
    public void cancelTdDyOfZjgcdy(String fwbdcdyh);

    /**
     * 删除或者退回，还原之前注销的与土地抵押
     * @param bdcXm
     */
    public void reverseCanceledTdDyOfZjgcdy(BdcXm bdcXm);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 更新抵押物清单
     */
    public void updateZjjzwDyzt(Map map);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 15:12
      * @description 根据proid查询BdcZjjzwxx
      */
    BdcZjjzwxx getBdcZjjzwxxByProid(String proid);
}
