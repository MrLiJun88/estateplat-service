package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记权利人
 * Created by lst on 2015/3/17.
 */
public interface BdcQlrService {
    /**
     * zdd 只获取项目的权利人
     *
     * @param proid
     * @return
     */
    List<BdcQlr> queryBdcQlrByProid(final String proid);

    /**
     * zdd 只获取义务人
     *
     * @param proid
     * @return
     */
    List<BdcQlr> queryBdcYwrByProid(final String proid);

    /**
     * zdd 获取项目的所有人员信息   包括权利人 义务人
     *
     * @param proid
     * @return
     */
    List<BdcQlr> queryBdcQlrYwrByProid(final String proid);

    /**
     * zdd 组合权利人名称
     *
     * @param bdcQlrList
     * @return
     */
    String combinationQlr(final List<BdcQlr> bdcQlrList);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:组合义务人名称
     * @Date 15:50 2017/2/15
     */
    String combinationYwr(final List<BdcQlr> bdcYwrList);

    /**
     * lst 录入project信息 权利人
     *
     * @param project
     * @param qlr
     * @return
     */
    BdcQlr getBdcQlrFromProject(final Project project, BdcQlr qlr);


    /**
     * lst 录入地籍房屋信息 权利人
     *
     * @param fwxx
     * @param qlr
     * @return
     */
    BdcQlr getBdcQlrFromFw(final DjsjFwxx fwxx, BdcQlr qlr);

    /**
     * lst录入地籍林权信息 权利人
     *
     * @param lqxx
     * @return
     */
    List<BdcQlr> getBdcQlrFromLq(final DjsjLqxx lqxx, BdcQlr qlr);

    /**
     * lst录入地籍滩涂信息 权利人
     *
     * @param djsjNydDcb
     * @return
     */
    List<BdcQlr> getBdcQlrFromTt(final DjsjNydDcb djsjNydDcb, BdcQlr qlr);

    /**
     * sc录入地籍承包信息 权利人
     *
     * @param cbzdDcb
     * @param qlr
     * @return
     */
    List<BdcQlr> getBdcQlrFromCb(final DjsjCbzdDcb cbzdDcb, BdcQlr qlr);

    List<BdcQlr> getBdcQlrFromQszd(final DjsjQszdDcb djsjQszdDcb, BdcQlr qlr);

    /**
     * lst 录入地籍宗地信息  权利人
     *
     * @param zdxx
     * @param qlr
     * @return
     */
    BdcQlr getBdcQlrFromZd(final DjsjZdxx zdxx, BdcQlr qlr);

    /**
     * zhx 录入地籍宗海信息  权利人
     *
     * @param zhxx
     * @param qlr
     * @return
     */
    BdcQlr getBdcQlrFromZh(final DjsjZhxx zhxx, BdcQlr qlr);

    /**
     * lst 录入土地证信息 权利人
     * sc 多条件查询
     *
     * @param map
     * @return
     */
    List<BdcQlr> queryBdcQlrList(final Map map);

    /**
     * zdd 根据项目ID删除权利人
     *
     * @param proid
     */
    void delBdcQlrByProid(final String proid);

    /**
     * zdd 根据权利人ID（主键）删除
     *
     * @param qlrid
     */
    void delBdcQlrByQlrid(final String qlrid);

    /**
     * zdd 根据主键以及权利人类型 删除权利人信息
     *
     * @param qlrid
     * @param qlrlx Constants.QLRLX_QLR  Constants.QLRLX_YWR
     */
    void delBdcQlrByQlrid(final String qlrid, String qlrlx);


    /**
     * zdd 将原项目的权利人转为义务人
     *
     * @param bdcQlr         原项目权利人信息
     * @param compareBdcqlrs 当前项目所有权利人
     * @param proid          当前项目ID
     * @return 当前项目义务人信息
     */
    BdcQlr qlrTurnProjectYwr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid);

    /**
     * zx 将原项目的权利人转为借款人
     *
     * @param bdcQlr         原项目权利人信息
     * @param compareBdcqlrs 当前项目所有权利人
     * @param proid          当前项目ID
     * @return 当前项目义务人信息
     */
    BdcQlr qlrTurnProjectJkr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid);

    /**
     * zdd 将原项目的权利人转为权利人  同时根据当前项目是否有相同的权利人来确定qlrid 值
     *
     * @param bdcQlr         原项目权利人信息
     * @param compareBdcqlrs 当前项目所有权利人
     * @param proid          当前项目ID
     * @return 当前项目权利人信息
     */
    BdcQlr qlrTurnProjectQlr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid);

    /**
     * zdd  将原项目的人方信息转换为当前项目的人方信息
     *
     * @param bdcQlr
     * @param compareBdcqlrs
     * @param proid
     * @return
     */
    BdcQlr bdcQlrTurnProjectBdcQlr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid);

    /**
     * sc 根据地基号获取农用地使用权
     *
     * @param djh
     * @return
     */
    List<NydQlr> getNydQlrByDjh(final String djh);

    /**
     * sc   根据地基号获取权属宗地使用权
     *
     * @param djh
     * @return
     */
    List<DjsjQszdQlr> getQszdQlrDjh(final String djh);

    /**
     * zx根据过渡信息获取不动产权利人
     *
     * @param ybdcQlrList
     * @param project
     * @return
     */
    List<BdcQlr> getBdcQlrFromGdxx(List<BdcQlr> ybdcQlrList, Project project);

    /**
     * zx根据过渡信息获取不动产义务人
     *
     * @param ybdcYwrList
     * @param project
     * @return
     */
    List<BdcQlr> getBdcYwrFromGdxx(List<BdcQlr> ybdcYwrList, Project project);

    /**
     * zx改变权利人类型
     *
     * @param bdcQlrList
     * @param qlrlx
     * @return
     */
    List<BdcQlr> changeBdcQlrlxFromQlrList(List<BdcQlr> bdcQlrList, String qlrlx);

    /**
     * 將不动产权利人里从过渡数据读取过来的身份证件种类数据转化为字典数据
     *
     * @param bdcQlrList
     * @return
     */
    List<BdcQlr> changeGdqlrYsjToZdsj(List<BdcQlr> bdcQlrList);

    List<String> getGyfsByProid(final String proid);

    /**
     * 获取抵押权人
     *
     * @param ybdcDyQlrList
     * @param project
     * @return
     */
    List<BdcQlr> getBdcDyQlrFromGdxx(List<BdcQlr> ybdcDyQlrList, Project project);

    /**
     * 根据证件类型给权利人性质赋值
     */
    List<BdcQlr> setQlrxzByZjlx(final String proid);

    /**
     * 根据权利人姓名和身份证号来查
     *
     * @param map
     * @return
     */
    List<Map> getBdcdyidByProid(final Map map);

    BdcQlr ppxmQlr(BdcQlr bdcQlr, List<BdcQlr> yqlrList);

    String getGyqk(final String proid);


    /**
     * @param proids 项目ids
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据项目类型数组删除权利人信息
     */
    void delQlrByProids(final String[] proids);

    /**
     * @param qlr 权利人对象
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 插入不动产权利人信息
     */
    void saveDjBdcQlrxx(BdcQlr qlr);

    /**
     * @param qlrsfzjzl
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 把权利人里面的过渡数据转换为字典数据
     */
    String changQlrsfzjzlToDm(final String qlrsfzjzl);

    /**
     * 通过proid获取权利人名称数组
     */
    List<String> getQlrmcByProid(final String proid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利人名称
     */
    String getQlrmcByProid(String proid, String qlrlx);

    List<HashMap> getQlrByXmList(List<BdcXm> bdcXmList);

    void delBdcQlrByProid(final String proid, final String qlrlx);

    /**
     * zdd 只获取借款人
     *
     * @param proid
     * @return
     */
    List<BdcQlr> queryBdcJkrByProid(final String proid);

    /**
     * lj 复制权利人到每一份proid
     *
     * @param bdcQlrList
     * @param proidList
     * @param qlrlx
     */
    void saveQlrsByProidsAndQlrlx(List<BdcQlr> bdcQlrList, List<String> proidList, String qlrlx);

    /**
     * @param proid
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn String
     * @description 通过外网收件项目初始化权利人
     */
    List<BdcQlr> initBdcQlrFromOntQlr(final String proid);

    /**
     * @param bdcQlr
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 保存权利人信息
     */
    void saveBdcQlr(BdcQlr bdcQlr);


    /**
     * 根据不动产单元ID获取活办理的司法裁定的权利人
     *
     * @param bdcdyh
     * @return
     */
    List<BdcQlr> getsfcdQlrByBdcdyh(String bdcdyh);

    /**
     * @author bianwen
     * @description 读取权籍构筑物权利人信息
     */
    List<BdcQlr> getBdcQlrFromGzw(DjsjGzwxx djsjGzwxx, BdcQlr bdcQlr);

    public List<DjsjGzwQlr> getGzwQlrByDcbIndex(final String dcbIndex);

    /**
     * @author zx
     * @description 通过权利人名称和证件号获取权利人
     */
    List<BdcQlr> getBdcQlrByQlrmcZjh(String qlrmc, String zjlx, String zjh);

    /**
     * juyulin 根据主键查找权利人信息
     *
     * @param qlrid
     */
    BdcQlr getBdcQlrByQlrid(final String qlrid);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:根据proid获取义务人的名称和证件号
     * @Date 19:35 2017/3/15
     */
    List<Map> ywrMcAndZjhByProid(String proid);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:根据proid获取权利人的名称和证件号
     * @Date 20:02 2017/3/15
     */
    List<Map> qlrMcAndZjhByProid(String proid);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description
     **/
    List<BdcQlr> getBdcQlrListByBdcqzh(String bdcqzh, String qlrlx);


    /**
     * @autor <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcQlrList
     * @return
     * @description 按份共有中是否包含共有共有
     **/
    Boolean isAfgyContainGtgy(List<BdcQlr> bdcQlrList);

    /**
     * @autor <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 是否为分别持证和不持证共同存在的情况
     **/
    Boolean isFbczContainBcz(List<BdcQlr> bdcQlrList);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量查询不动产权利人
     */
    List<BdcQlr> getBdcQlrListByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量删除不动产权利人
     */
    void batchDelBdcQlrByBdcXmList(List<BdcXm> bdcXmList);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @version
     * @param proid
     * @return
     * @description 根据proid获取现势产权权利人名称
     */
    String getXsQlQlrByProid(String proid,Boolean gdCq);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 根据proid获取持证人
     */
    List<BdcQlr> getBdcCzrListByProid(String proid);

    List<BdcQlr> getBdcQlrByProid(String proid,String qlrlx);

    /**
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @description 对比权利人是否一致
     */
    Boolean checkSameQlr(List<BdcQlr> bdcQlrList, List<BdcQlr> compareQlrList);
}
