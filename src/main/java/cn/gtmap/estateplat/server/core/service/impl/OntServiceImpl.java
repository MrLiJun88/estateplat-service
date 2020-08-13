package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSjcl;
import cn.gtmap.estateplat.model.server.core.BdcSjxx;
import cn.gtmap.estateplat.server.core.model.OntBdcXm;
import cn.gtmap.estateplat.server.core.model.OntQlr;
import cn.gtmap.estateplat.server.core.service.OntService;
import cn.gtmap.estateplat.server.utils.OntXmlModel;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.server.utils.ZipFileUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.service.FileService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @description 外网收件服务实现类
 */
@Service
public class OntServiceImpl implements OntService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param inputStream,proid,bdcdyh,sjh
     * @return List<Object>
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 读取Excel文件, 创建OntBdcXm, OntBdcQlr, 返回list
     */
    @Override
    public List<Object> getExcelAsInputStream(InputStream inputStream, String proid, String bdcdyh, String sjh) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        InputStream ins = null;
        Workbook wb = null;
        List<Object> list = new ArrayList<Object>();
        try {
            //获取文件的输入流
            ins = inputStream;
            //创建工作簿
            wb = WorkbookFactory.create(ins);
            //循环获取Excel的Sheet
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                //获取第一行
                Row topRow = sheet.getRow(0);
                //获取第一行的列数,即这个sheet的最大列数
                int lineNum = topRow.getLastCellNum();
                //获取行的迭代器,通过循环每一行进行赋值操作
                Iterator<Row> rowIterator = sheet.rowIterator();
                switch (i) {
                    //第一个sheet存放的是OntBdcXm的信息,初始化OntBdcXm,并存放至list
                    case 0:
                        int j = 0;
                        List<OntBdcXm> ontBdcXmList = new ArrayList<OntBdcXm>();
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            row.getLastCellNum();
                            //因为Excel第一行存放的是列名,跳过第一行数据,从第二行开始初始换OntBdcXm
                            if (j == 0) {
                                j++;
                                continue;
                            }
                            //OntBdcXm与Excel中存放的信息多一个proid,将列数加一以正确初始化OntBdcXm
                            lineNum++;
                            OntBdcXm ontBdcxm = OntBdcXm.initOntBdcXm(row, lineNum, proid, sjh);
                            if (StringUtils.isNotBlank(ontBdcxm.getSjh())) {
                                ontBdcXmList.add(ontBdcxm);
                                list.add(ontBdcxm);
                            }
                        }
                        //将OntBdcXmList存放至session中
                        session.setAttribute("ontBdcXm_" + proid, ontBdcXmList);
                        break;
                    //第二个sheet存放的是OntQlr的信息,初始化OntQlr,权利人类型为qlr,并存放至list
                    case 1:
                        int k = 0;
                        List<OntQlr> ontQlrList = new ArrayList<OntQlr>();
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            row.getLastCellNum();
                            //因为Excel第一行存放的是列名,跳过第一行数据,从第二行开始初始换OntQlr
                            if (k == 0) {
                                k++;
                                continue;
                            }
                            //OntQlr与Excel中存放的信息多一个proid以及一个权利人类型,将列数加二以正确初始化OntQlr
                            lineNum = lineNum + 2;
                            OntQlr ontQlr = OntQlr.initOntQlr(row, lineNum, "qlr", proid, sjh);
                            //剔除没有不动产单元号和proid的OntQlr
                            if (StringUtils.isNotBlank(ontQlr.getSjh()) && StringUtils.isNotBlank(ontQlr.getProid())) {
                                ontQlrList.add(ontQlr);
                                list.add(ontQlr);
                            }
                        }
                        //将OntQlrList存放至session中
                        session.setAttribute("ontQlr_" + proid, ontQlrList);
                        break;
                    //第二个sheet存放的是OntYwr的信息,初始化OntQlr,权利人类型为ywr,并存放至list
                    case 2:
                        int l = 0;
                        List<OntQlr> ontYwrList = new ArrayList<OntQlr>();
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            row.getLastCellNum();
                            //因为Excel第一行存放的是列名,跳过第一行数据,从第二行开始初始换OntQlr
                            if (l == 0) {
                                l++;
                                continue;
                            }
                            //OntQlr与Excel中存放的信息多一个proid以及一个权利人类型,将列数加二以正确初始化OntQlr
                            lineNum = lineNum + 2;
                            OntQlr ontQlr = OntQlr.initOntQlr(row, lineNum, "ywr", proid, sjh);
                            //剔除没有不动产单元号的OntQlr
                            if (StringUtils.isNotBlank(ontQlr.getSjh()) && StringUtils.isNotBlank(ontQlr.getProid())) {
                                ontYwrList.add(ontQlr);
                                list.add(ontQlr);
                            }
                        }
                        //将OntYwrList存放至session中
                        session.setAttribute("ontYwr_" + proid, ontYwrList);
                        break;
                }
            }
        } catch (IOException e) {
            throw new AppException("创建工作簿出错!");
        } catch (InvalidFormatException e) {
            throw new AppException("无效的格式!");
        } finally {
            if(ins != null) {
                try {
                    ins.close();
                    ins = null;
                } catch (IOException e) {
                   logger.error("关闭流失败！",e);
                }
            }
            if(inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    logger.error("关闭流失败！",e);
                }
            }
        }
        return list;
    }

    /**
     * @param wiid,proid
     * @return
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 初始化收件材料并上传附件
     */
    @Override
    public void initOntFile(String wiid, String proid) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        String tempFilePath = null;
        if (session.getAttribute("ontFiles_" + proid) instanceof File[]) {
            File[] files = (File[]) session.getAttribute("ontFiles_" + proid);
            if (files != null && files.length != 0) {
                for (File tempFile : files) {
                    if (tempFile.isDirectory()) {
                        tempFilePath = uploadFileToFileCenter(tempFile, proid, wiid);
                    }
                }
            }
        }
        if (session.getAttribute("ontFiles_" + proid) instanceof File) {
            File file = (File) session.getAttribute("ontFiles_" + proid);
            if (file.isDirectory()) {
                tempFilePath = uploadFileToFileCenter(file, proid, wiid);
            }
        }
        try {
            if (StringUtils.isNotBlank(tempFilePath)) {
                FileUtils.deleteDirectory(new File(tempFilePath));
            }
        } catch (IOException e) {
            logger.error("OntServiceImpl.initOntFile",e);
        }
    }

    /**
     * @param file,proid,wiid
     * @return
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 上传文件至文件管理中心
     */
    private String uploadFileToFileCenter(File file, String proid, String wiid) {
        String filePath = file.getAbsolutePath();
        filePath = ZipFileUtil.appendFileSeparator(filePath);
        createAllFileFolder(proid, wiid, filePath);
        return file.getAbsolutePath();
    }

    /**
     * @param proid,wiid,filePath
     * @return
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 查询收件信息, 如果原先存在收件信息, 则删除收件信息, 文件中心创建文件并上传附件
     */
    private void createAllFileFolder(String proid, String wiid, String filePath) {
        if (StringUtils.isNotBlank(wiid)) {
            Example example = new Example(BdcSjxx.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("wiid", wiid);
            criteria.andEqualTo("proid", proid);
            List<BdcSjxx> bdcSjxxList = entityMapper.selectByExample(example);
            for (BdcSjxx bdcSjxx : bdcSjxxList) {
                example = new Example(BdcSjcl.class);
                example.createCriteria().andEqualTo("sjxxid", bdcSjxx.getSjxxid());
                List<BdcSjcl> bdcSjclList = entityMapper.selectByExample(example);
                if (bdcSjclList == null) {
                    createFileFolder(proid, filePath);
                } else {
                    if(CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria()))
                        entityMapper.deleteByExample(example);
                    createFileFolder(proid, filePath);
                }
            }
        }
    }

    /**
     * @param proid,filePath
     * @return
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据proid和文件路径, 上传附件至文件管理中心
     */
    private void createFileFolder(String proid, String filePath) {
        renameFile(new File(filePath));
        Integer project_fileId = null;
        InputStream inputStream = null;
        try {
            if (StringUtils.isNotBlank(proid)) {
                project_fileId = PlatformUtil.getProjectFileId(proid);
                File root = new File(filePath);
                if (root.exists()) {
                    File[] files = root.listFiles();
                    if (files != null && files.length != 0) {
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].isDirectory()) {
                                Integer parentId = PlatformUtil.createFileFolderByclmc(project_fileId, files[i].getName());
                                //初始化收件材料
                                initOntSjcl(proid, files[i], parentId, files[i].listFiles().length, i);
                                FileService fileService = PlatformUtil.getFileService();
                                if (files[i].isDirectory() && files[i].listFiles() != null) {
                                    for (File tempFile : files[i].listFiles()) {
                                        inputStream = new FileInputStream(tempFile);
                                        fileService.uploadFile(inputStream, parentId, tempFile.getName(), null, true, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("OntServiceImpl.createFileFolder",e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    logger.error("OntServiceImpl.createFileFolder",e);
                }
            }
        }
    }

    /**
     * @param proid,file,nodeId,length,xh
     * @return 材料名称
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 初始化收件材料
     */
    private void initOntSjcl(String proid, File file, Integer nodeId, Integer length, int xh) {
        List<BdcSjcl> sjclList = new ArrayList<BdcSjcl>();
        if (file.isDirectory()) {
            Example example = new Example(BdcSjxx.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proid", proid);
            List<BdcSjxx> sjxxList = entityMapper.selectByExample(example);
            for (BdcSjxx bdcSjxx : sjxxList) {
                BdcSjcl bdcSjCl = new BdcSjcl();
                bdcSjCl.setSjclid(UUIDGenerator.generate());
                bdcSjCl.setSjxxid(bdcSjxx.getSjxxid());
                bdcSjCl.setClmc(file.getName());
                //待完善,目前设置材料类型为原件
                bdcSjCl.setCllx("1");
                //设置页数,默认份数为文件夹下文件的个数
                bdcSjCl.setMrfs(length);
                bdcSjCl.setYs(length);
                //设置序号为读取顺序
                bdcSjCl.setXh(xh);
                //设置文件中心ID为nodeID
                bdcSjCl.setWjzxid(nodeId);
                sjclList.add(bdcSjCl);
            }
        }
        entityMapper.batchSaveSelective(sjclList);
    }

    /**
     * @param workFlowName
     * @return SysWorkFlowDefineId
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据流程名称获取流程定义ID
     */
    private String getSysWorkFlowDefineIdByWorkFlowName(String workFlowName) {
        StringBuilder workFlowDefineId = new StringBuilder();
        List<PfWorkFlowDefineVo> pfWorkFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineList();
        if (pfWorkFlowDefineVoList != null && !pfWorkFlowDefineVoList.isEmpty()) {
            //循环比较,流程名称一致时获取流程定义ID
            for (PfWorkFlowDefineVo pfWorkFlowDefineVo : pfWorkFlowDefineVoList) {
                if (StringUtils.equals(pfWorkFlowDefineVo.getWorkflowName(), workFlowName)) {
                    workFlowDefineId.append(pfWorkFlowDefineVo.getWorkflowDefinitionId());
                }
            }
        }
        return workFlowDefineId.toString();
    }


    /**
     * @param inputStream
     * @return List<Map<String, String>>
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 通过Excel获取OntBdcXm信息, 返回一个List, 方便之后处理成XML形式
     */
    @Override
    public List<Map<String, String>> getOntBdcXmInfoByExcel(InputStream inputStream,String saveFileDir,String id) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        StringBuilder xml = new StringBuilder();
        InputStream ins = null;
        Workbook wb = null;
        try {
            //获取文件的输入流
            ins = inputStream;
            //创建工作簿
            wb = WorkbookFactory.create(ins);
            //循环获取Excel的Sheet
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                //根据第一行判定Sheet的列数
                Row topRow = sheet.getRow(0);
                int lineNum = topRow.getLastCellNum();
                //获取行的迭代器,通过循环每一行进行赋值操作
                Iterator<Row> rowIterator = sheet.rowIterator();
                switch (i) {
                    case 0:
                        int j = 0;
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            row.getLastCellNum();
                            if (j == 0) {
                                j++;
                                continue;
                            }
                            lineNum++;
                            OntBdcXm ontBdcxm = OntBdcXm.initOntBdcXm(row, lineNum, "", "");
                            Map<String, String> map = new HashMap<String, String>();
                            //生成唯一ID作为jqGrid的rowid
                            map.put("ID", UUIDGenerator.generate18());
                            //获取OntBdcXm的收件号
                            map.put("SJH", StringUtils.deleteWhitespace(ontBdcxm.getSjh()));
                            //获取OntBdcXm的申请登记类型
                            map.put("SQDJLX", StringUtils.deleteWhitespace(ontBdcxm.getSqdjlx()));
                            map.put("STATE", "");
                            //获取OntBdcXm的不动产单元号
                            map.put("BDCDYH", StringUtils.deleteWhitespace(ontBdcxm.getBdcdyh()));
                            //根据Excel中的申请登记类型获取OntBdcXm的工作流定义ID
                            map.put("WORKFLOWDEFINEID", getSysWorkFlowDefineIdByWorkFlowName(ontBdcxm.getSqdjlx()));
                            //存放文件名
                            map.put("saveFileDir",saveFileDir);
                            resultList.add(map);
                        }
                        break;
                }
            }
            xml.append(OntXmlModel.createOntBdcXmXML(resultList).asXML());
            session.setAttribute("ontXml_" + id, xml);
        } catch (InvalidFormatException e) {
            logger.error("OntServiceImpl.getOntBdcXmInfoByExcel",e);
        } catch (IOException e) {
            logger.error("OntServiceImpl.getOntBdcXmInfoByExcel",e);
        } finally {
            if(ins != null) {
                try {
                    ins.close();
                    ins = null;
                } catch (IOException e) {
                    logger.error("关闭流失败！",e);
                }
            }
            if(inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    logger.error("关闭流失败！",e);
                }
            }
        }
        return resultList;
    }

    /**
     * @param fileName
     * @return saveFileDir
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 拼接压缩包解压缩得到的文件存放的位置, 检验存放位置下是否已有相同文件名的文件夹, 有则删除此文件夹, 保证唯一
     */
    @Override
    public String appendPathAndDeleteFile(String fileName,String type) {
        //如果是单个创建项目,导入信息,文件存放至saveFileDir
        String saveFileDir = AppConfig.getProperty("saveFileDir");
        //如果是批量创建项目,文件存放至zipFileDir
        if(StringUtils.equals(type,"PL")){
            saveFileDir = AppConfig.getProperty("zipFileDir");
        }
        //获取配置文件中配置的文件存放位置字符串
        if (StringUtils.isBlank(saveFileDir) || (!saveFileDir.contains("\\") && !saveFileDir.contains("/"))) {
            throw new AppException("文件存放位置未配置或配置错误!");
        }
        //去除文件的后缀名
        if (fileName.lastIndexOf('.') > 0) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        } else {
            throw new AppException("未知文件格式!");
        }
        //检测是否有同名文件夹,有则删除
        File file = new File(ZipFileUtil.appendFileSeparator(saveFileDir) + fileName);
        if (file.exists()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                throw new AppException("无法删除文件夹!");
            }
        }
        //拼接文件位置,并返回
        return ZipFileUtil.appendFileSeparator(ZipFileUtil.appendFileSeparator(saveFileDir) + fileName);
    }


    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param file
     * @return
     * @description 重命名文件
     */
    private void renameFile(File file){
        StringBuilder sjClMc;
        File[] files = file.listFiles();
        if(files!=null&&files.length>0) {
            for (File tempFile : files) {
                int i = 0;
                if (isInteger(tempFile.getName())) {
                    sjClMc = new StringBuilder();
                    sjClMc.append(ReadXmlProps.getOntSjClMc(tempFile.getName()).get("mc"));
                    String filePath = tempFile.getAbsolutePath();
                    if (StringUtils.isNotBlank(sjClMc)) {
                        filePath = ZipFileUtil.appendFileSeparator(filePath.substring(0, filePath.lastIndexOf("\\"))) + sjClMc;
                        if (!new File(filePath).exists()) {
                            new File(filePath).mkdirs();
                        }
                    } else {
                        filePath = ZipFileUtil.appendFileSeparator(filePath.substring(0, filePath.lastIndexOf("\\"))) + "未知材料名称" + i;
                        if (!new File(filePath).exists()) {
                            new File(filePath).mkdirs();
                        }
                    }
                    try {
                        if (tempFile.exists()) {
                            FileUtils.copyDirectory(tempFile, new File(filePath));
                            FileUtils.deleteDirectory(tempFile);
                        }
                    } catch (IOException e) {
                        logger.error("OntServiceImpl.renameFile",e);
                    }
                }
                i++;
            }
        }
    }
}
