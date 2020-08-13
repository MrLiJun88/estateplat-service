package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hqz
 * Date: 15-3-17
 * Time: 下午6:23
 * Des:在建建筑物方法
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcZjjzw")
public class BdcZjjzwController extends BaseController {

    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcXmService bdcXmService;
	@Autowired
	private DjsjFwService djsjFwService;

     @Autowired
     private BdcdyService bdcdyService;

    /**
     * hqz
     *在建工程抵押物清单插入
     * @param project
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createZjjzwxx")
    public String createZjjzwxx(Project project) {
        String msg = "失败";
		//直接选择逻辑幢的情况
        if (project != null && CollectionUtils.isNotEmpty(project.getDcbIndexs()) ) {
			try {
                List<String> djsjIds = new ArrayList<String>();
                List<String> bdcdys = new ArrayList<String>();
				HashMap map = new HashMap();
                map.put("fw_dcb_indexs", project.getDcbIndexs());
                List<DjsjFwHs> djsjFwYcHsList = djsjFwService.getDjsjFwYcHs(map);
                if (CollectionUtils.isNotEmpty(djsjFwYcHsList)) {
                    for (DjsjFwHs djsjYcFwHs : djsjFwYcHsList) {
                        djsjIds.add(djsjYcFwHs.getFwHsIndex());
                        bdcdys.add(djsjYcFwHs.getBdcdyh());
                    }
                }
                project.getBdcdyhs().addAll(bdcdys);
                project.getDjIds().addAll(djsjIds);
			} catch (Exception e) {
				logger.error("BdcZjjzwController.createZjjzwxx",e);
			}
		}
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            String  proid = project.getProid();
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            String zjjzwfw= "";
            if(bdcXm!=null && CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, bdcXm.getSqlx())){
                zjjzwfw="true";
            }

            if(bdcXm!=null && StringUtils.equals(bdcXm.getXmly(),"1")) {//项目来源自业务平台
                if (project.getDjIds() != null) {
                    int xh = 1;
                    for (String bdcdyh : project.getBdcdyhs()) {
                        if(StringUtils.isNotBlank(bdcdyh)) {
                            if(StringUtils.equals(zjjzwfw,"true")){
                                BdcBdcdy bdcBdcdy=bdcdyService.queryBdcdyByBdcdyh(bdcdyh);
                                if(bdcBdcdy != null){
                                    String proids=bdcXmService.getProidsByProid(proid);
                                    if(StringUtils.isNotBlank(proids)){
                                        for(String proidTemp:StringUtils.split(proids,Constants.SPLIT_STR)){
                                            BdcXm bdcXm1=bdcXmService.getBdcXmByProid(proidTemp);
                                            if(StringUtils.equals(bdcXm1.getBdcdyid(),bdcBdcdy.getBdcdyid())){
                                                bdcZjjzwxxService.createZjjzwxx(proidTemp, xh + "", bdcdyh);
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                bdcZjjzwxxService.createZjjzwxx(proid, xh + "", bdcdyh);
                            }
                            xh++;
                        }else{
                            msg = "存在户室没有不动产单元号";
                        }
                    }
                } else {
                    msg = "存在户室没有不动产单元号";
                }
            }
            else if(bdcXm!=null&&project.getDjIds() != null){//否则来自过渡数据
                //获取多个不动产单元号，没有不动产单元号不予添加
                if (CollectionUtils.isNotEmpty(project.getBdcdyhs())) {
                    int xh = 1;
                    for (String bdcdyh : project.getBdcdyhs()) {
                        if(StringUtils.isNotBlank(bdcdyh)) {
                            bdcZjjzwxxService.createZjjzwxx(proid, xh + "", bdcdyh);
                            xh++;
                        }else{
                            msg = "存在户室没有不动产单元号";
                        }
                    }
                }else{
                    msg = "存在户室没有不动产单元号";
                }
            }
            msg = "成功";
        }
        return msg;
    }

}
