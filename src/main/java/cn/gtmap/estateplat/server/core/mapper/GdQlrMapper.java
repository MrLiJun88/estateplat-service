package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-9-18
 * Time: 上午8:57
 * @description 过渡权利人
 */
@Repository
public interface GdQlrMapper {
    void delGdQlrByQlid(String qlid);
}
