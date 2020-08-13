package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjbQlPro;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记项目
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-12
 */
@Repository
public interface BdcXmMapper {
    /**
     * 根据项目id数组删除项目
     *
     * @param proids
     */
    void delXmByIds(String[] proids);

    /**
     * 插入项目信息
     *
     * @param xm
     */
    void saveBdcXm(BdcXm xm);

    /**
     * zdd 根据项目ID获取项目信息
     *
     * @param proid
     * @return
     */
    BdcXm getBdcXmByProid(String proid);

    /**
     * 通过bhList获取proidList
     *
     * @param map
     * @return
     */
    List<String> getProidListByWiidList(HashMap map);

    /**
     * 通过proid获取djsj
     *
     * @param proid
     * @return
     */
    String getDjsjByProid(String proid);

    /**
     * sc: 根据项目ID获取不动产类型
     *
     * @param map
     * @return
     */
    String getXmLx(Map map);

    List<DjbQlPro> getBdcdyByDyh(Map map);

    List<Map> getBdcZsByPage(final Map map);

    /**
     * 根据流程名称获取所有类型
     *
     * @param workFlowName
     * @return
     */
    List<Map> getAllLxByWfName(String workFlowName);

    /**
     * 根据流程ID获取所有类型
     *
     * @param wdid
     * @return
     */
    public List<Map> getAllLxByWdid(String wdid);

    /**
     * sc 根据权利类型获取登记事由
     *
     * @param qllxdm
     * @return
     */
    String getBdcQllxDjsyRel(String qllxdm);

    /**
     * sc: 根据申请类型获取权利类型
     *
     * @param sqlxdm
     * @return
     */
    String getqllxBysqlx(String sqlxdm);

    /**
     * 通过地籍号获取权属性质
     */
    List<String> getQsxzByDjh(String djh);

    /**
     * 根据登记事由名称获取代码
     *
     * @param mc
     * @return
     */
    String getDjsyDmByMc(String mc);

    String getDjsyZsmcByDm(String dm);

    /**
     * 根据proid获取项目登记事由汉字描述
     *
     * @param proid
     * @return
     */
    String getDjsyMcByProid(String proid);

    /**
     * 获取不动产项目列表
     *
     * @param map
     * @return
     */
    List<BdcXm> getBdcXmList(Map map);

    /**
     * @param bh
     * @return bdcXmList
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @description
     */
    List<BdcXm> getBdcXmListByBh(String bh);

    /**
     * 获取不动产项目列表 为了证书关系查看，登记类型和申请类型都取中文 独立出来
     *
     * @param map
     * @return
     */
    List<BdcXm> getBdcXmListForXmRel(Map map);


    /**
     * 根据不动产单元和申请类型获取不动产项目
     *
     * @param map
     * @return
     */
    List<BdcXm> getBdcXmByBdcdyAndSqlx(Map map);

    /**
     * 根据不动产单元获取权利
     *
     * @param bdcdyh
     * @return
     */
    List<Map> getQlByBdcdyh(String bdcdyh);

    /**
     * 根据过渡ID获取项目id
     *
     * @param gdproid
     * @return
     */
    List<String> getProidByGdproid(String gdproid);

    /**
     * 根据收件编号获取不动产项目
     *
     * @param slbh
     * @return
     */
    List<BdcXm> getBdcXmBySlbh(@Param(value = "slbh") String slbh);

    /*
     * zwq 通过bdcdyh查询不动产项目
     * */
    List<BdcXm> queryBdcXmByBdcdyh(String bdcdyh);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 通过原权利id查询不动产项目
     */
    BdcXm queryBdcXmByYqlid(@Param(value = "yqlid") String yqlid);

    /**
     * @param param 受理时间段
     * @return 返回下一个受理流水号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/31
     * @description
     */
    Integer getMaxSllsh(Map param);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">yanyong</a>
     * @description根据wiid和申请类型获取不动产项目信息
     */
    List<BdcXm> getBdcXmBySqlxAndWiid(Map map);

    /**
     * @param proid
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
     * @Date 17:24 2017/3/15
     */
    List<String> queryCdzyProidByBdcdyh(Map map);

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:根据不动产单元获取裁最后一次裁定解封项目的proid
     * @Date 17:37 2017/3/15
     */
    List<String> queryCdBdcXmByBdcdyh(Map map);


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
     * @param map
     * @return
     */
    List<BdcXm> getBdcXmListOrderByBdcdyh(Map map);

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据zsid取不动产项目
     */
    List<BdcXm> getBdcXmListByBdcZs(HashMap hashMap);

    /**
     * @param map
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 批量更新项目状态
     */
    void batchChangeXmzt(Map map);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 批量删除项目
     */
    void batchDelBdcXm(List<BdcXm> bdcXmList);

    /**
     * @param
     * @author
     * @rerutn
     * @description 通过proidlsit获取需要打印的申请书信息
     */
    List<HashMap> getSqsxxList(HashMap map);

    /**
     * @param
     * @author
     * @rerutn
     * @description 通过proidlsit获取需要打印的审批表信息
     */
    List<HashMap> getSpbxxList(HashMap map);

    /**
     * @param
     * @author
     * @rerutn
     * @description 通过proidlsit获取需要打印的存根信息
     */
    List<HashMap> getFzjlxxList(HashMap map);

    /**
     * 获取获取现势权利proid
     *
     * @param map
     * @return
     */
    List<String> getXsQlProid(HashMap map);

    /**
     * @param
     * @return
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @description根据bdcdyid获得现势产权proid
     */
    List<String> getXsCqProid(HashMap map);

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
     * @author jiangganzhi
     * @description 通过proidlist获取需要打印的存根信息
     */
    List<HashMap> getFzjlZsxxList(HashMap map);

    /**
     * @param bdcqzh 不动产权证号
     * @return proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产权证号 获取proid
     */
    List<String> getProidByBdcqzh(String bdcqzh);

    /**
     * @param wiid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目信息
     */
    List<BdcXm> getYzdfBdcXmXx(String wiid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目
     */
    List<BdcXm> getYzdfBdcXm(Map map);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元ID获取项目ID
     */
    List<String> getProidByBdcdyid(String bdcdyid);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 根据bdcdyid获取现势产权proid（房屋优先）
     */
    List<String> getXsCqProidBybdcdyid(String bdcdyid);

    List<BdcXm> getBdcXmListByWiidOrdeBy(Map map);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程类型
     */
    List<String> getSqlx(String wiid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取项目数量
     */
    Integer getXmCount(String wiid);

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

