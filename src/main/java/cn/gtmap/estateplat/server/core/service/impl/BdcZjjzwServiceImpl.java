package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcXmRelMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZjjzwxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by hqz on 2016/4/19.
 */
@Repository
public class BdcZjjzwServiceImpl implements BdcZjjzwxxService {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcZjjzwxxMapper bdcZjjzwxxMapper;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcXmRelMapper bdcXmRelMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public BdcZjjzwxx createZjjzwxx(final String proId, final String xh, final String bdcdyh) {
        Map<String, String> map = new HashMap<String, String>();
        BdcZjjzwxx bdcZjjzwxx = new BdcZjjzwxx();
        map.put("bdcdyh", bdcdyh);
        List<DjsjFwHs> djsjFwhsList = djsjFwService.getDjsjFwYcHs(map);
        if(CollectionUtils.isEmpty(djsjFwhsList)){
            djsjFwhsList = djsjFwService.getDjsjFwHs(map);
        }
        if (CollectionUtils.isNotEmpty(djsjFwhsList)) {
            DjsjFwHs djsjFwHs = djsjFwhsList.get(0);

            map.put(ParamsConstants.PROID_LOWERCASE, proId);
            List<BdcZjjzwxx> bdcZjjzwxxList = bdcZjjzwxxMapper.getBdcZjjzwxx(map);
            if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                bdcZjjzwxx = bdcZjjzwxxList.get(0);
            }
            bdcZjjzwxx.setProid(proId);
            bdcZjjzwxx.setXh(xh + "");
            bdcZjjzwxx.setZl(djsjFwHs.getZl());
            bdcZjjzwxx.setBdcdyh(bdcdyh);
            bdcZjjzwxx.setYt(djsjFwHs.getGhyt());
            bdcZjjzwxx.setJzmj(djsjFwHs.getYcjzmj());
            bdcZjjzwxx.setFjh(djsjFwHs.getFjh());
            bdcZjjzwxx.setSzc(djsjFwHs.getDycs());
            bdcZjjzwxx.setJyjg(djsjFwHs.getJyjg());
            bdcZjjzwxx.setDymj(djsjFwHs.getYcjzmj());
            bdcZjjzwxx.setFtmj(djsjFwHs.getFttdmj());
            bdcZjjzwxx.setDw("1");
            bdcZjjzwxx.setDyzt("0");
            HashMap ljzMap = new HashMap();
            ljzMap.put("fw_dcb_index", djsjFwHs.getFwDcbIndex());
            List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(ljzMap);
            if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
                DjsjFwLjz djsjFwLjz = djsjFwLjzList.get(0);
                bdcZjjzwxx.setJzjg(djsjFwLjz.getFwjg());
                bdcZjjzwxx.setZcs(djsjFwLjz.getFwcs() + "");
                bdcZjjzwxx.setZrzh(djsjFwLjz.getDh());
            }
            if (StringUtils.isBlank(bdcZjjzwxx.getZjwid())) {
                bdcZjjzwxx.setZjwid(UUIDGenerator.generate());
            }
            entityMapper.saveOrUpdate(bdcZjjzwxx, bdcZjjzwxx.getZjwid());

        }
        if (CollectionUtils.isEmpty(djsjFwhsList)) {
            String bdcdyfwlx = bdcXmService.getBdcdyfwlxByYcbdcdyh(bdcdyh);
            if (StringUtils.isNotBlank(bdcdyfwlx) && StringUtils.equals(bdcdyfwlx, Constants.DJSJ_FWDZ_DM)) {
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proId);
                if (bdcSpxx != null) {
                    bdcZjjzwxx.setProid(proId);
                    bdcZjjzwxx.setXh(xh + "");
                    bdcZjjzwxx.setZl(bdcSpxx.getZl());
                    bdcZjjzwxx.setBdcdyh(bdcdyh);
                    bdcZjjzwxx.setYt(bdcSpxx.getYt());
                    bdcZjjzwxx.setJzmj(bdcSpxx.getMj());
                    bdcZjjzwxx.setDymj(bdcSpxx.getMj());
                    bdcZjjzwxx.setDw("1");
                    bdcZjjzwxx.setDyzt("0");
                    if (StringUtils.isBlank(bdcZjjzwxx.getZjwid())) {
                        bdcZjjzwxx.setZjwid(UUIDGenerator.generate());
                    }
                    entityMapper.saveOrUpdate(bdcZjjzwxx, bdcZjjzwxx.getZjwid());
                }
            }
        }
        return bdcZjjzwxx;
    }

    public List<BdcZjjzwxx> getZjjzwxx(Map map) {
        return bdcZjjzwxxMapper.getBdcZjjzwxx(map);
    }

    @Override
    public List<BdcZjjzwxx> getSameWFZjjzwxx(String proid) {
        return bdcZjjzwxxMapper.getSameWFZjjzwxx(proid);
    }

    public int getDyBdcZjjzwxxByBdcdyh(final String bdcdyh) {
        return bdcZjjzwxxMapper.getDyBdcZjjzwxxByBdcdyh(bdcdyh);
    }


    public void changeZjjzwxxDyzt(BdcXm bdcXm, String ydyzt, String dyzt) {
        BdcXmRel bdcXmRel = new BdcXmRel();
        bdcXmRel.setProid(bdcXm.getProid());
        List<BdcXmRel> list = bdcXmRelMapper.queryBdcXmRelMapper(bdcXmRel);
        String yproid = "";
        if (CollectionUtils.isNotEmpty(list)) {
            for (BdcXmRel bdcXmRel1 : list) {
                if (StringUtils.isNotBlank(bdcXmRel1.getYproid())) {
                    yproid = bdcXmRel1.getYproid();
                    break;
                }
            }
        }

        Example example = new Example(BdcZjjzwxx.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, yproid).andEqualTo("dyzt", ydyzt);
        List<BdcZjjzwxx> zjjzwxxList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(zjjzwxxList)) {
            for (int i = 0; i < zjjzwxxList.size(); i++) {
                BdcZjjzwxx zjjzwxx = zjjzwxxList.get(i);
                zjjzwxx.setDyzt(dyzt);
                entityMapper.updateByPrimaryKeySelective(zjjzwxx);
            }
        }
    }

    public void deleteZjjzwxx(BdcXm bdcXm) {
        if (StringUtils.isNotEmpty(bdcXm.getProid())) {

            Example example = new Example(BdcZjjzwxx.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
            entityMapper.deleteByExample(example);
        }
    }

    @Override
    public void cancelTdDyOfZjgcdy(String fwbdcdyh) {
        String bdcdyh = bdcdyService.getZdBdcdyh(fwbdcdyh);
        if (StringUtils.isNotBlank(bdcdyh)) {
            BdcDyaq bdcDyaq = getBdcDyaqByBdcdyh(bdcdyh);
            if (bdcDyaq != null) {
                updateBdcDyaq(bdcDyaq, Constants.QLLX_QSZT_HR);
            } else {
                GdDy gdDy = getGdDyByBdcdyh(bdcdyh);
                if (gdDy != null) {
                    updateGdDy(gdDy, 1);
                }
            }
        }
    }

    @Override
    public void reverseCanceledTdDyOfZjgcdy(BdcXm bdcXm) {
        if (bdcXm != null) {
            List<BdcZjjzwxx> bdcZjjzwxxList = getSameWFZjjzwxx(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                String fwbdcdyh = bdcZjjzwxxList.get(0).getBdcdyh();
                if (StringUtils.isNotBlank(fwbdcdyh)) {
                    String tdBdcdyh = bdcdyService.getZdBdcdyh(fwbdcdyh);
                    if (StringUtils.isNotBlank(tdBdcdyh)) {
                        BdcDyaq bdcDyaq = getBdcDyaqByBdcdyh(tdBdcdyh);
                        if (bdcDyaq != null && bdcDyaq.getQszt() == Constants.QLLX_QSZT_HR) {
                            updateBdcDyaq(bdcDyaq, Constants.QLLX_QSZT_XS);
                        } else {
                            GdDy gdDy = getGdDyByBdcdyh(tdBdcdyh);
                            if (gdDy != null && gdDy.getIsjy() == 1) {
                                updateGdDy(gdDy, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateZjjzwDyzt(Map map) {
        bdcZjjzwxxMapper.updateZjjzwDyzt(map);
    }

    @Override
    public BdcZjjzwxx getBdcZjjzwxxByProid(String proid) {
        if(StringUtils.isNotBlank(proid)){
            return bdcZjjzwxxMapper.getBdcZjjzwxxByProid(proid);
        }
        return null;
    }

    /**
     * 注销bdc_dyaq
     *
     * @param bdcDyaq
     */
    public void updateBdcDyaq(BdcDyaq bdcDyaq, Integer qszt) {
        bdcDyaq.setZxsj(new Date());
        bdcDyaq.setQszt(qszt);
        entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
    }

    /**
     * 注销gd_dy
     *
     * @param gdDy
     */
    public void updateGdDy(GdDy gdDy, Integer qszt) {
        gdDy.setIsjy(1);
        gdDy.setZxrq(new Date());
        entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
        gdFwService.changeGdqlztByQlid(gdDy.getDyid(),Constants.QLLX_QSZT_XS.toString());
    }

    /**
     * 根据bdcdyh获取bdc_dyaq
     *
     * @param bdcdyh
     * @return
     */
    public BdcDyaq getBdcDyaqByBdcdyh(String bdcdyh) {
        BdcDyaq bdcDyaq = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("bdcDyh", bdcdyh);
        List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(param);
        if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
            bdcDyaq = bdcDyaqList.get(0);
        }
        return bdcDyaq;
    }

    /**
     * 根据bdcdyh获取gd_dy
     *
     * @param bdcdyh
     * @return
     */
    public GdDy getGdDyByBdcdyh(String bdcdyh) {
        GdDy gdDy = null;
        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(bdcdyh);
        if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
            BdcGdDyhRel bdcGdDyhRel = bdcGdDyhRelList.get(0);
            String tdid = StringUtils.isNotBlank(bdcGdDyhRel.getTdid()) ? bdcGdDyhRel.getTdid() : bdcGdDyhRel.getGdid();
            if (StringUtils.isNotBlank(tdid)) {
                List<GdDy> gdDyList = gdTdService.queryTddyqByTdid(tdid,Constants.GDQL_ISZX_WZX);
                if (CollectionUtils.isNotEmpty(gdDyList)) {
                    for (GdDy dy : gdDyList) {
                        gdDy = dy;
                        break;
                    }
                }
            }
        }
        return gdDy;
    }
}
