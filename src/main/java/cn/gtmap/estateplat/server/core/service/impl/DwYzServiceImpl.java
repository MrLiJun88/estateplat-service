package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User: zwq
 * Date: 15-9-16
 * Time: 上午11:09
 */
@Repository
public class DwYzServiceImpl implements DwYzService {
    @Autowired
    DjsjFwService djsjFwService;
    @Autowired
    BdcDjsjService bdcDjsjService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    DjSjMapper djSjMapper;
    @Autowired
    BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdXmService gdXmService;

    DjsjLqxx djsjLqxx;
    DjsjFwxx djsjFwxx;
    List<DjsjZdxx> djsjZdxxList;
    DjsjZdxx djsjZdxx;
    DjsjCbzdDcb cbzdDcb;
    List<DjsjNydDcb> djsjNydDcbList;
    DjsjNydDcb djsjNydDcb;
    List<DjsjQszdDcb> djsjQszdDcbList;
    DjsjQszdDcb djsjQszdDcb;
    BdcXm bdcXm;
    List<String> xxlist;
    List<String> bdclxList;
    String messageDjsj;
    String messageGdsj;
    Iterator it;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public HashMap<String, Object> checkDw(Project project, final String xx, final String checktype, final String qllx) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String dw = null;
        String hdxx = "";
        //获取标准单位,当bdxm或project中qllx为空时标准单位取这个
        if (StringUtils.isNotBlank(qllx))
            //通过配置xml文件获取 某权利类型 对应的单位面积
            dw = getBzDw(qllx);

        if (StringUtils.equals(checktype, "0")) {
            map = checkCjDw(project, xx, dw);
            hdxx = checkZfDw(project, xx, dw);
            if (StringUtils.isNotBlank(hdxx)&&map != null) {
                if (map.size() > 0) {
                    hdxx = hdxx + map.get("info").toString();
                    map.put("info", hdxx);
                } else {
                    map.put("info", hdxx);
                }
            }


        } else if (StringUtils.equals(checktype, "1")) {
            map = checkCjDw(project, xx, dw);
        } else {
            String yzxx = checkZfDw(project, xx, dw);
            if (StringUtils.isNotBlank(yzxx))
                map.put("info", yzxx);
        }

        return map;
    }

    //检查选择不动产单元
    public HashMap<String, Object> checkCjDw(Project project, String xx, String bzdw) {
        messageDjsj = "";
        String dw = "";
        messageGdsj = "";
        bdcXm = null;
        xxlist = new ArrayList<String>();
        bdclxList = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        String hdxx = "";
        if (project != null) {
            if (StringUtils.isNotBlank(project.getProid()))
                bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
            if (bdcXm != null)
                project = bdcXmService.getProjectFromBdcXm(bdcXm, project);

            //获取标准单位
            if (StringUtils.isNotBlank(project.getQllx())) {
                dw = getBzDw(project.getQllx());
            } else {
                dw = bzdw;
            }

            if (StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
                //zwq  如果bdcxm中有则优先取
                if (bdcXm != null&&StringUtils.isNotBlank(bdcXm.getBdclx())) {
                    bdclxList.add(bdcXm.getBdclx());
                }
                if (StringUtils.isNotBlank(project.getBdclx()))
                    messageDjsj = getDjsj(project.getDjId(), project.getBdclx(), dw);
                if (StringUtils.isNotBlank(messageDjsj)) {
                    messageDjsj = xx + ":" + messageDjsj;
                    xxlist.add(messageDjsj);
                }
            } else if (StringUtils.equals(project.getXmly(), Constants.XMLY_FWSP) || StringUtils.equals(project.getXmly(), Constants.XMLY_TDSP)&&StringUtils.isNotBlank(project.getGdproid())) {
                List<String> bdcidList = gdXmService.getGdBdcidByProid(project.getGdproid());
                List<InsertVo> insertVoList = getGdsjFromBdcid(bdcidList, project.getBdclx());
                messageGdsj = getGdSj(insertVoList, dw);
                if (StringUtils.isNotBlank(messageGdsj)) {
                    messageGdsj = xx + ":" + messageGdsj;
                    xxlist.add(messageGdsj);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(xxlist)) {
            hdxx = xxlist.get(0);
            map.put("info", hdxx);
        }

        return map;
    }

    //检查转发时的单位
    public String checkZfDw(Project project, String xx, String bzdw) {
        String fhxx = "";
        String dw = "";
        BdcSpxx bdcSpxx = null;
        if (project != null) {
            if (StringUtils.isNotBlank(project.getProid())) {
                bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
                bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
            }

            if (bdcXm != null) {
                if (StringUtils.isNotBlank(bdcXm.getQllx())) {
                    //新建
                    dw = getBzDw(bdcXm.getQllx());
                } else {
                    dw = bzdw;
                }

                if (bdcSpxx != null) {
                    if (StringUtils.isNotBlank(bdcSpxx.getMjdw())) {
                        if (!StringUtils.equals(bdcSpxx.getMjdw(), dw)) {
                            fhxx = xx + ":选择的单位不是标准单位！";
                        }
                    } else {
                        fhxx = xx + ":项目单位不能为空!";
                    }
                }
            }
        }
        return fhxx;
    }

    //通过bdclx从地籍数据中取单位,返回错误信息
    public String getDjsj(String djid, String bdclx, String dw) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String message = "";
        List<String> listDjsj;
        if (StringUtils.isNotBlank(djid)) {
            if (Constants.BDCLX_TDFW.equals(bdclx)) {
                djsjFwxx = djsjFwService.getDjsjFwxx(djid);
                if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && StringUtils.length(djsjFwxx.getBdcdyh()) > 19)
                    djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
                if (CollectionUtils.isNotEmpty(djsjZdxxList))
                    djsjZdxx = djsjZdxxList.get(0);
                if (djsjZdxx != null) {
                    if (StringUtils.isBlank(djsjZdxx.getMjdw()))
                        djsjZdxx.setMjdw("1");
                    hashMap.put("zd_djdcb", djsjZdxx.getMjdw());
                }
            }
            if (Constants.BDCLX_TDSL.equals(bdclx)) {
                djsjLqxx = bdcDjsjService.getDjsjLqxx(djid);
                if (djsjLqxx != null) {
                    if (StringUtils.isBlank(djsjLqxx.getMjdw()))
                        djsjLqxx.setMjdw("2");
                    hashMap.put("lq_dcb", djsjLqxx.getMjdw());

                }
            }
            if (Constants.BDCLX_TD.equals(bdclx)) {
                cbzdDcb = djSjMapper.getDjsjCbzdDcbByDjid(djid);
                if (cbzdDcb != null && StringUtils.isNotBlank(cbzdDcb.getDjh()))
                    djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(cbzdDcb.getDjh());
                if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                    djsjNydDcb = djsjNydDcbList.get(0);
                    if (StringUtils.isBlank(djsjNydDcb.getMjdw()))
                        djsjNydDcb.setMjdw("2");
                    hashMap.put("nyd_djdcb", djsjNydDcb.getMjdw());
                }
            }
            if (bdclx.indexOf(Constants.BDCLX_TD) > -1) {
                djsjQszdDcbList = bdcDjsjService.getDjsjQszdDcb(djid);
                if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
                    djsjQszdDcb = djsjQszdDcbList.get(0);
                    if (StringUtils.isBlank(djsjQszdDcb.getMjdw()))
                        djsjQszdDcb.setMjdw("1");
                    hashMap.put("qszd_djdcb", djsjQszdDcb.getMjdw());
                }
                djsjZdxxList = bdcDjsjService.getDjsjZdxx(djid);
                if (CollectionUtils.isEmpty(djsjZdxxList))
                    djsjZdxxList = bdcDjsjService.getDjsjNydxx(djid);
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    djsjZdxx = djsjZdxxList.get(0);
                    if (StringUtils.isBlank(djsjZdxx.getMjdw()))
                        djsjZdxx.setMjdw("1");
                    hashMap.put("zd_djdcb", djsjZdxx.getMjdw());

                }
            }
        }

        listDjsj = isEqualDw(hashMap, dw);
        if (CollectionUtils.isNotEmpty(listDjsj)) {
            message = "权籍调查库单位有误;";
        }

        return message;
    }

    //从过渡数据中取单位
    public String getGdSj(List<InsertVo> insertVoList, String dw) {
        String message = "";
        HashMap<String, String> hashMapdw = new HashMap<String, String>();
        List<String> listGdSj = null;
        String djmsg = "";
        String bdclx = "";
        for (InsertVo vo : insertVoList) {
            if (vo instanceof GdFw) {
                GdFw gdFw = (GdFw) vo;
                gdFw = bdcCheckCancelService.getGdFwFilterZdsj(gdFw);
                if (StringUtils.isBlank(gdFw.getDw()))
                    gdFw.setDw("1");
                hashMapdw.put("gd_fw", gdFw.getDw());
                bdclx = Constants.BDCLX_TDFW;
            } else if (vo instanceof GdTd) {
                GdTd gdTd = (GdTd) vo;
                gdTd = bdcCheckCancelService.getGdTdFilterZdsj(gdTd);
                if (StringUtils.isBlank(gdTd.getDw()))
                    gdTd.setDw("1");
                hashMapdw.put("gd_td", gdTd.getDw());
                bdclx = Constants.BDCLX_TD;
            } else if (vo instanceof GdLq) {
                GdLq gdLq = (GdLq) vo;
                gdLq = bdcCheckCancelService.getGdLqFilterZdsj(gdLq);
                if (StringUtils.isBlank(gdLq.getDw())) {
                    gdLq.setDw("2");
                    hashMapdw.put("gd_lq", gdLq.getDw());
                }
                bdclx = Constants.BDCLX_LQ;
            } else if (vo instanceof GdCq) {
                GdCq gdCq = (GdCq) vo;
                gdCq = bdcCheckCancelService.getGdCqFilterZdsj(gdCq);
                if (StringUtils.isBlank(gdCq.getDw()))
                    gdCq.setDw("2");
                hashMapdw.put("gd_cq", gdCq.getDw());
                bdclx = Constants.BDCLX_TDQT;
            }

            //地籍数据单位验证
            if (StringUtils.isNotBlank(bdclx)) {
                Method method = AnnotationsUtils.getAnnotationsName(vo);
                String id = null;
                try {
                    if (method.invoke(vo) != null)
                        id = method.invoke(vo).toString();
                } catch (IllegalAccessException e) {
                    logger.error("DwYzServiceImpl.getGdSj",e);
                } catch (InvocationTargetException e) {
                    logger.error("DwYzServiceImpl.getGdSj",e);
                }
                if (StringUtils.isNotBlank(id)) {
                    Example example = new Example(GdDyhRel.class);
                    example.createCriteria().andEqualTo("gdid", id);
                    List<GdDyhRel> gdDyhRelList = entityMapper.selectByExample(example);
                    if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                        for (GdDyhRel gdDyhRel : gdDyhRelList) {
                            if (StringUtils.isNotBlank(gdDyhRel.getDjid())) {
                                djmsg = getDjsj(gdDyhRel.getDjid(), bdclx, dw);
                                if (StringUtils.isNotBlank(djmsg)) {
                                    message = djmsg;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        listGdSj = isEqualDw(hashMapdw, dw);
        if (CollectionUtils.isNotEmpty(listGdSj)) {
            message += "过渡库单位有误！";
        }
        return message;
    }

    //判断djsj单位是否是标准单位，否则将其key放入list中返回
    public List<String> isEqualDw(HashMap<String, String> hashMapHdDw, String dw) {
        List<String> list = new ArrayList<String>();
        boolean isEqual = false;
        if (hashMapHdDw != null && StringUtils.isNotBlank(dw)) {
            it = hashMapHdDw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                if (StringUtils.equals(dw, entry.getValue())) {
                    isEqual = true;
                    break;
                } else {
                    list.add(entry.getKey());
                }
            }
            if (isEqual) {
                list = null;
            }
        }
        return list;
    }


    //获取标准单位
    public String getBzDw(String qllx) {
        String dw = "";
        HashMap<String, String> hashMapBzDw;
        hashMapBzDw = ReadXmlProps.getdwYz();
        if (hashMapBzDw != null) {
            it = hashMapBzDw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                if (StringUtils.equals(entry.getKey(), qllx)) {
                    dw = entry.getValue();
                }
            }
            if (StringUtils.isBlank(dw))
                dw = "1";
        }
        return dw;
    }

    public List<InsertVo> getGdsjFromBdcid(List<String> bdcidList, String bdclx) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (CollectionUtils.isNotEmpty(bdcidList)) {
            for (String bdcid : bdcidList) {
                if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                    GdFw gdFw = entityMapper.selectByPrimaryKey(GdFw.class, bdcid);
                    if (gdFw != null)
                        insertVoList.add(gdFw);
                } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                    GdTd gdTd = entityMapper.selectByPrimaryKey(GdTd.class, bdcid);
                    if (gdTd != null) {
                        insertVoList.add(gdTd);
                    }
                } else if (StringUtils.equals(bdclx, Constants.BDCLX_LQ)) {
                    GdLq gdLq = entityMapper.selectByPrimaryKey(GdLq.class, bdcid);
                    if (gdLq != null) {
                        insertVoList.add((InsertVo) gdLq);
                    }
                } else {
                    GdCq gdCq = entityMapper.selectByPrimaryKey(GdCq.class, bdcid);
                    if (gdCq != null)
                        insertVoList.add((InsertVo) gdCq);
                }

            }
        }
        return insertVoList;
    }

}