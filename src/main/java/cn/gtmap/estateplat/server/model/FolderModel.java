package cn.gtmap.estateplat.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by zdd on 2015/11/19.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "folder")
public class FolderModel implements Serializable{

    private String id;
    private String name;
    private FilesModel files;


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
    @XmlElement(name = "files")
    public FilesModel getFiles() {
        return files;
    }

    public void setFiles(FilesModel files) {
        this.files = files;
    }
}
