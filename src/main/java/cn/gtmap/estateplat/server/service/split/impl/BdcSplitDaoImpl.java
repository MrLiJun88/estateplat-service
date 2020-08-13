package cn.gtmap.estateplat.server.service.split.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.impl.DelProjectDefaultServiceImpl;
import cn.gtmap.estateplat.server.service.split.BdcSplitDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 拆分数据数据层
 */
@Service
public class BdcSplitDaoImpl implements BdcSplitDao {
    @Autowired
    private DelProjectDefaultServiceImpl delProjectDefaultServiceImpl;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    @Resource(name = "creatProjectDefaultService")
    private CreatProjectService creatProjectService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcSjxxService bdcSjxxService;
    @Autowired
    private BdcSjclService bdcSjclService;
    @Autowired
    private BdcSfxxService bdcSfxxService;
    @Autowired
    private BdcSfxmService bdcSfxmService;
    @Autowired
    private BdcGdxxService bdcGdxxService;
    @Autowired
    private QllxService qllxService;

    /**
     * @param ysjList
     * @param bdcSplitDataList
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据，数据层
     */
    @Override
    @Transactional
    public Integer bdcSplitData(List<BdcSplitData> ysjList, List<BdcSplitData> bdcSplitDataList, String fgid) {
        insertYsz(ysjList, fgid);
        deleteYsz(ysjList);
        insertXsz(bdcSplitDataList);
        return null;
    }

    /**
     * @param ysjList
     * @param bdcSplitDataList
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 撤回分割
     */
    @Override
    @Transactional
    public void revokeBdcSplit(List<BdcSplitData> ysjList, List<BdcSplitData> bdcSplitDataList) {
        deleteYsz(bdcSplitDataList);
        insertXsz(ysjList);
    }

    /**
     * @param bdcSplitDataList 不动产拆分对象
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 删除原数据
     */
    @Override
    public void deleteYsz(List<BdcSplitData> bdcSplitDataList) {
        if (CollectionUtils.isNotEmpty(bdcSplitDataList)) {
            for (BdcSplitData bdcSplitData : bdcSplitDataList) {
                if (bdcSplitData != null && bdcSplitData.getBdcXm() != null && StringUtils.isNotBlank(bdcSplitData.getBdcXm().getProid())) {
                    delProjectDefaultServiceImpl.delBdcBdxx(bdcSplitData.getBdcXm());
                    delProjectDefaultServiceImpl.delBdcXm(bdcSplitData.getBdcXm().getProid());
                }
            }
        }
    }

    /**
     * @param bdcSplitDataList 不动产拆分对象
     * @param fgid
     * @return 新增次数
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 以Json格式插入原数据
     */
    @Override
    public Integer insertYsz(List<BdcSplitData> bdcSplitDataList, String fgid) {
        int count = 0;
        int fgcs = 1;
        if (CollectionUtils.isNotEmpty(bdcSplitDataList)) {
            // 查询被分割数据是否已经进行过分割，计算分割次数
            if (bdcSplitDataList.get(0).getBdcXm() != null && StringUtils.isNotBlank(bdcSplitDataList.get(0).getBdcXm().getFgid())) {
                List<BdcFglssj> bdcFglssjList = getBdcFglssjByFgid(bdcSplitDataList.get(0).getBdcXm().getFgid());
                if (CollectionUtils.isNotEmpty(bdcFglssjList)) {
                    BdcFglssj bdcFglssj = bdcFglssjList.get(0);
                    if (bdcFglssj != null && bdcFglssj.getFgcs() != null) {
                        fgcs = bdcFglssj.getFgcs() + 1;
                    }
                }
            }
            for (BdcSplitData bdcSplitData : bdcSplitDataList) {
                BdcFglssj bdcFglssj = new BdcFglssj();
                bdcFglssj.setId(UUIDGenerator.generate18());
                bdcFglssj.setFgid(fgid);
                bdcFglssj.setSj(JSON.toJSONString(bdcSplitData));
                bdcFglssj.setFgcs(fgcs);
                count += entityMapper.insertSelective(bdcFglssj);
            }
        }
        return count;
    }

    /**
     * @param bdcSplitDataList 不动产拆分对象
     * @return 新增次数
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 插入新数据
     */
    @Override
    public Integer insertXsz(List<BdcSplitData> bdcSplitDataList) {
        int count = 0;
        List<InsertVo> insertVoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(bdcSplitDataList)) {
            for (BdcSplitData bdcSplitData : bdcSplitDataList) {
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcXmzsRelList())) {
                    count += entityMapper.insertBatchSelective(bdcSplitData.getBdcXmzsRelList());
                }
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcZsQlrRelList())) {
                    count += entityMapper.insertBatchSelective(bdcSplitData.getBdcZsQlrRelList());
                }
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcZsList())) {
                    count += entityMapper.insertBatchSelective(bdcSplitData.getBdcZsList());
                }
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcSjclList())) {
                    count += entityMapper.insertBatchSelective(bdcSplitData.getBdcSjclList());
                }
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcSfxmList())) {
                    count += entityMapper.insertBatchSelective(bdcSplitData.getBdcSfxmList());
                }
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcFwfzxxList())) {
                    count += entityMapper.insertBatchSelective(bdcSplitData.getBdcFwfzxxList());
                }
                if (bdcSplitData.getBdcSfxx() != null) {
                    count += entityMapper.insertSelective(bdcSplitData.getBdcSfxx());
                }
                if (bdcSplitData.getBdcGdxx() != null) {
                    count += entityMapper.insertSelective(bdcSplitData.getBdcGdxx());
                }
                if (bdcSplitData.getBdcBdcdySd() != null) {
                    count += entityMapper.insertSelective(bdcSplitData.getBdcBdcdySd());
                }
                insertVoList.add(bdcSplitData.getBdcXm());
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcXmRelList())) {
                    insertVoList.addAll(bdcSplitData.getBdcXmRelList());
                }
                if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcQlrList())) {
                    insertVoList.addAll(bdcSplitData.getBdcQlrList());
                }
                insertVoList.add(bdcSplitData.getBdcSpxx());
                insertVoList.add(bdcSplitData.getBdcBdcdy());
                insertVoList.add(bdcSplitData.getBdcSjxx());
                insertVoList.add(bdcSplitData.getQllxVo());
            }
        }
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            creatProjectService.insertProjectData(insertVoList);
        }
        return count;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 初始化分割前所有数据
     */
    @Override
    public List<BdcSplitData> initYsjDataList(String proid) {
        List<BdcSplitData> ysjDataList = new ArrayList<>();
        // 原产权数据
        List<BdcSplitData> ycqDataList = initBdcSplitData(proid);
        // 原限制权利数据
        StringBuilder xzqlProids = new StringBuilder();
        if (CollectionUtils.isNotEmpty(ycqDataList) && ycqDataList.get(0) != null
                && ycqDataList.get(0).getBdcBdcdy() != null && StringUtils.isNotBlank(ycqDataList.get(0).getBdcBdcdy().getBdcdyh())) {
            // 不动产单元锁定
            List<BdcBdcdySd> bdcBdcdySdList = bdcdyService.queryBdcdySdByBdcdyh(ycqDataList.get(0).getBdcBdcdy().getBdcdyh());
            if (CollectionUtils.isNotEmpty(bdcBdcdySdList)) {
                ycqDataList.get(0).setBdcBdcdySd(bdcBdcdySdList.get(0));
            }
            ysjDataList.addAll(ycqDataList);
            // 查封、抵押、异议、预告
            xzqlProids = initXzqlProidsByQllxAndBdcdyh(new BdcCf(), ycqDataList.get(0).getBdcBdcdy().getBdcdyh(), xzqlProids);
            xzqlProids = initXzqlProidsByQllxAndBdcdyh(new BdcDyaq(), ycqDataList.get(0).getBdcBdcdy().getBdcdyh(), xzqlProids);
            xzqlProids = initXzqlProidsByQllxAndBdcdyh(new BdcYy(), ycqDataList.get(0).getBdcBdcdy().getBdcdyh(), xzqlProids);
            xzqlProids = initXzqlProidsByQllxAndBdcdyh(new BdcYg(), ycqDataList.get(0).getBdcBdcdy().getBdcdyh(), xzqlProids);
            if (StringUtils.isNotBlank(xzqlProids)) {
                List<BdcSplitData> yxzqlDataList = initBdcSplitData(xzqlProids.toString());
                ysjDataList.addAll(yxzqlDataList);
            }
        }
        return ysjDataList;
    }

    /**
     * @param splitNumList
     * @param proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化需要循环的数据
     */
    @Override
    public List<SplitNum> initSplitNum(List<SplitNum> splitNumList, String proid) {
        if (CollectionUtils.isEmpty(splitNumList)) {
            splitNumList = Lists.newArrayList();
        }
        List<BdcFwfzxx> bdcFwfzxxList = bdcFwfzxxService.queryBdcFwfzxxListByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
            for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                SplitNum splitNum = new SplitNum();
                splitNum.setBdcFwfzxx(bdcFwfzxx);
                splitNumList.add(splitNum);
            }
        }
        return splitNumList;
    }

    /**
     * @param fgid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取拆分前数据
     */
    @Override
    public List<BdcSplitData> getYszByFgid(String fgid) {
        List<BdcSplitData> bdcSplitDataList = Lists.newArrayList();
        if (StringUtils.isNotBlank(fgid)) {
            Example example = new Example(BdcFglssj.class);
            example.createCriteria().andEqualTo("fgid", fgid);
            List<BdcFglssj> bdcFglssjList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcFglssjList)) {
                for (BdcFglssj bdcFglssj : bdcFglssjList) {
                    if (StringUtils.isNotBlank(bdcFglssj.getSj())) {
                        BdcSplitData bdcSplitData = JSON.parseObject(bdcFglssj.getSj(), BdcSplitData.class);
                        JSONObject jsonObject = JSON.parseObject(bdcFglssj.getSj());
                        if (bdcSplitData != null && bdcSplitData.getBdcXm() != null && jsonObject.get("qllxVo") != null) {
                            //处理权利
                            QllxVo qllxVo = qllxService.makeSureQllx(bdcSplitData.getBdcXm());
                            if (qllxVo instanceof BdcFdcqDz || qllxVo instanceof BdcFdcq) {
                                BdcFdcqDz bdcFdcqDz = JSON.parseObject(jsonObject.get("qllxVo").toString(), BdcFdcqDz.class);
                                bdcSplitData.setQllxVo(bdcFdcqDz);
                            } else if (qllxVo instanceof BdcJsydzjdsyq) {
                                BdcJsydzjdsyq bdcJsydzjdsyq = JSON.parseObject(jsonObject.get("qllxVo").toString(), BdcJsydzjdsyq.class);
                                bdcSplitData.setQllxVo(bdcJsydzjdsyq);
                            } else if (qllxVo instanceof BdcCf) {
                                BdcCf bdcCf = JSON.parseObject(jsonObject.get("qllxVo").toString(), BdcCf.class);
                                bdcSplitData.setQllxVo(bdcCf);
                            } else if (qllxVo instanceof BdcDyaq) {
                                BdcDyaq bdcDyaq = JSON.parseObject(jsonObject.get("qllxVo").toString(), BdcDyaq.class);
                                bdcSplitData.setQllxVo(bdcDyaq);
                            } else if (qllxVo instanceof BdcYy) {
                                BdcYy bdcYy = JSON.parseObject(jsonObject.get("qllxVo").toString(), BdcYy.class);
                                bdcSplitData.setQllxVo(bdcYy);
                            } else if (qllxVo instanceof BdcYg) {
                                BdcYg bdcYg = JSON.parseObject(jsonObject.get("qllxVo").toString(), BdcYg.class);
                                bdcSplitData.setQllxVo(bdcYg);
                            }
                        }
                        if (bdcSplitData != null) {
                            bdcSplitDataList.add(bdcSplitData);
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(bdcSplitDataList)) {
            bdcSplitDataList = Collections.EMPTY_LIST;
        }
        return bdcSplitDataList;
    }


    /**
     * @param fgid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取拆分后数据
     */
    @Override
    public List<BdcSplitData> getCfhByFgid(String fgid) {
        List<BdcSplitData> bdcSplitDataList = Lists.newArrayList();
        if (StringUtils.isNotBlank(fgid)) {
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo("fgid", fgid);
            List<BdcXm> bdcXmList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    List<BdcSplitData> bdcSplitDatas = initBdcSplitData(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcSplitDatas)) {
                        bdcSplitDataList.addAll(bdcSplitDatas);
                    }
                }
            }
        }
        return bdcSplitDataList;
    }

    @Override
    public List<BdcFglssj> getBdcFglssjByFgid(String fgid) {
        List<BdcFglssj> bdcFglssjList = null;
        if (StringUtils.isNotBlank(fgid)) {
            Example example = new Example(BdcFglssj.class);
            example.createCriteria().andEqualTo("fgid", fgid);
            bdcFglssjList = entityMapper.selectByExample(example);
        }
        return bdcFglssjList;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 组织分割数据
     */
    private List<BdcSplitData> initBdcSplitData(String proids) {
        List<BdcSplitData> bdcSplitDataList = new ArrayList<>();
        if (StringUtils.isNotBlank(proids)) {
            String[] proidArray = StringUtils.split(proids, ",");
            if (proidArray != null && proidArray.length > 0) {
                for (String proid : proidArray) {
                    BdcSplitData bdcSplitData = new BdcSplitData();
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                    QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                    List<BdcFwfzxx> bdcFwfzxxList = null;
                    if (qllxVo != null && qllxVo instanceof BdcFdcqDz && StringUtils.isNotBlank(qllxVo.getQlid())) {
                        bdcFwfzxxList = bdcFwfzxxService.getBdcFwfzxxListByQlid(qllxVo.getQlid());
                    }
                    List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(bdcXm.getProid());
                    List<BdcZsQlrRel> bdcZsQlrRelList = bdcZsQlrRelService.queryBdcZsQlrRelByProid(proid);
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
                    BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
                    List<BdcSjcl> bdcSjclList = null;
                    if (bdcSjxx != null && StringUtils.isNotBlank(bdcSjxx.getSjxxid())) {
                        bdcSjclList = bdcSjclService.queryBdcSjclBySjxxid(bdcSjxx.getSjxxid());
                    }
                    BdcSfxx bdcSfxx = bdcSfxxService.getbdcSfxxByProid(proid);
                    List<BdcSfxm> bdcSfxmList = null;
                    if (bdcSfxx != null && StringUtils.isNotBlank(bdcSfxx.getSfxxid())) {
                        bdcSfxmList = bdcSfxmService.getBdcSfxmListBySfxxid(bdcSfxx.getSfxxid());
                    }
                    BdcGdxx bdcGdxx = bdcGdxxService.queryBdcGdxxByProid(proid);
                    bdcSplitData.setBdcXm(bdcXm);
                    bdcSplitData.setBdcXmRelList(bdcXmRelList);
                    bdcSplitData.setBdcQlrList(bdcQlrList);
                    bdcSplitData.setBdcSpxx(bdcSpxx);
                    bdcSplitData.setBdcBdcdy(bdcBdcdy);
                    bdcSplitData.setQllxVo(qllxVo);
                    bdcSplitData.setBdcFwfzxxList(bdcFwfzxxList);
                    bdcSplitData.setBdcXmzsRelList(bdcXmzsRelList);
                    bdcSplitData.setBdcZsQlrRelList(bdcZsQlrRelList);
                    bdcSplitData.setBdcZsList(bdcZsList);
                    bdcSplitData.setBdcSjxx(bdcSjxx);
                    bdcSplitData.setBdcSjclList(bdcSjclList);
                    bdcSplitData.setBdcSfxx(bdcSfxx);
                    bdcSplitData.setBdcSfxmList(bdcSfxmList);
                    bdcSplitData.setBdcGdxx(bdcGdxx);
                    bdcSplitDataList.add(bdcSplitData);
                }
            }
        }
        return bdcSplitDataList;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 根据权利和bdcdyh组织限制权利proids
     */
    private StringBuilder initXzqlProidsByQllxAndBdcdyh(QllxVo qllxVo, String bdcdyh, StringBuilder xzqlProids) {
        List<QllxVo> bdcQllxVoList = qllxService.getQllxByBdcdyh(qllxVo, bdcdyh);
        if (CollectionUtils.isNotEmpty(bdcQllxVoList)) {
            for (QllxVo qllxVoTemp : bdcQllxVoList) {
                if (qllxVoTemp != null && StringUtils.isNotBlank(qllxVoTemp.getProid())) {
                    if (StringUtils.isBlank(xzqlProids)) {
                        xzqlProids.append(qllxVoTemp.getProid());
                    } else {
                        xzqlProids.append(",").append(qllxVoTemp.getProid());
                    }
                }
            }
        }
        return xzqlProids;
    }
}
