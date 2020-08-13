package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXymxService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version V1.0, 2016-12-29
 * @description 不动产信用管理
 */
@Controller
@RequestMapping("/bdcXygl")
public class BdcXyglController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXymxService bdcXymxService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        String page = "query/bdcXygl";
        if(StringUtils.equals(AppConfig.getProperty("dwdm"), Constants.DWDM_SZ) || StringUtils.equals(AppConfig.getProperty("dwdm"), "320900")) {
            page = "query/320500/bdcXygl";
        }
        return page;

    }

    @RequestMapping(value = "viewXymx")
    public String viewXymx(Model model, String xymxid, String type) {
        List<BdcZdZjlx> bdcZdZjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("qlrzjhList", bdcZdZjlxList);
        model.addAttribute("type", type);
        if(StringUtils.isNotBlank(xymxid)) {
            BdcXymx bdcXymx = entityMapper.selectByPrimaryKey(BdcXymx.class, xymxid);
            if(bdcXymx != null && StringUtils.isNotBlank(bdcXymx.getXyglid())) {
                if(!StringUtils.equals(type, "add")) {
                    model.addAttribute("bdcXymx", bdcXymx);
                }
                BdcXygl bdcXygl = entityMapper.selectByPrimaryKey(BdcXygl.class, bdcXymx.getXyglid());
                if(bdcXygl != null) {
                    model.addAttribute("bdcXygl", bdcXygl);
                }
            }
        }
        return "query/bdcXymx";
    }

    @ResponseBody
    @RequestMapping("/getBdcXyglListJsonByPage")
    public Object getBdcXyglListJson(int page, int rows, String dcxc, String qlr, String zjh, String txdz, String zt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
        }
        if(StringUtils.isNotBlank(qlr)) {
            map.put("qlr", StringUtils.deleteWhitespace(qlr.trim()));
        }
        if(StringUtils.isNotBlank(zjh)) {
            map.put("zjh", StringUtils.deleteWhitespace(zjh.trim()));
        }
        if(StringUtils.isNotBlank(txdz)) {
            map.put("txdz", StringUtils.deleteWhitespace(txdz.trim()));
        }
        if(StringUtils.isNotBlank(zt)) {
            map.put("zt", zt);
        }
        return repository.selectPaging("getBdcXyglListJsonByPage", map, page - 1, rows);
    }

    @ResponseBody
    @RequestMapping("/getSzBdcXyglListJsonByPage")
    public Object getSzBdcXyglListJsonByPage(int page, int rows, String dcxc, String qlr, String zjh, String txdz, String zt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
        }
        if(StringUtils.isNotBlank(qlr)) {
            map.put("qlr", StringUtils.deleteWhitespace(qlr.trim()));
        }
        if(StringUtils.isNotBlank(zjh)) {
            map.put("zjh", StringUtils.deleteWhitespace(zjh.trim()));
        }
        if(StringUtils.isNotBlank(txdz)) {
            map.put("txdz", StringUtils.deleteWhitespace(txdz.trim()));
        }
        if(StringUtils.isNotBlank(zt)) {
            map.put("zt", zt);
        }
        return repository.selectPaging("getSzBdcXyglListJsonByPage", map, page - 1, rows);
    }


    @ResponseBody
    @RequestMapping(value = "changeSfzt")
    public String changeSfzt(String proid) {
        Example example = new Example(BdcSfxx.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proid", proid);
        List<BdcSfxx> bdcSfxxList = entityMapper.selectByExample(BdcSfxx.class, example);
        if(CollectionUtils.isNotEmpty(bdcSfxxList)) {
            for(BdcSfxx bdcSfxx : bdcSfxxList) {
                bdcSfxx.setSfzt("1");
                bdcSfxx.setSjrq(new Date());
                entityMapper.saveOrUpdate(bdcSfxx, bdcSfxx.getSfxxid());
            }
        }
        return null;
    }

    /**
     * 获取权利人
     *
     * @param model
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQlrByProid")
    public HashMap<String, Object> getQlrByProid(Model model, String proid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        Example example = new Example(BdcQlr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proid", proid).andEqualTo("qlrlx", "qlr");
        List<BdcQlr> bdcQlrList = entityMapper.selectByExample(BdcQlr.class, example);
        StringBuilder qlrs = new StringBuilder();
        if(CollectionUtils.isNotEmpty(bdcQlrList)) {
            for(int i = 0; i < bdcQlrList.size(); i++) {
                if(i == 0) {
                    qlrs.append(bdcQlrList.get(i).getQlrmc());
                } else {
                    qlrs.append(",").append(bdcQlrList.get(i).getQlrmc());
                }
            }
        }
        resultMap.put("qlr", qlrs);
        return resultMap;
    }

    /**
     * 获取不动产权证号
     *
     * @param model
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcqzhByProid")
    public HashMap<String, Object> getBdcqzhByProid(Model model, String proid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        BdcXm bdcXm = null;
        List<BdcZs> bdcZsList = null;
        if(StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcZsList = bdcZsService.getPlZsByWiid(bdcXm.getWiid());
        }
        StringBuilder bdcqzhs = new StringBuilder();
        //不动产证书list里面可能会存在重复的
        if(CollectionUtils.isNotEmpty(bdcZsList)) {
            for(int i = 0; i < bdcZsList.size(); i++) {
                if(i == 0) {
                    bdcqzhs.append(bdcZsList.get(i).getBdcqzh());
                } else {
                    if(bdcqzhs.indexOf(bdcZsList.get(i).getBdcqzh()) == -1) {
                        bdcqzhs.append(",").append(bdcZsList.get(i).getBdcqzh());
                    }
                }
            }
        }
        String[] bdcqzh = bdcqzhs.toString().split(",");
        if(bdcqzh.length > 1) {
            resultMap.put("bdcqzh", bdcqzh[0] + "等");
            return resultMap;
        }
        resultMap.put("bdcqzh", bdcqzhs);
        return resultMap;
    }

    /**
     * 获取坐落
     *
     * @param model
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getZlByProid")
    public HashMap<String, Object> getZlByProid(Model model, String proid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        BdcXm bdcXm = null;
        List<BdcSpxx> bdcSpxxList = null;
        if(StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcSpxxList = bdcSpxxService.getBdcSpxxByWiid(bdcXm.getWiid());
        }
        String zls = "";
        if(CollectionUtils.isNotEmpty(bdcSpxxList)) {
            if(bdcSpxxList.size() > 1) {
                zls = bdcSpxxList.get(0).getZl() + "等";
            } else {
                zls = bdcSpxxList.get(0).getZl();
            }
        }
        resultMap.put("zl", zls);
        return resultMap;
    }

    /**
     * 获取人员信息
     *
     * @param pageable
     * @param dcxc
     * @param rymc
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcRyxxPagesJson")
    public Object getBdcRyxxPages(Pageable pageable, String dcxc, String rymc) {
        Page<HashMap> dataPaging = null;
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            if(StringUtils.isNotBlank(rymc)) {
                map.put("rymc", StringUtils.deleteWhitespace(rymc));
            }
            dataPaging = repository.selectPaging("getBdcRyxxJsonByPage", map, pageable);
        } catch (Exception e) {
            logger.error("BdcRyxx/getBdcRyxxPagesJson", e);
        }
        return dataPaging;
    }

    /**
     * 添加信用管理
     *
     * @param model
     * @param bdcXygl
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBdcXygl", method = RequestMethod.POST)
    public Object addBdcXygl(Model model, BdcXygl bdcXygl) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> parameter = new HashMap<String, Object>();
        try {
            if(StringUtils.isNotBlank(bdcXygl.getQlrzjh())) {
                parameter.put("qlrzjh", bdcXygl.getQlrzjh());
            }
            if(StringUtils.isNotBlank(bdcXygl.getQlrmc())) {
                parameter.put("qlrmc", bdcXygl.getQlrmc());
            }
            BdcXygl xygl = getBdcXygl(parameter);
            if(xygl == null) {
                bdcXygl.setXyglid(UUIDGenerator.generate18());
                entityMapper.saveOrUpdate(bdcXygl, bdcXygl.getXyglid());
            } else {
                map.put("message", "该权利人已存在");
                return map;
            }
            map.put("message", "添加成功");
        } catch (Exception e) {
            logger.error("bdcxygl/addBdcXygl", e);
        }
        return map;
    }

    /**
     * @param parameter
     * @return
     */
    public BdcXygl getBdcXygl(Map<String, Object> parameter) {
        if(parameter.isEmpty()) {
            return null;
        }
        BdcXygl bdcXygl = null;
        try {
            Example example = new Example(BdcXygl.class);
            Example.Criteria criteria = example.createCriteria();
            for(Map.Entry column : parameter.entrySet()) {
                if(StringUtils.isNotBlank(column.getValue().toString())) {
                    criteria.andEqualTo(column.getKey().toString(), column.getValue().toString());
                }
            }
            List<BdcXygl> bdcXyglList = entityMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(bdcXyglList)) {
                bdcXygl = bdcXyglList.get(0);
            }
        } catch (Exception e) {
            logger.error("getBdcXygl", e);
        }
        return bdcXygl;

    }


    /**
     * 删除信用管理信息
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delXygl")
    public HashMap delXygl(Model model, String ids) {
        HashMap map = new HashMap();
        String result = "删除成功！";
        try {
            if(!StringUtils.isBlank(ids)) {
                /**
                 * 根据主键循环删除数据库记录
                 */
                String[] id = ids.split(",");
                for(String xymxid : id) {
                    //判断还有相关数据，没的话把管理也删除
                    BdcXymx bdcXymx = entityMapper.selectByPrimaryKey(BdcXymx.class, xymxid);
                    if(bdcXymx != null) {
                        List<BdcXymx> bdcXymxList = bdcXymxService.getBdcXymxByXyglid(bdcXymx.getXyglid());
                        if(CollectionUtils.isNotEmpty(bdcXymxList) && bdcXymxList.size() == 1) {
                            entityMapper.deleteByPrimaryKey(BdcXygl.class, bdcXymx.getXyglid());
                        }
                        entityMapper.deleteByPrimaryKey(BdcXymx.class, xymxid);
                    }
                }
            }
        } catch (Exception e) {
            result = "删除失败！";
            logger.error("BdcXyglController.delXygl",e);
        } finally {
            map.put("result", result);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "changeZtToXs")
    public String changeZtToXs(String xymxid) {
        String result = "";
        if(StringUtils.isNotBlank(xymxid)) {
            String[] ids = xymxid.split(",");
            for(String id : ids) {
                BdcXymx bdcXymx = entityMapper.selectByPrimaryKey(BdcXymx.class, id);
                if(bdcXymx != null) {
                    bdcXymx.setSfsx("1");
                    bdcXymx.setShr(getUserName());
                    bdcXymx.setShsj(new Date());
                    entityMapper.updateByPrimaryKeySelective(bdcXymx);
                    result = "修改成功";
                }
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "changeZtToHs")
    public String changeZtToHs(String xymxid, String reason) {
        String result = "";
        if(StringUtils.isNotBlank(xymxid)) {
            String[] ids = xymxid.split(",");
            for(String id : ids) {
                BdcXymx bdcXymx = entityMapper.selectByPrimaryKey(BdcXymx.class, id);
                if(bdcXymx != null) {
                    bdcXymx.setSfsx("2");
                    bdcXymx.setJcsxr(getUserName());
                    bdcXymx.setJcsxsj(new Date());
                    bdcXymx.setJcsxyy(reason);
                    entityMapper.updateByPrimaryKeySelective(bdcXymx);
                    result = "修改成功";
                }
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "saveData")
    public String saveData(BdcXygl bdcXygl, BdcXymx bdcXymx) {
        String result = "";
        if(bdcXygl != null && StringUtils.isNotBlank(bdcXygl.getQlrmc())) {
            String xyglid = bdcXygl.getXyglid();
            if(StringUtils.isBlank(xyglid)) {
                xyglid = UUIDGenerator.generate();
                bdcXygl.setXyglid(xyglid);
            }
            entityMapper.saveOrUpdate(bdcXygl, bdcXygl.getXyglid());
            if(bdcXymx != null && StringUtils.isNotBlank(bdcXymx.getNr())) {
                if(StringUtils.isBlank(bdcXymx.getXymxid())) {
                    bdcXymx.setXymxid(UUIDGenerator.generate());
                }
                bdcXymx.setXyglid(xyglid);
                bdcXymx.setBz("");
                entityMapper.saveOrUpdate(bdcXymx, bdcXymx.getXymxid());
            }
            result = "success";
        }
        return result;
    }

}
