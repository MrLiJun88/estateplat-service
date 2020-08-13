package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.service.core.config.ValidateNodeConfigService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/27 0027
 * @description
 */
public class BdcXnBdcdyhValidateServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private ValidateNodeConfigService validateNodeConfigService;
    private static final String xnzdjh = "320281030005GB00187";//江阴虚拟宗地地籍号

    /**
     * @param param 项目信息
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */
    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = Maps.newHashMap();
        List<String> proidList = Lists.newArrayList();
        Project project = (Project) param.get("project");
        Boolean validateEnable = validateNodeConfigService.nodeValidateEnable(project, this.getCode());
        if (project != null && validateEnable) {
            if (StringUtils.isNotBlank(project.getBdcdyh()) && project.getBdcdyh().length() == 28) {
                if (yzdyh1(project.getBdcdyh()) || yzdyh2(project.getBdcdyh())) {
                    proidList.add(project.getYxmid());
                }
            }
            if (CollectionUtils.isNotEmpty(project.getBdcXmRelList()) && project.getBdcXmRelList().size() > 1) {
                for (BdcXmRel bdcXmRel : project.getBdcXmRelList()) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXmRel.getYproid());
                        if (yzdyh1(bdcBdcdy.getBdcdyh()) || yzdyh2(bdcBdcdy.getBdcdyh())) {
                            proidList.add(bdcXmRel.getYproid());
                        }
                    }
                }
            }
        }
//        proidList.add(project.getYxmid());
        if (CollectionUtils.isNotEmpty(proidList)) {
            removeDuplicate(proidList);
            map.put("info", proidList);
        }
        return map;
    }

    private boolean yzdyh1(String bdcdyh) {
        boolean sfxndyh = false;
        if (StringUtils.equals(xnzdjh, StringUtils.substring(bdcdyh, 0, 19))) {
            sfxndyh = true;
        }
        return sfxndyh;
    }

    private boolean yzdyh2(String bdcdyh) {
        boolean sfxndyh = false;
        String djqdjzq = StringUtils.substring(bdcdyh, 6, 12);
        String zdsxh = StringUtils.substring(bdcdyh, 14, 19);
        if (StringUtils.equals(djqdjzq, "000000") && StringUtils.equals(zdsxh, "00000")) {
            sfxndyh = true;
        }
        return sfxndyh;
    }

    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<String>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "199";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证是否虚拟单元号";
    }
}
