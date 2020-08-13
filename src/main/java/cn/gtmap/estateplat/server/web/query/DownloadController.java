package cn.gtmap.estateplat.server.web.query;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by IntelliJ IDEA.
 * User: sc 下载工具
 * Date: 15-4-27
 * Time: 下午6:32
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/download")
public class DownloadController {
    /*审批表选择土地证**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String indexTdz(Model model) {
        return "query/download";
    }

    /*审批表选择土地证**/
    @RequestMapping(value = "/nm", method = RequestMethod.GET)
    public String nm(Model model) {
        return "query/download_nm";
    }
}
