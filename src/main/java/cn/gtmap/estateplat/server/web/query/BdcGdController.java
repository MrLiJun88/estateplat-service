package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.server.core.service.BdcGdxxService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.service.ArchivePostService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadJsonFileUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-09
 * @description 不动产收档查询
 */
@Controller
@RequestMapping("/bdcsd")
public class BdcGdController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ArchivePostService archivePostService;
    @Autowired
    private BdcGdxxService bdcGdxxService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "/query/bdcsdQuery";
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 查询
     */
    @ResponseBody
    @RequestMapping(value = "/getSdxxByPage", method = RequestMethod.GET)
    public Object getBdcZsListByPage(Pageable pageable, String sjh, String fwbm, String zl, String bdcqzh, String exactQuery) {
        Map param = Maps.newHashMap();
        if (StringUtils.isNotBlank(sjh)) {
            param.put(ParamsConstants.SJH_LOWERCASE, StringUtils.deleteWhitespace(sjh));
        }
        if (StringUtils.isNotBlank(bdcqzh)) {
            param.put(ParamsConstants.BDCQZH_LOWERCASE, StringUtils.deleteWhitespace(bdcqzh));
        }
        if (StringUtils.isNotBlank(zl)) {
            param.put(ParamsConstants.ZL_LOWERCASE, StringUtils.deleteWhitespace(zl));
        }
        // 根据fwbm定位bdcdyh
        if (StringUtils.isNotBlank(fwbm)) {
            List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHsByFwbm(fwbm);
            if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                List<String> bdcdyhList = new ArrayList();
                for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                    if (djsjFwHs != null && StringUtils.isNotBlank(djsjFwHs.getBdcdyh())) {
                        bdcdyhList.add(djsjFwHs.getBdcdyh());
                    }
                }
                param.put(ParamsConstants.BDCDYHS_LOWERCASE, bdcdyhList);
            }
        }
        param.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        String jsonStr = ReadJsonFileUtil.readJsonFile("conf/server/postBdcXmToArchive.json");
        if(StringUtils.isNotBlank(jsonStr)){
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if (jsonObject.containsKey("mulToOneSqlx")&&StringUtils.isNotBlank((CharSequence) jsonObject.get("mulToOneSqlx"))) {
                param.put("sqlxdm",StringUtils.split(String.valueOf(jsonObject.get("mulToOneSqlx")),","));
            }
        }
        return repository.selectPaging("getSdxxByPage", param, pageable);
    }


    @ResponseBody
    @RequestMapping("/getDataByproid")
    public HashMap<String, Object> getDataByproid(String proid) {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(proid)) {
            String bdcQlr = bdcQlrService.getQlrmcByProid(proid, Constants.QLRLX_QLR);
            resultMap.put("qlr", bdcQlr);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/sdByProid")
    public HashMap<String, Object> sdByProid(String proid) {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String msg = bdcXmService.changeSfsdByProid(proid);
        resultMap.put("msg", msg);
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/checkGdxxByProid")
    public HashMap<String, Object> checkGdxxByProid(String proids) {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String msg = bdcGdxxService.checkGdxxByProid(proids);
        resultMap.put("msg", msg);
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/gdByProid")
    public HashMap<String, Object> gdByProid(String proids,String cxgd) {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String msg=archivePostService.postBdcXmInfoByProid(proids,cxgd);
        resultMap.put("msg", msg);
        return resultMap;
    }
}
