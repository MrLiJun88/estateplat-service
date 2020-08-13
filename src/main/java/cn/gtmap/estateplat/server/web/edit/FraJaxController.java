package cn.gtmap.estateplat.server.web.edit;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcXmMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcXtYhMapper;
import cn.gtmap.estateplat.server.core.mapper.GdFwMapper;
import cn.gtmap.estateplat.server.core.mapper.GdTdMapper;
import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.service.ProjectCheckInfoService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.service.config.RedundantFieldService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-3-23
 * Time: 下午12:28
 * To change this template use File | Settings | File Templates.
 * dos:用于处理帆软报表Ajax请求
 */
@Controller
@RequestMapping("/fraJax")
public class FraJaxController extends BaseController {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcZdQllxService bdcZdQllxService;
    @Autowired
    BdcXmMapper bdcXmMapper;
    @Autowired
    GdFwMapper gdFwMapper;
    @Autowired
    GdTdMapper gdTdMapper;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcZsbhService bdcZsbhService;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    BdcSpxxService bdcSpxxService;

    @Autowired
    BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    ProjectCheckInfoService projectCheckInfoService;
    @Autowired
    ZmsFjService zmsFjService;
    @Autowired
    BdcXtQlqtzkConfigService bdcXtQlqtzkConfigService;
    @Autowired
    BdcXtLimitfieldService bdcXtLimitfieldService;
    @Autowired
    GdqlService gdqlService;
    @Autowired
    BdcXtYhMapper bdcXtYhMapper;

    @Autowired
    private BdcdyService bdcdyService;

    @Autowired
    private BdcTdService bdcTdService;

    @Autowired
    private BdcXmRelService bdcXmRelService;

    @Autowired
    private GdSaveLogSecvice gdSaveLogSecvice;

    @Autowired
    private BdcCfService bdcCfService;

    @Autowired
    @Resource(name = "creatProjectDefaultService")
    CreatProjectService creatProjectService;

    @Autowired
    private BdcDyaqService bdcDyaqService;

    @Resource(name = "delProjectDyBgForZjgcServiceImpl")
    private DelProjectService delProjectDyBgForZjgcServiceImpl;

    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;

    @Autowired
    private BdcSjdService bdcSjdService;

    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;

    @Autowired
    private BdcFwFsssService bdcFwFsssService;

    @Autowired
    BdcXymxService bdcXymxService;
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private RedundantFieldService redundantFieldService;
    @Autowired
    private BdcTdsyqService bdcTdsyqService;

    /**
     * 收件单如果没有编号，给他注入编号
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editbh", method = RequestMethod.GET)
    public String editbh(@RequestParam(value = "proid", required = false) String proid) {
        String xmbh = "";
        BdcXm bdcXm = new BdcXm();
        bdcXm.setDwdm(super.getWhereXzqdm());
        xmbh = bdcXmService.creatXmbh(bdcXm);
        return xmbh;
    }

    @ResponseBody
    @RequestMapping(value = "/savaFj", method = RequestMethod.POST)
    public String savaFj(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "fj", required = false) String fj) {
        String msg = "失败";
        BdcXm bdcXm = null;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if (bdcXm != null) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
            if (CollectionUtils.isNotEmpty(qllxVos)) {
                qllxVo = qllxVos.get(0);
                qllxVo.setFj(fj);
                entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
            }
            msg = "成功";
        }
        return msg;
    }


    /**
     * 发证记录获取备注
     *
     * @param wiid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBz", method = RequestMethod.POST)
    public String getBz(@RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "isSingel", required = false) String isSingel) {
        StringBuilder bz = new StringBuilder();
        List<BdcXm> bdcXmLst = null;
        if (StringUtils.isNotBlank(wiid)) {
            bdcXmLst = bdcXmService.getBdcXmListByWiid(wiid);
        } else if (StringUtils.isNotBlank(proid)) {
            bdcXmLst = bdcXmService.getBdcXmListByProid(proid);
        }
        if (CollectionUtils.isNotEmpty(bdcXmLst) && StringUtils.isNotBlank(bdcXmLst.get(0).getBz())) {
            String[] bz1 = bdcXmLst.get(0).getBz().split(" ");
            bz.append(bz1[0]);
        }
        int zsCount = 0;
        int zmCount = 0;
        Map<String, BdcZs> BdcZsMap = new HashMap<String, BdcZs>();
        for (BdcXm bdcXm : bdcXmLst) {
            List<BdcZs> bdcZsLst = bdcZsService.getPlZsByProid(bdcXm.getProid());
            for (BdcZs bdcZs : bdcZsLst) {
                String bdcqzh = bdcZs.getBdcqzh();
                if (!BdcZsMap.containsKey(bdcqzh)) {
                    if (StringUtils.equals(bdcZs.getZstype(), Constants.BDCQZS_BH_FONT)) {
                        zsCount++;
                    } else {
                        zmCount++;
                    }
                    BdcZsMap.put(bdcqzh, bdcZs);
                }
            }
        }
        //合并登记的发证记录只显示当前项目生成证书备注
        if (StringUtils.isNotBlank(isSingel) && StringUtils.equals(isSingel, "true")) {
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
            BdcZs bdcZs = null;
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                bdcZs = bdcZsList.get(0);
            }
            if (bdcZs != null) {
                String zsType = bdcZs.getZstype();
                if (StringUtils.equals(zsType, Constants.BDCQZS_BH_FONT)) {
                    zsCount = bdcZsList.size();
                    bz.append("  不动产权证书:").append(zsCount).append("本;");
                } else if (StringUtils.equals(zsType, Constants.BDCQZM_BH_FONT)) {
                    zmCount = bdcZsList.size();
                    bz.append("  不动产登记证明:").append(zmCount).append("本;");
                }
            }
        } else {
            if (zsCount > 0 && zmCount == 0) {
                bz.append("  不动产权证书:").append(zsCount).append("本;");
            }
            if (zmCount > 0 && zsCount == 0) {
                bz.append("  不动产登记证明:").append(zmCount).append("本;");
                if (CollectionUtils.isNotEmpty(bdcXmLst) && StringUtils.isNotBlank(bdcXmLst.get(0).getSqlx()) && (StringUtils.equals(bdcXmLst.get(0).getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXmLst.get(0).getSqlx(), Constants.SQLX_SPFXZBG_DM))) {
                    bz.delete(0, bz.length());
                    bz.append("  首次登记信息表:").append(zmCount).append("本;");
                }
            }
            if (zsCount > 0 && zmCount > 0) {
                bz.append("  不动产权证书:").append(zsCount).append("本;不动产登记证明:").append(zmCount).append("本;");
                if (CollectionUtils.isNotEmpty(bdcXmLst) && StringUtils.isNotBlank(bdcXmLst.get(0).getSqlx()) && StringUtils.equals(bdcXmLst.get(0).getSqlx(), Constants.SQLX_SPFGYSCDJ_DM)) {
                    bz.delete(0, bz.length());
                    bz.append("  不动产权证书:").append(zsCount).append("本;首次登记信息表:").append(zmCount).append("本;");
                }
            }
        }

        return bz.toString();
    }


    @ResponseBody
    @RequestMapping(value = "/saveGyqktoFj", method = RequestMethod.POST)
    public String saveGyqktoFj(@RequestParam(value = "proid", required = false) String proid) {
        String msg = "失败";
        StringBuilder fj = new StringBuilder();
        String bz = "按份共有";
        BdcXm bdcXm = null;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if (bdcXm != null) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            String gyqk = "按份共有：" + bdcQlrService.getGyqk(proid);
            List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
            if (CollectionUtils.isNotEmpty(qllxVos)) {
                for (int i = 0; i != qllxVos.size(); i++) {
                    qllxVo = qllxVos.get(i);
                    //防止附记重复取值
                    if (StringUtils.isNotBlank(qllxVo.getFj())) {
                        if (qllxVo.getFj().indexOf(bz) > -1) {
                            String[] qk = qllxVo.getFj().split("\\n");
                            String scfj = "";
                            if (qk[0].indexOf(bz) > -1) {
                                scfj = gyqk;
                            } else {
                                scfj = qk[0];
                            }
                            fj = new StringBuilder(scfj);
                            for (int j = 1; j != qk.length; j++) {
                                if (qk[j].indexOf(bz) > -1) {
                                    scfj = gyqk;
                                } else {
                                    scfj = qk[j];
                                }
                                fj.append("\n").append(scfj);
                            }
                        } else {
                            fj = new StringBuilder(qllxVo.getFj());
                            fj.append("\n").append(gyqk);
                        }
                    } else {
                        fj = new StringBuilder(gyqk);
                    }
                    qllxVo.setFj(fj.toString());
                    entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
                }
            }
            msg = "成功";
        }
        return msg;
    }


    @ResponseBody
    @RequestMapping(value = "/getZsQt", method = RequestMethod.POST)
    public String getZsQt(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "gdid", required = false) String gdid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "qllx", required = false) String qllx) {
        String msg = "失败";
        BdcXm bdcXm = null;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if (bdcXm != null && bdcXm.getSqlx() != null) {
            //获取权利
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            BdcXtQlqtzkConfig bdcXtQlqtzkConfig = new BdcXtQlqtzkConfig();
            bdcXtQlqtzkConfig.setSqlxdm(bdcXm.getSqlx());
            qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());

            //房地产权添加fwlx过滤
            if (qllxVo != null) {
                if (qllxVo instanceof BdcFdcq) {
                    bdcXtQlqtzkConfig.setQllxzlx(((BdcFdcq) qllxVo).getFwlx().toString());
                }
                if (qllxVo instanceof BdcFdcqDz) {
                    bdcXtQlqtzkConfig.setQllxzlx(((BdcFdcqDz) qllxVo).getFwlx().toString());
                }
            }
            //获取模板
            List<BdcXtQlqtzkConfig> listQlqtzk = bdcXtQlqtzkConfigService.getQlqtzk(bdcXtQlqtzkConfig);
            if (CollectionUtils.isNotEmpty(listQlqtzk)) {
                bdcXtQlqtzkConfig = listQlqtzk.get(0);
                //将权利其它状况内容写入
                msg = bdcXtQlqtzkConfigService.replaceMb(bdcXtQlqtzkConfig.getQlqtzkmb(), bdcXtQlqtzkConfig.getQtdb(), bdcXm, null);
            }
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/getUUID", method = RequestMethod.GET)
    public String getUUID() {
        return UUIDGenerator.generate18();
    }

    @ResponseBody
    @RequestMapping(value = "/savaQlxxGyqk")
    public String savaQlxxGyqk(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "gyqk", required = false) String gyqk) {
        String msg = "";
        BdcXm bdcxm = bdcXmService.getBdcXmByProid(proid);
        if (bdcxm != null) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcxm);
            HashMap map = new HashMap();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
            if (CollectionUtils.isNotEmpty(qllxVos)) {
                qllxVo = qllxVos.get(0);
                qllxVo.setGyqk(gyqk);
                if (!gyqk.equals("2") && !gyqk.equals("1")) {
                    //lx 共有情况为空时，默认为单独所有
                    if (StringUtils.isBlank(gyqk)) {
                        gyqk = "0";
                    }
                    qllxVo.setGyqk(gyqk);
                    String[] fjs = StringUtils.split(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(qllxVo.getFj()), "\\n");
                    StringBuilder newFj = new StringBuilder();
                    if (fjs.length > 0) {
                        for (String fj : fjs) {
                            if (!(fj.indexOf("按份共有") > -1)) {
                                newFj.append(fj);
                            }
                        }
                    }
                    qllxVo.setFj(newFj.toString());
                } else if (gyqk.equals("2")) {
                    StringBuilder fj = new StringBuilder();
                    String bz = "按份共有";
                    BdcXm bdcXm = null;
                    if (StringUtils.isNotBlank(proid)) {
                        bdcXm = bdcXmService.getBdcXmByProid(proid);
                    }
                    if (bdcXm != null) {
                        map.put(ParamsConstants.PROID_LOWERCASE, proid);
                        String gyqkfj = "";
                        if (StringUtils.isNotEmpty(bdcQlrService.getGyqk(proid))) {
                            gyqkfj = "按份共有：" + bdcQlrService.getGyqk(proid);
                        }
                        if (CollectionUtils.isNotEmpty(qllxVos)) {
                            for (int i = 0; i != qllxVos.size(); i++) {
                                qllxVo = qllxVos.get(i);
                                //防止附记重复取值
                                if (StringUtils.isNotBlank(qllxVo.getFj())) {
                                    String[] qk = qllxVo.getFj().split("\\n");
                                    for (int j = 0; j < qk.length; j++) {
                                        String scfj = gyqkfj;
                                        if (qk[j].indexOf(bz) > -1) {

                                        } else if (qk[j].indexOf("共同共有") > -1) {

                                        } else {
                                            scfj = qk[j];
                                        }
                                        if (StringUtils.isBlank(fj)) {
                                            fj = new StringBuilder(scfj);
                                        } else {
                                            fj.append("\n").append(scfj);
                                        }
                                    }
                                } else {
                                    fj = new StringBuilder(gyqkfj);
                                }
                                qllxVo.setFj(fj.toString());
                            }
                        }
                    }

                } else if (gyqk.equals("1")) {
                    StringBuilder fj = new StringBuilder();
                    String bz = "共同共有";
                    BdcXm bdcXm = null;
                    if (StringUtils.isNotBlank(proid)) {
                        bdcXm = bdcXmService.getBdcXmByProid(proid);
                    }
                    if (bdcXm != null) {
                        map.put(ParamsConstants.PROID_LOWERCASE, proid);
                        String gyqkfj = "";
                        if (StringUtils.isNotEmpty(bdcQlrService.getGyqk(proid))) {
                            gyqkfj = "共同共有：" + bdcQlrService.getGyqk(proid);
                        }
                        if (CollectionUtils.isNotEmpty(qllxVos)) {
                            for (int i = 0; i != qllxVos.size(); i++) {
                                qllxVo = qllxVos.get(i);
                                //防止附记重复取值
                                if (StringUtils.isNotBlank(qllxVo.getFj())) {
                                    String[] qk = qllxVo.getFj().split("\\n");
                                    for (int j = 0; j < qk.length; j++) {
                                        String scfj = gyqkfj;
                                        if (qk[j].indexOf(bz) > -1) {

                                        } else if (qk[j].indexOf("按份共有") > -1) {

                                        } else {
                                            scfj = qk[j];
                                        }
                                        if (StringUtils.isBlank(fj)) {
                                            fj = new StringBuilder(scfj);
                                        } else {
                                            fj.append("\n").append(scfj);
                                        }
                                    }
                                } else {
                                    fj = new StringBuilder(gyqkfj);
                                }
                                qllxVo.setFj(fj.toString());
                            }
                        }
                    }
                }
                entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
            }
            msg = "成功";
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/saveDjsy", method = RequestMethod.POST)
    public String saveDjsy(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "djsy", required = false) String djsy) {
        String msg = "";
        bdcXmService.saveDjsy(djsy, proid);
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/readDjsy", method = RequestMethod.POST)
    public HashMap readDjsy(@RequestParam(value = "djsy", required = false) String djsy) {
        HashMap map = Maps.newHashMap();
        String msg = "";
        if (StringUtils.isNotBlank(djsy)) {
            String[] newdjsy = djsy.split("/");
            if (newdjsy.length > 0) {
                String djsydmOne = bdcXmMapper.getDjsyZsmcByDm(newdjsy[0]);
                if (newdjsy.length > 1) {
                    String djsydmTwo = bdcXmMapper.getDjsyZsmcByDm(newdjsy[1]);
                    msg = djsydmOne + "/" + djsydmTwo;
                } else {
                    msg = djsydmOne;
                }
            }
        }
        map.put("msg", msg);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getYj", method = RequestMethod.GET)
    public Map<String, Object> getYj() {
        String bfbl = AppConfig.getProperty("bfbl");
        String sybl = AppConfig.getProperty("sybl");
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(sybl) && StringUtils.isNotBlank(bfbl)) {
            Double bfblD = Double.parseDouble(bfbl);
            Double syblD = Double.parseDouble(sybl);
            map = projectCheckInfoService.checkBdcdyZsbhsl(syblD, bfblD);
            List<String> list = (List<String>) map.get("info");
            if (CollectionUtils.isNotEmpty(list)) {
                map.put("url", bdcdjUrl + "/zsBhGl");
            }
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getBank", method = RequestMethod.POST)
    public HashMap getBank(@RequestParam(value = "bankmc", required = false) String bankmc, @RequestParam(value = "zjlx", required = false) String zjlx) {
        HashMap result = new HashMap();
        Example example = new Example(BdcXtYh.class);
        Example.Criteria criteria = example.createCriteria();
        List<BdcXtYh> bdcBanks = new ArrayList<BdcXtYh>();
        if (StringUtils.isNotBlank(bankmc)) {
            criteria.andEqualTo("yhmc", bankmc);
            if (StringUtils.isNotBlank(zjlx)) {
                criteria.andEqualTo("zjlx", zjlx);
            }
            bdcBanks = entityMapper.selectByExample(example);
        }

        if (CollectionUtils.isNotEmpty(bdcBanks)) {
            BdcXtYh bdcBank = bdcBanks.get(0);
            result.put("yhid", bdcBank.getYhid());
            result.put("zjlx", bdcBank.getZjlx());
            result.put("zjbh", bdcBank.getZjbh());
        }
        if (result.size() > 0) {
            return result;
        } else {
            return null;
        }
    }


    /**
     * 获取企业证件类型和证件编号
     *
     * @param
     * @return zjlx zjbh
     * @author wuhongrui
     * @description
     */
    @ResponseBody
    @RequestMapping(value = "/getCompany", method = RequestMethod.POST)
    public HashMap getCompany(@RequestParam(value = "qyid", required = false) String qyid) {
        HashMap result = new HashMap();
        Example example = new Example(BdcXtQy.class);
        Example.Criteria criteria = example.createCriteria();
        List<BdcXtQy> bdcQys = new ArrayList<BdcXtQy>();
        if (StringUtils.isNotBlank(qyid)) {
            criteria.andEqualTo("qyid", qyid);

            bdcQys = entityMapper.selectByExample(example);
        }

        if (CollectionUtils.isNotEmpty(bdcQys)) {
            BdcXtQy bdcQy = bdcQys.get(0);
            result.put("qyid", bdcQy.getQyid());
            result.put("zjlx", bdcQy.getZjlx());
            result.put("zjbh", bdcQy.getZjbh());
        }
        if (result.size() > 0) {
            return result;
        } else {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/saveQlrxz", method = RequestMethod.POST)
    public void saveQlrxz(@RequestParam(value = "proid", required = false) String proid) {
        List<BdcQlr> list = bdcQlrService.setQlrxzByZjlx(proid);
        for (BdcQlr bdcQlr : list) {
            entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
        }
    }

    /**
     * hqz 保存spxx宗地宗海用途
     *
     * @param proid
     * @param zdzhyt
     * @param zdzhyt2
     * @param zdzhyt3
     */
    @ResponseBody
    @RequestMapping(value = "/saveZdzhYt", method = RequestMethod.POST)
    public void saveZdzhYt(@RequestParam(value = "proid", required = false) String proid, String zdzhyt, String zdzhyt2, String zdzhyt3) {
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            bdcSpxx.setZdzhyt(zdzhyt);
            bdcSpxx.setZdzhyt2(zdzhyt2);
            bdcSpxx.setZdzhyt3(zdzhyt3);
            entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
        }
    }


    @ResponseBody
    @RequestMapping(value = "/checkZsbh", method = RequestMethod.GET)
    public String checkZsbh(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "zslx", required = false) String zslx, @RequestParam(value = "zsbh", required = false) String zsbh, @RequestParam(value = "zsid", required = false) String zsid) {
        String lqr = super.getUserName();
        String lqrid = super.getUserId();
        if (StringUtils.isNotBlank(proid)) {
            String xx = bdcZsbhService.checkZsbh(proid, zslx, zsbh, lqr, lqrid, zsid);
            //检查完后保存证书编号
            if (StringUtils.equals(xx, ParamsConstants.SUCCESS_LOWERCASE) && StringUtils.isNotBlank(zsid)) {
                BdcZs bdcZs = entityMapper.selectByPrimaryKey(BdcZs.class, zsid);
                if (bdcZs != null) {
                    bdcZs.setBh(zsbh);
                    entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                }
            }
            if (StringUtils.isBlank(xx)) {
                return ParamsConstants.SUCCESS_LOWERCASE;
            } else {
                return xx;
            }
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "/checkAllZsbh", method = RequestMethod.GET)
    public String checkAllZsbh(@RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "zslx", required = false) String zslx) {
        String lqr = super.getUserName();
        String lqrid = super.getUserId();
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcZs> bdcZsList = bdcZsService.getPlZsByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    //jyl 只要取一个proid用来取值就好了。
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByZsid(bdcZs.getZsid());
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        String xx = bdcZsbhService.checkZsbh(bdcXmList.get(0).getProid(), zslx, bdcZs.getBh(), lqr, lqrid, bdcZs.getZsid());
                        if (StringUtils.isBlank(xx)) {
                            return ParamsConstants.SUCCESS_LOWERCASE;
                        } else {
                            xx = "不动产证号为" + bdcZs.getBdcqzh() + "存在错误，错误原因是" + xx;
                            return xx;
                        }
                    }
                }
            }
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "/getZsQt", method = RequestMethod.GET)
    public HashMap getZsQt(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qlid", required = false) String qlid) {
        HashMap map = new HashMap();
        String qlqtqk = "";
        String fj = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcDyZs bdcDyZs = bdcZsService.getQtqkforView(proid);
            if (bdcDyZs != null) {
                qlqtqk = bdcDyZs.getQlqtzk();
            }
            fj = bdcZsService.getZsFjView(proid);

        }
        map.put("qlqtqk", qlqtqk);
        map.put("fj", fj);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getSyqx", method = RequestMethod.POST)
    public HashMap getSyqx(@RequestParam(value = "proid", required = false) String proid) {
        HashMap map = new HashMap();
        String fj = "";
        boolean isxjfj = false;//是否详见附记
        boolean isetFj = false;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcxm = bdcXmService.getBdcXmByProid(proid);
            String bdclx = bdcxm.getBdclx();
            if (StringUtils.isNotBlank(bdclx) && bdclx.equals(Constants.BDCLX_TD)) {
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                Map queryMap = new HashMap();
                queryMap.put(ParamsConstants.PROID_LOWERCASE, proid);
                BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqService.getBdcJsydzjdsyq(queryMap);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
                String zdzhyt = bdcSpxx.getZdzhyt();
                String zdzhyt2 = bdcSpxx.getZdzhyt2();
                String zdzhyt3 = bdcSpxx.getZdzhyt3();
                fj = bdcZsService.getZsFjView(proid);
                if ((StringUtils.isNotBlank(zdzhyt2) || StringUtils.isNotBlank(zdzhyt3))) {
                    isxjfj = true;
                    if (!fj.contains("土地用途为")) {//fj内容暂时只能用这个判断
                        isetFj = true;
                        String newFj = "";
                        String kssyrq = "";
                        Date ksrq;
                        Date jsrq;
                        String jssyrq = "";
                        String zdyt = "";
                        if (StringUtils.isNotBlank(zdzhyt)) {
                            Map yt = bdcZdGlService.getZdytByDm(zdzhyt);
                            if (null != yt) {
                                zdyt = (String) yt.get("MC");
                            }
                            ksrq = bdcJsydzjdsyq.getSyksqx();
                            jsrq = bdcJsydzjdsyq.getSyjsqx();
                            if (null != ksrq) {
                                kssyrq = sf.format(ksrq);
                            }
                            if (null != jsrq) {
                                jssyrq = sf.format(jsrq);
                            }
                            newFj = "土地用途为:" + zdyt + "的使用期限为:" + kssyrq + "起" + jssyrq + "止\n";
                            fj = fj + " " + newFj;
                        }
                        if (StringUtils.isNotBlank(zdzhyt2)) {
                            Map yt = bdcZdGlService.getZdytByDm(zdzhyt2);
                            if (null != yt) {
                                zdyt = (String) yt.get("MC");
                            }
                            ksrq = bdcJsydzjdsyq.getSyksqx2();
                            jsrq = bdcJsydzjdsyq.getSyjsqx2();
                            if (null != ksrq) {
                                kssyrq = sf.format(ksrq);
                            }
                            if (null != jsrq) {
                                jssyrq = sf.format(jsrq);
                            }
                            newFj = "土地用途为:" + zdyt + "的使用期限是:" + kssyrq + "起" + jssyrq + "止\n";
                            fj = fj + " " + newFj;
                        }
                        if (StringUtils.isNotBlank(zdzhyt3)) {
                            Map yt = bdcZdGlService.getZdytByDm(zdzhyt3);
                            if (null != yt) {
                                zdyt = (String) yt.get("MC");
                            }
                            ksrq = bdcJsydzjdsyq.getSyksqx3();
                            jsrq = bdcJsydzjdsyq.getSyjsqx3();
                            if (null != ksrq) {
                                kssyrq = sf.format(ksrq);
                            }
                            if (null != jsrq) {
                                jssyrq = sf.format(jsrq);
                            }
                            newFj = "土地用途为:" + zdyt + "的使用期限是:" + kssyrq + "起" + jssyrq + "止\n";
                            fj = fj + " " + newFj;
                        }
                    }
                }
            }

        }
        map.put("isetFj", isetFj);
        map.put("isxjfj", isxjfj);
        map.put("fj", fj);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getZmsFj")
    public void getZmsFj(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qllx", required = false) String qllx) {
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = null;
            if (StringUtils.isNotBlank(proid)) {
                bdcXm = bdcXmService.getBdcXmByProid(proid);
            }
            if (bdcXm != null && bdcXm.getSqlx() != null) {
                //获取权利
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                BdcXtQlqtzkConfig bdcXtQlqtzkConfig = new BdcXtQlqtzkConfig();
                bdcXtQlqtzkConfig.setSqlxdm(bdcXm.getSqlx());
                List<QllxVo> list = null;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(bdcXm.getProid())) {
                    qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());

                }
                BdcBdcdy bdcBdcdy = null;
                if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                    bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                }
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyfwlx())) {
                    //房地产权添加fwlx过滤
                    bdcXtQlqtzkConfig.setQllxzlx(bdcBdcdy.getBdcdyfwlx());
                }


                //获取模板
                List<BdcXtQlqtzkConfig> listQlqtzk = bdcXtQlqtzkConfigService.getQlqtzk(bdcXtQlqtzkConfig);
                if (CollectionUtils.isNotEmpty(listQlqtzk)) {
                    bdcXtQlqtzkConfig = listQlqtzk.get(0);

                    //获取权利类型 将附记内容写入
                    if (qllxVo != null) {
                        qllxVo.setFj(bdcXtQlqtzkConfigService.replaceMb(bdcXtQlqtzkConfig.getFjmb(), bdcXtQlqtzkConfig.getFjdb(), bdcXm, bdcBdcdy));
                        entityMapper.updateByPrimaryKeySelective(qllxVo);
                    }
                }
            }
        }
    }


    @ResponseBody
    @RequestMapping("/gdValidateColor")
    public HashMap getFRValidateColor(@RequestParam(value = "cptName", required = false) String cptName) {
        List<BdcXtLimitfieldofGd> list = new ArrayList<BdcXtLimitfieldofGd>();
        if (StringUtils.isNotBlank(cptName)) {
            list = bdcXtLimitfieldService.getLimitfieldOfGd(cptName);
        }
        HashMap result = new HashMap();
        String color = PlatformUtil.initOptProperties(AppConfig.getProperty("validate.bgcolor"));
        if (!org.apache.commons.lang3.StringUtils.isNotBlank(color)) {
            color = "#fcf7c7";
        }
        result.put("results", list);
        result.put("color", color);
        return result;
    }


    /**
     * lj 用proid获取当前项目所有的proid，适用于合并流程
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("getProidList")
    public String[] getProidList(String proid) {
        String[] proidList = null;
        String proids = bdcXmService.getProidsByProid(proid);
        if (StringUtils.isNotBlank(proids)) {
            proidList = StringUtils.split(proids, Constants.SPLIT_STR);
        }
        return proidList;
    }

    /**
     * jyl 用wiid和qllx获取当前项目所有同qllx的wiid
     *
     * @param wiid
     * @param qllx
     * @return
     */
    @ResponseBody
    @RequestMapping("getProidListByQllxAndWiid")
    public String[] getProidListByQllxAndWiid(String wiid, String qllx) {
        String[] proidList = null;
        String proids = bdcXmService.getProidsByQllxAndWiid(wiid, qllx);
        if (StringUtils.isNotBlank(proids)) {
            proidList = StringUtils.split(proids, Constants.SPLIT_STR);
        }
        return proidList;
    }

    /**
     * @param qlid
     * @param bdcid
     * @return
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 保存新增的房屋时直接关联权利
     */
    @ResponseBody
    @RequestMapping("glql")
    public String glql(String qlid, String bdcid, String bdclx) {
        String msg = "失败";
        if (StringUtils.isNotBlank(qlid) && StringUtils.isNotBlank(bdcid)) {
            Example tdsyqExample = new Example(GdTdsyq.class);
            tdsyqExample.createCriteria().andEqualTo("qlid", qlid);
            List<GdTdsyq> gdTdsyqList = entityMapper.selectByExample(GdTdsyq.class, tdsyqExample);
            if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                gdTdsyq.setIszx(0);
                gdTdsyq.setTdid(bdcid);
                entityMapper.saveOrUpdate(gdTdsyq, gdTdsyq.getQlid());
                gdFwService.changeGdqlztByQlid(gdTdsyq.getQlid(), Constants.QLLX_QSZT_LS.toString());
            }
            Example tdqlExample = new Example(GdTdQl.class);
            tdqlExample.createCriteria().andEqualTo("qlid", qlid);
            List<GdTdQl> gdTdqlList = entityMapper.selectByExample(GdTdQl.class, tdqlExample);
            if (CollectionUtils.isNotEmpty(gdTdqlList)) {
                GdTdQl gdTdQl = gdTdqlList.get(0);
                gdTdQl.setTdid(bdcid);
                entityMapper.saveOrUpdate(gdTdQl, gdTdQl.getQlid());
            }
            Example example = new Example(GdBdcQlRel.class);
            example.createCriteria().andEqualTo("qlid", qlid).andEqualTo("bdcid", bdcid);
            List<GdBdcQlRel> bdcQlRelList = entityMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(bdcQlRelList)) {
                GdBdcQlRel gdBdcQlRel = new GdBdcQlRel();
                gdBdcQlRel.setRelid(UUIDGenerator.generate18());
                gdBdcQlRel.setBdcid(bdcid);
                gdBdcQlRel.setQlid(qlid);
                gdBdcQlRel.setBdclx(bdclx);
                entityMapper.saveOrUpdate(gdBdcQlRel, gdBdcQlRel.getRelid());
            }
            gdqlService.saveFwzlByQlid(qlid);
            gdqlService.saveTdzlByQlid(qlid);
            msg = "成功";
        }
        return msg;
    }

    /**
     * @param proid
     * @return
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 多项目流程，每一个proid都保存一份权利人
     */
    @ResponseBody
    @RequestMapping("saveQlrForEveryProid")
    public void saveQlrForEveryProid(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            String[] proids = getProidList(proid);
            //排除当前proid的数据
            List<String> proidList = new ArrayList<String>();
            for (String tempProid : proids) {
                if (!StringUtils.equals(proid, tempProid)) {
                    proidList.add(tempProid);
                }
            }
            bdcQlrService.saveQlrsByProidsAndQlrlx(bdcQlrList, proidList, Constants.QLRLX_QLR);
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            if (!StringUtils.equals(Constants.DJLX_PLDY_YBZS_SQLXDM, bdcXm.getSqlx()) && !StringUtils.equals(Constants.SQLX_PLDYZX, bdcXm.getSqlx())) {
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
                bdcQlrService.saveQlrsByProidsAndQlrlx(bdcYwrList, proidList, Constants.QLRLX_YWR);
            }
        }
    }

    /**
     * @param proid
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 在建工程多抵多，每一个proid都保存一份债权日期
     */
    @ResponseBody
    @RequestMapping("saveZqrqForEveryProid")
    public void saveZqrqForEveryProid(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(proid);
            if (bdcDyaq != null) {
                String[] proids = getProidList(proid);
                //排除当前proid的数据
                List<String> proidList = new ArrayList<String>();
                for (String tempProid : proids) {
                    if (!StringUtils.equals(proid, tempProid)) {
                        proidList.add(tempProid);
                    }
                }
                bdcDyaqService.saveZqqxForEveryPoid(proidList, bdcDyaq);
            }
        }
    }


    @ResponseBody
    @RequestMapping("saveQlrInSameSqlx")
    public void saveQlrInSameSqlx(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            String[] proids = getProidList(proid);
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            List<String> proidList = new ArrayList<String>();
            for (String tempProid : proids) {
                BdcXm tempBdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, tempProid);
                if (!StringUtils.equals(proid, tempProid) && StringUtils.equals(bdcXm.getSqlx(), tempBdcXm.getSqlx())) {
                    proidList.add(tempProid);
                }
            }
            bdcQlrService.saveQlrsByProidsAndQlrlx(bdcQlrList, proidList, Constants.QLRLX_QLR);
        }
    }

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 修改宗地类别
     */
    @ResponseBody
    @RequestMapping("changeMjByZdlb")
    public String changeMjByZdlb(@RequestParam(value = "proid", required = true) String proid, @RequestParam(value = "mj", required = true) String mj) {
        String bdclx = "";
        String zdLb = bdcTdService.getZdLb(proid);
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            bdclx = bdcXm.getBdclx();
        }
        String zdMj = "";
        //jyl 南通市证书面积显示方式太特殊
        if (dwdm.equals("320600") || dwdm.equals("320683")) {
            zdMj = bdcTdService.changeMjByNt(mj, bdclx, proid);
        } else {
            zdMj = bdcTdService.changeMjByZdLb(zdLb, mj, bdclx);
        }
        return zdMj;
    }

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 苏州检查查封期限规则
     */
    @ResponseBody
    @RequestMapping(value = "/checkCfqx")
    public String checkCfqx(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "days", required = false) int days, @RequestParam(value = "djzx", required = false) String djzx) {
        String msg = "true";
        if (StringUtils.equals(djzx, Constants.DJZX_CF)) {
            if (days > 1096) {
                msg = "查封期限不能超过三年";
            }
        } else if (StringUtils.equals(djzx, Constants.DJZX_XF)) {
            List<BdcCf> bdcCfList = new ArrayList<BdcCf>();
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                bdcCfList = bdcCfService.queryCfByBdcdyid(bdcXm.getBdcdyid());
            }
            List<String> yProidList = bdcXmRelService.getYproid(proid);
            if (CollectionUtils.isNotEmpty(yProidList)) {
                for (String yProid : yProidList) {
                    Example example = new Example(BdcCf.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, yProid);
                    List<GdCf> gdCfList = entityMapper.selectByExample(GdCf.class, example);
                    if (CollectionUtils.isNotEmpty(bdcCfList)) {
                        BdcCf bdcCf = bdcCfList.get(0);
                        if (bdcCf.getCfjsqx() != null && bdcCf.getCfksqx() != null) {
                            Long dayL = cn.gtmap.estateplat.utils.CommonUtil.getDaySub(bdcCf.getCfksqx(), bdcCf.getCfjsqx());
                            int daysI = dayL.intValue() - 1;
                            if (days > daysI) {
                                msg = "续行期限不得超过前款规定期限";
                            }
                        }
                    } else if (CollectionUtils.isNotEmpty(gdCfList)) {
                        GdCf gdCf = gdCfList.get(0);
                        if (gdCf.getCfjsrq() != null && gdCf.getCfksrq() != null) {
                            Long dayL = cn.gtmap.estateplat.utils.CommonUtil.getDaySub(gdCf.getCfksrq(), gdCf.getCfjsrq());
                            int daysI = dayL.intValue() - 1;
                            if (days > daysI) {
                                msg = "续行期限不得超过前款规定期限";
                            }
                        }
                    }
                }
            }
        }
        return msg;
    }

    /**
     * @param
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 苏州检查查封期限规则
     */
    @ResponseBody
    @RequestMapping(value = "/checkCfqxForSz")
    public HashMap checkCfqxForSz(@RequestParam(value = "proid", required = false) String proid,
                                  @RequestParam(value = "days", required = false) int days,
                                  @RequestParam(value = "djzx", required = false) String djzx,
                                  @RequestParam(value = "cfksqx", required = false) String cfksqx,
                                  @RequestParam(value = "cfjsqx", required = false) String cfjsqx) {
        HashMap result = new HashMap();
        String msg = "";
        Date xmcjsj = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            xmcjsj = bdcXm.getCjsj();
        }
        if (StringUtils.isNotBlank(cfksqx) && StringUtils.isNotBlank(cfjsqx)) {
            Date cfkssj = CalendarUtil.formatDate(cfksqx);
            if (cfkssj == null) {
                cfkssj = DateUtils.parse(cfksqx);
                if (cfkssj != null) {
                    cfkssj = CalendarUtil.formatDate(cfkssj);
                    boolean sameDay = DateUtils.isSameDay(xmcjsj, cfkssj);
                    if (!sameDay) {
                        result.put(ParamsConstants.FALSE_LOWERCASE, "查封开始时间和项目创建时间不是同一天");
                        return result;
                    }
                }
            }
            Date cfjssj = CalendarUtil.formatDate(cfjsqx);
            if (cfjssj == null) {
                cfjssj = DateUtils.parse(cfjsqx);
                if (cfjssj != null) {
                    cfjssj = CalendarUtil.formatDate(cfjssj);
                }
            }
            //3年后日期
            Date snrq = CalendarUtil.addYears(cfkssj, 3);
            snrq = CalendarUtil.formatDate(CalendarUtil.subDay(CalendarUtil.formatDateToString(snrq), 1));
            //2年后日期
            Date lnrq = CalendarUtil.addYears(cfkssj, 2);
            lnrq = CalendarUtil.formatDate(CalendarUtil.subDay(CalendarUtil.formatDateToString(lnrq), 1));

            //验证查封日期
            if (StringUtils.equals(djzx, Constants.DJZX_CF)) {
                int compareResult = CalendarUtil.compareToDate(cfjssj, snrq);
                if (compareResult == 1) {
                    msg = "查封期限不能超过三年";
                    result.put(ParamsConstants.FALSE_LOWERCASE, msg);
                } else if (compareResult == -1) {
                    msg = "查封期限不满三年";
                    result.put("true", msg);
                }
            } else if (StringUtils.equals(djzx, Constants.DJZX_XF)) {
                int compareResult = CalendarUtil.compareToDate(cfjssj, lnrq);
                if (compareResult == 1) {
                    msg = "续封期限不能超过两年";
                    result.put(ParamsConstants.FALSE_LOWERCASE, msg);
                } else if (compareResult == -1) {
                    msg = "续封期限不满两年";
                    result.put("true", msg);
                }
            } else if (StringUtils.equals(djzx, Constants.DJZX_LH)) {
                int compareResult = CalendarUtil.compareToDate(cfjssj, snrq);
                if (compareResult == 1) {
                    msg = "轮候查封期限不能超过三年";
                    result.put(ParamsConstants.FALSE_LOWERCASE, msg);
                } else if (compareResult == -1) {
                    msg = "轮候查封期限不满三年";
                    result.put("true", msg);
                }
            }
        }

        return result;
    }

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 保存过度权利人
     */
    @ResponseBody
    @RequestMapping(value = "/saveGdQlr", method = RequestMethod.POST)
    public String saveGdQlr(@RequestParam(value = "qlid", required = false) String qlid) {
        if (StringUtils.isNotBlank(qlid)) {
            gdqlService.saveQlrByQlid(qlid);
        }
        return ParamsConstants.SUCCESS_LOWERCASE;
    }

    /**
     * @param gdproid
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 同步过渡冗余字段
     */
    @ResponseBody
    @RequestMapping(value = "/synchronizationGdField", method = RequestMethod.POST)
    public void synchronizationGdField(@RequestParam(value = "gdproid", required = false) String gdproid) {
        if (StringUtils.isNotBlank(gdproid) && StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationgdfield.queue")) && StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"), "true")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("proid", gdproid);
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            correlationData.setMessage(jsonObject);
            correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationgdfield.queue"));
            rabbitmqSendMessageService.sendMsg(gdproid, correlationData.getRoutingKey(), JSON.toJSONString(jsonObject), correlationData);
        } else if (StringUtils.isNotBlank(gdproid)) {
            redundantFieldService.synchronizationGdField(gdproid);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getSfdBz", method = RequestMethod.POST)
    public HashMap getSfdBz(@RequestParam(value = "xmmc", required = false) String xmmc,
                            @RequestParam(value = "qlrlx", required = false) String qlrlx,
                            @RequestParam(value = "tdmj", required = false) Double tdmj,
                            @RequestParam(value = "jzmj", required = false) Double jzmj) {
        HashMap result = new HashMap();
        if (StringUtils.isNotBlank(xmmc) && StringUtils.isNotBlank(qlrlx)) {
            HashMap map = bdcXtYhMapper.getSjdBz(xmmc, qlrlx);
            if (map.size() > 0) {
                String bz = "";
                String je = "";
                if (map.get("SFXMBZ") != null) {
                    bz = map.get("SFXMBZ").toString();
                }
                if (map.get("JE") != null) {
                    je = map.get("JE").toString();
                }
                String jsffString = "";
                if (map.get("JSFF") != null) {
                    jsffString = map.get("JSFF").toString();
                }
                result.put("bz", bz);
                Double jsff = Double.parseDouble(jsffString);
                result.put("jsff", jsff);
                if (StringUtils.isNotBlank(je)) {
                    result.put("sjjy", je);
                } else {
                    if (StringUtils.indexOf(xmmc, "土") > -1) {
                        Double sjjy = jsff * tdmj;
                        DecimalFormat df = new DecimalFormat("#.0");
                        sjjy = Double.parseDouble(df.format(sjjy));
                        result.put("sjjy", sjjy);
                    } else {
                        Double sjjy = jsff * jzmj;
                        DecimalFormat df = new DecimalFormat("#.0");
                        sjjy = Double.parseDouble(df.format(sjjy));
                        result.put("sjjy", sjjy);
                    }
                }
            }
        }
        if (result.size() > 0) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 保存过度权利人
     */
    @ResponseBody
    @RequestMapping(value = "/saveGdfw", method = RequestMethod.POST)
    public String saveGdfw(@RequestParam(value = "fwid", required = false) String fwid) {
        gdqlService.saveGdfw(fwid);
        return ParamsConstants.SUCCESS_LOWERCASE;
    }

    /**
     * @param
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @rerutn
     * @description 保存过度土地权利
     */
    @ResponseBody
    @RequestMapping(value = "/saveGdtd", method = RequestMethod.POST)
    public String saveGdtd(@RequestParam(value = "tdid", required = false) String tdid) {
        gdqlService.saveGdtd(tdid);
        return ParamsConstants.SUCCESS_LOWERCASE;
    }

    /**
     * @param proid 当前项目proid
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 预告预告抵押合并流程、转移抵押流程  获取主业务权利人，转为抵押的义务人
     */
    @ResponseBody
    @RequestMapping(value = "/changeYgzhYwr", method = RequestMethod.POST)
    public void changeYgzhYwr(@RequestParam(value = "proid", required = false) String proid) {
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        List<BdcQlr> qlrtemp = bdcQlrService.queryBdcQlrByProid(proid);
        String tempProid = "";
        String[] proidList = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm1 : bdcXmList) {
                    /**
                     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                     * @description 取抵押的项目proid
                     */
                    if (StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_YG_YGSPFDY) || StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_FWDY_DM) || StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_FWDY_XS_DM) || StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_YG_BDCDY)) {
                        if (StringUtils.isBlank(tempProid)) {
                            tempProid = bdcXm1.getProid();
                        } else {
                            tempProid = tempProid + Constants.SPLIT_STR + bdcXm1.getProid();
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(tempProid)) {
            proidList = StringUtils.split(tempProid, Constants.SPLIT_STR);
        }
        if (CollectionUtils.isNotEmpty(qlrtemp) && proidList != null) {
            for (String dyproid : proidList) {
                /**
                 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                 * @description 先删除义务人，防止重复
                 */
                bdcQlrService.delBdcQlrByProid(dyproid, Constants.QLRLX_YWR);
                for (BdcQlr bdcQlr : qlrtemp) {
                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                    bdcQlr.setProid(dyproid);
                    bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                    entityMapper.insertSelective(bdcQlr);
                }
            }
        }
    }

    /**
     * @param proid
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 转移抵押合并流程，受让方转抵押人
     */
    @ResponseBody
    @RequestMapping(value = "saveDyqrForZydy")
    public void saveDyqrForZydy(@RequestParam(value = "proid", required = false) String proid) {
        if (StringUtils.isNotBlank(proid)) {
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            String[] proids = getProidList(proid);
            //排除当前proid的数据,以及非抵押项目的proid
            List<String> proidList = new ArrayList<String>();
            for (String tempProid : proids) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(tempProid);
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                if (!StringUtils.equals(proid, tempProid) && qllxVo instanceof BdcDyaq) {
                    proidList.add(tempProid);
                }
            }
            bdcQlrService.saveQlrsByProidsAndQlrlx(bdcQlrList, proidList, Constants.QLRLX_YWR);
        }
    }

    /**
     * @param proid
     * @author <a href="mailto:liuxing@gtmap.cn">lx</a>
     * @description 合并流程，申请书不动产情况共享
     */
    @ResponseBody
    @RequestMapping(value = "saveBdcqkForHb")
    public void saveBdcqkForHb(@RequestParam(value = "proid", required = false) String proid) {
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            String wiid = bdcXm.getWiid();
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            List<BdcSpxx> bdcSpxxList = bdcSpxxService.getBdcSpxxByWiid(wiid);
            if (bdcSpxxList != null && bdcSpxxList.size() > 0) {
                for (BdcSpxx spxxTemp : bdcSpxxList) {
                    spxxTemp.setZl(bdcSpxx.getZl());
                    spxxTemp.setZdzhmj(bdcSpxx.getZdzhmj());
                    spxxTemp.setBdclx(bdcSpxx.getBdclx());
                    spxxTemp.setMj(bdcSpxx.getMj());
                    spxxTemp.setYt(bdcSpxx.getYt());
                    spxxTemp.setZdzhyt(bdcSpxx.getZdzhyt());
                    spxxTemp.setZdzhqlxz(bdcSpxx.getZdzhqlxz());
                    spxxTemp.setYhlx(bdcSpxx.getYhlx());
                    spxxTemp.setLz(bdcSpxx.getLz());
                    spxxTemp.setGzwlx(bdcSpxx.getGzwlx());
                    entityMapper.saveOrUpdate(spxxTemp, spxxTemp.getSpxxid());
                }
            }
        }
    }

    /**
     * @param
     * @author <a href="mailto:zhanglili@gtmap.cn">zhanglili</a>
     * @rerutn
     * @description 保存收費單大寫金額到bdc_sfxx表中的bz字段
     */
    @ResponseBody
    @RequestMapping(value = "/saveSFXXBz", method = RequestMethod.POST)
    public String saveSFXXBz(@RequestParam(value = "proid", required = false) String proid, String chinese_hj) {
        if (StringUtils.isNotBlank(chinese_hj)) {
            try {
                chinese_hj = URLDecoder.decode(chinese_hj, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Example example = new Example(BdcSfxx.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcSfxx> bdcSfxxList = entityMapper.selectByExample(BdcSfxx.class, example);
        if (CollectionUtils.isNotEmpty(bdcSfxxList)) {
            BdcSfxx bdcSfxx = bdcSfxxList.get(0);
            bdcSfxx.setBz(chinese_hj);
            entityMapper.saveOrUpdate(bdcSfxx, bdcSfxx.getSfxxid());
        }
        return ParamsConstants.SUCCESS_LOWERCASE;
    }

    /**
     * @param id gdTable
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据id gdTable保存日志
     */
    @ResponseBody
    @RequestMapping(value = "/saveGdLog")
    public void saveGdLog(String id, String gdTable) {
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(gdTable)) {
            if (StringUtils.equals(gdTable, Constants.GDQL_GDXM_CPT)) {
                GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, id);
                if (gdXm != null) {
                    gdSaveLogSecvice.gdXmLog(gdXm);
                }
            } else if (StringUtils.equals(gdTable, Constants.GDQL_FWSYQ_CPT)) {
                GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, id);
                if (gdFwsyq != null) {
                    gdSaveLogSecvice.gdFwsyqLog(gdFwsyq);
                }
            } else if (StringUtils.equals(gdTable, Constants.GDQL_TDSYQ_CPT)) {
                GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, id);
                if (gdTdsyq != null) {
                    gdSaveLogSecvice.gdTdsyqLog(gdTdsyq);
                }
            } else if (StringUtils.equals(gdTable, Constants.GDQL_DY_CPT)) {
                GdDy gdDy = entityMapper.selectByPrimaryKey(GdDy.class, id);
                if (gdDy != null) {
                    gdSaveLogSecvice.gdDyLog(gdDy);
                }
            } else if (StringUtils.equals(gdTable, Constants.GDQL_CF_CPT)) {
                GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class, id);
                if (gdCf != null) {
                    gdSaveLogSecvice.gdCfLog(gdCf);
                }
            } else if (StringUtils.equals(gdTable, Constants.GDQL_YG_CPT)) {
                GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, id);
                if (gdYg != null) {
                    gdSaveLogSecvice.gdYgLog(gdYg);
                }
            } else if (StringUtils.equals(gdTable, Constants.GDQL_YY_CPT)) {
                GdYy gdYy = entityMapper.selectByPrimaryKey(GdYy.class, id);
                if (gdYy != null) {
                    gdSaveLogSecvice.gdYyLog(gdYy);
                }
            } else if (StringUtils.equals(gdTable, Constants.GD_FW)) {
                GdFw gdFw = entityMapper.selectByPrimaryKey(GdFw.class, id);
                if (gdFw != null) {
                    gdSaveLogSecvice.gdFwLog(gdFw);
                }
            } else if (StringUtils.equals(gdTable, Constants.GD_TD)) {
                GdTd gdTd = entityMapper.selectByPrimaryKey(GdTd.class, id);
                if (gdTd != null) {
                    gdSaveLogSecvice.gdTdLog(gdTd);
                }
            }
        }
    }

    /**
     * @param proid
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @rerutn
     * @description 当bdclx时设置djsy和qllx
     */
    @ResponseBody
    @RequestMapping(value = "/setDjsyAndQllx")
    public void setDjsyAndQllx(@RequestParam(value = "proid", required = false) String proid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            bdcXm.setDjsy("9");
            bdcXm.setQllx("39");
            entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
        }
    }

    /**
     * @author bianwen
     * @description 批量流程更新所有项目的登记子项
     */
    @ResponseBody
    @RequestMapping(value = "/saveDjzxForAll")
    public void saveDjzxForAll(@RequestParam(value = "proid", required = true) String proid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (StringUtils.isNotBlank(bdcXm.getDjzx()) && CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    bdcXmTemp.setDjzx(bdcXm.getDjzx());
                    entityMapper.saveOrUpdate(bdcXmTemp, bdcXmTemp.getProid());
                }
            }
        }
    }

    /**
     * @param proid 项目proid
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 针对抵押登记、查封(批量) 根据收件单的 登记子项 ，设置抵押方式或者查封类型
     */
    @ResponseBody
    @RequestMapping(value = "/saveDyfsByDjzx")
    public void saveDyfsByDjzx(@RequestParam(value = "proid", required = true) String proid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
        String dyfs = "";
        String cflx = "";
        String mc = "";
        String cf = "查封";
        String xf = "续封";
        String ycf = "预查封";
        String lhcf = "轮候查封";

        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            String djzx = bdcXm.getDjzx();
            if (StringUtils.isNotBlank(djzx)) {
                HashMap map = new HashMap();
                map.put("dm", djzx);
                List<HashMap> djzxList = bdcZdGlService.getDjzx(map);
                if (CollectionUtils.isNotEmpty(djzxList) && djzxList.get(0).get("MC") != null) {
                    mc = djzxList.get(0).get("MC").toString();
                }
            }
            if (qllxVo != null) {
                if (qllxVo instanceof BdcCf) {
                    if (cf.equals(mc)) {
                        cflx = Constants.CFLX_ZD_CF;
                    } else if (xf.equals(mc)) {
                        cflx = Constants.CFLX_XF;
                    } else if (ycf.equals(mc)) {
                        cflx = Constants.CFLX_ZD_YCF;
                    } else if (lhcf.equals(mc)) {
                        cflx = Constants.CFLX_LHCF;
                    }
                } else if (qllxVo instanceof BdcDyaq) {
                    if (mc.contains("最高额抵押")) {
                        dyfs = Constants.DYFS_ZGEDY;
                    } else if (mc.contains("一般抵押")) {
                        dyfs = Constants.DYFS_YBDY;
                    }
                }
            }

            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm1 : bdcXmList) {
                    QllxVo qllxVo1 = qllxService.queryQllxVo(bdcXm1);
                    if (qllxVo1 != null) {
                        if (qllxVo1 instanceof BdcDyaq && StringUtils.isNotBlank(dyfs)) {
                            BdcDyaq bdcDyaq = (BdcDyaq) qllxVo1;
                            bdcDyaq.setDyfs(dyfs);
                            entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                        } else if (qllxVo1 instanceof BdcCf && StringUtils.isNotBlank(cflx)) {
                            BdcCf bdcCf = (BdcCf) qllxVo1;
                            bdcCf.setCflx(cflx);
                            entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                        }
                    }
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateGdFwSfsh")
    public void updateGdFwSfsh(@RequestParam(value = "qlids", required = false) String qlids) {
        String[] qlidArray = qlids.split(",");
        for (String qlid : qlidArray) {
            gdFwMapper.updateSfsh(qlid);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateGdTdSfsh")
    public void updateGdTdSfsh(@RequestParam(value = "qlids", required = false) String qlids) {
        String[] qlidArray = qlids.split(",");
        for (String qlid : qlidArray) {
            gdTdMapper.updateSfsh(qlid);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/addZhDyaq")
    public Object addZhDyaq(@RequestParam(value = "wiid", required = true) String wiid) {
        Map<String, String> map = new HashMap();
        Map<String, String> map1 = new HashMap();
        String msg = "";
        if (StringUtils.isNotBlank(wiid)) {
            map1.put("wiid", wiid);
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySqlxAndWiid(map1);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    //判断是不是抵押权
                    String qllx = bdcXm.getQllx();
                    if (StringUtils.isNotBlank(qllx) && qllx.equals(Constants.QLLX_DYAQ)) {
                        List<InsertVo> bdcxxList = creatProjectService.copyBdcxxListFromBdcxm(bdcXm);
                        if (CollectionUtils.isNotEmpty(bdcxxList)) {
                            creatProjectService.insertProjectData(bdcxxList);
                            msg = ParamsConstants.SUCCESS_LOWERCASE;
                            break;
                        }
                    }
                }
            }
        }

        map.put("msg", msg);
        return map;
    }

    /**
     * @param
     * @author bianwen
     * @rerutn
     * @description 删除抵押物清单信息
     */
    @ResponseBody
    @RequestMapping(value = "/delDyaqForZjgc")
    public String delXmxxForZjgc(@RequestParam(value = "proids", required = false) String proids, @RequestParam(value = "bdcdyhs", required = false) String bdcdyhs, @RequestParam(value = "wfProid", required = false) String wfProid) {
        String msg = "失败";
        BdcXm wfbdcXm = bdcXmService.getBdcXmByProid(wfProid);
        String zjgcdyFw = "";
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, wfbdcXm.getSqlx()) || StringUtils.equals(wfbdcXm.getSqlx(), Constants.SQLX_LQ_PLDY_DM)) {
            zjgcdyFw = "true";
        }
        if (StringUtils.equals(zjgcdyFw, "true") && StringUtils.isNotBlank(proids)) {
            String flag = "";
            for (String proidTemp : StringUtils.split(proids, ",")) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proidTemp);
                /**
                 * @author bianwen
                 * @description 判断需删除的抵押物清单对应的proid是不是流程的proid,
                 * 不相等可以直接删除所有的项目信息
                 * 若相等，则要将剩余的数据任选一条复制过来
                 */
                if (StringUtils.equals(proidTemp, wfProid)) {
                    flag = "true";
                } else {

                    // 删除项目关系表
                    bdcXmRelService.delBdcXmRelByProid(proidTemp);

                    //删除收件单信息表
                    List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByProid(proidTemp);
                    if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                        for (BdcSjxx bdcSjxx : bdcSjxxList) {
                            bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                            bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
                        }
                    }
                    //删除审批信息
                    bdcSpxxService.delBdcSpxxByProid(proidTemp);

                    //删除权利人证书关系信息表以及权利人信息
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proidTemp);
                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
                            bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
                        }
                    }
                    //删除权利类型信息
                    QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                    qllxService.delQllxByproid(qllxVo, proidTemp);

                    bdcdyService.delDjbAndTd(bdcXm);

                    if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                        List<BdcXm> bdcXmList = null;
                        HashMap map = new HashMap();
                        map.put("bdcdyid", bdcXm.getBdcdyid());
                        bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
                        if (bdcXmList != null && bdcXmList.size() == 1) {
                            bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
                        }
                    }
                    delProjectDyBgForZjgcServiceImpl.delZsbh(proidTemp);
                    bdcXmService.delBdcXmByProid(proidTemp);
                    delProjectDyBgForZjgcServiceImpl.delProjectNode(proidTemp);
                }
            }
            if (StringUtils.equals(flag, "true")) {
                String proidAll = bdcXmService.getProidsByProid(wfProid);
                String proid1 = "";
                if (StringUtils.isNotBlank(proidAll)) {
                    for (String proidStr : StringUtils.split(proidAll, Constants.SPLIT_STR)) {
                        if (!StringUtils.equals(proidStr, wfProid)) {
                            proid1 = proidStr;
                            break;
                        }
                    }
                    BdcXm xm1 = bdcXmService.getBdcXmByProid(proid1);
                    BdcXm wfxm = bdcXmService.getBdcXmByProid(wfProid);
                    try {
                        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid1);
                        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                            for (BdcXmRel bdcxmrel : bdcXmRelList) {
                                bdcxmrel.setProid(wfProid);
                                bdcXmRelService.delBdcXmRelByProid(wfProid);
                                entityMapper.saveOrUpdate(bdcxmrel, bdcxmrel.getRelid());
                            }
                        }
                        List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByProid(proid1);
                        List<BdcSjxx> wfbdcSjxxList = bdcSjdService.queryBdcSjdByProid(wfProid);
                        if (CollectionUtils.isNotEmpty(wfbdcSjxxList)) {
                            for (BdcSjxx bdcSjxx : wfbdcSjxxList) {
                                bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                                bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                            for (BdcSjxx bdcSjxx : bdcSjxxList) {
                                bdcSjxx.setProid(wfProid);
                                entityMapper.saveOrUpdate(bdcSjxx, bdcSjxx.getSjxxid());
                            }
                        }

                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid1);
                        if (bdcSpxx != null) {
                            bdcSpxx.setProid(wfProid);
                            bdcSpxxService.delBdcSpxxByProid(wfProid);
                            entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
                        }
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid1);
                        List<BdcQlr> wfbdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(wfProid);
                        if (CollectionUtils.isNotEmpty(wfbdcQlrList)) {
                            for (BdcQlr bdcQlr : wfbdcQlrList) {
                                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
                                bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                            for (BdcQlr bdcQlr : bdcQlrList) {
                                bdcQlr.setProid(wfProid);
                                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                            }
                        }
                        //删除权利类型信息
                        QllxVo qllxVo = qllxService.makeSureQllx(wfxm);
                        qllxService.delQllxByproid(qllxVo, wfProid);

                        HashMap param = new HashMap();
                        param.put(ParamsConstants.PROID_LOWERCASE, proid1);
                        List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(param);
                        if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                            for (BdcDyaq bdcDyaq : bdcDyaqList) {
                                bdcDyaq.setProid(wfProid);
                                entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                            }
                        }

                        bdcdyService.delDjbAndTd(wfxm);

                        if (StringUtils.isNotBlank(wfxm.getBdcdyid())) {
                            List<BdcXm> bdcXmList = null;
                            HashMap map = new HashMap();
                            map.put("bdcdyid", wfxm.getBdcdyid());
                            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
                            if (bdcXmList != null && bdcXmList.size() == 1) {
                                bdcdyService.delBdcdyById(wfxm.getBdcdyid());
                            }
                        }
                        delProjectDyBgForZjgcServiceImpl.delZsbh(wfProid);
                        delProjectDyBgForZjgcServiceImpl.delProjectNode(wfProid);

                        BdcXm xm = (BdcXm) BeanUtils.cloneBean(xm1);
                        xm.setProid(wfProid);
                        entityMapper.saveOrUpdate(xm, xm.getProid());
                        entityMapper.deleteByPrimaryKey(BdcXm.class, xm1.getProid());

                        List<BdcZjjzwxx> bdcZjjzwxxList = bdcZjjzwxxService.getZjjzwxx(param);
                        if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                            for (BdcZjjzwxx bdcZjjzwxx : bdcZjjzwxxList) {
                                bdcZjjzwxx.setProid(wfProid);
                                entityMapper.saveOrUpdate(bdcZjjzwxx, bdcZjjzwxx.getZjwid());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
            }
            msg = "成功";
        }
        return msg;
    }

    /**
     * @author bianwen
     * @description 获取批量流程是否可以提交权利人配置信息
     */
    @ResponseBody
    @RequestMapping(value = "/isUnSaveQlr")
    public String isUnSaveQlr(@RequestParam(value = "proid", required = false) String proid) {
        HashMap<String, String> unSaveQlrSqlxMap = ReadXmlProps.getUnSaveQlrSqlxMap();
        String msg = ParamsConstants.FALSE_LOWERCASE;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                String sqlxdm = bdcXm.getSqlx();
                if (unSaveQlrSqlxMap.containsKey(sqlxdm)) {
                    msg = "true";
                }
            }
        }
        return msg;
    }

    /**
     * @author chenjia
     * @description 根据证件编号查询信用
     */
    @ResponseBody
    @RequestMapping(value = "/testQlr")
    public HashMap testQlr(@RequestParam(value = "qlrzjh", required = false) String qlrzjh, @RequestParam(value = "qlrsfzjzl", required = false) String qlrsfzjzl) {
        HashMap map = Maps.newHashMap();
        List<BdcXygl> bdcXyglList = null;
        String qlrxx = "";
        if (StringUtils.isNotBlank(qlrzjh) && StringUtils.isNotBlank(qlrsfzjzl)) {
            Example example = new Example(BdcXygl.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("qlrsfzjzl", qlrsfzjzl).andEqualTo("qlrzjh", qlrzjh);
            bdcXyglList = entityMapper.selectByExample(BdcXygl.class, example);
        }
        if (CollectionUtils.isNotEmpty(bdcXyglList)) {
            for (BdcXygl bdcXygl : bdcXyglList) {
                if (StringUtils.equals(bdcXygl.getQlrsfzjzl(), qlrsfzjzl)) {
                    List<BdcZdZjlx> bdcZdZjlxList = null;
                    if (StringUtils.isNotBlank(bdcXygl.getQlrsfzjzl())) {
                        Example example = new Example(BdcZdZjlx.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("dm", bdcXygl.getQlrsfzjzl());
                        bdcZdZjlxList = entityMapper.selectByExample(BdcZdZjlx.class, example);
                    }
                    if (StringUtils.isNotBlank(bdcXygl.getXyglid())) {
                        map.put("xyglid", bdcXygl.getXyglid());
                    }
                    if (StringUtils.isNotBlank(bdcXygl.getQlrmc())) {
                        map.put("qlrmc", bdcXygl.getQlrmc());
                    }
                    if (bdcZdZjlxList != null) {
                        map.put("qlrsfzjzl", bdcZdZjlxList.get(0).getMc());
                    }
                    if (StringUtils.isNotBlank(bdcXygl.getQlrzjh())) {
                        map.put("qlrzjh", bdcXygl.getQlrzjh());
                    }
                }
            }
        }
        return map;
    }

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 苏州需要查询信用明细的状态，所以另写方法验证信用管理
     **/
    @ResponseBody
    @RequestMapping(value = "checkXygl")
    public HashMap checkXygl(String qlrzjh, @RequestParam(value = "qlrmc", required = false) String qlrmc) {
        HashMap hashMap = null;
        if (StringUtils.isNotBlank(qlrmc)) {
            try {
                qlrmc = URLDecoder.decode(qlrmc, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(qlrzjh)) {
            hashMap = bdcXymxService.getXsBdcXyxxByZjh(qlrzjh, qlrmc);
        }
        return hashMap;
    }

    /**
     * @author bianwen
     * @description 在建工程以房屋为主，填写一条权利的抵押权金额，期限，保存时同时保存所有权利
     */
    @ResponseBody
    @RequestMapping(value = "/saveDyInfoForAll")
    public String saveDyInfoForAll(@RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        String msg = "失败";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            String proids = bdcXmService.getProidsByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx()) && CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, bdcXm.getSqlx())) {
                HashMap param = new HashMap();
                param.put(ParamsConstants.PROID_LOWERCASE, proid);
                List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(param);
                if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                    BdcDyaq bdcDyaq = bdcDyaqList.get(0);
                    param.clear();
                    param.put("proids", StringUtils.split(proids, Constants.SPLIT_STR));
                    List<BdcDyaq> bdcDyaqListaAll = bdcDyaqService.queryBdcDyaq(param);
                    if (CollectionUtils.isNotEmpty(bdcDyaqListaAll)) {
                        for (BdcDyaq bdcDyaqTemp : bdcDyaqListaAll) {
                            if (!StringUtils.equals(bdcDyaq.getProid(), bdcDyaqTemp.getProid())) {
                                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcDyaqTemp.getProid());
                                bdcDyaqTemp.setZjgcdyfw(bdcDyaq.getZjgcdyfw());
                                bdcDyaqTemp.setBdbzzqse(bdcDyaq.getBdbzzqse());
                                bdcDyaqTemp.setZwlxksqx(bdcDyaq.getZwlxksqx());
                                bdcDyaqTemp.setZwlxjsqx(bdcDyaq.getZwlxjsqx());
                                bdcDyaqTemp.setDkfs(bdcDyaq.getDkfs());
                                if (bdcSpxx != null) {
                                    bdcDyaqTemp.setZjgczl(bdcSpxx.getZl());
                                }
                                entityMapper.saveOrUpdate(bdcDyaqTemp, bdcDyaqTemp.getQlid());
                            }
                        }
                    }
                }
                msg = "成功";
            }
        }
        return msg;
    }

    /**
     * @param wiid,proid
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 附属设施部分信息和主房同步
     */
    @ResponseBody
    @RequestMapping("saveFsssVorByZfVo")
    public void saveFsssVorByZfVo(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            //jyl 获取流程中所有附属设施的项目
            HashMap hashMap = new HashMap();
            hashMap.put("wiid", wiid);
            hashMap.put("hasBdcdy", "true");
            List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssList(hashMap);
            if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                    if (bdcFwfsss != null && StringUtils.isNotBlank(bdcFwfsss.getBdcdyid()) && StringUtils.isNotBlank(bdcFwfsss.getWiid()) && StringUtils.isNotBlank(bdcFwfsss.getZfbdcdyh())) {
                        String zfBdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcFwfsss.getZfbdcdyh());
                        List<BdcXm> fsssBdcXms = bdcXmService.getBdcXmListByWiidAndBdcdyid(bdcFwfsss.getWiid(), bdcFwfsss.getBdcdyid());
                        if (CollectionUtils.isNotEmpty(fsssBdcXms)) {
                            for (BdcXm fsssBdcXm : fsssBdcXms) {
                                //附属同步对应主房的信息
                                if (StringUtils.isNotBlank(zfBdcdyid)) {
                                    List<BdcXm> zfBdcXms = bdcXmService.getBdcXmListByWiidAndBdcdyid(bdcFwfsss.getWiid(), zfBdcdyid);
                                    if (CollectionUtils.isNotEmpty(zfBdcXms)) {
                                        for (BdcXm zfBdcXm : zfBdcXms) {
                                            if (StringUtils.isNotBlank(zfBdcXm.getQllx()) && StringUtils.isNotBlank(zfBdcXm.getSqlx())
                                                    && StringUtils.equals(zfBdcXm.getQllx(), fsssBdcXm.getQllx()) && StringUtils.equals(zfBdcXm.getSqlx(), fsssBdcXm.getSqlx())) {
                                                bdcFwFsssService.syncFsssVoByZfVo(fsssBdcXm, zfBdcXm);
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @author liujie
     * @description 删除bdc_xm表里的交易合同号
     */
    @ResponseBody
    @RequestMapping(value = "/deleteJyhth")
    public String deleteJyhth(@RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        String msg = "失败";
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    bdcXm.setSpxtywh("");
                    entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                    msg = "成功";
                }
            }
        } else if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                bdcXm.setSpxtywh("");
                entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                msg = "成功";
            }
        }
        return msg;
    }


    /**
     * @author <a href="mailto:zhangyu@gtmap.cn">zhangyu</a>
     * @description 更新林权权利森林、林木所有权人，森林、林木使用权人为当前权利人
     */
    @ResponseBody
    @RequestMapping("updataLqVo")
    public void updataLqVo(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            //判断林权转移登记才更新
            if (CommonUtil.indexOfStrs(Constants.SQLX_LQ_SCDJ, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_LQ_ZYDJ, bdcXm.getSqlx())
                    || CommonUtil.indexOfStrs(Constants.SQLX_LQ_BGDJ, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_HZ_DM, bdcXm.getSqlx())
                    || StringUtils.equals(Constants.SQLX_YSBZ_DM, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_LQ_GZDJ_DM, bdcXm.getSqlx())) {
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);

                Example example = new Example(BdcLq.class);
                example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
                List<BdcLq> list = entityMapper.selectByExample(BdcLq.class, example);

                StringBuilder lmsyqr = new StringBuilder();
                for (BdcQlr bdcQlr : bdcQlrList) {
                    lmsyqr.append(bdcQlr.getQlrmc()).append(" ");
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    BdcLq bdcLq = list.get(0);
                    bdcLq.setLmsyqr(String.valueOf(lmsyqr));
                    bdcLq.setLmsuqr(String.valueOf(lmsyqr));
                    entityMapper.saveOrUpdate(bdcLq, bdcLq.getQlid());
                }
            }
        }
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @description 新增土地证号同步更新土地证号简称字段
     */
    @ResponseBody
    @RequestMapping(value = "updateTdzhjc")
    public void updateTdzhjc(String qlid) {
        if (StringUtils.isNotBlank(qlid)) {
            bdcTdsyqService.updateCqzhjcByQlid(qlid);
        }
    }
}
