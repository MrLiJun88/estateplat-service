package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.xml.XmlUtils;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcHst;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.BarcodeUtil;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.GetQRcode;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.ArrayUtils;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">zx</a>
 * @version V1.0, 16-5-31
 *          不动产调用本地打印
 */
@Controller
@RequestMapping("/bdcPrint")
public class BdcPrintController extends BaseController {

    @Autowired
    private BdcZsPrintService bdcZsPrintService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcSfxxService bdcSfxxService;
    @Autowired
    private BdcGdxxService bdcGdxxService;
    @Autowired
    private BdcPpgxService bdcPpgxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcHstService bdcHstService;

    public static final String RESPONSE_CONTENTTYPE_CHARSET_GBK = "text/xml;charset=gbk";
    public static final String RESPONSE_CONTENTTYPE_CHARSET_UTF8 = "text/xml;charset=utf-8";
    public static final String PARAMETER_PROIDS = "proids";
    public static final String PARAMETER_GETBDCQZXML = "/bdcPrint/getBdcqzXml?proid=";
    public static final String PARAMETER_XML_ENDNODE_FRS = "</frs>";

    @ResponseBody
    @RequestMapping(value = "/printBdcqz")
    public void printBdcqz(String proid, String zsid, String zslx, String hiddeMode, HttpServletResponse response) throws IOException {
        //打印修改证书打印状态和证书打印次数
        if(StringUtils.isNotBlank(zsid)){
            BdcZs bdcZs = bdcZsService.queryBdcZsByZsid(zsid);
            if(bdcZs != null){
                bdcZs.setDyzt("1");
                if(bdcZs.getDycs() != null){
                    bdcZs.setDycs(bdcZs.getDycs()+1);
                }
                entityMapper.saveOrUpdate(bdcZs,bdcZs.getZsid());
            }
        }
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        if (StringUtils.equals(zslx, "zs")){
            List<BdcHst> bdcHstList=null;
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            if(bdcBdcdy!=null&&StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())){
                bdcHstList = bdcHstService.selectBdcHst(bdcBdcdy.getBdcdyh());
            }
            if (CollectionUtils.isEmpty(bdcHstList)) {
                modalUrls.append("C:/GTIS/zsPrint.fr3,");
            }else{
                modalUrls.append("C:/GTIS/zsPrintHst.fr3,");
            }
        }
        else if (StringUtils.equals(zslx, "zms"))
            modalUrls.append("C:/GTIS/zmsPrint.fr3,");
        else if (StringUtils.equals(zslx, "zmd"))
            modalUrls.append("C:/GTIS/zmdPrint.fr3,");
        else if(StringUtils.equals(zslx,"scdjxx"))
            modalUrls.append("C:/GTIS/scxx.fr3,");

        xmlUrls.append(bdcdjUrl).append(PARAMETER_GETBDCQZXML).append(proid).append("&amp;zsid=").append(zsid).append("&amp;zslx=").append(zslx).append(",");

        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description  根据wiid打印流程实例所有的证书
     */
    @ResponseBody
    @RequestMapping(value = "/printAllBdcqzs")
    public void printAllBdcqzs(String wiid, String zslx, String hiddeMode, HttpServletResponse response) throws IOException {
        StringBuilder zsids = new StringBuilder();
        List<BdcZs> bdcZsList = bdcZsService.getPlZsByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            for (BdcZs bdcZs : bdcZsList) {
                if((StringUtils.equals(zslx,"zms")&&StringUtils.equals(bdcZs.getZstype(),Constants.BDCQZM_BH_FONT))||(StringUtils.equals(zslx,"zs")&&StringUtils.equals(bdcZs.getZstype(),Constants.BDCQZS_BH_FONT)))
                zsids.append(bdcZs.getZsid()).append(",");
            }
        }
        List<String> zsidList = new ArrayList<String>();
        if (StringUtils.indexOf(zsids, ",") > -1) {
            zsidList = Arrays.asList(StringUtils.split(zsids.toString(), ","));
        }
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        for (String zsidTemp : zsidList) {
            //jyl 只要取一个proid用来取值就好了。
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByZsid(zsidTemp);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                if (StringUtils.equals(zslx, "zs"))
                    modalUrls.append("C:/GTIS/zsPrint.fr3,");
                else if (StringUtils.equals(zslx, "zms"))
                    modalUrls.append("C:/GTIS/zmsPrint.fr3,");
                else if (StringUtils.equals(zslx, "zmd"))
                    modalUrls.append("C:/GTIS/zmdPrint.fr3,");
                xmlUrls.append(bdcdjUrl).append(PARAMETER_GETBDCQZXML).append(bdcXmList.get(0).getProid()).append("&amp;zsid=").append(zsidTemp).append("&amp;zslx=").append(zslx).append(",");
            }
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    //商品房批量打印
    @ResponseBody
    @RequestMapping(value = "/spfMulPrint")
    public void spfMulPrint(String wiid, String[] proids, String hiddeMode, String zslx,
                            String startNum, String endNum, HttpServletResponse response) throws IOException {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        List<Map> bdcZsList = null;
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(wiid)) {
            HashMap<String, Object> param = new HashMap<String, Object>();
            HashMap<String, Object> dyztMap = new HashMap<String, Object>();
            if(proids != null && proids.length > 0){
                param.put(PARAMETER_PROIDS, proids);
            }else {
                param.put("wiid", wiid);
            }
            param.put("startNum", startNum);
            param.put("endNum", endNum);
            param.put("zstype", StringUtils.equals(zslx, "zs") ? "不动产权" :
                    StringUtils.equals(zslx, "scdjxx") ? "首次登记证" :
                            StringUtils.equals(zslx, "zm") ? "不动产证明" : "");
            bdcZsList = bdcZsService.getPlZs(param);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (Map zs : bdcZsList) {
                    linkedHashMap.put(zs.get(ParamsConstants.PROID_CAPITAL), zs.get("ZSID"));
                    if (!StringUtils.equals(String.valueOf(zs.get("DYZT")), "1")) {
//                        dyztMap.put("zsid", zs.get("ZSID"));
//                        dyztMap.put("dyzt", "1");
//                        bdcZsService.updateDyzt(dyztMap);
                        BdcZs bdcZsTemp = bdcZsService.queryBdcZsByZsid(zs.get("ZSID").toString());
                        if(bdcZsTemp != null){
                            bdcZsTemp.setDyzt("1");
                            if(bdcZsTemp.getDycs() != null){
                                bdcZsTemp.setDycs(bdcZsTemp.getDycs()+1);
                            }
                            entityMapper.saveOrUpdate(bdcZsTemp,bdcZsTemp.getZsid());
                        }
                    }
                }
            }
        }
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        StringBuilder xmlUrlsSingle = new StringBuilder();
        String[] zsArr = null;
        List<String> zsList = new LinkedList<String>();
        if (StringUtils.equals(zslx, "zmd")) {
            modalUrls.append("C:/GTIS/zmdPrint.fr3");
        } else if(StringUtils.equals(zslx, "zs")) {
            modalUrls.append("C:/GTIS/zsPrint.fr3");
        } else if(StringUtils.equals(zslx, "zms")){
            modalUrls.append("C:/GTIS/zmsPrint.fr3");
        } else{
            modalUrls.append("C:/GTIS/scxx.fr3");
        }
        if (linkedHashMap != null && linkedHashMap.size() > 0) {
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getKey()))) {
                    zsList.add(CommonUtil.formatEmptyValue(entry.getKey()) + "@" + CommonUtil.formatEmptyValue(entry.getValue()));
                    xmlUrlsSingle.append(bdcdjUrl).append(PARAMETER_GETBDCQZXML).append(CommonUtil.formatEmptyValue(entry.getKey())).append("&amp;zsid=").append(CommonUtil.formatEmptyValue(entry.getValue())).append("&amp;zslx=").append(zslx).append(",");
                }
            }
            if (zsList.size() == 1) {
                xmlUrls.append(xmlUrlsSingle.toString().substring(0,xmlUrlsSingle.length() -1));
            } else {
                zsArr = zsList.toArray(new String[0]);
                xmlUrls.append(bdcdjUrl).append("/bdcPrint/getBdcqzMulXml?zsArr=").append(StringUtils.join(zsArr, ";")).append("&amp;zslx=").append(zslx);
            }
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            logger.info(sb.toString());
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "/getPrintXxXml")
    public void getPrintXxXml(String proid,String printType,HttpServletResponse response) throws IOException {
        String xml = "";
        DataToPrintXml dataToPrintXml;
        dataToPrintXml = bdcXmService.getAllPrintXxXml(proid, printType);
        //将实体类转换为控件数据源，即相应格式的xml
        if (dataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(DataToPrintXml.class);
            xml = resultBinder.entityToXml(dataToPrintXml, Constants.DEFAULT_CHARSET);
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
        PrintWriter out = response.getWriter();
        out.write(xml);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/getBdcqzXml")
    public void getBdcqzXml(String proid, String zsid, String zslx, HttpServletResponse response) throws IOException {
        String xml = "";
        DataToPrintXml dataToPrintXml;
        /*单个权利人*/
        dataToPrintXml = bdcZsPrintService.getZsPrintXml(proid, zslx, bdcdjUrl, zsid);
        //将实体类转换为控件数据源，即相应格式的xml
        if (dataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(DataToPrintXml.class);
            xml = resultBinder.entityToXml(dataToPrintXml, Constants.DEFAULT_CHARSET);
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
        PrintWriter out = response.getWriter();
        out.write(xml);
        logger.info(xml);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/getBdcqzMulXml")
    public void getBdcqzMulXml(String zsArr,
                               String zslx, HttpServletResponse response) throws IOException {
        String xml = "";
        String[] zsArrs = zsArr.split(";");
        Map<String,String> proidAndzsidMap = new LinkedHashMap<String,String>();
        if (ArrayUtils.isNotEmpty(zsArrs)) {
            for (String proidAndzsid : zsArrs) {
                String[] param = proidAndzsid.split("@");
                String proid = param[0];
                String zsid = param[1];
                proidAndzsidMap.put(proid, zsid);
            }
        }
        MulDataToPrintXml muldataToPrintXml = null;
        muldataToPrintXml = bdcZsPrintService.getMulZsPrintXml(zslx, bdcdjUrl, proidAndzsidMap);
        if (muldataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(MulDataToPrintXml.class);
            xml = resultBinder.entityToXml(muldataToPrintXml, Constants.DEFAULT_CHARSET);
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
        PrintWriter out = response.getWriter();
        out.write(xml);
        logger.info(xml);
        out.flush();
        out.close();
    }


    @RequestMapping(value = "/getEwm")
    public void getEwmStream(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bz", required = false) String bz, HttpServletResponse response) {

        try {
            String content = "";
            if (StringUtils.isNotBlank(bz)) {
                content = bz;
            } else {
                if (StringUtils.isNotBlank(proid)) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    if (bdcXm != null)
                        content = bdcXm.getBh();
                }
            }
            response.setContentType("image/jpg;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=test.jpg");
            GetQRcode.encoderQRCode2(content, null, response);

        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                logger.error("BdcPrintController.getEwmStream",e);
            }
        }
    }


    @RequestMapping(value = "/getTxm")
    public void getTxmStream(@RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) {
        try {
            String content = "";
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null)
                    content = bdcXm.getBh();
            }
            response.setContentType("image/jpg;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=test.jpg");
            BarcodeUtil.generateBarCode128(response,content,"0.5","10","300");

        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                logger.error("BdcPrintController.getTxmStream",e);
            }
        }
    }


    @ResponseBody
    @RequestMapping(value = "/printSfd")
    public void printSfd(String hiddeMode, String userid, HttpServletResponse response, String sfxxid) throws IOException {
        if(StringUtils.isNotBlank(sfxxid)){
            StringBuilder modalUrls = new StringBuilder();
            StringBuilder xmlUrls = new StringBuilder();
            modalUrls.append("C:/GTIS/sfddj.fr3,");
            xmlUrls.append(bdcdjUrl).append("/bdcPrint/getSfdXml?sfxxid=").append(sfxxid).append(",");
            if (StringUtils.isNotBlank(modalUrls)) {
                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
                sb.append("<frs>");
                if (StringUtils.indexOf(modalUrls, ",") > -1) {
                    String[] modalUrlList = modalUrls.toString().split(",");
                    String[] xmlUrlsList = xmlUrls.toString().split(",");
                    for (int i = 0; i < modalUrlList.length; i++) {
                        sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                    }
                } else {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
                }
                sb.append(PARAMETER_XML_ENDNODE_FRS);
                response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
                PrintWriter out = response.getWriter();
                out.write(sb.toString());
                out.flush();
                out.close();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/printMulSfd")
    public void printMulSfd(String hiddeMode, String userid, HttpServletResponse response, String sfxxids) throws IOException {
        HashMap hashMap = new HashMap();
        List<String> sfxxidList = new ArrayList<String>();
        List<HashMap> bdcFpxxList = new ArrayList<HashMap>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(sfxxids)) {
            if(StringUtils.isNotBlank(sfxxids)){
                String[] sfxxidsTemp = sfxxids.split(",");
                if(sfxxidsTemp != null && sfxxidsTemp.length > 0){
                    for(String sfxxid : sfxxidsTemp){
                        if(StringUtils.isNotBlank(sfxxid)){
                            sfxxidList.add(sfxxid);
                        }
                    }
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(sfxxidList)){
                param.put("sfxxids", sfxxidList);
                bdcFpxxList = bdcSfxxService.getSfxxMapBySfxxid(param);
            }
            if (CollectionUtils.isNotEmpty(bdcFpxxList)) {
                for (Map fpxx : bdcFpxxList) {
                    hashMap.put(fpxx.get("SFXXID"), fpxx.get("SFXXID"));
                }
            }
        }
        String modalUrls = "";
        String xmlUrls = "";
        StringBuilder xmlUrlsSingle = new StringBuilder();
        String[] fpArr = null;
        List<String> fpList = new LinkedList<String>();
        modalUrls += "C:/GTIS/sfddj.fr3";
        if (hashMap != null && hashMap.size() > 0) {
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getKey()))) {
                    fpList.add(CommonUtil.formatEmptyValue(entry.getKey()) + "@" + CommonUtil.formatEmptyValue(entry.getValue()));
                    xmlUrlsSingle.append(bdcdjUrl).append("/bdcPrint/getSfdXml?sfxxid=").append(CommonUtil.formatEmptyValue(entry.getValue())).append(",");
                }
            }
            if (fpList.size() == 1) {
                xmlUrls = xmlUrlsSingle.substring(0,xmlUrlsSingle.length() -1);
            } else {
                fpArr = fpList.toArray(new String[0]);
                xmlUrls += bdcdjUrl + "/bdcPrint/getSfdMulXml?fpArr=" + StringUtils.join(fpArr, ";");
            }
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.split(",");
                String[] xmlUrlsList = xmlUrls.split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getSfdXml")
    public void getSfdXml(String sfxxid, String userid, HttpServletResponse response) throws IOException {
        String xml = "";
        DataToPrintXml dataToPrintXml;
        dataToPrintXml = bdcSfxxService.getFpxxPrintXml(sfxxid);
        //将实体类转换为控件数据源，即相应格式的xml
        if (dataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(DataToPrintXml.class);
            xml = resultBinder.entityToXml(dataToPrintXml, Constants.DEFAULT_CHARSET);
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
        PrintWriter out = response.getWriter();
        out.write(xml);
        out.flush();
        out.close();
    }


    @RequestMapping(value = "/getSfdMulXml")
    public void getSfdMulXml(String fpArr,HttpServletResponse response) throws IOException {
        String xml = "";
        String[] fpArrs = fpArr.split(";");
        Map<String, String> sfxxidMap = new HashMap<String, String>();
        if (ArrayUtils.isNotEmpty(fpArrs)) {
            for (String sfxxid : fpArrs) {
                String[] param = sfxxid.split("@");
                String proid = param[0];
                String zsid = param[1];
                sfxxidMap.put(proid, zsid);
            }
        }
        MulDataToPrintXml muldataToPrintXml = null;
        muldataToPrintXml = bdcSfxxService.getMulFpxxPrintXml(sfxxidMap);
        if (muldataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(MulDataToPrintXml.class);
            xml = resultBinder.entityToXml(muldataToPrintXml, Constants.DEFAULT_CHARSET);
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
        PrintWriter out = response.getWriter();
        out.write(xml);
        out.flush();
        out.close();
    }

    //本地打印档案信息
    @ResponseBody
    @RequestMapping(value = "/printDafp")
    public void printDafp(String hiddeMode,HttpServletResponse response, String proid) throws IOException {
        List<String> proidList = new ArrayList<String>();
        //取档案封皮信息
        List<HashMap> daFpxxHashMaps = new ArrayList<HashMap>();
        if(StringUtils.isNotEmpty(proid)){
            if(StringUtils.isNotBlank(proid)){
                String[] proidsTemp = proid.split(",");
                if(proidsTemp != null && proidsTemp.length > 0){
                    for(String proidtmp : proidsTemp){
                        if(StringUtils.isNotBlank(proidtmp)){
                            proidList.add(proidtmp);
                        }
                    }
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(proidList)){
                param.put(PARAMETER_PROIDS, proidList);
                daFpxxHashMaps = bdcGdxxService.getDafpxxMapByproid(param);
            }
        }
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        for (HashMap daFpxxHashmap : daFpxxHashMaps) {
            if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_CFDJ_DM)){
                modalUrls.append("C:/GTIS/cfdafp.fr3,");
            } else if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_DYDJ_DM)){
                modalUrls.append("C:/GTIS/dydafp.fr3,");
            } else{
                modalUrls.append("C:/GTIS/cqdafp.fr3,");
            }
            xmlUrls.append(bdcdjUrl).append("/bdcPrint/getDaFpxxXml?proid=").append(daFpxxHashmap.get(ParamsConstants.PROID_CAPITAL).toString());
            break;
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            logger.info(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getDaFpxxXml")
    public void getDaFpxxXml(String proid,HttpServletResponse response) throws IOException {
        String xml = "";
        String[] proidArrs = proid.split(",");
        DataToPrintXml dataToPrintXml;
        dataToPrintXml = bdcGdxxService.getDafpxxPrintXml(proidArrs[0]);
        //将实体类转换为控件数据源，即相应格式的xml
        if (dataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(DataToPrintXml.class);
            xml = resultBinder.entityToXml(dataToPrintXml,"gbk");
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_GBK);
        PrintWriter out = response.getWriter();
        out.write(xml);
        logger.info(xml);
        out.flush();
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "/printAllDafp")
    public void printAllDafp(String hiddeMode,HttpServletResponse response, String proids) throws IOException{
        HashMap hashMap = new HashMap();
        List<String> proidList = new ArrayList<String>();
        List<HashMap> bdcDaFpxxList = new ArrayList<HashMap>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(proids)) {
            if(StringUtils.isNotBlank(proids)){
                String[] proidsTemp = proids.split(",");
                if(proidsTemp != null && proidsTemp.length > 0){
                    for(String proid : proidsTemp){
                        if(StringUtils.isNotBlank(proid)){
                            proidList.add(proid);
                        }
                    }
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(proidList)){
                param.put(PARAMETER_PROIDS, proidList);
                bdcDaFpxxList = bdcGdxxService.getDafpxxMapByproid(param);
            }
            if (CollectionUtils.isNotEmpty(bdcDaFpxxList)) {
                for (Map fpxx : bdcDaFpxxList) {
                    hashMap.put(fpxx.get(ParamsConstants.PROID_CAPITAL), fpxx.get(ParamsConstants.PROID_CAPITAL));
                }
            }
        }
        StringBuilder modalUrls = new StringBuilder();
        String xmlUrls = "";
        StringBuilder xmlUrlsSingle = new StringBuilder();
        String[] fpArr = null;
        List<String> fpList = new LinkedList<String>();
        //取一条封皮信息的数据判断封皮类型
        for (HashMap daFpxxHashmap : bdcDaFpxxList) {
            if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_CFDJ_DM)){
                modalUrls.append("C:/GTIS/cfdafp.fr3");
            } else if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_DYDJ_DM)){
                modalUrls.append("C:/GTIS/dydafp.fr3");
            } else{
                modalUrls.append("C:/GTIS/cqdafp.fr3");
            }
            break;
        }
        if (hashMap != null && hashMap.size() > 0) {
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getKey()))) {
                    fpList.add(CommonUtil.formatEmptyValue(entry.getKey()) + "@" + CommonUtil.formatEmptyValue(entry.getValue()));
                    xmlUrlsSingle.append(bdcdjUrl).append("/bdcPrint/getDaFpxxXml?proid=").append(CommonUtil.formatEmptyValue(entry.getValue())).append(",");
                }
            }
            if (fpList.size() == 1) {
                xmlUrls = xmlUrlsSingle.substring(0,xmlUrlsSingle.length() -1);
            } else {
                fpArr = fpList.toArray(new String[0]);
                xmlUrls += bdcdjUrl + "/bdcPrint/getAllDaFpxxXml?fpArr=" + StringUtils.join(fpArr, ";");
            }
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getAllDaFpxxXml")
    public void getAllDaFpxxXml(String fpArr,HttpServletResponse response) throws IOException{
        String xml = "";
        String[] fpArrs = fpArr.split(";");
        List<String> list = new ArrayList<String>();
        if (ArrayUtils.isNotEmpty(fpArrs)) {
            for (String proidTmp : fpArrs) {
                String[] param = proidTmp.split("@");
                String proid = param[0];
                if(StringUtils.isNotBlank(proid) && !list.contains(proid)){
                    list.add(proid);
                }
            }
        }
        MulDataToPrintXml muldataToPrintXml = null;
        muldataToPrintXml = bdcGdxxService.getAllDafpxxPrintXml(list);
        if (muldataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(MulDataToPrintXml.class);
            xml = resultBinder.entityToXml(muldataToPrintXml, "gbk");
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_GBK);
        PrintWriter out = response.getWriter();
        out.write(xml);
        logger.info(xml);
        out.flush();
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "/printAllMulBdc")
    public void printAllMulBdc(String wiids,String hiddeMode,String printType,HttpServletResponse response) throws IOException{
        List<String> proidList = null;
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : "true";
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        if(StringUtils.isNotBlank(wiids)){
            proidList = bdcXmService.getProidListByWiid(wiids);
            for(String proid :proidList){
                //先通过权利类型区分再通过打印类别区分
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByProid(proid);
                if(CollectionUtils.isNotEmpty(bdcXmList)&&StringUtils.isNotBlank(bdcXmList.get(0).getQllx())){
                    if(StringUtils.equals(bdcXmList.get(0).getQllx(),Constants.QLLX_DYAQ)){
                        if(StringUtils.isNotBlank(printType)){
                            if(StringUtils.equals(printType,"sqs")){
                                modalUrls.append("C:/GTIS/printDyaqSqsxx.fr3,");
                            }else if(StringUtils.equals(printType,"spb")){
                                modalUrls.append("C:/GTIS/printDyaqSpbxx.fr3,");
                            }else if(StringUtils.equals(printType,"fzjl")){
                                modalUrls.append("C:/GTIS/printDyaqfzjl.fr3,");
                            }
                            xmlUrls.append(bdcdjUrl).append("/bdcPrint/getPrintXxXml?proid=").append(bdcXmList.get(0).getProid()).append("&amp;printType=").append(printType).append(",");
                        }
                    }else{
                        if(StringUtils.isNotBlank(printType)){
                            if(StringUtils.equals(printType,"sqs")){
                                modalUrls.append("C:/GTIS/printsqs.fr3,");
                            }else if(StringUtils.equals(printType,"spb")){
                                modalUrls.append("C:/GTIS/printspb.fr3,");
                            }else if(StringUtils.equals(printType,"fzjl")){
                                modalUrls.append("C:/GTIS/printfzjl.fr3,");
                            }
                            xmlUrls.append(bdcdjUrl).append("/bdcPrint/getPrintXxXml?proid=").append(bdcXmList.get(0).getProid()).append("&amp;printType=").append(printType).append(",");
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            logger.info(sb.toString());
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/printMulBdc")
    public void printMulBdc(String wiids,String hiddeMode,String printType,HttpServletResponse response) throws IOException{
        HashMap hashMap = new HashMap();
        List<String> proidList = null;
        List<HashMap> bdcPrintxxList = new ArrayList<HashMap>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        if(StringUtils.isNotBlank(wiids)){
            proidList = bdcXmService.getProidListByWiid(wiids);
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(proidList)){
                param.put(PARAMETER_PROIDS, proidList);
                param.put("printType",printType);
                bdcPrintxxList = bdcXmService.getPrintxxList(param);
            }
            if (CollectionUtils.isNotEmpty(bdcPrintxxList)) {
                for (Map printXx : bdcPrintxxList) {
                    hashMap.put(printXx.get(ParamsConstants.PROID_CAPITAL), printXx.get(ParamsConstants.PROID_CAPITAL));
                }
            }
            String modalUrls = "";
            String xmlUrls = "";
            String[] printXxArr = null;
            List<String> printXxList = new LinkedList<String>();
            //通过传过来的打印类型参数确定打印的模板
            if(StringUtils.isNotBlank(printType)){
                if(StringUtils.equals(printType,"sqs")){
                    modalUrls += "C:/GTIS/printsqs.fr3";
                }else if(StringUtils.equals(printType,"spb")){
                    modalUrls += "C:/GTIS/printspb.fr3";
                }else if(StringUtils.equals(printType,"fzjl")){
                    modalUrls += "C:/GTIS/printfzjl.fr3";
                }
            }
            if (hashMap != null && hashMap.size() > 0) {
                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getKey()))) {
                        printXxList.add(CommonUtil.formatEmptyValue(entry.getKey()) + "@" + CommonUtil.formatEmptyValue(entry.getValue()));
                    }
                }
                printXxArr = printXxList.toArray(new String[0]);
                if(StringUtils.equals(printType,"sqs")){
                    xmlUrls += bdcdjUrl + "/bdcPrint/getAllSqsxxXml?sqsArr=" + StringUtils.join(printXxArr, ";");
                }else if(StringUtils.equals(printType,"spb")){
                    xmlUrls += bdcdjUrl + "/bdcPrint/getAllSpbxxXml?spbArr=" + StringUtils.join(printXxArr, ";");
                }else if(StringUtils.equals(printType,"fzjl")){
                    xmlUrls += bdcdjUrl + "/bdcPrint/getAllFzjlxxXml?fzjlXxArr=" + StringUtils.join(printXxArr, ";");
                }
            }
            if (StringUtils.isNotBlank(modalUrls)) {
                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
                sb.append("<frs>");
                if (StringUtils.indexOf(modalUrls, ",") > -1) {
                    String[] modalUrlList = modalUrls.split(",");
                    String[] xmlUrlsList = xmlUrls.split(",");
                    for (int i = 0; i < modalUrlList.length; i++) {
                        sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                    }
                } else {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
                }
                sb.append(PARAMETER_XML_ENDNODE_FRS);
                response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
                PrintWriter out = response.getWriter();
                out.write(sb.toString());
                logger.info(sb.toString());
                out.flush();
                out.close();
            }
        }
    }

    //商品房首次批量打印申请书
    @ResponseBody
    @RequestMapping(value = "/printBdcSqsAll")
    public void printBdcSqsAll(String wiid, String hiddeMode, HttpServletResponse response) throws IOException{
        HashMap hashMap = new HashMap();
        List<String> proidList = new ArrayList<String>();
        List<HashMap> bdcSqsxxList = new ArrayList<HashMap>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        //获取要打印的所有proids用$符号分隔开
        String proids = bdcXmService.getProidsByQllxAndWiid(wiid, "");
        if (StringUtils.isNotBlank(proids)) {
            if(StringUtils.isNotBlank(proids)){
                String[] proidsTemp = proids.split("\\$");
                if(proidsTemp != null && proidsTemp.length > 0){
                    proidList = Arrays.asList(proidsTemp);
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(proidList)){
                param.put(PARAMETER_PROIDS, proidList);
                bdcSqsxxList = bdcXmService.getSqsxxList(param);
            }
            if (CollectionUtils.isNotEmpty(bdcSqsxxList)) {
                for (Map sqsxx : bdcSqsxxList) {
                    hashMap.put(sqsxx.get(ParamsConstants.PROID_CAPITAL), sqsxx.get(ParamsConstants.PROID_CAPITAL));
                }
            }
        }
        String modalUrls = "";
        String xmlUrls = "";
        String[] sqsArr = null;
        List<String> sqsList = new LinkedList<String>();
        modalUrls += "C:/GTIS/printsqs.fr3";
        if (hashMap != null && hashMap.size() > 0) {
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getKey()))) {
                    sqsList.add(CommonUtil.formatEmptyValue(entry.getKey()) + "@" + CommonUtil.formatEmptyValue(entry.getValue()));
                }
            }
            sqsArr = sqsList.toArray(new String[0]);
            xmlUrls += bdcdjUrl + "/bdcPrint/getAllSqsxxXml?sqsArr=" + StringUtils.join(sqsArr, ";");
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.split(",");
                String[] xmlUrlsList = xmlUrls.split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getAllFzjlxxXml")
    public void getAllFzjlxxXml(String printType,String fzjlXxArr,HttpServletResponse response) throws IOException{
        String xml = "";
        String[] printXxArrs = fzjlXxArr.split(";");
        Map<String, String> proidMap = new HashMap<String, String>();
        if (ArrayUtils.isNotEmpty(printXxArrs)) {
            for (String proidTmp : printXxArrs) {
                String[] param = proidTmp.split("@");
                String proid = param[0];
                proidMap.put(proid, proid);
            }
        }
        MulDataToPrintXml muldataToPrintXml = null;
        muldataToPrintXml = bdcXmService.getAllFzjlxxPrintXml(proidMap);
        if (muldataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(MulDataToPrintXml.class);
            xml = resultBinder.entityToXml(muldataToPrintXml, "gbk");
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_GBK);
        PrintWriter out = response.getWriter();
        out.write(xml);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/getAllSqsxxXml")
    public void getAllSqsxxXml(String sqsArr,HttpServletResponse response) throws IOException{
        String xml = "";
        String[] sqsArrs = sqsArr.split(";");
        Map<String, String> proidMap = new HashMap<String, String>();
        if (ArrayUtils.isNotEmpty(sqsArrs)) {
            for (String proidTmp : sqsArrs) {
                String[] param = proidTmp.split("@");
                String proid = param[0];
                proidMap.put(proid, proid);
            }
        }
        MulDataToPrintXml muldataToPrintXml = null;
        muldataToPrintXml = bdcXmService.getAllSqsxxPrintXml(proidMap);
        if (muldataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(MulDataToPrintXml.class);
            xml = resultBinder.entityToXml(muldataToPrintXml, "gbk");
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_GBK);
        PrintWriter out = response.getWriter();
        out.write(xml);
        out.flush();
        out.close();
    }

    //商品房首次批量打印审批表
    @ResponseBody
    @RequestMapping(value = "/printBdcSpbAll")
    public void printBdcSpbAll(String wiid, String hiddeMode, HttpServletResponse response) throws IOException{
        HashMap hashMap = new HashMap();
        List<String> proidList = new ArrayList<String>();
        List<HashMap> bdcSpbxxList = new ArrayList<HashMap>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        //获取要打印的所有proids用$符号分隔开
        String proids = bdcXmService.getProidsByQllxAndWiid(wiid, "");
        if (StringUtils.isNotBlank(proids)) {
            if(StringUtils.isNotBlank(proids)){
                String[] proidsTemp = proids.split("\\$");
                if(proidsTemp != null && proidsTemp.length > 0){
                    proidList = Arrays.asList(proidsTemp);
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(proidList)){
                param.put(PARAMETER_PROIDS, proidList);
                bdcSpbxxList = bdcXmService.getSpbxxList(param);
            }
            if (CollectionUtils.isNotEmpty(bdcSpbxxList)) {
                for (Map spbxx : bdcSpbxxList) {
                    hashMap.put(spbxx.get(ParamsConstants.PROID_CAPITAL), spbxx.get(ParamsConstants.PROID_CAPITAL));
                }
            }
        }
        String modalUrls = "";
        String xmlUrls = "";
        String[] spbArr = null;
        List<String> spbList = new LinkedList<String>();
        if (StringUtils.equals(AppConfig.getProperty("updata.bdcqzh.into.bdcXmBz"),ParamsConstants.TRUE_LOWERCASE)) {
            modalUrls += "C:/GTIS/printspbks.fr3";
        } else {
            modalUrls += "C:/GTIS/printspb.fr3";
        }
        if (hashMap != null && hashMap.size() > 0) {
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(entry.getKey()))) {
                    spbList.add(CommonUtil.formatEmptyValue(entry.getKey()) + "@" + CommonUtil.formatEmptyValue(entry.getValue()));
                }
            }
            spbArr = spbList.toArray(new String[0]);
            xmlUrls += bdcdjUrl + "/bdcPrint/getAllSpbxxXml?spbArr=" + StringUtils.join(spbArr, ";");
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.split(",");
                String[] xmlUrlsList = xmlUrls.split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getAllSpbxxXml")
    public void getAllSpbxxXml(String spbArr,HttpServletResponse response) throws IOException{
        String xml = "";
        String[] spbArrs = spbArr.split(";");
        Map<String, String> proidMap = new HashMap<String, String>();
        if (ArrayUtils.isNotEmpty(spbArrs)) {
            for (String proidTmp : spbArrs) {
                String[] param = proidTmp.split("@");
                String proid = param[0];
                proidMap.put(proid, proid);
            }
        }
        MulDataToPrintXml muldataToPrintXml = null;
        muldataToPrintXml = bdcXmService.getAllSpbxxPrintXml(proidMap);
        if (muldataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(MulDataToPrintXml.class);
            xml = resultBinder.entityToXml(muldataToPrintXml, "gbk");
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_GBK);
        PrintWriter out = response.getWriter();
        out.write(xml);
        out.flush();
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "/printAllDafpByWiids")
    public void printAllDafpByWiids(String hiddeMode,HttpServletResponse response, String wiids) throws IOException{
        List<String> proidList = new ArrayList<String>();
        List<HashMap> bdcDaFpxxList = new ArrayList<HashMap>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(wiids)) {
            String[] wiidsTemp = wiids.split(",");
            if(wiidsTemp.length > 0){
                for(String wiid : wiidsTemp){
                    if(StringUtils.isNotBlank(wiid)){
                        String proid = bdcGdxxService.getGdxxProidByWiid(wiid);
                        proidList.add(proid);
                    }
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(proidList)){
                param.put(PARAMETER_PROIDS, proidList);
                bdcDaFpxxList = bdcGdxxService.getDafpxxMapByproid(param);
            }
        }
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        StringBuilder xmlUrlsSingle = new StringBuilder();
        String[] fpArr = null;
        List<String> fpList = new LinkedList<String>();
        //取一条封皮信息的数据判断封皮类型
        for (HashMap daFpxxHashmap : bdcDaFpxxList) {
            if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_CFDJ_DM)){
                modalUrls.append("C:/GTIS/cfdafp.fr3");
            } else if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_DYDJ_DM)){
                modalUrls.append("C:/GTIS/dydafp.fr3");
            } else{
                modalUrls.append("C:/GTIS/cqdafp.fr3");
            }
            break;
        }
        if (CollectionUtils.isNotEmpty(bdcDaFpxxList)) {
            for (HashMap fpxx : bdcDaFpxxList) {
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(fpxx.get(ParamsConstants.PROID_CAPITAL))) && !fpList.contains(CommonUtil.formatEmptyValue(fpxx.get(ParamsConstants.PROID_CAPITAL)) + "@" + CommonUtil.formatEmptyValue(fpxx.get(ParamsConstants.PROID_CAPITAL)))) {
                    fpList.add(CommonUtil.formatEmptyValue(fpxx.get(ParamsConstants.PROID_CAPITAL)) + "@" + CommonUtil.formatEmptyValue(fpxx.get(ParamsConstants.PROID_CAPITAL)));
                    xmlUrlsSingle.append(bdcdjUrl).append("/bdcPrint/getDaFpxxXml?proid=").append(CommonUtil.formatEmptyValue(fpxx.get(ParamsConstants.PROID_CAPITAL))).append(",");
                }
            }
            if (fpList.size() == 1) {
                xmlUrls.append(xmlUrlsSingle.substring(0,xmlUrlsSingle.length() -1));
            } else {
                fpArr = fpList.toArray(new String[0]);
                xmlUrls.append(bdcdjUrl).append("/bdcPrint/getAllDaFpxxXml?fpArr=").append(StringUtils.join(fpArr, ";"));
            }
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/printAllDafpByProids")
    public void printAllDafpByProids(String hiddeMode,HttpServletResponse response, String proids) throws IOException{
        List<String> proidList = new ArrayList<String>();
        List<HashMap> bdcDaFpxxList = new ArrayList<HashMap>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : "false";
        if (StringUtils.isNotBlank(proids)) {
            String[] proidsTemp = proids.split(",");
            if(proidsTemp.length > 0){
                for(String proid : proidsTemp){
                    if(StringUtils.isNotBlank(proid)){
                        proidList.add(proid);
                    }
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(proidList)){
                param.put("proids", proidList);
                bdcDaFpxxList = bdcGdxxService.getDafpxxMapByproid(param);
            }
        }
        String modalUrls = "";
        String xmlUrls = "";
        String xmlUrlsSingle = "";
        String[] fpArr = null;
        List<String> fpList = new LinkedList<String>();
        //取一条封皮信息的数据判断封皮类型
        for (HashMap daFpxxHashmap : bdcDaFpxxList) {
            if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_CFDJ_DM)){
                modalUrls += "C:/GTIS/cfdafp.fr3";
            } else if (StringUtils.equals(daFpxxHashmap.get("DJLX").toString(), Constants.DJLX_DYDJ_DM)){
                modalUrls += "C:/GTIS/dydafp.fr3";
            } else{
                modalUrls += "C:/GTIS/cqdafp.fr3";
            }
            break;
        }
        if (bdcDaFpxxList.size() > 0) {
            for (HashMap fpxx : bdcDaFpxxList) {
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(fpxx.get("PROID"))) && !fpList.contains(CommonUtil.formatEmptyValue(fpxx.get("PROID")) + "@" + CommonUtil.formatEmptyValue(fpxx.get("PROID")))) {
                    fpList.add(CommonUtil.formatEmptyValue(fpxx.get("PROID")) + "@" + CommonUtil.formatEmptyValue(fpxx.get("PROID")));
                    xmlUrlsSingle += bdcdjUrl + "/bdcPrint/getDaFpxxXml?proid=" + CommonUtil.formatEmptyValue(fpxx.get("PROID")) + ",";
                }
            }
            if (fpList.size() == 1) {
                xmlUrls = xmlUrlsSingle.substring(0,xmlUrlsSingle.length() -1);
            } else {
                fpArr = fpList.toArray(new String[0]);
                xmlUrls += bdcdjUrl + "/bdcPrint/getAllDaFpxxXml?fpArr=" + StringUtils.join(fpArr, ";");
            }
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuffer sb = new StringBuffer();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.split(",");
                String[] xmlUrlsList = xmlUrls.split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append("</frs>");
            response.setContentType("text/xml;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            //System.out.println(sb.toString());
            out.flush();
            out.close();
        }
    }


    //本地打印档案信息
    @ResponseBody
    @RequestMapping(value = "/printGdDafp")
    public void printGdDafp(String hiddeMode,HttpServletResponse response, String gdxxid) throws IOException {
        //取档案封皮信息
        List<HashMap> daFpxxHashMaps = new ArrayList<HashMap>();
        List<String> gdxxidList = new ArrayList<String>();
        if(StringUtils.isNotEmpty(gdxxid)){
            if(StringUtils.isNotBlank(gdxxid)){
                String[] gdxxidsTemp = gdxxid.split(",");
                if(gdxxidsTemp != null && gdxxidsTemp.length > 0){
                    for(String gdxxidtmp : gdxxidsTemp){
                        if(StringUtils.isNotBlank(gdxxidtmp)){
                            gdxxidList.add(gdxxidtmp);
                        }
                    }
                }
            }
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(CollectionUtils.isNotEmpty(gdxxidList)){
                param.put("gdxxids", gdxxidList);
                daFpxxHashMaps = bdcGdxxService.getGdDafpxxMapByGdxxid(param);
            }
        }
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        for (HashMap daFpxxHashmap : daFpxxHashMaps) {
            modalUrls.append("C:/GTIS/cqdafp.fr3,");
            xmlUrls.append(bdcdjUrl).append("/bdcPrint/getGdDaFpxxXml?gdxxid=").append(daFpxxHashmap.get("GDXXID").toString());
            break;
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            logger.info(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getGdDaFpxxXml")
    public void getGdDaFpxxXml(String gdxxid,HttpServletResponse response) throws IOException {
        String xml = "";
        String[] gdxxidArrs = gdxxid.split(",");
        DataToPrintXml dataToPrintXml;
        dataToPrintXml = bdcGdxxService.getGdDafpxxPrintXml(gdxxidArrs[0]);
        //将实体类转换为控件数据源，即相应格式的xml
        if (dataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(DataToPrintXml.class);
            xml = resultBinder.entityToXml(dataToPrintXml,"gbk");
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_GBK);
        PrintWriter out = response.getWriter();
        out.write(xml);
        logger.info(xml);
        out.flush();
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "/printAllDafpByGdxxids")
    public void printAllDafpByGdxxids(String hiddeMode,HttpServletResponse response, String gdxxids) throws IOException{
        List<HashMap> bdcDaFpxxList = new ArrayList<HashMap>();
        List<String> gdxxidList = new ArrayList<String>();
        hiddeMode = StringUtils.isNotBlank(hiddeMode) ? hiddeMode : ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(gdxxids)) {
            String[] gdxxidsTemp = gdxxids.split(",");
            HashMap<String, Object> param = new HashMap<String, Object>();
            if(gdxxidsTemp != null && gdxxidsTemp.length > 0){
                for(String gdxxidtmp : gdxxidsTemp){
                    if(StringUtils.isNotBlank(gdxxidtmp)){
                        gdxxidList.add(gdxxidtmp);
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(gdxxidList)){
                param.put("gdxxids", gdxxidList);
                bdcDaFpxxList = bdcGdxxService.getGdDafpxxMapByGdxxid(param);
            }
        }
        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();
        StringBuilder xmlUrlsSingle = new StringBuilder();
        String[] fpArr = null;
        List<String> fpList = new LinkedList<String>();

        modalUrls.append("C:/GTIS/cqdafp.fr3");

        if (CollectionUtils.isNotEmpty(bdcDaFpxxList)) {
            for (HashMap fpxx : bdcDaFpxxList) {
                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(fpxx.get("GDXXID"))) && !fpList.contains(CommonUtil.formatEmptyValue(fpxx.get("GDXXID")) + "@" + CommonUtil.formatEmptyValue(fpxx.get("GDXXID")))) {
                    fpList.add(CommonUtil.formatEmptyValue(fpxx.get("GDXXID")) + "@" + CommonUtil.formatEmptyValue(fpxx.get("GDXXID")));
                    xmlUrlsSingle.append(bdcdjUrl).append("/bdcPrint/getGdDaFpxxXml?gdxxid=").append(CommonUtil.formatEmptyValue(fpxx.get("GDXXID"))).append(",");
                }
            }
            if (fpList.size() == 1) {
                xmlUrls.append(xmlUrlsSingle.substring(0,xmlUrlsSingle.length() -1));
            } else {
                fpArr = fpList.toArray(new String[0]);
                xmlUrls.append(bdcdjUrl).append("/bdcPrint/getAllGdDaFpxxXml?fpArr=").append(StringUtils.join(fpArr, ";"));
            }
        }
        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getAllGdDaFpxxXml")
    public void getAllGdDaFpxxXml(String fpArr,HttpServletResponse response) throws IOException{
        String xml = "";
        String[] fpArrs = fpArr.split(";");
        List<String> list = new ArrayList<String>();
        if (ArrayUtils.isNotEmpty(fpArrs)) {
            for (String gdxxidTmp : fpArrs) {
                String[] param = gdxxidTmp.split("@");
                String gdxxid = param[0];
                if(StringUtils.isNotBlank(gdxxid) && !list.contains(gdxxid)){
                    list.add(gdxxid);
                }
            }
        }
        MulDataToPrintXml muldataToPrintXml = null;
        muldataToPrintXml = bdcGdxxService.getAllGdDafpxxPrintXml(list);
        if (muldataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(MulDataToPrintXml.class);
            xml = resultBinder.entityToXml(muldataToPrintXml, "gbk");
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_GBK);
        PrintWriter out = response.getWriter();
        out.write(xml);
        logger.info(xml);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/printPpd", method = RequestMethod.GET)
    @ResponseBody
    public void printPpd(HttpServletResponse response,String proid,String hiddeMode) throws IOException {

        StringBuilder modalUrls = new StringBuilder();
        StringBuilder xmlUrls = new StringBuilder();

        modalUrls.append("C:/GTIS/ppjgPrint.fr3,");
        xmlUrls.append(bdcdjUrl).append("/bdcPrint/getPpjgXml?proid=").append(proid).append(",");

        if (StringUtils.isNotBlank(modalUrls)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<frs>");
            if (StringUtils.indexOf(modalUrls, ",") > -1) {
                String[] modalUrlList = modalUrls.toString().split(",");
                String[] xmlUrlsList = xmlUrls.toString().split(",");
                for (int i = 0; i < modalUrlList.length; i++) {
                    sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrlList[i] + "|dataURL=" + xmlUrlsList[i] + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=" + hiddeMode + "\"/>");
                }
            } else {
                sb.append("<fr url=\"eprt://v2|designMode=false|frURL=" + modalUrls + "|dataURL=" + xmlUrls + "|updateUrl=http://oa.gtis.com.cn:80/platform/pluging/update.ini|hiddeMode=true" + hiddeMode + "\"/>");
            }
            sb.append(PARAMETER_XML_ENDNODE_FRS);
            response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
            PrintWriter out = response.getWriter();
            out.write(sb.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/getPpjgXml")
    public void getBdcqzXml(String proid,HttpServletResponse response) throws IOException {
        String xml = "";
        DataToPrintXml dataToPrintXml;
        /*单个权利人*/
        dataToPrintXml = bdcPpgxService.getPpjgPrintXml(proid);
        //将实体类转换为控件数据源，即相应格式的xml
        if (dataToPrintXml != null) {
            XmlUtils resultBinder = new XmlUtils(DataToPrintXml.class);
            xml = resultBinder.entityToXml(dataToPrintXml, Constants.DEFAULT_CHARSET);
        }
        response.setContentType(RESPONSE_CONTENTTYPE_CHARSET_UTF8);
        PrintWriter out = response.getWriter();
        out.write(xml);
        logger.info(xml);
        out.flush();
        out.close();
    }
}
