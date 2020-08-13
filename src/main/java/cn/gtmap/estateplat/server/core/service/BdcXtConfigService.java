package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSqlxQllxRel;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;

import java.util.List;

/**
 * .
 * <p/>
 * 系统配置信息
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-19
 */
public interface BdcXtConfigService {
    /**
     * zdd 获取不动产权证号的配置信息
     *
     * @param bdcXm
     * @return
     */
    BdcXtConfig queryBdczsBhConfig(final BdcXm bdcXm);

    /**
     * lst 获取申请类型和权利类型的关系表数据
     *
     * @param sqlx
     * @return
     */
    List<BdcSqlxQllxRel> getOthersBySqlx(final String sqlx);

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @param dwdm
     * @return
     * @Description: 根据dwdm获取系统配置
     */
    BdcXtConfig queryBdcXtConfigByDwdm(String dwdm);
}
