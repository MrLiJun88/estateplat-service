package cn.gtmap.estateplat.server.web.evaluation;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcTszt;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.model.evaluation.Params;
import cn.gtmap.estateplat.server.model.evaluation.PushData;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.HttpRequestUtils;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @authoer bianwen
 * @createTime 2018/8/21
 * @description 评价系统接口调用
 */
@Controller
@RequestMapping("/evaluation")
public class EvaluationController extends BaseController {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private EntityMapper entityMapper;
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(EvaluationController.class);

    /**
     * @authoer bianwen
     * @description 摩柯评价系统接口调用
     * @param taskid
     * @return
     *
     */
    @ResponseBody
    @RequestMapping(value = "/triggerEvaluation", method = RequestMethod.GET)
    public Map triggerEvaluation(@RequestParam(value = "taskid", required = false) String taskid){
        Map resultMap = new HashMap();
        String resultUrl = "";
        String success = "false";
        String proid = PlatformUtil.getProidByTaskId(taskid);
        BdcXm bdcXm=bdcXmService.getBdcXmByProid(proid);
        if(bdcXm!=null){
            String ywbh=bdcXm.getBh();
            String jdmc="";
            String blry=bdcXm.getCjr();
            String sqrxm="";
            String sqrlxfs="";
            if (StringUtils.isNotBlank(taskid)) {
                jdmc=PlatformUtil.getPfActivityNameByTaskId(taskid);
                SysTaskService sysTaskService = PlatformUtil.getTaskService();
                PfTaskVo pfTask = sysTaskService.getTask(taskid);
                if (pfTask != null) {
                    PfUserVo userVo=pfTask.getUserVo();
                    blry=userVo.getUserName();
                }
            }
            if(StringUtils.isNotBlank(bdcXm.getProid())){
                List<BdcQlr> qlrList=bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                if(CollectionUtils.isNotEmpty(qlrList)){
                    BdcQlr bdcQlr=qlrList.get(0);
                    if(bdcQlr!=null){
                        sqrxm =bdcQlr.getQlrmc();
                        sqrlxfs=bdcQlr.getQlrlxdh();
                    }
                }
            }

            //qijiadong张家港新点评价器接口
            String epointEvaluationUrl=AppConfig.getProperty("epoint.evaluation.Url");
            if (StringUtils.isNotBlank(epointEvaluationUrl)) {
                PushData pushData = new PushData();
                Params params = new Params();
                params.setBlry(blry);
                params.setYwbh(ywbh);
                params.setJdmc(jdmc);
                params.setSqrxm(sqrxm);
                params.setSqrlxfs(sqrlxfs);
                pushData.setToken(ParamsConstants.TOKEN_VALUE);
                pushData.setParams(params);
                String pushdata = StringUtils.isNotBlank(JSON.toJSONString(pushData)) ? JSON.toJSONString(pushData) : "";
                if(StringUtils.isNotBlank(pushdata)){
                    String result = "";
                    try {
                        result = HttpRequestUtils.sendPost(epointEvaluationUrl, pushdata);
                        success = "true";
                    } catch (Exception e) {
                        log.error("返回异常：" + e);
                    }
                    BdcTszt bdcTszt=new BdcTszt();
                    bdcTszt.setProid(proid);
                    bdcTszt.setTsr(blry);
                    bdcTszt.setTsjdmc(jdmc);
                    bdcTszt.setTsqk(pushdata);
                    insertBdcTszt(bdcTszt,result);
                }
            } else {
                //摩柯评价器接口调用
                BdcTszt bdcTszt=new BdcTszt();
                bdcTszt.setProid(proid);
                bdcTszt.setTsr(blry);
                bdcTszt.setTsjdmc(jdmc);
                bdcTszt.setTsqk("ywbh="+ywbh+"&jdmc="+jdmc+"&blry="+blry+"&sqrxm="+sqrxm+"&sqrlxfs="+sqrlxfs);
                String bz = "由于摩柯调用接口未提供返回参数，默认点击评价按钮就表示评价成功";
                insertBdcTszt(bdcTszt,bz);

                StringBuilder stringBuilder=new StringBuilder();
                String mokeUrl=AppConfig.getProperty("mokeUrl");
                if(StringUtils.isNotBlank(mokeUrl)){
                    stringBuilder.append(mokeUrl);
                    stringBuilder.append("?ywbh="+ywbh+"&jdmc="+jdmc+"&blry="+blry+"&sqrxm="+sqrxm+"&sqrlxfs="+sqrlxfs);
                }
                resultUrl = stringBuilder.toString();
                success = "true";
            }
            resultMap.put("url",resultUrl);
            resultMap.put("success",success);
        }
        return resultMap;
    }


    public void insertBdcTszt(BdcTszt bdcTszt,String bz){
        // 推送对象5：评价系统
        bdcTszt.setTsdx(Constants.TSDX_PJXT);
        bdcTszt.setTsxs("1");
        bdcTszt.setTszt("1");
        bdcTszt.setBz(bz);
        bdcTszt.setTssj(new Date());
        bdcTszt.setTsid(UUIDGenerator.generate18());
        entityMapper.saveOrUpdate(bdcTszt,bdcTszt.getTsid());

    }

}
