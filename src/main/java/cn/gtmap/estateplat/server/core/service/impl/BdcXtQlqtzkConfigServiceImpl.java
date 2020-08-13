package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcFdcqDzMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.StringLengthComparator;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author lst
 * @version V1.0, 15-5-12
 */
@Service
public class BdcXtQlqtzkConfigServiceImpl implements BdcXtQlqtzkConfigService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXtLimitfieldService bdcXtLimitfieldService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcFdcqDzMapper bdcFdcqDzMapper;
    @Autowired
    private BdcZsService bdcZsService;

    private static final String SQLXDM = "sqlxdm";
    private static final String QLLXDM = "qllxdm";
    private static final String QLLXZLX = "qllxzlx";
    private static final String BDBZZQSE = "BDBZZQSE";
    private static final String BDCJZ = "BDCJZ";
    private static final String ZGZQQDSE = "ZGZQQDSE";


    @Override
    public void saveOrUpdateQlqtzk(BdcXtQlqtzkConfig bdcXtQlqtzkConfig) throws Exception {
        if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getSqlxdm())) {
            Example example = new Example(bdcXtQlqtzkConfig.getClass());
            Example.Criteria criteria = example.createCriteria().andEqualTo(SQLXDM, bdcXtQlqtzkConfig.getSqlxdm());
            if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getQllxdm())) {
                criteria.andEqualTo(QLLXDM, bdcXtQlqtzkConfig.getQllxdm());
            } else {
                criteria.andIsNull(QLLXDM);
            }
            if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getQllxzlx())) {
                criteria.andEqualTo(QLLXZLX, bdcXtQlqtzkConfig.getQllxzlx());
            } else {
                criteria.andIsNull(QLLXZLX);
            }
            if (CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
                List<BdcXtQlqtzkConfig> newQlqtzk = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(newQlqtzk)) {
                    entityMapper.deleteByExample(example);
                }
            }
            entityMapper.insertSelective(bdcXtQlqtzkConfig);
        }
    }

    @Override
    public void deleteQlqtzk(BdcXtQlqtzkConfig bdcXtQlqtzkConfig) throws Exception {
        if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getSqlxdm())) {
            Example example = new Example(bdcXtQlqtzkConfig.getClass());
            Example.Criteria criteria = example.createCriteria().andEqualTo(SQLXDM, bdcXtQlqtzkConfig.getSqlxdm());
            if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getQllxdm())) {
                criteria.andEqualTo(QLLXDM, bdcXtQlqtzkConfig.getQllxdm());
            } else {
                criteria.andIsNull(QLLXDM);
            }
            if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getQllxzlx())) {
                criteria.andEqualTo(QLLXZLX, bdcXtQlqtzkConfig.getQllxzlx());
            } else {
                criteria.andIsNull(QLLXZLX);
            }
            if (CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria()))
                entityMapper.deleteByExample(example);

        }
    }

    @Override
    public List<BdcXtQlqtzkConfig> getQlqtzk(BdcXtQlqtzkConfig bdcXtQlqtzkConfig) {
        List<BdcXtQlqtzkConfig> list = null;
        if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getSqlxdm())) {
            Example example = new Example(bdcXtQlqtzkConfig.getClass());
            Example.Criteria criteria = example.createCriteria().andEqualTo(SQLXDM, bdcXtQlqtzkConfig.getSqlxdm());
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 查询时不需要根据权利类型子类型查询
             */
            //zhouwanqing 因权利类型有可能都是空，则必需准确定位一个
            if (StringUtils.isNotBlank(bdcXtQlqtzkConfig.getQllxzlx())) {
                criteria.andEqualTo(QLLXZLX, bdcXtQlqtzkConfig.getQllxzlx());
            } else {
                criteria.andIsNull(QLLXZLX);
            }
            if (CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria()))
                list = entityMapper.selectByExample(example);
        }
        if (list == null) {
            list = new ArrayList<BdcXtQlqtzkConfig>();
        }
        return list;
    }

    @Override
    public String replaceMb(String mb, String sql, BdcXm bdcXm, BdcBdcdy bdcBdcdy) {
        String newMb = "";
        String mbLastTemp = "";
        String lastMb = mb;
        if (StringUtils.isNotBlank(mb) && StringUtils.isNotBlank(sql)) {
            String[] dbs = sql.split("；|;");
            //将数据源用;隔开成多条语句
            for (int i = 0; i < dbs.length; i++) {
                //不分大小写 替换项目id
                List<Map> list = bdcXtLimitfieldService.runSql(dbs[i].replaceAll("(?i)@proid", "'" + bdcXm.getProid() + "'").replaceAll("(?i)@bdcdyh", "'" + bdcBdcdy.getBdcdyh() + "'").replaceAll("(?i)@wiid", "'" + bdcXm.getWiid() + "'"));
                if (list != null && list.size() == 1) {
                    Map map = list.get(0);
                    //按map的key值字符串长度由高到低排序
                    TreeMap treeMap = new TreeMap(new StringLengthComparator());
                    treeMap.putAll(map);
                    Iterator j = treeMap.keySet().iterator();
                    while (j.hasNext()) {
                        Object o = j.next();
                        String key = o.toString();
                        if (map.get(key) != null) {
                            //对时间和数值的一些处理
                            mb = dealDataFormat(map, key, mb, true);
                        } else {
                            /**
                             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                             * @description 如果查询不出结果则不显示这条记录
                             */
                            String[] mbArray = mb.split("\\n");
                            if (mbArray.length > 0) {
                                mb = "";
                                for (String mbTemp : mbArray) {
                                    if (StringUtils.indexOf(StringUtils.upperCase(mbTemp), key) == -1) {
                                        mb += mbTemp + "\n";
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //针对项目内多幢的会有多个list
                    for (int a = 0; a <= list.size(); a++) {
                        if (a >= list.size()) {
                            break;
                        }
                        if (a > 0) {
                            mb = lastMb;
                        }
                        Map map = list.get(a);
                        //按map的key值字符串长度由高到低排序
                        TreeMap treeMap = new TreeMap(new StringLengthComparator());
                        treeMap.putAll(map);
                        Iterator j = treeMap.keySet().iterator();
                        while (j.hasNext()) {
                            Object o = j.next();
                            String key = o.toString();
                            if (map.get(key) != null) {
                                //对时间和数值的一些处理
                                mb = dealDataFormat(map, key, mb, true);
                            } else {
                                /**
                                 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                                 * @description 如果查询不出结果则不显示这条记录
                                 */
                                String[] mbArray = mb.split("\\n");
                                if (mbArray.length > 0) {
                                    mb = "";
                                    for (String mbTemp : mbArray) {
                                        if (org.apache.commons.lang.StringUtils.indexOf(org.apache.commons.lang.StringUtils.upperCase(mbTemp), key) == -1) {
                                            mb += mbTemp + "\n";
                                        }
                                    }
                                }
                            }
                        }
                        mbLastTemp += mb + "\n";
                    }
                    if (StringUtils.isNotBlank(mbLastTemp)) {
                        mb = mbLastTemp;
                    }
                }
            }

            /**
             * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
             * @description sql查询结果不存在则不显示该配置模板
             */
            String[] mbArray = mb.split("\\n");
            if (mbArray.length > 0) {
                StringBuilder mbBuilder = new StringBuilder();
                for (String mbTemp : mbArray) {
                    if (mbTemp.indexOf("@") == -1) {
                        mbBuilder.append(mbTemp).append("\n");
                    }
                }
                mb = mbBuilder.toString();
            }
            newMb = mb;
        }
        //苏州根据不同的抵押方式生成不同的其他状况说明
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjzx()) && CommonUtil.indexOfStrs(Constants.DJZX_ZGE, bdcXm.getDjzx())
                && StringUtils.isNotEmpty(newMb)) {
            newMb = newMb.replace(Constants.SEMS_YBDY, Constants.SEMS_ZGEDY);
            newMb = newMb.replace(Constants.SJMS_YBDY, Constants.SJMS_ZGEDY);
        }
        return newMb;
    }

    @Override
    public String replaceMbDisplayNull(String mb, String sql, BdcXm bdcXm, BdcBdcdy bdcBdcdy) {
        String newMb = "";
        String mbLastTemp = "";
        String mb2 = StringUtils.upperCase(mb);
        String lastMb = mb;
        Boolean sfxsYbdcqzh = true;
        //定义变量保存项目内多幢的房屋房子个数
        Integer bdcfwfzgs = null;
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        List<BdcFwfzxx> fwfzxxList = bdcFdcqDzMapper.queryBdcFwfzxxlstByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(fwfzxxList)) {
            bdcfwfzgs = fwfzxxList.size();
        }

        if (StringUtils.isNotBlank(mb) && StringUtils.isNotBlank(sql) && bdcXm != null && bdcBdcdy != null) {
            String[] dbs = sql.split("；|;");
            //将数据源用;隔开成多条语句
            for (int i = 0; i < dbs.length; i++) {
                //不分大小写 替换项目id
                List<Map> list = bdcXtLimitfieldService.runSql(dbs[i].replaceAll("(?i)@proid", "'" + bdcXm.getProid() + "'").replaceAll("(?i)@bdcdyh", "'" + bdcBdcdy.getBdcdyh() + "'").replaceAll("(?i)@wiid", "'" + bdcXm.getWiid() + "'"));
                if (list != null && list.size() == 1) {
                    Map map = list.get(0);
                    //按map的key值字符串长度由高到低排序
                    TreeMap treeMap = new TreeMap(new StringLengthComparator());
                    treeMap.putAll(map);
                    Iterator j = treeMap.keySet().iterator();
                    while (j.hasNext()) {
                        Object o = j.next();
                        String key = o.toString();
                        if (map.get(key) != null) {
                            //对时间和数值的一些处理
                           mb = dealDataFormat(map, key, mb, true);
                        }
                    }
                } else {
                    //针对项目内多幢的会有多个list
                    for (int a = 0; a <= list.size(); a++) {
                        if (a >= list.size()) {
                            break;
                        }
                        if (a > 0) {
                            mb = lastMb;
                        }
                        Map map = list.get(a);
                        //按map的key值字符串长度由高到低排序
                        TreeMap treeMap = new TreeMap(new StringLengthComparator());
                        treeMap.putAll(map);
                        Iterator j = treeMap.keySet().iterator();
                        while (j.hasNext()) {
                            Object o = j.next();
                            String key = o.toString();
                            if (map.get(key) != null) {
                                //对时间和数值的一些处理
                                mb = dealDataFormat(map, key, mb, sfxsYbdcqzh);
                            }
                        }
                        //遍历map后若value全为空则不显示该条数据
                        Iterator j2 = treeMap.keySet().iterator();
                        while (j2.hasNext()) {
                            String key2 = j2.next().toString();
                            mb2 = mb2.replaceAll("@" + key2, "");
                        }
                        if (StringUtils.equals(mb2, mb)) {
                            mb = "";
                        }
                        mbLastTemp += mb + "\n";
                    }
                    if (StringUtils.isNotBlank(mbLastTemp)) {
                        mb = mbLastTemp;
                    }
                }
            }

            /**
             * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
             * @description sql查询结果不存在则不显示该配置模板
             */
            //权利其他状况目前有字段保存数值了
            String[] mbArray = mb.split("\\n");
            Boolean noFwfzxx = false;
            if (mbArray.length > 0) {
                mb = "";
                //多幢情况下，多幢的房屋房子个数超过项目内多幢幢数的阈值时，修改为通过多幢附页展示分幢信息
                String bdc_fdcqdz_fwfzxx_yz = AppConfig.getProperty("bdc_fdcqdz_fwfzxx_yz");
                if (StringUtils.isNotBlank(bdc_fdcqdz_fwfzxx_yz) && bdcfwfzgs != null && bdcfwfzgs > Integer.parseInt(bdc_fdcqdz_fwfzxx_yz)) {
                    //实际个数超过阈值,设置参数nofwfzxx为true
                    noFwfzxx = true;
                }
                for (String mbTemp : mbArray) {
                    if (StringUtils.isNotEmpty(mbTemp)) {
                        //多幢的情况，幢号为空单的情况单独处理,为空也要加上去
                        if (mbTemp.contains("幢号")) {
                            if (!noFwfzxx) {
                                mb += mbTemp + "\n";
                            }
                        } else {
                            if (mbTemp.contains("@")) {
                                mb += mbTemp.split("@")[0] + "\n";
                            } else {
                                mb += mbTemp + "\n";
                            }
                        }
                    }
                }
            }
            if (noFwfzxx) {
                mb = mb + Constants.BDCFDCQDZ_FWFZXX_DZQKXJFY + "\n";
            }
            newMb = mb;
        }
        //苏州根据不同的抵押方式生成不同的其他状况说明
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjzx()) && CommonUtil.indexOfStrs(Constants.DJZX_ZGE, bdcXm.getDjzx())
                && StringUtils.isNotEmpty(newMb)) {
            newMb = newMb.replace(Constants.SEMS_YBDY, Constants.SEMS_ZGEDY);
            newMb = newMb.replace(Constants.SJMS_YBDY, Constants.SJMS_ZGEDY);
        }
        return newMb;
    }

    @Override
    public String replaceMbUndisplayNull(String mb, String sql, BdcXm bdcXm, BdcBdcdy bdcBdcdy) {
        String newMb = "";
        String mbLastTemp = "";
        String lastMb = mb;
        Boolean sfxsYbdcqzh = true;
        String dwdm = AppConfig.getProperty("dwdm");
        String mb2 = StringUtils.upperCase(mb);
        if (bdcXm != null && bdcBdcdy != null) {
            List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(bdcXm.getProid());
            if (CollectionUtils.isEmpty(bdcZsList) && dwdm.equals("320900")) {
                sfxsYbdcqzh = false;//还未生成证书时不显示原不动产权证号
            }
        }
        if (StringUtils.isNotBlank(mb) && StringUtils.isNotBlank(sql) && bdcXm != null && bdcBdcdy != null) {
            String[] dbs = sql.split("；|;");
            //将数据源用;隔开成多条语句
            for (int i = 0; i < dbs.length; i++) {
                //不分大小写 替换项目id
                List<Map> list = bdcXtLimitfieldService.runSql(dbs[i].replaceAll("(?i)@proid", "'" + bdcXm.getProid() + "'").replaceAll("(?i)@bdcdyh", "'" + bdcBdcdy.getBdcdyh() + "'").replaceAll("(?i)@wiid", "'" + bdcXm.getWiid() + "'"));
                if (list != null && list.size() == 1) {
                    Map map = list.get(0);
                    //按map的key值字符串长度由高到低排序
                    TreeMap treeMap = new TreeMap(new StringLengthComparator());
                    treeMap.putAll(map);
                    Iterator j = treeMap.keySet().iterator();
                    while (j.hasNext()) {
                        Object o = j.next();
                        String key = o.toString();
                        if (map.get(key) != null) {
                            //对时间和数值的一些处理
                            mb = dealDataFormat(map, key, mb, sfxsYbdcqzh);
                        } else {
                            /**
                             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                             * @description 如果查询不出结果则不显示这条记录
                             */
                            String[] mbArray = mb.split("\\n");
                            if (mbArray.length > 0) {
                                if (map.size() > 1) {
                                    mb = mb.replaceAll("(?i)@" + key, "");
                                }
                                for (String mbTemp : mbArray) {
                                    if (StringUtils.indexOf(StringUtils.upperCase(mbTemp), key) == -1) {
                                        mb += mbTemp + "\n";
                                    }
                                }
                            }
                        }
                    }
                    //遍历map后若value全为空则不显示该条数据
                    Iterator j2 = treeMap.keySet().iterator();
                    while (j2.hasNext()) {
                        String key2 = j2.next().toString();
                        mb2 = mb2.replaceAll("@" + key2, "");
                    }
                    if (StringUtils.equals(mb2, mb)) {
                        mb = "";
                    }
                } else {
                    //针对项目内多幢的会有多个list
                    for (int a = 0; a <= list.size(); a++) {
                        if (a >= list.size()) {
                            break;
                        }
                        if (a > 0 && i == 0) {
                            mb = lastMb;
                        }
                        Map map = list.get(a);
                        //按map的key值字符串长度由高到低排序
                        TreeMap treeMap = new TreeMap(new StringLengthComparator());
                        treeMap.putAll(map);
                        for (Object object : treeMap.keySet()) {
                            String key = object.toString();
                            String[] mbArray = mb.split("\\n");

                            if (map.get(key) != null) {
                                String mbIngredient = i == 0 ? mb : mbArray[a];
                                //对时间和数值的一些处理
                                mbIngredient = dealDataFormat(map, key, mbIngredient, sfxsYbdcqzh);
                                if (i == 0) {
                                    mb = mbIngredient;
                                } else {
                                    mbArray[a] = mbIngredient;
                                }
                            } else {
                                /**
                                 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                                 * @description 如果查询不出结果则不显示这条记录
                                 */
                                String[] mbArray2 = mb.split("\\n");
                                if (mbArray2.length > 0) {
                                    if (map.size() > 1) {
                                        mb = mb.replaceAll("(?i)@" + key, "");
                                    }
                                    for (String mbTemp : mbArray2) {
                                        if (StringUtils.indexOf(StringUtils.upperCase(mbTemp), key) == -1) {
                                            mb += mbTemp + "\n";
                                        }
                                    }
                                }
                                if (i > 0) {
                                    mbArray[a] = mb;
                                }
                            }
                            //遍历map后若value全为空则不显示该条数据
                            Iterator j2 = list.get(a).keySet().iterator();
                            while (j2.hasNext()) {
                                String key2 = j2.next().toString();
                                mb2 = mb2.replaceAll("@" + key2, "");
                            }
                            if (StringUtils.equals(mb2, mb)) {
                                mb = "";
                                mbArray[a] = "";
                            }
                            if (i > 0 && (mbArray != null || (mbArray == null && mbArray.length != 0))) {
                                mb = "";
                                for (String s : mbArray) {
                                    mb += s + '\n';
                                }
                            }
                        }
                        if (i == 0) {
                            mbLastTemp += mb + "\n";
                        }
                    }
                    if (StringUtils.isNotBlank(mbLastTemp) && i == 0) {
                        mb = mbLastTemp;
                    }
                }
            }

            /**
             * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
             * @description sql查询结果不存在则不显示该配置模板
             */
            //权利其他状况目前有字段保存数值了
            String[] mbArray = mb.split("\\n");
            if (mbArray.length > 0) {
                StringBuilder mbBuilder = new StringBuilder();
                for (String mbTemp : mbArray) {
                    if (StringUtils.isNotEmpty(mbTemp) && mbTemp.indexOf("@") == -1) {
                        mbBuilder.append(mbTemp).append("\n");
                    }
                }
                mb = mbBuilder.toString();
            }
            newMb = mb;
        }
        //苏州根据不同的抵押方式生成不同的其他状况说明
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjzx()) && CommonUtil.indexOfStrs(Constants.DJZX_ZGE, bdcXm.getDjzx())
                && (StringUtils.isNotEmpty(newMb))) {
            newMb = newMb.replace(Constants.SEMS_YBDY, Constants.SEMS_ZGEDY);
            newMb = newMb.replace(Constants.SJMS_YBDY, Constants.SJMS_ZGEDY);
        }
        return newMb;
    }
    //对时间和数值的一些处理
    String dealDataFormat (Map map, String key, String mb, boolean sfxsYbdcqzh){
        if (map.get(key) instanceof Date){
            mb = mb.replaceAll("(?i)@" + key, CalendarUtil.sdf_China.format((Date) map.get(key)));
        } else if (map.get(key) instanceof BigDecimal){
            if(!"ZCS".equals(key) && !"SZC".equals(key) ) {
                //债权数额等数据精确到6位小数
                if(BDBZZQSE.equals(key)||BDCJZ.equals(key)||ZGZQQDSE.equals(key)){
                    DecimalFormat df = new DecimalFormat("##############0.######");
                    String tempValue = df.format(((BigDecimal) map.get(key)).doubleValue());
                    mb = mb.replaceAll("(?i)@" + key, tempValue);
                }else {
                    DecimalFormat df = new DecimalFormat("##############0.00");
                    String tempValue = df.format(((BigDecimal) map.get(key)).doubleValue());
                    mb = mb.replaceAll("(?i)@" + key, tempValue);
                }
            }
            else{
                mb = mb.replaceAll("(?i)@" + key, CommonUtil.formatTwoNumber(((BigDecimal) map.get(key)).doubleValue()).toString().split("\\.")[0]);
            }
        } else if( "YBDCQZH".equals(key) &&!sfxsYbdcqzh){
            String[] mbArray = mb.split("\\n");
            if (mbArray.length > 0) {
                StringBuilder mbBuilder = new StringBuilder();
                for (String mbTemp : mbArray) {
                    if (StringUtils.indexOf(StringUtils.upperCase(mbTemp), key) == -1) {
                        mbBuilder.append(mbTemp).append("\n");
                    }
                }
                mb = mbBuilder.toString();
            }
        } else{
            //不分大小写
            mb = mb.replaceAll("(?i)@" + key, map.get(key).toString());
        }
        return mb;
    }
}

