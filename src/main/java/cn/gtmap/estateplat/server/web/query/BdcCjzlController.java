package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcCjlb;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcCjlbService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lst 持件信息
 */
@Controller
@RequestMapping("/bdcSecZl")
public class BdcCjzlController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcCjlbService bdcCjlbService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private EntityMapper entityMapper;

    /** 持件信息查询 **/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        String displayControl = AppConfig.getProperty("displayControl");
        model.addAttribute("displayControl", displayControl);
        return "query/bdcCjlbList";
    }

    /**
     * 统计查询
     */
    @ResponseBody
    @RequestMapping("/queryBdcSelzlList")
    public Object queryBdcSelzlList(Pageable pageable, String bh, String zl, String sqr, String cyr, String pc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("bh", bh);
        map.put("zl", zl);
        map.put("sqr", sqr);
        map.put("cyr", cyr);
        map.put("pc", pc);
        return repository.selectPaging("queryBdcSelzlByPage", map, pageable);
    }

    /**
     * 持件信息新增信息
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveSelZl")
    public HashMap saveSelZl(Model model, BdcCjlb bdcCjlb, String cjid, String pc) {
        HashMap map = new HashMap();
        String result = "保存成功";
        try {
            Date dt = new Date();
            if (bdcCjlb.getCysj() == null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(dt);
                bdcCjlb.setCysj(formatter.parse(dateString));
            }
            if (StringUtils.isNotBlank(bdcCjlb.getBh())) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(bdcCjlb.getBh());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    bdcCjlb.setZl(bdcXmList.get(0).getZl());
                    bdcCjlb.setCyr(bdcXmList.get(0).getCjr());
                    if (StringUtils.isBlank(bdcCjlb.getSqr())) {
                        List<BdcQlr> bdcQlrList;
                        if (bdcXmList.get(0).getDjlx().equals(Constants.DJLX_DYDJ_DM)) {
                            bdcQlrList = bdcQlrService.queryBdcYwrByProid(bdcXmList.get(0).getProid());
                        } else {
                            bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmList.get(0).getProid());
                        }
                        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                            bdcCjlb.setSqr(bdcQlrList.get(0).getQlrmc());
                        }
                    }
                }
            }
            bdcCjlb.setCjid(cjid);
            bdcCjlb.setPc(pc);
            bdcCjlbService.insertCjlbOrUpdateByPrimaryKey(bdcCjlb);
        } catch (Exception e) {
            logger.error("BdcCjzlController.saveSelZl",e);
            result = "保存失败";
        }
        map.put("result", result);
        return map;
    }

    /**
     * 持件信息新增信息
     * @param bh,pc,bz
     */
    @ResponseBody
    @RequestMapping("/addSelZl")
    public HashMap addSelZl(String bz, String bh, String pc, boolean isSend) {
        HashMap map = new HashMap();
        String result = "保存成功";
        BdcCjlb bdcCjlb = new BdcCjlb();
        bdcCjlb.setCjid(UUIDGenerator.generate18());
        bdcCjlb.setCysj(new Date(System.currentTimeMillis()));
        bdcCjlb.setCyr(getUserName());
        bdcCjlb.setBh(bh);
        bdcCjlb.setPc(pc);
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(bh);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            List<BdcQlr> bdcQlrList;
            if (bdcXmList.get(0).getDjlx().equals(Constants.DJLX_DYDJ_DM)) {
                bdcQlrList = bdcQlrService.queryBdcYwrByProid(bdcXmList.get(0).getProid());
            } else {
                bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmList.get(0).getProid());
            }
            bdcCjlb.setZl(bdcXmList.get(0).getZl());
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                bdcCjlb.setSqr(bdcQlrList.get(0).getQlrmc());
            }
        } else {
            result = "该业务不存在";
        }
        //根据传入的批次和项目编号查询存在相同的及更新
        if (StringUtils.isNotBlank(bh) && StringUtils.isNotBlank(pc)) {
            Example example = new Example(BdcCjlb.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("bh", bh).andEqualTo("pc", pc);
            List<BdcCjlb> bdcCjlbList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcCjlbList)) {
                bdcCjlb.setCjid(bdcCjlbList.get(0).getCjid());
            }
        }
        
        entityMapper.saveOrUpdate(bdcCjlb, bdcCjlb.getCjid());
        map.put("result", result);
        return map;
    }

}
