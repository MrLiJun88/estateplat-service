package cn.gtmap.estateplat.server.core.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @description 外网收件的不动产权利人类
 */
public class OntQlr {
    private String proid;
    private String sjh;
    private String bdcdyh;
    private String zlc;
    private String qlrmc;
    private String gybl;
    private String qlrsfzjzl;
    private String qlrzjh;
    private String qlrtxdz;
    private String qlrlxdh;
    private String qlrlx;


    public OntQlr() {
    }

    /**
     * @param row,lineNum,proid,bdcdyh
     * @return OntQlr
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据Excel的内容初始化OntQlr, 每一个属性初始化一遍
     */
    private OntQlr(Row row, int lineNum, String qlrlx, String proid,String sjh) {
        boolean proidFlag = false;
        boolean sjhFlag = false;
        boolean bdcdyhFlag = false;
        boolean zlcFlag = false;
        boolean qlrmcFlag = false;
        boolean gyblFlag = false;
        boolean qlrsfzjzlFlag = false;
        boolean qlrzjhFlag = false;
        boolean qlrtxdzFlag = false;
        boolean qlrlxdhFlag = false;
        boolean qlrlxFlag = false;
        for (int i = 0; i < lineNum; i++) {
            Cell currentCell = row.getCell(i);
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
            if (StringUtils.isBlank(this.getBdcdyh()) && !bdcdyhFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setBdcdyh(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                bdcdyhFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getZlc()) && !zlcFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setZlc(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                zlcFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getQlrmc()) && !qlrmcFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setQlrmc(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                qlrmcFlag = true;
                continue;
            }
            if (!StringUtils.equals(qlrlx, "ywr")&&StringUtils.isBlank(this.getGybl()) && !gyblFlag) {
                if (currentCell != null) {
                    this.setGybl(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                gyblFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getQlrsfzjzl()) && !qlrsfzjzlFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setQlrsfzjzl(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                qlrsfzjzlFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getQlrzjh()) && !qlrzjhFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setQlrzjh(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                qlrzjhFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getQlrtxdz()) && !qlrtxdzFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setQlrtxdz(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                qlrtxdzFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getQlrlxdh()) && !qlrlxdhFlag) {
                if (currentCell != null && StringUtils.isNotBlank(currentCell.toString())) {
                    this.setQlrlxdh(StringUtils.deleteWhitespace(currentCell.toString()));
                }
                qlrlxdhFlag = true;
                continue;
            }
            if (StringUtils.isBlank(this.getQlrlx()) && !qlrlxFlag) {
                this.setQlrlx(qlrlx);
                qlrlxFlag = true;
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

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getZlc() {
        return zlc;
    }

    public void setZlc(String zlc) {
        this.zlc = zlc;
    }

    public String getQlrmc() {
        return qlrmc;
    }

    public void setQlrmc(String qlrmc) {
        this.qlrmc = qlrmc;
    }

    public String getGybl() {
        return gybl;
    }

    public void setGybl(String gybl) {
        this.gybl = gybl;
    }

    public String getQlrsfzjzl() {
        return qlrsfzjzl;
    }

    public void setQlrsfzjzl(String qlrsfzjzl) {
        this.qlrsfzjzl = qlrsfzjzl;
    }

    public String getQlrzjh() {
        return qlrzjh;
    }

    public void setQlrzjh(String qlrzjh) {
        this.qlrzjh = qlrzjh;
    }

    public String getQlrtxdz() {
        return qlrtxdz;
    }

    public void setQlrtxdz(String qlrtxdz) {
        this.qlrtxdz = qlrtxdz;
    }

    public String getQlrlxdh() {
        return qlrlxdh;
    }

    public void setQlrlxdh(String qlrlxdh) {
        this.qlrlxdh = qlrlxdh;
    }

    public String getQlrlx() {
        return qlrlx;
    }

    public void setQlrlx(String qlrlx) {
        this.qlrlx = qlrlx;
    }

    @Override
    public String toString() {
        return "OntQlr{" +
                "proid='" + proid + '\'' +
                ", sjh='" + sjh + '\'' +
                ", bdcdyh='" + bdcdyh + '\'' +
                ", zlc='" + zlc + '\'' +
                ", qlrmc='" + qlrmc + '\'' +
                ", gybl='" + gybl + '\'' +
                ", qlrsfzjzl='" + qlrsfzjzl + '\'' +
                ", qlrzjh='" + qlrzjh + '\'' +
                ", qlrtxdz='" + qlrtxdz + '\'' +
                ", qlrlxdh='" + qlrlxdh + '\'' +
                ", qlrlx='" + qlrlx + '\'' +
                '}';
    }

    public static OntQlr initOntQlr(Row row, int lineNum, String qlrlx, String proid, String sjh) {
        return new OntQlr(row, lineNum, qlrlx, proid,sjh);
    }
}
