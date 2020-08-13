package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
import com.gtis.web.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lst 查封
 */
@Controller
@RequestMapping("/bdcCf")
public class BdcCfController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;


    /*查封列表**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        String bdcCfGjss = AppConfig.getProperty("bdcCfGjss.order");
        List<String> bdcCfGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(bdcCfGjss) && bdcCfGjss.split(",").length > 0){
            for(String bdcCfGjssZd : bdcCfGjss.split(",")){
                bdcCfGjssOrderList.add(bdcCfGjssZd);
            }
        }
        model.addAttribute("bdcCfGjss", bdcCfGjss);
        model.addAttribute("bdcCfGjssOrderList", bdcCfGjssOrderList);
        return "query/bdcCfdjList";
    }

    @ResponseBody
    @RequestMapping("/queryCfdjList")
    public Object queryCfdjList(Pageable pageable, String djqssj, String djjssj, String jfqssj, String jfjssj, String cfqsjsqx, String cfjsjsqx, BdcCf bdcCf, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isNotBlank(dcxc)) {
                map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                map.put("qszt", bdcCf.getQszt());
                map.put("ywh", bdcCf.getYwh());
                map.put("dbr", bdcCf.getDbr());
                map.put("jfdbr", bdcCf.getJfdbr());
                map.put("bzxr", bdcCf.getBzxr());
                if (StringUtils.isNotBlank(djqssj)) {
                    map.put("djqssj", simpleDateFormat.parse(djqssj));
                }
                if (StringUtils.isNotBlank(djjssj)) {
                    map.put("djjssj", simpleDateFormat.parse(djjssj));
                }
                if (StringUtils.isNotBlank(jfqssj)) {
                    map.put("jfqssj", simpleDateFormat.parse(jfqssj));
                }
                if (StringUtils.isNotBlank(jfjssj)) {
                    map.put("jfjssj", simpleDateFormat.parse(jfjssj));
                }
                if (StringUtils.isNotBlank(cfqsjsqx)) {
                    map.put("cfqsjsqx", simpleDateFormat.parse(cfqsjsqx));
                }
                if (StringUtils.isNotBlank(cfjsjsqx)) {
                    map.put("cfjsjsqx", simpleDateFormat.parse(cfjsjsqx));
                }
            }
            //根据行政区过滤
            String userDwdm = super.getWhereXzqdm();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm)) {
                map.put("xzqdm", userDwdm);
            }
        } catch (ParseException e) {
            logger.error("BdcCfController.queryCfdjList",e);
        }
        return repository.selectPaging("queryBdcCfByPage", map, pageable);
    }

    /**
     * 自动解封操作
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/jf")
    public String jf(Model model, BdcCf bdcCf) {
        String result = "解封成功";
        try {
            bdcCf.setJfdbr(SessionUtil.getCurrentUser().getUsername());
            bdcCf.setJfdjsj(new Date());
            bdcCf.setQszt(2);
            entityMapper.updateByPrimaryKeySelective(bdcCf);
        } catch (Exception e) {
            result = "解封失败";
            logger.error("BdcCfController.jf",e);
        }
        return result;
    }
}
