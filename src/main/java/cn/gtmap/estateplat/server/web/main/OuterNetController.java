package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.BdcSqlxQllxRel;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.model.OntBdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSqlxQllxRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.OntService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ZipFileUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @description 外网收件控制器
 */
@Controller
@RequestMapping("ont")
public class OuterNetController extends  BaseController{
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSqlxQllxRelService bdcSqlxQllxRelService;
    @Autowired
    private OntService ontService;
    @Autowired
    private DjxxMapper djxxMapper;

    @RequestMapping("toOntSjgl")
    public String toOntSjgl(Model model, String proid, String wiid) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(wiid)) {
            HashMap querymap = new HashMap();
            querymap.put("wiid", wiid);
            bdcXmList = bdcXmService.andEqualQueryBdcXm(querymap);
        }
        BdcXm xmxx = null;
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            xmxx = bdcXmList.get(0);
        } else {
            xmxx = bdcXmService.getBdcXmByProid(proid);
        }
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("wiid", wiid);
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String bdclxdm = "";
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        String qlxzdm = "";
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        String workFlowName = PlatformUtil.getWorkFlowNameByProid(proid);
        String qllx = "";
        String dyfs = "";
        String ysqlxdm = "";
        if (StringUtils.isNotBlank(workFlowName)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(workFlowName);
            if (CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                if (map.get(ParamsConstants.QLLXDM_CAPITAL) != null)
                    qllx = CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL));
            }
        }

        if (xmxx != null && StringUtils.isNotBlank(xmxx.getSqlx())) {

            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sqlxdm", xmxx.getSqlx());
            if (StringUtils.isNotBlank(qllx))
                queryMap.put("qllxdm", qllx);
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }

        if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm()))
                zdtzm = bdcSqlxQllxRel.getZdtzm();
            if (bdcSqlxQllxRel.getBdcdyly() != null)
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getDyfs()))
                dyfs = bdcSqlxQllxRel.getDyfs();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getBdclx()))
                bdclxdm = bdcSqlxQllxRel.getBdclx();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getQlxzdm()))
                qlxzdm = bdcSqlxQllxRel.getQlxzdm();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYsqlxdm()))
                ysqlxdm = bdcSqlxQllxRel.getYsqlxdm();
        }
        model.addAttribute("dyfs", dyfs);
        model.addAttribute(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm);
        model.addAttribute(ParamsConstants.ZDTZM_LOWERCASE, zdtzm);
        model.addAttribute(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm);
        String bdcdjUrl = AppConfig.getProperty("bdcdj.url");
        model.addAttribute("bdcdyly", bdcdyly);
        model.addAttribute("bdcdjUrl", bdcdjUrl);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm);
        StringBuilder bdcdyhs = new StringBuilder();
        List<OntBdcXm> ontBdcXmList = (List<OntBdcXm>) session.getAttribute("ontBdcXm_" + proid);
        if (ontBdcXmList != null && !ontBdcXmList.isEmpty()) {
            for (OntBdcXm ontBdcXm : ontBdcXmList) {
                String bdcdyh = ontBdcXm.getBdcdyh();
                if (StringUtils.isNotBlank(bdcdyh)) {
                    bdcdyhs.append(bdcdyh);
                    bdcdyhs.append(",");
                }
            }
        }
        if (StringUtils.isNotBlank(bdcdyhs)) {
            model.addAttribute(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhs.substring(0, bdcdyhs.length() - 1));
        }
        return "sjgl/ontSjgl";
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param proid,multipartFile
     * @description 解压缩上传的压缩包,读取压缩包下的Excel并将附件文件存放至session中
     */
    @ResponseBody
    @RequestMapping("uncompressZip")
    public void uncompressZip(String proid, MultipartFile multipartFile) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        //获取上传的文件的文件名
        String originalFilename = multipartFile.getOriginalFilename();
        //获取文件存放位置
        String saveFileDir = ontService.appendPathAndDeleteFile(originalFilename,"");
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new AppException("获取文件输入流出错!");
        }
        if(inputStream != null){
            //解压缩压缩包至指定文件夹
            ZipFileUtil.uncompressZip(inputStream, saveFileDir);
            File root = new File(ZipFileUtil.appendFileSeparator(saveFileDir));
            if (root.exists()) {
                //获取文件夹下所有文件
                File[] files = root.listFiles();
                List<Object> list = null;
                if (files != null) {
                    for (File file : files) {
                        //只获取一级目录下的Excel文件,读取Excel文件内容初始化外网不动产项目,外网权利人(义务人)两个类
                        String tempFileName = file.getName();
                        if (file.isFile() && (StringUtils.equals(tempFileName.substring(tempFileName.lastIndexOf(".")), ".xls") || StringUtils.equals(tempFileName.substring(tempFileName.lastIndexOf(".")), ".xlsx"))) {
                            String fileName = saveFileDir + file.getName();
                            try {
                                File tempFile = new File(fileName);
                                if (tempFile.exists()) {
                                    list = ontService.getExcelAsInputStream(new FileInputStream(tempFile), proid, "", "");
                                }
                            } catch (Exception e) {
                                logger.error("OuterNetController.uncompressZip",e);
                            }
                        }
                    }
                    //如果初始化后的list不为空,则将附件存放至session中(Excel文件也存放至了session中,但之后上传附件时会加以判断所以不影响)
                    if (list != null && !list.isEmpty()) {
                        session.setAttribute("ontFiles_" + proid, files);
                    }
                }
            }
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                logger.error("OuterNetController.uncompressZip",e);
            }
        }
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param
     * @return
     * @description excel选择页面
     */
    @RequestMapping("toSelectExcel")
    public String toSelectExcel(Model model){
        model.addAttribute("bdcdjUrl", bdcdjUrl);
        model.addAttribute("portalUrl",portalUrl);
        model.addAttribute("platformUrl",platformUrl);
        return "sjgl/ontExcel";
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param multipartFile
     * @return
     * @description 外网收件批量创建项目
     */
    @ResponseBody
    @RequestMapping("readExcel")
    public void readExcel(MultipartFile multipartFile,String type,String id){
        //获取上传的文件的文件名
        String originalFilename = multipartFile.getOriginalFilename();
        //获取文件存放位置
        String saveFileDir = ontService.appendPathAndDeleteFile(originalFilename,type);
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new AppException("获取文件输入流出错!");
        }
        if(inputStream != null){
            try {
                ZipFileUtil.uncompressZip(inputStream,saveFileDir);
                //获取解压后服务器上的文件路径
                File file = new File(saveFileDir);
                if(file.exists() && file.listFiles()!=null && file.listFiles().length != 0) {
                    for (File tempFile : file.listFiles()) {
                        String fileName = tempFile.getName();
                        if (tempFile.isFile() && (StringUtils.equals(fileName.substring(fileName.lastIndexOf(".")), ".xls") || StringUtils.equals(fileName.substring(fileName.lastIndexOf(".")), ".xlsx"))) {
                            ontService.getOntBdcXmInfoByExcel(new FileInputStream(tempFile), saveFileDir, id);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("OuterNetController.readExcel",e);
            } finally {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    logger.error("OuterNetController.readExcel",e);
                }
            }
        }
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param
     * @return xml
     * @description 获取session中的XML数据
     */
    @ResponseBody
    @RequestMapping("getOntXmlData")
    public String getOntXmlData(String id){
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        String xml = StringUtils.EMPTY;
        if(session.getAttribute("ontXml_" + id) != null){
            xml = session.getAttribute("ontXml_" + id).toString();
            session.removeAttribute("ontXml_" + id);
        }
        return xml;
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param proid,sjh,bdcdyh
     * @return
     * @description 根据配置的文件路径获取附件文件
     */
    @ResponseBody
    @RequestMapping("getFileFromDir")
    public String getFileFromDir(String proid, String sjh, String bdcdyh,String saveFileDir) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        StringBuilder msg = new StringBuilder();
        if(StringUtils.isNotBlank(saveFileDir)){
            if(!new File(saveFileDir).exists()){
                new File(saveFileDir).mkdirs();
            }
            saveFileDir = ZipFileUtil.appendFileSeparator(saveFileDir);
            File zipFile = new File(saveFileDir);
            File[] zipFiles = zipFile.listFiles();
            if(zipFiles != null && zipFiles.length != 0){
                for(File file : zipFiles){
                    //获取session中的Excel文件路径
                    String fileName = file.getName();
                    if(file.isFile() && (StringUtils.equals(fileName.substring(fileName.lastIndexOf(".")), ".xls") || StringUtils.equals(fileName.substring(fileName.lastIndexOf(".")), ".xlsx"))){
                        String filePath = file.getAbsolutePath();
                        if (StringUtils.isNotBlank(filePath) && new File(filePath).exists()) {
                            //获取Excel内信息,并组织成OntBdcXm,OntBdcQlr
                            try {
                                ontService.getExcelAsInputStream(new FileInputStream(new File(filePath)), proid, bdcdyh, sjh);
                            } catch (FileNotFoundException e) {
                                logger.error("OuterNetController.getFileFromDir",e);
                            }
                        } else {
                            msg.append("无法获取Excel文件!");
                        }
                    }
                    if(file.isDirectory() && StringUtils.equals(fileName,sjh)){
                        saveFileDir = ZipFileUtil.appendFileSeparator(file.getAbsolutePath());
                        File root = new File(saveFileDir);
                        if (root.exists()) {
                            session.setAttribute("ontFiles_" + proid, root);
                        }
                    }
                }
            }else{
                msg.append("指定路径不存在或指定路径下不存在任何文件!");
            }
        }else{
            msg.append("文件路径配置错误!");
            throw new AppException("未配置压缩包解压后文件存放路径!");
        }
        return msg.toString();
    }

    @ResponseBody
    @RequestMapping("getBdcdyInfo")
    public Object getBdcdylyForOnt(String proid,String sjh){
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Map<String,Object> resultMap = new HashMap<String, Object>();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String workFlowName = PlatformUtil.getWorkFlowNameByProid(proid);
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String bdclxdm = "";
        String qlxzdm = "";
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        String qllx = "";
        String dyfs = "";
        String ysqlxdm = "";
        if (StringUtils.isNotBlank(workFlowName)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(workFlowName);
            if (mapList != null && !mapList.isEmpty()) {
                for(Map map : mapList){
                    if (map.get(ParamsConstants.QLLXDM_CAPITAL) != null){
                        qllx = CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL));
                    }
                }
            }
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx())) {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sqlxdm", bdcXm.getSqlx());
            if (StringUtils.isNotBlank(qllx)){
                queryMap.put("qllxdm", qllx);
            }
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }
        if (bdcSqlxQllxRelList != null && !bdcSqlxQllxRelList.isEmpty()) {
            for(BdcSqlxQllxRel bdcSqlxQllxRel : bdcSqlxQllxRelList){
                if (StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm())){
                    zdtzm = bdcSqlxQllxRel.getZdtzm();
                }
                if (bdcSqlxQllxRel.getBdcdyly() != null){
                    bdcdyly = bdcSqlxQllxRel.getBdcdyly();
                }
                if (StringUtils.isNotBlank(bdcSqlxQllxRel.getDyfs())){
                    dyfs = bdcSqlxQllxRel.getDyfs();
                }
                if (StringUtils.isNotBlank(bdcSqlxQllxRel.getBdclx())){
                    bdclxdm = bdcSqlxQllxRel.getBdclx();
                }
                if (StringUtils.isNotBlank(bdcSqlxQllxRel.getQlxzdm())){
                    qlxzdm = bdcSqlxQllxRel.getQlxzdm();
                }
                if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYsqlxdm())){
                    ysqlxdm = bdcSqlxQllxRel.getYsqlxdm();
                }
            }
            StringBuilder bdcdyhs = new StringBuilder();
            List<OntBdcXm> ontBdcXmList = (List<OntBdcXm>) session.getAttribute("ontBdcXm_" + proid);
            if (ontBdcXmList != null && !ontBdcXmList.isEmpty()) {
                for (OntBdcXm ontBdcXm : ontBdcXmList) {
                    if(StringUtils.equals(ontBdcXm.getSjh(),sjh)){
                        String bdcdyh = ontBdcXm.getBdcdyh();
                        if (StringUtils.isNotBlank(bdcdyh)) {
                            bdcdyhs.append(bdcdyh);
                            bdcdyhs.append(",");
                        }
                    }
                }
            }else{
                resultMap.put("msg","获取外网不动产项目失败!");
            }
            if(StringUtils.isNotBlank(bdcdyhs)){
                resultMap.put(ParamsConstants.BDCDYHS_LOWERCASE,bdcdyhs.substring(0,bdcdyhs.length()-1));
            }
            resultMap.put(ParamsConstants.PROID_LOWERCASE,proid);
            resultMap.put("wiid",bdcXm.getWiid());
            resultMap.put(ParamsConstants.ZDTZM_LOWERCASE,zdtzm);
            resultMap.put("bdcdyly",bdcdyly);
            resultMap.put("dyfs",dyfs);
            resultMap.put(ParamsConstants.BDCLXDM_LOWERCASE,bdclxdm);
            resultMap.put(ParamsConstants.QLXZDM_LOWERCASE,qlxzdm);
            resultMap.put(ParamsConstants.YSQLXDM_LOWERCASE,ysqlxdm);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("getDjsjBdcdyInfo")
    public Object getDjsjBdcdyInfo(String bdclxdm, String zdtzm, String qlxzdm,String bdcdyhs){
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        if (StringUtils.isNotBlank(qlxzdm)) {
            map.put(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm.split(","));
        }
        map.put("bdclx","TDFW");
        if(StringUtils.isNotBlank(bdcdyhs)){
            List<String> bdcdyhList = new ArrayList<String>();
            for(String tempBdcdyh : bdcdyhs.split(",")){
                if(StringUtils.isNotBlank(tempBdcdyh)){
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put(ParamsConstants.BDCDYHS_LOWERCASE,bdcdyhList);
        }else{
            throw new AppException("不动产单元信息获取失败!");
        }
        String userDwdm = sysUserService.getUserRegionCode(SessionUtil.getCurrentUserId());
        if (StringUtils.isNotBlank(userDwdm)) {
            while (StringUtils.isNotBlank(userDwdm) && userDwdm.endsWith("0")) {
                userDwdm = userDwdm.substring(0, userDwdm.length() - 1);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm)){
            map.put("xzqdm", userDwdm);
        }
        return djxxMapper.getDjsjBdcdyByPage(map);
    }

    @ResponseBody
    @RequestMapping("getBdcZsInfo")
    public Object getBdcZsInfo(String bdclxdm, String qllx,String zdtzm, String qlxzdm,String dyfs,String ysqlxdm, String proid,String bdcdyhs){
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(bdclxdm)) {
            map.put(ParamsConstants.BDCLXDM_LOWERCASE, bdclxdm.split(","));
        }
        if (StringUtils.isNotBlank(qllx)) {
            map.put("qllx", qllx.split(","));
        }
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put(ParamsConstants.ZDTZM_LOWERCASE, zdtzm.split(","));
        }
        if (StringUtils.isNotBlank(dyfs)) {
            map.put("dyfs", dyfs);
        }
        if (StringUtils.isNotBlank(qlxzdm)) {
            map.put(ParamsConstants.QLXZDM_LOWERCASE, qlxzdm.split(","));
        }
        if (StringUtils.isNotBlank(ysqlxdm)) {
            map.put(ParamsConstants.YSQLXDM_LOWERCASE, ysqlxdm.split(","));
        }
        if(StringUtils.isNotBlank(bdcdyhs)){
            List<String> bdcdyhList = new ArrayList<String>();
            for(String tempBdcdyh : bdcdyhs.split(",")){
                if(StringUtils.isNotBlank(tempBdcdyh)){
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put(ParamsConstants.BDCDYHS_LOWERCASE,bdcdyhList);
        }else{
            throw new AppException("不动产单元信息获取失败!");
        }
        //根据行政区过滤 查封的无不动产权证号的无法查出
        String userDwdm = sysUserService.getUserRegionCode(SessionUtil.getCurrentUserId());
        if (StringUtils.isNotBlank(userDwdm)) {
            while (StringUtils.isNotBlank(userDwdm) && userDwdm.endsWith("0")) {
                userDwdm = userDwdm.substring(0, userDwdm.length() - 1);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm)){
            map.put("xzqdm", userDwdm);
        }
        //sc 需要展示不生成证书的权力信息
        map.put("filterNullBdcqzh", true);
        return bdcXmService.getBdcZsByPage(map);
    }


    /**
     * @param model
     * @param userid
     * @version 1.0, 2018/7/3
     * @author<a href = "mailto:liujie@gtmap.cn">liujie</a>
     * @description 批量受理
     */
    @RequestMapping(value = "/batchCreateXm")
    public String batchCreateXm(Model model, String userid) {
        model.addAttribute("userid", userid);
        String wxgzhDwdm = "";
        Integer sldPrintNum = 1;
        if(StringUtils.isNotBlank(AppConfig.getProperty("sld_print_num"))){
            sldPrintNum = Integer.parseInt(AppConfig.getProperty("sld_print_num"));
        }
        wxgzhDwdm = AppConfig.getProperty("dwdm.wxgzh");
        model.addAttribute("wxgzhDwdm",wxgzhDwdm);
        model.addAttribute("sldPrintNum", sldPrintNum);
        return "outerNet/ont";
    }
}
