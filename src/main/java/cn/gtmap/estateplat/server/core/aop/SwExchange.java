package cn.gtmap.estateplat.server.core.aop;

import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.model.sw.DbtsswModel;
import cn.gtmap.estateplat.server.model.sw.SwDbxx;
import cn.gtmap.estateplat.server.utils.ReadJsonUtil;
import com.gtis.util.ThreadPool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-16
 * @description 税务共享数据
 */
public class SwExchange implements Serializable {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    private static DbtsswModel dbtsswModel = ReadJsonUtil.getdbtsswPz();

    /**
     * @author
     * @description 创建项目时即推送数据
     */
    public void dbtsxx(final JoinPoint jp) {
        ThreadPool.execute(new Runnable() {
            public void run() {
                String proid = (String) jp.getArgs()[0];
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && dbtsswModel != null && StringUtils.indexOf(dbtsswModel.getSqlx(), bdcXm.getSqlx()) > -1) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        for (BdcXm xm : bdcXmList) {
                            String htbh = "";
                            QllxVo qllxVo = qllxService.queryQllxVo(xm);
                            if (qllxVo instanceof BdcFdcq) {
                                BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                                htbh = bdcFdcq.getFdcjyhth();
                            }
                            if (qllxVo instanceof BdcFdcqDz) {
                                BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                                htbh = bdcFdcqDz.getFdcjyhth();
                            }
                            if (StringUtils.isNotBlank(htbh)) {
                                SwDbxx swDbxx = new SwDbxx(htbh, "1", qllxVo.getDbr(), qllxVo.getDjsj());
                                String url = dbtsswModel.getUrl();
                                SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                                requestFactory.setConnectTimeout(50000);
                                requestFactory.setReadTimeout(100000);
                                RestTemplate restTemplate = new RestTemplate(requestFactory);
                                restTemplate.postForObject(url, swDbxx, String.class);
                            }
                        }
                    }

                }
            }
        });
    }
}
