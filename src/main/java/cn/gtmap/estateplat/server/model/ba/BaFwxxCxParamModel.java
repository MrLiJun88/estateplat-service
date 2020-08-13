package cn.gtmap.estateplat.server.model.ba;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class BaFwxxCxParamModel {
    private String SN	;//请求用户识别码
    private List<House> houseList=new ArrayList<House>();

    @JSONField(name = "SN")
    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    @JSONField(name = "HouseList")
    public List<House> getHouseList() {
        return houseList;
    }

    public void setHouseList(List<House> houseList) {
        this.houseList = houseList;
    }
}
