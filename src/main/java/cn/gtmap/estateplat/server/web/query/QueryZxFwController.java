package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.server.core.service.BdcDyaqService;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/31
 * @description 查找需要注销的房屋
 */

@Controller
@RequestMapping("/queryZxFw")
public class QueryZxFwController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcDyaqService bdcDyaqService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        List<BdcXmRel> bdcXmRelList =  bdcXmRelService.queryBdcXmRelByProid(proid);
        BdcXmRel bdcXmRel = null;
        String dyid = "";
        String yproid = "";
        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
            bdcXmRel = bdcXmRelList.get(0);
            dyid = bdcXmRel.getYqlid();
            yproid = bdcXmRel.getYproid();
        }

        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class,proid);
        if(StringUtils.isNotBlank(dyid)){
            model.addAttribute("dyid",dyid);
        }
        if(StringUtils.isNotBlank(yproid)){
            BdcXm bdcXm1 = entityMapper.selectByPrimaryKey(BdcXm.class,yproid);
            if(bdcXm1 != null){
                model.addAttribute("ywiid",bdcXm1.getWiid());
            }
        }
        model.addAttribute("proid",proid);
        model.addAttribute("splitStr", Constants.SPLIT_STR);
        String path = "";
        if(bdcXm != null){
            if(StringUtils.isNotBlank(bdcXm.getWiid()))
                model.addAttribute("wiid",bdcXm.getWiid());
            if(StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC))
                path = "query/queryZxDyaQl";
            else
                path = "query/queryZxFw";

        }
        return path ;
    }

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @description  获取当前他项证抵押的房屋
     */
    @ResponseBody
    @RequestMapping("/getDyFwPagesJson")
    public Object getDyFwPagesJson(Pageable pageable, String dyid,String hhSearch) {
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(hhSearch)) {
            map.put("hhSearch", StringUtils.deleteWhitespace(hhSearch.trim()));
        }
        if(StringUtils.isNotBlank(dyid)){
            map.put("dyid",dyid);
        }
        return repository.selectPaging("getDyFwJsonByPage", map, pageable);
    }

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @description 获取选择的要注销的房屋操作
     */
    @ResponseBody
    @Transactional( rollbackFor = Exception.class)
    @RequestMapping("/changeQszt")
    public Object changeQszt(String proid,String wiid, String ids, String xmly) {
        String msg = "";
        HashMap map = new HashMap();
        String[] idList =null;
        try{
            List<BdcDyaq> bdcDyaqList = new ArrayList<BdcDyaq>();
            if(StringUtils.isNotBlank(ids)){
                idList = StringUtils.split(ids,Constants.SPLIT_STR);
            }
            if(StringUtils.equals(xmly,"1")){
                if(idList != null && idList.length > 0){
                    for(String id : idList){
                        HashMap map1 = new HashMap();
                        map1.put("yproid",id);
                        map1.put("wiid",wiid);
                        BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByFwid(map1);
                        if(bdcDyaq != null)
                            bdcDyaqList.add(bdcDyaq);
                    }
                }
            }else if(StringUtils.equals(xmly,"3")&&idList != null&&idList.length > 0){
                for(String id : idList){
                    HashMap map1 = new HashMap();
                    map1.put("gdid",id);
                    map1.put("wiid",wiid);
                    BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByFwid(map1);
                    if(bdcDyaq != null) {
                        bdcDyaqList.add(bdcDyaq);
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(bdcDyaqList)){
                for(BdcDyaq bdcDyaq : bdcDyaqList){
                    bdcDyaq.setQszt(Constants.QLLX_QSZT_HR);
                    entityMapper.updateByPrimaryKeySelective(bdcDyaq);
                    msg = "success";
                }
            }

        }catch (Exception e){
            logger.error("QueryZxFwController.changeQszt",e);
            msg = "error";
        }

        map.put("msg",msg);
        return map;
    }
}
