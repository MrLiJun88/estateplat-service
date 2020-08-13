package cn.gtmap.estateplat.server.service.archives;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/19
 * @description 张家港激扬档案推送接口
 */
public interface ArchivesService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param userid
     * @rerutn
     * @description 向激扬档案推送文件
     */
    void pushArchivesFile(String proid,String userid);

}
