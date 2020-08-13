package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.BdcZsbh;
import cn.gtmap.estateplat.server.core.mapper.BdcXtConfigMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZsbhMapper;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.BdcZsbhService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-9-18
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcZsbhServiceImpl implements BdcZsbhService {
    @Autowired
    BdcZsbhMapper bdcZsbhMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcXtConfigMapper bdcXtConfigMapper;
    @Autowired
    BdcZsService bdcZsService;


    @Override
    public List<BdcZsbh> getBdcZsBhListByBhfw(Map map) {
        return bdcZsbhMapper.getBdcZsBhListByBhfw(map);
    }

    @Override
    public String checkAndSaveZsh(List<BdcZsbh> bdcZsbhList, BdcZsbh bdcZsbh, String qsbh, String jsbh) {
        String msg = "保存失败";
        if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
            return "您所需要新增证书编号在系统中已存在，请重新选择";
        } else {
            if(StringUtils.isNotBlank(qsbh) && StringUtils.isNotBlank(jsbh)) {
                HashMap map = new HashMap();
                map.put("dwdm", bdcZsbh.getDwdm());
                BdcXtConfig bdcXtConfig = bdcXtConfigMapper.selectBdcXtConfig(map);

                if(bdcXtConfig != null){
                    DecimalFormat df = new DecimalFormat("000000000");
                    for(int i = 0; i < (Integer.parseInt(jsbh) - Integer.parseInt(qsbh) + 1); i++) {
                        bdcZsbh.setZsbhid(UUIDGenerator.generate18());
                        String zsbh = bdcXtConfig.getSqdm() + df.format(Integer.parseInt(qsbh) + i);
                        bdcZsbh.setZsbh(zsbh);
                        entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                    }
                    msg = "保存成功";
                }

            }
        }
        return msg;
    }

    @Override
    public String getMaxZsbh(final String zslx) {
        return bdcZsbhMapper.getMaxZsbh(zslx);
    }

    @Override
    public String getZsbhByDwdm(HashMap hashMap) {
        return bdcZsbhMapper.getZsbhByDwdm(hashMap);
    }

    @Override
    public String getZsbh(final String zslx) {
        return bdcZsbhMapper.getZsbh(zslx);
    }

    @Override
    public String getzfbl(final String zslx) {
        return bdcZsbhMapper.getzfbl(zslx);
    }

    @Override
    public Map getZsYjByZslx(final String zslx) {
        return bdcZsbhMapper.getZsYjByZslx(zslx);
    }

    @Override
    public String getZsBh(final String zslx, final String zsid) {
        String zsbh = "";
        String zsbhTemp = getZsbh(zslx);
        if(StringUtils.isNotBlank(zslx)) {
            HashMap map = new HashMap();
            map.put("zsbh", zsbhTemp);
            map.put("zslx", zslx);
            List<BdcZsbh> bdcZsbhList = getBdcZsBhListByBhfw(map);
            if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
                BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                bdcZsbh.setSyqk("3");
                bdcZsbh.setZsid(zsid);
                bdcZsbh.setLqrid(SessionUtil.getCurrentUserId());
                bdcZsbh.setLqrid(SessionUtil.getCurrentUser().getUsername());
                entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
            }
            zsbh = zsbhTemp;
        }
        return zsbh;
    }

    @Override
    public void changeZsBhZt(BdcXm bdcXm, BdcZsbh bdcZsbh) {
        List<BdcZs> zsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
        if(CollectionUtils.isNotEmpty(zsList)) {
            for(BdcZs zs : zsList) {
                HashMap map = new HashMap();
                map.put("zsid", zs.getZsid());
                List<BdcZsbh> bdcZsbhList = getBdcZsBhListByBhfw(map);
                if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
                    for(BdcZsbh bdcZsbhFor : bdcZsbhList) {
                        if(StringUtils.equals(bdcZsbhFor.getZsbh(), zs.getBh())) {
                            bdcZsbhFor.setSyqk(Constants.BDCZSBH_SYQK_YSY);
                            entityMapper.saveOrUpdate(bdcZsbhFor, bdcZsbhFor.getZsbhid());
                        } else {
                            if(StringUtils.equals(bdcZsbhFor.getSyqk(), Constants.BDCZSBH_SYQK_LS)) {
                                bdcZsbhFor.setZsid("");
                                bdcZsbhFor.setSyqk(Constants.BDCZSBH_SYQK_WSY);
                                bdcZsbhFor.setLqr("");
                                bdcZsbhFor.setLqrid("");
                                bdcZsbhFor.setBfyy("");
                                entityMapper.saveOrUpdate(bdcZsbhFor, bdcZsbhFor.getZsbhid());
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void workFlowBackZsBh(final String proid) {
        List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(proid);
        if(CollectionUtils.isNotEmpty(bdcZsList)) {
            for(BdcZs bdcZs : bdcZsList) {
                HashMap zsBhMap = new HashMap();
                zsBhMap.put("zsid", bdcZs.getZsid());
                if(StringUtils.equals(bdcZs.getZstype(), Constants.BDCQZS_BH_FONT))
                    zsBhMap.put("zslx", Constants.BDCQZS_BH_DM);
                else if(StringUtils.equals(bdcZs.getZstype(), Constants.BDCQZM_BH_FONT))
                    zsBhMap.put("zslx", Constants.BDCQZM_BH_DM);
                List<BdcZsbh> bdcZsbhList = getBdcZsBhListByBhfw(zsBhMap);
                if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
                    for(BdcZsbh bdcZsbh : bdcZsbhList) {
                        if(!(StringUtils.equals(bdcZsbh.getSyqk(), Constants.BDCZSBH_SYQK_ZF))) {
                            bdcZsbh.setZsid("");
                            bdcZsbh.setSyqk(Constants.BDCZSBH_SYQK_WSY);
                            bdcZsbh.setLqr("");
                            bdcZsbh.setLqrid("");
                            bdcZsbh.setBfyy("");
                            entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                        }
                    }
                }
            }
        }
    }

    /**
     * @param proid zslx     zsbh     lqr   lqrid    zsid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 帆软保存证书时验证证书编号填写是否正确，如果正确保存（这里只验证显示的证书编号）
     */
    @Override
    public String checkZsbh(final String proid, final String zslx, final String zsbh, final String lqr, final String lqrid, final String zsid) {
        String msg = "success";
        if(StringUtils.isNotBlank(zsbh)) {
            HashMap map = new HashMap();
            map.put("zsbh", zsbh);
            map.put("zslx", zslx);
            List<BdcZsbh> bdcZsbhList = getBdcZsBhListByBhfw(map);
            if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
                BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                if(StringUtils.equals(bdcZsbh.getSyqk(), Constants.BDCZSBH_SYQK_ZF)) {
                    msg = "填写的证书已作废！";
                } else if(StringUtils.equals(bdcZsbh.getSyqk(), Constants.BDCZSBH_SYQK_YSY) || StringUtils.equals(bdcZsbh.getSyqk(), Constants.BDCZSBH_SYQK_LS)) {
                    if(!StringUtils.equals(bdcZsbh.getZsid(), zsid)) {
                        msg = "填写的证书已使用！";
                    }
                } else if(StringUtils.equals(bdcZsbh.getSyqk(), Constants.BDCZSBH_SYQK_WSY)) {
                    //按人员分配
                    if(StringUtils.equals(AppConfig.getProperty("getZsbhByRy"), "true")&&!StringUtils.equals(lqrid, bdcZsbh.getLqrid())) {
                        msg = "该证书编号不是你领取的！";
                        return msg;
                    }
                    bdcZsbh.setZsid(zsid);
                    bdcZsbh.setLqr(lqr);
                    bdcZsbh.setLqrid(lqrid);
                    bdcZsbh.setSyqk(Constants.BDCZSBH_SYQK_LS);
                    entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                    clearZsbhData(zsbh, zsid);
                }
            } else {
                msg = "填写的证书编号不存在！";
            }
        }
        return msg;
    }

    @Override
    public List<BdcZsbh> getZsBhByMap(final HashMap hashMap) {
        return bdcZsbhMapper.getZsbhByMap(hashMap);
    }

    @Override
    public void changeZsSyqk(BdcXm bdcXm) {
        HashMap hashMap = new HashMap();
        //查封和注销不生成证书
        if(bdcXm != null && !StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_ZXDJ_DM) && !StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CFDJ_DM)) {
            hashMap.put("proid", bdcXm.getProid());
            hashMap.put("syqk", "3");
            List<BdcZsbh> bdcZsbhList = getZsBhByMap(hashMap);
            if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
                //因一个项目对应一本证书，即一个zsbh,但有可能是分别持证，所以要循环
                for(BdcZsbh bdcZsbh : bdcZsbhList) {
                    bdcZsbh.setSyqk("1");
                    entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                }
            }
        }
    }

    @Override
    public void batchChangeZsSyqk(List<BdcXm> bdcXmList) {

    }

    /**
     * 修改证书编号的时候将之前的zsbh改为未使用状态
     *
     * @param zsbh
     * @param zsid
     * @return
     */
    public boolean clearZsbhData(String zsbh, String zsid) {
        List<BdcZsbh> bdcZsbhList = null;
        if(StringUtils.isNotBlank(zsid)) {
            Map m = new HashMap();
            m.put("zsid", zsid);
            bdcZsbhList = getBdcZsBhListByBhfw(m);
            BdcZsbh zs = null;
            Iterator<BdcZsbh> iterator = bdcZsbhList.iterator();
            while(iterator.hasNext()) {
                zs = iterator.next();
                if(!zsbh.equals(zs.getZsbh()) && !"0".equals(zs.getSyqk()) && ("2".equals(zs.getSyqk()) || "3".equals(zs.getSyqk()))) {
                    zs.setSyqk("0");
                    zs.setSyr(null);
                    zs.setSyrid(null);
                    zs.setSysj(null);
                    zs.setZsid(null);
                    zs.setLqr(null);
                    zs.setLqrid(null);
                    bdcZsbhMapper.updateZsSyData(zs);
                }

            }
            return true;
        }
        return false;
    }

    @Override
    public List<BdcZsbh> getBdcZsBhListByQzBh(Map map) {
        return bdcZsbhMapper.getBdcZsBhListByQzBh(map);
    }

    @Override
    public String checkZsbhNew(String proid, String zslx, String zsbh, String organName, String zsid,String syr){
        String msg = "success";
        if(StringUtils.isNotBlank(zsbh)){
            HashMap map = new HashMap();
            map.put("zsbh", zsbh);
            map.put("zslx", zslx);
            List<BdcZsbh> bdcZsbhList = getBdcZsBhListByBhfw(map);
            if(CollectionUtils.isNotEmpty(bdcZsbhList)) {
                BdcZsbh bdcZsbh = bdcZsbhList.get(0);
                if(StringUtils.equals(bdcZsbh.getSyqk(),"6")){
                    msg = "证书编号未被领用，请检查录入证本编号是否正确！";
                }else if(!StringUtils.equals(bdcZsbh.getLqdw(),organName)){
                    msg ="证书编号已被其他部门领用，请检查录入证本编号是否正确！";
                }else if(StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_ZF)){
                    msg = "证书已作废，请检查录入证本编号是否正确。";
                }else if(StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_YS) || StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_XH)){
                    msg = "证书已遗失，请检查录入证本编号是否正确。";
                }else if(StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_YSY) || StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_LS)){
                    if(StringUtils.isNotBlank(zsid) &&!zsid.equals(bdcZsbh.getZsid())) {
                        msg = "证书编号已使用";
                    }
                }else if(StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_WSY)){
                    bdcZsbh.setZsid(zsid);
                    bdcZsbh.setSyqk(Constants.BDCZSBH_SYQK_LS);
                    bdcZsbh.setSyr(syr);
                    bdcZsbh.setSysj(new Date());
                    entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                    clearZsbhData(zsbh, zsid);
                }
            }else{
                msg = "证书编号未被领用，请检查录入证本编号是否正确！";
            }

        } else{
            msg ="请填写证书编号";
        }
        return msg;
    }

    @Override
    public int getCountBdcZsBhByXh(int xh) {
        return bdcZsbhMapper.getCountBdcZsBhByXh(xh);
    }

    @Override
    public void zsbhXh(String zsbhid,String xhr,String xhrid,String xhjzr) {
        Example example = new Example(BdcZsbh.class);
        example.createCriteria().andEqualTo("zsbhid",zsbhid);
        List<BdcZsbh> bdcZsbhList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcZsbhList)){
            for (BdcZsbh bdcZsbh :bdcZsbhList){
                if (bdcZsbh != null && StringUtils.isNotBlank(bdcZsbh.getZsbhid())&&StringUtils.isNotBlank(bdcZsbh.getSyqk()) && StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_ZF)){
                    bdcZsbh.setSyqk(Constants.BDCZSBH_SYQK_XH);
                    if (StringUtils.isNotBlank(xhr))
                        bdcZsbh.setXhr(xhr);
                    if (StringUtils.isNoneBlank(xhrid))
                        bdcZsbh.setXhrid(xhrid);
                    if (StringUtils.isNotBlank(xhjzr))
                        bdcZsbh.setXhjzr(xhjzr);
                    bdcZsbh.setXhsj(new Date());
                    entityMapper.saveOrUpdate(bdcZsbh,bdcZsbh.getZsbhid());
                }
            }
        }
    }

    @Override
    public void qxZsbhXh(String zsbhid) {
        Example example = new Example(BdcZsbh.class);
        example.createCriteria().andEqualTo("zsbhid",zsbhid);
        List<BdcZsbh> bdcZsbhList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcZsbhList)){
            for (BdcZsbh bdcZsbh :bdcZsbhList){
                if (bdcZsbh != null && StringUtils.isNotBlank(bdcZsbh.getZsbhid())&&StringUtils.isNotBlank(bdcZsbh.getSyqk()) && StringUtils.equals(bdcZsbh.getSyqk(),Constants.BDCZSBH_SYQK_XH)){
                    bdcZsbh.setSyqk(Constants.BDCZSBH_SYQK_ZF);
                    bdcZsbh.setXhr(null);
                    bdcZsbh.setXhrid(null);
                    bdcZsbh.setXhsj(null);
                    bdcZsbh.setXhjzr(null);
                    entityMapper.saveOrUpdate(bdcZsbh,bdcZsbh.getZsbhid());
                }
            }
        }
    }

    @Override
    public List<BdcZsbh> batchSelectBdcZsbh(List<BdcZs> bdcZsList) {
        List<BdcZsbh> bdcZsbhList = null;
        if(CollectionUtils.isNotEmpty(bdcZsList)){
            HashMap map = Maps.newHashMap();
            map.put("bdcZsList",bdcZsList);
            bdcZsbhList = bdcZsbhMapper.batchSelectBdcZsbh(map);
        }
        return bdcZsbhList;
    }

    @Override
    public void batchUpdateBdcZsbh(List<BdcZsbh> bdcZsbhList) {
        if(CollectionUtils.isNotEmpty(bdcZsbhList)){
            bdcZsbhMapper.batchUpdateBdcZsbh(bdcZsbhList);
        }
    }
}
