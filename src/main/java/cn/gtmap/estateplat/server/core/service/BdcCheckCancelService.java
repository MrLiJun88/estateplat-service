package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;

/**
 * 检查是否可以注销
 * Created by lst on 2015/5/5.
 */
public interface BdcCheckCancelService {

    /**
     * 通过申请类型代码和不动产id去判断是否可以注销
     *
     * @param sqlxdm
     * @param yqllxdm
     * @param djlxdm
     * @param bdcid
     * @return
     */
    boolean checkCancel(final String sqlxdm,final String yqllxdm,final String djlxdm,final String bdcid);

    /**
     * 根据不动产id和状态获取数据
     * @param proid
     * @param isJy
     * @return
     */
     List<GdDy> getGdDyByProid(final String proid,final Integer isJy);

    /**
     * 根据不动产单元id和状态获取获取抵押权利
     *
     * @param bdcdyid
     * @param qszt
     * @return
     */
    List<BdcDyaq> getBdcDyaqByBdcdyid(final String bdcdyid,final Integer qszt);

    /**
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @param bdcdyid,qszt
     * @rerutn
     * @description 根据不动产单元id和状态获取获取地役权利
     */
    List<BdcDyq> getBdcDyqByBdcdyid(String bdcdyid, Integer qszt);
    /**
     * 根据不动产id和状态获取数据
     * @param proid

     * @param qszt
     * @return
     */
     List<GdCf> getGdCfByProid(final String proid,final Integer qszt);


    /**
     * 根据不动产单元id和状态获取获取查封权利
     *
     * @param bdcdyid
     * @param qszt
     * @return
     */
    List<BdcCf> getBdcCfByBdcdyid(final String bdcdyid,final Integer qszt);

    /**
     * 根据不动产id和状态获取数据
     * @param proid
     * @param iszx
     * @return
     */
     List<GdYg> getGdYgByProid(final String proid,final Integer iszx);


    /**
     * 根据不动产单元id和状态获取获取预告权利
     *
     * @param bdcdyid
     * @param qszt
     * @return
     */
    List<BdcYg> getBdcYgByBdcdyid(final String bdcdyid,final Integer qszt);

    /**
     * 将要注销的信息从过度表里取出存入bdc表里
     *
     * @param yqllxdm
     * @param bdcid
     * @return
     */
    Project cancelInfo(Project project, String yqllxdm, String bdcid, String lx);

    /**
     * 验证过度房屋数据里的字典数据是否在字典表里存在
     *
     * @param fwid
     * @return
     */
    List<String> checkGdFwZdsjByFwid(final String fwid);


    /**
     * 获取过度房屋数据里的错误字典数据
     *
     * @param fwid
     * @return
     */
    List<String> getGdFwErrorZdsjByFwid(final String fwid);

    /**
     * 获取过度土地数据里的错误字典数据
     *
     * @param tdid
     * @param bdclx
     * @return
     */
    List<String> getGdTdErrorZdsjByTdid(final String tdid,final String bdclx);

    /**
     * 获取过度证件号类型错误字典数据
     *
     * @param proid
     * @return
     */
    List<String> getQlrZjhlx(final String proid);

    /**
     * 获取过度林权数据里的错误字典数据
     *
     * @param lqid
     * @return
     */
    List<String> getGdLqErrorZdsjByLqid(final String lqid);

    /**
     * 获取过度草权数据里的错误字典数据
     *
     * @param cqid
     * @return
     */
    List<String> getGdCqErrorZdsjByCqid(final String cqid);

    /**
     * 获取房屋信息，如果不是字典数据不取值
     *
     * @param gdfw
     * @return
     */
    GdFw getGdFwFilterZdsj(final GdFw gdfw);

    /**
     * 获取土地信息，如果不是字典数据不取值
     *
     * @param gdTd
     * @return
     */
    GdTd getGdTdFilterZdsj(final GdTd gdTd);

    /**
     * 获取林权信息，如果不是字典数据不取值
     *
     * @param gdLq
     * @return
     */
    GdLq getGdLqFilterZdsj(final GdLq gdLq);

    /**
     * 获取草权信息，如果不是字典数据不取值
     *
     * @param gdCq
     * @return
     */
    GdCq getGdCqFilterZdsj(final GdCq gdCq);

    /**
     * 获取过渡预告信息，如果不是字典数据不取值
     *
     * @param gdYg
     * @return
     */
    GdYg getGdYgFilterZdsj(final GdYg gdYg);

    /**
     * 获取过渡查封信息，如果不是字典数据不取值
     *
     * @param gdCf
     * @return
     */
    GdCf getGdCfFilterZdsj(final GdCf gdCf);

    /**
     * 获取过渡抵押信息，如果不是字典数据不取值
     *
     * @param gdDy
     * @return
     */
    GdDy getGdDyFilterZdsj(final GdDy gdDy);

    /**
     * 获取过渡权利人信息，如果不是字典数据不取值
     *
     * @param gdQlr
     * @return
     */
    GdQlr getGdQlrFilterZdsj(final GdQlr gdQlr);

    /**
     * 根据项目ID获取过渡异议
     * @param proid
     * @param iszx
     * @return
     */
    List<GdYy> getGdYyByProid(final String proid,final Integer iszx);
    /**
     * 根据不动产单元id和状态获取获取异议权利
     * @param bdcdyid
     * @param qszt
     * @return
     */
     List<BdcYy> getBdcYyByBdcdyid(final String bdcdyid,final Integer qszt);

    /**
     * 根据不动产单元ID获取权利状态
     * @param bdcdyid
     * @param iszx
     * @return
     */
    List getQlListByBdcdyid(final String bdcdyid,final Integer iszx);

    /**
     * 批量获取房屋信息，如果不是字典数据不取值
     * @param gdFwList
     * @return
     */
    List<GdFw> getGdFwFilterZdsj(final List<GdFw>  gdFwList);

    /**
     * 批量获取土地信息，如果不是字典数据不取值
     * @param gdTdList
     * @return
     */
    List<GdTd> getGdTdFilterZdsj(final List<GdTd>  gdTdList);
}
