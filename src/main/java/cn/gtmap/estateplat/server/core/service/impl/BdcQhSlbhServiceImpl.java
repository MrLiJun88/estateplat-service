package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcLshService;
import cn.gtmap.estateplat.server.core.service.BdcSlbhBhService;
import cn.gtmap.estateplat.server.core.service.BdcSllshService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/4/12
 * @description  带区号的不动产项目受理编号服务
 */
@Service
public class BdcQhSlbhServiceImpl implements BdcSlbhBhService {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(BdcQhSlbhServiceImpl.class);
    private static final FastDateFormat SLBH_DATE_MM_FORMAT = FastDateFormat.getInstance("yyyyMM");
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcSllshService bdcSllshService;
    @Autowired

    BdcLshService bdcLshService;

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/31
     * @param bdcXm 不动产项目
     * @return 返回新生成的受理编号
     * @description 为不动产项目生成受理编号，并返回
     */
    @Override
    public synchronized String generateBdcXmSlbh(final BdcXm bdcXm) {
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            String slbh = null;
            if (bdcXm.getCjsj() == null)
                bdcXm.setCjsj(new Date());

            String qh = "";
            if (AppConfig.getBooleanProperty("xmbh.useMode.order", false))  {
                Map map= Maps.newHashMap();
                qh = bdcLshService.getQhByDwdm(bdcXm.getDwdm());
                if(StringUtils.isNotBlank(qh))
                    map.put("qh",qh);
                else
                    throw new AppException(4007);
                map.put("cjyf", DateUtils.formatTime(bdcXm.getCjsj(), SLBH_DATE_MM_FORMAT));
                final Integer maxLsh = bdcSllshService.getSlbhLsh(map);
                log.info(("获取受理号：" + maxLsh));
                final Integer lsh = (maxLsh == null ? 1 : (maxLsh + 1));
                bdcXm.setLsh(formatLsh(lsh));
                slbh = formatSlbh(bdcXm.getCjsj(), bdcXm.getLsh(),qh);
            } else {
                slbh = cn.gtmap.estateplat.utils.CommonUtil.getCurrentTimeMillisId();
            }

            if (isExistedSlbh(slbh, bdcXm.getWiid())) {
                log.info("存在：" + slbh);
                throw new AppException(4002);
            }

            bdcXm.setBh(slbh);
            Object newBdcXm = new BdcXm();
            try {
                BeanUtils.copyProperties(newBdcXm, bdcXm);
            } catch (Exception e) {
                throw new AppException(4002);
            }

            if (AppConfig.getBooleanProperty("xmbh.useMode.order", false)) {
                log.info("保存：" + bdcXm.getLsh());
                //保存受理编号
                bdcSllshService.saveBh(bdcXm.getLsh(),qh);
                log.info("保存成功：" + bdcXm.getLsh());
            }

            ((BdcXm) newBdcXm).setCjsj(new Date());

            return entityMapper.saveOrUpdate(newBdcXm, ((BdcXm) newBdcXm).getProid()) > 0 ? slbh : null;
        } else
            return null;
    }

    @Override
    public String getBdcXmSlbh(BdcXm bdcXm) {
        String slbh;
        if (AppConfig.getBooleanProperty("xmbh.useMode.order", false)) {
            Map map = Maps.newHashMap();
            String qh = bdcLshService.getQhByDwdm(bdcXm.getDwdm());
            if (StringUtils.isNotBlank(qh)) {
                map.put("qh", qh);
            } else {
                throw new AppException(4007);
            }
            map.put("cjyf", DateUtils.formatTime(bdcXm.getCjsj(), SLBH_DATE_MM_FORMAT));
            final Integer maxLsh = bdcSllshService.getSlbhLsh(map);
            log.info(("获取受理号：" + maxLsh));
            final Integer lsh = (maxLsh == null ? 1 : (maxLsh + 1));
            bdcXm.setLsh(formatLsh(lsh));
            slbh = formatSlbh(bdcXm.getCjsj(), bdcXm.getLsh(), qh);
        } else {
            slbh = cn.gtmap.estateplat.utils.CommonUtil.getCurrentTimeMillisId();
        }
        return slbh;
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/31
     * @param lsh 数值型流水号，位格式化
     * @return 格式化的流水号
     * @description 根据配置文件中的流水号配置项，生成符合格式要求的流水号字符串
     */
    private synchronized String formatLsh(final Integer lsh){
        String slbhPattern = AppConfig.getProperty("slbh.lsh.pattern");
        final DecimalFormat df;
        if(StringUtils.isNotBlank(slbhPattern)){
            df =  new DecimalFormat(slbhPattern);
        }else{
            df = new DecimalFormat("0000");
        }

        return df.format(lsh);
    }

    private synchronized String formatSlbh(final Date slsj,final String lsh,final String qh){
        String xmbhMb =  AppConfig.getProperty("xmbh.mb");
        if(StringUtils.isNotBlank(xmbhMb)) {
            return xmbhMb.replace("sj",DateUtils.formatTime(slsj, SLBH_DATE_MM_FORMAT)).replace("qh",qh).replace("lsh",lsh);
        }else{
            return DateUtils.formatTime(slsj, SLBH_DATE_MM_FORMAT) + qh + lsh;
        }
    }

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/31
     * @param slbh 受理编号
     * @param wiid 工作流实例Id
     * @return 受理编号是否存在
     * @description
     */
    private synchronized boolean isExistedSlbh(final String slbh,final String wiid){
        Example example = new Example(BdcXm.class);
        example.createCriteria().andEqualTo("bh",slbh);
        example.createCriteria().andNotEqualTo("wiid",wiid);
        final int result = entityMapper.countByExample(example);
        return result>0?true:false;
    }
}
