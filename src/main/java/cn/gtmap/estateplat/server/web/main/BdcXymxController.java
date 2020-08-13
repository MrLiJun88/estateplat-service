package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcXymx;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcXymx")
public class BdcXymxController extends BaseController {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;

    @RequestMapping(value = "")
    public String index(Model model, String xyglid) {
        model.addAttribute("xyglid", xyglid);
        return "sjgl/bdcXymx";
    }

    @ResponseBody
    @RequestMapping(value = "/getBdcXymxPagesJson")
    public Object getBdcXymxPages(Pageable pageable, String dcxc, BdcXymx bdcXymx, String shsjStart,
                                  String shsjEnd, String xyglid) {
        Page<HashMap> dataPaging = null;
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            if (bdcXymx != null) {
                map.put("shrName", bdcXymx.getShr());
                map.put("sfsx", bdcXymx.getSfsx());
            }
            if (StringUtils.isNotBlank(shsjStart)) {
                map.put("shsjStart", CalendarUtil.formatDate(shsjStart));
            }
            if (StringUtils.isNotBlank(shsjEnd)) {
                map.put("shsjEnd", CalendarUtil.formatDate(shsjEnd));
            }
            if (StringUtils.isNotBlank(xyglid)) {
                map.put("xyglid", xyglid);
            }
            dataPaging = repository.selectPaging("getBdcXymxListByPage", map, pageable);
        } catch (Exception e) {
            logger.error("BdcXymx/getBdcXymxListByPage", e);
        }
        return dataPaging;
    }

    /**
     * 更新生效状态
     *
     * @param xymxid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateBdcXymx")
    public Object updateBdcXymx(String xymxid) {
        Map<String, String> returnMap = new HashMap<String, String>();
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("xymxid", xymxid);
        try {
            BdcXymx bdcXymx = getBdcXymx(parameter);
            if (bdcXymx != null && !StringUtils.equals(bdcXymx.getSfsx(), "1")) {
                bdcXymx.setSfsx("1");
                bdcXymx.setShr(super.getUserName());
                bdcXymx.setShsj(new Date());
                entityMapper.saveOrUpdate(bdcXymx, bdcXymx.getXymxid());
                returnMap.put(ParamsConstants.MESSAGE_LOWERCASE, "成功生效");
            } else {
                returnMap.put(ParamsConstants.MESSAGE_LOWERCASE, "生效失败");
                return returnMap;
            }
        } catch (Exception e) {
            logger.info("/updateBdcXymx", e);
        }

        return returnMap;
    }

    /**
     * 新增信用明细
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBdcXymx")
    public Object addBdcXymx(String xyglid, String nr) {
        Map<String, String> returnMap = new HashMap<String, String>();
        try {
            if (StringUtils.isNotBlank(xyglid)) {
                BdcXymx bdcXymx = new BdcXymx();
                bdcXymx.setXymxid(UUIDGenerator.generate18());
                bdcXymx.setNr(nr);
                bdcXymx.setXyglid(xyglid);
                bdcXymx.setCjsj(new Date());
                entityMapper.saveOrUpdate(bdcXymx, bdcXymx.getXymxid());
                returnMap.put(ParamsConstants.MESSAGE_LOWERCASE, "添加成功");
            } else {
                returnMap.put(ParamsConstants.MESSAGE_LOWERCASE, "添加失败");
                return returnMap;
            }
        } catch (Exception e) {
            logger.info("/addBdcXymx", e);
        }
        return returnMap;
    }

    /**
     * @param parameter
     * @return
     */
    public BdcXymx getBdcXymx(Map<String, Object> parameter) {
        if (parameter.isEmpty()) {
            return null;
        }
        BdcXymx bdcXymx = null;
        try {
            Example example = new Example(BdcXymx.class);
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry column : parameter.entrySet()) {
                if (StringUtils.isNotBlank(column.getValue().toString())) {
                    criteria.andEqualTo(column.getKey().toString(), column.getValue().toString());
                }
            }
            List<BdcXymx> bdcXymxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcXymxList)) {
                bdcXymx = bdcXymxList.get(0);
            }
        } catch (Exception e) {
            logger.error("getBdcXymx", e);
        }
        return bdcXymx;

    }


}
