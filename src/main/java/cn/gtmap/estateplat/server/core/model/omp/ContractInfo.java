package cn.gtmap.estateplat.server.core.model.omp;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2017/6/25
 * @description 合同信息
 */
public class ContractInfo {

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 合同号
     */
    private String hth;

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 项目名称
     */
    private String xmmc;

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 项目id
     */
    private String xmid;

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description proid
     */
    private String proid;

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 地块信息
     */
    private LandBlockInfo dkinfo;

    public String getHth() {
        return hth;
    }

    public void setHth(String hth) {
        this.hth = hth;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public LandBlockInfo getDkinfo() {
        return dkinfo;
    }

    public void setDkinfo(LandBlockInfo dkinfo) {
        this.dkinfo = dkinfo;
    }
}
