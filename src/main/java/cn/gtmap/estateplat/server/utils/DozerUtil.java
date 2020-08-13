package cn.gtmap.estateplat.server.utils;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by lst on 2015/11/19.
 */

@Service
public class DozerUtil {

    @Resource(name = "dozerMapper")
    private DozerBeanMapper dozerMapper;
    @Resource(name = "dozerSameNullFMapper")
    private DozerBeanMapper dozerMapperF;
    @Resource(name = "dozerSameNullTMapper")
    private DozerBeanMapper dozerMapperT;

    @PostConstruct
    private void checkDozerXml() {
        dozerMapper.map(new Object(), new Object());
        dozerMapperF.map(new Object(), new Object());
        dozerMapperT.map(new Object(), new Object());
    }

    /**
     * 实体类之间数据转换公共方法  没有mapId,有配置文件
     *
     * @param source
     * @param destination
     */
    public void beanDateConvert(Object source, Object destination) {
        dozerMapper.map(source, destination);
    }

    /**
     * 实体类之间数据转换公共方法  如果存在mapId匹配
     *
     * @param source
     * @param destination
     */
    public void beanDateConvert(Object source, Object destination, String mapId) {
        if (StringUtils.isNotBlank(mapId)) {
            dozerMapper.map(source, destination, mapId);
        } else {
            beanDateConvert(source, destination);
        }
    }

    /**
     * xhc
     * 实体类之间数据转换公共方法  没有mapId,有配置文件,参数为true时，null覆盖；false时，null不覆盖
     *
     * @param source
     * @param destination
     * @param args
     */
    public void sameBeanDateConvert(Object source, Object destination, boolean args) {
        if (args) {
            dozerMapperT.map(source, destination);
        } else {
            dozerMapperF.map(source, destination);
        }
    }
}
