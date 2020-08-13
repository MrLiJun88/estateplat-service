package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.model.server.core.GdYy;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcYyIssx")
public class BdcYyIssxController extends BaseController {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;
    /**
     * 不动产异议失效
     *
     * @param
     */
    @ResponseBody
    @RequestMapping(value = "/setBdcYyIssx")
    public String setCfIssx(String qlid,HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        String result = "成功";
        /**
         * @author <a href="mailto:liangqing@gtmap.cn">liangqing</a>
         * @description ajax跨域请求，需要前台请求定义callback，后台组织成jsonp格式
         */
        String callback = request.getParameter("callback");
        try {
            if (StringUtils.isNotBlank(qlid)) {
                BdcYy bdcYy = getBdcYyByQlid(qlid);
                GdYy gdYy = getGdYyByQlid(qlid);
                if (bdcYy != null) {
                    bdcYy.setZxdbr(super.getUserName());
                    bdcYy.setZxsj(new Date());
                    bdcYy.setQszt(Constants.QLLX_QSZT_HR);
                    bdcYy.setIssx("1");
                    entityMapper.saveOrUpdate(bdcYy, bdcYy.getQlid());
                } else if (gdYy != null) {
                    gdYy.setIszx(1);
                    gdYy.setIssx("1");
                    entityMapper.saveOrUpdate(gdYy, gdYy.getYyid());
                    gdFwService.changeGdqlztByQlid(gdYy.getYyid(),Constants.QLLX_QSZT_XS.toString());
                }
            }
            callback = request.getParameter("callback");
            resultJson.put("result", result);
            result = callback + "(" + resultJson + ")";
            return result;
        } catch (Exception e) {
            logger.info("bdcYyIssx/setBdcYyIssx", e);
        }
        result = "失败";
        resultJson.put("result", result);
        result = callback + "(" + resultJson + ")";
        return result;
    }

    /**
     * 根据qlid获取bdc_yy
     *
     * @param qlid
     * @return
     */
    public BdcYy getBdcYyByQlid(String qlid) {
        BdcYy bdcYy = null;
        Example example = new Example(BdcYy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("qlid", qlid);
        List<BdcYy> bdcYyList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcYyList)) {
            bdcYy = bdcYyList.get(0);
        }
        return bdcYy;
    }

    /**
     * 根据qlid获取gd_yy
     *
     * @param yyid
     * @return
     */
    public GdYy getGdYyByQlid(String yyid) {
        GdYy gdYy = null;
        Example example = new Example(GdYy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("yyid", yyid);
        List<GdYy> gdYyList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(gdYyList)) {
            gdYy = gdYyList.get(0);
        }
        return gdYy;
    }
}
