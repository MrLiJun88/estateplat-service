package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJjd;
import cn.gtmap.estateplat.model.server.core.BdcJjdXx;
import cn.gtmap.estateplat.server.core.service.ArchiveExchangeSecivce;
import cn.gtmap.estateplat.utils.Charsets;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * 归档服务
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 15-12-10
 * Time: 下午11:27
 */
@Repository
public class ArchiveExchangeSecivceImpl implements ArchiveExchangeSecivce {
    private static final String ARCHIVE_GATEWAY_MATERIAL_URL ="/gateway!materialIn.action";

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public String gdjjd(final String jjdbhs, final String archiveUrl) throws IOException {
        if (jjdbhs.split(",").length > 0) {
            for (String jjdbh : jjdbhs.split(",")) {
                BdcJjd bdcJjd = getBdcjjd(jjdbh);
                if (bdcJjd != null) {
                    List<BdcJjdXx> bdcJjdXxList = getBdcJjdXxByjjdid(bdcJjd.getJjdid());
                    if (CollectionUtils.isNotEmpty(bdcJjdXxList)) {
                        JSONArray jsonJjdXxList = JSONArray.fromObject(bdcJjdXxList);
                        HttpMethodParams params = new HttpMethodParams();
                        params.setContentCharset("GB2312");
                        String url = archiveUrl + ARCHIVE_GATEWAY_MATERIAL_URL;
                        HttpClient client = new HttpClient();
                        client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                        client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Charsets.CHARSET_UTF8);
                        PostMethod method = new PostMethod(url);
                        method.setRequestHeader("Connection", "close");
                        method.setParameter("bdcjjd", JSONObject.toJSONStringWithDateFormat(bdcJjd, "yyyy-MM-dd hh:MM:ss"));
                        method.setParameter("bdcjjdxx", JSON.toJSONString(jsonJjdXxList));
                        client.executeMethod(method);
                        method.releaseConnection();
                        ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                    }
                }
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public BdcJjd getBdcjjd(final String jjdbh) {
        if(StringUtils.isNotBlank(jjdbh)) {
            Example example = new Example(BdcJjd.class);
            example.createCriteria().andEqualTo("jjdbh", jjdbh);
            List<BdcJjd> bdcJjdList = entityMapper.selectByExample(BdcJjd.class, example);
            if (CollectionUtils.isNotEmpty(bdcJjdList)) {
                return bdcJjdList.get(0);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<BdcJjdXx> getBdcJjdXxByjjdid(final String jjdid) {
        List<BdcJjdXx> bdcJjdXxList = null;
        if(StringUtils.isNotBlank(jjdid)) {
            Example example = new Example(BdcJjdXx.class);
            example.createCriteria().andEqualTo("jjdid", jjdid);
            bdcJjdXxList = entityMapper.selectByExample(BdcJjdXx.class, example);
        }
        return bdcJjdXxList;
    }
}
