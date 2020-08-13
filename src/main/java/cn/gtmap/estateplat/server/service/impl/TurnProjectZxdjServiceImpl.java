package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * .
 * <p/>
 * 注销登记转发 不需要生成权利类型 不需要生成证书
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class TurnProjectZxdjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        //获取项目来源
        String xmly = bdcXm.getXmly();

        //如果项目来源不是过渡数据
        if (!StringUtils.equals(xmly,Constants.XMLY_BDC)) {
            super.saveQllxVo(bdcXm);
            super.saveYQllxVo(bdcXm);
        } else {
            //修改原权利信息
            super.saveYQllxVo(bdcXm);
        }
        return null;
    }

    @Override
    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {

        return null;
    }

}
