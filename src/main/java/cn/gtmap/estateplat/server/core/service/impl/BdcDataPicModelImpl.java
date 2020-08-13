package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.service.BdcDataPicModel;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.vo.PfBusinessVo;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/9/26
 * @description
 */
@Service
public class BdcDataPicModelImpl implements BdcDataPicModel {
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Override
    public void initBdcDataPicModel(Model model) {
        List<PfBusinessVo> pfBusinessVoList = sysWorkFlowDefineService.getBusinessList();
        List<Map> djlxList = bdcZdGlService.getDjlxByBdclx(Constants.BDCLX_TDFW);
        List<Map> sqlxList = bdcZdGlService.getSqlxByBdclxDjlx(Constants.BDCLX_TDFW, Constants.DJLX_CSDJ_DM);
        String gdTabOrder = AppConfig.getProperty("gdTab.order");
        List<String> gdTabOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(gdTabOrder) && gdTabOrder.split(",").length > 0) {
            for(String gdTab : gdTabOrder.split(",")) {
                gdTabOrderList.add(gdTab);
            }
        }
        //xc 匹配界面注销按钮配置是否可见
        //xc 土地，房屋分开配置
        String zxVisible = "";
        String zxTdVisible = "";
        String[] zxVisibleRoleNameArr = null;
        String[] zxTdVisibleRoleNameArr = null;
        String zxVisibleRoleNames = AppConfig.getProperty("zxVisibleRoleNames");
        String zxVisibleTdRoleNames = AppConfig.getProperty("zxVisibleTdRoleNames");
        String currUserId = SessionUtil.getCurrentUserId();
        List<PfRoleVo> pfRoleVoList = sysUserService.getRoleListByUser(currUserId);
        if(StringUtils.isNotBlank(zxVisibleRoleNames) && StringUtils.isNotBlank(zxVisibleTdRoleNames)) {
            zxVisibleRoleNameArr = zxVisibleRoleNames.split(",");
            if(StringUtils.isNotBlank(zxVisibleTdRoleNames))
                zxTdVisibleRoleNameArr = zxVisibleTdRoleNames.split(",");
            if(CollectionUtils.isNotEmpty(pfRoleVoList)) {
                for(PfRoleVo pfRoleVo : pfRoleVoList) {
                    if(CommonUtil.indexOfStrs(zxVisibleRoleNameArr, pfRoleVo.getRoleName())) {
                        zxVisible = "true";
                    }
                    if(CommonUtil.indexOfStrs(zxTdVisibleRoleNameArr, pfRoleVo.getRoleName())) {
                        zxTdVisible = "true";
                    }
                }
            }
        }
        model.addAttribute("zxVisible", zxVisible);
        model.addAttribute("zxTdVisible", zxTdVisible);
        List<String> sqlxdmList = ReadXmlProps.getUnPpGdfwtdSqlxDm();
        List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
        bppSqlxdmList.add(Constants.SQLX_FWCF_DM);
        bppSqlxdmList.add(Constants.SQLX_TDCF_DM_NEW);
        String userName = SessionUtil.getCurrentUser().getUsername();
        String wfids = bdcZdGlService.getWdidsBySqlxdm(sqlxdmList);
        String bppwfids = bdcZdGlService.getWdidsBySqlxdm(bppSqlxdmList);
        String gdTabLoadData = AppConfig.getProperty("gdTab.loadData");
        String sysVersion = AppConfig.getProperty("sys.version");
        //jiangganzhi 匹配界面是否只显示数据匹配功能
        String onlyShowDatePic = AppConfig.getProperty("onlyShowDatePic");
        model.addAttribute("onlyShowDatePic", onlyShowDatePic);
        model.addAttribute("sysVersion", sysVersion);
        model.addAttribute("gdTabOrderList", gdTabOrderList);
        model.addAttribute("gdTabOrder", gdTabOrder);
        model.addAttribute("gdTabLoadData", gdTabLoadData);
        model.addAttribute("sqlxList", sqlxList);
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("bppwfids", bppwfids);
        model.addAttribute("wfids", wfids);
        model.addAttribute("userName", userName);
        model.addAttribute("pfBusinessVoList", pfBusinessVoList);
    }
}
