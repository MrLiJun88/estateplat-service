package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
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
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcJsxx")
public class BdcJsxxController extends BaseController {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;


    public static final String PARAMETER_GBDCDYWXZ = "该不动产单元未限制";

    @RequestMapping(value = "")
    public String index(Model model, String proid, String sdlx) {
        return "sjgl/bdcJsxx";
    }

    /**
     * 添加
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBdcJsxx", method = RequestMethod.POST)
    public Object addBdcJsxx(Model model, String bdcdyh, String cqzh, String zsid, String qlr, String zl,
                             String bdclx, String xzyy) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
            BdcJsxx js = new BdcJsxx();
            js.setJsid(UUIDGenerator.generate18());
            js.setBdcdyh(StringUtils.deleteWhitespace(bdcdyh));
            js.setBdclx(StringUtils.isNotBlank(bdclx) ? bdclx : bdcBdcdy != null ? bdcBdcdy.getBdclx() : null);
            js.setJbr(super.getUserName());
            js.setJssj(new Date(System.currentTimeMillis()));
            js.setJszt(Constants.JSZT_JS);
            js.setJsyy(StringUtils.deleteWhitespace(xzyy));
            js.setZsid(zsid);
            js.setQlr(StringUtils.deleteWhitespace(qlr));
            js.setZl(StringUtils.deleteWhitespace(zl));
            js.setCqzh(StringUtils.deleteWhitespace(cqzh));
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            Map<String, String> parmeter = new HashMap<String, String>();
            parmeter.put("cqzh",cqzh);
            parmeter.put(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
            parmeter.put("jszt",Constants.JSZT_JS);
            parmeter.put("jsid","");
            BdcJsxx bdcJsxx = getBdcJsxx(parmeter);
            if (bdcJsxx != null) {
                if (!bdcJsxx.getJszt().equals(Constants.JSZT_JS)) {
                    entityMapper.saveOrUpdate(js, js.getJsid());
                } else {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "该不动产单元已限制，请先取消限制");
                    return map;
                }
            } else {
                entityMapper.saveOrUpdate(js, js.getJsid());
            }
            map.put(ParamsConstants.MESSAGE_LOWERCASE, "添加成功");
        } catch (Exception e) {
            logger.error("sjsd/addBdcqzToBdcdySd", e);
        }
        return map;
    }


    /**
     * 获取警示信息
     *
     * @param pageable
     * @param cqzh
     * @param zsid
     * @param bdcdyh
     * @param jbr
     * @param qxr
     * @param jszt
     * @param dcxc
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcJsxxPagesJson")
    public Object getBdcJsxxPages(Pageable pageable, String cqzh, String zsid, String zl,
                                  String bdcdyh, String jbr, String qxr, String jszt, String dcxc) {
        Page<HashMap> dataPaging = null;
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            map.put("cqzh", StringUtils.deleteWhitespace(cqzh));
            map.put("jbr", StringUtils.deleteWhitespace(jbr));
            map.put("qxr", StringUtils.deleteWhitespace(qxr));
            map.put("zsid", StringUtils.isNotBlank(zsid)?zsid:"");
            map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
            map.put("zl", StringUtils.deleteWhitespace(zl));
            map.put("jbr", StringUtils.deleteWhitespace(jbr));
            map.put("jszt", jszt);
            dataPaging = repository.selectPaging("getBdcJsxxListByPage", map, pageable);
        } catch (Exception e) {
            logger.error("bdcJsxx/getBdcJsxxPagesJson", e);
        }
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping(value = "/updateBdcJsxx", method = RequestMethod.POST)
    public Object updateBdcJsxx(Model model, String cqzh, String bdcdyh, String zsid, String xzzt,
                                String xzyy, String jsid) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> parmeter = new HashMap<String, String>();
        try {
            parmeter.put("cqzh",cqzh);
            parmeter.put(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
            parmeter.put("jszt",Constants.JSZT_JS);
            parmeter.put("jsid",jsid);
            BdcJsxx bdcJsxx = getBdcJsxx(parmeter);
            if (bdcJsxx != null) {
                if (StringUtils.equals(bdcJsxx.getJszt(), Constants.JSZT_JS)) {
                    bdcJsxx.setJsyy(xzyy);
                    entityMapper.saveOrUpdate(bdcJsxx, bdcJsxx.getJsid());
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "修改成功");
                    return map;
                } else {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, PARAMETER_GBDCDYWXZ);
                    return map;
                }
            }
            map.put(ParamsConstants.MESSAGE_LOWERCASE, PARAMETER_GBDCDYWXZ);
        } catch (Exception e) {
            logger.error("sjsd/updateBdcJsxx", e.getStackTrace());
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/cancelBdcJsxx", method = RequestMethod.POST)
    public Object cancelBdcJsxx(Model model, String cqzh, String bdcdyh, String zsid, String xzzt,
                                String qxyy, String jsid) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> parmeter = new HashMap<String, String>();
        try {
            parmeter.put("cqzh",cqzh);
            parmeter.put(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
            parmeter.put("jszt",Constants.JSZT_JS);
            parmeter.put("jsid",jsid);
            BdcJsxx bdcJsxx = getBdcJsxx(parmeter);
            if (bdcJsxx != null) {
                if (StringUtils.equals(bdcJsxx.getJszt(), Constants.JSZT_JS)) {
                    bdcJsxx.setQxr(super.getUserName());
                    bdcJsxx.setQxsj(new Date(System.currentTimeMillis()));
                    bdcJsxx.setQxyy(StringUtils.deleteWhitespace(qxyy));
                    bdcJsxx.setJszt(Constants.JSZT_QX);
                    entityMapper.saveOrUpdate(bdcJsxx, bdcJsxx.getJsid());
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, "取消成功");
                    return map;
                } else {
                    map.put(ParamsConstants.MESSAGE_LOWERCASE, PARAMETER_GBDCDYWXZ);
                    return map;
                }
            }
            map.put(ParamsConstants.MESSAGE_LOWERCASE, PARAMETER_GBDCDYWXZ);
        } catch (Exception e) {
            logger.error("sjsd/updateBdcJsxx", e);
        }
        return map;
    }

    /**
     * 获取BdcJsxx
     *
     * @param
     * @param
     * @param
     * @return
     */
    public BdcJsxx getBdcJsxx(Map<String,String> parameter) {
        if (StringUtils.isBlank(parameter.get("cqzh")) && StringUtils.isBlank(parameter.get(ParamsConstants.BDCDYH_LOWERCASE))) {
            return null;
        }
        BdcJsxx bdcJsxx = null;
        try {
            Example example = new Example(BdcJsxx.class);
            Example.Criteria criteria = example.createCriteria();
            for(Map.Entry column:parameter.entrySet()){
                if(StringUtils.isNotBlank(column.getValue().toString())){
                    criteria.andEqualTo(column.getKey().toString(), column.getValue().toString());
                }
            }
            List<BdcJsxx> bdcJsxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJsxxList)) {
                bdcJsxx = bdcJsxxList.get(0);
            }
        } catch (Exception e) {
            logger.error("getBdcJsxx", e);
        }
        return bdcJsxx;

    }
}
