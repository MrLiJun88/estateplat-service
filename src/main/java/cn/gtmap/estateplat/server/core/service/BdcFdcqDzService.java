package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjFwXmxx;
import cn.gtmap.estateplat.model.server.core.QllxVo;

import java.util.List;
import java.util.Map;

/**
 * 不动产登记房地产权（多幢）权利服务
 * Created by IntelliJ IDEA.
 * User: sc
 * Date: 15-4-17
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public interface BdcFdcqDzService {

    /**
     * @author
     * @param bdcFdcqDz 不动产登记房地产权多幢权利信息
     * @param qllxVo 权利基本信息
     * @return
     * @description
     */
    void saveBdcFdcqDz(BdcFdcqDz bdcFdcqDz, QllxVo qllxVo);

    /**
     * @author
     * @param bdcFdcqDz 不动产登记房地产权多幢权利信息
     * @param
     * @return
     * @description
     */
    void saveBdcFdcqDz(BdcFdcqDz bdcFdcqDz);


    /**
     * zx :更删除不动产房地产多幢权利信息
     *
     * @param proid 不动产登记项目ID
     * @return
     */
    void delBdcFdcqDzByProid(final String proid);

    /**
     * 获取房地产权登记信息（项目内多幢房屋）
     *
     * @param proid 不动产登记项目ID
     * @return
     */
    BdcFdcqDz getBdcFdcqDz(final String proid);

    /**
     * 获取房地产权登记信息（项目内多幢房屋）
     *
     * @param bdcdyid 不动产单元ID
     * @return
     */
    BdcFdcqDz getBdcFdcqDzByBdcdyid(final String  bdcdyid);

    /**
     * 获取房地产权登记信息（独幢、层、套、间房屋）
     *
     * @param map
     * @return
     */
    List<BdcFdcqDz> getBdcFdcqDzList(final Map map);

    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param bdcXm
     *@return
     *@description 获取权籍信息生成房地产权多幢和bdc_fwfzxx
     */
    BdcFdcqDz reGenerateBdcFdcqDzFromQj(BdcXm bdcXm,BdcFdcqDz bdcFdcqDz);

    /**
     * 项目内多幢获取面积信息
     * @author yanzhenkun
     * @param
     * @return fwzmj
     */
    Double getBdcFdcqDzFwzmj(String xmly,String bdcdyid,String bdcdyh,String djlx);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 获取权籍信息更新分幢信息
      */
    void updateFdcqdfzxxByDjsjfwXmxx(DjsjFwXmxx djsjFwXmxx, BdcFdcqDz bdcFdcqDz);
}
