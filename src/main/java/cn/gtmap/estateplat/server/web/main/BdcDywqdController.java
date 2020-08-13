package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcDywqd;
import cn.gtmap.estateplat.model.server.core.BdcZjjzwxx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 抵押物清单
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 16-4-18
 */
@Controller
@RequestMapping("/bdcDywqd")
public class BdcDywqdController extends BaseController {
    @Autowired
    private EntityMapper entityMapper;


    /**
     * @author bianwen
     * @param
     * @rerutn
     * @description  注销抵押物清单信息
     */
    @ResponseBody
    @RequestMapping(value = "/zxDywqd")
    public String zxDywqd(@RequestParam(value = "ids", required = false) String ids) {
        String msg = "失败";
        if(StringUtils.isNotEmpty(ids)){
            for(String id:ids.split(",")){
                Example example=new Example(BdcZjjzwxx.class);
                example.createCriteria().andEqualTo("zjwid",id);
                List<BdcZjjzwxx> list=entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(list)){
                    BdcZjjzwxx zjjzwxx=list.get(0);
                    if(!StringUtils.equals(zjjzwxx.getDyzt(),"1")){
                        zjjzwxx.setDyzt("2");
                        entityMapper.saveOrUpdate(zjjzwxx, zjjzwxx.getZjwid());
                    }
                }
            }
            msg = "成功";
        }
        return msg;
    }
    /**
     * @author bianwen
     * @param
     * @rerutn
     * @description  还原注销抵押物清单信息
     */
    @ResponseBody
    @RequestMapping(value = "/hyDywqd")
    public String hyDywqd(@RequestParam(value = "ids", required = false) String ids){
        String msg = "失败";
        if(StringUtils.isNotEmpty(ids)){
            for(String id:ids.split(",")){
                Example example=new Example(BdcZjjzwxx.class);
                example.createCriteria().andEqualTo("zjwid",id);
                List<BdcZjjzwxx> list=entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(list)){
                    BdcZjjzwxx zjjzwxx=list.get(0);
                    zjjzwxx.setDyzt("0");
                    entityMapper.saveOrUpdate(zjjzwxx,zjjzwxx.getZjwid());
                }
            }
            msg = "成功";
        }
        return msg;
    }



    /**
     * @author <a href="mailto:zhangyu@gtmap.cn">zhangyu</a>
     * @Date 2017/3/22  16:21
     * @description  批量流程  注销抵押物清单
     */
    @ResponseBody
    @RequestMapping(value = "/zxPlDywqd")
    public String zxPlDywqd(@RequestParam(value = "ids", required = false) String ids) {
        String msg = "失败";
        if(StringUtils.isNotEmpty(ids)){
            for(String id:ids.split(",")){
                Example example=new Example(BdcDywqd.class);
                example.createCriteria().andEqualTo("proid",id);
                List<BdcDywqd> list=entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(list)){
                    BdcDywqd bdcDywqd=list.get(0);
                    if(!StringUtils.equals(bdcDywqd.getQszt(),"1")){
                        bdcDywqd.setQszt("2");
                        entityMapper.saveOrUpdate(bdcDywqd, bdcDywqd.getDywid());
                    }
                }
            }
            msg = "成功";
        }
        return msg;
    }

    /**
     * @author <a href="mailto:zhangyu@gtmap.cn">zhangyu</a>
     * @Date 2017/3/22  16:27
     * @description 还原批量流程，原注销抵押物清单信息
     */

    @ResponseBody
    @RequestMapping(value = "/hyPlDywqd")
    public String hyPlDywqd(@RequestParam(value = "ids", required = false) String ids){
        String msg = "失败";
        if(StringUtils.isNotEmpty(ids)){
            for(String id:ids.split(",")){
                Example example=new Example(BdcDywqd.class);
                example.createCriteria().andEqualTo("proid",id);
                List<BdcDywqd> list=entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(list)){
                    BdcDywqd  bdcdywqd =list.get(0);
                    bdcdywqd.setQszt("0");
                    entityMapper.saveOrUpdate(bdcdywqd,bdcdywqd.getDywid());
                }
            }
            msg = "成功";
        }
        return msg;
    }
}
