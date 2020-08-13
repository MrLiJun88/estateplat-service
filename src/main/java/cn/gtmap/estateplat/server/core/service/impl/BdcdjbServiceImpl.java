package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDyMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcdjbMapper;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcdjbService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-18
 */
@Repository
public class BdcdjbServiceImpl implements BdcdjbService {

    @Autowired
    private BdcdjbMapper bdcdjbMapper;
    @Autowired
    private BdcDyMapper bdcDyMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private DjsjFwService djsjFwService;


    @Override
    @Transactional(readOnly = true)
    public List<BdcBdcdjb> selectBdcdjb(final String zdzhh) {
        List<BdcBdcdjb> bdcdjbs = null;
        if (StringUtils.isNotBlank(zdzhh)) {
            bdcdjbs = bdcdjbMapper.selectBdcdjb(zdzhh);
        }
        return bdcdjbs;
    }

    @Override
    public BdcBdcdjb getBdcdjbFromZdxx(DjsjZdxx djsjZdxx, BdcBdcdjb bdcdjb) {
        if (djsjZdxx != null) {
            if (bdcdjb == null)
                bdcdjb = new BdcBdcdjb();

            if (StringUtils.isBlank(bdcdjb.getDjbid()))
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            if (StringUtils.isNotBlank(djsjZdxx.getZdsz()))
                bdcdjb.setWzsm(djsjZdxx.getZdsz());
            if (StringUtils.isNotBlank(djsjZdxx.getDjh()))
                bdcdjb.setZdzhh(djsjZdxx.getDjh());
            if (StringUtils.isNotBlank(djsjZdxx.getTdzl()))
                bdcdjb.setZl(djsjZdxx.getTdzl());
        }
        return bdcdjb;
    }

    @Override
    public BdcBdcdjb getBdcdjbFromLqxx(DjsjLqxx djsjLqxx, BdcBdcdjb bdcdjb) {
        if (djsjLqxx != null) {
            if (bdcdjb == null)
                bdcdjb = new BdcBdcdjb();
            if (StringUtils.isBlank(bdcdjb.getDjbid()))
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            if (StringUtils.isNotBlank(djsjLqxx.getZl()))
                bdcdjb.setZl(djsjLqxx.getZl());
        }
        return bdcdjb;
    }

    @Override
    public BdcBdcdjb getBdcdjbFromFwxx(DjsjFwxx djsjFwxx, BdcBdcdjb bdcdjb) {
        if (djsjFwxx != null) {
            if (bdcdjb == null)
                bdcdjb = new BdcBdcdjb();

            if (StringUtils.isBlank(bdcdjb.getDjbid()))
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && djsjFwxx.getBdcdyh().length() > 19)
                bdcdjb.setZdzhh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
        }
        return bdcdjb;
    }

    @Override
    public BdcBdcdjb getBdcdjbFromProject(Project project, BdcBdcdjb bdcdjb) {
        if (bdcdjb == null)
            bdcdjb = new BdcBdcdjb();
        if (StringUtils.isBlank(bdcdjb.getDjbid())) {
            if (StringUtils.isNotBlank(project.getDjbid())) {
                bdcdjb.setDjbid(project.getDjbid());
            } else {
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            }
        }
        if (project == null)
            return bdcdjb;
        if (StringUtils.isNotBlank(project.getCjr()))
            bdcdjb.setDbr(project.getCjr());
        if (project.getCjsj() != null)
            bdcdjb.setDjsj(project.getCjsj());
        if (StringUtils.isNotBlank(project.getZdzhh()))
            bdcdjb.setZdzhh(project.getZdzhh());
        bdcdjb.setProid(project.getProid());
        return bdcdjb;
    }

    /**
     * zdd 将项目信息读取到不动产登记薄
     *
     * @param projectPar
     * @param bdcdjb
     * @return
     */
    @Override
    public BdcBdcdjb getBdcdjbFromProjectPar(ProjectPar projectPar, BdcBdcdjb bdcdjb) {
        if (projectPar == null) {
            return null;
        }
        if (bdcdjb == null) {
            bdcdjb = new BdcBdcdjb();
        }
        if (StringUtils.isBlank(bdcdjb.getDjbid())) {
            if (StringUtils.isNotBlank(projectPar.getDjbid())) {
                bdcdjb.setDjbid(projectPar.getDjbid());
            } else {
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            }
        }
        if (StringUtils.isNotBlank(projectPar.getCjr()))
            bdcdjb.setDbr(projectPar.getCjr());
        if (projectPar.getCjsj() != null)
            bdcdjb.setDjsj(projectPar.getCjsj());
        if (StringUtils.isNotBlank(projectPar.getDjh()))
            bdcdjb.setZdzhh(projectPar.getDjh());
        bdcdjb.setProid(projectPar.getProid());
        return bdcdjb;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcBdcdjb> getBdcdjbByPage(final Map map) {
        return bdcdjbMapper.getBdcdjbByPage(map);
    }

    @Override
    @Transactional(readOnly = true)
    public String getJosnByDjbList(final Map map) {
        StringBuilder json = new StringBuilder("[");
        List<BdcBdcdjb> djbList = getBdcdjbByPage(map);
        if (CollectionUtils.isNotEmpty(djbList)) {
            for (int i = 0; i < djbList.size(); i++) {
                BdcBdcdjb djb = djbList.get(i);
                json.append("{zdzhh:'").append(CommonUtil.formatEmptyValue(djb.getZdzhh())).append("',dbr:'").
                        append(CommonUtil.formatEmptyValue(djb.getDbr())).append("',zl:'").append(CommonUtil.formatEmptyValue(djb.getZl())).append("',ck:'<button  class=\"button\" onclick=\"doneCf(this)\" style=\"border:0;\" title=\"").append(djb.getDjbid()).append("\"><i class=\"icon icon-wrench\"></i></button>'");
                if (StringUtils.isNotBlank(djb.getZdzhh())) {
                    List<BdcDyForQuery> bdcBdcdyItems = bdcDyMapper.queryBdcdyByZdzhh(djb.getDjbid());
                    if (CollectionUtils.isNotEmpty(bdcBdcdyItems)) {
                        json.append(",records:[");
                        for (int j = 0; j < bdcBdcdyItems.size(); j++) {
                            BdcDyForQuery bdcBdcdy = bdcBdcdyItems.get(j);
                            json.append("{dyh:'").append(CommonUtil.formatEmptyValue(bdcBdcdy.getBdcdyh())).append("',lx:'").
                                    append(CommonUtil.formatEmptyValue(bdcBdcdy.getBdclx())).append(CommonUtil.getZszt(CommonUtil.formatEmptyValue(bdcBdcdy.getQszt()))).append("',dy:'<button  class=\"button\" onclick=\"doneCf(this)\" style=\"border:0;\" title=\"").append(bdcBdcdy.getBdcdyid()).append("\"><i class=\"icon icon-wrench\"></i></button>'}");
                            if (bdcBdcdyItems.size() - j > 1) {
                                json.append(",");
                            }
                        }
                        json.append("]");
                    }
                }
                if (djbList.size() - i <= 1) {
                    json.append("}");
                } else {
                    json.append("},");
                }
            }
        }
        json.append("]");
        return json.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public BdcBdcdjb getBdcBdcdjbByDjbid(final String djbid) {
        return StringUtils.isNotBlank(djbid) ? entityMapper.selectByPrimaryKey(BdcBdcdjb.class, djbid) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map> getQldjByPage(final Map map) {
        return bdcdjbMapper.getQldjByPage(map);
    }

    @Override
    @Transactional
    public void updateDjb(final String proid, final String userName, final QllxVo qllxVo) {
        if (StringUtils.isBlank(proid)) return;
        BdcBdcdy bdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
        if (bdcdy != null) {
            List<BdcBdcdjb> bdcBdcdjbList = selectBdcdjb(StringUtils.substring(bdcdy.getBdcdyh(), 0, 19));
            BdcBdcdjb bdcBdcdjb = null;
            if (CollectionUtils.isNotEmpty(bdcBdcdjbList))
                bdcBdcdjb = bdcBdcdjbList.get(0);
            if (bdcBdcdjb != null) {
                bdcBdcdjb.setDbr(userName);
                bdcBdcdjb.setDjsj(qllxVo.getDjsj());
                bdcBdcdjb.setFj(qllxVo.getFj());
                entityMapper.saveOrUpdate(bdcBdcdjb, bdcBdcdjb.getDjbid());
            }
        }
    }


    @Override
    public BdcBdcdjb getBdcdjbFromQsdcb(DjsjQszdDcb djsjQszdDcb, BdcBdcdjb bdcdjb) {
        if (djsjQszdDcb != null) {
            if (bdcdjb == null)
                bdcdjb = new BdcBdcdjb();
            if (StringUtils.isNotBlank(djsjQszdDcb.getZdsz()))
                bdcdjb.setWzsm(djsjQszdDcb.getZdsz());
            if (StringUtils.isNotBlank(djsjQszdDcb.getDjh()))
                bdcdjb.setZdzhh(djsjQszdDcb.getDjh());
            if (StringUtils.isNotBlank(djsjQszdDcb.getTdzl()))
                bdcdjb.setZl(djsjQszdDcb.getTdzl());
        }
        return bdcdjb;

    }

    @Override
    public BdcBdcdjb getBdcdjbFromNydZdxx(List<DjsjNydDcb> djsjNydDcbList, BdcBdcdjb bdcdjb) {
        if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
            DjsjNydDcb djsjNydDcb = djsjNydDcbList.get(0);
            if (bdcdjb == null)
                bdcdjb = new BdcBdcdjb();

            if (StringUtils.isBlank(bdcdjb.getDjbid()))
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            if (StringUtils.isNotBlank(djsjNydDcb.getZdsz()))
                bdcdjb.setWzsm(djsjNydDcb.getZdsz());
            if (StringUtils.isNotBlank(djsjNydDcb.getDjh()))
                bdcdjb.setZdzhh(djsjNydDcb.getDjh());
            if (StringUtils.isNotBlank(djsjNydDcb.getTdzl()))
                bdcdjb.setZl(djsjNydDcb.getTdzl());
        }
        return bdcdjb;
    }

    @Override
    public BdcBdcdjb getBdcdjbFromZhxx(DjsjZhxx djsjZhxx, BdcBdcdjb bdcdjb) {
        if (djsjZhxx != null) {
            if (bdcdjb == null)
                bdcdjb = new BdcBdcdjb();

            if (StringUtils.isBlank(bdcdjb.getDjbid()))
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            if (StringUtils.isNotBlank(djsjZhxx.getYhwzsm()))
                bdcdjb.setWzsm(djsjZhxx.getYhwzsm());
            if (StringUtils.isNotBlank(djsjZhxx.getZhdm()))
                bdcdjb.setZdzhh(djsjZhxx.getZhdm());
            if (StringUtils.isNotBlank(djsjZhxx.getYhwzsm()))
                bdcdjb.setZl(djsjZhxx.getHdwz());
        }
        return bdcdjb;
    }

    @Override
    public void delBdcdjbByZdzhh(final String zdzhh) {
        if (StringUtils.isNotBlank(zdzhh)) {
            Example example = new Example(BdcBdcdjb.class);
            example.createCriteria().andEqualTo("zdzhh", zdzhh);
            entityMapper.deleteByExample(BdcBdcdjb.class, example);
        }
    }

    @Override
    public BdcBdcdjb getBdcdjbFromGzwxx(DjsjGzwxx djsjGzwxx, BdcBdcdjb bdcdjb) {
        if (djsjGzwxx != null) {
            if (bdcdjb == null)
                bdcdjb = new BdcBdcdjb();
            if (StringUtils.isBlank(bdcdjb.getDjbid()))
                bdcdjb.setDjbid(UUIDGenerator.generate18());
            if (StringUtils.isNotBlank(djsjGzwxx.getZl()))
                bdcdjb.setZl(djsjGzwxx.getZl());
        }
        return bdcdjb;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initBdcBdcdjb(Xmxx xmxx) {
//        if(xmxx instanceof Project){
//            Project project = (Project) xmxx;
//            // 如果直接修改project 对象，有可能会在后面程序影响程序对于房屋类型的判断，
//            // 因此，通过深复制project，通过操作project的拷贝来创建登记簿信息。
//            List<Project> projects = new ArrayList<Project>();
//            getFwhsXxByDcbIndex(project, projects);
//            getFwhsXxByDjid(project, projects);
//            List<InitVoFromParm> initVoFromParms = new ArrayList<InitVoFromParm>();
//            for(Project projectTemp : projects){
//                InitVoFromParm initVoFromParm = getDjxx(projectTemp);
//                initVoFromParms.add(initVoFromParm);
//            }
//            initVoFromParms = singleZdzhhFilter(initVoFromParms);
//            for(InitVoFromParm initVoFromParm : initVoFromParms){
//                if(initVoFromParm != null && initVoFromParm.getProject() != null && StringUtils.isNotBlank(initVoFromParm.getProject().getZdzhh())){
//                    List<BdcBdcdjb> bdcBdcdjbListTemp = bdcdjbMapper.selectBdcdjb(initVoFromParm.getProject().getZdzhh());
//                    if(CollectionUtils.isEmpty(bdcBdcdjbListTemp)){
//                        BdcBdcdjb bdcBdcdjb = initBdcdjb(initVoFromParm);
//                        entityMapper.saveOrUpdate(bdcBdcdjb, bdcBdcdjb.getDjbid());
//                    }
//                }
//            }
//        }
    }

    @Override
    public void initBdcBdcdjb(final Xmxx xmxx, final List<String> zdzhhList) {
        if (xmxx instanceof Project) {
            final Project project = (Project) xmxx;
            for (String zdzhh : zdzhhList) {
                InitVoFromParm initVoFromParm = getDjxx(zdzhh, project);
                if (initVoFromParm.getProject() != null && StringUtils.isNotBlank(zdzhh)) {
                    List<BdcBdcdjb> bdcBdcdjbList = bdcdjbMapper.selectBdcdjb(zdzhh);
                    if (CollectionUtils.isEmpty(bdcBdcdjbList)) {
                        BdcBdcdjb bdcBdcdjb = initBdcdjb(initVoFromParm, zdzhh);
                        if (bdcBdcdjb != null && StringUtils.isNotBlank(bdcBdcdjb.getDjbid())) {
                            entityMapper.saveOrUpdate(bdcBdcdjb, bdcBdcdjb.getDjbid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public BdcBdcdjb initBdcdjb(InitVoFromParm initVoFromParm,final String fwProid,final String tdProid,String zdzhh,final String userid) {
        BdcBdcdjb bdcdjb = null;
        if (initVoFromParm != null && StringUtils.isNotBlank(zdzhh)) {
            bdcdjb = new BdcBdcdjb();
            bdcdjb.setDjbid(UUIDGenerator.generate18());
            if (StringUtils.isNotBlank(userid)){
                bdcdjb.setDbr(PlatformUtil.getCurrentUserName(userid));
            }
            bdcdjb.setDjsj(new Date());
            if (StringUtils.isNotBlank(zdzhh)){
                bdcdjb.setZdzhh(zdzhh);
            }
            if(StringUtils.isNotBlank(fwProid)){
                bdcdjb.setProid(fwProid);
            }else if(StringUtils.isNotBlank(tdProid)){
                bdcdjb.setProid(tdProid);
            }
            bdcdjb = getBdcdjbFromProject(initVoFromParm.getProject(), bdcdjb);
            bdcdjb = getBdcdjbFromQsdcb(initVoFromParm.getDjsjQszdDcb(), bdcdjb);
            bdcdjb = getBdcdjbFromNydZdxx(initVoFromParm.getDjsjNydDcbList(), bdcdjb);
            bdcdjb = getBdcdjbFromZdxx(initVoFromParm.getDjsjZdxx(), bdcdjb);
            bdcdjb = getBdcdjbFromFwxx(initVoFromParm.getDjsjFwxx(), bdcdjb);
            bdcdjb = getBdcdjbFromLqxx(initVoFromParm.getDjsjLqxx(), bdcdjb);
            bdcdjb = getBdcdjbFromZhxx(initVoFromParm.getDjsjZhxx(), bdcdjb);
        }
        return bdcdjb;
    }

    @Override
    public void saveBdcBdcdjb(BdcBdcdjb bdcBdcdjb) {
        if(bdcBdcdjb != null && StringUtils.isNotBlank(bdcBdcdjb.getDjbid())){
            entityMapper.saveOrUpdate(bdcBdcdjb,bdcBdcdjb.getDjbid());
        }
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @Description: 宗地参数过滤器。由于不动产登记簿数据有唯一的宗地宗海号，因此，相同的宗地宗海号是没有必要重复验证保存的
     * 虽然做了验证，但是这样会大幅度影响性能。
     */
    private List<InitVoFromParm> singleZdzhhFilter(List<InitVoFromParm> initVoFromParms) {
        List<InitVoFromParm> initVoFromParmListTemp = new ArrayList<InitVoFromParm>();
        List<String> list = new ArrayList<String>();
        for (InitVoFromParm initVoFromParm : initVoFromParms) {
            if (initVoFromParm != null && initVoFromParm.getProject() != null && StringUtils.isNotBlank(initVoFromParm.getProject().getZdzhh())) {
                if (!list.contains(initVoFromParm.getProject().getZdzhh())) {
                    initVoFromParmListTemp.add(initVoFromParm);
                    list.add(initVoFromParm.getProject().getZdzhh());
                }
            }
        }
        return initVoFromParmListTemp;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @Description: 对逻辑幢数据，通过fw_dcb_index获取djid,保证getDjxx的统一处理
     * 我们通过对project 的深复制，保证原始的project不被修改。
     */
    private void getFwhsXxByDcbIndex(Project project, List<Project> projects) {
        String dcbIndex = project.getDcbIndex();
        List<String> dcbIndexes = project.getDcbIndexs();
        if (StringUtils.isNotBlank(dcbIndex) || CollectionUtils.isNotEmpty(dcbIndexes)) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(dcbIndex) && CollectionUtils.isNotEmpty(dcbIndexes)) {
                if (!dcbIndexes.contains(dcbIndex)) {
                    dcbIndexes.add(dcbIndex);
                }
                map.put("fw_dcb_indexs", dcbIndexes);
            } else if (StringUtils.isNotBlank(dcbIndex)) {
                map.put("fw_dcb_index", dcbIndex);
            } else if (CollectionUtils.isNotEmpty(dcbIndexes)) {
                map.put("fw_dcb_indexs", dcbIndexes);
            }
            List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(map);
            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                Project newProject = null;
                try {
                    newProject = (Project) BeanUtils.cloneBean(project);
                } catch (Exception e) {
                }
                String djId = djsjFwHs.getFwHsIndex();
                String bdcdyh = djsjFwHs.getBdcdyh();
                if (newProject != null) {
                    newProject.setDjId(djId);
                    newProject.setBdcdyh(bdcdyh);
                    newProject.setZdzhh(djsjFwHs.getLszd());
                    // 这里的本质是在为bdc_bdcdjb.proid 赋值 暂时赋值成一样的proid.
                    newProject.setProid(project.getProid());
                    projects.add(newProject);
                }
            }
        }

    }

    /**
     * @param
     * @return
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @Description: 深复制project ，并且赋予每个project 自己的djid。如果 djid和djids发生重复也无妨，因为在后期
     * 会对zdzhh做过滤。
     */
    private void getFwhsXxByDjid(Project project, List<Project> projects) {
        if (StringUtils.isNotBlank(project.getDjId()) && StringUtils.isNotBlank(project.getBdcdyh())) {
            Project newProject = null;
            try {
                newProject = (Project) BeanUtils.cloneBean(project);
            } catch (Exception e) {
            }
            if (newProject != null) {
                newProject.setDjId(project.getDjId());
                newProject.setBdcdyh(project.getBdcdyh());
                newProject.setProid(project.getProid());
                if (project.getBdcdyh().length() > 19 && StringUtils.isBlank(project.getZdzhh())) {
                    newProject.setZdzhh(project.getBdcdyh().substring(0, 19));
                }
                projects.add(newProject);
            }
        }
        if (CollectionUtils.isNotEmpty(project.getDjIds()) && CollectionUtils.isNotEmpty(project.getBdcdyhs())) {
            List<String> djids = project.getDjIds();
            List<String> bdcdyhs = project.getBdcdyhs();
            if (CollectionUtils.isNotEmpty(djids) && CollectionUtils.isNotEmpty(bdcdyhs)
                    && djids.size() == bdcdyhs.size() && djids.size() == 1 && djids.get(0).contains("\\$")) {
                String[] djidArray = StringUtils.split(djids.get(0), "\\$");
                String[] bdcdyhArray = StringUtils.split(bdcdyhs.get(0), "\\$");
                djids = Arrays.asList(djidArray);
                bdcdyhs = Arrays.asList(bdcdyhArray);
            }
            if (CollectionUtils.isNotEmpty(djids) && CollectionUtils.isNotEmpty(bdcdyhs)
                    && djids.size() == bdcdyhs.size()) {
                for (int i = 0; i < djids.size(); i++) {
                    Project newProject = null;
                    try {
                        newProject = (Project) BeanUtils.cloneBean(project);
                    } catch (Exception e) {
                    }
                    if (newProject != null) {
                        newProject.setDjId(djids.get(i));
                        newProject.setBdcdyh(bdcdyhs.get(i));
                        newProject.setProid(project.getProid());
                        if (project.getBdcdyh().length() > 19 && StringUtils.isBlank(project.getZdzhh())) {
                            newProject.setZdzhh(project.getBdcdyh().substring(0, 19));
                        }
                        projects.add(newProject);
                    }
                }
            }
        }
    }

    private InitVoFromParm getDjxx(final String zdzhh, final Project project) {
        DjsjZdxx djsjZdxx = null;
        DjsjQszdDcb djsjQszdDcb = null;
        List<DjsjZdxx> djsjZdxxList;
        List<DjsjQszdDcb> djsjQszdDcbList;
        InitVoFromParm initVoFromParm = new InitVoFromParm();
        // 土地房屋中的房屋部分
        djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(zdzhh);
        // 土地房屋中的土地部分
        djsjQszdDcbList = bdcDjsjService.getDjsjQszdDcb(zdzhh);
        if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
            djsjQszdDcb = djsjQszdDcbList.get(0);
        }
        if (CollectionUtils.isEmpty(djsjZdxxList)) {
            djsjZdxxList = bdcDjsjService.getDjsjNydxxByDjh(zdzhh);
        }
        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            djsjZdxx = djsjZdxxList.get(0);
        }
        initVoFromParm.setProject(project);
        initVoFromParm.setDjsjQszdDcb(djsjQszdDcb);
        initVoFromParm.setDjsjZdxx(djsjZdxx);
        return initVoFromParm;
    }

    private BdcBdcdjb initBdcdjb(final InitVoFromParm initVoFromParm, final String zdzhh) {
        BdcBdcdjb bdcdjb = null;
        //zdd 如果宗地宗海号为空  则不生成
        if (initVoFromParm != null && initVoFromParm.getProject() != null && StringUtils.isNotBlank(zdzhh)) {
            bdcdjb = getBdcdjbFromProject(initVoFromParm.getProject(), bdcdjb);
            bdcdjb = getBdcdjbFromQsdcb(initVoFromParm.getDjsjQszdDcb(), bdcdjb);
            bdcdjb = getBdcdjbFromZdxx(initVoFromParm.getDjsjZdxx(), bdcdjb);
            if (StringUtils.isBlank(bdcdjb.getZdzhh())) {
                bdcdjb.setZdzhh(zdzhh);
            }
        }
        return bdcdjb;
    }
}
