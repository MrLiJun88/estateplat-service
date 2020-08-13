package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/3
 * @description 
 */

import cn.gtmap.estateplat.server.core.service.SyncQlztService;
import cn.gtmap.estateplat.server.core.service.UpdateCxBdcdyZtService;
import cn.gtmap.estateplat.server.core.service.UpdateCxZsZmService;
import cn.gtmap.estateplat.server.core.service.UpdateGxZtService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SyncQlztServiceImpl implements SyncQlztService {
    @Autowired
    private UpdateCxZsZmService updateCxZsZmService;
    @Autowired
    private UpdateGxZtService updateGxZtService;
    @Autowired
    private UpdateCxBdcdyZtService updateCxBdcdyZtService;

    @Override
    public boolean updateQlztAfterDelet(List<Map<String, Object>> mapList) {
        //hzj 证书证明删除后更新管理
        boolean result=false;
        if(null !=mapList && CollectionUtils.isNotEmpty(mapList)){
            updateCxZsZmService.updateCxZsZmByCqzh(mapList);
            result=true;
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> queryQlztBeforDelet(String proid) {
        //hzj 证书证明删除前管理
        return updateCxZsZmService.getCqzhListByProid(proid);
    }

    @Override
    public void updateGxztAfterDelet(Map map) {
        updateGxZtService.updateGxztByMap(map);
    }

    @Override
    public Map queryGxztBeforDelet(String proid) {
        return updateGxZtService.getGxZtByProid(proid,"true","delet");
    }

    @Override
    public Map queryBdcdyZtMapBeforDelet(String proid) {
        return updateCxBdcdyZtService.getMapByProid(proid);
    }

    @Override
    public void updateBdcdyZtMapAfterDelet(Map<String, Object> map) {
        updateCxBdcdyZtService.updateBdcdyZtByMap(map);
    }

    @Override
    public void updateQlztByProid(String proid) {
        //hzj 证书证明管理
        updateCxZsZmService.updateCxZsZmByProid(proid);
        //hzj 不动产单元状态管理
        updateCxBdcdyZtService.updateBdcdyZtByProid(proid);
        //hzj 共享的管理
        updateGxZtService.updateGxztByProid(proid);
    }

    @Override
    public void updateQlztByProidForBack(String proid) {
        //hzj 证书证明管理
        updateCxZsZmService.updateCxZsZmByProid(proid);
        //hzj 不动产单元状态管理
        updateCxBdcdyZtService.updateBdcdyZtByProid(proid);
        //hzj 共享的管理
        updateGxZtService.updateGxztForBack(proid);
    }

    @Override
    public void updateQlztForDelete(String proid) {
        //hzj 证书证明管理
        updateCxZsZmService. updateZszmZtDeleteEnvent(proid);
        //hzj 共享的管理
    }
}
