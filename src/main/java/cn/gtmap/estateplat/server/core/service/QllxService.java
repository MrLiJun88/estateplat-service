package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-20
 * @description 不动产登记权利信息数据服务
 */
public interface QllxService {

    /**
     * zdd 确定不动产登记所产生的权利类型
     *
     * @param bdcXm
     * @return 权利类型VO
     */
    QllxVo makeSureQllx(final BdcXm bdcXm);

    QllxVo makeSureQllx(ProjectPar projectPar);

    /**
     * hqz 退回还原权利状态
     *
     * @param bdcXm
     */
    void backQllxZt(BdcXm bdcXm);

    /**
     * zdd 根据qllx字典项确定权利类型vo
     *
     * @param qllx
     * @return
     */
    QllxVo makeSureQllx(final String qllx);

    /**
     * zdd 根据bdcXm  自动判断15种类型并读取QllxVo
     *
     * @param bdcXm
     * @return
     */
    QllxVo getQllxVoFromBdcXm(BdcXm bdcXm);

    List<String> getQllxIdByproid(String proid);

    /**
     * zdd 根据bdcXm  自动判断15种类型并读取QllxVo
     *
     * @param bdcXm
     * @param bdcXmRel
     * @param qllxVo
     * @return
     */
    QllxVo getQllxVoFromBdcXm(BdcXm bdcXm, BdcXmRel bdcXmRel, QllxVo qllxVo);

    /**
     * zdd 读取项目基本权利信息
     *
     * @param qllxVo
     * @param bdcXm
     * @return
     */
    QllxVo getQllxParentFrom(QllxVo qllxVo, BdcXm bdcXm);

    /**
     * zdd 从宗地信息中读取建设用地宅基地使用权权利信息
     *
     * @param bdcJsydzjdsyq
     * @param djsjZdxx
     * @return
     */
    BdcJsydzjdsyq getJsydzjdsyqFromZdxx(BdcJsydzjdsyq bdcJsydzjdsyq, DjsjZdxx djsjZdxx);

    /**
     * zdd 从房屋信息中读取房地产权利信息
     *
     * @param bdcFdcq
     * @param djsjFwxx
     * @return
     */
    BdcFdcq getBdcFdcqFromFwxx(BdcFdcq bdcFdcq, DjsjFwxx djsjFwxx, DjsjZdxx djsjZdxx);

    /**
     * @param bdcFdcqDz,djsjFwxx,djsjZdxx
     * @return
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 从房屋信息中读取房地产权多幢权利信息
     */
    BdcFdcqDz getBdcFdcqDzFromFwxx(BdcFdcqDz bdcFdcqDz, DjsjFwxx djsjFwxx, DjsjZdxx djsjZdxx);

    List<QllxParent> queryQllxByProid(String proid);

    /**
     * zdd 根据权利类型以及项目信息删除权利类型
     *
     * @param qllxVo
     * @param proid
     */
    void delQllxByproid(QllxVo qllxVo, String proid);

    /**
     * zdd 根据QllxVo实现类  获取其对应的tablename
     *
     * @param vo
     * @return
     */
    String getTableName(QllxVo vo);

    /**
     * 通过不动产单元号获取匹配的过度预告信息
     *
     * @param bdcdy
     * @return
     */
    List<Map> getGdYgByBdcdyh(String bdcdy);

    /**
     * 通过不动产单元号获取匹配的过度预告信息
     *
     * @param bdcdy
     * @return
     */
    List<GdYg> getGdYgXxByBdcdyh(String bdcdy);

    /**
     * zdd 改变项目对应权利类型的权属状态
     *
     * @param proid
     * @param qszt
     */
    void changeQllxZt(String proid, Integer qszt);

    /**
     * zdd  改变项目对应权利类型的权属状态
     *
     * @param bdcxm
     * @param qszt
     */
    void changeQllxZt(BdcXm bdcxm, Integer qszt);

    /**
     * zdd  改变项目对应权利类型的权属状态
     *
     * @param bdcxm
     * @param qszt
     */
    void changeQllxZt(BdcXm bdcxm, Integer qszt, Boolean ignoreHr);

    /**
     * zdd 根据权利类型确定证书类型
     *
     * @param qllxVo
     * @return BDCQZS_BH_FONT or BDCQZM_BH_FONT
     */
    String makeSureBdcqzlx(QllxVo qllxVo);

    /**
     * zdd 根据权利类型代码以及项目ID  找到权利类型实体类
     * zwq 按bdclx来查询
     * //     * @param qllx  权利类型代码
     * //     * @param proid 项目ID
     * //     * @return 权利类型实体类
     */
    QllxVo queryQllxVo(BdcXm bdcXm);

    /**
     * zdd 根据实体qllxVo 以及项目ID查找
     *
     * @param qllxVo
     * @param proid
     * @return
     */
    QllxVo queryQllxVo(QllxVo qllxVo, String proid);

    /**
     * zdd 办结修改当前权利状态为1
     *
     * @param bdcXm
     */
    void endQllxZt(BdcXm bdcXm);


    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 批量改变当前权利状态
     */
    void batchEndQllxZt(List<BdcXm> bdcXmList);

    /**
     * zdd 根据map条件查询 QllxVo
     *
     * @param qllxVo
     * @param map
     * @return
     */
    List<QllxVo> andEqualQueryQllx(QllxVo qllxVo, Map<String, Object> map);

    /**
     * zdd 根据map条件模糊查询 QllxVo
     *
     * @param qllxVo
     * @param map
     * @return
     */
    List<QllxVo> andLikeQueryQllx(QllxVo qllxVo, Map<String, String> map);

    /**
     * lst 获取QllxVo的所有实现类并根据proid查询
     *
     * @param proid
     * @return
     */
    QllxVo getQllxVoByProid(String proid);

    /**
     * lst 根据权利类型获取登记事由
     *
     * @param qllx
     * @return
     */
    String queryDjsyByQllx(String qllx);

    /**
     * 更新登簿人,读取先后顺序：1、配置的签名人和时间2、读取当前用户（某个特定人）和当前时间
     *
     * @param qllxVo
     * @param userId
     * @param dbsj
     * @return
     */
    QllxVo updateDbr(QllxVo qllxVo, String userId, Date dbsj);

    /**
     * 更新过渡登簿人,读取先后顺序：1、配置的签名人和时间2、读取当前用户（某个特定人）和当前时间
     *
     * @param bdcXm
     * @param userId
     * @param dbrRead
     * @return
     */
    void updateGdDbr(BdcXm bdcXm, String userId, String defaultUserId, String dbrRead);

    /**
     * 更新登簿人,读取先后顺序：1、配置的签名人和时间2、读取某个特定人和当前时间3、读取当前用户和当前时间
     *
     * @param yqllxVo
     * @param userId
     * @param signKey
     * @param proid
     * @return
     */
    QllxVo updateZxDbr(QllxVo yqllxVo, String userId, String defaultUserId, String signKey, String proid);

    /**
     * 更新过渡注销业务登簿人,读取先后顺序：1、配置的签名人和时间2、读取某个特定人和当前时间3、读取当前用户和当前时间
     *
     * @param bdcXm
     * @param userId
     * @param signKey
     * @param proid
     * @return
     */
    void updateGdZxDbr(BdcXm bdcXm, String userId, String defaultUserId, String signKey, String proid);

    /**
     * 获取权利信息列表
     *
     * @param map
     * @return
     */
    List<Map> getQllxListByPage(HashMap map);

    QllxVo gyqkDefutl(QllxVo qllxVo);

    /**
     * 根据项目关系列表，和不动产单元号获取查封信息
     *
     * @param bdcXmRelList
     * @param bdcdyh
     * @return
     */
    List<Map> getCfxxByProid(List<BdcXmRel> bdcXmRelList, String bdcdyh);

    /**
     * 根据不动产单元id获取查封信息
     *
     * @param bdcdyid 不动产单元id
     * @return
     */
    List<Map> getCfxxByBdcdyId(String bdcdyid);

    /**
     * 根据不动产单元id获取抵押信息
     *
     * @param bdcdyid 不动产单元id
     * @return
     */
    List<BdcDyaq> getDyxxByBdcdyId(String bdcdyid);

    /**
     * 根据不动产单元id获取房地产权信息
     *
     * @param bdcdyid 不动产单元id
     * @return
     */
    List<BdcFdcq> getFdcqByBdcdyId(String bdcdyid);

    /**
     * 根据不动产单元id获取房地产权多幢信息
     *
     * @param bdcdyid 不动产单元id
     * @return
     */
    BdcFdcqDz getFdcqDzByBdcdyId(String bdcdyid);

    /**
     * 根据不动产单元id获取宅基地信息
     *
     * @param bdcdyid 不动产单元id
     * @return
     */
    List<BdcJsydzjdsyq> getJsydzjdsyqByBdcdyId(String bdcdyid);


    /**
     * 根据不动产单元id获取抵押信息
     *
     * @param bdcdyid 不动产单元id
     * @return
     */
    List<BdcYy> getYyxxByBdcdyId(String bdcdyid);

    /**
     * 根据不动产单元id获取预告信息
     *
     * @param bdcdyid 不动产单元id
     * @return
     */
    List<BdcYg> getYgxxByBdcdyId(String bdcdyid);

    /**
     * 根据项目关系列表，和不动产单元号获取抵押信息
     *
     * @param bdcXmRelList
     * @param bdcdyh
     * @return
     */
    List<BdcDyaq> getDyaxxByProid(List<BdcXmRel> bdcXmRelList, String bdcdyh);

    List<QllxVo> queryQllx(BdcXm bdcXm);

    /**
     * 根据不动产单元号获取权利信息
     *
     * @param bdcdyh
     * @param qszt
     * @return
     */
    List<Map> getQlxxListByBdcdyh(String bdcdyh, String qszt);

    /**
     * 获取海域使用权信息
     *
     * @param bdcHysyq
     * @param djsjZhxx
     * @return
     */
    BdcHysyq getHysyqFromZhxx(BdcHysyq bdcHysyq, DjsjZhxx djsjZhxx);

    /**
     * zdd 读取过渡数据到权利信息
     *
     * @param yqlid  过渡项目ID
     * @param qllxVo
     * @param bdcXm
     * @return
     */
    QllxVo getQllxVoFromGdxm(String yqlid, QllxVo qllxVo, BdcXm bdcXm);

    /**
     * @param
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @rerutn
     * @description 根据不动产单元号获取权利信息
     */
    List<QllxVo> getQllxByBdcdyh(QllxVo qllxVo, String bdcdyh);

    /**
     * 获取林权使用权信息
     *
     * @param bdcLq
     * @param djsjLqxx
     * @return
     */
    BdcLq getBdcLqFromLqxx(BdcLq bdcLq, DjsjLqxx djsjLqxx);

    /**
     * 获取水产养殖使用权信息
     *
     * @param tdcbnydsyq
     * @param djsjNydDcb
     * @return
     */
    BdcTdcbnydsyq getBdcTtFromTtxx(BdcTdcbnydsyq tdcbnydsyq, DjsjNydDcb djsjNydDcb);

    /**
     * @param bdcdyid
     * @return 抵押信息列表
     * @author xiejianan
     * @description 通过不动产单元第查询登记库和过渡库抵押信息
     */
    List<Map> getDyaxxByBdcdyid(String bdcdyid);

    /**
     * @param
     * @return
     * @author xiejianan
     * @description 根据不动产单元id获取查封信息
     */
    List<Map> getBdcGDCfxxByBdcdyid(String bdcqzh);

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 根据不动产单元号获取过渡权利信息
     */
    HashMap getGdQlZtByBdcdyh(String bdcdyh, HashMap resultMap);

    /**
     * @param qllxVo,proid
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn QllxVo
     * @description 通过外网收件项目初始化权利
     */
    QllxVo initQllxVoFromOntBdcXm(QllxVo qllxVo, String proid);

    /**
     * @author bianwen
     * @description 获取构筑物使用权
     */
    BdcJzwsyq getBdcJzwsyqFromGzw(BdcJzwsyq bdcJzwsyq, DjsjGzwxx djsjGzwxx);

    /**
     * @param qllxVo
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 保存权利信息
     */
    void saveQllxVo(QllxVo qllxVo);

    /**
     * 改原不动产单元首次信息表权利状态
     *
     * @param bdcXm
     * @param qszt
     */
    void changeYbdcdyScxxbQlzt(BdcXm bdcXm, Integer qszt);

    //crj 根据 过度权利类型 找到对应的 不动产权利类型
    String getQllxdmFromGdQl(String yqlid);

    //根据不动产单元id获取zs信息
    HashMap<String, String> getBdcZsByBdcdyid(String bdcdyid);

    /**
     * @param bdcdyh
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据不动产单元号获取权利状态
     */
    HashMap getBdcdyhQlxx(String bdcdyh);

    /**
     * @param bdcdyh
     * @return 返回获取的信息
     * @author xiejianan
     * @description 获取不动产单元的不动产类型，权利人，坐落，权利状态信息
     */
    HashMap getBdcdyhxx(String bdcdyh);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 转移登记、变更登记、更正登记、换证登记不继承交易价格
     */
    void noInheritJyjgByQllxVo(QllxVo qllxVo);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 续查封等获取查封的权利信息
     */
    BdcCf getBdcCfFromCf(BdcCf bdcCf, BdcCf bdcCfFromYxm, List<BdcQlr> bdcQlrList, BdcXm bdcxm, BdcXmRel bdcXmRel);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 通过共有情况名称转为代码
     */
    String changeGyqkMcToDm(String gyqk);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 通过权籍数据初始化BdcTdcbnydsyq
     */
    BdcTdcbnydsyq getBdcTdcbnydsyqFromTdcb(DjsjCbzdDcb cbzdDcb, BdcTdcbnydsyq bdcTdcbnydsyq, List<DjsjCbzdCbf> djsjCbzdCbfList, List<DjsjCbzdFbf> djsjCbzdFbfList);

    /**
     * @auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @description 更新权籍数据至登记系统权利信息表
     */
    void updateQlxxByDjsj(ProjectPar projectPar);

    /**
     * @param bdcdyh 不动产单元号
     * @return 房地产权权利信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取房地产权权利信息
     */
    List<QllxVo> queryFdcqByBdcdyh(String bdcdyh);
}
