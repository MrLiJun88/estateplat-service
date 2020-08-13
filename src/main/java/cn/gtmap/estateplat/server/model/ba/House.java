package cn.gtmap.estateplat.server.model.ba;

import com.alibaba.fastjson.annotation.JSONField;

public class House {
    private String EstateUnitNo	;//不动产单元号
    private String HouseCode	;//房屋代码

    @JSONField(name = "EstateUnitNo")
    public String getEstateUnitNo() {
        return EstateUnitNo;
    }

    public void setEstateUnitNo(String estateUnitNo) {
        EstateUnitNo = estateUnitNo;
    }

    @JSONField(name = "HouseCode")
    public String getHouseCode() {
        return HouseCode;
    }

    public void setHouseCode(String houseCode) {
        HouseCode = houseCode;
    }
}
