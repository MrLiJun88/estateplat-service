package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 过渡地号对照表
 * Created by lst on 2015/3/23
 */
@Repository
public interface GdDhDzhMapper {
    /**
     * 根据老地号获取新地号
     *
     * @param ydjh
     * @return
     */
    String getNewDhByOldDh(String ydjh);


    /**
     * 根据地号获取所有地号
     *
     * @param dh
     * @return
     */
    List getAllDjh(String dh);
}
