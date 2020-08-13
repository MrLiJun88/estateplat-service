package cn.gtmap.estateplat.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by zdd on 2015/11/19.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "file")
public class FileModel implements Serializable{
    private String id;
    private String name;
    private String url   ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
