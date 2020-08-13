package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXtLimitfieldService;
import cn.gtmap.estateplat.server.core.service.BdcXtOpinionService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 */
@Repository
public class BdcXtOpinionServiceImpl implements BdcXtOpinionService{
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXtLimitfieldService bdcXtLimitfieldService;
    @Override
    public String getConfigOpinion(String proid,BdcXtOpinion bdcXtOpinion){
        String opinion = "";
        opinion = replaceMb(bdcXtOpinion.getContent(),bdcXtOpinion.getContentdb(), proid) + "\n";
        return opinion;
    }

    @Override
    public String replaceMb(String mb, String sql, String proid) {
        String newMb = "";
        if (StringUtils.isNotBlank(mb) && StringUtils.isNotBlank(sql)) {
            String[] dbs = sql.split("；|;");
            //将数据源用;隔开成多条语句
            for (int i = 0; i < dbs.length; i++) {
                //不分大小写 替换项目id
                List<Map> list = null;
                if(StringUtils.isNotBlank(dbs[i].replaceAll("(?i)@proid", "'" +proid + "'")))
                    list = bdcXtLimitfieldService.runSql(dbs[i].replaceAll("(?i)@proid", "'" +proid + "'"));
                if (list != null && list.size() > 0) {
                    Map map = list.get(0);
                    Iterator j = list.get(0).keySet().iterator();
                    while (j.hasNext()) {
                        Object o = j.next();
                        String key = o.toString();
                        if (map.get(key) != null) {
                            //对时间和数值的一些处理
                            if (map.get(key) instanceof Date)
                                mb = mb.replaceAll("(?i)@" + key, CalendarUtil.sdf_China.format((Date) map.get(key)));
                            else if (map.get(key) instanceof BigDecimal)
                                //mb = mb.replaceAll("(?i)@" + key, CommonUtil.formatTwoNumber((((BigDecimal) map.get(key))).doubleValue()).toString());
                                mb = mb.replaceAll("(?i)@" + key, map.get(key).toString());
                            else
                                //不分大小写
                                mb = mb.replaceAll("(?i)@" + key, map.get(key).toString());
                        }
                    }
                }
            }
            newMb = mb;
        }
        return newMb;
    }

    @Override
    public String getDefautConfigOpinion(String wdid, String activityName, String proid) {
        String option = "";
        Example example = new Example(BdcXtOpinion.class);
        if (StringUtils.isNotBlank(wdid) && StringUtils.isNotBlank(activityName))
            example.createCriteria().andEqualTo("workflowid", wdid).andEqualTo("activitytype", activityName);
        else if (StringUtils.isNotBlank(wdid))
            example.createCriteria().andEqualTo("workflowid", wdid);
        if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
            List<BdcXtOpinion> bdcXtOpinionList = entityMapper.selectByExample(example);
            if (bdcXtOpinionList != null && bdcXtOpinionList.size() > 0){
                option = getConfigOpinion(proid, bdcXtOpinionList.get(0));
                if(StringUtils.isBlank(option)){
                    option = bdcXtOpinionList.get(0).getContent();
                }
            }
        }
        return option;
    }
}
