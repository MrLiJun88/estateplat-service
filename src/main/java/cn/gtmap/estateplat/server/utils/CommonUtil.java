package cn.gtmap.estateplat.server.utils;


import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.config.AppConfigPlaceholderConfigurer;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by trr on 2016/9/26.
 */
public class CommonUtil extends DateUtils {
    private static Logger logger = Logger.getLogger(AppConfigPlaceholderConfigurer.class);
    private static final List<FastDateFormat> CUSTOM_FORMATS = Lists.newArrayList(JDK_TIME_FORMAT);

    public static String getCurrStrDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(new Date());
        return str;
    }

    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(date);
        return str;
    }

    public static Date getDateParse(String dateString, String parse) throws ParseException {
        DateFormat format1 = new SimpleDateFormat(parse);
        Date date = format1.parse(dateString);
        return date;
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-11-03
     * @description 返回指定格式的date
     */
    public static Date getCurrDate() {
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            logger.error("msg", e);
            throw new AppException(e);
        }
        return date;
    }

    public static String getCurrDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
        return str;
    }

    public static String getCurrStrDateForNYR() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(new Date());
        return str;
    }

    public static String writeEmptyExcel(String fileName) {
        String excelName;
        String msg;
        String filePath = AppConfig.getProperty("exportFilePath");
        String middleFilePath = getCurrStrDate();
        String timeStamp = StringUtils.EMPTY;
        String fileNameTemp = StringUtils.EMPTY;
        if (fileName.contains("${timeStamp:")) {
            String[] fileNames = fileName.split("timeStamp:");
            if (fileNames.length > 1) {
                fileNameTemp = fileNames[0].substring(0, fileNames[0].length() - 2);
                timeStamp = fileNames[1].substring(0, fileNames[1].length() - 1);
            } else {
                fileNameTemp = StringUtils.EMPTY;
                timeStamp = fileNames[0].substring(0, fileNames[0].length() - 1);
            }
        } else {
            fileNameTemp = fileName;
        }
        String filePathAndName = filePath + File.separator + middleFilePath;
        try {
            if (!(new File(filePath).isDirectory())) {
                new File(filePath).mkdir();
                new File(filePathAndName).mkdir();
            } else {
                new File(filePathAndName).mkdir();
            }
            if (StringUtils.isNotBlank(timeStamp) && !StringUtils.equalsIgnoreCase(timeStamp, "noTime")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(timeStamp);
                    middleFilePath = sdf.format(new Date());
                } catch (Exception e) {
                    logger.error("msg", e);
                }
            } else if (StringUtils.equalsIgnoreCase(timeStamp, "noTime") && StringUtils.isNotBlank(fileNameTemp)) {
                middleFilePath = StringUtils.EMPTY;
            }
            excelName = fileNameTemp + middleFilePath + ".xls";
            File myFile = new File(filePathAndName + "\\" + excelName);
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            msg = filePathAndName + "@" + excelName;
        } catch (Exception e) {
            msg = "文件新建失败！";
            logger.error("msg", e);
        }
        return msg;
    }

    public static String writeStrToExcel(String exceltxt, String fileName) {
        String excelName;
        String msg;
        String filePath = AppConfig.getProperty("exportFilePath");
        String middleFilePath = getCurrStrDate();
        String timeStamp = StringUtils.EMPTY;
        String fileNameTemp = StringUtils.EMPTY;
        if (fileName.contains("${timeStamp:")) {
            String[] fileNames = fileName.split("timeStamp:");
            if (fileNames.length > 1) {
                fileNameTemp = fileNames[0].substring(0, fileNames[0].length() - 2);
                timeStamp = fileNames[1].substring(0, fileNames[1].length() - 1);
            } else {
                fileNameTemp = StringUtils.EMPTY;
                timeStamp = fileNames[0].substring(0, fileNames[0].length() - 1);
            }
        } else {
            fileNameTemp = fileName;
        }
        String filePathAndName = filePath + File.separator + middleFilePath;
        try {
            if (!(new File(filePath).isDirectory())) {
                new File(filePath).mkdir();
                new File(filePathAndName).mkdir();
            } else {
                new File(filePathAndName).mkdir();
            }
            if (StringUtils.isNotBlank(timeStamp) && !StringUtils.equalsIgnoreCase(timeStamp, "noTime")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(timeStamp);
                    middleFilePath = sdf.format(new Date());
                } catch (Exception e) {
                    logger.error("msg", e);
                }
            } else if (StringUtils.equalsIgnoreCase(timeStamp, "noTime") && StringUtils.isNotBlank(fileNameTemp)) {
                middleFilePath = StringUtils.EMPTY;
            }
            excelName = fileNameTemp + middleFilePath + ".xls";
            File myFile = new File(filePathAndName + "\\" + excelName);
            FileOutputStream fos = new FileOutputStream(myFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(exceltxt);
            osw.flush();
            osw.close();
            fos.close();
            fos.flush();
            msg = filePathAndName + "@" + excelName;
        } catch (Exception e) {
            msg = "文件导出失败！";
            logger.error("msg", e);
        }
        return msg;
    }

    public static String writeStrToExcelFromExcel(Workbook work, String fileName) {
        String excelName;
        String msg;
        String filePath = AppConfig.getProperty("exportFilePath");
        String middleFilePath = getCurrStrDate();
        String filePathAndName = filePath + File.separator + getCurrStrDateForNYR().replaceAll("-", "");
        try {
            if (!(new File(filePath).isDirectory())) {
                new File(filePath).mkdir();
                new File(filePathAndName).mkdir();
            } else {
                new File(filePathAndName).mkdir();
            }
            String suffix = work instanceof HSSFWorkbook ? ".xls" : ".xlsx";
            excelName = fileName + middleFilePath + suffix;
            File myFile = new File(filePathAndName + "\\" + excelName);
            FileOutputStream fos = new FileOutputStream(myFile);
            work.write(fos);
            fos.close();
            fos.flush();
            msg = filePathAndName + "@" + excelName;
        } catch (Exception e) {
            msg = "文件导出失败！";
            logger.error("msg", e);
        }
        return msg;
    }

    public static Date parse(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }

        if (value instanceof Date) {
            return (Date) value;
        }

        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }

        if (value instanceof String) {
            String strVal = ((String) value).trim();
            if (strVal.length() == 0) {
                return null;
            } else if (strVal.indexOf('-') != -1) {
                FastDateFormat format;
                switch (strVal.length()) {
                    case 10:
                        format = DATE_FORMAT;
                        break;
                    case 16:
                        format = DATEMIN_FORMAT;
                        break;
                    case 19:
                        format = strVal.indexOf('T') > -1 ? DateFormatUtils.ISO_DATETIME_FORMAT : DATETIME_FORMAT;
                        break;
                    default:
                        format = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;
                }
                try {
                    return format.parse(strVal);
                } catch (ParseException ignored) {
                    logger.error("errorMsg:", ignored);
                }
            } else {
                try {
                    return new Date(Long.parseLong(strVal));
                } catch (NumberFormatException ignored) {
                    logger.error("errorMsg:", ignored);
                }
            }
            for (FastDateFormat fdf : CUSTOM_FORMATS) {
                try {
                    return fdf.parse(strVal);
                } catch (ParseException ignored) {
                    logger.error("errorMsg:", ignored);
                }
            }
        }
        throw new IllegalArgumentException("Can not cast to Date, value : " + value);
    }

    public void deleteFile(String Name, File myFile) {
        if (myFile.isDirectory()) {
            File[] files = myFile.listFiles();
            for (File file2 : files) {
                deleteFile(Name, file2);
            }
        } else {
            String name = myFile.getName();
            if (Name.equals(name)) {
                logger.info(myFile.getAbsolutePath());
                boolean result = myFile.delete();
                if (result) {
                    logger.info("删除文件成功");
                } else {
                    logger.info("删除文件失败");
                }
                logger.info(myFile);
            }
        }
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-06-07
     * @description 用set.entrySet()遍历map，效率更高
     */
    public static Map<String, Object> turnStrToMap(String smap) {
        Map<String, Object> ps = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.fromObject(smap);
        Iterator iterator = jsonObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object value = entry.getValue();
            if ("null".equals(value)) {
                value = StringUtils.EMPTY;
            }
            ps.put((String) entry.getKey(), value);
        }
        return ps;
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法
     */
    public static String ternaryOperator(Object object) {
        return object != null ? object.toString() : StringUtils.EMPTY;
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法
     */
    public static String ternaryOperatorNotBlank(Object object) {
        String str = ternaryOperator(object);
        return StringUtils.isNotBlank(str) ? str : "/";
    }

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn;">xiejianan</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法, 添加默认值写入
     */
    public static String ternaryOperator(Object object, String defaultValue) {
        String str = object != null ? object.toString() : ternaryOperator(defaultValue);
        return StringUtils.isNotBlank(str) ? str : ternaryOperator(defaultValue);
    }

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn;">xiejianan</a>
     * @version 1.0, 2017-06-08
     * @description 封装三目运算符转换公共方法，适用于获取非字符串数据
     */
    public static <T> T ternaryOperator(Object object, Object defaultValue) {
        return object != null ? (T) object : (T) defaultValue;
    }


    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-08-30
     * @description 判断某个字符串在某一字符串中出现多少次
     */
    public static int getDisplayTimes(String str, String s) {
        int count = str.length() - str.replace(s, "").length();
        return count;
    }

    /**
     * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
     * @version 1.0, 2017-08-30
     * @description 获取某个字符在某个
     */
    public static int getCharacterPosition(String string, String s, int psition) {
        //这里是获取"-"符号的位置
        Matcher slashMatcher = Pattern.compile(s).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == psition) {
                break;
            }
        }
        return slashMatcher.start();
    }


    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * @param a
     * @param b
     * @return boolean
     * @author <a href ="mailto:liangqing@gtmap.cn"></a>
     * @version 1.3
     * @date 10:00 2017/11/21
     * @description 验证某个字符串是否包含在数组中
     */
    public static boolean indexOfStrs(String[] a, String b) {
        boolean msg = false;
        if (a != null) {
            for (String temp : a) {
                if (StringUtils.equals(temp, b)) {
                    msg = true;
                    break;
                }
            }
        }
        return msg;
    }

    /**
     * @param str String
     * @return boolean
     * @author <a href ="mailto:xiejianan@gtmap.cn"></a>
     * @date: Created in 17:03 2017/12/6
     * description 严格检测字符串匹配true，大小写不限
     * @version 1.3
     */
    public static boolean equalsExcatTrueIgnoreCase(String str) {
        return StringUtils.equalsIgnoreCase(str, String.valueOf(Boolean.TRUE));
    }

    /**
     * @param str String
     * @return boolean
     * @author <a href ="mailto:xiejianan@gtmap.cn"></a>
     * @date: Created in 17:03 2017/12/6
     * description 严格检测字符串匹配false，大小写不限
     * @version 1.3
     */
    public static boolean equalsExcatFalseIgnoreCase(String str) {
        return StringUtils.equalsIgnoreCase(str, String.valueOf(Boolean.FALSE));
    }

    public static String removeDuplicateStr(String str, String splitStr) {
        if (StringUtils.isNotBlank(str)) {
            LinkedHashSet<String> set = new LinkedHashSet();
            String[] strs = str.split(splitStr);
            StringBuilder finalStr = new StringBuilder();
            for (int i = 0; i < strs.length; i++) {
                set.add(strs[i]);
            }
            for (Object strTemp : set.toArray()) {
                if (finalStr.length() > 0) {
                    finalStr.append(splitStr);
                }
                finalStr.append(strTemp.toString());
            }
            return finalStr.toString();
        } else {
            return ternaryOperator(str);
        }
    }

    public static String getFile(String fileName) {
        String excelName;
        String msg;
        String filePath = AppConfig.getProperty("exportFilePath");
        String middleFilePath = getCurrStrDate();
        String timeStamp = StringUtils.EMPTY;
        String fileNameTemp = StringUtils.EMPTY;
        if (fileName.contains("${timeStamp:")) {
            String[] fileNames = fileName.split("timeStamp:");
            if (fileNames.length > 1) {
                fileNameTemp = fileNames[0].substring(0, fileNames[0].length() - 2);
                timeStamp = fileNames[1].substring(0, fileNames[1].length() - 1);
            } else {
                fileNameTemp = "";
                timeStamp = fileNames[0].substring(0, fileNames[0].length() - 1);
            }
        } else {
            fileNameTemp = fileName;
        }
        String filePathAndName = filePath + File.separator + middleFilePath;
        try {
            if (!(new File(filePath).isDirectory())) {
                new File(filePath).mkdir();
                new File(filePathAndName).mkdir();
            } else {
                new File(filePathAndName).mkdir();
            }
            if (StringUtils.isNotBlank(timeStamp) && !StringUtils.equalsIgnoreCase(timeStamp, "noTime")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(timeStamp);
                    middleFilePath = sdf.format(new Date());
                } catch (Exception e) {
                    logger.error("msg", e);
                }
            } else if (StringUtils.equalsIgnoreCase(timeStamp, "noTime") && StringUtils.isNotBlank(fileNameTemp)) {
                middleFilePath = "";
            }
            excelName = fileNameTemp + middleFilePath + ".xls";
            File myFile = new File(filePathAndName + "\\" + excelName);
            FileOutputStream fos = new FileOutputStream(myFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.flush();
            osw.close();
            fos.close();
            fos.flush();
            msg = filePathAndName + "@" + excelName;
        } catch (Exception e) {
            msg = "文件导出失败！";
            logger.error("msg", e);
        }
        return msg;
    }

    /**
     * @param list      待分割的list
     * @param sizeLimit 每个分割后的列表的大小
     * @return 分割后的列表
     * @description 按列表大小将大列表分割为小列表
     */
    public static Map<String, List> seperateBigListSmallList(List list, int sizeLimit) {
        return seperateBigListSmallList(list, sizeLimit, "list");
    }

    /**
     * @param list      待分割的list
     * @param sizeLimit 每个分割后的列表的大小
     * @return 分割后的列表
     * @description 按列表大小将大列表分割为小列表
     */
    public static Map<String, List> seperateBigListSmallList(List list, int sizeLimit, String prefix) {
        Map<String, List> map = new LinkedHashMap<String, List>();
        if (CollectionUtils.isNotEmpty(list)) {
            // 获取列表长度
            int size = CollectionUtils.size(list);
            // 计算列表分割次数
            int times = size / sizeLimit + (size % sizeLimit > 0 ? 1 : 0);
            for (int i = 0; i < times; i++) {
                map.put(prefix + i, list.subList(i * sizeLimit, (i + 1) * sizeLimit < size ? (i + 1) * sizeLimit : size));
            }
        }
        return map;
    }

    /**
     * @Description: 获取URL中参数值
     * @Param:
     * @return:
     * @Author: ww
     * @Date: 2018/10/24
     */
    public static String getUrlParameters(String url, String para) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(para)) {
            return null;
        }
        String paraStr = url.contains("?") ? url.substring(url.indexOf("?") + 1) : "";
        String[] paraArr = StringUtils.split(url, "&");
        if (paraArr.length > 0) {
            for (int i = 0; i < paraArr.length; i++) {
                String[] p = StringUtils.split(paraArr[i], "=");
                if (p.length > 0 && StringUtils.equals(p[0], para)) {
                    return p[1];
                }
            }
        }
        return null;
    }

    /**
     * 组织查询条件
     *
     * @param keyId
     * @param param
     * @return
     */
    public static String conbineQueryCondition(String keyId, Map<String, List> param) {
        StringBuilder sql = new StringBuilder("(");
        // 计数使用，用来判断是否需要拼接or
        int i = 0;
        // 拼接查询条件
        for (String key : param.keySet()) {
            if (i > 0) {
                sql.append(" or ");
            }
            // 查询条件使用别名
            sql.append(keyId).append(" in (:").append(key).append(")");
            i++;
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * version 1.3, 2018/9/10 15:09 description 获取colb字段值
     *
     * @param
     * @return
     * @author <a href ="mailto:xiejianan@gtmap.cn"></a>
     */
    public static String clobToString(Clob clob) throws SQLException, IOException {

        String reString = "";
        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s).append("\r\n");
            s = br.readLine();
        }
        reString = sb.toString();
        is.close();
        return reString;
    }

    /**
     * 将新的List放入返回对象map中，并重设分页参数
     *
     * @param returnMap  当前分页返回值对象 可为空
     * @param resultList 所有数据集
     * @param pageParam  分页参数——page，size，loadTotal
     * @return void
     * @date 2019.05.15 13:50
     * @author hanyaning
     */
    public static void fillPagemapByNewdata(Map returnMap, List resultList, Map pageParam) {
        if (returnMap == null) {
            returnMap = new HashMap();
        }
        if (resultList == null) {
            resultList = new ArrayList();
        }
        // 分页参数信息
        int page = Integer.parseInt(CommonUtil.ternaryOperator(pageParam.get("page"), "1"));
        int size = Integer.parseInt(CommonUtil.ternaryOperator(pageParam.get("rows"), "30"));
        boolean loadTotal = Boolean.parseBoolean(CommonUtil.ternaryOperator(pageParam.get("loadTotal"), "true"));

        // 当前returnMap包含信息
        int total = 0;
        int records = 0;
        List currectRows = new ArrayList();
        if (returnMap.get("rows") != null) {
            currectRows.addAll((ArrayList) returnMap.get("rows"));
        }
        records += resultList.size();

        // 当前resurnMap中的行数
        int begin = (page - 1) * size;
        int end = page * size;
        if (end > records) {
            end = records;
        }
        // 需要加入的行数，取list中的数据补充
        if (end > begin) {
            currectRows.addAll(resultList.subList(begin, end));
        }
        // 分别计算total
        if (loadTotal) {
            total = ((0 != (records % size)) || (0 == records)) ? ((records / size) + 1)
                    : (records / size);
        } else {
            total = -1;
        }

        returnMap.put("total", total);
        returnMap.put("page", page);
        returnMap.put("size", size);
        returnMap.put("records", records);
        returnMap.put("rows", currectRows);
    }

    /**
     * 将json对象中包含的null和JSONNull属性修改成""
     *
     * @param jsonObj
     */
    public static JSONObject filterNull(JSONObject jsonObj) {
        Iterator<String> it = jsonObj.keys();
        Object obj = null;
        String key = null;
        while (it.hasNext()) {
            key = it.next();
            obj = jsonObj.get(key);
            if (obj instanceof JSONObject) {
                filterNull((JSONObject) obj);
            }
            if (obj instanceof JSONArray) {
                JSONArray objArr = (JSONArray) obj;
                for (int i = 0; i < objArr.size(); i++) {
                    filterNull(objArr.getJSONObject(i));
                }
            }
            if (obj == null || obj instanceof JSONNull) {
                jsonObj.put(key, "");
            }
            if (obj.equals(null)) {
                jsonObj.put(key, "");
            }
        }
        return jsonObj;
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Author xiejianan
     * @Description
     * @Date 16:36 2019/3/7
     * @Param [list, key]
     */
    public static List<String> stringListFromMapList(List<Map> list, String key) {
        LinkedHashSet<String> strList = new LinkedHashSet<String>();
        String keyValue;
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map map : list) {
                keyValue = MapUtils.getString(map, key);
                if (StringUtils.isNotBlank(keyValue)) {
                    strList.add(keyValue);
                }
            }
        }
        return new ArrayList<String>(strList);
    }

    /**
     * @return
     * @Author xiejianan
     * @Description 将map列表按照主要信息分类
     * @Date 17:04 2019/8/14
     * @Param [list, key]
     */
    public static Map<String, List<Map>> divideMapByKey(List<Map> list, String key, boolean removeKey) {
        String keyValue;
        Map<String, List<Map>> resultMap = Maps.newHashMap();
        List<Map> dividedMapList;
        for (Map map : list) {
            keyValue = MapUtils.getString(map, key);
            if (StringUtils.isNotBlank(keyValue)) {
                dividedMapList = (List<Map>) MapUtils.getObject(resultMap, keyValue);
                if (dividedMapList == null) {
                    dividedMapList = Lists.newArrayList();
                }
                if (removeKey) {
                    map.remove(keyValue);
                }
                dividedMapList.add(map);
                resultMap.put(keyValue, dividedMapList);
            }
        }
        return resultMap;
    }

    /**
     * @return java.util.List
     * @Author xiejianan
     * @Description 通用方法，处理list里的内容，一般是map集合的重新组织，可能是命名不同，可能是显示属性的过滤
     * @Date 20:15 2019/3/26
     * @Param [list, resultFields, targetFields]
     */
    public static List generateNewList(List list, String resultFields, String targetFields) {
        List newList = Lists.newArrayList();
        String[] resultFieldArr = org.apache.commons.lang.StringUtils.split(resultFields, ",");
        String[] targetFieldArr = org.apache.commons.lang.StringUtils.split(targetFields, ",");
        if (CollectionUtils.isNotEmpty(list) && resultFieldArr != null && targetFieldArr != null) {
            if (resultFieldArr.length > 0 && targetFieldArr.length > 0 && resultFieldArr.length == targetFieldArr.length) {
                Map temp;
                Map resultMap;
                for (Object obj : list) {
                    temp = (Map) obj;
                    resultMap = generateNewMap(temp, resultFields, targetFields);
                    newList.add(resultMap);
                }
            } else if (resultFieldArr.length == 0 || targetFieldArr.length == 0) {
                logger.error("errorMsg:resultFields 或者 targetFields 参数为空");
            } else if (resultFieldArr.length != targetFieldArr.length) {
                logger.error("errorMsg:resultFields 或者 targetFields 参数个数不一致");
            }
        }
        return newList;
    }

    /**
     * @return java.util.Map
     * @Author xiejianan
     * @Description 通过指定字段，将map里的部分信息提取出来
     * @Date 17:30 2019/8/14
     * @Param [map, resultFields, targetFields]
     */
    public static Map generateNewMap(Map map, String resultFields, String targetFields) {
        String[] resultFieldArr = org.apache.commons.lang.StringUtils.split(resultFields, ",");
        String[] targetFieldArr = org.apache.commons.lang.StringUtils.split(targetFields, ",");
        int size = resultFieldArr.length;
        Map resultMap = Maps.newHashMap();
        for (int i = 0; i < size; i++) {
            if (map.get(targetFieldArr[i]) instanceof Collection) {
                resultMap.put(resultFieldArr[i], MapUtils.getObject(map, targetFieldArr[i], new ArrayList<>()));
            } else {
                //在此加入你的业务逻辑
                resultMap.put(resultFieldArr[i], MapUtils.getObject(map, targetFieldArr[i], "").toString());
            }
        }
        return resultMap;
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

    //将map中key全部转为小写
    public static Map MapKeyUpperToLower(Map target) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (MapUtils.isNotEmpty(target)) {
            Set<String> keyList = target.keySet();
            for (String key : keyList) {
                if (target.get(key) != null) {
                    resultMap.put(key.toLowerCase(), target.get(key));
                } else {
                    resultMap.put(key.toLowerCase(), StringUtils.EMPTY);
                }
            }
        }
        return resultMap;
    }

    //将map中key全部转为小写
    public static List<Map> MapKeyLowerToUpper(List<Map> target) {
        List<Map> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(target)) {
            for (Map jgResult : target) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                Set<String> jgResultKeyList = jgResult.keySet();
                for (String set : jgResultKeyList) {
                    if (jgResult.get(set) != null) {
                        resultMap.put(set.toUpperCase(), jgResult.get(set));
                    } else {
                        resultMap.put(set.toUpperCase(), StringUtils.EMPTY);
                    }
                }
                resultList.add(resultMap);
            }
        }
        return resultList;
    }

}
