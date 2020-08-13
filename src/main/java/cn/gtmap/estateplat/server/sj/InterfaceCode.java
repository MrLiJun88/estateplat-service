package cn.gtmap.estateplat.server.sj;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2019/8/12
 * @description 定义统一接口标识码获取方法
 */
public interface InterfaceCode {
    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    String getIntetfacaCode();
}
