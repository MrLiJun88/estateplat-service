package cn.gtmap.estateplat.server.core.model;

import cn.gtmap.estateplat.model.server.print.FdcqDzxxPage;
import cn.gtmap.estateplat.model.server.print.XmlData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class PageXml implements Serializable {
    @XmlElementWrapper(
            name = "datas"
    )

    @XmlElement(
            name = "data"
    )
    private List<XmlData> data;
    @XmlElement(
            name = "datas"
    )
    private String datas;
    @XmlElement(
            name = "page"
    )
    private String page;
    @XmlElement(
            name = "detail"
    )
    private List<FdcqDzxxPage> detail;

    public PageXml() {
    }

    public List<XmlData> getData() {
        return this.data;
    }

    public void setData(List<XmlData> data) {
        this.data = data;
    }

    public List<FdcqDzxxPage> getDetail() {
        return this.detail;
    }

    public void setDetail(List<FdcqDzxxPage> detail) {
        this.detail = detail;
    }

    public String getDatas() {
        return this.datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
