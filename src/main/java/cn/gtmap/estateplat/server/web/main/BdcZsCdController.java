package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZsCd;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsCdService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2019/12/12
 * @description 不动产证书裁定
 */
@Controller
@RequestMapping("/bdcZsCd")
public class BdcZsCdController extends BaseController{
    @Autowired
    private Repo repository;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsCdService bdcZsCdService;

    /**
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 证书裁定列表
     */
    @RequestMapping(value = "")
    public String index(Model model,String bdcqzh) {
        model.addAttribute(ParamsConstants.BDCQZH_LOWERCASE, bdcqzh);
        return "main/bdcZsCdList";
    }

    /**
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取证书裁定信息
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcZsCdByPages")
    public Object getBdcZsCdByPages(Pageable pageable,String bdcdyh,String cqzh,String exactQuery) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(bdcdyh)) {
            map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
        }
        if (StringUtils.isNotBlank(cqzh)) {
            map.put(ParamsConstants.CQZH_LOWERCASE, StringUtils.deleteWhitespace(cqzh));
        }
        if(StringUtils.isNotBlank(exactQuery)) {
            map.put("exactQuery", exactQuery);
        }
        return repository.selectPaging("getBdcZsCdByPage", map, pageable);
    }

    /**
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取过渡土地数据
     */
    @ResponseBody
    @RequestMapping("/getGdTdJson")
    public Object getGdTdJson(int page, int rows,
                              @RequestParam(value = "iszx", required = false) Integer iszx,
                              @RequestParam(value = "hhSearch", required = false) String hhSearch,
                              @RequestParam(value = "tdid", required = false) String tdid,
                              @RequestParam(value = "qlid", required = false) String qlid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("hhSearch", StringUtils.deleteWhitespace(hhSearch));
        map.put("iszx", iszx);
        map.put("tdid", tdid);
        map.put("bdclx", Constants.BDCLX_TD);
        map.put("qlid", qlid);
        map.put("ppBdcdyh", ParamsConstants.TRUE_LOWERCASE);
        return repository.selectPaging("getGdTdByPage", map, page - 1, rows);
    }

    /**
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取过渡房屋数据
     */
    @ResponseBody
    @RequestMapping("/getGdFwJson")
    public Object getGdFwJson(int page, int rows,String hhSearch, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdQlids", required = false) String gdQlids, @RequestParam(value = "checkMulGdFw", required = false) String checkMulGdFw) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("hhSearch", StringUtils.deleteWhitespace(hhSearch));
        if (StringUtils.isNoneBlank(gdproid)) {
            String qlids = gdFwService.getGdFwQlidsByProid(gdproid);
            if (StringUtils.isNoneBlank(qlids) && qlids.split(",").length > 0) {
                map.put(ParamsConstants.QLIDS_LOWERCASE, qlids.split(","));
            } else {
                map.put(ParamsConstants.QLIDS_LOWERCASE, "1");
            }
        }
        if (StringUtils.equals(checkMulGdFw, "true")) {
            if (StringUtils.isNotBlank(gdQlids)) {
                map.put(ParamsConstants.QLIDS_LOWERCASE, gdQlids.split(","));
            } else {
                map.put(ParamsConstants.QLIDS_LOWERCASE, "1");
            }
        }
        map.put("ppBdcdyh", ParamsConstants.TRUE_LOWERCASE);
        return repository.selectPaging("getGdFwSdJsonByPage", map, page - 1, rows);
    }


    @ResponseBody
    @RequestMapping("/getBdczsListByPage")
    public Object getBdczsListByPage(Pageable pageable, String bdcdyh, String qllx, String bdcqzh, String qlr, String zl, String bdclxdm, String dcxc, String zdtzm
            , @RequestParam(value = "qlxzdm", required = false) String qlxzdm, @RequestParam(value = "dyfs", required = false) String dyfs
            , @RequestParam(value = "ysqlxdm", required = false) String ysqlxdm, @RequestParam(value = "proid", required = false) String proid, String bdcdyhs
            , String fzqssj, String fzjssj, String zstype) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String sqlx = bdcXm != null ? bdcXm.getSqlx() : "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(bdcqzh)) {
                map.put(ParamsConstants.BDCQZH_LOWERCASE, bdcqzh);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
        }
        if (StringUtils.isNotBlank(bdclxdm)) {
            map.put("bdclxdm", bdclxdm.split(","));
        }
        /**
         * @author jiangganzhi
         * @description 异议登记不根据权利类型过滤 否则无法搜出证明
         */
        map.put("qllx", Constants.QLLX_CQ.split(","));
        if (StringUtils.isNotBlank(zdtzm)) {
            map.put("zdtzm", zdtzm.split(","));
        }
        if (StringUtils.isNotBlank(dyfs)) {
            map.put("dyfs", dyfs);
        }
        if (StringUtils.isNotBlank(qlxzdm)) {
            map.put("qlxzdm", qlxzdm.split(","));
        }
        if (StringUtils.isNotBlank(ysqlxdm)) {
            map.put("ysqlxdm", ysqlxdm.split(","));
        }

        if (StringUtils.isNotBlank(zstype)) {
            if (StringUtils.equals(zstype, Constants.BDCQZS_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZS_BH_FONT);
            } else if (StringUtils.equals(zstype, Constants.BDCQZM_BH_DM)) {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, Constants.BDCQZM_BH_FONT);
            } else {
                map.put(ParamsConstants.ZSTYPE_LOWERCASE, "不动产权证明单");
            }
        }
        if (StringUtils.isNotBlank(sqlx) && (Constants.SQLX_SPFSCKFSZC_DM.equals(sqlx) || Constants.SQLX_SPFXZBG_DM.equals(sqlx))) {
            zstype = AppConfig.getProperty("spfscdj.zstype");
            map.put(ParamsConstants.ZSTYPE_LOWERCASE, zstype);
        }
        //如果不是商品房买卖转移登记和商品房转移登记，首次登记信息表变更登记，商品房首次开发商自持要求不能搜到首次登记证
        if (StringUtils.isNotBlank(sqlx) && !StringUtils.equals(sqlx, Constants.SQLX_SPFMMZYDJ_DM) && !StringUtils.equals(sqlx, Constants.SQLX_CLF) && !StringUtils.equals(sqlx, Constants.SQLX_SPFSCKFSZC_DM) && !StringUtils.equals(sqlx, Constants.SQLX_SPFXZBG_DM) && !StringUtils.equals(sqlx, Constants.SQLX_PLCF) && !StringUtils.equals(sqlx, Constants.SQLX_CF)) {
            map.put("disSpfsc", "true");
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotBlank(fzqssj)) {
                map.put("fzqssj", simpleDateFormat.parse(fzqssj));
            }
            if (StringUtils.isNotBlank(fzjssj)) {
                map.put("fzjssj", simpleDateFormat.parse(fzjssj));
            }
        } catch (ParseException e) {
            logger.error("BdcSjSdController.getBdczsListByPage", e);
        }
        if (StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for (String tempBdcdyh : bdcdyhs.split(",")) {
                if (StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put("bdcdyhs", bdcdyhList);
        }
        //sc 需要展示不生成证书的权力信息
        map.put("filterNullBdcqzh", true);
        /**
         * @author bianwen
         * @description 将权利已注销的证书过滤掉
         */
        map.put("qszt", Constants.QLLX_QSZT_HR);

        /**
         * @author bianwen
         * @description 在建工程选择不动产单元页面显示一条信息
         */
        return repository.selectPaging("getBdcSdZsByPage", map, pageable);
    }


    @ResponseBody
    @RequestMapping(value = "/addBdcZsCd", method = RequestMethod.POST)
    public Map addBdcqzSd(@RequestParam(value = "bdcdyh", required = false) String bdcdyh,
                          @RequestParam(value = "cqzh", required = false) String cqzh,
                          @RequestParam(value = "cdyy", required = false) String cdyy,
                          @RequestParam(value = "proid", required = false) String proid) {
        BdcZsCd bdcZsCd = new BdcZsCd();
        bdcZsCd.setBdcdyh(bdcdyh);
        bdcZsCd.setCqzh(cqzh);
        bdcZsCd.setCdyy(cdyy);
        bdcZsCd.setProid(proid);
        bdcZsCd.setCdjbr(super.getUserName());
        Map resultMap = bdcZsCdService.addBdcZsCd(bdcZsCd);
        return resultMap;
    }
}
