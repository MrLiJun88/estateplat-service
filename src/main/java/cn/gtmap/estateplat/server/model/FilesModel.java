package cn.gtmap.estateplat.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zdd on 2015/11/19.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "fileList")
public class FilesModel implements Serializable{
    private List<FileModel> fileModelList;
    @XmlElement(name = "file")
    public List<FileModel> getFileModelList() {
        return fileModelList;
    }

    public void setFileModelList(List<FileModel> fileModelList) {
        this.fileModelList = fileModelList;
    }
}
