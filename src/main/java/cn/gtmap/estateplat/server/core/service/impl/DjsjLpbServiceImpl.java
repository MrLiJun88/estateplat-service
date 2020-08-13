package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDjsjMapper;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.core.service.DjsjLpbService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zx on 2015/6/15.
 */
@Service
public class DjsjLpbServiceImpl implements DjsjLpbService {
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private DjSjMapper djsjMapper;
    @Autowired
    private BdcDjsjMapper bdcDjsjMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    private static final String PARAMETER_BDCDY = "BDCDY";


    @Override
    public String getLpbMenu(final String zdzhh) {
        String xml = "";
        Document doc = null;
        if (StringUtils.isNotBlank(zdzhh)) {
            HashMap map = new HashMap();
            map.put("lszd", zdzhh);
            map.put("orderByStr", "t.lszd,t.zrzh");
            List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(map);
            List<String> zrzhzdList = new ArrayList<String>();

            if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
                //房屋
                doc = DocumentHelper.createDocument();

                Element lszdEl = doc.addElement("LSZD");
                lszdEl.addAttribute("DJH", zdzhh);

                //取土地权利
                List<BdcBdcdy> bdcBdcdyList = bdcdyService.getBdcdyByZdzhh(zdzhh, Constants.BDCLX_TD);
                if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {
                    for (BdcBdcdy bdcBdcdy : bdcBdcdyList) {
                        Element tdEl = lszdEl.addElement("TD");
                        tdEl.addAttribute(ParamsConstants.BDCDYH_CAPITAL, bdcBdcdy.getBdcdyh());
                        String zl = "";
                        BdcSpxx bdcSpxx = bdcSpxxService.getBdcSpxxByBdcdyh(bdcBdcdy.getBdcdyh());
                        if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getZl()))
                            zl = bdcSpxx.getZl();
                        tdEl.addAttribute("DZ", zl);
                    }
                }
                //zx多幢读取项目信息中项目名称
                if(StringUtils.equals(djsjFwLjzList.get(0).getBdcdyfwlx(), "1")){
                    Element bdcdyEl = lszdEl.addElement(PARAMETER_BDCDY);
                    if(StringUtils.isNotBlank(djsjFwLjzList.get(0).getFwXmxxIndex())){
                        HashMap map1= Maps.newHashMap();
                        map1.put("fw_xmxx_index",djsjFwLjzList.get(0).getFwXmxxIndex());
                        List<DjsjFwXmxx> djsjFwXmxxList=djsjFwService.getDjsjFwXmxx(map1);
                        if(CollectionUtils.isNotEmpty(djsjFwXmxxList)) {
                            bdcdyEl.addAttribute(ParamsConstants.BDCDYH_CAPITAL, djsjFwXmxxList.get(0).getBdcdyh());
                            bdcdyEl.addAttribute("DZ", djsjFwXmxxList.get(0).getXmmc());
                        }
                    }

                }else {
                    //除了多幢
                    Element fwEl = null;

                    for (DjsjFwLjz djsjFwLjz : djsjFwLjzList) {
                        if (StringUtils.isNotBlank(djsjFwLjz.getZrzh())) {
                            if (!StringUtils.equals(djsjFwLjz.getBdcdyfwlx(), "1")&&!zrzhzdList.contains(djsjFwLjz.getZrzh())) {
                                fwEl = lszdEl.addElement("FW");
                                fwEl.addAttribute("FWMC", djsjFwLjz.getFwmc());
                                fwEl.addAttribute("ZRZH", djsjFwLjz.getZrzh());
                                zrzhzdList.add(djsjFwLjz.getZrzh());
                            }
                            if(fwEl != null){
                                Element ljzEl = fwEl.addElement("LJZ");
                                if (StringUtils.isNotBlank(djsjFwLjz.getFwmc()))
                                    ljzEl.addAttribute("FWMC", djsjFwLjz.getFwmc());
                                else
                                    ljzEl.addAttribute("FWMC", djsjFwLjz.getZldz());
                                ljzEl.addAttribute("KEYCode", djsjFwLjz.getFwDcbIndex());
                            }

                        }
                    }
                }
            } else {
                //土地
                List<DjsjZdxx> getDjsjZdxxList = djsjMapper.getDjsjZdxxByDjh(zdzhh);
                List<DjsjZdxx> djsjZdxxList = new ArrayList<DjsjZdxx>();
                if (getDjsjZdxxList != null && getDjsjZdxxList.size() > 1)
                    djsjZdxxList.add(getDjsjZdxxList.get(0));
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    doc = DocumentHelper.createDocument();
                    for (DjsjZdxx djzdxx : djsjZdxxList) {
                        Element bdcdyEl = doc.addElement(PARAMETER_BDCDY);
                        bdcdyEl.addAttribute(ParamsConstants.BDCDYH_CAPITAL, djzdxx.getBdcdyh());
                        bdcdyEl.addAttribute("DZ", djzdxx.getTdzl());
                    }
                } else {

                    //                    林权权土地
                    List<DjsjLqxx> getDjsjLqxxList = bdcDjsjMapper.getDjsjLqxxByDjh(zdzhh);
                    List<DjsjLqxx> djsjLqxxList = new ArrayList<DjsjLqxx>();
                    if (getDjsjLqxxList != null && getDjsjLqxxList.size() > 1)
                        djsjLqxxList.add(getDjsjLqxxList.get(0));
                    if (CollectionUtils.isNotEmpty(djsjLqxxList)) {
                        doc = DocumentHelper.createDocument();
                        for (DjsjLqxx djzdxx : djsjLqxxList) {
                            Element bdcdyEl = doc.addElement(PARAMETER_BDCDY);
                            bdcdyEl.addAttribute(ParamsConstants.BDCDYH_CAPITAL, djzdxx.getBdcdyh());
                            bdcdyEl.addAttribute("DZ", djzdxx.getXdm());
                        }
                    } else {
                        //                    所有权土地
                        List<DjsjQszdDcb> getDjsjQszdDcbList = djsjMapper.getDjsjQszdDcb(zdzhh);

                        List<DjsjQszdDcb> djsjQszdDcbList = new ArrayList<DjsjQszdDcb>();
                        if (getDjsjQszdDcbList != null && getDjsjQszdDcbList.size() > 1)
                            djsjQszdDcbList.add(getDjsjQszdDcbList.get(0));

                        if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
                            doc = DocumentHelper.createDocument();
                            for (DjsjQszdDcb djzdxx : djsjQszdDcbList) {
                                Element bdcdyEl = doc.addElement(PARAMETER_BDCDY);
                                bdcdyEl.addAttribute(ParamsConstants.BDCDYH_CAPITAL, djzdxx.getBdcdyh());
                                bdcdyEl.addAttribute("DZ", djzdxx.getTdzl());
                            }
                        } else {
                            //                    承包土地
                            List<DjsjCbzdDcb> getDjsjCbzdDcbList = djsjMapper.getDjsjCbzdDcbByDjh(zdzhh);

                            List<DjsjCbzdDcb> djsjCbzdDcbList = new ArrayList<DjsjCbzdDcb>();
                            if (getDjsjCbzdDcbList != null && getDjsjCbzdDcbList.size() > 1)
                                djsjCbzdDcbList.add(getDjsjCbzdDcbList.get(0));

                            if (CollectionUtils.isNotEmpty(djsjCbzdDcbList)) {
                                doc = DocumentHelper.createDocument();
                                for (DjsjCbzdDcb djzdxx : djsjCbzdDcbList) {
                                    Element bdcdyEl = doc.addElement(PARAMETER_BDCDY);
                                    bdcdyEl.addAttribute(ParamsConstants.BDCDYH_CAPITAL, djzdxx.getBdcdyh());
                                    bdcdyEl.addAttribute("DZ", "");
                                }
                            }
                        }
                    }

                }
            }
        }
        if (doc != null)
            xml = doc.asXML();
        return xml;
    }
}
