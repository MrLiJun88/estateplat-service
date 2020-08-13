package cn.gtmap.estateplat.server.core.service;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * 获取房产附件.
 * User: lj
 * Date: 16-5-5
 * Time: 下午7:58
 * To change this template use File | Settings | File Templates.
 */
public interface EtlFcFjPicToBdcService {
    /**
     * 根据路径获取图片
     * @param filePath
     * @param fileName
     * @return
     */
    public byte[] getImage(String filePath, String fileName);

    public byte[] getTdImage(String filePath, String fileName);

    /**
     * 获取房产附件文件路径
     * @param syqid
     * @return
     */
    List<HashMap> getImageRoute(String syqid);

    List<HashMap> getTdImageRoute(String projectid);

    /**
     * 获取房产业务id
     * @param syqid
     * @return
     */
    List<HashMap> getfcywid(String syqid);
}
