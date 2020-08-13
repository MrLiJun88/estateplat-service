package cn.gtmap.estateplat.server.utils;
import java.math.BigDecimal;

/**
 * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
 * @version 1.0, 2018-12-25
 * @description: 计算常用类
 */
public class CalculationUtils {
    public static double doubleAdd(double a, double b){
        BigDecimal bg1 = BigDecimal.valueOf(a);
        BigDecimal bg2 = BigDecimal.valueOf(b);
        return bg1.add(bg2).doubleValue();
    }

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 金额位数过多时，科学计数法会出现数值带E的问题
     */
    public static String bigDecimalAddToString(double a, double b){
        BigDecimal bg1 = BigDecimal.valueOf(a);
        BigDecimal bg2 = BigDecimal.valueOf(b);
        return bg1.add(bg2).toString();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }
}
