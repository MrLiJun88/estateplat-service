package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcFdcqService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sc
 * Date: 15-4-15
 * Time: 上午10:41
 * To change this template use File | Settings | File Templates.
 * dos:定位功能
 */
@Controller
@RequestMapping("/bdzDyMap")
public class BdzDyMapContorller extends BaseController {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcFdcqService bdcFdcqService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    DjxxMapper djxxMapper;

    public static final String CONFIGURATION_PARAMETER_BDCCYDW_TPL = "bdcCydw.tpl";
    public static final String CONFIGURATION_PARAMETER_BDCFWDW_TPL = "bdcFwdw.tpl";
    public static final String CONFIGURATION_PARAMETER_BDCLQDW_TPL = "bdcLqdw.tpl";
    public static final String CONFIGURATION_PARAMETER_BDCZDDW_TPL = "bdcZddw.tpl";
    public static final String CONFIGURATION_PARAMETER_BDCZHDW_TPL = "bdcZhdw.tpl";
    public static final String CONFIGURATION_PARAMETER_BDCFWDW_LAYERALIAS = "bdcFwdw.layerAlias";
    public static final String CONFIGURATION_PARAMETER_BDCZDDW_LAYERALIAS = "bdcZddw.layerAlias";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView indexBdcBdcqz(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdclxdm", required = false) String bdclxdm, HttpServletResponse response) throws Exception {
        StringBuilder url = new StringBuilder();
        String ompUrl = PlatformUtil.initOptProperties("${omp.url}") + "/map";
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(proid)) {
            map.put("proid", proid);
        }
        else if (StringUtils.isNotBlank(bdcdyh)) {
            map.put("bdcdyh", bdcdyh);
        }
        if (StringUtils.isBlank(bdclxdm)) {
            bdclxdm = bdcXmService.getXmLx(map);
        }
        String zdh = "";
        String xzqdm = "";
        BdcXm bdcXm = null;
        if (StringUtils.isBlank(bdcdyh) && StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                bdcdyh = bdcBdcdy.getBdcdyh();
            }
        }

        if (StringUtils.isNotBlank(bdcdyh)) {
            zdh = StringUtils.substring(bdcdyh, 0, 19);
            xzqdm = StringUtils.substring(bdcdyh, 0, 6);
        } else if (StringUtils.isNotBlank(proid)) {
            zdh = bdcdyService.getZhhByProid(proid);
            xzqdm = StringUtils.substring(zdh, 0, 6);
        }
        if (StringUtils.equals(Constants.BDCLX_TDFW, bdclxdm)) {
            List<String> zrddyList = null;
            String fwdwTpl = AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_TPL);
            if (StringUtils.isNotBlank(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_TPL)) && StringUtils.indexOf(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_TPL), ParamsConstants.XZQDM_LOWERCASE) > -1) {
                fwdwTpl = StringUtils.replace(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_TPL), ParamsConstants.XZQDM_LOWERCASE, xzqdm);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                zrddyList = new ArrayList<String>();
                List<String> lszds = new ArrayList<String>();

                //多个不动产单元定位
                if (StringUtils.isNotBlank(bdcdyh)) {
                    zrddyList.addAll(djxxMapper.getZrzhByBdcdyh(bdcdyh));
                    if (!lszds.contains(StringUtils.substring(bdcdyh, 0, 19))) {
                        lszds.add(StringUtils.substring(bdcdyh, 0, 19));
                    }
                }
                if (CollectionUtils.isNotEmpty(zrddyList)) {
                    url.append(ompUrl).append("/").append(StringUtils.trim(fwdwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").
                            append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22ZRZH%20in%20(");
                    for (int i = 0; i < zrddyList.size(); i++) {
                        String zrddy = zrddyList.get(i);
                        if (i < zrddyList.size() - 1) {
                            url.append("%27").append(zrddy).append("%27,");
                        }
                        else {
                            url.append("%27").append(zrddy).append("%27");
                        }
                    }
                    url.append(")%20and%20LSZD%20in%20(");
                    for (int i = 0; i < lszds.size(); i++) {
                        String lszd = lszds.get(i);
                        if (i < zrddyList.size() - 1) {
                            url.append("%27").append(lszd).append("%27,");
                        }
                        else {
                            url.append("%27").append(lszd).append("%27");
                        }
                    }
                    url.append(")%20%22,%22showInfo%22:true}}");
                } else {
                    url.append(ompUrl).append("/").append(StringUtils.trim(fwdwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22ZRZH=''%22,%22showInfo%22:true}}");
                }
            } else {
                if (StringUtils.isNotBlank(bdcdyh)) {
                    zrddyList = djxxMapper.getZrzhByBdcdyh(bdcdyh);
                }
                if (CollectionUtils.isNotEmpty(zrddyList)) {
                    url.append(ompUrl).append("/").append(StringUtils.trim(fwdwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22ZRZH%20in%20(");
                    for (int i = 0; i < zrddyList.size(); i++) {
                        String zrddy = zrddyList.get(i);
                        if (i < zrddyList.size() - 1) {
                            url.append("%27").append(zrddy).append("%27,");
                        }
                        else {
                            url.append("%27").append(zrddy).append("%27");
                        }
                    }
                    url.append(")%20and%20LSZD=%27").append(zdh).append("%27%22,%22showInfo%22:true}}");
                } else {
                    url.append(ompUrl).append("/").append(StringUtils.trim(fwdwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCFWDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22ZRZH=''%22,%22showInfo%22:true}}");
                }
            }

        } else if (StringUtils.equals(Constants.BDCLX_TD, bdclxdm)) {
            String zddwTpl = AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL);
            if (StringUtils.isNotBlank(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL)) && StringUtils.indexOf(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL), ParamsConstants.XZQDM_LOWERCASE) > -1) {
                zddwTpl = StringUtils.replace(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL), ParamsConstants.XZQDM_LOWERCASE, xzqdm);
            }
            if (StringUtils.isNotBlank(zdh)) {
                url.append(ompUrl).append("/").append(StringUtils.trim(zddwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22DJH=%27").append("%27%22,%22showInfo%22:true}}");
            } else{
                url.append(ompUrl).append("/").append(StringUtils.trim(zddwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22DJH=%27%27%22,%22showInfo%22:true}}");
            }
        } else if (StringUtils.equals(Constants.BDCLX_LQ, bdclxdm)) {
            String lqdwTpl = AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCLQDW_TPL);
            if (StringUtils.isNotBlank(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCLQDW_TPL)) && StringUtils.indexOf(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCLQDW_TPL), ParamsConstants.XZQDM_LOWERCASE) > -1) {
                lqdwTpl = StringUtils.replace(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCLQDW_TPL), ParamsConstants.XZQDM_LOWERCASE, xzqdm);
            }
            url.append(ompUrl).append( "/").append(StringUtils.trim(lqdwTpl)).append( "?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty("bdcLqdw.layerAlias"), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22DJH=%27").append("%27%22,%22showInfo%22:true}}");
        } else if (StringUtils.equals(Constants.BDCLX_QT, bdclxdm)) {     //承包定位服务暂无
            String cqdwTpl = AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCCYDW_TPL);
            if (StringUtils.isNotBlank(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCCYDW_TPL)) && StringUtils.indexOf(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCCYDW_TPL), ParamsConstants.XZQDM_LOWERCASE) > -1) {
                cqdwTpl = StringUtils.replace(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCCYDW_TPL), ParamsConstants.XZQDM_LOWERCASE, xzqdm);
            }
            url.append(ompUrl).append("/").append(StringUtils.trim(cqdwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty("bdcCydw.layerAlias"), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22DJH=%27").append("%27%22,%22showInfo%22:true}}");
        } else if (StringUtils.equals(Constants.BDCLX_HY, bdclxdm)) {
            String zhdwTpl = AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZHDW_TPL);
            if (StringUtils.isNotBlank(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZHDW_TPL)) && StringUtils.indexOf(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZHDW_TPL), ParamsConstants.XZQDM_LOWERCASE) > -1) {
                zhdwTpl = StringUtils.replace(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZHDW_TPL), ParamsConstants.XZQDM_LOWERCASE, xzqdm);
            }
            url.append(ompUrl).append("/").append(StringUtils.trim(zhdwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty("bdcZhdw.layerAlias"), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22ZHDM=%27").append( "%27%22,%22showInfo%22:true}}");
        } else {
            String zddwTpl = AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL);
            if (StringUtils.isNotBlank(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL)) && StringUtils.indexOf(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL), ParamsConstants.XZQDM_LOWERCASE) > -1) {
                zddwTpl = StringUtils.replace(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_TPL), ParamsConstants.XZQDM_LOWERCASE, xzqdm);
            }
            //zdd 默认定位地籍号
            if (StringUtils.isNotBlank(zdh)) {
                url.append(ompUrl).append("/").append(StringUtils.trim(zddwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22DJH=%27").append("%27%22,%22showInfo%22:true}}");
            } else {
                url.append(ompUrl).append("/").append(StringUtils.trim(zddwTpl)).append("?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22").append(URLEncoder.encode(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCZDDW_LAYERALIAS), Constants.DEFAULT_CHARSET)).append("%22,%22where%22:%22DJH=%27%27%22,%22showInfo%22:true}}");
            }

        }
        response.sendRedirect(url.toString());
        return null;
    }
}
