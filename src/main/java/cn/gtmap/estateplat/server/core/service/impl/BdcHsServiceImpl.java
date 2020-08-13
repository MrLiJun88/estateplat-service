package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.BdcFwHs;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.mapper.BdcHsMapper;
import cn.gtmap.estateplat.server.core.service.BdcDyaqService;
import cn.gtmap.estateplat.server.core.service.BdcFdcqService;
import cn.gtmap.estateplat.server.core.service.BdcHsService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lst
 * @version V1.0, 15-4-17
 */
@Repository
public class BdcHsServiceImpl implements BdcHsService {

    @Autowired
    private BdcHsMapper bdcHsMapper;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcXmService bdcXmService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PARAMETER_FW_HS_INDEX = "FW_HS_INDEX";
    private static final String PARAMETER_KEYCODE = "KEYCode";
    private static final String PARAMETER_BDCDJB_SHOWQL_BDCDYH_URL = "/bdcDjb/showQL?bdcdyh=";
    private static final String PARAMETER_ISQL_FALSE = "&isql=false";

    @Override
    public List<HashMap> xmlToJson(final String xmlDoc, final String djbid) {
        //如果为空的话
        if (StringUtils.isBlank(xmlDoc)) {
            return new ArrayList<HashMap>();
        }
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        List<HashMap> fwlist = new ArrayList<HashMap>();
        //opt
        String bdcdjUrl = AppConfig.getProperty("bdcdj.url");
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
            if (root == null)
                throw new NullPointerException("XML的ROOT为空！");
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Element et = null;
            if (CollectionUtils.isNotEmpty(jiedian)) {
                for (int i = 0; i < jiedian.size(); i++) {
                    HashMap fwMap = new HashMap();
                    et = (Element) jiedian.get(i);//循环依次得到子元素
                    //土地等
                    if (StringUtils.equals(et.getName(), "BDCDY") || StringUtils.equals(et.getName(), "TD")) {
                        //json统一用mc和dm
                        if ((root.getAttributeValue("DZ") != null && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(root.getAttributeValue("DZ")))) || (et.getAttributeValue("DZ") != null && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(et.getAttributeValue("DZ")))))
                            fwMap.put("MC", et.getAttributeValue("DZ"));
                        else
                            fwMap.put("MC", et.getAttributeValue(ParamsConstants.BDCDYH_CAPITAL));
                        fwMap.put("DM", et.getAttributeValue(ParamsConstants.BDCDYH_CAPITAL));
                        fwMap.put("URL", bdcdjUrl + PARAMETER_BDCDJB_SHOWQL_BDCDYH_URL + et.getAttributeValue(ParamsConstants.BDCDYH_CAPITAL) + "&isql=false&djbid=" + djbid);
                        fwlist.add(fwMap);
                        continue;
                    } else {
                        //房屋
                        fwMap.put("MC", et.getAttributeValue("FWMC"));
                        fwMap.put("DM", et.getAttributeValue("ZRZH"));
                    }
                    List zjiedian = et.getChildren();
                    List<HashMap> ljzList = new ArrayList<HashMap>();
                    fwMap.put("LJZ", ljzList);
                    fwlist.add(fwMap);
                    for (int j = 0; j < zjiedian.size(); j++) {
                        HashMap lzjMap = new HashMap();
                        Element xet = (Element) zjiedian.get(j);
                        lzjMap.put("FWMC", xet.getAttributeValue("FWMC"));
                        lzjMap.put("KEYCODE", xet.getAttributeValue(PARAMETER_KEYCODE));
                        String type = "";
                        String url = "";
                        if (StringUtils.isNotBlank(xet.getAttributeValue(PARAMETER_KEYCODE))) {
                            List<String> bdcdyList = bdcHsMapper.selectHsCount(xet.getAttributeValue(PARAMETER_KEYCODE));
                            if (bdcdyList != null && bdcdyList.size() > 1) {
                                type = "LPB";
                                url = bdcdjUrl + "/lpb/lpb?dcbId=" + xet.getAttributeValue(PARAMETER_KEYCODE) + "&showQl=true&isNotBack=true&isNotWf=true&openQlWay=win&djbid=" + djbid;
                            } else if (bdcdyList != null && bdcdyList.size() == 1) {
                                type = "DY";
                                url = bdcdjUrl + PARAMETER_BDCDJB_SHOWQL_BDCDYH_URL + bdcdyList.get(0) + PARAMETER_ISQL_FALSE;
                            }
                        }
                        lzjMap.put("TYPE", type);
                        lzjMap.put("URL", url);
                        ljzList.add(lzjMap);
                    }
                }
            } else if (root != null) {
                HashMap fwMap = new HashMap();
                if (StringUtils.equals(root.getName(), "BDCDY")) {
                    //json统一用mc和dm
                    if (root.getAttributeValue("DZ") != null && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(root.getAttributeValue("DZ"))))
                        fwMap.put("MC", root.getAttributeValue("DZ"));
                    else
                        fwMap.put("MC", root.getAttributeValue(ParamsConstants.BDCDYH_CAPITAL));
                    fwMap.put("DM", root.getAttributeValue(ParamsConstants.BDCDYH_CAPITAL));
                    fwMap.put("URL", bdcdjUrl + PARAMETER_BDCDJB_SHOWQL_BDCDYH_URL + root.getAttributeValue(ParamsConstants.BDCDYH_CAPITAL) + PARAMETER_ISQL_FALSE);
                    fwlist.add(fwMap);
                }
            }
        } catch (JDOMException e) {
            logger.error("BdcHsServiceImpl.xmlToJson",e);
        } catch (IOException e) {
            logger.error("BdcHsServiceImpl.xmlToJson",e);
        }
        return fwlist;
    }

    @Override
    @Transactional(readOnly = true)
    public String getLpbUrl(final String djbId) {
        String url = "";
        if (StringUtils.isNotBlank(djbId)) {
            String bdcdjUrl = AppConfig.getProperty("bdcdj.url");
            List<String> bdcdyList = bdcHsMapper.selectHsCount(djbId);

            if (CollectionUtils.isNotEmpty(bdcdyList) && bdcdyList.size() > 1) {
                url = bdcdjUrl + "/lpb/lpb?dcbId=" + djbId + "&showQl=true&isNotWf=true&openQlWay=win";
            } else if (CollectionUtils.isNotEmpty(bdcdyList) && bdcdyList.size() == 1) {
                url = bdcdjUrl + PARAMETER_BDCDJB_SHOWQL_BDCDYH_URL + bdcdyList.get(0) + PARAMETER_ISQL_FALSE;
            }
        }
        return url;
    }

    @Transactional(readOnly = true)
    public List<BdcFwHs> getBdcFwhsQlztList(final Map map) {
        return bdcHsMapper.getBdcFwhsQlztList(map);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map> getGdFwhsList(final Map map) {
        return bdcHsMapper.getGdFwhsList(map);
    }

    @Override
    public String getGdFwQszt(final List<Map> gdFwList, final String hsId) {
        String qszt = null;
        if (CollectionUtils.isNotEmpty(gdFwList)&& StringUtils.isNotBlank(hsId)) {
            for (Map gdFw : gdFwList) {
                if (StringUtils.equals(hsId, CommonUtil.formatEmptyValue(gdFw.get(PARAMETER_FW_HS_INDEX)))) {
                    qszt = CommonUtil.formatEmptyValue(gdFw.get("QSZT"));
                    break;
                }
            }
        }
        return qszt;
    }

    @Override
    public String getHsQsztByHsId(final List<BdcFwHs> bdcFwHsList, final List<Map> gdFwhsList, final String hsId, final String djHsQlzt) {
        StringBuilder qszt = new StringBuilder();
        if (StringUtils.isNotBlank(hsId)) {
            if (CollectionUtils.isNotEmpty(bdcFwHsList)) {
                //1、不动产业务数据的权利
                //根据存在的qszt的优先级，3，2，1。
                for (BdcFwHs bdcFwHs : bdcFwHsList) {
                    if (StringUtils.equals(hsId, bdcFwHs.getFwHsIndex())) {
                        qszt.append(bdcFwHs.getQlzt());
                    }
                }
                if (StringUtils.isNotBlank(qszt)) {
                    if (qszt.toString().contains("3"))
                        qszt = new StringBuilder("3");
                    else if (qszt.toString().contains("2"))
                        qszt = new StringBuilder("2");
                    else {
                        for (BdcFwHs bdcFwHs : bdcFwHsList) {
                            if (StringUtils.equals(hsId, bdcFwHs.getFwHsIndex())) {
                                qszt = new StringBuilder(bdcFwHs.getQlzt());
                                break;
                            }
                        }
                    }
                }
                if ((StringUtils.equals(qszt.toString(), "0") || StringUtils.isBlank(qszt.toString())) && StringUtils.isNotBlank(hsId)) {
                    String bdcdyh = bdcHsMapper.getBdcdyhByHsid(hsId);
                    if (StringUtils.isNotBlank(bdcdyh)) {
                        String buildingContractUrl = AppConfig.getProperty("building-contract.url");
                        if (StringUtils.isNotBlank(buildingContractUrl)) {
                            String url = buildingContractUrl + "/htbaServerClient/checkIsba";
                            HttpClient client = null;
                            PostMethod method = null;
                            try {
                                client = new HttpClient();
                                client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                                client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                                method = new PostMethod(url);
                                method.setRequestHeader("Connection", "close");
                                method.addParameter("bdcdyh", bdcdyh);
                                client.executeMethod(method);
                                String isba = method.getResponseBodyAsString();
                                if (StringUtils.equals(isba, "true")) {
                                    qszt = new StringBuilder("9");
                                }
                            } catch (IOException e) {
                                logger.error("BdcHsServiceImpl.getHsQsztByHsId",e);
                            }finally {
                                if(method != null) {
                                    method.releaseConnection();
                                }
                                if(client != null) {
                                    ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                                }
                            }
                        }
                    }
                }
            }
            //2、地籍数据库中户室的权利状态
            if ((StringUtils.isBlank(qszt) || StringUtils.equals(qszt, "0")) && StringUtils.isNotBlank(djHsQlzt))
                qszt = new StringBuilder(djHsQlzt);
            //3、过渡房屋权利状态
            if ((StringUtils.isBlank(qszt) || StringUtils.equals(qszt, "0"))&&CollectionUtils.isNotEmpty(gdFwhsList)) {
                for (Map gdFwhs : gdFwhsList) {
                    if (StringUtils.equals(hsId, CommonUtil.formatEmptyValue(gdFwhs.get(PARAMETER_FW_HS_INDEX)))) {
                        qszt.append(CommonUtil.formatEmptyValue(gdFwhs.get("QSZT")));
                    }
                }
                if (StringUtils.isNotBlank(qszt)) {
                    if (qszt.toString().contains("3"))
                        qszt = new StringBuilder("3");
                    else if (qszt.toString().contains("2"))
                        qszt = new StringBuilder("2");
                    else {
                        for (Map gdFwhs : gdFwhsList) {
                            if (StringUtils.equals(hsId, CommonUtil.formatEmptyValue(gdFwhs.get(PARAMETER_FW_HS_INDEX)))) {
                                qszt = new StringBuilder(CommonUtil.formatEmptyValue(gdFwhs.get("QSZT")));
                                break;
                            }
                        }
                    }
                }
            }
        }

        return qszt.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> selectHsCount(@Param("keyCode") final String keyCode) {
        return bdcHsMapper.selectHsCount(keyCode);
    }


    /**
     * @param map
     * @author bianwen
     * @rerutn
     * @description 获取访问预测戶室信息
     */
    @Override
    public List<BdcFwHs> getBdcYcFwhsQlztList(HashMap map) {
        return bdcHsMapper.getBdcYcFwhsQlztList(map);
    }

    /**
     * @param map
     * @author bianwen
     * @rerutn
     * @description 获取过渡预测房屋信息状态
     */
    @Override
    public List<Map> getGdYcFwhsList(HashMap map) {
        return bdcHsMapper.getGdYcFwhsList(map);
    }

    @Override
    public String getFzlxQlzt(String bdcdyh,String qlzt) {
        //1、新建商品房首次时，只登簿，不发证
        //2、新建商品房首次时，发了信息表
        //3、新建商品房首次时，直接发了不动产权证书
        //4、先进行预告登记，再进行新建商品房首次登记时，发了信息表
        //5、先进行在建工程抵押登记，再进行新建商品房首次登记时，发了不动产权证书
        if(StringUtils.isBlank(qlzt) || StringUtils.equals(qlzt, "0") || StringUtils.equals(qlzt, "1") || StringUtils.equals(qlzt, "4")) {
            qlzt = getSpfscdjHsFzlx(bdcdyh,qlzt);
        }else if(StringUtils.equals(qlzt, "2")&&StringUtils.isNotBlank(bdcdyh)){
            HashMap map = new HashMap();
            map.put("bdcDyh",bdcdyh);
            map.put("qszt",Constants.QLLX_QSZT_XS);
            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
            if(CollectionUtils.isNotEmpty(bdcDyaqList)){
                for(BdcDyaq bdcDyaq:bdcDyaqList){
                    BdcXm bdcDyaqXm = bdcXmService.getBdcXmByProid(bdcDyaq.getProid());
                    if(bdcDyaqXm != null && CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM,bdcDyaqXm.getSqlx())){
                        qlzt = getSpfscdjHsFzlx(bdcdyh,qlzt);
                    }
                }
            }
        }
        return qlzt;
    }

    private String getSpfscdjHsFzlx(String bdcdyh,String qlzt) {
        if(StringUtils.isNotBlank(bdcdyh)){
            HashMap map = new HashMap();
            map.put("sqlx", Constants.SQLX_PLFZ_DM);
            map.put("bdcdyh", bdcdyh);
            String fzlx = bdcFdcqService.getSpfscdjHsFzlx(map);
            if (StringUtils.isNotBlank(fzlx)) {
                if (StringUtils.equals(fzlx, Constants.FZLX_FZM)) {
                    qlzt = "8";
                } else if (StringUtils.equals(fzlx, Constants.FZLX_FZS)) {
                    qlzt = "9";
                } else {
                    qlzt = "10";
                }
            }
        }
        return qlzt;
    }

}
