package cn.gtmap.estateplat.server.core.model.vo;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcFwfzxx;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/4/1 0001
 * @description
 */
public class SplitNum {
    private BdcFwfzxx bdcFwfzxx;
    private BdcBdcdy bdcBdcdy;
    private String proid;
    private double tdmj;
    private String tdzl;
    private String ycqproid;

    public BdcFwfzxx getBdcFwfzxx() {
        return bdcFwfzxx;
    }

    public void setBdcFwfzxx(BdcFwfzxx bdcFwfzxx) {
        this.bdcFwfzxx = bdcFwfzxx;
    }

    public BdcBdcdy getBdcBdcdy() {
        return bdcBdcdy;
    }

    public void setBdcBdcdy(BdcBdcdy bdcBdcdy) {
        this.bdcBdcdy = bdcBdcdy;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public double getTdmj() {
        return tdmj;
    }

    public void setTdmj(double tdmj) {
        this.tdmj = tdmj;
    }

    public String getTdzl() {
        return tdzl;
    }

    public void setTdzl(String tdzl) {
        this.tdzl = tdzl;
    }

    public String getYcqproid() {
        return ycqproid;
    }

    public void setYcqproid(String ycqproid) {
        this.ycqproid = ycqproid;
    }
}
