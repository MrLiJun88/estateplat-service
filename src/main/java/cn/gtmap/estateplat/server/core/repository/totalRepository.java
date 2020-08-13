package cn.gtmap.estateplat.server.core.repository;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repository;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:lisongtao@gtmap.cn">lisongtao</a>
 * @version 1.0  2017/12/7.
 * @description
 */
public class totalRepository extends Repository {
    @Autowired
    HttpServletRequest request;

    @Override
    public <T> Page<T> selectPaging(String statement, Object parameter, Pageable pageable) {
        Map map=request.getParameterMap();
        Map paramMap=new HashMap();
        if(parameter!=null){
            paramMap=JSONObject.parseObject(JSONObject.toJSONString(parameter), Map.class);
        }
        if(map!=null && map.containsKey("loadTotal")){
            String loadTotal="";
            if(map.get("loadTotal").getClass().isArray()){
                loadTotal=((String[])map.get("loadTotal"))[0];
            }else{
                loadTotal=map.get("loadTotal").toString();
            }
            paramMap.put("loadTotal", StringUtils.isNotBlank(loadTotal)? BooleanUtils.toBoolean(loadTotal):false );
        }
        return super.selectPaging(statement,paramMap,pageable);
    }
}
