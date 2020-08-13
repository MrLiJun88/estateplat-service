package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcFdcqMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 房产产权（独幢）
 * User: Administrator
 * Date: 15-4-15
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcFdcqServiceImpl implements BdcFdcqService {
    @Autowired
    BdcFdcqMapper bdcFdcqMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private DjsjFwService djsjFwService;

    @Override
    public List<BdcFdcq> getBdcFdcq(Map map) {
        return bdcFdcqMapper.getBdcFdcq(map);
    }

    @Override
    @Transactional(readOnly = true)
    public String getzrddh(String proid) {
        return bdcFdcqMapper.getzrddh(proid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcFdcq> getBdcFdcqByProid(String proid) {
        return bdcFdcqMapper.getBdcFdcqByProid(proid);
    }

    @Override
    @Transactional
    public void delBdcFdcqByProid(String proid) {
        List<BdcFdcq> bdcFdcqList = getBdcFdcqByProid(proid);
        if (bdcFdcqList != null) {
            for (BdcFdcq bdcFdcq : bdcFdcqList) {
                if (bdcFdcq != null && StringUtils.isNotBlank(bdcFdcq.getQlid()))
                    entityMapper.deleteByPrimaryKey(BdcFdcq.class, bdcFdcq.getQlid());
            }
        }
    }

    /**
     * @param bdcFdcq
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 保存房地产权信息
     */
    @Override
    public void saveBdcFdcq(BdcFdcq bdcFdcq) {
        entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
    }

    /**
     * @param model,qlid,bdcXm
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 初始化不动产房地产权信息页面
     */
    @Override
    public Model initBdcFdcqForPl(Model model, String qlid, BdcXm bdcXm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String djlxMc="";
        String sqlxMc="";
        if(bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getDjlx())){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dm", bdcXm.getDjlx());
            List<HashMap> djlxList = bdcZdGlService.getBdcZdDjlx(map);
            if (CollectionUtils.isNotEmpty(djlxList) && djlxList.get(0).get("MC") != null)
                djlxMc=djlxList.get(0).get("MC").toString();
        }
        if(bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getSqlx())){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dm", bdcXm.getSqlx());
            List<HashMap> sqlxList = bdcZdGlService.getBdcZdSqlx(map);
            if (CollectionUtils.isNotEmpty(sqlxList) && sqlxList.get(0).get("MC") != null)
                sqlxMc=sqlxList.get(0).get("MC").toString();
        }
        List<HashMap> djzxList = bdcZdGlService.getDjzxByProid(bdcXm.getProid());
        List<HashMap> fwlxList = bdcZdGlService.getBdcZdFwlxList(new HashMap());
        List<HashMap> fwxzList = bdcZdGlService.getBdcZdFwxz(new HashMap());
        List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
        List<Map> fwjgList = bdcZdGlService.getZdFwjg();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        BdcFdcq bdcFdcq = null;
        if(StringUtils.isNotBlank(qlid))
            bdcFdcq = entityMapper.selectByPrimaryKey(BdcFdcq.class, qlid);
        String tdsyksqx = "";
        String tdsyjsqx = "";
        String jgsj = "";
        String djsj = "";
        if (bdcFdcq != null && bdcFdcq.getTdsyksqx() != null)
            tdsyksqx = sdf.format(bdcFdcq.getTdsyksqx());
        if (bdcFdcq != null && bdcFdcq.getTdsyjsqx() != null)
            tdsyjsqx = sdf.format(bdcFdcq.getTdsyjsqx());
        if (bdcFdcq != null && bdcFdcq.getJgsj() != null)
            jgsj = sdf.format(bdcFdcq.getJgsj());
        if (bdcFdcq != null && bdcFdcq.getDjsj() != null)
            djsj = sdf.format(bdcFdcq.getDjsj());
        model.addAttribute("tdsyksqx", tdsyksqx);
        model.addAttribute("tdsyjsqx", tdsyjsqx);
        model.addAttribute("jgsj", jgsj);
        model.addAttribute("djsj", djsj);
        //处理面积单位
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getMjdw()))
            model.addAttribute("mjdw", bdcSpxx.getMjdw());
        if(bdcFdcq==null)
            bdcFdcq=new BdcFdcq();
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("zjlxList", zjlxList);
        model.addAttribute("djlxMc", djlxMc);
        model.addAttribute("sqlxMc", sqlxMc);
        model.addAttribute("djzxList", djzxList);
        model.addAttribute("fwlxList", fwlxList);
        model.addAttribute("fwxzList", fwxzList);
        model.addAttribute("fwytList", fwytList);
        model.addAttribute("fwjgList", fwjgList);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("bdcFdcq", bdcFdcq);
        return model;
    }

    @Override
    public Map<String, Object> getTdsyqx(String proid) {
        return bdcFdcqMapper.getTdsyqx( proid);
    }

    @Override
    public String getSpfscdjHsFzlx(HashMap map) {
        return bdcFdcqMapper.getSpfscdjHsFzlx(map);
    }


    @Override
    public void batchDelBdcFdcqByBdcXmList(List<BdcXm> bdcXmList) {
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            bdcFdcqMapper.batchDelBdcFdcqByBdcXmList(bdcXmList);
        }
    }

    @Override
    public void batchChangeQllxZt(List<BdcXm> bdcXmList, Integer qszt,Date qlqssj) {
        if(CollectionUtils.isNotEmpty(bdcXmList) && qszt != null && qlqssj != null){
            HashMap map = Maps.newHashMap();
            map.put("bdcXmList",bdcXmList);
            map.put("qszt",qszt);
            map.put("qlqssj",qlqssj);
            bdcFdcqMapper.batchChangeQllxZt(map);
        }
    }

    @Override
    public HashMap<String, String> getTdAndFwSjxx(String proid){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(proid)){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null) {
                String tdOrTdfwMj = "";
                String tdOrTdfwYt = "";
                String tdOrTdfwQlxz = "";
                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                if(qllxVo != null&&(qllxVo instanceof BdcFdcq||qllxVo instanceof BdcFdcqDz)) {
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    List<BdcFdcq> bdcFdcqList = getBdcFdcqByProid(proid);
                    BdcFdcq bdcFdcq = null;
                    if(CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        bdcFdcq = bdcFdcqList.get(0);
                    }
                    BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(proid);
                    tdOrTdfwMj = getTdFwMj(bdcSpxx, bdcXm, bdcFdcq);
                    tdOrTdfwYt = getTdFwYt(bdcSpxx, bdcXm);
                    tdOrTdfwQlxz = getTdFwxz(bdcSpxx, bdcXm, bdcFdcq, bdcFdcqDz);
                }else{
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                    if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        for(BdcXmRel bdcXmRel:bdcXmRelList) {
                            if(bdcXmRel!= null&&StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                                BdcXm yBdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                                if(yBdcXm != null) {
                                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXmRel.getYproid());
                                    List<BdcFdcq> bdcFdcqList = getBdcFdcqByProid(bdcXmRel.getYproid());
                                    BdcFdcq bdcFdcq = null;
                                    if(CollectionUtils.isNotEmpty(bdcFdcqList)) {
                                        bdcFdcq = bdcFdcqList.get(0);
                                    }
                                    BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXmRel.getYproid());
                                    tdOrTdfwMj = getTdFwMj(bdcSpxx, bdcXm, bdcFdcq);
                                    tdOrTdfwYt = getTdFwYt(bdcSpxx, bdcXm);
                                    tdOrTdfwQlxz = getTdFwxz(bdcSpxx, bdcXm, bdcFdcq, bdcFdcqDz);
                                }
                            }
                        }
                    }
                }
                resultMap.put("tdOrTdfwMj",tdOrTdfwMj);
                resultMap.put("tdOrTdfwYt",tdOrTdfwYt);
                resultMap.put("tdOrTdfwQlxz",tdOrTdfwQlxz);
            }
        }
        return resultMap;
    }

    @Override
    public void updateFdcqByDjsjfwhs(DjsjFwHs djsjFwHs,BdcFdcq bdcFdcq) {
        if (bdcFdcq != null && djsjFwHs != null) {
            bdcFdcq.setJzmj(djsjFwHs.getScjzmj());
            bdcFdcq.setTnjzmj(djsjFwHs.getSctnjzmj());
            bdcFdcq.setGhyt(djsjFwHs.getGhyt());
            bdcFdcq.setFtjzmj(djsjFwHs.getScftjzmj());
            bdcFdcq.setBdcdybh(djsjFwHs.getFwbm());
            bdcFdcq.setFwdah(djsjFwHs.getFwbm());
            saveBdcFdcq(bdcFdcq);
        }
    }

    public String getTdFwMj(BdcSpxx bdcSpxx, BdcXm bdcXm, BdcFdcq bdcFdcq) {
        String zdzhmj = null;
        String jzmj = null;
        if (bdcSpxx != null && bdcSpxx.getZdzhmj() != null) {
            zdzhmj = bdcSpxx.getZdzhmj().toString();
            if (StringUtils.equals(bdcXm.getBdclx(), Constants.BDCLX_TDFW)) {
                if (bdcFdcq != null && bdcFdcq.getJzmj() != null) {
                    jzmj = bdcFdcq.getJzmj().toString();
                } else {
                    jzmj = String.valueOf(bdcSpxx.getMj());
                }
            }
            zdzhmj = combineString(zdzhmj, jzmj);
        }
        return zdzhmj;
    }

    public String getTdFwYt(BdcSpxx bdcSpxx, BdcXm bdcXm) {
        String zdzhyt = null;
        if (null !=bdcSpxx && StringUtils.isNotBlank(bdcSpxx.getZdzhyt())) {
            String tdYtDm = bdcSpxx.getZdzhyt();
            String fwYtDm = null;
            String fwYt = null;
            String tdYt = bdcZdGlService.getZdytMcByDm(tdYtDm);
            if (StringUtils.isNotBlank(tdYt)){
                zdzhyt = tdYt;
            }
            if (bdcSpxx.getYt() != null && StringUtils.equals(bdcXm.getBdclx(), Constants.BDCLX_TDFW)) {
                fwYtDm = bdcSpxx.getYt();
            }
            if (StringUtils.isNotBlank(fwYtDm)) {
                fwYt = bdcZdGlService.getFwytByDm(fwYtDm);
            }
            zdzhyt = combineString(zdzhyt, fwYt);
        }
        return zdzhyt;
    }

    public String getTdFwxz(BdcSpxx bdcSpxx, BdcXm bdcXm, BdcFdcq bdcFdcq, BdcFdcqDz bdcFdcqDz) {
        String zdzhqlxz = null;
        String djsjFwxz = null;
        if (StringUtils.isNotBlank(bdcSpxx.getZdzhqlxz())) {
            HashMap queryMap = new HashMap();
            String fwxzDm = null;
            List<HashMap> resultTdMapList = null;
            String tdQlxzDm = bdcSpxx.getZdzhqlxz();
            queryMap.put("dm", tdQlxzDm);
            resultTdMapList = bdcZdGlService.getQlxzZdb(queryMap);
            if (CollectionUtils.isNotEmpty(resultTdMapList)) {
                zdzhqlxz = resultTdMapList.get(0).get("MC").toString();
            }
            if (StringUtils.equals(bdcXm.getBdclx(), Constants.BDCLX_TDFW)) {
                if (bdcFdcq != null && bdcFdcq.getGhyt() != null) {
                    fwxzDm = bdcFdcq.getFwxz();
                }else if(bdcFdcqDz != null){
                    fwxzDm = bdcFdcqDz.getFwxz();
                } else {
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    DjsjFwxx djsjFwxx = null;
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                        if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                            djsjFwxx = djsjFwService.getDjsjFwxx(bdcXmRel.getQjid());
                        }
                        if (djsjFwxx != null) {
                            fwxzDm = djsjFwxx.getFwxz();
                        }
                    }
                }
            }
            List<HashMap> resultFwMapList = new ArrayList<HashMap>();
            if (StringUtils.isNotBlank(fwxzDm)) {
                queryMap.clear();
                queryMap.put("dm", fwxzDm);
                resultFwMapList = bdcZdGlService.getBdcZdFwxz(queryMap);
            }
            if (CollectionUtils.isNotEmpty(resultFwMapList)) {
                djsjFwxz = resultFwMapList.get(0).get("MC").toString();
            }
            zdzhqlxz = combineString(zdzhqlxz, djsjFwxz);
        }
        return zdzhqlxz;
    }

    public String combineString(String a, String b) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(a) && StringUtils.isNotBlank(b)) {
            sb.append(a);
            sb.append("/");
            sb.append(b);
        }
        if (StringUtils.isBlank(sb)) {
            if (StringUtils.isNotBlank(a)) {
                sb.append(a);
            }
            if (StringUtils.isNotBlank(b)) {
                sb.append(b);
            }
        }
        return sb.toString();
    }
}
