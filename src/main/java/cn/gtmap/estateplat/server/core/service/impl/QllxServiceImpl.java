package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.*;
import cn.gtmap.estateplat.server.core.model.OntBdcXm;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.InterfaceCodeBeanFactory;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.ql.impl.BdcNoQlDqServiceImpl;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-20
 */
@Repository
public class QllxServiceImpl implements QllxService {
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcQllxMapper bdcQllxMapper;
    @Autowired
    BdcZdQllxService bdcZdQllxService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    private GdqlMapper gdqlMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private DjSjMapper djSjMapper;
    @Autowired
    private BdcDyMapper bdcDyMapper;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private GdDyService gdDyService;

    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    private SysSignService sysSignService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BdcFdcqService bdcFdcqService;

    @Autowired
    private BdcdjbService bdcdjbService;

    @Autowired
    private BdcYgService bdcYgService;

    @Autowired
    private BdcCfService bdcCfService;

    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private GdLqService gdLqService;
    @Autowired
    private GdCqService gdCqService;
    @Autowired
    private BdcFdcqDzMapper bdcFdcqDzMapper;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private BdcYyService bdcYyService;
    @Autowired
    private OntService ontService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;

    @Autowired
    private DjxxMapper djxxMapper;
    @Autowired
    private DjsjFwMapper djsjFwMapper;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private GdYgService gdYgService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwService;
    @Autowired
    private Set<BdcQlDqService> bdcQlDqServiceList;
    @Resource(name = "bdcNoQlDqServiceImpl")
    BdcNoQlDqServiceImpl bdcNoQlDqServiceImpl;

    @Override
    public String getTableName(QllxVo vo) {
        String tableName = "";
        if (vo != null) {
            Annotation[] aos = vo.getClass().getAnnotations();
            for (Annotation ao : aos) {
                tableName = AnnotationUtils.getValue(ao, "name").toString();
            }
        }
        return tableName;
    }

    @Override
    public List<Map> getGdYgByBdcdyh(String bdcdyh) {
        return bdcQllxMapper.getGdYgByBdcdyh(bdcdyh);
    }

    @Override
    public List<GdYg> getGdYgXxByBdcdyh(String bdcdyh) {
        return bdcQllxMapper.getGdYgXxByBdcdyh(bdcdyh);
    }


    @Override
    public QllxVo makeSureQllx(final String qllx) {
        QllxVo qllxVo = null;
        if (StringUtils.isNotBlank(qllx)) {
            ConcurrentMap map = getQllxMap();
            BdcZdQllx bdcZdQllx = bdcZdQllxService.queryBdcZdQllxByDm(qllx);
            if (bdcZdQllx != null && map.containsKey(bdcZdQllx.getTableName().toUpperCase())) {
                qllxVo = (QllxVo) map.get(bdcZdQllx.getTableName().toUpperCase());
            }
        }
        return qllxVo;
    }


    @Override
    public QllxVo makeSureQllx(final BdcXm bdcXm) {
        String qllx;
        if (bdcXm != null) {
            qllx = bdcXm.getQllx();
        } else {
            return null;
        }
        QllxVo vo = null;
        if (StringUtils.isNotBlank(qllx)) {
            ConcurrentMap map = getQllxMap();
            vo = makeSureQllx(bdcXm.getQllx());
            if (vo instanceof BdcFdcq) {
                String fwlx = djsjFwService.getFwlxByProid(bdcXm.getProid());
                String bdcdyfwlx = "";
                BdcBdcdy bdcBdcdy = null;
                if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                    bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                }
                if (bdcBdcdy != null) {
                    bdcdyfwlx = bdcBdcdy.getBdcdyfwlx();
                }
                if ((StringUtils.isNotBlank(fwlx) && StringUtils.equals(Constants.DJSJ_FWDZ_DM, fwlx)) ||
                        StringUtils.equals(Constants.DJSJ_FWDZ_DM, bdcdyfwlx)) {
                    vo = (QllxVo) map.get("BDC_FDCQ_DZ".toUpperCase());
                }
            }
        }
        return vo;
    }

    private ConcurrentMap getQllxMap() {
        ConcurrentMap map = new ConcurrentHashMap();
        BdcCf bdcCf = new BdcCf();
        map.put(getTableName(bdcCf).toUpperCase(), bdcCf);

        BdcDyaq bdcDyaq = new BdcDyaq();
        map.put(getTableName(bdcDyaq).toUpperCase(), bdcDyaq);

        BdcDyq bdcDyq = new BdcDyq();
        map.put(getTableName(bdcDyq).toUpperCase(), bdcDyq);

        BdcFdcq bdcFdcq = new BdcFdcq();
        map.put(getTableName(bdcFdcq).toUpperCase(), bdcFdcq);

        BdcFdcqDz bdcFdcqDz = new BdcFdcqDz();
        map.put(getTableName(bdcFdcqDz).toUpperCase(), bdcFdcqDz);

        BdcHysyq bdcHysyq = new BdcHysyq();
        map.put(getTableName(bdcHysyq).toUpperCase(), bdcHysyq);

        BdcJsydzjdsyq bdcJsydzjdsyq = new BdcJsydzjdsyq();
        map.put(getTableName(bdcJsydzjdsyq).toUpperCase(), bdcJsydzjdsyq);

        BdcJzwgy bdcJzwgy = new BdcJzwgy();
        map.put(getTableName(bdcJzwgy).toUpperCase(), bdcJzwgy);

        BdcJzwsyq bdcJzwsyq = new BdcJzwsyq();
        map.put(getTableName(bdcJzwsyq).toUpperCase(), bdcJzwsyq);

        BdcLq bdcLq = new BdcLq();
        map.put(getTableName(bdcLq).toUpperCase(), bdcLq);

        BdcQsq bdcQsq = new BdcQsq();
        map.put(getTableName(bdcQsq).toUpperCase(), bdcQsq);

        BdcTdcbnydsyq bdcTdcbnydsyq = new BdcTdcbnydsyq();
        map.put(getTableName(bdcTdcbnydsyq).toUpperCase(), bdcTdcbnydsyq);

        BdcTdsyq bdcTdsyq = new BdcTdsyq();
        map.put(getTableName(bdcTdsyq).toUpperCase(), bdcTdsyq);

        BdcYg bdcYg = new BdcYg();
        map.put(getTableName(bdcYg).toUpperCase(), bdcYg);

        BdcYy bdcYy = new BdcYy();
        map.put(getTableName(bdcYy).toUpperCase(), bdcYy);

        BdcFdcqDz bdcFdcqDzTemp = new BdcFdcqDz();
        map.put(getTableName(bdcFdcqDz).toUpperCase(), bdcFdcqDzTemp);
        return map;
    }

    @Override
    public QllxVo makeSureQllx(ProjectPar projectPar) {
        QllxVo vo = null;
        if (projectPar != null && projectPar.getBdcXm() != null && StringUtils.isNotBlank(projectPar.getBdcXm().getQllx())) {
            String qllx = projectPar.getBdcXm().getQllx();
            vo = makeSureQllx(qllx);
            ConcurrentMap map = getQllxMap();
            if (vo instanceof BdcFdcq) {
                String fwlx = "";
                String bdcdyfwlx = "";
                if (projectPar.getDjsjFwxx() != null) {
                    fwlx = projectPar.getDjsjFwxx().getBdcdyfwlx();
                }
                if (projectPar.getBdcBdcdy() != null) {
                    bdcdyfwlx = projectPar.getBdcBdcdy().getBdcdyfwlx();
                }
                if ((StringUtils.isNotBlank(fwlx) && StringUtils.equals(Constants.DJSJ_FWDZ_DM, fwlx)) ||
                        StringUtils.equals(Constants.DJSJ_FWDZ_DM, bdcdyfwlx)) {
                    vo = (QllxVo) map.get("BDC_FDCQ_DZ".toUpperCase());
                }
            }
        }
        return vo;
    }

    @Override
    public void backQllxZt(BdcXm bdcXm) {
        changeQllxZt(bdcXm, Constants.QLLX_QSZT_LS);
    }

    /*建设用地使用权**/
    @Override
    public BdcJsydzjdsyq getJsydzjdsyqFromZdxx(BdcJsydzjdsyq bdcJsydzjdsyq, DjsjZdxx djsjZdxx) {
        if (bdcJsydzjdsyq == null) {
            bdcJsydzjdsyq = new BdcJsydzjdsyq();
        }
        if (djsjZdxx != null) {
            bdcJsydzjdsyq.setQdjg(djsjZdxx.getQdjg());
            bdcJsydzjdsyq.setGyqk(djsjZdxx.getGysyqqh());
            bdcJsydzjdsyq.setSyjsqx(djsjZdxx.getZzrq());
            bdcJsydzjdsyq.setSyksqx(djsjZdxx.getQsrq());
            if (djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() != 0) {
                bdcJsydzjdsyq.setSyqmj(djsjZdxx.getFzmj());
            } else {
                bdcJsydzjdsyq.setSyqmj(djsjZdxx.getScmj());
            }
        }
        return bdcJsydzjdsyq;
    }

    @Override
    public QllxVo getQllxParentFrom(QllxVo qllxVo, BdcXm bdcXm) {
        if (bdcXm != null) {
            if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                qllxVo.setBdcdyid(bdcXm.getBdcdyid());
            }

            qllxVo.setProid(bdcXm.getProid());
            //过渡解封不赋值业务号
            if ((StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_FWSP) || StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_TDSP))
                    && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_JF) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLJF))) {
                qllxVo.setYwh(null);
            } else {
                qllxVo.setYwh(bdcXm.getBh());
            }
            qllxVo.setQllx(bdcXm.getQllx());
        }
        return qllxVo;
    }


    @Override
    public BdcFdcq getBdcFdcqFromFwxx(BdcFdcq bdcFdcq, DjsjFwxx djsjFwxx, DjsjZdxx djsjZdxx) {
        if (bdcFdcq == null) {
            bdcFdcq = new BdcFdcq();
        }
        if (djsjFwxx != null) {
            /*sc:房屋信息添加产权来源**/
            bdcFdcq.setCqly(djsjFwxx.getCqly());
            /*sc:房屋信息添加房屋档案号**/
            bdcFdcq.setFwdah(djsjFwxx.getFcdah());
            /*sc:房屋信息添加自然栋栋号**/
            bdcFdcq.setZrzh(djsjFwxx.getZrddh());
            if (djsjFwxx.getDytdmj() != null && djsjFwxx.getDytdmj() != 0) {
                bdcFdcq.setDytdmj(djsjFwxx.getDytdmj());
            }
            /**
             * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
             * @description 连云港独用土地面积只读fw_hs, 不读调查表的scmj
             */
            //            else
            bdcFdcq.setFtjzmj(djsjFwxx.getFtjzmj());
            bdcFdcq.setFttdmj(djsjFwxx.getFttdmj());
            if (StringUtils.isNotBlank(djsjFwxx.getFwlx()) && NumberUtils.isNumber(djsjFwxx.getFwlx())) {
                bdcFdcq.setFwlx(Integer.valueOf(djsjFwxx.getFwlx()));
            }
            bdcFdcq.setFwsyqr(djsjFwxx.getQlr());
            /**
             * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
             * @description fwxz, gyqk, jyjg, szc, zcs, jzmj 如果房地产权中有值则不从权藉数据中取得
             */
            if (StringUtils.isBlank(bdcFdcq.getFwxz())) {
                bdcFdcq.setFwxz(djsjFwxx.getFwxz());
            }
            if (StringUtils.isBlank(bdcFdcq.getGyqk())) {
                bdcFdcq.setGyqk(djsjFwxx.getGyqk());
            }
            if ((bdcFdcq.getJyjg() == null)) {
                bdcFdcq.setJyjg(djsjFwxx.getJyjg());
            }
            bdcFdcq.setTnjzmj(djsjFwxx.getTnjzmj());
            bdcFdcq.setCg(djsjFwxx.getCg());

            /**
             * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
             * @description 整数存szc，非整数存szmyc
             */
            if (StringUtils.isNotBlank(djsjFwxx.getDycs())) {
                if (NumberUtils.isNumber(djsjFwxx.getDycs())) {
                    if (bdcFdcq.getSzc() == null) {
                        bdcFdcq.setSzc((int) Double.parseDouble(djsjFwxx.getDycs()));
                    }
                } else {
                    if (bdcFdcq.getSzmyc() == null) {
                        bdcFdcq.setSzmyc(djsjFwxx.getDycs());
                    }
                }
            }

            /*sc:独幢，多幢共有信息**/
            if (CollectionUtils.isNotEmpty(djsjFwxx.getFwzbxxList())) {
                DjsjFwzbxx djsjFwzbxx = djsjFwxx.getFwzbxxList().get(0);
                bdcFdcq.setJgsj(djsjFwzbxx.getJgsj());
                bdcFdcq.setFwjg(djsjFwzbxx.getFwjg());
            }
            if (StringUtils.isNotBlank(djsjFwxx.getGhyt())) {
                bdcFdcq.setGhyt(djsjFwxx.getFwyt());
            }
            List<DjsjFwzbxx> djsjFwzbxxList = djsjFwxx.getFwzbxxList();
            if (CollectionUtils.isNotEmpty(djsjFwzbxxList)) {
                if (bdcFdcq.getJzmj() == null) {
                    bdcFdcq.setJzmj(djsjFwzbxxList.get(0).getJzmj());
                }
                if (bdcFdcq.getScmj() == null) {
                    bdcFdcq.setScmj(djsjFwzbxxList.get(0).getJzmj());
                }
                if (bdcFdcq.getZcs() == null) {
                    bdcFdcq.setZcs(djsjFwzbxxList.get(0).getZcs());
                }
                if (bdcFdcq.getMyzcs() == null) {
                    bdcFdcq.setMyzcs(String.valueOf(djsjFwzbxxList.get(0).getZcs()));
                }
            }
        }
        if (djsjZdxx != null) {

            if (StringUtils.isBlank(bdcFdcq.getTdsyqr())) {
                bdcFdcq.setTdsyqr(djsjZdxx.getQlrmc());
            }

            String dwdm = AppConfig.getProperty("dwdm");
            if (StringUtils.equals(dwdm, Constants.DWDM_SZ)) {
                //土地多用途时权利登记信息中的土地使用年限和用途应读取fw_hs里的zzrq，qsrq，tdyt，如果这三个字段的值为空，则取zd_djdcb中的zzrq，qsrq，tdyt三个字段
                if (djsjFwxx != null && djsjFwxx.getQsrq() != null && djsjFwxx.getZzrq() != null && djsjFwxx.getTdyt() != null) {
                    bdcFdcq.setTdsyksqx(djsjFwxx.getQsrq());
                    bdcFdcq.setTdsyjsqx(djsjFwxx.getZzrq());
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcFdcq.getProid());
                    if (bdcSpxx != null) {
                        bdcSpxx.setZdzhyt(djsjFwxx.getTdyt());
                        bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    }
                } else {
                    if (djsjFwxx != null) {
                        if (null == bdcFdcq.getTdsyksqx() && djsjFwxx.getQsrq() != null) {
                            bdcFdcq.setTdsyksqx(djsjFwxx.getQsrq());
                        }
                        if (null == bdcFdcq.getTdsyjsqx() && djsjFwxx.getZzrq() != null) {
                            bdcFdcq.setTdsyjsqx(djsjFwxx.getZzrq());
                        }
                    }
                    if (null == bdcFdcq.getTdsyksqx() && djsjZdxx.getQsrq() != null) {
                        bdcFdcq.setTdsyksqx(djsjZdxx.getQsrq());
                    }
                    if (null == bdcFdcq.getTdsyjsqx() && djsjZdxx.getZzrq() != null) {
                        bdcFdcq.setTdsyjsqx(djsjZdxx.getZzrq());
                    }
                }
            } else {
                if (null == bdcFdcq.getTdsyksqx()) {
                    bdcFdcq.setTdsyksqx(djsjZdxx.getQsrq());
                }
                if (null == bdcFdcq.getTdsyjsqx()) {
                    bdcFdcq.setTdsyjsqx(djsjZdxx.getZzrq());
                }
            }

        }
        return bdcFdcq;
    }

    /**
     * sc :房屋多幢
     *
     * @param bdcFdcqDz
     * @param djsjFwxx
     * @param djsjZdxx
     * @return
     */
    @Override
    public BdcFdcqDz getBdcFdcqDzFromFwxx(BdcFdcqDz bdcFdcqDz, DjsjFwxx djsjFwxx, DjsjZdxx djsjZdxx) {
        if (bdcFdcqDz == null) {
            bdcFdcqDz = new BdcFdcqDz();
        }
        if (djsjFwxx != null) {
            /*sc:房屋信息添加产权来源**/
            bdcFdcqDz.setCqly(djsjFwxx.getCqly());
            /*sc:房屋信息添加房屋档案号**/
            bdcFdcqDz.setFwdah(djsjFwxx.getFcdah());
            /*sc:房屋信息添加自然栋栋号**/
            bdcFdcqDz.setZrzh(djsjFwxx.getZrddh());
            bdcFdcqDz.setDytdmj(djsjFwxx.getDytdmj());
            bdcFdcqDz.setFtjzmj(djsjFwxx.getFtjzmj());
            if (StringUtils.isNotBlank(djsjFwxx.getFwlx()) && NumberUtils.isNumber(djsjFwxx.getFwlx())) {
                bdcFdcqDz.setFwlx(Integer.valueOf(djsjFwxx.getFwlx()));
            }
            bdcFdcqDz.setFttdmj(djsjFwxx.getFttdmj());
            bdcFdcqDz.setFwsyqr(djsjFwxx.getQlr());
            bdcFdcqDz.setFwxz(djsjFwxx.getFwxz());
            bdcFdcqDz.setGyqk(djsjFwxx.getGyqk());
            bdcFdcqDz.setFdcjyjg(djsjFwxx.getJyjg());
            bdcFdcqDz.setTnjzmj(djsjFwxx.getTnjzmj());
            if (bdcFdcqDz.getTnjzmj() == null || (bdcFdcqDz.getTnjzmj() != null && bdcFdcqDz.getTnjzmj() == 0)) {
                bdcFdcqDz.setTnjzmj(djsjFwxx.getJzmj());
            }


            List<DjsjFwzbxx> djsjFwzbxxList = djsjFwxx.getFwzbxxList();
            List<BdcFwfzxx> bdcFwfzxxList = new ArrayList<BdcFwfzxx>();
            if (CollectionUtils.isNotEmpty(djsjFwzbxxList)) {
                for (int i = 0; i < djsjFwzbxxList.size(); i++) {
                    DjsjFwzbxx djsjFwzbxx = djsjFwzbxxList.get(i);
                    BdcFwfzxx bdcFwfzxx = new BdcFwfzxx();
                    bdcFwfzxx.setJgsj(djsjFwzbxx.getJgsj());
                    bdcFwfzxx.setFwjg(djsjFwzbxx.getFwjg());
                    bdcFwfzxx.setJzmj(djsjFwzbxx.getJzmj());
                    bdcFwfzxx.setZh(djsjFwzbxx.getDh());
                    if (StringUtils.isNotBlank(djsjFwzbxx.getZts()) && NumberUtils.isNumber(djsjFwzbxx.getZts())) {
                        bdcFwfzxx.setZts(Integer.valueOf(djsjFwzbxx.getZts()));
                    }
                    bdcFwfzxx.setQlid(bdcFdcqDz.getQlid());
                    bdcFwfzxx.setFzid(UUIDGenerator.generate18());
                    bdcFwfzxx.setXmmc(djsjFwzbxx.getXmmc());
                    bdcFwfzxx.setGhyt(djsjFwzbxx.getGhyt());
                    bdcFwfzxx.setZcs(CommonUtil.formatEmptyValue(djsjFwzbxx.getZcs()));
                    bdcFwfzxx.setBdcdybh(djsjFwzbxx.getBdcdybh());
                    bdcFwfzxxList.add(bdcFwfzxx);
                    //zwq 房屋房子信息保存，这个取地籍的,所以过度不要保存
                    entityMapper.saveOrUpdate(bdcFwfzxx, bdcFwfzxx.getFzid());
                }
            }
            bdcFdcqDz.setFwfzxxList(bdcFwfzxxList);
        }
        if (djsjZdxx != null) {
            bdcFdcqDz.setTdsyqr(djsjZdxx.getQlrmc());
            bdcFdcqDz.setTdsyksqx(djsjZdxx.getQsrq());
            bdcFdcqDz.setTdsyjsqx(djsjZdxx.getZzrq());
        }
        return bdcFdcqDz;
    }

    /**
     * zx :从过渡数据获取房屋多幢
     *
     * @param bdcFdcqDz
     * @param gdFwList
     * @param gdTd
     * @return
     */
    public BdcFdcqDz getBdcFdcqDzFromGdFw(BdcFdcqDz bdcFdcqDz, List<GdFw> gdFwList, GdTd gdTd, GdTdsyq gdTdsyq, BdcXm bdcXm) {
        if (bdcFdcqDz == null) {
            bdcFdcqDz = new BdcFdcqDz();
        }
        if (CollectionUtils.isNotEmpty(gdFwList)) {
            GdFw gdFw = gdFwList.get(0);
            if (gdFw != null) {
                gdFw = bdcCheckCancelService.getGdFwFilterZdsj(gdFw);
                /*sc:房屋信息添加产权来源**/
                bdcFdcqDz.setCqly(gdFw.getCqly());
                /*sc:房屋信息添加房屋档案号**/
                bdcFdcqDz.setFwdah(gdFw.getDah());
                bdcFdcqDz.setFtjzmj(gdFw.getFtjzmj());
                bdcFdcqDz.setFwxz(gdFw.getFwxz());
                bdcFdcqDz.setGyqk(gdFw.getGyqk());
                bdcFdcqDz.setFdcjyjg(gdFw.getJyjg());
                bdcFdcqDz.setTnjzmj(gdFw.getTnjzmj());
                if (StringUtils.isNotBlank(gdFw.getFwlx()) && NumberUtils.isNumber(gdFw.getFwlx())) {
                    bdcFdcqDz.setFwlx(Integer.parseInt(gdFw.getFwlx()));
                }
            }
            //zwq 分摊土地面积取土地证的分摊面积
            if (gdTdsyq != null) {
                bdcFdcqDz.setFttdmj(gdTdsyq.getFtmj());
                bdcFdcqDz.setDytdmj(gdTdsyq.getDymj());
            }
            if (StringUtils.isBlank(bdcFdcqDz.getQlid())) {
                bdcFdcqDz.setQlid(UUIDGenerator.getLngTime());
            }
        }
        if (gdTd != null) {
            if (bdcFdcqDz.getTdsyksqx() == null) {
                bdcFdcqDz.setTdsyksqx(CalendarUtil.formatDate(gdTd.getQsrq()));
            }
            if (bdcFdcqDz.getTdsyjsqx() == null) {
                bdcFdcqDz.setTdsyjsqx(CalendarUtil.formatDate(gdTd.getZzrq()));
            }
        }
        return bdcFdcqDz;
    }

    @Transactional(readOnly = true)
    public QllxVo getQllxVoFromBdcXm(BdcXm bdcXm, BdcXmRel bdcXmRel, QllxVo qllxVo) {
        QllxVo returnQllx = null;
        if (qllxVo != null) {
            if (StringUtils.isBlank(qllxVo.getQlid())) {
                qllxVo.setQlid(UUIDGenerator.generate18());
            }
            qllxVo = getQllxParentFrom(qllxVo, bdcXm);
            Project project = null;
            if (bdcXm instanceof Project) {
                project = (Project) bdcXm;
            }
            if (bdcXmRel != null) {
                if (qllxVo instanceof BdcJsydzjdsyq) {
                    BdcJsydzjdsyq bdcJsydzjdsyq = (BdcJsydzjdsyq) qllxVo;
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxx(bdcXmRel.getQjid());
                        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                            bdcJsydzjdsyq = getJsydzjdsyqFromZdxx(bdcJsydzjdsyq, djsjZdxxList.get(0));
                        }
                    }
                    returnQllx = bdcJsydzjdsyq;
                } else if (qllxVo instanceof BdcFdcq) {
                    BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        DjsjZdxx djsjZdxx = new DjsjZdxx();
                        DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(bdcXmRel.getQjid());
                        if (djsjFwxx != null) {
                            if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && djsjFwxx.getBdcdyh().length() > 19) {
                                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(djsjFwxx.getBdcdyh().substring(0, 19));
                                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                                    djsjZdxx = djsjZdxxList.get(0);
                                }
                            }
                        } else if (project != null && StringUtils.isNotBlank(project.getBdcdyh()) && project.getBdcdyh().length() > 19) {
                            List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(project.getBdcdyh().substring(0, 19));
                            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                                djsjZdxx = djsjZdxxList.get(0);
                            }
                        }
                        bdcFdcq = getBdcFdcqFromFwxx(bdcFdcq, djsjFwxx, djsjZdxx);

                        if ((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM))) {
                            bdcFdcq.setFzlx(Constants.FZLX_FZM);
                        }

                        /**
                         * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
                         * @description 商品房首次登记存在在建工程抵押的户室, 能选择生成不动产权证书；存在预告登记的户室，只能办理首次登记信息表
                         */
                        if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLFZ_DM) && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcXm.getBdcdyid());
                            map.put("qszt", "1");
                            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                bdcFdcq.setFzlx(Constants.FZLX_FZS);
                            }
                            List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(map);
                            if (CollectionUtils.isNotEmpty(bdcYgList)) {
                                bdcFdcq.setFzlx(Constants.FZLX_FZM);
                            }
                        }

                    }
                    returnQllx = bdcFdcq;
                } else if (qllxVo instanceof BdcCf) {
                    BdcCf bdcCfFromYxm;
                    BdcCf bdcCf = (BdcCf) qllxVo;
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {

                        bdcCfFromYxm = bdcCfService.selectCfByProid(bdcXmRel.getYproid());

                        //zdd 查封从原项目读取信息
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmRel.getYproid());

                        //zwq 续查封等获取查封的权利信息

                        bdcCf = getBdcCfFromCf(bdcCf, bdcCfFromYxm, bdcQlrList, bdcXm, bdcXmRel);


                    } else if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        bdcCf.setCflx(Constants.CFLX_ZD_YCF);
                        //jiangganzhi 关联附属设施 附属设施登记子项要跟随主房登记子项
                        HashMap map = new HashMap();
                        map.put("wiid", bdcXm.getWiid());
                        List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(map);
                        if (CollectionUtils.isNotEmpty(bdcXmList)) {
                            BdcXm zfBdcXm = bdcXmList.get(0);
                            BdcCf zfBdcCf = bdcCfService.selectCfByProid(zfBdcXm.getProid());
                            if (zfBdcCf != null && StringUtils.isNotBlank(zfBdcCf.getCflx()) && !StringUtils.equals(zfBdcCf.getCflx(), Constants.CFLX_ZD_YCF)) {
                                bdcCf.setCflx(zfBdcCf.getCflx());
                            }
                        }
                        //当查封类型是预查封时，被查封权利人继承收件单上权利人
                        String bzxr = "";
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmRel.getProid());
                        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                            for (int i = 0; i < bdcQlrList.size(); i++) {
                                if (i == 0) {
                                    bzxr = bdcQlrList.get(i).getQlrmc();
                                } else {
                                    if (StringUtils.isNotBlank(bzxr) && StringUtils.isNotBlank(bdcQlrList.get(i).getQlrmc())) {
                                        bzxr = bzxr + "、" + bdcQlrList.get(i).getQlrmc();
                                    } else if (StringUtils.isBlank(bzxr)) {
                                        bzxr = bdcQlrList.get(i).getQlrmc();
                                    }
                                }
                            }
                        }
                        bdcCf.setBzxr(bzxr);
                    }
                    bdcCf.setCfksqx(DateUtils.now());
                    if (StringUtils.equals(Constants.DJZX_XF, bdcXm.getDjzx())) {
                        bdcCf.setCflx(Constants.CFLX_XF);
                    }
                    //查封创建即生效，不需要等项目办结
                    bdcCf.setQszt(Constants.QLLX_QSZT_XS);

                    returnQllx = bdcCf;
                } else if (qllxVo instanceof BdcDyaq) {
                    BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                    /**
                     * @author bianwen
                     * @description 抵押权信息优先读取预告抵押的信息
                     */
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                    if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                        List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(bdcBdcdy.getBdcdyh(), Constants.QLLX_QSZT_XS.toString());
                        List<GdYg> gdYgList = gdYgService.getGdygListByBdcdyh(bdcBdcdy.getBdcdyh());
                        if (CollectionUtils.isNotEmpty(bdcYgList)) {
                            for (BdcYg bdcYg : bdcYgList) {
                                if (bdcYg != null && (StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPFDY) || StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_QTYGSPFDY))) {
                                    if (bdcYg.getQdjg() != null) {
                                        bdcDyaq.setBdbzzqse(bdcYg.getQdjg());
                                    }
                                    if (bdcYg.getZwlxksqx() != null) {
                                        bdcDyaq.setZwlxksqx(bdcYg.getZwlxksqx());
                                    }
                                    if (bdcYg.getZwlxjsqx() != null) {
                                        bdcDyaq.setZwlxjsqx(bdcYg.getZwlxjsqx());
                                    }
                                    if (bdcYg.getQljssj() != null) {
                                        bdcDyaq.setZxsj(bdcYg.getDjsj());
                                    }
                                    if (bdcYg.getYwh() != null) {
                                        bdcDyaq.setZxdyywh(bdcYg.getYwh());
                                    }
                                    if (bdcYg.getFj() != null) {
                                        bdcDyaq.setFj(bdcYg.getFj());
                                    }
                                }
                            }
                        } else if (CollectionUtils.isNotEmpty(gdYgList)) {
                            for (GdYg gdYg : gdYgList) {
                                if (gdYg != null && StringUtils.isNotBlank(gdYg.getYgdjzl()) && StringUtils.equals(gdYg.getYgdjzl(), Constants.YGDJZL_YGSPFDYAQ_MC)) {
                                    if (gdYg.getQdjg() != null) {
                                        bdcDyaq.setBdbzzqse(gdYg.getQdjg());
                                    }
                                    if (gdYg.getDyksrq() != null) {
                                        bdcDyaq.setZwlxksqx(gdYg.getDyksrq());
                                    }
                                    if (gdYg.getDyjsrq() != null) {
                                        bdcDyaq.setZwlxjsqx(gdYg.getDyjsrq());
                                    }
                                }
                            }
                        } else if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                            /**
                             * @author bianwen
                             * @description 若不存在预告抵押，则判断该不动产单元之前有没有做过抵押，若存在则直接继承
                             */
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcXm.getBdcdyid());
                            map.put("qszt", 1);
                            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                /**
                                 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                                 * @description 抵押首次不需要继承
                                 */
                                if (!CommonUtil.indexOfStrs(Constants.SPYZ_DYSC_SQLX, bdcXm.getSqlx())) {
                                    bdcDyaq.setBdbzzqse(bdcDyaqList.get(0).getBdbzzqse());
                                    bdcDyaq.setZwlxjsqx(bdcDyaqList.get(0).getZwlxjsqx());
                                    bdcDyaq.setZwlxksqx(bdcDyaqList.get(0).getZwlxksqx());
                                    bdcDyaq.setFj(bdcDyaqList.get(0).getFj());
                                }
                                /**
                                 * @author bianwen
                                 * @description 在建工程抵押相关
                                 */
                                bdcDyaq.setZjgczl(bdcDyaqList.get(0).getZjgczl());
                                bdcDyaq.setZjgcdyfw(bdcDyaqList.get(0).getZjgcdyfw());
                            } else {
                                /**
                                 * @author bianwen
                                 * @description 在建工程，新生成的抵押权 要获取spxx里的坐落，不再在帆软中处理
                                 */
                                if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, bdcXm.getSqlx())) {
                                    Example example = new Example(BdcSpxx.class);
                                    example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
                                    List<BdcSpxx> spxxList = entityMapper.selectByExample(example);
                                    if (CollectionUtils.isNotEmpty(spxxList)) {
                                        bdcDyaq.setZjgczl(spxxList.get(0).getZl());
                                    }
                                }
                            }
                            /**
                             * @author yanzhenkun
                             * @description 苏州1019抵押首次登记，针对项目内多幢，处理抵押面积初始化信息
                             */
                            if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWDY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWDY_XS_DM) && bdcBdcdy != null && StringUtils.isNotEmpty(bdcBdcdy.getBdcdyh()) && StringUtils.equals("1", bdcDjsjService.getBdcdyfwlxByBdcdyh(bdcBdcdy.getBdcdyh())) && StringUtils.isNotBlank(bdcXm.getXmly())) {
                                String xmly = bdcXm.getXmly();
                                String bdcdyid = "";
                                if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                    bdcdyid = bdcXm.getBdcdyid();
                                } else if (StringUtils.isNotBlank(bdcXm.getBdcdyh())) {
                                    bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcXm.getBdcdyh());
                                }
                                String bdcdyh = "";
                                if (StringUtils.isNotBlank(bdcXm.getBdcdyh())) {
                                    bdcdyh = bdcXm.getBdcdyh();
                                } else if (StringUtils.isNotBlank(bdcXm.getProid())) {
                                    bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
                                }
                                String djlx = "";
                                djlx = bdcXm.getDjlx();
                                Double fwzmj = bdcFdcqDzService.getBdcFdcqDzFwzmj(xmly, bdcdyid, bdcdyh, djlx);
                                if (bdcDyaq.getFwdymj() == null && fwzmj > 0) {
                                    bdcDyaq.setFwdymj(fwzmj);
                                }
                            }

                        }
                    }

                    //抵押转移
                    if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZGDY_SQLXDM, bdcXm.getSqlx())) {
                        bdcDyaq.setDyfs(Constants.DYFS_ZGEDY);
                    } else {
                        bdcDyaq.setDyfs(Constants.DYFS_YBDY);
                    }
                    //读取过渡抵押数据

                    List<GdDy> gdDyList = gdFwService.getGdDyListByGdproid(bdcXmRel.getYproid(), 0);
                    if (CollectionUtils.isNotEmpty(gdDyList)) {
                        GdDy gdDy = gdDyList.get(0);
                        gdDy = bdcCheckCancelService.getGdDyFilterZdsj(gdDy);
                        bdcDyaq = gdFwService.readBdcDyaqFromGdDy(gdDy, bdcDyaq, null);
                    }

                    if (bdcXm != null && StringUtils.equals(bdcXm.getBdclx(), Constants.BDCLX_TDFW)) {
                        DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(bdcXmRel.getQjid());
                        if (djsjFwxx != null) {
                            bdcDyaq.setFttdmj(djsjFwxx.getFttdmj());
                        }
                    }

                    if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZYDJ, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, bdcXm.getSqlx()) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GZ_DM))) {
                        Example example = new Example(BdcDyaq.class);
                        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXmRel.getYproid());
                        List<BdcDyaq> yBdcDyaqList = entityMapper.selectByExample(example);
                        if (CollectionUtils.isNotEmpty(yBdcDyaqList)) {
                            BdcDyaq yBdcDyaq = yBdcDyaqList.get(0);
                            if (StringUtils.equals(yBdcDyaq.getBdcdyid(), bdcXm.getBdcdyid())) {
                                if (StringUtils.isNotBlank(bdcDyaq.getProid())) {
                                    yBdcDyaq.setProid(bdcDyaq.getProid());
                                }
                                if (StringUtils.isNotBlank(bdcDyaq.getQlid())) {
                                    yBdcDyaq.setQlid(bdcDyaq.getQlid());
                                }
                                if (StringUtils.isNotBlank(bdcDyaq.getYwh())) {
                                    yBdcDyaq.setYwh(bdcDyaq.getYwh());
                                }

                            } else {
                                if (StringUtils.isNotBlank(bdcDyaq.getProid())) {
                                    yBdcDyaq.setProid(bdcDyaq.getProid());
                                }
                                if (StringUtils.isNotBlank(bdcDyaq.getQlid())) {
                                    yBdcDyaq.setQlid(bdcDyaq.getQlid());
                                }
                                if (StringUtils.isNotBlank(bdcDyaq.getYwh())) {
                                    yBdcDyaq.setYwh(bdcDyaq.getYwh());
                                }

                                yBdcDyaq.setBdcdyid(bdcXm.getBdcdyid());
                            }

                            yBdcDyaq.setDbr(null);
                            yBdcDyaq.setDjsj(null);
                            yBdcDyaq.setQszt(Constants.QLLX_QSZT_LS);
                            bdcDyaq = yBdcDyaq;

                        }
                    }
                    /**
                     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                     *@description 抵押不动产类型
                     */
                    if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                        bdcDyaq.setDybdclx(bdcBdcdy.getBdclx());
                    }
                    returnQllx = bdcDyaq;
                } else if (qllxVo instanceof BdcDyq) {
                    BdcDyq bdcDyq = (BdcDyq) qllxVo;

                    returnQllx = bdcDyq;

                } else if (qllxVo instanceof BdcHysyq) {
                    BdcHysyq bdcHysyq = (BdcHysyq) qllxVo;
                    if (bdcXmRel.getQjid() != null && !bdcXmRel.getQjid().equals("")) {
                        DjsjZhxx djsjZhxx = bdcDjsjService.getDjsjZhxx(bdcXmRel.getQjid());
                        if (djsjZhxx != null) {
                            bdcHysyq = getHysyqFromZhxx(bdcHysyq, djsjZhxx);
                        }
                    }
                    returnQllx = bdcHysyq;
                } else if (qllxVo instanceof BdcJzwgy) {
                    BdcJzwgy bdcJzwgy = (BdcJzwgy) qllxVo;
                    returnQllx = bdcJzwgy;

                } else if (qllxVo instanceof BdcJzwsyq) {
                    BdcJzwsyq bdcJzwsyq = (BdcJzwsyq) qllxVo;
                    //zdd 读取构筑物信息

                    returnQllx = bdcJzwsyq;
                } else if (qllxVo instanceof BdcLq) {             //林权
                    BdcLq bdcLq = (BdcLq) qllxVo;
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        DjsjLqxx djsjLqxx = bdcDjsjService.getDjsjLqxx(bdcXmRel.getQjid());
                        bdcLq = getBdcLqFromLqxx(bdcLq, djsjLqxx);
                    }
                    returnQllx = bdcLq;
                } else if (qllxVo instanceof BdcQsq) {
                    BdcQsq bdcQsq = (BdcQsq) qllxVo;
                    returnQllx = bdcQsq;
                } else if (qllxVo instanceof BdcTdcbnydsyq) {                                      //土地承包经营权
                    BdcTdcbnydsyq bdcTdcbnydsyq = (BdcTdcbnydsyq) qllxVo;
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        DjsjCbzdDcb cbzdDcb = djSjMapper.getDjsjCbzdDcbByDjid(bdcXmRel.getQjid());
                        if (cbzdDcb != null) {
                            List<DjsjCbzdCbf> djsjCbzdCbfList = null;
                            if (StringUtils.isNotBlank(cbzdDcb.getZddcbIndex())) {
                                djsjCbzdCbfList = djSjMapper.getDjsjCbzdCbfByDcbid(cbzdDcb.getZddcbIndex());
                            }
                            List<DjsjCbzdFbf> djsjCbzdFbfList = null;
                            if (StringUtils.isNotBlank(cbzdDcb.getZddcbIndex())) {
                                djsjCbzdFbfList = djSjMapper.getDjsjCbzdFbfByDcbid(cbzdDcb.getZddcbIndex());
                            }
                            bdcTdcbnydsyq = getBdcTdcbnydsyqFromTdcb(cbzdDcb, bdcTdcbnydsyq, djsjCbzdCbfList, djsjCbzdFbfList);
                        }
                    }
                    returnQllx = bdcTdcbnydsyq;
                } else if (qllxVo instanceof BdcTdsyq) {         //土地所有权
                    BdcTdsyq bdcTdsyq = (BdcTdsyq) qllxVo;
                    String djh = bdcDyMapper.getZhhByProid(bdcTdsyq.getProid());
                    if (StringUtils.isNotBlank(djh)) {
                        BdcQszdZdmj qszdZdmj = djSjMapper.getBdcQszdZdmj(djh);
                        if (qszdZdmj != null) {
                            bdcTdsyq.setNydmj(qszdZdmj.getNydmj());
                            bdcTdsyq.setGdmj(qszdZdmj.getGdmj());
                            bdcTdsyq.setLdmj(qszdZdmj.getLdmj());
                            bdcTdsyq.setCdmj(qszdZdmj.getCdmj());
                            bdcTdsyq.setQtmj(qszdZdmj.getQtmj());
                            bdcTdsyq.setJsydmj(qszdZdmj.getJsydmj());
                            bdcTdsyq.setWlydmj(qszdZdmj.getWlydmj());
                        }
                    }
                    //zx 如果存在土地信息  则以土地信息为主(过渡目前没有考虑到所有权暂时先写，方法实现没有)
                    returnQllx = bdcTdsyq;
                } else if (qllxVo instanceof BdcYg) {
                    BdcYg bdcYg = (BdcYg) qllxVo;

                    /*判断项目来源是否为不动产系统，如果是则取过渡数据，否则取地籍数据*/
                    if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)) {
                        if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                            DjsjZdxx djsjZdxx = new DjsjZdxx();
                            DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(bdcXmRel.getQjid());
                            if (djsjFwxx != null) {
                                if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && djsjFwxx.getBdcdyh().length() > 19) {
                                    List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(djsjFwxx.getBdcdyh().substring(0, 19));
                                    if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                                        djsjZdxx = djsjZdxxList.get(0);
                                    }
                                }
                            } else if (project != null && StringUtils.isNotBlank(project.getBdcdyh()) && project.getBdcdyh().length() > 19) {
                                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(project.getBdcdyh().substring(0, 19));
                                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                                    djsjZdxx = djsjZdxxList.get(0);
                                }
                            }
                            if (djsjFwxx != null) {
                                bdcYg.setFttdmj(djsjFwxx.getFttdmj());
                            }
                            bdcYg = getBdcYgFromFwxx(bdcYg, djsjFwxx, djsjZdxx);
                        }
                    } else {
                        GdFw gdFw = new GdFw();
                        GdQlr gdQlr = new GdQlr();
                        if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                            List<GdFw> gdFwList = gdFwService.getGdFwByQlid(bdcXmRel.getYqlid());
                            if (CollectionUtils.isNotEmpty(gdFwList)) {
                                gdFw = gdFwList.get(0);
                            }
                        }


                        List<GdQlr> gdQlrs = gdQlrService.queryGdQlrs(bdcXmRel.getYqlid(), Constants.QLRLX_QLR);
                        if (CollectionUtils.isNotEmpty(gdQlrs)) {
                            gdQlr = gdQlrs.get(0);
                        }
                        //jyl过渡预告登记的种类
                        GdYg gdYg = gdFwService.getGdYgByYgid(bdcXmRel.getYqlid(), null);

                        bdcYg = getBdcYgFromGdFwxx(bdcYg, gdFw, gdQlr, gdYg);
                    }
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXmRel.getYproid());
                        BdcFdcq bdcFdcq = null;
                        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                            bdcFdcq = bdcFdcqList.get(0);
                        }
                        if (bdcFdcq != null) {
                            /**
                             * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                             * @description 整数存szc，非整数存szmyc
                             */
                            if (bdcFdcq.getSzc() != null) {
                                bdcYg.setSzc(bdcFdcq.getSzc());
                            }
                            if (bdcFdcq.getSzmyc() != null) {
                                bdcYg.setSzmyc(bdcFdcq.getSzmyc());
                            }

                            if (bdcFdcq.getZcs() != null) {
                                bdcYg.setZcs(bdcFdcq.getZcs());
                            }
                            bdcYg.setJzmj(bdcFdcq.getJzmj());
                            bdcYg.setGyqk(bdcFdcq.getGyqk());
                            bdcYg.setFwxz(bdcFdcq.getFwxz());
                        }
                        List<BdcQlr> qlrList = bdcQlrService.queryBdcQlrByProid(bdcXmRel.getYproid());
                        if (CollectionUtils.isNotEmpty(qlrList) && StringUtils.isNotBlank(qlrList.get(0).getQlrmc())) {
                            bdcYg.setTdqlr(qlrList.get(0).getQlrmc());
                        }
                        //sc继承原预告的信息
                        BdcYg yBdcyg = bdcYgService.getBdcYgByProid(bdcXmRel.getYproid());
                        if (yBdcyg != null) {
                            bdcYg.setJzmj(yBdcyg.getJzmj());
                            bdcYg.setFwxz(yBdcyg.getFwxz());
                            bdcYg.setSzc(yBdcyg.getSzc());
                            bdcYg.setSzmyc(yBdcyg.getSzmyc());
                            bdcYg.setZcs(yBdcyg.getZcs());
                            bdcYg.setTdqlr(yBdcyg.getTdqlr());
                            bdcYg.setQdjg(yBdcyg.getQdjg());
                            bdcYg.setZwlxksqx(yBdcyg.getZwlxksqx());
                            bdcYg.setZwlxjsqx(yBdcyg.getZwlxjsqx());
                        }
                    }

                    if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPF)) {
                        bdcYg.setYgdjzl(Constants.YGDJZL_YGSPF);
                        //预购商品房，土地使用权人默认全体业主
                        bdcYg.setTdqlr(Constants.TDSYQR_QTYZ);
                    } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFDY)) {
                        bdcYg.setYgdjzl(Constants.YGDJZL_YGSPFDY);
                    } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_FWZYYG) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_GYJSYT) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_JSYTJT))) {
                        bdcYg.setYgdjzl(Constants.YGDJZL_QTBDCMMYG);
                    } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BDCDY))//lcl增加不动产抵押预告登记判断
                    {
                        bdcYg.setYgdjzl(Constants.YGDJZL_YGSPFDY);
                    }
                    returnQllx = bdcYg;
                } else if (qllxVo instanceof BdcYy) {
                    BdcYy bdcYy = (BdcYy) qllxVo;

                    returnQllx = bdcYy;
                } else if (qllxVo instanceof BdcFdcqDz) {                             //sc：房屋多幢
                    BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        DjsjZdxx djsjZdxx = new DjsjZdxx();
                        DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(bdcXmRel.getQjid());
                        if (djsjFwxx != null) {
                            if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && djsjFwxx.getBdcdyh().length() > 19) {
                                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(djsjFwxx.getBdcdyh().substring(0, 19));
                                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                                    djsjZdxx = djsjZdxxList.get(0);
                                }
                            }
                        } else if (project != null && StringUtils.isNotBlank(project.getBdcdyh()) && project.getBdcdyh().length() > 19) {
                            List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(project.getBdcdyh().substring(0, 19));
                            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                                djsjZdxx = djsjZdxxList.get(0);
                            }
                        }

                        bdcFdcqDz = getBdcFdcqDzFromFwxx(bdcFdcqDz, djsjFwxx, djsjZdxx);
                    }
                    //zx 如果存在房产信息  则以房产信息为主
                    returnQllx = bdcFdcqDz;
                }

                //zdd 当项目来源不是不动产业务时：读取过渡数据到对应的权利信息中   后面可以考虑用aop拦截这个方法  实现数据的读取
                if (StringUtils.isNotBlank(bdcXmRel.getYqlid()) && !bdcXmRel.getYdjxmly().equals("1")) {
                    returnQllx = getQllxVoFromGdxm(bdcXmRel.getYqlid(), returnQllx, bdcXm);
                }


            }
        }
        //zwq 匹配换证登记没有权利，所以需要条件
        if (returnQllx != null) {
            returnQllx = gyqkDefutl(returnQllx);
        }
        return returnQllx;
    }

    @Override
    @Transactional(readOnly = true)
    public QllxVo getQllxVoFromBdcXm(BdcXm bdcXm) {
        QllxVo qllxVo = null;

        if (bdcXm != null) {
            qllxVo = makeSureQllx(bdcXm);
            if (qllxVo != null) {
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                BdcXmRel bdcXmRel = null;
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    bdcXmRel = bdcXmRelList.get(0);
                }
                qllxVo = getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);


            }
        }
        return qllxVo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getQllxIdByproid(String proid) {
        return bdcQllxMapper.getQllxIdByproid(proid);
    }

    public BdcYg getBdcYgFromFwxx(BdcYg bdcYg, DjsjFwxx djsjFwxx, DjsjZdxx djsjZdxx) {
        if (bdcYg == null) {
            bdcYg = new BdcYg();
        }
        if (djsjFwxx != null) {
            bdcYg.setGhyt(djsjFwxx.getGhyt());
            bdcYg.setFwxz(djsjFwxx.getFwxz());
            List<DjsjFwzbxx> djsjFwzbxxList = djsjFwxx.getFwzbxxList();
            if (CollectionUtils.isNotEmpty(djsjFwzbxxList)) {
                bdcYg.setZcs(djsjFwzbxxList.get(0).getZcs());
                bdcYg.setFwjg(djsjFwzbxxList.get(0).getFwjg());
            }


            if (StringUtils.isNotBlank(djsjFwxx.getDycs())) {
                //相城数据是双精度，我们数据库数据是int型，遇到5.0这种情况转化报错，统一放入szmyc
                bdcYg.setSzmyc(djsjFwxx.getDycs());
            }

            if (StringUtils.isNotBlank(djsjFwxx.getFwxz()) && NumberUtils.isNumber(djsjFwxx.getFwxz())) {
                bdcYg.setFwxz(Integer.valueOf(djsjFwxx.getFwxz()).toString());
            }
            bdcYg.setJzmj(djsjFwxx.getJzmj());
        }
        if (djsjZdxx != null) {
            bdcYg.setTdqlr(djsjZdxx.getQlrmc());
            bdcYg.setTdsyksqx(djsjZdxx.getQsrq());
            bdcYg.setTdsyjsqx(djsjZdxx.getZzrq());
        }
        return bdcYg;
    }

    /*将过渡房屋信息填充到预告信息中*/
    public BdcYg getBdcYgFromGdFwxx(BdcYg bdcYg, GdFw gdFw, GdQlr gdQlr, GdYg gdYg) {
        if (bdcYg == null) {
            bdcYg = new BdcYg();
        }
        if (gdFw != null) {
            bdcYg.setFwxz(gdFw.getFwxz());
            bdcYg.setZcs(gdFw.getZcs());
            try {
                Integer szc = Integer.parseInt(gdFw.getSzc());
                bdcYg.setSzc(szc);
            } catch (NumberFormatException e) {
                String szmyc = gdFw.getSzc();
                bdcYg.setSzmyc(szmyc);
            }
            bdcYg.setJzmj(gdFw.getJzmj());
        }
        bdcYg.setTdqlr(gdQlr.getQlr());
        if (gdYg != null && StringUtils.isNotBlank(gdYg.getYgdjzl())) {
            if (StringUtils.equals(gdYg.getYgdjzl(), Constants.YGDJZL_YGSPF_MC)) {
                bdcYg.setYgdjzl(Constants.YGDJZL_YGSPF);
            } else if (StringUtils.equals(gdYg.getYgdjzl(), Constants.YGDJZL_YGSPFDYAQ_MC)) {
                bdcYg.setYgdjzl(Constants.YGDJZL_YGSPFDY);
            }
        }
        return bdcYg;
    }

    public BdcYg getBdcYgFromGdYg(BdcYg bdcYg, GdYg gdYg, List<GdQlr> gdTdQlrList, List<GdFw> gdFwList) {
        if (bdcYg == null) {
            bdcYg = new BdcYg();
        }

        if (gdYg != null) {
            if (StringUtils.isNotBlank(gdYg.getYgdjzl())) {
                if (StringUtils.equals(gdYg.getYgdjzl(), Constants.YGDJZL_YGSPF_MC)) {
                    bdcYg.setYgdjzl(Constants.YGDJZL_YGSPF);
                } else if (StringUtils.equals(gdYg.getYgdjzl(), Constants.YGDJZL_YGSPFDYAQ_MC)) {
                    bdcYg.setYgdjzl(Constants.YGDJZL_YGSPFDY);
                }
            }
            bdcYg.setQdjg(gdYg.getQdjg());
            //zwq 将gd_yg的fj赋予bdc_yg
            if (StringUtils.isNotBlank(gdYg.getFj())) {
                bdcYg.setFj(gdYg.getFj());
            }
            //lx  将gd_yg的日期赋给bdc_yg
            if (gdYg.getDyksrq() != null) {
                bdcYg.setZwlxksqx(gdYg.getDyksrq());
            }
            if (gdYg.getDyjsrq() != null) {
                bdcYg.setZwlxjsqx(gdYg.getDyjsrq());
            }
        }
        if (CollectionUtils.isNotEmpty(gdFwList)) {
            Double jzmj = 0.0;
            for (GdFw gdFw : gdFwList) {
                if (gdFw.getZcs() != null) {
                    bdcYg.setZcs(gdFw.getZcs());
                }
                if (StringUtils.isNotBlank(gdFw.getSzc())) {
                    try {
                        bdcYg.setSzc(Integer.parseInt(gdFw.getSzc()));
                    } catch (Exception e) {
                        bdcYg.setSzmyc(gdFw.getSzc());
                    }
                }
                if (gdFw.getJzmj() != null) {
                    jzmj = jzmj + gdFw.getJzmj();
                }
            }
            if (jzmj > 0) {
                bdcYg.setJzmj(jzmj);
            }
        }
        String qlrs = gdQlrService.combinationQlr(gdTdQlrList);
        bdcYg.setTdqlr(qlrs);
        return bdcYg;
    }

    public BdcYy getBdcYyFromGdYy(BdcYy bdcYy, GdYy gdYy) {
        if (bdcYy == null) {
            bdcYy = new BdcYy();
        }

        if (gdYy != null) {
            bdcYy.setYysx(gdYy.getYysx());
        }
        return bdcYy;
    }


    public BdcCf getBdcCfFromCf(BdcCf bdcCf, BdcCf bdcCfFromYxm, List<BdcQlr> bdcQlrList, BdcXm bdcxm, BdcXmRel bdcXmRel) {
        String bzxr = bdcQlrService.combinationQlr(bdcQlrList);
        //查询单元的是否查封
        List<BdcCf> bdcCfList = bdcCfService.getCfByBdcdyid(bdcCf.getBdcdyid());
        if (CollectionUtils.isNotEmpty(bdcCfList)) {
            boolean cflxCf = false;
            for (BdcCf cf : bdcCfList) {
                if (cf != null && (StringUtils.equals(cf.getCflx(), Constants.CFLX_ZD_CF) || StringUtils.equals(cf.getCflx(), Constants.CFLX_LHCF) || StringUtils.equals(cf.getCflx(), Constants.CFLX_XF))) {
                    cflxCf = true;
                    break;
                }
            }
            if (cflxCf) {
                bdcCf.setCflx(Constants.CFLX_LHCF);
            } else {
                bdcCf.setCflx(Constants.CFLX_ZD_LHYCF);
            }
        } else {
            bdcCf.setCflx(Constants.CFLX_ZD_CF);
        }
        //查看当前选择的查封
        if (bdcCfFromYxm != null) {
            //zdd 如果原项目是查封   那么被执行人一定是相同的
            if (StringUtils.isNotBlank(bdcCfFromYxm.getBzxr())) {
                bzxr = bdcCfFromYxm.getBzxr();
            }
            bdcCf.setCflx(Constants.CFLX_ZD_CF);
        }
        if (StringUtils.equals(Constants.DJZX_XF, bdcxm.getDjzx())) {
            bdcCf.setCflx(Constants.CFLX_XF);
        }
        bdcCf.setBzxr(bzxr);

        return bdcCf;
    }


    public BdcCf getBdcCfFromGdCf(BdcCf bdcCf, GdCf gdCf, List<GdQlr> gdQlrList, String sjppType, BdcXm bdcXm) {
        if (bdcCf == null) {
            bdcCf = new BdcCf();
        }

        bdcCf.setCfjg(gdCf.getCfjg());
        if (StringUtils.isNotBlank(gdCf.getDbr())) {
            bdcCf.setDbr(gdCf.getDbr());
        }
        if (gdCf.getDjsj() != null) {
            bdcCf.setDjsj(gdCf.getDjsj());
        }
        //zhouwanqing 成果匹配的查封类型不能取过渡查封中的查封类型,对成果的查封权利进行查封就是查封(续封的查封类型是查封)
        if (StringUtils.equals(sjppType, Constants.PPLX_GC)) {
            bdcCf.setCflx(gdCf.getCflx());
        } else {
            //解封登记查封类型继承原权利的查封类型
            if (CommonUtil.indexOfStrs(Constants.SQLX_ZXCFDJ_DM, bdcXm.getSqlx())) {
                String cflxdm = bdcZdGlService.getCflxDmByMc(gdCf.getCflx());
                if (StringUtils.isNotBlank(cflxdm)) {
                    bdcCf.setCflx(cflxdm);
                }
            } else {
                bdcCf.setCflx(Constants.CFLX_ZD_CF);
            }
        }

        /**
         * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
         * @description 过渡续封不继承查封文号, 查封开始日期, 查封结束日期
         */
        if (!StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_CF) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLCF)) {
            bdcCf.setCfwh(gdCf.getCfwh());
            bdcCf.setCfksqx(gdCf.getCfksrq());
            bdcCf.setCfjsqx(gdCf.getCfjsrq());
            bdcCf.setCfwj(gdCf.getCfwj());
        }
        bdcCf.setCffw(gdCf.getCffw());
        bdcCf.setFj(gdCf.getFj());
        String bzxr = gdQlrService.combinationQlr(gdQlrList);
        bdcCf.setBzxr(bzxr);
        //zwq (海门)添加法院送达人和联系方式
        String sysVersion = AppConfig.getProperty("sys.version");
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_HM)) {
            bdcCf.setFysdr(gdCf.getFysdr());
            bdcCf.setFysdrlxfs(gdCf.getFysdrlxfs());
        }
        return bdcCf;
    }

    /*水产养殖**/
    @Override
    public BdcTdcbnydsyq getBdcTtFromTtxx(BdcTdcbnydsyq tdcbnydsyq, DjsjNydDcb djsjNydDcb) {
        if (tdcbnydsyq == null) {
            tdcbnydsyq = new BdcTdcbnydsyq();
        }
        if (djsjNydDcb != null) {
            String djh = "";
            if (StringUtils.isNotBlank(djsjNydDcb.getDjh())) {
                djh = djsjNydDcb.getDjh();
            } else if (StringUtils.isNotBlank(djsjNydDcb.getBdcdyh())) {
                djh = StringUtils.substring(djsjNydDcb.getBdcdyh(), 0, 19);
            }
            String fbfmc = djSjMapper.getFbfmcByDjh(djh);
            List<DjsjNydDcb> djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(djh);
            if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                djsjNydDcb = djsjNydDcbList.get(0);
            }
            if (StringUtils.isNotBlank(fbfmc)) {
                tdcbnydsyq.setFbfmc(fbfmc);
            } else if (CollectionUtils.isNotEmpty(djsjNydDcbList) && StringUtils.isNotBlank(djsjNydDcbList.get(0).getTdsyzmc())) {
                tdcbnydsyq.setFbfmc(djsjNydDcbList.get(0).getTdsyzmc());
            } else {
                if (djsjNydDcb != null) {
                    if (djsjNydDcb.getFzmj() != null && djsjNydDcb.getFzmj() != 0) {
                        tdcbnydsyq.setSyqmj(djsjNydDcb.getFzmj());
                    } else if (djsjNydDcb.getScmj() != null && djsjNydDcb.getScmj() != 0) {
                        tdcbnydsyq.setSyqmj(djsjNydDcb.getScmj());
                    }
                    if (djsjNydDcb.getQsrq() != null) {
                        tdcbnydsyq.setQlqssj(djsjNydDcb.getQsrq());
                    }
                    if (djsjNydDcb.getZzrq() != null) {
                        tdcbnydsyq.setQljssj(djsjNydDcb.getZzrq());
                    }
                }
            }
        }
        if (djsjNydDcb != null) {
            tdcbnydsyq.setQllx(djsjNydDcb.getQsxz());
        }
        return tdcbnydsyq;
    }


    /*林权**/
    public BdcLq getBdcLqFromLqxx(BdcLq bdcLq, DjsjLqxx djsjLqxx) {
        if (bdcLq == null) {
            bdcLq = new BdcLq();
        }
        if (djsjLqxx != null) {
            StringBuilder lmsyqr = new StringBuilder();
            StringBuilder lmsuqr = new StringBuilder();
            String djh = "";
            if (StringUtils.isNotBlank(djsjLqxx.getDjh())) {
                djh = djsjLqxx.getDjh();
            } else if (StringUtils.isNotBlank(djsjLqxx.getBdcdyh())) {
                djh = StringUtils.substring(djsjLqxx.getBdcdyh(), 0, 19);
            }
            List<NydQlr> nydQlrList = bdcQlrService.getNydQlrByDjh(djh);
            if (CollectionUtils.isNotEmpty(nydQlrList)) {
                for (int i = 0; i < nydQlrList.size(); i++) {
                    NydQlr nydQlr = nydQlrList.get(i);
                    if (StringUtils.equals(nydQlr.getSflmsuqr(), "1")) {
                        if (i != nydQlrList.size() - 1) {
                            lmsuqr.append(nydQlr.getQlr()).append(" ");
                        } else {
                            lmsuqr.append(nydQlr.getQlr());
                        }
                    }
                    if (StringUtils.equals(nydQlr.getSflmsyqr(), "1")) {
                        if (i != nydQlrList.size() - 1) {
                            lmsyqr.append(nydQlr.getQlr()).append(" ");
                        } else {
                            lmsyqr.append(nydQlr.getQlr());
                        }
                    }
                }
            }
            String fbfmc = djSjMapper.getFbfmcByDjh(djh);
            List<DjsjNydDcb> djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(djh);
            DjsjNydDcb djsjNydDcb = null;
            if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                djsjNydDcb = djsjNydDcbList.get(0);
            }
            if (StringUtils.isNotBlank(fbfmc)) {
                bdcLq.setFbfmc(fbfmc);
            } else if (CollectionUtils.isNotEmpty(djsjNydDcbList) && StringUtils.isNotBlank(djsjNydDcbList.get(0).getTdsyzmc())) {
                bdcLq.setFbfmc(djsjNydDcbList.get(0).getTdsyzmc());
            }
            if (djsjLqxx.getMj() != null && djsjLqxx.getMj() != 0) {
                bdcLq.setSyqmj(djsjLqxx.getMj());
            } else {
                if (djsjNydDcb != null) {
                    if (djsjNydDcb.getFzmj() != null && djsjNydDcb.getFzmj() != 0) {
                        bdcLq.setSyqmj(djsjNydDcb.getFzmj());
                    } else {
                        bdcLq.setSyqmj(djsjNydDcb.getScmj());
                    }
                    if (djsjNydDcb.getQsrq() != null) {
                        bdcLq.setLdsyksqx(djsjNydDcb.getQsrq());
                    }
                    if (djsjNydDcb.getZzrq() != null) {
                        bdcLq.setLdsyjsqx(djsjNydDcb.getZzrq());
                    }
                }

            }
            if (djsjNydDcb != null) {
                if (djsjNydDcb.getQsrq() != null) {
                    bdcLq.setLdsyksqx(djsjNydDcb.getQsrq());
                }
                if (djsjNydDcb.getZzrq() != null) {
                    bdcLq.setLdsyjsqx(djsjNydDcb.getZzrq());
                }
            }
            bdcLq.setLmsuqr(lmsuqr.toString());
            bdcLq.setLmsyqr(lmsyqr.toString());
            bdcLq.setZysz(djsjLqxx.getZysz());
            bdcLq.setQy(djsjLqxx.getQy());
            bdcLq.setZs(djsjLqxx.getZs());
            bdcLq.setLz(djsjLqxx.getLz());
            if (djsjLqxx.getZlnd() != null) {
                bdcLq.setZlnd(djsjLqxx.getZlnd().toString());
            }
            bdcLq.setXdm(djsjLqxx.getXdm());
            bdcLq.setLb(djsjLqxx.getLb());
            bdcLq.setXb(djsjLqxx.getXb());
            String gyqk = djsjLqxx.getGyqk();
            if (StringUtils.isNotBlank(gyqk)) {
                if (!NumberUtils.isNumber(gyqk)) {
                    gyqk = changeGyqkMcToDm(gyqk);
                }
                bdcLq.setGyqk(gyqk);
            }
            String isg = null;
            if (StringUtils.isNotBlank(djsjLqxx.getBdcdyh()) && StringUtils.length(djsjLqxx.getBdcdyh()) > 13) {
                isg = StringUtils.substring(djsjLqxx.getBdcdyh(), 12, 13);
            }
            if (StringUtils.equals(isg, "G")) {
                bdcLq.setLdsyqxz("国家所有");
            } else {
                bdcLq.setLdsyqxz("集体所有");
            }
        }

        return bdcLq;
    }

    public String changeGyqkMcToDm(String gyqk) {
        if (StringUtils.equals(gyqk, Constants.GYFS_DDGY_MC)) {
            gyqk = Constants.GYFS_DDGY_DM;
        }
        if (StringUtils.equals(gyqk, Constants.GYFS_GTGY_MC)) {
            gyqk = Constants.GYFS_GTGY_DM;
        }
        if (StringUtils.equals(gyqk, Constants.GYFS_AFGY_MC)) {
            gyqk = Constants.GYFS_AFGY_DM;
        }
        if (StringUtils.equals(gyqk, Constants.GYFS_QTGY_MC)) {
            gyqk = Constants.GYFS_QTGY_DM;
        }
        return gyqk;
    }

    /*sc:土地承包经营权*/
    public BdcTdcbnydsyq getBdcTdcbnydsyqFromTdcb(DjsjCbzdDcb cbzdDcb, BdcTdcbnydsyq bdcTdcbnydsyq, List<DjsjCbzdCbf> djsjCbzdCbfList, List<DjsjCbzdFbf> djsjCbzdFbfList) {
        if (bdcTdcbnydsyq == null) {
            bdcTdcbnydsyq = new BdcTdcbnydsyq();
        }
        if (cbzdDcb != null) {
            List<DjsjNydDcb> djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(cbzdDcb.getDjh());
            DjsjNydDcb djsjNydDcb = new DjsjNydDcb();
            if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                djsjNydDcb = djsjNydDcbList.get(0);
            }
            if (CollectionUtils.isNotEmpty(djsjCbzdFbfList)) {
                bdcTdcbnydsyq.setFbfmc(djsjCbzdFbfList.get(0).getFbfmc());
            }
            if (CollectionUtils.isNotEmpty(djsjCbzdCbfList)) {
                bdcTdcbnydsyq.setQdcbfs(djsjCbzdCbfList.get(0).getQdcbfs());
            }

            if (djsjNydDcb.getFzmj() != null && djsjNydDcb.getFzmj() != 0) {
                bdcTdcbnydsyq.setSyqmj(djsjNydDcb.getFzmj());
            } else if (djsjNydDcb.getScmj() != null && djsjNydDcb.getScmj() != 0) {
                bdcTdcbnydsyq.setSyqmj(djsjNydDcb.getScmj());
            }
            bdcTdcbnydsyq.setTdsyqxz(djsjNydDcb.getQsxz());
            bdcTdcbnydsyq.setSyttlx(cbzdDcb.getSyttlx());
            bdcTdcbnydsyq.setYzyfs(cbzdDcb.getYzyfs());
            bdcTdcbnydsyq.setCyzl(cbzdDcb.getCdysz());
            bdcTdcbnydsyq.setSyzcl(cbzdDcb.getSyzxl());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            List<DjsjCbzdJtcy> djsjCbzdTtcyList = djSjMapper.getDjsjCbzdTtcyByProid(bdcTdcbnydsyq.getProid());
            StringBuilder msg = new StringBuilder();
            if (CollectionUtils.isNotEmpty(djsjCbzdTtcyList)) {
                for (int j = 0; j < djsjCbzdTtcyList.size(); j++) {
                    DjsjCbzdJtcy djsjCbzdTtcy = djsjCbzdTtcyList.get(j);
                    String magTemp = "";
                    int age = 0;
                    String sex = "";
                    if (StringUtils.isNotBlank(djsjCbzdTtcy.getSfzhm())) {
                        int csn = Integer.parseInt(StringUtils.substring(djsjCbzdTtcy.getSfzhm(), 6, 10));
                        age = Integer.parseInt(sdf.format(new Date())) - csn;
                        int sexInt = Integer.parseInt(StringUtils.substring(djsjCbzdTtcy.getSfzhm(), 16, 17));
                        if (sexInt % 2 == 0) {
                            sex = "女";
                        } else {
                            sex = "男";
                        }
                    }
                    if (StringUtils.isNotBlank(djsjCbzdTtcy.getXm())) {
                        magTemp += djsjCbzdTtcy.getXm() + " ";
                    }
                    String xxxx = "";
                    if (StringUtils.isNotBlank(sex)) {
                        xxxx += sex;
                    }
                    if (age != 0) {
                        xxxx += "," + age + "岁";
                    }
                    if (StringUtils.isNotBlank(djsjCbzdTtcy.getGx())) {
                        xxxx += "," + djsjCbzdTtcy.getGx();
                    }
                    if (StringUtils.isNotBlank(xxxx)) {
                        magTemp += "(" + xxxx + ")";
                    }
                    if (StringUtils.isNotBlank(magTemp)) {
                        if (j != djsjCbzdTtcyList.size() - 1) {
                            msg.append(magTemp).append("\n");
                        } else {
                            msg.append(magTemp);
                        }
                    }
                }

            }
            bdcTdcbnydsyq.setGyqk(msg.toString());
        }
        return bdcTdcbnydsyq;
    }

    @Override
    public void updateQlxxByDjsj(ProjectPar projectPar) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(projectPar.getProid());
        if (bdcXm != null) {
            projectPar.setBdcXm(bdcXm);
        }
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(projectPar.getProid());
        if (bdcBdcdy != null) {
            projectPar.setBdcBdcdy(bdcBdcdy);
            projectPar.setBdcdyid(bdcBdcdy.getBdcdyid());
        }
        QllxVo qllxVo = makeSureQllx(projectPar);
        BdcQlDqService bdcQlDqService = InterfaceCodeBeanFactory.getBean(bdcQlDqServiceList, getTableName(qllxVo));
        //注销登记不生成权利
        if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, projectPar.getSqlx())) {
            bdcQlDqService = bdcNoQlDqServiceImpl;
        }
        if (bdcQlDqService != null) {
            delQllxByproid(qllxVo, projectPar.getProid());
            List<QllxVo> qllxVoList = Lists.newArrayList();
            qllxVoList = bdcQlDqService.readQlFormXm(projectPar, qllxVoList);
            qllxVoList = bdcQlDqService.readQlFormQj(projectPar, qllxVoList);
            qllxVo = qllxVoList.get(0);
            saveQllxVo(qllxVo);
        }
    }

    @Override
    public List<QllxParent> queryQllxByProid(String proid) {
        List<QllxParent> qllxParentList = null;
        if (StringUtils.isNotBlank(proid)) {
            qllxParentList = bdcQllxMapper.queryQllxByProid(proid);
        }

        return qllxParentList;
    }

    @Override
    public void delQllxByproid(QllxVo qllxVo, String proid) {
        if (StringUtils.isNotBlank(proid) && qllxVo != null) {
            /**
             * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
             * @description 房地产权多幢删除时，同时删除分幢信息
             */
            if (qllxVo instanceof BdcFdcqDz) {
                bdcFwfzxxService.deleteProjectBdcFwfzxx(proid);
            }
            Example qllx = new Example(qllxVo.getClass());
            qllx.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            entityMapper.deleteByExample(qllxVo.getClass(), qllx);
        }
    }

    @Override
    public void changeQllxZt(String proid, Integer qszt) {
        if (StringUtils.isNotBlank(proid)) {
            String[] proids = proid.split(",");
            for (String yproid : proids) {
                if (StringUtils.isNotBlank(yproid)) {
                    BdcXm bdcxm = null;
                    if (StringUtils.isNotBlank(yproid)) {
                        bdcxm = bdcXmService.getBdcXmByProid(proid);
                    }
                    if (bdcxm != null) {
                        changeQllxZt(bdcxm, qszt);
                    }
                }
            }

        }
    }

    @Override
    public void changeQllxZt(BdcXm bdcxm, Integer qszt) {
        if (bdcxm != null) {
            QllxVo qllxVo = makeSureQllx(bdcxm);
            if (qllxVo != null) {
                Example qllx = new Example(qllxVo.getClass());
                qllx.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid());

                List<QllxVo> qllxVoList = (List<QllxVo>) entityMapper.selectByExample(qllxVo.getClass(), qllx);
                if (CollectionUtils.isNotEmpty(qllxVoList)) {
                    for (QllxVo yqllxVo : qllxVoList) {
                        if (yqllxVo != null) {
                            //zhouwanqing 过度解封生成的权利没有相关解封信息，所以在这添上
                            if (yqllxVo instanceof BdcCf && !StringUtils.equals(bdcxm.getXmly(), Constants.XMLY_BDC) && !CommonUtil.indexOfStrs(Constants.CF_SQLX, bdcxm.getSqlx())) {
                                BdcCf bdcCf = (BdcCf) yqllxVo;
                                bdcCf.setJfdjsj(new Date());
                                /**
                                 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
                                 * @description 过渡创建的续封做解封, 解封业务号不再赋值
                                 */
                                if (StringUtils.isBlank(bdcCf.getJfywh())) {
                                    bdcCf.setJfywh(bdcxm.getBh());
                                }
                                yqllxVo = bdcCf;
                            }
                            yqllxVo.setQszt(qszt);
                            //zdd 根据权利状态  处理权利开始以及结束时间属性
                            if (Constants.QLLX_QSZT_XS.equals(qszt)) {
                                yqllxVo.setQlqssj(CalendarUtil.getCurHMSDate());
                                if (StringUtils.contains(yqllxVo.getFj(), "已注销") || StringUtils.contains(yqllxVo.getFj(), "\n已注销")) {
                                    yqllxVo.setFj(StringUtils.remove(yqllxVo.getFj(), "\n已注销"));
                                    yqllxVo.setFj(StringUtils.remove(yqllxVo.getFj(), "已注销"));
                                }
                            } else if (Constants.QLLX_QSZT_HR.equals(qszt)) {
                                yqllxVo.setQljssj(CalendarUtil.getCurDate());
                                //原权利注销后登记簿附记记录是否注销,如果没有已注销则增加已注销
                                if (!StringUtils.contains(yqllxVo.getFj(), "已注销")) {
                                    if (StringUtils.isNotBlank(yqllxVo.getFj())) {
                                        yqllxVo.setFj(yqllxVo.getFj() + "\n已注销");
                                    } else {
                                        yqllxVo.setFj("已注销");
                                    }
                                }
                            }
                            entityMapper.updateByPrimaryKeySelective(yqllxVo);
                        }
                    }
                }
            }
        }

    }


    @Override
    public void changeQllxZt(BdcXm bdcxm, Integer qszt, Boolean ignoreHr) {
        if (bdcxm != null) {
            QllxVo qllxVo = makeSureQllx(bdcxm);
            if (qllxVo != null) {
                Example qllx = new Example(qllxVo.getClass());

                /**
                 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                 * @description 过滤掉历史状态的权利。如果ignoreHr为true,则需要将qszt为历史的过滤掉，主要针对【批量抵押变更做部分注销】
                 * 当前权利的权属状态操作要过滤历史状态的，针对原权利权属状态的不需要过滤
                 */
                if (ignoreHr) {
                    qllx.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid()).andNotEqualTo("qszt", Constants.QLLX_QSZT_HR);
                } else {
                    qllx.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid());
                }

                List<QllxVo> qllxVoList = (List<QllxVo>) entityMapper.selectByExample(qllxVo.getClass(), qllx);
                if (CollectionUtils.isNotEmpty(qllxVoList)) {
                    for (QllxVo yqllxVo : qllxVoList) {
                        if (yqllxVo != null) {
                            //zhouwanqing 过度解封生成的权利没有相关解封信息，所以在这添上
                            if (yqllxVo instanceof BdcCf && !StringUtils.equals(bdcxm.getXmly(), Constants.XMLY_BDC) && !CommonUtil.indexOfStrs(Constants.CF_SQLX, bdcxm.getSqlx())) {
                                BdcCf bdcCf = (BdcCf) yqllxVo;
                                bdcCf.setJfdjsj(new Date());
                                /**
                                 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
                                 * @description 过渡创建的续封做解封, 解封业务号不再赋值
                                 */
                                if (StringUtils.isBlank(bdcCf.getJfywh())) {
                                    bdcCf.setJfywh(bdcxm.getBh());
                                }
                                yqllxVo = bdcCf;
                                gdCfService.updateGdCfInfo(bdcxm, bdcCf);
                            }
                            yqllxVo.setQszt(qszt);
                            //zdd 根据权利状态  处理权利开始以及结束时间属性
                            if (qszt == Constants.QLLX_QSZT_XS) {
                                yqllxVo.setQlqssj(CalendarUtil.getCurHMSDate());
                            } else if (qszt == Constants.QLLX_QSZT_HR) {
                                yqllxVo.setQljssj(CalendarUtil.getCurDate());
                            }
                            String fj = yqllxVo.getFj();
                            StringBuilder fjBuffer = new StringBuilder();
                            if (StringUtils.isNotBlank(fj)) {
                                fjBuffer.append(fj);
                                fjBuffer.append("\n");
                            }
                            if (!StringUtils.contains(fj, "已注销") && yqllxVo instanceof BdcCf && qszt == Constants.QLLX_QSZT_HR) {
                                fjBuffer.append("已注销");
                                yqllxVo.setFj(fjBuffer.toString());
                            }
                            entityMapper.updateByPrimaryKeySelective(yqllxVo);
                        }
                    }
                }
            }
        }

    }

    @Override
    public String makeSureBdcqzlx(QllxVo qllxVo) {
        String zsFont = "";
        if (qllxVo instanceof BdcCf) {
            zsFont = Constants.BDCQZM_BH_FONT;
        } else if (qllxVo instanceof BdcDyaq) {
            zsFont = Constants.BDCQZM_BH_FONT;
        } else if (qllxVo instanceof BdcDyq) {
            zsFont = Constants.BDCQZM_BH_FONT;
        } else if (qllxVo instanceof BdcFdcq) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else if (qllxVo instanceof BdcFdcqDz) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else if (qllxVo instanceof BdcHysyq) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else if (qllxVo instanceof BdcJsydzjdsyq) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else if (qllxVo instanceof BdcJzwgy) {
            zsFont = Constants.BDCQZM_BH_FONT;
        } else if (qllxVo instanceof BdcJzwsyq) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else if (qllxVo instanceof BdcLq) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else if (qllxVo instanceof BdcQsq) {
            zsFont = Constants.BDCQZM_BH_FONT;
        } else if (qllxVo instanceof BdcTdcbnydsyq) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else if (qllxVo instanceof BdcTdsyq) {
            zsFont = Constants.BDCQZS_BH_FONT;
        } else {
            zsFont = Constants.BDCQZM_BH_FONT;
        }
        return zsFont;
    }

    @Override
    public QllxVo queryQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = makeSureQllx(bdcXm);
        if (qllxVo != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            if (qllxVo instanceof BdcFdcqDz) {
                qllxVo = queryQllxVo(qllxVo, bdcXm.getProid());
                if (qllxVo == null) {
                    qllxVo = new BdcFdcq();
                    qllxVo = queryQllxVo(qllxVo, bdcXm.getProid());
                }
            } else {
                qllxVo = queryQllxVo(qllxVo, bdcXm.getProid());
            }
        }
        return qllxVo;
    }

    @Override
    public QllxVo queryQllxVo(QllxVo qllxVo, String proid) {
        QllxVo returnQllx = null;
        if (qllxVo != null && StringUtils.isNotBlank(proid)) {
            Example qllxTemp = new Example(qllxVo.getClass());
            qllxTemp.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            List<QllxVo> qllxVoList = (List<QllxVo>) entityMapper.selectByExample(qllxVo.getClass(), qllxTemp);
            if (CollectionUtils.isNotEmpty(qllxVoList)) {
                returnQllx = qllxVoList.get(0);
            }
            //hqz 判断当前是否为房屋多幢，并把项目取出
            if (qllxVo instanceof BdcFdcqDz) {
                String qlid = qllxVo.getQlid();
                if (StringUtils.isNotBlank(qlid)) {
                    List<BdcFwfzxx> fwfzxxs = bdcFdcqDzMapper.queryBdcFwfzxxlstByQlid(qlid);
                    ((BdcFdcqDz) qllxVo).setFwfzxxList(fwfzxxs);
                }
            }
        }
        return returnQllx;
    }

    @Override
    public void endQllxZt(BdcXm bdcXm) {
        if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_ZJJZW_ZX_DM, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_ZJJZW_ZX_FW_DM, bdcXm.getSqlx())) {
            changeQllxZt(bdcXm, Constants.QLLX_QSZT_HR, true);
        } else {
            changeQllxZt(bdcXm, Constants.QLLX_QSZT_XS, true);
        }
    }

    @Override
    public void batchEndQllxZt(List<BdcXm> bdcXmList) {
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            BdcXm bdcXm = bdcXmList.get(0);
            QllxVo qllxVo = makeSureQllx(bdcXm);
            if (qllxVo instanceof BdcFdcq) {
                bdcFdcqService.batchChangeQllxZt(bdcXmList, Constants.QLLX_QSZT_XS, new Date());
            }
        }

    }

    @Override
    public List<QllxVo> andEqualQueryQllx(QllxVo qllxVo, Map<String, Object> map) {
        List<QllxVo> list = null;
        if (qllxVo != null) {
            Example qllxTemp = new Example(qllxVo.getClass());
            if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
                Iterator iter = map.entrySet().iterator();
                Example.Criteria criteria = qllxTemp.createCriteria();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    if (val != null) {
                        criteria.andEqualTo(key.toString(), val);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria())) {
                list = (List<QllxVo>) entityMapper.selectByExample(qllxVo.getClass(), qllxTemp);
            }
        }
        return list;
    }

    @Override
    public List<QllxVo> andLikeQueryQllx(QllxVo qllxVo, Map<String, String> map) {
        List<QllxVo> list = null;
        if (qllxVo != null) {
            Example qllxTemp = new Example(qllxVo.getClass());
            if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
                Iterator iter = map.entrySet().iterator();
                Example.Criteria criteria = qllxTemp.createCriteria();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    if (val != null && StringUtils.isNotBlank(val.toString())) {
                        criteria.andLike(key.toString(), val.toString());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria())) {
                list = (List<QllxVo>) entityMapper.selectByExample(qllxVo.getClass(), qllxTemp);
            }
        }
        return list;
    }

    @Override
    public QllxVo getQllxVoByProid(String proid) {
        //判断是否是查封
        Example example = new Example(BdcCf.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<?> list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcCf();
        }
        //判断是否是抵押
        example = new Example(BdcDyaq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcDyaq();
        }
        //判断是否是地役权
        example = new Example(BdcDyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcDyq();
        }
        //判断是否是房地产权
        example = new Example(BdcFdcq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcFdcq();
        }
        //判断是否是房地产权 多撞
        BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(proid);
        if (bdcFdcqDz != null) {
            return new BdcFdcqDz();
        }
        //判断是否是海域（含无居民海岛） 使用权
        example = new Example(BdcHysyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcHysyq();
        }
        //判断是否是建设用地使用权、宅基地使用权
        example = new Example(BdcJsydzjdsyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcJsydzjdsyq();
        }
        //判断是否是建筑物区分所有权
        example = new Example(BdcJzwgy.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcJzwgy();
        }
        //判断是否是构（建）筑物所有权
        example = new Example(BdcJzwsyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcJzwsyq();
        }
        //判断是否是林权
        example = new Example(BdcLq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcLq();
        }
        //判断是否是其他相关权利
        example = new Example(BdcQsq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcQsq();
        }
        //判断是否是土地承包经营权、农用地使用权
        example = new Example(BdcTdcbnydsyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcTdcbnydsyq();
        }
        //判断是否是土地所有权
        example = new Example(BdcTdsyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcTdsyq();
        }
        //判断是否是预告
        example = new Example(BdcYg.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcYg();
        }
        //判断是否是异议
        example = new Example(BdcYy.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return new BdcYy();
        }
        return null;
    }

    @Override
    public String queryDjsyByQllx(String qllx) {
        String djsy = "";
        List<String> list = bdcQllxMapper.queryDjsyByQllx(qllx);
        if (CollectionUtils.isNotEmpty(list)) {
            djsy = list.get(0);
        }
        return djsy;
    }

    @Override
    public QllxVo updateDbr(QllxVo qllxVo, String userId, Date dbsj) {
        if (qllxVo != null && StringUtils.isNotBlank(userId)) {
            PfUserVo pfUserVo = sysUserService.getUserVo(userId);
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(qllxVo.getProid());
            if (pfUserVo != null) {
                if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_FWSP) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_JF)) {
                    qllxVo.setDbr(null);
                    qllxVo.setDjsj(null);
                    if (qllxVo instanceof BdcCf) {
                        BdcCf bdcCf = (BdcCf) qllxVo;
                        bdcCf.setJfdbr(pfUserVo.getUserName());
                        bdcCf.setJfdjsj(dbsj);
                        entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                    }
                } else {
                    qllxVo.setDbr(pfUserVo.getUserName());
                    if (qllxVo instanceof BdcCf) {
                        BdcCf bdcCf = (BdcCf) qllxVo;
                        bdcCf.setDjsj(new Date());
                    } else if (qllxVo instanceof BdcDyaq) {
                        /**
                         * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
                         * @description 针对合并登记转移登簿时间比抵押登簿时间快1秒（一般抵押也加1秒不影响）
                         */
                        qllxVo.setDjsj(DateUtils.addSeconds(dbsj, 1));
                    } else {
                        qllxVo.setDjsj(dbsj);
                    }
                    //qijiadong 抵押转移、抵押更正、抵押变更登记同步原抵押信息中的注销信息
                    if (bdcXm != null && CommonUtil.indexOfStrs(Constants.SQLX_DY_ZYGZBG, bdcXm.getSqlx()) && qllxVo instanceof BdcDyaq) {
                        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                                    BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(bdcXmRel.getYproid());
                                    if (bdcDyaq != null) {
                                        bdcDyaq.setZxsj(qllxVo.getDjsj());
                                        bdcDyaq.setZxdbr(qllxVo.getDbr());
                                        bdcDyaq.setZxdyywh(((BdcDyaq) qllxVo).getYwh());
                                        bdcDyaq.setZxdyyy(bdcZdGlService.getSqlxMcByDm(bdcXm.getSqlx()));
                                        bdcDyaqService.saveBdcDyaq(bdcDyaq);
                                    } else if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                                        GdDy gdDy = gdDyService.getGdDyByDyDyid(bdcXmRel.getYqlid());
                                        if (gdDy != null) {
                                            gdDy.setZxdbsj(qllxVo.getDjsj());
                                            gdDy.setZxdbr(qllxVo.getDbr());
                                            gdDy.setZxdyywh(((BdcDyaq) qllxVo).getYwh());
                                            gdDy.setZxdyyy(bdcZdGlService.getSqlxMcByDm(bdcXm.getSqlx()));
                                            entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //同时更新登记簿
                bdcdjbService.updateDjb(qllxVo.getProid(), pfUserVo.getUserName(), qllxVo);
            }
        }
        return qllxVo;
    }

    @Override
    public void updateGdDbr(BdcXm bdcXm, String userId, String defaultUserId, String dbrRead) {
        if (bdcXm != null && StringUtils.isNotBlank(userId)) {
            PfUserVo pfUserVo = sysUserService.getUserVo(userId);
            if (pfUserVo != null && StringUtils.isNotEmpty(bdcXm.getProid())) {
                List<GdCf> gdCfList = gdCfService.getGdCfListByAnyGdProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(gdCfList)) {
                    for (GdCf gdCf : gdCfList) {
                        if (StringUtils.isNotEmpty(gdCf.getProid()) && StringUtils.equals(gdCf.getProid(), bdcXm.getProid())) {
                            gdCf.setDbr(pfUserVo.getUserName());
                            gdCf.setDjsj(new Date(System.currentTimeMillis()));
                            entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public QllxVo updateZxDbr(QllxVo yqllxVo, String userId, String defaultUserId, String signKey, String proid) {
        String getUserId = "";
        Date getUserDate = null;
        if (yqllxVo != null && StringUtils.isNotBlank(signKey)) {
            List<PfSignVo> pfSignVoList = sysSignService.getSignList(signKey, proid);
            if (CollectionUtils.isNotEmpty(pfSignVoList)) {
                getUserId = pfSignVoList.get(0).getUserId();
                getUserDate = pfSignVoList.get(0).getSignDate();
            }
        } else if (yqllxVo != null && StringUtils.isNotBlank(defaultUserId)) {
            getUserId = defaultUserId;
            getUserDate = new Date();
        } else if (yqllxVo != null && StringUtils.isNotBlank(userId)) {
            getUserId = userId;
            getUserDate = new Date();
        }
        if (yqllxVo != null && StringUtils.isNotBlank(getUserId)) {
            PfUserVo pfUserVo = sysUserService.getUserVo(getUserId);
            if (pfUserVo != null) {
                if (yqllxVo instanceof BdcDyaq) {
                    BdcDyaq bdcDyaq = (BdcDyaq) yqllxVo;
                    bdcDyaq.setZxdbr(pfUserVo.getUserName());
                    bdcDyaq.setZxsj(getUserDate);
                    yqllxVo = initZxXmxx(bdcDyaq, proid);
                } else if (yqllxVo instanceof BdcCf) {
                    BdcCf bdcCf = (BdcCf) yqllxVo;
                    if (StringUtils.equals(bdcCf.getCflx(), Constants.CFLX_XF)) {
                        //解封最新的续封时，解封该链的一系列查封
                        bdcCfService.dealXfjf(bdcCf, proid, pfUserVo.getUserName(), getUserDate);
                    }
                    bdcCf.setJfdbr(pfUserVo.getUserName());
                    bdcCf.setJfdjsj(getUserDate);
                    yqllxVo = bdcCf;
                } else if (yqllxVo instanceof BdcYy) {
                    BdcYy bdcYy = (BdcYy) yqllxVo;
                    bdcYy.setZxdbr(pfUserVo.getUserName());
                    bdcYy.setZxsj(getUserDate);
                    yqllxVo = bdcYy;
                }
            }
        }
        return yqllxVo;
    }

    @Override
    public void updateGdZxDbr(BdcXm bdcXm, String userId, String defaultUserId, String signKey, String proid) {
        String getUserId = "";
        if (StringUtils.isNotEmpty(proid) && StringUtils.isNotBlank(signKey)) {
            List<PfSignVo> pfSignVoList = sysSignService.getSignList(signKey, proid);
            if (CollectionUtils.isNotEmpty(pfSignVoList)) {
                getUserId = pfSignVoList.get(0).getUserId();
            }
        } else if (StringUtils.isNotEmpty(proid) && StringUtils.isNotBlank(defaultUserId)) {
            getUserId = defaultUserId;
        } else if (StringUtils.isNotEmpty(proid) && StringUtils.isNotBlank(userId)) {
            getUserId = userId;
        }
        if (StringUtils.isNotBlank(getUserId)) {
            PfUserVo pfUserVo = sysUserService.getUserVo(getUserId);
            if (pfUserVo != null) {
                List<BdcXmRel> bdcXmRelList = null;
                String zxdjywh = "";
                if (bdcXm != null) {
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (StringUtils.isNotBlank(bdcXm.getBh())) {
                        zxdjywh = bdcXm.getBh();
                    }
                }
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        String qlid = bdcXmRel.getYqlid();
                        if (StringUtils.isNotBlank(qlid)) {
                            GdDy gddy = entityMapper.selectByPrimaryKey(GdDy.class, qlid);
                            GdCf gdcf = entityMapper.selectByPrimaryKey(GdCf.class, qlid);
                            GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, qlid);
                            GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, qlid);
                            GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, qlid);
                            //存在一证多房匹配土地证情况
                            List<String> tdQlids = gdTdService.getGdTdQlidsByFwQlid(qlid);
                            if (CollectionUtils.isNotEmpty(tdQlids)) {
                                for (String tdQlid : tdQlids) {
                                    if (StringUtils.isNotBlank(tdQlid)) {
                                        GdDy tdGddy = entityMapper.selectByPrimaryKey(GdDy.class, tdQlid);
                                        GdCf tdGdcf = entityMapper.selectByPrimaryKey(GdCf.class, tdQlid);
                                        GdTdsyq tdGdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, tdQlid);
                                        if (tdGddy != null) {
                                            tdGddy.setZxr(pfUserVo.getUserName());
                                            tdGddy.setZxrq(new Date(System.currentTimeMillis()));
                                            entityMapper.saveOrUpdate(tdGddy, tdGddy.getDyid());
                                        } else if (tdGdcf != null) {
                                            tdGdcf.setJfr(pfUserVo.getUserName());
                                            tdGdcf.setJfrq(new Date(System.currentTimeMillis()));
                                            entityMapper.saveOrUpdate(tdGdcf, tdGdcf.getCfid());
                                        } else if (tdGdTdsyq != null) {
                                            entityMapper.saveOrUpdate(tdGdTdsyq, tdGdTdsyq.getQlid());
                                        }
                                        gdFwService.changeGdqlztByQlid(tdQlid, Constants.QLLX_QSZT_XS.toString());
                                    }
                                }
                            }
                            if (gddy != null) {
                                gddy.setFj("已注销");
                                gddy.setZxdyywh(zxdjywh);
                                gddy.setZxr(pfUserVo.getUserName());
                                gddy.setZxdbr(pfUserVo.getUserName());
                                gddy.setZxrq(new Date(System.currentTimeMillis()));
                                gddy.setZxdbsj(new Date(System.currentTimeMillis()));
                                entityMapper.saveOrUpdate(gddy, gddy.getDyid());
                            } else if (gdcf != null) {
                                gdcf.setJfdbr(pfUserVo.getUserName());
                                gdcf.setJfr(pfUserVo.getUserName());
                                gdcf.setJfywh(zxdjywh);
                                gdcf.setJfrq(new Date(System.currentTimeMillis()));
                                gdcf.setJfdbsj(new Date(System.currentTimeMillis()));
                                entityMapper.saveOrUpdate(gdcf, gdcf.getCfid());
                            }
                            gdFwService.changeGdqlztByQlid(qlid, Constants.QLLX_QSZT_XS.toString());
                        }
                        //司法处置解封（解封过渡）对解封登簿人和解封登记时间进行赋值
                        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid()) && StringUtils.equals(Constants.SQLX_SFCD, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_SFCD_PL, bdcXm.getSqlx())) {
                            // 匹配不动产单元的不动产查封
                            List<BdcCf> bdcCfListTemp = bdcCfService.getCfByBdcdyid(bdcXm.getBdcdyid());
                            if (CollectionUtils.isNotEmpty(bdcCfListTemp)) {
                                for (BdcCf bdcCfTemp : bdcCfListTemp) {
                                    if (bdcCfTemp != null && bdcCfTemp.getQszt() == Constants.QLLX_QSZT_XS) {
                                        bdcCfTemp.setJfsj(new Date(System.currentTimeMillis()));
                                        bdcCfTemp.setJfdbr(pfUserVo.getUserName());
                                        bdcCfTemp.setJfdjsj(new Date(System.currentTimeMillis()));
                                        entityMapper.saveOrUpdate(bdcCfTemp, bdcCfTemp.getQlid());
                                    }
                                }
                            }
                            // 匹配不动产单元的过渡查封
                            List<GdCf> gdCfList = gdXmService.getGdCfListByBdcdyid(bdcXm.getBdcdyid());
                            if (CollectionUtils.isNotEmpty(gdCfList)) {
                                for (GdCf gdCf : gdCfList) {
                                    if (gdCf != null && StringUtils.isNotBlank(gdCf.getCfid())) {
                                        gdCf.setJfdbsj(new Date(System.currentTimeMillis()));
                                        gdCf.setJfdbr(pfUserVo.getUserName());
                                        gdCf.setJfr(pfUserVo.getUserName());
                                        gdCf.setJfrq(new Date(System.currentTimeMillis()));
                                        entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                                    }
                                }
                            }
                        }
                        // 不匹配不动产单元
                        if (bdcXm != null && StringUtils.equals(Constants.SQLX_SFCD, bdcXm.getSqlx()) && StringUtils.isNotBlank(bdcXm.getProid())) {
                            List<String> qlidList = bdcXmRelService.getAllGdCfQlidListByBdcXmid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(qlidList)) {
                                for (String qlidTemp : qlidList) {
                                    GdCf gdCf = gdFwService.getGdCfByCfid(qlidTemp, 0);
                                    if (gdCf != null && StringUtils.isNotBlank(gdCf.getCfid())) {
                                        gdCf.setJfdbsj(new Date(System.currentTimeMillis()));
                                        gdCf.setJfdbr(pfUserVo.getUserName());
                                        gdCf.setJfr(pfUserVo.getUserName());
                                        gdCf.setJfrq(new Date(System.currentTimeMillis()));
                                        entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
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
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 设置注销项目信息
     */
    private BdcDyaq initZxXmxx(BdcDyaq bdcDyaq, String proid) {
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcDyaq.setZxdyyy(bdcXm.getDjyy());
            bdcDyaq.setZxdyywh(bdcXm.getBh());
        }
        return bdcDyaq;
    }

    @Override
    public List<Map> getQllxListByPage(HashMap map) {
        return bdcQllxMapper.getQllxListByPage(map);
    }

    @Override
    public QllxVo gyqkDefutl(QllxVo qllxVo) {
        List<String> gyfs = bdcQlrService.getGyfsByProid(qllxVo.getProid());
        if (CollectionUtils.isNotEmpty(gyfs) && StringUtils.isNotBlank(gyfs.get(0))) {
            qllxVo.setGyqk(gyfs.get(0));
        } else {
            qllxVo.setGyqk(Constants.GYQK_DDGY);
        }
        return qllxVo;
    }

    public List<Map> getCfxxByBdcdyId(String bdcdyid) {
        List<Map> cfList = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);

            cfList = bdcCfService.queryBdcCfByPage(map);
        }
        return cfList;
    }

    public List<BdcDyaq> getDyxxByBdcdyId(String bdcdyid) {
        List<BdcDyaq> dyaqList = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            dyaqList = bdcDyaqService.queryBdcDyaq(map);
        }
        return dyaqList;
    }

    public List<BdcFdcq> getFdcqByBdcdyId(String bdcdyid) {
        List<BdcFdcq> fdcq = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            fdcq = bdcFdcqService.getBdcFdcq(map);
        }
        return fdcq;
    }

    public BdcFdcqDz getFdcqDzByBdcdyId(String bdcdyid) {
        BdcFdcqDz fdcq = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            fdcq = bdcFdcqDzService.getBdcFdcqDzByBdcdyid(bdcdyid);
        }
        return fdcq;
    }

    public List<BdcJsydzjdsyq> getJsydzjdsyqByBdcdyId(String bdcdyid) {
        List<BdcJsydzjdsyq> bdcJsydzjdsyq = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            bdcJsydzjdsyq = bdcJsydzjdsyqService.getBdcJsydzjdsyqList(map);
        }
        return bdcJsydzjdsyq;
    }


    public List<BdcYy> getYyxxByBdcdyId(String bdcdyid) {
        List<BdcYy> yyList = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            yyList = bdcYyService.queryBdcYy(map);
        }
        return yyList;
    }

    public List<BdcYg> getYgxxByBdcdyId(String bdcdyid) {
        List<BdcYg> ygList = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            ygList = bdcYgService.getBdcYgList(map);
        }
        return ygList;
    }

    @Override
    public List<Map> getCfxxByProid(List<BdcXmRel> bdcXmRelList, String bdcdyh) {
        List<Map> cfList = null;
        HashMap hashMap = new HashMap();
        List<String> bdcdyList = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            cfList = new ArrayList<Map>();
            StringBuilder proids = new StringBuilder();
            for (int i = 0; i < bdcXmRelList.size(); i++) {
                if (StringUtils.indexOf(proids, bdcXmRelList.get(i).getProid()) < 0) {
                    if (StringUtils.isBlank(proids)) {
                        proids.append(bdcXmRelList.get(i).getProid());
                    } else {
                        proids.append(",").append(bdcXmRelList.get(i).getProid());
                    }
                }
            }
            if (StringUtils.isNotBlank(proids)) {
                HashMap map = new HashMap();
                map.put("proids", proids.toString().split(","));
                map.put("bdcdyh", bdcdyh);
                map.put("qszt", Constants.QLLX_QSZT_XS);
                map.put("isjf", "0");
                cfList = bdcCfService.queryBdcCfByPage(map);
                map.put("qszt", Constants.QLLX_QSZT_LS);
                cfList.addAll(bdcCfService.queryBdcCfByPage(map));

                //zhouwanqing 若有proids且未查询到了查封信息，则必需验证是否有预查封
                if (StringUtils.isNotBlank(proids) && CollectionUtils.isEmpty(cfList)) {
                    if (StringUtils.isNotBlank(bdcdyh)) {
                        bdcdyList.add(bdcdyh);
                    } else {
                        String[] proidArray = proids.toString().split(",");
                        for (int i = 0; i != proidArray.length; i++) {
                            BdcBdcdy bdcdy = bdcdyService.queryBdcBdcdyByProid(proidArray[i]);
                            if (bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
                                bdcdyList.add(bdcdy.getBdcdyh());
                            }
                        }
                    }
                    //zhouwanqing 查询现势预查封
                    if (CollectionUtils.isNotEmpty(bdcdyList)) {
                        for (String dyh : bdcdyList) {
                            List<BdcCf> ycfList = bdcCfService.queryYcfByBdcdyh(dyh);
                            if (CollectionUtils.isNotEmpty(ycfList)) {
                                hashMap.put("cf", ycfList.get(0));
                                cfList.add(hashMap);
                                break;
                            }
                        }
                    }
                }
            }
        }

        return cfList;
    }

    @Override
    public List<BdcDyaq> getDyaxxByProid(List<BdcXmRel> bdcXmRelList, String bdcdyh) {
        List<BdcDyaq> dyaqList = null;
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            dyaqList = new ArrayList<BdcDyaq>();
            StringBuilder proids = new StringBuilder();
            for (int i = 0; i < bdcXmRelList.size(); i++) {
                if (StringUtils.indexOf(proids, bdcXmRelList.get(i).getProid()) < 0) {
                    if (StringUtils.isBlank(proids)) {
                        proids.append(bdcXmRelList.get(i).getProid());
                    } else {
                        proids.append(",").append(bdcXmRelList.get(i).getProid());
                    }
                }
            }
            if (StringUtils.isNotBlank(proids)) {
                HashMap map = new HashMap();
                map.put("proids", proids.toString().split(","));
                map.put("bdcDyh", bdcdyh);
                map.put("qszt", Constants.QLLX_QSZT_XS);
                dyaqList = bdcDyaqService.queryBdcDyaq(map);
            }
        }

        return dyaqList;
    }

    public List<QllxVo> queryQllx(BdcXm bdcXm) {
        List<QllxVo> qllxVoList = new ArrayList<QllxVo>();
        HashMap map = new HashMap();
        QllxVo qllxVoTemp = makeSureQllx(bdcXm);
        map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcXm.getBdcdyid());
        }
        map.put("tableName", getTableName(qllxVoTemp));
        List<QllxParent> qllxParentList = bdcQllxMapper.queryQllx(map);
        if (CollectionUtils.isNotEmpty(qllxParentList)) {
            for (QllxParent qllxParent : qllxParentList) {
                QllxVo qlVo = entityMapper.selectByPrimaryKey(qllxVoTemp.getClass(), qllxParent.getQlid());
                //hqz 判断当前是否为房屋多幢，并把项目取出
                if (qlVo instanceof BdcFdcqDz) {
                    String qlid = qlVo.getQlid();
                    if (StringUtils.isNotBlank(qlid)) {
                        List<BdcFwfzxx> fwfzxxs = bdcFdcqDzMapper.queryBdcFwfzxxlstByQlid(qlid);
                        ((BdcFdcqDz) qlVo).setFwfzxxList(fwfzxxs);
                    }
                }
                qllxVoList.add(qlVo);
            }
        }
        return qllxVoList;
    }

    @Override
    public List<Map> getQlxxListByBdcdyh(String bdcdyh, String qszt) {
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(qszt)) {
            map.put("qszt", qszt);
        }
        map.put("bdcdyh", bdcdyh);

        return bdcQllxMapper.getBdcdyQlxxList(map);
    }

    /*海域使用权**/
    @Override
    public BdcHysyq getHysyqFromZhxx(BdcHysyq bdcHysyq, DjsjZhxx djsjZhxx) {
        if (bdcHysyq == null) {
            bdcHysyq = new BdcHysyq();
        }
        //循环分类信息表获取每种的金额相加作为使用总金额
        if (djsjZhxx != null) {
            if (djsjZhxx.getZhdm() != null) {
                List<DJsjZhjnbdyjlb> dJsjZhjnbdyjlbList = bdcDjsjService.getDJsjZhjnbdyjlb(djsjZhxx.getZhdm());
                Double syjse = 0.00;
                for (DJsjZhjnbdyjlb jlb : dJsjZhjnbdyjlbList) {
                    if (jlb.getSyjse() != null) {
                        syjse = syjse + jlb.getSyjse();
                    }
                }
                if (syjse > 0) {
                    bdcHysyq.setSyzje(syjse);
                }
            }
            bdcHysyq.setSyjjnqk(djsjZhxx.getSyjjnqk());
            bdcHysyq.setSyjyj(djsjZhxx.getSyjbzyj());
            if (djsjZhxx.getZhmj() != null) {
                bdcHysyq.setSyqmj(djsjZhxx.getZhmj());
            } else if (djsjZhxx.getYhzmj() != null) {
                bdcHysyq.setSyqmj(djsjZhxx.getYhzmj());
            }
        }
        return bdcHysyq;
    }

    @Override
    public QllxVo getQllxVoFromGdxm(String yqlid, QllxVo qllxVo, BdcXm bdcXm) {
        String sjppType = AppConfig.getProperty("sjpp.type");
        if (StringUtils.isNotBlank(yqlid) && qllxVo != null) {
            //zdd 代码后续完善   参照getQllxVoFromBdcXm方法中读取过渡数据的赋值逻辑
            if (qllxVo instanceof BdcJsydzjdsyq) {
                BdcJsydzjdsyq bdcJsydzjdsyq = (BdcJsydzjdsyq) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(yqlid);
                    if (CollectionUtils.isNotEmpty(gdTdList)) {
                        GdTd gdTd = gdTdList.get(0);
                        qllxVo = gdTdService.readBdcJsydzjdsyqFromGdTd(gdTd, bdcJsydzjdsyq);
                    }
                }
            } else if (qllxVo instanceof BdcFdcq) {
                BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                //zdd 如果存在房产信息  则以房产信息为主
                if (StringUtils.isNotBlank(yqlid)) {
                    GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(yqlid);
                    GdYg gdYg = gdFwService.getGdYgByYgid(yqlid, 0);
                    GdTdsyq gdTdsyq = null;
                    List<GdFw> gdFwList = gdFwService.getGdFwByQlid(yqlid);
                    List<GdTd> gdTdList = gdTdService.getGdTdListByFwQlid(yqlid);
                    List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
                    List<GdFw> gdZhFwList = new ArrayList<GdFw>();
                    List<GdTd> gdZhTdList = new ArrayList<GdTd>();
                    if (CollectionUtils.isNotEmpty(gdTdList)) {
                        for (GdTd gdTd : gdTdList) {
                            List<GdTdsyq> tdsyqList = gdTdService.queryTdsyqByTdid(gdTd.getTdid());
                            if (CollectionUtils.isNotEmpty(tdsyqList)) {
                                gdTdsyqList.addAll(tdsyqList);
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(gdFwList)) {
                        for (GdFw gdFw : gdFwList) {
                            gdZhFwList.add(bdcCheckCancelService.getGdFwFilterZdsj(gdFw));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(gdTdList)) {
                        for (GdTd gdTd : gdTdList) {
                            gdZhTdList.add(bdcCheckCancelService.getGdTdFilterZdsj(gdTd));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                        gdTdsyq = gdTdsyqList.get(0);
                    }

                    qllxVo = gdFwService.readBdcFdcqFromGdxx(gdZhFwList, gdZhTdList, bdcFdcq, gdFwsyq, gdTdsyq, bdcXm, gdYg);
                }
            } else if (qllxVo instanceof BdcCf) {
                BdcCf bdcCf = (BdcCf) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    GdCf gdCf = gdFwService.getGdCfByCfid(yqlid, null);
                    List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(yqlid, Constants.BDCLX_TDFW);
                    if (gdCf != null) {
                        String cflxdm = bdcZdGlService.getCflxDmByMc(gdCf.getCflx());
                        if (StringUtils.isNotBlank(cflxdm)) {
                            bdcCf.setCflx(cflxdm);
                        }
                        qllxVo = getBdcCfFromGdCf(bdcCf, gdCf, gdQlrList, sjppType, bdcXm);

                    } else if (StringUtils.equals(sjppType, Constants.PPLX_CG)) {
                        //zhouwanqing 成果匹配的若选择的土地证或房产证，则需判断这个房屋或土地是否存在查封
                        List<GdCf> gdCfList = gdCfService.getGdCfListByQlid(yqlid, 0);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            bdcCf.setCflx(Constants.CFLX_LHCF);
                            qllxVo = bdcCf;
                        }
                    }
                }
                //成果匹配也根据过渡查封类型确定不动产查封类型
                if (StringUtils.equals(Constants.DJZX_XF, bdcXm.getDjzx())) {
                    bdcCf.setCflx(Constants.CFLX_XF);
                } else if (StringUtils.equals(Constants.DJZX_LH, bdcXm.getDjzx())) {
                    bdcCf.setCflx(Constants.CFLX_LHCF);
                } else if (StringUtils.equals(Constants.DJZX_YCF, bdcXm.getDjzx())) {
                    bdcCf.setCflx(Constants.CFLX_ZD_YCF);
                }
            } else if (qllxVo instanceof BdcDyaq) {
                BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    GdDy gdDy = gdFwService.getGdDyByDyid(yqlid, null);
                    GdYg gdYg = gdFwService.getGdYgByYgid(yqlid, 0);
                    if (gdDy != null) {
                        gdDy = bdcCheckCancelService.getGdDyFilterZdsj(gdDy);
                    }
                    List<GdTd> gdTdList = gdTdService.getGdTdListByFwQlid(yqlid);
                    if (CollectionUtils.isNotEmpty(gdTdList)) {
                        List<GdTdsyq> tdsyqList = gdTdService.queryTdsyqByTdid(gdTdList.get(0).getTdid());
                        if (CollectionUtils.isNotEmpty(tdsyqList)) {
                            bdcDyaq.setFttdmj(tdsyqList.get(0).getFtmj());
                        }
                    }
                    qllxVo = gdFwService.readBdcDyaqFromGdDy(gdDy, bdcDyaq, gdYg);
                }
            } else if (qllxVo instanceof BdcLq) {
                BdcLq bdcLq = (BdcLq) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    GdLq gdLq = gdLqService.queryGdLqById(yqlid);
                    if (gdLq != null) {
                        gdLq = bdcCheckCancelService.getGdLqFilterZdsj(gdLq);
                    }
                    qllxVo = gdLqService.getBdcLqFromGdLq(gdLq, bdcLq);
                }
            } else if (qllxVo instanceof BdcTdcbnydsyq) {
                BdcTdcbnydsyq bdcTdcbnydsyq = (BdcTdcbnydsyq) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    GdCq gdCq = gdCqService.queryGdCqById(yqlid);
                    if (gdCq != null) {
                        gdCq = bdcCheckCancelService.getGdCqFilterZdsj(gdCq);
                    }
                    qllxVo = gdCqService.getBdcTdcbFromGdCq(gdCq, bdcTdcbnydsyq);
                }
            } else if (qllxVo instanceof BdcTdsyq) {
                BdcTdsyq bdcTdsyq = (BdcTdsyq) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    //zhouwanqing 这边需要继承gd_tdsyq的信息
                    GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, yqlid);
                    GdTd gdTd = null;
                    if (gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getTdid())) {
                        gdTd = gdTdService.queryGdTd(gdTdsyq.getTdid());
                    }
                    if (gdTd != null) {
                        gdTd = bdcCheckCancelService.getGdTdFilterZdsj(gdTd);
                    }
                    qllxVo = gdTdService.readBdcTdsyqFromGdTd(gdTd, gdTdsyq, bdcTdsyq);
                }
            } else if (qllxVo instanceof BdcYg) {
                BdcYg bdcYg = (BdcYg) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    GdYg gdYg = gdFwService.getGdYgByYgid(yqlid, null);
                    if (gdYg != null) {
                        gdYg = bdcCheckCancelService.getGdYgFilterZdsj(gdYg);
                    }
                    List<GdFw> gdFwList = gdFwService.getGdFwByQlid(yqlid);
                    List<GdTd> gdTdList = gdTdService.getGdTdListByFwQlid(yqlid);
                    List<GdQlr> gdTdQlrList = new ArrayList<GdQlr>();

                    if (CollectionUtils.isNotEmpty(gdTdList)) {
                        for (GdTd gdTd : gdTdList) {
                            List<GdTdsyq> tdsyqList = gdTdService.queryTdsyqByTdid(gdTd.getTdid());
                            if (CollectionUtils.isNotEmpty(tdsyqList)) {
                                gdTdQlrList = gdQlrService.queryGdQlrs(tdsyqList.get(0).getQlid(), Constants.QLRLX_QLR);
                            }
                        }
                        List<GdTdsyq> tdsyqList = gdTdService.queryTdsyqByTdid(gdTdList.get(0).getTdid());
                        if (CollectionUtils.isNotEmpty(tdsyqList)) {
                            bdcYg.setFttdmj(tdsyqList.get(0).getFtmj());
                        }
                    }

                    if (gdYg != null) {
                        qllxVo = getBdcYgFromGdYg(bdcYg, gdYg, gdTdQlrList, gdFwList);
                    }
                }
            } else if (qllxVo instanceof BdcYy) {
                BdcYy bdcYy = (BdcYy) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    GdYy gdYy = gdFwService.getGdYyByYyid(yqlid, null);
                    if (gdYy != null) {
                        qllxVo = getBdcYyFromGdYy(bdcYy, gdYy);
                    }
                }
            } else if (qllxVo instanceof BdcFdcqDz) {
                BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                if (StringUtils.isNotBlank(yqlid)) {
                    GdTdsyq gdTdsyq = null;
                    List<GdFw> gdFwList = gdFwService.getGdFwByQlid(yqlid);
                    List<GdTd> gdTdList = gdTdService.getGdTdListByFwQlid(yqlid);
                    List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
                    GdTd gdZhTd = null;
                    if (CollectionUtils.isNotEmpty(gdTdList)) {
                        for (GdTd gdTd : gdTdList) {
                            List<GdTdsyq> tdsyqList = gdTdService.queryTdsyqByTdid(gdTd.getTdid());
                            if (CollectionUtils.isNotEmpty(tdsyqList)) {
                                gdTdsyqList.addAll(tdsyqList);
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(gdTdList)) {
                        gdZhTd = bdcCheckCancelService.getGdTdFilterZdsj(gdTdList.get(0));

                    }
                    if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                        gdTdsyq = gdTdsyqList.get(0);
                    }
                    qllxVo = getBdcFdcqDzFromGdFw(bdcFdcqDz, gdFwList, gdZhTd, gdTdsyq, bdcXm);
                }
            }

        }
        return qllxVo;
    }

    @Override
    public List<QllxVo> getQllxByBdcdyh(QllxVo qllxVo, String bdcdyh) {
        List<QllxVo> qllxVoList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
            if (StringUtils.isNotBlank(bdcdyid)) {
                HashMap map = Maps.newHashMap();
                map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                map.put("qszt", Constants.QLLX_QSZT_XS);
                qllxVoList = andEqualQueryQllx(qllxVo, map);
            }
        }
        return qllxVoList;
    }

    @Override
    public List<Map> getBdcGDCfxxByBdcdyid(String bdcdyid) {
        List<Map> cfList = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            cfList = bdcCfService.queryBdcGdCfByPage(map);
        }
        return cfList;
    }

    @Override
    public List<Map> getDyaxxByBdcdyid(String bdcdyid) {
        List<Map> dyaList = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            dyaList = bdcDyaqService.queryBdcDyaqByPage(map);
        }
        return dyaList;
    }

    @Override
    public HashMap getGdQlZtByBdcdyh(String bdcdyh, HashMap resultMap) {
        if (StringUtils.isNotBlank(bdcdyh)) {
            HashMap<String, String> hashMap = gdFwService.getBdcdyhAndFwid(bdcdyh, "");
            if (hashMap != null) {
                String fwId = hashMap.get("FWID");
                List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelByBdcId(fwId);
                if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                    for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                        List gdQlList = gdFwService.getGdQlListByQlid(gdBdcQlRel.getQlid(), null, bdcdyService.getBdcdylxByBdcdyh(bdcdyh));
                        String qlzts = gdFwService.getQlztByQlid(gdBdcQlRel.getQlid(), null, bdcdyService.getBdcdylxByBdcdyh(bdcdyh), gdQlList);
                        for (String qlzt : qlzts.split(",")) {
                            if (StringUtils.equals(qlzt, Constants.GD_QLZT_DY)) {
                                resultMap.put("dy", false);
                            } else if (StringUtils.equals(qlzt, Constants.GD_QLZT_CF)) {
                                resultMap.put("cf", false);
                            } else if (StringUtils.equals(qlzt, Constants.GD_QLZT_YY)) {
                                resultMap.put("yy", false);
                            }
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * @param qllxVo,proid
     * @return QllxVo
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据OntBdcXm初始化QllxVo, 并上传附件
     */
    @Override
    public QllxVo initQllxVoFromOntBdcXm(QllxVo qllxVo, String proid) {
        if (RequestContextHolder.getRequestAttributes() != null && ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest() != null
                && ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession() != null) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            List<OntBdcXm> ontBdcXmList = (List<OntBdcXm>) session.getAttribute("ontBdcXm_" + proid);
            if (ontBdcXmList != null && !ontBdcXmList.isEmpty()) {
                OntBdcXm ontBdcXm = ontBdcXmList.get(0);
                if (qllxVo instanceof BdcYg) {
                    qllxVo.setGyqk(ontBdcXm.getGyfs());
                    //预告流程赋值取得价格为交易价格
                    ((BdcYg) qllxVo).setQdjg(ontBdcXm.getJyjg());
                    if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFDY)) {
                        //预告抵押流程赋值取得价格为被担保债权数额
                        ((BdcYg) qllxVo).setQdjg(Double.parseDouble(ontBdcXm.getBdbzzqse()));
                    }
                    ((BdcYg) qllxVo).setZwlxksqx(ontBdcXm.getZwlxksrq());
                    ((BdcYg) qllxVo).setZwlxjsqx(ontBdcXm.getZwlxjsrq());
                }
                if (qllxVo instanceof BdcFdcq) {
                    qllxVo.setGyqk(ontBdcXm.getGyfs());
                    ((BdcFdcq) qllxVo).setJyjg(ontBdcXm.getJyjg());
                }
            }
            //初始化权利后,上传材料附件,预告抵押合并流程只需要上传一次附件,加以流程类型判断
            String lclx = null;
            if (session.getAttribute("lclx_" + proid) != null) {
                lclx = session.getAttribute("lclx_" + proid).toString();
            }
            if (StringUtils.isBlank(lclx) || StringUtils.equals(lclx, "YG")) {
                ontService.initOntFile(bdcXm.getWiid(), bdcXm.getProid());
            }
            //清除session信息
            if (session.getAttribute("ontBdcXm_" + proid) != null) {
                session.removeAttribute("ontBdcXm_" + proid);
            }
            if (session.getAttribute("ontQlr_" + proid) != null) {
                session.removeAttribute("ontQlr_" + proid);
            }
            if (session.getAttribute("ontYwr_" + proid) != null) {
                session.removeAttribute("ontYwr_" + proid);
            }
            if (session.getAttribute("ontFiles_" + proid) != null) {
                session.removeAttribute("ontFiles_" + proid);
            }
            if (session.getAttribute("lclx_" + proid) != null) {
                session.removeAttribute("lclx_" + proid);
            }
        }
        return qllxVo;
    }

    private List<GdBdcQlRel> getGdBdcQlRelByBdcId(String bdcId) {
        Example example = new Example(GdBdcQlRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bdcid", bdcId);
        return entityMapper.selectByExample(example);
    }

    @Override
    public BdcJzwsyq getBdcJzwsyqFromGzw(BdcJzwsyq bdcJzwsyq, DjsjGzwxx djsjGzwxx) {
        if (bdcJzwsyq == null) {
            bdcJzwsyq = new BdcJzwsyq();
        }

        if (djsjGzwxx != null) {
            StringBuilder qlrmc = new StringBuilder();
            String zdzhh = "";
            if (StringUtils.isNotBlank(djsjGzwxx.getZdzhh())) {
                zdzhh = djsjGzwxx.getZdzhh();
            } else if (StringUtils.isNotBlank(djsjGzwxx.getBdcdyh())) {
                zdzhh = StringUtils.substring(djsjGzwxx.getBdcdyh(), 0, 19);
            }

            List<DjsjGzwQlr> gzwQlrList = bdcQlrService.getGzwQlrByDcbIndex(djsjGzwxx.getGzwDcbIndex());
            if (CollectionUtils.isNotEmpty(gzwQlrList)) {
                for (int i = 0; i < gzwQlrList.size(); i++) {
                    DjsjGzwQlr djsjGzwQlr = gzwQlrList.get(i);
                    if (i != gzwQlrList.size() - 1) {
                        qlrmc.append(djsjGzwQlr.getQlr()).append(" ");
                    } else {
                        qlrmc.append(djsjGzwQlr.getQlr());
                    }

                }
            }
            String zdtzm = StringUtils.substring(zdzhh, 13, 14);
            HashMap map = new HashMap();
            if (StringUtils.equals(zdtzm, Constants.ZDZHTZM_H)) {
                List<DjsjZhxx> djsjZhxxList = bdcDjsjService.getDjsjZhxxForDjh(zdzhh);
                if (CollectionUtils.isNotEmpty(djsjZhxxList)) {
                    DjsjZhxx djsjZhxx = djsjZhxxList.get(0);
                    if (djsjZhxx.getZhmj() != null && djsjZhxx.getZhmj() != 0) {
                        bdcJzwsyq.setTdhysymj(djsjZhxx.getZhmj());
                    }
                    map.put("bdclx", Constants.BDCLX_HY);
                }
            } else {
                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(zdzhh);
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    DjsjZdxx djsjZdxx = djsjZdxxList.get(0);
                    if (djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() != 0) {
                        bdcJzwsyq.setTdhysymj(djsjZdxx.getFzmj());
                    } else if (djsjZdxx.getScmj() != null && djsjZdxx.getScmj() != 0) {
                        bdcJzwsyq.setTdhysymj(djsjZdxx.getScmj());
                    }
                    if (djsjZdxx.getQsrq() != null) {
                        bdcJzwsyq.setSyksqx(djsjZdxx.getQsrq());
                    }
                    if (djsjZdxx.getZzrq() != null) {
                        bdcJzwsyq.setSyjsqx(djsjZdxx.getZzrq());
                    }
                    map.put("bdclx", Constants.BDCLX_TD);
                }
            }
            map.put("djh", zdzhh);
            List<Map> qlrList = djxxMapper.getDjQlrList(map);
            if (CollectionUtils.isNotEmpty(qlrList)) {
                for (int i = 0; i < qlrList.size(); i++) {
                    Map qlrMap = qlrList.get(i);
                    if (i != qlrList.size() - 1) {
                        qlrmc.append(qlrMap.get("QLR")).append(" ");
                    } else {
                        qlrmc.append(qlrMap.get("QLR"));
                    }
                }
                bdcJzwsyq.setTdhysyqr(qlrmc.toString());
            }

            if (djsjGzwxx.getMj() != null && djsjGzwxx.getMj() != 0) {
                bdcJzwsyq.setGzwmj(djsjGzwxx.getMj());
            }
            bdcJzwsyq.setJzwsyqr(qlrmc.toString());
            bdcJzwsyq.setGzwlx(djsjGzwxx.getGzwlx());
            String gyqk = djsjGzwxx.getGyqk();
            if (StringUtils.isNotBlank(gyqk)) {
                if (!NumberUtils.isNumber(gyqk)) {
                    gyqk = changeGyqkMcToDm(gyqk);
                }
                bdcJzwsyq.setGyqk(gyqk);
            }
            bdcJzwsyq.setJzwghyt(djsjGzwxx.getGzwghyt());
            bdcJzwsyq.setJgsj(djsjGzwxx.getJgsj());
        }

        return bdcJzwsyq;
    }

    @Override
    public void saveQllxVo(QllxVo qllxVo) {
        entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
    }

    /**
     * 修改原不动产单元对应的不动产信息表状态
     *
     * @param bdcXm
     * @param qszt
     */
    public void changeYbdcdyScxxbQlzt(BdcXm bdcXm, Integer qszt) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(ParamsConstants.BDCDYID_LOWERCASE, bdcXm.getBdcdyid());
            List<Map> list = djsjFwMapper.getDjsjHsBgJlb(param);
            if (CollectionUtils.isNotEmpty(list)) {
                for (Map data : list) {
                    String ybdcdyh = (String) data.get("YBDCDYH");
                    if (StringUtils.isNotBlank(ybdcdyh)) {
                        String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(ybdcdyh);
                        if (StringUtils.isNotBlank(bdcdyid)) {
                            param.clear();
                            param.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcq(param);
                            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                                for (BdcFdcq bdcFdcq : bdcFdcqList) {
                                    if (StringUtils.equals(bdcFdcq.getFzlx(), Constants.FZLX_FZM)) {
                                        bdcFdcq.setQszt(qszt);
                                        entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getQllxdmFromGdQl(String yqlid) {
        if (StringUtils.isNotBlank(yqlid)) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (StringUtils.isNotBlank(yqlid)) {
                map.put("QLID", yqlid.split(",")[0]);
            }
            HashMap<String, String> flagmap = gdqlMapper.getQllxdmFromGdQl(map);
            if (map.size() > 0) {
                String flag = flagmap.get("FLAG");
                //房屋所有权 和土地所有权 设定要细化
                if (StringUtils.equals(flag, "isGdFwSyq")) {
                    return "4";
                } else if (StringUtils.equals(flag, "isGdTdSyq")) {
                    return Constants.QLLX_GYTD_SUQ;
                } else if (StringUtils.equals(flag, "isGdCf")) {
                    return Constants.QLLX_CFDJ;
                } else if (StringUtils.equals(flag, "isGdYg")) {
                    return Constants.QLLX_YGDJ;
                } else if (StringUtils.equals(flag, "isGdYy")) {
                    return Constants.QLLX_YYDJ;
                } else if (StringUtils.equals(flag, "isGdDy")) {
                    return Constants.QLLX_DYAQ;
                }

            }
        }
        return null;
    }

    @Override
    public HashMap<String, String> getBdcZsByBdcdyid(String bdcdyid) {
        HashMap<String, String> zsMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(bdcdyid)) {
            zsMap = bdcDyMapper.getBdcZsByBdcdyid(bdcdyid);
        }
        return zsMap;
    }

    @Override
    public HashMap getBdcdyhQlxx(String bdcdyh) {
        HashMap resultMap = new HashMap();
        //获取查封数据
        List<Map> cfList = null;
        //获取抵押数据
        List<Map> dyList = null;
        //获取异议数据
        List<Map> yyList = null;
        /**
         * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
         * @description 增加查询以土地为主的在建建筑物抵押物清单中的不动产单元是否存在处于现势状态的抵押权
         */
        int zjgcdyBdcdyCount = 0;

        if (StringUtils.isNotBlank(bdcdyh)) {
            HashMap result = getBdcdyhxx(bdcdyh);
            Object qszt = result.get("QSZT");
            if (qszt != null && qszt.equals("2")) {
                resultMap.put("qszt", qszt);
            }
            cfList = getQlxxListByBdcdyh(bdcdyh, "(qlzt='3')");
            dyList = getQlxxListByBdcdyh(bdcdyh, "(qlzt='2' or qlzt='7')");
            yyList = getQlxxListByBdcdyh(bdcdyh, "(qlzt='6')");
            zjgcdyBdcdyCount = bdcZjjzwService.getDyBdcZjjzwxxByBdcdyh(bdcdyh);
        }
        // ture 正常 false 异议
        if (CollectionUtils.isEmpty(yyList)) {
            resultMap.put("yy", true);
        } else {
            resultMap.put("yy", false);
        }
        // true 正常  false 查封
        if (CollectionUtils.isEmpty(cfList)) {
            resultMap.put("cf", true);
        } else {
            resultMap.put("cf", false);
        }
        // true 正常  false 抵押
        if (CollectionUtils.isEmpty(dyList) && zjgcdyBdcdyCount == 0) {
            resultMap.put("dy", true);
        } else {
            resultMap.put("dy", false);
        }
        /**
         * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
         * @description 增加过渡权利
         */
        resultMap = getGdQlZtByBdcdyh(bdcdyh, resultMap);
        return resultMap;
    }

    @Override
    public HashMap getBdcdyhxx(String bdcdyh) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        StringBuilder qlrMcTemp = new StringBuilder();
        StringBuilder qsztTemp = new StringBuilder();
        if (StringUtils.isNotBlank(bdcdyh)) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
            if (bdcBdcdy != null) {
                if (StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                    resultMap.put("BDCLX", bdcBdcdy.getBdclx());
                }
                if (StringUtils.isNotBlank(bdcBdcdy.getBdcdyid())) {
                    Example example = new Example(BdcXm.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("bdcdyid", bdcBdcdy.getBdcdyid());
                    example.setOrderByClause("cjsj DESC");
                    List<BdcXm> bdcXmList = entityMapper.selectByExample(BdcXm.class, example);
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        BdcXm bdcXm = bdcXmList.get(0);
                        resultMap.put("ZL", bdcXm.getZl());
                        List<String> qlrmc = bdcQlrService.getQlrmcByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(qlrmc)) {
                            for (String qlr : qlrmc) {
                                if (StringUtils.isBlank(qlrMcTemp)) {
                                    qlrMcTemp.append(qlr);
                                } else {
                                    qlrMcTemp.append("," + qlr);
                                }
                            }
                            resultMap.put("QLR", qlrMcTemp.toString());
                        }
                        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                            HashMap<String, Object> query = new HashMap<String, Object>();
                            query.put("bdcdyid", bdcXm.getBdcdyid());
                            List<HashMap> bdcqlxxList = bdcdyService.getBdcQlxxList(query);
                            if (CollectionUtils.isNotEmpty(bdcqlxxList)) {
                                for (HashMap bdcqlxx : bdcqlxxList) {
                                    Object qszt = bdcqlxx.get("QSZT");
                                    if (StringUtils.isBlank(qsztTemp)) {
                                        qsztTemp.append(qszt);
                                    } else {
                                        qsztTemp.append("," + qszt);
                                    }
                                }
                                if (qsztTemp.indexOf("2") > -1) {
                                    resultMap.put("QSZT", "2");
                                } else {
                                    HashMap<String, Object> query2 = new HashMap<String, Object>();
                                    query2.put("proid", bdcXm.getProid());
                                    List<HashMap> bdcqlxxList2 = bdcdyService.getBdcQlxxList(query2);
                                    HashMap bdcqlxx = bdcqlxxList2.get(0);
                                    Object qszt = bdcqlxx.get("QSZT");
                                    resultMap.put("QSZT", qszt);
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public void noInheritJyjgByQllxVo(QllxVo qllxVo) {
        if (qllxVo != null && StringUtils.isNotBlank(qllxVo.getProid())) {
            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(qllxVo.getProid());
            BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(qllxVo.getProid());
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                bdcFdcq.setJyjg(null);
                entityMapper.updateByPrimaryKeyNull(bdcFdcq);
            } else if (bdcFdcqDz != null) {
                bdcFdcqDz.setFdcjyjg(null);
                entityMapper.updateByPrimaryKeyNull(bdcFdcqDz);
            }
        }
    }

    /**
     * @param bdcdyh 不动产单元号
     * @return 房地产权权利信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取房地产权权利信息
     */
    @Override
    public List<QllxVo> queryFdcqByBdcdyh(String bdcdyh) {
        List<QllxVo> qllxVoList = Lists.newArrayList();
        String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
        if (StringUtils.isNotBlank(bdcdyid)) {
            Example bdcFdcqExample = new Example(BdcFdcq.class);
            bdcFdcqExample.createCriteria().andEqualTo("bdcdyid", bdcdyid).andEqualTo("qszt", "1");
            List<BdcFdcq> bdcFdcqList = entityMapper.selectByExample(bdcFdcqExample);
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                qllxVoList.addAll(bdcFdcqList);
            }
            Example bdcFdcqDzExample = new Example(BdcFdcqDz.class);
            bdcFdcqDzExample.createCriteria().andEqualTo("bdcdyid", bdcdyid).andEqualTo("qszt", "1");
            List<BdcFdcqDz> bdcFdcqDzList = entityMapper.selectByExample(bdcFdcqDzExample);
            if (CollectionUtils.isNotEmpty(bdcFdcqDzList)) {
                qllxVoList.addAll(bdcFdcqDzList);
            }
        }
        if (CollectionUtils.isEmpty(qllxVoList)) {
            qllxVoList = Collections.EMPTY_LIST;
        }
        return qllxVoList;
    }
}