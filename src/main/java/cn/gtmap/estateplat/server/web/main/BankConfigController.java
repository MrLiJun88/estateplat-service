package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcXtYh;
import cn.gtmap.estateplat.model.server.core.BdcZdZjlx;
import cn.gtmap.estateplat.server.core.service.BdcBankService;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liujie
 * Date: 15-9-18
 * Time: 下午2:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcConfig")
public class BankConfigController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcBankService bdcBankService;

    /**
     * @param model
     * @return
     */
    @RequestMapping("/toBankConfig")
    public String toOpinionConfig(Model model) {
        List<BdcZdZjlx> bdcZdZjlxList = bdcZdGlService.getBdcZdZjlx();
        UserInfo info = SessionUtil.getCurrentUser();
        String hasEdit="false";
        String username = info.getUsername();
        String uploadBank = AppConfig.getProperty("uploadBank");
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(uploadBank)&&uploadBank.contains(username)){
            hasEdit="true";
        }
        model.addAttribute("hasEdit",hasEdit);
        model.addAttribute("bdcZdZjlxListJosn", JSONObject.toJSONString(bdcZdZjlxList));
        model.addAttribute("bdcZdZjlxList", bdcZdZjlxList);
        return "config/bank";
    }


    @ResponseBody
    @RequestMapping("/getBankPagesJson")
    public Object getBankFieldPagesJson(Pageable pageable, String sidx, String sord, BdcXtYh bdcBank) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        return repository.selectPaging("getBankListByPage", map, pageable);
    }

    /**
     * 保存必填字段表格配置页面
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveBank")
    public HashMap saveBank(Model model, BdcXtYh bdcBank) {
        HashMap map = new HashMap();
        String result = "保存成功";
        try {
            bdcBankService.insertOrUpdateByPrimaryKey(bdcBank);
        } catch (Exception e) {
            logger.error("BankConfigController.saveBank",e);
            result = "保存失败";
        }
        map.put("result", result);
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
    @RequestMapping("/delBank")
    public HashMap delBank(Model model, String ids) {
        HashMap map = new HashMap();
        String result = "删除成功！";
        try {
            bdcBankService.deleteBankByPrimaryKey(ids);
        } catch (Exception e) {
            result = "删除失败！";
            logger.error("BankConfigController.delBank",e);
        } finally {
            map.put("result", result);
        }
        return map;
    }
}
