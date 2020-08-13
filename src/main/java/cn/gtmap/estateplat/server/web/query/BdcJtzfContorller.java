package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcJtzf;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-11-25
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcJtzf")
public class BdcJtzfContorller extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;


    /*家庭住房情况证明查询**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "query/bdcJtzf";
    }

    @ResponseBody
    @RequestMapping("/getJtzf")
    public Object getJtzf(Pageable pageable,String qlr, String qlrzjh, String dcxc) {
        return null;
    }

    @ResponseBody
    @RequestMapping("/savejtfw")
    public String savejtfw(BdcJtzf bdcJtzf) {
        String msg = "失败";
        if (bdcJtzf != null) {
            Example example = new Example(BdcJtzf.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("zsid", bdcJtzf.getZsid());
            List<BdcJtzf> bdcJtzfList = entityMapper.selectByExample(BdcJtzf.class, example);
            if (CollectionUtils.isNotEmpty(bdcJtzfList)) {
                bdcJtzf.setId(bdcJtzfList.get(0).getId());
            } else {
                bdcJtzf.setId(UUIDGenerator.generate18());
            }
            entityMapper.saveOrUpdate(bdcJtzf, bdcJtzf.getId());
            msg = "成功";
        }
        return msg;
    }
}
