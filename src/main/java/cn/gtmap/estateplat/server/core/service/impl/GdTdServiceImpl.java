package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdTdMapper;
import cn.gtmap.estateplat.server.core.mapper.GdXmMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过渡土地
 * Created by lst on 2015/3/17.
 */
@Repository
public class GdTdServiceImpl implements GdTdService {
    @Autowired
    GdTdMapper gdTdMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdXmMapper gdXmMapper;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    GdQlrService gdQlrService;
    @Autowired
    private BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    private GdFwService gdFwService;


    public GdTd queryGdTdByProid(final String proid) {
        GdTd gdTd = null;
        if(StringUtils.isNotBlank(proid)) {
            Example gdTdExample = new Example(GdTd.class);
            gdTdExample.createCriteria().andEqualTo("proid", proid);
            List<GdTd> gdTdList = entityMapper.selectByExample(GdTd.class, gdTdExample);
            if(CollectionUtils.isNotEmpty(gdTdList))
                gdTd = gdTdList.get(0);
        }
        return gdTd;
    }

    @Override
    public List<Map> getGdTdJson(final HashMap map) {
        return gdTdMapper.getGdTdJsonByPage(map);
    }


    @Override
    public GdTd queryGdTd(final String tdId) {
        GdTd gdTd = null;
        if(StringUtils.isNotBlank(tdId)) {

            gdTd = entityMapper.selectByPrimaryKey(GdTd.class, tdId);
        }
        return gdTd;
    }

    @Override
    public GdTd queryGdTdByTdzh(final String tdzh) {
        GdTd gdTd = null;
        if(StringUtils.isNotBlank(tdzh)) {
            Example example = new Example(GdTdsyq.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("tdzh", tdzh);
            List<GdTdsyq> gdTdsyqList = entityMapper.selectByExample(GdTdsyq.class, example);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)&&StringUtils.isNotBlank(gdTdsyqList.get(0).getTdid())) {
                gdTd = entityMapper.selectByPrimaryKey(GdTd.class, gdTdsyqList.get(0).getTdid());
            }
        }
        return gdTd;
    }

    @Override
    public BdcTdsyq readBdcTdsyqFromGdTd(GdTd gdTd, GdTdsyq gdTdsyq, BdcTdsyq bdcTdsyq) {
        if(gdTd != null) {
            if(bdcTdsyq == null)
                bdcTdsyq = new BdcTdsyq();
            if(StringUtils.isNotBlank(gdTd.getGyqk()))
                bdcTdsyq.setGyqk(gdTd.getGyqk());

            if(gdTdsyq != null) {
                bdcTdsyq.setNydmj(gdTdsyq.getNyd());
                bdcTdsyq.setJsydmj(gdTdsyq.getJsyd());
                bdcTdsyq.setWlydmj(gdTdsyq.getWlyd());
                bdcTdsyq.setGdmj(gdTdsyq.getGd());
                bdcTdsyq.setLdmj(gdTdsyq.getLd());
                bdcTdsyq.setCdmj(gdTdsyq.getMcd());
                bdcTdsyq.setQtmj(gdTdsyq.getQtnyd());
            }
        }
        return bdcTdsyq;
    }


    @Override
    public BdcJsydzjdsyq readBdcJsydzjdsyqFromGdTd(GdTd gdTd, BdcJsydzjdsyq bdcJsydzjdsyq) {
        Date date = null;
        if(gdTd != null) {
            if(bdcJsydzjdsyq == null)
                bdcJsydzjdsyq = new BdcJsydzjdsyq();
            if(StringUtils.isNotBlank(gdTd.getGyqk()))
                bdcJsydzjdsyq.setGyqk(gdTd.getGyqk());
            if(gdTd.getQdjg() != null)
                bdcJsydzjdsyq.setQdjg(gdTd.getQdjg());

            if(StringUtils.isNotBlank(gdTd.getQsrq()) && StringUtils.isNotBlank(gdTd.getZzrq())) {
                bdcJsydzjdsyq.setSyksqx(CalendarUtil.formatDate(gdTd.getQsrq()));
                bdcJsydzjdsyq.setSyjsqx(CalendarUtil.formatDate(gdTd.getZzrq()));
            }
            if(StringUtils.isBlank(gdTd.getQsrq()) && StringUtils.isNotBlank(gdTd.getZzrq())) {
                date = gdXmService.getRqFromYt(null, gdTd.getZzrq(), gdTd.getYt(), gdTd.getSyqlx());
                bdcJsydzjdsyq.setSyksqx(date);
                bdcJsydzjdsyq.setSyjsqx(CalendarUtil.formatDate(gdTd.getZzrq()));
            }
            if(StringUtils.isNotBlank(gdTd.getQsrq()) && StringUtils.isBlank(gdTd.getZzrq())) {
                date = gdXmService.getRqFromYt(gdTd.getQsrq(), null, gdTd.getYt(), gdTd.getSyqlx());
                bdcJsydzjdsyq.setSyksqx(CalendarUtil.formatDate(gdTd.getQsrq()));
                bdcJsydzjdsyq.setSyjsqx(date);
            }

            if(gdTd.getZdmj() != null)
                bdcJsydzjdsyq.setSyqmj(gdTd.getZdmj());
        }
        return bdcJsydzjdsyq;
    }

    @Override
    public List<GdTdsyq> queryTdsyqByFwid(final String fwid) {
        List<GdTdsyq> gdTdsyqList = null;
        if(StringUtils.isNotBlank(fwid)) {
            Example example = new Example(GdTdsyq.class);
            example.createCriteria().andEqualTo("fwid", fwid);
            gdTdsyqList = entityMapper.selectByExampleNotNull(GdTdsyq.class, example);
        }
        return gdTdsyqList;
    }

    @Override
    public List<GdTdsyq> queryTdsyqByTdid(final String tdid) {
        List<GdTdsyq> list = null;
        if(StringUtils.isNotBlank(tdid)) {
            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(tdid, null);
            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                list = new ArrayList<GdTdsyq>();
                for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    GdTdsyq gdTdsyq = getGdTdsyqByQlid(gdBdcQlRel.getQlid());
                    if(gdTdsyq != null)
                        list.add(gdTdsyq);
                }
            }
        }

        return list;
    }

    @Override
    public List<GdDy> queryTddyqByTdid(final String tdid, final Integer iszx) {
        List<GdDy> list = null;
        if(StringUtils.isNotBlank(tdid)) {
            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(tdid, null);
            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                list = new ArrayList<GdDy>();
                for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    GdDy gdDy = getGddyqByQlid(gdBdcQlRel.getQlid(),iszx);
                    if(gdDy != null) {
                        list.add(gdDy);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<BdcQlr> getQlrFromGdTd(final String tdid, final String qlid) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if(StringUtils.isNotBlank(qlid)) {
            //zdd 过度抵押权转移注销登记时   直接通过权利ID读取权利人
            List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(qlid, Constants.QLRLX_QLR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        } else if(StringUtils.isNotBlank(tdid)) {
            List<GdTdsyq> gdTdsyqList = queryTdsyqByTdid(tdid);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)) {
                for(GdTdsyq gdTdsyq : gdTdsyqList) {
                    List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(gdTdsyq.getQlid(), Constants.QLRLX_QLR);
                    bdcQlrList.addAll(gdQlrService.readGdQlrs(gdQlrs));
                }
            }
        }
        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> getYwrFromGdTd(final String tdid, final String qlid) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if(StringUtils.isNotBlank(qlid)) {
            //zdd 过度抵押权转移注销登记时   直接通过权利ID读取权利人
            List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(qlid, Constants.QLRLX_YWR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        } else if(StringUtils.isNotBlank(tdid)) {
            List<GdTdsyq> gdTdsyqList = queryTdsyqByTdid(tdid);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)) {
                for(GdTdsyq gdTdsyq : gdTdsyqList) {
                    List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(gdTdsyq.getQlid(), Constants.QLRLX_YWR);
                    bdcQlrList.addAll(gdQlrService.readGdQlrs(gdQlrs));
                }
            }
        }
        return bdcQlrList;
    }

    @Override
    public List<GdQlr> getQlrByName(final String qlrname) {
        return gdTdMapper.getQlrByName(qlrname);
    }

    @Override
    public String[] getQlrByNameToArr(final String qlrname) {
        List<String> idList = new ArrayList<String>();
        String[] ids = null;
        if(StringUtils.isNotBlank(qlrname)) {
            List<GdQlr> gdQlrList = getQlrByName(qlrname);
            if(CollectionUtils.isNotEmpty(gdQlrList)) {
                for(GdQlr gdQlr : gdQlrList) {
                    if(StringUtils.isNotBlank(CommonUtil.formatEmptyValue(gdQlr.getQlid())) && !idList.contains(CommonUtil.formatEmptyValue(gdQlr.getQlid())))
                        idList.add(CommonUtil.formatEmptyValue(gdQlr.getQlid()));
                }
            }
        }
        if(CollectionUtils.isNotEmpty(idList)) {
            ids = new String[idList.size()];
            int i = 0;
            for(String id : idList) {
                ids[i] = id;
                i++;
            }
        }
        return ids;
    }

    @Override
    public String getqlrByQlid(final String qlid) {
        StringBuilder qlr = new StringBuilder();
        List<GdQlr> qlrList = gdQlrService.queryGdQlrs(qlid, Constants.QLRLX_QLR);
        if(CollectionUtils.isNotEmpty(qlrList)) {
            for(GdQlr qlr1 : qlrList) {
                if(StringUtils.isBlank(qlr)) {
                    qlr.append(CommonUtil.formatEmptyValue(qlr1.getQlr()));
                }else {
                    qlr.append("/").append(CommonUtil.formatEmptyValue(qlr1.getQlr()));
                }
            }
        }
        return qlr.toString();
    }

    @Override
    public String getTdzhsByProid(final String proid) {
        StringBuilder tdzhs = new StringBuilder();
        if(StringUtils.isNotBlank(proid)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for(BdcXmRel bdcXmRel : bdcXmRelList) {
                    if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        String bdcid = "";
                        List<String> bdcidList = gdXmService.getGdBdcidByProid(bdcXmRel.getYproid());
                        if(CollectionUtils.isNotEmpty(bdcidList)) {
                            bdcid = bdcidList.get(0);
                        }
                        List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel(null, bdcid);
                        if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                            for(BdcGdDyhRel gdDyhRel : gdDyhRelList) {
                                if(gdDyhRel != null && StringUtils.isNotBlank(gdDyhRel.getGdid())) {
                                    List<GdTdsyq> gdTdsyqList = gdTdMapper.getGdTdSyq(gdDyhRel.getGdid());
                                    GdTdsyq gdTdsyq = null;
                                    if(CollectionUtils.isNotEmpty(gdTdsyqList))
                                        gdTdsyq = gdTdsyqList.get(0);
                                    if(gdTdsyq != null && StringUtils.indexOf(tdzhs, gdTdsyq.getTdzh()) < 0) {
                                        if(StringUtils.isBlank(tdzhs)) {
                                            tdzhs.append(gdTdsyq.getTdzh());
                                        }else {
                                            tdzhs.append(",").append(gdTdsyq.getTdzh());
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        return tdzhs.toString();
    }

    @Override
    public String getDhDzbDjhByTdid(final String tdid) {
        return gdTdMapper.getDhDzbDjhByTdid(tdid);
    }

    @Override
    public String getZdDcbDjhByTdid(final String tdid) {
        return gdTdMapper.getZdDcbDjhByTdid(tdid);
    }

    @Override
    public String getNewDjh(final String tdzdz, final String tdid) {
        String djh = "";
        if(StringUtils.isNotBlank(tdzdz)) {
            if(StringUtils.equals(tdzdz, "zddcb"))
                djh = getZdDcbDjhByTdid(tdid);
            else if(StringUtils.equals(tdzdz, "dhdzb"))
                djh = getDhDzbDjhByTdid(tdid);
        }
        return djh;
    }

    @Override
    public String getTdzhByTdid(final String tdid) {
        return gdTdMapper.getTdzhByTdid(tdid);
    }

    @Override
    public String getBdcdyhByTdids(final Map map) {
        return gdTdMapper.getBdcdyhByTdids(map);
    }

    @Override
    public List<GdTd> getGdTdyTdids(final Map map) {
        List<GdTd> gdTds = new ArrayList<GdTd>();
        List<GdTd> gdTdList = gdTdMapper.getGdTdyTdids(map);
        if(CollectionUtils.isNotEmpty(gdTdList)) {
            for(GdTd gdTd : gdTdList) {
                gdTd = bdcCheckCancelService.getGdTdFilterZdsj(gdTd);
                gdTds.add(gdTd);
            }
        }

        return gdTds;
    }

    @Override
    public String getTdzsZt(final String bdcid, final String dyid) {
        String xx = "";
        //获取纯土地匹配证书状态
        xx = queryTdZtbyBdcdyh(bdcid, false);

        //获取房屋匹配的土地证书状态
        if(StringUtils.isBlank(xx)) {
            xx = queryTdZtbyBdcdyh(bdcid, true);
        }

        List<GdCf> cfList = null;
        List<GdDy> dyList = null;
        List<GdYg> ygList = null;

        //zwq 先取不动产中状态，再取过度状态
        if(StringUtils.isBlank(xx)) {
            //获取预告数据
            ygList = bdcCheckCancelService.getGdYgByProid(bdcid, 0);
            if(CollectionUtils.isNotEmpty(ygList)) {
                xx = "预告";
            }
            //获取抵押数据
            dyList = bdcCheckCancelService.getGdDyByProid(bdcid, 0);
            if(CollectionUtils.isNotEmpty(dyList)) {
                xx = "抵押";
            }
            //获取查封数据
            cfList = bdcCheckCancelService.getGdCfByProid(bdcid, 0);
            if(CollectionUtils.isNotEmpty(cfList)) {
                xx = "查封";
            }

            if(StringUtils.isNotBlank(dyid)) {
                GdDy gdDy = gdFwService.getGdDyByDyid(dyid, null);
                if(gdDy != null && gdDy.getIsjy() == 1)
                    xx = "注销";
            }
        }

        return xx;
    }

    //zwq 通过单元号查询土地证书状态
    public String queryTdZtbyBdcdyh(String tdid, boolean isfw) {
        String xx = "";
        List<QllxParent> qllxList = null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(tdid);

        if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
            for(BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                if(StringUtils.isNotBlank(xx)) {
                    break;
                }
                if(StringUtils.isNotBlank(bdcGdDyhRel.getBdcdyh())) {
                    map.put("bdcdyh", bdcGdDyhRel.getBdcdyh());
                    map.put("qszt", "1");
                    //预告
                    qllxList = qllxParentService.queryZtzcQllxVo(new BdcYg(), map);
                    if(CollectionUtils.isNotEmpty(qllxList)) {
                        xx = "预告";
                    }
                    //抵押
                    qllxList = qllxParentService.queryZtzcQllxVo(new BdcDyaq(), map);
                    if(CollectionUtils.isNotEmpty(qllxList)) {
                        xx = "抵押";
                    }
                    //查封
                    qllxList = qllxParentService.queryZtzcQllxVo(new BdcCf(), map);
                    if(CollectionUtils.isNotEmpty(qllxList)) {
                        xx = "查封";
                    }
                    //注销
                    map.clear();
                    map.put("bdcdyh", bdcGdDyhRel.getBdcdyh());
                    map.put("qszt", "2");
                    qllxList = qllxParentService.queryZtzcQllxVo(new BdcJsydzjdsyq(), map);
                    if(CollectionUtils.isNotEmpty(qllxList)) {
                        xx = "注销";
                    } else {
                        qllxList = qllxParentService.queryZtzcQllxVo(new BdcTdcbnydsyq(), map);
                        if(CollectionUtils.isNotEmpty(qllxList))
                            xx = "注销";
                    }
                }
            }
        }


        return xx;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdTdsyq> getGdTdsyqListByGdproid(final String gdProid, final Integer iszx) {
        List<GdTdsyq> gdTdsyqList = null;
        if(StringUtils.isNotBlank(gdProid)) {
            HashMap map = new HashMap();
            map.put("proid", gdProid);
            map.put("iszx", iszx);
            gdTdsyqList = andEqualQueryGdTdsyq(map);
        }
        return gdTdsyqList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdTdsyq> andEqualQueryGdTdsyq(final Map<String, Object> map) {
        List<GdTdsyq> list = null;
        Example qllxTemp = new Example(GdTdsyq.class);
        if(map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if(val != null)
                    criteria.andEqualTo(key.toString(), val);
            }
        }
        if(CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdTdsyq.class, qllxTemp);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdTd> getGdTdListByQlid(final String qlid) {
        List<GdTd> gdTdList = null;
        if(StringUtils.isNotBlank(qlid)) {
            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(null, qlid);
            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                gdTdList = new ArrayList<GdTd>();
                for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    if(StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                        GdTd gdTd = queryGdTd(gdBdcQlRel.getBdcid());
                        if(gdTd != null)
                            gdTdList.add(gdTd);
                    }
                }
            }
        }
        return gdTdList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdTd> getGdTdListByProid(final String proid, String qlids) {
        List<GdTd> gdTdList = null;
        if(StringUtils.isBlank(qlids))
            qlids = gdFwService.getGdFwQlidsByProid(proid);
        if(StringUtils.isNotBlank(qlids)) {
            gdTdList = new ArrayList<GdTd>();
            for(String qlid : qlids.split(",")) {
                List<GdTd> tdList = getGdTdListByQlid(qlid);
                if(CollectionUtils.isNotEmpty(tdList))
                    gdTdList.addAll(tdList);
            }
        }
        return gdTdList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdTdsyq> andLikeQueryGdTdsyq(final Map<String, Object> map) {
        List<GdTdsyq> list = null;
        Example qllxTemp = new Example(GdTdsyq.class);
        if(map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if(val != null)
                    criteria.andLike(key.toString(), val.toString());
            }
        }
        if(CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdTdsyq.class, qllxTemp);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public GdTdsyq getGdTdsyqByQlid(final String qlid) {
        GdTdsyq gdTdsyq = null;
        if(StringUtils.isNotBlank(qlid)) {
            HashMap map = new HashMap();
            map.put("qlid", qlid);
            List<GdTdsyq> gdTdsyqList = andEqualQueryGdTdsyq(map);
            if(CollectionUtils.isNotEmpty(gdTdsyqList))
                gdTdsyq = gdTdsyqList.get(0);
        }
        return gdTdsyq;
    }

    public GdDy getGddyqByQlid(final String qlid, final Integer iszx) {
        GdDy gdDy = null;
        if(StringUtils.isNotBlank(qlid)) {
            HashMap map = new HashMap();
            if (iszx != null) {
                map.put("iszx", iszx);
            }
            map.put("dyid", qlid);
            List<GdDy> gdDyList = andEqualQueryGdDy(map);
            if(CollectionUtils.isNotEmpty(gdDyList)) {
                gdDy = gdDyList.get(0);
            }
        }
        return gdDy;
    }

    public List<GdDy> andEqualQueryGdDy(Map<String, Object> map) {
        List<GdDy> list = null;
        Example qllxTemp = new Example(GdDy.class);
        if(map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = qllxTemp.createCriteria();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                //zhouwanqing 通过这个判断现势或历史
                if(StringUtils.equals(CommonUtil.formatEmptyValue(key), "iszx")) {
                    if(val != null && NumberUtils.isNumber(CommonUtil.formatEmptyValue(val))) {
                        criteria.andEqualNvlTo("isjy", val, 0);
                    }
                } else if(val != null) {
                    criteria.andEqualTo(key.toString(), val);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(GdDy.class, qllxTemp);

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdTd> getGdTdListByFwQlid(final String qlid) {
        List<GdTd> gdTdList = null;
        if(StringUtils.isNotBlank(qlid)) {
            List<GdFw> gdFwList = gdFwService.getGdFwByQlid(qlid);
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                for(GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> gdBdcQlRelList = bdcGdDyhRelService.getGdDyhRel(null, gdFw.getFwid());
                    if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        gdTdList = new ArrayList<GdTd>();
                        for(BdcGdDyhRel bdcGdDyhRel : gdBdcQlRelList) {
                            if(StringUtils.isNotBlank(bdcGdDyhRel.getTdid())) {
                                GdTd gdTd = queryGdTd(bdcGdDyhRel.getTdid());
                                if(gdTd != null)
                                    gdTdList.add(gdTd);
                            }
                        }
                    }
                }
            }
        }
        return gdTdList;
    }


    @Override
    public List<GdTdsyq> getTdsyqByFczh(final Map hashMap) {
        return gdTdMapper.getTdsyqByFczh(hashMap);
    }

    public String getGdTdQlidByFwQlid(String qlid) {
        return gdTdMapper.getGdTdQlidByFwQlid(qlid);
    }

    @Override
    public List<GdDy> getTdDyByFczh(final Map hashMap) {
        return gdTdMapper.getTdDyByFczh(hashMap);
    }

    @Override
    public List<GdTd> getGdTdByQlid(final String qlid) {
        return gdTdMapper.getGdTdByQlid(qlid);
    }

    @Override
    public List<String> getGdTdListByZl(String zl) {
        List<String> gdTdList = null;
        if(StringUtils.isNotBlank(zl)) {
            gdTdList = gdTdMapper.getGdTdQlidByZl(zl);
        }
        return gdTdList;
    }

    @Override
    public List<String> getGdTdQlidByDjh(final String djh) {
        return gdTdMapper.getGdTdQlidByDjh(djh);
    }

    @Override
    public List<GdTdsyq> getGdTdsyqByFwQlid(final String qlid) {
        List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
        if(StringUtils.isNotBlank(qlid)) {
            List<GdFw> gdFwList = gdFwService.getGdFwByQlid(qlid);
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                for(GdFw gdFw : gdFwList) {
                    List<GdTd> gdTdList = getGdTdByGdDyhFwid(gdFw.getFwid());
                    if(CollectionUtils.isNotEmpty(gdTdList)) {
                        for(GdTd gdTd : gdTdList) {
                            List<GdTdsyq> gdTdsyqLists = queryTdsyqByTdid(gdTd.getTdid());
                            //一个土地只有一个gdTdsyq
                            if(CollectionUtils.isNotEmpty(gdTdsyqLists)) {
                                gdTdsyqList.add(gdTdsyqLists.get(0));
                            }
                        }
                    }
                }
            }
        }
        return gdTdsyqList;
    }

    @Override
    public List<GdTd> getGdTdByGdDyhFwid(final String fwid) {
        List<GdTd> gdTdList = new ArrayList<GdTd>();
        List<String> tdidList = new ArrayList<String>();
        if(StringUtils.isNotBlank(fwid)) {
            Example example = new Example(BdcGdDyhRel.class);
            example.createCriteria().andEqualTo("gdid", fwid);
            List<BdcGdDyhRel> gdDyhRelList = entityMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                for(BdcGdDyhRel bdcGdDyhRel : gdDyhRelList) {
                    if(StringUtils.isNotBlank(bdcGdDyhRel.getTdid()) && !tdidList.contains(bdcGdDyhRel.getTdid())) {
                        tdidList.add(bdcGdDyhRel.getTdid());
                    }

                }
            }
        }
        if(CollectionUtils.isNotEmpty(tdidList)) {
            for(String tdid : tdidList) {
                GdTd gdTd = queryGdTd(tdid);
                if(gdTd != null)
                    gdTdList.add(gdTd);
            }
        }
        return gdTdList;
    }

    @Override
    public List<HashMap> getGdTdQl(final Map map) {
        return gdTdMapper.getGdTdQl(map);
    }

    @Override
    public GdTd getGdTdByDjh(String djh) {
        GdTd gdTd = null;
        if(StringUtils.isNotBlank(djh)) {
            Example gdTdExample = new Example(GdTd.class);
            gdTdExample.createCriteria().andEqualTo("djh", djh);
            List<GdTd> gdTdList = entityMapper.selectByExample(GdTd.class, gdTdExample);
            if(CollectionUtils.isNotEmpty(gdTdList))
                gdTd = gdTdList.get(0);
        }
        return gdTd;
    }

    @Override
    public List<GdTdsyq> getGdTdsyqByTdid(String tdid) {
        return gdTdMapper.getGdTdsyqByTdid(tdid);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTdzhByBdcdyh(String bdcdyh) {
        return gdTdMapper.getTdzhByBdcdyh(bdcdyh);
    }

    public List<String> getGdTdQlidsByFwQlid(String qlid) {
        return gdTdMapper.getGdTdQlidsByFwQlid(qlid);
    }

    @Override
    public List<GdCf> getTdCfByFczh(final Map hashMap) {
        return gdTdMapper.getTdCfByFczh(hashMap);
    }

    @Override
    public List<GdDy> getGdTdDyByTdid(String tdid) {
        return gdTdMapper.getGdTdDyByTdid(tdid);
    }


    @Override
    public List<Map> selectGdtdNopp() {
        return gdTdMapper.selectGdtdNopp();
    }

    @Override
    public List<GdTdsyq> queryGdTdsyqByTdidAndQszt(String tdid, String qszt) {
        List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
        List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(tdid,"");
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                HashMap map = new HashMap();
                map.put("qlid",gdBdcQlRel.getQlid());
                if (StringUtils.isNotBlank(qszt))
                    map.put("iszx",qszt);
                List<GdTdsyq> gdTdsyqList1 = andEqualQueryGdTdsyq(map);
                if (CollectionUtils.isNotEmpty(gdTdsyqList1))
                    gdTdsyqList.addAll(gdTdsyqList1);
            }
        }
        return gdTdsyqList;
    }

    @Override
    public List<GdTdsyq> getTdsyqByTdzh(String tdzh) {
        List<GdTdsyq> gdTdsyqList= null;
        if(StringUtils.isNotBlank(tdzh)){
            HashMap map = new HashMap();
            map.put("tdzh",tdzh);
            gdTdsyqList=gdTdMapper.getGdTdSyqList(map);
        }
        return gdTdsyqList;
    }

    @Override
    public Map validateTdzh(String tdzh) {
        Map resultMap = new HashMap();
        if(StringUtils.isNotBlank(tdzh)){
            //用正则去掉英文括号
            String regex = "[(]|[)]|[\uff08-\uff09]";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(tdzh);
            tdzh = mat.replaceAll("");
            List<GdTdsyq> gdTdsyqList = getTdsyqByCqzhjc(tdzh);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                resultMap.put("result", ParamsConstants.TRUE_LOWERCASE);
            }
        }
        return resultMap;
    }

    @Override
    public List<GdTdsyq> getTdsyqByCqzhjc(String cqzhjc) {
        List<GdTdsyq> gdTdsyqList= null;
        if(StringUtils.isNotBlank(cqzhjc)){
            HashMap map = new HashMap();
            map.put("cqzhjc",cqzhjc);
            gdTdsyqList=gdTdMapper.getGdTdSyqList(map);
        }
        return gdTdsyqList;
    }

}
