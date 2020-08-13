package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.model.BdcZdGdlx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lst on 2015/3/24
 *
 * @description 不动产登记字典管理服务
 */
public interface BdcZdGlService {

    /**
     * lst 获取不动产登记类型的字典表数据
     *
     * @return
     */
    List<BdcZdDjlx> getBdcDjlx();

    /**
     * sc 获取不动产申请类型的字典表数据
     *
     * @return
     */
    List<BdcZdSqlx> getBdcSqlx();

    /**
     * 根据dm排序获取申请类型
     *
     * @return
     */
    List<BdcZdSqlx> getSqlxOrderbyDm();

    /**
     * sc 获取不动产权属状态类型的字典表数据
     *
     * @return
     */
    List<BdcZdQszt> getBdcZdQszt();

    /**
     * sc 获取不动产fw用途的字典表数据
     *
     * @return
     */
    List<BdcZdFwyt> getBdcZdFwyt();

    /**
     * lst 获取不动产权力类型的字典表数据
     *
     * @return
     */
    List<BdcZdQllx> getBdcQllx();

    /**
     * lst 获取不动产类型的字典表数据
     *
     * @return
     */
    List<Map> getZdBdclx();

    /**
     * @return
     */
    String getBdclxMcByDm(String dm,List<Map> bdclxList);

    /**
     * 初始化方法 获取不动产类型和权力类型的关系数据
     *
     * @return
     */
    List<?> getqllxByDjlxOrBdclx(final String djlxdm,final String bdclxdm);


    /**
     * zdd 获取用途字典表
     *
     * @return
     */
    List<Map> getZdYt();

    /**
     * hqz 根据宗地代码获取宗地描述
     *
     * @return
     */
    Map getZdytByDm(final String dm);

    //根据dm获取宗地用途
    String getZdytMcByDm(String dm);

    //根据dm获取房屋用途
    String getFwytByDm(String dm);

    /**
     * zx 获取根据登记类型DM不动产权力类型的代码
     *
     * @return
     */
    List<String> getBdcQllxDmByDjlx(final String djlxdm);

    /**
     * 获取申请字典数据
     *
     * @return
     */
    List<Map> getZdSqlxList();


    /**
     * 通过调用的方法路径获取对应字典数据
     *
     * @param controllerPath
     * @return
     */
    BdcZdLogController getZdLogControllerByPath(final String controllerPath);

    /**
     * 获取调用方法路径的字典表
     *
     * @param
     * @return
     */
    List<BdcZdLogController> getZdLogController();

    HashMap getZdLogControllerMap();

    /**
     * zdd 获取登记类型申请类型关系表
     *
     * @return
     */
    List<Map> getDjlxSqlxRel();

    /**
     * zx 根据申请类型代码获取登记类型代码
     *
     * @return
     */
    String getDjlxDmBySqlxdm(final String sqlxdm);

    /**
     * hzj 根据地类代码获取土地使用期限
     *
     * @return
     */
    String getTdsyqxByDm(final String dldm);

    List<BdcZdSqlx> getBdcSqlxList();

    /**
     * 根据申请类型获取原权利类型
     *
     * @return
     */
    String getYqllxBySqlx(final String sqlxdm);

    /**
     * 获取抵押方式字典表
     *
     * @return
     */
    List<Map> getZdDyfs();

    /**
     * 根据不动产类型和登记类型获取申请类型
     *
     * @return
     */
    List<Map> getSqlxByBdclxDjlx(final String bdclxdm,final String djlxdm);

    /**
     * 根据不动产类型获取登记类型
     *
     * @return
     */
    List<Map> getDjlxByBdclx(final String bdclxdm);

    /**
     * 根据登记类型获取申请类型代码
     *
     * @return
     */
    List<BdcZdSqlx> getSqlxBydjlx(final String djlxdm);

    /**
     * lst获取房屋结构字典表
     *
     * @return
     */
    List<Map> getZdFwjg();

    /**
     * lst 获取宗地特征码字典表 拼接的数据
     *
     * @return
     */
    List<Map> getZdZdtzm();

    /*
    * zwq 更具代码获取房屋结构名称
    *
    * */
    String getFwjgMc(final String dm);

    /**
     * lst 获取宗地特征码字典表
     *
     * @return
     */
    List<Map> getZdtzm();

    /**
     * lst获取定着物特征码字典表
     *
     * @return
     */
    List<Map> getZdDzwtzm();

    /**
     * lst 根据xml获取workflownode
     *
     * @param xml
     * @return
     */
    List<HashMap> getWorkFlowNodes(final String xml);

    /**
     * lst 获取必填验证的table字典表
     *
     * @return
     */
    List<BdcZdTables> getBdcZdTables();

    /**
     * lst 获取必填验证的table字典表的没有验证的字段
     *
     * @return
     */
    List<HashMap> getFields(final String workFlowId,final String workFlowNodeid, final String tableId, final String cptName);

    /**
     * 获取权利性质字典表
     */
    List<Map> getZdQlxz();

    /**
     * 根据申请类型获取工作流定义id
     *
     * @param sqlxdmList
     * @return
     */
    String getWdidsBySqlxdm(List<String> sqlxdmList);

    List<BdcZdZjlx> getBdcZdZjlx();

    /**
     * 根据证件类型代码获取证件类型
     *
     * @param zjlxdm
     * @return
     */
    String getZjlxMcByDm(String zjlxdm,List<BdcZdZjlx> zjlxList);

    /**
     * 获取地籍数据宗地面积(发证面积)
     */
    Double getZdmj(final String djh);

    /**
     * 获取宗地上的房地产权
     */
    List<BdcFdcq> getFdcqs(final String djh);

    /**
     * 根据申请类型代码的工作流id
     *
     * @param sqlxdm
     * @return
     */
    String getWdidBySqlxdm(final String sqlxdm);

    /**
     * 获取所有的权利状态字典表
     *
     * @return
     */
    List<BdcZdQlzt> getBdcZdQlztList();

    /**
     * 获取权利状态颜色根据权利状态
     *
     * @param qlzt
     * @param bdcZdQlztList
     * @return
     */
    String getQlColorByQlzt(final String qlzt, List<BdcZdQlzt> bdcZdQlztList);

    /**
     * 获取权利状态名称根据权利状态
     *
     * @param qlzt
     * @param bdcZdQlztList
     * @return
     */
    String getQlMCByQlzt(final String qlzt, List<BdcZdQlzt> bdcZdQlztList);

    /**
     * lst 获取不动产房屋类型字典表
     *
     * @return
     */
    List<BdcZdFwlx> getBdcZdFwlx();

    /**
     * 获取过渡房产登记对应的不动产申请类型
     *
     * @param djlxdm
     * @return
     */
    String getGdFcDjlxToSqlxWdid(final String djlxdm);


    /*
    * zwq 将bdc_spxx中的中文换成代码
    * */
    BdcSpxx changeToDm(BdcSpxx bdcSpxx);

    /**
     * zwq  定着物用途的字典表
     */
    List<HashMap> getDzwytZdb(HashMap hashMap);

    /**
     * zwq  宗地宗海用途的字典表
     */
    List<HashMap> getZdzhytZdb(HashMap hashMap);

    /*
    * zwq 性质字典表
    * */
    List<HashMap> getQlxzZdb(HashMap hashMap);

    /*zwq 获取构建筑物类型*/
    List<HashMap> getGjzwLxZdb(HashMap hashMap);

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/3/30
     * @param:HashMap
     * @return:List<BdcZdSqlx>
     * @description: 字典写了要可拓展的, 不要定死一个字段
     */
    List<BdcZdSqlx> getBdcSqlxByMap(HashMap hashMap);
    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param
     * @rerutn
     * @description 根据工作流定义ID获取不动产申请类型代码
     */
    String getBdcSqlxdmByWdid(final String wdid);

    /**
     * @param
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @rerutn
     * @description 根据结点名称获取结点定义ID
     */
    String getWorkflowNodeId(final String nodeName,final String wdid) ;
    /**
     * @param
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @rerutn
     * @description 根据结点名称获取结点定义ID
     */
    String getWorkflowNodeName(final String nodeName,final String wdid) ;

    /**
     *@author <a herf="mailto:lichaolong@gtmap.cn">lichaolong</a>
     *@version V1.0,
     *@param
     *@return
     *@description 获取收费配置项
    */
    List<BdcXtSfxm> getBdcSfdMrz(final String sfxmmc,final String sfxmbz);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 查询登记子项
     */
    List<HashMap>getDjzx(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 查询抵押方式
     */
    List<HashMap> getDyfs(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @rerutn
     * @description wdid查询登记子项
     */
    List<HashMap>getDjzxBywdid(HashMap hashMap);

	/**
     * @author xiejianan
     * @description 获取过度苦衷的登记类型做下拉框用
     * @return 返回登记类型dm和名称列表
     */
    List<HashMap<String,String>> getTDFWSqlxGddjlx();

    /**
     * @param
     * @return 查封类型字典表 mc
     * @author<a href="mailto:zhoudefu@gtmap.cn">zhoudefu</a>
     * @discription.根据查封类型名称 获取 对应代码
     */
    String getCflxDmByMc(String mc);

    /**
     * @param
     * @return 申请类型字典表 mc
     * @author<a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @discription.根据申请类型名称 获取 对应代码
     */
    String getSqlxMcByDm(String dm);

    /**
     * @author bianwen
     * @description spxx表中代码转换为名称
     */
    public BdcSpxx changeDmToMc(BdcSpxx bdcSpxx);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn  BdcZdQllx
     * @description 获取权利类型字典
     */
    List<BdcZdQllx> getBdcZdQllx(HashMap hashMap);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取用海类型字典
     */
    List<HashMap> getBdcZdYhlx(HashMap hashMap);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取林种类型字典
     */
    List<HashMap> getBdcZdLz(HashMap hashMap);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取房屋性质类型字典
     */
    List<HashMap> getBdcZdFwxz(HashMap hashMap);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取查封类型字典
     */
    List<HashMap> getBdcZdCflx(HashMap hashMap);

    /**
     * @param dm 登记事由名称
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 根据代码 查  登记事由名称
     */
    String getDjsyByDm(String dm);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取登记类型字典
     */
    List<HashMap> getBdcZdDjlx(HashMap hashMap);
    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取申请类型字典
     */
    List<HashMap> getBdcZdSqlx(HashMap hashMap);

    /** * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @param
     * @rerutn
     * @description     根据proid查询登记子项
     */
    List<HashMap> getDjzxByProid(String proid);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取公告类型字典
    *@Date 21:04 2017/3/7
    */
    List<Map> getZdGglxlist(HashMap hashMap);


    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取房屋类型字典
     */
    List<HashMap> getBdcZdFwlxList(HashMap hashMap);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取登记事由类型字典
     */
    List<HashMap> getBdcZdDjsy(HashMap hashMap);


    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param hashMap
     * @rerutn
     * @description 获取抵押不动产类型类型字典
     */
    List<HashMap> getBdcZdDybdclx(HashMap hashMap);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取以"/"组合的登记事由列表
    *@Date 9:41 2017/3/21
    */
    List<Map> getComDjsy();

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 获取不动产查封类型字典表数据
     */
    List<BdcZdCflx> getBdcCflx();

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 获取流程对应的申请类型代码
     */
    String getWorkFlowSqlxdm(String proid);


    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过名称获取相对应的证件类型
     **/
    String getZjzlByMc(String mc);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过登记类型获取相应的dm
     **/
    String getDjlxDmByMc(String mc);


    /**
     * @param
     * @return
     * @auto
     * @description 通过登记类型代码获取名称
    **/
    String getDjlxMcByDm(String dm,List<HashMap> bdcdjlxList);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过sqlx获取相应的dm
     **/
    String getSqlxDmByMc(String mc);


    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过权利类型获取相应的dm
     **/
    String getQllxDmByMC(String mc);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过权利类型获取相应的mc
     **/
    String getQllxMcByDm(String dm);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过共有方式的名称获取dm
     **/
    String getGyfsDmByMc(String mc);

    /**
     * @param
     * @return
     * @auto <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description 通过共有方式的dm获取名称
     **/
    String getGyfsMcByDm(String dm);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 获取贷款方式名称
     **/
    String getDkfsDmByMc(String mc);

    /**
     * @param
     * @return
     * @auto
     * @description 通过代码获取房屋用途的名称
     **/
    String getFwytMcByDm(String dm,List<BdcZdFwyt> fwytList);

    /**
     * @param
     * @return
     * @auto
     * @description 通过代码获取宗地宗海用途名称
     **/
    String getZdzhytMcByDm(String dm,List<HashMap> bdcZdzhytList);

    /**
     * @param
     * @return
     * @auto
     * @description 通过代码获取宗地宗海权利性质代码
     **/
    String getZdzhqlxzMcByDm(String dm,List<HashMap> bdcZdzhQlxzList);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 从申请类型获取登记类型
     */
    String getDjlxBySqlx(final String sqlxdm);

    /**
     *@auther <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     *@params
     *@return
     *@description 获取归档类型对照表
     */
    List<Map> getBdcZdGdlx();


    /**
     * @param sqlx 申请类型
     * @return 权利类型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据申请类型获取权利类型
     */
    String getQllxBySqlx(String sqlx);

     /**
     *@auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     *@description 根据申请类型获取archiveName
     */
    String getArchiveNameBySqlx(String sqlx);
}