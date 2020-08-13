package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcBdcdySd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdysdMapper;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/12/14
 * @description 不动产单元冻结项目
 */
@Controller
@RequestMapping("/bdcdyDjXm")
public class BdcDjxmController extends BaseController {
    @Autowired
    Repo repository;
    @Autowired
    BdcBdcdysdMapper bdcBdcdysdMapper;
    @Autowired
    BdcXmService bdcXmService;

    @RequestMapping(value = "/bdcdyDjXmGl", method = RequestMethod.GET)
    public String bdcdyDjXmGl(Model model) {
        return "query/bdcdyDjXmList";
    }

    @ResponseBody
    @RequestMapping("/getBdcdyDjXmPagesJson")
    public Object getBdcdyDjXmPagesJson(Pageable pageable, String bh,String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(bh)) {
            map.put("bh", StringUtils.deleteWhitespace(bh));
        }
        if(StringUtils.isNotBlank(dcxc)){
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        }
        map.put("sqlx",Constants.SQLX_BDCDYDJ_DM);
        return repository.selectPaging("getBdcdyDjXmPagesJson", map, pageable);
    }


    /**
     * 0:未冻结，1：部分冻结，2：全部冻结
     *
     * @param proid
     * @return
     *
     */
    @ResponseBody
    @RequestMapping(value = "getBdcdyDjXmStatus")
    public String getBdcDjXmStatus(Model model, String proid) {
        String  bdcDjXmStatus = "0";
        String  bh = null;
        if(StringUtils.isNotBlank(proid)){
            HashMap hashMap = new HashMap();
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh())){
                hashMap.put("bh",bh);
            }
            List<BdcBdcdySd> bdcdySdList = bdcBdcdysdMapper.getBdcdySdListByMap(hashMap);
            if(CollectionUtils.isNotEmpty(bdcdySdList)){
                int bdcdyZcSum = 0;
                for (BdcBdcdySd bdcBdcdySd:bdcdySdList){
                    if(StringUtils.equals(bdcBdcdySd.getXzzt(), Constants.XZZT_ZC)) {
                        bdcdyZcSum++;
                    }
                }

                if(bdcdySdList.size() == bdcdyZcSum) {
                    bdcDjXmStatus = "0";
                }
                else if(bdcdyZcSum < bdcdySdList.size()) {
                    bdcDjXmStatus = "1";
                }
                if(bdcdyZcSum == 0) {
                    bdcDjXmStatus = "2";
                }
            }
        }
        return bdcDjXmStatus;
    }

}
