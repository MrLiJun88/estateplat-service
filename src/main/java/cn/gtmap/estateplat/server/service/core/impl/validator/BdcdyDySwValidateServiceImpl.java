package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcDyaqService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.GdXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @description 验证抵押权抵押顺位是否正确
 */
public class BdcdyDySwValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private GdXmService gdXmService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        String proid = null;
        if (project != null && StringUtils.isNotBlank(project.getProid())
                && StringUtils.isNotBlank(project.getQllx()) && StringUtils.equals(project.getQllx(), Constants.QLLX_DYAQ)) {
            //抵押顺位验证
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
            String sqlxdm = bdcXm.getSqlx();
            QllxVo qllxVO = qllxService.makeSureQllx(bdcXm);//通过bdcXm权利类型QllxVo
            if (qllxVO instanceof BdcDyaq && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                HashMap queryMap = new HashMap();
                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                    queryMap.put("proid", bdcXm.getProid());
                    List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(queryMap);
                    BdcDyaq bdcDyaq = null;
                    if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                        bdcDyaq = bdcDyaqList.get(0);
                    }
                    //缮证后不需要验证 抵押在缮证后未归档前抵押注销后也不需要验证抵押顺位
                    if (bdcDyaq != null) {
                        if (bdcDyaq.getQszt() != null && (bdcDyaq.getQszt() == 1 || bdcDyaq.getQszt() == 2)) {
                        } else {
                            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                Integer dySw = bdcDyaq.getDysw();
                                if (dySw != null) {
                                    queryMap.clear();
                                    queryMap.put("bdcdyid", bdcXm.getBdcdyid());
                                    //查询出的bdcdyaq的qszt应该是现势的
                                    queryMap.put("qszt", "0");
                                    List<BdcDyaq> bdcDyaqList1 = bdcDyaqService.queryBdcDyaq(queryMap);
                                    //当前项目缮证后 当前proid对应的抵押权要过滤
                                    Integer bdcDyaqNumber = 0;
                                    BdcDyaq existBdcdyaq = null;
                                    for (BdcDyaq bdcDyaq1 : bdcDyaqList1) {
                                        if (StringUtils.isNotBlank(bdcDyaq1.getProid()) && !StringUtils.equals(bdcDyaq.getProid(), bdcDyaq1.getProid())) {
                                            bdcDyaqNumber += 1;
                                            existBdcdyaq = bdcDyaq1;
                                        }
                                    }
                                    List<GdDy> gdFwDyList = gdXmService.getGdFwDybyBdcdyid(bdcXm.getBdcdyid());
                                    List<GdDy> gdTdDyList = gdXmService.getGdTdDybyBdcdyid(bdcXm.getBdcdyid());
                                    if (dySw == 1) {
                                        if (bdcDyaqNumber > 0) {
                                            proid = existBdcdyaq.getProid();
                                        }
                                        if (CollectionUtils.isNotEmpty(gdFwDyList))
                                            proid = gdFwDyList.get(0).getProid();
                                        if (CollectionUtils.isNotEmpty(gdTdDyList))
                                            proid = gdTdDyList.get(0).getProid();
                                    } else {
                                        //之前无抵押权时 填写除1以外抵押顺位进行提示
                                        if (bdcDyaqNumber == 0 && CollectionUtils.isEmpty(gdFwDyList) && CollectionUtils.isEmpty(gdTdDyList)) {
                                            proid = bdcDyaq.getProid();
                                        } else {
                                            //填写的抵押顺位不为1时 验证抵押顺位
                                            //计算当前应填正确的抵押顺位
                                            Integer correctDysw = 0;
                                            if (CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, sqlxdm)) {
                                                correctDysw = bdcDyaqNumber + gdFwDyList.size() + gdTdDyList.size();
                                            } else {
                                                correctDysw = bdcDyaqNumber + gdFwDyList.size() + gdTdDyList.size() + 1;
                                            }
                                            if (correctDysw != dySw) {
                                                if (bdcDyaqNumber > 0) {
                                                    proid = existBdcdyaq.getProid();
                                                } else if (CollectionUtils.isNotEmpty(gdFwDyList)) {
                                                    proid = gdFwDyList.get(0).getProid();
                                                } else if (CollectionUtils.isNotEmpty(gdTdDyList)) {
                                                    proid = gdTdDyList.get(0).getProid();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        map.put("info", proid);
        return map;
    }

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "1002";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "抵押顺位填写错误！";
    }
}
