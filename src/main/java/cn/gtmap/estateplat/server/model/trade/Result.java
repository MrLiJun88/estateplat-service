package cn.gtmap.estateplat.server.model.trade;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/3/28
 * @description
 */
@XmlRootElement(name = "Result")
public class Result {
    private String isSucessfull; //0 失败；1：成功
    private Data data;           //结果集字
    private String message;     //服务端信息

    @XmlElement(name = "IsSucessfull")
    public String getIsSucessfull() {
        return isSucessfull;
    }

    public void setIsSucessfull(String isSucessfull) {
        this.isSucessfull = isSucessfull;
    }

    @XmlElement(name = "Data")
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @XmlElement(name = "Message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
