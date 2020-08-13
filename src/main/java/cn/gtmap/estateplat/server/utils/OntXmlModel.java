package cn.gtmap.estateplat.server.utils;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2016/7/14
 * @description 外网收件项目XML模型
 */
public class OntXmlModel {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 构造函数
     */
    private OntXmlModel(){

    }
    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param ontBdcXmList
     * @return document
     * @description 创建外网收件项目的XML,用于jqGrid组织呈现成表格
     */
    public static Document createOntBdcXmXML(List<Map<String,String>> ontBdcXmList) {
        //建立document对象
        Document document = DocumentHelper.createDocument();
        //设置document的encoding
        document.setXMLEncoding("UTF-8");
        //建立XML文档的根books rows
        Element booksElement = document.addElement("rows");
        //创建page
        Element bookElement = booksElement.addElement("page");
        bookElement.setText("1");
        //创建total
        bookElement = booksElement.addElement("total");
        bookElement.setText("1");
        //创建records设置值为list的长度
        bookElement = booksElement.addElement("records");
        bookElement.setText(Integer.toString(ontBdcXmList.size()));
        //循环list创建每一行row
        for(Map<String,String> map : ontBdcXmList){
            bookElement = booksElement.addElement("row");
            bookElement.addAttribute("id",map.get("ID"));
            //添加收件号列
            Element titleElement = bookElement.addElement("cell");
            titleElement.setText(map.get("SJH"));
            //添加申请登记类型列
            titleElement = bookElement.addElement("cell");
            titleElement.setText(map.get("SQDJLX"));
            //添加不动产单元列
            titleElement = bookElement.addElement("cell");
            titleElement.setText(map.get("BDCDYH"));
            //添加状态列
            titleElement = bookElement.addElement("cell");
            titleElement.setText(map.get("STATE"));
            //添加工作流定义ID列
            titleElement = bookElement.addElement("cell");
            titleElement.setText(map.get("WORKFLOWDEFINEID"));
            //添加文件
            titleElement = bookElement.addElement("cell");
            titleElement.setText(map.get("saveFileDir"));
            //添加rowId列
            titleElement = bookElement.addElement("cell");
            titleElement.setText(map.get("ID"));
        }
        return document;
    }

}

