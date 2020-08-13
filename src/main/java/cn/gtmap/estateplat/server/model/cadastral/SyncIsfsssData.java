package cn.gtmap.estateplat.server.model.cadastral;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/9/26
 * @description 同步附属设施数据
 */
public class SyncIsfsssData {

    private String dah; //档案号
    private String isfsss; //是否是附属设施（0：否；1：是）

    public void setDah(String dah) {
        this.dah = dah;
    }

    public String getDah() {
        return dah;
    }

    public void setIsfsss(String isfsss) {
        this.isfsss = isfsss;
    }

    public String getIsfsss() {
        return isfsss;
    }

}