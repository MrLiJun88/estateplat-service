package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZsCdMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2019/12/12
 * @description 不动产证书裁定
 */

@Service
public class BdcZsCdServiceImpl implements BdcZsCdService {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcZsCdMapper bdcZsCdMapper;

    @Override
    public Map addBdcZsCd(BdcZsCd bdcZsCd) {
        Map resultMap = new HashMap();
        if(bdcZsCd != null
                && StringUtils.isNotBlank(bdcZsCd.getBdcdyh()) && StringUtils.isNotBlank(bdcZsCd.getCdjbr())
                && StringUtils.isNotBlank(bdcZsCd.getCqzh()) && StringUtils.isNotBlank(bdcZsCd.getCdyy())
                && StringUtils.isNotBlank(bdcZsCd.getProid())){
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcZsCd.getBdcdyh());
            if(StringUtils.isNotBlank(bdcdyid)){
                bdcZsCd.setBdcdyid(bdcdyid);
            }
            bdcZsCd.setCdid(UUIDGenerator.generate18());
            bdcZsCd.setCdsj(new Date());
            bdcZsCd.setCdzt(Constants.CDZT_CD);

            Example example = new Example(BdcZsCd.class);
            example.createCriteria().andEqualTo("cqzh", bdcZsCd.getCqzh());
            List<BdcZsCd> bdcZsCdList = entityMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(bdcZsCdList)){
                if(StringUtils.equals(bdcZsCdList.get(0).getCdzt(),Constants.CDZT_CD)){
                    resultMap.put(ParamsConstants.MESSAGE_LOWERCASE,"该产权证已存在于裁定表中，且已被裁定！");
                }else{
                    resultMap.put(ParamsConstants.MESSAGE_LOWERCASE,"该产权证已存在于裁定表中，但未被裁定！");
                }
            }else{
                entityMapper.saveOrUpdate(bdcZsCd,bdcZsCd.getCdid());
                resultMap.put(ParamsConstants.MESSAGE_LOWERCASE,"新增成功！");
            }
        }else{
            resultMap.put(ParamsConstants.MESSAGE_LOWERCASE,"新增失败！");
        }
        return resultMap;
    }

    @Override
    public void updateBdcZscdByBdcXm(BdcXm bdcXm) {
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())
                && StringUtils.isNotBlank(bdcXm.getSqlx())
                && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZY_SFCD)){
            String cqzh = null;
            String proid = null;
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            Map<String,String> cqzhMap = bdcXmService.zscdGetXsCqzhAndProidByBdcXm(bdcXm);
            if(cqzhMap != null && cqzhMap.get("cqzh") != null && cqzhMap.get("proid") != null){
                cqzh = cqzhMap.get("cqzh");
                proid = cqzhMap.get("proid");
            }
            if(StringUtils.isNotBlank(cqzh) && StringUtils.isNotBlank(proid)){
                //根据产权证号查询bdczscd是否存在数据
                Example example = new Example(BdcZsCd.class);
                example.createCriteria().andEqualTo("cqzh", cqzh);
                List<BdcZsCd> bdcZsCdList = entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(bdcZsCdList)){
                    //存在则更新数据
                    BdcZsCd bdcZsCd = bdcZsCdList.get(0);
                    bdcZsCd.setCdzt(Constants.CDZT_CD);
                    bdcZsCd.setCdjbr(bdcXm.getCjr());
                    bdcZsCd.setCdxmid(bdcXm.getProid());
                    entityMapper.saveOrUpdate(bdcZsCd,bdcZsCd.getCdid());
                }else{
                    //不存在则插入数据
                    BdcZsCd bdcZsCd = new BdcZsCd();
                    bdcZsCd.setCdid(UUIDGenerator.generate18());
                    bdcZsCd.setCdzt(Constants.CDZT_CD);
                    bdcZsCd.setCdsj(new Date());
                    if(bdcBdcdy != null){
                        bdcZsCd.setBdcdyid(bdcBdcdy.getBdcdyid());
                        bdcZsCd.setBdcdyh(bdcBdcdy.getBdcdyh());
                    }
                    bdcZsCd.setCdjbr(bdcXm.getCjr());
                    bdcZsCd.setProid(proid);
                    bdcZsCd.setCqzh(cqzh);
                    bdcZsCd.setCdxmid(bdcXm.getProid());
                    bdcZsCd.setCdyy("由于办理司法裁定解封");
                    entityMapper.saveOrUpdate(bdcZsCd,bdcZsCd.getCdid());
                }
            }
        }
    }

    @Override
    public void cancelBdcZscdByBdcXm(BdcXm bdcXm) {
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())
                && StringUtils.isNotBlank(bdcXm.getSqlx())
                && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZY_SFCD)){
            String cqzh = null;
            //依法转移登记选择的是对应的产权证 通过bdcxmrel找到产权证号
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                if(StringUtils.equals(bdcXmRel.getYdjxmly(),Constants.XMLY_BDC)){
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmRel.getYproid());
                    if(CollectionUtils.isNotEmpty(bdcZsList)){
                        BdcZs bdcZs = bdcZsList.get(0);
                        cqzh = bdcZs.getBdcqzh();
                    }
                }else if(StringUtils.equals(bdcXmRel.getYdjxmly(),Constants.XMLY_FWSP)){
                    GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(bdcXmRel.getYqlid());
                    cqzh = gdFwsyq.getFczh();
                }else if(StringUtils.equals(bdcXmRel.getYdjxmly(),Constants.XMLY_TDSP)){
                    GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(bdcXmRel.getYqlid());
                    cqzh = gdTdsyq.getTdzh();
                }
            }
            if(StringUtils.isNotBlank(cqzh)){
                Example example = new Example(BdcZsCd.class);
                example.createCriteria().andEqualTo("cqzh", cqzh);
                List<BdcZsCd> bdcZsCdList = entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(bdcZsCdList)){
                    BdcZsCd bdcZsCd = bdcZsCdList.get(0);
                    bdcZsCd.setCdzt(Constants.CDZT_ZC);
                    bdcZsCd.setJccdsj(new Date());
                    bdcZsCd.setJccdjbr(bdcXm.getCjr());
                    bdcZsCd.setJccdxmid(bdcXm.getProid());
                    bdcZsCd.setJccdyy("由于办理司法裁定转移后失效");
                    entityMapper.saveOrUpdate(bdcZsCd,bdcZsCd.getCdid());
                }
            }
        }
    }

    @Override
    public List<BdcZsCd> getBdcZscdList(Map map) {
        if(MapUtils.isNotEmpty(map)){
            return bdcZsCdMapper.getBdcZscdList(map);
        }
        return null;
    }
}
