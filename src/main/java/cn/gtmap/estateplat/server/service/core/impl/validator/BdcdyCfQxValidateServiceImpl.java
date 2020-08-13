package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 查封期限验证服务，只在任务转发验证
 */
public class BdcdyCfQxValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private QllxParentService qllxParentService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    EntityMapper entityMapper;
    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project= (Project)param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        String msg="";
        if (project != null && StringUtils.isNotBlank(project.getProid()) && StringUtils.equals(project.getDjzx(), Constants.DJZX_XF)) {
            List<BdcCf> bdcCfList = new ArrayList<BdcCf>();
            List<BdcCf> ybdcCfList = new ArrayList<BdcCf>();
            String proid =project.getProid();
            //不动产现势查封
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null) {
                bdcCfList = bdcCfService.queryCfByBdcdyid(bdcXm.getBdcdyid());
            }
            List<String> yProidList = bdcXmRelService.getYproid(proid);
            if(CollectionUtils.isNotEmpty(yProidList)) {
                for(String yProid:yProidList) {
                    //获取不动产原不动产查封
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yProid);
                    if(bdcXm != null && StringUtils.isNotBlank(ybdcXm.getBdcdyid())) {
                        ybdcCfList = bdcCfService.queryCfByBdcdyid(ybdcXm.getBdcdyid());
                    }
                    //获取过渡不动产查封权利
                    Example example = new Example(BdcCf.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("proid", yProid);
                    List<GdCf> gdCfList = entityMapper.selectByExample(GdCf.class, example);

                    if(CollectionUtils.isNotEmpty(bdcCfList) && CollectionUtils.isNotEmpty(ybdcCfList)) {
                        BdcCf bdcCf = bdcCfList.get(0);
                        BdcCf ybdccf = ybdcCfList.get(0);
                        if(bdcCf.getCfjsqx() != null && bdcCf.getCfksqx() != null && ybdccf.getCfjsqx() != null && ybdccf.getCfksqx() != null) {
                            Long daycf = CommonUtil.getDaySub(bdcCf.getCfksqx(), bdcCf.getCfjsqx());
                            Long dayycf = CommonUtil.getDaySub(ybdccf.getCfksqx(), ybdccf.getCfjsqx());
                            int daysIntCf = daycf.intValue() - 1;
                            int daysIntyCf = dayycf.intValue() - 1;
                            if(daysIntCf > daysIntyCf) {
                                proidList.add(ybdccf.getProid());
                            }
                        }
                    } else if(CollectionUtils.isNotEmpty(bdcCfList) && CollectionUtils.isNotEmpty(gdCfList)) {
                        BdcCf bdcCf = bdcCfList.get(0);
                        GdCf gdCf = gdCfList.get(0);
                        if(bdcCf.getCfjsqx() != null && bdcCf.getCfksqx() != null && gdCf.getCfjsrq() != null && gdCf.getCfksrq() != null) {
                            Long daycf = CommonUtil.getDaySub(bdcCf.getCfksqx(), bdcCf.getCfjsqx());
                            Long dayycf = CommonUtil.getDaySub(gdCf.getCfksrq(), gdCf.getCfjsrq());
                            int daysIntCf = daycf.intValue() - 1;
                            int daysIntyCf = dayycf.intValue() - 1;
                            if(daysIntCf > daysIntyCf) {
                                proidList.add(gdCf.getCfid());
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(msg)) {
                        proidList.add(msg);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }else {
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
        return "101";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否查封";
    }
}
