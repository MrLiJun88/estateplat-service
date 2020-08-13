package cn.gtmap.estateplat.server.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/6/25
 * @description 传到高拍仪控件的文件信息
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "fileInfo")
public class FileInfo {
    @JSONField(name = "ProName")
    private String proName;
    @JSONField(name = "Count")
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }


}
