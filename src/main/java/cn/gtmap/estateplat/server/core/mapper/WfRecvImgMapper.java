package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lj
 * Date: 16-5-5
 * Time: 下午10:24
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface WfRecvImgMapper {
    List<HashMap> getImageRoute(String syqid);
    List<HashMap> getTdImageRoute(String projectid);
    List<HashMap> getFcywid(String syqid);
}
