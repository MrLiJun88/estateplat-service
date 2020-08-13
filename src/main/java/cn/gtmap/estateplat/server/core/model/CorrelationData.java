package cn.gtmap.estateplat.server.core.model;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/12
 * @description 扩展CorrelationData
 */
public class CorrelationData extends org.springframework.amqp.rabbit.support.CorrelationData {
    private volatile Object message;//消息体
    private String exchange;//交换机
    private String routingKey;//路由键
    private int retryCount = 0;//重试次数

    public CorrelationData(String id) {
        super(id);
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
