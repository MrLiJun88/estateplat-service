package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2016/7/27
 * @description 关联证书相关服务
 */
public interface GlZsService {
    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 将土地证信息带入房地产权
     */
    void saveTdxxToBdcFdcq(List<GdTdsyq> gdTdsyqList, List<GdTd> gdTdList, BdcXm bdcXm);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 将抵押信息带入抵押权
     */
    void saveDyxxToBdcDyaq(List<GdDy> gdDyList, List<GdTd> gdTdList, BdcXm bdcXm);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 将证书权利人信息带入项目
     */
    void saveZsQlr(BdcXm bdcXm, String xmly, String yproid,String yqlid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 将房产证证信息带入房地产权
     */
    void saveFwxxToBdcFdcq(List<GdFwsyq> gdFwsyqList, List<GdFw> gdFwList, BdcXm bdcXm);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 附属设施信息导入房地产权
     */
    void addFsssxx2BdcFdcq(List<GdFwsyq> gdFwsyqList, List<GdFw> gdFwList, BdcXm bdcXm);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 将土地权利信息带入项目
      */
    void saveTdqlxx(BdcXm bdcXm, String yproid, String yqlid);

    /**
     * @param qlids 权利ID
     * @param proid
     * @return wiid 工作流ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 关联土地证
     */
    void gltdz(String qlids, String wiid, String proid);
}
