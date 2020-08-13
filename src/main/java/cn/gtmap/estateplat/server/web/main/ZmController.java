package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.mapper.BdcZmMapper;
import com.alibaba.fastjson.JSONObject;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysAuthorService;
import com.gtis.plat.vo.PfPartitionInfoVo;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xhc on 2015/11/11.
 * 证明
 */
@Controller
@RequestMapping("/zm")
public class ZmController extends BaseController {

    @Autowired
    private Repo repository;
    @Autowired
    private BdcZmMapper bdcZmMapper;
    @Autowired
    private SysAuthorService sysAuthorService;

    /**
     * 页面跳转
     *
     * @param model
     * @return
     */
    @RequestMapping("/zm")
    public String index(Model model, String rid) {
        List<Map> zmmcList = bdcZmMapper.getZmMc();
        UserInfo info = SessionUtil.getCurrentUser();
        //管理员的时候取不到角色id
        String roleIds = info.getRoleIds();
        //没有角色去数据库查一遍
        if (org.apache.commons.lang3.StringUtils.isBlank(roleIds)) {
            roleIds = "'" + this.getRoleIdsByUser(info.getId()) + "'";
        }
        List<PfPartitionInfoVo> list = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(rid)) {
            //将资源id中的r:去掉
            if (rid.indexOf("r:") > -1) {
                rid = rid.replaceAll("r:", "");
            }
            list = sysAuthorService.getSystemResrouceFunAuthorList(roleIds, rid);
        }
        model.addAttribute("zmmcList", zmmcList);
        model.addAttribute("authorList", JSONObject.toJSONString(list));
        return "query/zmList";
    }

    /**
     * 删除证明
     *
     * @param model
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delBdcZm")
    public HashMap<String, Object> delBdcZm(Model model, String id) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String result = "删除成功！";
        try {
            if (StringUtils.isNotBlank(id)) {
                bdcZmMapper.delBdcZmByZmid(id);
            }
        } catch (Exception e) {
            result = "删除失败！";
            logger.error("ZmController.delBdcZm",e);
        } finally {
            resultMap.put("result", result);
        }
        return resultMap;
    }

    /**
     * 创建证明
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/creatBdcZm")
    public HashMap<String, Object> creatBdcZm(Model model, String zmlx) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        HashMap<String, Object> saveMap = new HashMap<String, Object>();
        String zmid = UUIDGenerator.generate18();
        String zmslr = SessionUtil.getCurrentUser().getUsername();
        Date date = new Date();
        saveMap.put("date", date);
        saveMap.put("zmid", zmid);
        saveMap.put("zmslr", zmslr);
        saveMap.put("zmlx", zmlx);
        resultMap.put("zmid", zmid);
        bdcZmMapper.creatZm(saveMap);
        return resultMap;
    }

    /**
     * 返回页面数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/getZmPagesJson")
    public Object getLpbPagesJson(Pageable pageable, String zmid, String zmlx, String zmslr, String zmsqr, String zmqsrq, String zmjsrq) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("zmid", zmid);
            map.put("zmlx", zmlx);
            map.put("zmslr", zmslr);
            map.put("zmsqr", zmsqr);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(zmjsrq)) {
                map.put("zmjsrq", new SimpleDateFormat("yyyy-MM-dd").parse(zmjsrq));
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(zmqsrq)) {
                map.put("zmqsrq", new SimpleDateFormat("yyyy-MM-dd").parse(zmqsrq));
            }
        } catch (ParseException e) {
            logger.error("ZmController.getLpbPagesJson",e);
        }
        return repository.selectPaging("getZmListByPage", map, pageable);
    }

}
