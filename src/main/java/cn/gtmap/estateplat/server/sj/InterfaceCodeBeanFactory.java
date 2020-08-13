package cn.gtmap.estateplat.server.sj;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2019/8/12
 * @description InterfaceCode 接口的工厂处理类
 */
public class InterfaceCodeBeanFactory {

    public static <T> T getBean(Set<T> listBeans, String code) {
        InterfaceCode interfaceCode = null;
        if (CollectionUtils.isNotEmpty(listBeans) && StringUtils.isNotBlank(code)) {
            for (Object interfaceCodeTemp : listBeans) {
                if (interfaceCodeTemp instanceof InterfaceCode) {
                    interfaceCode = (InterfaceCode) interfaceCodeTemp;
                    String[] returnCodeArray = StringUtils.split(interfaceCode.getIntetfacaCode(), ",");
                    boolean isReturn = false;
                    for (String returnCode : returnCodeArray) {
                        if (StringUtils.equals(returnCode, code)) {
                            isReturn = true;
                            break;
                        }
                    }
                    if (isReturn) {
                        break;
                    }
                }
            }
        }
        return (T) interfaceCode;
    }

    public static <T> List getBeans(Set<T> listBeans, String code) {
        List<InterfaceCode> interfaceCodeList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(listBeans) && StringUtils.isNotBlank(code)) {
            for (Object interfaceCodeTemp : listBeans) {
                if (interfaceCodeTemp instanceof InterfaceCode) {
                    InterfaceCode interfaceCode = (InterfaceCode) interfaceCodeTemp;
                    String[] codeArray = StringUtils.split(code, ",");
                    boolean isReturn = false;
                    for (String codeTemp : codeArray) {
                        if (StringUtils.equals(interfaceCode.getIntetfacaCode(), codeTemp)) {
                            isReturn = true;
                            break;
                        }
                    }
                    if (isReturn) {
                        interfaceCodeList.add(interfaceCode);
                        continue;
                    }
                }
            }
        }
        return  interfaceCodeList;
    }
}
