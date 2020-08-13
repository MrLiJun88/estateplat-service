package cn.gtmap.estateplat.server.service;/*
 * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
 * @version 1.0, 16-11-21
 * @description        国有建设用地及房屋所有权首次登记
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gtis.common.util.ByteObjectAccess;
import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.web.SplitParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

public class GyjsydFwsyqScdj extends BdcBaseUnitTest {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    ProjectService projectService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;

    //初始化数据
    @Test
    public void initData() {
        Project project = new Project();
        List<InsertVo> insertVoList = new LinkedList<InsertVo>();
        //填写项目的proid
        project.setProid("0BN946012NWLK602");
        project.setDjId("{642e3b67-8451-8635-9224-3805a71149b");
        project.setBdclx("TDFW");
        project.setBdcdyh("320705002011GB00530F00010126");
        String flag = "fail";
        if (StringUtils.isNotBlank(project.getProid())) {
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
            if (bdcXm != null) {
                if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.size() == 1) {
                        //zhouwanqing 多次选择不动产单元会出现冗余不动产单元和登记簿以及bdc_td的数据
                        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                            bdcdyService.delDjbAndTd(bdcXm);
                            bdcdyService.delXmBdcdy(bdcXm.getBdcdyid());
                        }
                    }
                    project = (Project) bdcXmService.getProjectFromBdcXm(bdcXm, project);
                }
            }
            project.setBdcdyid(null);
            List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
            if (CollectionUtils.isEmpty(project.getDjIds())) {
                BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                if (bdcXmRel != null)
                    bdcXmRelList.add(bdcXmRel);
            }

            project.setUserId("0");
            project.setBdcXmRelList(bdcXmRelList);
            //获取哪个登记类型service
            CreatProjectService creatProjectService = projectService.getCreatProjectService(project);
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
            insertVoList = creatProjectService.initVoFromOldData(project);
            if (insertVoList != null && insertVoList.size() > 0) {
                creatProjectService.saveOrUpdateProjectData(insertVoList);
            }

            List<BdcXm> bdcXmList = null;
            if (bdcXm != null && StringUtils.isNotBlank(project.getWiid())) {
                HashMap map = new HashMap();
                map.put("wiid", bdcXm.getWiid());
                bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
            }
            if (bdcXmList != null && bdcXmList.size() > 0) {
                for (BdcXm xm : bdcXmList) {
                    turnProjectDefaultService.saveQllxVo(xm);
                }
            }
            flag = "success";
            System.out.println("初始化数据：" + flag);
        } else {
            System.out.println("初始化数据：" + flag);
        }

    }

    //生成证书
    @Test
    public void creatZs() {
        String proid = "0BN946012NWLK602";
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
            turnProjectDefaultService.saveBdcZs(bdcXm);
        }
    }

    @Test
    public void select(){

        bdcXmService.getBdcXmListByWiid("43QE43279ZMVA401");

    }

    public static void main(String[] args) throws Exception {
      
        Double tdsyqmj =0.0;
        Double s = null;
        
        tdsyqmj+=s;
    }

    @Test
    public void testFile(){



    }

}
