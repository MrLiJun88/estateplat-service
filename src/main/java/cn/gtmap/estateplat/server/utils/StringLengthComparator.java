package cn.gtmap.estateplat.server.utils;

import java.util.Comparator;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/1/21
 * @description 重写compare方法，按字符长度排序
 */
public class StringLengthComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        int num = o2.length() - o1.length();
        if(num == 0)
            return o1.compareTo(o2);
        return num;
    }
}
