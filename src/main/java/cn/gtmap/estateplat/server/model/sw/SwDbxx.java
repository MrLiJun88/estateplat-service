package cn.gtmap.estateplat.server.model.sw;

import java.util.Date;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-16
 * @description
 */
public class SwDbxx {
    private String htbh;
    private String dbzt;
    private String dbr;
    private Date dbsj;

    public SwDbxx(String htbh, String dbzt, String dbr, Date dbsj) {
        this.htbh = htbh;
        this.dbzt = dbzt;
        this.dbr = dbr;
        this.dbsj = dbsj;
    }

    public SwDbxx() {
        super();
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getDbzt() {
        return dbzt;
    }

    public void setDbzt(String dbzt) {
        this.dbzt = dbzt;
    }

    public String getDbr() {
        return dbr;
    }

    public void setDbr(String dbr) {
        this.dbr = dbr;
    }

    public Date getDbsj() {
        return dbsj;
    }

    public void setDbsj(Date dbsj) {
        this.dbsj = dbsj;
    }
}
