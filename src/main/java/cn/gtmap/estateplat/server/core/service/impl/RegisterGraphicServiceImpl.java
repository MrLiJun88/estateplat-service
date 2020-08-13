package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.BdcDataSynServerService;
import cn.gtmap.estateplat.server.core.service.RegisterGraphicService;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/2/17
 * @description
 */
@Service
public class RegisterGraphicServiceImpl implements RegisterGraphicService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Async
    @Override
    public void insertDjtxkSj(BdcXm bdcXm) {
        bdcDataSyn("Insert",bdcXm);
    }

    @Async
    @Override
    public void deleteDjtxkSj(BdcXm bdcXm) {
        bdcDataSyn("Delete",bdcXm);
    }


    void bdcDataSyn(String method,BdcXm bdcXm) {
        String syncRegisterGraphicUrl = AppConfig.getProperty("syncRegisterGraphic.url");
        String isSyncRegisterGraphic = AppConfig.getProperty("isSyncRegisterGraphic");
        if(bdcXm != null&&StringUtils.isNotBlank(method)&&StringUtils.isNotBlank(syncRegisterGraphicUrl)&&Boolean.parseBoolean(isSyncRegisterGraphic)) {
            String bdcdyh = "";
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByProid(bdcXm.getProid());
            for(BdcXm bdcXmTemp:bdcXmList){
                bdcdyh = bdcdyService.getBdcdyhByProid(bdcXmTemp.getProid());
                if(StringUtils.isNotBlank(bdcdyh)) {
                    BdcDataSynServerImpl.setUrl(syncRegisterGraphicUrl);
                    BdcDataSynServerService service = new BdcDataSynServerImpl().getBasicHttpBindingIBDCDataSynServer();
                    Map<String,Object> param = new HashMap<String,Object>();
                    param.put("TYPE",method);
                    param.put("BDCDYH",new String[]{bdcdyh});
                    param.put("PROID",bdcXmTemp.getProid());
                    Object dataJson = JSON.toJSON(param);
                    logger.info(dataJson.toString());
                    String result = service.doSynchron(dataJson.toString());
                    logger.info("result====="+result);
                }
            }

        }
    }
}
