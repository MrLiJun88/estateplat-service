package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSjxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * 查封登记删除表单信息（不需要删除权利人 证书表）
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class DelProjectCfdjServiceImpl extends DelProjectDefaultServiceImpl {
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;


    @Override
    public void delBdcBdxx(final BdcXm bdcXm) {
        String proid = bdcXm.getProid();
        //zdd 删除项目关系表
        bdcXmRelService.delBdcXmRelByProid(proid);

        //zdd 删除收件单信息表
        List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(bdcXm.getWiid());
        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
            for (BdcSjxx bdcSjxx : bdcSjxxList) {
                bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
            }
        }
        //zdd 删除审批信息
        bdcSpxxService.delBdcSpxxByProid(proid);

        //zdd 删除权利人证书关系信息表以及权利人信息
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
            }
        }

        //zdd 删除权利类型信息
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, proid);

        //zhouwanqing 删除多余的bdc_bdcdjb和bdc_td
        bdcdyService.delDjbAndTd(bdcXm);

        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            List<BdcXm> bdcXmList = null;
            HashMap map = new HashMap();
            map.put("bdcdyid", bdcXm.getBdcdyid());
            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            if (bdcXmList != null && bdcXmList.size() == 1) {
                bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
            }
        }

    }
}
