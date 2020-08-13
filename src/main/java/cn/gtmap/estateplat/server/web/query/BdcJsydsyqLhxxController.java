package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcJsydsyqLhxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/5/25
 * @description 建设用地使用权量化信息查询
 */
@Controller
@RequestMapping(value = "jsydLhxx")
public class BdcJsydsyqLhxxController extends BaseController {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcJsydsyqLhxxService bdcJsydsyqLhxxService;


    @ResponseBody
    @RequestMapping(value = "initJsydZrzxx")
    public String initJsydZrzxx(String proid, String[] ids, String djh) {
        String msg = "";
        if (StringUtils.isNotBlank(djh) && ids != null) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            HashMap tdMap = new HashMap();
            tdMap.put("djh", djh);
            List<DjsjZdJsydsyb> djsjZdJsydsybList = bdcJsydsyqLhxxService.getZdJsydsybList(tdMap);

            HashMap fwMap = new HashMap();
            fwMap.put("ids", ids);
            List<DjsjFwJsydzrzxx> djsjFwJsydzrzxx = bdcJsydsyqLhxxService.getFwJsydzrzxxList(fwMap);

            List<BdcJsydsyqLhxx> bdcJsydsyqLhxx = bdcJsydsyqLhxxService.getBdcJsydsyqLhxxFromDjsj(bdcXm, djsjZdJsydsybList, djsjFwJsydzrzxx);
            if (CollectionUtils.isNotEmpty(bdcJsydsyqLhxx)) {
                entityMapper.batchSaveSelective(bdcJsydsyqLhxx);
                msg = "成功";
            }
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "updateJsydsyqLhxx")
    public void updateJsydsyqLhxx(String proid) {
        bdcJsydsyqLhxxService.updateDjsjFwLhxx(proid);
    }

}
