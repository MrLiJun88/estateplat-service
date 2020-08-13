package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdysdMapper;
import cn.gtmap.estateplat.server.core.mapper.GdBdcSdMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcSjSd")
public class BdcSjSdController extends BaseController {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcBdcdysdMapper bdcBdcdysdMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdBdcSdMapper gdBdcSdMapper;
    @Autowired
    private BdcBdcdysdService bdcBdcdysdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private GdTdService gdTdService;

    @RequestMapping(value = "")
    public String index(Model model, String proid, String sdlx, String bdcqzh) {
        List<BdcZdSqlx> sqlxList = bdcZdGlService.getBdcSqlx();
        List<BdcZdQszt> qsztList = bdcZdGlService.getBdcZdQszt();
        model.addAttribute("sqlxListJson", JSONObject.toJSONString(sqlxList));
        model.addAttribute("qsztListJson", JSONObject.toJSONString(qsztList));
        model.addAttribute("sqlxList", sqlxList);
        model.addAttribute(ParamsConstants.BDCQZH_LOWERCASE, bdcqzh);
        String path = null;
        if ("bdcdy".equals(sdlx)) {
            path = "sjgl/bdcdySd";
        }
        if ("bdcqz".equals(sdlx)) {
            if(StringUtils.equals(AppConfig.getProperty("bdcqzSd.showOptimization"),ParamsConstants.TRUE_LOWERCASE)){
                path = "sjgl/320500/optimization/bdcqzSd";
            }else{
                path = "sjgl/bdcqzSd";
            }
        }
        return path;
    }

    @RequestMapping(value = "/bdcdySdList")
    public String bdcdysdList(Model model, String proid, String visible) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute("visible", visible);
        return "query/bdcdySdList";
    }

    @RequestMapping(value = "/bdcdySdxxList")
    public String bdcdysdxxList(Model model, String proid, String visible, String bh, String bdcdyh) {
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh())) {
                model.addAttribute("bh", bdcXm.getBh());
            }
        } else {
            if (StringUtils.isNotBlank(bdcdyh)) {
                model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            } else {
                if (StringUtils.isNotBlank(bh))
                    model.addAttribute("bh", bh);
            }
        }
        model.addAttribute("visible", visible);
        return "query/bdcdySdxxList";
    }

    /**
     * 添加不动产权证到不动产单元锁定
     *
     * @param model
     * @param bdcdyh
     * @param qlr
     * @param zl
     * @param bdclx
     * @param proid
     * @param wiid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBdcdySd", method = RequestMethod.POST)
    public Object addBdcdySd(Model model, String bdcdyh, String qlr, String zl,
                             String bdclx, String proid, String wiid, String xzyy) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            BdcXm xm = bdcXmService.getBdcXmByProid(proid);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
            BdcBdcdySd sd = new BdcBdcdySd();
            sd.setBdcdyid(bdcBdcdy != null ? bdcBdcdy.getBdcdyid() : UUIDGenerator.generate18());
            sd.setBdcdyh(StringUtils.deleteWhitespace(bdcdyh));
            sd.setBdclx(StringUtils.isNotBlank(bdclx) ? bdclx : bdcBdcdy != null ? bdcBdcdy.getBdclx() : null);
            sd.setSdr(super.getUserName());
            sd.setSdsj(new Date(System.currentTimeMillis()));
            sd.setZl(zl);
            sd.setQlr(qlr);
            sd.setXzzt(Constants.XZZT_SD);
            sd.setXzyy(xzyy);
            if (StringUtils.isNotBlank(proid)) {
                sd.setProid(proid);
            }
            if (StringUtils.isNotBlank(wiid)) {
                sd.setWiid(xm.getWiid());
            }
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);

            BdcBdcdySd bdcdysd = bdcBdcdysdMapper.findBdcBdcdySdByBdcdyh(StringUtils.deleteWhitespace(bdcdyh));
            if (bdcdysd != null) {
                if (!bdcdysd.getXzzt().equals(Constants.XZZT_SD)) {
                    sd.setXzzt(Constants.XZZT_SD);
                    sd.setSdr(super.getUserName());
                    sd.setSdsj(new Date(System.currentTimeMillis()));
                    sd.setJsr(null);
                    sd.setJssj(null);
                    sd.setXzyy(xzyy);
                    bdcBdcdysdMapper.updateBdcbdcdySd(sd);
                } else {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "该不动产单元已限制");
                    return map;
                }
            } else {
                bdcBdcdysdMapper.insertBdcbdcdySd(sd);
            }
            map.put(ParamsConstants.MESSAGE_LOWERCASE, "添加成功");
        } catch (Exception e) {
            logger.error("sjsd/addBdcqzToBdcdySd", e);
        }
        return map;
    }

    /**
     * 修改不动产权证到不动产单元锁定
     *
     * @param model
     * @param bdcdyh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateBdcdySd", method = RequestMethod.POST)
    public Object updateBdcdySd(Model model, String bdcdyh, String xzyy) {
        Map<String, String> map = new HashMap<String, String>();
        BdcBdcdySd bdcdysd = bdcBdcdysdMapper.findBdcBdcdySdByBdcdyh(StringUtils.deleteWhitespace(bdcdyh));
        if (bdcdysd != null) {
            if (bdcdysd.getXzzt().equals(Constants.XZZT_SD)) {
                bdcdysd.setXzyy(xzyy);
                bdcBdcdysdMapper.updateBdcbdcdySd(bdcdysd);
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "修改成功");
            } else {
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "该不动产单元未限制");
                return map;
            }
        } else {
            map.put(ParamsConstants.MESSAGE_LOWERCASE, "修改失败");
        }
        return map;
    }

    /**
     * 添加不动产权证到不动产单元锁定
     *
     * @param model
     * @param bdcdyh
     * @param bdcqzh
     * @param zl
     * @param bdclx
     * @param proid
     * @param wiid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBdcqzSd", method = RequestMethod.POST)
    public Object addBdcqzSd(Model model, String bdcdyh, String bdcqzh, String zl,
                             String bdclx, String proid, String wiid, String xzyy) {
        Map<String, String> map = new HashMap<String, String>();
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
        Example exampleZs = new Example(BdcZs.class);
        exampleZs.createCriteria().andEqualTo(ParamsConstants.BDCQZH_LOWERCASE, bdcqzh);
        List<BdcZs> bdcZsList = entityMapper.selectByExample(exampleZs);
        BdcZs bdcZs = null;
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            bdcZs = bdcZsList.get(0);
        }

        BdcBdcZsSd zssd = new BdcBdcZsSd();
        zssd.setSdid(UUIDGenerator.generate18());
        zssd.setBdclx(StringUtils.isNotBlank(bdclx) ? bdclx : bdcBdcdy != null ? bdcBdcdy.getBdclx() : null);
        zssd.setSdr(super.getUserName());
        zssd.setSdsj(new Date(System.currentTimeMillis()));
        zssd.setCqzh(bdcqzh);
        if (bdcZs != null && StringUtils.isNotBlank(bdcZs.getZsid())) {
            zssd.setZsid(bdcZs.getZsid());
        }
        zssd.setXzzt(Integer.valueOf(Constants.XZZT_SD));
        zssd.setXzyy(xzyy);
        if (StringUtils.isNotBlank(proid)) {
            zssd.setProid(proid);
        }

        Example example = new Example(BdcBdcZsSd.class);
        example.createCriteria().andEqualTo("cqzh", bdcqzh);
        List<BdcBdcZsSd> bdcBdcZsSdList = entityMapper.selectByExample(example);
        BdcBdcZsSd sd = null;
        if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
            sd = bdcBdcZsSdList.get(0);
        }
        if (sd == null) {
            entityMapper.saveOrUpdate(zssd, zssd.getSdid());
            map.put(ParamsConstants.MESSAGE_LOWERCASE, "添加成功");
        } else {
            if (sd.getXzzt() == 1) {
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "该产权证已存在于锁定表中，且已被锁定");
            } else if (sd.getXzzt() == 0) {
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "该产权证已存在于锁定表中，但未锁定");
            }
        }
        return map;
    }

    /**
     * 添加不动产权锁定
     *
     * @param model
     * @param cqzh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateBdcqzSd", method = RequestMethod.POST)
    public Object updateBdcqzSd(Model model, String cqzh,
                                String qlid) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Example example = new Example(BdcBdcZsSd.class);
            example.createCriteria().andEqualTo("cqzh", cqzh);
            List<BdcBdcZsSd> bdcBdcZsSdList = entityMapper.selectByExample(example);
            BdcBdcZsSd sd = null;
            if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                sd = bdcBdcZsSdList.get(0);
            }
            if (sd != null) {
                if (String.valueOf(sd.getXzzt()).equals(Constants.XZZT_ZC)) {
                    //修改锁定状态为锁定,清空解锁人、解锁时间和解锁原因
                    sd.setXzzt(1);
                    sd.setJsr("");
                    sd.setJssj(null);
                    sd.setJsyy("");
                    sd.setSdr(super.getUserName());
                    sd.setSdsj(new Date(System.currentTimeMillis()));
                    entityMapper.saveOrUpdate(sd, sd.getSdid());
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "修改成功");
                } else if (String.valueOf(sd.getXzzt()).equals(Constants.XZZT_SD)) {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "该产权证已为锁定状态");
                } else {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "数据错误");
                }
            }

        } catch (Exception e) {
            logger.error("sjsd/updateBdcqzSd", e);
        }
        return map;
    }

    /**
     * 更新过渡锁定
     *
     * @param model
     * @param cqzh
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/updateGdSd", method = RequestMethod.POST)
    public Object updateGdSd(Model model, String cqzh,
                             String qlid, String sdid) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Example example = new Example(GdBdcSd.class);
            if (StringUtils.isNotBlank(sdid)) {
                example.createCriteria().andEqualTo("sdid", sdid);
            } else {
                example.createCriteria().andEqualTo("cqzh", cqzh).andEqualTo("xzzt", "0");
            }
            List<GdBdcSd> gdBdcSdList = entityMapper.selectByExample(example);
            GdBdcSd gdbdcsd = null;
            if (CollectionUtils.isNotEmpty(gdBdcSdList)) {
                gdbdcsd = gdBdcSdList.get(0);
            }
            if (gdbdcsd != null) {
                if (StringUtils.equals(Constants.XZZT_ZC, gdbdcsd.getXzzt().toString())) {
                    //修改锁定状态为锁定,清空解锁人、解锁时间和解锁原因
                    gdbdcsd.setXzzt(1);
                    gdbdcsd.setSdr(super.getUserName());
                    gdbdcsd.setSdsj(new Date(System.currentTimeMillis()));
                    gdbdcsd.setJsyy("");
                    gdbdcsd.setJssj(null);
                    gdbdcsd.setJsr("");
                    entityMapper.saveOrUpdate(gdbdcsd, gdbdcsd.getSdid());
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "修改成功");
                } else if (StringUtils.equals(Constants.XZZT_SD, gdbdcsd.getXzzt().toString())) {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "该产权证已为锁定状态");
                } else {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "数据错误");
                }
            }

        } catch (Exception e) {
            logger.error("sjsd/updateGdSd", e);
        }
        return map;
    }

    /**
     * 获取不动产单元锁定
     *
     * @param pageable
     * @param proid
     * @param bdcdyid
     * @param bdcdyh
     * @param sdr
     * @param jsr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcdySdPagesJson")
    public Object getBdcdySdPages(Pageable pageable, String proid, String bdcdyid, String zl,
                                  String bdcdyh, String sdr, String jsr, String xzzt, String dcxc, String bh) {
        Page<HashMap> dataPaging = null;
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            if (StringUtils.isNotBlank(dcxc)) {
                map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            }
            if (StringUtils.isNotBlank(proid)) {
                map.put(ParamsConstants.PROID_LOWERCASE, proid);
            }
            if (StringUtils.isNotBlank(sdr)) {
                map.put("sdr", StringUtils.deleteWhitespace(sdr));
            }
            if (StringUtils.isNotBlank(jsr)) {
                map.put("jsr", StringUtils.deleteWhitespace(jsr));
            }
            if (StringUtils.isNotBlank(bdcdyid)) {
                map.put("bdcdyid", bdcdyid);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(xzzt)) {
                map.put("xzzt", xzzt);
            }
            if (StringUtils.isNotBlank(bh) && !StringUtils.equals(bh, "null")) {
                map.put("bh", bh);
            }
            dataPaging = repository.selectPaging("getBdcdySdListByPage", map, pageable);
        } catch (Exception e) {
            logger.error("bdcSjSd/getBdcdySdPagesJson", e);
        }
        return dataPaging;

    }

    /**
     * 获取不动产单元锁定
     *
     * @param pageable
     * @param proid
     * @param bdcdyh
     * @param sdr
     * @param jsr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcqzSdPagesJson")
    public Object getBdcqzSdByPages(Pageable pageable, String proid, String zl,
                                    String bdcdyh, String bdcqzh, String sdr, String jsr, String xzzt, String dcxc, String exactQuery) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        }
        if (StringUtils.isNotBlank(proid)) {
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
        }
        if (StringUtils.isNotBlank(sdr)) {
            map.put("sdr", StringUtils.deleteWhitespace(sdr));
        }
        if (StringUtils.isNotBlank(jsr)) {
            map.put("jsr", StringUtils.deleteWhitespace(jsr));
        }
        if (StringUtils.isNotBlank(bdcdyh)) {
            map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
        }
        if (StringUtils.isNotBlank(bdcqzh)) {
            map.put(ParamsConstants.BDCQZH_LOWERCASE, StringUtils.deleteWhitespace(bdcqzh));
        }
        if (StringUtils.isNotBlank(xzzt)) {
            map.put("xzzt", xzzt);
        }
        if(StringUtils.isNotBlank(zl)) {
            map.put("zl", zl);
        }
        if(StringUtils.isNotBlank(exactQuery)) {
            map.put("exactQuery", exactQuery);
        }
        if(StringUtils.equals(AppConfig.getProperty("bdcqzSd.showOptimization"),ParamsConstants.TRUE_LOWERCASE)) {
            return repository.selectPaging("getBdcqzSdOptimizeByPage", map, pageable);
        }else{
            return repository.selectPaging("getBdcqzSdByPage", map, pageable);
        }
    }

    /**
     * 解锁
     *
     * @param model
     * @param proid
     * @param bdcdyh
     * @param qlid
     * @param bdcdyid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unlockBdcdySd")
    public Map unlockBdcdy(Model model, String proid, String bdcdyh,
                           String qlid, String bdcdyid, String type) {
        Map map = new HashMap();
        try {
            BdcBdcdySd bdcdysd = null;
            if (StringUtils.isBlank(bdcdyh) && StringUtils.isNotBlank(bdcdyid))
                bdcdysd = bdcBdcdysdMapper.findBdcBdcdySd(bdcdyid);
            else
                bdcdysd = bdcBdcdysdMapper.findBdcBdcdySdByBdcdyh(bdcdyh);

            if (bdcdysd != null && StringUtils.equals(bdcdysd.getXzzt(), Constants.XZZT_SD)) {
                bdcdysd.setJsr(super.getUserName());
                bdcdysd.setJssj(new Date(System.currentTimeMillis()));
                bdcdysd.setXzzt(Constants.XZZT_ZC);
                bdcdysd.setXzyy(null);
                bdcBdcdysdMapper.updateBdcbdcdySd(bdcdysd);
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "解冻成功");
                return map;
            } else {
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "该不动产单元已解冻");
                return map;
            }
        } catch (Exception e) {
            logger.error("sjsd/unlockBdcdySd", e);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "getdateByQlidAndProid")
    public HashMap<String, Object> getdateByQlidAndProid(String proid, String qlid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        StringBuilder zl = new StringBuilder();
        List<String> zlList = new ArrayList<String>();
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getZl())) {
                zl.append(bdcSpxx.getZl());
            }
        }
        if (StringUtils.isBlank(zl) && StringUtils.isNotBlank(qlid)) {
            List<GdFw> gdFwList = gdFwService.getGdFwByQlid(qlid);
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                for (GdFw gdFw : gdFwList) {
                    if (StringUtils.isNotBlank(zl) && !zlList.contains(gdFw.getFwzl())) {
                        zl.append("/").append(gdFw.getFwzl());
                        zlList.add(gdFw.getFwzl());
                    } else if (StringUtils.isBlank(zl)) {
                        zl.append(gdFw.getFwzl());
                        zlList.add(gdFw.getFwzl());
                    }
                }
            }
            List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(qlid);
            if (CollectionUtils.isNotEmpty(gdTdList)) {
                for (GdTd gdTd : gdTdList) {
                    if (StringUtils.isNotBlank(zl) && !zlList.contains(gdTd.getZl())) {
                        zl.append("/").append(gdTd.getZl());
                        zlList.add(gdTd.getZl());
                    } else if (StringUtils.isBlank(zl)) {
                        zl.append(gdTd.getZl());
                        zlList.add(gdTd.getZl());
                    }
                }
            }
        }
        resultMap.put("zl", zl);
        return resultMap;
    }

    /**
     * 锁定
     *
     * @param model
     * @param proid
     * @param bdcdyh
     * @param qlid
     * @param bdcdyid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "lockBdcdySd")
    public Map lockBdcdy(Model model, String proid, String bdcdyh,
                         String qlid, String bdcdyid, String type) {
        Map map = new HashMap();
        try {
            BdcBdcdySd bdcdysd = null;
            if (StringUtils.isBlank(bdcdyh) && StringUtils.isNotBlank(bdcdyid))
                bdcdysd = bdcBdcdysdMapper.findBdcBdcdySd(bdcdyid);
            else
                bdcdysd = bdcBdcdysdMapper.findBdcBdcdySdByBdcdyh(bdcdyh);

            if (bdcdysd != null && !StringUtils.equals(bdcdysd.getXzzt(), Constants.XZZT_SD)) {
                bdcdysd.setJsr("");
                bdcdysd.setJssj(null);
                bdcdysd.setXzzt(Constants.XZZT_SD);
                bdcBdcdysdMapper.updateBdcbdcdySd(bdcdysd);
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "冻结成功");
                return map;
            } else {
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "该不动产单元已冻结");
                return map;
            }
        } catch (Exception e) {
            logger.error("sjsd/lockBdcdySd", e);
        }
        return map;
    }

    /**
     * 解锁
     *
     * @param model
     * @param bdcqzh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unlockBdcqzSd")
    public Map unlockBdcqz(Model model, String bdcqzh, String jsyy,String sdid) {
        Map map = new HashMap();
        try {
            List<BdcBdcZsSd> bdcBdcZsSdList = null;
            if(StringUtils.isNotBlank(bdcqzh) && StringUtils.isNotBlank(sdid)){
                Example example = new Example(BdcBdcZsSd.class);
                example.createCriteria().andEqualTo("cqzh", bdcqzh).andEqualTo("sdid", sdid);
                bdcBdcZsSdList = entityMapper.selectByExample(example);
            }
            BdcBdcZsSd zssd = null;
            if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                zssd = bdcBdcZsSdList.get(0);
            }
            if (zssd != null && Constants.XZZT_SD.equals(String.valueOf(zssd.getXzzt())) && jsyy != null) {
                zssd.setJsr(super.getUserName());
                zssd.setJssj(new Date(System.currentTimeMillis()));
                zssd.setXzzt(Integer.valueOf(Constants.XZZT_ZC));
                zssd.setJsyy(jsyy);
                entityMapper.saveOrUpdate(zssd, zssd.getSdid());
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "解锁成功");
                return map;
            } else {
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "该不动产单元已解除限制");
                return map;
            }
        } catch (Exception e) {
            logger.error("sjsd/unlockBdcqzSd", e);
        }
        return map;
    }

    /**
     * 高級查詢不动产单元锁定
     *
     * @param pageable
     * @param sdid
     * @param proid
     * @param qlid
     * @param sdr
     * @param jsr
     * @param xzzt
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/highLevelSearchBdcdySd")
    public Object highLevelSearchBdcdySd(Pageable pageable, String sdid, String proid, String qlid,
                                         String sdr, String jsr, String xzzt) {
        Page<HashMap> dataPaging = null;
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            map.put("sdr", sdr);
            map.put("jsr", jsr);
            map.put("sdid", sdid);
            map.put("xzzt", xzzt);
            map.put("qlid", qlid);

            dataPaging = null;
        } catch (Exception e) {
            logger.error("bdcSjSd/highLevelSearchBdcdySd", e);
        }
        return dataPaging;
    }

    /**
     * @return
     */
    @RequestMapping(value = "selectBdcdy", method = RequestMethod.GET)
    public String turnToBdcdyPage(Model model) {
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        model.addAttribute("bdcdyly", bdcdyly);
        return "sjgl/bdcdyListForSd";
    }

    /**
     * 获取过度土地
     *
     * @param page
     * @param rows
     * @param sidx
     * @param sord
     * @param iszx
     * @param hhSearch
     * @param rf1Dwmc
     * @param rf1zjh
     * @param tdzh
     * @param gdTd
     * @param tdid
     * @param bdcdyh
     * @param request
     * @param filterFwPpzt
     * @param tdids
     * @param fwtdz
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdTdJson")
    public Object getGdTdJson(int page, int rows, @RequestParam(value = "sidx", required = false) String sidx, @RequestParam(value = "sord", required = false) String sord,
                              @RequestParam(value = "iszx", required = false) Integer iszx, @RequestParam(value = "hhSearch", required = false) String hhSearch,
                              @RequestParam(value = "rf1Dwmc", required = false) String rf1Dwmc, @RequestParam(value = "rf1zjh", required = false) String rf1zjh, @RequestParam(value = "tdzh", required = false) String tdzh,
                              @RequestParam(value = "gdTd", required = false) GdTd gdTd, @RequestParam(value = "tdid", required = false) String tdid,
                              @RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletRequest request, @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt,
                              @RequestParam(value = "tdids", required = false) String tdids, @RequestParam(value = "fwtdz", required = false) String fwtdz,
                              @RequestParam(value = "qlid", required = false) String qlid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("hhSearch", StringUtils.deleteWhitespace(hhSearch));
        String sysVersion = AppConfig.getProperty("sys.version");
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if (StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        map.put("iszx", Constants.GDQL_ISZX_YZX);
        map.put("tdid", tdid);
        map.put("bdclx", Constants.BDCLX_TD);
        map.put("qlid", qlid);
        return repository.selectPaging("getGdTdByPage", map, page - 1, rows);
    }


    /**
     * 获取房屋信息分页数据
     *
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdFwJson")
    public Object getGdFwJson(int page, int rows, String dah, GdFw gdFw, String hhSearch, String fczh, HttpServletRequest request, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdQlids", required = false) String gdQlids, @RequestParam(value = "checkMulGdFw", required = false) String checkMulGdFw) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("hhSearch", StringUtils.deleteWhitespace(hhSearch));
        if (StringUtils.isNoneBlank(gdproid)) {
            String qlids = gdFwService.getGdFwQlidsByProid(gdproid);
            if (StringUtils.isNoneBlank(qlids) && qlids.split(",").length > 0) {
                map.put(ParamsConstants.QLIDS_LOWERCASE, qlids.split(","));
            } else {
                map.put(ParamsConstants.QLIDS_LOWERCASE, "1");
            }
        }
        if (StringUtils.equals(checkMulGdFw, "true")) {
            if (StringUtils.isNotBlank(gdQlids)) {
                map.put(ParamsConstants.QLIDS_LOWERCASE, gdQlids.split(","));
            } else {
                map.put(ParamsConstants.QLIDS_LOWERCASE, "1");
            }
        }
        map.put("iszx",Constants.GDQL_ISZX_YZX);
        return repository.selectPaging("getGdFwSdJsonByPage", map, page - 1, rows);
    }

    @ResponseBody
    @RequestMapping("/getBdczsListByPage")
    public Object getBdczsListByPage(Pageable pageable, String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm
            , @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs
            , @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm, @RequestParam(value = "proid", required = false) String proid, String bdcdyhs
            , String fzqssj, String fzjssj, String zstype) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String sqlx = bdcXm != null ? bdcXm.getSqlx() : "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(bdcqzh)) {
                map.put(ParamsConstants.BDCQZH_LOWERCASE, bdcqzh);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
        }
        if (StringUtils.isNotBlank(bdclxdm)) {
            map.put("bdclxdm", bdclxdm.split(","));
        }
        /**
         * @author jiangganzhi
         * @description 异议登记不根据权利类型过滤 否则无法搜出证明
         */
        if (StringUtils.isNotBlank(sqlx) && !StringUtils.equals(sqlx, Constants.SQLX_YYDJ_DM)
                && StringUtils.isNotBlank(qllx)) {
            map.put("qllx", qllx.split(","));
        }
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put("zdtzm", zdtzm.split(","));
        }
        if (StringUtils.isNotBlank(dyfs)) {
            map.put("dyfs", dyfs);
        }
        if (StringUtils.isNotBlank(qlxzdm)) {
            map.put("qlxzdm", qlxzdm.split(","));
        }
        if (StringUtils.isNotBlank(ysqlxdm)) {
            map.put("ysqlxdm", ysqlxdm.split(","));
        }

        if (StringUtils.isNotBlank(zstype)) {
            if (StringUtils.equals(zstype, Constants.BDCQZS_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZS_BH_FONT);
            } else if (StringUtils.equals(zstype, Constants.BDCQZM_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZM_BH_FONT);
            } else {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, "不动产权证明单");
            }
        }
        if (StringUtils.isNotBlank(sqlx) && (Constants.SQLX_SPFSCKFSZC_DM.equals(sqlx) || Constants.SQLX_SPFXZBG_DM.equals(sqlx))) {
            zstype = AppConfig.getProperty("spfscdj.zstype");
            map.put(ParamsConstants.ZSTYPE_LOWERCASE, zstype);
        }
        //如果不是商品房买卖转移登记和商品房转移登记，首次登记信息表变更登记，商品房首次开发商自持要求不能搜到首次登记证
        if (StringUtils.isNotBlank(sqlx) && !StringUtils.equals(sqlx, Constants.SQLX_SPFMMZYDJ_DM) && !StringUtils.equals(sqlx, Constants.SQLX_CLF) && !StringUtils.equals(sqlx, Constants.SQLX_SPFSCKFSZC_DM) && !StringUtils.equals(sqlx, Constants.SQLX_SPFXZBG_DM) && !StringUtils.equals(sqlx, Constants.SQLX_PLCF) && !StringUtils.equals(sqlx, Constants.SQLX_CF)) {
            map.put("disSpfsc", "true");
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotBlank(fzqssj)) {
                map.put("fzqssj", simpleDateFormat.parse(fzqssj));
            }
            if (StringUtils.isNotBlank(fzjssj)) {
                map.put("fzjssj", simpleDateFormat.parse(fzjssj));
            }
        } catch (ParseException e) {
            logger.error("BdcSjSdController.getBdczsListByPage", e);
        }
        if (StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for (String tempBdcdyh : bdcdyhs.split(",")) {
                if (StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put("bdcdyhs", bdcdyhList);
        }
        //sc 需要展示不生成证书的权力信息
        map.put("filterNullBdcqzh", true);
        /**
         * @author bianwen
         * @description 将权利已注销的证书过滤掉
         */
        map.put("qszt", Constants.QLLX_QSZT_HR);

        /**
         * @author bianwen
         * @description 在建工程选择不动产单元页面显示一条信息
         */
        return repository.selectPaging("getBdcSdZsByPage", map, pageable);
    }


    /**
     * 添加房产证土地证
     *
     * @param model
     * @param qlid
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBdcqzToBdcGdSd", method = RequestMethod.POST)
    public Object addBdcGdSd(Model model, String qlid, String proid,
                             String cqzh, String tdfwid, String type, String xzyy, String tdzh, String fczh) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            map.put("type", type);
            map.put("tdfwid", tdfwid);
            map.put("qlid", qlid);
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            GdBdcSd sd = new GdBdcSd();
            BdcXm xm = bdcXmService.getBdcXmByProid(proid);
            sd.setSdid(UUIDGenerator.generate18());
            sd.setProid(proid);
            if (StringUtils.isNotBlank(cqzh)) {
                sd.setCqzh(cqzh);
            } else if (StringUtils.isNotBlank(fczh)) {
                sd.setCqzh(fczh);
            } else if (StringUtils.isNotBlank(tdzh)) {
                sd.setCqzh(tdzh);
            }
            sd.setSdr(super.getUserName());
            sd.setSdsj(new Date(System.currentTimeMillis()));
            //xzzt: 0 是正常，1 是锁定，新加的应该是锁定
            sd.setXzzt(1);
            if (xm != null) {
                sd.setWiid(xm.getWiid());
                sd.setBdclx(xm.getBdclx());
            }
            sd.setXzyy(xzyy);
            if (StringUtils.isNotBlank(qlid)) {
                sd.setQlid(qlid);
                GdBdcSd gdsd = gdBdcSdMapper.findGdBdcSd(qlid);
                if (gdsd != null) {
                    if (gdsd.getXzzt() == 1) {
                        map.put(ParamsConstants.MESSAGE_LOWERCASE, "该产权证已存在于锁定表中，且已被锁定");
                    } else if (gdsd.getXzzt() == 0) {
                        map.put(ParamsConstants.MESSAGE_LOWERCASE, "该产权证已存在于锁定表中，但未锁定");
                    }
                    return map;
                } else {
                    gdBdcSdMapper.insertGdBdcSd(sd);
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "添加成功");
                }
            }
            return map;
        } catch (Exception e) {
            logger.error("", e);
        }
        map.put(ParamsConstants.MESSAGE_LOWERCASE, "添加失败");
        return map;
    }

    /**
     * 解锁过度
     *
     * @param model
     * @param proid
     * @param
     * @param qlid
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unlockGdSd")
    public Map unlockGdsd(Model model, String qlid, String sdid, String proid, String type, String jsyy) {
        Map map = new HashMap();
        map.put("type", type);
        try {
            GdBdcSd sd = null;
            if (StringUtils.isNotBlank(sdid)) {
                sd = entityMapper.selectByPrimaryKey(GdBdcSd.class, sdid);
            } else {
                sd = gdBdcSdMapper.findGdBdcSd(qlid);
            }
            if (sd != null && 1 == sd.getXzzt() && jsyy != null) {
                sd.setXzzt(0);
                sd.setJsr(super.getUserName());
                sd.setJssj(new Date(System.currentTimeMillis()));
                sd.setJsyy(jsyy);
                gdBdcSdMapper.updateGdBdcSd(sd);
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "解锁成功");
                return map;
            }
        } catch (Exception e) {
            logger.error("sjsd/unlockGdSd", e);
        }
        map.put(ParamsConstants.MESSAGE_LOWERCASE, "解锁失败");
        return map;
    }

    /**
     * 修改锁定原因并更新锁定时间
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "modifySdSave")
    public Map modifySdSave(Model model, String sdids, String xzyy) {
        Map map = new HashMap();
        try {
            if (StringUtils.isNotBlank(sdids)) {
                bdcBdcdysdService.updateXzyyAndSdsj(sdids, xzyy);
                map.put(ParamsConstants.MESSAGE_LOWERCASE, "修改成功");
                return map;
            }
        } catch (Exception e) {
            logger.error("sjsd/modifySdSave", e);
        }
        map.put(ParamsConstants.MESSAGE_LOWERCASE, "修改失败");
        return map;
    }
}
