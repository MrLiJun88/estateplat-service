package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.DjbQlPro;

import java.util.HashMap;
import java.util.List;

/**
 * lst 登记簿树相关接口
 */
public interface CreatBdcDjbService {
    /**
     * 根据不动产单元 获取权利信息数据
     *
     * @param bdcdyh
     * @return
     */
    List<DjbQlPro> getQlByBdcdy(final String bdcdyh);

    /**
     * 根据不动产单元 获取权利信息页数
     *
     * @param bdcdyh
     * @return
     */
    HashMap getQlPageByBdcdyh(final String bdcdyh);

    /**
     * zdd 查找宗地宗海号下所有不动产单元权利信息
     *
     * @param zdzhh
     * @return
     */
    List<DjbQlPro> getQlByzdzhh(final String zdzhh);


    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/4/8
     * @param:
     * @return:null
     * @description:根据不动产单元号和权利获取该权利登记簿页数的第一页
     */
    public String getStartPageBy(final String bdcdyh,final String qllx);
}
