package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;

/**
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-17
 * @description 不动产登记工作流项目转发时会调用该接口
 */
public interface TurnProjectService {
    /**
     * zdd 创建权利类型，只是根据不动产登记项目信息组织了需要创建的权利对象，并未真正保存到数据库中
     *
     * @param bdcXm
     */
    List<InsertVo> createQllxVo(final BdcXm bdcXm);

    /**
     * zdd 保存权利类型
     *
     * @param dataList
     */
    void saveOrUpdateInsertVo(final List<InsertVo> dataList);


    /**
     * zdd 保存权利类型
     *
     * @param bdcXm
     */
    QllxVo saveQllxVo(BdcXm bdcXm);


    /**
     * zx 保存原权利类型
     *
     * @param bdcXm
     */
    void saveYQllxVo(final BdcXm bdcXm);


    /**
     * zdd 根据权利人表以及是否分别持证  创建证书 证书与权利人关系表  证书与项目关系表
     *
     * @param bdcXm
     * @return
     */
    List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs);

    List<BdcZs> saveBdcZs(final BdcXm bdcXm);
    /**
     * 工作流转发结点
     *
     * @param project
     */
    Project turnWfActivity(Project project);

    /**
     * 保存不动产权证号
     * @param wiid
     */
    void saveBdcqzh(String wiid);

    /**
     * 设置userid
     * @param userid
     */
    void setUserid(String userid);

    /**
     * @param bdcqzFlag (1：一证多房；0：一证一房)
     * @param bdcqzmFlag (1：多抵一；0：多抵多)
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description  生成证书(任意流程)
     */
    List<BdcZs> saveBdcZsArbitrary(final BdcXm bdcXm,final String bdcqzFlag,final String bdcqzmFlag);

    /**
     * 补全证书信息
     * @param bdcXm
     */
    void completeZsInfo(BdcXm bdcXm);

    /**
     *  生成证书
     */
    void creatBdcZsInfo(String proid,String userid);
}
