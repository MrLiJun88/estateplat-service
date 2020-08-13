package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.HashMap;
import java.util.List;

/**
 * @description 不动产登记项目审批信息
 * Created by lst on 2015/3/17.
 */
public interface BdcSpxxService {

    /**
     * zdd 根据项目ID删除审批信息
     *
     * @param proid
     */
    void delBdcSpxxByProid(final String proid);

    /**
     * zdd 根据项目ID查找项目
     *
     * @param proid
     * @return
     */
    BdcSpxx queryBdcSpxxByProid(final String proid);

    /**
     * zdd 读取project信息到审批信息
     *
     * @param project
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromProject(final Project project, BdcSpxx bdcSpxx);

    /**
     * zdd 读取宗地信息到审批信息
     *
     * @param zdxx
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromZd(final DjsjZdxx zdxx, BdcSpxx bdcSpxx);


    /**
     * zdd 读取房屋数据到审批信息
     *
     * @param fwxx
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromFw(final DjsjFwxx fwxx, BdcSpxx bdcSpxx, Project project);

    /**
     * zdd 读取林权信息到审批信息
     *
     * @param lqxx
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromLq(final DjsjLqxx lqxx, BdcSpxx bdcSpxx);

    /**
     * zdd 读取滩涂信息到审批信息
     *
     * @param nydDcb
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromNyd(final DjsjNydDcb nydDcb, BdcSpxx bdcSpxx);

    /**
     * zdd 读取宗海信息到审批信息
     *
     * @param zhxx
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromZh(final DjsjZhxx zhxx, BdcSpxx bdcSpxx);

    /**
     * zdd 读取农用地信息到审批表
     *
     * @param djsjNydDcb
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromTdcb(final DjsjNydDcb djsjNydDcb, BdcSpxx bdcSpxx);

    /**
     * zdd 读取土地所有权信息到审批表
     *
     * @param bdcSpxx
     * @param djsjQszdDcb
     * @return
     */
    BdcSpxx getBdcSpxxFromTdSyq(final DjsjQszdDcb djsjQszdDcb, BdcSpxx bdcSpxx);

    /**
     * zdd 读取原项目信息到审批信息
     *
     * @param project
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromYProject(final Project project, BdcSpxx bdcSpxx);

    /**
     * zdd 读取过渡信息到审批信息
     *
     * @param gdProid 根据过渡项目ID(gdProid)查找过渡数据
     * @param xmly    项目来源确定读取哪一类数据（房屋审批数据、土地审批数据等）
     * @param bdcSpxx
     * @return
     */
    BdcSpxx getBdcSpxxFromGdxm(final String gdProid,final String yqlid, final String xmly, BdcSpxx bdcSpxx);

    /*
    * zwq 从预告信息中获取不动产单元情况，这个是为了商品房买卖转移流程
    * */
    BdcSpxx getBdcSpxxFromYg(final String bdcdyh, BdcSpxx bdcSpxx);
    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param
     * @rerutn
     * @description 根据不动产单元号获取审批信息
     */
    BdcSpxx getBdcSpxxByBdcdyh(final String bdcdyh);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">jyl</a>
     * @version
     * @param bdcSpxx
     * @return
     * @description
     */
    void saveBdcSpxx(BdcSpxx bdcSpxx);
    
    /**
     * @author <a href="mailto:chenjia@gtmap.cn">cj</a>
     * @version
     * @param wiid
     * @return
     * @description 根据wiid获取bdc_spxx
     */
    public List<BdcSpxx> getBdcSpxxByWiid(String wiid);

    /**
     * @author bianwen
     * @description  获取构筑物信息到审批表
     */
    BdcSpxx getBdcSpxxFromGzw(final DjsjGzwxx gzwxx, BdcSpxx bdcSpxx);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量删除审批信息
     */
    void batchDelBdcSpxx(List<BdcXm> bdcXmList);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @version
     * @param
     * @return
     * @description 获取zdzhmj
     */
    Double getZdzhmjByQlid(String qlid,String dqzd,String bdclx);

    /**
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @version
     * @param
     * @return
     * @description 获取zdzhmj
     */
    Double getZdzhmj(String djh,String bdcdyh,String bdclx,List<BdcXmRel> bdcXmRelList);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXm
     * @return
     * @description 处理审批信息宗地宗海面积取值问题
     */
    void dealWithSpxxZdzhmj(BdcXm bdcXm);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param   bdcXm
     * @description 获取导出激扬档案jpg非数据集获取的附加信息
    */
    HashMap<String,String> getArchiveAdditionalInfo(BdcXm bdcXm);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过fwhs中的数据更新spxx的坐落、面积、用途
     */
    void updateSpxxByDjsj( ProjectPar projectPar);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过projectpar初始化bdc_spxx表
     */
    BdcSpxx getBdcSpxxFromProjectPar(final ProjectPar projectPar, BdcSpxx bdcSpxx);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过权籍房屋信息初始化bdc_spxx表
     */
    BdcSpxx getBdcSpxxFromDjsjFwxx(final DjsjFwxx djsjFwxx, BdcSpxx bdcSpxx, ProjectPar projectPar);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 从原项目获取bdc_spxx
     */
    BdcSpxx getBdcSpxxFromYProjectPar(ProjectPar projectPar, BdcSpxx bdcSpxx);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 通过权籍项目信息初始化bdc_spxx表
      */
    void updateSpxxByDjsjFwXmxx(DjsjFwXmxx djsjFwXmxx, BdcSpxx bdcSpxx);
}
