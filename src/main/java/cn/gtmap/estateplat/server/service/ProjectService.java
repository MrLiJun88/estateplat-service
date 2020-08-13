package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;

/**
 * .
 * <p/>
 * 抽象一个项目的完整生命周期  创建   转发   办结   删除
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-16
 */
public interface ProjectService {
    /**
     * zdd 创建项目包含 创建工作流  读取老数据  插入项目数据
     *
     * @param creatProjectService 根据不同的业务注入实现类
     * @param bdcXm
     * @return
     */
    Project creatProjectEvent(CreatProjectService creatProjectService, Xmxx bdcXm);

    /**
     * zdd   转发时  生成权利类型  证书
     *
     * @param turnProjectService
     * @param bdcXm
     * @return
     */
    void turnProjectEvent(TurnProjectService turnProjectService, Xmxx bdcXm);

    /**
     * sc   转发时  权利信息添加登簿人
     *
     * @param userName
     * @param bdcXm
     * @return
     */
    void turnProjectEventDbr(String userName, Xmxx bdcXm);

    /**
     * zdd 办结  改变项目状态
     *
     * @param endProjectService
     * @param bdcXm
     */
    void endProjectEvent(EndProjectService endProjectService, BdcXm bdcXm) throws Exception;

    /**
     * @param  bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 批量办结
     */
    void batchEndProjectEvent(EndProjectService endProjectService, List<BdcXm> bdcXmList) throws Exception;

    /**
     * hqz 登薄现实  只改权利状态
     *
     * @param endProjectService
     * @param bdcXm
     */
    void changeQlztProjectEvent(EndProjectService endProjectService, BdcXm bdcXm) throws Exception;

    /**
     * zdd 项目删除时  还原老数据状态  删除表单信息  处理项目基本信息 删除文件中心 删除平台任务
     *
     * @param delProjectService
     * @param proid
     * @return
     */
    void delProjectEvent(DelProjectService delProjectService, String proid);

    void delProjectEvent(DelProjectService delProjectService, BdcXm bdcXm);

    /**
     * @param bdcXmList 项目信息
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 批量删除项目操作
     */
    void batchDelProjectEvent(DelProjectService delProjectService, List<BdcXm> bdcXmList, String mainProid);

    /**
     * hqz 退回 还原权利状态
     *
     * @param endProjectService
     * @param bdcXm
     */
    void backProjectEvent(EndProjectService endProjectService, BdcXm bdcXm);


    /**
     * zx 验证项目
     *
     * @param proid 项目ID
     * @return 节点ID
     */
    String checkProject(String proid);

    /**
     * zdd 根据项目基本信息获取要执行的CreatProjectService接口的实现类  有待完善
     *
     * @param bdcXm
     * @return
     */
    CreatProjectService getCreatProjectService(BdcXm bdcXm);

    /**
     * @param bdcXm 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 根据项目基本信息获取要执行的CreatProjectService接口的实现类  有待完善(增加权利)
     */
    CreatProjectService getCreatProjectServiceForAddQl(BdcXm bdcXm);

    /**
     * zdd 根据项目基本信息获取要执行的TurnProjectService接口的实现类  有待完善
     *
     * @param bdcXm
     * @return
     */
    TurnProjectService getTurnProjectService(BdcXm bdcXm);

    /**
     * @param bdcXm 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 根据项目基本信息获取要执行的TurnProjectService接口的实现类  有待完善(增加权利)
     */
    TurnProjectService getTurnProjectServiceForAddQl(BdcXm bdcXm);

    /**
     * zdd 根据项目基本信息获取要执行的EndProjectService接口的实现类  有待完善
     *
     * @param bdcXm
     * @return
     */
    EndProjectService getEndProjectService(BdcXm bdcXm);

    /**
     * zdd 根据项目基本信息获取要执行的DelProjectService接口的实现类  有待完善
     *
     * @param bdcXm
     * @return
     */
    DelProjectService getDelProjectService(BdcXm bdcXm);

    /**
     * 生成证书
     *
     * @param turnProjectService
     * @param xmxx
     */
    void creatZs(TurnProjectService turnProjectService, Xmxx xmxx);

    /**
     * 工作流转发结点
     *
     * @param turnProjectService
     * @param project
     */
    Project turnWfActivity(TurnProjectService turnProjectService, Project project);

    /**
     * @param gdproids,过渡项目id集合
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn Project
     * @description 初始化过渡数据
     */
    Project initGdDataToBdcXmRel(Project project,String gdproids,String qlids);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 多对多匹配和项目内多幢时初始化过渡数据
     */
    void initGdMultiPpAndDzToBdcXmRel(Project project,List<BdcXmRel> bdcXmRelList);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 完善过渡bdcXmRel信息
     */
    void completeGdDataToBdcXmRel(Project project,GdBdcQlRel gdBdcQlRel, BdcXmRel bdcXmRel,List<BdcGdDyhRel> gdDyhRelList,String djlxdm,String qllx,String[] qlidsArr,boolean hasppbdcdy);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 批量选择相同不动产单元数据，组织bdcXmRel
     */
    int multiSelectSameBdcdyBdcXmRel(List<BdcXmRel> bdcXmRelList,BdcXmRel bdcXmRel);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 数据重组 去除重复fwid
     */
    void recombineBdcXmRelDate(List<BdcXmRel> bdcXmRelList,List<BdcXmRel> newBbdcXmRelList,boolean hasppbdcdy,String sqlxdm);
    /**
     * @param gdproids,过渡项目id集合
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn Project
     * @description 初始化过渡数据
     */
    Project initGdDataToBdcXmRel(Project project,String gdproids,String qlids,String bdclx);

    /**
     * @param gdproids,过渡项目id集合
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn Project
     * @description 初始化过渡数据（任意组合流程)
     */
    Project initGdDataToBdcXmRelForPl(Project project,String gdproids,String qlids);

    /**
     * @param project
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn Project
     * @description 初始化不动产数据
     */
    Project initBdcDataToBdcXmRel(Project project,String gwc);

    /**
     * zx   登薄不动产项目
     *
     * @param turnProjectService
     * @param bdcXm
     * @return
     */
    void dbBdcXm(TurnProjectService turnProjectService, BdcXm bdcXm);

    /**
     * @param project
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn Project
     * @description 批量界面选择不动产单元初始化Project
     */
    Project initProject(Project project);

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn Project
     * @description 任意流程生成证书的TurnProjectService接口的实现类
     */
    TurnProjectService getTurnProjectServiceArbitrary();

    /**
     * @param  project,qlids
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn Project
     * @description 初始化附属设施
     */
    void initBdcFwfsss(Project project, String qlids);

    /**
     * @param  bdcXm
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn Project
     * @description 初始化附属设施(不动产系统)
     */
    void initBdcFwfsss(BdcXm bdcXm);

    /**
     * @param isScFsss
     * @descriptiond 针对合并登记关联附属设施生成项目的时候，sqlxdm还是合并的，导致生成多余项目，重载getCreatProjectService
     */
    CreatProjectService getCreatProjectService(BdcXm bdcXm,String isScFsss);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @return RegisterProjectService
     * @description 根据项目基本信息获取要执行的RegisterProjectService接口的实现类
     */
    RegisterProjectService getRegisterProjectService(BdcXm bdcXm);


}
