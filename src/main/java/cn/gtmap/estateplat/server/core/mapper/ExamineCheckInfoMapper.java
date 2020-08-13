package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2016/12/2
 * @description
 */
@Repository
public interface ExamineCheckInfoMapper {

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param paramMap
     * @return List<Map<String,String>>
     * @description 根据wiid获取当前项目所有不动产单元号及PROID
     */
    List<Map<String,String>> queryBdcdyhByWiid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param paramMap
     * @return List<Map<String,String>>
     * @description 根据wiid获取当前项目所有YPROID和YQLID
     */
    List<Map<String,String>> queryYproidByWiid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param paramMap
     * @return List<Map<String,String>>
     * @description 根据BDCDYH获取FCDAH
     */
    List<Map<String,String>> queryFwHsInfoByBdcdyh(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param paramMap
     * @return List<Map<String,String>>
     * @description 根据gdproid获取gdqlid
     */
    List<Map<String,String>> queryGdproidByProject(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param paramMap
     * @return List<Map<String,String>>
     * @description 根据gdqlid获取dah
     */
    List<Map<String,String>> queryDahByGdQlid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param paramMap
     * @return List<Map<String,String>>
     * @description 根据gdqlid获取不动产单元号
     */
    List<Map<String,String>> queryBdcdyhByGdQlid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param paramMap
     * @return List<Map<String,String>>
     * @description 根据gdqlid获取不动产单元号
     */
    List<Map<String,String>> queryBdcQlidByProid(Map<String, Object> paramMap);
}
