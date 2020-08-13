package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 证书打印
 */
@Repository
public interface BdcZsPrintMapper {
    /**
     * @param hashMap
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<HashMap>
     * @description 获取证书打印的相关数据
     */
    public List<HashMap> getZsPrint(HashMap hashMap);

    /**
     * @param proid
     * @return List<HashMap>
     * @author <a href="mailto:liaoxiang@gtmap.cn">liaoxiang</a>
     * @description 根据proid获取证明单信息
     */
    public List<HashMap> getZmdPrint(String proid);

}
