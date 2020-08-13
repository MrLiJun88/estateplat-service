package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import com.rabbitmq.tools.json.JSONReader;
import com.rabbitmq.tools.json.JSONWriter;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version V1.0, 15-4-25
 */
public class GeometryOperationUtil {
    public static String msg;
    private static final Logger logger = LoggerFactory.getLogger(GeometryOperationUtil.class);
    private static final String PARAMETE_LAYERNAME = "layerName";
    private static final String PARAMETE_GEOMETRY = "geometry";
    private static final String PARAMETE_FEATURES = "features";
    private static final String PARAMETE_PROPERTIES = "properties";

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 构造函数
     */
    private GeometryOperationUtil(){

    }

    /**
     * 调用新数据中心的图形查询功能
     *
     * @param layerName   业务类型
     * @param whereClause 查询条件
     * @param outFields   返回值
     * @return
     * @throws Exception
     */
    public static String queryGeometry(String layerName, String whereClause, String outFields) throws Exception {
        String message = "";
        HttpClient client = null;
        PostMethod method = null;
        try {
            if (StringUtils.isNotBlank(layerName)) {
                //查询现有图形要素objectid
                String url = AppConfig.getProperty("omp.url") + "/geometryService/rest/query";
                client = new HttpClient();
                client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                method = new PostMethod(url);
                method.setRequestHeader("Connection", "close");
                method.addParameter(PARAMETE_LAYERNAME, layerName);
                if (StringUtils.isNotBlank(outFields)) {
                    outFields = outFields.toUpperCase();
                    if (!outFields.equals("*") && outFields.indexOf(ParamsConstants.OBJECTID_CAPITAL) < 0) {
                        outFields += ",OBJECTID";
                    }
                }
                method.addParameter("outFields", outFields);
                method.addParameter("where", whereClause);
                if (StringUtils.isNotBlank(AppConfig.getProperty("sde.db.dsname"))) {
                    method.addParameter("dataSource", AppConfig.getProperty("sde.db.dsname"));
                }
                client.executeMethod(method);
                //打印服务器返回的状态
                String returnJson = method.getResponseBodyAsString();
                if (StringUtils.isNotBlank(returnJson)) {
                    JSONReader jsonReader = new JSONReader();
                    Object json = jsonReader.read(returnJson);
                    if (json != null) {
                        HashMap resultMap = (HashMap) json;
                        if (!resultMap.containsKey(ParamsConstants.RESULT_LOWERCASE)) {
                            logger.info("尚未有图形数据！");
                        } else {
                            List<String> objList = getKeyListByGeoJson(resultMap.get(ParamsConstants.RESULT_LOWERCASE), ParamsConstants.OBJECTID_CAPITAL);
                            if (CollectionUtils.isNotEmpty(objList)) {
                                return resultMap.get(ParamsConstants.RESULT_LOWERCASE).toString();
                            } else {
                                message = "尚未有图形数据！";
                            }
                        }

                    }
                }
            } else {
                message = "ERROR:没有配置该【" + layerName + "】业务类型的图形参数！";
            }
        } catch (Exception e) {
            logger.error("GeometryOperationUtil.queryGeometry",e);
        }finally {
            if(method != null) {
                method.releaseConnection();
            }
            if(client != null) {
                ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            }
        }
        return message;
    }

    /**
     * 调用新数据中心的图形插入功能
     *
     * @param proid    项目id
     * @param busiType 业务类型
     * @param check    是否进行拓扑判断
     * @param geometry 图形数据
     * @return
     * @throws Exception
     */
    public static String insertGeometry(String proid, String busiType, String check, String geometry) throws Exception {
        String message = "";
        if (StringUtils.isNotBlank(busiType) && StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(geometry)) {
            String layerName = "";
            if (StringUtils.isNotBlank(layerName)) {
                String url = "";
                HttpClient client = new HttpClient();
                client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                PostMethod method = new PostMethod(url);
                method.setRequestHeader("Connection", "close");
                method.addParameter(PARAMETE_LAYERNAME, layerName);
                method.addParameter(PARAMETE_GEOMETRY, geometry);
                if (!(StringUtils.isNotBlank(check) && StringUtils.equals(check, "false"))) {
                    check = "true";
                }
                method.addParameter("check", check);
                client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
                client.executeMethod(method);
                //打印服务器返回的状态
                logger.info("服务器返回状态：" + method.getStatusLine());
                //打印服务器返回的数据
                String returnJson = method.getResponseBodyAsString();
                logger.info(returnJson);
                if (StringUtils.isNotBlank(returnJson)) {
                    JSONReader jsonReader = new JSONReader();
                    Object json = jsonReader.read(returnJson);
                    if (json != null) {
                        HashMap resultMap = (HashMap) json;
                        if (!resultMap.containsKey(ParamsConstants.RESULT_LOWERCASE)) {
                            logger.info("当前项目创建图形失败！");
                            message = returnJson;
                        } else {
                            message = "true";
                        }
                    }
                }
                method.releaseConnection();
                ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            } else {
                message = "gis.properties文件没有配置该【" + busiType + "】业务类型的图形参数！";
            }
        } else {
            message = "构建平台没有配置当前工作流对应的busiType！";
        }
        return message;
    }

    /**
     * 调用新数据中心的图形编辑功能
     *
     * @param proid    项目id
     * @param busiType 业务类型
     * @param param    更新数据的map
     * @return
     * @throws Exception
     */
    public static String updateGeometry(String proid, String busiType, HashMap<String, Object> param) throws Exception {
        String message = "";
        if (StringUtils.isNotBlank(proid)) {
            String whereClause = "PROID='" + proid + "'";
            return updateGeometry(proid, busiType, param, whereClause);
        }
        message = "项目id为空！";
        return message;
    }

    /**
     * 调用新数据中心的图形编辑功能
     *
     * @param proid       项目id
     * @param busiType    业务类型
     * @param param       更新数据的map
     * @param whereClause 查询条件
     * @return
     * @throws Exception
     */
    public static String updateGeometry(String proid, String busiType, HashMap<String, Object> param, String whereClause) throws Exception {
        String message = "";
        if (StringUtils.isNotBlank(busiType)) {
            String layerName = "";
            String geoJson = queryGeometry(busiType, whereClause, "*");
            if (StringUtils.isNotBlank(geoJson)) {
                FeatureJSON queryJson = new FeatureJSON();
                FeatureCollection queryCollection = queryJson.readFeatureCollection(geoJson);
                if (queryCollection != null && queryCollection.size() > 0) {
                    FeatureIterator queryIterator = queryCollection.features();
                    while (queryIterator.hasNext()) {
                        Feature feature = queryIterator.next();
                        //更新属性数据
                        if (param != null) {
                            Iterator it = param.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry entry = (Map.Entry) it.next();
                                Object key = entry.getKey();
                                Object value = entry.getValue();
                                if (feature.getProperty(key.toString()) != null) {
                                    feature.getProperty(key.toString()).setValue(value);
                                }
                            }
                        }

                        String objectId = feature.getProperty(ParamsConstants.OBJECTID_CAPITAL).getValue().toString();
                        String url = "";
                        HttpClient client = new HttpClient();
                        client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                        client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                        PostMethod method = new PostMethod(url);
                        method.setRequestHeader("Connection", "close");
                        method.addParameter(PARAMETE_LAYERNAME, layerName);
                        method.addParameter("primaryKey", objectId);
                        String geometry = toFeatureJSON(feature);
                        if (StringUtils.isNotBlank(geometry)) {
                            method.addParameter(PARAMETE_GEOMETRY, geometry);
                        }
                        int status = client.executeMethod(method);
                        //打印服务器返回的状态
                        logger.info("服务器返回的状态" + method.getStatusLine());
                        //打印服务器返回的数据
                        String returnJson = method.getResponseBodyAsString();
                        logger.info(returnJson);
                        if (StringUtils.isNotBlank(returnJson)) {
                            JSONReader jsonReader = new JSONReader();
                            Object json = jsonReader.read(returnJson);
                            if (json != null) {
                                HashMap resultMap = (HashMap) json;
                                if (!resultMap.containsKey(ParamsConstants.RESULT_LOWERCASE)) {
                                    message += returnJson + "\r\n";
                                    logger.info("更新图形失败！");
                                } else {
                                    logger.info("更新图形成功！");
                                }
                            }
                        }
                        method.releaseConnection();
                        ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                    }
                } else {
                    message = "当前项目尚未创建图形！";
                }
            }
        }
        return message;
    }

    /**
     * 解析
     *
     * @param geoJson
     * @param propertys 默认返回字段{"OBJECTID","PROID","PRONAME","REGIONCODE","SHAPE_AREA","OG_SHAPE_AREA","AREA"};
     *                  其中查询中面积字段使用的是AREA
     *                  分析中面积字段使用的是SHAPE_AREA","OG_SHAPE_AREA
     * @return
     * @throws Exception
     */
    public static List<HashMap<String, Object>> getGeometryProperty(String geoJson, String[] propertys) throws Exception {
        return getGeometryProperty(geoJson, propertys, false);
    }

    /**
     * 解析
     *
     * @param geoJson
     * @param propertys
     * @param merge     是否合并相同的objecdtid（要素）默认不进行合并
     *                  默认返回字段{"OBJECTID","PROID","PRONAME","REGIONCODE","SHAPE_AREA","OG_SHAPE_AREA","AREA"};
     *                  其中查询中面积字段使用的是AREA
     *                  分析中面积字段使用的是SHAPE_AREA","OG_SHAPE_AREA
     * @return
     * @throws Exception
     */
    public static List<HashMap<String, Object>> getGeometryProperty(String geoJson, String[] propertys, Boolean merge) throws Exception {
        List<HashMap<String, Object>> objList = new ArrayList<HashMap<String, Object>>();
        String message = "";
        try {
            HashMap<String, HashMap<String, Object>> param = new HashMap<String, HashMap<String, Object>>();
            if (StringUtils.isNotBlank(geoJson)) {
                List<String> tempList = getKeyListByGeoJson(geoJson, ParamsConstants.OBJECTID_CAPITAL);
                if (CollectionUtils.isNotEmpty(tempList)) {
                    FeatureJSON queryJson = new FeatureJSON();
                    FeatureCollection queryCollection = queryJson.readFeatureCollection(geoJson);
                    if (queryCollection != null && queryCollection.size() > 0) {
                        //处理默认值，默认赋值下面数据中的字段
                        String[] defaultProperties = {ParamsConstants.OBJECTID_CAPITAL, "SHAPE_AREA", "OG_SHAPE_AREA", "AREA"};
                        FeatureIterator queryIterator = queryCollection.features();
                        int index = 0;
                        while (queryIterator.hasNext()) {
                            Feature feature = queryIterator.next();
                            String objectId = feature.getProperty(ParamsConstants.OBJECTID_CAPITAL).getValue().toString();
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            for (int i = 0; i < defaultProperties.length; i++) {
                                if (feature.getProperty(defaultProperties[i]) != null) {
                                    map.put(defaultProperties[i], feature.getProperty(defaultProperties[i]).getValue());
                                }
                            }
                            param.put(objectId + "_" + index, map);
                        }
                    }
                } else {
                    message = "图层数据不存在！";
                }
            }
            if (param != null) {
                if (merge != null && merge) {
                    HashMap<String, HashMap<String, Object>> objParam = new HashMap<String, HashMap<String, Object>>();
                    //处理默认值，默认赋值下面数据中的字段
                    String[] areaProperties = {"SHAPE_AREA", "OG_SHAPE_AREA", "AREA"};
                    Set keySet = param.keySet();
                    for (Object obj : keySet) {
                        String key = obj.toString();
                        if (StringUtils.isNotBlank(key) && key.indexOf('_') > -1) {
                            String curObjectId = key.split("_")[0];
                            HashMap<String, Object> curMap = param.get(key);
                            //针对重复的objectid进行合并处理
                            if (objParam.containsKey(curObjectId)) {
                                HashMap<String, Object> map = objParam.get(curObjectId);
                                //开始对比处理上一个map和当前value，合并其中的属性值，如果是面积字段，则需要合计面积
                                for (int i = 0; i < areaProperties.length; i++) {
                                    if (curMap.containsKey(areaProperties[i]) && map.containsKey(areaProperties[i])) {
                                        map.put(areaProperties[i], CommonUtil.addBigDecimal(curMap.get(areaProperties[i]), map.get(areaProperties[i])));
                                    }
                                }
                                objParam.put(curObjectId, map);

                            } else {
                                objParam.put(curObjectId, curMap);
                            }
                        }
                    }
                    objList.addAll(objParam.values());
                } else {
                    objList.addAll(param.values());
                }
            }
        } catch (Exception e) {
            logger.error("GeometryOperationUtil.getGeometryProperty",e);
        }
        return objList;
    }

    /**
     * * 调用新数据中心的图形删除功能
     *
     * @param proid       项目id
     * @param busiType    业务类型
     * @param whereClause 查询条件
     * @return
     * @throws Exception
     * @return"OBJECTID："+analyobjectId+"；PROID："+PROID+"；叠加面积："+SHAPE_AREA+"；目标数据总面积：" +OG_SHAPE_AREA
     */
    public static String delGeometry(String proid, String busiType, String whereClause) throws Exception {
        StringBuilder message = new StringBuilder();
        if (StringUtils.isNotBlank(busiType)) {
            String layerName = "";
            String geoJson = queryGeometry(busiType, whereClause, "*");
            if (StringUtils.isNotBlank(geoJson)) {
                FeatureJSON queryJson = new FeatureJSON();
                FeatureCollection queryCollection = queryJson.readFeatureCollection(geoJson);
                if (queryCollection != null && queryCollection.size() > 0) {
                    FeatureIterator queryIterator = queryCollection.features();
                    String url = "";
                    while (queryIterator.hasNext()) {
                        Feature feature = queryIterator.next();
                        String objectId = feature.getProperty(ParamsConstants.OBJECTID_CAPITAL).getValue().toString();
                        HttpClient client = new HttpClient();
                        client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                        client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                        PostMethod method = new PostMethod(url);
                        method.setRequestHeader("Connection", "close");
                        method.addParameter(PARAMETE_LAYERNAME, layerName);
                        method.addParameter("primaryKey", objectId);
                        //打印服务器返回的状态
                        logger.info("服务器返回的状态" + method.getStatusLine());
                        //打印服务器返回的数据
                        String returnJson = method.getResponseBodyAsString();
                        logger.info(returnJson);
                        if (StringUtils.isNotBlank(returnJson)) {
                            JSONReader jsonReader = new JSONReader();
                            Object json = jsonReader.read(returnJson);
                            if (json != null) {
                                HashMap resultMap = (HashMap) json;
                                if (!resultMap.containsKey(ParamsConstants.RESULT_LOWERCASE)) {
                                    message.append(returnJson).append("\r\n");
                                    logger.info("删除失败！");
                                } else {
                                    logger.info("删除成功！");
                                }
                            }
                        }
                        method.releaseConnection();
                        ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                    }
                }
            } else {
                message = new StringBuilder("当前项目尚未创建图形！");
            }
        } else {
            message = new StringBuilder("构建平台没有配置当前工作流对应的busiType！");
        }
        return message.toString();
    }

    /**
     * 调用新数据中心的图形删除功能
     *
     * @param proid    项目id
     * @param busiType 业务类型
     * @throws Exception
     * @return"OBJECTID："+analyobjectId+"；PROID："+PROID+"；叠加面积："+SHAPE_AREA+"；目标数据总面积：" +OG_SHAPE_AREA
     */
    public static String delGeometry(String proid, String busiType) throws Exception {
        String message = "";
        if (StringUtils.isNotBlank(proid)) {
            String whereClause = "PROID='" + proid + "'";
            return delGeometry(proid, busiType, whereClause);
        }
        message = "项目id为空！";
        return message;
    }

    /**
     * 调用新数据中心的图形分析功能
     *
     * @param geoJson   源数据图形
     * @param layerName 目标图层数据源业务类型
     * @return
     * @throws Exception
     */
    public static String analysisGeometry(String geoJson, String layerName) throws Exception {
        String message = "";
        logger.info("分析开始......");

        if (StringUtils.isNotBlank(layerName)) {
            logger.info("分析目标图层：" + layerName);
            //开始进行分析
            String url = AppConfig.getProperty("omp.url") + "/geometryService/rest/intersect";
            //叠加分析
            HttpClient client = new HttpClient();
            client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
            client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
            PostMethod method = new PostMethod(url);
            method.setRequestHeader("Connection", "close");
            method.addParameter(PARAMETE_LAYERNAME, layerName);
            method.addParameter(PARAMETE_GEOMETRY, geoJson);
            method.addParameter("dataSource", "sgbwm");
            client.executeMethod(method);
            //打印服务器返回的数据
            String returnJson = method.getResponseBodyAsString();
            logger.error(returnJson);
            if (StringUtils.isNotBlank(returnJson)) {
                JSONReader jsonReader = new JSONReader();
                Object json = jsonReader.read(returnJson);
                if (json != null) {
                    HashMap resultMap = (HashMap) json;
                    if (resultMap.containsKey(ParamsConstants.RESULT_LOWERCASE)) {
                        List<String> objList = getKeyListByGeoJson(resultMap.get(ParamsConstants.RESULT_LOWERCASE), ParamsConstants.OBJECTID_CAPITAL);
                        if (CollectionUtils.isNotEmpty(objList)) {
                            return resultMap.get(ParamsConstants.RESULT_LOWERCASE).toString();
                        }
                    }
                }
            }
            method.releaseConnection();
            ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
        } else {
            message = "gis.properties文件没有配置该【" + layerName + "】业务类型的图形参数！";
        }
        logger.info("分析结束！");
        return message;
    }

    /**
     * 根据feature对象获取对应的geojson字符串
     *
     * @param feature
     * @return
     */
    public static String toFeatureJSON(final Object feature) {
        String geoJson = "";
        try {
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(14));
            StringWriter out = new StringWriter();
            if (feature instanceof SimpleFeature) {
                featureJSON.setEncodeFeatureBounds(((SimpleFeature) feature).getBounds() == null ? false : true);
                featureJSON.setEncodeFeatureCRS(((SimpleFeature) feature).getFeatureType().
                        getCoordinateReferenceSystem() == null ? false : true);
                featureJSON.writeFeature((SimpleFeature) feature, out);
            } else if (feature instanceof FeatureCollection) {
                if (((FeatureCollection) feature).size() > 0) {
                    featureJSON.setEncodeFeatureCollectionBounds(((SimpleFeature) ((FeatureCollection) feature).toArray()[0]).getBounds() == null ? false : true);
                    featureJSON.setEncodeFeatureCollectionCRS(((SimpleFeature) ((FeatureCollection) feature).toArray()[0]).getFeatureType().
                            getCoordinateReferenceSystem() == null ? false : true);
                }
                featureJSON.writeFeatureCollection((FeatureCollection) feature, out);
            }
            geoJson = out.toString();
        } catch (Exception e) {
            logger.error("GeometryOperationUtil.toFeatureJSON",e);
        }
        return geoJson;
    }

    /**
     * 获取Geometry对象的geoJson字符串
     *
     * @param geometry
     * @return
     */
    public static String toGeoJSON(Geometry geometry) {
        String geoJson = "";
        try {
            GeometryJSON geometryJSON = new GeometryJSON(14);
            StringWriter out = new StringWriter();
            geometryJSON.write(geometry, out);
            geoJson = out.toString();
        } catch (Exception e) {
            logger.error("GeometryOperationUtil.toGeoJSON",e);
        }
        return geoJson;
    }

    /**
     * 根据图形查询返回json字符串，解析出所有的objectid，返回list集合
     *
     * @param returnJson
     * @return
     * @throws Exception
     */
    public static List<String> getObjectIdListByReturnJson(String returnJson) throws Exception {
        List<String> objectidList = new ArrayList<String>();
        try {
            JSONReader jsonReader = new JSONReader();
            Object json = jsonReader.read(returnJson);
            if (json != null) {
                HashMap resultMap = (HashMap) json;
                if (resultMap.containsKey(ParamsConstants.RESULT_LOWERCASE) && resultMap.get(ParamsConstants.RESULT_LOWERCASE) != null) {
                    objectidList = getKeyListByGeoJson(resultMap.get(ParamsConstants.RESULT_LOWERCASE), ParamsConstants.OBJECTID_CAPITAL);
                }
            }
        } catch (Exception e) {
            logger.error("GeometryOperationUtil.getObjectIdListByReturnJson",e);
        }
        return objectidList;
    }

    /**
     * 根据图形查询返回json字符串，解析出所有的key，返回list集合
     *
     * @param geoJson
     * @param key
     * @return
     * @throws Exception
     */
    public static List<String> getKeyListByGeoJson(Object geoJson, String key) throws Exception {
        List<String> objectidList = new ArrayList<String>();
        try {
            if (geoJson != null && StringUtils.isNotBlank(geoJson.toString())) {
                HashMap geoMap = new HashMap();
                try {
                    JSONReader jsonReader = new JSONReader();
                    Object json = jsonReader.read(geoJson.toString());
                    if (json != null)
                        geoMap = (HashMap) json;
                } catch (Exception e) {
                    JSONWriter writer = new JSONWriter();
                    String write = writer.write(geoJson);
                    JSONReader jsonReader = new JSONReader();
                    Object json = jsonReader.read(write);
                    if (json != null)
                        geoMap = (HashMap) json;
                }
                if (geoMap.containsKey(PARAMETE_FEATURES) && geoMap.get(PARAMETE_FEATURES) != null) {
                    //解析FeatureCollection中的features
                    JSONWriter writer = new JSONWriter();
                    String write = writer.write(geoMap.get(PARAMETE_FEATURES));
                    JSONReader jsonReader = new JSONReader();
                    Object json = jsonReader.read(write);
                    if (json != null) {
                        List<HashMap> featureListMap = (List<HashMap>) json;
                        if (CollectionUtils.isNotEmpty(featureListMap)) {
                            for (int i = 0; i < featureListMap.size(); i++) {
                                //解析FeatureCollection中的features中的单个feature，获取其中的属性数据
                                HashMap featureMap = featureListMap.get(i);
                                if (featureMap.containsKey(PARAMETE_PROPERTIES) && featureMap.get(PARAMETE_PROPERTIES) != null) {
                                    JSONWriter propertiesWriter = new JSONWriter();
                                    String propertiesWrite = propertiesWriter.write(featureMap.get(PARAMETE_PROPERTIES));
                                    JSONReader propertiesJsonReader = new JSONReader();
                                    Object propertiesJson = propertiesJsonReader.read(propertiesWrite);
                                    HashMap propertyMap = (HashMap) propertiesJson;
                                    if (propertyMap.containsKey(key) && propertyMap.get(key) != null && StringUtils.isNotBlank(propertyMap.get(key).toString())) {
                                        objectidList.add(propertyMap.get(key).toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("GeometryOperationUtil.getKeyListByGeoJson",e);
        }
        return objectidList;
    }

    /**
     * 根据id和业务类型，获取对应的业务数据
     *
     * @param busiType
     * @param proid
     * @return
     * @throws Exception
     */
    public static HashMap<String, String> getBusiMap(String busiType, String proid) throws Exception {
        HashMap<String, String> busiMap = new HashMap<String, String>();
        try {
            if (StringUtils.isNotBlank(busiType) && StringUtils.isNotBlank(proid)) {
                String busiUrl = "";
                if (StringUtils.isNotBlank(busiUrl)) {
                    logger.info("业务数据获取URL地址：" + busiUrl);
                    HttpClient client = new HttpClient();
                    client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                    client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                    PostMethod method = new PostMethod(busiUrl);
                    method.setRequestHeader("Connection", "close");
                    method.addParameter("proid", proid);
                    client.executeMethod(method);
                    //打印服务器返回的状态
                    logger.info("服务器返回状态：" + method.getStatusLine());
                    //打印服务器返回的数据
                    String returnJson = method.getResponseBodyAsString();
                    logger.info(returnJson);
                    if (StringUtils.isNotBlank(returnJson)) {
                        returnJson = java.net.URLDecoder.decode(returnJson, "UTF-8");
                        JSONReader reader = new JSONReader();
                        Object json = reader.read(returnJson);
                        if (json != null) {
                            busiMap = (HashMap<String, String>) json;
                        }
                    }
                    method.releaseConnection();
                    ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                }
            }
        } catch (Exception e) {
            logger.error("GeometryOperationUtil.getBusiMap",e);
        }
        return busiMap;
    }

}

