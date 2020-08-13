package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * 海门转移登记创建项目实现类（由于房屋系统正常用所以转移登记和林口有差别，做转移是过渡房屋权利人就是实际转移的权利人）
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-16
 */

public class CreatProjectHmZydjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }else
            throw new AppException(4005);
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if (bdcxm != null && StringUtils.isNotBlank(project.getYxmid()) && StringUtils.equals(bdcxm.getDjlx(),Constants.DJLX_DYDJ_DM)) {
            bdcXmService.getYBdcXmDjzx(project.getYxmid(), bdcxm);
        }
        list.add(bdcxm);

        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);
        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }

            // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
            BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
            if (bdcTd == null) {
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(),initVoFromParm.getProject(), bdcxm.getQllx());
                list.add(bdcTd);
            }
        }

        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null)
            list.add(bdcSpxx);
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);

            //sc根据不动产单元号获取预告项目，如果存在把权利人获取过来      预购商品房预告登记
            if (CommonUtil.indexOfStrs(Constants.SQLX_ZYDJ_DM, bdcxm.getSqlx())) {
                HashMap map = new HashMap();
                map.put("bdcdyh", bdcdy.getBdcdyh());
                for (int i = 0; i < Constants.SQLX_ZYDJ_DM.length; i++) {
                    if (StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_ZYDJ_DM[i])) {
                        map.put("sqlx", Constants.SQLX_YGZY_DM[i]);
                        break;
                    }
                }
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmByBdcdyAndSqlx(map);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    List<BdcQlr> ybdcDyQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmList.get(0).getProid());
                    if (CollectionUtils.isNotEmpty(ybdcDyQlrList)) {
                        List<BdcQlr> tempBdcQlrs = new ArrayList<BdcQlr>();
                        for (BdcQlr bdcQlr : ybdcDyQlrList) {
                            bdcQlr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcQlr, tempBdcQlrs, project.getProid());
                            tempBdcQlrs.add(bdcQlr);
                        }
                        list.addAll(tempBdcQlrs);
                    }
                }
            }
        }
        QllxVo qllxVo = qllxService.makeSureQllx(bdcxm);
        String zsFont = qllxService.makeSureBdcqzlx(qllxVo);
        String pplx = AppConfig.getProperty("sjpp.type");
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)&&StringUtils.equals(project.getBdclx(),Constants.BDCLX_TDFW)) {
            List<BdcQlr> bdcQlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(bdcQlrList))
                list.addAll(bdcQlrList);
        } else {

            List<BdcQlr> ybdcQlrList;
            //zdd 如果为证书  则将原权利人转为义务人  如果证明 则将义务人转为义务人
            if (StringUtils.isNotBlank(zsFont) && zsFont.equals(Constants.BDCQZS_BH_FONT)) {
                ybdcQlrList = getYbdcQlrList(project);
            } else {
                ybdcQlrList = getYbdcYwrList(project);
            }
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList)
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
            }
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                for (BdcQlr bdcQlr : ybdcQlrList) {
                    bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                    list.add(bdcQlr);
                }
            }
        }
        return list;
    }

}
