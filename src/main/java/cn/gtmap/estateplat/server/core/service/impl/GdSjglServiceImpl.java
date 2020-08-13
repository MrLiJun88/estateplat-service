package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.log.AuditLog;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.GdSjglService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 过渡数据管理
 * Created with IntelliJ IDEA.
 * User: lst
 * Date: 15-5-5
 * Time: 下午7:17
 * To change this template use File | Settings | File Templates.
 */
@Repository
@Transactional
public class GdSjglServiceImpl implements GdSjglService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void delGdsjByIds(final String[] ids, final String bdclx) {
        Class newClass = null;
        if (StringUtils.equals(bdclx, Constants.BDCLX_TDQT) || StringUtils.equals(bdclx, Constants.BDCLX_LQ)) {
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDQT)) {
                newClass = GdCq.class;
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_LQ)) {
                newClass = GdLq.class;
            }
            for (int i = 0; i < ids.length; i++) {
                //删除草权或林权数据
                entityMapper.deleteByPrimaryKey(newClass, ids[i]);
                Example example = new Example(GdQlr.class);
                example.createCriteria().andEqualTo("qlid", ids[i]);
                //删除权利人
                entityMapper.deleteByExample(example);
            }
        } else {
            if (StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                newClass = GdTd.class;
                List<GdTdsyq> qlidList = new ArrayList<GdTdsyq>();
                for (int i = 0; i < ids.length; i++) {
                    //删除土地信息
                    entityMapper.deleteByPrimaryKey(newClass, ids[i]);
                    Example example = new Example(GdTdsyq.class);
                    example.createCriteria().andEqualTo("tdid", ids[i]);
                    //获取土地所有权的数据
                    List<GdTdsyq> list = entityMapper.selectByExample(example);
                    if (list != null) {
                        qlidList.addAll(list);
                    }
                    //删除土地所有权关系信息
                    entityMapper.deleteByExample(example);
                }
                for (int k = 0; k < qlidList.size(); k++) {
                    GdTdsyq gdTdsyq = qlidList.get(k);
                    Example example = new Example(GdQlr.class);
                    example.createCriteria().andEqualTo("qlid", gdTdsyq.getQlid());
                    //删除权利人
                    entityMapper.deleteByExample(example);
                }
            } else if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                newClass = GdFw.class;
                List<GdFwsyq> qlidList = new ArrayList<GdFwsyq>();
                for (int i = 0; i < ids.length; i++) {
                    //删除房屋信息
                    entityMapper.deleteByPrimaryKey(newClass, ids[i]);
                    Example example = new Example(GdFwsyq.class);
                    example.createCriteria().andEqualTo("fwid", ids[i]);
                    //获取房屋所有权的数据
                    List<GdFwsyq> list = entityMapper.selectByExample(example);
                    if (list != null) {
                        qlidList.addAll(list);
                    }
                    //删除房屋所有权关系信息
                    entityMapper.deleteByExample(example);
                }
                for (int k = 0; k < qlidList.size(); k++) {
                    GdFwsyq gdFwsyq = qlidList.get(k);
                    Example example = new Example(GdQlr.class);
                    example.createCriteria().andEqualTo("qlid", gdFwsyq.getQlid());
                    //删除权利人
                    entityMapper.deleteByExample(example);
                }
            }

        }
    }

    /**
     * 读取房产证
     *
     * @param filePath
     */
    @Override
    public String readFczFromXls(final String filePath) {
        String msg = "";
        File file = new File(filePath);
        List<GdFw> gdFws = new ArrayList<GdFw>();
        List<GdFwsyq> gdFwsyqs = new ArrayList<GdFwsyq>();
        List<GdXm> gdXms = new ArrayList<GdXm>();
        List<GdQlr> gdQlrs = new ArrayList<GdQlr>();
        List<GdBdcQlRel> gdBdcQlRels = new ArrayList<GdBdcQlRel>();
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            //更新日期
            Date gxrq = new Date();
            //记录房屋编号数组，防止插入重复数据
            List<String> fwbh = new ArrayList<String>();
            for (int i = 1; i < sheet.getRows(); i++) {
                GdFw gdFw = new GdFw();
                GdFwsyq gdFwsyq = new GdFwsyq();
                GdQlr gdQlr = new GdQlr();
                for (int j = 0; j < sheet.getColumns(); j++) {
                    String data = sheet.getCell(j, i).getContents();
                    if (sheet.getCell(j, i).getType() == CellType.DATE) {
                        DateCell dateCell = (DateCell) sheet.getCell(j, i);
                        data = CalendarUtil.formateDatetoStr(dateCell.getDate());
                    }
                    String titleName = sheet.getCell(j, 0).getContents();
                    String className = reflectEntity(titleName);
                    String fieldName = reflectFieldName(titleName);
                    if (StringUtils.equals(className, Constants.GD_FW)) {
                        savaData(gdFw.getClass(), gdFw, fieldName, data);
                    } else if (StringUtils.equals(className, Constants.GDQL_FWSYQ_CPT)) {
                        savaData(gdFwsyq.getClass(), gdFwsyq, fieldName, data);
                    } else if (StringUtils.equals(className, Constants.GD_QLR)) {
                        savaData(gdQlr.getClass(), gdQlr, fieldName, data);
                    }
                }
                if (!fwbh.contains(gdFw.getDah())) {
                    //保存房屋编号
                    fwbh.add(gdFw.getDah());
                    //几张表间的联系
                    GdXm gdXm = new GdXm();
                    String bdcid = UUIDGenerator.generate18();
                    gdXm.setProid(bdcid);
                    gdXm.setBdclx(Constants.BDCLX_TDFW);
                    String qlid = UUIDGenerator.generate18();
                    gdFwsyq.setQlid(qlid);
                    gdQlr.setQlid(qlid);
                    gdQlr.setQlrlx(Constants.QLRLX_QLR);
                    gdQlr.setQlrid(UUIDGenerator.generate18());
                    String fwid = UUIDGenerator.generate18();
                    gdFw.setFwid(fwid);
                    GdBdcQlRel gdBdcQlRel = new GdBdcQlRel();
                    gdBdcQlRel.setBdcid(fwid);
                    gdBdcQlRel.setQlid(qlid);
                    gdBdcQlRel.setRelid(UUIDGenerator.generate18());
                    //更新日期
                    gdFw.setGxrq(gxrq);
                    gdFwsyq.setGxrq(gxrq);
                    gdXm.setGxrq(gxrq);
                    //保存到数组中
                    gdFws.add(gdFw);
                    gdFwsyqs.add(gdFwsyq);
                    gdXms.add(gdXm);
                    gdQlrs.add(gdQlr);
                    gdBdcQlRels.add(gdBdcQlRel);
                } else {
                    //关联已有房屋
                    String qlid = UUIDGenerator.generate18();
                    gdFwsyq.setQlid(qlid);
                    gdQlr.setQlid(qlid);
                    gdQlr.setQlrlx(Constants.QLRLX_QLR);
                    gdQlr.setQlrid(UUIDGenerator.generate18());
                    GdBdcQlRel gdBdcQlRel = new GdBdcQlRel();
                    gdBdcQlRel.setQlid(qlid);
                    for (GdFw gdFw1 : gdFws) {
                        if (StringUtils.equals(gdFw1.getDah(), gdFw.getDah())) {
                            String bdcid = gdFw1.getFwid();
                            gdBdcQlRel.setBdcid(bdcid);
                        }
                    }
                    gdBdcQlRel.setRelid(UUIDGenerator.generate18());
                    gdFwsyq.setGxrq(gxrq);
                    //存入数组
                    gdFwsyqs.add(gdFwsyq);
                    gdBdcQlRels.add(gdBdcQlRel);
                    gdQlrs.add(gdQlr);
                }
            }
            //String name=""; //测试使用断点
            for (GdFw gdFw : gdFws)
                entityMapper.saveOrUpdate(gdFw, gdFw.getFwid());
            for (GdFwsyq gdFwsyq : gdFwsyqs)
                entityMapper.saveOrUpdate(gdFwsyq, gdFwsyq.getQlid());
            for (GdQlr gdQlr : gdQlrs)
                entityMapper.saveOrUpdate(gdQlr, gdQlr.getQlrid());
            for (GdXm gdXm : gdXms)
                entityMapper.saveOrUpdate(gdXm, gdXm.getProid());
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRels)
                entityMapper.saveOrUpdate(gdBdcQlRel, gdBdcQlRel.getRelid());
        } catch (IOException e) {
            msg = "badfile";
            return msg;
        } catch (BiffException e) {
            msg = "baddata";
            return msg;
        } finally {
            file.delete();
            msg = "0";
        }
        return msg;
    }

    /**
     * 读取土地证
     *
     * @param filePath
     */
    @Override
    public String readTdzFromXls(final String filePath) {
        String msg = "";
        File file = new File(filePath);
        List<GdTd> gdTds = new ArrayList<GdTd>();
        List<GdTdsyq> gdTdsyqs = new ArrayList<GdTdsyq>();
        List<GdQlr> gdQlrs = new ArrayList<GdQlr>();
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date gxrq = new Date();
            for (int i = 1; i < sheet.getRows(); i++) {
                GdTd gdTd = new GdTd();
                GdTdsyq gdTdsyq = new GdTdsyq();
                GdQlr gdQlr = new GdQlr();
                for (int j = 0; j < sheet.getColumns(); j++) {
                    String data = sheet.getCell(j, i).getContents();
                    if (sheet.getCell(j, i).getType() == CellType.DATE) {
                        DateCell dateCell = (DateCell) sheet.getCell(j, i);
                        data = sdf.format(dateCell.getDate());
                    }
                    String titleName = sheet.getCell(j, 0).getContents();
                    String className = reflectEntity(titleName);
                    String fieldName = reflectFieldName(titleName);
                    if (StringUtils.equals(className, Constants.GD_TD)) {
                        savaData(gdTd.getClass(), gdTd, fieldName, data);
                    } else if (StringUtils.equals(className, Constants.GDQL_TDSYQ_CPT)) {
                        savaData(gdTdsyq.getClass(), gdTdsyq, fieldName, data);
                    } else if (StringUtils.equals(className, Constants.GD_QLR)) {
                        savaData(gdQlr.getClass(), gdQlr, fieldName, data);
                    }

                }
                //表之间的联系
                String tdid = UUIDGenerator.generate18();
                gdTd.setTdid(tdid);
                gdTdsyq.setTdid(tdid);
                String qlid = UUIDGenerator.generate18();
                gdTdsyq.setQlid(qlid);
                gdQlr.setQlid(qlid);
                gdQlr.setQlrid(UUIDGenerator.generate18());
                gdQlr.setQlrlx(Constants.QLRLX_QLR);
                //更新日期
                gdTd.setGxrq(gxrq);
                //保存到数组中
                gdTds.add(gdTd);
                gdTdsyqs.add(gdTdsyq);
                gdQlrs.add(gdQlr);
            }
            for (GdTd gdTd : gdTds)
                entityMapper.saveOrUpdate(gdTd, gdTd.getTdid());
            for (GdTdsyq gdTdsyq : gdTdsyqs)
                entityMapper.saveOrUpdate(gdTdsyq, gdTdsyq.getQlid());
            for (GdQlr gdQlr : gdQlrs)
                entityMapper.saveOrUpdate(gdQlr, gdQlr.getQlrid());

        } catch (IOException e) {
            msg = "badfile";
            return msg;
        } catch (BiffException e) {
            msg = "baddata";
            return msg;
        } finally {
            file.delete();
            msg = "0";
        }

        return msg;

    }

    @Override
    @AuditLog(name = "过渡房屋数据注销",description = "过渡房屋数据注销日志记录")
    public String zxQl(final String qlid, String userid) {
        String msg = "fail";
        try {
            if(StringUtils.isNotBlank(qlid)) {
                HashMap map = new HashMap();
                map.put("dyid", qlid);
                List<GdDy> gdDyList = gdFwService.andEqualQueryGdDy(map);
                map.clear();
                map.put("cfid", qlid);
                List<GdCf> gdCfList = gdFwService.andEqualQueryGdCf(map);
                if (CollectionUtils.isNotEmpty(gdDyList)) {
                    GdDy gdDy = gdDyList.get(0);
                    gdDy.setIsjy(Constants.GDQL_ISZX_YZX);
                    gdDy.setZxdyyy(ParamsConstants.ZXYY_YJZX);
                    gdDy.setZxr(PlatformUtil.getCurrentUserName(userid));
                    gdDy.setZxdbsj(CalendarUtil.getCurHMSDate());
                    entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                }else if(CollectionUtils.isNotEmpty(gdCfList)){
                    GdCf gdCf = gdCfList.get(0);
                    gdCf.setIsjf(Constants.GDQL_ISZX_YZX);
                    gdCf.setJfr(PlatformUtil.getCurrentUserName(userid));
                    gdCf.setJfdbsj(CalendarUtil.getCurHMSDate());
                    gdCf.setJfyy(ParamsConstants.ZXYY_YJZX);
                    entityMapper.saveOrUpdate(gdCf,gdCf.getCfid());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                }else {
                    msg = "只允许抵押权他项证和查封记录进行注销";
                }
                gdFwService.changeGdqlztByQlid(qlid,CommonUtil.formatEmptyValue(Constants.GDQL_ISZX_YZX));
            }
        }catch (Exception e) {
            logger.error("GdSjglServiceImplc.zxQl",e);
        }
        return msg;
    }

    @Override
    @AuditLog(name = "解除过渡房屋数据注销",description = "解除过渡房屋数据注销日志记录")
    public String jczxQl(final String qlid) {
        String msg = "fail";
        try{
            if(StringUtils.isNotBlank(qlid)) {
                HashMap map = new HashMap();
                map.put("qlid", qlid);
                List<GdFwsyq> gdFwsyqList = gdFwService.andEqualQueryGdFwsyq(map);
                map.clear();
                map.put("dyid", qlid);
                List<GdDy> gdDyList = gdFwService.andEqualQueryGdDy(map);
                map.clear();
                map.put("cfid", qlid);
                List<GdCf> gdCfList = gdFwService.andEqualQueryGdCf(map);
                map.clear();
                map.put("ygid", qlid);
                List<GdYg> gdYgList = gdFwService.andEqualQueryGdYg(map);
                map.clear();
                map.put("yyid", qlid);
                List<GdYy> gdYyList = gdFwService.andEqualQueryGdYy(map);
                if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                    GdFwsyq gdFwsyq = gdFwsyqList.get(0);
                    gdFwsyq.setIszx(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdFwsyq, gdFwsyq.getQlid());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                } else if (CollectionUtils.isNotEmpty(gdDyList)) {
                    GdDy gdDy = gdDyList.get(0);
                    gdDy.setIsjy(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                } else if (CollectionUtils.isNotEmpty(gdCfList)) {
                    GdCf gdCf = gdCfList.get(0);
                    gdCf.setIsjf(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                } else if (CollectionUtils.isNotEmpty(gdYgList)) {
                    GdYg gdYg = gdYgList.get(0);
                    gdYg.setIszx(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdYg, gdYg.getYgid());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                } else if (CollectionUtils.isNotEmpty(gdYyList)) {
                    GdYy gdYy = gdYyList.get(0);
                    gdYy.setIszx(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdYy, gdYy.getYyid());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                }
                gdFwService.changeGdqlztByQlid(qlid, Constants.GDQL_ISZX_WZX.toString());
            }
        }catch (Exception e) {
            logger.error("GdSjglServiceImplc.jczxQl",e);
        }
        return msg;
    }

    /**
     * 通过反射存储数据
     *
     * @param entity
     * @param ob
     * @param filedName
     * @param data
     */
    public void savaData(Class<?> entity, Object ob, String filedName, String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.isBlank(filedName)) {
                return;
            }
            String methodName = "set" + StringUtils.upperCase(filedName.substring(0, 1)) + StringUtils.substring(filedName, 1);
            String type = entity.getDeclaredField(filedName).getGenericType().toString();
            Method method = null;
            if (StringUtils.equals(type, "class java.lang.String")) {
                method = entity.getDeclaredMethod(methodName, String.class);
                method.invoke(ob, data);
            } else if (StringUtils.equals(type, "class java.lang.Double")) {
                method = entity.getDeclaredMethod(methodName, Double.class);
                method.invoke(ob, Double.parseDouble(data));
            } else if (StringUtils.equals(type, "class java.lang.Integer")) {
                method = entity.getDeclaredMethod(methodName, Integer.class);
                method.invoke(ob, Integer.parseInt(data));
            } else if (StringUtils.equals(type, "class java.util.Date")) {
                method = entity.getDeclaredMethod(methodName, Date.class);
                Date date = sdf.parse(data);
                method.invoke(ob, date);
            }
        } catch (Exception e) {
            logger.error("GdSjglServiceImpl.savaData",e);
        }

    }

    /**
     * 获取中文标题对应的实体类
     *
     * @param titleName
     * @return
     */
    public String reflectEntity(String titleName) {
        HashMap gdFw = ReadXmlProps.getGdFw();
        HashMap gdFwsyq = ReadXmlProps.getGdFwsyq();
        HashMap gdTd = ReadXmlProps.getGdTd();
        HashMap gdTdsyq = ReadXmlProps.getGdTdsyq();
        HashMap gdQlr = ReadXmlProps.getGdQlr();
        List<HashMap> list = new ArrayList<HashMap>();
        list.add(gdFw);
        list.add(gdFwsyq);
        list.add(gdTd);
        list.add(gdTdsyq);
        list.add(gdQlr);
        for (HashMap hashMap : list) {
            if (hashMap.containsKey(titleName)) {
                return hashMap.get("classname").toString();
            }
        }
        return null;
    }

    /**
     * 获取中文标题对应的类的变量
     *
     * @param titleName
     * @return
     */
    public String reflectFieldName(String titleName) {
        HashMap gdFw = ReadXmlProps.getGdFw();
        HashMap gdFwsyq = ReadXmlProps.getGdFwsyq();
        HashMap gdTd = ReadXmlProps.getGdTd();
        HashMap gdTdsyq = ReadXmlProps.getGdTdsyq();
        HashMap gdQlr = ReadXmlProps.getGdQlr();
        List<HashMap> list = new ArrayList<HashMap>();
        list.add(gdFw);
        list.add(gdFwsyq);
        list.add(gdTd);
        list.add(gdTdsyq);
        list.add(gdQlr);
        for (HashMap hashMap : list) {
            if (hashMap.containsKey(titleName)) {
                return hashMap.get(titleName).toString();
            }
        }
        return null;
    }
    @Override
    @AuditLog(name = "过渡土地数据注销",description = "过渡土地数据注销日志记录")
    public String zxGdTdQl(final String qlid, final String userid) {
        String msg = "fail";
        try{
            if(StringUtils.isNotBlank(qlid)){
                GdDy gdDy = entityMapper.selectByPrimaryKey(GdDy.class,qlid);
                if(gdDy!=null){
                    gdDy.setIsjy(Constants.GDQL_ISZX_YZX);
                    gdDy.setZxr(PlatformUtil.getCurrentUserName(userid));
                    gdDy.setZxdyyy(ParamsConstants.ZXYY_YJZX);
                    gdDy.setZxdbsj(CalendarUtil.getCurHMSDate());
                    entityMapper.saveOrUpdate(gdDy,qlid);
                    gdFwService.changeGdqlztByQlid(qlid,Constants.QLLX_QSZT_XS.toString());
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                }else{
                    msg = "只允许注销抵押权他项证";
                }
            }
        }catch (Exception e) {
            logger.error("GdSjglServiceImpl.zxGdTdQl",e);
        }
        return msg;
    }

    @Override
    @AuditLog(name = "解除过渡土地数据注销",description = "解除过渡土地数据注销日志记录")
    public String jczxGdTdQl(final String qlid) {
        String msg = "fail";
        try{
            if(StringUtils.isNotBlank(qlid)){
                GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class,qlid);
                GdDy gdDy = entityMapper.selectByPrimaryKey(GdDy.class,qlid);
                GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class,qlid);
                if(gdTdsyq != null){
                    gdTdsyq.setIszx(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdTdsyq,qlid);
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                }
                if(gdDy != null){
                    gdDy.setIsjy(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdDy,qlid);
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                }
                if(gdCf != null){
                    gdCf.setIsjf(Constants.GDQL_ISZX_WZX);
                    entityMapper.saveOrUpdate(gdCf,qlid);
                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                }
                gdFwService.changeGdqlztByQlid(qlid,Constants.QLLX_QSZT_LS.toString());
            }
        }catch (Exception e) {
            logger.error("GdSjglServiceImpl.jczxGdTdQl",e);
        }
        return msg;
    }

}
