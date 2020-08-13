package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcBdcdySd;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询不动产单元
 * User: zq
 * Date: 15-3-29
 * Time: 下午3:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryDjsjBdcdy")
public class QueryDjsjBdcdyContorller extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;


    /*审批表选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        List<Map> bdcList = bdcZdGlService.getZdBdclx();
        model.addAttribute("bdcList", bdcList);
        return "query/djsjQuerybdcdyList";
    }

    @ResponseBody
    @RequestMapping("/getBdcdyPagesJson")
    public Object getBdcdyPagesJson(Pageable pageable, String zl, String bdcdyh, String bdclx, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
        } else {
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl.trim());
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put("bdcdyh", bdcdyh.trim());
            }
        }

        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm)) {
            map.put("xzqdm", userDwdm);
        }
        return repository.selectPaging("queryDjsjBdcdyListByPage", map,pageable);
    }

    @ResponseBody
    @RequestMapping("/updateBdcDyXxzt")
    public String updateBdcDyXxzt(String bdcdyh, BdcBdcdySd bdcBdcdySd, String bdcdyid, @RequestParam(value = "xzyy") String xzyy, String xzzt) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(bdcdyid)) {
            if (bdcBdcdySd == null) {
                bdcBdcdySd = new BdcBdcdySd();
            }
            bdcBdcdySd.setXztype("1");
            entityMapper.saveOrUpdate(bdcBdcdySd, bdcBdcdySd.getBdcdyid());
        }
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/getBdcdyhQlxx")
    public HashMap getBdcdyhQlxx(String bdcdyh) {
        HashMap resultMap = new HashMap();
        //获取查封数据
        List<Map> cfList = null;
        //获取抵押数据
        List<Map> dyList = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcdyh)) {
            cfList = qllxService.getQlxxListByBdcdyh(bdcdyh, "(qlzt='3')");
            dyList = qllxService.getQlxxListByBdcdyh(bdcdyh, "(qlzt='2' or qlzt='7')");
        }

        // true 正常  false 查封
        if (CollectionUtils.isEmpty(cfList)) {
            resultMap.put("cf", true);
        } else {
            resultMap.put("cf", false);
        }
        // true 正常  false 抵押
        if (CollectionUtils.isEmpty(dyList)) {
            resultMap.put("dy", true);
        } else {
            resultMap.put("dy", false);
        }
        return resultMap;
    }

}
