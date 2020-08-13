package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.ArrayUtils;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 * 注销登记办结  注销原权利证书
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class EndProjectZxdjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 过度的创建的注销登记创建的权利需要注销
             */
            if (!StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC) && !StringUtils.equals(Constants.SQLX_PLDYZX,bdcXm.getSqlx())) {
                qllxService.changeQllxZt(bdcXm.getProid(), Constants.QLLX_QSZT_HR);
            }
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());

        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            String yqllxdm = bdcZdGlService.getYqllxBySqlx(bdcXm.getSqlx());
            //当前项目的权利类型包含在查出的原权利类型之中时，不修改项目的qllx
            String[] yqllxdmlist = null;
            if(StringUtils.isNotBlank(yqllxdm)){
                yqllxdmlist = StringUtils.split(yqllxdm,",");
            }
            if(ArrayUtils.isNotEmpty(yqllxdmlist)){
                List<String> yqllxlist = Arrays.asList(yqllxdmlist);
                if(StringUtils.isNotBlank(bdcXm.getQllx())&&!yqllxlist.contains(bdcXm.getQllx())){
                    bdcXm.setQllx(yqllxdm);
                }
            }
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                }
                changeGdsjQszt(bdcXmRel, qllxVo, 1);
                //批量抵押注销
                List<BdcXm> ybdcXmList = bdcXmService.getYbdcXmListByProid(bdcXmRel.getProid());
                if(StringUtils.equals(Constants.SQLX_PLDYZX,bdcXm.getSqlx()) && CollectionUtils.isEmpty(ybdcXmList)) {
                    BdcDyaq bdcDyaq = (BdcDyaq)qllxService.queryQllxVo(bdcXm);
                    UserInfo user= SessionUtil.getCurrentUser();
                    String username= user!=null ? user.getUsername() :"";
                    if(StringUtils.isBlank(bdcDyaq.getZxdyywh())) {
                        bdcDyaq.setZxdyywh(bdcXm.getBh());
                    }
                    if(StringUtils.isBlank(bdcDyaq.getZxdyyy())) {
                        bdcDyaq.setZxdyyy(bdcXm.getDjyy());
                    }
                    if(StringUtils.isBlank(bdcDyaq.getZxdbr())) {
                        bdcDyaq.setZxdbr(username);
                    }
                    if(null == bdcDyaq.getZxsj()) {
                        bdcDyaq.setZxsj(new Date());
                    }
                    entityMapper.updateByPrimaryKeySelective(bdcDyaq);
                }
            }
            changeFj(bdcXm);


            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }
    }

    protected void changeGdsjQszt(BdcXmRel bdcXmRel, QllxVo qllxVo, Integer qszt) {

        if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && !bdcXmRel.getYdjxmly().equals("1")&&StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
            gdXmService.updateGdQszt(bdcXmRel.getYqlid(), qszt);
        }

    }

    public void changeFj(BdcXm bdcxm) {
        if (bdcxm != null) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 过度的创建的注销登记创建的权利需要注销
             */
            if (!StringUtils.equals(bdcxm.getXmly(), Constants.XMLY_BDC)) {
                Example example = new Example(bdcxm.getClass());
                example.createCriteria().andEqualTo("bh", bdcxm.getBh());
                changeFjByExample(example);
            }

            List<String> yProidList = bdcXmRelService.getYproid(bdcxm.getProid());
            //根据原项目id获取原项目信息
            if (CollectionUtils.isNotEmpty(yProidList)) {
                BdcXm ybdcXm = null;
                for(String yProid:yProidList) {
                    ybdcXm = bdcXmService.getBdcXmByProid(yProid);
                    /*根据现项目proid获取原项目proid，然后去找到原权利*/
                    if (ybdcXm != null) {
                        if (StringUtils.equals(ybdcXm.getSqlx(), Constants.SQLX_GYJSYDHB_BGDJ)) {
                            Example bdcxmExample = new Example(ybdcXm.getClass());
                            if (StringUtils.isNotBlank(ybdcXm.getProid())) {
                                bdcxmExample.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, ybdcXm.getProid());
                            }
                            changeFjByExample(bdcxmExample);
                        } else {
                            Example bdcxmExample = new Example(ybdcXm.getClass());
                            if (StringUtils.isNotBlank(ybdcXm.getProid())) {
                                bdcxmExample.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, ybdcXm.getProid());
                            }
                            changeFjByExample(bdcxmExample);
                        }
                    } else {
                        //当bdcXmRelList的yproid取不到的时候才根据ybh去找。主要是合并流程编号一样。
                        /*根据现项目编号获得原项目proid*/
                        if (StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_GYJSYDHB_BGDJ)) {
                            String ybhStr = bdcxm.getYbh();
                            if (StringUtils.isNotBlank(ybhStr)) {
                                String[] ybhs = ybhStr.split(",");
                                for (String ybh : ybhs) {
                                    Example bdcxmExample = new Example(bdcxm.getClass());
                                    if (StringUtils.isNotBlank(ybh)) {
                                        bdcxmExample.createCriteria().andEqualTo("bh", ybh);
                                    }
                                    changeFjByExample(bdcxmExample);
                                }
                            }

                        } else {
                            Example bdcxmExample = new Example(bdcxm.getClass());
                            if (StringUtils.isNotBlank(bdcxm.getYbh())) {
                                bdcxmExample.createCriteria().andEqualTo("bh", bdcxm.getYbh());
                            }
                            changeFjByExample(bdcxmExample);
                        }
                    }
                }
            }
        }
    }

    private void changeFjByExample(Example bdcxmExample) {
        BdcXm ybdcxm;
        if(CollectionUtils.isNotEmpty( bdcxmExample.getOredCriteria()) && CollectionUtils.isNotEmpty(bdcxmExample.getOredCriteria().get(0).getAllCriteria())) {
            List<BdcXm> ybdcxmList = entityMapper.selectByExample(bdcxmExample);
            if (CollectionUtils.isNotEmpty(ybdcxmList)) {
                ybdcxm = ybdcxmList.get(0);
                String yproid = ybdcxm.getProid() == null ? "" : ybdcxm.getProid();
            /*如果原权利类型是抵押、异议、查封，则不做附记处理*/
                if (!StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_DYAQ) && !StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_YYDJ) && !StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_CFDJ)) {
                /*获得原项目权利类型*/
                    QllxVo yQllxVo = qllxService.makeSureQllx(ybdcxm);
                    Example yQllx = new Example(yQllxVo.getClass());
                    yQllx.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, yproid);
                    List<QllxVo> yQllxVoList = (List<QllxVo>) entityMapper.selectByExample(yQllxVo.getClass(), yQllx);
                    if (CollectionUtils.isNotEmpty(yQllxVoList)) {
                        for (QllxVo yqllxVo : yQllxVoList) {
                            if (yqllxVo != null) {
                                yqllxVo.setFj((yqllxVo.getFj() == null ? "" : yqllxVo.getFj()) + "\n" + CalendarUtil.formateToStrChinaYMDDate(CalendarUtil.getCurDate()) + "\n注销权利");
                                entityMapper.updateByPrimaryKeySelective(yqllxVo);
                            }
                        }
                    }
                }
            }
        }
    }
}
