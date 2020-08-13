package cn.gtmap.estateplat.server.service;

import org.omg.IOP.ProfileIdHelper;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2018/7/30
 * @description 选择不动产单元管理
 */
public interface SelectBdcdyManageService {

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取选择不动产单元路径
     */
    String getSelectBdcdyPath(String dwdm, Boolean multiselect, Boolean joinselect);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取选择不动产单元model
     */
    void getSelectBdcdyModel(String wiid, String proid, String glbdcdy, String glzs, Model model);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取添加合并项目Model
     */
    String getAddHbXmModel(Model model);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取查询权籍不动产单元数据map
     */
    Map<String, Object> getDjsjBdcdySearchMap(String djh, String bdcdyh, String dcxc, String qlr, String tdzl, String bdclx,
                                              String bdclxdm, String zdtzm, String htbh, String qlxzdm, String bdcdyhs, String exactQuery, String fwbm);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取查询不动产证书数据map
     */
    Map<String, Object> getBdczsListSearchMap(String bdcdyh, String qllx, String bdcqzh, String qlr, String  dyr, String zl, String bdclx, String bdclxdm, String dcxc, String zdtzm,
                                              String qlxzdm, String dyfs, String ysqlxdm, String proid, String bdcdyhs, String fzqssj, String fzjssj, String zstype, String cqzhjc, String exactQuery, String fwbm, String proids);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取查询房产证数据map和查询方法名
     */
    Map<String, Object> getGdfczListSearchMapAndPath(String bdcdyh, String fczh, String qlr, String fwzl, String dcxc, String qllx, String zdtzm, String proid, String cqzhjc, String exactQuery);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取查询土地证数据map和查询方法名
     */
    Map<String, Object> getGdtdzListSearchMapAndPath(String bdcdyh, String tdzh, String qlr, String dcxc, String tdzl, String qllx, String zdtzm, String proid, String cqzhjc, String exactQuery);


    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据qlid获取不动产信息
     */
    Map<String, String> getBdcDateByQlid(String qlid);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据qlid, cqzh获取限制原因
     */
    Map<String, String> getXzyy(String qlid, String cqzh);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取查询过渡查封数据map
     */
    Map<String, Object> getGdcfListSearchMap(String proid, String bdcdyh, String cfwh, String qlr, String dcxc, String yqzh, String fwzl, String tdzl, String exactQuery);

    /**
     * @param bdcdyid
     * @param gdproid
     * @return
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取过渡查封权利现势不动产产权证号
     */
    String getGdCfCqzh(String bdcdyid, String gdproid, String qlid);

    /**
     * @return
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取hhcfModel
     */
    void getShowHhcfModel(Model model, String proid);

    /**
     * @return
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取不动产查询详细信息map
     */
    Map<String, Object> getdataMapByProid(String proid);

    /**
     * @return
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取不动产查封信息查询map
     */
    Map<String, Object> getQlxxListMap(String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm, String qlxzdm, String dyfs, String ysqlxdm, String proid, String bzxr, String cfwh, String cqzhjc, String exactQuery, String fwbm, String proids);

    /**
     * @return
     * @author <a herf="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 获取不动产单元状态
     */
    Map<String, Object> getBdcdyZt(String proid, String djid, String bdclx);

    /**
     * @return
     * @author <a herf="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 获取不动产单元产权权利状态
     */
    Map<String, Object> getBdcdyQlZt(String proid, String djid, String bdclx);

    /**
     * @return
     * @author <a herf="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 获取不动产单元房屋编码获取房屋备案状态
     */
    Map<String, Object> getBdcdyJyZt(String fwbm,String proid);

    /**
     * @return
     * @author <a herf="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 批量创建的时候根据项目id获取原流程所有现势权利信息
     */
    List<Map<String, Object>> getMulBdcqzxx(String yxmids);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据djh获取宗地权利人信息
     */
    Map<String, Object> getZdQlrByDjh(String djh);
}
