package cn.gtmap.estateplat.server.core.model;

import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @description 外网收件的不动产项目类
 */
public class OntBdcXm {
    private static final Logger logger = LoggerFactory.getLogger(OntBdcXm.class);
    private String proid;
    private String sjh;
    private String sqdjlx;
    private String qllx;
    private String djlx;
    private String gyfs;
    private String sffbcz;
    private String bdcdyh;
    private Double jyjg;
    private String bdbzzqse;
    private Date zwlxksrq;
    private Date zwlxjsrq;
    private Double pgjz;
    private String dyfw;
    private String dyfs;
    private String dkfs;
    private String ybdcqzh;

    public OntBdcXm() {
    }

    /**
     * @param row,lineNum,proid,sjh
     * @return OntBdcXm
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据Excel的内容初始化OntBdcXm, 每一个属性初始化一遍
     */
    private OntBdcXm(Row row, int lineNum, String proid, String sjh) {
        boolean proidFlag = false;
        boolean sjhFlag = false;
        boolean sqdjlxFlag = false;
        boolean qllxFlag = false;
        boolean djlxFlag = false;
        boolean gyfsFlag = false;
        boolean sffbczFlag = false;
        boolean bdcdyhFlag = false;
        boolean jyjgFlag = false;
        boolean bdbzzqseFlag = false;
        boolean zwlxksrqFlag = false;
        boolean zwlxjsrqFlag = false;
        boolean pgjzFlag = false;
        boolean dyfwFlag = false;
        boolean dyfsFlag = false;
        boolean dkfsFlag = false;
        boolean ybdcqzhFlag = false;
        Cell currentCell;
        for (int i = 0; i < lineNum; i++) {
             currentCell = row.getCell(i);
            if (i == 0 && StringUtils.isNotBlank(sjh) && !StringUtils.equals(sjh, currentCell.toString())) {
                break;
            }
            if (StringUtils.isBlank(this.getSjh()) && !sjhFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setSjh(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                sjhFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getSqdjlx()) && !sqdjlxFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setSqdjlx(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                sqdjlxFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getQllx()) && !qllxFlag) {
                if (currentCell != null && StringUtils.equals(currentCell.toString(), "国有建设用地使用权/房屋所有权")) {
                    this.setQllx(Constants.QLLX_GYTD_FWSUQ);
                }
                qllxFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getDjlx()) && !djlxFlag) {
                if (currentCell != null) {
                    if (StringUtils.indexOf(currentCell.toString(), "预告") > -1) {
                        this.setDjlx(Constants.DJLX_YGDJ_DM);
                    }
                    if (StringUtils.indexOf(currentCell.toString(), "转移") > -1) {
                        this.setDjlx(Constants.DJLX_ZYDJ_DM);
                    }
                }
                djlxFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getGyfs()) && !gyfsFlag) {
                if (currentCell != null) {
                    if (StringUtils.equals(currentCell.toString(), Constants.GYFS_DDGY_MC)) {
                        this.setGyfs(Constants.GYFS_DDGY_DM);
                    } else if (StringUtils.equals(currentCell.toString(), Constants.GYFS_GTGY_MC)) {
                        this.setGyfs(Constants.GYFS_GTGY_DM);
                    } else if (StringUtils.equals(currentCell.toString(), Constants.GYFS_AFGY_MC)) {
                        this.setGyfs(Constants.GYFS_AFGY_DM);
                    } else if (StringUtils.equals(currentCell.toString(), Constants.GYFS_QTGY_MC)) {
                        this.setGyfs(Constants.GYFS_QTGY_DM);
                    }
                }
                gyfsFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getSffbcz()) && !sffbczFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setSffbcz(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                sffbczFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getBdcdyh()) && !bdcdyhFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setBdcdyh(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                bdcdyhFlag = true;
                continue;
            }
            if (this.getJyjg() == null && !jyjgFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setJyjg(Double.parseDouble(currentCell.toString()));
                }
                jyjgFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getBdbzzqse()) && !bdbzzqseFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setBdbzzqse(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                bdbzzqseFlag = true;
                continue;
            }
            if (this.getZwlxksrq() == null && !zwlxksrqFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdf.parse(currentCell.toString());
                        this.setZwlxksrq(date);
                    } catch (ParseException e) {
                        logger.error("OntBdcXm.OntBdcXm",e);
                    }
                }
                zwlxksrqFlag = true;
                continue;
            }
            if (this.getZwlxjsrq() == null && !zwlxjsrqFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdf.parse(currentCell.toString());
                        this.setZwlxjsrq(date);
                    } catch (ParseException e) {
                        logger.error("OntBdcXm.OntBdcXm",e);
                    }
                }
                zwlxjsrqFlag = true;
                continue;
            }
            if (this.getPgjz() == null && !pgjzFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setPgjz(Double.parseDouble(currentCell.toString()));
                }
                pgjzFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getDyfw()) && !dyfwFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setDyfw(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                dyfwFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getDyfs()) && !dyfsFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    if (StringUtils.equals(currentCell.toString(), "一般抵押")) {
                        this.setDyfs(Constants.DYFS_YBDY);
                    } else if (StringUtils.equals(currentCell.toString(), "最高额抵押")) {
                        this.setDyfs(Constants.DYFS_ZGEDY);
                    }
                }
                dyfsFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getDkfs()) && !dkfsFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setDkfs(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                dkfsFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getYbdcqzh()) && !ybdcqzhFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setYbdcqzh(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                ybdcqzhFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getProid()) && !proidFlag) {
                if (StringUtils.isNotBlank(proid)) {
                    this.setProid(proid);
                }
                proidFlag = true;
            }
        }
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getSjh() {
        return sjh;
    }

    public void setSjh(String sjh) {
        this.sjh = sjh;
    }

    public String getSqdjlx() {
        return sqdjlx;
    }

    public void setSqdjlx(String sqdjlx) {
        this.sqdjlx = sqdjlx;
    }

    public String getQllx() {
        return qllx;
    }

    public void setQllx(String qllx) {
        this.qllx = qllx;
    }

    public String getDjlx() {
        return djlx;
    }

    public void setDjlx(String djlx) {
        this.djlx = djlx;
    }

    public String getGyfs() {
        return gyfs;
    }

    public void setGyfs(String gyfs) {
        this.gyfs = gyfs;
    }

    public String getSffbcz() {
        return sffbcz;
    }

    public void setSffbcz(String sffbcz) {
        this.sffbcz = sffbcz;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public Double getJyjg() {
        return jyjg;
    }

    public void setJyjg(Double jyjg) {
        this.jyjg = jyjg;
    }

    public String getBdbzzqse() {
        return bdbzzqse;
    }

    public void setBdbzzqse(String bdbzzqse) {
        this.bdbzzqse = bdbzzqse;
    }

    public Date getZwlxksrq() {
        return zwlxksrq;
    }

    public void setZwlxksrq(Date zwlxksrq) {
        this.zwlxksrq = zwlxksrq;
    }

    public Date getZwlxjsrq() {
        return zwlxjsrq;
    }

    public void setZwlxjsrq(Date zwlxjsrq) {
        this.zwlxjsrq = zwlxjsrq;
    }

    public Double getPgjz() {
        return pgjz;
    }

    public void setPgjz(Double pgjz) {
        this.pgjz = pgjz;
    }

    public String getDyfw() {
        return dyfw;
    }

    public void setDyfw(String dyfw) {
        this.dyfw = dyfw;
    }

    public String getDyfs() {
        return dyfs;
    }

    public void setDyfs(String dyfs) {
        this.dyfs = dyfs;
    }

    public String getDkfs() {
        return dkfs;
    }

    public void setDkfs(String dkfs) {
        this.dkfs = dkfs;
    }

    public String getYbdcqzh() {
        return ybdcqzh;
    }

    public void setYbdcqzh(String ybdcqzh) {
        this.ybdcqzh = ybdcqzh;
    }

    @Override
    public String toString() {
        return "OntBdcXm{" +
                "proid='" + proid + '\'' +
                ", sjh='" + sjh + '\'' +
                ", sqdjlx='" + sqdjlx + '\'' +
                ", qllx='" + qllx + '\'' +
                ", djlx='" + djlx + '\'' +
                ", gyfs='" + gyfs + '\'' +
                ", sffbcz='" + sffbcz + '\'' +
                ", bdcdyh='" + bdcdyh + '\'' +
                ", jyjg=" + jyjg +
                ", bdbzzqse='" + bdbzzqse + '\'' +
                ", zwlxksrq=" + zwlxksrq +
                ", zwlxjsrq=" + zwlxjsrq +
                ", pgjz=" + pgjz +
                ", dyfw='" + dyfw + '\'' +
                ", dyfs='" + dyfs + '\'' +
                ", dkfs='" + dkfs + '\'' +
                ", ybdcqzh='" + ybdcqzh + '\'' +
                '}';
    }

    public static OntBdcXm initOntBdcXm(Row row, int lineNum, String proid, String sjh) {
        return new OntBdcXm(row, lineNum, proid, sjh);
    }
}
