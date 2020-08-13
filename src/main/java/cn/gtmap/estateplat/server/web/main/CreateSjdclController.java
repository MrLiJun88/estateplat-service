package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.xml.XmlUtils;
import cn.gtmap.estateplat.model.server.core.BdcSjcl;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSjclService;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.model.FoldersModel;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.Charsets;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-3-17
 * Time: 下午6:23
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/createSjdcl")
public class CreateSjdclController extends BaseController {
    private static final long serialVersionUID = -2676839172755812066L;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    BdcXmService bdcXmService;

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcSjclService bdcSjclService;

    @RequestMapping(value = "/createAllFileFolder", method = RequestMethod.GET)
    public String createAllFileFolder(Model model, @RequestParam(value = "proid", required = false) String proid,
                                      @RequestParam(value = "clmc", required = false) String clmc, @RequestParam(value = "wiid", required = false) String wiid) {
        String fileCenterUrl = "fcm";
        Integer project_fileId = null;
        if (super.fileCenterUrl != null && super.fileCenterUrl.length() > 4)
            fileCenterUrl = fileCenterUrl + super.fileCenterUrl.substring(4);
        try {
            if (proid != null && !proid.equals("")) {
                project_fileId = PlatformUtil.getProjectFileId(proid);
                if (StringUtils.isBlank(wiid)) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    if(bdcXm!=null)
                     wiid = bdcXm.getWiid();
                }
                List<BdcSjcl> bdcSjclList = bdcSjdService.getSjclListByWiid(wiid,proid);
                if (CollectionUtils.isNotEmpty(bdcSjclList)) {
                    for (BdcSjcl bdcSjcl1 : bdcSjclList) {
                        Integer clNodeId=null;
                        if (StringUtils.isNotBlank(bdcSjcl1.getClmc()))
                            clNodeId=PlatformUtil.createFileFolderByclmcAndnodeid(project_fileId, bdcSjcl1.getClmc(),bdcSjcl1.getWjzxid());

                        if(bdcSjcl1.getWjzxid()==null){
                            bdcSjcl1.setWjzxid(clNodeId);
                            entityMapper.updateByPrimaryKeySelective(bdcSjcl1);
                        }
                    }
                }
                if (StringUtils.isNotBlank(clmc)) {
                    clmc = URLDecoder.decode(clmc, Constants.DEFAULT_CHARSET);
                    project_fileId = PlatformUtil.createFileFolderByclmc(project_fileId, clmc);
                }
            }
        } catch (Exception e) {
           logger.error("",e);
        }
        model.addAttribute("fileCenterUrl", fileCenterUrl);
        model.addAttribute("nodeId", project_fileId);
        return "main/sjd";
    }

    @RequestMapping(value = "/saveSjclFs", method = RequestMethod.POST)
    @ResponseBody
    public String saveSjclFs(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        String msg = "ok";
        if (StringUtils.isNotBlank(proid)) {
            if (StringUtils.isBlank(wiid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                wiid = bdcXm.getWiid();
            }
            List<BdcSjcl> bdcSjclList = bdcSjdService.getSjclListByWiid(wiid,proid);
            Integer project_fileId = PlatformUtil.getProjectFileId(proid);
            if (CollectionUtils.isNotEmpty(bdcSjclList)) {
                for (BdcSjcl bdcSjcl1 : bdcSjclList) {
                    if (StringUtils.isNotBlank(bdcSjcl1.getClmc())) {
                        bdcSjcl1.setFs(PlatformUtil.getAllChildFilesCountByNodeName(project_fileId, bdcSjcl1.getClmc()));
                        bdcSjcl1.setYs(PlatformUtil.getAllChildFilesCountByNodeName(project_fileId, bdcSjcl1.getClmc()));
                        bdcSjdService.saveSjcl(bdcSjcl1);
                    }
                }
            }
        }
        return msg;
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteFile(@RequestParam(value = "proid", required = false) String proid,@RequestParam(value = "sjxxid", required = false) String sjxxid, @RequestParam(value = "clmc", required = false) String clmc,@RequestParam(value = "sjclid", required = false) String sjclid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        BdcSjcl bdcSjcl=null;
        final char[] strChar = sjclid.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        if(firstChar=='['){
            JSONArray sjclID=JSONArray.parseArray(sjclid);
            JSONArray sjclmc=JSONArray.parseArray(clmc);
            if(sjclID!=null&&sjclID.size()>0&&StringUtils.isNotBlank(proid)){
                for (int i = 0; i < sjclID.size(); i++) {
                    if(sjclID.get(i)!=null){
                        bdcSjcl=bdcSjclService.getBdcSjclById(sjclID.get(i).toString());
                        //判断sjclid是否可以找到数据
                        if(bdcSjcl!=null){
                            bdcSjclService.delSjclxx(sjclID.get(i).toString());
                        }
                        else{
                            if(sjclmc.get(i)!=null){
                                bdcSjclService.delSjclBySjxxidAndClmc(sjxxid,sjclmc.get(i).toString());
                            }
                        }
                    }
                    if (sjclmc.get(i)!=null) {
                        PlatformUtil.deleteFileByProidAndClmc(proid, sjclmc.getString(i));
                    }
                }
                returnvalue = "success";
            }
        } else {
            if (StringUtils.isNotBlank(sjclid) && StringUtils.isNotBlank(proid)) {
                bdcSjcl=bdcSjclService.getBdcSjclById(sjclid);
                //判断sjclid是否可以找到数据
                if (bdcSjcl!=null) {
                    bdcSjclService.delSjclxx(sjclid);
                }
                else{
                    if(StringUtils.isNotBlank(clmc)){
                        bdcSjclService.delSjclBySjxxidAndClmc(sjxxid,clmc);
                    }
                }
                if (StringUtils.isNotBlank(clmc)) {
                    PlatformUtil.deleteFileByProidAndClmc(proid, clmc);
                }
                returnvalue = "success";
            }
        }
        map.put("msg", returnvalue);
        return map;
    }
    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/uploadSjcl")
    public String gdFile(HttpServletRequest request)  {
        String result = "0";
        String xml = "";
        BufferedReader br=null;
        try {
            // 读取请求内容
            if (request.getInputStream() != null) {
                br = new BufferedReader(new InputStreamReader(request.getInputStream(), Charsets.CHARSET_UTF8));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                xml = URLDecoder.decode(sb.toString(), Constants.DEFAULT_CHARSET);
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(xml)) {
                XmlUtils xmlUtils = new XmlUtils(FoldersModel.class);
                FoldersModel foldersModel = (FoldersModel) xmlUtils.fromXml(xml, false);
                if (foldersModel != null)
                    bdcSjdService.updateSjclsFiles(foldersModel);
                result = "1";
            }
        } catch (Exception e) {
            result = "0";
            logger.error("CreateSjdclController.gdFile",e);
        } finally {
            if (br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("CreateSjdclController.gdFile",e);
                }
            }
        }
        return result;
    }
}
