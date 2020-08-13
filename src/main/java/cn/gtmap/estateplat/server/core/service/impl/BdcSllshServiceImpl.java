package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcSllsh;
import cn.gtmap.estateplat.server.core.mapper.BdcSllshMapper;
import cn.gtmap.estateplat.server.core.service.BdcSllshService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 不动产受理流水号管理.
 *
 * @author zx
 * @version V1.0, 16-12-07
 * @description
 */
@Repository
public class BdcSllshServiceImpl implements BdcSllshService {
    private static final FastDateFormat SLBH_DATE_MM_FORMAT = FastDateFormat.getInstance("yyyyMM");
    /**
     * entity对象Mapper.
     *
     * @author liujie
     * @description entity对象Mapper.
     */
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSllshMapper bdcSllshMapper;

    @Override
    @Transactional
    public void saveBh(String slbhlsh) {
        if (StringUtils.isNotBlank(slbhlsh)) {
            Map map= Maps.newHashMap();
            map.put("cjsj",CalendarUtil.formatDateToString(new Date()));
            List<BdcSllsh> bdcSllshList=bdcSllshMapper.getBdcSllshList(map);
            BdcSllsh bdcSllsh=null;
            if (CollectionUtils.isNotEmpty(bdcSllshList)) {
                bdcSllsh=bdcSllshList.get(0);
                bdcSllsh.setSlbhlsh(slbhlsh);
            }else {
                bdcSllsh = new BdcSllsh();
                bdcSllsh.setBhid(UUIDGenerator.generate());
                bdcSllsh.setSlbhlsh(slbhlsh);
            }
            bdcSllsh.setCjsj(new Date());
            entityMapper.saveOrUpdate(bdcSllsh, bdcSllsh.getBhid());
        }
    }

    @Override
    @Transactional
    public void saveBh(String slbhlsh, String qh) {
        if (StringUtils.isNotBlank(slbhlsh) && StringUtils.isNotBlank(qh)) {
            Map map= Maps.newHashMap();
            map.put("cjyf", DateUtils.formatTime(new Date(),SLBH_DATE_MM_FORMAT));
            map.put("qh",qh);
            List<BdcSllsh> bdcSllshList=bdcSllshMapper.getBdcSllshList(map);
            BdcSllsh bdcSllsh=null;
            if (CollectionUtils.isNotEmpty(bdcSllshList)) {
                bdcSllsh=bdcSllshList.get(0);
                bdcSllsh.setSlbhlsh(slbhlsh);
            }else {
                bdcSllsh = new BdcSllsh();
                bdcSllsh.setBhid(UUIDGenerator.generate());
                bdcSllsh.setQh(qh);
                bdcSllsh.setSlbhlsh(slbhlsh);
            }
            bdcSllsh.setCjsj(new Date());
            entityMapper.saveOrUpdate(bdcSllsh, bdcSllsh.getBhid());
        }
    }

    @Override
    @Transactional
    public Integer getSlbhLsh() {
        Map map= Maps.newHashMap();
        map.put("cjsj",CalendarUtil.formatDateToString(new Date()));
        List<BdcSllsh> bdcSllshList=bdcSllshMapper.getBdcSllshList(map);
        if (CollectionUtils.isNotEmpty(bdcSllshList) && StringUtils.isNotBlank(bdcSllshList.get(0).getSlbhlsh()))
            return Integer.parseInt(bdcSllshList.get(0).getSlbhlsh());
        return null;
    }

    @Override
    @Transactional
    public Integer getSlbhLsh(Map map) {
        List<BdcSllsh> bdcSllshList=bdcSllshMapper.getBdcSllshList(map);
        if (CollectionUtils.isNotEmpty(bdcSllshList) && StringUtils.isNotBlank(bdcSllshList.get(0).getSlbhlsh()))
            return Integer.parseInt(bdcSllshList.get(0).getSlbhlsh());
        return null;
    }
}
