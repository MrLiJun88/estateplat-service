package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.omp.BdcZzdzjFjxx;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Base64Util;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.FileService;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangwentao@gtmap.cn">zhangwentao</a>
 * @version 1.0, 2018/9/12
 * @description 自助打印证书服务
 */
@Service
public class SelfHelpPrintBdcZsServiceImpl implements SelfHelpPrintBdcZsService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcZsbhService bdcZsbhService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    private BdcSjxxService bdcSjxxService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcXtLogService bdcXtLogService;
    @Autowired
    private FileService fileService;

    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;


    @Override
    public Map checkData(BdcXm bdcXm, BdcZs bdcZs) {
        Map resultMap = null;
        // 验证受理编号和证书的项目受理编号的一致性
        resultMap = checkSlbhAndZs(bdcXm, bdcZs);
        if (null == resultMap) {
            // 验证领证人证件号
            resultMap = checkLzrzjh(bdcXm);
            if (null == resultMap) {
                // 验证证书编号
                resultMap = checkZsbh(bdcZs);
            }
        }
        return resultMap;
    }

    @Override
    public Map savePrintZsFj(BdcZzdzjFjxx bdcZzdzjFjxx) {
        Map map = new HashMap();
        String zsid = bdcZzdzjFjxx.getZsid();
        String dzzp = bdcZzdzjFjxx.getDzzp();
        String smj = bdcZzdzjFjxx.getSmj();

        if (StringUtils.isNotEmpty(zsid) && StringUtils.isNotEmpty(dzzp) && StringUtils.isNotEmpty(smj)) {

            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByZsid(zsid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                String wfProid = bdcXmList.get(0).getProid();
                BdcSjxx bdcSjxx = bdcSjxxService.saveBdcSjxx(wfProid);

                //循环给该流程赋值NodeID
                Space space = fileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF");
                Integer proNodeId = createFileFolderByclmc(space.getId(), wfProid);//流程主Proid对应的文件中心ID

                //创建文件名称ID
                Integer node = createFileFolderByclmc(proNodeId, "其他资料");
                //创建收件材料
                createBdcdjSjcl("其他资料", bdcSjxx, node);
                try {
                    byte[] inputDzzpBytes = Base64Util.decodeBase64StrToByte(dzzp);
                    byte[] inputSmjBytes = Base64Util.decodeBase64StrToByte(smj);
                    //不替换里边的图片
                    fileService.uploadFile(new ByteArrayInputStream(inputDzzpBytes), node, "领取人照片.jpg");
                    fileService.uploadFile(new ByteArrayInputStream(inputSmjBytes), node, "自助打印机扫描件.jpg");
                    map.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
                    map.put(ParamsConstants.CHECKMSG_HUMP, "操作成功");
                } catch (Exception e) {
                    logger.error("自助打证机上传附件异常 /savePrintZsFj", e);
                    map.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                    map.put(ParamsConstants.CHECKMSG_HUMP, "操作失败，附件异常");
                }
                //记录日志
                String parmjson = "自助打证机上传附件操作！项目id:" + wfProid + "证书id：" + zsid;
                BdcXtLog bdcXtLog = new BdcXtLog();
                bdcXtLog.setLogid(UUIDGenerator.generate18());
                bdcXtLog.setUsername(null);
                bdcXtLog.setUserid(null);
                bdcXtLog.setCzrq(new Date());
                bdcXtLog.setController("/selfHelpPrint/savePrintZsFj");
                bdcXtLog.setParmjson(parmjson);
                bdcXtLogService.addLog(bdcXtLog);
            } else {
                map.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                map.put(ParamsConstants.CHECKMSG_HUMP, "未查询到数据");
            }
        } else {
            map.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
            map.put(ParamsConstants.CHECKMSG_HUMP, "打证照片或扫描件为空");
        }
        return map;
    }

    /**
     * @author <a href="mailto:zhangyu@gtmap.cn">zhangyu</a>
     * @description 保存至不动产附件材料
     */

    private BdcSjcl createBdcdjSjcl(String clmc, BdcSjxx bdcSjxx, Integer node) {
        BdcSjcl bdcSjcl = new BdcSjcl();
        bdcSjcl.setSjclid(UUIDGenerator.generate18());
        bdcSjcl.setSjxxid(bdcSjxx.getSjxxid());
        bdcSjcl.setClmc(clmc);
        bdcSjcl.setCllx("2");
        bdcSjcl.setWjzxid(node);
        bdcSjcl.setMrfs(1);
        bdcSjcl.setXh(1);
        return bdcSjcl;
    }

    /**
     * @author <a href="mailto:zhangyu@gtmap.cn">zhangyu</a>
     * @description 文件中心 附件材料
     */
    private Integer createFileFolderByclmc(Integer parentId, String folderNodeName) {
        Node tempNode = null;
        if (StringUtils.isNotBlank(folderNodeName)) {
            try {
                tempNode = fileCenterNodeServiceImpl.getNode(parentId, folderNodeName, true);
            } catch (NodeNotFoundException e) {
                logger.error("附件上传获取tempNode异常！", e);
            }
            if (null != tempNode) {
                return tempNode.getId();
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:zhangwentao@gtmap.cn">zhangwentao</a>
     * @description 验证证书编号
     */
    private Map checkZsbh(BdcZs bdcZs) {
        Map resultMap = new HashMap();
        if (StringUtils.isNotBlank(bdcZs.getZsid()) && StringUtils.isNotBlank(bdcZs.getBh())) {
            BdcZs bdcZsTemp = entityMapper.selectByPrimaryKey(BdcZs.class, bdcZs.getZsid());
            if (null != bdcZsTemp) {
                String zslx = "";
                if (StringUtils.equals(bdcZsTemp.getZstype(), Constants.BDCQZS_BH_FONT)) {
                    zslx = Constants.BDCQZS_BH_DM;
                } else if (StringUtils.equals(bdcZsTemp.getZstype(), Constants.BDCQZM_BH_FONT)) {
                    zslx = Constants.BDCQZM_BH_DM;
                }
                Example example = new Example(BdcZsbh.class);
                example.createCriteria().andEqualTo("zsbh", bdcZs.getBh()).andEqualTo("zslx", zslx).andEqualTo("nf", CalendarUtil.formatYearToStr(bdcZs.getFzrq()));
                List<BdcZsbh> bdcZsbhList = entityMapper.selectByExampleNotNull(example);
                if (CollectionUtils.isNotEmpty(bdcZsbhList) && null != bdcZsbhList.get(0)) {
                    BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                    if (StringUtils.equals(bdcZsbh.getSyqk(), Constants.BDCZSBH_SYQK_WSY)) {
                        updateData(bdcZs, bdcZsTemp, bdcZsbh);

                        resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
                        resultMap.put(ParamsConstants.CHECKMSG_HUMP, "操作成功！");
                    } else {
                        resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                        resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该证书编号不可用！");
                    }
                } else {
                    resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                    resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该证书编号不存在！");
                }
            } else {
                resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                resultMap.put(ParamsConstants.CHECKMSG_HUMP, "该证书不存在！");
            }
        } else {
            resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "证书ID或证书编号为空！");
        }
        return resultMap;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:zhangwentao@gtmap.cn">zhangwentao</a>
     * @description 更新数据
     */
    private void updateData(BdcZs bdcZsParam, BdcZs bdcZs, BdcZsbh bdcZsbh) {
        // 更新证书
        bdcZs.setBh(bdcZsParam.getBh());
        bdcZs.setFzrq(bdcZsParam.getFzrq());
        bdcZs.setLzrq(bdcZsParam.getLzrq());
        bdcZs.setLzrzjh(bdcZsParam.getLzrzjh());
        bdcZs.setDycs(1);
        bdcZs.setDyzt("1");
        entityMapper.updateByPrimaryKeySelective(bdcZs);

        // 更新证书编号
        bdcZsbh.setZsid(bdcZs.getZsid());
        bdcZsbh.setSyqk(Constants.BDCZSBH_SYQK_YSY);
        entityMapper.updateByPrimaryKeySelective(bdcZsbh);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:zhangwentao@gtmap.cn">zhangwentao</a>
     * @description 验证领证人的证件号码是否一致
     */
    private Map checkLzrzjh(BdcXm bdcXm) {
        Map resultMap = new HashMap();
        if (StringUtils.isNotBlank(bdcXm.getBh()) && StringUtils.isNotBlank(bdcXm.getLzrzjh())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(bdcXm.getBh());
            if (CollectionUtils.isNotEmpty(bdcXmList) && null != bdcXmList.get(0)) {
                if (StringUtils.equals(bdcXmList.get(0).getLzrzjh(), bdcXm.getLzrzjh())) {
                    BdcXm bdcXmTemp = bdcXmList.get(0);
                    bdcXmTemp.setLzrq(bdcXm.getLzrq());
                    entityMapper.updateByPrimaryKeySelective(bdcXmTemp);
                    resultMap = null;
                } else {
                    resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                    resultMap.put(ParamsConstants.CHECKMSG_HUMP, "领证人证件号码不一致！");
                }
            } else {
                resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                resultMap.put(ParamsConstants.CHECKMSG_HUMP, "受理编号有误！");
            }
        } else {
            resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "受理编号或领证人证件号为空！");
        }
        return resultMap;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:zhangwentao@gtmap.cn">zhangwentao</a>
     * @description 验证受理编号是不是证书的受理编号
     */
    private Map checkSlbhAndZs(BdcXm bdcXm, BdcZs bdcZs) {
        Map resultMap = new HashMap();
        if (StringUtils.isNotBlank(bdcXm.getBh()) && StringUtils.isNotBlank(bdcZs.getZsid())) {
            String proid = bdcXmZsRelService.getProidByZsid(bdcZs.getZsid());
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
                if (null != bdcXmTemp && StringUtils.equals(bdcXmTemp.getBh(), bdcXm.getBh())) {
                    resultMap = null;
                } else {
                    resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                    resultMap.put(ParamsConstants.CHECKMSG_HUMP, "受理编号与证书不对应！");
                }
            } else {
                resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
                resultMap.put(ParamsConstants.CHECKMSG_HUMP, "证书ID有误！");
            }
        } else {
            resultMap.put(ParamsConstants.INFO_LOWERCASE, ParamsConstants.FALSE_LOWERCASE);
            resultMap.put(ParamsConstants.CHECKMSG_HUMP, "受理编号或证书ID为空！");
        }
        return resultMap;
    }
}
