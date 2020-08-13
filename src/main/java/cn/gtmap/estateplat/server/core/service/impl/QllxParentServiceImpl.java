package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.QllxParent;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.QllxParentService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.core.mapper.QllxParentMapper;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-1
 */
@Service
public class QllxParentServiceImpl implements QllxParentService {

    @Autowired
    private QllxService qllxService;
    @Autowired
    private QllxParentMapper qllxParentMapper;

    private static final String PARAMETER_TABLENAME = "tableName";

    @Override
    public List<QllxParent> queryZtzcQllxVo(QllxVo qllxVo, Map<String, Object> map) {
        List<QllxParent> list = null;
        if (map != null && !map.isEmpty() && qllxVo != null) {
            map.put("qszt", Constants.QLLX_QSZT_XS);
            map.put(PARAMETER_TABLENAME, qllxService.getTableName(qllxVo));
            list = qllxParentMapper.queryQllxVo(map);
        }
        return list;
    }

    @Override
    public List<Map> queryZtzcQllxMap(QllxVo qllxVo, Map<String, Object> map) {
        List<Map> list = null;
        if (map != null && !map.isEmpty() && qllxVo != null) {

            map.put("qszt", Constants.QLLX_QSZT_XS);
            map.put(PARAMETER_TABLENAME, qllxService.getTableName(qllxVo));
            list = qllxParentMapper.queryQllxMap(map);
        }
        return list;
    }

    @Override
    public List<QllxParent> queryLogoutQllxVo(QllxVo qllxVo, String bdcdyh) {
        List<QllxParent> list = null;
        if (StringUtils.isNotBlank(bdcdyh) && qllxVo != null) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("bdcdyh", bdcdyh);
            map.put("qszt", Constants.QLLX_QSZT_HR);
            map.put(PARAMETER_TABLENAME, qllxService.getTableName(qllxVo));
            qllxService.getTableName(qllxVo);
            list = qllxParentMapper.queryQllxVo(map);
        }
        return list;
    }

    @Override
    public List<QllxParent> queryLogcfQllxVo(QllxVo qllxVo, String bdcdyh, String xmzt, String isycf) {
        List<QllxParent> list = null;
        if (StringUtils.isNotBlank(bdcdyh) && qllxVo != null) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("bdcdyh", bdcdyh);
            map.put("xmzt", xmzt);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            map.put(PARAMETER_TABLENAME, qllxService.getTableName(qllxVo));
            //判断是否是预查封
            if (StringUtils.equals(isycf, "true")) {
                map.put("cflx", "'"+Constants.CFLX_ZD_YCF+"','"+Constants.CFLX_ZD_LHYCF+"'");
            } else if (StringUtils.equals(isycf, "false")) {
                map.put("cflx", "'"+Constants.CFLX_ZD_CF+"','"+Constants.CFLX_LHCF+"'");
            }
            qllxService.getTableName(qllxVo);
            list = qllxParentMapper.queryQllxVo(map);
        }
        return list;
    }

    @Override
    public List<QllxParent> queryQllxVo(QllxVo qllxVo, Map<String, Object> map) {
        List<QllxParent> list = null;
        if (map != null && !map.isEmpty() && qllxVo != null) {
            map.put(PARAMETER_TABLENAME, qllxService.getTableName(qllxVo));
            list = qllxParentMapper.queryQllxVo(map);
        }
        return list;
    }


}
