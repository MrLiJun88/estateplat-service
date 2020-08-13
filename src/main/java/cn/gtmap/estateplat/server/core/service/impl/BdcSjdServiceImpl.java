package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcSjxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.model.FileModel;
import cn.gtmap.estateplat.server.model.FolderModel;
import cn.gtmap.estateplat.server.model.FoldersModel;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-19
 */
@Service
public class BdcSjdServiceImpl implements BdcSjdService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcSjxxMapper bdcSjxxMapper;

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String PARAMETER_SJXXID = "sjxxid";


    @Override
    @Transactional(readOnly = true)
    public List<BdcSjcl> getSjclListByWiid(final String wiid,final String proid) {
        List<BdcSjcl> bdcSjclList = null;
        Example bdcSjxx = new Example(BdcSjxx.class);
        bdcSjxx.createCriteria().andEqualTo("wiid", wiid);
        List<BdcSjxx> bdcSjxxList = entityMapper.selectByExample(BdcSjxx.class, bdcSjxx);
        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
            String sjxxid = bdcSjxxList.get(0).getSjxxid();
            Example bdcSjcl = new Example(BdcSjcl.class);
            bdcSjcl.createCriteria().andEqualTo(PARAMETER_SJXXID, sjxxid);
            bdcSjclList = entityMapper.selectByExample(BdcSjcl.class, bdcSjcl);
        }
        return bdcSjclList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcSjxx> queryBdcSjdByWiid(final String wiid) {
        List<BdcSjxx> bdcSjxxList = null;
        if (StringUtils.isNotBlank(wiid)) {
            Example bdcSjxx = new Example(BdcSjxx.class);
            bdcSjxx.createCriteria().andEqualTo("wiid", wiid);
            bdcSjxxList = entityMapper.selectByExample(BdcSjxx.class, bdcSjxx);
        }
        return bdcSjxxList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcSjcl> getSjclListBySjxxid(String sjxxid) {
        List<BdcSjcl> bdcSjclList = null;
        if (StringUtils.isNotBlank(sjxxid)) {
            Example bdcSjcl = new Example(BdcSjcl.class);
            bdcSjcl.createCriteria().andEqualTo(PARAMETER_SJXXID, sjxxid);
            bdcSjclList = entityMapper.selectByExample(BdcSjcl.class, bdcSjcl);
        }
        return bdcSjclList;
    }

    @Override
    @Transactional
    public void delSjclListBySjxxid(final String sjxxid) {
        if (StringUtils.isNotBlank(sjxxid)) {
            Example bdcSjcl = new Example(BdcSjcl.class);
            bdcSjcl.createCriteria().andEqualTo(PARAMETER_SJXXID, sjxxid);
            entityMapper.deleteByExample(BdcSjcl.class, bdcSjcl);
        }

    }


    @Override
    @Transactional
    public void delBdcSjxxBySjxxid(final String sjxxid) {
        if (StringUtils.isNotBlank(sjxxid))
            entityMapper.deleteByPrimaryKey(BdcSjxx.class, sjxxid);
    }

    @Override
    @Transactional
    public void saveSjcl(BdcSjcl bdcSjcl) {
        if (bdcSjcl != null) {
            if (StringUtils.isBlank(bdcSjcl.getSjclid()))
                bdcSjcl.setSjclid(UUIDGenerator.generate());
            entityMapper.updateByPrimaryKeySelective(bdcSjcl);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcSjclRel> getSjclRelList(final String sqlxdm, final String ysqlxdm) {
        List<BdcSjclRel> bdcSjclRelList = null;
        if (StringUtils.isNotBlank(sqlxdm) && StringUtils.isNotBlank(ysqlxdm)) {
            Example bdcSjcl = new Example(BdcSjclRel.class);
            bdcSjcl.createCriteria().andEqualTo("sqlxdm", sqlxdm);
            bdcSjcl.createCriteria().andEqualTo("ysqlxdm", ysqlxdm);
            bdcSjclRelList = entityMapper.selectByExample(BdcSjclRel.class, bdcSjcl);
        }
        return bdcSjclRelList;
    }

    @Override
    @Transactional
    public void updateSjclFromYpro(final String proid, final String yproid) {
        if (StringUtils.isNotBlank(yproid) && StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm==null)
                throw new AppException(4006);
            List<BdcSjcl> bdcSjclList = getSjclListByWiid(bdcXm.getWiid(), bdcXm.getProid());
            List<BdcSjxx> bdcSjxxList = queryBdcSjdByWiid(bdcXm.getWiid());
            Integer yproject_fileId = PlatformUtil.getProjectFileId(yproid);
            Integer project_fileId = PlatformUtil.getProjectFileId(proid);
            if (CollectionUtils.isEmpty(bdcSjxxList)) {
                BdcSjxx bdcSjxx = new BdcSjxx();
                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                bdcSjxx.setWiid(bdcXm.getWiid());
                addBdcSjxx(bdcSjxx);
            }
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
            if (ybdcXm != null && StringUtils.isNotBlank(ybdcXm.getSqlx())&& StringUtils.isNotBlank(bdcXm.getSqlx())) {
                List<BdcSjclRel> bdcSjclRelList = getSjclRelList(bdcXm.getSqlx(), ybdcXm.getSqlx());
                if (CollectionUtils.isNotEmpty(bdcSjclRelList) && CollectionUtils.isNotEmpty(bdcSjclList)) {
                    for (BdcSjclRel bdcSjclRel : bdcSjclRelList) {
                        boolean hasSjcl = false;
                        int xh = 0;
                        BdcSjcl sjcl = null;
                        for (BdcSjcl bdcSjcl : bdcSjclList) {
                            if (StringUtils.equals(bdcSjcl.getClmc(), bdcSjclRel.getSjclmc())) {
                                hasSjcl = true;
                                sjcl = bdcSjcl;
                            }
                            if (bdcSjcl.getXh() > xh)
                                xh = bdcSjcl.getXh();
                        }
                        if (hasSjcl && sjcl != null) {
                            //只继承收件材料存在材料
                            sjcl.setFs(PlatformUtil.getAllChildFilesCountByNodeName(yproject_fileId, bdcSjclRel.getYsjclmc()));
                            PlatformUtil.copyNodeToNode(yproject_fileId, bdcSjclRel.getYsjclmc(), project_fileId, bdcSjclRel.getSjclmc(), false);
                            saveSjcl(sjcl);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void addBdcSjxx(BdcSjxx bdcSjxx) {
        if (bdcSjxx != null) {
            if (StringUtils.isBlank(bdcSjxx.getSjxxid()))
                bdcSjxx.setSjxxid(UUIDGenerator.generate());
            entityMapper.insertSelective(bdcSjxx);
        }
    }

    @Override
    @Transactional
    public void addBdcSjcl(BdcSjcl bdcSjcl) {
        if (bdcSjcl != null) {
            if (StringUtils.isBlank(bdcSjcl.getSjclid()))
                bdcSjcl.setSjclid(UUIDGenerator.generate());
            entityMapper.insertSelective(bdcSjcl);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcSjclConfig> getSjclConfigList(final String sqlxdm) {
        List<BdcSjclConfig> bdcSjclList = null;
        if (StringUtils.isNotBlank(sqlxdm)) {
            Example bdcSjcl = new Example(BdcSjclConfig.class);
            bdcSjcl.createCriteria().andEqualTo("sqlxdm", sqlxdm);

            bdcSjclList = entityMapper.selectByExample(BdcSjclConfig.class, bdcSjcl);
        }
        return bdcSjclList;
    }

    @Override
    public BdcSjxx createSjxxByBdcxm(BdcXm bdcXm) {
        BdcSjxx bdcSjxx = new BdcSjxx();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcSjxx> bdcSjxxList = queryBdcSjdByWiid(bdcXm.getWiid());
            if (CollectionUtils.isEmpty(bdcSjxxList)) {
                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                bdcSjxx.setProid(bdcXm.getProid());
                bdcSjxx.setWiid(bdcXm.getWiid());
                bdcSjxx.setSjr(bdcXm.getCjr());
                bdcSjxx.setSjrq(bdcXm.getCjsj());
                addBdcSjxx(bdcSjxx);
            }
        }
        return bdcSjxx;
    }

    @Override
    @Transactional(readOnly = true)
    public BdcSjxx createSjxxByBdcxmByProid(BdcXm bdcXm) {
        BdcSjxx bdcSjxx = new BdcSjxx();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            List<BdcSjxx> bdcSjxxList = queryBdcSjdByProid(bdcXm.getProid());
            if (CollectionUtils.isEmpty(bdcSjxxList)) {
                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                bdcSjxx.setProid(bdcXm.getProid());
                bdcSjxx.setWiid(bdcXm.getWiid());
                bdcSjxx.setSjr(bdcXm.getCjr());
                bdcSjxx.setSjrq(bdcXm.getCjsj());
            }
        }
        return bdcSjxx;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcSjxx> queryBdcSjdByProid(final String proid) {
        List<BdcSjxx> bdcSjxxList = null;
        if (StringUtils.isNotBlank(proid)) {
            Example bdcSjxx = new Example(BdcSjxx.class);
            bdcSjxx.createCriteria().andEqualTo("proid", proid);
            bdcSjxxList = entityMapper.selectByExample(BdcSjxx.class, bdcSjxx);
        }
        return bdcSjxxList;
    }

    @Override
    public void delBdcSjxxByWiid(final String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            Example bdcSjxx = new Example(BdcSjxx.class);
            bdcSjxx.createCriteria().andEqualTo("wiid", wiid);
            entityMapper.deleteByExample(BdcSjxx.class, bdcSjxx);
        }
    }

    @Override
    public boolean updateSjcls(FoldersModel foldersModel) {
        boolean tag = false;
        if (foldersModel != null && StringUtils.isNotBlank(foldersModel.getSjbh())) {
            String proid = "";
            Integer project_fileId = null;
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(foldersModel.getSjbh());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                String wiid = bdcXmList.get(0).getWiid();
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
                if (pfWorkFlowInstanceVo != null)
                    proid = pfWorkFlowInstanceVo.getProId();
            }

            try {
                if (StringUtils.isNotBlank(proid)) {
                    project_fileId = PlatformUtil.getProjectFileId(proid);
                    if (CollectionUtils.isNotEmpty(foldersModel.getFolderList())) {
                        for (FolderModel folderModel : foldersModel.getFolderList()) {
                            if (StringUtils.isNotBlank(folderModel.getName())) {
                                Integer nodeId = PlatformUtil.createFileFolderByclmc(project_fileId, folderModel.getName());
                                if (folderModel.getFiles() != null && CollectionUtils.isNotEmpty(folderModel.getFiles().getFileModelList())) {
                                    for (FileModel fileModel : folderModel.getFiles().getFileModelList()) {
                                        PlatformUtil.uploadFileFromUrl(fileModel.getUrl(), nodeId, fileModel.getName());
                                    }
                                }
                            }
                        }
                    }

                }
                tag = true;
            } catch (Exception e) {
                logger.error("BdcSjdServiceImpl.updateSjcls",e);
                tag = false;
            }
        }
        return tag;
    }

    @Override
    public boolean updateSjclsFiles(FoldersModel foldersModel) {
        boolean tag = false;
        if (foldersModel != null && StringUtils.isNotBlank(foldersModel.getSjbh())) {
            String proid = "";
            Integer project_fileId = null;
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmBySlbh(foldersModel.getSjbh());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                String wiid = bdcXmList.get(0).getWiid();
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
                if (pfWorkFlowInstanceVo != null)
                    proid = pfWorkFlowInstanceVo.getProId();
            }

            try {
                if (StringUtils.isNotBlank(proid)) {
                    project_fileId = PlatformUtil.getProjectFileId(proid);
                    if (CollectionUtils.isNotEmpty(foldersModel.getFolderList())) {
                        for (FolderModel folderModel : foldersModel.getFolderList()) {
                            if (StringUtils.isNotBlank(folderModel.getName())&&folderModel.getFiles() != null && CollectionUtils.isNotEmpty(folderModel.getFiles().getFileModelList())) {
                                for (FileModel fileModel : folderModel.getFiles().getFileModelList()) {
                                    PlatformUtil.uploadFileFromUrl(fileModel.getUrl(), project_fileId, fileModel.getName());
                                }
                            }
                        }
                    }

                }
                tag = true;
            } catch (Exception e) {
                logger.error("BdcSjdServiceImpl.updateSjclsFiles",e);
                tag = false;
            }
        }
        return tag;
    }

    @Override
    public List<String> getSjxxidlistByBdcXmList(List<BdcXm> bdcXmList) {
        List<String> sjxxidList = null;
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            sjxxidList = bdcSjxxMapper.getSjxxidlistByBdcXmList(bdcXmList);
        }
        return sjxxidList;
    }

    @Override
    public void batchDelSjclListBySjxxidList(List<String> sjxxidlist) {
        if(CollectionUtils.isNotEmpty(sjxxidlist)){
            bdcSjxxMapper.batchDelSjclListBySjxxidList(sjxxidlist);
        }
    }

    @Override
    public void batchDelSjxxListBySjxxidList(List<String> sjxxidlist) {
        if(CollectionUtils.isNotEmpty(sjxxidlist)){
            bdcSjxxMapper.batchDelSjxxListBySjxxidList(sjxxidlist);
        }
    }
}
