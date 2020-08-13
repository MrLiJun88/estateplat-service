package cn.gtmap.estateplat.server.service.archives;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.model.server.core.BdcGdxx;
import cn.gtmap.estateplat.server.model.BdcCqGdQd;
import cn.gtmap.estateplat.server.model.BdcDyGdQd;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/26
 * @description 昆山归档信息service
 */
public interface BdcArchivesService {
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param model
     * @return
     * @description 昆山归档model处理
     */
    void initGdxxModel(Model model,String userid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param model
     * @return
     * @description 太仓归档model处理
     */
    void initTcGdxxModel(Model model,String userid,String slbh);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 昆山归档信息查询台账
     */
    Page getGdxxJson(String cqzh, String dyzmh, String dah, String slbhs,String gdlx,String cxlx,Pageable pageable,Boolean gdByProidFlag);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @return
     * @description 太仓归档信息查询台账
     */
    Page getTcGdxxJson(String slbhs,Pageable pageable);


    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 昆山归档信息异步获取权利人名称
     */
    String getGdxxQlrMc (String wiid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param xmids,gdlx,userid
     * @return
     * @description 昆山归档
     */
    String bdcGd(String xmids,String gdlx,String userid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param xmids,gdlx,userid
     * @return
     * @description 太仓归档
     */
    String tcBdcGd(String xmids,String gdlx,String userid);


    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 高新区归档
     */
    String bdcArchives(String xmids,String gdlx,String userid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param dah
     * @return
     * @description 根据档案号和归档类型查询归档信息
     */
    List<BdcGdxx> getBdcGdxxByDah(String dah,String gdlx);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过盒号和归档类型查询归档信息
     */
    List<BdcGdxx> getBdcGdxxByHh(String hh, String gdlx);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 异步获取项目流程名称
     */
    String getBdcGdxxLcMc(String wiid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 根据项目id获取bdcGdxx
     */
    List<BdcGdxx> getBdcGdxxByXmid(String xmid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 根据xmid删除归档信息
     */
    String delGdxxByXmids(String xmids);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过xmid获取归档信息档案号和盒号
     */
    Map getDahAndHhByXmid(String xmid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 保存归档信息档案号和盒号
     */
    String saveGdxxDahAndHh(String xmid,String dah, String hh, String yzDah);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 导出产权归档清单
     */
    void exportCqGdQd(List<BdcCqGdQd> list, HttpServletResponse response);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过wiids获取BdcCqGdQd
     */
    List<BdcCqGdQd> getBdcCqGdQdListByWiids(String wiids);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过wiids获取BdcDyGdQd
     */
    List<BdcDyGdQd> getBdcDyGdQdListByWiids(String wiids);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过proids获取BdcCqGdQd
     */
    List<BdcCqGdQd> getBdcCqGdQdListByProids(String proids,String qsNumber,String jsNumber);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过proids获取BdcDyGdQd
     */
    List<BdcDyGdQd> getBdcDyGdQdListByProids(String proids,String qsNumber,String jsNumber);
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 导出抵押归档清单
     */
    void exportDyGdQd(List<BdcDyGdQd> list, HttpServletResponse response);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 整理gdXmlStr
     */
    String getGdXmlStr(String xmids, String gdlx, Boolean gdByProidFlag,String qsNumber,String jsNumber);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 判断编辑档案号是否存在
     */
    String validateExitDah(String dah);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 异步获取归档编号和审批信息
     */
    Map asyncGetBhAndSpxx(String wiid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 获取选择到的归档信息的最大档案号和最小档案号
     */
    Map getQsDahAndJsDah(String gdlx, String xmids);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 获取归档页面上需要展示的信息
     */
    Map getRestOfGdxx(String wiid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 按项目归档组织台账信息
     */
    Map getRestOfGdxxMul(String proid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 根据wiid组织产权证号
     */
    String getBdcCqzhByWiid(String wiid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 根据xmid删除bdcGdxx
     */
    void deleteBdcGdxxByXmid(String xmid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过xmids判断项目是否已经归档
     */
    String validateDahExist(String xmids);
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 删除归档信息重新生成;
     */
    String reBdcArchive(String xmids,String gdlx, String userid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 将wiids转换成wfproids
     */
    String transferWiidsToProids(String wiids);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description
     */
    String bdcGdMulFlow(String xmids,String gdlx,String userid);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param xmid,gdlx,userid
     * @return
     * @description 太仓归档信息处理
     */
    String dealTcGdxx(String xmid,String gdlx,String userid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param gdlx
     * @return
     * @description 太仓归档根据gdlx查询最大案卷号
     */
    Integer getMaxAjhByGdlx(String gdlx);


    /**
     * @param map
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 更新目录号
     */
    void changeMlh(Map map);
}
