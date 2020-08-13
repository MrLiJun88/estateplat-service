package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2016/3/10.
 */
public class PublicUtil {
    private static final Logger logger = LoggerFactory.getLogger(PublicUtil.class);

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 构造函数
     */
    private PublicUtil() {

    }

    /*
     * zwq 实现类似js的join功能，即在字符中间添加分隔符
     * split（分隔符）
     * */
    public static String join(String split, List<String> list) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i != list.size(); i++) {
                if (i == 0) {
                    sb.append(list.get(i));
                } else {
                    sb.append(split).append(list.get(i));
                }
            }
            result = CommonUtil.formatEmptyValue(sb);
        }
        return result;
    }

    /*
     * zwq 实体类中一个字段的所有值用分隔符分开获得一个字符串
     * split:分隔符
     * list:装实体类的list
     * fieldName:要组织的实体类成员变量名
     * */

    public static String voJoin(String split, List<?> list, String fieldName) {
        String msg = "";
        List<String> fieldList = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                try {
                    Class objectClass = object.getClass();
                    Field field = objectClass.getDeclaredField(fieldName);
                    String firstLetter = field.getName().substring(0, 1).toUpperCase();
                    String getMethod = "get" + firstLetter + field.getName().substring(1);
                    Method method = objectClass.getMethod(getMethod);
                    Object value = method.invoke(object, new Object[]{});
                    if (value != null)
                        fieldList.add(CommonUtil.formatEmptyValue(value));
                } catch (Exception e) {
                    logger.error("PublicUtil.voJoin", e);
                }
            }
        }
        msg = join(split, fieldList);
        return msg;
    }

    /**
     * @author bianwen
     * @description 数组转set
     */
    public static Set arrayToSet(String[] arr) {
        //数组-->Set
        return new HashSet<String>(Arrays.asList(arr));
    }

    /**
     * @param number
     * @return 百分比
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 处理权利比例百分比
     */
    public static String percentage(String number) {
        String percentage = "";
        if ((NumberUtils.isNumber(number))) {
            int qlbl = (int) (Double.parseDouble(number) * 100);
            percentage = qlbl + "%";
        }
        if (StringUtils.indexOf(number, "%") > -1) {
            percentage = number;
        }
        return percentage;
    }


    /**
     * @param input
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 判断 String 是否是 int
     */
    public static boolean isInteger(String input) {
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.find();
    }

    /**
     * @return
     * @description 合并字符串
     */
    public static String combineString(String a, String b) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(a) && StringUtils.isNotBlank(b)) {
            sb.append(a);
            sb.append("/");
            sb.append(b);
        }
        if (StringUtils.isBlank(sb)) {
            if (StringUtils.isNotBlank(a)) {
                sb.append(a);
            }
            if (StringUtils.isNotBlank(b)) {
                sb.append(b);
            }
        }
        return sb.toString();
    }


    public static List<Map<String, Object>> qcCheckMap(List<Map<String, Object>> resultList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        for (Map<String, Object> map : resultList) {
            if (CollectionUtils.isEmpty(list)) {
                list.add(map);
            } else {
                boolean sftj = false;
                for (Map<String, Object> map1 : list) {
                    if (map.containsKey("checkCode") && map1.containsKey("checkCode") && StringUtils.equals(map.get("checkCode").toString(), map1.get("checkCode").toString())) {
                        if (map.containsKey("checkPorids") && map1.containsKey("checkPorids")) {
                            if(map.get("checkPorids") instanceof String){
                                String checkPorids = String.valueOf(map.get("checkPorids"));
                                String checkPorids1 = String.valueOf(map1.get("checkPorids"));
                                if (StringUtils.equals(checkPorids,checkPorids1)) {
                                    sftj = true;
                                }
                            }else{
                                List<String> checkPorids = (List<String>) map.get("checkPorids");
                                List<String> checkPorids1 = (List<String>) map1.get("checkPorids");
                                if (!equals(checkPorids, checkPorids1)) {
                                    sftj = true;
                                }
                            }
                        }
                    } else {
                        sftj = true;
                    }
                }
                if (sftj) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    public static boolean equals(List aList, List bList) {
        if (aList.size() != bList.size()) {
            return false;
        }
        for (int i = 0; i < bList.size(); i++) {
            if (!aList.contains(bList.get(i))) {
                return false;
            }
        }
        return true;
    }

    //将map中key全部转为小写
    public static List<Map> MapKeyUpperToLower(List<Map> target) {
        List<Map> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(target)) {
            for (Map jgResult : target) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                Set<String> jgResultKeyList = jgResult.keySet();
                for (String set : jgResultKeyList) {
                    if (jgResult.get(set) != null) {
                        resultMap.put(set.toLowerCase(), jgResult.get(set));
                    } else {
                        resultMap.put(set.toLowerCase(), StringUtils.EMPTY);
                    }
                }
                resultList.add(resultMap);
            }
        }
        return resultList;
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-06-07
     * @description 用set.entrySet()遍历map，效率更高
     */
    public static Map<String, Object> turnStrToMap(String smap) {
      Map<String, Object> ps = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(smap);
        Iterator iterator = jsonObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object value = entry.getValue();
            if ("null".equals(value)) {
                value = "";
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                if (CollectionUtils.isNotEmpty(array)) {
                    if (array.get(0) instanceof Map) {
                        List<Map> collection = JSON.parseObject(array.toJSONString(), new TypeReference<List<Map>>() {
                        });
                        value = collection;
                    } else if (array.get(0) instanceof String
                            || array.get(0) instanceof Integer || array.get(0) instanceof Short || array.get(0) instanceof Long || array.get(0) instanceof Byte
                            || array.get(0) instanceof Double || array.get(0) instanceof Float
                            || array.get(0) instanceof Character) {
                        List list = Lists.newArrayList();
                        for (int i = 0; i < array.size(); i++) {
                            list.add(array.get(i));
                        }
                        value = list;
                    }
                }

            }
            ps.put((String) entry.getKey(), value);
        }
        return ps;
    }
}
