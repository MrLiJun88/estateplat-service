package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.omp.ContractInfo;
import cn.gtmap.estateplat.server.core.model.omp.LandBlockInfo;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.FileService;
import com.gtis.fileCenter.service.NodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2017/6/28
 * @description
 */
@Service
public class OmpDataServiceImpl implements OmpDataService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcGdDyhRelService gdDyhRelService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;
    @Resource(name = "ompFileCenterNodeServiceImpl")
    private NodeService ompFileCenterNodeServiceImpl;
    @Autowired
    private FileService fileService;
    @Autowired
    private BdcSjxxService bdcSjxxService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getContractInfoByHth(String hth) {
        String responseContractInfo = StringUtils.EMPTY;
        String ompInterfaceUrl = AppConfig.getProperty("onemap.unifiedPlatform.contractInfoUrl");
        if(StringUtils.isNoneBlank(ompInterfaceUrl)){
            HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
            connectionManager.getParams().setDefaultMaxConnectionsPerHost(10);
            connectionManager.getParams().setConnectionTimeout(30000);
            connectionManager.getParams().setSoTimeout(30000);
            HttpClient client = new HttpClient(connectionManager);
            PostMethod method = new PostMethod(ompInterfaceUrl);
            method.setRequestHeader("Connection", "close");
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            NameValuePair[] postData = new NameValuePair[1];
            postData[0] = new NameValuePair("hth", StringUtils.deleteWhitespace(hth));
            method.addParameters(postData);
            try {
                client.executeMethod(method);
                responseContractInfo = method.getResponseBodyAsString();
            } catch (Exception e) {
                logger.error("OmpDataServiceImpl.getContractInfoByHth",e);
            } finally {
                method.releaseConnection();
                ((MultiThreadedHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            }
        }
        return responseContractInfo;
    }

    @Override
    public synchronized String impContractInfo(ContractInfo contractInfo, String bdcdyh, String proid) {
        String msg = "SUCCESS";
        try{
            if (contractInfo != null && StringUtils.isNoneBlank(bdcdyh, proid)) {
                Example example = new Example(BdcGdRel.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("bdcdyh", bdcdyh);
                List<BdcGdRel> bdcGdRelList = entityMapper.selectByExampleNotNull(example);
                if (CollectionUtils.isEmpty(bdcGdRelList)) {
                    insertBdcGdRel(contractInfo, bdcdyh, proid);
                } else {
                    entityMapper.deleteByExampleNotNull(example);
                    insertBdcGdRel(contractInfo, bdcdyh, proid);
                }
            } else {
                msg = "FAIL";
            }
        }catch (Exception e) {
            logger.error("OmpDataServiceImpl.impContractInfo");
            msg = "FAIL";
        }
        return msg;
    }

    @Override
    public String getTdSfDyInfoByDjh(String djh) {
        String tdSfDyInfo = "0";
        if(StringUtils.isNotBlank(djh)){
            String tdBdcdyh = djh + "W00000000";
            Map map = Maps.newHashMap();
            map.put("bdcDyh",tdBdcdyh);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
            if(CollectionUtils.isNotEmpty(bdcDyaqList)){
                tdSfDyInfo = "1";
                return tdSfDyInfo;
            }else{
                GdTd gdTd = gdTdService.getGdTdByDjh(djh);
                if(gdTd != null && StringUtils.isNotBlank(gdTd.getTdid())){
                    List<GdDy> gdDyList = gdTdService.getGdTdDyByTdid(gdTd.getTdid());
                    if(CollectionUtils.isNotEmpty(gdDyList)){
                        tdSfDyInfo = "1";
                        return tdSfDyInfo;
                    }
                }

                List<BdcGdDyhRel> gdDyhRelList = gdDyhRelService.getGdDyhRelByDyh(tdBdcdyh);
                if(CollectionUtils.isNotEmpty(gdDyhRelList)){
                    for(BdcGdDyhRel gdDyhRel:gdDyhRelList){
                       if(StringUtils.isNotBlank(gdDyhRel.getGdid())&&gdTd != null){
                           List<GdDy> gdDyList = gdTdService.getGdTdDyByTdid(gdTd.getTdid());
                           if(CollectionUtils.isNotEmpty(gdDyList)){
                               tdSfDyInfo = "1";
                               return tdSfDyInfo;
                           }
                       }
                    }
                }
            }
        }
        return tdSfDyInfo;
    }


    private void insertBdcGdRel(ContractInfo contractInfo, String bdcdyh, String proid) {
        BdcGdRel bdcGdRel = new BdcGdRel();
        bdcGdRel.setRelid(UUIDGenerator.generate18());
        bdcGdRel.setBdcdyh(bdcdyh);
        bdcGdRel.setBdcxmid(proid);
        bdcGdRel.setGdhth(contractInfo.getHth());
        bdcGdRel.setGdxmid(contractInfo.getProid());
        LandBlockInfo landBlockInfo = contractInfo.getDkinfo();
        if (landBlockInfo != null) {
            bdcGdRel.setGddkid(landBlockInfo.getDkid());
        }
        entityMapper.saveOrUpdate(bdcGdRel, bdcGdRel.getRelid());
    }

    @Override
    public List<BdcGdRel> getBdcGdRelList(String bdcdyh, String proid) {
        Map map=Maps.newHashMap();
        map.put("bdcxmid",proid);
        map.put("bdcdyh",bdcdyh);
        return andEqualQueryBdcGdRel(map,null);
    }

    @Override
    public String extractAttachment(ContractInfo contractInfo, String proid) {
        String msg = "SUCCESS";
        try{
            Space ompSpace = ompFileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF");
            Integer ompNodeId = createOmpFileFolderByclmc(ompSpace.getId(), contractInfo.getProid());
            BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
            if (ompNodeId != null) {
                List<Node> nodeList = ompFileCenterNodeServiceImpl.getAllChildNodes(ompNodeId);
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    Space space = fileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF");
                    Integer proNodeId = createFileFolderByclmc(space.getId(), proid);
                    List<HashMap> fileMapList = new ArrayList<HashMap>();
                    List<Node> clmccNodes = new ArrayList<Node>();
                    for (Node node : nodeList) {
                        if (node != null && node.getType() == 1) {
                            HashMap<Integer, Object> fileMap = new HashMap<Integer, Object>();
                            fileMap.put(node.getParentId(), node);
                            fileMapList.add(fileMap);
                        } else {
                            clmccNodes.add(node);
                        }
                    }
                    for (Node node : clmccNodes) {
                        if (node != null && node.getType() == 0) {
                            Integer clmcid = createFileFolderByclmc(proNodeId, node.getName());
                            //创建收件材料
                            createBdcdjSjcl(bdcSjxx, node);
                            if (clmcid != null) {
                                for (HashMap fileHashMap : fileMapList) {
                                    Object file = fileHashMap.get(node.getId());
                                    if (file != null) {
                                        String url = AppConfig.getProperty("omp.fileCenter.url") + "/file/get.do?fid=" + ((Node) file).getId() + "&token=whosyourdaddy";
                                        uploadFileFromUrl(url, clmcid, ((Node) file).getName());
                                    }
                                }

                            }
                        }
                    }

                    //项目根目录中上传文件
                    for (HashMap fileHashMap : fileMapList) {
                        Object file = fileHashMap.get(ompNodeId);
                        if (file != null) {
                            String url = AppConfig.getProperty("omp.fileCenter.url") + "/file/get.do?fid=" + ((Node) file).getId() + "&token=whosyourdaddy";
                            uploadFileFromUrl(url, proNodeId, ((Node) file).getName());
                        }
                    }
                }
            }
        }catch (Exception e) {
            logger.error("OmpDataServiceImpl.extractAttachment",e);
            msg = "FAIL";
        }
        return msg;
    }

    public List<BdcGdRel> andEqualQueryBdcGdRel(final Map map, final String orderByClause) {
        List<BdcGdRel> list = null;
        if (map != null) {
            Example qllxTemp = new Example(BdcGdRel.class);
            if (StringUtils.isNotBlank(orderByClause))
                qllxTemp.setOrderByClause(orderByClause);
            if (CollectionUtils.isNotEmpty(map.entrySet())) {
                Iterator iter = map.entrySet().iterator();
                Example.Criteria criteria = qllxTemp.createCriteria();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    if (val != null)
                        criteria.andEqualTo(key.toString(), val);
                }
            }
            if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria())) {
                list = entityMapper.selectByExample(BdcGdRel.class, qllxTemp);
            }
        }
        return list;
    }

    private void createBdcdjSjcl(BdcSjxx bdcSjxx, Node node) {
        if(bdcSjxx != null&&node != null) {
            BdcSjcl bdcSjcl = new BdcSjcl();
            bdcSjcl.setSjclid(UUIDGenerator.generate18());
            bdcSjcl.setSjxxid(bdcSjxx.getSjxxid());
            bdcSjcl.setClmc(node.getName());
            bdcSjcl.setCllx(ParamsConstants.CLLX_YJ_DM);
            bdcSjcl.setMrfs(1);
            entityMapper.saveOrUpdate(bdcSjcl, bdcSjcl.getSjclid());
        }
    }


    private Integer createOmpFileFolderByclmc(Integer parentId, String folderNodeName) {
        com.gtis.fileCenter.model.Node tempNode = null;
        if (StringUtils.isNotBlank(folderNodeName)) {
            try {
                tempNode = ompFileCenterNodeServiceImpl.getNode(parentId, folderNodeName, true);
            } catch (NodeNotFoundException e) {
                logger.error("OmpDataServiceImpl.createFileFolderByclmc",e);
            }
            return tempNode!=null?tempNode.getId():-1;
        } else {
            return -1;
        }
    }

    private Integer createFileFolderByclmc(Integer parentId, String folderNodeName) {
        com.gtis.fileCenter.model.Node tempNode = null;
        if (StringUtils.isNotBlank(folderNodeName)) {
            try {
                tempNode = fileCenterNodeServiceImpl.getNode(parentId, folderNodeName, true);
            } catch (NodeNotFoundException e) {
                logger.error("OmpDataServiceImpl.createFileFolderByclmc",e);
            }
            return tempNode!=null?tempNode.getId():-1;
        } else {
            return -1;
        }
    }

    private Boolean uploadFileFromUrl(String url, Integer parentId, String name) {
        Boolean bol = false;
        try {
            if (StringUtils.isNotBlank(url) && parentId!=null) {
                URL fileUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                if (conn != null) {
                    //增加请求完毕后关闭链接的头信息
                    conn.setRequestProperty("Connection", "close");
                    //文件上传
                    fileService.uploadFile(conn.getInputStream(), parentId, name, null, true, false);
                    conn.disconnect();
                }
            }
            bol = true;
        } catch (Exception e) {
            logger.error("OmpDataServiceImpl.uploadFileFromUrl",e);
            bol = false;
        }
        return bol;
    }
}
