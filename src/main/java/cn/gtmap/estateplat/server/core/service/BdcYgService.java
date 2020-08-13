package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcYg;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zx@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-23
 */
@Service
public interface BdcYgService {
    /**
     * zx 预告登记
     *
     * @param bdcdyh
     * @return
     */
    List<BdcYg> getBdcYgList(final String bdcdyh,final String qszt);


    /**
     * liujie 预告登记
     *
     * @param map
     * @return
     */
    List<BdcYg> getBdcYgList(HashMap map);

    /**
     * zx 更新预告登记
     *
     * @param bdcYg
     * @return
     */
    void updateBdcYg(BdcYg bdcYg);

    BdcYg getBdcYgByProid(final String proid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @param
     * @rerutn
     * @description     根据不动产单元权属状态预告登记类型查询预告
     */
    List<BdcYg> getBdcYgList(final String bdcdyh,final String qszt,final String ygdjlx);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存预告登记信息
    *@Date 16:04 2017/2/16
    */
    void saveYgxx(BdcYg bdcYg);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产预告信息页面
     */
    Model initBdcYgForPl(Model model, String qlid, BdcXm bdcXm);
}
