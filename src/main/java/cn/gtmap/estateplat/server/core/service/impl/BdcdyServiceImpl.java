package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcDyMapper;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 宗地/林权/房屋信息
 * Created by lst on 2015/3/17.
 */
@Deprecated
@Repository
public class BdcdyServiceImpl implements BdcdyService {

    @Autowired
    private BdcDyMapper bdcDyMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DjxxMapper djxxMapper;
    @Autowired
    private BdcBdcdyMapper bdcBdcdyMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdjbService bdcdjbService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private DjsjFwMapper djsjFwMapper;
    @Autowired
    private BdcPpgxService bdcPpgxService;

    protected final Logger logger = LoggerFactory.getLogger(BdcdyServiceImpl.class);


    @Override
    public BdcBdcdy getBdcdyFromProject(final Project project, BdcBdcdy bdcdy) {
        if (project == null) {
            return bdcdy;
        }
        //存入不动产单元的实体类中
        if (bdcdy == null) {
            bdcdy = new BdcBdcdy();
        }
        if (StringUtils.isNotBlank(project.getBdclx()))
            bdcdy.setBdclx(project.getBdclx());
        if (StringUtils.isNotBlank(project.getBdcdyh()))
            bdcdy.setBdcdyh(project.getBdcdyh());
        //在建建筑物抵押流程修改不动产类型
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, project.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, project.getSqlx())) {
            bdcdy.setBdclx(Constants.BDCLX_TDFW);
        }
        return bdcdy;
    }

    /**
     * @param projectPar
     * @param bdcBdcdy
     * @return 不动产单元
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化不动产单元信息
     */
    @Override
    public BdcBdcdy getBdcdyFromProjectPar(ProjectPar projectPar, BdcBdcdy bdcBdcdy) {
        if (projectPar == null) {
            return bdcBdcdy;
        }
        if (bdcBdcdy == null) {
            bdcBdcdy = new BdcBdcdy();
        }
        if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            bdcBdcdy.setBdcdyh(projectPar.getBdcdyh());
        }
        if (StringUtils.isNotBlank(projectPar.getBdclx())) {
            bdcBdcdy.setBdclx(projectPar.getBdclx());
        }
        //在建建筑物抵押流程修改不动产类型
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_DM, projectPar.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, projectPar.getSqlx())) {
            bdcBdcdy.setBdclx(Constants.BDCLX_TDFW);
        }
        return bdcBdcdy;
    }

    @Override
    public BdcBdcdy getBdcdyFromFw(DjsjFwxx fwxx, BdcBdcdy bdcdy) {
        if (fwxx == null) {
            return bdcdy;
        }
        //存入不动产单元的实体类中
        if (bdcdy == null) {
            bdcdy = new BdcBdcdy();
        }
        if (StringUtils.isNotBlank(fwxx.getBdcdyh()))
            bdcdy.setBdcdyh(fwxx.getBdcdyh());
        if (StringUtils.isNotBlank(fwxx.getBdcdyfwlx()))
            bdcdy.setBdcdyfwlx(fwxx.getBdcdyfwlx());
        if (StringUtils.isNotBlank(fwxx.getFcdah())) {
            bdcdy.setFwbm(fwxx.getFcdah());
        }
        return bdcdy;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcBdcdy> queryBdcBdcdy(final String wiid) {
        List<BdcBdcdy> bdcBdcdyList = null;
        if (StringUtils.isNotBlank(wiid)) {
            Map<String, String> map = Maps.newHashMap();
            map.put("wiid", wiid);
            bdcBdcdyList = bdcDyMapper.queryBdcBdcdy(map);
        }
        return bdcBdcdyList;
    }

    @Transactional(readOnly = true)
    public BdcBdcdy queryBdcBdcdyByProid(final String proid) {
        return StringUtils.isNotBlank(proid) ? bdcDyMapper.getBdcdyByProid(proid) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public String getZhhByProid(final String proid) {
        return StringUtils.isNotBlank(proid) ? bdcDyMapper.getZhhByProid(proid) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map> queryBdcdyhByDah(final Map map) {
        return bdcDyMapper.queryBdcdyhByDah(map);
    }

    @Override
    @Transactional(readOnly = true)
    public String getBdclxByPorid(final String proid) {
        return StringUtils.isNotBlank(proid) ? bdcDyMapper.getBdclxByPorid(proid) : null;
    }


    @Override
    @Transactional(readOnly = true)
    public BdcBdcdy queryBdcdyById(final String bdcdyid) {
        return StringUtils.isNotBlank(bdcdyid) ? entityMapper.selectByPrimaryKey(BdcBdcdy.class, bdcdyid) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public BdcBdcdy queryBdcdyByBdcdyh(final String bdcdyh) {
        if (StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(BdcBdcdy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            List<BdcBdcdy> list = entityMapper.selectByExample(BdcBdcdy.class, example);
            if (CollectionUtils.isNotEmpty(list))
                return list.get(0);
        }
        return null;
    }


    @Override
    @Transactional(readOnly = true)
    public BdcBdcdySd queryBdcdySdById(final String bdcdyid) {
        return StringUtils.isNotBlank(bdcdyid) ? entityMapper.selectByPrimaryKey(BdcBdcdySd.class, bdcdyid) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcBdcdySd> queryBdcdySdByBdcdyh(final String bdcdyh) {
        List<BdcBdcdySd> bdcBdcdySdList = new ArrayList<BdcBdcdySd>();
        if (StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(BdcBdcdySd.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            List<BdcBdcdySd> bdcBdcdySdListTemp = entityMapper.selectByExample(BdcBdcdySd.class, example);
            if (CollectionUtils.isNotEmpty(bdcBdcdySdListTemp)) {
                for (BdcBdcdySd bdcBdcdySd : bdcBdcdySdListTemp) {
                    if (StringUtils.isNotBlank(bdcBdcdySd.getXzzt()) && StringUtils.equals(bdcBdcdySd.getXzzt(), Constants.XZZT_SD))
                        bdcBdcdySdList.add(bdcBdcdySd);
                }
            }
        }
        return bdcBdcdySdList;
    }


    @Override
    @Transactional
    public void delBdcdyById(final String bdcdyid) {
        if (StringUtils.isNotBlank(bdcdyid))
            entityMapper.deleteByPrimaryKey(BdcBdcdy.class, bdcdyid);
    }

    @Override
    @Transactional
    public void delBdcdyByBdcdyh(final String bdcdyh) {
        if (StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(BdcBdcdy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            entityMapper.deleteByExample(BdcBdcdy.class, example);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map> getDjBdcdyListByPage(final Map map) {
        return djxxMapper.getDjBdcdyListByPage(map);
    }


    @Override
    public String getQllxFormBdcdy(final String bdcdyh) {
        String qllxdm = "";
        if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() >= 28) {
            String zdzhTzm = StringUtils.substring(bdcdyh, 12, 14);
            String dzwTzm = StringUtils.substring(bdcdyh, 19, 20);
            if (StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_JA)) {
                qllxdm = "1";
            } else if (StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_GB) || StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_GX)) {
                if (StringUtils.equals(dzwTzm, Constants.DZWTZM_W))
                    qllxdm = "3";
                else if (StringUtils.equals(dzwTzm, Constants.DZWTZM_F))
                    qllxdm = "4";
            } else if (StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_JC)) {
                if (StringUtils.equals(dzwTzm, Constants.DZWTZM_F))
                    qllxdm = "6";
                else
                    qllxdm = "5";
            } else if (StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_JB)) {
                if (StringUtils.equals(dzwTzm, Constants.DZWTZM_W))
                    qllxdm = "7";
                else if (StringUtils.equals(dzwTzm, Constants.DZWTZM_F))
                    qllxdm = "8";
            } else if (StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_JF) || StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_JD) || StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_GF) || StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_GD)) {
                qllxdm = "9";
            } else if (StringUtils.equals(zdzhTzm, Constants.ZDZHTZM_GE)) {
                qllxdm = "10";
            }
        }
        return qllxdm;
    }

    @Override
    public BdcBdcdy getBdcdyFromYProject(Project project, BdcBdcdy bdcdy) {
        if (project == null) {
            return bdcdy;
        }
        //存入不动产单元的实体类中
        if (bdcdy == null) {
            bdcdy = new BdcBdcdy();
        }
        if (StringUtils.isNotBlank(project.getBdclx()))
            bdcdy.setBdclx(project.getBdclx());
        if (StringUtils.isNotBlank(project.getBdcdyh()))
            bdcdy.setBdcdyh(project.getBdcdyh());
        if (StringUtils.isNotBlank(project.getYxmid())) {
            //参数修改为wiid @gtsy
            List<BdcBdcdy> bdcBdcdyList = queryBdcBdcdy(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcBdcdyList) && StringUtils.isNotBlank(project.getYbdcdyid())) {
                for (BdcBdcdy bdcBdcdy : bdcBdcdyList) {
                    if (bdcBdcdy != null && StringUtils.equals(bdcBdcdy.getBdcdyid(), project.getYbdcdyid())) {
                        bdcdy = bdcBdcdy;
                        break;
                    }
                }
            } else if (CollectionUtils.isNotEmpty(bdcBdcdyList))
                bdcdy = bdcBdcdyList.get(0);
        }

        return bdcdy;
    }

    @Override
    @Transactional(readOnly = true)
    public String getDjQlrByDjid(final String djid, String bdclxdm, final String zdtzm) {
        StringBuilder qlr = new StringBuilder();
        HashMap dyhMap = new HashMap();
        dyhMap.put("id", djid);
        if (StringUtils.isNotBlank(bdclxdm)) {
            if (StringUtils.equals(bdclxdm, Constants.DZWTZM_F)) {
                bdclxdm = Constants.BDCLX_TDFW;
            } else if (StringUtils.equals(bdclxdm, "W")) {
                if (StringUtils.isNotBlank(zdtzm) && zdtzm.indexOf("H") > -1) {
                    bdclxdm = Constants.BDCLX_HY;
                } else {
                    bdclxdm = Constants.BDCLX_TD;
                }

            } else if (StringUtils.equals(bdclxdm, "L"))
                bdclxdm = Constants.BDCLX_TDSL;
            else if (StringUtils.equals(bdclxdm, "Q"))
                bdclxdm = Constants.BDCLX_TDQT;
        }
        dyhMap.put(ParamsConstants.BDCLX_LOWERCASE, bdclxdm);
        List<Map> bdcdyMapList = djxxMapper.getDjBdcdyListByPage(dyhMap);
        HashMap map = new HashMap();
        map.put("djid", djid);
        map.put(ParamsConstants.BDCLX_LOWERCASE, bdclxdm);
        if (CollectionUtils.isNotEmpty(bdcdyMapList) && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(bdcdyMapList.get(0).get("DJH"))))
            map.put("djh", CommonUtil.formatEmptyValue(bdcdyMapList.get(0).get("DJH")));
        List<Map> djQlrList = djxxMapper.getDjQlrList(map);
        if (CollectionUtils.isNotEmpty(djQlrList)) {
            for (Map qlrMap : djQlrList) {
                if (StringUtils.isBlank(qlr)) {
                    qlr.append(CommonUtil.formatEmptyValue(qlrMap.get("QLR")));
                } else {
                    qlr.append("/").append(CommonUtil.formatEmptyValue(qlrMap.get("QLR")));
                }
            }
        }
        return String.valueOf(qlr);
    }

    @Override
    @Transactional(readOnly = true)
    public String[] getDjQlrIdsByQlr(final String qlr, final String bdclx) {
        List<String> idList = new ArrayList<String>();
        String[] ids = null;
        if (StringUtils.isNotBlank(qlr)) {
            HashMap map = new HashMap();
            map.put("qlr", qlr);
            map.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
            List<Map> djQlrList = djxxMapper.getDjQlrList(map);
            if (CollectionUtils.isNotEmpty(djQlrList)) {
                for (Map qlrMap : djQlrList) {
                    if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(qlrMap.get("ID"))) && !idList.contains(CommonUtil.formatEmptyValue(qlrMap.get("ID"))))
                        idList.add(CommonUtil.formatEmptyValue(qlrMap.get("ID")));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(idList)) {
            ids = new String[idList.size()];
            int i = 0;
            for (String id : idList) {
                ids[i] = id;
                i++;
            }
        }
        return ids;
    }

    @Override
    @Transactional(readOnly = true)
    public String getZdBdcdyh(final String bdcdyh) {
        String zdBdcdyh = "";
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(bdcdyh)) {
            map.put("djh", bdcdyh.substring(0, 19));
            // zdgl(宗地过滤)---过滤掉TDFW的不动产类型的地籍数据
            map.put(ParamsConstants.BDCLX_LOWERCASE, "TD");
            //zwq 当bdclx非为TDFW时候通过djh只会找到一条数据
            List<Map> zdxxList = djxxMapper.getDjBdcdyListByPage(map);
            if (CollectionUtils.isNotEmpty(zdxxList)) {
                map.clear();
                map = zdxxList.get(0);
                if (map != null && StringUtils.isNotBlank(map.get(ParamsConstants.BDCDYH_CAPITAL))) {
                    zdBdcdyh = map.get(ParamsConstants.BDCDYH_CAPITAL);
                }
            }
        }
        return zdBdcdyh;
    }

    @Override
    @Transactional(readOnly = true)
    public String getBdcdyidByBdcdyh(final String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcBdcdyMapper.getBdcdyidByBdcdyh(bdcdyh) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public String getBdcdylxByBdcdyh(final String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcBdcdyMapper.getBdcdylxByBdcdyh(bdcdyh) : null;
    }

    @Override
    public String creatBdcdyh(final Map map) {
        return bdcBdcdyMapper.creatBdcdyh(map);
    }

    @Override
    @Transactional
    public String createBdcdy(final String djh, final String zrzh, final String bdclx) {
        String bdcdyh = "";
        String zdtzm = "";
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(djh))
            map.put("djh", djh);
        if (StringUtils.isNotBlank(bdclx)) {
            if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.DZWTZM_W);
                zdtzm = Constants.DZWTZM_W;
                map.put("zrzhNull", true);
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.DZWTZM_F);
                zdtzm = Constants.DZWTZM_F;
                if (StringUtils.isNotBlank(zrzh))
                    map.put("zrzh", zrzh);
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TDSL))
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.DZWTZM_L);
            else if (StringUtils.equals(bdclx, Constants.BDCLX_TDQT))
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.DZWTZM_Q);
            map.put("bdclx", bdclx);
        }
        String bdcdyh4 = creatBdcdyh(map);
        if (StringUtils.isNotBlank(bdcdyh4)) {
            if (StringUtils.isNotBlank(zrzh)) {
                bdcdyh = djh + zdtzm + bdcdyh4;
            } else {
                bdcdyh = djh + zdtzm + "0000" + bdcdyh4;
            }
        } else {
            if (StringUtils.isNotBlank(zrzh)) {
                bdcdyh = djh + zdtzm + zrzh + "0001";
            } else {
                bdcdyh = djh + zdtzm + "00000001";
            }
        }
        if (StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(BdcCreateBdcdyh.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            List<BdcCreateBdcdyh> bdcCreatBdcdyList = entityMapper.selectByExample(BdcCreateBdcdyh.class, example);
            if (!CollectionUtils.isNotEmpty(bdcCreatBdcdyList)) {
                BdcCreateBdcdyh bdcCreatBdcdy = new BdcCreateBdcdyh();
                bdcCreatBdcdy.setBdcdyh(bdcdyh);
                bdcCreatBdcdy.setId(UUIDGenerator.generate18());
                entityMapper.saveOrUpdate(bdcCreatBdcdy, bdcCreatBdcdy.getId());
            }

        }
        return bdcdyh;
    }

    @Override
    public List<BdcBdcdy> getBdcdyByZdzhh(final String zdzhh, final String bdclx) {
        List<BdcBdcdy> bdcBdcdyList = null;
        if (StringUtils.isNotBlank(zdzhh)) {
            Example example = new Example(BdcBdcdy.class);
            if (StringUtils.isNotBlank(bdclx)) {
                example.createCriteria().andLike(ParamsConstants.BDCDYH_LOWERCASE, zdzhh + "%").andEqualTo(ParamsConstants.BDCLX_LOWERCASE, bdclx);
            } else {
                example.createCriteria().andLike(ParamsConstants.BDCDYH_LOWERCASE, zdzhh + "%");
            }
            bdcBdcdyList = entityMapper.selectByExample(BdcBdcdy.class, example);
        }
        return bdcBdcdyList;
    }


    @Override
    public void delDjbAndTd(final BdcXm bdcXm) {
        String zdzhh = "";
        //这边是为了删除项目的时候清楚唯一的不动产单元数据
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            BdcBdcdy bdcdy = queryBdcdyById(bdcXm.getBdcdyid());
            if (bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
                HashMap hashMap = new HashMap();
                hashMap.put(ParamsConstants.BDCDYID_LOWERCASE, bdcXm.getBdcdyid());
                List<BdcXm> bdcXmList = bdcXmService.andEqualQueryBdcXm(hashMap);
                //先判断该不动产单元是否有其他流程
                if ((bdcXmList == null || bdcXmList.size() == 1) && StringUtils.length(bdcdy.getBdcdyh()) > 19
                        && !StringUtils.equals(Constants.XNZDJH, StringUtils.substring(bdcdy.getBdcdyh(), 0, 19))
                        && !(StringUtils.equals(StringUtils.substring(bdcdy.getBdcdyh(), 6, 12), "000000")
                        && StringUtils.equals(StringUtils.substring(bdcdy.getBdcdyh(), 14, 19), "00000"))) {
                    // 江阴虚拟不动产单元号不需要删除，防止卡死
                    zdzhh = bdcdy.getBdcdyh().substring(0, 19);
                    //再判断不动产单元的宗地宗海号是否其他的不动产单元
                    List<BdcBdcdy> bdcBdcdyList = getBdcdyByZdzhh(zdzhh, null);
                    //若通过zdzhh未搜索到不动产单元或者搜索到本单元，则可以删除bdc_bdcdjb和bdc_td
                    if (bdcBdcdyList == null || bdcBdcdyList.size() == 1) {
                        bdcdjbService.delBdcdjbByZdzhh(zdzhh);
                        bdcTdService.delBdcTdByZdzhh(zdzhh);
                    }
                }
            }
        }
    }

    @Override
    public void delXmBdcdy(final String bdcdyid) {
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            List<BdcXm> bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            if (bdcXmList != null && bdcXmList.size() == 1) {
                delBdcdyById(bdcdyid);
            }
        }

    }

    @Override
    public String getBdcdyhByProid(final String proid) {
        return StringUtils.isNotBlank(proid) ? bdcBdcdyMapper.getBdcdyhByProid(proid) : null;
    }

    @Override
    public HashMap getBdcdyxxById(final String id) {
        if (StringUtils.isBlank(id)) return null;
        HashMap fwhsmap = new HashMap();
        fwhsmap.put("id", id);
        List<HashMap> bdcdyMapList = bdcBdcdyMapper.queryBdcdyFwByPage(fwhsmap);
        HashMap map = new HashMap();
        if (CollectionUtils.isNotEmpty(bdcdyMapList) && bdcdyMapList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
            Example example = new Example(BdcBdcdy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcdyMapList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
            List<BdcBdcdy> bdcBdcdyList = entityMapper.selectByExample(BdcBdcdy.class, example);
            if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {
                List<Map> cfList = qllxService.getCfxxByBdcdyId(bdcBdcdyList.get(0).getBdcdyid());
                List<BdcDyaq> dyList = qllxService.getDyxxByBdcdyId(bdcBdcdyList.get(0).getBdcdyid());
                List<Map> xsqlList = qllxService.getQlxxListByBdcdyh(bdcdyMapList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString(), "(qlzt='1')");
                // true 注销  false 正常
                if (CollectionUtils.isEmpty(xsqlList)) {
                    map.put("zx", true);
                } else {
                    if (CollectionUtils.isEmpty(cfList)) {
                        map.put("cf", true);
                    } else {
                        map.put("cf", false);
                    }
                    // true 正常  false 抵押
                    if (CollectionUtils.isEmpty(dyList)) {
                        map.put("dy", true);
                    } else {
                        map.put("dy", false);
                    }
                }
                Example exampleXm = new Example(BdcXm.class);
                exampleXm.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcBdcdyList.get(0).getBdcdyid());
                exampleXm.setOrderByClause("bjsj DESC");
                List<BdcXm> bdcXmList = entityMapper.selectByExample(BdcXm.class, exampleXm);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    if (!StringUtils.equals(bdcXmList.get(0).getXmzt(), "0")) {
                        map.put("DJZT", "已登记");
                    }
                    List<String> qlrmcList = bdcQlrService.getQlrmcByProid(bdcXmList.get(0).getProid());
                    if (CollectionUtils.isNotEmpty(qlrmcList)) {
                        StringBuilder qlr = new StringBuilder();
                        for (String qlrmc : qlrmcList) {
                            qlr.append(qlrmc).append(" ");
                        }
                        map.put("QLR", qlr);
                    }
                    String zl = bdcSpxxService.queryBdcSpxxByProid(bdcXmList.get(0).getProid()).getZl();
                    map.put("ZL", zl);
                }
                if (bdcXmList != null && bdcXmList.size() == 1 && StringUtils.equals(bdcXmList.get(0).getXmzt(), "0")) {
                    map.put("DJZT", "正在登记");
                }
            } else {
                map.put("DJZT", "未登记");
                HashMap qlrMap = new HashMap();
                if (bdcdyMapList.get(0).get("BDCLX") != null) {
                    String bdclx = bdcdyMapList.get(0).get("BDCLX").toString();
                    if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW_CHA)) {
                        qlrMap.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDFW);
                        qlrMap.put("djid", id);
                    } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD_CHA)) {
                        qlrMap.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TD);
                        qlrMap.put("djh", StringUtils.substring(bdcdyMapList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString(), 0, 19));

                    } else if (StringUtils.equals(bdclx, Constants.BDCLX_HY_CHA)) {
                        qlrMap.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_HY);
                        qlrMap.put("djh", StringUtils.substring(bdcdyMapList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString(), 0, 19));
                    }
                }
                List<Map> qlrMapList = djxxMapper.getDjQlrList(qlrMap);
                if (CollectionUtils.isNotEmpty(qlrMapList)) {
                    StringBuilder qlr = new StringBuilder();
                    for (Map qlrMap1 : qlrMapList) {
                        if (qlrMap1.get("QLR") != null) {
                            if (StringUtils.isNotBlank(qlr)) {
                                qlr.append(" ").append(qlrMap1.get("QLR"));
                            } else {
                                qlr.append(qlrMap1.get("QLR"));
                            }

                        }
                    }
                    map.put("QLR", qlr);
                }
            }
        }
        return map;
    }

    @Override
    public List<String> getDjhByQlr(final HashMap map) {
        return bdcBdcdyMapper.getDjhByQlr(map);
    }

    @Override
    public List<String> getCqqidByBdcdy(final String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcBdcdyMapper.getCqqidByBdcdy(bdcdyh) : null;
    }

    @Override
    public List<HashMap> getBdcQlxxList(HashMap map) {
        return bdcBdcdyMapper.getBdcQlxxList(map);
    }

    @Override
    public String getBdcdyfwlxBybdcdyh(String bdcdyh) {
        if(StringUtils.isNotBlank(bdcdyh)) {
            String bdcfwlx = bdcBdcdyMapper.getBdcdyfwlxBybdcdyh(bdcdyh);
            if (StringUtils.isBlank(bdcfwlx)) {
                List<String> bdcfwlxList = djsjFwMapper.getBdcfwlxByBdcdyh(bdcdyh);
                if (CollectionUtils.isNotEmpty(bdcfwlxList)) {
                    bdcfwlx = bdcfwlxList.get(0);
                }
            }
            return bdcfwlx;
        }
        return null;
    }


    public boolean sfYdhDfw(String bdcdyh) {
        boolean sfYdhDfw = false;
        String bdcfwlx = getBdcdyfwlxBybdcdyh(bdcdyh);
        if (StringUtils.equals(bdcfwlx, Constants.BDCFWLX_DZ_DM)) {
            List<BdcPpgx> bdcPpgxList = bdcPpgxService.getBdcPpgxByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                List<QllxVo> qllxVoList = qllxService.queryFdcqByBdcdyh(bdcdyh);
                if (CollectionUtils.isNotEmpty(qllxVoList)) {
                    sfYdhDfw = true;
                }
            }
        }
        return sfYdhDfw;
    }

    @Override
    public void saveBdcdy(BdcBdcdy bdcdy) {
        entityMapper.saveOrUpdate(bdcdy, bdcdy.getBdcdyid());
    }

    @Override
    public List<HashMap> getBdcdyidAndZlByWiid(final String wiid) {
        return bdcBdcdyMapper.getBdcdyidAndZlByWiid(wiid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcBdcdy> queryBdcBdcdyFilterBdcFwfsss(final String wiid) {
        List<BdcBdcdy> bdcBdcdyList = null;
        if (StringUtils.isNotBlank(wiid)) {
            Map<String, String> map = Maps.newHashMap();
            map.put("wiid", wiid);
            bdcBdcdyList = bdcDyMapper.queryBdcBdcdy(map);
        }
        return bdcBdcdyList;
    }

    @Override
    public List<String> getBdcdyhByBdcXmList(List<BdcXm> bdcXmList) {
        List<String> bdcdyhList = null;
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            bdcdyhList = bdcBdcdyMapper.getBdcdyhByBdcXmList(bdcXmList);
        }
        return bdcdyhList;
    }

    @Override
    public void batchDelBdcBdcdyBdcXmList(List<BdcXm> bdcXmList) {
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            List<BdcXm> bdcXmListTemp = new ArrayList<BdcXm>();
            //qijiadong 删除前需要判断bdcdyid是否存在其他预告等项目
            for (BdcXm bdcXm : bdcXmList) {
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid()) && StringUtils.isNotBlank(bdcXm.getWiid())
                        && !judgeBdcdyidExistsOtherXm(bdcXm.getBdcdyid(), bdcXm.getWiid())) {
                    bdcXmListTemp.add(bdcXm);
                }
            }
            if (CollectionUtils.isNotEmpty(bdcXmListTemp)) {
                bdcBdcdyMapper.batchDelBdcBdcdyBdcXmList(bdcXmListTemp);
            }
        }
    }

    @Override
    public String getTdDjsjBdclxByBdcdyh(String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcBdcdyMapper.getTdDjsjBdclxByBdcdyh(bdcdyh) : null;
    }

    @Override
    public List<BdcBdcdy> getBdcdyInfoByQueryMap(Map map) {
        return map != null ? bdcBdcdyMapper.getBdcdyInfoByQueryMap(map) : null;
    }

    @Override
    public List<BdcBdcZsSd> getBdcBdcZsSdByCqzh(final String cqzh) {
        List<BdcBdcZsSd> bdcBdcZsSdList = null;
        if (StringUtils.isNotBlank(cqzh)) {
            Example example = new Example(BdcBdcZsSd.class);
            example.createCriteria().andEqualTo("cqzh", cqzh);
            bdcBdcZsSdList = entityMapper.selectByExample(example);
        }
        return bdcBdcZsSdList;
    }

    @Override
    public List<GdBdcSd> getGdBdcSdByCqzh(final String cqzh) {
        List<GdBdcSd> gdBdcSdList = null;
        if (StringUtils.isNotBlank(cqzh)) {
            Example example = new Example(GdBdcSd.class);
            example.createCriteria().andEqualTo("cqzh", cqzh);
            gdBdcSdList = entityMapper.selectByExample(example);
        }
        return gdBdcSdList;
    }

    @Override
    public Boolean judgeBdcdyidExistsOtherXm(final String bdcdyid, final String wiid) {
        Boolean returnboolean = false;
        if (StringUtils.isNotBlank(bdcdyid) && StringUtils.isNotBlank(wiid)) {
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo("bdcdyid", bdcdyid).andNotEqualTo("wiid", wiid);
            List<BdcXm> bdcXmList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                returnboolean = true;
            }
        }
        return returnboolean;
    }

    @Override
    public BdcBdcdy initBdcdy(InitVoFromParm initVoFromParm, BdcBdcdjb bdcdjb, String bdcdyh, String bdclx) {
        BdcBdcdy bdcdy = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            bdcdy = new BdcBdcdy();
            if (bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid())) {
                bdcdy.setDjbid(bdcdjb.getDjbid());
            }
            if (StringUtils.isNotBlank(bdclx)) {
                bdcdy.setBdclx(bdclx);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                bdcdy.setBdcdyh(bdcdyh);
            }

            bdcdy = getBdcdyFromFw(initVoFromParm.getDjsjFwxx(), bdcdy);

            BdcBdcdy tempBdcdy = queryBdcdyByBdcdyh(bdcdy.getBdcdyh());
            if (tempBdcdy != null) {
                bdcdy.setBdcdyid(tempBdcdy.getBdcdyid());
            } else {
                bdcdy.setBdcdyid(UUIDGenerator.generate18());
            }
        }
        return bdcdy;
    }

    @Override
    public List<BdcBdcdy> queryBdcBdcdyByFwbm(String fwbm) {
        List<BdcBdcdy> bdcBdcdyList = null;
        if (StringUtils.isNotBlank(fwbm)) {
            Example example = new Example(BdcBdcdy.class);
            example.createCriteria().andEqualTo("fwbm", fwbm);
            bdcBdcdyList = entityMapper.selectByExample(example);
        }
        return bdcBdcdyList;
    }


    @Override
    public synchronized String getBdcdyh(String djh, int xh) {
        String bdcdyh = "";
        if (StringUtils.isNotBlank(djh)) {
            Integer lsh = bdcBdcdyMapper.getMaxLshByDjh(djh + "%");
            DecimalFormat df = new DecimalFormat("00000000");
            if (lsh != null) {
                bdcdyh = djh + df.format(lsh + xh);
            } else {
                bdcdyh = djh + df.format(xh);
            }
        }
        if (StringUtils.isBlank(bdcdyh)) {
            throw new AppException("生成虚拟单元号失败");
        } else {
            if (StringUtils.length(bdcdyh) != 28) {
                throw new AppException("生成虚拟单元号位数不正确，请联系管理员");
            }
            String bdcdyid = getBdcdyidByBdcdyh(bdcdyh);
            if (StringUtils.isNotBlank(bdcdyid)) {
                throw new AppException("生成虚拟单元号已存在请联系系统管理员");
            }
        }
        return bdcdyh;
    }

    /**
     * @param bdcdyh 不动产单元号
     * @return 产权proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取产权proid
     */
    @Override
    public String getCqproidByBdcdyh(String bdcdyh) {
        String proid = "";
        if (StringUtils.isNotBlank(bdcdyh)) {
            List<String> proidList = bdcBdcdyMapper.getCqproidByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(proidList)) {
                proid = proidList.get(0);
            }
        }
        return proid;
    }

    @Override
    public BdcBdcdy getBdcdyByProid(String proid) {
        if(StringUtils.isNotBlank(proid)){
            return bdcBdcdyMapper.getBdcdyByProid(proid);
        }
       return null;
    }
}
