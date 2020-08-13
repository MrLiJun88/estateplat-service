package cn.gtmap.estateplat.server.service.split;

import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 拆分数据
 */
public interface BdcSplitService {
    /**
     * @param proid 项目ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据
     */
    Map bdcSplit(List<SplitNum> splitNumList, String proid);

    /**
     * @param fgid 分割ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 撤回拆分
     */
    String revokeBdcSplit(String fgid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 初始化土地录入数据页面
     */
    String initTdSplit(Model model, String proid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据proid获取分割id
     */
    Map getFgidByproid(String proid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 验证是否可以进行撤回
     */
    Map validateWithdrawFg(String fgid);
}
