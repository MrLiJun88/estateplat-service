package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZdFwRelMapper;
import cn.gtmap.estateplat.server.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *不动产商品房与宗地核减关系
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2016/9/13
 */
@Controller
@RequestMapping("/bdcZdFwRel")
public class BdcZdFwRelController extends BaseController  {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcZdFwRelMapper bdcZdFwRelMapper;

    @RequestMapping(value = "/updateMulMjAndJe", method = RequestMethod.GET)
    public String updateMulMjAndJe(@RequestParam(value = "proid", required = false) String proid,
                                    @RequestParam(value = "hjjyje", required = false) Double hjjyje,
                                    @RequestParam(value = "xmhjdyje", required = false) Double xmhjdyje,
                                    @RequestParam(value = "bz", required = false) String bz,
                                    @RequestParam(value = "hjtdmj", required = false) Double hjtdmj)
    {
        try {
            Map<String,Object> map=new HashMap<String,Object>();
            if(hjjyje!=null){
                map.put("fwhjdyje",hjjyje);
            }
            if(hjtdmj!=null){
                map.put("fwhjtdmj",hjtdmj);
            }
            if(xmhjdyje!=null){
                map.put("xmhjdyje",xmhjdyje);
            }
            if(bz!=null){
                map.put("bz",bz);
            }
            map.put("proid",proid);
            bdcZdFwRelMapper.updateJeAndMj(map);
        }catch (Exception e){
            logger.info("updateMulMjAndJe:error");
        }
        return null;
    }
}
