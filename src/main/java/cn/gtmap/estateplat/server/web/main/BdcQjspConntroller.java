package cn.gtmap.estateplat.server.web.main;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: juyulin
 * Date: 16-5-7
 * Time: 下午2:04
 * dos:不动产权籍审批
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/bdcQjsp")
public class BdcQjspConntroller extends BaseController {
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private EntityMapper entityMapper;

    /**
     * 权籍审批导入不动产单元信息
     *
     * @param proid 项目id
     * @param id    不动产单元id
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "impBdcdy", method = RequestMethod.POST)
    public String impBdcdy(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "id", required = false) String id) {
        String msg = "导入失败";
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(id)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null) {
                if (StringUtils.isNotBlank(id)) {
                    HashMap map = new HashMap();
                    map.put("id", id);
                    List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyList)) {
                        if (bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null)
                            bdcSpxx.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                        if (bdcdyList.get(0).containsKey(ParamsConstants.TDZL_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.TDZL_CAPITAL) != null)
                            bdcSpxx.setZl(bdcdyList.get(0).get(ParamsConstants.TDZL_CAPITAL).toString());
                        entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
                        msg = "导入成功";
                    }
                }
            } else {
                bdcSpxx = new BdcSpxx();
                bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                bdcSpxx.setProid(proid);
                if (StringUtils.isNotBlank(id)) {
                    HashMap map = new HashMap();
                    map.put("id", id);
                    List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyList)) {
                        if (bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null)
                            bdcSpxx.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                        if (bdcdyList.get(0).containsKey(ParamsConstants.TDZL_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.TDZL_CAPITAL) != null)
                            bdcSpxx.setZl(bdcdyList.get(0).get(ParamsConstants.TDZL_CAPITAL).toString());
                        entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
                        msg = "导入成功";
                    }
                }
            }
        }
        return msg;
    }
}
