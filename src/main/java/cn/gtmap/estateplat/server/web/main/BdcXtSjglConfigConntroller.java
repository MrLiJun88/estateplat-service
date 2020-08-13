package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdQllxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登记资源的配置功能
 *
 * @author lst
 * @version V1.0, 15-3-20
 */
@Controller
@RequestMapping("/bdcConfig")
public class BdcXtSjglConfigConntroller extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXtSjglRelationService bdcXtSjglRelationService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZdCheckService bdcZdCheckService;
    @Autowired
    private BdcXtCheckinfoService bdcXtCheckinfoService;
    @Autowired
    private BdcXtConfigService bdcXtConfigService;
    @Autowired
    private BdcXtLimitfieldService bdcXtLimitfieldService;
    @Autowired
    private BdcZdQllxMapper bdcZdQllxMapper;
    @Autowired
    private BdcXtQlqtzkConfigService bdcXtQlqtzkConfigService;
    @Autowired
    private BdcSqlxQllxRelService bdcSqlxQllxRelService;


    private static final String PARAMETER_DJLIST = "djList";
    private static final String PARAMETER_SQLIST = "sqList";
    private static final String PARAMETER_QLLIST = "qlList";
    private static final String PARAMETER_TJCG_MC = "添加成功！";
    private static final String PARAMETER_TJSB_MC = "添加失败！";
    private static final String PARAMETER_SCCG_MC = "删除成功！";
    private static final String PARAMETER_SCSB_MC = "删除失败！";


    @ResponseBody
    @RequestMapping("/getBdcXtResourcePagesJson")
    public Object getBdcXtResourcePagesJson(Pageable pageable) {
        HashMap<String, String> map = new HashMap<String, String>();
        return repository.selectPaging("getBdcXtResourcePagesJson", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcXtRelationPagesJson")
    public Object getBdcXtRelationPagesJson(Pageable pageable, String djlxId, String resourceId) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("djlxId", djlxId);
        map.put("resourceId", resourceId);
        return repository.selectPaging("getBdcXtRelationPagesJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcXtCheckinfoPagesJson")
    public Object getBdcXtCheckinfoPagesJson(Pageable pageable, BdcXtCheckinfo bdcXtCheckinfo) {
        return repository.selectPaging("getBdcXtCheckinfoJsonByPage", bdcXtCheckinfo, pageable);
    }

    /**
     * 跳转配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toXtConfig")
    public String toXtConfig(Model model) {
        List<BdcXtSjglResource> list = entityMapper.select(new BdcXtSjglResource());
        List<Map> sqlxList = bdcZdGlService.getZdSqlxList();
        List<BdcZdQllx> qllxList = bdcZdGlService.getBdcQllx();
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        Example example = new Example(BdcZdCheck.class);
        List<BdcZdCheck> zdCheckList = bdcZdCheckService.getZdCheck(example);
        model.addAttribute("resourceList", list);
        model.addAttribute(PARAMETER_DJLIST, djlxList);
        model.addAttribute(PARAMETER_SQLIST, sqlxList);
        model.addAttribute(PARAMETER_QLLIST, qllxList);
        model.addAttribute("checkList", zdCheckList);
        return "sjgl/djzyManage";
    }

    /**
     * 跳转资源配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toResourceConfig")
    public String toResourceConfig(Model model) {
        return "sjgl/resourceManage";
    }

    /**
     * 跳转登记资源配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toRelationConfig")
    public String toRelationConfig(Model model) {
        List<BdcXtSjglResource> list = entityMapper.select(new BdcXtSjglResource());
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        model.addAttribute("resourceList", list);
        model.addAttribute(PARAMETER_DJLIST, djlxList);
        return "sjgl/relationManage";
    }

    /**
     * 跳转验证配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toCheckConfig")
    public String toCheckConfig(Model model) {
        List<Map> sqlxList = bdcZdGlService.getZdSqlxList();
        List<BdcZdQllx> qllxList = bdcZdGlService.getBdcQllx();
        Example example = new Example(BdcZdCheck.class);
        List<BdcZdCheck> zdCheckList = bdcZdCheckService.getZdCheck(example);
        model.addAttribute(PARAMETER_SQLIST, sqlxList);
        model.addAttribute(PARAMETER_QLLIST, qllxList);
        model.addAttribute("checkList", zdCheckList);
        return "sjgl/xtCheck";
    }

    /**
     * 修改资源
     *
     * @param model
     * @param RESOURCE_URL
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateResource")
    public HashMap updateResource(Model model, String id, String RESOURCE_URL, String RESOURCE_IMG) {
        HashMap map = new HashMap();
        String result = "修改成功！";
        try {
            BdcXtSjglResource bdcXtSjglResource;
            bdcXtSjglResource = entityMapper.selectByPrimaryKey(BdcXtSjglResource.class, id);
            //判断修改的是图片还是url
            if (RESOURCE_URL == null) {
                bdcXtSjglResource.setResourceImg(RESOURCE_IMG);
            } else {
                bdcXtSjglResource.setResourceUrl(RESOURCE_URL);
            }
            entityMapper.updateByPrimaryKeySelective(bdcXtSjglResource);
        } catch (Exception e) {
            e.printStackTrace();
            result = "修改失败！";
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }

        return map;
    }


    /**
     * 新增资源
     *
     * @param model
     * @param bdcXtSjglResource
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveResource")
    public HashMap saveResource(Model model, BdcXtSjglResource bdcXtSjglResource) {
        HashMap map = new HashMap();
        String result = PARAMETER_TJCG_MC;
        try {
            bdcXtSjglResource.setResourceId(UUIDGenerator.generate18());
            entityMapper.insertSelective(bdcXtSjglResource);
        } catch (Exception e) {
            e.printStackTrace();
            result = PARAMETER_TJSB_MC;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * 删除资源
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delResource")
    public HashMap delResource(Model model, String ids) {
        HashMap map = new HashMap();
        String result = PARAMETER_SCCG_MC;
        try {
            String id[] = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                entityMapper.deleteByPrimaryKey(BdcXtSjglResource.class, id[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = PARAMETER_SCSB_MC;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * 删除登记资源关系
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delRelation")
    public HashMap delRelation(Model model, String ids) {
        HashMap map = new HashMap();
        String result = PARAMETER_SCCG_MC;
        try {
            String id[] = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                entityMapper.deleteByPrimaryKey(BdcXtSjglRelation.class, id[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = PARAMETER_SCSB_MC;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * 删除验证信息
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delYz")
    public HashMap delYz(Model model, String ids) {
        HashMap map = new HashMap();
        String result = PARAMETER_SCCG_MC;
        try {
            String id[] = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                entityMapper.deleteByPrimaryKey(BdcXtCheckinfo.class, id[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = PARAMETER_SCSB_MC;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * 新增登记资源关系
     *
     * @param model
     * @param bdcXtSjglRelation
     * @return
     */
    @ResponseBody
    @RequestMapping("/addRelation")
    public HashMap addRelation(Model model, BdcXtSjglRelation bdcXtSjglRelation) {
        HashMap map = new HashMap();
        String result = PARAMETER_TJCG_MC;
        try {
            bdcXtSjglRelation.setRelationId(UUIDGenerator.generate18());
            Integer xh = bdcXtSjglRelationService.getMaxXh(bdcXtSjglRelation.getDjlxId());
            if (xh == null) {
                xh = 1;
            }
            bdcXtSjglRelation.setXh(xh);
            entityMapper.insertSelective(bdcXtSjglRelation);
        } catch (Exception e) {
            e.printStackTrace();
            result = PARAMETER_TJSB_MC;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * 新增验证信息
     *
     * @param model
     * @param bdcXtCheckinfo
     * @return
     */
    @ResponseBody
    @RequestMapping("/addCheckinfo")
    public HashMap addCheckinfo(Model model, BdcXtCheckinfo bdcXtCheckinfo) {
        HashMap map = new HashMap();
        String result = PARAMETER_TJCG_MC;
        try {
            bdcXtCheckinfo.setId(UUIDGenerator.generate18());
            bdcXtCheckinfoService.saveXtCheckinfo(bdcXtCheckinfo);
        } catch (Exception e) {
            e.printStackTrace();
            result = PARAMETER_TJSB_MC;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * 通过登记类型获取资源生成tab页
     *
     * @param model
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getResourceByDjlx")
    public List<BdcXtSjglResource> getResourceByDjlx(Model model, String proid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        List<BdcXtSjglResource> list = new ArrayList<BdcXtSjglResource>();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            list = bdcXtSjglRelationService.getResourceByDjlx(bdcXm.getDjlx());
        }
        if (list != null && list.size() > 0)
            list = bdcXtSjglRelationService.initPropertyUrl(list);
        return list;
    }

    /**
     * 修改序号
     *
     * @param model
     * @param XH
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateRelation")
    public HashMap updateRelation(Model model, String id, Integer XH) {
        HashMap map = new HashMap();
        String result = "修改成功！";
        try {
            BdcXtSjglRelation bdcXtSjglRelation = entityMapper.selectByPrimaryKey(BdcXtSjglRelation.class, id);
            bdcXtSjglRelation.setXh(XH);
            entityMapper.updateByPrimaryKeySelective(bdcXtSjglRelation);
        } catch (Exception e) {
            e.printStackTrace();
            result = "修改失败！";
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * 跳转申请类型和权利类型关系表配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toSqlxQllxRelConfig")
    public String toSqlxQllxRelConfig(Model model) {
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<Map> sqlxList = bdcZdGlService.getZdSqlxList();
        List<BdcZdQllx> qllxList = bdcZdGlService.getBdcQllx();
        List<Map> bdclxList = bdcZdGlService.getZdDzwtzm();
        List<Map> qlxzList = bdcZdGlService.getZdQlxz();
        List<Map> dyfsList = bdcZdGlService.getZdDyfs();
        List<Map> zdtzmList = bdcZdGlService.getZdtzm();
        List<Map> dzwtzmList = bdcZdGlService.getZdDzwtzm();
        model.addAttribute(PARAMETER_DJLIST, djlxList);
        model.addAttribute(PARAMETER_SQLIST, sqlxList);
        model.addAttribute(PARAMETER_QLLIST, qllxList);
        model.addAttribute("bdcList", bdclxList);
        model.addAttribute("qlxzList", qlxzList);
        model.addAttribute("dyfsList", dyfsList);
        model.addAttribute("zdtzmList", zdtzmList);
        model.addAttribute("dzwtzmList", dzwtzmList);
        //登记类型和申请类型默认值
        //model.addAttribute("djlx", Constants.DJLX_CSDJ_DM);
        model.addAttribute("djlx", Constants.DJLX_DYDJ_DM);
        return "config/sqlxQllxRelConfig";
    }


    /**
     * 关系表配置页面
     * 根据登记类型获取申请类型
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getSqlxByDjlx")
    public List<BdcZdSqlx> getSqlxByDjlx(Model model, String djlx) {
        List<BdcZdSqlx> list = new ArrayList<BdcZdSqlx>();
        if (StringUtils.isNotBlank(djlx)) {
            list = bdcZdGlService.getSqlxBydjlx(djlx);
        }
        return list;
    }

    /**
     * 关系表配置页面
     * 根据申请类型获其他数据
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getOthersBySqlx")
    public BdcSqlxQllxRel getOthersBySqlx(Model model, String sqlx) {
        BdcSqlxQllxRel bdcSqlxQllxRel = new BdcSqlxQllxRel();
        if (StringUtils.isNotBlank(sqlx)) {
            List<BdcSqlxQllxRel> list = bdcXtConfigService.getOthersBySqlx(sqlx);
            if (list != null) {
                bdcSqlxQllxRel = list.get(0);
            }
        }
        return bdcSqlxQllxRel;
    }


    @ResponseBody
    @RequestMapping("/getBdcXtLogConfigPagesJson")
    public Object getBdcXtLogConfigPagesJson(Pageable pageable, String sidx, String sord) {
        HashMap<String, String> map = new HashMap<String, String>();
        Page<HashMap> dataPaging = repository.selectPaging("getBdcXtLogConfigByPage", map, pageable);
        return dataPaging;
    }

    /**
     * 跳转拦截方法记录日志配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toZdLogConfig")
    public String toZdLogConfig(Model model) {
        return "config/xtLogConfig";
    }

    /**
     * 保存系统日志配置页面
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveSqlxQllxRel")
    public HashMap saveSqlxQllxRel(Model model, BdcSqlxQllxRel bdcSqlxQllxRel) {
        HashMap map = new HashMap();
        String result = "保存成功";
        try {
            //获取申请类型
            String sqlx = bdcSqlxQllxRel.getSqlxdm();
            //判断数据库是否已有该申请类型，有则更新。无则添加
            if (bdcXtConfigService.getOthersBySqlx(sqlx) != null) {
                //有则获取已存在数据库中的ID，因为未传入，因此要获取
                BdcSqlxQllxRel oBdcSqlxQllxRel = bdcXtConfigService.getOthersBySqlx(sqlx).get(0);
                bdcSqlxQllxRel.setId(oBdcSqlxQllxRel.getId());
                entityMapper.updateByPrimaryKeyNull(bdcSqlxQllxRel);
            } else {
                bdcSqlxQllxRel.setId(UUIDGenerator.generate18());
                entityMapper.insertSelective(bdcSqlxQllxRel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "保存失败";
        }
        map.put(ParamsConstants.RESULT_LOWERCASE, result);
        return map;
    }

    /**
     * 删除系统日志配置
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delLogConfig")
    public HashMap delLogConfig(Model model, String ids) {
        HashMap map = new HashMap();
        String result = PARAMETER_SCCG_MC;
        try {
            String id[] = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                entityMapper.deleteByPrimaryKey(BdcZdLogController.class, id[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = PARAMETER_SCSB_MC;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }


    /**
     * 跳转必填字段表格配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toZdLimitTableConfig")
    public String toZdLimitTableConfig(Model model, boolean selTable) {
        model.addAttribute("isSelect", selTable);
        return "config/zdLimitTable";
    }

    /**
     * 保存必填字段表格配置页面
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveLimitTable")
    public HashMap saveLimitTable(Model model, BdcZdTables bdcZdTables) {
        HashMap map = new HashMap();
        String result = "保存成功";
        try {
            bdcZdTables.setId(UUIDGenerator.generate18());
            entityMapper.insertSelective(bdcZdTables);
        } catch (Exception e) {
            e.printStackTrace();
            result = "保存失败";
        }
        map.put(ParamsConstants.RESULT_LOWERCASE, result);
        return map;
    }

    /**
     * 删除必填字段表格配置
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delLimitTableConfig")
    public HashMap delLimitTableConfig(Model model, String ids) {
        HashMap map = new HashMap();
        String result = PARAMETER_SCCG_MC;
        try {
            String id[] = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                Example example = new Example(BdcXtLimitfield.class);
                example.createCriteria().andEqualTo("tableId", id[i]);
                int count = entityMapper.countByExample(example);
                if (count > 0) {
                    result = "此数据与其他表关联,无法删除!";
                } else {
                    entityMapper.deleteByPrimaryKey(BdcZdTables.class, id[i]);
                }
            }
        } catch (Exception e) {
            result = PARAMETER_SCSB_MC;
            e.printStackTrace();
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/getBdcLimitTableConfigPagesJson")
    public Object getBdcLimitTableConfigPagesJson(Pageable pageable, String sidx, String sord) {
        HashMap<String, String> map = new HashMap<String, String>();
        Page<HashMap> dataPaging = repository.selectPaging("getBdcLimitTableConfigByPage", map, pageable);
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping("/getLimitFieldPagesJson")
    public Object getLimitFieldPagesJson(Pageable pageable, String sidx, String sord, BdcXtLimitfield bdcXtLimitfield) {
        Page<HashMap> dataPaging = repository.selectPaging("getLimitFieldByPage", bdcXtLimitfield, pageable);
        return dataPaging;
    }

    /**
     * 跳转必填字段表格配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toLimitFieldConfig")
    public String toLimitFieldConfig(Model model) {
        List<PfWorkFlowDefineVo> workFlowList = PlatformUtil.getWorkFlowDefineService().getWorkFlowDefineList();
        List<BdcZdTables> list = bdcZdGlService.getBdcZdTables();
        //zwq 取所有的qllx
        List<BdcZdQllx> qllxList = bdcZdQllxMapper.queryBdcZdQllxByDm();

        model.addAttribute("workFlowList", workFlowList);
        model.addAttribute("zdTableList", list);
        model.addAttribute("qllxList", qllxList);
        return "config/limitField";
    }

    /**
     * 获取工作流的节点
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/getWorkFlowNodes")
    public List<HashMap> getWorkFlowNodes(Model model, String workflowId) {
        String xml = PlatformUtil.getWorkFlowDefineService().getWorkFlowDefineXml(workflowId);
        return bdcZdGlService.getWorkFlowNodes(xml);
    }

    /**
     * 获取验证table的字典表数据
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/getZdTable")
    public List<BdcZdTables> getZdTable(Model model, String workflowId) {
        List<BdcZdTables> list = bdcZdGlService.getBdcZdTables();
        return list;
    }


    /**
     * 获取验证table的字典表数据
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFields")
    public List<HashMap> getFields(Model model, String workFlowId, String workFlowNodeid, String id, @RequestParam(value = "cptName", required = false) String cptName) {
        return bdcZdGlService.getFields(workFlowId, workFlowNodeid, id, cptName);
    }

    /**
     * 保存必填字段表格配置页面
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveLimitField")
    public HashMap saveLimitField(Model model, BdcXtLimitfield bdcXtLimitfield) {
        HashMap map = new HashMap();
        String result = "保存成功";
        try {
            if (StringUtils.isNotBlank(bdcXtLimitfield.getId())) {
                entityMapper.updateByPrimaryKeyNull(bdcXtLimitfield);
            } else {
                bdcXtLimitfield.setId(UUIDGenerator.generate18());
                entityMapper.insertSelective(bdcXtLimitfield);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "保存失败";
        }
        map.put(ParamsConstants.RESULT_LOWERCASE, result);
        return map;
    }

    /**
     * 删除必填字段配置
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delLimitFieldConfig")
    public HashMap delLimitFieldConfig(Model model, String ids) {
        HashMap map = new HashMap();
        String result = PARAMETER_SCCG_MC;
        try {
            String id[] = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                entityMapper.deleteByPrimaryKey(BdcXtLimitfield.class, id[i]);
            }
        } catch (Exception e) {
            result = PARAMETER_SCSB_MC;
            e.printStackTrace();
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * sc 跳转默认意见配置
     *
     * @param model
     * @return
     */
    @RequestMapping("/toOpinionConfig")
    public String toOpinionConfig(Model model) {
        List<PfWorkFlowDefineVo> workFlowList = PlatformUtil.getWorkFlowDefineService().getWorkFlowDefineList();
        model.addAttribute("workFlowList", workFlowList);
        return "config/opinion";
    }

    @ResponseBody
    @RequestMapping("/getOpinionPagesJson")
    public Object getOpinionFieldPagesJson(Pageable pageable, String sidx, String sord, BdcXtOpinion bdcXtOpinion) {
        return repository.selectPaging("getOpinionPage", bdcXtOpinion, pageable);
    }

    /**
     * 保存必填字段表格配置页面
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOpinion")
    public HashMap saveOpinion(Model model, BdcXtOpinion bdcXtOpinion) {
        HashMap map = new HashMap();
        String result = "保存成功";
        try {
            if (StringUtils.isNotBlank(bdcXtOpinion.getOpinid())) {
                bdcXtOpinion.setUserid(super.getUserId());
                entityMapper.updateByPrimaryKeyNull(bdcXtOpinion);
            } else {
                bdcXtOpinion.setOpinid(UUIDGenerator.generate18());
                bdcXtOpinion.setUserid(super.getUserId());
                entityMapper.insertSelective(bdcXtOpinion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "保存失败";
        }
        map.put(ParamsConstants.RESULT_LOWERCASE, result);
        return map;
    }

    /**
     * 删除必填字段配置
     *
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/delOpinion")
    public HashMap delOpinion(Model model, String ids) {
        HashMap map = new HashMap();
        String result = PARAMETER_SCCG_MC;
        try {
            String id[] = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                entityMapper.deleteByPrimaryKey(BdcXtOpinion.class, id[i]);
            }
        } catch (Exception e) {
            result = PARAMETER_SCSB_MC;
            e.printStackTrace();
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
        }
        return map;
    }

    /**
     * lst
     * 跳转权利其他状况模板配置文件展示页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toQlqtzkConfig")
    public String toQlqtzkConfig(Model model) {
        model.addAttribute("sqlxList", bdcZdGlService.getBdcSqlxList());
        model.addAttribute("fwlxList", bdcZdGlService.getBdcZdFwlx());
        model.addAttribute("qllxList", bdcZdGlService.getBdcQllx());
        return "config/qlqtzkTempConfig";
    }

    /**
     * lst
     * 根据申请类型和子类型
     * 获取对应的模板内容和sql语句
     *
     * @param model
     * @param bdcXtQlqtzkConfig
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQlqtzkData")
    public List<BdcXtQlqtzkConfig> getQlqtzkData(Model model, BdcXtQlqtzkConfig bdcXtQlqtzkConfig) {
        List<BdcXtQlqtzkConfig> list = null;
        list = bdcXtQlqtzkConfigService.getQlqtzk(bdcXtQlqtzkConfig);
        return list;
    }

    /*
     * lst
     * 根据申请类型和子类型
     * 新增或修改模板内容和sql语句
     * @param model
     * @param bdcXtQlqtzkConfig
     * @return
     * */
    @ResponseBody
    @RequestMapping("/saveOrUpdateQlqtzk")
    public boolean saveOrUpdateQlqtzk(Model model, BdcXtQlqtzkConfig bdcXtQlqtzkConfig) {
        boolean result = true;
        try {
            bdcXtQlqtzkConfigService.saveOrUpdateQlqtzk(bdcXtQlqtzkConfig);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * lst
     * 根据申请类型和子类型
     * 删除模板内容和sql语句
     *
     * @param model
     * @param bdcXtQlqtzkConfig
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteQlqtzk")
    public boolean deleteQlqtzk(Model model, BdcXtQlqtzkConfig bdcXtQlqtzkConfig) {
        boolean result = true;
        try {
            bdcXtQlqtzkConfigService.deleteQlqtzk(bdcXtQlqtzkConfig);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/validateSql")
    public HashMap validateSql(Model model, String sqls) {
        HashMap map = new HashMap();
        boolean result = true;
        int num = 1;
        try {
            if (StringUtils.isNotBlank(sqls)) {
                String[] sql = sqls.split(";|；");
                for (int i = 0; i < sql.length; i++) {
                    if (StringUtils.isNotBlank(sql[i])) {
                        sql[i] = sql[i].replaceAll("(?i)@proid", "'test'");
                        bdcXtLimitfieldService.runSql(PlatformUtil.initOptProperties(sql[i]));
                        num += 1;
                    }
                }
            }
        } catch (Exception e) {
            result = false;
        } finally {
            map.put(ParamsConstants.RESULT_LOWERCASE, result);
            map.put("msg", "第" + num + "条sql语句错误！");
        }
        return map;
    }

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/3/29
     * @param:sqlxdm
     * @return:String
     * @description: 通过申请类型查询权利类型
     */
    @ResponseBody
    @RequestMapping(value = "getQllxBySqlx")
    public String getQllxBySqlx(String sqlxdm) {
        return bdcSqlxQllxRelService.getQllxBySqlx(sqlxdm);
    }
}
