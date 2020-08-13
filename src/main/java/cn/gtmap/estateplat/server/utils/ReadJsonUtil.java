package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.server.model.AutoSignWfNodeName;
import cn.gtmap.estateplat.server.model.sw.DbtsswModel;
import cn.gtmap.estateplat.server.model.TradingStatusAndDjzx;
import cn.gtmap.estateplat.server.model.login.UserComparison;
import cn.gtmap.estateplat.server.model.trade.DjzxValidate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/3/29
 * @description 读取json文件
 */
public class ReadJsonUtil {

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取登记子项验证信息
     */
    public static List<DjzxValidate> getDjzxValidate() {
        String jsonStr = ReadJsonFileUtil.readJsonFile(AppConfig.getEgovHome() + "/conf/server/djzxValidate.json");
        List<DjzxValidate> djzxValidateList = JSONArray.parseArray(jsonStr, DjzxValidate.class);
        return djzxValidateList;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @description 获取登记子项
     */
    public static List<TradingStatusAndDjzx> getDjzxAndTradingStatus() {
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/bdcTradingStatusDjzx.json");
        List<TradingStatusAndDjzx> tradingStatusAndDjzxList = JSONArray.parseArray(jsonStr, TradingStatusAndDjzx.class);
        return tradingStatusAndDjzxList;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取用户对照关系
     */
    public static List<UserComparison> getUserComparison() {
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/userComparison.json");
        List<UserComparison> userComparisonList = JSONArray.parseArray(jsonStr, UserComparison.class);
        return userComparisonList;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取用户对照关系
     */
    public static DbtsswModel getdbtsswPz() {
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/pz/dbtssw.json");
        DbtsswModel dbtsswModel = JSON.parseObject(jsonStr, DbtsswModel.class);
        return dbtsswModel;
    }

    public static List<AutoSignWfNodeName> getWorkFlowNodeName() {
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/autoSignWorkFlowNodeName.json");
        List<AutoSignWfNodeName> autoSignWfNodeNames = JSONArray.parseArray(jsonStr, AutoSignWfNodeName.class);
        return autoSignWfNodeNames;
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @description 更加sqlx和djlx获取督查随机概率
     */
    public static Integer getDcsjzs(String sqlx, String djlx) {
        Integer gl = null;
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/dcglpz.json");
        JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonStr);
        if (jsonObject.containsKey("sqlxdm")) {
            JSONObject sqlxJson = (JSONObject) jsonObject.get("sqlxdm");
            if (sqlxJson.containsKey(sqlx)) {
                JSONObject glJson = (JSONObject) sqlxJson.get(sqlx);
                if (glJson.containsKey("gl") && StringUtils.isNotBlank((CharSequence) glJson.get("gl"))) {
                    gl = Integer.parseInt(String.valueOf(glJson.get("gl")));
                }
            }
        }
        if (gl == null) {
            if (jsonObject.containsKey("djlxdm")) {
                JSONObject djlxJson = (JSONObject) jsonObject.get("djlxdm");
                if (djlxJson.containsKey(djlx)) {
                    JSONObject glJson = (JSONObject) djlxJson.get(djlx);
                    if (glJson.containsKey("gl") && StringUtils.isNotBlank((CharSequence) glJson.get("gl"))) {
                        gl = Integer.parseInt(String.valueOf(glJson.get("gl")));
                    }
                }
            }
        }
        return gl;
    }
}
