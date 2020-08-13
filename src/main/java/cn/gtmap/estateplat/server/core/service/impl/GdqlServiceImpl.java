package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdqlMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0,2016/4/19.
 * @description 过度权利展示
 */
@Service
public class GdqlServiceImpl implements GdqlService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdqlMapper gdqlMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private GdDyService gdDyService;
    @Autowired
    private GdYgService gdYgService;
    @Autowired
    private GdYyService gdYyService;

    @Override
    public void saveFwzlByQlid(String qlid) {
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo("qlid", qlid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            StringBuilder fwzl = new StringBuilder();
            for (int i = 0; i < gdBdcQlRelList.size(); i++) {
                String fwid = gdBdcQlRelList.get(i).getBdcid();
                GdFw gdFw = entityMapper.selectByPrimaryKey(GdFw.class, fwid);
                if (gdFw != null) {
                    fwzl.append(gdFw.getFwzl()).append(" ");
                }
            }
            if (StringUtils.isNotBlank(fwzl)) {
                GdFwQl gdFwQl = entityMapper.selectByPrimaryKey(GdFwQl.class, qlid);
                if (null != gdFwQl) {
                    gdFwQl.setFwzl(fwzl.toString());
                    entityMapper.saveOrUpdate(gdFwQl, qlid);
                }
            }
        } else {
            GdFwQl gdFwQl = entityMapper.selectByPrimaryKey(GdFwQl.class, qlid);
            gdFwQl.setFwzl("");
            entityMapper.saveOrUpdate(gdFwQl, qlid);
        }
    }

    @Override
    public void saveTdzlByQlid(String qlid) {
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo("qlid", qlid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            StringBuilder tdzl = new StringBuilder();
            StringBuilder tddjh = new StringBuilder();
            for (int i = 0; i < gdBdcQlRelList.size(); i++) {
                String tdid = gdBdcQlRelList.get(i).getBdcid();
                GdTd gdtd = entityMapper.selectByPrimaryKey(GdTd.class, tdid);
                if (gdtd != null) {
                    if (i == gdBdcQlRelList.size() - 1) {
                        tdzl.append(gdtd.getZl());
                        tddjh.append(gdtd.getDjh());
                    } else {
                        tdzl.append(gdtd.getZl()).append(" ");
                        tddjh.append(gdtd.getDjh()).append(" ");
                    }

                }
            }
            GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class, qlid);
            if (null != gdTdQl) {
                gdTdQl.setTdzl(tdzl.toString());
                gdTdQl.setDjh(tddjh.toString());
                entityMapper.saveOrUpdate(gdTdQl, qlid);
            }
        } else {
            GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class, qlid);
            gdTdQl.setTdzl("");
            entityMapper.saveOrUpdate(gdTdQl, qlid);
        }
    }

    @Override
    public void saveQlrByQlid(String qlid) {
        Example example = new Example(GdQlr.class);
        example.createCriteria().andEqualTo("qlid", qlid).andEqualTo("qlrlx", "qlr");
        List<GdQlr> gdQlrList = entityMapper.selectByExample(GdQlr.class, example);
        if (CollectionUtils.isNotEmpty(gdQlrList)) {
            StringBuilder qlr = new StringBuilder();
            for (int i = 0; i < gdQlrList.size(); i++) {
                GdQlr gdQlr = gdQlrList.get(i);
                qlr.append(gdQlr.getQlr()).append(" ");
            }
            if (StringUtils.isNotBlank(qlr)) {
                GdFwQl gdFwQl = entityMapper.selectByPrimaryKey(GdFwQl.class, qlid);
                GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class, qlid);
                GdCf gdcf = entityMapper.selectByPrimaryKey(GdCf.class, qlid);
                if (null == gdcf) {
                    if (gdFwQl != null) {
                        gdFwQl.setQlr(qlr.toString());
                        entityMapper.saveOrUpdate(gdFwQl, qlid);
                    }
                    if (gdTdQl != null) {
                        gdTdQl.setQlr(qlr.toString());
                        entityMapper.saveOrUpdate(gdTdQl, qlid);
                    }
                }
            }
        }
    }

    @Override
    public List<String> getGdfwqlQlid(final String hh, final HashMap map) {
        if (StringUtils.isNotBlank(hh)) {
            map.put("hhSearch", hh);
        }
        return gdqlMapper.getGdfwqlQlid(map);
    }

    @Override
    public List<String> getGdtdqlQlid(final String hh) {
        return gdqlMapper.getGdtdqlQlid(hh);
    }

    @Override
    public Integer getGdfwqlCount(final String hh, final HashMap map) {
        if (StringUtils.isNotBlank(hh)) {
            map.put("hhSearch", hh);
        }
        return gdqlMapper.getGdtdqlCount(map);
    }

    @Override
    public void saveGdfw(final String fwid) {
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo("bdcid", fwid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                if (StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                    GdFwQl gdFwQl = entityMapper.selectByPrimaryKey(GdFwQl.class, gdBdcQlRel.getQlid());
                    if (gdFwQl != null) {
                        saveFwzlByQlid(gdFwQl.getQlid());
                    }
                }
            }
        }
    }

    @Override
    public void saveGdtd(final String tdid) {
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo("bdcid", tdid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                if (StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                    GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class, gdBdcQlRel.getQlid());
                    if (gdTdQl != null) {
                        saveTdzlByQlid(gdTdQl.getQlid());
                    }
                }
            }
        }
    }

    @Override
    public HashMap<String, String> getBdcLxAndQlIdsByGdId(@Param(value = "gdId") final String gdId) {
        return gdqlMapper.getBdcLxAndQlIdsByGdId(gdId);
    }

    @Override
    public List<HashMap<String, String>> getCqZhQlrZslxByQlId(final HashMap map) {
        return gdqlMapper.getCqZhQlrZslxByQlId(map);
    }

    @Override
    public String getGdZsZt(final HashMap map) {
        HashMap<String, Object> hashMap = gdqlMapper.getZsZtByQlId(map);
        if (StringUtils.isNotBlank(hashMap.get("ISZX").toString())) {
            return hashMap.get("ISZX").toString();
        } else {
            throw new AppException("注销状态字段的值为空或未找到相应权利", AppException.ENTITY_EX, new Object());
        }
    }

    @Override
    public String getGdIdsByQlId(final HashMap map, final String flag) {
        List<HashMap<String, String>> resultList = gdqlMapper.getGdIdsByQlId(map);
        List<String> stringList = new ArrayList<String>();
        for (HashMap<String, String> stringHashMap : resultList) {
            stringList.add(stringHashMap.get(flag));
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        StringBuilder stringBuilder = new StringBuilder();
        hashMap.put(flag, "");
        if (CollectionUtils.isNotEmpty(stringList)) {
            for (String str : stringList) {
                if (!StringUtils.equals(hashMap.get(flag), str)) {
                    hashMap.put(flag, str);
                    stringBuilder.append(str);
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public List<HashMap> getGdFwQlByFczh(String fczh) {
        List<HashMap> hashMapList = null;
        if (StringUtils.isNotBlank(fczh)) {
            HashMap hashMap = new HashMap();
            hashMap.put("fczh", fczh);
            hashMapList = gdqlMapper.getGdFwQlByMap(hashMap);
        }
        return hashMapList;
    }

    @Override
    public String getQllxNameByQlid(final String qlid) {
        String qllxName = "";
        if (StringUtils.isNotBlank(qlid)) {
            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(qlid);
            if (null != gdFwsyq) {
                qllxName = Constants.GDQL_FWSYQ;
            } else {
                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(qlid);
                if (null != gdTdsyq) {
                    qllxName = Constants.GDQL_TDSYQ;
                }else{
                    GdDy gdDy = gdDyService.getGdDyByDyDyid(qlid);
                    if(null!=gdDy){
                        qllxName = Constants.GDQL_DY;
                    }else{
                        GdCf gdCf = gdCfService.getGdCfByCfid(qlid);
                        if(null!=gdCf){
                            qllxName = Constants.GDQL_CF;
                        }else{
                            GdYg gdYg = gdYgService.queryGdYgByYgid(qlid);
                            if(null != gdYg){
                                qllxName = Constants.GDQL_YG;
                            }else{
                                GdYy gdYy = gdYyService.queryGdYyByYyid(qlid);
                                if(null != gdYy){
                                    qllxName = Constants.GDQL_YY;
                                }
                            }
                        }
                    }
                }
            }
        }
        return qllxName;
    }
}
