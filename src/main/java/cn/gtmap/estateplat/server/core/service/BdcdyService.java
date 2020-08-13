package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产单元
 * Created by lst on 2015/3/17.
 */
public interface BdcdyService {
    /**
     * lst 录入project信息 不动产单元
     *
     * @param project
     * @param bdcdy
     * @return
     */
    BdcBdcdy getBdcdyFromProject(final Project project, BdcBdcdy bdcdy);

    /**
     * @param projectPar
     * @param bdcBdcdy
     * @return 不动产单元
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化不动产单元信息
     */
    BdcBdcdy getBdcdyFromProjectPar(ProjectPar projectPar, BdcBdcdy bdcBdcdy);

    /**
     * lst 录入地籍房屋信息 不动产单元
     *
     * @param fwxx
     * @param bdcdy
     * @return
     */
    BdcBdcdy getBdcdyFromFw(DjsjFwxx fwxx, BdcBdcdy bdcdy);

    /**
     * zdd 通过流程id查询不动产单元 @gtsy修改
     *
     * @param wiid
     * @return
     */
    List<BdcBdcdy> queryBdcBdcdy(final String wiid);


    /**
     * 通过项目id查询唯一不动产单元
     *
     * @param proid
     * @return
     */
    BdcBdcdy queryBdcBdcdyByProid(final String proid);

    /**
     * 根据proid 查询唯一不动产单元
     *
     * @param bdcdyid
     * @return
     */
    BdcBdcdy queryBdcdyById(final String bdcdyid);

    /**
     * zdd 根据不动产单元号查找不动产单元
     *
     * @param bdcdyh
     * @return
     */
    BdcBdcdy queryBdcdyByBdcdyh(final String bdcdyh);

    /**
     * zq 查询不动产单元锁定表
     *
     * @param bdcdyid
     * @return
     */
    BdcBdcdySd queryBdcdySdById(final String bdcdyid);

    /**
     * zq 查询不动产单元锁定表LIst
     *
     * @param bdcdyh
     * @return
     */
    List<BdcBdcdySd> queryBdcdySdByBdcdyh(final String bdcdyh);

    /**
     * zdd 删除不动产单元
     *
     * @param bdcdyid
     */
    void delBdcdyById(final String bdcdyid);

    void delBdcdyByBdcdyh(final String bdcdyh);


    /**
     * sc: 根据proid获取宗地号
     *
     * @param proid
     * @return
     */
    String getZhhByProid(final String proid);

    /**
     * 匹配不动产单元和房产证
     *
     * @param map
     * @return
     */
    List<Map> queryBdcdyhByDah(final Map map);

    /**
     * sc：根据proid获取不动产lx
     *
     * @param proid
     * @return
     */
    String getBdclxByPorid(String proid);

    /**
     * 查询不动产单元
     *
     * @param map
     * @return
     */
    List<Map> getDjBdcdyListByPage(final Map map);

    /**
     * 根据不动产单元获取权利类型
     *
     * @param bdcdyh
     * @return
     */
    String getQllxFormBdcdy(final String bdcdyh);

    /**
     * lst 录入yproject信息 不动产单元
     *
     * @param project
     * @param bdcdy
     * @return
     */
    BdcBdcdy getBdcdyFromYProject(Project project, BdcBdcdy bdcdy);

    /**
     * 根据地籍id获取权利人
     *
     * @param djid
     * @return
     */
    String getDjQlrByDjid(final String djid, String bdclxdm, final String zdtzm);

    /**
     * 根据qlr获取所有地籍数据id
     *
     * @param qlr
     * @param bdclx
     * @return
     */
    String[] getDjQlrIdsByQlr(final String qlr, final String bdclx);


    /*
     * zwq
     * 通过房屋的不动产单元号寻找到宗地的不动产单元号
     * */
    String getZdBdcdyh(final String bdcdyh);

    /**
     * zx根据不动产单元号获取不动产单元id
     *
     * @param bdcdyh
     */
    String getBdcdyidByBdcdyh(final String bdcdyh);

    /**
     * hqz根据不动产单元号查询出不动产类型（TD TDFW）
     *
     * @param bdcdyh 不动产单元号
     * @return
     */
    String getBdcdylxByBdcdyh(final String bdcdyh);


    /**
     * 生成不动产单元号
     *
     * @param map
     * @return
     */
    String creatBdcdyh(final Map map);

    /**
     * 创建不动产单元
     *
     * @param djh
     * @param zrzh
     * @return
     */
    String createBdcdy(final String djh, final String zrzh, final String bdclx);

    /**
     * @param
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @rerutn
     * @description 通过宗地宗海号和不动产类型获取不动产单元
     */
    List<BdcBdcdy> getBdcdyByZdzhh(final String zdzhh, final String bdclx);

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/4/8
     * @param:
     * @return:null
     * @description: 根据zdzhh删除bdc_bdcdjb和bdc_td
     */
    void delDjbAndTd(final BdcXm bdcXm);


    /**
     * zx 删除项目时删除不动产单元
     *
     * @param bdcdyid
     */
    void delXmBdcdy(final String bdcdyid);

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 通过proid获取不动产单元号
     */
    String getBdcdyhByProid(final String proid);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 查询不动产单元信息
     */
    HashMap getBdcdyxxById(final String id);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据不权利人查询查询参数
     */
    List<String> getDjhByQlr(final HashMap map);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据不动产单元号查询CQQID
     */
    List<String> getCqqidByBdcdy(final String bdcdyh);


    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 根据bdcdyid或proid查询权利表
     */
    List<HashMap> getBdcQlxxList(HashMap map);

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 根据bdcdyh查询不动产单元房屋类型
     */
    String getBdcdyfwlxBybdcdyh(String bdcdyh);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 判断是否是一单元多房
     */
    boolean sfYdhDfw(String bdcdyh);

    void saveBdcdy(BdcBdcdy bdcdy);

    /**
     * @param wiid
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据wiid获取本实例所有不动产单元id和对应的坐落
     */
    List<HashMap> getBdcdyidAndZlByWiid(final String wiid);

    /**
     * @param wiid
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 过流程id查询不动产单元 过滤不动产附属设施
     */
    List<BdcBdcdy> queryBdcBdcdyFilterBdcFwfsss(final String wiid);


    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 根据bdcXmList获取不动产单元号
     */
    List<String> getBdcdyhByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 根据bdcXmList批量删除不动产单元
     */
    void batchDelBdcBdcdyBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 通过不动产单元号查询不动产类型
     */
    String getTdDjsjBdclxByBdcdyh(final String bdcdyh);

    /**
     * @param map
     * @author: <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据bdclx和zl去找登记库的bdcdyh
     */
    List<BdcBdcdy> getBdcdyInfoByQueryMap(final Map map);

    /**
     * @return
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params
     * @description 根据cqzh去找登记库中bdc_bdczssd表中的数据
     */
    List<BdcBdcZsSd> getBdcBdcZsSdByCqzh(final String cqzh);

    /**
     * @return
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params
     * @description 根据cqzh去找登记库中gd_bdcsd表中的数据
     */
    List<GdBdcSd> getGdBdcSdByCqzh(final String cqzh);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @params bdcdyid
     * @description 判断bdcdyid是否存在其他项目
     */
    Boolean judgeBdcdyidExistsOtherXm(final String bdcdyid, final String wiid);

    /**
     * @param initVoFromParm
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @Description: 初始化登记簿
     */
    BdcBdcdy initBdcdy(final InitVoFromParm initVoFromParm, final BdcBdcdjb bdcdjb, String bdcdyh, String bdclx);

    /**
     * @param fwbm
     * @return
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @Description: 根据fwbm获取不动产单元信息
     */
    List<BdcBdcdy> queryBdcBdcdyByFwbm(String fwbm);

    String getBdcdyh(String djh, int xh);

    /**
     * @param bdcdyh 不动产单元号
     * @return 产权proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取产权proid
     */
    String getCqproidByBdcdyh(String bdcdyh);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/4 16:35
      * @description 根据proid查询bdcdy
      */
    BdcBdcdy getBdcdyByProid(String proid);
}
