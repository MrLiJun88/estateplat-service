package cn.gtmap.estateplat.server.model;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/5/23
 * @description
 */
public class AutoSignWfNodeName {
    //所有自动签名的节点名称
    private String totalNodeName;
    //初审自动签名的节点名称
    private  String csNodeName;
    //复审自动签名的节点名称
    private  String fsNodeName;
    //核定自动签名的节点名称
    private  String hdNodeName;

    public String getTotalNodeName() {
        return totalNodeName;
    }

    public void setTotalNodeName(String totalNodeName) {
        this.totalNodeName = totalNodeName;
    }

    public String getCsNodeName() {
        return csNodeName;
    }

    public void setCsNodeName(String csNodeName) {
        this.csNodeName = csNodeName;
    }

    public String getFsNodeName() {
        return fsNodeName;
    }

    public void setFsNodeName(String fsNodeName) {
        this.fsNodeName = fsNodeName;
    }

    public String getHdNodeName() {
        return hdNodeName;
    }

    public void setHdNodeName(String hdNodeName) {
        this.hdNodeName = hdNodeName;
    }
}
