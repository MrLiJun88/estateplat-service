package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.GdFwsyq;
import cn.gtmap.estateplat.model.server.core.GdXm;
import cn.gtmap.estateplat.server.core.service.EtlFcFjPicToBdcService;
import cn.gtmap.estateplat.server.core.service.GdXmService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lj</a>
 *         Date: 16-5-5
 *         Time: 下午7:20
 *         To change this template use File | Settings | File Templates.
 * @description 展现房产附件
 */
@Controller
@RequestMapping("/showFcFjPic")
public class ShowFcFjPicController extends BaseController {
    @Autowired
    private EtlFcFjPicToBdcService etlFcFjPicToBdcService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private EntityMapper entityMapper;


    @RequestMapping("openImg")
    public String openImg(Model model, @RequestParam(value = "qlid", required = false) String qlid) {
        List<HashMap> imgRouteList = etlFcFjPicToBdcService.getImageRoute(qlid);
        List<String> imgPathList = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(imgRouteList)) {
            for (HashMap map : imgRouteList) {
                String path = bdcdjUrl + "/showFcFjPic/showImg?filePath=" + CommonUtil.formatEmptyValue(map.get("FILEPATH")) + "&fileName=" + CommonUtil.formatEmptyValue(map.get("RIMGID"));
                imgPathList.add(path);
            }
        }
        if (CollectionUtils.isNotEmpty(imgPathList)) {
            model.addAttribute("list", imgPathList);
        }
        return "query/showImg";
    }

    @RequestMapping(value = "showImg")
    public void ShowImg(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "filePath", required = false) String filePath, @RequestParam(value = "fileName", required = false) String fileName) throws IOException {
        if (StringUtils.isBlank(filePath))
            filePath = "";
        byte[] data = etlFcFjPicToBdcService.getImage(filePath, fileName);
        if (data != null && data.length > 0) {
            response.setContentType("image/*"); //设置返回的文件类型
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
            out = null;
        }
    }

    /**
     * 查看房产系统中的附件，直接调取房产的页面
     *
     * @param model
     * @param qlid
     * @return
     */
    @ResponseBody
    @RequestMapping("openFcxx")
    public HashMap openFcxx(Model model, @RequestParam(value = "qlid", required = false) String qlid) {
        String params = "";
        List<HashMap> fcywidlist = etlFcFjPicToBdcService.getfcywid(qlid);
        if (CollectionUtils.isNotEmpty(fcywidlist)) {
            String proid = CommonUtil.formatEmptyValue(fcywidlist.get(0).get("PROID"));
            GdXm gdXm = gdXmService.getGdXm(proid);
            if (gdXm != null) {
                params = CommonUtil.formatEmptyValue(fcywidlist.get(0).get("OINSID")) + "_" + gdXm.getSlbh() + "_" + qlid.substring(5, qlid.length());
            }
        }
        System.out.println("传给房产的值" + params);
        HashMap resultMap = new HashMap();
        resultMap.put("params", params);
        return resultMap;
    }

    /**
     * 给交易的权属查询页面
     */
    @RequestMapping(value = "showQsxx")
    public void showQsxx(@RequestParam(value = "fczh", required = false) String fczh, HttpServletResponse response,HttpServletRequest request) throws Exception {    	
        if (StringUtils.isNotBlank(fczh)) {
            Example example = new Example(GdFwsyq.class);
            example.createCriteria().andLike("fczh",fczh);
            List<GdFwsyq> gdFwsyqList = entityMapper.selectByExample(example);
            String zsids ="";
            String zsid = "";
            if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
            	for(GdFwsyq gdFwsyq :gdFwsyqList){
           		 zsids += "\'"+gdFwsyq.getQlid()+"\'"+',';
           		 zsid= zsids.substring(1,zsids.length()-2);
           	}
                String url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/bdc_qscxjgzm_fc&op=write&cptName=bdc_qscxjgzm_fc" + "&zsid=" +java.net.URLEncoder.encode(zsid,"utf-8");  
                response.sendRedirect(url);
            }else {
                Example zsExample = new Example(BdcZs.class);
                zsExample.createCriteria().andEqualTo("bdcqzh", StringUtils.deleteWhitespace(fczh));
                List<BdcZs> bdcZsList = entityMapper.selectByExample(zsExample);
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    String url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/bdc_qscxjgzm_fc&op=write&cptName=bdc_qscxjgzm_fc" + "&zsid=" +java.net.URLEncoder.encode(bdcZsList.get(0).getZsid(), "utf-8");
                    response.sendRedirect(url);
                }
            }
        }
    }


}
