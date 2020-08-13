package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.List;
import java.util.Map;

/**
 * 不动产登记宗地信息
 *
 * @author zzhw
 * @version V1.0, 15-3-18
 */
public interface BdcTdService {
    /**
     * zzhw根据宗地宗海号查找bdc_td
     *
     * @param zdzhh
     * @return
     */
    BdcTd selectBdcTd(final String zdzhh);

    /**
     * zzhw 将地籍库里面的信息继承到bdc_td里面
     *
     * @param djsjZdxx
     * @param bdcTd
     * @return
     */
    BdcTd getBdcTdFromZdxx(final DjsjZdxx djsjZdxx, final Project project, BdcTd bdcTd);

    /**
     * zx 将地籍库里面的信息继承到bdc_td里面
     *
     * @param djsjZdxx
     * @return
     */
    BdcTd getBdcTdFromDjxx(final DjsjZdxx djsjZdxx, final List<DjsjQszdDcb> djsjQszdDcbList, final List<DjsjNydDcb> djsjNydDcbList, final Project project, String qllx);

    BdcTd getBdcTdFromDjsjZdxx(DjsjZdxx djsjZdxx, ProjectPar projectPar);

    BdcTd getBdcTdFromQszdDcb(List<DjsjQszdDcb> djsjQszdDcbList, ProjectPar projectPar);

    BdcTd getBdcTdFromNydDcb(List<DjsjNydDcb> djsjNydDcbList, ProjectPar projectPar);

    /**
     * zx 将地籍库里面的权属宗地信息继承到bdc_td里面
     *
     * @param djsjQszdDcbList
     * @param bdcTd
     * @param qllx
     * @return
     */
    BdcTd getBdcTdFromQsZdxx(final List<DjsjQszdDcb> djsjQszdDcbList, final Project project, BdcTd bdcTd, final String qllx);

    /**
     * zdd 删除项目对应的宗地信息
     * @param proid
     */
    //void deleteBdcTdByProid(String proid);

    /**
     * sc 如果是土地房屋办结时查看是否有
     *
     * @param bdcXm
     */
    void changeGySjydZt(BdcXm bdcXm,List<String> djhList);

    /**
     * hqz 土地退回
     *
     * @param bdcXm
     */
    void backGySjydZt(BdcXm bdcXm);

    /**
     * sc 如果是土地房屋办结时查看是否有
     *
     * @param bdcXm
     */
    void changeBackQlFj(BdcXm bdcXm);

    //zhouwanqing 根据zdzhh删除bdc_td
    void delBdcTdByZdzhh(final String zdzhh);

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 获取宗地类别
     */
    String getZdLb(final String proid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据宗地类别修改面积显示
     */
    String changeMjByZdLb(final String zdLb, final String mj, final String bdclx);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 根据宗地类别修改面积显示（南通)
     */
    String changeMjByNt(String mj, String bdclx, String proid);

    /**
     * @param
     * @return List<Map>
     * @author wangtao
     * @description
     */
    List<Map> getZdty();

    /**
     * @param xmxx
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 在多线程初始化项目数据之前提前插入bdctd，避免多线程重复插入
     */
    void initBdcTdAhead(final Xmxx xmxx);


    /**
     * @param xmxx
     * @author <a href="mailto:xinghuajian@gtmap.cn">xusong</a>
     * @description 在多线程初始化项目数据之前提前插入bdctd，避免多线程重复插入
     */
    void initBdcTdAhead(final Xmxx xmxx, final List<String> zdzhhList);


    /**
     * @param bdcTdList
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 保存bdcTd
     */
    void saveBdcTdList(List<BdcTd> bdcTdList);


    /**
     * @param
     * @return
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @Description: 获取宗地宗海号列表
     */
    List<String> getZdzhhList(Xmxx xmxx);
}
