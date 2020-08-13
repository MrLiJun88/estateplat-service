package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by sunchao on 2016-5-28.
 * 不动产权证书本地打印
 */
public interface BdcZsPrintService {
    /**
     * @param proid,zslx,serverUrl,zsid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn DataToPrintXml
     * @description 获取可转换符合控件的xml的证书数据的实体类
     */
    DataToPrintXml getZsPrintXml(final String proid,final String zslx,final String serverUrl,final String zsid) throws UnsupportedEncodingException;

    /**
     *
     * @param zslx
     * @param serverUrl
     * @param proidAndzsid
     * @return
     * @throws UnsupportedEncodingException
     */
    MulDataToPrintXml getMulZsPrintXml(final String zslx,final String serverUrl,Map<String, String> proidAndzsid) throws UnsupportedEncodingException;

}
