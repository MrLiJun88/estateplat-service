package cn.gtmap.estateplat.server.core.model.vo;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 不动产拆分对象
 */
public class BdcSplitData {
    private BdcXm bdcXm;
    private BdcSpxx bdcSpxx;
    private BdcSjxx bdcSjxx;
    private List<BdcSjcl> bdcSjclList;
    private BdcBdcdy bdcBdcdy;
    private List<BdcXmRel> bdcXmRelList;
    private QllxVo qllxVo;
    private List<BdcFwfzxx> bdcFwfzxxList;
    private List<BdcXmzsRel> bdcXmzsRelList;
    private List<BdcZs> bdcZsList;
    private BdcSfxx bdcSfxx;
    private List<BdcSfxm> bdcSfxmList;
    private List<BdcZsQlrRel> bdcZsQlrRelList;
    private List<BdcQlr> bdcQlrList;
    private BdcGdxx bdcGdxx;
    private BdcBdcdySd bdcBdcdySd;
    private String qllx;
    private Integer xh;

    public BdcXm getBdcXm() {
        return bdcXm;
    }

    public void setBdcXm(BdcXm bdcXm) {
        this.bdcXm = bdcXm;
    }

    public BdcSpxx getBdcSpxx() {
        return bdcSpxx;
    }

    public void setBdcSpxx(BdcSpxx bdcSpxx) {
        this.bdcSpxx = bdcSpxx;
    }

    public BdcSjxx getBdcSjxx() {
        return bdcSjxx;
    }

    public void setBdcSjxx(BdcSjxx bdcSjxx) {
        this.bdcSjxx = bdcSjxx;
    }

    public List<BdcSjcl> getBdcSjclList() {
        return bdcSjclList;
    }

    public void setBdcSjclList(List<BdcSjcl> bdcSjclList) {
        this.bdcSjclList = bdcSjclList;
    }

    public BdcBdcdy getBdcBdcdy() {
        return bdcBdcdy;
    }

    public void setBdcBdcdy(BdcBdcdy bdcBdcdy) {
        this.bdcBdcdy = bdcBdcdy;
    }

    public List<BdcXmRel> getBdcXmRelList() {
        return bdcXmRelList;
    }

    public void setBdcXmRelList(List<BdcXmRel> bdcXmRelList) {
        this.bdcXmRelList = bdcXmRelList;
    }

    public QllxVo getQllxVo() {
        return qllxVo;
    }

    public void setQllxVo(QllxVo qllxVo) {
        this.qllxVo = qllxVo;
    }

    public List<BdcFwfzxx> getBdcFwfzxxList() {
        return bdcFwfzxxList;
    }

    public void setBdcFwfzxxList(List<BdcFwfzxx> bdcFwfzxxList) {
        this.bdcFwfzxxList = bdcFwfzxxList;
    }

    public List<BdcXmzsRel> getBdcXmzsRelList() {
        return bdcXmzsRelList;
    }

    public void setBdcXmzsRelList(List<BdcXmzsRel> bdcXmzsRelList) {
        this.bdcXmzsRelList = bdcXmzsRelList;
    }

    public List<BdcZs> getBdcZsList() {
        return bdcZsList;
    }

    public void setBdcZsList(List<BdcZs> bdcZsList) {
        this.bdcZsList = bdcZsList;
    }

    public BdcSfxx getBdcSfxx() {
        return bdcSfxx;
    }

    public void setBdcSfxx(BdcSfxx bdcSfxx) {
        this.bdcSfxx = bdcSfxx;
    }

    public List<BdcSfxm> getBdcSfxmList() {
        return bdcSfxmList;
    }

    public void setBdcSfxmList(List<BdcSfxm> bdcSfxmList) {
        this.bdcSfxmList = bdcSfxmList;
    }

    public String getQllx() {
        return qllx;
    }

    public void setQllx(String qllx) {
        this.qllx = qllx;
    }

    public List<BdcZsQlrRel> getBdcZsQlrRelList() {
        return bdcZsQlrRelList;
    }

    public void setBdcZsQlrRelList(List<BdcZsQlrRel> bdcZsQlrRelList) {
        this.bdcZsQlrRelList = bdcZsQlrRelList;
    }

    public List<BdcQlr> getBdcQlrList() {
        return bdcQlrList;
    }

    public void setBdcQlrList(List<BdcQlr> bdcQlrList) {
        this.bdcQlrList = bdcQlrList;
    }

    public BdcGdxx getBdcGdxx() {
        return bdcGdxx;
    }

    public void setBdcGdxx(BdcGdxx bdcGdxx) {
        this.bdcGdxx = bdcGdxx;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public BdcBdcdySd getBdcBdcdySd() {
        return bdcBdcdySd;
    }

    public void setBdcBdcdySd(BdcBdcdySd bdcBdcdySd) {
        this.bdcBdcdySd = bdcBdcdySd;
    }
}
