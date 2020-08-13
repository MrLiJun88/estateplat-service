package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记项目
 * Created by lst on 2015/3/17.
 */
public interface BdcXmService {
    /**
     * zdd 创建不动产项目编号
     *
     * @param bdcXm
     * @return 年月日时分秒毫秒+行政代码
     */
    String creatXmbh(BdcXm bdcXm);

    /**
     * zdd 将父类数据转移到子类
     *
     * @param project
     * @return
     */
    BdcXm getBdcXmFromProject(Project project);

    /**
     * zdd 将子类数据转移到父类
     *
     * @param bdcXm
     * @return
     */
    Project getProjectFromBdcXm(BdcXm bdcXm, Project project);

    /**
     * zdd 根据项目ID获取项目信息
     *
     * @param proid
     * @return
     */
    BdcXm getBdcXmByProid(final String proid);

    /**
     * zdd 根据项目ID删除项目信息
     *
     * @param proid
     */
    void delBdcXmByProid(final String proid);

    /**
     * 批量更新bdcXm表中的领证日期
     *
     * @param bdcXmList，date
     * @return
     */
    void batchUpdateBdcXmLzrq(List<BdcXm> bdcXmList, Date date);

    /**
     * zdd 办结不动产项目
     *
     * @param bdcXm
     * @return
     */
    BdcXm endBdcXm(BdcXm bdcXm);

    /**
     * zdd 创建项目时  根据project 创建bdcxm
     *
     * @param project
     * @return
     */
    BdcXm newBdcxm(final Project project);

    /**
     * @param map 查询条件
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据查询条件获取证书分页
     */

    List<Map> getBdcZsByPage(final Map map);

    /**
     * jyl批量页面新增权利，前置条件创建不动产项目
     *
     * @param project
     * @param ybdcXm
     * @param userName
     * @return
     */
    BdcXm creatBdcXm(Project project, BdcXm ybdcXm, String userName);


    /**
     * sc:所有流程创建项目
     *
     * @param proid
     * @param userName
     * @param wdid
     * @param wiid     工作流实例ID
     * @return
     */
    BdcXm creatBdcXm(final String proid, final String userName, final String wdid, final String wiid);

    /**
     * sc: 根据项目ID获取不动产类型
     *
     * @param map
     * @return
     */
    String getXmLx(final Map map);

    /**
     * 根据流程名称获取所有类型
     *
     * @param workFlowName
     * @return
     */
    List<Map> getAllLxByWfName(final String workFlowName);

    /**
     * 根据wdid名称获取所有类型
     *
     * @param wdid
     * @return
     */
    List<Map> getAllLxByWdid(String wdid);

    List<BdcXm> andEqualQueryBdcXm(final HashMap<String, String> map);

    /**
     * sc 根据权利类型获取登记事由
     *
     * @param qllxdm
     * @return
     */
    String getBdcQllxDjsyRel(final String qllxdm);


    BdcXm getDjsyByBdcdyh(BdcXm bdcxm, final String bdcdyh);

    /**
     * 根据项目id获取原项目
     *
     * @param proid
     * @return
     */
    List<BdcXm> getYbdcXmListByProid(final String proid);

    void saveDjsy(final String djsy, final String proid);

    /**
     * 获取不动产项目列表
     *
     * @param map
     * @return
     */
    List<BdcXm> getBdcXmList(final Map map);

    /**
     * 获取不动产项目列表 为了证书关系查看，登记类型和申请类型都取中文 独立出来
     *
     * @param map
     * @return
     */
    List<BdcXm> getBdcXmListForXmRel(final Map map);

    /**
     * 根据不动产单元和申请类型获取不动产项目
     *
     * @param map
     * @return
     */
    List<BdcXm> getBdcXmByBdcdyAndSqlx(final Map map);

    void changeXmzt(final String proid, final String xmzt);


    /**
     * @param bdcXmList
     * @param xmzt
     * @return
     * @auto <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 批量更新项目状态
     **/
    void batchChangeXmzt(List<BdcXm> bdcXmList, String xmzt);


    /**
     * 根据收件编号获取不动产项目
     *
     * @param slbh
     * @return
     */
    List<BdcXm> getBdcXmBySlbh(final String slbh);

    /**
     * @param
     * @return
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 根据收件编号获取平台申请类型代码
     */
    String getPlatfromSqlxByBh(String bh);

    /**
     * 根据过渡ID获取项目id
     *
     * @return
     */
    String getProidByGdproid(final String gdproid);

    /*
     * zwq 根据不动产单元号查询不动产项目
     *
     * */
    List<BdcXm> queryBdcxmByBdcdyh(final String bdcdyh);

    /**
     * 根据proid获取项目关系项目ID
     *
     * @param proid
     * @return
     */
    String getProidsByProid(final String proid);

    /**
     * 根据批量项目编号获取批量项目id
     *
     * @param
     * @return
     */
    List<String> getProidListByWiid(String wiids);

    /**
     * 用proid和qllx获取当前项目所有同qllx的proid
     *
     * @param wiid
     * @param qllx
     * @return
     */
    String getProidsByQllxAndWiid(final String wiid, final String qllx);

    /**
     * 根据proid和bdcdyid获取项目关系项目ID
     *
     * @param proid,bdcdyid
     * @return
     */
    String getProidsByProidAndBdcdyid(final String proid, final String bdcdyid);

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 根据proid获取不动产项目的登记子项
     */
    void getYBdcXmDjzx(final String proid, BdcXm bdcXm);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description
     */
    List<BdcXm> getBdcXmListByWiid(final String wiid);


    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description
     */
    List<BdcXm> getBdcXmListByWiidOrdeBy(BdcXm bdcXm);

    /**
     * @param wiid,bdcdyid
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据wiid和bdcdyid确定不动产项目
     */
    List<BdcXm> getBdcXmListByWiidAndBdcdyid(final String wiid, final String bdcdyid);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description
     */
    List<BdcXm> getBdcXmListByProid(final String proid);

    /**
     * @param
     * @return
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 获取可转换符合控件的xml的本地打印数据的实体类
     */
    DataToPrintXml getAllPrintXxXml(final String proid, final String printType);

    /**
     * @param
     * @return
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 获取可转换符合控件的xml的本地打印数据的实体类
     */
    DataToPrintXml getSqsXxXml(final String proid);

    /**
     * @param
     * @return
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 获取可转换符合控件的xml的本地打印数据的实体类
     */
    DataToPrintXml getSpbXxXml(final String proid);

    /**
     * @param
     * @return
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 获取可转换符合控件的xml的本地打印数据的实体类
     */
    DataToPrintXml getFzjlXxXml(final String proid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 通过原权利id查询不动产项目
     */
    BdcXm getBdcXmByYqlid(final String yqlid);


    /**
     * @param bdcXm
     * @return
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @version
     * @description
     */
    void saveBdcXm(BdcXm bdcXm);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">yanyong</a>
     * @description根据wiid和申请类型获取不动产项目信息
     */
    List<BdcXm> getBdcXmBySqlxAndWiid(Map map);

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 根据proid 获得同流程下抵押权没有现世的所有项目proid
     */
    List<String> getXsDyxmProidsByproid(String proid);

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 用户在建工程抵押转现抵押，用当前proid查询在建工程抵押的项目
     */
    BdcXm getYzjgcXm(String proid);

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据zsid取不动产项目
     */
    List<BdcXm> getBdcXmListByZsid(final String zsid);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:根据不动产单元号获取裁定转移项目proid
     * @Date 16:58 2017/3/15
     */
    List<String> queryCdzyProidByBdcdyh(String bdcdyh);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:根据不动产单元获取裁最后一次裁定解封项目的proid
     * @Date 17:04 2017/3/15
     */
    List<String> queryCdBdcXmByBdcdyh(String bdcdyh);

    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 根据预测不动产单元号获取不动产单元房屋类型
     */
    String getBdcdyfwlxByYcbdcdyh(final String bdcdyh);

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据权利人信息获取不动产项目
     */
    List<BdcXm> getBdcXmByQlr(Map map);

    /**
     * 获取bdc_xm按照bdcdyh排序
     *
     * @return
     */
    List<BdcXm> getBdcXmListOrderByBdcdyh(Map map);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过bdcqzh获取bdc_xm
     **/
    List<BdcXm> getBdcXmByBdcqzh(String bdcqzh);

    /**
     * @param bdcXmList
     * @return
     * @auto <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 批量删除项目
     **/
    void batchDelBdcXm(List<BdcXm> bdcXmList);

    /**
     * 根据qlid获取过渡锁定表中的数据
     *
     * @param colName
     * @param colValue
     * @param sdzt
     */
    List<BdcBdcZsSd> getBdcSd(final String colName, final String colValue, final int sdzt);

    /**
     * 根据proid获取本地打印的申请书的信息
     *
     * @param
     */
    List<HashMap> getSqsxxList(HashMap map);

    /**
     * 根据proid和打印类型获取本地打印的信息
     *
     * @param
     */
    List<HashMap> getPrintxxList(HashMap map);

    /**
     * @author yanzhenkun
     * @description 组织批量sqs信息数据
     */
    public MulDataToPrintXml getAllSqsxxPrintXml(Map<String, String> proidmap);

    /**
     * 根据proid获取本地打印的审批表的信息
     *
     * @param
     */
    List<HashMap> getSpbxxList(HashMap map);

    /**
     * @author yanzhenkun
     * @description 组织批量spb信息数据
     */
    public MulDataToPrintXml getAllSpbxxPrintXml(Map<String, String> proidmap);

    /**
     * @author yanzhenkun
     * @description 组织批量存根信息数据
     */
    public MulDataToPrintXml getAllFzjlxxPrintXml(Map<String, String> proidmap);


    /**
     * 获取获取现势权利proid
     *
     * @param map
     * @return
     */
    List<String> getXsQlProid(HashMap map);

    /**
     * @author jiangganzhi
     * @description 补全证书编号
     */
    String completeXmbh(String proid);

    /**
     * @param bdcdyid
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @description 根据bdcdyid 获得现势产权 proid
     */
    List<String> getXsCqProid(String bdcdyid);

    /**
     * @author jiangganzhi
     * @description 获取原项目相关信息
     */
    BdcXm getYxmInfo(BdcXm bdcXm, BdcXm yBdcXm, Project project);

    /**
     * @author jiangganzhi
     * @description 选择房产证、土地证创建项目读取gd_dy中的抵押方式作为登记子项
     */
    String getDjzxFromGddy(String yqlid);

    /**
     * @author jiangganzhi
     * @description 根据wiid获取bdcXm的领证日期
     */
    Date getLzrqByWiid(String wiid, BdcXm bdcXm);

    /**
     * @author jiangganzhi
     * @description 获取bdcXm的yfczh
     */
    String getBdcXmYfczh(String yxmid, BdcXm bdcXm);

    /**
     * @author jiangganzhi
     * @description 完善处理bdcXm的ybdcqzh
     */
    String completeBdcXmYbdcqzh(Project project, BdcXm bdcXm);

    /**
     * @author jiangganzhi
     * @description 处理bdcXm的djsy
     */
    void dealBdcXmDjsy(Project project, BdcXm bdcXm);

    /**
     * @author jiangganzhi
     * @description 根据不动产单元号获取现势产权proid
     */
    List<String> getXsBdcCqProidByBdcdyh(String bdcdyh);

    /**
     * @author jiangganzhi
     * @description 根据不动产单元号获取现势产权proid
     */
    List<String> getXsGdCqProidByBdcdyh(String bdcdyh);


    /**
     * @return
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params
     * @description 判断是否为合并登记
     */
    Boolean isHb(String proid);

    /**
     * @return
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params
     * @description 限制转让年限相关信息继承到附记
     */
    void inheritXzzrnxInfo(BdcXm bdcXm, Boolean sz);

    /**
     * @param bdcXm
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 受理退回删除权利附记中的限制转让信息
     */
    void delXzzrnxInfoFj(BdcXm bdcXm);

    /**
     * @param bdcXm
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 在没有生成证书预览的情况下限制转让年限相关信息继承到附记
     */
    void inheritXzzrnxInfoForNullPreviewZs(BdcXm bdcXm);

    /**
     * @param bdcXm
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 核定转缮证aop切点
     */
    void zsAopByBdcXm(BdcXm bdcXm);

    /**
     * @param bdcXm
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据裁定相关流程bdcxm查询现势产权proid和cqzh
     */
    Map<String, String> zscdGetXsCqzhAndProidByBdcXm(BdcXm bdcXm);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 通过ProjectPar初始化BdcXm
     */
    BdcXm initBdcXmFromProjectPar(ProjectPar projectPar);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 通过原项目初始化bdcXm
     */
    void initBdcXmFromYbdcXm(BdcXm bdcXm, BdcXm yBdcXm, ProjectPar projectPar);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 处理bdcXm的djsy
     */
    void dealBdcXmDjsyByProjectPar(ProjectPar projectPar, BdcXm bdcXm);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 处理抵押登记类型
     */
    BdcXm getDydjlx(BdcXm bdcxm);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 根据OntBdcXm初始化不动产项目
     */
    BdcXm initBdcXmFromOntBdcXm(BdcXm bdcXm, String proid);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 处理Bdcxm的原不动产权证明号
     */
    String completeBdcXmYbdcqzmh(ProjectPar projectPar, BdcXm bdcXm);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 处理Bdcxm的原不动产权证号
     */
    String completeBdcXmYbdcqzh(ProjectPar projectPar, BdcXm bdcXm);

    /**
     * @param bdcqzh 不动产权证号
     * @param qlr    权利人名称
     * @return proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产权证号、权利人名称获取proid
     */
    List<String> getProid(String bdcqzh, String qlr);

    /**
     * @param proid 项目ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 改变是否收档
     */
    String changeSfsdByProid(String proid);

    /**
     * @param wiid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目信息
     */
    List<BdcXm> getYzdfBdcXm(String wiid);


    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目
     */
    List<BdcXm> getYzdfBdcXm(List<String> proidList);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目
     */
    List<String> getYzdfProids(List<String> proidList);


    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元ID获取项目ID
     */
    String getProidByBdcdyid(String bdcdyid);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 根据bdcdyid获取现势产权proid（房屋优先）
     */
    List<String> getXsCqProidBybdcdyid(String bdcdyid);

    /**
     * @auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 根据wiid获取申请类型名称
     */
    String getSqlxMcByWiid(String wiid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程类型
     */
    String getLclx(String wiid);

    /**
     * @param bdcdyh 不动产单元号
     * @return 现势不动产信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取现势不动产信息
     */
    List<Map> getXsxxByBdcdyh(String bdcdyh);

    /**
     * @param wiid
     * @return 扫描状态
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 根据wiid获取扫描状态
     */
    String getSmztByWiid(String wiid);
}