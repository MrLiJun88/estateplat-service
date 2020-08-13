package cn.gtmap.estateplat.server.core.model.omp;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2017/6/25
 * @description 地块信息
 */
public class LandBlockInfo {

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 地块id
     */
    private String dkid;

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 地块名称
     */
    private String dkmc;

    public String getDkid() {
        return dkid;
    }

    public void setDkid(String dkid) {
        this.dkid = dkid;
    }

    public String getDkmc() {
        return dkmc;
    }

    public void setDkmc(String dkmc) {
        this.dkmc = dkmc;
    }
}
