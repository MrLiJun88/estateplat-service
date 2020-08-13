package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.YztIntService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-10-9
 * Time: 下午6:23
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class YztIntServiceImpl implements YztIntService {
    @Autowired
    private Repo repository;
    @Autowired
    private DjxxMapper djxxMapper;

    @Override
    @Transactional(readOnly = true)
    public HashMap<String, Object> getSerchJson(JSONArray jsonArray, String l, int p, int s) {
        Map map = new HashMap();
        map.put("layerName", l);
        List list = new ArrayList();
        HashMap querymap = new HashMap();
        List<String> bdcdys = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject field = (JSONObject) jsonArray.get(i);
                querymap.put(field.get("name"), field.get("value"));
                //根据自然幢号获取不动产单元号集合
                if (StringUtils.equals(field.get("name").toString(), "ZRZH")) {
                    String zrzh = field.get("value").toString();
                    //获取所对应的房屋类型
                    List<String> bdcfwlxList = djxxMapper.getBdcfwlxByzrzh(zrzh);
                    if (CollectionUtils.isNotEmpty(bdcfwlxList)) {
                        for (int j = 0; j < bdcfwlxList.size(); j++) {
                            HashMap zrzhMap = new HashMap();
                            zrzhMap.put("bdcfwlx", bdcfwlxList.get(j));
                            zrzhMap.put("zrzh", zrzh);
                            //根据房屋类型和自然幢号去不同的表查询不动产单元号
                            List<String> bdcdyhList = djxxMapper.getBdcdyhByZrzh(zrzhMap);
                            if (CollectionUtils.isNotEmpty(bdcdyhList)) {
                                for (String bdcdyh : bdcdyhList) {
                                    bdcdys.add(bdcdyh);
                                }
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(bdcdys)) {
                String[] newStr = bdcdys.toArray(new String[1]);
                querymap.put("bdcdyhs", newStr);
            }
        }
        Page<HashMap> dataPaging = null;
        if (dataPaging != null) {
            map.put("page", dataPaging.getPage());
            map.put("total", dataPaging.getTotal());
            List<HashMap> zsList = dataPaging.getRows();
            if (CollectionUtils.isNotEmpty(zsList)) {
                for (int i = 0; i < zsList.size(); i++) {
                    HashMap zsTemp = new HashMap();
                    HashMap zs = zsList.get(i);
                    Set set = zs.keySet();
                    Iterator iter = set.iterator();
                    StringBuilder zrzh = new StringBuilder();
                    //将Hash的key从大写转换成小写
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        //获取自然幢号
                        if (StringUtils.equals(key, "BDCDYH")) {
                            List<String> zrzhList = djxxMapper.getZrzhByBdcdyh(zs.get(key).toString());
                            if (CollectionUtils.isNotEmpty(zrzhList)) {
                                for (int j = 0; j < zrzhList.size(); j++) {
                                    String zrddy = zrzhList.get(i);
                                    if (i < zrzhList.size() - 1)
                                        zrzh.append(zrddy).append(",");
                                    else
                                        zrzh.append(zrddy);
                                }
                            }
                        }
                        zsTemp.put(key.toLowerCase(), zs.get(key));
                    }
                    zsTemp.put("zrzh", zrzh);
                    list.add(zsTemp);
                }
            }
        }

        map.put("returns", list);
        HashMap<String, Object> result = new HashMap();
        result.put("success", true);
        result.put("result", map);
        return result;
    }
}
