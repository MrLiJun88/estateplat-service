package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 * 注销登记办结  注销原权利证书
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class EndProjectWppZxdjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);
    }

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        UserInfo user= SessionUtil.getCurrentUser();
        String username= user!=null ? user.getUsername() :"";
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
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
                    if(CollectionUtils.isNotEmpty(tdQlids)) {
                        for(String tdQlid:tdQlids) {
                            if (StringUtils.isNotBlank(tdQlid)) {
                                GdDy tdGddy = entityMapper.selectByPrimaryKey(GdDy.class, tdQlid);
                                GdCf tdGdcf = entityMapper.selectByPrimaryKey(GdCf.class, tdQlid);
                                GdTdsyq tdGdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, tdQlid);
                                if (tdGddy != null) {
                                    tdGddy.setIsjy(1);
                                    tdGddy.setZxr(username);
                                    tdGddy.setZxrq(new Date(System.currentTimeMillis()));
                                    entityMapper.saveOrUpdate(tdGddy, tdGddy.getDyid());
                                } else if (tdGdcf != null) {
                                    tdGdcf.setIsjf(1);
                                    tdGdcf.setJfr(username);
                                    tdGdcf.setJfrq(new Date(System.currentTimeMillis()));
                                    entityMapper.saveOrUpdate(tdGdcf, tdGdcf.getCfid());
                                } else if (tdGdTdsyq != null) {
                                    tdGdTdsyq.setIszx(1);
                                    entityMapper.saveOrUpdate(tdGdTdsyq, tdGdTdsyq.getQlid());
                                }
                                gdFwService.changeGdqlztByQlid(tdQlid,Constants.QLLX_QSZT_XS.toString());
                            }
                        }
                    }
                    if (gddy != null) {
                        gddy.setIsjy(1);
                        gddy.setZxr(username);
                        gddy.setZxrq(new Date(System.currentTimeMillis()));
                        entityMapper.saveOrUpdate(gddy, gddy.getDyid());
                    } else if (gdcf != null) {
                        gdcf.setIsjf(1);
                        gdcf.setJfr(username);
                        gdcf.setJfrq(new Date(System.currentTimeMillis()));
                        entityMapper.saveOrUpdate(gdcf, gdcf.getCfid());
                    } else if (gdYg != null) {
                        gdYg.setIszx(1);
                        entityMapper.saveOrUpdate(gdYg, gdYg.getYgid());
                    } else if (gdFwsyq != null) {
                        gdFwsyq.setIszx(1);
                        entityMapper.saveOrUpdate(gdFwsyq, gdFwsyq.getQlid());
                    } else if(gdTdsyq != null){
                        gdTdsyq.setIszx(1);
                        entityMapper.saveOrUpdate(gdTdsyq, gdTdsyq.getQlid());
                    }
                    gdFwService.changeGdqlztByQlid(qlid, Constants.QLLX_QSZT_XS.toString());
                }
                //注销查封对解封登簿人和解封登记时间进行赋值
                if(StringUtils.isNotBlank(bdcXmRel.getYproid())){
                    Example example = new Example(BdcCf.class);
                    example.createCriteria().andEqualTo("proid",bdcXmRel.getYproid());
                    List<BdcCf> bdcCfList = entityMapper.selectByExample(example);
                    if(CollectionUtils.isNotEmpty(bdcCfList)){
                        BdcCf bdcCf = bdcCfList.get(0);
                        bdcCf.setJfdbr(username);
                        bdcCf.setJfdjsj(new Date(System.currentTimeMillis()));
                        entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                    }
                }
            }
        }
    }
}
