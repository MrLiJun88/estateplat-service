package cn.gtmap.estateplat.server.model;


import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.1.0, 2016/5/7.
 * @description 初始化表单参数实体对象  根据实际情况增加参数字段
 */
public class InitVoFromParm {


    private Project project;
    private DjsjFwxx djsjFwxx;
    private DjsjLqxx djsjLqxx;
    private DjsjZdxx djsjZdxx;
    private DjsjZhxx djsjZhxx;
    private DjsjQszdDcb djsjQszdDcb;
    private DjsjCbzdDcb cbzdDcb ;
    private DjsjNydDcb djsjNydDcb;
    private List<DjsjFwxx> djsjFwQlrList;
    private List<DjsjQszdDcb> djsjQszdDcbList;
    private List<DjsjNydDcb> djsjNydDcbList;
    private List<DjsjZdxx> djsjZdxxList;
    private List<DjsjZhxx> djsjZhxxList;
    private BdcXm bdcXm;
    private String djbid;
    //zdd 2016-08-11 不动产单元初始化需要读取过渡信息
    private List<GdFw> gdFwList;
    private List<GdFwsyq> gdFwsyqList;
    private List<GdTd> gdTdList;
    private List<GdTdsyq> gdTdsyqList;

    public List<GdFw> getGdFwList() {
        return gdFwList;
    }

    public void setGdFwList(List<GdFw> gdFwList) {
        this.gdFwList = gdFwList;
    }

    public List<GdFwsyq> getGdFwsyqList() {
        return gdFwsyqList;
    }

    public void setGdFwsyqList(List<GdFwsyq> gdFwsyqList) {
        this.gdFwsyqList = gdFwsyqList;
    }

    public List<GdTd> getGdTdList() {
        return gdTdList;
    }

    public void setGdTdList(List<GdTd> gdTdList) {
        this.gdTdList = gdTdList;
    }

    public List<GdTdsyq> getGdTdsyqList() {
        return gdTdsyqList;
    }

    public void setGdTdsyqList(List<GdTdsyq> gdTdsyqList) {
        this.gdTdsyqList = gdTdsyqList;
    }

    public List<DjsjFwxx> getDjsjFwQlrList() {
        return djsjFwQlrList;
    }

    public void setDjsjFwQlrList(List<DjsjFwxx> djsjFwQlrList) {
        this.djsjFwQlrList = djsjFwQlrList;
    }

    public String getDjbid() {
        return djbid;
    }

    public void setDjbid(String djbid) {
        this.djbid = djbid;
    }

    public BdcXm getBdcXm() {
        return bdcXm;
    }

    public void setBdcXm(BdcXm bdcXm) {
        this.bdcXm = bdcXm;
    }

    public DjsjCbzdDcb getCbzdDcb() {
        return cbzdDcb;
    }

    public void setCbzdDcb(DjsjCbzdDcb cbzdDcb) {
        this.cbzdDcb = cbzdDcb;
    }

    public DjsjNydDcb getDjsjNydDcb() {
        return djsjNydDcb;
    }

    public void setDjsjNydDcb(DjsjNydDcb djsjNydDcb) {
        this.djsjNydDcb = djsjNydDcb;
    }

    public DjsjLqxx getDjsjLqxx() {
        return djsjLqxx;
    }

    public void setDjsjLqxx(DjsjLqxx djsjLqxx) {
        this.djsjLqxx = djsjLqxx;
    }

    public DjsjZdxx getDjsjZdxx() {
        return djsjZdxx;
    }

    public void setDjsjZdxx(DjsjZdxx djsjZdxx) {
        this.djsjZdxx = djsjZdxx;
    }

    public DjsjZhxx getDjsjZhxx() {
        return djsjZhxx;
    }

    public void setDjsjZhxx(DjsjZhxx djsjZhxx) {
        this.djsjZhxx = djsjZhxx;
    }

    public DjsjQszdDcb getDjsjQszdDcb() {
        return djsjQszdDcb;
    }

    public void setDjsjQszdDcb(DjsjQszdDcb djsjQszdDcb) {
        this.djsjQszdDcb = djsjQszdDcb;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public DjsjFwxx getDjsjFwxx() {
        return djsjFwxx;
    }

    public void setDjsjFwxx(DjsjFwxx djsjFwxx) {
        this.djsjFwxx = djsjFwxx;
    }

    public List<DjsjQszdDcb> getDjsjQszdDcbList() {
        return djsjQszdDcbList;
    }

    public void setDjsjQszdDcbList(List<DjsjQszdDcb> djsjQszdDcbList) {
        this.djsjQszdDcbList = djsjQszdDcbList;
    }

    public List<DjsjNydDcb> getDjsjNydDcbList() {
        return djsjNydDcbList;
    }

    public void setDjsjNydDcbList(List<DjsjNydDcb> djsjNydDcbList) {
        this.djsjNydDcbList = djsjNydDcbList;
    }

    public List<DjsjZdxx> getDjsjZdxxList() {
        return djsjZdxxList;
    }

    public void setDjsjZdxxList(List<DjsjZdxx> djsjZdxxList) {
        this.djsjZdxxList = djsjZdxxList;
    }

    public List<DjsjZhxx> getDjsjZhxxList() {
        return djsjZhxxList;
    }

    public void setDjsjZhxxList(List<DjsjZhxx> djsjZhxxList) {
        this.djsjZhxxList = djsjZhxxList;
    }
}
