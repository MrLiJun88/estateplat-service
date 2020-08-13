package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记查封解封权利服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-12
 */
public interface BdcCfService {

    /**
     * zdd 根据map条件查询  根据orderByClause排序  参数形式例如：lhsx desc
     *
     * @param map
     * @param orderByClause
     * @return
     */
    List<BdcCf> andEqualQueryCf(final Map map, final String orderByClause);

    /**
     * zdd 只更新查封登记的解封信息
     *
     * @param bdcCf
     */
    void updateBdcCfForJfxx(final BdcCf bdcCf);


    BdcCf selectCfByProid(final String proid);


    /**
     * @param bdcdyid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<BdcCf>
     * @description 根据不动产单元id查询cf
     */
    List<BdcCf> queryCfByBdcdyid(final String bdcdyid);


    /**
     *@Author:<a href="mailto:yanzhnekun@gtmap.cn">yanzhenkun</a>
     *@Description:不继承附属设施的情况下处理spxx里面的面积
     */
    void dealNotInheritFsssForCf(String proid,String yproid);


    /**
     * zdd 更新当前不动产单元下面的轮候查封 排在第一位的转为查封  更新其余的LHSX
     * *************** 注意 调用此方法将确认系统数据库中已经没有查封记录了  才会将轮候查封转为查封 ********************
     *
     * @param bdcCf   当前注销的查封记录
     * @param bdcdyid
     */
    void turnLhcfToCf(final BdcCf bdcCf, final String bdcdyid,final BdcXm bdcXm);

    /**
     * @author jiangganzhi
     * @description  判断系统中不存在查封记录时，根据查封权利将轮候查封的记录更新为查封
     */
    void turnLhcfToCfByCfList(List<BdcCf> bdcCfList,List<GdCf> gdCfList,BdcCf bdcCf,BdcXm bdcXm);

    /**
     * @author jiangganzhi
     * @description  根据cfid将过渡轮候查封转为过渡查封
     */
    void turnLhcfToCfByGdCfid(String cfid);

    /**
     * @author jiangganzhi
     * @description  根据cfid将不动产轮候查封转为不动产查封
     */
    void turnLhcfToCfByBdcCfid(String cfid,BdcXm bdcXm,BdcCf bdcCf);

    /**
     * @author jiangganzhi
     * @description  生成轮候查封的解封项目
     */
    void createLhcfJfXm(BdcXm ybdcXm,BdcXm bdcXm,BdcCf bdcLhCf,String proid);

    /**
     * @author jiangganzhi
     * @description  生成轮候查封转查封的不动产查封项目
     */
    void turnLhcfToBdcCf(BdcXm ybdcXm,BdcXm bdcXm,BdcCf bdcLhCf,String proid);

    /**
     * 将续封转为查封
     * @param bdcCf
     * @param bdcdyid
     * @return
     */
    boolean turnXfToCf(final BdcCf bdcCf,final String bdcdyid);


    /*
   * zwq 预查封转查封
    *
   * */
    void ycfChangeCf(BdcXm xbdcXm,BdcCf ybdcCf);

    /*
    * zwq
    * 查询现势预查封
    * */
    List<BdcCf> queryYcfByBdcdyh(final String bdcdyh);

    /**
     * zx 根据bdcdyid获取查封信息
     *
     * @param bdcdyid
     * @return
     */
    List<BdcCf> getCfByBdcdyid(final String bdcdyid);


    /**
     * 获取查封类型名称
     *
     * @return
     */
    List<String> getBdcCflxMc();


    /**
     * 查询查封信息
     *
     * @param map
     * @return
     */
    List<Map> queryBdcCfByPage(final Map map);



    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/4/5
     * @param:
     * @return:null
     * @description:获取现势查封
     */
    List<BdcCf> getXsCfByBdcdyh(final String bdcdyh);

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/4/5
     * @param:
     * @return:null
     * @description:获取查封的登记子项
     */
    String getCfDjzx(Project project, BdcXm ybdcXm);

   /**
    * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
    * @param bdcdyh
    * @rerutn List<BdcCf>
    * @description 根据不动产单元号查询查封项目
    */
    List<BdcXm> getCfXmByBdcdyh(final String bdcdyh);

    /**
     * @author xiejianan
     * @description 根据不动产单元id获取查封信息
     * @param map
     * @return
     */
    List<Map> queryBdcGdCfByPage(final Map map);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param bdcCf
     * @return
     * @description 保存查封信息
     */
    void saveBdcCf(BdcCf bdcCf);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产查封信息页面
     */
    Model initBdcCfForPl(Model model, String qlid, BdcXm bdcXm);

    /**
     * 获取解封业务号
     * @param proid
     * @return
     */
    String createNewJfywh(String proid, String userid);

    /**
     * @author bianwen
     * @description 更新查封顺序
     */
    public void updateCfsx(String cflx,String bdcdyh,String fwid,String tdid);


    /**
     * @author
     * @description  续封解封时处理一系列的查封情况
     */
    void dealXfjf(BdcCf bdcCf,String proid,String username,Date date);

    /**
     * @author
     * @description  处理不动产续封链上所有续封
     */
    void dealBdcXfJf(List<BdcXmRel> allBdcXmRelList,BdcXmRel bdcXmRel,String proid,String jfywh,String jfwh,String jfwj,Date jfsj,String jfjg,String username,Date date);

    /**
     * @author
     * @description  处理过渡续封链上所有续封
     */
    void dealGdXfJf(List<GdLsCfBh> gdLsCfBhList, BdcXmRel bdcXmRel, String proid, String jfywh, String jfwh, String jfwj, Date jfsj, String jfjg, String username, Date date);

    /**
     * @param
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 更新司法裁定解封裁定状态等信息
    */
    void updateAdjudicationState(BdcXm bdcXm,List<BdcXmRel> bdcXmRelList);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcCf
     * @return
     * @description 解封续封时把上一手查封解封掉
     */
    void jfBdcCfByXf(BdcCf bdcCf);

    /**
     * @author jiangganzhi
     * @description  通过不动产单元号和查封文号获取查封权利
     */
    List<BdcCf> getBdcCfByCfwhAndBdcdyh(Map map);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcCf
     * @return
     * @description 不动产查封失效
     */
    void bdcCfsx(BdcCf bdcCf);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gdCf
     * @return
     * @description 过渡查封失效
     */
    void gdCfsx(GdCf gdCf);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取查封登记子项
     */
    String getCfDjzxByProjectPar(ProjectPar projectPar, BdcXm ybdcXm);

    /**
     *@auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     *@description 验证是否是续封
     */
    boolean checkIsXf(BdcXm bdcXm);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 轮候查封转查封
     */
    Map changeLhcfToCf(String proid, String userid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 查询修改查封信息
     */
    Map changeCfxx(String ip, BdcCf bdcCf);
}
