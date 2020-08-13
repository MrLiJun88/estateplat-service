package cn.gtmap.estateplat.server.core.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@XmlRootElement(
        name = "fetchdatas"
)
@XmlAccessorType(XmlAccessType.FIELD)
public class MulDataToPrintXml {
    @XmlElement(
            name = "page"
    )
    private List<PageXml> page;

    public List<PageXml> getPage() {
        return page;
    }

    public void setPage(List<PageXml> page) {
        this.page = page;
    }
}
