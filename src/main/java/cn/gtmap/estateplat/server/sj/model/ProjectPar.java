package cn.gtmap.estateplat.server.sj.model;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/12 0012
 * @description 流程参数类
 */
public class ProjectPar implements Comparable<ProjectPar> {
    //项目ID
    private String proid;
    //主项目ID
    private String zProid;
    //工作流实例ID
    private String wiid;
    // 工作流定义ID
    private String wdid;
    //顺序
    private Integer sx;
    //不动产类型
    private String bdclx;
    //申请类型
    private String sqlx;
    //申请类型
    private String dsqlx;
    //权利类型
    private String qllx;
    //登记簿ID
    private String djbid;
    //地籍号
    private String djh;
    //不动产单元ID
    private String bdcdyid;
    //不动产单元号
    private String bdcdyh;
    //权籍ID
    private String qjid;
    // 创建人
    private String cjr;
    //创建时间
    private Date cjsj;
    //不动产项目
    private BdcXm bdcXm;
    //不动产单元
    private BdcBdcdy bdcBdcdy;
    //登记簿
    private BdcBdcdjb bdcBdcdjb;
    //不动产土地
    private BdcTd bdcTd;
    //不动产审批信息
    private BdcSpxx bdcSpxx;
    //不动产权利人
    private List<BdcQlr> bdcQlrList;
    //项目关系表
    private List<BdcXmRel> bdcXmRelList;
    //业务模型
    private BdcXtYwmx bdcXtYwmx;
    //权籍房屋信息
    private DjsjFwxx djsjFwxx;
    //权籍房屋权利人集合
    private List<DjsjFwxx> djsjFwQlrList;
    //权籍宗地信息
    private DjsjZdxx djsjZdxx;
    //权籍宗地信息集合
    private List<DjsjZdxx> djsjZdxxList;
    //权籍承包宗地信息
    private DjsjCbzdDcb djsjCbzdDcb;
    //权籍权属宗地调查表
    private DjsjQszdDcb djsjQszdDcb;
    //权籍权属宗地信息集合
    private List<DjsjQszdDcb> djsjQszdDcbList;
    //权籍农用地调查表
    private List<DjsjNydDcb> djsjNydDcbList;
    // 权籍林权信息
    private DjsjLqxx djsjLqxx;
    //权籍宗海信息
    private DjsjZhxx djsjZhxx;
    //用户id
    private String userId;
    //单位代码
    private String dwdm;
    //编号
    private String bh;
    //过渡受理编号
    private String gdslbh;
    //项目来源
    private String xmly;
    //登记事由
    private String djsy;
    //原项目id
    private String yxmid;
    //登记类型
    private String djlx;
    //原不动产权证
    private String ybdcqzh;
    //不动产权属宗地宗地面积
    private BdcQszdZdmj bdcQszdZdmj;
    private Integer dcsjzs;
    private boolean sfdyYzh;

    private Double jzmj;
    //创建来源
    private String xtly;

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getzProid() {
        return zProid;
    }

    public void setzProid(String zProid) {
        this.zProid = zProid;
    }

    public String getWiid() {
        return wiid;
    }

    public void setWiid(String wiid) {
        this.wiid = wiid;
    }

    public String getWdid() {
        return wdid;
    }

    public void setWdid(String wdid) {
        this.wdid = wdid;
    }

    public Integer getSx() {
        return sx;
    }

    public void setSx(Integer sx) {
        this.sx = sx;
    }

    public String getBdclx() {
        return bdclx;
    }

    public void setBdclx(String bdclx) {
        this.bdclx = bdclx;
    }

    public String getDsqlx() {
        return dsqlx;
    }

    public void setDsqlx(String dsqlx) {
        this.dsqlx = dsqlx;
    }

    public String getSqlx() {
        return sqlx;
    }

    public void setSqlx(String sqlx) {
        this.sqlx = sqlx;
    }

    public String getQllx() {
        return qllx;
    }

    public void setQllx(String qllx) {
        this.qllx = qllx;
    }

    public String getDjbid() {
        return djbid;
    }

    public void setDjbid(String djbid) {
        this.djbid = djbid;
    }

    public String getDjh() {
        return djh;
    }

    public void setDjh(String djh) {
        this.djh = djh;
    }

    public String getBdcdyid() {
        return bdcdyid;
    }

    public void setBdcdyid(String bdcdyid) {
        this.bdcdyid = bdcdyid;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getQjid() {
        return qjid;
    }

    public void setQjid(String qjid) {
        this.qjid = qjid;
    }

    public String getCjr() {
        return cjr;
    }

    public void setCjr(String cjr) {
        this.cjr = cjr;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    public BdcXm getBdcXm() {
        return bdcXm;
    }

    public void setBdcXm(BdcXm bdcXm) {
        this.bdcXm = bdcXm;
    }

    public BdcBdcdy getBdcBdcdy() {
        return bdcBdcdy;
    }

    public void setBdcBdcdy(BdcBdcdy bdcBdcdy) {
        this.bdcBdcdy = bdcBdcdy;
    }

    public BdcSpxx getBdcSpxx() {
        return bdcSpxx;
    }

    public void setBdcSpxx(BdcSpxx bdcSpxx) {
        this.bdcSpxx = bdcSpxx;
    }

    public List<BdcQlr> getBdcQlrList() {
        return bdcQlrList;
    }

    public void setBdcQlrList(List<BdcQlr> bdcQlrList) {
        this.bdcQlrList = bdcQlrList;
    }

    public List<BdcXmRel> getBdcXmRelList() {
        return bdcXmRelList;
    }

    public void setBdcXmRelList(List<BdcXmRel> bdcXmRelList) {
        this.bdcXmRelList = bdcXmRelList;
    }

    public BdcXtYwmx getBdcXtYwmx() {
        return bdcXtYwmx;
    }

    public void setBdcXtYwmx(BdcXtYwmx bdcXtYwmx) {
        this.bdcXtYwmx = bdcXtYwmx;
    }

    public DjsjFwxx getDjsjFwxx() {
        return djsjFwxx;
    }

    public void setDjsjFwxx(DjsjFwxx djsjFwxx) {
        this.djsjFwxx = djsjFwxx;
    }

    public List<DjsjFwxx> getDjsjFwQlrList() {
        return djsjFwQlrList;
    }

    public void setDjsjFwQlrList(List<DjsjFwxx> djsjFwQlrList) {
        this.djsjFwQlrList = djsjFwQlrList;
    }

    public DjsjZdxx getDjsjZdxx() {
        return djsjZdxx;
    }

    public void setDjsjZdxx(DjsjZdxx djsjZdxx) {
        this.djsjZdxx = djsjZdxx;
    }

    public List<DjsjZdxx> getDjsjZdxxList() {
        return djsjZdxxList;
    }

    public void setDjsjZdxxList(List<DjsjZdxx> djsjZdxxList) {
        this.djsjZdxxList = djsjZdxxList;
    }

    public DjsjCbzdDcb getDjsjCbzdDcb() {
        return djsjCbzdDcb;
    }

    public void setDjsjCbzdDcb(DjsjCbzdDcb djsjCbzdDcb) {
        this.djsjCbzdDcb = djsjCbzdDcb;
    }

    public DjsjQszdDcb getDjsjQszdDcb() {
        return djsjQszdDcb;
    }

    public void setDjsjQszdDcb(DjsjQszdDcb djsjQszdDcb) {
        this.djsjQszdDcb = djsjQszdDcb;
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

    public DjsjLqxx getDjsjLqxx() {
        return djsjLqxx;
    }

    public void setDjsjLqxx(DjsjLqxx djsjLqxx) {
        this.djsjLqxx = djsjLqxx;
    }

    public DjsjZhxx getDjsjZhxx() {
        return djsjZhxx;
    }

    public void setDjsjZhxx(DjsjZhxx djsjZhxx) {
        this.djsjZhxx = djsjZhxx;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDwdm() {
        return dwdm;
    }

    public void setDwdm(String dwdm) {
        this.dwdm = dwdm;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getGdslbh() {
        return gdslbh;
    }

    public void setGdslbh(String gdslbh) {
        this.gdslbh = gdslbh;
    }

    public String getXmly() {
        return xmly;
    }

    public void setXmly(String xmly) {
        this.xmly = xmly;
    }

    public String getDjsy() {
        return djsy;
    }

    public void setDjsy(String djsy) {
        this.djsy = djsy;
    }

    public String getYxmid() {
        return yxmid;
    }

    public void setYxmid(String yxmid) {
        this.yxmid = yxmid;
    }

    public String getDjlx() {
        return djlx;
    }

    public void setDjlx(String djlx) {
        this.djlx = djlx;
    }

    public String getYbdcqzh() {
        return ybdcqzh;
    }

    public void setYbdcqzh(String ybdcqzh) {
        this.ybdcqzh = ybdcqzh;
    }

    public BdcQszdZdmj getBdcQszdZdmj() {
        return bdcQszdZdmj;
    }

    public void setBdcQszdZdmj(BdcQszdZdmj bdcQszdZdmj) {
        this.bdcQszdZdmj = bdcQszdZdmj;
    }

    @Override
    public int compareTo(ProjectPar o) {
        return this.sx.compareTo(o.getSx());
    }

    public Integer getDcsjzs() {
        return dcsjzs;
    }

    public void setDcsjzs(Integer dcsjzs) {
        this.dcsjzs = dcsjzs;
    }

    public boolean isSfdyYzh() {
        return sfdyYzh;
    }

    public void setSfdyYzh(boolean sfdyYzh) {
        this.sfdyYzh = sfdyYzh;
    }

    public Double getJzmj() {
        return jzmj;
    }

    public void setJzmj(Double jzmj) {
        this.jzmj = jzmj;
    }

    public String getXtly() {
        return xtly;
    }

    public void setXtly(String xtly) {
        this.xtly = xtly;
    }


    public BdcBdcdjb getBdcBdcdjb() {
        return bdcBdcdjb;
    }

    public void setBdcBdcdjb(BdcBdcdjb bdcBdcdjb) {
        this.bdcBdcdjb = bdcBdcdjb;
    }

    public BdcTd getBdcTd() {
        return bdcTd;
    }

    public void setBdcTd(BdcTd bdcTd) {
        this.bdcTd = bdcTd;
    }
}
