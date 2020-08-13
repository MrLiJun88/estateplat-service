package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/3/1
 * @description
 */
@Service
public class BdcCheckMatchDataServiceImpl implements BdcCheckMatchDataService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private DjsjFwMapper djsjFwMapper;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdDyService gdDyService;
    @Autowired
    private GdYyService gdYyService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private GdYgService gdYgService;

    @Override
    public HashMap checkMatchData(String qlid, String gdid, String bdcdyh, String tdid, String tdqlid, String bdclx) {
        HashMap result = new HashMap();
        //jiangganzhi 不动产单元号匹配做项目后应该不能再进行匹配
        if (StringUtils.isNotBlank(bdcdyh) && StringUtils.isNotBlank(qlid)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(qlid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                        if (bdcBdcdy != null && StringUtils.equals(bdcBdcdy.getBdcdyh(), bdcdyh)) {
                            result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                            result.put("msg", "该不动产单元号已创建项目，无法匹配！");
                            return result;
                        }
                    }
                }
            }
        }
        //jiangganzhi 匹配时验证不动产单元号存在2条及2条以上匹配关系
        String needMultiPp = AppConfig.getProperty("sjpp.mulFczTdzPp");
        if (!StringUtils.equals(needMultiPp, ParamsConstants.TRUE_LOWERCASE) && StringUtils.isNotBlank(bdcdyh) && StringUtils.isNotBlank(qlid)) {
            String bdcdyfwlx = bdcDjsjService.getBdcdyfwlxByBdcdyh(bdcdyh);
            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(qlid);
            List<GdBdcQlRel> gdBdcQlRelList = null;
            if (gdFwsyq != null) {
                gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
            }
            //多幢和一证多房不验证
            if (StringUtils.isNotBlank(bdcdyfwlx) && !StringUtils.equals(bdcdyfwlx, "1") && CollectionUtils.isNotEmpty(gdBdcQlRelList) && gdBdcQlRelList.size() < 2) {
                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(bdcdyh);
                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                    result.put("msg", "该不动产单元号存在匹配关系，无法匹配！");
                    return result;
                }
            }
        }
        //jiangganzhi 匹配时验证不动产单元号是否已经发过不动产产权证
        if (StringUtils.isNotBlank(bdcdyh)) {
            List<String> bdcProidList = bdcXmService.getXsBdcCqProidByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(bdcProidList)) {
                result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                result.put("msg", "该不动产单元号已发过不动产权证，无法匹配！");
                return result;
            }
        }
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 对于单停模式，可能一个房屋推过来多条项目，其中一个房屋匹配了，则不允许匹配别的不动产单元
         */
        String fcdah = gdFwService.getDahByFwid(gdid);
        if (StringUtils.isNotBlank(fcdah)) {
            List<String> fwidList = gdFwService.getFwidByDah(fcdah);
            if (CollectionUtils.isNotEmpty(fwidList)) {
                for (String fwid : fwidList) {
                    if (!fwid.equals(gdid)) {
                        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, fwid);
                        if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                            BdcGdDyhRel bdcGdDyhRel = bdcGdDyhRelList.get(0);
                            if (!StringUtils.equals(bdcGdDyhRel.getBdcdyh(), bdcdyh)) {
                                result.put("flag", ParamsConstants.WARNING_LOWERCASE);
                                result.put("msg", "该房屋已匹配其他不动产单元，单元号为：" + bdcGdDyhRel.getBdcdyh() + "；是否重新匹配");
                                return result;
                            }
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(bdclx) && bdclx.equals(Constants.BDCLX_TD)) {
            //土地证
            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, gdid);
            List<BdcGdDyhRel> bdcTdGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(tdid);
            if (CollectionUtils.isEmpty(bdcGdDyhRelList)) {
                if (CollectionUtils.isNotEmpty(bdcTdGdDyhRelList)) {
                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                    result.put("msg", "该土地证已匹配房产证，无法匹配！");
                    return result;
                }
                //不存在匹配关系，验证通过
                result.put("flag", "true");
                return result;
            } else {
                //存在匹配关系，检查土地证是否已创建项目
                List<BdcXmRel> tdbdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(qlid);
                if (CollectionUtils.isEmpty(tdbdcXmRelList)) {
                    //未创建，验证是否匹配房屋，是否创建房地一体项目
                    Example bdcGdDyhRelEx = new Example(BdcGdDyhRel.class);
                    bdcGdDyhRelEx.createCriteria().andEqualTo("tdid", gdid);
                    List<BdcGdDyhRel> fwbdcGdDyhRelList = entityMapper.selectByExample(bdcGdDyhRelEx);
                    if (CollectionUtils.isEmpty(fwbdcGdDyhRelList)) {
                        if (StringUtils.equals(bdcGdDyhRelList.get(0).getBdcdyh(), bdcdyh)) {
                            //未匹配房屋，提示更新
                            result.put("flag", "true");
                            result.put("msg", "是否更新匹配关系");
                            return result;
                        } else {
                            //未匹配房屋，提示更新
                            result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                            result.put("msg", "该土地已经匹配不动产单元，不动产单元号为：" + bdcGdDyhRelList.get(0).getBdcdyh() + ",无法匹配");
                            return result;
                        }

                    } else {
                        //已匹配房屋，检查房屋是否创建流程
                        Example gdQlDyhRelEx = new Example(GdQlDyhRel.class);
                        gdQlDyhRelEx.createCriteria().andEqualTo("tdqlid", qlid);
                        List<GdQlDyhRel> gdQlDyhRelList = entityMapper.selectByExample(gdQlDyhRelEx);
                        //此处可能有疑问，正常情况下，GdQlDyhRel表里应该是有数据的
                        if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                            GdQlDyhRel gdQlDyhRel = gdQlDyhRelList.get(0);
                            String fwqlid = gdQlDyhRel.getQlid();
                            if (StringUtils.isNotBlank(fwqlid)) {
                                List<BdcXmRel> fwbdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(fwqlid);
                                if (CollectionUtils.isEmpty(fwbdcXmRelList)) {
                                    //未创建
                                    result.put("flag", ParamsConstants.WARNING_LOWERCASE);
                                    result.put("msg", "该房屋已匹配其他不动产单元，单元号为：" + gdQlDyhRel.getBdcdyh() + "；是否重新匹配！");
                                    return result;
                                } else {
                                    //已创建房地一体流程
                                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                    result.put("msg", "该房屋已匹配其他不动产单元，单元号为：" + gdQlDyhRel.getBdcdyh() + "；无法匹配！");
                                    return result;
                                }
                            }
                        }
                    }
                } else {
                    //已经创建项目
                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                    result.put("msg", "该证书已创建项目，不可重新匹配");
                    return result;
                }
            }

        } else if (StringUtils.isNotBlank(bdclx) && bdclx.equals(Constants.BDCLX_TDFW)) {

            List<BdcGdDyhRel> checkbdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, gdid);
            if (CollectionUtils.isNotEmpty(checkbdcGdDyhRelList) && checkbdcGdDyhRelList.size() > 1) {
                for (BdcGdDyhRel bdcGdDyhRel : checkbdcGdDyhRelList) {
                    if (!StringUtils.equals(bdcGdDyhRel.getBdcdyh(), bdcdyh)) {
                        result.put("flag", ParamsConstants.WARNING_LOWERCASE);
                        result.put("msg", "该房屋已匹配其他不动产单元，单元号为：" + bdcGdDyhRel.getBdcdyh() + "；是否重新匹配");
                        return result;
                    }
                }
            }

            if (StringUtils.isNotBlank(bdcdyh)) {
                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(bdcdyh, "");
                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                    List<String> bdcFwlxList = djsjFwMapper.getBdcfwlxByBdcdyh(bdcdyh);
                    if (CollectionUtils.isNotEmpty(bdcFwlxList)) {
                        String bdcFwlx = bdcFwlxList.get(0);
                        if (!StringUtils.equals(bdcFwlx, "1")) {
                            for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                                if (!StringUtils.equals(bdcGdDyhRel.getGdid(), gdid)) {
                                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcGdDyhRel.getGdid());
                                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                            if (!StringUtils.equals(gdBdcQlRel.getQlid(), qlid)) {
                                                //验证该不动产单元前后匹配的权利是否是同一种类型，如果是则不让其重新匹配，反之可以
                                                Boolean sameQlVo = false;
                                                if (StringUtils.isNotBlank(qlid) && StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                                                    GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, qlid);
                                                    GdFwsyq ygdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, gdBdcQlRel.getQlid());
                                                    if (gdFwsyq != null && ygdFwsyq != null) {
                                                        sameQlVo = true;
                                                    } else {
                                                        GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, qlid);
                                                        GdYg ygdYg = entityMapper.selectByPrimaryKey(GdYg.class, gdBdcQlRel.getQlid());
                                                        if (gdYg != null && ygdYg != null && StringUtils.equals(Constants.YGDJZL_YGSPF_MC, StringUtils.deleteWhitespace(gdYg.getYgdjzl())) && StringUtils.equals(Constants.YGDJZL_YGSPF_MC, StringUtils.deleteWhitespace(ygdYg.getYgdjzl()))) {
                                                            sameQlVo = true;
                                                        }
                                                    }
                                                }
                                                GdFwQl gdFwQl = entityMapper.selectByPrimaryKey(GdFwQl.class, gdBdcQlRel.getQlid());
                                                if (gdFwQl != null && sameQlVo) {
                                                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                                    result.put("msg", "该不动产单元号已匹配其他产权，房产证号为：" + gdFwQl.getFczh() + "；不得重新匹配");
                                                    return result;
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


            if (StringUtils.isNotBlank(tdqlid)) {
                List<GdBdcQlRel> gdBdcQlRelLst = gdBdcQlRelService.queryGdBdcQlListByQlid(tdqlid);
                if (CollectionUtils.isNotEmpty(gdBdcQlRelLst)) {
                    tdid = gdBdcQlRelLst.get(0).getBdcid();
                }
            }

            /**
             * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
             * @description 验证证书类型是否可以匹配
             */
            if (StringUtils.isNotBlank(qlid) && StringUtils.isNotBlank(tdqlid)) {
                GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, qlid);
                GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, tdqlid);
                GdDy gdfwDy = entityMapper.selectByPrimaryKey(GdDy.class, qlid);
                GdDy gdtdDy = entityMapper.selectByPrimaryKey(GdDy.class, tdqlid);
                GdCf gdfwCf = entityMapper.selectByPrimaryKey(GdCf.class, qlid);
                GdCf gdtdCf = entityMapper.selectByPrimaryKey(GdCf.class, tdqlid);
                if (!((gdFwsyq != null && gdTdsyq != null) || (gdfwDy != null && gdtdDy != null) || (gdfwCf != null && gdtdCf != null))) {
                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                    result.put("msg", "不同类型证书不能匹配");
                    return result;
                }
            }
            //是否存在匹配关系
            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, gdid);
            if (CollectionUtils.isEmpty(bdcGdDyhRelList)) {
                if (StringUtils.isNotBlank(tdqlid)) {
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(tdqlid);
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        //对于不匹配不动产单元流程建立的项目 登记库查到后仍然允许匹配
                        Boolean needTip = false;
                        for (BdcXmRel bdcXmRel : bdcXmRelList) {
                            if (StringUtils.isNotBlank(bdcXmRel.getProid())) {
                                BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                    if (bdcBdcdy != null) {
                                        needTip = true;
                                    }
                                }
                            }
                        }
                        if (needTip) {
                            result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                            result.put("msg", "该土地证已创建项目，不得匹配");
                        } else {
                            result.put("flag", "true");
                        }
                        return result;
                    }
                    bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, tdid);
                    if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                        result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                        result.put("msg", "该土地证已存在匹配关系，不得匹配");
                        return result;
                    }
                } else {
                    //不存在匹配关系，验证通过
                    result.put("flag", "true");
                    return result;
                }
            } else {
                //检查不动产单元号是否一致
                BdcGdDyhRel bdcGdDyhRel = bdcGdDyhRelList.get(0);
                if (!StringUtils.equals(bdcdyh, bdcGdDyhRel.getBdcdyh())) {
                    //不一致，检查是否创建项目
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(qlid);
                    if (CollectionUtils.isEmpty(bdcXmRelList)) {
                        //未创建，提示替换
                        result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                        result.put("msg", "该房屋已匹配其他不动产单元，单元号为：" + bdcGdDyhRel.getBdcdyh() + "；请取消匹配后再试！");
                        return result;
                    } else {
                        //已创建，不可匹配
                        for (BdcXmRel bdcXmRel : bdcXmRelList) {
                            if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getProid())) {
                                BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                    result.put("msg", "该证书已匹配不动产单元创建项目，单元号为：" + bdcGdDyhRel.getBdcdyh() + "；不得重新匹配");
                                    return result;
                                }
                            }
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(tdqlid) && StringUtils.isNotBlank(tdid)) {
                        if (StringUtils.isNotBlank(bdcGdDyhRel.getTdid()) && !bdcGdDyhRel.getTdid().equals(tdid)) {
                            //和原匹配的土地证不相同，检查房产证是否创建项目
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(qlid);
                            if (CollectionUtils.isEmpty(bdcXmRelList)) {
                                //未创建项目，检查土地证是否创建项目，只是提示信息的区别，都可匹配
                                List<BdcXmRel> tdbdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(tdqlid);
                                if (CollectionUtils.isNotEmpty(tdbdcXmRelList)) {
                                    for (BdcXmRel bdcXmRel : tdbdcXmRelList) {
                                        if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getProid())) {
                                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                                            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                                result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                                result.put("msg", "该土地证已创建土地项目,无法匹配");
                                                return result;
                                            }
                                        }
                                    }

                                }

                                bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, tdid);
                                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                                    result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                    result.put("msg", "该土地证已存在匹配关系，不得匹配");
                                    return result;
                                } else {
                                    result.put("flag", ParamsConstants.WARNING_LOWERCASE);
                                    result.put("msg", "已匹配其他土地证,是否更新？");
                                    return result;
                                }

                            } else {
                                //已创建项目，不得匹配
                                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                    if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getProid())) {
                                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                                        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                            if (bdcBdcdy != null) {
                                                result.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                                result.put("msg", "该证书已创建项目，不得重新匹配");
                                                return result;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            result.put("flag", "true");
                            return result;
                        }
                    }
                }
            }
        }
        //林权等暂不验证
        if (!result.containsKey("flag")) {
            result.put("flag", "true");
        }
        return result;
    }

    @Override
    public HashMap checkMulMatchData(String qlid, String gdid, String bdcdyh, String tdqlid, String bdclx) {
        HashMap hashMap = new HashMap();
        //zwq 目前没遇到多个土地匹配的，所以土地的暂且预留
        if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            //验证证书类型
            hashMap = checkZslx(qlid, tdqlid);
            if (hashMap.size() == 0) {
                //验证是否创建项目
                hashMap = checkCreateXm(qlid, tdqlid);
            }

            if (hashMap.size() == 0) {
                //验证是否已经匹配
                hashMap = checkDyh(gdid, tdqlid, bdcdyh);
            }
        }
        return hashMap;
    }

    //验证证书是否一致
    public HashMap checkZslx(String qlid, String tdqlid) {
        HashMap hashMap = new HashMap();
        if (StringUtils.isNotBlank(qlid) && StringUtils.isNotBlank(tdqlid)) {
            List<String> qlids = new ArrayList<String>();
            qlids.addAll(Arrays.asList(qlid.split(",")));
            qlids.addAll(Arrays.asList(tdqlid.split(",")));
            List<String> zsTypelist = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(qlids)) {
                for (String qlidTemp : qlids) {
                    getZsTypeListFromQlid(qlidTemp, zsTypelist);
                }
            }
            if (CollectionUtils.isNotEmpty(zsTypelist) && zsTypelist.size() > 1) {
                hashMap.put("flag", ParamsConstants.FALSE_LOWERCASE);
                hashMap.put("msg", "不同类型证书不能匹配");
            }
        }
        return hashMap;
    }

    //验证是否创建项目
    public HashMap checkCreateXm(String qlid, String tdqlid) {
        HashMap hashMap = new HashMap();
        if (StringUtils.isNotBlank(qlid)) {
            String[] qlidArray = qlid.split(",");
            for (String id : qlidArray) {
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(id);
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getProid())) {
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                if (bdcBdcdy != null) {
                                    hashMap.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                    hashMap.put("msg", "房产证已创建项目，不得重新匹配");
                                    return hashMap;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(tdqlid)) {
            String[] qlidArray = tdqlid.split(",");
            for (String id : qlidArray) {
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelListByYqlid(id);
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getProid())) {
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                if (bdcBdcdy != null) {
                                    hashMap.put("flag", ParamsConstants.FALSE_LOWERCASE);
                                    hashMap.put("msg", "土地证已创建项目，不得重新匹配");
                                    return hashMap;
                                }
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public HashMap checkDyh(String gdid, String tdqlid, String bdcdyh) {
        HashMap hashMap = new HashMap();
        if (StringUtils.isNotBlank(gdid)) {
            //判断是否匹配过不动产单元号
            String[] gdidArray = gdid.split(",");
            for (String id : gdidArray) {
                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(id);
                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                    BdcGdDyhRel bdcGdDyhRel = bdcGdDyhRelList.get(0);
                    if (!StringUtils.equals(bdcdyh, bdcGdDyhRel.getBdcdyh())) {
                        hashMap.put("flag", ParamsConstants.FALSE_LOWERCASE);
                        hashMap.put("msg", "该房屋已匹配其他不动产单元，单元号为：" + bdcGdDyhRel.getBdcdyh());
                        break;
                    }
                }
            }
        }

        if (hashMap.size() == 0 && StringUtils.isNotBlank(tdqlid)) {
            //判断该土地证是否匹配过其余不动产单元号
            //一种情况是土地证匹配土地不动产单元号
            List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(null, tdqlid, null);
            if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                hashMap.put("flag", ParamsConstants.FALSE_LOWERCASE);
                hashMap.put("msg", "该土地证已存在匹配关系，不得匹配");
            } else {
                //一种情况，其余房屋关联这个土地证匹配了其他不动产单元号
                gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(null, null, tdqlid);
                if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                    GdQlDyhRel gdQlDyhRel = gdQlDyhRelList.get(0);
                    if (!StringUtils.equals(gdQlDyhRel.getBdcdyh(), bdcdyh)) {
                        hashMap.put("flag", ParamsConstants.FALSE_LOWERCASE);
                        hashMap.put("msg", "该土地证已和" + gdQlDyhRel.getBdcdyh() + "匹配，不得匹配");
                    }
                }
            }
        }
        return hashMap;
    }

    /**
     * @param qlid
     * @param zsTypeList
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据qlid获取权利类型集合，搜素次序按照可能出现的频率排序
     */
    private void getZsTypeListFromQlid(String qlid, List<String> zsTypeList) {
        if (StringUtils.isNotBlank(qlid) && null != zsTypeList) {
            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(qlid);
            if (null != gdFwsyq && !zsTypeList.contains(Constants.GD_SYQ)) {
                zsTypeList.add(Constants.GD_SYQ);
            } else {
                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(qlid);
                if (gdTdsyq != null && !zsTypeList.contains(Constants.GD_SYQ)) {
                    zsTypeList.add(Constants.GD_SYQ);
                } else {
                    GdDy gdDy = gdDyService.getGdDyByDyDyid(qlid);
                    if (gdDy != null && !zsTypeList.contains(Constants.GDQL_DY)) {
                        zsTypeList.add(Constants.GDQL_DY);
                    } else {
                        GdCf gdCf = gdCfService.getGdCfByCfid(qlid);
                        if (null != gdCf && !zsTypeList.contains(Constants.GDQL_CF)) {
                            zsTypeList.add(Constants.GDQL_CF);
                        } else {
                            GdYg gdYg = gdYgService.queryGdYgByYgid(qlid);
                            if (null != gdYg && !zsTypeList.contains(Constants.GDQL_YG)) {
                                zsTypeList.add(Constants.GDQL_YG);
                            } else {
                                GdYy gdYy = gdYyService.queryGdYyByYyid(qlid);
                                if (null != gdYy && !zsTypeList.contains(Constants.GDQL_YY)) {
                                    zsTypeList.add(Constants.GDQL_YY);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
