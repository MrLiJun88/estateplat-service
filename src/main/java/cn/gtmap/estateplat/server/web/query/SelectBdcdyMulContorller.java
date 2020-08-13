package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcSqlxQllxRel;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSqlxQllxRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 15-12-9
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/selectBdcdyMul")
public class SelectBdcdyMulContorller extends BaseController {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlMapper bdcZdGlMapper;
    @Autowired
    private BdcXmService bdcXmService;

    @Autowired
    private BdcSqlxQllxRelService bdcSqlxQllxRelService;

    /*不动产单元列表选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        BdcXm xmxx = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        model.addAttribute("proid", proid);
        List<Map> bdclxList = bdcZdGlMapper.getZdBdclx();
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String yqllxdm = "";
        String bdclxdm = "";
        String qlxzdm = "";
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        String workFlowName = PlatformUtil.getWorkFlowNameByProid(proid);
        String qllx = "";
        String dyfs = "";
        String ysqlxdm = "";
        if (StringUtils.isNotBlank(workFlowName)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(workFlowName);
            if (CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                if (map.get("QLLXDM") != null)
                    qllx = CommonUtil.formatEmptyValue(map.get("QLLXDM"));
            }
        }

        if (xmxx != null && StringUtils.isNotBlank(xmxx.getSqlx())) {

            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sqlxdm", xmxx.getSqlx());
            if (StringUtils.isNotBlank(qllx))
                queryMap.put("qllxdm", qllx);
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }

        if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYqllxdm()))
                yqllxdm = bdcSqlxQllxRel.getYqllxdm();
            if (bdcSqlxQllxRel.getBdcdyly() != null)
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm()))
                zdtzm = bdcSqlxQllxRel.getZdtzm();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getDyfs()))
                dyfs = bdcSqlxQllxRel.getDyfs();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getBdclx()))
                bdclxdm = bdcSqlxQllxRel.getBdclx();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getQlxzdm()))
                qlxzdm = bdcSqlxQllxRel.getQlxzdm();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYsqlxdm()))
                ysqlxdm = bdcSqlxQllxRel.getYsqlxdm();
        }
        model.addAttribute("dyfs", dyfs);
        model.addAttribute("yqllxdm", yqllxdm);
        model.addAttribute("bdclxdm", bdclxdm);
        model.addAttribute("bdcdyly", bdcdyly);
        model.addAttribute("zdtzm", zdtzm);
        model.addAttribute("qlxzdm", qlxzdm);
        model.addAttribute("bdclxList", bdclxList);
        model.addAttribute("ysqlxdm", ysqlxdm);
        return "query/djsjBdcdyMulList";
    }
}
