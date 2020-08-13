package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.model.server.core.BdcZdQlzt;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 15-9-7
 * Time: 下午9:03
 * Des:读取xml配置
 * To change this template use File | Settings | File Templates.
 */
public class ReadXmlProps {

    private static final Logger logger = LoggerFactory.getLogger(ReadXmlProps.class);
    private static final String PARAMETER_CLASSNAME = "classname";
    private static final String PARAMETER_CONF_SERVER_GDFWTDGDQL = "/conf/server/gdFwTdGdQl-props.xml";
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 构造函数
     */
    private ReadXmlProps(){

    }
    /**
     * 获取过渡房屋匹配时，如果申请类型为配置不匹配房屋土地的申请类型代码
     *
     * @return
     */
    public static List<String> getUnPpGdfwtdSqlxDm() {
        List<String> listAllNodeid = new ArrayList<String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/unPpGdfwtd-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getUnPpGdfwtdSqlxDm",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            List<Node> nodes = root.selectNodes(ParamsConstants.SQLXDM_LOWERCASE);
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    listAllNodeid.add(nodes.get(i).getText());
                }
            }
        }
        return listAllNodeid;
    }

    /**
     * zwq
     * 标准单位获取
     *
     * @return
     */
    public static HashMap<String, String> getdwYz() {
        HashMap hashMap = new HashMap();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/dwYz-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getdwYz",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Iterator i = root.elementIterator("qllx");
            while (i.hasNext()) {
                foo = (Element) i.next();
                if (foo != null)
                    hashMap.put(foo.elementText(ParamsConstants.QLLXDM_LOWERCASE), foo.elementText("dw"));
            }
        }
        return hashMap;
    }

	/**
	 * @author<a href="mailto:lijian@gtmap.cn">lijian</a>
	 * @param
	 * @return
	 * @description 读取楼盘表权利配置颜色
	 */
	public static List<BdcZdQlzt> getZdQlztList(){
		List<BdcZdQlzt> qlYsList = new ArrayList<BdcZdQlzt>();
		SAXReader sr = new SAXReader();
		Document document = null;
		try {
			document = sr.read(AppConfig.getEgovHome() + "/conf/server/qlYs.xml");
		} catch (DocumentException e) {
            logger.error("ReadXmlProps.getZdQlztList",e);
		}
		if (document != null) {
			Element root = document.getRootElement();
			Element foo;
			Iterator i = root.elementIterator("qllx");
			while (i.hasNext()) {
				foo = (Element) i.next();
				if (foo != null) {
					BdcZdQlzt bdcZdQlzt = new BdcZdQlzt();
					bdcZdQlzt.setDm(foo.elementText("dm"));
					bdcZdQlzt.setMc(foo.elementText("mc"));
					bdcZdQlzt.setColor(foo.elementText("color"));
					bdcZdQlzt.setSxh(foo.elementText("sxh"));
					qlYsList.add(bdcZdQlzt);
				}
			}
		}
		return qlYsList;
	}


    /**
     * zx
     * 根据权利类型获取标准单位
     *
     * @return
     */
    public static HashMap<String, String> getDwByQllx(String qllx) {
        HashMap hashMap = new HashMap();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/dwYz-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getDwByQllx",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Iterator i = root.elementIterator("qllx");
            while (i.hasNext()) {
                foo = (Element) i.next();
                if (foo != null&&StringUtils.equals(qllx, foo.elementText(ParamsConstants.QLLXDM_LOWERCASE))) {
                    hashMap.put(foo.elementText(ParamsConstants.QLLXDM_LOWERCASE), foo.elementText("dw"));
                    break;
                }
            }
        }
        return hashMap;
    }

    /**
     * 批量导入中英文对照表
     */
    public static HashMap getGdFw() {
        HashMap<String, String> gdFw = new HashMap<String, String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + PARAMETER_CONF_SERVER_GDFWTDGDQL);
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getGdFw",e);
        }
        if (document == null)
            throw new NullPointerException();
        Element root = document.getRootElement();

        Element element = root.element("gd_fw");
        for (Iterator it = element.elementIterator(); it.hasNext(); ) {
            Element element1 = (Element) it.next();
            gdFw.put(element1.getText(), element1.getName());
        }
        gdFw.put(PARAMETER_CLASSNAME, "gd_fw");
        return gdFw;

    }

    public static HashMap getGdTd() {
        HashMap<String, String> gdTd = new HashMap<String, String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + PARAMETER_CONF_SERVER_GDFWTDGDQL);
        } catch (DocumentException e) {
            return null;
        }
        if (document == null)
            throw new NullPointerException();

        Element root = document.getRootElement();

        Element element = root.element("gd_td");
        for (Iterator it = element.elementIterator(); it.hasNext(); ) {
            Element element1 = (Element) it.next();
            gdTd.put(element1.getText(), element1.getName());
        }
        gdTd.put(PARAMETER_CLASSNAME, "gd_td");
        return gdTd;

    }

    public static HashMap getGdFwsyq() {
        HashMap<String, String> gdFwsyq = new HashMap<String, String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + PARAMETER_CONF_SERVER_GDFWTDGDQL);
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getGdFwsyq",e);
        }
        if (document == null)
            throw new NullPointerException();
        Element root = document.getRootElement();

        Element element = root.element("gd_fwsyq");
        for (Iterator it = element.elementIterator(); it.hasNext(); ) {
            Element element1 = (Element) it.next();
            gdFwsyq.put(element1.getText(), element1.getName());
        }
        gdFwsyq.put(PARAMETER_CLASSNAME, "gd_fwsyq");
        return gdFwsyq;

    }

    public static HashMap getGdTdsyq() {
        HashMap<String, String> gdTdsyq = new HashMap<String, String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + PARAMETER_CONF_SERVER_GDFWTDGDQL);
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getGdTdsyq",e);
        }
        if (document == null)
            throw new NullPointerException();
        Element root = document.getRootElement();

        Element element = root.element("gd_tdsyq");
        for (Iterator it = element.elementIterator(); it.hasNext(); ) {
            Element element1 = (Element) it.next();
            gdTdsyq.put(element1.getText(), element1.getName());
        }
        gdTdsyq.put(PARAMETER_CLASSNAME, "gd_tdsyq");
        return gdTdsyq;

    }

    public static HashMap getGdQlr() {
        HashMap<String, String> gdQlr = new HashMap<String, String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + PARAMETER_CONF_SERVER_GDFWTDGDQL);
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getGdQlr",e);
        }
        if (document == null)
            throw new NullPointerException();
        Element root = document.getRootElement();

        Element element = root.element("gd_qlr");
        for (Iterator it = element.elementIterator(); it.hasNext(); ) {
            Element element1 = (Element) it.next();
            gdQlr.put(element1.getText(), element1.getName());
        }
        gdQlr.put(PARAMETER_CLASSNAME, "gd_qlr");
        return gdQlr;

    }

    /**
     * zx
     * 根据权利类型获取标准单位
     *
     * @return
     */
    public static HashMap<String, String> getHbSqlx(String sqlxdm) {
        HashMap hashMap = new HashMap();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/hb-sqlx.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getHbSqlx",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Node sqlx1 = root.selectSingleNode("//sqlxs/sqlx[@dm='"+sqlxdm + "']/sqlx1");
            if(sqlx1!=null)
                hashMap.put("sqlx1",sqlx1.getText());
            else
                hashMap.put("sqlx1","");
            Node sqlx2 = root.selectSingleNode("//sqlxs/sqlx[@dm='"+sqlxdm + "']/sqlx2");
            if(sqlx2!=null)
                hashMap.put("sqlx2",sqlx2.getText());
            else
                hashMap.put("sqlx2","");
        }
        return hashMap;
    }


    /**
     * 获取过渡房屋匹配时，如果申请类型为配置不匹配房屋土地的申请类型代码
     *
     * @return
     */
    public static List<String> getUnBdcdySqlxDm() {
        List<String> listAllNodeid = new ArrayList<String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/unPpBdcdy-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getUnBdcdySqlxDm",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            List<Node> nodes = root.selectNodes(ParamsConstants.SQLXDM_LOWERCASE);
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    listAllNodeid.add(nodes.get(i).getText());
                }
            }
        }
        return listAllNodeid;
    }


    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param dm
     * @return HashMap
     * @description 根据代码获取收件材料名称
     */
    public static HashMap<String, String> getOntSjClMc(String dm) {
        HashMap hashMap = new HashMap();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/ontSjcl.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getOntSjClMc",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Node mc = root.selectSingleNode("//sjcls/sjcl[@dm='"+dm + "']/mc");
            if(mc!=null)
                hashMap.put("mc",mc.getText());
            else
                hashMap.put("mc","");
        }
        return hashMap;
    }

    /**
     * @author bianwen
     * @description  获取批量流程无需提交权利人的申请类型配置
     */
    public static HashMap<String,String> getUnSaveQlrSqlxMap(){
        HashMap<String,String>  unSaveQlrSqlxMap=new HashMap<String, String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/unsaveQlr-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getUnSaveQlrSqlxMap",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Iterator i = root.elementIterator("sqlx");
            while (i.hasNext()) {
                foo = (Element) i.next();
                if (foo != null) {
                    unSaveQlrSqlxMap.put(foo.elementText("dm"),foo.elementText("mc"));
                }
            }
        }
        return unSaveQlrSqlxMap;
    }

    /**
     * @author chenjia
     * @description  获取公告的下拉框选项
     */
    public static List<HashMap>  getGgList() {
       List<HashMap> hashMapList = new ArrayList<HashMap>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/gglist-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getGgList",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Iterator i = root.elementIterator("gg");
            HashMap hashMap = null;
            while (i.hasNext()) {
                foo = (Element) i.next();
                if (foo != null) {
                    hashMap = new HashMap();
                    hashMap.put("ftl",foo.elementText("ftl"));
                    hashMap.put("gglx",foo.elementText("gglx"));
                    hashMapList.add(hashMap);
                }
            }
        }
        return hashMapList;
    }

   /**
    * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
    * @param
    * @description 获取角色配置
    */
    public static List<HashMap> getRoleList() {
        List<HashMap> hashMapList = new ArrayList<HashMap>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/role-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getRoleList",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Iterator i = root.elementIterator("role");
            HashMap hashMap = null;
            while (i.hasNext()) {
                foo = (Element) i.next();
                if (foo != null) {
                    hashMap = new HashMap();
                    hashMap.put("sqlx",foo.elementText("sqlx"));
                    hashMap.put("node",foo.elementText("node"));
                    hashMap.put("roleid",foo.elementText("roleid"));
                    hashMapList.add(hashMap);
                }
            }
        }
        return hashMapList;
    }

    /**
     * @author liujie
     * @description  获取配置的xml的对比字段
     */
    public static HashMap<String,List<String>> getSqlxDbzdMap() {
        HashMap<String,List<String>> hashMap = new HashMap<String,List<String>>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/sqlx-dbzd.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getSqlxDbzdMap",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Element zdmcElement;
            Iterator i = root.elementIterator("sqlx");
            List<String> dbzdList = null;
            while (i.hasNext()) {
                foo = (Element) i.next();
                String sqlx = foo.attribute("dm").getText();
                Iterator j = foo.elementIterator("zdmc");
                dbzdList = new ArrayList<String>();
                while (j.hasNext()) {
                    zdmcElement = (Element) j.next();
                    String zdmc = zdmcElement.getText();
                    if(StringUtils.isNotBlank(zdmc))
                        dbzdList.add(zdmc);
                }
                if(StringUtils.isNotBlank(sqlx))
                    hashMap.put(sqlx,dbzdList);
            }
        }
        return hashMap;
    }

    /**
     * @author
     * @description  获取配置的xml的字段
     */
    public static HashMap<String,HashMap<String,String>> getSqlxZdMap() {
        HashMap<String,HashMap<String,String>> hashMap = new HashMap<String,HashMap<String,String>>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/sqlx-zd.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getSqlxZdMap",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Element zdmcElement;
            Iterator i = root.elementIterator("sqlx");
            HashMap<String,String> zdMap=null;
            while (i.hasNext()) {
                foo = (Element) i.next();
                String sqlx = foo.attribute("dm").getText();
                Iterator j = foo.elementIterator("zd");
                zdMap = new  HashMap<String,String>();
                while (j.hasNext()) {
                    zdmcElement = (Element) j.next();
                    zdMap.put(zdmcElement.element("zdmc").getText(),zdmcElement.element("qjzdmc").getText());
                }
                if(StringUtils.isNotBlank(sqlx)){
                    hashMap.put(sqlx,zdMap);
                }
            }
        }
        return hashMap;
    }


    public static List<String> getUnExamineSqlxDm() {
        List<String> listAllNodeid = new ArrayList<String>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/unExamine-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getUnExamineSqlxDm",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            List<Node> nodes = root.selectNodes(ParamsConstants.SQLXDM_LOWERCASE);
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    listAllNodeid.add(nodes.get(i).getText());
                }
            }
        }
        return listAllNodeid;
    }

    public static List<HashMap> getSmsPropsList() {
        List<HashMap> smsPropsList = new ArrayList<HashMap>();
        SAXReader sr = new SAXReader();
        Document document = null;
        try {
            document = sr.read(AppConfig.getEgovHome() + "/conf/server/sms-props.xml");
        } catch (DocumentException e) {
            logger.error("ReadXmlProps.getSmsPropsList",e);
        }
        if (document != null) {
            Element root = document.getRootElement();
            Element foo;
            Iterator i = root.elementIterator("sms");
            while (i.hasNext()) {
                foo = (Element) i.next();
                if (foo != null) {
                    HashMap smsPropMap = new HashMap();
                    smsPropMap.put("node",foo.elementText("node"));
                    smsPropMap.put("content",foo.elementText("content"));
                    smsPropMap.put("delay",foo.elementText("delay"));
                    smsPropsList.add(smsPropMap);
                }
            }
            Node unsqlx = root.selectSingleNode("//props/unsqlx");
            HashMap unsqlxMap = new HashMap();
            if(unsqlx!=null)
                unsqlxMap.put("unsqlx",unsqlx.getText());
            else
                unsqlxMap.put("unsqlx","");
            smsPropsList.add(unsqlxMap);
        }
        return smsPropsList;
    }

    public static List<Map> getJsPzByType(String cxgn, String type) {
        List<Map> mapList=new ArrayList<>();
        if (StringUtils.isNotBlank(cxgn)) {
            try {
                FileInputStream fileInputStream = new FileInputStream(StringUtils.replace(AppConfig.getEgovHome(), "file:/", "") + "conf/server/" + cxgn);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String sTemp = "";
                String s;
                while ((s = reader.readLine()) != null) {
                    sTemp += s;
                }
                if (StringUtils.isNotBlank(sTemp)) {
                    sTemp = sTemp.substring(sTemp.lastIndexOf("*/") + 2);
                }
                JSONObject jsJsonObject= (JSONObject) JSONObject.parse(sTemp);
                String jsJsonObjectList="";
                if(jsJsonObject.containsKey(type)){
                    jsJsonObjectList=jsJsonObject.getString(type);
                }
                mapList= JSONObject.parseArray(jsJsonObjectList, Map.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mapList;
    }
}

