package cn.gtmap.estateplat.server.service.impl;

import com.gtis.common.Page;
import com.gtis.plat.service.SplitDataService;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * cms获取数据源
 */
@SuppressWarnings("unchecked")
public class SplitDataServiceImpl extends SqlSessionDaoSupport implements SplitDataService, Serializable {
    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public <T> List<T> query(String statementName, Object param) {
        return this.getSqlSession().selectList(statementName, param);
    }

    @Override
    public Page query(String statementName, Object param, int start, int size) {
        RowBounds rowBounds = new RowBounds(start, size);
        List rows = this.getSqlSession().selectList(statementName, param, rowBounds);
        if (rows == null)
            rows = new ArrayList();
        Page page = new Page();
        page.setStart(start);
        page.setSize(size);

        page.setTotalCount(getCount(statementName, param));
        if (!page.isEmpty()) {
            page.setItems(rows);
        }
        return page;
    }

    @Override
    public int getCount(String statementName, Object param) {
        List rows = this.getSqlSession().selectList(statementName, param);
        if (rows == null)
            rows = new ArrayList();

        return rows.size();
    }
}
