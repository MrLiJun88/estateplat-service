package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcTd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcAdapterService;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zdd on 2015/12/9.
 * 某一个业务类数据读取逻辑实现类
 */
@Service
public class ReadQzToBbcServiceImpl implements ReadQzToBbcAdapterService {


    //zdd 将所有的不动产业务数据转换服务注入进来
    @Autowired
    Set<ReadQzToBbcService> readQzToBbcServices;

    @Autowired
    DozerUtil dozerUtil;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public <T> List<T> getBdcDataSet(QzHead qzHead) {
        List<T> list = new ArrayList<T>();

        for (ReadQzToBbcService readQzToBbcService : readQzToBbcServices) {
            List<T> listBdcData = readQzToBbcService.getBdcData(qzHead);
            if (CollectionUtils.isNotEmpty(listBdcData))
                list.addAll(listBdcData);
        }

        return list;
    }

    /**
     * @param fromList 来源集合
     * @param tolist   转换集合
     * @return
     */
    public List<InsertVo> getBdInsertList(List fromList, List tolist) {
        //zdd list由三部分数据组成，一部分是审批与登记业务类型匹配上的集合（需要转换的）；一部分是审批也登记业务类型匹配不上的集合（需要主外键赋值）；一分部是登记业务与审批业务匹配不上的集合（直接插入）
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 存放主键、外键的map对象  用于下面处理外键时用
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            if (fromList != null && tolist != null) {
                //zdd 获取一些关键字段的值  用于处理那些没有在业务中建立关联关系的对象
                map.put("qlid", UUIDGenerator.generate18());
                map.put("sjxxid", UUIDGenerator.generate18());
                for (Object obj : tolist) {
                    //zdd 默认将所有目标对象返回
                    list.add((InsertVo) obj);
                    if (obj.getClass().equals(BdcBdcdy.class)) {
                        BdcBdcdy bdcdy = (BdcBdcdy) obj;
                        map.put("bdcdyid", bdcdy.getBdcdyid());
                    } else if (obj.getClass().equals(BdcXm.class)) {
                        map.put("proid", ((BdcXm) obj).getProid());
                        if (((BdcXm) obj).getWiid() != null)
                            map.put("wiid", ((BdcXm) obj).getWiid());
                        if (((BdcXm) obj).getBh() != null)
                            map.put("ywh", ((BdcXm) obj).getBh());
                    } else if (obj.getClass().equals(BdcTd.class)) {
                        BdcTd bdcTd = (BdcTd) obj;
                        map.put("zdzhh", bdcTd.getZdzhh());
                    }
                }
                //zdd 审批与业务匹配上且需要转换的审批对象集合
                List<InsertVo> convertInsertList = new ArrayList<InsertVo>();
                //zdd 审批与业务未匹配上且需要主外键赋值的审批对象集合
                List<Object> notConvertInsertList = new ArrayList<Object>();
                //zdd 审批与业务匹配上且需要将其排除在外的登记业务对象集合，防止与匹配上的审批对象重复
                List<Object> removeToList = new ArrayList<Object>();

                //zdd 循环区分审批与登记业务，得到审批业务的全集
                for (int i = 0; i < fromList.size(); i++) {
                    Object obj = fromList.get(i);
                    Boolean flag = false;
                    int n = 0;
                    for (n = 0; n < tolist.size(); n++) {
                        Object bdcxmObj = tolist.get(n);
                        if (obj.getClass().getName().equals(bdcxmObj.getClass().getName())) {
                            removeToList.add(tolist.get(n));
                            dozerUtil.sameBeanDateConvert(obj, bdcxmObj, false);
                            convertInsertList.add((InsertVo) bdcxmObj);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        notConvertInsertList.add(obj);
                    } else {
                        tolist.remove(n);
                    }
                }
                //zdd 得到登记业务与审批业务的差集
                if (CollectionUtils.isNotEmpty(removeToList)) {
                    for (Object removeObj : removeToList) {
                        if (list.contains(removeObj))
                            list.remove(removeObj);
                    }
                }

                if (CollectionUtils.isNotEmpty(convertInsertList)) {
                    list.addAll(convertInsertList);
                }
                if (CollectionUtils.isNotEmpty(notConvertInsertList)) {
                    for (Object obj : notConvertInsertList) {
                        //如果在不动产登记业务中未找到相对应的对象，也需要将数据插入业务库
                        //首先处理主键
                        Class[] parameterTypes = new Class[1];
                        List<Field> idFields = AnnotationsUtils.getAnnotationField(obj, Id.class);
                        if (CollectionUtils.isNotEmpty(idFields)) {
                            //zdd 主键 应该只有一个
                            Field idField = idFields.get(0);
                            parameterTypes[0] = idField.getType();
                            StringBuilder sb = new StringBuilder();
                            sb.append("set");
                            sb.append(idField.getName().substring(0, 1).toUpperCase());
                            sb.append(idField.getName().substring(1));
                            Method method = obj.getClass().getMethod(sb.toString(), parameterTypes);
                            method.invoke(obj, UUIDGenerator.generate18());
                        }
                        //其次处理外键   需要在对象上定义外键
                        List<Field> joinFields = AnnotationsUtils.getAnnotationField(obj, JoinColumn.class);
                        if (CollectionUtils.isNotEmpty(joinFields)) {
                            for (Field joinField : joinFields) {
                                if (map.containsKey(joinField.getName())) {
                                    parameterTypes[0] = joinField.getType();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("set");
                                    sb.append(joinField.getName().substring(0, 1).toUpperCase());
                                    sb.append(joinField.getName().substring(1));
                                    Method method = obj.getClass().getMethod(sb.toString(), parameterTypes);
                                    method.invoke(obj, map.get(joinField.getName()));
                                }
                            }
                        }
                        list.add((InsertVo) obj);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("ReadQzToBbcServiceImpl.getBdInsertList",e);
        }
        return list;
    }
}
