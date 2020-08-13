package cn.gtmap.estateplat.server.core.model.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-13
 * @description
 */
public class SjglResponse {
    private Head head;
    private Object data;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public SjglResponse(String msg, Object data) {
        this.head = new Head();
        if (StringUtils.isBlank(msg)) {
            this.head.setReturncode("0000");
        } else {
            this.head.setReturncode(msg);
        }
        this.data = data;
    }

    public SjglResponse() {
        super();
    }
}
