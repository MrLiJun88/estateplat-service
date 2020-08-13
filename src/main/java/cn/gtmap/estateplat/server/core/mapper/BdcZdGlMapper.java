package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表管理mapper
 * Created by lst on 2015/3/30
 */
@Repository
public interface BdcZdGlMapper {

    /**
     * 获取不动产类型字典数据
     *
     * @return
     */
    List<Map> getZdBdclx();


    /**
     * zdd 获取用途字典表
     *
     * @return
     */
    List<Map> getZdYt();

    /**
     * zdd 获取不动产类型权利类型关系表
     *
     * @return
     */
    List<Map> getbdclxQllxRel();

    /**
     * zdd 获取登记类型权利类型关系表
     *
     * @return
     */
    List<Map> getdjlxQllxRel();

    /**
     * zdd 获取登记类型申请类型关系表
     *
     * @return
     */
    List<Map> getDjlxSqlxRel();

    /**
     * 获取申请类型字典数据列表
     *
     * @return
     */
    List<Map> getZdSqlxList();

    /**
     * zdd 获取申请类型权利类型关系表
     *
     * @return
     */
    List<Map> getSqllxQllxRel();

    /**
     * 根据宗地代码获取宗地描述
     *
     * @return
     */
    Map getZdytByDm(String dm);

    /**
     * 根据dm排序获取申请类型
     *
     * @return
     */
    List<BdcZdSqlx> getSqlxOrderbyDm();

    String getSqlxMcByDm(String dm);

    /**
     * 根据申请类型获取原权利类型
     *
     * @return
     */
    String getYqllxBySqlx(@Param("sqlxdm") String sqlxdm);

    /**
     * 获取抵押方式字典表
     *
     * @return
     */
    List<Map> getZdDyfs();

    /**
     * 获取共有方式字典表
     *
     * @return
     */
    List<Map> getZdGyfs();

    /**
     * 获取权利性质字典表
     */
    List<Map> getZdQlxz();

    /**
     * 根据不动产类型和登记类型获取申请类型
     *
     * @return
     */
    List<Map> getSqlxByBdclxDjlx(@Param("bdclxdm") String bdclxdm, @Param("djlxdm") String djlxdm);

    /**
     * 根据不动产类型获取登记类型
     *
     * @return
     */
    List<Map> getDjlxByBdclx(@Param("bdclxdm") String bdclxdm);

    /**
     * 根据登记类型获取申请类型
     *
     * @return
     */
    List<BdcZdSqlx> getSqlxBydjlx(@Param("djlxdm") String djlxdm);

    /**
     * lst获取房屋结构字典表
     *
     * @return
     */
    List<Map> getZdFwjg();

    /**
     * lst获取权属类型字典表
     *
     * @return
     */
    List<Map> getZdQslx();

    /**
     * lst获取定着物特征码字典表
     *
     * @return
     */
    List<Map> getZdDzwtzm();

    /**
     * lst获取宗地特征码字典表
     *
     * @return
     */
    List<Map> getZdtzm();

    List<BdcZdTables> getBdcLimitTableConfigByPage();

    /**
     * 获取过渡房产登记类型对应的不动产申请类型
     *
     * @param djlxDm
     * @return
     */
    List<GdFcDjlxSqlxRel> getGdFcDjlxSqlxRel(String djlxDm);


    /*
     * zwq获取bdclx代码
     *
     * */
    List<String> getBdclxdm(String qllxdm);

    /**
     * 获取地籍数据宗地面积(发证面积)
     */
    Double getZdmj(String djh);

    /**
     * 获取宗地上的房地产权
     */
    List<BdcFdcq> getFdcqs(String djh);

    /*
     * zwq 通过数据库表名获取数据库字段名
     * */
    List<String> getFiledName(String tableName);

    List<HashMap> getGjzwLxZdb(HashMap hashMap);

    List<HashMap> getQlxzZdb(HashMap hashMap);

    List<HashMap> getDzwytZdb(HashMap hashMap);

    List<HashMap> getZdzhytZdb(HashMap hashMap);

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/3/30
     * @param:HashMap
     * @return:List<BdcZdSqlx>
     * @description: 字典写了要可拓展的, 不要定死一个字段
     */
    List<BdcZdSqlx> getBdcSqlxByMap(HashMap hashMap);


    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 查询登记子项
     */
    List<HashMap> getDjzx(HashMap hashMap);

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
    List<HashMap> getDjzxBywdid(HashMap hashMap);

    /**
     * @return
     * @author xiejianan
     * @description 查询登记库中tdfw类型的sqlx代码，和过渡库中的登记类型
     */
    List<HashMap<String, String>> getTDFWSqlxGddjlx();

    /**
     * @param dm 登记事由名称
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 根据代码 查  登记事由名称
     */
    String getDjsyByDm(String dm);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:根据土地用途代码获取土地使用期限
     * @Date 11:13 2017/2/27
     */
    String getTdsyqxByDm(@Param("dldm") String dldm);

    /**
     * @param
     * @return 查封类型字典表 mc
     * @author<a href="mailto:zhoudefu@gtmap.cn">zhoudefu</a>
     * @discription.根据查封类型名称 获取 对应代码
     */
    String getCflxDmByMc(String mc);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn BdcZdQllx
     * @description 获取权利类型字典
     */
    List<BdcZdQllx> getbdcZdQllx(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取用海类型字典
     */
    List<HashMap> getBdcZdYhlx(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取林种类型字典
     */
    List<HashMap> getBdcZdLz(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取房屋性质类型字典
     */
    List<HashMap> getBdcZdFwxz(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取查封类型字典
     */
    List<HashMap> getBdcZdCflx(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取登记类型字典
     */
    List<HashMap> getBdcZdDjlx(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取申请类型字典
     */
    List<HashMap> getBdcZdSqlx(HashMap hashMap);

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 根据proid查询登记子项
     */
    List<HashMap> getDjzxByProid(String proid);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:查询公告类型字典表
     * @Date 22:45 2017/3/7
     */
    List<Map> getZdGglxlist(HashMap hashMap);


    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取房屋类型字典
     */
    List<HashMap> getBdcZdFwlxList(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取登记事由类型字典
     */
    List<HashMap> getBdcZdDjsy(HashMap hashMap);

    /**
     * @param hashMap
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 获取抵押不动产类型类型字典
     */
    List<HashMap> getBdcZdDybdclx(HashMap hashMap);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:获取以"/"组合的登记事由列表
     * @Date 9:43 2017/3/21
     */
    List<Map> getComDjsy();


    String getZjzlByMc(String mc);

    String getDjlxDmByMc(String mc);

    String getSqlxDmByMc(String mc);

    String getQllxDmByMC(String mc);

    String getQllxMcByDm(String dm);

    String getGyfsDmByMc(String mc);

    String getGyfsMcByDm(String dm);

    //根据dm获取宗地用途
    String getZdytMcByDm(String dm);

    //根据dm获取房屋用途
    String getFwytByDm(String dm);

    //根据dm获取不动产类型
    String getBdclxMcByDm(String dm);

    /**
     * @return
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params
     * @description 从申请类型获取登记类型
     */
    String getDjlxBySqlx(final String sqlxdm);

    /**
     * @return
     * @auther <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @params
     * @description 获取归档类型对照表
     */
    List<Map> getBdcZdGdlx();

    /**
     * @param sqlx 申请类型
     * @return 权利类型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据申请类型获取权利类型
     */
    List<String> getQllxBySqlx(String sqlx);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取排序后的bdclx字典表，tdfw放第一个，td放第二个
     */
    List<Map> getZdBdclxOrderBy();

    /**
     *@auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     *@description 根据申请类型获取archiveName
     */
    String getArchiveNameBySqlx(String sqlx);
}
