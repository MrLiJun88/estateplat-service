package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcZsbhSy")
public class BdcZsbhSyController extends BaseController {
    @Autowired
    private Repo repository;

    @RequestMapping(value = "")
    public String index(Model model, String zsid, String zsbh) {
        model.addAttribute("zsid", zsid);
        model.addAttribute("zsbh", zsbh);
        return "sjgl/bdcZsbhSy";
    }

    @ResponseBody
    @RequestMapping(value = "/getBdcZsbhSyPagesJson")
    public Object getBdcZsbhSyPages(Pageable pageable, String zsid, String zsbh, String dcxc,
                                    String fzr, String fzrq, String bdcdyh, String bdcqzh) {
        Page<HashMap> dataPaging = null;
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            String search = StringUtils.deleteWhitespace(dcxc);
            map.put("dcxc", StringUtils.isNotBlank(search) ? search : null);
            map.put("zsid", StringUtils.isNotBlank(zsid) ? zsid : null);
            map.put("zsbh", StringUtils.isNotBlank(zsbh) ? zsbh : null);
            map.put("fzr", StringUtils.isNotBlank(fzr) ? StringUtils.deleteWhitespace(fzr) : null);
            map.put("fzrq", StringUtils.isNotBlank(fzrq) ? StringUtils.deleteWhitespace(fzrq) : null);
            map.put("bdcdyh", StringUtils.isNotBlank(bdcdyh) ? StringUtils.deleteWhitespace(bdcdyh) : null);
            map.put("bdcqzh", StringUtils.isNotBlank(bdcqzh) ? StringUtils.deleteWhitespace(bdcqzh) : null);
            dataPaging = repository.selectPaging("getBdcZsbhSyListByPage", map, pageable);
        } catch (Exception e) {
            logger.error("bdcZsbhSy/getBdcZsbhSyPagesJson", e);
        }
        return dataPaging;
    }
}
