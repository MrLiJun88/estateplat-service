package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hqz
 * @version 1.0, 2016/12/26
 * @description 验证不动产单元是否裁定
 */
public class BdcdyCdQxValidateServiceImpl implements ProjectValidateService {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        List<String> cdProidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(project.getBdcdyh())) {
            String bdcdyh = project.getBdcdyh();
            List<BdcXm> bdcXmLst = bdcXmService.queryBdcxmByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(bdcXmLst)) {
                for (BdcXm bdcXm : bdcXmLst) {
                    if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD_PL)) {
                        cdProidList.add(bdcXm.getProid());
                    }
                }

                if (CollectionUtils.isNotEmpty(cdProidList)) {
                    for (String cdProid : cdProidList) {
                        if (StringUtils.isNotBlank(cdProid)) {
                            BdcCf bdcCf = bdcCfService.selectCfByProid(cdProid);
                            if (null != bdcCf && null != bdcCf.getQszt() && bdcCf.getQszt() == Constants.QLLX_QSZT_XS) {
                                proidList.add(cdProid);
                            }
                            List<BdcXmRel> bdcXmRelLst = bdcXmRelService.queryBdcXmRelByProid(cdProid);
                            if (CollectionUtils.isNotEmpty(bdcXmRelLst)) {
                                for (BdcXmRel bdcXmRel : bdcXmRelLst) {
                                    String yproid = bdcXmRel.getYproid();
                                    if (StringUtils.isNotBlank(yproid)) {
                                        bdcCf = bdcCfService.selectCfByProid(yproid);
                                        if (null != bdcCf && bdcCf.getQszt() != null &&
                                                bdcCf.getQszt() == Constants.QLLX_QSZT_XS && StringUtils.isNotBlank(bdcCf.getIscd()) && StringUtils.equals(bdcCf.getIscd(), Constants.QLLX_QSZT_XS.toString()) && StringUtils.isNotBlank(bdcCf.getIssx()) && StringUtils.equals(bdcCf.getIssx(), Constants.QLLX_QSZT_LS.toString())) {
                                            proidList.add(cdProid);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Example example = new Example(BdcZsCd.class);
            example.createCriteria().andEqualTo("bdcdyh", project.getBdcdyh());
            List<BdcZsCd> bdcZsCdList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcZsCdList)) {
                BdcZsCd bdcZsCd = bdcZsCdList.get(0);
                if (StringUtils.equals(bdcZsCd.getCdzt(), Constants.CDZT_CD)) {
                    proidList.add(bdcZsCd.getCdxmid());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList.get(0));
        } else {
            map.put("info", null);
        }
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
        return "136";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否是裁定状态";
    }
}
