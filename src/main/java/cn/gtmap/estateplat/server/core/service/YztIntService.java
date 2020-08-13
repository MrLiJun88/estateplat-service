package cn.gtmap.estateplat.server.core.service;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-10-9
 * Time: 下午6:12
 * @description 与一张图地图集成服务
 */
public interface YztIntService {
    /**
     * 组织返回一张图Json
     *
     * @param jsonArray
     * @param l
     * @return
     */
    HashMap<String, Object> getSerchJson(JSONArray jsonArray, String l, int p, int s);
}
