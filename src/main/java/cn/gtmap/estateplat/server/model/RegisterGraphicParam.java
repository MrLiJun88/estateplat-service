package cn.gtmap.estateplat.server.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/2/17
 * @description
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class RegisterGraphicParam implements Serializable {

    @JsonProperty("TYPE")
    private String type;
    @JsonProperty("BDCDYH")
    private List<String> bdcdyh;
    @JsonProperty("PROID")
    private String proid;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(List<String> bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }


}
