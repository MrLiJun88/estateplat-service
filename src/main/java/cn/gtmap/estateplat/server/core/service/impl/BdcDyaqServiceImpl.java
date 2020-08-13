package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDyaqMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcXmMapper;
import cn.gtmap.estateplat.server.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-11
 */
@Service
public class BdcDyaqServiceImpl implements BdcDyaqService {
    @Autowired
    BdcDyaqMapper bdcDyaqMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmMapper bdcXmMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    QllxParentService qllxParentService;
    @Autowired
    GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcFwFsssService bdcFwFsssService;
    @Override
    @Transactional
    public void updateBdcDyaqForZxdj(final BdcDyaq bdcDyaq) {
        if (bdcDyaq != null && StringUtils.isNotBlank(bdcDyaq.getQlid()))
            bdcDyaqMapper.updateBdcDyaqForZxdj(bdcDyaq);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcDyaq> queryBdcDyaq(final Map map) {
        return bdcDyaqMapper.queryBdcDyaq(map);
    }

    @Override
    public List<BdcDyaq> queryYdyaqByProid(final String proid,final Integer qszt) {
        return bdcDyaqMapper.queryYdyaqByProid(proid, qszt);
    }
    
    /**
     * @author  xiejianan
     * @see cn.gtmap.estateplat.server.core.service.BdcDyaqService#queryBdcDyaqByPage(java.util.Map)
     * 2016年5月20日
     * @description 
     * @param map
     * @return
     */
    @Override
	public List<Map> queryBdcDyaqByPage(final Map map) {
		return bdcDyaqMapper.queryBdcDyaqByPage(map);
	}

    /**
     * @param map
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 通过fwid和wiid确定当前项目下的抵押权
     */
    @Override
    public BdcDyaq queryBdcDyaqByFwid(HashMap map) {
        return bdcDyaqMapper.queryBdcDyaqByFwid(map);
    }

    @Override
    public BdcDyaq queryBdcDyaqByProid(String proid) {
        return bdcDyaqMapper.queryBdcDyaqByProid(proid);
    }

    @Override
    public Model initBdcDyaqForPl(Model model, String qlid, BdcXm bdcXm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder bdcYwr = new StringBuilder();
        List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
        if(CollectionUtils.isNotEmpty(bdcYwrList)){
            for(int j=0;j<bdcYwrList.size();j++){
                if(StringUtils.isNotBlank(bdcYwrList.get(j).getQlrmc())){
                    if(j==0){
                        bdcYwr.append(bdcYwrList.get(j).getQlrmc());
                    }else{
                        if(bdcYwr.indexOf(bdcYwrList.get(j).getQlrmc())<0){
                            bdcYwr.append(" ").append(bdcYwrList.get(j).getQlrmc());
                        }
                    }
                }
            }
        }
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
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        BdcDyaq bdcDyaq = entityMapper.selectByPrimaryKey(BdcDyaq.class, qlid);
        List<HashMap> djzxList = bdcZdGlService.getDjzxByProid(bdcXm.getProid());
        List<Map>  dyfsList = bdcZdGlService.getZdDyfs();
        //处理面积单位
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getMjdw()))
            model.addAttribute("mjdw", bdcSpxx.getMjdw());
        //处理证件类型
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        // 处理登记事由
        if(StringUtils.isNotBlank(bdcXm.getDjsy())){
            String djsy="";
            djsy=bdcZdGlService.getDjsyByDm (bdcXm.getDjsy());
            if(djsy!=null){
                model.addAttribute("djsy", djsy);
            }else
            {
                model.addAttribute("djsy", bdcXm.getDjsy());
            }
        }
        List<HashMap> djsyList = bdcZdGlService.getBdcZdDjsy(new HashMap());
        List<HashMap>  dybdclxList= bdcZdGlService.getBdcZdDybdclx(new HashMap());
        //处理时间格式
        String zwlxksqx="";
        String zwlxjsqx="";
        String djsj="";
        String zxsj="";
        if(bdcDyaq!=null&&bdcDyaq.getZwlxksqx()!=null)
            zwlxksqx = sdf.format(bdcDyaq.getZwlxksqx());
        if(bdcDyaq!=null&&bdcDyaq.getZwlxjsqx()!=null)
            zwlxjsqx = sdf.format(bdcDyaq.getZwlxjsqx());
        if(bdcDyaq!=null&&bdcDyaq.getZxsj()!=null)
            zxsj = sdf.format(bdcDyaq.getZxsj());
        if(bdcDyaq!=null&&bdcDyaq.getZwlxjsqx()!=null)
            djsj = sdf.format(bdcDyaq.getZwlxjsqx());
        model.addAttribute("bdcYwr", bdcYwr);
        model.addAttribute("bdcQlrList", bdcQlrList);
        model.addAttribute("bdcDyaq", bdcDyaq);
        model.addAttribute("djlxMc", djlxMc);
        model.addAttribute("sqlxMc", sqlxMc);
        model.addAttribute("djzxList",djzxList);
        model.addAttribute("dyfsList",dyfsList);
        model.addAttribute("zwlxksqx", zwlxksqx);
        model.addAttribute("zwlxjsqx", zwlxjsqx);
        model.addAttribute("zxsj", zxsj);
        model.addAttribute("djsj", djsj);
        model.addAttribute("djsyList", djsyList);
        model.addAttribute("dybdclxList", dybdclxList);
        model.addAttribute("zjlxList", zjlxList);
        return model;
    }

    @Override
    public void saveBdcDyaq(BdcDyaq bdcDyaq) {
        entityMapper.saveOrUpdate(bdcDyaq,bdcDyaq.getQlid());
    }

    @Override
    public Map<String,Object> getDyaqxxForPrint(String proid){
        return bdcDyaqMapper.getDyaqxxForPrint(proid);
    }

    @Override
    public void  dealInheritFsssForDyaq(String proid, List<BdcFwfsss> bdcFwfsssList){
        if(StringUtils.isNotBlank(proid)&&CollectionUtils.isNotEmpty(bdcFwfsssList)){
            BdcDyaq bdcDyaq = queryBdcDyaqByProid(proid);
            double fsssZmj = 0.00;
            for(BdcFwfsss bdcFwfsss:bdcFwfsssList){
                if(bdcFwfsss.getJzmj()!=null&&bdcFwfsss.getJzmj() > 0){
                    fsssZmj += bdcFwfsss.getJzmj();
                }
            }
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if(bdcSpxx != null&&bdcSpxx.getMj() != null) {
                bdcSpxx.setMj(bdcSpxx.getMj()+fsssZmj);
                bdcSpxxService.saveBdcSpxx(bdcSpxx);
            }
            if(bdcDyaq!=null&&bdcDyaq.getFwdymj()!=null){
                bdcDyaq.setFwdymj(bdcDyaq.getFwdymj()+fsssZmj);
                saveBdcDyaq(bdcDyaq);
            }
        }
    }

    @Override
    public void  dealNotInheritFsssForDyaq(String proid,String yproid){
        if(StringUtils.isNotBlank(proid)&&StringUtils.isNotBlank(yproid)){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(yproid);
            HashMap hashMap = new HashMap();
            if(bdcXm!=null && ybdcXm != null && bdcBdcdy != null) {
                hashMap.put("proid",yproid);
                hashMap.put("zfbdcdyh",bdcBdcdy.getBdcdyh());
                double fsssZmj = 0.00;
                List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssList(hashMap);
                if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                    for(BdcFwfsss bdcFwfsss:bdcFwfsssList){
                        if(bdcFwfsss.getJzmj()!=null&&StringUtils.isNotBlank(bdcFwfsss.getProid())){
                            fsssZmj += bdcFwfsss.getJzmj();
                        }
                    }
                }
                BdcDyaq bdcDyaq = queryBdcDyaqByProid(proid);
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                if(bdcDyaq!=null&&bdcDyaq.getFwdymj()!=null){
                    bdcDyaq.setFwdymj(bdcDyaq.getFwdymj()-fsssZmj);
                    saveBdcDyaq(bdcDyaq);
                }
            }
        }
    }

    @Override
    public void saveZqqxForEveryPoid(List<String> proidList,BdcDyaq bdcDyaq){
        if(bdcDyaq.getZwlxksqx() != null && bdcDyaq.getZwlxjsqx() != null){
            Date zwlxKsqx = bdcDyaq.getZwlxksqx();
            Date zwlxJsqx = bdcDyaq.getZwlxjsqx();
            for(String proid : proidList){
                BdcDyaq otherBdcDyaq = queryBdcDyaqByProid(proid);
                if(otherBdcDyaq != null){
                    otherBdcDyaq.setZwlxksqx(zwlxKsqx);
                    otherBdcDyaq.setZwlxjsqx(zwlxJsqx);
                    saveBdcDyaq(otherBdcDyaq);
                }
            }
        }
    }

    @Override
    public  Boolean validateDyaq(String bdcdyid){
        Boolean isExsitDyaq = false;
        if(StringUtils.isNotBlank(bdcdyid)){
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
            if(bdcBdcdy != null){
                String bdcdyh = bdcBdcdy.getBdcdyh();
                if(StringUtils.isNotBlank(bdcdyh)){
                    HashMap<String, Object> querymap = new HashMap<String, Object>();
                    querymap.put("bdcdyh",bdcdyh);
                    List<QllxParent> list = qllxParentService.queryZtzcQllxVo(new BdcDyaq(), querymap);
                    if (CollectionUtils.isNotEmpty(list)) {
                        isExsitDyaq = true;
                    } else {
                        List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.getGdBdcQlRelByBdcdyh(bdcdyh);
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRel.getQlid(), 0);
                                if (gdDy != null) {
                                    isExsitDyaq = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return  isExsitDyaq;
    }
}
