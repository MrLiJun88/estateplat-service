package cn.gtmap.estateplat.server.core.service;


import java.util.Map;

/**
 * 不动产受理流水号
 *
 * @author zx
 * @version V1.0, 16-12-7
 * @since
 */
public interface BdcSllshService {

    /**
     * 保存受理编号流水号.
     * @param slbhlsh
     */
    public void saveBh(final String slbhlsh);

    /**
     * 保存受理编号流水号.
     * @param slbhlsh
     * @param qh
     */
    public void saveBh(final String slbhlsh,final String qh);

    /**
     * 获取受理编号流水号.
     */
    public Integer getSlbhLsh();


    /**
     * 获取受理编号流水号.
     */
    public Integer getSlbhLsh(final Map map);

}
