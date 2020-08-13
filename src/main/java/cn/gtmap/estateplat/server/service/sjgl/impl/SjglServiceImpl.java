package cn.gtmap.estateplat.server.service.sjgl.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.BdcBdcdjb;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.model.vo.SjglData;
import cn.gtmap.estateplat.server.core.model.vo.SjglRequest;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdjbService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.service.sjgl.SjglService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
import cn.gtmap.estateplat.server.sj.yw.BdcTsYwService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-13
 * @description
 */
@Service
public class SjglServiceImpl implements SjglService {
    @Resource(name = "bdcDyYwServiceImpl")
    private BdcDataYwService bdcDyYwServiceImpl;
    @Resource(name = "bdcTsYwDjbTdServiceImpl")
    private BdcTsYwService bdcTsYwDjbTdServiceImpl;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;

    /**
     * @param sjglRequest
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取单元信息
     */
    @Override
    public Map getDyh(SjglRequest sjglRequest) {
        Map map = Maps.newHashMap();
        if (sjglRequest != null && sjglRequest.getData() != null) {
            SjglData sjglData = sjglRequest.getData();
            if (StringUtils.isNotBlank(sjglData.getBdcdyh()) && StringUtils.isNotBlank(sjglData.getBdclx()) && StringUtils.isNotBlank(sjglData.getDjid())) {
                List<ProjectPar> projectParList = Lists.newArrayList();
                ProjectPar projectPar = new ProjectPar();
                projectPar.setBdclx(sjglData.getBdclx());
                projectPar.setQjid(sjglData.getDjid());
                projectPar.setBdcdyh(sjglData.getBdcdyh());
                projectParList.add(projectPar);
                projectPar.setDjh(StringUtils.substring(projectPar.getBdcdyh(), 0, 19));
                List<BdcBdcdjb> bdcBdcdjbList = bdcDjbService.selectBdcdjb(projectPar.getDjh());
                if (CollectionUtils.isNotEmpty(bdcBdcdjbList)) {
                    projectPar.setDjbid(bdcBdcdjbList.get(0).getDjbid());
                }
                bdcDyYwServiceImpl.initbdcData(projectPar, null);
                bdcTsYwDjbTdServiceImpl.initTsYw(projectParList);
                if (projectPar.getBdcBdcdy() != null) {
                    map.put(StringUtils.lowerCase(projectPar.getBdcBdcdy().getClass().getAnnotation(Table.class).name()), projectPar.getBdcBdcdy());
                }
                if (projectPar.getBdcBdcdjb() != null) {
                    map.put(StringUtils.lowerCase(projectPar.getBdcBdcdjb().getClass().getAnnotation(Table.class).name()), projectPar.getBdcBdcdjb());
                }
                if (projectPar.getBdcTd() != null) {
                    map.put(StringUtils.lowerCase(projectPar.getBdcTd().getClass().getAnnotation(Table.class).name()), projectPar.getBdcTd());
                }
            }
        }
        return map;
    }

    /**
     * @param sjglRequest
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息信息
     */
    @Override
    public Map getQl(SjglRequest sjglRequest) {
        Map returnMap = Maps.newHashMap();
        if (sjglRequest != null && sjglRequest.getData() != null) {
            SjglData sjglData = sjglRequest.getData();
            if (StringUtils.isNotBlank(sjglData.getBdcdyh())) {
                List<Map> mapList = bdcXmService.getXsxxByBdcdyh(sjglData.getBdcdyh());
                if (CollectionUtils.isNotEmpty(mapList)) {
                    int fdcqcount = 0;
                    for (Map map : mapList) {
                        if (CommonUtil.indexOfStrs(Constants.SQLX_CQ, CommonUtil.formatEmptyValue(map.get("QLLX")))) {
                            if (StringUtils.equals(CommonUtil.formatEmptyValue(map.get("SFCL")), "1")) {
                                BdcXm bdcXm = bdcXmService.getBdcXmByProid(CommonUtil.formatEmptyValue(map.get("PROID")));
                                if (bdcXm != null) {
                                    QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                                    if (qllxVo != null) {
                                        Map qllxMap = Maps.newHashMap();
                                        qllxMap.put(StringUtilMin(qllxVo.getClass().getSimpleName()), qllxVo);
                                        returnMap.put(StringUtils.lowerCase(qllxVo.getClass().getAnnotation(Table.class).name()), qllxMap);
                                    }
                                }
                            } else {
                                throw new AppException("不动产单元现势产权不是存量数据，不允许修改");
                            }
                        } else {
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(CommonUtil.formatEmptyValue(map.get("PROID")));
                            if (bdcXm != null) {
                                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                                if (qllxVo != null) {
                                    List<Map> qllxMapList = Lists.newArrayList();
                                    String key = StringUtils.lowerCase(qllxVo.getClass().getAnnotation(Table.class).name());
                                    if (returnMap.containsKey(key)) {
                                        qllxMapList = (List<Map>) returnMap.get(key);
                                    }
                                    Map qllxMap = Maps.newHashMap();
                                    qllxMap.put(StringUtilMin(qllxVo.getClass().getSimpleName()), qllxVo);
                                    qllxMapList.add(qllxMap);
                                    returnMap.put(key, qllxMapList);
                                }
                            }
                        }
                        if (StringUtils.equals(CommonUtil.formatEmptyValue(map.get("QLLX")), Constants.QLLX_GYTD_FWSUQ)) {
                            fdcqcount++;
                        }
                        if (fdcqcount > 1) {
                            throw new AppException("不动产单元有多条现势产权，不允许修改");
                        }

                    }
                }
            }
        }
        return returnMap;
    }


    private String StringUtilMin(String str){
        //如果字符串str为null和""则返回原数据
        if (str==null||"".equals(str)){
            return str ;
        }
        if(str.length()==1){
            //如果字符串str的长度为1，则调用专门把字符串转换为小写的string方法tuUpperCase()
            return str.toLowerCase() ;
        }
        //用字符串截取方法subString()截取第一个字符并调用toUpperCase()方法把它转换为小写字母
        //再与从str第二个下标截取的字符串拼接
        return str.substring(0,1).toLowerCase()+str.substring(1) ;
    }
}

