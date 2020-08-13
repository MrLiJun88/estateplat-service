package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcFdcqDzService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 16-12-14
 * @description       不动产登记房地产权多幢信息
 */
@Controller
@RequestMapping("/bdcFdcqDzxx")
public class BdcFdcqDzxxController extends BaseController {
    @Autowired
    BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private Repo repository;

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 不动产单元信息
     */
    @ResponseBody
    @RequestMapping("/getBdcFwfzxxPagesJson")
    public Object getBdcdyxxPagesJson(Pageable pageable, String qlid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(qlid)) {
            map.put("qlid", qlid);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getBdcFwfzxxPagesJson", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存房地产权多幢信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcFdcqDzxx", method = RequestMethod.POST)
    public Map saveBdcFdcqDzxx(Model model, BdcFdcqDz bdcFdcqDz, BdcSpxx bdcSpxx, BdcXm bdcXm) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcFdcqDz != null && StringUtils.isNotBlank(bdcFdcqDz.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcFdcqDzService.saveBdcFdcqDz(bdcFdcqDz);
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
}
