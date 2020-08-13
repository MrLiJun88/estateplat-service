package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXtOpinionService;
import cn.gtmap.estateplat.server.core.service.SignService;
import cn.gtmap.estateplat.server.model.AutoSignWfNodeName;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadJsonUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/3/5
 */
public class SignServiceImpl implements SignService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 政务平台签名服务
     */
    private SysSignService sysSignService;
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 政务平台任务服务
     */
    private SysTaskService sysTaskService;
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 政务平台工作流实例服务
     */
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;

    private SysUserService sysUserServiceImpl;

    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXtOpinionService bdcXtOpinionService;
    @Autowired
    private BdcXmService bdcXmService;
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 签名map
     */
    private Map<String,String> signKeyMap;

    public SysSignService getSysSignService() {
        return sysSignService;
    }

    public void setSysSignService(SysSignService sysSignService) {
        this.sysSignService = sysSignService;
    }

    public SysTaskService getSysTaskService() {
        return sysTaskService;
    }

    public void setSysTaskService(SysTaskService sysTaskService) {
        this.sysTaskService = sysTaskService;
    }

    public SysWorkFlowInstanceService getSysWorkFlowInstanceService() {
        return sysWorkFlowInstanceService;
    }

    public SysUserService getSysUserServiceImpl() {
        return sysUserServiceImpl;
    }

    public void setSysUserServiceImpl(SysUserService sysUserServiceImpl) {
        this.sysUserServiceImpl = sysUserServiceImpl;
    }

    public void setSysWorkFlowInstanceService(SysWorkFlowInstanceService sysWorkFlowInstanceService) {
        this.sysWorkFlowInstanceService = sysWorkFlowInstanceService;
    }

    public Map<String, String> getSignKeyMap() {
        return signKeyMap;
    }

    public void setSignKeyMap(Map<String, String> signKeyMap) {
        this.signKeyMap = signKeyMap;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 项目ID
     * @param userid 用户ID
     * @param activityid 当前活动ID
     * @param targetActivityDefids 目标活动定义ID
     * @return
     * @description 针对工作流活动取回或退回的签名意见处理
     */
    @Override
    public void handleRetreatSign(final String proid, final String userid, final String activityid, final String targetActivityDefids) {
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        PfActivityVo backActivitys =null;
        if(pfWorkFlowInstanceVo!=null){
            backActivitys = sysTaskService.getActivityBywIdandadId(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
        }
        if(backActivitys!=null && StringUtils.isNotBlank(backActivitys.getActivityName())&&signKeyMap.containsKey(backActivitys.getActivityName())){
            String signKey = signKeyMap.get(backActivitys.getActivityName());
            deleleSign(proid,signKey);
        }
    }

    /**
     * @author <a href="mailto:xuchao@gtmap.cn">xuchao</a>
     * @param proid 项目ID
     * @return
     * @description 针对工作流活动转发，根据配置签名userid,和key ，并自动签名
     */
    @Override
    public String handleTurnAutoSignBySignkeys(final String proid,final String userid) {
        String str = "";
        //获取需要自动签名的Userid
        String autoSignUserId = AppConfig.getProperty("sign.auto.user");
        //获取需要自动签名的signkey,多个值","隔开
        String autoSignKey = AppConfig.getProperty("sign.auto.key");
        if(StringUtils.isNotBlank(autoSignUserId) && StringUtils.isNotBlank(autoSignKey)){
            String[] autoSignUserIdArr = autoSignUserId.split(",");
            //因为最后一个是“，”分割后会少一个，所以判断如果最后一个字符是",",则给autoSignUserIdArr添加一个空值
            if(autoSignUserId.endsWith(",")){
                autoSignUserIdArr = Arrays.copyOf(autoSignUserIdArr, autoSignUserIdArr.length + 1);//数组扩容
                autoSignUserIdArr[autoSignUserIdArr.length-1] = "";
            }
            String[] autoSignKeyArr = autoSignKey.split(",");
            //判断如果autoSignUserIdArr.length != autoSignKeyArr.length，不允许转发，配置错误
            if(autoSignUserIdArr.length!=autoSignKeyArr.length){
                str =  "请检查配置文件，sign.auto.user、sign.auto.key两个值无法匹配！";
            }else{
                for(int i=0;i<autoSignKeyArr.length;i++){
                    String tempUserId = autoSignUserIdArr[i];
                    String tempSignKey = autoSignKeyArr[i];
                    //判断是否已经签名
                    List<PfSignVo> pfSignVoList = sysSignService.getSignList(tempSignKey, proid);
                    if(CollectionUtils.isEmpty(pfSignVoList)){
                        //如果tempUserId="",则取当前转发人id
                        if(StringUtils.isBlank(tempUserId)){
                            tempUserId = userid;
                        }
                        //自动签名
                        PfSignVo pfSignVo= initPfSignVo(proid,tempSignKey,tempUserId);
                        sysSignService.insertAutoSign(pfSignVo);
                    }
                }
            }
        }
        return str;
    }

    @Override
    public void autoSignBeforeTurn(String proid, String userid, String workflowNodeName) {
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(userid)){
            List<AutoSignWfNodeName> autoSignWfNodeNameList = ReadJsonUtil.getWorkFlowNodeName();
            String signKey = null;
            if (CollectionUtils.isNotEmpty(autoSignWfNodeNameList)){
                String[] csNodeNameArray = autoSignWfNodeNameList.get(0).getCsNodeName().split(",");
                if (CommonUtil.indexOfStrs(csNodeNameArray,workflowNodeName)){
                    signKey = "csr";
                }else{
                    String[] fsNodeNameArray = autoSignWfNodeNameList.get(0).getFsNodeName().split(",");
                    if (CommonUtil.indexOfStrs(fsNodeNameArray,workflowNodeName)){
                        signKey="fsr";
                    }else{
                        String[] hdNodeNameArray = autoSignWfNodeNameList.get(0).getHdNodeName().split(",");
                        if (CommonUtil.indexOfStrs(hdNodeNameArray,workflowNodeName)) {
                            signKey="hdr";
                        }
                    }
                }
            }
            List<PfSignVo> pfSignVoList = sysSignService.getSignList(signKey, proid);
            if (CollectionUtils.isEmpty(pfSignVoList)){
                PfSignVo pfSignVo= initPfSignVo(proid,signKey,userid);
                //添加默认opinion
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                    if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())){
                        pfSignVo.setSignOpinion(bdcXtOpinionService.getDefautConfigOpinion(pfWorkFlowInstanceVo.getWorkflowDefinitionId(),workflowNodeName,proid));
                    }
                }
                sysSignService.insertAutoSign(pfSignVo);
            }
        }
     }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 项目ID
     * @param signKey 签名key，多个key以逗号隔开
     * @return
     * @description 根据签名key删除项目的某些签名
     */
    private void deleleSign(String proid,String signKey){
        String signKeysArr[]  = signKey.split(",");
        for (String tmpSignKey:signKeysArr){
            List<PfSignVo> pfSignVoList = sysSignService.getSignList(tmpSignKey, proid);
            if(CollectionUtils.isNotEmpty(pfSignVoList)){
                for(PfSignVo pfSignVo:pfSignVoList){
                    saveSignInfoToSpxx(proid,pfSignVo);
                    sysSignService.deleteSign(pfSignVo.getSignId());
                }
            }
        }
    }

    private PfSignVo initPfSignVo(String proid,String signkey,String singUserid){
        PfSignVo pfSignVo = new PfSignVo();
        pfSignVo.setProId(proid);
        pfSignVo.setSignKey(signkey);
        pfSignVo.setSignId(UUIDGenerator.generate());
        pfSignVo.setSignOpinion("");//目前设置为空，可读取配置
        pfSignVo.setSignType("1");
        pfSignVo.setUserId(singUserid);
        pfSignVo.setSignName(sysUserServiceImpl.getUserVo(singUserid).getUserName());
        pfSignVo.setSignDate(Calendar.getInstance().getTime());
        return pfSignVo;
    }

    /**
      * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
      * @param proid,pfSignVo
      * @description 删除平台的签名记录的时候把签名意见保存到bdc_spxx
     */
    private void saveSignInfoToSpxx(String proid, PfSignVo pfSignVo){
        if(null != pfSignVo && StringUtils.isNotBlank(proid)){
           BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if(null != bdcSpxx && StringUtils.isNotBlank(pfSignVo.getSignKey())){
              if(StringUtils.equals(Constants.SHRLX_CSR_MC,pfSignVo.getSignKey())){
                  bdcSpxx.setCsyj(pfSignVo.getSignOpinion());
              }else if(StringUtils.equals(Constants.SHRLX_FSR_MC,pfSignVo.getSignKey())){
                  bdcSpxx.setFsyj(pfSignVo.getSignOpinion());
              }else if(StringUtils.equals(Constants.SHRLX_HDR_MC,pfSignVo.getSignKey())){
                  bdcSpxx.setHdyj(pfSignVo.getSignOpinion());
              }
            }
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
        }
    }

}
