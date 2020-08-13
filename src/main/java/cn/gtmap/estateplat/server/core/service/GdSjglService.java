package cn.gtmap.estateplat.server.core.service;

/**
 * Created by lst on 2015/5/9
 * @description 过渡数据管理服务
 */
public interface GdSjglService {

    /**
     * 批量删除过度数据
     *
     * @param ids
     * @param bdclx
     */
    void delGdsjByIds(final String ids[], final String bdclx);

    /**
     * 批量导入房屋数据
     */
    String readFczFromXls(final String filePath);

    /**
     * 批量导入土地数据
     */
    String readTdzFromXls(final String filePath);

    /**
     * 通过权利id注销权利
     */
    String zxQl(final String qlid, final String userid);

    /**
     * 通过权利id解除注销权利
     */
    String jczxQl(final String qlid);

    /**
     * 注销过渡土地权利
     */
    String zxGdTdQl(final String qlid, final String userid);

    /**
     * 解除注销权利
     */
    String jczxGdTdQl(final String qlid);

}
