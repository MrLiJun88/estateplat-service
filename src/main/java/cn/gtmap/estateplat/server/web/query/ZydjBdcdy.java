package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-3-29
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 * sc:转移登记选择不动产单元
 */
@Controller
@RequestMapping("/zydjBdcdy")
public class ZydjBdcdy extends BaseController {
    @Autowired
    @Resource(name = "creatProjectZydjService")
    CreatProjectService creatProjectService;

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;


    @ResponseBody
    @RequestMapping(value = "/getYxmid", method = RequestMethod.GET)
    public String getYxmid(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        String msg = "";
        BdcXm xmxx = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        if (xmxx != null && StringUtils.isNotBlank(xmxx.getDjlx())
                &&(StringUtils.equals(xmxx.getDjlx(), Constants.DJLX_ZYDJ_DM) || StringUtils.equals(xmxx.getDjlx(), Constants.DJLX_BGDJ_DM))
                &&StringUtils.isNotBlank(bdcdyh)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            List<Map> bdcXmList = bdcXmService.getBdcZsByPage(map);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                if (bdcXmList.get(0).get(ParamsConstants.PROID_CAPITAL) != null) {
                    msg = bdcXmList.get(0).get(ParamsConstants.PROID_CAPITAL).toString();
                }
            } else {
                msg = "error";
            }
        }
        return msg;
    }
}
