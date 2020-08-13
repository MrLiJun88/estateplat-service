package cn.gtmap.estateplat.server.model;

import cn.gtmap.estateplat.model.exchange.national.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zdd on 2015/11/19.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "folders")
public class FoldersModel implements Serializable{
    private String sjbh;
    private List<FolderModel> folderList;

    @XmlElement(name = "sjbh")
    public String getSjbh() {
        return sjbh;
    }

    public void setSjbh(String sjbh) {
        this.sjbh = sjbh;
    }



    @XmlElement(name = "folder")
    public List<FolderModel> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<FolderModel> folderList) {
        this.folderList = folderList;
    }
}
