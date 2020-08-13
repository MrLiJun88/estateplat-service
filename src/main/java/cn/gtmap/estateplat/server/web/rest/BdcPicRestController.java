package cn.gtmap.estateplat.server.web.rest;

import cn.gtmap.estateplat.server.core.model.vo.BdcPicData;
import cn.gtmap.estateplat.server.core.service.BdcPicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-21
 * @description
 */
@Controller
@RequestMapping(value = "/rest")
@Api(tags = "虚拟单元号匹配服务")
public class BdcPicRestController {
    @Autowired
    private BdcPicService bdcPicService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/v1.0/bdc/pic")
    @ApiOperation(value = "虚拟单元号匹配服务", httpMethod = "POST")
    public Object bdcPic(@RequestBody BdcPicData bdcPicData) {
        return bdcPicService.bdcdyMatch(bdcPicData);
    }

}
