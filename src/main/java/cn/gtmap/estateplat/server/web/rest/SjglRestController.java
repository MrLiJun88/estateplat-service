package cn.gtmap.estateplat.server.web.rest;

import cn.gtmap.estateplat.server.core.model.vo.SjglRequest;
import cn.gtmap.estateplat.server.core.model.vo.SjglResponse;
import cn.gtmap.estateplat.server.service.sjgl.SjglService;
import cn.gtmap.estateplat.service.config.QlztService;
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
 * @version 1.0, 2020-05-13
 * @description 数据管理服务
 */
@Controller
@RequestMapping(value = "/rest")
@Api(tags = "数据管理服务")
public class SjglRestController {
    @Autowired
    private SjglService sjglService;
    @Autowired
    QlztService qlztService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/v1.0/sjgl/dyh")
    @ApiOperation(value = "获取单元信息", httpMethod = "POST")
    public SjglResponse dyh(@RequestBody SjglRequest sjglRequest) {
        String msg = "";
        Object o = null;
        try {
            o = sjglService.getDyh(sjglRequest);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        SjglResponse sjglResponse = new SjglResponse(msg, o);
        return sjglResponse;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/v1.0/sjgl/ql")
    @ApiOperation(value = "获取权利信息", httpMethod = "POST")
    public SjglResponse ql(@RequestBody SjglRequest sjglRequest) {
        String msg = "";
        Object o = null;
        try {
            o = sjglService.getQl(sjglRequest);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        SjglResponse sjglResponse = new SjglResponse(msg, o);
        return sjglResponse;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/v1.0/dsrw/ceshinode/")
    @ApiOperation(value = "ceshinode", httpMethod = "POST")
    public void ceshinode() {
        qlztService.updateKsYsByBdcdyh("[]");
    }
}
