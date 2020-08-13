package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXtLimitfieldService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.model.impl.File;
import com.gtis.fileCenter.service.NodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-25
 */

public class ArchiveModel {
    private static final String PARAMETER_FIELD = "field";

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 构造函数
     */
    private ArchiveModel() {

    }

    public static Document createFwbdcXml(Document document, NodeService nodeService, BdcXm bdcXm, QllxVo qllxVo, BdcSpxx bdcSpxx, List<BdcQlr> bdcQlrList, List<BdcZs> bdcZsList, String archiveType, String mlh, List<BdcSjcl> bdcSjclList, String wfProid, String currentUserDwdm) {
        Element archivesElement = null;
        if (document != null) {
            archivesElement = document.getRootElement();
        } else {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("UTF-8");
            //创建根元素
            archivesElement = document.addElement("list");
        }

        //添加第一个档案节点
        Element archiveElement = archivesElement.addElement("archive");
        //添加节点的属性
        archiveElement.addAttribute("type", archiveType);

        String zl = "";
        String zdzhh = "";
        String bdcdyh = "";
        String yt = "";
        if (bdcSpxx != null) {
            if (StringUtils.isNotBlank(bdcSpxx.getZl())) {
                zl = bdcSpxx.getZl();
            }
            if (StringUtils.isNotBlank(bdcSpxx.getYt())) {
                yt = bdcSpxx.getYt();
            }
            bdcdyh = bdcSpxx.getBdcdyh();
            if (StringUtils.isNotEmpty(bdcdyh) && bdcdyh.length() > 19) {
                zdzhh = bdcdyh.substring(0, 19);
            }
        }

        StringBuilder qlr = new StringBuilder();
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                if (StringUtils.isBlank(qlr)) {
                    qlr.append(bdcQlr.getQlrmc());
                } else {
                    qlr.append("/").append(bdcQlr.getQlrmc());
                }
            }
        }

        StringBuilder cqzh = new StringBuilder();
        String fzrq = "";
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            for (BdcZs bdcZs : bdcZsList) {
                if (StringUtils.isBlank(cqzh)) {
                    cqzh.append(bdcZs.getBdcqzh());
                } else {
                    cqzh.append("/").append(bdcZs.getBdcqzh());
                }
                if (bdcZs.getCzrq() != null) {
                    fzrq = CalendarUtil.formateDatetoStr(bdcZs.getCzrq());
                }
            }
        }

        String qllx = "";
        String dwdm = "";
        if (bdcXm != null) {
            if (bdcXm.getQllx() != null) {
                qllx = bdcXm.getQllx();
            }
            if (StringUtils.isNotBlank(currentUserDwdm)) {
                dwdm = currentUserDwdm;
            } else {
                dwdm = bdcXm.getDwdm();
            }
        }

        //添加子节点   宗地宗海号
        Element field1 = archiveElement.addElement(PARAMETER_FIELD);
        field1.addAttribute("name", "zdzhh");
        field1.setText(zdzhh);

        //添加子节点   不动产单元号
        Element field2 = archiveElement.addElement(PARAMETER_FIELD);
        field2.addAttribute("name", "bdcdyh");
        if (bdcdyh != null) {
            field2.setText(bdcdyh);
        }

        //添加子节点   目录号
        Element field8 = archiveElement.addElement(PARAMETER_FIELD);
        field8.addAttribute("name", "mlh");
        field8.setText(mlh);

        //添加子节点   权利人名称
        Element field3 = archiveElement.addElement(PARAMETER_FIELD);
        field3.addAttribute("name", "qlrmc");
        field3.setText(String.valueOf(qlr));

        //添加子节点   坐落
        Element field4 = archiveElement.addElement(PARAMETER_FIELD);
        field4.addAttribute("name", "zl");
        field4.setText(zl);


        Element field5 = archiveElement.addElement(PARAMETER_FIELD);
        field5.addAttribute("name", "qllx");
        field5.setText(qllx);

        //添加子节点   不动产权证号
        Element field6 = archiveElement.addElement(PARAMETER_FIELD);
        field6.addAttribute("name", "bdcqzh");
        field6.setText(String.valueOf(cqzh));

        //添加子节点   用途
        Element field7 = archiveElement.addElement(PARAMETER_FIELD);
        field7.addAttribute("name", "yt");
        field7.setText(yt);

        //添加子节点   发证日期
        Element field11 = archiveElement.addElement(PARAMETER_FIELD);
        field11.addAttribute("name", "fzrq");
        field11.setText(fzrq);

        //归档默认值
        Element field12 = archiveElement.addElement(PARAMETER_FIELD);
        field12.addAttribute("name", "bgqx");
        field12.setText("永久");

        Element field13 = archiveElement.addElement(PARAMETER_FIELD);
        field13.addAttribute("name", "mj");
        field13.setText("秘密");

        Element field14 = archiveElement.addElement(PARAMETER_FIELD);
        field14.addAttribute("name", "tm");
        field14.setText(String.valueOf(qlr));

        //添加子节点 单位代码
        Element field15 = archiveElement.addElement(PARAMETER_FIELD);
        field15.addAttribute("name", "dwdm");
        field15.setText(dwdm);

        /**
         * @author bianwen
         * @description 获取案卷（即文件夹外的附件）
         */
        String fileCenterUrl = AppConfig.getFileCenterUrl();
        Space sourspace = nodeService.getWorkSpace("WORK_FLOW_STUFF", true);
        Node sournode = nodeService.getNode(sourspace.getId(), wfProid, true);
        List<Node> listNode = nodeService.getChildNodes(sournode.getId());
        if (listNode != null) {
            Integer size = listNode.size();
            for (int j = 0; j < size; j++) {
                try {
                    if (listNode.get(j) instanceof File) {
                        String url = fileCenterUrl + "/file/get.do?fid=" + listNode.get(j).getId();
                        Element field16 = archiveElement.addElement("file");
                        field16.addAttribute("url", url);
                        Element field161 = field16.addElement(PARAMETER_FIELD);
                        field161.addAttribute("name", "relationid");
                        field161.setText(bdcXm.getWiid() + listNode.get(j).getId());
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        /**
         * @author bianwen
         * @description 卷内附件
         */
        if (CollectionUtils.isNotEmpty(bdcSjclList)) {
            for (BdcSjcl bdcSjcl : bdcSjclList) {
                String sxh = bdcSjcl.getXh() + "";
                String tm = bdcSjcl.getClmc();
                Element field22 = archiveElement.addElement("doc");
                //添加子节点   顺序号
                Element field22_1 = field22.addElement(PARAMETER_FIELD);
                field22_1.addAttribute("name", "sxh");
                field22_1.setText(sxh);

                //添加子节点   题目
                Element field22_2 = field22.addElement(PARAMETER_FIELD);
                field22_2.addAttribute("name", "tm");
                field22_2.setText(tm);

                Node fileNode = nodeService.getNode(sournode.getId(), tm, true);
                List<Node> childListNode = nodeService.getAllChildNodes(fileNode.getId());
                if (CollectionUtils.isNotEmpty(childListNode)) {
                    for (Node childNode : childListNode) {
                        String url = fileCenterUrl + "/file/get.do?fid=" + childNode.getId();
                        Element field22_5 = field22.addElement("file");
                        field22_5.addAttribute("url", url);
                        Element field22_5_1 = field22_5.addElement(PARAMETER_FIELD);
                        field22_5_1.addAttribute("name", "relationid");
                        field22_5_1.setText(bdcXm.getWiid() + childNode.getId());
                    }
                }

            }
            Element field23 = archiveElement.addElement(PARAMETER_FIELD);
            field23.addAttribute("name", "proId");
            field23.setText(bdcXm.getProid());
        }
        return document;
    }

    public static Document createFwbdcXmlNew(Document document, NodeService nodeService, BdcXtLimitfieldService bdcXtLimitfieldService, BdcXm bdcXm, String archiveType, String mlh, List<BdcSjcl> bdcSjclList, String wfProid, List<JSONObject> jsonObjectList, List<BdcQlr> bdcQlrList) {
        Element archivesElement = null;
        if (document != null) {
            archivesElement = document.getRootElement();
        } else {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("UTF-8");
            //创建根元素
            archivesElement = document.addElement("list");
        }
        //添加第一个档案节点
        Element archiveElement = archivesElement.addElement("archive");
        //添加节点的属性
        archiveElement.addAttribute("type", archiveType);

        //添加子节点   目录号
        Element fieldmlh = archiveElement.addElement(PARAMETER_FIELD);
        fieldmlh.addAttribute("name", "mlh");
        fieldmlh.setText(mlh);

        if (CollectionUtils.isNotEmpty(jsonObjectList)) {
            for (JSONObject jsonObject : jsonObjectList) {
                if (jsonObject.containsKey("sql") && StringUtils.isNotBlank((CharSequence) jsonObject.get("sql"))) {
                    String sql = StringUtils.replace((String) jsonObject.get("sql"), "@proid", "'" + bdcXm.getProid() + "'");
                    List<Map> resultList = bdcXtLimitfieldService.runSql(sql);
                    if (CollectionUtils.isNotEmpty(resultList)) {
                        resultList = PublicUtil.MapKeyUpperToLower(resultList);
                        Map result = resultList.get(0);
                        for (Object key : result.keySet()) {
                            //添加子节点
                            Element fieldTemp = archiveElement.addElement(PARAMETER_FIELD);
                            fieldTemp.addAttribute("name", String.valueOf(key));
                            fieldTemp.setText(String.valueOf(result.get(key)));
                        }
                    }
                }
            }
        }

        /**
         * @author bianwen
         * @description 获取案卷（即文件夹外的附件）
         */
        String fileCenterUrl = AppConfig.getFileCenterUrl();
        Space sourspace = nodeService.getWorkSpace("WORK_FLOW_STUFF", true);
        Node sournode = nodeService.getNode(sourspace.getId(), wfProid, true);
        List<Node> listNode = nodeService.getChildNodes(sournode.getId());
        if (listNode != null) {
            Integer size = listNode.size();
            for (int j = 0; j < size; j++) {
                try {
                    if (listNode.get(j) instanceof File) {
                        String url = fileCenterUrl + "/file/get.do?fid=" + listNode.get(j).getId();
                        Element field16 = archiveElement.addElement("file");
                        field16.addAttribute("url", url);
                        Element field161 = field16.addElement(PARAMETER_FIELD);
                        field161.addAttribute("name", "relationid");
                        field161.setText(bdcXm.getWiid() + listNode.get(j).getId());
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        /**
         * @author bianwen
         * @description 卷内附件
         */
        if ( CollectionUtils.isNotEmpty(bdcSjclList)) {
            for (BdcSjcl bdcSjcl : bdcSjclList) {
                String sxh = bdcSjcl.getXh() + "";
                String tm = bdcSjcl.getClmc();
                Node fileNode = nodeService.getNode(sournode.getId(), tm, true);
                List<Node> childListNode = nodeService.getAllChildNodes(fileNode.getId());
                if(CollectionUtils.isEmpty(childListNode)){
                    continue;
                }
                if (CollectionUtils.isNotEmpty(bdcQlrList) && StringUtils.equals(bdcXm.getSqlx(), "9979912")) {
                    BdcQlr bdcQlr = bdcQlrList.get(0);
                    tm = bdcQlr.getCqxm() + "因" + bdcQlr.getQlrmc() + "实施的拆迁项目";
                }
                Element field22 = archiveElement.addElement("doc");
                //添加子节点   顺序号
                Element field22_1 = field22.addElement(PARAMETER_FIELD);
                field22_1.addAttribute("name", "sxh");
                field22_1.setText(sxh);

                //添加子节点   题目
                Element field22_2 = field22.addElement(PARAMETER_FIELD);
                field22_2.addAttribute("name", "tm");
                field22_2.setText(tm);

                if (CollectionUtils.isNotEmpty(childListNode)) {
                    for (Node childNode : childListNode) {
                        String url = fileCenterUrl + "/file/get.do?fid=" + childNode.getId();
                        Element field22_5 = field22.addElement("file");
                        field22_5.addAttribute("url", url);
                        Element field22_5_1 = field22_5.addElement(PARAMETER_FIELD);
                        field22_5_1.addAttribute("name", "relationid");
                        field22_5_1.setText(bdcXm.getWiid() + childNode.getId());
                    }
                }

            }
            Element field23 = archiveElement.addElement(PARAMETER_FIELD);
            field23.addAttribute("name", "proId");
            field23.setText(bdcXm.getProid());
        }
        return document;
    }
}
