package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.GdFw;
import cn.gtmap.estateplat.model.server.core.GdTd;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.GdTdService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: zuozhengwei
 * Date: 15-4-10
 * Time: 下午6:12
 * To change this template use File | Settings | File Templates.
 * doc:过度房屋土地的字段继承关系
 */
@Controller
@RequestMapping("/gdFwTdToQlxx")
public class GdFwTdToQLXXController extends BaseController {
    @Autowired
    GdTdService gdTdService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    EntityMapper entityMapper;

    @ResponseBody
    @RequestMapping(value = "/gdFwTdToFdcq", method = RequestMethod.GET)
    public String createWfProject(@RequestParam(value = "tdId", required = false) String tdId, @RequestParam(value = "fwId", required = false) String fwId) {
        String msg = "失败";
        GdTd gdTd = null;
        GdFw gdFw = null;
        if (StringUtils.isNotBlank(tdId)) {
            gdTd = gdTdService.queryGdTd(tdId);
        }
        if (StringUtils.isNotBlank(tdId)) {
            gdFw = gdFwService.queryGdFw(fwId);
        }
        if (gdFw != null && gdTd != null) {
            //由于只存一个fdcq信息没有使用insertVoList来插入多个信息，要使用这个方法必须要确定登记类型，我不知道这个是什么类型，使用不了creatProjectService这个方法
            // BdcFdcq bdcFdcq = gdFwService.readTdFw(gdFw,gdTd);
            // if(bdcFdcq!=null){
            //所获得的权利信息，proid是自己生成的，包括业务号、权利id,(业务号应该是收件单编号，proid应该是项目id，这些不知道怎么传过来)，暂时就写成了单个的信息插入
            //entityMapper.insertSelective(bdcFdcq);
            // msg = "成功";
            // }
        }
        return msg;
    }
}
