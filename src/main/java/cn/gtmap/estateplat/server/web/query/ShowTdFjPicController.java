package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.server.core.service.EtlFcFjPicToBdcService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jiangganzhi
 * @description 展现土地附件
 */

@Controller
@RequestMapping("/showTdFjPic")
public class ShowTdFjPicController extends BaseController {
    @Autowired
    private EtlFcFjPicToBdcService etlFcFjPicToBdcService;


    @RequestMapping("openImg")
    public String openImg(Model model, @RequestParam(value = "projectid", required = false) String projectid) {
        List<HashMap> imgRouteList = etlFcFjPicToBdcService.getTdImageRoute(projectid);
        List<String> imgPathList = new ArrayList<String>();
        List<HashMap> imgPathMapList = new LinkedList<HashMap>();
        if (CollectionUtils.isNotEmpty(imgRouteList)) {
            for (HashMap map : imgRouteList) {
                HashMap urlMap = new HashMap();
                String storeUrl = map.get("STORE_URL").toString();
                String route = "";
                if (StringUtils.contains(storeUrl,"store://fileStore1/")){
                    route ="content1\\";
                }
                else if(StringUtils.contains(storeUrl,"store://fileStore2/")){
                    route ="content2\\";
                }else {
                    route ="附件上传\\";
                }
                storeUrl = storeUrl.replace("store://fileStore1/","");
                storeUrl = storeUrl.replace("store://fileStore2/","");
                storeUrl = storeUrl.replace("/","\\");
                String clickUrl = storeUrl.replace("\\","\\\\");
                String path = bdcdjUrl + "/showTdFjPic/showImg?filePath=" + route + CommonUtil.formatEmptyValue(storeUrl) + "&fileName=";
                String clickPath = bdcdjUrl + "/showTdFjPic/showImg?filePath=" + route + "\\" + CommonUtil.formatEmptyValue(clickUrl) + "&fileName=";
                imgPathList.add(path);
                urlMap.put("pagePath",path);
                urlMap.put("clickPath",clickPath);
                imgPathMapList.add(urlMap);
            }
        }
        if (CollectionUtils.isNotEmpty(imgPathList)) {
            model.addAttribute("list", imgPathList);
        }
        if (CollectionUtils.isNotEmpty(imgPathMapList)) {
            model.addAttribute("maplist", imgPathMapList);
        }
        return "query/showTdImg";
    }

    @RequestMapping(value = "showImg")
    public void ShowImg(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "filePath", required = false) String filePath, @RequestParam(value = "fileName", required = false) String fileName) throws IOException {
        if (StringUtils.isBlank(filePath))
            filePath = "";
        byte[] data = etlFcFjPicToBdcService.getTdImage(filePath, fileName);
        if (data != null && data.length > 0) {
            response.setContentType("image/*"); //设置返回的文件类型
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
            out = null;
        }
    }
}
