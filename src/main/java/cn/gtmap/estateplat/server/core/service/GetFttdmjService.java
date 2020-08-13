package cn.gtmap.estateplat.server.core.service;

/**
 * <p/>
 * <p/>
 * Created with IntelliJ IDEA.
 * User: lj
 * Date: 15-10-22
 * Time: 下午7:51
 * @description 分摊土地面积服务
 */
public interface GetFttdmjService {
    /**
     * 计算分摊土地面积
     */
    String calculateFttdmj(final String djh);
}
