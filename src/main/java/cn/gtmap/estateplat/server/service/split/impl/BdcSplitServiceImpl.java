package cn.gtmap.estateplat.server.service.split.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcFglssj;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.split.BdcSplitDao;
import cn.gtmap.estateplat.server.service.split.BdcSplitDataService;
import cn.gtmap.estateplat.server.service.split.BdcSplitService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 拆分数据
 */
@Service
public class BdcSplitServiceImpl implements BdcSplitService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private List<BdcSplitDataService> bdcSplitDataServiceList;
    @Autowired
    private BdcSplitDao bdcSplitDao;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;


    /**
     * @param proid 项目ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据
     */
    @Override
    public Map bdcSplit(List<SplitNum> splitNumList, String proid) {
        Map map = Maps.newHashMap();
        String msg = ParamsConstants.SUCCESS_LOWERCASE;
        try {
            CommonUtil.sort(bdcSplitDataServiceList, "getSxh", null);
            List<BdcSplitData> ysjDataList = bdcSplitDao.initYsjDataList(proid);
            splitNumList = bdcSplitDao.initSplitNum(splitNumList, proid);
            List<BdcSplitData> bdcSplitDataList = Lists.newArrayList();
            String fgid = UUIDGenerator.generate18();
            if (CollectionUtils.isNotEmpty(ysjDataList) && CollectionUtils.isNotEmpty(splitNumList)) {
                int xh = 1;
                for (SplitNum splitNum : splitNumList) {
                    for (BdcSplitData ysjData : ysjDataList) {
                        BdcSplitData bdcSplitData = new BdcSplitData();
                        bdcSplitData.setXh(xh);
                        for (int i = 0; i < bdcSplitDataServiceList.size(); i++) {
                            bdcSplitDataServiceList.get(i).splitDate(ysjData, splitNum, bdcSplitData, fgid);
                        }
                        bdcSplitDataList.add(bdcSplitData);
                        xh++;
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(ysjDataList) && CollectionUtils.isNotEmpty(bdcSplitDataList)) {
                bdcSplitDao.bdcSplitData(ysjDataList, bdcSplitDataList, fgid);
                if (bdcSplitDataList.get(0) != null && bdcSplitDataList.get(0).getBdcXm() != null && StringUtils.isNotBlank(bdcSplitDataList.get(0).getBdcXm().getProid())) {
                    map.put("proid", bdcSplitDataList.get(0).getBdcXm().getProid());
                }
            }
        } catch (Exception e) {
            msg = e.getMessage();
            logger.error(e.getMessage());
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * @param fgid 分割ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 撤回拆分
     */
    @Override
    public String revokeBdcSplit(String fgid) {
        String msg = ParamsConstants.SUCCESS_LOWERCASE;
        try {
            List<BdcSplitData> ysjDataList = bdcSplitDao.getYszByFgid(fgid);
            List<BdcSplitData> fghDataList = bdcSplitDao.getCfhByFgid(fgid);
            bdcSplitDao.revokeBdcSplit(ysjDataList, fghDataList);
        } catch (Exception e) {
            msg = e.getMessage();
            logger.error(e.getMessage());
        }
        return msg;
    }

    @Override
    public String initTdSplit(Model model, String proid) {
        String path = "/wf/dataFghb/tdSplit";
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            model.addAttribute("bdcSpxx", bdcSpxx);
            model.addAttribute("proid", proid);
        }
        return path;
    }

    @Override
    public Map getFgidByproid(String proid) {
        Map map = Maps.newHashMap();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                map.put("fgid", bdcXm.getFgid());
            }
        }
        return map;
    }

    @Override
    public Map validateWithdrawFg(String fgid) {
        Map map = Maps.newHashMap();
        String msg = "success";
        if (StringUtils.isNotBlank(fgid)) {
            List<BdcFglssj> bdcFglssjList = bdcSplitDao.getBdcFglssjByFgid(fgid);
            if (CollectionUtils.isNotEmpty(bdcFglssjList) && bdcFglssjList.get(0).getFgcs() != null) {
                int fgcs = bdcFglssjList.get(0).getFgcs();
                Example Example = new Example(BdcXm.class);
                Example.createCriteria().andEqualTo("fgid", fgid);
                List<BdcXm> bdcXmList = entityMapper.selectByExample(Example);
                // 同一wiid下的项目中有比当前撤回项目fgcs更大的则存在子分割，不允许撤回
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    List<BdcXm> bdcXmListTemp = bdcXmService.getBdcXmListByWiid(bdcXmList.get(0).getWiid());
                    for (BdcXm bdcXm : bdcXmListTemp) {
                        List<BdcFglssj> bdcFglssjListTemp = bdcSplitDao.getBdcFglssjByFgid(bdcXm.getFgid());
                        if (CollectionUtils.isNotEmpty(bdcFglssjListTemp) && bdcFglssjListTemp.get(0) != null
                                && bdcFglssjListTemp.get(0).getFgcs() != null && fgcs < bdcFglssjListTemp.get(0).getFgcs()) {
                            msg = "fail";
                        }
                    }
                }
            }
        }
        map.put("msg", msg);
        return map;
    }
}
