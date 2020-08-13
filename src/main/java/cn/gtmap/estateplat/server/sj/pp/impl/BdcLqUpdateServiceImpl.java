package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.server.sj.pp.BdcdyPicUpdateService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/27
 * @description 匹配不动产单元 更新bdc_lq表数据
 */
@Service
public class BdcLqUpdateServiceImpl implements BdcdyPicUpdateService {
    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {

    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {

    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {

    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_lq";
    }
}
