package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.BdcFwfsss;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记抵押权权利服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-11
 */
public interface BdcDyaqService {
    /**
     * zdd 只更新注销登记的抵押信息
     *
     * @param bdcDyaq
     */
    void updateBdcDyaqForZxdj(final BdcDyaq bdcDyaq);


    /**
     * 获取不动产抵押权登记信息
     *
     * @param map
     * @return
     */
    List<BdcDyaq> queryBdcDyaq(final Map map);

    /**
     * 通过proid获取原项目的对应的某权属状态的抵押权数据
     * @author lisongtao
     * @param proid 流程id
     * @param qszt 权属状态
     * @return
     */
    List<BdcDyaq> queryYdyaqByProid(final String proid,final Integer qszt);
    

    /**
     * @author xiejianan
     * @description 从过渡库和登记库里获取抵押权登记信息
     * @param map
     * @return
     */
    List<Map> queryBdcDyaqByPage(final Map map);

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @param map
     * @rerutn
     * @description 通过fwid和wiid确定当前项目下的抵押权
     */
    BdcDyaq queryBdcDyaqByFwid(HashMap map);

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @param proid
     * @rerutn
     * @description 通过proid确定当前项目下的抵押权
     */
    BdcDyaq queryBdcDyaqByProid(String proid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产抵押信息页面
     */
    Model initBdcDyaqForPl(Model model,String qlid,BdcXm bdcXm);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存抵押信息
    *@Date 11:25 2017/2/15
    */
    void saveBdcDyaq(BdcDyaq bdcDyaq);

    /**
     *@Author:<a href="mailto:yanzhnekun@gtmap.cn">yanzhenkun</a>
     *@Description:获取本地打印需要的抵押信息
     */
    Map<String,Object> getDyaqxxForPrint(String proid);

    /**
     *@Author:<a href="mailto:yanzhnekun@gtmap.cn">yanzhenkun</a>
     *@Description:继承附属设施的情况下处理抵押面积
     */
    void  dealInheritFsssForDyaq(String proid, List<BdcFwfsss> bdcFwfsssList);

    /**
     *@Author:<a href="mailto:yanzhnekun@gtmap.cn">yanzhenkun</a>
     *@Description:不继承附属设施的情况下处理抵押面积和spxx里面的面积
     */
    void  dealNotInheritFsssForDyaq(String proid,String yproid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @version
     * @param proidList bdcdyaq
     * @return
     * @description 将债权期限存入每个项目
     */
    void saveZqqxForEveryPoid(List<String> proidList,BdcDyaq bdcDyaq);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @version
     * @return
     * @description 验证是否存在抵押权
     */
    Boolean validateDyaq(String bdcdyid);
}
