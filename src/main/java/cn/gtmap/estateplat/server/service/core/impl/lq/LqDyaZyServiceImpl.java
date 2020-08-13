package cn.gtmap.estateplat.server.service.core.impl.lq;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/11/2
 * @description
 */
@Service
public class LqDyaZyServiceImpl extends LqdjService{

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcDyaqService bdcDyaqService;


    @Override
    public void initializeProject(Xmxx xmxx) {
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project;
        String pplx = AppConfig.getProperty("sjpp.type");
        List<BdcQlr> ybdcYwrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcYwrList = getYbdcYwrList(project);
        } else
            throw new AppException(4005);
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        //初始化不动产登记簿 bdcdjb
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(project);
                list.add(bdcdjb);
            } else
                bdcdjb = bdcdjbTemps.get(0);
        }
        // 初始化不动产土地属性信息 bdcTd
        BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
        if (bdcTd == null) {
            list.add(intBdcTd(getDjsjNydDcbByBdcdyh(project.getBdcdyh()), project, bdcxm.getQllx()));
        }
        //初始化审批表信息 bdcSpxx
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(project, bdcSpxx);
        if (bdcSpxx != null)
            list.add(bdcSpxx);
        //初始化不动产单元信息 bdcdy
        BdcBdcdy bdcdy = initBdcdy(project, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
        }
        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);
        //过程匹配，则权利人和义务人都不需要改变
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
            List<BdcQlr> qlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(qlrList))
                list.addAll(qlrList);
        } else {
            //不是过渡数据执行
            //lx 林权抵押权转移登记不带原抵押权人，带义务人
            if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
                bdcQlrService.delBdcQlrByProid(project.getProid(),Constants.QLRLX_YWR);
                for (BdcQlr bdcYwr : ybdcYwrList) {
                    bdcYwr.setQlrid(UUIDGenerator.generate18());
                    bdcYwr.setProid(project.getProid());
                }
                list.addAll(ybdcYwrList);
            }
        }
        if (CollectionUtils.isNotEmpty(list))
            saveOrUpdateProjectDate(list);
        //初始化权利信息
        if (bdcxm != null)
            saveQllx(bdcxm);
    }

    @Override
    public void updateProjectQszt(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                changeYqszt(bdcXm);
            }
        }
    }


    @Override
    public void deleteProjectQlxx(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                String proid = bdcXm.getProid();
                //删除义务人证书关系信息表以及义务人信息
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
                if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                    for (BdcQlr bdcYwr : bdcYwrList) {
                        bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcYwr.getQlrid());
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid());
                    }
                }
                //删除权利类型信息
                Example example = new Example(BdcDyaq.class);
                example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
                entityMapper.deleteByExample(BdcDyaq.class, example);
                //删除项目的时候清楚唯一的不动产单元数据
                bdcdyService.delDjbAndTd(bdcXm);
            }
        }
    }

    @Override
    public void revertYqlxx(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                List<BdcXmRel> bdcXmRelList = null;
                if (bdcXm != null)
                    bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        //还原过渡权属状态
                        revertGdsjQszt(bdcXmRel, 0);
                        //还原上一手项目权属状态
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                        }
                    }
                }
            }
        }
    }

    private void saveQllx(BdcXm bdcXm) {
        BdcDyaq bdcDyaq = new BdcDyaq();
        HashMap map = new HashMap();
        map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
        List<BdcDyaq> dyaqList = bdcDyaqService.queryBdcDyaq(map);
        BdcDyaq bdcDyaqTemp = null;
        if (CollectionUtils.isNotEmpty(dyaqList))
            bdcDyaqTemp = dyaqList.get(0);
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        BdcXmRel bdcXmRel = null;
        if (CollectionUtils.isNotEmpty(bdcXmRelList))
            bdcXmRel = bdcXmRelList.get(0);
        if (StringUtils.isBlank(bdcDyaq.getQlid()))
            bdcDyaq.setQlid(UUIDGenerator.generate18());
        qllxService.getQllxParentFrom(bdcDyaq, bdcXm);
        if (bdcXmRel != null&&StringUtils.isNotBlank(bdcXmRel.getYproid())) {
            //读取上一手业务权利
            Example example = new Example(BdcDyaq.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXmRel.getYproid());
            List<BdcDyaq> yBdcDyaqList = entityMapper.selectByExample(example);
            BdcDyaq yBdcDyaq = yBdcDyaqList.get(0);
            bdcDyaq = readFromYdyaq(yBdcDyaq,bdcDyaq);
        }
        bdcDyaq.setQszt(Constants.QLLX_QSZT_LS);
        if (bdcDyaqTemp == null) {
            entityMapper.insertSelective(bdcDyaq);
        } else {
            bdcDyaq.setQlid(bdcDyaqTemp.getQlid());
            entityMapper.updateByPrimaryKeySelective(bdcDyaq);
        }
    }

    private BdcDyaq readFromYdyaq(BdcDyaq yBdcDyaq, BdcDyaq bdcDyaq) {
        if(StringUtils.isNotBlank(bdcDyaq.getProid())) {
            yBdcDyaq.setProid(bdcDyaq.getProid());
        }
        if(StringUtils.isNotBlank(bdcDyaq.getQlid())) {
            yBdcDyaq.setQlid(bdcDyaq.getQlid());
        }
        if(StringUtils.isNotBlank(bdcDyaq.getYwh())) {
            yBdcDyaq.setYwh(bdcDyaq.getYwh());
        }
        yBdcDyaq.setDbr(null);
        yBdcDyaq.setDjsj(null);
        return yBdcDyaq;
    }


    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param
    * @return
    * @description 改变上一首业务状态
    */
    private void changeYqszt(BdcXm bdcXm) {
        //抵押登记不需要修改原权利状态    如果为老数据   注销过渡数据的权利状态
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            changeYgQszt(bdcXm);
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx())) {
                    changeGdsjQszt(bdcXmRel, 1);
                }
                if(StringUtils.isNotBlank(bdcXmRel.getProid())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(),Constants.QLLX_QSZT_HR);
                }
            }
        }
    }
}
